#!/bin/sh

mvn clean install
cp target/todo-app.jar docker/
cd docker
docker build -t logging-demo/todo-app:latest .
rm todo-app.jar
cd ..
