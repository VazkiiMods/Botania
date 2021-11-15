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
import com.blamejared.crafttweaker.api.util.StringUtils;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionAddRecipe;
import com.blamejared.crafttweaker.impl_native.blocks.ExpandBlockState;
import com.blamejared.crafttweaker_annotations.annotations.Document;

import net.minecraft.block.BlockState;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;

import org.openzen.zencode.java.ZenCodeType;

import vazkii.botania.api.recipe.IPureDaisyRecipe;
import vazkii.botania.api.recipe.StateIngredient;
import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.botania.common.crafting.RecipePureDaisy;
import vazkii.botania.common.integration.crafttweaker.actions.ActionRemovePureDaisyRecipe;

import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.function.Function;

/**
 * @docParam this <recipetype:botania:pure_daisy>
 */
@Document("mods/Botania/PureDaisy")
@ZenRegister
@IRecipeHandler.For(RecipePureDaisy.class)
@ZenCodeType.Name("mods.botania.PureDaisy")
public class PureDaisyRecipeManager implements IRecipeManager, IRecipeHandler<RecipePureDaisy> {

	/**
	 * Adds a Pure Daisy conversion recipe.
	 *
	 * @param name   Name of the recipe to add
	 * @param output Output block state
	 * @param input  Input ingredient
	 * @param time   Optional conversion time (note that the real time is multiplied by 8)
	 *
	 * @docParam name "pure_daisy_test"
	 * @docParam output <blockstate:minecraft:diamond_block>
	 * @docParam input <blockstate:minecraft:gold_block>
	 * @docParam time 50
	 */
	@ZenCodeType.Method
	public void addRecipe(String name, BlockState output, StateIngredient input, @ZenCodeType.OptionalInt(RecipePureDaisy.DEFAULT_TIME) int time) {
		name = fixRecipeName(name);
		ResourceLocation resourceLocation = new ResourceLocation("crafttweaker", name);
		CraftTweakerAPI.apply(new ActionAddRecipe(this,
				new RecipePureDaisy(resourceLocation, input, output, time), ""));
	}

	/**
	 * Removes recipes by output block state
	 * 
	 * @param state Output block state
	 * @docParam state <blockstate:botania:livingrock>
	 */
	@ZenCodeType.Method
	public void removeRecipe(BlockState state) {
		CraftTweakerAPI.apply(new ActionRemovePureDaisyRecipe(this, state));
	}

	@Override
	public void removeRecipe(IItemStack output) {
		throw new UnsupportedOperationException(
				"The Pure Daisy does not output IItemStacks, use removeRecipeByBlock(BlockState)!");
	}

	@Override
	public void removeRecipe(IIngredient output) {
		throw new UnsupportedOperationException(
				"The Pure Daisy does not output IItemStacks, use removeRecipeByBlock(BlockState)!");
	}

	@Override
	public IRecipeType<IPureDaisyRecipe> getRecipeType() {
		return ModRecipeTypes.PURE_DAISY_TYPE;
	}

	@Override
	public String dumpToCommandString(IRecipeManager manager, RecipePureDaisy recipe) {
		StringJoiner s = new StringJoiner(", ", manager.getCommandString() + ".addRecipe(", ");");

		s.add(StringUtils.quoteAndEscape(recipe.getId()));
		s.add(ExpandBlockState.getCommandString(recipe.getOutputState()));
		s.add(CTIntegration.ingredientToCommandString(recipe.getInput()));
		if (recipe.getTime() != RecipePureDaisy.DEFAULT_TIME) {
			s.add(String.valueOf(recipe.getTime()));
		}
		return s.toString();
	}

	@Override
	public Optional<Function<ResourceLocation, RecipePureDaisy>> replaceIngredients(IRecipeManager manager, RecipePureDaisy recipe, List<IReplacementRule> rules) {
		return Optional.empty(); // Not supported by CT at the moment
	}
}
