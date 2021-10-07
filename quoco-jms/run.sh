#!/bin/bash

projectName="quoco_jms"

function cleanDocker() {

  echo ">> Cleaning docker images and containers <<"
  declare -a images=("rmohr/activemq:latest" "auldfellas_jms_img" "client_jms_img")
  declare -a containers=("activemq" "auldfellas_jms" "client_jms")

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
