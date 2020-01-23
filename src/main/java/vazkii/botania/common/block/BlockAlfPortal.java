/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jun 9, 2014, 7:17:46 PM (GMT)]
 */
package vazkii.botania.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.AlfPortalState;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.common.advancements.AlfPortalTrigger;
import vazkii.botania.common.block.tile.TileAlfPortal;

import javax.annotation.Nonnull;

public class BlockAlfPortal extends BlockMod implements IWandable {

	public BlockAlfPortal(Properties builder) {
		super(builder);
		setDefaultState(stateContainer.getBaseState().with(BotaniaStateProps.ALFPORTAL_STATE, AlfPortalState.OFF));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(BotaniaStateProps.ALFPORTAL_STATE);
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull BlockState state, @Nonnull IBlockReader world) {
		return new TileAlfPortal();
	}

	@Override
	public boolean onUsedByWand(PlayerEntity player, ItemStack stack, World world, BlockPos pos, Direction side) {
		boolean did = ((TileAlfPortal) world.getTileEntity(pos)).onWanded();
		if(!world.isRemote && did && player instanceof ServerPlayerEntity) {
			AlfPortalTrigger.INSTANCE.trigger((ServerPlayerEntity) player, (ServerWorld) world, pos, stack);
		}
		return did;
	}

	@Override
	public int getLightValue(@Nonnull BlockState state) {
		return state.get(BotaniaStateProps.ALFPORTAL_STATE) != AlfPortalState.OFF ? 15 : super.getLightValue(state);
	}

}
