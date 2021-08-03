/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.mana;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import vazkii.botania.common.block.BlockModWaterloggable;
import vazkii.botania.common.block.tile.TileSpawnerClaw;

import javax.annotation.Nonnull;

public class BlockSpawnerClaw extends BlockModWaterloggable implements EntityBlock {

	private static final VoxelShape SHAPE = box(2, 0, 2, 14, 2, 14);

	public BlockSpawnerClaw(Properties builder) {
		super(builder);
	}

	@Nonnull
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
		return SHAPE;
	}

	@Override
	public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> list) {
		super.fillItemCategory(group, list);
		list.add(new ItemStack(Blocks.SPAWNER));
	}

	@Nonnull
	@Override
	public BlockEntity newBlockEntity(BlockGetter world) {
		return new TileSpawnerClaw();
	}

}
