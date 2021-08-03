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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.common.block.tile.TileBifrost;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;

public class BlockBifrost extends BlockBifrostPerm implements EntityBlock {

	public BlockBifrost(Properties builder) {
		super(builder);
	}

	@Nonnull
	@Override
	public ItemStack getCloneItemStack(@Nonnull BlockGetter world, @Nonnull BlockPos pos, @Nonnull BlockState state) {
		return new ItemStack(ModItems.rainbowRod);
	}

	@Nonnull
	@Override
	public BlockEntity newBlockEntity(@Nonnull BlockGetter world) {
		return new TileBifrost();
	}
}
