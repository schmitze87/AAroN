#!/bin/bash

directory=$(dirname "$(readlink -f "$0")")
cd "$directory" || exit

REVISION=$(sed -n 's:.*<revision>\(.*\)</revision>.*:\1:p' pom.xml | head -n 1)
CHANGELIST=$(sed -n 's:.*<changelist>\(.*\)</changelist>.*:\1:p' pom.xml | head -n 1)
SHA1=$(sed -n 's:.*<sha1>\(.*\)</sha1>.*:\1:p' pom.xml | head -n 1)
VERSION="${REVISION}${CHANGELIST}${SHA1}"
echo "AAroN version $VERSION"

echo "Build AAroN Container Image"
podman build -f ./AAroN-Container/Dockerfile -t docker.io/mschmitze87/aaron:latest -t docker.io/mschmitze87/aaron:$VERSION .
