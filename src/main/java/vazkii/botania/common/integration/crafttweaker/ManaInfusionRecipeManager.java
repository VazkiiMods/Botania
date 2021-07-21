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
import com.blamejared.crafttweaker.impl.actions.recipes.ActionAddRecipe;
import com.blamejared.crafttweaker.impl.item.MCItemStack;
import com.blamejared.crafttweaker_annotations.annotations.Document;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;

import org.openzen.zencode.java.ZenCodeType;

import vazkii.botania.api.recipe.IManaInfusionRecipe;
import vazkii.botania.api.recipe.StateIngredient;
import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.botania.common.crafting.RecipeManaInfusion;

import javax.annotation.Nonnull;

/**
 * @docParam this <recipetype:botania:mana_infusion>
 */
@Document("mods/Botania/ManaInfusion")
@ZenRegister
@ZenCodeType.Name("mods.botania.ManaInfusion")
public class ManaInfusionRecipeManager implements IRecipeManager {

	// TODO: remember to manually fix the docs for this one because of CraftTweaker/CraftTweaker#1277
	/**
	 * Adds a mana infusion recipe.
	 *
	 * @param name     Name of the recipe to add
	 * @param output   Output item
	 * @param input    Input item
	 * @param mana     Required mana
	 * @param catalyst Optional catalyst required under the mana pool for this recipe to craft
	 * @param group    Optional recipe group, use if you want to replace a grouped recipe in the lexicon
	 * @param function Optional function modifying the output
	 *
	 * @docParam name "mana_infusion_test_catalyst"
	 * @docParam output <item:minecraft:diamond>
	 * @docParam input <item:minecraft:apple>
	 * @docParam mana 200
	 * @docParam catalyst <block:botania:alchemy_catalyst>
	 * @docParam group null
	 * @docParam function (usualOut as IItemStack, input as IItemStack) => { return usualOut.withTag(input.tag); }
	 */
	@ZenCodeType.Method
	public void addRecipe(String name, IItemStack output, IIngredient input, int mana, @ZenCodeType.Optional StateIngredient catalyst, @ZenCodeType.OptionalString String group, @ZenCodeType.Optional RecipeFunctionSingle function) {
		name = fixRecipeName(name);
		ResourceLocation resourceLocation = new ResourceLocation("crafttweaker", name);
		if (group == null) {
			group = "";
		}
		RecipeManaInfusion recipe;
		if (function == null) {
			recipe = new RecipeManaInfusion(resourceLocation, output.getInternal(), input.asVanillaIngredient(), mana, group, catalyst);
		} else {
			recipe = new RecipeManaInfusion(resourceLocation, output.getInternal(), input.asVanillaIngredient(), mana, group, catalyst) {
				@Nonnull
				@Override
				public ItemStack getRecipeOutput(@Nonnull ItemStack input) {
					return function.process(new MCItemStack(getRecipeOutput()), new MCItemStack(input)).getInternal().copy();
				}
			};
		}
		CraftTweakerAPI.apply(new ActionAddRecipe(this, recipe, ""));
	}

	@Override
	public IRecipeType<IManaInfusionRecipe> getRecipeType() {
		return ModRecipeTypes.MANA_INFUSION_TYPE;
	}
}
