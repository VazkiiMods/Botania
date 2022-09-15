package vazkii.botania.fabric.integration.emi;

import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;

import vazkii.botania.api.recipe.PetalApothecaryRecipe;
import vazkii.botania.common.block.ModBlocks;

import java.util.List;

public class PetalApothecaryEmiRecipe extends BotaniaEmiRecipe {
	private static final EmiStack APOTHECARY = EmiStack.of(ModBlocks.defaultAltar);

	public PetalApothecaryEmiRecipe(PetalApothecaryRecipe recipe) {
		super(BotaniaEmiPlugin.PETAL_APOTHECARY, recipe);
		this.input = recipe.getIngredients().stream().map(EmiIngredient::of).toList();
		this.output = List.of(EmiStack.of(recipe.getResultItem()));
	}

	@Override
	public int getDisplayHeight() {
		return 107;
	}

	@Override
	public int getDisplayWidth() {
		return 106;
	}

	@Override
	public void addWidgets(WidgetHolder widgets) {
		RunicAltarEmiRecipe.addRunicAltarWidgets(widgets, this, input, APOTHECARY, output.get(0));
	}
}
