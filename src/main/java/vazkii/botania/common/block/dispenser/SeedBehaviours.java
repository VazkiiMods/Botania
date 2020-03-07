/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.dispenser;

import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.item.Items;

public class SeedBehaviours {

	// This entire class is a copy from Quark, for the seed planting feature
	// Quark's own feature disables itself if Botania is present

	public static void init() {
		DispenserBlock.registerDispenseBehavior(Items.WHEAT_SEEDS, new BehaviourSeeds(Blocks.WHEAT));
		DispenserBlock.registerDispenseBehavior(Items.POTATO, new BehaviourSeeds(Blocks.POTATOES));
		DispenserBlock.registerDispenseBehavior(Items.CARROT, new BehaviourSeeds(Blocks.CARROTS));
		DispenserBlock.registerDispenseBehavior(Items.NETHER_WART, new BehaviourSeeds(Blocks.NETHER_WART));
		DispenserBlock.registerDispenseBehavior(Items.PUMPKIN_SEEDS, new BehaviourSeeds(Blocks.PUMPKIN_STEM));
		DispenserBlock.registerDispenseBehavior(Items.MELON_SEEDS, new BehaviourSeeds(Blocks.MELON_STEM));
		DispenserBlock.registerDispenseBehavior(Items.BEETROOT_SEEDS, new BehaviourSeeds(Blocks.BEETROOTS));
		DispenserBlock.registerDispenseBehavior(Blocks.CHORUS_FLOWER, new BehaviourSeeds(Blocks.CHORUS_FLOWER));

		DispenserBlock.registerDispenseBehavior(Items.COCOA_BEANS, new BehaviourCocoaBeans());
	}

}
