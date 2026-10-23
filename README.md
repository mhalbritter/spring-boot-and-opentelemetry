# Spring Boot and OpenTelemetry

This project showcases the OpenTelemetry integration in Spring Boot.

## Prerequisites

* Java 25 (`sdk env` picks the version from `.sdkmanrc`)
* Docker

## Preparation

Start the [Grafana LGTM stack](https://github.com/grafana/docker-otel-lgtm), which receives logs, metrics and traces:

```shell
docker compose up
```

## Start services

Run each service in its own terminal, from the project root:

```shell
./gradlew :user-service:bootRun
./gradlew :greeting-service:bootRun
./gradlew :hello-service:bootRun
```

| Service          | Port |
|------------------|------|
| hello-service    | 8080 |
| user-service     | 8081 |
| greeting-service | 8082 |

When starting from an IDE, use the service's module directory as the working directory.

## Call hello-service

```shell
curl -i localhost:8080/api/1
```

You can request different users:

```shell
curl -i localhost:8080/api/2
```

or

```shell
curl -i localhost:8080/api/3
```


You can also request the greeting in different languages:

```shell
curl -i -H "Accept-Language: de" localhost:8080/api/1
```

It knows about English, German and Spanish.

If you want to see a trace with an error in it, use this:

```shell
curl -i localhost:8080/api/boom
```

## Observability signals

The applications are configured to send logs, metrics and traces to the Grafana LGTM stack, which is running in Docker Compose.

* [Logs](http://localhost:3000/a/grafana-lokiexplore-app/explore)
* [Metrics](http://localhost:3000/a/grafana-metricsdrilldown-app/drilldown)
* [Traces](http://localhost:3000/a/grafana-exploretraces-app/explore)

No login is needed.

### Without Grafana

To see the raw signals instead, use an OpenTelemetry Collector which logs a line for every batch it receives:

```shell
docker compose -f otel-collector-compose.yaml up
```

It uses the same ports as the LGTM stack, so stop that first.
Start the services with `--args='--spring.docker.compose.file=../otel-collector-compose.yaml'`.

## Configuration

hello-service has these switches in `hello-service/src/main/resources/application.properties`:

| Property                                | Default in file | Effect                                                                                                                                                         |
|-----------------------------------------|-----------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `hello.async`                           | `true`          | Call user-service and greeting-service in parallel on the `applicationTaskExecutor`. Shows that trace context crosses thread boundaries.                       |
| `hello.open-telemetry-api-usage`        | `false`         | Enable a component instrumented with the OpenTelemetry API instead of Micrometer. Every 10s it creates `dummy-span` and increments `dummy-counter`.            |
| `hello.greeting-client.use-rest-client` | `true`          | Call greeting-service with a `RestClient` instead of the HTTP interface client. See `HttpFacade` for how a self-built `RestClient` breaks context propagation. |

## Architecture

user-service knows the users, greeting-service knows the greetings.
hello-service calls the user-service to get the user for a given id, then calls the greeting-service to get the greeting for a given locale, then combines the two to create a greeting and returns it.

```
                 curl localhost:8080/api/1
                 Accept-Language: de
                            |
                            v
              +---------------------------+
              |       hello-service       |
              |           :8080           |
              +---------------------------+
                 |                     |
     GET /api/1  |                     |  GET /api
    (traceparent)|                     |  (traceparent, Accept-Language)
                 v                     v
  +---------------------+       +---------------------+
  |    user-service     |       |  greeting-service   |
  |        :8081        |       |        :8082        |
  |   users in H2 DB    |       |      greetings      |
  +---------------------+       +---------------------+
                 |                     |
                 |  user     greeting  |
                 +----------+----------+
                            |
                            v
                 hello-service answers "Hallo Moritz"


  All three services export logs, metrics and traces via OTLP:

  hello-service ----+
  user-service -----+------- OTLP ------> +----------------------------+
  greeting-service -+                     |   Grafana LGTM (Docker)    |
                                          |  Loki       <- logs        |
                                          |  Prometheus <- metrics     |
                                          |  Tempo      <- traces      |
                                          |  Grafana UI :3000          |
                                          +----------------------------+
```

The `traceparent` header carries the trace context, so one request results in a single trace spanning all three services.
Log lines carry the trace ID, so the logs of a trace can be found in Loki.
