#!/usr/bin/env bash
set -euo pipefail

# Remove 'refs/tags/' from front
TAGNAME="${GIT_REF/#refs\/tags\/}"

# Remove 'release-' from front
VERSION="${TAGNAME/#release-}"
# also remove '_<anything>' from end (for potential retries)
VERSION="${VERSION/%_*}"
MC_VERSION=$(echo "${VERSION}" | cut -d '-' -f 1)
CHANGELOG_TEXT=$(sed -n "/version=\"${VERSION}\"/,/---/"'{//!p;}' web/changelog.md)
CHANGELOG_MARKDOWN=$(cat <<EOF
Changes in this version:
${CHANGELOG_TEXT}
EOF
)

function release_github() {
	echo >&2 'Creating GitHub Release'
	gh release create "${TAGNAME}" --verify-tag -t "Botania ${VERSION}" -n "${CHANGELOG_MARKDOWN}"
	echo >&2 'Uploading Fabric Jar and Signature to GitHub'
	gh release upload "${TAGNAME}" "${FABRIC_JAR}#Fabric Jar"
	gh release upload "${TAGNAME}" "${FABRIC_JAR}.asc#Fabric Signature"
	echo >&2 'Uploading NeoForge Jar and Signature to GitHub'
	gh release upload "${TAGNAME}" "${NEOFORGE_JAR}#NeoForge Jar"
	gh release upload "${TAGNAME}" "${NEOFORGE_JAR}.asc#NeoForge Signature"
	echo >&2 'Uploading GoG Jar and Signature to GitHub'
	gh release upload "${TAGNAME}" "${GOG_JAR}#GoG Jar"
	gh release upload "${TAGNAME}" "${GOG_JAR}.asc#GoG Signature"
}

function release_modrinth() {
	echo >&2 'Uploading Fabric Jar to Modrinth'
	local MODRINTH_FABRIC_SPEC
	MODRINTH_FABRIC_SPEC=$(cat <<EOF
{
	"dependencies": [
		{
			"project_id": "P7dR8mSH",
			"dependency_type": "required"
		},
		{
			"project_id": "nU0bVIaL",
			"dependency_type": "required"
		},
		{
			"project_id": "5aaWibi9",
			"dependency_type": "required"
		}
	],
	"version_type": "release",
	"loaders": ["fabric", "quilt"],
	"featured": false,
	"project_id": "pfjLUfGv",
	"file_parts": [
		"jar", "signature"
	],
	"primary_file": "jar",
	"file_types": {
		"signature": "signature"
	}
}
EOF
						)

	MODRINTH_FABRIC_SPEC=$(echo "${MODRINTH_FABRIC_SPEC}" | \
							   jq --arg name "${VERSION}-fabric" \
								  --arg mcver "${MC_VERSION}" \
								  --arg changelog "${CHANGELOG_MARKDOWN}" \
								  '.name=$ARGS.named.name | .version_number=$ARGS.named.name | .game_versions=[$ARGS.named.mcver] | .changelog=$ARGS.named.changelog')
	curl 'https://api.modrinth.com/v2/version' \
		 -H "Authorization: ${MODRINTH_TOKEN}" \
		 -F "data=${MODRINTH_FABRIC_SPEC}" \
		 -F "jar=@${FABRIC_JAR}" \
		 -F "signature=@${FABRIC_JAR}.asc"

	echo >&2 'Uploading NeoForge Jar to Modrinth'
	local MODRINTH_NEOFORGE_SPEC
	MODRINTH_NEOFORGE_SPEC=$(cat <<EOF
{
	"dependencies": [
		{
			"project_id": "nU0bVIaL",
			"dependency_type": "required"
		},
		{
			"project_id": "vvuO3ImH",
			"dependency_type": "required"
		}
	],
	"version_type": "release",
	"loaders": ["neoforge"],
	"featured": false,
	"project_id": "pfjLUfGv",
	"file_parts": [
		"jar", "signature"
	],
	"primary_file": "jar",
	"file_types": {
	  "signature": "signature"
	}
}
EOF
					   )

	MODRINTH_NEOFORGE_SPEC=$(echo "${MODRINTH_NEOFORGE_SPEC}" | \
							  jq --arg name "${VERSION}-neoforge" \
								 --arg mcver "${MC_VERSION}" \
								 --arg changelog "${CHANGELOG_MARKDOWN}" \
								 '.name=$ARGS.named.name | .version_number=$ARGS.named.name | .game_versions=[$ARGS.named.mcver] | .changelog=$ARGS.named.changelog')
	curl 'https://api.modrinth.com/v2/version' \
		 -H "Authorization: ${MODRINTH_TOKEN}" \
		 -F "data=${MODRINTH_NEOFORGE_SPEC}" \
		 -F "jar=@${NEOFORGE_JAR}" \
		 -F "signature=@${NEOFORGE_JAR}.asc"
}

