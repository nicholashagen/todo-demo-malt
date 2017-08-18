#!/bin/sh

echo Starting Logstash...
mkdir -p `pwd`/logstash
mkdir -p `pwd`/logs
logstash --path.data `pwd`/logstash/ -f `pwd`/logstash.conf > `pwd`/logs/logstash.log 2>&1 &
sleep 2
ps -ef | grep -v grep | grep `pwd`/logstash | awk '{ print $2 }' > `pwd`/logstash/logstash.pid
