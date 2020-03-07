/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.decor;

import net.minecraft.block.BlockState;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import vazkii.botania.api.item.IHornHarvestable;
import vazkii.botania.common.block.BlockModFlower;

import javax.annotation.Nonnull;

public class BlockShinyFlower extends BlockModFlower implements IHornHarvestable {

	public BlockShinyFlower(DyeColor color, Properties builder) {
		super(color, builder);
	}

	@Override
	public boolean canGrow(@Nonnull IBlockReader world, @Nonnull BlockPos pos, @Nonnull BlockState state, boolean fuckifiknow) {
		return false;
	}

	@Override
	public boolean canHornHarvest(World world, BlockPos pos, ItemStack stack, EnumHornType hornType) {
		return false;
	}

	@Override
	public boolean hasSpecialHornHarvest(World world, BlockPos pos, ItemStack stack, EnumHornType hornType) {
		return false;
	}

	@Override
	public void harvestByHorn(World world, BlockPos pos, ItemStack stack, EnumHornType hornType) {}

}
