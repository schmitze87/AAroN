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

function run_import {
    local import_command="neo4j-admin"

    if [[ $neo4j_version =~ 5 ]]; then
        import_command+=" database import full"
    else
        import_command+=" import --database=aramis"
    fi

    if running_as_root; then
        su-exec neo4j:neo4j $import_command \
            --input-encoding=UTF-8 \
            --legacy-style-quoting=false \
            --multiline-fields=true \
            --ignore-extra-columns=true \
            --ignore-empty-strings=false \
            --overwrite-destination=true \
            "${nodes[@]}" \
            "${edges[@]}" &> /import/neo4j-admin.log
    else
        $import_command \
            --input-encoding=UTF-8 \
            --legacy-style-quoting=false \
            --multiline-fields=true \
            --ignore-extra-columns=true \
            --ignore-empty-strings=false \
            --overwrite-destination=true \
            "${nodes[@]}" \
            "${edges[@]}" &> /import/neo4j-admin.log
    fi
}

set -x
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

neo4j_version=$(neo4j --version)

if [[ -n "${nodes:-}" ]]; then
    run_import
else
    echo "no architecture files to import"
fi

set +x