/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 16, 2014, 5:50:31 PM (GMT)]
 */
package vazkii.botania.common.block;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.IGrowable;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.client.lib.LibRenderIDs;
import vazkii.botania.common.Botania;
import vazkii.botania.common.achievement.IPickupAchievement;
import vazkii.botania.common.achievement.ModAchievements;
import vazkii.botania.common.core.BotaniaCreativeTab;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.item.block.ItemBlockWithMetadataAndName;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockModFlower extends BlockFlower implements ILexiconable, IPickupAchievement, IGrowable {

	public static IIcon[] icons;
	public static IIcon[] iconsAlt;

	public int originalLight;

	public static final String ALT_DIR = "alt";

	protected BlockModFlower() {
		this(LibBlockNames.FLOWER);
	}

	protected BlockModFlower(String name) {
		super(0);
		setBlockName(name);
		setHardness(0F);
		setStepSound(soundTypeGrass);
		setBlockBounds(0.3F, 0.0F, 0.3F, 0.8F, 1, 0.8F);
		setTickRandomly(false);
		setCreativeTab(registerInCreative() ? BotaniaCreativeTab.INSTANCE : null);
	}

	public boolean registerInCreative() {
		return true;
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
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		icons = new IIcon[17];
		iconsAlt = new IIcon[17];

		for(int i = 0; i < icons.length; i++) {
			icons[i] = IconHelper.forBlock(par1IconRegister, this, i);
			iconsAlt[i] = IconHelper.forBlock(par1IconRegister, this, i, ALT_DIR);
		}
	}

	@Override
	public Block setLightLevel(float p_149715_1_) {
		originalLight = (int) (p_149715_1_ * 15);
		return super.setLightLevel(p_149715_1_);
	}

	@Override
	public IIcon getIcon(int par1, int par2) {
		return (ConfigHandler.altFlowerTextures ? iconsAlt : icons)[Math.min(icons.length - 1, par2)];
	}

	@Override
	public int getRenderType() {
		return LibRenderIDs.idSpecialFlower;
	}

	@Override
	public int damageDropped(int par1) {
		return par1;
	}

	@Override
	public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random) {
		int meta = par1World.getBlockMetadata(par2, par3, par4);
		float[] color = EntitySheep.fleeceColorTable[meta];

		if(par5Random.nextDouble() < ConfigHandler.flowerParticleFrequency)
			Botania.proxy.sparkleFX(par1World, par2 + 0.3 + par5Random.nextFloat() * 0.5, par3 + 0.5 + par5Random.nextFloat() * 0.5, par4 + 0.3 + par5Random.nextFloat() * 0.5, color[0], color[1], color[2], par5Random.nextFloat(), 5);
	}

	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.flowers;
	}

	@Override
	public Achievement getAchievementOnPickup(ItemStack stack, EntityPlayer player, EntityItem item) {
		return ModAchievements.flowerPickup;
	}

	@Override
	public boolean func_149851_a(World world, int x, int y, int z, boolean fuckifiknow) {
		return world.isAirBlock(x, y + 1, z);
	}

	@Override
	public boolean func_149852_a(World world, Random rand, int x, int y, int z) {
		return func_149851_a(world, x, y, z, false);
	}

	@Override
	public void func_149853_b(World world, Random rand, int x, int y, int z) {
		int meta = world.getBlockMetadata(x, y, z);
		placeDoubleFlower(world, x, y, z, meta, 1 | 2);
	}

	public static void placeDoubleFlower(World world, int x, int y, int z, int meta, int flags) {
		Block flower = meta >= 8 ? ModBlocks.doubleFlower2 : ModBlocks.doubleFlower1;
		int placeMeta = meta & 7;
		world.setBlock(x, y, z, flower, placeMeta, flags);
		world.setBlock(x, y + 1, z, flower, placeMeta | 8, flags);
	}

}
