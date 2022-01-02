/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.fabric.compat.rei;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;

import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.common.crafting.RecipeBrew;
import vazkii.botania.common.item.ModItems;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BreweryREIDisplay extends BotaniaRecipeDisplay<RecipeBrew> {
	private final EntryIngredient containers;

	public BreweryREIDisplay(RecipeBrew recipe) {
		super(recipe);
		List<ItemStack> items = Arrays.asList(new ItemStack(ModItems.vial), new ItemStack(ModItems.flask), new ItemStack(ModItems.incenseStick), new ItemStack(ModItems.bloodPendant));
		this.containers = EntryIngredients.ofItemStacks(items);
		List<ItemStack> outputs = new ArrayList<>();
		for (ItemStack stack : items) {
			ItemStack brewed = recipe.getOutput(stack);
			if (!brewed.isEmpty()) {
				outputs.add(brewed);
			}
		}
		this.outputs = EntryIngredients.ofItemStacks(outputs);
	}

	public EntryIngredient getContainers() {
		return this.containers;
	}

	@Override
	public int getManaCost() {
		return recipe.getManaUsage();
	}

	@Override
	public @NotNull CategoryIdentifier<?> getCategoryIdentifier() {
		return BotaniaREICategoryIdentifiers.BREWERY;
	}
}
