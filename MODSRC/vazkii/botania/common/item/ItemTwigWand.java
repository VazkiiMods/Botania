/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
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
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import vazkii.botania.api.wand.ICoordBoundItem;
import vazkii.botania.api.wand.ITileBound;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.BlockPistonRelay;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileEnchanter;
import vazkii.botania.common.block.tile.mana.TileSpreader;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.entity.EntityManaBurst;
import vazkii.botania.common.lib.LibItemNames;

public class ItemTwigWand extends Item16Colors implements ICoordBoundItem {

	IIcon[] icons;

	private static final String TAG_COLOR1 = "color1";
	private static final String TAG_COLOR2 = "color2";
	private static final String TAG_TICKS_SINCE_SPREADER = "ticksSinceSpreader";
	private static final String TAG_SPREADER_X = "spreaderX";
	private static final String TAG_SPREADER_Y = "spreaderY";
	private static final String TAG_SPREADER_Z = "spreaderZ";

	public ItemTwigWand() {
		super(LibItemNames.TWIG_WAND);
		setMaxStackSize(1);
	}

	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
		Block block = par3World.getBlock(par4, par5, par6);
		ChunkCoordinates boundSpreader = getBoundSpreader(par1ItemStack);

		if(boundSpreader.posY != -1 && par2EntityPlayer.isSneaking() && !par3World.isRemote && (boundSpreader.posX != par4 || boundSpreader.posY != par5 || boundSpreader.posZ != par6)) {
			TileEntity tile = par3World.getTileEntity(boundSpreader.posX, boundSpreader.posY, boundSpreader.posZ);
			if(tile instanceof TileSpreader) {
				TileSpreader spreader = (TileSpreader) tile;
				
				Vector3 spreaderVec = Vector3.fromTileEntityCenter(spreader);
				Vector3 blockVec = new Vector3(par4 + 0.5, par5 + 0.5, par6 + 0.5);
				Vector3 diffVec =  blockVec.sub(spreaderVec);
				Vector3 diffVec2D = new Vector3(diffVec.x, diffVec.z, 0);
				Vector3 rotVec = new Vector3(0, 1, 0);
				double angle = rotVec.angle(diffVec2D) / Math.PI * 180.0;
				
				if(par4 < spreader.xCoord)
					angle = -angle;
				
				spreader.rotationX = (float) angle + 90;
				
				rotVec = new Vector3(diffVec.x, 0, diffVec.z);
				angle = diffVec.angle(rotVec) * 180F / Math.PI;
				if(par5 < boundSpreader.posY)
					angle = -angle;
				spreader.rotationY = (float) angle;
				
				spreader.checkForReceiver();
				par3World.markBlockForUpdate(boundSpreader.posX, boundSpreader.posY, boundSpreader.posZ);
				return true;
			} else setBoundSpreader(par1ItemStack, 0, -1, 0);
		} else if(par2EntityPlayer.isSneaking()) {
			block.rotateBlock(par3World, par4, par5, par6, ForgeDirection.getOrientation(par7));
			if(par3World.isRemote)
				par2EntityPlayer.swingItem();
		}

