Useful Queries
==============

# Zipkin

`http.status_code=500`

Find all traces that resulted in a 500

`http.path=/users/john`

Find all traces with the given URI path (not including query params)

# Kibana

`type:hitlog AND NOT application:"eureka-service" AND NOT application:"zipkin-service"`

Pull all hitlogs across all services (except for Eureka and Zipkin)

`application:"auth-api-service"`

Pull all logs including hitlogs and application logs for the given service

`message:"LOGIN FAIL"`

Pull all messages with the given message