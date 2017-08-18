package com.znet.logging.demo.metrics.influxdb;

import java.lang.management.CompilationMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.ExportMetricReader;
import org.springframework.boot.actuate.autoconfigure.ExportMetricWriter;
import org.springframework.boot.actuate.metrics.reader.MetricReader;
import org.springframework.boot.actuate.metrics.reader.MetricRegistryMetricReader;
import org.springframework.boot.actuate.metrics.writer.GaugeWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.MetricSet;
import com.codahale.metrics.jvm.ClassLoadingGaugeSet;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.sun.management.OperatingSystemMXBean;
import com.sun.management.ThreadMXBean;

@Configuration
public class InfluxMetricsConfiguration {

	@Bean
	@ExportMetricReader
	public MetricReader createMetricReader(MetricRegistry registry) {
		registry.registerAll(new JvmMetrics());

		return new MetricRegistryMetricReader(registry);
	}

	@Bean
	@ExportMetricWriter
	public GaugeWriter createMetricWriter(
			@Value("${spring.application.name}") String app,
			@Value("${metrics.influxdb.url}") String url) {

		InfluxDBGaugeWriter writer = new InfluxDBGaugeWriter(5000, 5000);
		writer.setUrl(url);

		Map<String, String> tags = new HashMap<>();
		tags.put("app", app);

		DefaultInfluxDBNamingStrategy namingStrategy = new DefaultInfluxDBNamingStrategy();
		namingStrategy.setTags(tags);
		writer.setNamingStrategy(namingStrategy);

		writer.setRestTemplate(new RestTemplate());
		return writer;
	}

	public static class JvmCpuGauge implements Gauge<Float> {
		private RuntimeMXBean mRuntimeBean;
		private OperatingSystemMXBean mOSBean;
		private long mPrevUpTime;
		private long mPrevProcessCpuTime;

		public JvmCpuGauge() {
			mRuntimeBean = ManagementFactory.getRuntimeMXBean();
			mOSBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

		}

		/**
		 * Calculate the current cpu usage based on uptime and processor cpu time.
		 * <p>
		 * <b>Note:</b> This method is directly copied from the Jconsole <a href = "http://grepcode.com/file/repository.grepcode.com/java/root/jdk/openjdk/8u40-b25/sun/tools/jconsole/SummaryTab.java#351"
		 * >updateCPUInfo</a> method
		 * </p>
		 * @param upTime the amount of time this JVM has been up for in ms
		 * @param processCpuTime the amount of cpu time used for this jvm in ns
		 * @param nCPUs the number of cpus this JVM is using
		 * @return the calculated value for current cpu
		 */
		private float updateCPUInfo(long upTime, long processCpuTime, int nCPUs) {
			float cpuUsage = 0;
			if (mPrevUpTime > 0L && upTime > mPrevUpTime) {
				// elapsedCpu is in ns and elapsedTime is in ms.
				long elapsedCpu = processCpuTime - mPrevProcessCpuTime;
				long elapsedTime = upTime - mPrevUpTime;
				// cpuUsage could go higher than 100% because elapsedTime
				// and elapsedCpu are not fetched simultaneously. Limit to
				// 99% to avoid Plotter showing a scale from 0% to 200%.
				cpuUsage =
						Math.min(99F,
								elapsedCpu / (elapsedTime * 10000F * nCPUs));
			}
			this.mPrevUpTime = upTime;
			this.mPrevProcessCpuTime = processCpuTime;
			return cpuUsage;
		}

		/**
		 * Update the current stats used to calculate cpu usage. Such as current uptime and cpu usage. Then
		 * Calculates a current CPU%
		 * @return percent of cpu being used at the time the method is called.
		 */
		private float updateAndGetCurrentCPU() {
			long uptime = mRuntimeBean.getUptime();
			return updateCPUInfo(uptime, mOSBean.getProcessCpuTime(), mOSBean.getAvailableProcessors());
		}


		/**
		 * Returns the current CPU usuage as a percent.
		 */
		@Override
		public Float getValue() {
			return updateAndGetCurrentCPU();
		}

	}

	public static class JvmMetrics implements MetricSet {

		@Override
		public Map<String, Metric> getMetrics() {
			Map<String,Metric> metrics = new HashMap<>();

			metrics.putAll(prefixMetrics("jvm.gc",getGcMetrics()));
			metrics.putAll(prefixMetrics("jvm.memory",new MemoryUsageGaugeSet().getMetrics()));
			metrics.putAll(prefixMetrics("jvm.classes", new ClassLoadingGaugeSet().getMetrics()));
			metrics.putAll(getAdditionalJVMMetrics());

			return Collections.unmodifiableMap(metrics);
		}

		/**
		 * Alot of the gc metrics end with {@code .time} which translates into a field in
		 * influx. time is reserved though, so this method renames to {@code gc_time}
		 */
		private Map<String,Metric> getGcMetrics() {
			Map<String,Metric> metrics = new GarbageCollectorMetricSet().getMetrics();
			Map<String,Metric> fixedNames = new HashMap<>(metrics.size());

			for(Map.Entry<String, Metric> entry : metrics.entrySet()) {
				String name = entry.getKey();
				if(name.endsWith(".time")) {
					String fieldName = name;
					fieldName = fieldName.replaceFirst("\\.time$", ".gc_time");
					name = fieldName;
				}

				fixedNames.put(name, entry.getValue());
			}

			return fixedNames;
		}

		/**
		 * get an additional set of default metrics that are not part of the standard JVM metric sets that
		 * come with the dropwizard metrics
		 * @return
		 */
		private Map<String,Metric> getAdditionalJVMMetrics() {
			Map<String,Metric> metrics = new HashMap<>();
			final RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
			Gauge<Long> uptime = () -> runtime.getUptime();
			metrics.put("jvm.uptime", uptime);

			final ThreadMXBean threadBean = (ThreadMXBean) ManagementFactory.getThreadMXBean();
			Gauge<Integer> threads = () -> threadBean.getThreadCount();
			metrics.put("jvm.threads.count", threads);

			final CompilationMXBean compileBean = ManagementFactory.getCompilationMXBean();
			Gauge<Long> compileTime = () -> compileBean.getTotalCompilationTime();
			metrics.put("jvm.compileTime", compileTime);

			metrics.put("jvm.cpu", new JvmCpuGauge());

			return metrics;


		}

		/**
		 * prefix a map of metric names with the given MetricName
		 * @param prefix the MetricName prefix to apply to all metrics
		 * @param metrics the metrics to apply a prefix to
		 * @return a new map of prefixed metric names
		 */
		private Map<String,Metric> prefixMetrics (String prefix, Map<String,Metric> metrics) {
			Map<String,Metric> prefixedMetrics = new HashMap<>();
			metrics.forEach( (k,v) -> prefixedMetrics.put(prefix + '.' + k, v));
			return prefixedMetrics;
		}




	}

}
