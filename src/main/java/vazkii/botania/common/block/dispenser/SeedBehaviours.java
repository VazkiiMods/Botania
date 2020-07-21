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
		DispenserBlock.registerBehavior(Items.WHEAT_SEEDS, new BehaviourSeeds(Blocks.WHEAT));
		DispenserBlock.registerBehavior(Items.POTATO, new BehaviourSeeds(Blocks.POTATOES));
		DispenserBlock.registerBehavior(Items.CARROT, new BehaviourSeeds(Blocks.CARROTS));
		DispenserBlock.registerBehavior(Items.NETHER_WART, new BehaviourSeeds(Blocks.NETHER_WART));
		DispenserBlock.registerBehavior(Items.PUMPKIN_SEEDS, new BehaviourSeeds(Blocks.PUMPKIN_STEM));
		DispenserBlock.registerBehavior(Items.MELON_SEEDS, new BehaviourSeeds(Blocks.MELON_STEM));
		DispenserBlock.registerBehavior(Items.BEETROOT_SEEDS, new BehaviourSeeds(Blocks.BEETROOTS));
		DispenserBlock.registerBehavior(Blocks.CHORUS_FLOWER, new BehaviourSeeds(Blocks.CHORUS_FLOWER));

		DispenserBlock.registerBehavior(Items.COCOA_BEANS, new BehaviourCocoaBeans());
	}

}
