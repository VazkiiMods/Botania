/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jun 7, 2014, 2:20:51 PM (GMT)]
 */
package vazkii.botania.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.BlockRenderType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.botania.common.block.tile.TileCamo;

public abstract class BlockCamo extends BlockMod {

	protected BlockCamo(Properties builder) {
		super(builder);
	}

	public static boolean isValidBlock(BlockState state, World world, BlockPos pos) {
		return state.isOpaqueCube(world, pos) || state.getRenderType() == BlockRenderType.MODEL;
	}

	@Override
	public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, Direction side, float hitX, float hitY, float hitZ) {
		TileEntity tile = world.getTileEntity(pos);

		if(world.isRemote)
			return true;

		ItemStack currentStack = player.getHeldItem(hand);
		if(!currentStack.isEmpty()
				&& Block.getBlockFromItem(currentStack.getItem()) != null
				&& tile instanceof TileCamo) {
			TileCamo camo = (TileCamo) tile;
			BlockItemUseContext ctx = new BlockItemUseContext(world, player, currentStack, pos, side, 0, 0, 0);
			BlockState changeState = Block.getBlockFromItem(currentStack.getItem()).getStateForPlacement(ctx);

			if(isValidBlock(changeState, world, pos) && !(changeState.getBlock() instanceof BlockCamo) && changeState.getMaterial() != Material.AIR) {
				camo.camoState = changeState;
				world.notifyBlockUpdate(pos, state, state, 8);

				return true;
			}
		}

		return false;
	}

	@Override
	public boolean isFullCube(BlockState state) {
		return false;
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

}