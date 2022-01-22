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

import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Blocks;

import vazkii.botania.api.recipe.IOrechidRecipe;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.botania.common.crafting.RecipeOrechid;

import javax.annotation.Nonnull;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class OrechidRecipeCategory extends OrechidRecipeCategoryBase {
	public static final ResourceLocation UID = prefix("orechid");

	public OrechidRecipeCategory(IGuiHelper guiHelper) {
		super(guiHelper, new ItemStack(ModSubtiles.orechid), new ItemStack(Blocks.STONE, 64),
				new TranslatableComponent("botania.nei.orechid"));
	}

	@Nonnull
	@Override
	public ResourceLocation getUid() {
		return UID;
	}

	@Override
	public Class<? extends IOrechidRecipe> getRecipeClass() {
		return RecipeOrechid.class;
	}

	@Override
	protected RecipeType<? extends IOrechidRecipe> recipeType() {
		return ModRecipeTypes.ORECHID_TYPE;
	}
}
