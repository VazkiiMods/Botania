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
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionAddRecipe;
import com.blamejared.crafttweaker.impl.blocks.MCBlock;
import com.blamejared.crafttweaker.impl.blocks.MCBlockState;
import com.blamejared.crafttweaker.impl.tag.MCTag;

import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;

import org.openzen.zencode.java.ZenCodeType;

import vazkii.botania.api.recipe.IPureDaisyRecipe;
import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.botania.common.crafting.RecipePureDaisy;
import vazkii.botania.common.crafting.StateIngredientHelper;
import vazkii.botania.common.integration.crafttweaker.actions.ActionRemovePureDaisyRecipe;

import java.util.Arrays;
import java.util.stream.Collectors;

@ZenRegister
@ZenCodeType.Name("mods.botania.PureDaisy")
public class PureDaisyRecipeManager implements IRecipeManager {

	@ZenCodeType.Method
	public void addRecipe(String name, MCBlockState output, MCBlockState input, int time) {
		name = fixRecipeName(name);
		ResourceLocation resourceLocation = new ResourceLocation("crafttweaker", name);
		CraftTweakerAPI.apply(new ActionAddRecipe(this,
				new RecipePureDaisy(resourceLocation,
						StateIngredientHelper.of(input.getInternal()),
						output.getInternal(), time),
				""));
	}

	@ZenCodeType.Method
	public void addRecipe(String name, MCBlockState output, MCBlock[] inputs, int time) {
		name = fixRecipeName(name);
		ResourceLocation resourceLocation = new ResourceLocation("crafttweaker", name);
		CraftTweakerAPI.apply(new ActionAddRecipe(this,
				new RecipePureDaisy(resourceLocation,
						StateIngredientHelper.of(
								Arrays.stream(inputs).map(MCBlock::getInternal).collect(Collectors.toSet())),
						output.getInternal(), time),
				""));
	}

	@ZenCodeType.Method
	public void addRecipe(String name, MCBlockState output, MCTag<MCBlock> input, int time) {
		name = fixRecipeName(name);
		ResourceLocation resourceLocation = new ResourceLocation("crafttweaker", name);
		CraftTweakerAPI.apply(new ActionAddRecipe(this,
				new RecipePureDaisy(resourceLocation,
						StateIngredientHelper.of(input.getIdInternal()),
						output.getInternal(), time),
				""));
	}

	@ZenCodeType.Method
	public void removeRecipe(MCBlockState state) {
		CraftTweakerAPI.apply(new ActionRemovePureDaisyRecipe(this, state));
	}

	@Override
	public void removeRecipe(IItemStack output) {

		throw new IllegalArgumentException(
				"The Pure Daisy does not output IItemStacks, use removeRecipeByBlock(MCBlockState)!");
	}

	@Override
	public IRecipeType<IPureDaisyRecipe> getRecipeType() {
		return ModRecipeTypes.PURE_DAISY_TYPE;
	}
}
