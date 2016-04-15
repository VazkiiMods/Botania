/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Nov 17, 2014, 5:31:53 PM (GMT)]
 */
package vazkii.botania.common.block;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.RecipeSorter.Category;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.item.IFloatingFlower;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.subtile.ISpecialFlower;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.common.block.decor.BlockFloatingFlower;
import vazkii.botania.common.block.tile.TileFloatingSpecialFlower;
import vazkii.botania.common.block.tile.TileSpecialFlower;
import vazkii.botania.common.crafting.recipe.SpecialFloatingFlowerRecipe;
import vazkii.botania.common.integration.coloredlights.LightHelper;
import vazkii.botania.common.item.block.ItemBlockFloatingSpecialFlower;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;
import vazkii.botania.common.lib.LibBlockNames;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockFloatingSpecialFlower extends BlockFloatingFlower implements ISpecialFlower, IWandable, ILexiconable, IWandHUD {

	public BlockFloatingSpecialFlower() {
		super(LibBlockNames.FLOATING_SPECIAL_FLOWER);

		GameRegistry.addRecipe(new SpecialFloatingFlowerRecipe());
		RecipeSorter.register("botania:floatingSpecialFlower", SpecialFloatingFlowerRecipe.class, Category.SHAPELESS, "");
		setDefaultState(((IExtendedBlockState) blockState.getBaseState())
				.withProperty(BotaniaStateProps.ISLAND_TYPE, IFloatingFlower.IslandType.GRASS)
				.withProperty(BotaniaStateProps.SUBTILE_ID, "daybloom")
				.withProperty(BotaniaStateProps.COLOR, EnumDyeColor.WHITE));
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new ExtendedBlockState(this, new IProperty[] { BotaniaStateProps.COLOR }, new IUnlistedProperty[] { BotaniaStateProps.SUBTILE_ID, BotaniaStateProps.ISLAND_TYPE });
	}

	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
		state = super.getExtendedState(state, world, pos);
		TileEntity te = world.getTileEntity(pos);
		if (te instanceof TileFloatingSpecialFlower) {
			state = ((IExtendedBlockState) state).withProperty(BotaniaStateProps.SUBTILE_ID, ((TileFloatingSpecialFlower) te).subTileName);
		}
		return state;
	}

	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		TileEntity tile = world.getTileEntity(pos);
		int currentLight = tile instanceof TileSpecialFlower ? ((TileSpecialFlower) tile).getLightValue() : -1;
		if(currentLight == -1)
			currentLight = originalLight;
		return LightHelper.getPackedColor(world.getBlockState(pos).getValue(BotaniaStateProps.COLOR), currentLight);
	}

	@Override
	public boolean hasComparatorInputOverride(IBlockState state) {
		return true;
	}

	@Override
	public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
		return ((TileSpecialFlower) world.getTileEntity(pos)).getComparatorInputOverride();
	}

	@Override
	public int getWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return ((TileSpecialFlower) world.getTileEntity(pos)).getPowerLevel(side);
	}

	@Override
	public int getStrongPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return getWeakPower(state, world, pos, side);
	}

	@Override
	public boolean canProvidePower(IBlockState state) {
		return true;
	}

	@Override
	public void randomDisplayTick(IBlockState state, World par1World, BlockPos pos, Random par5Random) {
		// NO-OP
	}

	@Override
	public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List) {
		for(String s : BotaniaAPI.subtilesForCreativeMenu) {
			par3List.add(ItemBlockSpecialFlower.ofType(new ItemStack(par1), s));
			if(BotaniaAPI.miniFlowers.containsKey(s))
				par3List.add(ItemBlockSpecialFlower.ofType(new ItemStack(par1), BotaniaAPI.miniFlowers.get(s)));
		}
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		String name = ((TileSpecialFlower) world.getTileEntity(pos)).subTileName;
		return ItemBlockSpecialFlower.ofType(new ItemStack(state.getBlock()), name);
	}

	@Override
	public void onBlockHarvested(World par1World, BlockPos pos, IBlockState state, EntityPlayer par6EntityPlayer) {
		if(!par6EntityPlayer.capabilities.isCreativeMode) {
			dropBlockAsItem(par1World, pos, state, 0);
			((TileSpecialFlower) par1World.getTileEntity(pos)).onBlockHarvested(par1World, pos, state, par6EntityPlayer);
		}
	}

	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		ArrayList<ItemStack> list = new ArrayList<>();
		TileEntity tile = world.getTileEntity(pos);

		if(tile != null) {
			String name = ((TileSpecialFlower) tile).subTileName;
			list.add(ItemBlockSpecialFlower.ofType(new ItemStack(state.getBlock()), name));
			((TileSpecialFlower) tile).getDrops(list);
		}

		return list;
	}

	@Override
	public boolean onBlockEventReceived(World par1World, BlockPos pos, IBlockState state, int eventID, int value) {
		super.onBlockEventReceived(par1World, pos, state, eventID, value);
		TileEntity tileentity = par1World.getTileEntity(pos);
		return tileentity != null ? tileentity.receiveClientEvent(eventID, value) : false;
	}

	@Override
	public boolean onUsedByWand(EntityPlayer player, ItemStack stack, World world, BlockPos pos, EnumFacing side) {
		return ((TileSpecialFlower) world.getTileEntity(pos)).onWanded(stack, player);
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack) {
		((TileSpecialFlower) world.getTileEntity(pos)).onBlockPlacedBy(world, pos, state, entity, stack);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack stack, EnumFacing side, float hitX, float hitY, float hitZ) {
		return ((TileSpecialFlower) world.getTileEntity(pos)).onBlockActivated(world, pos, state, player, hand, stack, side, hitX, hitY, hitZ) || super.onBlockActivated(world, pos, state, player, hand, stack, side, hitX, hitY, hitZ);
	}

	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
		((TileSpecialFlower) world.getTileEntity(pos)).onBlockAdded(world, pos, state);
	}

	@Override
	public void renderHUD(Minecraft mc, ScaledResolution res, World world, BlockPos pos) {
		((TileSpecialFlower) world.getTileEntity(pos)).renderHUD(mc, res);
	}

	@Override
	public void registerItemForm() {
		GameRegistry.register(new ItemBlockFloatingSpecialFlower(this), getRegistryName());
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileFloatingSpecialFlower();
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return ((TileSpecialFlower) world.getTileEntity(pos)).getEntry();
	}
}
