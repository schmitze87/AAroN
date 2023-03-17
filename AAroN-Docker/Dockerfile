FROM docker.io/library/neo4j:4.4.16-community

RUN set -eux \
    && DEBIAN_FRONTEND=noninteractive apt-get update \
    && DEBIAN_FRONTEND=noninteractive apt-get upgrade -y \
    && rm -rf /var/lib/apt/lists/*

COPY aaron-cli aaron-import.sh /usr/bin/

RUN chmod +x /usr/bin/aaron-cli && chmod +x /usr/bin/aaron-import.sh