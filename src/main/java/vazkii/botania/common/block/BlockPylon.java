/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;
import vazkii.botania.common.block.tile.TilePylon;

import javax.annotation.Nonnull;

public class BlockPylon extends BlockModWaterloggable implements BlockEntityProvider {
	private static final VoxelShape SHAPE = Block.createCuboidShape(2, 0, 2, 14, 21, 14);

	public enum Variant {
		MANA,
		NATURA,
		GAIA
	}

	public final Variant variant;

	public BlockPylon(Variant v, Settings builder) {
		super(builder);
		this.variant = v;
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext ctx) {
		return SHAPE;
	}

	@Nonnull
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public float getEnchantPowerBonus(BlockState state, WorldView world, BlockPos pos) {
		if (variant == Variant.MANA) {
			return 8;
		} else {
			return 15;
		}
	}

	@Nonnull
	@Override
	public BlockEntity createBlockEntity(@Nonnull BlockView world) {
		return new TilePylon();
	}

}
