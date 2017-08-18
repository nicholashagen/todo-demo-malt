#!/bin/sh

echo "Running logstash..."
mkdir -p /app/logstash/data
mkdir -p /app/logstash/logs
/app/logstash/bin/logstash --path.data /app/logstash/data -f /app/logstash.conf &
sleep 60

echo "Running Java..."
java -Xmx1g -Dapp.name=auth-api-service -jar auth-api-service.jar
