/*
 * Copyright 2012-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.znet.logging.demo.metrics.influxdb;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Influxdb Data.
 *
 * @author Dave Syer
 * @since 1.3.0
 */
public class InfluxDBData {

	private InfluxDBName name;

	private Long timestamp;

	private Number value;

	protected InfluxDBData() {
		this.name = new InfluxDBName();
	}

	public InfluxDBData(String metric, Number value) {
		this(metric, value, System.currentTimeMillis());
	}

	public InfluxDBData(String metric, Number value, Long timestamp) {
		this(new InfluxDBName(metric), value, timestamp);
	}

	public InfluxDBData(InfluxDBName name, Number value, Long timestamp) {
		this.name = name;
		this.value = value;
		this.timestamp = timestamp;
	}

	public String getMetric() {
		return this.name.getMetric();
	}

	public void setMetric(String metric) {
		this.name.setMetric(metric);
	}

	public Long getTimestamp() {
		return this.timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public Number getValue() {
		return this.value;
	}

	public void setValue(Number value) {
		this.value = value;
	}

	public Map<String, String> getTags() {
		return this.name.getTags();
	}

	public void setTags(Map<String, String> tags) {
		this.name.setTags(tags);
	}

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(name.toString().replace(".", "_"));
        stringBuilder.append(" ");
		stringBuilder.append("value=").append(value);
		if (value instanceof Double) {
		}
		if (value instanceof Integer || value instanceof Long || value instanceof Short) {
			stringBuilder.append("i");
		}
		stringBuilder.append(" ").append(TimeUnit.NANOSECONDS.convert(timestamp, TimeUnit.MILLISECONDS));
        return stringBuilder.toString();
    }
}
