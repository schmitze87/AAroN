#!/bin/bash

directory=$(dirname "$(readlink -f "$0")")
cd "$directory" || exit

docker run -q --rm -v "$(pwd)":/usr/src/mymaven -w /usr/src/mymaven maven:3.9-eclipse-temurin-11-alpine mvn -ntp versions:set -DnewVersion=$1
docker run -q --rm -v "$(pwd)":/usr/src/mymaven -w /usr/src/mymaven maven:3.9-eclipse-temurin-11-alpine mvn -ntp versions:commit