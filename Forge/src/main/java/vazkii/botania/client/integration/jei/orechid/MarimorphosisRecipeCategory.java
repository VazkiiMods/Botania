package vazkii.botania.client.integration.jei.orechid;

import mezz.jei.api.helpers.IGuiHelper;

import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;

import vazkii.botania.api.recipe.IOrechidRecipe;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.botania.common.crafting.RecipeMarimorphosis;

import javax.annotation.Nonnull;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class MarimorphosisRecipeCategory extends OrechidRecipeCategoryBase {
	public static final ResourceLocation UID = prefix("marimorphosis");

	public MarimorphosisRecipeCategory(IGuiHelper guiHelper) {
		super(guiHelper, new ItemStack(ModSubtiles.marimorphosis), new TranslatableComponent("botania.nei.marimorphosis"));
	}

	@Nonnull
	@Override
	public ResourceLocation getUid() {
		return UID;
	}

	@Nonnull
	@Override
	public Class<? extends IOrechidRecipe> getRecipeClass() {
		return RecipeMarimorphosis.class;
	}

	@Override
	protected RecipeType<? extends IOrechidRecipe> recipeType() {
		return ModRecipeTypes.MARIMORPHOSIS_TYPE;
	}
}
