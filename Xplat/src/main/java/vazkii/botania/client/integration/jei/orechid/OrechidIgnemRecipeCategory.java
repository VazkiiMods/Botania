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
import net.minecraft.world.item.crafting.RecipeType;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.common.block.BotaniaFlowerBlocks;
import vazkii.botania.common.crafting.BotaniaRecipeTypes;
import vazkii.botania.common.crafting.OrechidIgnemRecipe;
import vazkii.botania.common.lib.LibMisc;

public class OrechidIgnemRecipeCategory extends OrechidRecipeCategoryBase<OrechidIgnemRecipe> {

	public static final mezz.jei.api.recipe.RecipeType<OrechidIgnemRecipe> TYPE =
			mezz.jei.api.recipe.RecipeType.create(LibMisc.MOD_ID, "orechid_ignem", OrechidIgnemRecipe.class);

	public OrechidIgnemRecipeCategory(IGuiHelper guiHelper) {
		super(guiHelper, new ItemStack(BotaniaFlowerBlocks.orechidIgnem), Component.translatable("botania.nei.orechidIgnem"));
	}

	@NotNull
	@Override
	public mezz.jei.api.recipe.RecipeType<OrechidIgnemRecipe> getRecipeType() {
		return TYPE;
	}

	@Override
	protected RecipeType<OrechidIgnemRecipe> recipeType() {
		return BotaniaRecipeTypes.ORECHID_IGNEM_TYPE;
	}
}
