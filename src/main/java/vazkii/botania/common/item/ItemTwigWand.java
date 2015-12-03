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
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.wand.ICoordBoundItem;
import vazkii.botania.api.wand.ITileBound;
import vazkii.botania.api.wand.IWandBindable;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.client.core.helper.IconHelper;
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

	IIcon[] icons;

	private static final String TAG_COLOR1 = "color1";
	private static final String TAG_COLOR2 = "color2";
	private static final String TAG_BOUND_TILE_X = "boundTileX";
	private static final String TAG_BOUND_TILE_Y = "boundTileY";
	private static final String TAG_BOUND_TILE_Z = "boundTileZ";
	private static final String TAG_BIND_MODE = "bindMode";

	public ItemTwigWand() {
		super(LibItemNames.TWIG_WAND);
		setMaxStackSize(1);
	}

	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
		Block block = par3World.getBlock(par4, par5, par6);
		ChunkCoordinates boundTile = getBoundTile(par1ItemStack);

		if(boundTile.posY != -1 && par2EntityPlayer.isSneaking() && (boundTile.posX != par4 || boundTile.posY != par5 || boundTile.posZ != par6)) {
			TileEntity tile = par3World.getTileEntity(boundTile.posX, boundTile.posY, boundTile.posZ);
			if(tile instanceof IWandBindable) {
				if(((IWandBindable) tile).bindTo(par2EntityPlayer, par1ItemStack, par4, par5, par6, par7)) {
					Vector3 orig = new Vector3(boundTile.posX + 0.5, boundTile.posY + 0.5, boundTile.posZ + 0.5);
					Vector3 end = new Vector3(par4 + 0.5, par5 + 0.5, par6 + 0.5);
					doParticleBeam(par3World, orig, end);

					VanillaPacketDispatcher.dispatchTEToNearbyPlayers(par3World, boundTile.posX, boundTile.posY, boundTile.posZ);
					setBoundTile(par1ItemStack, 0, -1, 0);
				}

				return true;
			} else setBoundTile(par1ItemStack, 0, -1, 0);
		} else if(par2EntityPlayer.isSneaking()) {
			block.rotateBlock(par3World, par4, par5, par6, ForgeDirection.getOrientation(par7));
			if(par3World.isRemote)
				par2EntityPlayer.swingItem();
		}

		if(block == Blocks.lapis_block && ConfigHandler.enchanterEnabled) {
			int meta = -1;
			if(TileEnchanter.canEnchanterExist(par3World, par4, par5, par6, 0))
				meta = 0;
			else if(TileEnchanter.canEnchanterExist(par3World, par4, par5, par6, 1))
				meta = 1;

			if(meta != -1 && !par3World.isRemote) {
				par3World.setBlock(par4, par5, par6, ModBlocks.enchanter, meta, 1 | 2);
				par2EntityPlayer.addStat(ModAchievements.enchanterMake, 1);
				par3World.playSoundEffect(par4, par5, par6, "botania:enchanterBlock", 0.5F, 0.6F);
				for(int i = 0; i < 50; i++) {
					float red = (float) Math.random();
					float green = (float) Math.random();
					float blue = (float) Math.random();

					double x = (Math.random() - 0.5) * 6;
					double y = (Math.random() - 0.5) * 6;
					double z = (Math.random() - 0.5) * 6;

					float velMul = 0.07F;

					Botania.proxy.wispFX(par3World, par4 + 0.5 + x, par5 + 0.5 + y, par6 + 0.5 + z, red, green, blue, (float) Math.random() * 0.15F + 0.15F, (float) -x * velMul, (float) -y * velMul, (float) -z * velMul);
				}
			}
		} else if(block instanceof IWandable) {
			TileEntity tile = par3World.getTileEntity(par4, par5, par6);
			boolean bindable = tile instanceof IWandBindable;

			boolean wanded = false;
			if(getBindMode(par1ItemStack) && bindable && par2EntityPlayer.isSneaking() && ((IWandBindable) tile).canSelect(par2EntityPlayer, par1ItemStack, par4, par5, par6, par7)) {
				if(boundTile.posX == par4 && boundTile.posY == par5 && boundTile.posZ == par6)
					setBoundTile(par1ItemStack, 0, -1, 0);
				else setBoundTile(par1ItemStack, par4, par5, par6);

				if(par3World.isRemote)
					par2EntityPlayer.swingItem();
				par3World.playSoundAtEntity(par2EntityPlayer, "botania:ding", 0.1F, 1F);

				wanded = true;
			} else {
				wanded = ((IWandable) block).onUsedByWand(par2EntityPlayer, par1ItemStack, par3World, par4, par5, par6, par7);
				if(wanded && par3World.isRemote)
					par2EntityPlayer.swingItem();
			}

			return wanded;
		} else if(BlockPistonRelay.playerPositions.containsKey(par2EntityPlayer.getCommandSenderName()) && !par3World.isRemote) {
			String bindPos = BlockPistonRelay.playerPositions.get(par2EntityPlayer.getCommandSenderName());
			String currentPos = BlockPistonRelay.getCoordsAsString(par3World.provider.dimensionId, par4, par5, par6);

			BlockPistonRelay.playerPositions.remove(par2EntityPlayer.getCommandSenderName());
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
		ChunkCoordinates coords = getBoundTile(par1ItemStack);
		TileEntity tile = par2World.getTileEntity(coords.posX, coords.posY, coords.posZ);
		if(tile == null || !(tile instanceof IWandBindable))
			setBoundTile(par1ItemStack, 0, -1, 0);
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
	public void registerIcons(IIconRegister par1IconRegister) {
		icons = new IIcon[4];
		for(int i = 0; i < icons.length; i++)
			icons[i] = IconHelper.forItem(par1IconRegister, this, i);
	}

	@Override
	public IIcon getIcon(ItemStack stack, int pass) {
		if(pass == 3 && !getBindMode(stack))
			pass = 0;

		return icons[Math.min(icons.length - 1, pass)];
	}

	@Override
	public int getColorFromItemStack(ItemStack par1ItemStack, int par2) {
		if(par2 == 0 || par2 == 3)
			return 0xFFFFFF;

		float[] color = EntitySheep.fleeceColorTable[par2 == 1 ? getColor1(par1ItemStack) : getColor2(par1ItemStack)];
		return new Color(color[0], color[1], color[2]).getRGB();
	}

	@Override
	public boolean requiresMultipleRenderPasses() {
		return true;
	}

	@Override
	public int getRenderPasses(int metadata) {
		return 4;
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
		return EnumRarity.rare;
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

	public static void setBoundTile(ItemStack stack, int x, int y, int z) {
		ItemNBTHelper.setInt(stack, TAG_BOUND_TILE_X, x);
		ItemNBTHelper.setInt(stack, TAG_BOUND_TILE_Y, y);
		ItemNBTHelper.setInt(stack, TAG_BOUND_TILE_Z, z);
	}

	public static ChunkCoordinates getBoundTile(ItemStack stack) {
		int x = ItemNBTHelper.getInt(stack, TAG_BOUND_TILE_X, 0);
		int y = ItemNBTHelper.getInt(stack, TAG_BOUND_TILE_Y, -1);
		int z = ItemNBTHelper.getInt(stack, TAG_BOUND_TILE_Z, 0);
		return new ChunkCoordinates(x, y, z);
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
	public ChunkCoordinates getBinding(ItemStack stack) {
		ChunkCoordinates bound = getBoundTile(stack);
		if(bound.posY != -1)
			return bound;

		MovingObjectPosition pos = Minecraft.getMinecraft().objectMouseOver;
		if(pos != null) {
			TileEntity tile = Minecraft.getMinecraft().theWorld.getTileEntity(pos.blockX, pos.blockY, pos.blockZ);
			if(tile != null && tile instanceof ITileBound) {
				ChunkCoordinates coords = ((ITileBound) tile).getBinding();
				return coords;
			}
		}

		return null;
	}

}
