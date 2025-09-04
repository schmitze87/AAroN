#!/bin/bash

directory=$(dirname "$(readlink -f "$0")")
cd "$directory" || exit 1

cd docker_test

docker run --rm -it --name aaron \
  --volume "`pwd`/import:/import" \
  --volume "`pwd`/data:/data" \
  --volume "`pwd`/logs:/logs" \
  --volume "`pwd`/conf:/conf" \
  --volume "`pwd`/plugins:/plugins" \
  --volume "`realpath ~`/fbembed:/opt/fbembed" \
  --publish=7474:7474 \
  --publish=7687:7687 \
  --cap-add "CAP_CHOWN" \
  --cap-add "CAP_DAC_OVERRIDE" \
  --cap-add "CAP_FOWNER" \
  --cap-add "CAP_FSETID" \
  --cap-add "CAP_KILL" \
  --cap-add "CAP_NET_BIND_SERVICE" \
  --cap-add "CAP_SETFCAP" \
  --cap-add "CAP_SETGID" \
  --cap-add "CAP_SETPCAP" \
  --cap-add "CAP_SETUID" \
  --cap-add "CAP_SYS_CHROOT" \
  -e "NEO4J_AUTH=neo4j/test12345678" \
  -m 4096m \
   docker.io/mschmitze87/aaron:latest