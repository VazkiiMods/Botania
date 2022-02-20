#!/usr/bin/env bash

RUNDIR="run"
CRASH="crash-reports"
SERVERLOG="server.log"

if [[ -d $RUNDIR/$CRASH ]]; then
  echo "Crash reports detected:"
  cat $RUNDIR/$CRASH/crash*.txt
  exit 1
fi

if grep --quiet "Fatal errors were detected" $SERVERLOG; then
  echo "Fatal errors detected:"
  cat server.log
  exit 1
fi

if grep --quiet "The state engine was in incorrect state ERRORED and forced into state SERVER_STOPPED" $SERVERLOG; then
  echo "Server force stopped:"
  cat server.log
  exit 1
fi

if ! grep --quiet -Po '.+Done \(.+\)\! For help, type "help" or "\?"' $SERVERLOG; then
  echo "Server didn't finish startup:"
  cat server.log
  exit 1
fi

echo "No crash reports detected"
exit 0

