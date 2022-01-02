/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.brew;

import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.brew.Brew;
import vazkii.botania.api.brew.IBrewContainer;
import vazkii.botania.api.brew.IBrewItem;
import vazkii.botania.common.brew.ModBrews;
import vazkii.botania.common.core.helper.ItemNBTHelper;

import javax.annotation.Nonnull;

import java.util.List;

public class ItemIncenseStick extends Item implements IBrewItem, IBrewContainer {

	private static final String TAG_BREW_KEY = "brewKey";
	public static final int TIME_MULTIPLIER = 60;

	public ItemIncenseStick(Properties builder) {
		super(builder);
	}

	@Override
	public void fillItemCategory(@Nonnull CreativeModeTab tab, @Nonnull NonNullList<ItemStack> list) {
		super.fillItemCategory(tab, list);
		if (allowdedIn(tab)) {
			for (Brew brew : BotaniaAPI.instance().getBrewRegistry()) {
				ItemStack brewStack = getItemForBrew(brew, new ItemStack(this));
				if (!brewStack.isEmpty()) {
					list.add(brewStack);
				}
			}
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, Level world, List<Component> list, TooltipFlag flags) {
		Brew brew = getBrew(stack);
		if (brew == ModBrews.fallbackBrew) {
			list.add(new TranslatableComponent("botaniamisc.notInfused").withStyle(ChatFormatting.LIGHT_PURPLE));
			return;
		}

		list.add(new TranslatableComponent("botaniamisc.brewOf", new TranslatableComponent(brew.getTranslationKey(stack))).withStyle(ChatFormatting.LIGHT_PURPLE));
		ItemBrewBase.addPotionTooltip(brew.getPotionEffects(stack), list, TIME_MULTIPLIER);
	}

	@Override
	public Brew getBrew(ItemStack stack) {
		String key = ItemNBTHelper.getString(stack, TAG_BREW_KEY, "");
		return BotaniaAPI.instance().getBrewRegistry().get(ResourceLocation.tryParse(key));
	}

	public static void setBrew(ItemStack stack, Brew brew) {
		setBrew(stack, BotaniaAPI.instance().getBrewRegistry().getKey(brew));
	}

	public static void setBrew(ItemStack stack, ResourceLocation brew) {
		ItemNBTHelper.setString(stack, TAG_BREW_KEY, brew.toString());
	}

	@Override
	public ItemStack getItemForBrew(Brew brew, ItemStack stack) {
		if (!brew.canInfuseIncense() || brew.getPotionEffects(stack).size() != 1 || brew.getPotionEffects(stack).get(0).getEffect().isInstantenous()) {
			return ItemStack.EMPTY;
		}

		ItemStack brewStack = new ItemStack(this);
		setBrew(brewStack, brew);
		return brewStack;
	}

	@Override
	public int getManaCost(Brew brew, ItemStack stack) {
		return brew.getManaCost() * 10;
	}
}
