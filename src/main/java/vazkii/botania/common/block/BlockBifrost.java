/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

import vazkii.botania.common.block.tile.TileBifrost;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;

public class BlockBifrost extends BlockBifrostPerm implements BlockEntityProvider {

	public BlockBifrost(Settings builder) {
		super(builder);
	}

	@Nonnull
	@Override
	public ItemStack getPickStack(@Nonnull BlockView world, @Nonnull BlockPos pos, @Nonnull BlockState state) {
		return new ItemStack(ModItems.rainbowRod);
	}

	@Nonnull
	@Override
	public BlockEntity createBlockEntity(@Nonnull BlockView world) {
		return new TileBifrost();
	}
}
