package vazkii.botania.client.integration.emi;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class BotaniaEmiRecipe implements EmiRecipe {
	private final EmiRecipeCategory category;
	private final ResourceLocation id;
	protected List<EmiIngredient> input = List.of();
	protected List<EmiIngredient> catalysts = List.of();
	protected List<EmiStack> output = List.of();
	private final String group;

	public BotaniaEmiRecipe(EmiRecipeCategory category, Recipe<?> recipe) {
		this.category = category;
		this.id = recipe.getId();
		this.group = recipe.getGroup();
	}

	@Override
	public EmiRecipeCategory getCategory() {
		return category;
	}

	@Override
	public @Nullable ResourceLocation getId() {
		return id;
	}

	@Override
	public List<EmiIngredient> getInputs() {
		return input;
	}

	@Override
	public List<EmiIngredient> getCatalysts() {
		return catalysts;
	}

	@Override
	public List<EmiStack> getOutputs() {
		return output;
	}

	public String getGroup() {
		return group;
	}
}
