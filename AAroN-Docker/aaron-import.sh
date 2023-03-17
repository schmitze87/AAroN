#!/bin/bash

#directory=$(dirname "$(readlink -f "$0")")
#cd "$directory" || exit

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

export -f printNodes
export -f printEdges

fileParams=()
nodes=()
edges=()
eaFiles=()

mapfile -t -d " " eaFiles < <(find /import/ -iregex '.*\.\(eapx\|eap\|qea\|qeax\)')
if [[ -n "${eaFiles:-}" ]]; then
  for eaFile in $eaFiles
  do
  #  echo $eaFile
    file=$(readlink -f "$eaFile")
    fileParams+=("-f $file")
    name=$(basename "$file")
    nodes+=("--nodes='/import/nodes_$name.csv'")
    edges+=("--relationships='/import/edges_$name.csv'")
  done

  echo ${fileParams[*]}

  aaron-cli convert ${fileParams[*]} -o /import/ &> /import/aaron.log

  neo4j-admin import \
  --database=aramis \
  --input-encoding=UTF-8 \
  --legacy-style-quoting=false \
  --multiline-fields=true \
  --ignore-extra-columns=true \
  --ignore-empty-strings=false \
  ${nodes[*]} \
  ${edges[*]} &> /import/neo4j-admin.log

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
