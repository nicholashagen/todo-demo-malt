#!/bin/sh

echo Killing Logstash...
if [ -e ./logstash/logstash.pid ]
then
	kill -9 `cat ./logstash/logstash.pid`
	rm ./logstash/logstash.pid
fi
