/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Sep 1, 2015, 5:32:15 PM (GMT)]
 *//*

package vazkii.botania.common.integration.multipart;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModFluffBlocks;
import codechicken.microblock.BlockMicroMaterial;
import codechicken.microblock.MicroMaterialRegistry;

public class MultipartHandler {

	public MultipartHandler() {
		registerMultiparts(ModBlocks.livingrock, 0, 4);
		registerMultiparts(ModBlocks.livingwood, 0, 5);
		registerMultiparts(ModBlocks.storage, 0, 4);
		registerMultiparts(ModBlocks.dreamwood, 0, 4);
		registerMultiparts(ModBlocks.prismarine, 0, 2);
		registerMultiparts(ModBlocks.seaLamp);
		registerMultiparts(ModBlocks.reedBlock);
		registerMultiparts(ModBlocks.thatch);
		registerMultiparts(ModBlocks.customBrick, 0, 15);
		registerMultiparts(ModBlocks.elfGlass);
		registerMultiparts(ModBlocks.manaGlass);
		registerMultiparts(ModBlocks.endStoneBrick, 0, 3);
		registerMultiparts(ModBlocks.blazeBlock);
		registerMultiparts(ModBlocks.bifrostPerm);
		registerMultiparts(ModBlocks.shimmerrock);
		registerMultiparts(ModBlocks.shimmerwoodPlanks);

		registerMultiparts(ModFluffBlocks.darkQuartz, 0, 2);
		registerMultiparts(ModFluffBlocks.manaQuartz, 0, 2);
		registerMultiparts(ModFluffBlocks.blazeQuartz, 0, 2);
		registerMultiparts(ModFluffBlocks.lavenderQuartz, 0, 2);
		registerMultiparts(ModFluffBlocks.redQuartz, 0, 2);
		registerMultiparts(ModFluffBlocks.elfQuartz, 0, 2);
		registerMultiparts(ModFluffBlocks.sunnyQuartz, 0, 2);
		registerMultiparts(ModFluffBlocks.biomeStoneA, 0, 15);
		registerMultiparts(ModFluffBlocks.biomeStoneB, 0, 15);
		registerMultiparts(ModFluffBlocks.stone, 0, 15);
		registerMultiparts(ModFluffBlocks.pavement, 0, 5);
	}

	private static void registerMultiparts(Block block) {
		registerMultiparts(block, 0);
	}

	private static void registerMultiparts(Block block, int meta) {
		MicroMaterialRegistry.registerMaterial(new BlockMicroMaterial(block, meta), block.getUnlocalizedName() + (meta == 0 ? "" : "_" + meta));
	}

	private static void registerMultiparts(Block block, int metamin, int metamax) {
		for (int i = metamin; i <= metamax; i++) {
			registerMultiparts(block, i);
		}
	}

}*/
