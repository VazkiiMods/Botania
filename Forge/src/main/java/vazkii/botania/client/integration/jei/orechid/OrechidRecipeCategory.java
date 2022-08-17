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

import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.botania.common.crafting.RecipeOrechid;
import vazkii.botania.common.lib.LibMisc;

public class OrechidRecipeCategory extends OrechidRecipeCategoryBase<RecipeOrechid> {
	public static final mezz.jei.api.recipe.RecipeType<RecipeOrechid> TYPE =
			mezz.jei.api.recipe.RecipeType.create(LibMisc.MOD_ID, "orechid", RecipeOrechid.class);

	public OrechidRecipeCategory(IGuiHelper guiHelper) {
		super(guiHelper, new ItemStack(ModSubtiles.orechid), Component.translatable("botania.nei.orechid"));
	}

	@NotNull
	@Override
	public mezz.jei.api.recipe.RecipeType<RecipeOrechid> getRecipeType() {
		return TYPE;
	}

	@Override
	protected RecipeType<RecipeOrechid> recipeType() {
		return ModRecipeTypes.ORECHID_TYPE;
	}
}
