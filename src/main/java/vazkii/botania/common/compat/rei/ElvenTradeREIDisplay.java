/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.compat.rei;

import net.minecraft.resources.ResourceLocation;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.common.crafting.RecipeElvenTrade;

import java.util.Collections;
import java.util.List;

import me.shedaniel.rei.api.EntryStack;

public class ElvenTradeREIDisplay extends BotaniaRecipeDisplay<RecipeElvenTrade> {

	public ElvenTradeREIDisplay(RecipeElvenTrade recipe) {
		super(recipe);
		this.outputs = EntryStack.ofItemStacks(recipe.getOutputs());
	}

	@Override
	public int getManaCost() {
		//hardcoded. Sad!
		return 500;
	}

	@Override
	public @NotNull List<List<EntryStack>> getResultingEntries() {
		return Collections.singletonList(this.outputs);
	}

	@Override
	public @NotNull ResourceLocation getRecipeCategory() {
		return RecipeElvenTrade.TYPE_ID;
	}
}
