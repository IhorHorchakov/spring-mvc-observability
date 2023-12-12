This is a draft project to play with observability && monitoring configs.

### Prerequisites

* `Spring MVC` is used as core to build modules
* `Spring Boot Actuator` is a metric collector/registry
* `Micrometer` is a facade for monitoring systems and metric aggregation tools (Prometheus)
* `Prometheus` as a metrics aggregation tool
* `Loki` is a log aggregation tool that receives the logs from our application and indexes the logs to be visualized using Grafana
* `Grafana` helps to visualize the metrics by building different dashboards. References to Prometheus to 
* `Tempo` is used as a distributed tracing tool, which can track requests that span across different systems
* `Docker` used for containerized builds

### Theory

Observability is the ability to observe the internal state of a running system from the outside. It consists of the 
three pillars - **logging, metrics and traces**.

For metrics and traces Spring uses `Spring Boot Actuator`. For logging ...

#### Metrics and Traces | Spring Boot Actuator

Actuator brings production-ready features to our application: monitoring our app, gathering metrics, and understanding 
traffic or the state of our database becomes trivial with this dependency.

The main benefit of this library is that we can get production-grade tools without actually having to implement these 
features ourselves.

The actuator mainly exposes operational information about the running application — health, metrics, info, dump, env, etc.
It uses HTTP endpoints or JMX beans to enable us to interact with it.

##### Actuator: 1.x vs 2.x

Spring Boot Actuator has been available since April 2014, together with the first Spring Boot release. With the release 
of Spring Boot 2, Actuator has been redesigned, and new exciting endpoints were added, this makes a breaking changes: 

* 2.x Actuator become technology-agnostic. Actuator 1.x is tied to MVC and, therefore, to the Servlet API. In 2.x Actuator
the in-house metrics were replaced with Micrometer support, so we can expect breaking changes. If our application were 
using metric services such as GaugeService or CounterService, they would no longer be available. We will interact with 
Micrometer directly.

* 2.x Actuator security model is merged with the application one, so the security can be configured in one 
place with main app.

* In 2.x, Actuator defines its model as pluggable and extensible without relying on MVC for this. Hence, with this new 
model, we can take advantage of MVC and WebFlux as an underlying web technology. Forthcoming technologies could be added
by implementing the right adapters.

* In 2.x the Observation API has been completely reworked.

We will be using 2.x Actuator at this project.

##### Actuator: 'Discovery' endpoint and hypermedia

All Actuator endpoints are now placed under the `/actuator` path by default. We can tweak this path using the new property
`management.endpoints.web.base-path=/car-rental-api-actuator-discovery`:
<p align="center"><img src="img/actuator-discovery-endpoint-response.png" width="600px"/></p>

In order to access all the actuator endpoints using HTTP, we need to both enable and expose them. By default, all endpoints
but `/shutdown` are enabled, and only the `/health` and `/info` endpoints are exposed. We have to add the following
configuration to expose all endpoints:
`management.endpoints.web.exposure.include=*`

or define a list of exposed endpoints:
`management.endpoints.web.exposure.include=health, info, beans, metrics`

