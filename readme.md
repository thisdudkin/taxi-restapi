# Distributed application of the Taxi Aggregator

[![Build Status](https://github.com/thisdudkin/cab-aggregator/actions/workflows/maven-build.yml/badge.svg)](https://github.com/thisdudkin/cab-aggregator/actions/workflows/maven-build.yml)

## Starting services locally without Docker

Every microservice is a Spring Boot application and can be started locally using IDE or `../mvnw spring-boot:run` command.
Please note that supporting service (Discovery Server) must be started before any other application (Passenger, Driver, Rides and API).
If everything goes well, you can access the following services at given location:
* Discovery Server - http://localhost:8761
* Authentication Server - http://localhost:8087
* Notification Service - http://localhost:8084
* Promocode Service - http://localhost:8088
* Passenger, Driver, Rides and Payment Services - check Eureka Dashboard.

## Starting services locally with docker-compose

In order to start entire infrastructure using Docker, you have to build images by executing
``bash
./mvnw clean install -P buildDocker
``
This requires `Docker` or `Docker desktop` to be installed and running.

By default, the Docker OCI image is build for an `linux/amd64` platform.
For other architectures, you could change it by using the `-Dcontainer.platform` maven command line argument.
For instance, if you target container images for an Apple M2, you could use the command line with the `linux/arm64` architecture:
```bash
./mvnw clean install -P buildDocker -Dcontainer.platform="linux/arm64"
```

Once images are ready, you can start them with a single command
`docker compose up -d`. 

Containers startup order is coordinated with the `service_healthy` condition of the Docker Compose [depends-on](https://github.com/compose-spec/compose-spec/blob/main/spec.md#depends_on) expression
and the [healthcheck](https://github.com/compose-spec/compose-spec/blob/main/spec.md#healthcheck) of the service containers.
After starting services, it takes a while for API Gateway to be in sync with service registry,
so don't be scared of initial Spring Cloud Gateway timeouts. You can track services availability using Eureka dashboard
available by default at http://localhost:8761.

The `master` branch uses an OpenJDK with Java 21 as Docker base image.

*NOTE: Under MacOSX or Windows, make sure that the Docker VM has enough memory to run the microservices. The default settings
are usually not enough and make the `docker-compose up` painfully slow.*

## Database configuration

In its default configuration, application uses a PostgreSQL database.
Dependency for PostgreSQL JDBC Driver is already included in the `pom.xml` files.
*NOTE: Some services need a `postgis` extension for PostgreSQL, so you might pull `postgis/postgis` image.

You may build a custom postgres image. First enter the `docker` directory: `ls docker`.
Then build the docker image:

```
docker buildx build -t repository_prefix/image-tag -f pg.Dockerfile .
```

## Pushing to a Docker registry

Docker images for `linux/amd64` and `linux/arm64` platforms have been published into DockerHub
in the [rainaddan](https://hub.docker.com/u/rainaddan) organization.
You can pull an image:
```bash
docker pull rainaddan/taxi-restapi-discovery-server
```
You may prefer to build then push images to your own Docker registry.
