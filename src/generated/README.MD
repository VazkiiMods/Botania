The files under this directory should **not** be edited by hand, and any PR's trying to do so
will be closed without warning. Please see the vazkii.botania.common.data package and edit the appropriate datagenerator,
then run the `runData` gradle task to regenerate these files.

Basically: src/main/resources = hand-edited, src/generated/resources = `runData`-generated.