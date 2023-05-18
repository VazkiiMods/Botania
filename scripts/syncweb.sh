#!/usr/bin/env sh
set -x
cd web/
bundle exec jekyll build
rsync -av _site/ customer@botaniaweb.playat.ch:/var/www/botaniamod_net

