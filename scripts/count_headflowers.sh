#!/usr/bin/env bash
# Fun little script to see who picks which headflowers the most

PROGRAM=$(cat <<EOF
match(\$0, /\w+=(.+)/, matches) {
	table[matches[1]]++
}

END {
	for (flower in table) {
		print table[flower],flower
	}
}
EOF
)

awk "${PROGRAM}" contributors.properties | sort -nr

