/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.corporea;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.common.block.BlockMod;
import vazkii.botania.common.block.tile.corporea.TileCorporeaRetainer;

import javax.annotation.Nonnull;

public class BlockCorporeaRetainer extends BlockMod implements BlockEntityProvider, IWandable, IWandHUD {

	public BlockCorporeaRetainer(AbstractBlock.Settings builder) {
		super(builder);
		setDefaultState(getDefaultState().with(Properties.POWERED, false));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(Properties.POWERED);
	}

	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		boolean power = world.getReceivedRedstonePower(pos) > 0 || world.getReceivedRedstonePower(pos.up()) > 0;
		boolean powered = state.get(Properties.POWERED);

		if (power && !powered) {
			((TileCorporeaRetainer) world.getBlockEntity(pos)).fulfilRequest();
			world.setBlockState(pos, state.with(Properties.POWERED, true));
		} else if (!power && powered) {
			world.setBlockState(pos, state.with(Properties.POWERED, false));
		}
	}

	@Override
	public boolean hasComparatorOutput(BlockState state) {
		return true;
	}

	@Override
	public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		return ((TileCorporeaRetainer) world.getBlockEntity(pos)).getComparatorValue();
	}

	@Nonnull
	@Override
	public BlockEntity createBlockEntity(@Nonnull BlockView world) {
		return new TileCorporeaRetainer();
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void renderHUD(MatrixStack ms, MinecraftClient mc, World world, BlockPos pos) {
		BlockEntity te = world.getBlockEntity(pos);
		if (te instanceof TileCorporeaRetainer) {
			((TileCorporeaRetainer) te).renderHUD(ms, mc);
		}
	}

	@Override
	public boolean onUsedByWand(PlayerEntity player, ItemStack stack, World world, BlockPos pos, Direction side) {
		BlockEntity te = world.getBlockEntity(pos);
		return te instanceof TileCorporeaRetainer && ((TileCorporeaRetainer) te).onUsedByWand();
	}
}
