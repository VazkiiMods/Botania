/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.compat.rei;

import com.google.common.collect.ImmutableList;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;

import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.common.crafting.RecipePureDaisy;

import java.util.Collections;

public class PureDaisyREIDisplay extends BotaniaRecipeDisplay<RecipePureDaisy> {

	public PureDaisyREIDisplay(RecipePureDaisy recipe) {
		super(recipe);
		ImmutableList.Builder<EntryStack<?>> inputs = ImmutableList.builder();
		for (BlockState state : recipe.getInput().getDisplayed()) {
			if (!state.getFluidState().isEmpty()) {
				inputs.add(EntryStacks.of(state.getFluidState().getType()));
			} else {
				inputs.add(EntryStacks.of(state.getBlock()));
			}
		}
		this.inputs = Collections.singletonList(EntryIngredient.of(inputs.build()));
		this.outputs = EntryIngredients.of(recipe.getOutputState().getBlock());
	}

	/*todo implement time-based hints?
	public int getProcessingTime() {
		return recipe.getTime();
	}
	*/

	@Override
	public int getManaCost() {
		return 0;
	}

	@Override
	public @NotNull CategoryIdentifier<?> getCategoryIdentifier() {
		return BotaniaREICategoryIdentifiers.PURE_DAISY;
	}
}
