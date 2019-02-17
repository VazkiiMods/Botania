/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Nov 1, 2014, 5:45:50 PM (GMT)]
 */
package vazkii.botania.common.item.brew;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.brew.Brew;
import vazkii.botania.api.brew.IBrewContainer;
import vazkii.botania.common.item.ItemMod;
import vazkii.botania.common.item.ModItems;

public class ItemVial extends ItemMod implements IBrewContainer {

	public ItemVial(Properties builder) {
		super(builder);
	}

	@Override
	public ItemStack getItemForBrew(Brew brew, ItemStack stack) {
		ItemStack brewStack = new ItemStack(this == ModItems.flask ? ModItems.brewFlask : ModItems.brewVial);
		ItemBrewBase.setBrew(brewStack, brew);
		return brewStack;
	}

	@Override
	public int getManaCost(Brew brew, ItemStack stack) {
		if (this == ModItems.flask) {
			return brew.getManaCost() * 2;
		} else {
			return brew.getManaCost();
		}
	}
}
