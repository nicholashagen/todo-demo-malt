#!/bin/sh

mvn clean install
cp target/zipkin-service.jar docker/
cd docker
docker build -t logging-demo/zipkin-service:latest .
cd ..
