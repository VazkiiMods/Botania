#!/usr/bin/env sh
set -x
LOGIN=${1:-$USER}
rsync -av web/ $LOGIN@botaniamod.net:/var/www/html/subdom/botania/

