package vazkii.botania.client.integration.emi;

import dev.emi.emi.api.recipe.EmiCraftingRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;

import net.minecraft.resources.ResourceLocation;

import vazkii.botania.common.crafting.recipe.TiaraWingsRecipe;
import vazkii.botania.common.item.BotaniaItems;

import java.util.List;

public class TiaraWingsEmiRecipe extends EmiCraftingRecipe {
	public TiaraWingsEmiRecipe(TiaraWingsRecipe recipe, ResourceLocation id) {
		super(List.of(EmiStack.of(BotaniaItems.flightTiara), EmiIngredient.of(recipe.material())),
				EmiStack.of(recipe.getResultItem(BotaniaEmiRecipe.getRegistryAccess())),
				id);
	}
}
