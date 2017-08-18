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

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * OpenTSDB Name.
 *
 * @author Dave Syer
 * @since 1.3.0
 */
public class InfluxDBName {

	private String metric;

	private Map<String, String> tags = new LinkedHashMap<String, String>();

	protected InfluxDBName() {
	}

	public InfluxDBName(String metric) {
		this.metric = metric;
	}

	public String getMetric() {
		return this.metric;
	}

	public void setMetric(String metric) {
		this.metric = metric;
	}

	public Map<String, String> getTags() {
		return this.tags;
	}

	public void setTags(Map<String, String> tags) {
		this.tags.putAll(tags);
	}

	public void tag(String name, String value) {
		this.tags.put(name, value);
	}

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(metric);
        if (!getTags().isEmpty()) {
            stringBuilder.append(",");
        }
        for (Map.Entry<String, String> entry : getTags().entrySet()) {
            stringBuilder.append(entry.getKey()).append("=").append(entry.getValue().replace(" ", "\\ ")).append(",");
        }
        if (!getTags().isEmpty()) {
            stringBuilder.deleteCharAt(stringBuilder.lastIndexOf(","));
        }

        return stringBuilder.toString();
   }
}
