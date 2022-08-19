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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import vazkii.botania.common.block.tile.TileCamo;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class BlockCamo extends BlockModContainer<TileCamo> {

	static List<Integer> validRenderTypes = Arrays.asList(0, 31, 39);

	protected BlockCamo(Material par2Material) {
		super(par2Material);
	}

	@Override
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
		TileEntity tile = world.getTileEntity(x, y, z);
		int meta = world.getBlockMetadata(x, y, z);

		if(tile instanceof TileCamo) {
			TileCamo camo = (TileCamo) tile;
			Block block = camo.camo;
			if(block != null && isValidBlock(block))
				return block.getIcon(side, camo.camoMeta);
		}

		return getIconFromSideAfterCheck(tile, meta, side);
	}

	public static boolean isValidBlock(Block block) {
		return block.isOpaqueCube() || isValidRenderType(block.getRenderType());
	}

	public static boolean isValidRenderType(int type) {
		return validRenderTypes.contains(type);
	}

	@Override
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
		TileEntity tile = par1World.getTileEntity(par2, par3, par4);

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
					case 0:
					case 1:
						break;
					case 2:
						metadata = metadata & 12 | 2;
						break;
					case 3:
						metadata = metadata & 12;
						break;
					case 4:
						metadata = metadata & 12 | 1;
						break;
					case 5:
						metadata = metadata & 12 | 3;
						break;
					}
				}
				camo.camo = Block.getBlockFromItem(currentStack.getItem());
				camo.camoMeta = metadata;
				par1World.markBlockForUpdate(par2,par3,par4);

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
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int colorMultiplier(IBlockAccess par1World, int par2, int par3, int par4) {
		TileEntity tile = par1World.getTileEntity(par2, par3, par4);
		if(tile instanceof TileCamo) {
			TileCamo camo = (TileCamo) tile;
			Block block = camo.camo;
			if(block != null)
				return block instanceof BlockCamo ? 0xFFFFFF : block.getRenderColor(camo.camoMeta);

		}
		return 0xFFFFFF;
	}

	public IIcon getIconFromSideAfterCheck(TileEntity tile, int meta, int side) {
		return getIcon(side, meta);
	}

}