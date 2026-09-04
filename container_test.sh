#!/bin/bash

directory=$(dirname "$(readlink -f "$0")")
cd "$directory" || exit 1

cd docker_test || exit

#--userns=keep-id \
#  --user "`id -u`:`id -g`" \

podman run -it --name aaron \
  --volume "`pwd`/import:/import:Z" \
  --volume "`pwd`/import:/output:Z" \
  --volume "`pwd`/conf:/conf:Z" \
  --memory "8000m" \
    docker.io/mschmitze87/aaron:latest