#!/bin/sh

cd metrics-influxdb && mvn clean install && cd ..
cd logstash-sidecar && sh build.sh && cd ..
cd eureka-service && sh build.sh && cd ..
cd zipkin-service && sh build.sh && cd ..
cd auth-api-service && sh build.sh && cd ..
cd friend-api-service && sh build.sh && cd ..
cd task-api-service && sh build.sh && cd ..
cd todo-app && sh build.sh && cd ..
