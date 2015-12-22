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

	static List<Integer> validRenderTypes = Arrays.asList(0, 31, 39);

	protected BlockCamo(Material par2Material) {
		super(par2Material);
	}

	public static boolean isValidBlock(Block block) {
		return block.isOpaqueCube() || isValidRenderType(block.getRenderType());
	}

	public static boolean isValidRenderType(int type) {
		return validRenderTypes.contains(type);
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
				int metadata = currentStack.getItemDamage();
				if(block instanceof BlockDirectional) {
					switch (par6) {
					case DOWN:
					case UP:
						break;
					case NORTH:
						metadata = metadata & 12 | 2;
						break;
					case SOUTH:
						metadata = metadata & 12;
						break;
					case WEST:
						metadata = metadata & 12 | 1;
						break;
					case EAST:
						metadata = metadata & 12 | 3;
						break;
					}
				}
				camo.camo = Block.getBlockFromItem(currentStack.getItem());
				camo.camoMeta = metadata;
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
			Block block = camo.camo;
			if(block != null)
				return block instanceof BlockCamo ? 0xFFFFFF : block.colorMultiplier(par1World, pos, pass);

		}
		return 0xFFFFFF;
	}

}