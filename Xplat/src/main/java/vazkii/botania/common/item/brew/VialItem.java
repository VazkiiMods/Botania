/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.brew;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import vazkii.botania.api.brew.Brew;
import vazkii.botania.api.brew.BrewContainer;
import vazkii.botania.common.item.BotaniaItems;

public class VialItem extends Item implements BrewContainer {

	public VialItem(Properties builder) {
		super(builder);
	}

	@Override
	public ItemStack getItemForBrew(Brew brew, ItemStack stack) {
		ItemStack brewStack = new ItemStack(stack.is(BotaniaItems.flask) ? BotaniaItems.brewFlask : BotaniaItems.brewVial);
		BaseBrewItem.setBrew(brewStack, brew);
		return brewStack;
	}

	@Override
	public int getManaCost(Brew brew, ItemStack stack) {
		if (stack.is(BotaniaItems.flask)) {
			return brew.getManaCost() * 2;
		} else {
			return brew.getManaCost();
		}
	}
}
