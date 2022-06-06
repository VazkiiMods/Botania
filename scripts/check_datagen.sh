#!/usr/bin/env sh
set -e

./gradlew :Fabric:runXplatDatagen :Forge:runData :Fabric:runFabricDatagen || exit 1

STATUS="$(git status --porcelain)"
if [ -z "$STATUS" ]
then
  echo "Datagen ok"
else
  echo "The repository is dirty after running data generators. Please make sure you committed generated files. Dirty files:"
  echo "$STATUS"
  exit 1
fi

