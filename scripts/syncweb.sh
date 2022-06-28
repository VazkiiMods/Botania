#!/usr/bin/env sh
set -x
LOGIN=${1:-$USER}
cd web/
bundle exec jekyll build
rsync -av _site/ $LOGIN@botaniamod.net:/var/www/html/subdom/botania/

