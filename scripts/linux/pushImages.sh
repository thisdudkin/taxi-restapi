#!/bin/bash
docker push ${REPOSITORY_PREFIX}/taxi-restapi-api-gateway:${DOCKER_VERSION}
docker push ${REPOSITORY_PREFIX}/taxi-restapi-authentication-server:${DOCKER_VERSION}
docker push ${REPOSITORY_PREFIX}/taxi-restapi-discovery-server:${DOCKER_VERSION}
docker push ${REPOSITORY_PREFIX}/taxi-restapi-driver-service:${DOCKER_VERSION}
docker push ${REPOSITORY_PREFIX}/taxi-restapi-notification-service:${DOCKER_VERSION}
docker push ${REPOSITORY_PREFIX}/taxi-restapi-passenger-service:${DOCKER_VERSION}
docker push ${REPOSITORY_PREFIX}/taxi-restapi-payment-service:${DOCKER_VERSION}
docker push ${REPOSITORY_PREFIX}/taxi-restapi-promocode-service:${DOCKER_VERSION}
docker push ${REPOSITORY_PREFIX}/taxi-restapi-rides-service:${DOCKER_VERSION}
