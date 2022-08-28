package vazkii.botania.client.integration.jei.orechid;

import mezz.jei.api.helpers.IGuiHelper;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.botania.common.crafting.RecipeMarimorphosis;
import vazkii.botania.common.lib.LibMisc;

public class MarimorphosisRecipeCategory extends OrechidRecipeCategoryBase<RecipeMarimorphosis> {
	public static final mezz.jei.api.recipe.RecipeType<RecipeMarimorphosis> TYPE =
			mezz.jei.api.recipe.RecipeType.create(LibMisc.MOD_ID, "marimorphosis", RecipeMarimorphosis.class);

	public MarimorphosisRecipeCategory(IGuiHelper guiHelper) {
		super(guiHelper, new ItemStack(ModSubtiles.marimorphosis), Component.translatable("botania.nei.marimorphosis"));
	}

	@NotNull
	@Override
	public mezz.jei.api.recipe.RecipeType<RecipeMarimorphosis> getRecipeType() {
		return TYPE;
	}

	@Override
	protected RecipeType<RecipeMarimorphosis> recipeType() {
		return ModRecipeTypes.MARIMORPHOSIS_TYPE;
	}
}
