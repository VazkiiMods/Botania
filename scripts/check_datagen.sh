#!/usr/bin/env sh
set -e

./gradlew :Fabric:runXplatDatagen :Forge:runData :Fabric:runFabricDatagen || exit 1

STATUS="$(git status --porcelain Xplat/src/generated/resources Fabric/src/generated/resources Forge/src/generated/resources)"
if [ -z "$STATUS" ]
then
  echo "Datagen ok"
else
  echo "Generated resources are dirty after running data generators. Please make sure you committed generated files. Dirty files:"
  echo "$STATUS"
  exit 1
fi

