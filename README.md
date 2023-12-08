This is a draft project to play with observability && monitoring configs.

### Theory

Observability is the ability to observe the internal state of a running system from the outside. It consists of the 
three pillars - logging, metrics and traces.

For metrics and traces, Spring Boot uses Actuator. For logging ...

#### Spring Boot Actuator | metrics and traces

Actuator brings production-ready features to our application: monitoring our app, gathering metrics, and understanding 
traffic or the state of our database becomes trivial with this dependency.

We need to add the spring-boot-actuator dependency to our package manager to enable the Spring Boot Actuator.

The main benefit of this library is that we can get production-grade tools without actually having to implement these 
features ourselves.

The actuator mainly exposes operational information about the running application — health, metrics, info, dump, env, etc.
It uses HTTP endpoints or JMX beans to enable us to interact with it.

##### Actuator 1.x vs 2.x

Spring Boot Actuator has been available since April 2014, together with the first Spring Boot release. With the release 
of Spring Boot 2, Actuator has been redesigned, and new exciting endpoints were added, this makes a breaking changes: 

* 2.x Actuator become technology-agnostic by using [Micrometer Observation](https://micrometer.io/docs/observation),
Actuator 1.x is tied to MVC and, therefore, to the Servlet API. In 2.x Actuator the in-house metrics were replaced with 
Micrometer support, so we can expect breaking changes. If our application were using metric services such as GaugeService
or CounterService, they would no longer be available. We will interact with Micrometer directly.

* 2.x Actuator security model is merged with the application one, so the security can be configured in one 
place with main app.

* In 2.x, Actuator defines its model as pluggable and extensible without relying on MVC for this. Hence, with this new 
model, we can take advantage of MVC and WebFlux as an underlying web technology. Forthcoming technologies could be added
by implementing the right adapters.

We will use 2.x Actuator.

##### 'Discovery' endpoint and hypermedia

All Actuator endpoints are now placed under the `/actuator` path by default. We can tweak this path using the new property
`management.endpoints.web.base-path=/car-rental-api-actuator-discovery`:

![](img/actuator-discovery-endpoint-response.png)

In order to access all the actuator endpoints using HTTP, we need to both enable and expose them. By default, all endpoints
but `/shutdown` are enabled. Only the `/health` and `/info` endpoints are exposed by default. We need to add the following
configuration to expose all endpoints:
`management.endpoints.web.exposure.include=*`

or define a list of exposed endpoints:
`management.endpoints.web.exposure.include=health, info, beans, metrics`

All the possible endpoints are listed here: [actuator.endpoints](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html#actuator.endpoints)

Example of requesting `/beans` endpoint, that we just have enabled:
![](img/actuator-beans-endpoint-response.png)


##### 'Health' indicators

##### Metrics | Micrometer
Micrometer is now part of the Actuator’s dependencies, so we should be good to go as long as the Actuator dependency is 
in the classpath.
We can get a metrics response from the `/metrics` endpoint:


##### Tracing

##### Loggers

##### Custom endpoints








#### Logging


*Grafana helps to monitor and visualize the metrics by building different dashboards and can use different kinds of 
charts to visualize the metrics
* Micrometer as a metrics collector for Spring Boot
* Prometheus as a metrics aggregation tool
* Loki is a log aggregation tool that receives the logs from our application and indexes the logs to be visualized using Grafana
* Tempo is used as a distributed tracing tool, which can track requests that span across different systems
* micrometer-tracing - distributed tracing capabilities, Spring Cloud Sleuth used in Spring earlier


https://docs.spring.io/spring-boot/docs/2.0.x/actuator-api/html/
https://www.baeldung.com/micrometer
https://spring.io/blog/2018/03/16/micrometer-spring-boot-2-s-new-application-metrics-collector

