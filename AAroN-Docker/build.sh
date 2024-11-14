#!/bin/bash

directory=$(dirname "$(readlink -f "$0")")
cd "$directory" || exit


echo "Build AAroN Container Image"

VERSION=$(docker run -q --rm -v "$(pwd)/..":/usr/src/mymaven -w /usr/src/mymaven maven:3.9-eclipse-temurin-11-alpine mvn --non-recursive help:evaluate -Dexpression=project.version -q -DforceStdout)
echo "code version $VERSION"
NEO4J_VERSION=$(sed 's/-SNAPSHOT//' <<< $VERSION)
echo "neo4j version $NEO4J_VERSION"
NEO4J_TAG=$(echo -n "${NEO4J_VERSION}-community")
echo "neo4j container tag $NEO4J_TAG"
docker build -f ./Dockerfile --pull --no-cache --build-arg NEO4J_VERSION=$NEO4J_TAG -t mschmitze87/aaron:latest .