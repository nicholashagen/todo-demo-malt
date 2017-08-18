#!/bin/sh

echo "Running logstash..."
mkdir -p /app/logstash/data
mkdir -p /app/logstash/logs
/app/logstash/bin/logstash --path.data /app/logstash/data -f /app/logstash.conf &
sleep 60

echo "Running $APP_NAME Java..."
java -Xmx1g -Dapp.name=$APP_NAME -jar $APP_NAME.jar
