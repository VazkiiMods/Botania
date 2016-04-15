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
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.botania.common.block.tile.TileCamo;

public abstract class BlockCamo extends BlockMod {

	protected BlockCamo(Material par2Material, String name) {
		super(par2Material, name);
	}

	public static boolean isValidBlock(IBlockState state) {
		return state.isOpaqueCube() || state.getRenderType() == EnumBlockRenderType.MODEL;
	}

	@Override
	public boolean onBlockActivated(World par1World, BlockPos pos, IBlockState state, EntityPlayer par5EntityPlayer, EnumHand hand, ItemStack currentStack, EnumFacing par6, float par7, float par8, float par9) {
		TileEntity tile = par1World.getTileEntity(pos);

		if(tile instanceof TileCamo) {
			TileCamo camo = (TileCamo) tile;

			if(currentStack == null)
				currentStack = new ItemStack(Block.getBlockFromName("air"), 1, 0);

			boolean doChange = true;
			IBlockState testState = null;
			checkChange : {
				if(currentStack.getItem() != Item.getItemFromBlock(Block.getBlockFromName("air"))) {
					if(Item.getIdFromItem(currentStack.getItem()) == 0) {
						doChange = false;
						break checkChange;
					}

					testState = Block.getBlockFromItem(currentStack.getItem()).getStateFromMeta(currentStack.getItemDamage());
					if(testState == null || !isValidBlock(testState) || testState.getBlock() instanceof BlockCamo || testState.getMaterial() == Material.air)
						doChange = false;
				}
			}

			if(doChange && currentStack.getItem() != null) {
				IBlockState changeState = Block.getBlockFromItem(currentStack.getItem()).getStateFromMeta(currentStack.getItemDamage());
				if(testState.getBlock() instanceof BlockDirectional) {
					if (BlockDirectional.FACING.getAllowedValues().contains(par6.getOpposite())) {
						changeState = changeState.withProperty(BlockDirectional.FACING, par6.getOpposite());
					}
				}
				camo.camoState = changeState;
				par1World.notifyBlockUpdate(pos, state, state, 8);

				return true;
			}
		}

		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

}