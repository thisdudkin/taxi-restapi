@echo off
docker tag %REPOSITORY_PREFIX%/taxi-restapi-api-gateway %REPOSITORY_PREFIX%/taxi-restapi-api-gateway:%DOCKER_VERSION%
docker tag %REPOSITORY_PREFIX%/taxi-restapi-authentication-server %REPOSITORY_PREFIX%/taxi-restapi-authentication-server:%DOCKER_VERSION%
docker tag %REPOSITORY_PREFIX%/taxi-restapi-discovery-server %REPOSITORY_PREFIX%/taxi-restapi-discovery-server:%DOCKER_VERSION%
docker tag %REPOSITORY_PREFIX%/taxi-restapi-driver-service %REPOSITORY_PREFIX%/taxi-restapi-driver-service:%DOCKER_VERSION%
docker tag %REPOSITORY_PREFIX%/taxi-restapi-notification-service %REPOSITORY_PREFIX%/taxi-restapi-notification-service:%DOCKER_VERSION%
docker tag %REPOSITORY_PREFIX%/taxi-restapi-passenger-service %REPOSITORY_PREFIX%/taxi-restapi-passenger-service:%DOCKER_VERSION%
docker tag %REPOSITORY_PREFIX%/taxi-restapi-payment-service %REPOSITORY_PREFIX%/taxi-restapi-payment-service:%DOCKER_VERSION%
docker tag %REPOSITORY_PREFIX%/taxi-restapi-promocode-service %REPOSITORY_PREFIX%/taxi-restapi-promocode-service:%DOCKER_VERSION%
docker tag %REPOSITORY_PREFIX%/taxi-restapi-rides-service %REPOSITORY_PREFIX%/taxi-restapi-rides-service:%DOCKER_VERSION%

