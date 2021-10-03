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

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import org.openzen.zencode.java.ZenCodeType;

import vazkii.botania.api.recipe.IElvenTradeRecipe;
import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.botania.common.crafting.RecipeElvenTrade;
import vazkii.botania.common.integration.crafttweaker.actions.ActionRemoveElvenTradeRecipe;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @docParam this <recipetype:botania:elven_trade>
 */
@Document("mods/Botania/ElvenTrade")
@ZenRegister
@IRecipeHandler.For(RecipeElvenTrade.class)
@ZenCodeType.Name("mods.botania.ElvenTrade")
public class ElvenTradeRecipeManager implements IRecipeManager, IRecipeHandler<RecipeElvenTrade> {

	/**
	 * Adds an elven trade recipe.
	 *
	 * @param name    Name of the recipe to add
	 * @param outputs Array of outputs
	 * @param inputs  Inputs for the recipe
	 *
	 * @docParam name "elven_trade_test"
	 * @docParam outputs [<item:minecraft:apple>, <item:minecraft:lapis_block>]
	 * @docParam inputs <item:minecraft:glowstone>, <item:minecraft:yellow_wool>
	 */
	@ZenCodeType.Method
	public void addRecipe(String name, IItemStack[] outputs, IIngredient... inputs) {
		name = fixRecipeName(name);
		ResourceLocation resourceLocation = new ResourceLocation("crafttweaker", name);
		CraftTweakerAPI.apply(new ActionAddRecipe(this,
				new RecipeElvenTrade(resourceLocation,
						Arrays.stream(outputs).map(IItemStack::getInternal).toArray(ItemStack[]::new),
						Arrays.stream(inputs).map(IIngredient::asVanillaIngredient).toArray(Ingredient[]::new)),
				""));
	}

	/**
	 * Removes a single-output recipe.
	 *
	 * @param output Recipe output
	 *
	 * @docParam output <item:botania:dragonstone>
	 */
	@Override
	@ZenCodeType.Method
	public void removeRecipe(IItemStack output) {
		removeRecipe(new IItemStack[] { output });
	}

	/**
	 * Removes a recipe with multiple outputs.
	 *
	 * Note that as Botania does not come with any multiple-output recipes, this example will not work out of the box.
	 *
	 * @param outputs Recipe outputs
	 *
	 * @docParam outputs [<item:botania:dragonstone>, <item:minecraft:diamond>]
	 */
	@ZenCodeType.Method
	public void removeRecipe(IItemStack[] outputs) {
		CraftTweakerAPI.apply(new ActionRemoveElvenTradeRecipe(this, outputs));
	}

	@Override
	public IRecipeType<IElvenTradeRecipe> getRecipeType() {
		return ModRecipeTypes.ELVEN_TRADE_TYPE;
	}

	@Override
	public String dumpToCommandString(IRecipeManager manager, RecipeElvenTrade recipe) {
		StringJoiner s = new StringJoiner(", ", manager.getCommandString() + ".addRecipe(", ");");

		s.add(StringUtils.quoteAndEscape(recipe.getId()));
		s.add(recipe.getOutputs().stream()
				.map(MCItemStackMutable::new)
				.map(MCItemStackMutable::getCommandString)
				.collect(Collectors.joining(", ", "[", "]")));
		recipe.getIngredients().stream()
				.map(IIngredient::fromIngredient)
				.map(IIngredient::getCommandString)
				.forEach(s::add);
		return s.toString();
	}

	@Override
	public Optional<Function<ResourceLocation, RecipeElvenTrade>> replaceIngredients(IRecipeManager manager, RecipeElvenTrade recipe, List<IReplacementRule> rules) {
		if ((recipe.getOutputs().size() == 1
				&& recipe.getIngredients().size() == 1
				&& recipe.containsItem(recipe.getOutputs().get(0)))) {
			return Optional.empty();
		}

		return ReplacementHandlerHelper.replaceNonNullIngredientList(recipe.getIngredients(),
				Ingredient.class, recipe, rules,
				ingr -> id -> new RecipeElvenTrade(id, recipe.getOutputs().toArray(new ItemStack[0]), ingr.toArray(new Ingredient[0])));
	}
}
