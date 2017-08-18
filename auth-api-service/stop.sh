#!/bin/sh

echo "Stopping APIs"
if [ -e ./logs/app.pid ]
then
	kill -9 `cat ./logs/app.pid`
	rm ./logs/app.pid
fi

echo Killing Logstash...
if [ -e ./logstash/logstash.pid ]
then
	kill -9 `cat ./logstash/logstash.pid`
	rm ./logstash/logstash.pid
fi
