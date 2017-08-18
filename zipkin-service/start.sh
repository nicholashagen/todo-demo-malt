#!/bin/sh

PWD=`pwd`
APP_NAME=zipkin-service
APP_PORT=9411
JMX_PORT=9412
LOG_PORT=9413

echo "Starting Logstash..."
mkdir -p $PWD/logs
mkdir -p $PWD/logstash
LOG_PORT=$LOG_PORT APP_DIR=$PWD APP_NAME=$APP_NAME logstash --path.data $PWD/logstash/ -f $PWD/logstash.conf > $PWD/logs/logstash.log 2>&1 &
sleep 2
ps -ef | grep -v grep | grep $PWD/logstash | awk '{ print $2 }' > $PWD/logstash/logstash.pid

# api settings
JAR="target/${APP_NAME}.jar"
JAVA_OPTS="-Dapp.name=${APP_NAME}"
JAVA_OPTS="${JAVA_OPTS} -Djava.net.preferIPv4Stack=true"
JAVA_OPTS="${JAVA_OPTS} -Dcom.sun.management.jmxremote"
JAVA_OPTS="${JAVA_OPTS} -Dcom.sun.management.jmxremote.port=${JMX_PORT}"
JAVA_OPTS="${JAVA_OPTS} -Dcom.sun.management.jmxremote.local.only=false"
JAVA_OPTS="${JAVA_OPTS} -Dcom.sun.management.jmxremote.authenticate=false"
JAVA_OPTS="${JAVA_OPTS} -Dcom.sun.management.jmxremote.ssl=false"
JAVA_OPTS="${JAVA_OPTS} -Dserver.port=${APP_PORT}"
JAVA_OPTS="${JAVA_OPTS} -Dlogstash.port=${LOG_PORT}"
JAVA_OPTS="${JAVA_OPTS} -Dserver.tomcat.accesslog.directory=$PWD/logs"
JAVA_OPTS="${JAVA_OPTS} -Dserver.tomcat.accesslog.enabled=true"
JAVA_OPTS="${JAVA_OPTS} -Dserver.tomcat.accesslog.buffered=false"
JAVA_OPTS="${JAVA_OPTS} -Dserver.tomcat.accesslog.request-attributes-enabled=true"

sleep 30
echo "Starting up ${APP_NAME}..."
java $JAVA_OPTS -jar "$JAR" > $PWD/logs/app.log 2>&1 &
sleep 2
ps -ef | grep -v grep | grep $APP_NAME.jar | awk '{ print $2 }' > $PWD/logs/app.pid
