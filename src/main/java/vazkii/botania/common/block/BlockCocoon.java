/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jul 8, 2015, 4:29:01 PM (GMT)]
 */
package vazkii.botania.common.block;

import java.util.ArrayList;
import java.util.Random;

import vazkii.botania.client.lib.LibRenderIDs;
import vazkii.botania.common.block.tile.TileCocoon;
import vazkii.botania.common.lib.LibBlockNames;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockCocoon extends BlockModContainer {

	protected BlockCocoon() {
		super(Material.cloth);
		setHardness(3.0F);
		setResistance(50.0F);
		setStepSound(soundTypeCloth);
		setBlockName(LibBlockNames.COCOON);
		float f = 3F / 16F;
		float f1 = 14F / 16F;
		setBlockBounds(f, 0F, f, 1F - f, f1, 1F - f);
	}
	
	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		// NO-OP
	}
	
	@Override
	public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
		return Blocks.web.getBlockTextureFromSide(0);
	}
	
	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
	public int getRenderType() {
		return LibRenderIDs.idCocoon;
	}
	
	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
		return new ArrayList();
	}
	
	@Override
	protected boolean canSilkHarvest() {
		return false;
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileCocoon();
	}

}
