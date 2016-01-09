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

import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import vazkii.botania.common.block.tile.TileCamo;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class BlockCamo extends BlockModContainer<TileCamo> {

	protected BlockCamo(Material par2Material) {
		super(par2Material);
	}

	public static boolean isValidBlock(Block block) {
		return block.isOpaqueCube() || block.getRenderType() == 3;
	}

	@Override
	public boolean onBlockActivated(World par1World, BlockPos pos, IBlockState state, EntityPlayer par5EntityPlayer, EnumFacing par6, float par7, float par8, float par9) {
		TileEntity tile = par1World.getTileEntity(pos);

		if(tile instanceof TileCamo) {
			TileCamo camo = (TileCamo) tile;
			ItemStack currentStack = par5EntityPlayer.getCurrentEquippedItem();

			if(currentStack == null)
				currentStack = new ItemStack(Block.getBlockFromName("air"), 1, 0);

			boolean doChange = true;
			Block block = null;
			checkChange : {
				if(currentStack.getItem() != Item.getItemFromBlock(Block.getBlockFromName("air"))) {
					if(Item.getIdFromItem(currentStack.getItem()) == 0) {
						doChange = false;
						break checkChange;
					}

					block = Block.getBlockFromItem(currentStack.getItem());
					if(block == null || !isValidBlock(block) || block instanceof BlockCamo || block.getMaterial() == Material.air)
						doChange = false;
				}
			}

			if(doChange && currentStack.getItem() != null) {
				IBlockState changeState = Block.getBlockFromItem(currentStack.getItem()).getStateFromMeta(currentStack.getItemDamage());
				if(block instanceof BlockDirectional) {
					if (BlockDirectional.FACING.getAllowedValues().contains(par6.getOpposite())) {
						changeState = changeState.withProperty(BlockDirectional.FACING, par6.getOpposite());
					}
				}
				camo.camoState = changeState;
				par1World.markBlockForUpdate(pos);

				return true;
			}
		}

		return false;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean isFullCube() {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockAccess par1World, BlockPos pos, int pass) {
		TileEntity tile = par1World.getTileEntity(pos);
		if(tile instanceof TileCamo) {
			TileCamo camo = (TileCamo) tile;
			IBlockState state = camo.camoState;
			if(state != null)
				return state.getBlock() instanceof BlockCamo ? 0xFFFFFF : state.getBlock().getRenderColor(state);
		}
		return 0xFFFFFF;
	}

}