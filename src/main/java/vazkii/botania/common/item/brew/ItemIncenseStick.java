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

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.brew.Brew;
import vazkii.botania.api.brew.IBrewContainer;
import vazkii.botania.api.brew.IBrewItem;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.item.ItemMod;
import vazkii.botania.common.lib.LibItemNames;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemIncenseStick extends ItemMod implements IBrewItem, IBrewContainer {

	private static final String TAG_BREW_KEY = "brewKey";
	public static final int TIME_MULTIPLIER = 60;

	public ItemIncenseStick() {
		super(LibItemNames.INCENSE_STICK);
		setMaxStackSize(1);
	}

	@Override
	public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> list) {
		super.getSubItems(tab, list);
		if(isInCreativeTab(tab)) {
			for(String s : BotaniaAPI.brewMap.keySet()) {
				ItemStack brewStack = getItemForBrew(BotaniaAPI.brewMap.get(s), new ItemStack(this));
				if(!brewStack.isEmpty())
					list.add(brewStack);
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World world, List<String> list, ITooltipFlag flags) {
		Brew brew = getBrew(stack);
		if(brew == BotaniaAPI.fallbackBrew) {
			addStringToTooltip(TextFormatting.LIGHT_PURPLE + I18n.format("botaniamisc.notInfused"), list);
			return;
		}

		addStringToTooltip(TextFormatting.LIGHT_PURPLE + I18n.format("botaniamisc.brewOf", I18n.format(brew.getUnlocalizedName(stack))), list);
		for(PotionEffect effect : brew.getPotionEffects(stack)) {
			TextFormatting format = effect.getPotion().isBadEffect() ? TextFormatting.RED : TextFormatting.GRAY;
			PotionEffect longEffect = new PotionEffect(effect.getPotion(), effect.getDuration() * TIME_MULTIPLIER, effect.getAmplifier(), false, true);
			addStringToTooltip(" " + format + I18n.format(effect.getEffectName()) + (effect.getAmplifier() == 0 ? "" : " " + I18n.format("botania.roman" + (effect.getAmplifier() + 1))) + TextFormatting.GRAY + (effect.getPotion().isInstant() ? "" : " (" + Potion.getPotionDurationString(longEffect, 1F) + ")"), list);
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
		if(!brew.canInfuseIncense() || brew.getPotionEffects(stack).size() != 1 || brew.getPotionEffects(stack).get(0).getPotion().isInstant())
			return ItemStack.EMPTY;

		ItemStack brewStack = new ItemStack(this);
		setBrew(brewStack, brew);
		return brewStack;
	}

	@Override
	public int getManaCost(Brew brew, ItemStack stack) {
		return brew.getManaCost() * 10;
	}
}
