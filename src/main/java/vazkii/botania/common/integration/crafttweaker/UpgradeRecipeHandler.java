/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.integration.crafttweaker;

import com.blamejared.crafttweaker.api.CraftTweakerRegistry;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;
import com.blamejared.crafttweaker.api.recipes.IRecipeHandler;
import com.blamejared.crafttweaker.api.recipes.IReplacementRule;
import com.blamejared.crafttweaker.impl.brackets.RecipeTypeBracketHandler;

import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.util.ResourceLocation;

import vazkii.botania.common.crafting.recipe.ArmorUpgradeRecipe;
import vazkii.botania.common.crafting.recipe.ManaUpgradeRecipe;
import vazkii.botania.common.crafting.recipe.ShapelessManaUpgradeRecipe;
import vazkii.botania.common.crafting.recipe.TwigWandRecipe;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public abstract class UpgradeRecipeHandler<T extends ICraftingRecipe, I extends ICraftingRecipe> implements IRecipeHandler<T> {
	@Override
	public final String dumpToCommandString(IRecipeManager manager, T recipe) {
		I inner = getInner(recipe);
		IRecipeHandler<I> handler = CraftTweakerRegistry.getHandlerFor(inner);
		String commandString = handler.dumpToCommandString(manager, inner);
		if (getFunction() != null) {
			commandString = commandString.substring(0, commandString.length() - 2) + ", " + getFunction() + ");";
		}
		return commandString;
	}

	@Override
	public final Optional<Function<ResourceLocation, T>> replaceIngredients(IRecipeManager manager, T recipe, List<IReplacementRule> rules) throws ReplacementNotSupportedException {
		I inner = getInner(recipe);
		IRecipeHandler<I> handler = CraftTweakerRegistry.getHandlerFor(inner);
		IRecipeManager mgr = RecipeTypeBracketHandler.getOrDefault(inner.getType());
		Optional<Function<ResourceLocation, I>> function = handler.replaceIngredients(mgr, inner, rules);
		return function.map(f -> f.andThen(this::wrapRecipe));
	}

	protected abstract T wrapRecipe(I inner);

	protected abstract I getInner(T recipe);

	protected String getFunction() { // TODO give a working sample here?
		return "(usualOut, inputs) => { ... }";
	}

	@For(ArmorUpgradeRecipe.class)
	public static class Armor extends UpgradeRecipeHandler<ArmorUpgradeRecipe, ShapedRecipe> {
		@Override
		protected ArmorUpgradeRecipe wrapRecipe(ShapedRecipe inner) {
			return new ArmorUpgradeRecipe(inner);
		}

		@Override
		protected ShapedRecipe getInner(ArmorUpgradeRecipe recipe) {
			return recipe.getCompose();
		}
	}

	@For(ManaUpgradeRecipe.class)
	public static class Mana extends UpgradeRecipeHandler<ManaUpgradeRecipe, ShapedRecipe> {
		@Override
		protected ManaUpgradeRecipe wrapRecipe(ShapedRecipe inner) {
			return new ManaUpgradeRecipe(inner);
		}

		@Override
		protected ShapedRecipe getInner(ManaUpgradeRecipe recipe) {
			return recipe.getCompose();
		}
	}

	@For(ShapelessManaUpgradeRecipe.class)
	public static class ShapelessMana extends UpgradeRecipeHandler<ShapelessManaUpgradeRecipe, ShapelessRecipe> {
		@Override
		protected ShapelessManaUpgradeRecipe wrapRecipe(ShapelessRecipe inner) {
			return new ShapelessManaUpgradeRecipe(inner);
		}

		@Override
		protected ShapelessRecipe getInner(ShapelessManaUpgradeRecipe recipe) {
			return recipe.getCompose();
		}
	}

	@For(TwigWandRecipe.class)
	public static class TwigWand extends UpgradeRecipeHandler<TwigWandRecipe, ShapedRecipe> {
		@Override
		protected TwigWandRecipe wrapRecipe(ShapedRecipe inner) {
			return new TwigWandRecipe(inner);
		}

		@Override
		protected ShapedRecipe getInner(TwigWandRecipe recipe) {
			return recipe.getCompose();
		}

		@Override
		protected String getFunction() {
			return null;
		}
	}
}
