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
import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.ingredient.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.recipe.handler.IRecipeHandler;
import com.blamejared.crafttweaker.api.recipe.handler.IReplacementRule;
import com.blamejared.crafttweaker.api.recipe.handler.helper.ReplacementHandlerHelper;
import com.blamejared.crafttweaker.api.recipe.manager.base.IRecipeManager;
import com.blamejared.crafttweaker.api.util.StringUtil;
import com.blamejared.crafttweaker.platform.Services;
import com.blamejared.crafttweaker_annotations.annotations.Document;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;

import org.openzen.zencode.java.ZenCodeType;

import vazkii.botania.api.recipe.IPetalRecipe;
import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.botania.common.crafting.RecipePetals;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.function.Function;

/**
 * @docParam this <recipetype:botania:petal_apothecary>
 */
@Document("mods/Botania/recipe/manager/PetalApothecaryRecipeManager")
@ZenRegister
@IRecipeHandler.For(IPetalRecipe.class)
@ZenCodeType.Name("mods.botania.recipe.manager.PetalApothecaryRecipeManager")
public class PetalApothecaryRecipeManager implements IRecipeManager<IPetalRecipe>, IRecipeHandler<IPetalRecipe> {

	/**
	 * Adds the specified petal apothecary recipe.
	 *
	 * @param name   Name of the recipe to add
	 * @param output Output item
	 * @param inputs Input items
	 * @docParam name "rune_altar_test"
	 * @docParam output <item:minecraft:diamond>
	 * @docParam mana 2000
	 * @docParam inputs <item:minecraft:dirt>, <item:minecraft:apple>
	 */
	@ZenCodeType.Method
	public void addRecipe(String name, IItemStack output, IIngredient... inputs) {
		name = fixRecipeName(name);
		ResourceLocation resourceLocation = CraftTweakerConstants.rl(name);
		CraftTweakerAPI.apply(new ActionAddRecipe<>(this,
				new RecipePetals(resourceLocation,
						output.getInternal(),
						Arrays.stream(inputs).map(IIngredient::asVanillaIngredient).toArray(Ingredient[]::new))));
	}

	@Override
	public RecipeType<IPetalRecipe> getRecipeType() {
		return ModRecipeTypes.PETAL_TYPE;
	}

	@Override
	public String dumpToCommandString(@SuppressWarnings("rawtypes") IRecipeManager manager, IPetalRecipe recipe) {
		StringJoiner s = new StringJoiner(", ", manager.getCommandString() + ".addRecipe(", ");");

		s.add(StringUtil.quoteAndEscape(recipe.getId()));
		s.add(Services.PLATFORM.createMCItemStack(recipe.getResultItem()).getCommandString());
		recipe.getIngredients().stream()
				.map(IIngredient::fromIngredient)
				.map(IIngredient::getCommandString)
				.forEach(s::add);
		return s.toString();
	}

	@Override
	public Optional<Function<ResourceLocation, IPetalRecipe>> replaceIngredients(@SuppressWarnings("rawtypes") IRecipeManager manager, IPetalRecipe recipe, List<IReplacementRule> rules) {
		return ReplacementHandlerHelper.replaceNonNullIngredientList(recipe.getIngredients(),
				Ingredient.class, recipe, rules,
				ingr -> id -> new RecipePetals(id, recipe.getResultItem(), ingr.toArray(new Ingredient[0])));
	}
}
