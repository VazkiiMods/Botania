#!/usr/bin/env sh
# Sets up some git config defaults
set -x

git config sendemail.to "~williewillus/violet-moon@lists.sr.ht"
git config format.subjectPrefix "PATCH botania"
git config pull.rebase true
