/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.compat.rei.brewery;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.common.compat.rei.BotaniaRecipeDisplay;
import vazkii.botania.common.crafting.RecipeBrew;
import vazkii.botania.common.item.ModItems;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import me.shedaniel.rei.api.EntryStack;

@Environment(EnvType.CLIENT)
public class BreweryREIDisplay extends BotaniaRecipeDisplay<RecipeBrew> {
	private List<EntryStack> containers = new ArrayList<>();

	public BreweryREIDisplay(RecipeBrew recipe) {
		super(recipe);
		List<ItemStack> items = Arrays.asList(new ItemStack(ModItems.vial), new ItemStack(ModItems.flask), new ItemStack(ModItems.incenseStick), new ItemStack(ModItems.bloodPendant));
		List<EntryStack> outputs = new ArrayList<>();
		for (ItemStack stack : items) {
			ItemStack brewed = recipe.getOutput(stack);
			if (!brewed.isEmpty()) {
				containers.add(EntryStack.create(stack));
				outputs.add(EntryStack.create(brewed));
			}
		}
		this.outputs = outputs;
	}

	public List<EntryStack> getContainers() {
		return this.containers;
	}

	@Override
	public @NotNull List<List<EntryStack>> getResultingEntries() {
		return Collections.singletonList(this.outputs);
	}

	@Override
	public int getManaCost() {
		return recipe.getManaUsage();
	}

	@Override
	public @NotNull Identifier getRecipeCategory() {
		return RecipeBrew.TYPE_ID;
	}
}
