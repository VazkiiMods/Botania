/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.integration.crafttweaker.recipe.manager;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.recipe.handler.IRecipeHandler;
import com.blamejared.crafttweaker_annotations.annotations.Document;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;

import org.openzen.zencode.java.ZenCodeType;

import vazkii.botania.api.recipe.IOrechidRecipe;
import vazkii.botania.api.recipe.StateIngredient;
import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.botania.common.crafting.RecipeOrechidIgnem;
import vazkii.botania.common.integration.crafttweaker.recipe.manager.base.IOrechidManagerBase;

/**
 * @docParam this <recipetype:botania:orechid_ignem>
 */
@Document("mods/Botania/recipe/manager/OrechidIgnemRecipeManager")
@ZenRegister
@IRecipeHandler.For(RecipeOrechidIgnem.class)
@ZenCodeType.Name("mods.botania.recipe.manager.OrechidIgnemRecipeManager")
public class OrechidIgnemRecipeManager implements IOrechidManagerBase<IOrechidRecipe> {
	@Override
	public IOrechidRecipe makeRecipe(ResourceLocation name, Block input, StateIngredient output, int weight) {
		return new RecipeOrechidIgnem(name, input, output, weight);
	}

	@Override
	public RecipeType<IOrechidRecipe> getRecipeType() {
		return ModRecipeTypes.ORECHID_IGNEM_TYPE;
	}

}
