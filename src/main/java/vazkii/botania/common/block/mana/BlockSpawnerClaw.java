/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.mana;

import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

import vazkii.botania.common.block.BlockModWaterloggable;
import vazkii.botania.common.block.tile.TileSpawnerClaw;

import javax.annotation.Nonnull;

public class BlockSpawnerClaw extends BlockModWaterloggable implements BlockEntityProvider {

	private static final VoxelShape SHAPE = createCuboidShape(2, 0, 2, 14, 2, 14);

	public BlockSpawnerClaw(Settings builder) {
		super(builder);
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext ctx) {
		return SHAPE;
	}

	@Override
	public void addStacksForDisplay(ItemGroup group, DefaultedList<ItemStack> list) {
		super.addStacksForDisplay(group, list);
		list.add(new ItemStack(Blocks.SPAWNER));
	}

	@Nonnull
	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new TileSpawnerClaw();
	}

}
