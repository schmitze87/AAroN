#!/bin/bash

directory=$(dirname "$(readlink -f "$0")")
cd "$directory" || exit

echo "Build AAroN Container Image"
docker build -f ./Dockerfile --pull -t mschmitze87/aaron:neo4j-4.4.19_1 -t mschmitze87/aaron:latest .