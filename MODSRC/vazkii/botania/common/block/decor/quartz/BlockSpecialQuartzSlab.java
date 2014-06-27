/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
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
import vazkii.botania.common.block.ModBlocks;
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
		if(source == ModBlocks.darkQuartz)
			return (BlockSlab) ModBlocks.darkQuartzSlabFull;
		if(source == ModBlocks.manaQuartz)
			return (BlockSlab) ModBlocks.manaQuartzSlabFull;
		if(source == ModBlocks.blazeQuartz)
			return (BlockSlab) ModBlocks.blazeQuartzSlabFull;
		if(source == ModBlocks.lavenderQuartz)
			return (BlockSlab) ModBlocks.lavenderQuartzSlabFull;
		if(source == ModBlocks.redQuartz)
			return (BlockSlab) ModBlocks.redQuartzSlabFull;
		if(source == ModBlocks.elfQuartz)
			return (BlockSlab) ModBlocks.elfQuartzSlabFull;

		return this;
	}

	@Override
	public BlockSlab getSingleBlock() {
		if(source == ModBlocks.darkQuartz)
			return (BlockSlab) ModBlocks.darkQuartzSlab;
		if(source == ModBlocks.manaQuartz)
			return (BlockSlab) ModBlocks.manaQuartzSlab;
		if(source == ModBlocks.blazeQuartz)
			return (BlockSlab) ModBlocks.blazeQuartzSlab;
		if(source == ModBlocks.lavenderQuartz)
			return (BlockSlab) ModBlocks.lavenderQuartzSlab;
		if(source == ModBlocks.redQuartz)
			return (BlockSlab) ModBlocks.redQuartzSlab;
		if(source == ModBlocks.elfQuartz)
			return (BlockSlab) ModBlocks.elfQuartzSlab;

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
		return this == ModBlocks.elfQuartzSlab ? LexiconData.elvenResources : LexiconData.decorativeBlocks;
	}

}
