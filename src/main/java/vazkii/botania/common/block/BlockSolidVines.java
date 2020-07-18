/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.VineBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.server.ServerWorld;

import vazkii.botania.mixin.AccessorFireBlock;

import javax.annotation.Nonnull;

import java.util.Random;

public class BlockSolidVines extends VineBlock {

	public BlockSolidVines(Properties builder) {
		super(builder);
		((AccessorFireBlock) Blocks.FIRE).callSetFireInfo(this, 15, 100);
	}

	@Override
	public void tick(@Nonnull BlockState state, ServerWorld world, @Nonnull BlockPos pos, @Nonnull Random rand) {}

	@Nonnull
	@Override
	public ItemStack getItem(@Nonnull IBlockReader world, @Nonnull BlockPos pos, @Nonnull BlockState state) {
		return new ItemStack(Blocks.VINE);
	}
}
