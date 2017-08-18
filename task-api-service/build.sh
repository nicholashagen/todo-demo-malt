#!/bin/sh

mvn clean install
cp target/task-api-service.jar docker/
cd docker
docker build -t logging-demo/task-api-service:latest .
rm task-api-service.jar
cd ..
