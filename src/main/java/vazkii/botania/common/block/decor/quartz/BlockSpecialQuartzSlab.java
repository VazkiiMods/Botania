/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jun 11, 2014, 1:08:28 AM (GMT)]
 */
package vazkii.botania.common.block.decor.quartz;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.block.ModFluffBlocks;
import vazkii.botania.common.block.decor.slabs.BlockModSlab;
import vazkii.botania.common.lexicon.LexiconData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockSpecialQuartzSlab extends BlockModSlab {

	Block source;

	public BlockSpecialQuartzSlab(Block source, boolean par2) {
		super(par2, Material.rock, "quartzSlab" + ((BlockSpecialQuartz) source).type + (par2 ? "Full" : "Half"));
		setHardness(0.8F);
		setResistance(10F);
		this.source = source;
	}

	@Override
	public BlockSlab getFullBlock() {
		if(source == ModFluffBlocks.darkQuartz)
			return (BlockSlab) ModFluffBlocks.darkQuartzSlabFull;
		if(source == ModFluffBlocks.manaQuartz)
			return (BlockSlab) ModFluffBlocks.manaQuartzSlabFull;
		if(source == ModFluffBlocks.blazeQuartz)
			return (BlockSlab) ModFluffBlocks.blazeQuartzSlabFull;
		if(source == ModFluffBlocks.lavenderQuartz)
			return (BlockSlab) ModFluffBlocks.lavenderQuartzSlabFull;
		if(source == ModFluffBlocks.redQuartz)
			return (BlockSlab) ModFluffBlocks.redQuartzSlabFull;
		if(source == ModFluffBlocks.elfQuartz)
			return (BlockSlab) ModFluffBlocks.elfQuartzSlabFull;
		if(source == ModFluffBlocks.sunnyQuartz)
			return (BlockSlab) ModFluffBlocks.sunnyQuartzSlabFull;

		return this;
	}

	@Override
	public BlockSlab getSingleBlock() {
		if(source == ModFluffBlocks.darkQuartz)
			return (BlockSlab) ModFluffBlocks.darkQuartzSlab;
		if(source == ModFluffBlocks.manaQuartz)
			return (BlockSlab) ModFluffBlocks.manaQuartzSlab;
		if(source == ModFluffBlocks.blazeQuartz)
			return (BlockSlab) ModFluffBlocks.blazeQuartzSlab;
		if(source == ModFluffBlocks.lavenderQuartz)
			return (BlockSlab) ModFluffBlocks.lavenderQuartzSlab;
		if(source == ModFluffBlocks.redQuartz)
			return (BlockSlab) ModFluffBlocks.redQuartzSlab;
		if(source == ModFluffBlocks.elfQuartz)
			return (BlockSlab) ModFluffBlocks.elfQuartzSlab;
		if(source == ModFluffBlocks.sunnyQuartz)
			return (BlockSlab) ModFluffBlocks.sunnyQuartzSlab;

		return this;
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
		return new ItemStack(getSingleBlock());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int par1, int par2) {
		return source.getBlockTextureFromSide(par1);
	}

	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
		return Item.getItemFromBlock(getSingleBlock());
	}

	@Override
	public ItemStack createStackedBlock(int par1) {
		return new ItemStack(getSingleBlock());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		// NO-OP
	}

	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
		return this == ModFluffBlocks.elfQuartzSlab ? LexiconData.elvenResources : LexiconData.decorativeBlocks;
	}

}