function release_curseforge() {
	# Java versions, Loaders, and Environment tags are actually "game versions" (lmfao), as are real game versions.

	echo >&2 'Uploading Fabric Jar to CurseForge'
	local CURSEFORGE_FABRIC_SPEC
	CURSEFORGE_FABRIC_SPEC=$(cat <<EOF
{
  "changelogType": "markdown",
  "releaseType": "release",
  "relations": {
    "projects": [
      {
        "slug": "fabric-api",
        "type": "requiredDependency"
      },
      {
        "slug": "patchouli",
        "type": "requiredDependency"
      },
      {
        "slug": "trinkets",
        "type": "requiredDependency"
      }
    ]
  },
  "gameVersionNames": ["Client", "Server", "Fabric"]
}
EOF
						  )

	CURSEFORGE_FABRIC_SPEC=$(echo "${CURSEFORGE_FABRIC_SPEC}" | \
								 jq --arg changelog "${CHANGELOG_MARKDOWN}" \
									--arg mcver "${MC_VERSION}" \
									'.gameVersionNames += [$ARGS.named.mcver] | .changelog=$ARGS.named.changelog')
	curl 'https://minecraft.curseforge.com/api/projects/421839/upload-file' \
		 -H "X-Api-Token: ${CURSEFORGE_TOKEN}" \
		 -F "metadata=${CURSEFORGE_FABRIC_SPEC}" \
		 -F "file=@${FABRIC_JAR}"
	# TODO: Upload the asc as an 'Additional file'

	echo >&2 'Uploading NeoForge Jar to CurseForge'
	local CURSEFORGE_NEOFORGE_SPEC
	CURSEFORGE_NEOFORGE_SPEC=$(cat <<EOF
{
  "changelogType": "markdown",
  "releaseType": "release",
  "relations": {
    "projects": [
      {
        "slug": "patchouli",
        "type": "requiredDependency"
      },
      {
        "slug": "curios",
        "type": "requiredDependency"
      }
    ]
  },
  "gameVersionNames": ["Client", "Server", "NeoForge"]
}
EOF
						 )

	CURSEFORGE_NEOFORGE_SPEC=$(echo "${CURSEFORGE_NEOFORGE_SPEC}" | \
								jq --arg changelog "${CHANGELOG_MARKDOWN}" \
								   --arg mcver "${MC_VERSION}" \
								   '.gameVersionNames += [$ARGS.named.mcver] | .changelog=$ARGS.named.changelog')
	curl 'https://minecraft.curseforge.com/api/projects/225643/upload-file' \
		 -H "X-Api-Token: ${CURSEFORGE_TOKEN}" \
		 -F "metadata=${CURSEFORGE_NEOFORGE_SPEC}" \
		 -F "file=@${NEOFORGE_JAR}"
	# TODO: Upload the asc as an 'Additional file'
}

release_github
release_modrinth
release_curseforge
