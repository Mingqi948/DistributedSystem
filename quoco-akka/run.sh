#!/bin/bash

function cleanDocker() {

  echo ">> Cleaning docker images and containers <<"
  declare -a images=("auldfellas_akka_img" "broker_akka_img" "dodgydrivers_akka_img" "girlpower_akka_img" "client_akka_img")
  declare -a containers=("auldfellas_akka" "broker_akka" "dodgydrivers_akka" "girlpower_akka" "client_akka")

  #Stop & Remove containers
  for container in "${containers[@]}"
  do
    docker stop "$container"
    docker rm "$container"
  done

  #Remove images
  for image in "${images[@]}"
  do
    imageId=$(docker images "$image" -q)
    docker rmi "$imageId"
  done
}

#Main scripts

cleanDocker #Clean
echo ">> Packaging maven project <<"
mvn clean package #Packaging
echo ">> Running docker compose <<"
docker-compose -p quoco_akka up #Run docker-compose
cleanDocker #Finish up
