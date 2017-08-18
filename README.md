TODO Demo
=========

This repository contains a simple example of APIs for a TODO app that 
uses microservices to provide clean separation.  The main premise is to
highlight the MALT stack (metrics, alerting, logging, and tracing) along
with service discovery.  These particular examples use the following 
frameworks.

- APIs : Spring MVC
- Discovery : Eureka
- Metrics : Dropwizard Metrics, Telegraf, InfluxDB, Grafana
- Alerting : ElastAlert
- Logging : Logback, Logstash, Elasticsearch, Kibana
- Tracing : Zipkin
- Service Broker : Kafka

The `todo-app` is the central API for the service.  The app uses the
Spring RestTemplate with the Netflix Ribbon API to pull data from the
other available APIs via Service Discovery: `auth-api-service` for 
user information, `task-api-service` for task management, and 
`friend-api-service` to manage friendships.  Each of these services use
Spring Boot with Spring Cloud.  The APIs use MySQL to fetch the data.
Alongside each application is a Logstash collector responsible for 
parsing the necessary logs and shipping to Kafka.

The `eureka-service` is a Spring Boot application that sets up Eureka
for service discovery.  Each application registers itself with Eureka.

The `zipkin-service` is a Spring Boot application that sets up a Zipkin
service for managing tracing between calls.

The `alerting` module sets up ElastAlert rules that query Elasticsearch
for logs to determine if alerts need to be sent out.

The `docker` module provides a single docker compose configuration for
loading all available applications.  Warning that running this image
requires lots of memory and CPU available to the host.
