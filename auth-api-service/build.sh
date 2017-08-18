#!/bin/sh

mvn clean install
cp target/auth-api-service.jar docker/
cd docker
docker build -t logging-demo/auth-api-service:latest .
rm auth-api-service.jar
cd ..
