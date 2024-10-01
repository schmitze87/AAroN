#!/bin/bash

set -x

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

if [ -d "data/databases/aramis" ]; then
  return
fi

# Datei und Pfad zur YAML-Datei
aaron_output="/import/aaron_output.yml"

aaron-cli convert -o /import/ -d /import/ &> $aaron_output

# Extrahiere die Liste mit yq und speichere sie in einem Array
mapfile -t nodesToImport < <(yq '.nodesToImport[]' "$aaron_output")
mapfile -t edgesToImport < <(yq '.edgesToImport[]' "$aaron_output")

nodes=()
edges=()

# Iteriere durch das Array und gebe jeden Eintrag mit echo aus
for entry in "${nodesToImport[@]}"; do
  nodes+=("--nodes='$(printf '%q' "$entry")'")
done
for entry in "${edgesToImport[@]}"; do
  edges+=("--relationships='$(printf '%q' "$entry")'")
done

if [[ -n "${nodes:-}" ]]; then
  if running_as_root; then
    su-exec neo4j:neo4j neo4j-admin import \
                       --database=aramis \
                       --input-encoding=UTF-8 \
                       --legacy-style-quoting=false \
                       --multiline-fields=true \
                       --ignore-extra-columns=true \
                       --ignore-empty-strings=false \
                       "${nodes[@]}" \
                       "${edges[@]}" &> /import/neo4j-admin.log
  else
    neo4j-admin import \
        --database=aramis \
        --input-encoding=UTF-8 \
        --legacy-style-quoting=false \
        --multiline-fields=true \
        --ignore-extra-columns=true \
        --ignore-empty-strings=false \
        "${nodes[@]}" \
        "${edges[@]}" &> /import/neo4j-admin.log
  fi
else
  echo "no architecture files to import"
fi

  # Neo4j 5 Version
  #neo4j-admin database import full \
  #--input-encoding=UTF-8 \
  #--legacy-style-quoting=false \
  #--multiline-fields=true \
  #--ignore-extra-columns=true \
  #--ignore-empty-strings=false \
  #--overwrite-destination=true \
  #${nodes[*]} \
  #${edges[*]} \
  #--verbose
  #aramis

set +x
