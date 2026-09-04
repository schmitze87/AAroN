############################
# 1) Build stage
############################
FROM docker.io/library/maven:3.9.9-eclipse-temurin-21-alpine AS build

WORKDIR /workspace

COPY pom.xml ./
COPY AAroN-CLI/pom.xml ./AAroN-CLI/pom.xml
COPY AAroN-Core/pom.xml ./AAroN-Core/pom.xml
COPY AAroN-Framework/pom.xml ./AAroN-Framework/pom.xml
COPY AAroN-Plugin/pom.xml ./AAroN-Plugin/pom.xml

RUN --mount=type=cache,target=/root/.m2 \
    mvn --batch-mode --no-transfer-progress -DskipTests dependency:go-offline

COPY AAroN-CLI/src ./AAroN-CLI/src
COPY AAroN-Core/src ./AAroN-Core/src
COPY AAroN-Framework/src ./AAroN-Framework/src
COPY AAroN-Plugin/src ./AAroN-Plugin/src

RUN --mount=type=cache,target=/root/.m2 \
    mvn --batch-mode --no-transfer-progress -DskipTests clean package -pl AAroN-CLI -am

FROM docker.io/library/eclipse-temurin:21-jre-alpine AS runtime

WORKDIR app
# Create non-root user
#RUN adduser -S -D -H -u 10001 -G root appuser

RUN mkdir -p /config /import /output /log /opt/fbembed

COPY --from=build /workspace/AAroN-CLI/target/aaron-cli.jar /app/aaron-cli.jar
COPY AAroN-Container/aaron_config.yml /config/aaron_config.yml

ENV LANG=C.UTF-8 \
    LC_ALL=C.UTF-8 \
    AARON_CONFIG=/config/aaron_config.yml \
    AARON_IMPORT_DIR=/import \
    AARON_OUTPUT_DIR=/import \
    PATH="${PATH}:/opt/fbembed" \
    LD_LIBRARY_PATH=/opt/fbembed \
    FIREBIRD=/opt/fbembed

WORKDIR /import
VOLUME ["/import"]
VOLUME ["/output"]
VOLUME ["/log"]

ENTRYPOINT ["java", "-XX:InitialRAMPercentage=20", "-XX:MaxRAMPercentage=80", "-jar", "/app/aaron-cli.jar"]
CMD ["convert", "-o", "/output", "-c", "/config/aaron_config.yml", "-d", "/import"]
