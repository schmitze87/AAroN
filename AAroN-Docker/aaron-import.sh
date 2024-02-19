#!/bin/bash

#directory=$(dirname "$(readlink -f "$0")")
#cd "$directory" || exit

set -x

function running_as_root
{
    test "$(id -u)" = "0"
}

function printNodes {
  for eapFile in "$@"
  do
    file=$(basename "$(readlink -f "$eapFile")")
    echo -n "--nodes='/import/nodes_$file.csv' "
  done
}

function printEdges {
  for eapFile in "$@"
  do
    file=$(basename "$(readlink -f "$eapFile")")
    echo -n "--relationships='/import/edges_$file.csv' "
  done
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

export -f printNodes
export -f printEdges

nodes=()
edges=()
eaFiles=()

mapfile -t eaFiles < <(find /import/ -iregex '.*\.\(eapx\|eap\|qea\|qeax\)')
if [[ -n "${eaFiles:-}" ]]; then
  for eaFile in "${eaFiles[@]}"; do
    file=$(readlink -f "$eaFile")
    name=$(basename "$file")
    nodes=("${nodes[@]}" "--nodes='/import/nodes_$name.csv'")
    edges=("${edges[@]}" "--relationships='/import/edges_$name.csv'")
  done

  echo "${eaFiles[@]}"
  aaron-cli convert -o /import/ "${eaFiles[@]/#/-f}" &> /import/aaron.log
  if running_as_root; then
    gosu neo4j:neo4j neo4j-admin import \
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
else
  echo "no architecture files to import"
fi

unset -f printNodes
unset -f printEdges

set +x
