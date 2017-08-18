#!/bin/sh

mvn clean install
cp target/eureka-service.jar docker/
cd docker
docker build -t logging-demo/eureka-service:latest .
rm eureka-service.jar
cd ..
