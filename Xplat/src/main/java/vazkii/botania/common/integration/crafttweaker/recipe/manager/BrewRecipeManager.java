/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.integration.crafttweaker.recipe.manager;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.CraftTweakerConstants;
import com.blamejared.crafttweaker.api.action.recipe.ActionAddRecipe;
import com.blamejared.crafttweaker.api.action.recipe.ActionRemoveRecipe;
import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.ingredient.IIngredient;
import com.blamejared.crafttweaker.api.recipe.handler.IRecipeHandler;
import com.blamejared.crafttweaker.api.recipe.handler.IReplacementRule;
import com.blamejared.crafttweaker.api.recipe.handler.helper.ReplacementHandlerHelper;
import com.blamejared.crafttweaker.api.recipe.manager.base.IRecipeManager;
import com.blamejared.crafttweaker.api.util.StringUtil;
import com.blamejared.crafttweaker_annotations.annotations.Document;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;

import org.openzen.zencode.java.ZenCodeType;

import vazkii.botania.api.brew.Brew;
import vazkii.botania.api.recipe.IBrewRecipe;
import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.botania.common.crafting.RecipeBrew;
import vazkii.botania.common.integration.crafttweaker.natives.ExpandBrew;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.function.Function;

/**
 * @docParam this <recipetype:botania:brew>
 */
@Document("mods/Botania/recipe/manager/BrewRecipeManager")
@ZenRegister
@IRecipeHandler.For(IBrewRecipe.class)
@ZenCodeType.Name("mods.botania.recipe.manager.BrewRecipeManager")
public class BrewRecipeManager implements IRecipeManager<IBrewRecipe>, IRecipeHandler<IBrewRecipe> {

	/**
	 * Adds the specified brew recipe.
	 *
	 * @param name   Name of the recipe to add
	 * @param output Output brew
	 * @param inputs Input items
	 * @docParam name "rune_altar_test"
	 * @docParam output <brew:botania:bloodthirst>
	 * @docParam inputs <item:minecraft:dirt>, <item:minecraft:apple>
	 */
	@ZenCodeType.Method
	public void addRecipe(String name, Brew output, IIngredient... inputs) {
		name = fixRecipeName(name);
		ResourceLocation resourceLocation = CraftTweakerConstants.rl(name);
		CraftTweakerAPI.apply(new ActionAddRecipe<>(this,
				new RecipeBrew(resourceLocation,
						output,
						Arrays.stream(inputs).map(IIngredient::asVanillaIngredient).toArray(Ingredient[]::new))));
	}

	/**
	 * Removes recipes that output the given {@link Brew}
	 *
	 * @param output The output to remove.
	 * @docParam output <brew:botania:bloodthirst>
	 */
	@ZenCodeType.Method
	public void remove(Brew output) {
		CraftTweakerAPI.apply(new ActionRemoveRecipe<>(this, recipe -> recipe.getBrew() == output));
	}

	@Override
	public RecipeType<IBrewRecipe> getRecipeType() {
		return ModRecipeTypes.BREW_TYPE;
	}

	@Override
	public String dumpToCommandString(@SuppressWarnings("rawtypes") IRecipeManager manager, IBrewRecipe recipe) {
		StringJoiner s = new StringJoiner(", ", manager.getCommandString() + ".addRecipe(", ");");

		s.add(StringUtil.quoteAndEscape(recipe.getId()));
		s.add(ExpandBrew.getCommandString(recipe.getBrew()));
		recipe.getIngredients().stream()
				.map(IIngredient::fromIngredient)
				.map(IIngredient::getCommandString)
				.forEach(s::add);
		return s.toString();
	}

	@Override
	public Optional<Function<ResourceLocation, IBrewRecipe>> replaceIngredients(@SuppressWarnings("rawtypes") IRecipeManager manager, IBrewRecipe recipe, List<IReplacementRule> rules) {
		return ReplacementHandlerHelper.replaceNonNullIngredientList(recipe.getIngredients(),
				Ingredient.class, recipe, rules,
				ingr -> id -> new RecipeBrew(id, recipe.getBrew(), ingr.toArray(new Ingredient[0])));
	}
}
