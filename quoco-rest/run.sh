#!/bin/bash

projectName="quoco_rest"

function cleanDocker() {

  echo ">> Cleaning docker images and containers <<"
  declare -a images=("auldfellas_rest_img" "client_rest_img" "broker_rest_img" "dodgydrivers_rest_img" "girlpower_rest_img")
  declare -a containers=("auldfellas_rest" "client_rest" "broker_rest" "dodgydrivers_rest" "girlpower_rest")

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
docker compose -p "$projectName" up #Run docker-compose
cleanDocker #Finish up
