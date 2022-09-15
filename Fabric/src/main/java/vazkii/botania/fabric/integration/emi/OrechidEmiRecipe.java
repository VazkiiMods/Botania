package vazkii.botania.fabric.integration.emi;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;

import net.minecraft.world.item.ItemStack;

import vazkii.botania.api.recipe.OrechidRecipe;

import java.util.List;

public class OrechidEmiRecipe extends BotaniaEmiRecipe {
	private final EmiIngredient orechid;

	public OrechidEmiRecipe(EmiRecipeCategory category, OrechidRecipe recipe, int totalWeight, EmiIngredient orechid) {
		super(category, recipe);
		final int weight = recipe.getWeight();
		final int amount = Math.max(1, Math.round((float) (weight * 64) / totalWeight));
		this.input = List.of(EmiStack.of(new ItemStack(recipe.getInput(), 64)));
		try {
			this.output = recipe.getOutput().getDisplayed().stream()
					.map(s -> EmiStack.of(new ItemStack(s.getBlock(), amount))).toList();
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		this.orechid = orechid;
	}

	@Override
	public int getDisplayHeight() {
		return 44;
	}

	@Override
	public int getDisplayWidth() {
		return 76;
	}

	@Override
	public boolean supportsRecipeTree() {
		return false;
	}

	@Override
	public void addWidgets(WidgetHolder widgets) {
		PureDaisyEmiRecipe.addPureDaisyWidgets(widgets, this, input.get(0), orechid, output.get(0));
	}
}
