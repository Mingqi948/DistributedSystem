#!/bin/bash

function cleanDocker() {

  echo ">> Cleaning docker images and containers <<"
  declare -a images=("auldfellas_ws_img" "broker_ws_img" "dodgydrivers_ws_img" "girlpower_ws_img" "client_ws_img")
  declare -a containers=("auldfellas_ws" "broker_ws" "dodgydrivers_ws" "girlpower_ws" "client_ws")

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
docker-compose -p quoco_ws up #Run docker-compose
cleanDocker #Finish up
