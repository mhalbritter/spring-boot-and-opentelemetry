# Spring Boot and OpenTelemetry

This project showcases the OpenTelemetry integration in Spring Boot.

## Preparation

```shell
docker compose up
```

## Start services

Start all three services: user-service, greeting-service and hello-service.

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

## Architecture

user-service knows the users, greeting-service knows the greetings.
hello-service calls the user-service to get the user for a given id, then calls the greeting-service to get the greeting for a given locale, then combines the two to create a greeting and returns it.
