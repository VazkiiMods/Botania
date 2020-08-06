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
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;

import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TilePylon;

import javax.annotation.Nonnull;

public class BlockPylon extends BlockModWaterloggable implements ITileEntityProvider {
	private static final VoxelShape SHAPE = Block.makeCuboidShape(2, 0, 2, 14, 21, 14);

	public enum Variant {
		MANA(8f, 0.5f, 0.5f, 1f, ModBlocks.enchanter),
		NATURA(15f, 0.5f, 1f, 0.5f, ModBlocks.alfPortal),
		GAIA(15f, 1f, 0.5f, 1f, ModBlocks.alfPortal);

		public final float powerBonus;
		public final float r, g, b;
		public final Block metaBlock;

		private Variant(float pb, float r, float g, float b, Block mb) {
			powerBonus = pb;
			this.r = r;
			this.g = g;
			this.b = b;
			metaBlock = mb;
		}
	}

	public final Variant variant;

	public BlockPylon(@Nonnull Variant v, Properties builder) {
		super(builder);
		this.variant = v;
	}

	@Nonnull
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx) {
		return SHAPE;
	}

	@Nonnull
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public float getEnchantPowerBonus(BlockState state, IWorldReader world, BlockPos pos) {
		return variant.powerBonus;
	}

	@Nonnull
	@Override
	public TileEntity createNewTileEntity(@Nonnull IBlockReader world) {
		return new TilePylon();
	}

}
