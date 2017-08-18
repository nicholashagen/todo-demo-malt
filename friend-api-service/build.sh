#!/bin/sh

mvn clean install
cp target/friend-api-service.jar docker/
cd docker
docker build -t logging-demo/friend-api-service:latest .
rm friend-api-service.jar
cd ..
