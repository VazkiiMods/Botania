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
import vazkii.botania.api.brew.IBrewContainer;
import vazkii.botania.common.item.ModItems;

public class ItemVial extends Item implements IBrewContainer {

	public ItemVial(Properties builder) {
		super(builder);
	}

	@Override
	public ItemStack getItemForBrew(Brew brew, ItemStack stack) {
		ItemStack brewStack = new ItemStack(stack.is(ModItems.flask) ? ModItems.brewFlask : ModItems.brewVial);
		ItemBrewBase.setBrew(brewStack, brew);
		return brewStack;
	}

	@Override
	public int getManaCost(Brew brew, ItemStack stack) {
		if (stack.is(ModItems.flask)) {
			return brew.getManaCost() * 2;
		} else {
			return brew.getManaCost();
		}
	}
}
