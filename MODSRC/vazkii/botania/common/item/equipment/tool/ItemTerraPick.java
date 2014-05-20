/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [May 20, 2014, 10:56:14 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.tool;

import java.awt.Color;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.mana.IManaItem;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.lib.LibItemNames;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemTerraPick extends ItemManasteelPick implements IManaItem {

	private static final String TAG_ENABLED = "enabled";
	private static final String TAG_MANA = "mana";

	private static final int MAX_MANA = 1000000000;

	private static final Material[] MATERIALS = new Material[] { Material.rock, Material.iron, Material.ice, Material.glass, Material.piston, Material.anvil, Material.grass, Material.ground, Material.sand, Material.snow, Material.craftedSnow, Material.clay };

	private static final int[] LEVELS = new int[] {
		0, 10000, 1000000, 10000000, 100000000, 1000000000
	};

	IIcon iconTool, iconOverlay;

	public ItemTerraPick() {
		super(BotaniaAPI.terrasteelToolMaterial, LibItemNames.TERRA_PICK);
	}

	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		for(int level : LEVELS) {
			ItemStack stack = new ItemStack(item);
			setMana(stack, level);
			list.add(stack);
		}
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		String rankFormat = StatCollector.translateToLocal("botaniamisc.toolRank");
		String rank = StatCollector.translateToLocal("botania.rank" + getLevel(par1ItemStack));
		par3List.add(String.format(rankFormat, rank).replaceAll("&", "\u00a7"));
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		int level = getLevel(par1ItemStack);
		if(level != 0)
			ItemNBTHelper.setBoolean(par1ItemStack, TAG_ENABLED, !isEnabled(par1ItemStack));
		return par1ItemStack;
	}

	@Override
	public boolean onBlockStartBreak(ItemStack stack, int x, int y, int z, EntityPlayer player) {
		if(!isEnabled(stack))
			return false;

		World world = player.worldObj;
		Material mat = world.getBlock(x, y, z).getMaterial();
		if(!ManasteelToolCommons.isRightMaterial(mat, MATERIALS))
			return false;

		MovingObjectPosition block = ManasteelToolCommons.raytraceFromEntity(world, player, true, 4.5);
		if(block == null)
			return false;

		ForgeDirection direction = ForgeDirection.getOrientation(block.sideHit);
		int fortune = EnchantmentHelper.getFortuneModifier(player);
		boolean silk = EnchantmentHelper.getSilkTouchModifier(player);
		boolean doX = direction.offsetX == 0;
		boolean doY = direction.offsetY == 0;
		boolean doZ = direction.offsetZ == 0;

		int level = getLevel(stack);
		int range = Math.max(0, level - 1);
		int rangeY = Math.max(1, range);

		if(range == 0 && level != 1)
			return false;

		ManasteelToolCommons.removeBlocksInIteration(player, stack, world, x, y, z, doX ? -range : 0, doY ? -1 : 0, doZ ? -range : 0, doX ? range + 1 : 1, doY ? rangeY * 2 : 1, doZ ? range + 1 : 1, null, MATERIALS, silk, fortune);

		return false;
	}

	@Override
	public void registerIcons(IIconRegister par1IconRegister) {
		iconTool = IconHelper.forItem(par1IconRegister, this, 0);
		iconOverlay = IconHelper.forItem(par1IconRegister, this, 1);
	}

	@Override
	public boolean requiresMultipleRenderPasses() {
		return true;
	}

	@Override
	public IIcon getIcon(ItemStack stack, int pass) {
		return pass == 1 && isEnabled(stack) ? iconOverlay : iconTool;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack par1ItemStack, int par2) {
		if(par2 == 0 || !isEnabled(par1ItemStack))
			return 0xFFFFFF;

		return Color.HSBtoRGB(0.375F, (float) Math.min(1F, (Math.sin((double) System.currentTimeMillis() / 200D) * 0.5 + 1F)), 1F);
	}

	boolean isEnabled(ItemStack stack) {
		return ItemNBTHelper.getBoolean(stack, TAG_ENABLED, false);
	}

	public static void setMana(ItemStack stack, int mana) {
		ItemNBTHelper.setInt(stack, TAG_MANA, mana);
	}

	@Override
	public int getMana(ItemStack stack) {
		return ItemNBTHelper.getInt(stack, TAG_MANA, 0);
	}

	public int getLevel(ItemStack stack) {
		int mana = getMana(stack);
		for(int i = 0; i < LEVELS.length; i++) {
			if(mana > LEVELS[i])
				continue;
			return i;
		}
		return 0;
	}

	@Override
	public int getMaxMana(ItemStack stack) {
		return MAX_MANA;
	}

	@Override
	public void addMana(ItemStack stack, int mana) {
		setMana(stack, Math.min(getMana(stack) + mana, MAX_MANA));
	}

	@Override
	public boolean canReceiveManaFromPool(ItemStack stack, TileEntity pool) {
		return true;
	}

	@Override
	public boolean canReceiveManaFromItem(ItemStack stack, ItemStack otherStack) {
		return true;
	}

	@Override
	public boolean canExportManaToPool(ItemStack stack, TileEntity pool) {
		return false;
	}

	@Override
	public boolean canExportManaToItem(ItemStack stack, ItemStack otherStack) {
		return false;
	}

}
