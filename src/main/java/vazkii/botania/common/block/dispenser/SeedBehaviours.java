/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [21/09/2016, 21:06:43 (GMT)]
 */
package vazkii.botania.common.block.dispenser;

import net.minecraft.block.BlockDispenser;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class SeedBehaviours {

	// This entire class is a copy from Quark, for the seed planting feature
	// Quark's own feature disables itself if Botania is present

	public static void init() {
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.WHEAT_SEEDS, new BehaviourSeeds(Blocks.WHEAT));
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.POTATO, new BehaviourSeeds(Blocks.POTATOES));
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.CARROT, new BehaviourSeeds(Blocks.CARROTS));
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.NETHER_WART, new BehaviourSeeds(Blocks.NETHER_WART));
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.PUMPKIN_SEEDS, new BehaviourSeeds(Blocks.PUMPKIN_STEM));
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.MELON_SEEDS, new BehaviourSeeds(Blocks.MELON_STEM));
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.BEETROOT_SEEDS, new BehaviourSeeds(Blocks.BEETROOTS));
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Item.getItemFromBlock(Blocks.CHORUS_FLOWER), new BehaviourSeeds(Blocks.CHORUS_FLOWER));

		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(Items.DYE, new BehaviourCocoaBeans(BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.getObject(Items.DYE)));
	}

}
