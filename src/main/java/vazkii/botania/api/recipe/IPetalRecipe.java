package vazkii.botania.api.recipe;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import vazkii.botania.api.BotaniaAPI;

import javax.annotation.Nonnull;

public interface IPetalRecipe extends IRecipe<RecipeWrapper> {
	ResourceLocation TYPE_ID = new ResourceLocation(BotaniaAPI.MODID, "petal_apothecary");

	@Nonnull
	@Override
	default IRecipeType<?> getType() {
		return Registry.RECIPE_TYPE.getValue(TYPE_ID).get();
	}

	@Override
	default boolean canFit(int width, int height) {
		return false;
	}
}
