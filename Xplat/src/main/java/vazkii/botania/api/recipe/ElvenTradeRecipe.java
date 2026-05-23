/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.api.recipe;

import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
import it.unimi.dsi.fastutil.ints.Int2IntMap;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

public interface ElvenTradeRecipe extends Recipe<ProcessingRecipeInput> {
	ResourceLocation TYPE_ID = botaniaRL("elven_trade");
	ResourceLocation TYPE_ID_LEXICON = botaniaRL("elven_trade_lexicon");

	/**
	 * @return Preview of the inputs
	 */
	@Override
	NonNullList<Ingredient> getIngredients();

	/**
	 * @return Preview of the outputs
	 */
	List<ItemStack> getOutputs();

	/**
	 * Attempt to create the outputs of this recipe. If successful, also return the matched slots in the input.
	 */
	Optional<AssemblyResult> tryAssemble(ProcessingRecipeInput input, HolderLookup.Provider registries);

	@Override
	default RecipeType<?> getType() {
		return Objects.requireNonNull(BuiltInRegistries.RECIPE_TYPE.get(TYPE_ID));
	}

	// Ignored IRecipe boilerplate

	@Override
	default boolean matches(ProcessingRecipeInput input, Level level) {
		return tryAssemble(input, level.registryAccess()).isPresent();
	}

	/**
	 * @deprecated This recipe type can create multiple output items. Use
	 *             {@link #tryAssemble(ProcessingRecipeInput, HolderLookup.Provider)} to obtain the complete list.
	 */
	@Override
	@Deprecated
	default ItemStack assemble(ProcessingRecipeInput input, HolderLookup.Provider registries) {
		Optional<AssemblyResult> potentialResult = tryAssemble(input, registries);
		return potentialResult.isPresent() && !potentialResult.get().outputs().isEmpty()
				? potentialResult.get().outputs().getFirst()
				: ItemStack.EMPTY;
	}

	@Override
	default boolean canCraftInDimensions(int width, int height) {
		return false;
	}

	/**
	 * @deprecated This recipe can create multiple output items. Use {@link #getOutputs()} for a preview of the results,
	 *             or {@link #tryAssemble(ProcessingRecipeInput, HolderLookup.Provider)} to generate an output from an
	 *             input.
	 */
	@Deprecated
	@Override
	default ItemStack getResultItem(HolderLookup.Provider registries) {
		return getOutputs().stream().findFirst().orElse(ItemStack.EMPTY);
	}

	/**
	 * Checks if this recipe is a "return" recipe, meaning that it returns the item that was thrown into it.
	 *
	 * @return {@code true} if recipe has exactly one ingredient and a single output item that matches the ingredient,
	 *         {@code false} otherwise.
	 */
	default boolean isReturnRecipe() {
		return this.getOutputs().size() == 1
				&& this.getIngredients().size() == 1
				&& this.getIngredients().getFirst().test(this.getOutputs().getFirst());
	}

	/**
	 * The result of assembling the recipe.
	 * 
	 * @param outputs           List of item stacks to return.
	 * @param matchedInputSlots Map of input slots to number of matched items in that slot.
	 */
	record AssemblyResult(List<ItemStack> outputs, Int2IntMap matchedInputSlots) {
		public AssemblyResult(List<ItemStack> outputs, int matchedSlot) {
			this(outputs, singleItemSlot(matchedSlot));
		}

		public AssemblyResult(ItemStack output, int matchedSlot) {
			this(List.of(output), singleItemSlot(matchedSlot));
		}

		private static Int2IntMap singleItemSlot(int slot) {
			Int2IntArrayMap map = new Int2IntArrayMap(1);
			map.put(slot, 1);
			return map;
		}
	}
}