All the possible endpoints are listed here: [actuator.endpoints](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html#actuator.endpoints)

Example of requesting `/beans` endpoint, that we just have enabled:
<p align="center"><img src="img/actuator-beans-endpoint-response.png" width="600px"/></p>

##### Actuator: Health indicators

Out-of-the-box Spring Boot registers `DiskSpaceHealthIndicator` and `PingHealthIndicator` to report 
the healthiness of particular application aspect. Also, Spring Boot registers some indicators conditionally - if some 
dependencies are on the classpath or some other conditions are met.

Here is a full list of [auto-configured indicators](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html#actuator.endpoints.health.auto-configured-health-indicators)

To create a custom health indicator we have to implement `HealthIndicator` interface, see `com.example.health.RandomHealthIndicator`.

Here is what `/health` endpoint returns in response when RandomHealthIndicator is created:
<p align="center"><img src="img/actuator-health-endpoint-response.png" width="600px"/></p>

##### Actuator: Metrics | Micrometer

Spring Boot Actuator provides dependency management and auto-configuration for [Micrometer](https://micrometer.io/), an application metrics facade
that supports numerous monitoring systems: Datadog, Dynatrace, Graphite, Humio, New Relic, OpenTelemetry, Prometheus, Wavefront.

We can use Actuator to get a possible metric names from the `/metrics` endpoint:
<p align="center"><img src="img/actuator-metrics-endpoint-response.png" width="600px"/></p>

##### Micrometer: Meter and MeterRegistry

Spring Boot configures a `SimpleMeterRegistry` by default.

Using `CompositeMeterRegistry` provides a solution to publish application metrics to various
supported monitoring systems simultaneously. Using `MeterRegistryCustomizer` you can customize the whole set of registries
at once or individual implementations in particular. For example, a commonly requested setup is to:
* (1) export metrics to both Prometheus and CloudWatch, 
* (2) add a set of common tags to metrics flowing to both (for example, host and application identifying tags),
* (3) whitelist only a small subset of metrics to CloudWatch.

`Meter` represents a family of metrics of one type(Gauge/Timers/Counter/Other). 

Well-known meter types/families:
* Counter: merely reports a count over a specified property of an application. Could be useful when counting a number of
method calls or number of created instances of some class. We can also use `@Counted` annotation to measure a method 
execution time in cross-cutting manner. We need to add a CountedAspect as a bean, then the metrics are created when 
calling the method.
* Timer: measures latencies or frequency of events in the system. A Timer will report at least the total time and events 
count of a specific time series. Could be useful when there is a need to measure execution time of a code/method. The 
Timer is used by `@Timed` annotation to measure execution time in cross-cutting manner.
* Gauge: reports data only when observed. Gauges can be useful when monitoring stats of cache or collections.
* DistributionSummary: tracks the sample distribution of events. An example would be the response sizes for requests 
hitting an http server.

When we use annotations `@Counted` and `@Timed`, the corresponding aspect registers the Meter of some type(COUNTER, TIMER).
We also can register the Meter programmatically like we do for Gauge (see RentalInMemoryRepository).

Here is a response with new metrics added by using annotations:
<p align="center"><img src="img/actuator-new-metrics-endpoint-response.png" width="600px"/></p>

Getting Timer metric `Timed:CarRentalManagerClient.rentCar`:
<p align="center"><img src="img/actuator-metrics-timed-CarRentalManagerClient-rentCar.png" width="600px"/></p>

Getting Counter metric `Counted:RestApiController.getCars`:
<p align="center"><img src="img/actuator-metrics-counted-RestApiController-getCars.png" width="600px"/></p>

Getting Gauge metric `Gauge:RentalInMemoryRepository.storage.size`:
<p align="center"><img src="img/actuator-metrics-gauge-RentalInMemoryRepository-storage-size.png" width="600px"/></p>

##### Micrometer: Tracing

Distributed tracing allows you to see the entire journey of your requests throughout a distributed system. 

Before Spring Boot 3 we used to add the Spring Cloud Sleuth dependency to add distributed tracing capabilities to our 
application, but starting from Spring Boot 3 Spring Cloud Sleuth is no longer needed and this is replaced by the Spring Boot 
Actuator using [Micrometer Tracing](https://micrometer.io/docs/tracing), which is as a tracing facade.

Supported Tracers: 
* [OpenTelemetry Specification](https://opentelemetry.io/) - [OpenTelemetry protocol](https://opentelemetry.io/docs/specs/otel/protocol/) by dependency `io.micrometer:micrometer-tracing-bridge-otel`
* [OpenZipkin Brave](https://github.com/openzipkin/brave) - by dependency `io.micrometer:micrometer-tracing-bridge-brave`
* [OpenTracing Specification](https://opentracing.io/) - is considered to be archived, so projects smoothly migrate to OpenTelemetry

Bridge dependency gives us the Tracer for programmatic/manual tracing, so we will be able to use Spans programmatically.
In addition, the bridge creates a default preconfigured beans in Spring application context so that we will use the tracer
by convention.

We will be using `Zipkin` Tracer to create traces.

Micrometer uses the concept of Reporter to export traces via HTTP/RPC to some destination trace collector:
* Wavefront reporter by dependency `io.micrometer:micrometer-tracing-reporter-wavefront`
* OpenZipkin Zipkin Brave reporter by dependency `io.zipkin.reporter2:zipkin-reporter-brave`
* OpenZipkin Zipkin exporter with OpenTelemetry by dependency `io.opentelemetry:opentelemetry-exporter-zipkin`
* OpenZipkin URL sender by dependency `io.zipkin.reporter2:zipkin-sender-urlconnection`

We will be using `OpenZipkin Zipkin Brave` Reporter to export traces.

Having chosen a tracer and reporter, we need to decide what's the destination to report and collect traces. The most famous 
trace collectors are: [Jaeger](https://www.jaegertracing.io/), [Zipkin](https://zipkin.io/), and [Grafana Tempo](https://grafana.com/oss/tempo/).
Here is a comparison guide [Jaeger vs Zipkin vs Grafana Tempo](https://codersociety.com/blog/articles/jaeger-vs-zipkin-vs-tempo) 
that will help us to get the right collector.

We will be using `Grafana Tempo` Collector.

##### Micrometer: Instrumentation using Observation API
DRAFT
https://softwaremill.com/new-micrometer-observation-api-with-spring-boot-3/
Instrumentation is the most powerful feature of Micrometer. **The idea is to shift our focus from how we want to observe
to what we want to observe.** We don’t need to think about low-level abstractions like Timer, Counter, or Gauge to measure
something, we just need to tell what we want to observe using the Observation API.

Micrometer Observation API is a new type of API that allows us to hide low level APIs such as metrics, logging, and tracing.
Instead, we have a new concept called `Observation`. Now we just want to observe that something happened in our system,
and based on that, we may add metrics, logs, or traces. And because it is just a facade for low level API we can still
expose metrics to monitoring systems.

A `DefaultMeterObservationHandler` is automatically registered on the `ObservationRegistry`, which creates metrics for
every completed observation.

### Visualization tools

For the sake of simplicity, we will use Prometheus as a metric-storage and Grafana as a metric-view tool. Grafana is
widely used for visualization because it provides abilities to dynamically build metric dashboards to see consolidated metric representations. Prometheus is
also providing a metric view UI, but its representational capabilities are much poorer compared to Grafana.

TODO

---
Useful links:

* https://docs.spring.io/spring-boot/docs/2.0.x/actuator-api/html/
* https://www.baeldung.com/spring-boot-health-indicators
* https://www.baeldung.com/micrometer
* https://spring.io/blog/2018/03/16/micrometer-spring-boot-2-s-new-application-metrics-collector

Samples

* https://github.com/SaiUpadhyayula/springboot3-observablity
* https://programmingtechie.com/2023/09/09/spring-boot3-observability-grafana-stack/
* https://mehmetozkaya.medium.com/monitor-spring-boot-custom-metrics-with-micrometer-and-prometheus-using-docker-62798123c714