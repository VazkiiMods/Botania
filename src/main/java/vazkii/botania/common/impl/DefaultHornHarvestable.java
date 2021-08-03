/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.impl;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import vazkii.botania.api.item.IHornHarvestable;

public class DefaultHornHarvestable implements IHornHarvestable {
	public static final IHornHarvestable INSTANCE = new DefaultHornHarvestable();

	@Override
	public boolean canHornHarvest(Level world, BlockPos pos, ItemStack stack, EnumHornType hornType) {
		return false;
	}

	@Override
	public boolean hasSpecialHornHarvest(Level world, BlockPos pos, ItemStack stack, EnumHornType hornType) {
		return false;
	}

	@Override
	public void harvestByHorn(Level world, BlockPos pos, ItemStack stack, EnumHornType hornType) {}
}
