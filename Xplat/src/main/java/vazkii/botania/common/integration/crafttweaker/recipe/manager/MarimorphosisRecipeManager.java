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
import com.blamejared.crafttweaker.api.block.CTBlockIngredient;
import com.blamejared.crafttweaker.api.ingredient.IIngredient;
import com.blamejared.crafttweaker.api.recipe.handler.IRecipeHandler;
import com.blamejared.crafttweaker.api.recipe.handler.IReplacementRule;
import com.blamejared.crafttweaker.api.recipe.manager.base.IRecipeManager;
import com.blamejared.crafttweaker.api.util.StringUtil;
import com.blamejared.crafttweaker.natives.block.ExpandBlock;
import com.blamejared.crafttweaker_annotations.annotations.Document;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import org.openzen.zencode.java.ZenCodeType;

import vazkii.botania.api.recipe.IOrechidRecipe;
import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.botania.common.crafting.RecipeMarimorphosis;
import vazkii.botania.common.integration.crafttweaker.CTPlugin;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @docParam this <recipetype:botania:marimorphosis>
 */
@Document("mods/Botania/recipe/manager/MarimorphosisRecipeManager")
@ZenRegister
@IRecipeHandler.For(RecipeMarimorphosis.class)
@ZenCodeType.Name("mods.botania.recipe.manager.MarimorphosisRecipeManager")
public class MarimorphosisRecipeManager implements IRecipeManager<IOrechidRecipe>, IRecipeHandler<RecipeMarimorphosis> {

	/**
	 * Registers a new ore weight.
	 *
	 * @param name        The name of the weight
	 * @param output      The blocks to output
	 * @param input       The input block
	 * @param weight      The weight
	 * @param weightBonus The bonus weight
	 * @docParam biomes the biomes to apply the extra weight in
	 * @docParam name "orechid_test"
	 * @docParam output <blockstate:minecraft:dirt>
	 * @docParam input <block:minecraft:diamond_ore>
	 * @docParam weight 50
	 * @docParam weightBonus 10
	 * @docParam biomes <constant:minecraft:world/biome/category:beach>
	 */
	@ZenCodeType.Method
	public void registerOreWeight(String name, CTBlockIngredient output, Block input, int weight, int weightBonus, Biome.BiomeCategory... biomes) {
		name = fixRecipeName(name);
		ResourceLocation resourceLocation = CraftTweakerConstants.rl(name);
		CraftTweakerAPI.apply(new ActionAddRecipe<>(this,
				new RecipeMarimorphosis(resourceLocation,
						input, CTPlugin.blockIngredientToStateIngredient(output), weight, weightBonus, Arrays.stream(biomes).toList())));
	}

	/**
	 * Removes marimorphosis weights that output the given blockstate.
	 *
	 * @param output The blockstate output to remove.
	 * @docParam output <blockstate:minecraft:dirt>
	 */
	@ZenCodeType.Method
	public void remove(BlockState output) {
		CraftTweakerAPI.apply(new ActionRemoveRecipe<>(this, recipe -> recipe.getOutput().test(output)));
	}

	@Override
	public void remove(IIngredient output) {
		throw new UnsupportedOperationException(
				"Orechid does not output IItemStacks, use remove(BlockState)!");
	}

	@Override
	public RecipeType<IOrechidRecipe> getRecipeType() {
		return ModRecipeTypes.MARIMORPHOSIS_TYPE;
	}

	@Override
	public String dumpToCommandString(@SuppressWarnings("rawtypes") IRecipeManager manager, RecipeMarimorphosis recipe) {
		StringJoiner s = new StringJoiner(", ", manager.getCommandString() + ".addRecipe(", ");");

		s.add(StringUtil.quoteAndEscape(recipe.getId()));
		s.add(CTPlugin.ingredientToCommandString(recipe.getOutput()));
		s.add(ExpandBlock.getCommandString(recipe.getInput()));
		s.add(String.valueOf(recipe.getWeight()));
		s.add(String.valueOf(recipe.getWeightBonus()));
		s.add(recipe.getBiomes().stream().map(biomeCategory -> "<constant:minecraft:world/biome/category:" + biomeCategory.name().toLowerCase(Locale.ENGLISH) + ">").collect(Collectors.joining(", ")));
		return s.toString();
	}

	@Override
	public Optional<Function<ResourceLocation, RecipeMarimorphosis>> replaceIngredients(@SuppressWarnings("rawtypes") IRecipeManager manager, RecipeMarimorphosis recipe, List<IReplacementRule> rules) {
		return Optional.empty();
	}
}
