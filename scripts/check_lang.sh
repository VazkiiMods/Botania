#!/usr/bin/env sh

FAILED=0

for f in ./Xplat/src/main/resources/assets/botania/lang/*.json
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
else
    echo "Lang files ok"
fi
