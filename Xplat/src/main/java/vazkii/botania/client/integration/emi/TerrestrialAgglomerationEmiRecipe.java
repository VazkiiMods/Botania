package vazkii.botania.client.integration.emi;

import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;

import net.minecraft.core.RegistryAccess;

import vazkii.botania.api.recipe.TerrestrialAgglomerationRecipe;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.block_entity.mana.ManaPoolBlockEntity;

import java.util.List;

public class TerrestrialAgglomerationEmiRecipe extends BotaniaEmiRecipe {
	private static final EmiStack PLATE = EmiStack.of(BotaniaBlocks.terraPlate);
	private final int mana;

	public TerrestrialAgglomerationEmiRecipe(TerrestrialAgglomerationRecipe recipe) {
		super(BotaniaEmiPlugin.TERRESTRIAL_AGGLOMERATION, recipe);
		this.input = recipe.getIngredients().stream().map(EmiIngredient::of).toList();
		// TODO 1.19.4 figure out the proper way to get a registry access
		this.output = List.of(EmiStack.of(recipe.getResultItem(RegistryAccess.EMPTY)));
		this.mana = recipe.getMana();
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
		widgets.add(new ManaWidget(2, 100, mana, ManaPoolBlockEntity.MAX_MANA / 10));
		RunicAltarEmiRecipe.addRunicAltarWidgets(widgets, this, input, PLATE, output.get(0));
	}
}
