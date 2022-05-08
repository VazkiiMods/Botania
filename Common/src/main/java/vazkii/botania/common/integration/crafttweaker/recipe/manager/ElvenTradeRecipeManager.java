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
import com.blamejared.crafttweaker.api.bracket.CommandStringDisplayable;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;

import org.openzen.zencode.java.ZenCodeType;

import vazkii.botania.api.recipe.IElvenTradeRecipe;
import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.botania.common.crafting.RecipeElvenTrade;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @docParam this <recipetype:botania:elven_trade>
 */
@Document("mods/Botania/recipe/manager/ElvenTradeRecipeManager")
@ZenRegister
@IRecipeHandler.For(IElvenTradeRecipe.class)
@ZenCodeType.Name("mods.botania.recipe.manager.ElvenTradeRecipeManager")
public class ElvenTradeRecipeManager implements IRecipeManager<IElvenTradeRecipe>, IRecipeHandler<IElvenTradeRecipe> {

	/**
	 * Adds an elven trade recipe.
	 *
	 * @param name    Name of the recipe to add
	 * @param outputs Array of outputs
	 * @param inputs  Inputs for the recipe
	 * @docParam name "elven_trade_test"
	 * @docParam outputs [<item:minecraft:apple>, <item:minecraft:lapis_block>]
	 * @docParam inputs <item:minecraft:glowstone>, <item:minecraft:yellow_wool>
	 */
	@ZenCodeType.Method
	public void addRecipe(String name, IItemStack[] outputs, IIngredient... inputs) {
		name = fixRecipeName(name);
		ResourceLocation resourceLocation = CraftTweakerConstants.rl(name);
		CraftTweakerAPI.apply(new ActionAddRecipe<>(this,
				new RecipeElvenTrade(resourceLocation,
						Arrays.stream(outputs).map(IItemStack::getInternal).toArray(ItemStack[]::new),
						Arrays.stream(inputs).map(IIngredient::asVanillaIngredient).toArray(Ingredient[]::new))));
	}

	/**
	 * Removes a single-output recipe.
	 *
	 * @param output Recipe output
	 * @docParam output <item:botania:dragonstone>
	 */
	@Override
	@ZenCodeType.Method
	public void remove(IIngredient output) {
		remove(new IIngredient[] { output });
	}

	/**
	 * Removes a recipe with multiple outputs.
	 * <p>
	 * Note that as Botania does not come with any multiple-output recipes, this example will not work out of the box.
	 *
	 * @param outputs Recipe outputs
	 * @docParam outputs [<item:botania:dragonstone>, <item:minecraft:diamond>]
	 */
	@ZenCodeType.Method
	public void remove(IIngredient[] outputs) {
		CraftTweakerAPI.apply(new ActionRemoveRecipe<>(this, recipe -> {
			if (recipe.getOutputs().size() != outputs.length) {
				return false;
			}
			List<IItemStack> collected = recipe.getOutputs().stream().map(Services.PLATFORM::createMCItemStack).toList();
			return IntStream.range(0, outputs.length).allMatch(i -> outputs[i].matches(collected.get(i)));
		}));
	}

	@Override
	public RecipeType<IElvenTradeRecipe> getRecipeType() {
		return ModRecipeTypes.ELVEN_TRADE_TYPE;
	}

	@Override
	public String dumpToCommandString(@SuppressWarnings("rawtypes") IRecipeManager manager, IElvenTradeRecipe recipe) {
		StringJoiner s = new StringJoiner(", ", manager.getCommandString() + ".addRecipe(", ");");

		s.add(StringUtil.quoteAndEscape(recipe.getId()));
		s.add(recipe.getOutputs().stream()
				.map(Services.PLATFORM::createMCItemStack)
				.map(CommandStringDisplayable::getCommandString)
				.collect(Collectors.joining(", ", "[", "]")));
		recipe.getIngredients().stream()
				.map(IIngredient::fromIngredient)
				.map(IIngredient::getCommandString)
				.forEach(s::add);
		return s.toString();
	}

	@Override
	public Optional<Function<ResourceLocation, IElvenTradeRecipe>> replaceIngredients(@SuppressWarnings("rawtypes") IRecipeManager manager, IElvenTradeRecipe recipe, List<IReplacementRule> rules) {
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
