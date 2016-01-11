/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 20, 2014, 7:42:46 PM (GMT)]
 */
package vazkii.botania.common.item;

import java.awt.Color;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.world.World;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.wand.ICoordBoundItem;
import vazkii.botania.api.wand.ITileBound;
import vazkii.botania.api.wand.IWandBindable;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.common.Botania;
import vazkii.botania.common.achievement.ModAchievements;
import vazkii.botania.common.block.BlockPistonRelay;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileEnchanter;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.lib.LibItemNames;

public class ItemTwigWand extends Item16Colors implements ICoordBoundItem {

	private static final String TAG_COLOR1 = "color1";
	private static final String TAG_COLOR2 = "color2";
	private static final String TAG_BOUND_TILE_X = "boundTileX";
	private static final String TAG_BOUND_TILE_Y = "boundTileY";
	private static final String TAG_BOUND_TILE_Z = "boundTileZ";
	private static final String TAG_BIND_MODE = "bindMode";
	private static final BlockPos UNBOUND_POS = new BlockPos(0, -1, 0);

	public ItemTwigWand() {
		super(LibItemNames.TWIG_WAND);
		setMaxStackSize(1);
	}

	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, BlockPos pos, EnumFacing side, float par8, float par9, float par10) {
		Block block = par3World.getBlockState(pos).getBlock();
		BlockPos boundTile = getBoundTile(par1ItemStack);

		if(boundTile.getY() != -1 && par2EntityPlayer.isSneaking() && !pos.equals(boundTile)) {
			TileEntity tile = par3World.getTileEntity(boundTile);
			if(tile instanceof IWandBindable) {
				if(((IWandBindable) tile).bindTo(par2EntityPlayer, par1ItemStack, pos, side)) {
					Vector3 orig = new Vector3(boundTile.getX() + 0.5, boundTile.getY() + 0.5, boundTile.getZ() + 0.5);
					Vector3 end = new Vector3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
					doParticleBeam(par3World, orig, end);

					VanillaPacketDispatcher.dispatchTEToNearbyPlayers(par3World, boundTile);
					setBoundTile(par1ItemStack, UNBOUND_POS);
				}

				return true;
			} else setBoundTile(par1ItemStack, UNBOUND_POS);
		} else if(par2EntityPlayer.isSneaking()) {
			block.rotateBlock(par3World, pos, side);
			if(par3World.isRemote)
				par2EntityPlayer.swingItem();
		}

		if(block == Blocks.lapis_block && ConfigHandler.enchanterEnabled) {
			EnumFacing.Axis axis = null;
			if(TileEnchanter.canEnchanterExist(par3World, pos, EnumFacing.Axis.X))
				axis = EnumFacing.Axis.X;
			else if(TileEnchanter.canEnchanterExist(par3World, pos, EnumFacing.Axis.Z))
				axis = EnumFacing.Axis.Z;

			if(axis != null && !par3World.isRemote) {
				par3World.setBlockState(pos, ModBlocks.enchanter.getDefaultState().withProperty(BotaniaStateProps.ENCHANTER_DIRECTION, axis), 1 | 2);
				par2EntityPlayer.addStat(ModAchievements.enchanterMake, 1);
				par3World.playSoundEffect(pos.getX(), pos.getY(), pos.getZ(), "botania:enchanterBlock", 0.5F, 0.6F);
				for(int i = 0; i < 50; i++) {
					float red = (float) Math.random();
					float green = (float) Math.random();
					float blue = (float) Math.random();

					double x = (Math.random() - 0.5) * 6;
					double y = (Math.random() - 0.5) * 6;
					double z = (Math.random() - 0.5) * 6;

					float velMul = 0.07F;

					Botania.proxy.wispFX(par3World, pos.getX() + 0.5 + x, pos.getY() + 0.5 + y, pos.getZ() + 0.5 + z, red, green, blue, (float) Math.random() * 0.15F + 0.15F, (float) -x * velMul, (float) -y * velMul, (float) -z * velMul);
				}
			}
		} else if(block instanceof IWandable) {
			TileEntity tile = par3World.getTileEntity(pos);
			boolean bindable = tile instanceof IWandBindable;

			boolean wanded = false;
			if(getBindMode(par1ItemStack) && bindable && par2EntityPlayer.isSneaking() && ((IWandBindable) tile).canSelect(par2EntityPlayer, par1ItemStack, pos, side)) {
				if(boundTile.equals(pos))
					setBoundTile(par1ItemStack, UNBOUND_POS);
				else setBoundTile(par1ItemStack, pos);

				if(par3World.isRemote)
					par2EntityPlayer.swingItem();
				par3World.playSoundAtEntity(par2EntityPlayer, "botania:ding", 0.1F, 1F);

				wanded = true;
			} else {
				wanded = ((IWandable) block).onUsedByWand(par2EntityPlayer, par1ItemStack, par3World, pos, side);
				if(wanded && par3World.isRemote)
					par2EntityPlayer.swingItem();
			}

			return wanded;
		} else if(BlockPistonRelay.playerPositions.containsKey(par2EntityPlayer.getName()) && !par3World.isRemote) {
			String bindPos = BlockPistonRelay.playerPositions.get(par2EntityPlayer.getName());
			String currentPos = BlockPistonRelay.getCoordsAsString(par3World.provider.getDimensionId(), pos);

			BlockPistonRelay.playerPositions.remove(par2EntityPlayer.getName());
			BlockPistonRelay.mappedPositions.put(bindPos, currentPos);
			BlockPistonRelay.WorldData.get(par3World).markDirty();

			par3World.playSoundAtEntity(par2EntityPlayer, "botania:ding", 1F, 1F);
		}

		return false;
	}

	public static void doParticleBeam(World world, Vector3 orig, Vector3 end) {
		if(!world.isRemote)
			return;

		Vector3 diff = end.copy().sub(orig);
		Vector3 movement = diff.copy().normalize().multiply(0.05);
		int iters = (int) (diff.mag() / movement.mag());
		float huePer = 1F / iters;
		float hueSum = (float) Math.random();

		Vector3 currentPos = orig.copy();
		for(int i = 0; i < iters; i++) {
			float hue = i * huePer + hueSum;
			Color color = Color.getHSBColor(hue, 1F, 1F);
			float r = color.getRed() / 255F;
			float g = color.getGreen() / 255F;
			float b = color.getBlue() / 255F;

			Botania.proxy.setSparkleFXNoClip(true);
			Botania.proxy.sparkleFX(world, currentPos.x, currentPos.y, currentPos.z, r, g, b, 0.5F, 4);
			Botania.proxy.setSparkleFXNoClip(false);
			currentPos.add(movement);
		}
	}

	@Override
	public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
		BlockPos coords = getBoundTile(par1ItemStack);
		TileEntity tile = par2World.getTileEntity(coords);
		if(tile == null || !(tile instanceof IWandBindable))
			setBoundTile(par1ItemStack, UNBOUND_POS);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if(!world.isRemote && player.isSneaking()) {
			setBindMode(stack, !getBindMode(stack));
			world.playSoundAtEntity(player, "botania:ding", 0.1F, 1F);
		}

		return stack;
	}

	@Override
	public boolean isFull3D() {
		return true;
	}

	@Override
	public int getColorFromItemStack(ItemStack par1ItemStack, int par2) {
		if(par2 == 0 || par2 == 3)
			return 0xFFFFFF;

		EnumDyeColor color = EnumDyeColor.byMetadata(par2 == 1 ? getColor1(par1ItemStack) : getColor2(par1ItemStack));
		return color.getMapColor().colorValue;
	}

	@Override
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List) {
		for(int i = 0; i < 16; i++)
			par3List.add(forColors(i, i));
	}

	@Override
	public String getUnlocalizedName(ItemStack par1ItemStack) {
		return getUnlocalizedNameLazy(par1ItemStack);
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer p, List list, boolean adv) {
		list.add(StatCollector.translateToLocal(getModeString(stack)));
	}

	@Override
	public EnumRarity getRarity(ItemStack par1ItemStack) {
		return EnumRarity.RARE;
	}

	public static ItemStack forColors(int color1, int color2) {
		ItemStack stack = new ItemStack(ModItems.twigWand);
		ItemNBTHelper.setInt(stack, TAG_COLOR1, color1);
		ItemNBTHelper.setInt(stack, TAG_COLOR2, color2);

		return stack;
	}

	public static int getColor1(ItemStack stack) {
		return ItemNBTHelper.getInt(stack, TAG_COLOR1, 0);
	}

	public static int getColor2(ItemStack stack) {
		return ItemNBTHelper.getInt(stack, TAG_COLOR2, 0);
	}

	public static void setBoundTile(ItemStack stack, BlockPos pos) {
		ItemNBTHelper.setInt(stack, TAG_BOUND_TILE_X, pos.getX());
		ItemNBTHelper.setInt(stack, TAG_BOUND_TILE_Y, pos.getY());
		ItemNBTHelper.setInt(stack, TAG_BOUND_TILE_Z, pos.getZ());
	}

	public static BlockPos getBoundTile(ItemStack stack) {
		int x = ItemNBTHelper.getInt(stack, TAG_BOUND_TILE_X, 0);
		int y = ItemNBTHelper.getInt(stack, TAG_BOUND_TILE_Y, -1);
		int z = ItemNBTHelper.getInt(stack, TAG_BOUND_TILE_Z, 0);
		return new BlockPos(x, y, z);
	}

	public static boolean getBindMode(ItemStack stack) {
		return ItemNBTHelper.getBoolean(stack, TAG_BIND_MODE, true);
	}

	public static void setBindMode(ItemStack stack, boolean bindMode) {
		ItemNBTHelper.setBoolean(stack, TAG_BIND_MODE, bindMode);
	}

	public static String getModeString(ItemStack stack) {
		return "botaniamisc.wandMode." + (getBindMode(stack) ? "bind" : "function");
	}

	@Override
	public BlockPos getBinding(ItemStack stack) {
		BlockPos bound = getBoundTile(stack);
		if(bound.getY() != -1)
			return bound;

		MovingObjectPosition pos = Minecraft.getMinecraft().objectMouseOver;
		if(pos != null && pos.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
			TileEntity tile = Minecraft.getMinecraft().theWorld.getTileEntity(pos.getBlockPos());
			if(tile != null && tile instanceof ITileBound) {
				BlockPos coords = ((ITileBound) tile).getBinding();
				return coords;
			}
		}

		return null;
	}

}
