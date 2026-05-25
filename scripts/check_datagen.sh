#!/usr/bin/env sh
set -e

./gradlew :Fabric:runXplatDatagen :NeoForge:runNeoforgeDatagen :Fabric:runFabricDatagen :NeoForge:runGardenOfGlassDatagen :Xplat:normalizeLanguageFiles || exit 1

STATUS="$(git status --porcelain Xplat/src/generated/resources Xplat/src/main/resources/assets/botania/lang Fabric/src/generated/resources NeoForge/src/generated/resources garden_of_glass/src/generated/resources)"
if [ -z "$STATUS" ]
then
  echo "Datagen ok"
else
  echo "Generated resources are dirty after running data generators. Please make sure you committed generated files. Dirty files:"
  echo "$STATUS"
  exit 1
fi

