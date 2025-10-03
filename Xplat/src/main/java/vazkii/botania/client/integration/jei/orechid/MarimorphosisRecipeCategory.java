package vazkii.botania.client.integration.jei.orechid;

import mezz.jei.api.helpers.IGuiHelper;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.OrechidRecipe;
import vazkii.botania.common.block.BotaniaBlocks;

public class MarimorphosisRecipeCategory extends OrechidRecipeCategoryBase {
	public static final mezz.jei.api.recipe.RecipeType<OrechidRecipe> TYPE =
			mezz.jei.api.recipe.RecipeType.create(BotaniaAPI.MODID, "marimorphosis", OrechidRecipe.class);

	public MarimorphosisRecipeCategory(IGuiHelper guiHelper) {
		super(guiHelper, new ItemStack(BotaniaBlocks.marimorphosis), Component.translatable("botania.nei.marimorphosis"));
	}

	@Override
	public mezz.jei.api.recipe.RecipeType<OrechidRecipe> getRecipeType() {
		return TYPE;
	}
}
