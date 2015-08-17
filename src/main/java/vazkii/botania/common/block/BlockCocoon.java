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

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.client.lib.LibRenderIDs;
import vazkii.botania.common.block.tile.TileCocoon;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;

public class BlockCocoon extends BlockModContainer implements ILexiconable {

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
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int s, float xs, float ys, float zs) {
		TileCocoon cocoon = (TileCocoon) world.getTileEntity(x, y, z);
		ItemStack item = player.getCurrentEquippedItem();
		if(cocoon.emeraldsGiven < TileCocoon.MAX_EMERALDS && item != null && item.getItem() == Items.emerald) {
			if(!player.capabilities.isCreativeMode)
				item.stackSize--;
			cocoon.emeraldsGiven++;
			world.playAuxSFX(2005, x, y, z, 6 + world.rand.nextInt(4));
		}
		return false;
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

	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.cocoon;
	}

}
