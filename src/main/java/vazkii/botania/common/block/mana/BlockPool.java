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
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
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
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.PoolVariant;
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
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockPool extends BlockModContainer implements IWandHUD, IWandable, ILexiconable, ICraftAchievement {

	boolean lastFragile = false;

	public BlockPool() {
		super(Material.rock);
		setHardness(2.0F);
		setResistance(10.0F);
		setStepSound(soundTypeStone);
		setUnlocalizedName(LibBlockNames.POOL);
		setBlockBounds(0F, 0F, 0F, 1F, 0.5F, 1F);

		setDefaultState(blockState.getBaseState().withProperty(BotaniaStateProps.POOL_VARIANT, PoolVariant.DEFAULT));

		BotaniaAPI.blacklistBlockFromMagnet(this, Short.MAX_VALUE);
	}

	@Override
	public BlockState createBlockState() {
		return new BlockState(this, BotaniaStateProps.POOL_VARIANT);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(BotaniaStateProps.POOL_VARIANT).ordinal();
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		if (meta > PoolVariant.values().length) {
			meta = 0;
		}
		return getDefaultState().withProperty(BotaniaStateProps.POOL_VARIANT, PoolVariant.values()[meta]);
	}

	@Override
	protected boolean shouldRegisterInNameSet() {
		return false;
	}

	@Override
	public Block setUnlocalizedName(String par1Str) {
		GameRegistry.registerBlock(this, ItemBlockPool.class, par1Str);
		return super.setUnlocalizedName(par1Str);
	}

	@Override
	public int damageDropped(IBlockState state) {
		return state.getBlock().getMetaFromState(state);
	}

	@Override
	public void breakBlock(World par1World, BlockPos pos, IBlockState state) {
		TilePool pool = (TilePool) par1World.getTileEntity(pos);
		lastFragile = pool.fragile;
		super.breakBlock(par1World, pos, state);
	}

	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		ArrayList<ItemStack> drops = new ArrayList();

		if(!lastFragile)
			drops.add(new ItemStack(this, 1, state.getBlock().getMetaFromState(state)));

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
	public void onEntityCollidedWithBlock(World par1World, BlockPos pos, IBlockState state, Entity par5Entity) {
		if(par5Entity instanceof EntityItem) {
			TilePool tile = (TilePool) par1World.getTileEntity(pos);
			if(tile.collideEntityItem((EntityItem) par5Entity))
				VanillaPacketDispatcher.dispatchTEToNearbyPlayers(par1World, pos);
		}
	}

	@Override
	public void addCollisionBoxesToList(World p_149743_1_, BlockPos pos, IBlockState state, AxisAlignedBB p_149743_5_, List<AxisAlignedBB> p_149743_6_, Entity p_149743_7_) {
		float f = 1F / 16F;
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, f, 1.0F);
		super.addCollisionBoxesToList(p_149743_1_, pos, state, p_149743_5_, p_149743_6_, p_149743_7_);
		setBlockBounds(0.0F, 0.0F, 0.0F, f, 0.5F, 1.0F);
		super.addCollisionBoxesToList(p_149743_1_, pos, state, p_149743_5_, p_149743_6_, p_149743_7_);
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, f);
		super.addCollisionBoxesToList(p_149743_1_, pos, state, p_149743_5_, p_149743_6_, p_149743_7_);
		setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
		super.addCollisionBoxesToList(p_149743_1_, pos, state, p_149743_5_, p_149743_6_, p_149743_7_);
		setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 0.5F, 1.0F);
		super.addCollisionBoxesToList(p_149743_1_, pos, state, p_149743_5_, p_149743_6_, p_149743_7_);
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
	}

	@Override
	public boolean isSideSolid(IBlockAccess world, BlockPos pos, EnumFacing side) {
		return side == EnumFacing.DOWN;
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
	public int getRenderType() {
		return 2;
	}

	@Override
	public boolean hasComparatorInputOverride() {
		return true;
	}

	@Override
	public int getComparatorInputOverride(World par1World, BlockPos pos) {
		TilePool pool = (TilePool) par1World.getTileEntity(pos);
		int val = (int) ((double) pool.getCurrentMana() / (double) pool.manaCap * 15.0);
		if(pool.getCurrentMana() > 0)
			val = Math.max(val, 1);

		return val;
	}

	@Override
	public void renderHUD(Minecraft mc, ScaledResolution res, World world, BlockPos pos) {
		((TilePool) world.getTileEntity(pos)).renderHUD(mc, res);
	}

	@Override
	public boolean onUsedByWand(EntityPlayer player, ItemStack stack, World world, BlockPos pos, EnumFacing side) {
		((TilePool) world.getTileEntity(pos)).onWanded(player, stack);
		return true;
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return world.getBlockState(pos).getValue(BotaniaStateProps.POOL_VARIANT) == PoolVariant.FABULOUS ? LexiconData.rainbowRod : LexiconData.pool;
	}

	@Override
	public Achievement getAchievementOnCraft(ItemStack stack, EntityPlayer player, IInventory matrix) {
		return ModAchievements.manaPoolPickup;
	}
}
