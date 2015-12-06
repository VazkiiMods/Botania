/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 26, 2014, 12:22:58 AM (GMT)]
 */
package vazkii.botania.common.block.mana;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.client.lib.LibRenderIDs;
import vazkii.botania.common.achievement.ICraftAchievement;
import vazkii.botania.common.achievement.IPickupAchievement;
import vazkii.botania.common.achievement.ModAchievements;
import vazkii.botania.common.block.BlockModContainer;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.mana.TilePool;
import vazkii.botania.common.item.block.ItemBlockPool;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;
import cpw.mods.fml.common.registry.GameRegistry;

public class BlockPool extends BlockModContainer implements IWandHUD, IWandable, ILexiconable, ICraftAchievement {

	boolean lastFragile = false;

	public static IIcon manaIcon;

	public BlockPool() {
		super(Material.rock);
		setHardness(2.0F);
		setResistance(10.0F);
		setStepSound(soundTypeStone);
		setBlockName(LibBlockNames.POOL);
		setBlockBounds(0F, 0F, 0F, 1F, 0.5F, 1F);

		BotaniaAPI.blacklistBlockFromMagnet(this, Short.MAX_VALUE);
	}

	@Override
	protected boolean shouldRegisterInNameSet() {
		return false;
	}

	@Override
	public Block setBlockName(String par1Str) {
		GameRegistry.registerBlock(this, ItemBlockPool.class, par1Str);
		return super.setBlockName(par1Str);
	}

	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		manaIcon = IconHelper.forName(par1IconRegister, "manaWater");
	}

	@Override
	public int damageDropped(int meta) {
		return meta;
	}

	@Override
	public void breakBlock(World par1World, int par2, int par3, int par4, Block par5, int par6) {
		TilePool pool = (TilePool) par1World.getTileEntity(par2, par3, par4);
		lastFragile = pool.fragile;
		super.breakBlock(par1World, par2, par3, par4, par5, par6);
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
		ArrayList<ItemStack> drops = new ArrayList();

		if(!lastFragile)
			drops.add(new ItemStack(this, 1, metadata));

		return drops;
	}

	@Override
	public void getSubBlocks(Item par1, CreativeTabs par2, List par3) {
		par3.add(new ItemStack(par1, 1, 0));
		par3.add(new ItemStack(par1, 1, 2));
		par3.add(new ItemStack(par1, 1, 3));
		par3.add(new ItemStack(par1, 1, 1));
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TilePool();
	}

	@Override
	public void onEntityCollidedWithBlock(World par1World, int par2, int par3, int par4, Entity par5Entity) {
		if(par5Entity instanceof EntityItem) {
			TilePool tile = (TilePool) par1World.getTileEntity(par2, par3, par4);
			if(tile.collideEntityItem((EntityItem) par5Entity))
				VanillaPacketDispatcher.dispatchTEToNearbyPlayers(par1World, par2, par3, par4);
		}
	}

	@Override
	public void addCollisionBoxesToList(World p_149743_1_, int p_149743_2_, int p_149743_3_, int p_149743_4_, AxisAlignedBB p_149743_5_, List p_149743_6_, Entity p_149743_7_) {
		float f = 1F / 16F;
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, f, 1.0F);
		super.addCollisionBoxesToList(p_149743_1_, p_149743_2_, p_149743_3_, p_149743_4_, p_149743_5_, p_149743_6_, p_149743_7_);
		setBlockBounds(0.0F, 0.0F, 0.0F, f, 0.5F, 1.0F);
		super.addCollisionBoxesToList(p_149743_1_, p_149743_2_, p_149743_3_, p_149743_4_, p_149743_5_, p_149743_6_, p_149743_7_);
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, f);
		super.addCollisionBoxesToList(p_149743_1_, p_149743_2_, p_149743_3_, p_149743_4_, p_149743_5_, p_149743_6_, p_149743_7_);
		setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
		super.addCollisionBoxesToList(p_149743_1_, p_149743_2_, p_149743_3_, p_149743_4_, p_149743_5_, p_149743_6_, p_149743_7_);
		setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 0.5F, 1.0F);
		super.addCollisionBoxesToList(p_149743_1_, p_149743_2_, p_149743_3_, p_149743_4_, p_149743_5_, p_149743_6_, p_149743_7_);
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
	}

	@Override
	public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
		return side == ForgeDirection.DOWN;
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
	public IIcon getIcon(int par1, int par2) {
		return ModBlocks.livingrock.getIcon(par1, 0);
	}

	@Override
	public int getRenderType() {
		return LibRenderIDs.idPool;
	}

	@Override
	public boolean hasComparatorInputOverride() {
		return true;
	}

	@Override
	public int getComparatorInputOverride(World par1World, int par2, int par3, int par4, int par5) {
		TilePool pool = (TilePool) par1World.getTileEntity(par2, par3, par4);
		int val = (int) ((double) pool.getCurrentMana() / (double) pool.manaCap * 15.0);
		if(pool.getCurrentMana() > 0)
			val = Math.max(val, 1);

		return val;
	}

	@Override
	public void renderHUD(Minecraft mc, ScaledResolution res, World world, int x, int y, int z) {
		((TilePool) world.getTileEntity(x, y, z)).renderHUD(mc, res);
	}

	@Override
	public boolean onUsedByWand(EntityPlayer player, ItemStack stack, World world, int x, int y, int z, int side) {
		((TilePool) world.getTileEntity(x, y, z)).onWanded(player, stack);
		return true;
	}

	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
		return world.getBlockMetadata(x, y, z) == 3 ? LexiconData.rainbowRod : LexiconData.pool;
	}

	@Override
	public Achievement getAchievementOnCraft(ItemStack stack, EntityPlayer player, IInventory matrix) {
		return ModAchievements.manaPoolPickup;
	}
}
