FROM docker.io/library/neo4j:4.4.31-community

RUN set -eux \
    && DEBIAN_FRONTEND=noninteractive apt-get update \
    && DEBIAN_FRONTEND=noninteractive apt-get upgrade -y \
    && DEBIAN_FRONTEND=noninteractive apt-get install -y locales \
    && rm -rf /var/lib/apt/lists/*

COPY aaron-cli aaron-import.sh /usr/bin/

RUN chmod +x /usr/bin/aaron-cli && chmod +x /usr/bin/aaron-import.sh

RUN localedef -i de_DE -f UTF-8 de_DE.UTF-8
RUN echo "LANG=\"C.UTF-8\"" > /etc/locale.conf
RUN ln -s -f /usr/share/zoneinfo/CET /etc/localtime
ENV LANG C.UTF-8
#ENV LANGUAGE de_DE.UTF-8
ENV LC_ALL C.UTF-8