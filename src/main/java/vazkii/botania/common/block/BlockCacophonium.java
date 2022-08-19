/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Aug 23, 2015, 7:23:26 PM (GMT)]
 */
package vazkii.botania.common.block;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.block.tile.TileCacophonium;
import vazkii.botania.common.lib.LibBlockNames;

public class BlockCacophonium extends BlockModContainer {

	IIcon top;

	protected BlockCacophonium() {
		super(Material.wood);
		setBlockName(LibBlockNames.CACOPHONIUM);
		setHardness(0.8F);
	}

	@Override
	public void registerBlockIcons(IIconRegister reg) {
		blockIcon = IconHelper.forBlock(reg, this, 0);
		top = IconHelper.forBlock(reg, this, 1);
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		return side == 1 ? blockIcon : top;
	}

	@Override
	protected boolean canSilkHarvest() {
		return false;
	}

	@Override
	public boolean registerInCreative() {
		return false;
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
		boolean power = world.isBlockIndirectlyGettingPowered(x, y, z) || world.isBlockIndirectlyGettingPowered(x, y + 1, z);
		int meta = world.getBlockMetadata(x, y, z);
		boolean powered = (meta & 8) != 0;

		if(power && !powered) {
			TileEntity tile = world.getTileEntity(x, y, z);
			if(tile != null && tile instanceof TileCacophonium)
				((TileCacophonium) tile).annoyDirewolf();
			world.setBlockMetadataWithNotify(x, y, z, meta | 8, 4);
		} else if(!power && powered)
			world.setBlockMetadataWithNotify(x, y, z, meta & -9, 4);
	}

	@Override
	public void onBlockHarvested(World par1World, int par2, int par3, int par4, int par5, EntityPlayer par6EntityPlayer) {
		if(!par6EntityPlayer.capabilities.isCreativeMode)
			dropBlockAsItem(par1World, par2, par3, par4, par5, 0);
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
		ArrayList<ItemStack> stacks = new ArrayList();

		TileEntity tile = world.getTileEntity(x, y, z);
		if(tile != null && tile instanceof TileCacophonium) {
			stacks.add(new ItemStack(Blocks.noteblock));
			ItemStack thingy = ((TileCacophonium) tile).stack;
			if(thingy != null)
				stacks.add(thingy.copy());
		}

		return stacks;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileCacophonium();
	}

}
