/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Feb 4, 2014, 6:00:44 PM (GMT)]
 */
package vazkii.botania.common.block.decor;

import java.awt.Color;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.BlockMod;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.item.block.ItemBlockWithMetadataAndName;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;
import cpw.mods.fml.common.registry.GameRegistry;

public class BlockUnstable extends BlockMod implements ILexiconable {

	public BlockUnstable() {
		super(Material.iron);
		setHardness(5.0F);
		setResistance(10.0F);
		setStepSound(soundTypeMetal);
		setBlockBounds(0.25F, 0.25F, 0.25F, 0.75F, 0.75F, 0.75F);
		setBlockName(LibBlockNames.UNSTABLE_BLOCK);
	}

	@Override
	public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
		for(int i = 0; i < 16; i++)
			par3List.add(new ItemStack(par1, 1, i));
	}

	@Override
	public Block setBlockName(String par1Str) {
		GameRegistry.registerBlock(this, ItemBlockWithMetadataAndName.class, par1Str);
		return super.setBlockName(par1Str);
	}

	@Override
	protected boolean shouldRegisterInNameSet() {
		return false;
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
	public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random) {
		int color = getRenderColor(par1World.getBlockMetadata(par2, par3, par4));
		int colorBright = new Color(color).brighter().getRGB();
		int colorDark = new Color(color).darker().getRGB();

		Vector3 origVector = new Vector3(par2 + 0.5, par3 + 0.5, par4 + 0.5);
		Vector3 endVector = origVector.copy().add(par1World.rand.nextDouble() * 2 - 1, par1World.rand.nextDouble() * 2 - 1, par1World.rand.nextDouble() * 2 - 1);
		Botania.proxy.lightningFX(par1World, origVector, endVector, 5F, colorDark, colorBright);
	}

	@Override
	public int damageDropped(int par1) {
		return par1;
	}

	@Override
	public int getRenderColor(int par1) {
		float[] color = EntitySheep.fleeceColorTable[par1];
		return new Color(color[0], color[1], color[2]).getRGB();
	}

	@Override
	public int colorMultiplier(IBlockAccess par1iBlockAccess, int par2, int par3, int par4) {
		return getRenderColor(par1iBlockAccess.getBlockMetadata(par2, par3, par4));
	}

	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.unstableBlocks;
	}

}
