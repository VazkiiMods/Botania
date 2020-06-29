/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.brew;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

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
	public void fillItemGroup(@Nonnull ItemGroup tab, @Nonnull NonNullList<ItemStack> list) {
		super.fillItemGroup(tab, list);
		if (isInGroup(tab)) {
			for (Brew brew : BotaniaAPI.instance().getBrewRegistry()) {
				ItemStack brewStack = getItemForBrew(brew, new ItemStack(this));
				if (!brewStack.isEmpty()) {
					list.add(brewStack);
				}
			}
		}
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World world, List<ITextComponent> list, ITooltipFlag flags) {
		Brew brew = getBrew(stack);
		if (brew == ModBrews.fallbackBrew) {
			list.add(new TranslationTextComponent("botaniamisc.notInfused").func_240699_a_(TextFormatting.LIGHT_PURPLE));
			return;
		}

		list.add(new TranslationTextComponent("botaniamisc.brewOf", new TranslationTextComponent(brew.getTranslationKey(stack))).func_240699_a_(TextFormatting.LIGHT_PURPLE));
		ItemBrewBase.addPotionTooltip(brew.getPotionEffects(stack), list, TIME_MULTIPLIER);
	}

	@Override
	public Brew getBrew(ItemStack stack) {
		String key = ItemNBTHelper.getString(stack, TAG_BREW_KEY, "");
		return BotaniaAPI.instance().getBrewRegistry().getOrDefault(ResourceLocation.tryCreate(key));
	}

	public static void setBrew(ItemStack stack, Brew brew) {
		setBrew(stack, BotaniaAPI.instance().getBrewRegistry().getKey(brew));
	}

	public static void setBrew(ItemStack stack, ResourceLocation brew) {
		ItemNBTHelper.setString(stack, TAG_BREW_KEY, brew.toString());
	}

	@Override
	public ItemStack getItemForBrew(Brew brew, ItemStack stack) {
		if (!brew.canInfuseIncense() || brew.getPotionEffects(stack).size() != 1 || brew.getPotionEffects(stack).get(0).getPotion().isInstant()) {
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
