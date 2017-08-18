/*
 * Copyright 2012-2016 the original author or authors.
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

import org.springframework.boot.actuate.metrics.Metric;
import org.springframework.boot.actuate.metrics.writer.GaugeWriter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * A {@link GaugeWriter} for the Open TSDB database (version 2.0), writing metrics to the
 * HTTP endpoint provided by the server. Data are buffered according to the
 * {@link #setBufferSize(int) bufferSize} property, and only flushed automatically when
 * the buffer size is reached. Users should either manually {@link #flush()} after writing
 * a batch of data if that makes sense, or consider adding a {@link Scheduled Scheduled}
 * task to flush periodically.
 *
 * @author Dave Syer
 * @author Thomas Badie
 * @since 1.3.0
 */
public class InfluxDBGaugeWriter implements GaugeWriter {

	private static final int DEFAULT_CONNECT_TIMEOUT = 10000;

	private static final int DEFAULT_READ_TIMEOUT = 30000;

	private RestOperations restTemplate;

	/**
	 * URL for POSTing data. Defaults to http://localhost:4242/api/put.
	 */
	private String url = "http://localhost:8086/write?db=metrics";

	/**
	 * Buffer size to fill before posting data to server.
	 */
	private int bufferSize = 128;

	/**
	 * The media type to use to serialize and accept responses from the server. Defaults
	 * to "text/plain".
	 */
	private MediaType mediaType = MediaType.TEXT_PLAIN;

	private final List<InfluxDBData> buffer = new ArrayList<InfluxDBData>(
			this.bufferSize);

	private InfluxDBNamingStrategy namingStrategy = new DefaultInfluxDBNamingStrategy();

	/**
	 * Creates a new {@code InfluxDBGaugeWriter} with the default connect (10 seconds) and
	 * read (30 seconds) timeouts.
	 */
	public InfluxDBGaugeWriter() {
		this(DEFAULT_CONNECT_TIMEOUT, DEFAULT_READ_TIMEOUT);
	}

	/**
	 * Creates a new {@code InfluxDBGaugeWriter} with the given millisecond
	 * {@code connectTimeout} and {@code readTimeout}.
	 *
	 * @param connectTimeout the connect timeout in milliseconds
	 * @param readTimeout the read timeout in milliseconds
	 */
	public InfluxDBGaugeWriter(int connectTimeout, int readTimeout) {
		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
		requestFactory.setConnectTimeout(connectTimeout);
		requestFactory.setReadTimeout(readTimeout);
		this.restTemplate = new RestTemplate(requestFactory);
	}

	public RestOperations getRestTemplate() {
		return this.restTemplate;
	}

	public void setRestTemplate(RestOperations restTemplate) {
		this.restTemplate = restTemplate;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}

	public void setMediaType(MediaType mediaType) {
		this.mediaType = mediaType;
	}

	public void setNamingStrategy(InfluxDBNamingStrategy namingStrategy) {
		this.namingStrategy = namingStrategy;
	}

	public void set(Metric<?> value) {
		InfluxDBData data = new InfluxDBData(this.namingStrategy.getName(value.getName()),
				value.getValue(), value.getTimestamp().getTime());
		synchronized (this.buffer) {
			this.buffer.add(data);
			if (this.buffer.size() >= this.bufferSize) {
				flush();
			}
		}
	}

	/**
	 * Flush the buffer without waiting for it to fill any further.
	 */
	@SuppressWarnings("rawtypes")
	public void flush() {
		List<InfluxDBData> snapshot = getBufferSnapshot();
		if (snapshot.isEmpty()) {
			return;
		}
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(this.mediaType));
		headers.setContentType(this.mediaType);
        StringBuilder stringBuilder = new StringBuilder();
        for (InfluxDBData data : snapshot) {
            stringBuilder.append(data.toString()).append("\n");
        }
		stringBuilder.deleteCharAt(stringBuilder.lastIndexOf("\n"));
		ResponseEntity<Map> response = this.restTemplate.postForEntity(this.url,
				new HttpEntity<String>(stringBuilder.toString(), headers), Map.class);
		if (!response.getStatusCode().is2xxSuccessful()) {

		}
	}

	private List<InfluxDBData> getBufferSnapshot() {
		synchronized (this.buffer) {
			if (this.buffer.isEmpty()) {
				return Collections.emptyList();
			}
			List<InfluxDBData> snapshot = new ArrayList<InfluxDBData>(this.buffer);
			this.buffer.clear();
			return snapshot;
		}
	}

}
