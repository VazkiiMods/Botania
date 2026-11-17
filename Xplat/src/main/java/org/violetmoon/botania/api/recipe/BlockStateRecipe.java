/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.api.recipe;

import net.minecraft.commands.CacheableFunction;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.Level;

import java.util.Optional;

/**
 * A special category of recipes that don't operate on items, but on blocks in the world, and thus define input and
 * output in the form of block states.
 */
public interface BlockStateRecipe extends Recipe<RecipeInput> {
	/**
	 * Valid inputs for the recipe
	 */
	StateIngredient getInput();

	/**
	 * Output to display in recipes and to be used by default.
	 */
	StateIngredient getOutput();

	/**
	 * An <code>.mcfunction</code> that is executed right before placing the output block.
	 * At this point it is known that a specific block replacement is supposed to happen and all preconditions are
	 * technically met, but there is still a possibility that the actual replacement will not happen. In that case the
	 * success function is not called. Data packs that rely on a pre-update function should take this into account.
	 */
	Optional<CacheableFunction> getPreUpdateFunction();

	/**
	 * An <code>.mcfunction</code> that is executed after the input block was replaced with the output block.
	 */
	Optional<CacheableFunction> getSuccessFunction();

	/**
	 * @deprecated Not meant to be used for item crafting in a container.
	 */
	@Override
	@Deprecated
	default boolean matches(RecipeInput container, Level level) {
		return false;
	}

	/**
	 * @deprecated Not meant to be used for item crafting in a container.
	 */
	@Override
	@Deprecated
	default ItemStack assemble(RecipeInput container, HolderLookup.Provider registryAccess) {
		return ItemStack.EMPTY;
	}

	/**
	 * @deprecated Not meant to be used for item crafting in a container.
	 */
	@Override
	@Deprecated
	default boolean canCraftInDimensions(int width, int height) {
		return false;
	}

	/**
	 * @deprecated Not meant to be used for item crafting in a container.
	 */
	@Override
	@Deprecated
	default ItemStack getResultItem(HolderLookup.Provider registryAccess) {
		return ItemStack.EMPTY;
	}

	@Override
	default boolean isSpecial() {
		return true;
	}
}
