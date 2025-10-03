/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.jei.orechid;

import mezz.jei.api.helpers.IGuiHelper;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.OrechidRecipe;
import vazkii.botania.common.block.BotaniaBlocks;

public class OrechidRecipeCategory extends OrechidRecipeCategoryBase {
	public static final mezz.jei.api.recipe.RecipeType<OrechidRecipe> TYPE =
			mezz.jei.api.recipe.RecipeType.create(BotaniaAPI.MODID, "orechid", OrechidRecipe.class);

	public OrechidRecipeCategory(IGuiHelper guiHelper) {
		super(guiHelper, new ItemStack(BotaniaBlocks.orechid), Component.translatable("botania.nei.orechid"));
	}

	@Override
	public mezz.jei.api.recipe.RecipeType<OrechidRecipe> getRecipeType() {
		return TYPE;
	}
}
