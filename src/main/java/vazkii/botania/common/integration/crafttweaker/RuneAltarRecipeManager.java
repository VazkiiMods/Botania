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
import com.blamejared.crafttweaker_annotations.annotations.Document;

import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import org.openzen.zencode.java.ZenCodeType;

import vazkii.botania.api.recipe.IRuneAltarRecipe;
import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.botania.common.crafting.RecipeRuneAltar;

import java.util.Arrays;

/**
 * @docParam this <recipetype:botania:runic_altar>
 */
@Document("mods/Botania/RunicAltar")
@ZenRegister
@ZenCodeType.Name("mods.botania.RuneAltar")
public class RuneAltarRecipeManager implements IRecipeManager {

	/**
	 * Adds the specified runic altar recipe.
	 *
	 * @param name   Name of the recipe to add
	 * @param output Output item
	 * @param mana   Recipe mana cost
	 * @param inputs Input items
	 *
	 * @docParam name "rune_altar_test"
	 * @docParam output <item:minecraft:diamond>
	 * @docParam mana 5000
	 * @docParam inputs <item:botania:rune_air>, <item:botania:orange_petal>, <item:botania:red_petal>
	 */
	@ZenCodeType.Method
	public void addRecipe(String name, IItemStack output, int mana, IIngredient... inputs) {
		name = fixRecipeName(name);
		ResourceLocation resourceLocation = new ResourceLocation("crafttweaker", name);
		CraftTweakerAPI.apply(new ActionAddRecipe(this,
				new RecipeRuneAltar(resourceLocation,
						output.getInternal(),
						mana,
						Arrays.stream(inputs).map(IIngredient::asVanillaIngredient).toArray(Ingredient[]::new)),
				""));
	}

	@Override
	public IRecipeType<IRuneAltarRecipe> getRecipeType() {
		return ModRecipeTypes.RUNE_TYPE;
	}
}
