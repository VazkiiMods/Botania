/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Mar 16, 2015, 6:03:16 PM (GMT)]
 */
package vazkii.botania.common.block.subtile.generating;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import vazkii.botania.api.subtile.SubTileGenerating;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.helper.ItemNBTHelper;

public class SubTilePassiveGenerating extends SubTileGenerating {

	@Override
	public boolean isPassiveFlower() {
		return true;
	}

	@Override
	public ArrayList<ItemStack> getDrops(ArrayList<ItemStack> list) {
		ArrayList<ItemStack> drops = super.getDrops(list);
		populateDropStackNBTs(drops);
		return drops;
	}

	public void populateDropStackNBTs(List<ItemStack> drops) {
		if(isPassiveFlower() && ticksExisted > 0 && ConfigHandler.hardcorePassiveGeneration > 0)
			ItemNBTHelper.setInt(drops.get(0), TAG_TICKS_EXISTED, ticksExisted);
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
		super.onBlockPlacedBy(world, x, y, z, entity, stack);
		ticksExisted = ItemNBTHelper.getInt(stack, TAG_TICKS_EXISTED, 0);
	}

}
