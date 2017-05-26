#!/bin/bash

TAG=latest

if [ x"$1" != x ]; then
TAG="$1"
fi

echo Updating nabu:$TAG

docker build -t nabu:$TAG .
docker tag nabu:$TAG thomann/nabu:$TAG
docker push thomann/nabu:$TAG

