/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package org.violetmoon.botania.client.integration.jei.orechid;

import mezz.jei.api.helpers.IGuiHelper;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import org.violetmoon.botania.api.BotaniaAPI;
import org.violetmoon.botania.api.recipe.OrechidRecipe;
import org.violetmoon.botania.common.block.BotaniaBlocks;

public class MarimorphosisRecipeCategory extends OrechidRecipeCategoryBase {
	public static final mezz.jei.api.recipe.RecipeType<OrechidRecipe> TYPE =
			mezz.jei.api.recipe.RecipeType.create(BotaniaAPI.MODID, "marimorphosis", OrechidRecipe.class);

	public MarimorphosisRecipeCategory(IGuiHelper guiHelper) {
		super(guiHelper, new ItemStack(BotaniaBlocks.MARIMORPHOSIS), Component.translatable("botania.nei.marimorphosis"));
	}

	@Override
	public mezz.jei.api.recipe.RecipeType<OrechidRecipe> getRecipeType() {
		return TYPE;
	}
}
