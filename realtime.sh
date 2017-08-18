#!/bin/sh

kafka-console-consumer --zookeeper localhost:2181 --topic logs_$1 --from-beginning | jq '.["@timestamp"] + " " + .["message"]'