		if(block == Blocks.lapis_block) {
			int meta = -1;
			if(TileEnchanter.canEnchanterExist(par3World, par4, par5, par6, 0))
				meta = 0;
			else if(TileEnchanter.canEnchanterExist(par3World, par4, par5, par6, 1))
				meta = 1;

			if(meta != -1 && !par3World.isRemote) {
				par3World.setBlock(par4, par5, par6, ModBlocks.enchanter, meta, 1 | 2);
				par3World.playSoundEffect(par4, par5, par6, "random.levelup", 0.5F, 0.6F);
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
			boolean spreader = par3World.getTileEntity(par4, par5, par6) instanceof TileSpreader;
			int ticks = getTicksSinceSpreaderUse(par1ItemStack);
			boolean wanded = false;
			if(spreader && ticks == 20 && par2EntityPlayer.isSneaking()) {
				if(boundSpreader.posX == par4 && boundSpreader.posY == par5 && boundSpreader.posZ == par6)
					setBoundSpreader(par1ItemStack, 0, -1, 0);
				else setBoundSpreader(par1ItemStack, par4, par5, par6);

				if(par3World.isRemote)
					par2EntityPlayer.swingItem();
				par3World.playSoundAtEntity(par2EntityPlayer, "random.orb", 0.1F, 1F);

				setTicksSinceSpreaderUse(par1ItemStack, 0);
				wanded = true;
			} else {
				wanded = ((IWandable) block).onUsedByWand(par2EntityPlayer, par1ItemStack, par3World, par4, par5, par6, par7);
				if(wanded) {
					if(par3World.isRemote)
						par2EntityPlayer.swingItem();

					if(spreader)
						setTicksSinceSpreaderUse(par1ItemStack, 0);
				}
			}

			return wanded;

		} else if(BlockPistonRelay.playerPositions.containsKey(par2EntityPlayer.getCommandSenderName())) {
			String bindPos = BlockPistonRelay.playerPositions.get(par2EntityPlayer.getCommandSenderName());
			String currentPos = BlockPistonRelay.getCoordsAsString(par3World.provider.dimensionId, par4, par5, par6);

			BlockPistonRelay.playerPositions.remove(par2EntityPlayer.getCommandSenderName());
			BlockPistonRelay.mappedPositions.put(bindPos, currentPos);
			BlockPistonRelay.WorldData.get(par3World).markDirty();

			if(par3World.isRemote)
				par2EntityPlayer.swingItem();
		}

		return false;
	}

	@Override
	public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
		int ticks = getTicksSinceSpreaderUse(par1ItemStack);
		if(ticks < 20)
			setTicksSinceSpreaderUse(par1ItemStack, ticks + 1);

		ChunkCoordinates coords = getBoundSpreader(par1ItemStack);
		TileEntity tile = par2World.getTileEntity(coords.posX, coords.posY, coords.posZ);
		if(tile == null || !(tile instanceof TileSpreader))
			setBoundSpreader(par1ItemStack, 0, -1, 0);
	}

	@Override
	public boolean isFull3D() {
		return true;
	}

	@Override
	public void registerIcons(IIconRegister par1IconRegister) {
		icons = new IIcon[3];
		for(int i = 0; i < icons.length; i++)
			icons[i] = IconHelper.forItem(par1IconRegister, this, i);
	}

	@Override
	public IIcon getIcon(ItemStack stack, int pass) {
		return icons[Math.min(2, pass)];
	}

	@Override
	public int getColorFromItemStack(ItemStack par1ItemStack, int par2) {
		if(par2 == 0)
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
		return 3;
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

	public static void setTicksSinceSpreaderUse(ItemStack stack, int ticks) {
		ItemNBTHelper.setInt(stack, TAG_TICKS_SINCE_SPREADER, ticks);
	}

	public static int getTicksSinceSpreaderUse(ItemStack stack) {
		return ItemNBTHelper.getInt(stack, TAG_TICKS_SINCE_SPREADER, 20);
	}

	public static void setBoundSpreader(ItemStack stack, int x, int y, int z) {
		ItemNBTHelper.setInt(stack, TAG_SPREADER_X, x);
		ItemNBTHelper.setInt(stack, TAG_SPREADER_Y, y);
		ItemNBTHelper.setInt(stack, TAG_SPREADER_Z, z);
	}

	public static ChunkCoordinates getBoundSpreader(ItemStack stack) {
		int x = ItemNBTHelper.getInt(stack, TAG_SPREADER_X, 0);
		int y = ItemNBTHelper.getInt(stack, TAG_SPREADER_Y, -1);
		int z = ItemNBTHelper.getInt(stack, TAG_SPREADER_Z, 0);
		return new ChunkCoordinates(x, y, z);
	}

	@Override
	public ChunkCoordinates getBinding(ItemStack stack) {
		MovingObjectPosition pos = Minecraft.getMinecraft().objectMouseOver;
		if(pos != null) {
			TileEntity tile = Minecraft.getMinecraft().theWorld.getTileEntity(pos.blockX, pos.blockY, pos.blockZ);
			if(tile != null && tile instanceof ITileBound) {
				ChunkCoordinates coords = ((ITileBound) tile).getBinding();
				return coords;
			}
		}

		ChunkCoordinates bound = getBoundSpreader(stack);
		if(bound.posY != -1)
			return bound;

		return null;
	}

}
