FROM openjdk:8

RUN mkdir -p /app && cd /app && \
	wget -O logstash.zip https://artifacts.elastic.co/downloads/logstash/logstash-5.5.1.zip && \
	unzip logstash.zip && \
	chmod +x logstash-5.5.1/bin/logstash && \
	mv logstash-5.5.1 logstash

ADD run.sh /app/run.sh
ADD logstash.conf /app/logstash.conf
WORKDIR /app

CMD ["/bin/sh", "/app/run.sh"]
