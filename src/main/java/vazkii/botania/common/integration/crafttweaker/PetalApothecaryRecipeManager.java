/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.integration.crafttweaker;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;
import com.blamejared.crafttweaker.api.recipes.IRecipeHandler;
import com.blamejared.crafttweaker.api.recipes.IReplacementRule;
import com.blamejared.crafttweaker.api.recipes.ReplacementHandlerHelper;
import com.blamejared.crafttweaker.api.util.StringUtils;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionAddRecipe;
import com.blamejared.crafttweaker.impl.item.MCItemStackMutable;
import com.blamejared.crafttweaker_annotations.annotations.Document;

import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

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
@Document("mods/Botania/PetalApothecary")
@ZenRegister
@IRecipeHandler.For(RecipePetals.class)
@ZenCodeType.Name("mods.botania.PetalApothecary")
public class PetalApothecaryRecipeManager implements IRecipeManager, IRecipeHandler<RecipePetals> {

	/**
	 * Adds the specified petal apothecary recipe.
	 *
	 * @param name   Name of the recipe to add
	 * @param output Output item
	 * @param inputs Input items
	 *
	 * @docParam name "rune_altar_test"
	 * @docParam output <item:minecraft:diamond>
	 * @docParam mana 2000
	 * @docParam inputs <item:minecraft:dirt>, <item:minecraft:apple>
	 */
	@ZenCodeType.Method
	public void addRecipe(String name, IItemStack output, IIngredient... inputs) {
		name = fixRecipeName(name);
		ResourceLocation resourceLocation = new ResourceLocation("crafttweaker", name);
		CraftTweakerAPI.apply(new ActionAddRecipe(this,
				new RecipePetals(resourceLocation,
						output.getInternal(),
						Arrays.stream(inputs).map(IIngredient::asVanillaIngredient).toArray(Ingredient[]::new)),
				""));
	}

	@Override
	public IRecipeType<IPetalRecipe> getRecipeType() {
		return ModRecipeTypes.PETAL_TYPE;
	}

	@Override
	public String dumpToCommandString(IRecipeManager manager, RecipePetals recipe) {
		StringJoiner s = new StringJoiner(", ", manager.getCommandString() + ".addRecipe(", ");");

		s.add(StringUtils.quoteAndEscape(recipe.getId()));
		s.add(new MCItemStackMutable(recipe.getRecipeOutput()).getCommandString());
		recipe.getIngredients().stream()
				.map(IIngredient::fromIngredient)
				.map(IIngredient::getCommandString)
				.forEach(s::add);
		return s.toString();
	}

	@Override
	public Optional<Function<ResourceLocation, RecipePetals>> replaceIngredients(IRecipeManager manager, RecipePetals recipe, List<IReplacementRule> rules) {
		return ReplacementHandlerHelper.replaceNonNullIngredientList(recipe.getIngredients(),
				Ingredient.class, recipe, rules,
				ingr -> id -> new RecipePetals(id, recipe.getRecipeOutput(), ingr.toArray(new Ingredient[0])));
	}
}
