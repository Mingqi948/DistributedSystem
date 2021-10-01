#!/bin/bash

echo "Cleaning docker images and containers:"
docker stop $(docker ps -aq)
docker rm $(docker ps -aq)
docker rmi $(docker images -q)

echo "Packaging maven project:"
mvn clean package

echo "Running docker compose:"
docker-compose -p quoco-rmi build
docker compose -p quoco-rmi up


docker stop $(docker ps -aq)
docker rm $(docker ps -aq)
docker rmi $(docker images -q)