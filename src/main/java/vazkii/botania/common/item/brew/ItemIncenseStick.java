/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [May 15, 2015, 3:13:43 PM (GMT)]
 */
package vazkii.botania.common.item.brew;

import java.awt.Color;
import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.brew.Brew;
import vazkii.botania.api.brew.IBrewContainer;
import vazkii.botania.api.brew.IBrewItem;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.item.ItemMod;
import vazkii.botania.common.lib.LibItemNames;

public class ItemIncenseStick extends ItemMod implements IBrewItem, IBrewContainer {

	private static final String TAG_BREW_KEY = "brewKey";
	public static final int TIME_MULTIPLIER = 60;

	IIcon[] icons;

	public ItemIncenseStick() {
		setUnlocalizedName(LibItemNames.INCENSE_STICK);
		setMaxStackSize(1);
	}

	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		super.getSubItems(item, tab, list);
		for(String s : BotaniaAPI.brewMap.keySet()) {
			ItemStack brewStack = getItemForBrew(BotaniaAPI.brewMap.get(s), new ItemStack(this));
			if(brewStack != null)
				list.add(brewStack);
		}
	}

	@Override
	public void registerIcons(IIconRegister par1IconRegister) {
		icons = new IIcon[2];
		for(int i = 0; i < icons.length; i++)
			icons[i] = IconHelper.forItem(par1IconRegister, this, i);
	}

	@Override
	public boolean requiresMultipleRenderPasses() {
		return true;
	}

	@Override
	public IIcon getIcon(ItemStack stack, int pass) {
		return icons[pass];
	}

	@Override
	public int getColorFromItemStack(ItemStack stack, int pass) {
		if(pass == 0)
			return 0xFFFFFF;

		Brew brew = getBrew(stack);
		if(brew == BotaniaAPI.fallbackBrew)
			return 0x989898;

		Color color = new Color(brew.getColor(stack));
		int add = (int) (Math.sin(ClientTickHandler.ticksInGame * 0.2) * 24);

		int r = Math.max(0, Math.min(255, color.getRed() + add));
		int g = Math.max(0, Math.min(255, color.getGreen() + add));
		int b = Math.max(0, Math.min(255, color.getBlue() + add));

		return r << 16 | g << 8 | b;
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean adv) {
		Brew brew = getBrew(stack);
		if(brew == BotaniaAPI.fallbackBrew) {
			addStringToTooltip(EnumChatFormatting.LIGHT_PURPLE + StatCollector.translateToLocal("botaniamisc.notInfused"), list);
			return;
		}

		addStringToTooltip(EnumChatFormatting.LIGHT_PURPLE + String.format(StatCollector.translateToLocal("botaniamisc.brewOf"), StatCollector.translateToLocal(brew.getUnlocalizedName(stack))), list);
		for(PotionEffect effect : brew.getPotionEffects(stack)) {
			Potion potion = Potion.potionTypes[effect.getPotionID()];
			EnumChatFormatting format = potion.isBadEffect() ? EnumChatFormatting.RED : EnumChatFormatting.GRAY;
			PotionEffect longEffect = new PotionEffect(effect.getPotionID(), effect.getDuration() * TIME_MULTIPLIER, effect.getAmplifier(), false);
			addStringToTooltip(" " + format + StatCollector.translateToLocal(effect.getEffectName()) + (effect.getAmplifier() == 0 ? "" : " " + StatCollector.translateToLocal("botania.roman" + (effect.getAmplifier() + 1))) + EnumChatFormatting.GRAY + (potion.isInstant() ? "" : " (" + Potion.getDurationString(longEffect) + ")"), list);
		}
	}

	void addStringToTooltip(String s, List<String> tooltip) {
		tooltip.add(s.replaceAll("&", "\u00a7"));
	}

	@Override
	public Brew getBrew(ItemStack stack) {
		String key = ItemNBTHelper.getString(stack, TAG_BREW_KEY, "");
		return BotaniaAPI.getBrewFromKey(key);
	}

	public static void setBrew(ItemStack stack, Brew brew) {
		setBrew(stack, brew.getKey());
	}

	public static void setBrew(ItemStack stack, String brew) {
		ItemNBTHelper.setString(stack, TAG_BREW_KEY, brew);
	}

	@Override
	public ItemStack getItemForBrew(Brew brew, ItemStack stack) {
		if(!brew.canInfuseIncense() || brew.getPotionEffects(stack).size() != 1 || Potion.potionTypes[brew.getPotionEffects(stack).get(0).getPotionID()].isInstant())
			return null;

		ItemStack brewStack = new ItemStack(this);
		setBrew(brewStack, brew);
		return brewStack;
	}

	@Override
	public int getManaCost(Brew brew, ItemStack stack) {
		return brew.getManaCost() * 10;
	}
}
