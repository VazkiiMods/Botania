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
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.block.HornHarvestable;

public class DefaultHornHarvestable implements HornHarvestable {
	public static final HornHarvestable INSTANCE = new DefaultHornHarvestable();

	@Override
	public boolean canHornHarvest(Level world, BlockPos pos, ItemStack stack, EnumHornType hornType, @Nullable LivingEntity living) {
		return false;
	}
}
