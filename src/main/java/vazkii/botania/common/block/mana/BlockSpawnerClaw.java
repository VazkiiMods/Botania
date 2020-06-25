/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.mana;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

import vazkii.botania.common.block.BlockModWaterloggable;
import vazkii.botania.common.block.tile.TileSpawnerClaw;

import javax.annotation.Nonnull;

public class BlockSpawnerClaw extends BlockModWaterloggable implements ITileEntityProvider {

	private static final VoxelShape SHAPE = makeCuboidShape(2, 0, 2, 14, 2, 14);

	public BlockSpawnerClaw(Properties builder) {
		super(builder);
	}

	@Nonnull
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx) {
		return SHAPE;
	}

	@Override
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> list) {
		super.fillItemGroup(group, list);
		list.add(new ItemStack(Blocks.SPAWNER));
	}

	@Nonnull
	@Override
	public TileEntity createNewTileEntity(IBlockReader world) {
		return new TileSpawnerClaw();
	}

}
