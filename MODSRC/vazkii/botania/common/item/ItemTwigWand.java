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
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import vazkii.botania.api.IWandable;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.BlockPistonRelay;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileEnchanter;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.lib.LibItemIDs;
import vazkii.botania.common.lib.LibItemNames;

public class ItemTwigWand extends Item16Colors {

	Icon[] icons;

	private static final String TAG_COLOR1 = "color1";
	private static final String TAG_COLOR2 = "color2";

	public ItemTwigWand() {
		super(LibItemIDs.idTwigWand, LibItemNames.TWIG_WAND);
		setMaxStackSize(1);
	}

	@Override
	public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10) {
		int id = par3World.getBlockId(par4, par5, par6);
		if(id == Block.blockLapis.blockID) {
			int meta = -1;
			if(TileEnchanter.canEnchanterExist(par3World, par4, par5, par6, 0))
				meta = 0;
			else if(TileEnchanter.canEnchanterExist(par3World, par4, par5, par6, 1))
				meta = 1;

			if(meta != -1 && !par3World.isRemote) {
				par3World.setBlock(par4, par5, par6, ModBlocks.enchanter.blockID, meta, 1 | 2);
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
		} else if(Block.blocksList[id] instanceof IWandable) {
			boolean wanded = ((IWandable) Block.blocksList[id]).onUsedByWand(par2EntityPlayer, par1ItemStack, par3World, par4, par5, par6, par7);
			if(wanded && par3World.isRemote)
				par2EntityPlayer.swingItem();

			return wanded;

		} else if(BlockPistonRelay.InternalTickHandler.playerPositions.containsKey(par2EntityPlayer.username)) {
			String bindPos = BlockPistonRelay.InternalTickHandler.playerPositions.get(par2EntityPlayer.username);
			String currentPos = BlockPistonRelay.getCoordsAsString(par3World.provider.dimensionId, par4, par5, par6);

			BlockPistonRelay.InternalTickHandler.playerPositions.remove(par2EntityPlayer.username);
			BlockPistonRelay.InternalTickHandler.mappedPositions.put(bindPos, currentPos);
			BlockPistonRelay.WorldData.get(par3World).markDirty();

			if(par3World.isRemote)
				par2EntityPlayer.swingItem();
		}

		return false;
	}

	@Override
	public boolean isFull3D() {
		return true;
	}

	@Override
	public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
		if(par3Entity instanceof EntityPlayer && ((EntityPlayer) par3Entity).getCurrentEquippedItem() == par1ItemStack)
			Botania.proxy.twigWandClientUpdate();
	}

	@Override
	public void registerIcons(IconRegister par1IconRegister) {
		icons = new Icon[3];
		for(int i = 0; i < icons.length; i++)
			icons[i] = IconHelper.forItem(par1IconRegister, this, i);
	}

	@Override
	public Icon getIcon(ItemStack stack, int pass) {
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
	public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List) {
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
		return ItemNBTHelper.getInt(stack, TAG_COLOR1, 0xFFFFFF);
	}

	public static int getColor2(ItemStack stack) {
		return ItemNBTHelper.getInt(stack, TAG_COLOR2, 0xFFFFFF);
	}

}
