#!/usr/bin/env sh
set -e

./gradlew :Fabric:runXplatDatagen :NeoForge:runNeoforgeDatagen :Fabric:runFabricDatagen :NeoForge:runGardenOfGlassDatagen || exit 1

STATUS="$(git status --porcelain Xplat/src/generated/resources Fabric/src/generated/resources NeoForge/src/generated/resources)"
if [ -z "$STATUS" ]
then
  echo "Datagen ok"
else
  echo "Generated resources are dirty after running data generators. Please make sure you committed generated files. Dirty files:"
  echo "$STATUS"
  exit 1
fi

