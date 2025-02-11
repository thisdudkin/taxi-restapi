# Distributed application of the Taxi Aggregator

[![Build Status](https://github.com/thisdudkin/cab-aggregator/actions/workflows/maven-build.yml/badge.svg)](https://github.com/thisdudkin/cab-aggregator/actions/workflows/maven-build.yml)

## Running the Hashicorp Vault Container

Before starting any microservice, ensure you set the required secrets in Vault: `POSTGRES_PASSWORD` and `KEYCLOAK_CLIENT_SECRET`.

Use the following command to start the Vault container:

```bash
docker run -d \
  --name vault \
  -p 8200:8200 \
  -e VAULT_DEV_ROOT_TOKEN_ID=keyword \
  -e VAULT_DEV_LISTEN_ADDRESS=0.0.0.0:8200 \
  --restart unless-stopped \
  vault:1.13.3
```

## Environment Configuration

Before starting the services, create the following configuration file in the root directory:

1. Create `.env` file:
```env
# PostgreSQL
POSTGRES_PASSWORD=your_password_here

# Keycloak
KC_DB=postgres
KC_DB_URL=jdbc:postgresql://postgres:5432/keycloak_db
KC_DB_USERNAME=postgres
KC_DB_PASSWORD=your_password_here
KC_BOOTSTRAP_ADMIN_USERNAME=admin
KC_BOOTSTRAP_ADMIN_PASSWORD=your_admin_password_here

# Elasticsearch
DISCOVERY_TYPE=single-node

# Kafka
KAFKA_ADVERTISED_LISTENERS=INTERNAL://kafka:19092,EXTERNAL://${DOCKER_HOST_IP:-127.0.0.1}:9092,DOCKER://host.docker.internal:29092
KAFKA_LISTENER_SECURITY_PROTOCOL_MAP=INTERNAL:PLAINTEXT,EXTERNAL:PLAINTEXT,DOCKER:PLAINTEXT
KAFKA_INTER_BROKER_LISTENER_NAME=INTERNAL
KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181
KAFKA_BROKER_ID=1
KAFKA_LOG4J_LOGGERS=kafka.controller=INFO,kafka.producer.async.DefaultEventHandler=INFO,state.change.logger=INFO
KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1
KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR=1
KAFKA_TRANSACTION_STATE_LOG_MIN_ISR=1
KAFKA_JMX_PORT=9999
KAFKA_JMX_HOSTNAME=${DOCKER_HOST_IP:-127.0.0.1}
KAFKA_AUTHORIZER_CLASS_NAME=kafka.security.authorizer.AclAuthorizer
KAFKA_ALLOW_EVERYONE_IF_NO_ACL_FOUND=true

# Zookeeper
ZOOKEEPER_CLIENT_PORT=2181
ZOOKEEPER_SERVER_ID=1
ZOOKEEPER_SERVERS=zookeeper:2888:3888
```

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
