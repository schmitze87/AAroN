#!/bin/bash

function running_as_root
{
    test "$(id -u)" = "0"
}

function deleteCSV {
  for eapFile in "$@"
  do
    file=$(basename "$(readlink -f "$eapFile")")
    rm "/import/nodes_$file.csv"
    rm "/import/edges_$file.csv"
  done
}

function run_load_dump {
  local load_dump_command="neo4j-admin"
  local dumpFile="$1"

  if [[ $neo4j_version =~ 4 ]]; then
    load_dump_command+=" load --from=- --database=$NEO4J_dbms_default__database --force"
  elif [[ $neo4j_version =~ 5 ]]; then
    load_dump_command+=" database load --overwrite-destination=true --from-stdin $NEO4J_initial_dbms_default__database"
  else
    load_dump_command+=" database load --overwrite-destination=true --from-stdin $NEO4J_initial_dbms_default__database"
  fi

  if running_as_root; then
    su-exec neo4j:neo4j $load_dump_command < $dumpFile
  else
    $load_dump_command < $dumpFile
  fi
}

function run_import {
    local import_command="neo4j-admin"

    if [[ $neo4j_version =~ 4 ]]; then
      import_command+=" import --database=$NEO4J_dbms_default__database"
    else
      import_command+=" database import full $NEO4J_initial_dbms_default__database"
    fi

    if running_as_root; then
        su-exec neo4j:neo4j $import_command \
            --input-encoding=UTF-8 \
            --legacy-style-quoting=false \
            --multiline-fields=true \
            --ignore-extra-columns=true \
            --ignore-empty-strings=false \
            "${nodes[@]}" \
            "${edges[@]}" &> /import/neo4j-admin.log
    else
        $import_command \
            --input-encoding=UTF-8 \
            --legacy-style-quoting=false \
            --multiline-fields=true \
            --ignore-extra-columns=true \
            --ignore-empty-strings=false \
            "${nodes[@]}" \
            "${edges[@]}" &> /import/neo4j-admin.log
    fi
}

#set -x
neo4j_version=$(neo4j --version)

#Check if database already exists. If yes then exit script and continue with startup
if [[ $neo4j_version =~ 4 ]]; then
  dbDir=$NEO4J_dbms_default__database
else
  dbDir=$NEO4J_initial_dbms_default__database
fi
if [ -f "/data/databases/$dbDir/neostore" ]; then
  echo "Database $dbDir already exists. Not going to import any data. Using existing database."
  return
fi

if [ -f "/backups/${dbDir}.dump" ]; then
  echo "Found dump for database $dbDir. Going to import dump."
  run_load_dump "/backups/${dbDir}.dump"
  return
fi

aaron_config=$AARON_CONFIG
aaron_output=$AARON_OUTPUT
aaron_log=$AARON_LOG

if [ -f "$aaron_config" ]; then
  java -XX:InitialRAMPercentage=20 -XX:MaxRAMPercentage=80 -Djna.library.path="/opt/firebird/" \
    -Djavax.net.ssl.trustStore=/opt/java/openjdk/lib/security/cacerts -Djavax.net.ssl.trustStorePassword=changeit \
    -jar /usr/bin/aaron-cli.jar convert -o /import/ -c "$aaron_config" &> $aaron_log
elif [ -d "/import/" ]; then
  java -XX:InitialRAMPercentage=20 -XX:MaxRAMPercentage=80 -Djna.library.path="/opt/firebird/" \
    -Djavax.net.ssl.trustStore=/opt/java/openjdk/lib/security/cacerts -Djavax.net.ssl.trustStorePassword=changeit \
    -jar /usr/bin/aaron-cli.jar convert -o /import/ -d /import/ &> $aaron_log
fi

aaronExitCode=$?
if [ $aaronExitCode -ne 0 ]; then
    echo "Error during aaron processing. Aborting..."
    exit $aaronExitCode
fi

if [ -f "${aaron_output}" ]; then
  # Extract list with yq and store in array
  mapfile -t nodesToImport < <(yq '.nodesToImport[]' "$aaron_output")
  mapfile -t edgesToImport < <(yq '.edgesToImport[]' "$aaron_output")

  nodes=()
  edges=()

  for entry in "${nodesToImport[@]}"; do
    nodes+=("--nodes='$(printf '%q' "$entry")'")
  done
  for entry in "${edgesToImport[@]}"; do
    edges+=("--relationships='$(printf '%q' "$entry")'")
  done

  if [[ -n "${nodes:-}" ]]; then
      run_import
  else
      echo "no architecture csv-files to import"
  fi
fi

#set +x