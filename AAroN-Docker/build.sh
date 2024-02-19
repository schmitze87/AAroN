#!/bin/bash

directory=$(dirname "$(readlink -f "$0")")
cd "$directory" || exit

echo "Build AAroN Container Image"
podman build -f ./Dockerfile --pull -t mschmitze87/aaron:neo4j-5.16.0 .