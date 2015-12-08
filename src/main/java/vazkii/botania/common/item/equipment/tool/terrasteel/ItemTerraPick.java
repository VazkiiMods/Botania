/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [May 20, 2014, 10:56:14 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.tool.terrasteel;

import java.awt.Color;
import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.RecipeSorter.Category;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.item.ISequentialBreaker;
import vazkii.botania.api.mana.IManaGivingItem;
import vazkii.botania.api.mana.IManaItem;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.achievement.ModAchievements;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.crafting.recipe.TerraPickTippingRecipe;
import vazkii.botania.common.item.ItemSpark;
import vazkii.botania.common.item.ItemTemperanceStone;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.bauble.ItemAuraRing;
import vazkii.botania.common.item.equipment.bauble.ItemGreaterAuraRing;
import vazkii.botania.common.item.equipment.tool.ToolCommons;
import vazkii.botania.common.item.equipment.tool.manasteel.ItemManasteelPick;
import vazkii.botania.common.item.relic.ItemLokiRing;
import vazkii.botania.common.item.relic.ItemThorRing;
import vazkii.botania.common.lib.LibItemNames;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemTerraPick extends ItemManasteelPick implements IManaItem, ISequentialBreaker {

	private static final String TAG_ENABLED = "enabled";
	private static final String TAG_MANA = "mana";
	private static final String TAG_TIPPED = "tipped";

	private static final int MAX_MANA = Integer.MAX_VALUE;
	private static final int MANA_PER_DAMAGE = 100;

	private static final Material[] MATERIALS = new Material[] { Material.rock, Material.iron, Material.ice, Material.glass, Material.piston, Material.anvil, Material.grass, Material.ground, Material.sand, Material.snow, Material.craftedSnow, Material.clay };

	public static final int[] LEVELS = new int[] {
		0, 10000, 1000000, 10000000, 100000000, 1000000000
	};

	private static final int[] CREATIVE_MANA = new int[] {
		10000 - 1, 1000000 - 1, 10000000 - 1, 100000000 - 1, 1000000000 - 1, MAX_MANA - 1
	};

	IIcon iconTool, iconOverlay, iconTipped;

	public ItemTerraPick() {
		super(BotaniaAPI.terrasteelToolMaterial, LibItemNames.TERRA_PICK);
		GameRegistry.addRecipe(new TerraPickTippingRecipe());
		RecipeSorter.register("botania:terraPickTipping", TerraPickTippingRecipe.class, Category.SHAPELESS, "");
	}

	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		for(int mana : CREATIVE_MANA) {
			ItemStack stack = new ItemStack(item);
			setMana(stack, mana);
			list.add(stack);
		}
	}

	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		String rankFormat = StatCollector.translateToLocal("botaniamisc.toolRank");
		String rank = StatCollector.translateToLocal("botania.rank" + getLevel(par1ItemStack));
		par3List.add(String.format(rankFormat, rank).replaceAll("&", "\u00a7"));
		if(getMana(par1ItemStack) == Integer.MAX_VALUE)
			par3List.add(EnumChatFormatting.RED + StatCollector.translateToLocal("botaniamisc.getALife"));
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		getMana(par1ItemStack);
		int level = getLevel(par1ItemStack);

		if(level != 0) {
			setEnabled(par1ItemStack, !isEnabled(par1ItemStack));
			if(!par2World.isRemote)
				par2World.playSoundAtEntity(par3EntityPlayer, "botania:terraPickMode", 0.5F, 0.4F);
		}

		return par1ItemStack;
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int s, float sx, float sy, float sz) {
		return player.isSneaking() && super.onItemUse(stack, player, world, x, y, z, s, sx, sy, sz);
	}

	@Override
	public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
		super.onUpdate(par1ItemStack, par2World, par3Entity, par4, par5);
		if(isEnabled(par1ItemStack)) {
			int level = getLevel(par1ItemStack);

			if(level == 0)
				setEnabled(par1ItemStack, false);
			else if(par3Entity instanceof EntityPlayer && !((EntityPlayer) par3Entity).isSwingInProgress)
				addMana(par1ItemStack, -level);
		}
	}

	@Override
	public boolean onBlockStartBreak(ItemStack stack, int x, int y, int z, EntityPlayer player) {
		MovingObjectPosition raycast = ToolCommons.raytraceFromEntity(player.worldObj, player, true, 10);
		if(raycast != null) {
			breakOtherBlock(player, stack, x, y, z, x, y, z, raycast.sideHit);
			ItemLokiRing.breakOnAllCursors(player, this, stack, x, y, z, raycast.sideHit);
			// ^ Doable with API access through the IInternalMethodHandler.
		}

		return false;
	}

	@Override
	public int getManaPerDmg() {
		return MANA_PER_DAMAGE;
	}

	@Override
	public void breakOtherBlock(EntityPlayer player, ItemStack stack, int x, int y, int z, int originX, int originY, int originZ, int side) {
		if(!isEnabled(stack))
			return;

		World world = player.worldObj;
		Material mat = world.getBlock(x, y, z).getMaterial();
		if(!ToolCommons.isRightMaterial(mat, MATERIALS))
			return;

		if(world.isAirBlock(x, y, z))
			return;

		ForgeDirection direction = ForgeDirection.getOrientation(side);
		int fortune = EnchantmentHelper.getFortuneModifier(player);
		boolean silk = EnchantmentHelper.getSilkTouchModifier(player);
		boolean thor = ItemThorRing.getThorRing(player) != null;
		boolean doX = thor || direction.offsetX == 0;
		boolean doY = thor || direction.offsetY == 0;
		boolean doZ = thor || direction.offsetZ == 0;

		int origLevel = getLevel(stack);
		int level = origLevel + (thor ? 1 : 0);
		if(ItemTemperanceStone.hasTemperanceActive(player) && level > 2)
			level = 2;

		int range = Math.max(0, level - 1);
		int rangeY = Math.max(1, range);

		if(range == 0 && level != 1)
			return;

		ToolCommons.removeBlocksInIteration(player, stack, world, x, y, z, doX ? -range : 0, doY ? -1 : 0, doZ ? -range : 0, doX ? range + 1 : 1, doY ? rangeY * 2 : 1, doZ ? range + 1 : 1, null, MATERIALS, silk, fortune, isTipped(stack));
		if(origLevel == 5)
			player.addStat(ModAchievements.rankSSPick, 1);
	}

	@Override
	public int getEntityLifespan(ItemStack itemStack, World world) {
		return Integer.MAX_VALUE;
	}

	@Override
	public void registerIcons(IIconRegister par1IconRegister) {
		iconTool = IconHelper.forItem(par1IconRegister, this, 0);
		iconOverlay = IconHelper.forItem(par1IconRegister, this, 1);
		iconTipped = IconHelper.forItem(par1IconRegister, this, 2);
	}

	@Override
	public boolean requiresMultipleRenderPasses() {
		return true;
	}

	@Override
	public IIcon getIcon(ItemStack stack, int pass) {
		return pass == 1 && isEnabled(stack) ? iconOverlay : isTipped(stack) ? iconTipped : iconTool;
	}

	public static boolean isTipped(ItemStack stack) {
		return ItemNBTHelper.getBoolean(stack, TAG_TIPPED, false);
	}

	public static void setTipped(ItemStack stack) {
		ItemNBTHelper.setBoolean(stack, TAG_TIPPED, true);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack par1ItemStack, int par2) {
		if(par2 == 0 || !isEnabled(par1ItemStack))
			return 0xFFFFFF;

		return Color.HSBtoRGB(0.375F, (float) Math.min(1F, Math.sin(System.currentTimeMillis() / 200D) * 0.5 + 1F), 1F);
	}

	boolean isEnabled(ItemStack stack) {
		return ItemNBTHelper.getBoolean(stack, TAG_ENABLED, false);
	}

	void setEnabled(ItemStack stack, boolean enabled) {
		ItemNBTHelper.setBoolean(stack, TAG_ENABLED, enabled);
	}

	public static void setMana(ItemStack stack, int mana) {
		ItemNBTHelper.setInt(stack, TAG_MANA, mana);
	}

	@Override
	public int getMana(ItemStack stack) {
		return getMana_(stack);
	}

	public static int getMana_(ItemStack stack) {
		return ItemNBTHelper.getInt(stack, TAG_MANA, 0);
	}

	public static int getLevel(ItemStack stack) {
		int mana = getMana_(stack);
		for(int i = LEVELS.length - 1; i > 0; i--)
			if(mana >= LEVELS[i])
				return i;

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
		return !(otherStack.getItem() instanceof IManaGivingItem);
	}

	@Override
	public boolean canExportManaToPool(ItemStack stack, TileEntity pool) {
		return false;
	}

	@Override
	public boolean canExportManaToItem(ItemStack stack, ItemStack otherStack) {
		return false;
	}

	@Override
	public boolean isNoExport(ItemStack stack) {
		return true;
	}

	@Override
	public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
		return par2ItemStack.getItem() == ModItems.manaResource && par2ItemStack.getItemDamage() == 4 ? true : super.getIsRepairable(par1ItemStack, par2ItemStack);
	}

	@Override
	public boolean disposeOfTrashBlocks(ItemStack stack) {
		return isTipped(stack);
	}

}
