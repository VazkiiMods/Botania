// Botania has built-in integration for CraftTweaker. Here are some script examples.
// For some more detailed documentation, refer to CT docs: https://docs.blamejared.com/

// Note: if you are adding a recipe that replaces an existing recipe, to keep it working with the lexicon,
// name it the same as the path of the one you replaced - eg. if you replaced "botania:pure_daisy/livingrock",
// name your replacement "pure_daisy/livingrock". Changes like this will be detected automatically.
// This only works for recipes of the same type, though.


// Pure Daisy

// First up, recipe removal by output. Pure Daisy recipes output blockstates, not stacks.
<recipetype:botania:pure_daisy>.removeRecipe(<blockstate:botania:livingrock>);

// Adding a recipe takes an output, an input ingredient and (optionally) recipe time. The default is 150, which translates to (150 * 8) ticks = 1 minute.
<recipetype:botania:pure_daisy>.addRecipe("pure_daisy_test", <blockstate:minecraft:clay>, <tag:blocks:forge:storage_blocks/gold>, 20);

// As inputs, you can use any state ingredient, which are created implicitly from: block tags, blocks, specific blockstates, arrays of blocks. No explicit casting required here.
// Should you actually need them elsewhere, check docs for StateIngredient.
<recipetype:botania:pure_daisy>.addRecipe("pure_daisy_test2", <blockstate:minecraft:clay>, <tag:blocks:forge:storage_blocks/iron>);
<recipetype:botania:pure_daisy>.addRecipe("pure_daisy_test3", <blockstate:minecraft:clay>, <block:minecraft:diamond_ore>);
<recipetype:botania:pure_daisy>.addRecipe("pure_daisy_test4", <blockstate:minecraft:clay>, <blockstate:minecraft:redstone_ore:lit=true>);
<recipetype:botania:pure_daisy>.addRecipe("pure_daisy_test5", <blockstate:minecraft:clay>, [<block:minecraft:coarse_dirt>, <block:minecraft:grass_block>]);


// Mana Infusion

// Removing recipes by output.
<recipetype:botania:mana_infusion>.removeRecipe(<item:minecraft:apple>);

// A simple recipe that converts an item into dirt for a small mana cost...
<recipetype:botania:mana_infusion>.addRecipe("mana_infusion_test", <item:minecraft:dirt>, <item:minecraft:enchanted_golden_apple>, 200);

// And some that convert items to dirt, but require a catalyst underneath.
// You can use any state ingredient as a catalyst, just like with the pure daisy inputs.
// The two default catalysts you may be familiar with are <block:botania:alchemy_catalyst> and <block:botania:conjuration_catalyst>.
<recipetype:botania:mana_infusion>.addRecipe("mana_infusion_test_catalyst", <item:minecraft:dirt>, <item:minecraft:apple>, 200, <block:botania:alchemy_catalyst>);
<recipetype:botania:mana_infusion>.addRecipe("mana_infusion_test_catalyst2", <item:minecraft:dirt>, <item:minecraft:golden_apple>, 2000, <block:minecraft:oak_stairs>);

// If you are replacing an existing recipe set or adding to an existing one (like cycling between different flowers with an alchemy catalyst),
// you can define the recipe group to make it seamlessly appear in the lexicon.
// If you don't need a group, you can use anything, like "" (empty string).
<recipetype:botania:mana_infusion>.addRecipe("mana_infusion_test_group",
        <item:minecraft:poppy>, <item:minecraft:wither_rose>, 20000, <block:botania:alchemy_catalyst>, "botania:flower_cycle");

// Lastly, mana infusion allows setting output depending on input. An example that copies stack data when crafting:
import crafttweaker.api.item.IItemStack;
<recipetype:botania:mana_infusion>.addRecipe("mana_infusion_test_helmet", <item:botania:manasteel_helmet>, <item:minecraft:iron_helmet>.anyDamage(),
        500, null, "", (usualOut as IItemStack, inputs as IItemStack) => { return usualOut.withTag(inputs.tag); });


// Elven Trade

// Removing recipes by output.
<recipetype:botania:elven_trade>.removeRecipe(<item:botania:dragonstone>);
// If some mod adds a recipe with multiple outputs, you can remove them by specifying an array. No such recipes exist in base Botania.
// <recipetype:botania:elven_trade>.removeRecipe([<item:botania:dragonstone>, <item:botania:dragonstone>]);

// Adding recipes. They can have multiple outputs and multiple inputs.
<recipetype:botania:elven_trade>.addRecipe("elven_trade_1_to_1_test", [<item:minecraft:dirt>], <item:minecraft:glass>);
<recipetype:botania:elven_trade>.addRecipe("elven_trade_multiple_to_1_test", [<item:minecraft:apple>], <item:minecraft:glowstone>, <item:minecraft:yellow_wool>, <item:minecraft:green_wool>);
<recipetype:botania:elven_trade>.addRecipe("elven_trade_1_to_multiple_test", [<item:minecraft:apple>, <item:minecraft:lapis_block>], <item:minecraft:redstone>);


// Petal Apothecary - nothing special here.
<recipetype:botania:petal_apothecary>.removeRecipe(<item:botania:hydroangeas>);
<recipetype:botania:petal_apothecary>.addRecipe("petal_apothecary_test", <item:minecraft:diamond>, <item:minecraft:dirt>);

// Runic Altar - recipes require mana, but nothing else is special here.
<recipetype:botania:runic_altar>.removeRecipe(<item:botania:rune_wrath>);
<recipetype:botania:runic_altar>.addRecipe("rune_altar_test", <item:minecraft:diamond>, 200, <item:minecraft:dirt>, <item:minecraft:apple>);

// Terrestrial Agglomeration Plate - same as above.
<recipetype:botania:terra_plate>.removeRecipe(<item:botania:terrasteel_ingot>);
<recipetype:botania:terra_plate>.addRecipe("terra_plate_test", <item:minecraft:diamond>, 2000, <item:minecraft:dirt>);


// Orechid and Orechid Ignem

// You can use the fields directly, but it is more convenient to put them into a variable.
val main = mods.botania.Orechid.main;
val nether = mods.botania.Orechid.nether;

// Clearing the full list of ores, if you want to replace them completely.
// main.clear();

// Removing an ore. If multiple outputs have the same block, all of them are removed.
main.removeOreWeight(<blockstate:minecraft:emerald_ore>);
nether.removeOreWeight(<blockstate:minecraft:nether_gold_ore>);

// Adding ores. You can use the same things as in pure daisy inputs here.
// Note that outputs added through scripts do not respect the "orechidPriorityMods" config option.
// Prefer using explicit blocks over tags.

main.registerOreWeight(<blockstate:minecraft:clay>, 150);
main.registerOreWeight(<tag:blocks:forge:ores/diamond>, 150);
nether.registerOreWeight(<tag:blocks:minecraft:coral_blocks>, 100);

// For a complete set of orechid weights, run /ct dump orechidOutputs.