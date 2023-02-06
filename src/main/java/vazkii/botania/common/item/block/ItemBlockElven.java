/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Sep 9, 2014, 5:02:06 PM (GMT)]
 */
package vazkii.botania.common.item.block;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import vazkii.botania.api.recipe.IElvenItem;
import vazkii.botania.common.core.handler.ConfigHandler;

import java.util.List;

public class ItemBlockElven extends ItemBlockMod implements IElvenItem {

	public ItemBlockElven(Block block) {
		super(block);
	}

	@Override
	public boolean isElvenItem(ItemStack stack) {
		return ((IElvenItem) field_150939_a).isElvenItem(stack);
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advancedTooltips) {
		if (ConfigHandler.noMobSpawnOnBlocks)
			list.add(StatCollector.translateToLocal("nomobspawnsonthisblock.tip"));
	}
}
