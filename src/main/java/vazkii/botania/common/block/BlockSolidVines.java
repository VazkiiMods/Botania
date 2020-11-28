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
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

import vazkii.botania.mixin.AccessorFireBlock;

import javax.annotation.Nonnull;

import java.util.Random;

public class BlockSolidVines extends VineBlock {

	public BlockSolidVines(Settings builder) {
		super(builder);
		((AccessorFireBlock) Blocks.FIRE).botania_register(this, 15, 100);
	}

	@Override
	public void scheduledTick(@Nonnull BlockState state, ServerWorld world, @Nonnull BlockPos pos, @Nonnull Random rand) {}

	@Nonnull
	@Override
	public ItemStack getPickStack(@Nonnull BlockView world, @Nonnull BlockPos pos, @Nonnull BlockState state) {
		return new ItemStack(Blocks.VINE);
	}
}
