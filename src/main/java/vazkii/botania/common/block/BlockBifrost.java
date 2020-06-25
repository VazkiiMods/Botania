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
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockReader;

import vazkii.botania.common.block.tile.TileBifrost;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;

public class BlockBifrost extends BlockBifrostPerm implements ITileEntityProvider {

	public BlockBifrost(Properties builder) {
		super(builder);
	}

	@Nonnull
	@Override
	public ItemStack getItem(@Nonnull IBlockReader world, @Nonnull BlockPos pos, @Nonnull BlockState state) {
		return new ItemStack(ModItems.rainbowRod);
	}

	@Nonnull
	@Override
	public TileEntity createNewTileEntity(@Nonnull IBlockReader world) {
		return new TileBifrost();
	}
}
