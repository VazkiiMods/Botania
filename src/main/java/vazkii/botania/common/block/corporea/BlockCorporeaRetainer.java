/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.corporea;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.common.block.BlockMod;
import vazkii.botania.common.block.tile.corporea.TileCorporeaRetainer;

import javax.annotation.Nonnull;

public class BlockCorporeaRetainer extends BlockMod implements ITileEntityProvider, IWandable, IWandHUD {

	public BlockCorporeaRetainer(AbstractBlock.Properties builder) {
		super(builder);
		setDefaultState(getDefaultState().with(BlockStateProperties.POWERED, false));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.POWERED);
	}

	@Override
	public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		boolean power = world.getRedstonePowerFromNeighbors(pos) > 0 || world.getRedstonePowerFromNeighbors(pos.up()) > 0;
		boolean powered = state.get(BlockStateProperties.POWERED);

		if (power && !powered) {
			((TileCorporeaRetainer) world.getTileEntity(pos)).fulfilRequest();
			world.setBlockState(pos, state.with(BlockStateProperties.POWERED, true));
		} else if (!power && powered) {
			world.setBlockState(pos, state.with(BlockStateProperties.POWERED, false));
		}
	}

	@Override
	public boolean hasComparatorInputOverride(BlockState state) {
		return true;
	}

	@Override
	public int getComparatorInputOverride(BlockState state, World world, BlockPos pos) {
		return ((TileCorporeaRetainer) world.getTileEntity(pos)).getComparatorValue();
	}

	@Nonnull
	@Override
	public TileEntity createNewTileEntity(@Nonnull IBlockReader world) {
		return new TileCorporeaRetainer();
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void renderHUD(MatrixStack ms, Minecraft mc, World world, BlockPos pos) {
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof TileCorporeaRetainer) {
			((TileCorporeaRetainer) te).renderHUD(ms, mc);
		}
	}

	@Override
	public boolean onUsedByWand(PlayerEntity player, ItemStack stack, World world, BlockPos pos, Direction side) {
		TileEntity te = world.getTileEntity(pos);
		return te instanceof TileCorporeaRetainer && ((TileCorporeaRetainer) te).onUsedByWand();
	}
}
