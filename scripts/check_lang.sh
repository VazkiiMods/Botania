#!/usr/bin/env sh

FAILED=0

for f in ./Common/src/main/resources/assets/botania/lang/*.json
do
    if ! jq . "${f}" > /dev/null
    then
        echo "Lang file ${f} is malformed JSON"
        FAILED=1
    fi
done

if [ "${FAILED}" -ne 0 ]
then
    exit 1
fi
