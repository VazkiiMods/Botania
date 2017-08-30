@echo off
:: Vazkii's JSON creator for items
:: Put in your /resources/assets/%modid%/models/item
:: Makes basic item JSON files
:: Requires a _standard_item.json to be present for parenting
:: Can make multiple items at once
::
:: Usage:
:: _make (item name 1) (item name 2) (item name x)
::
:: Change this to your mod's ID
set modid=botania

setlocal enabledelayedexpansion

for %%x in (%*) do (
	echo Making %%x.json
	(
		echo {
		echo 	"parent": "item/generated",
		echo 	"textures": { 
		echo 		"layer0": "%modid%:items/%%x"
		echo 	}
		echo }
	) > %%x.json

)