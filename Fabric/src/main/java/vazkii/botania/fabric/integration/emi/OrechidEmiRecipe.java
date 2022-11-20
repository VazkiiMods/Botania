package vazkii.botania.fabric.integration.emi;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import vazkii.botania.api.recipe.OrechidRecipe;

public class OrechidEmiRecipe extends BotaniaEmiRecipe {
	private final EmiIngredient orechid;

	public OrechidEmiRecipe(EmiRecipeCategory category, OrechidRecipe recipe, EmiIngredient orechid) {
		super(category, recipe);
		try {
			this.input = recipe.getInput().getDisplayed().stream()
					.map(s -> EmiIngredient.of(Ingredient.of(s.getBlock()), 1)).toList();
			this.output = recipe.getOutput().getDisplayed().stream()
					.map(s -> EmiStack.of(new ItemStack(s.getBlock()))).toList();
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
