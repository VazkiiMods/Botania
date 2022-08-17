/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.mixin.AccessorFireBlock;

public class BlockSolidVines extends VineBlock {

	public BlockSolidVines(Properties builder) {
		super(builder);
		((AccessorFireBlock) Blocks.FIRE).botania_register(this, 15, 100);
	}

	@Override
	public void tick(@NotNull BlockState state, ServerLevel world, @NotNull BlockPos pos, @NotNull RandomSource rand) {}

	@NotNull
	@Override
	public ItemStack getCloneItemStack(@NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull BlockState state) {
		return new ItemStack(Blocks.VINE);
	}
}
