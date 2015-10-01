/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Sep 1, 2015, 5:32:15 PM (GMT)]
 */
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
		registerAllMultiparts(ModBlocks.livingrock);
		registerAllMultiparts(ModBlocks.livingwood);
		registerAllMultiparts(ModBlocks.storage);
		registerAllMultiparts(ModBlocks.dreamwood);
		registerAllMultiparts(ModBlocks.livingrock);
		registerAllMultiparts(ModBlocks.prismarine);
		registerAllMultiparts(ModBlocks.seaLamp);
		registerAllMultiparts(ModBlocks.reedBlock);
		registerAllMultiparts(ModBlocks.thatch);
		registerAllMultiparts(ModBlocks.customBrick);
		registerAllMultiparts(ModBlocks.elfGlass);
		registerAllMultiparts(ModBlocks.manaGlass);
		registerAllMultiparts(ModBlocks.endStoneBrick);
		registerAllMultiparts(ModBlocks.blazeBlock);
		registerAllMultiparts(ModBlocks.livingrock);
		registerAllMultiparts(ModBlocks.bifrostPerm);

		registerAllMultiparts(ModFluffBlocks.darkQuartz);
		registerAllMultiparts(ModFluffBlocks.manaQuartz);
		registerAllMultiparts(ModFluffBlocks.blazeQuartz);
		registerAllMultiparts(ModFluffBlocks.lavenderQuartz);
		registerAllMultiparts(ModFluffBlocks.redQuartz);
		registerAllMultiparts(ModFluffBlocks.elfQuartz);
		registerAllMultiparts(ModFluffBlocks.sunnyQuartz);
		registerAllMultiparts(ModFluffBlocks.biomeStoneA);
		registerAllMultiparts(ModFluffBlocks.biomeStoneB);
		registerAllMultiparts(ModFluffBlocks.stone);
		registerAllMultiparts(ModFluffBlocks.pavement);
	}

	private static void registerAllMultiparts(Block block) {
		List<ItemStack> stacks = new ArrayList();
		Item item = Item.getItemFromBlock(block);
		block.getSubBlocks(item, block.getCreativeTabToDisplayOn(), stacks);

		for(ItemStack stack : stacks)
			if(stack.getItem() == item)
				registerMultipart(block, stack.getItemDamage());
	}

	private static void registerMultipart(Block block, int meta) {
		MicroMaterialRegistry.registerMaterial(new BlockMicroMaterial(block, meta), block.getUnlocalizedName() + (meta == 0 ? "" : "_" + meta));
	}

}