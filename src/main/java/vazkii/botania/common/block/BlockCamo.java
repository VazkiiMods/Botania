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
import net.minecraft.block.properties.IProperty;
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

import java.util.ArrayList;
import java.util.List;

public abstract class BlockCamo extends BlockMod {

	protected BlockCamo(Material par2Material, String name) {
		super(par2Material, name);
	}

	public static boolean isValidBlock(IBlockState state) {
		return state.isOpaqueCube() || state.getRenderType() == EnumBlockRenderType.MODEL;
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean onBlockActivated(World par1World, BlockPos pos, IBlockState state, EntityPlayer par5EntityPlayer, EnumHand hand, ItemStack currentStack, EnumFacing side, float hitX, float hitY, float hitZ) {
		TileEntity tile = par1World.getTileEntity(pos);

		if(par1World.isRemote)
			return true;

		if(currentStack != null
				&& Block.getBlockFromItem(currentStack.getItem()) != null
				&& tile instanceof TileCamo) {
			TileCamo camo = (TileCamo) tile;
			IBlockState changeState = Block.getBlockFromItem(currentStack.getItem()).onBlockPlaced(par1World, pos, side, hitX, hitY, hitZ, currentStack.getItemDamage(), par5EntityPlayer);

			if(isValidBlock(changeState) && !(changeState.getBlock() instanceof BlockCamo) && changeState.getMaterial() != Material.AIR) {
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