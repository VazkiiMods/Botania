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

/**
 * @docParam this <recipetype:botania:elven_trade>
 */
@Document("mods/Botania/ElvenTrade")
@ZenRegister
@ZenCodeType.Name("mods.botania.ElvenTrade")
public class ElvenTradeRecipeManager implements IRecipeManager {

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
	 * @docParam [<item:botania:dragonstone>, <item:minecraft:diamond>]
	 */
	@ZenCodeType.Method
	public void removeRecipe(IItemStack[] outputs) {
		CraftTweakerAPI.apply(new ActionRemoveElvenTradeRecipe(this, outputs));
	}

	@Override
	public IRecipeType<IElvenTradeRecipe> getRecipeType() {
		return ModRecipeTypes.ELVEN_TRADE_TYPE;
	}
}
