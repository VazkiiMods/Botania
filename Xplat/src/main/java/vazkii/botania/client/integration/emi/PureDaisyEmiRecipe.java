package vazkii.botania.client.integration.emi;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;

import net.minecraft.resources.ResourceLocation;

import vazkii.botania.api.recipe.PureDaisyRecipe;
import vazkii.botania.common.block.BotaniaFlowerBlocks;

import java.util.List;
import java.util.stream.Collectors;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class PureDaisyEmiRecipe extends BotaniaEmiRecipe {
	private static final ResourceLocation TEXTURE = prefix("textures/gui/pure_daisy_overlay.png");
	private static final EmiStack PURE_DAISY = EmiStack.of(BotaniaFlowerBlocks.pureDaisy);

	public PureDaisyEmiRecipe(PureDaisyRecipe recipe) {
		super(BotaniaEmiPlugin.PURE_DAISY, recipe);
		this.input = List.of(EmiIngredient.of(recipe.getInput().getDisplayed().stream().map(s -> {
			if (s.getFluidState().isEmpty()) {
				return EmiStack.of(s.getBlock());
			} else {
				return EmiStack.of(s.getFluidState().getType());
			}
		}).collect(Collectors.toList())));
		this.output = List.of(EmiStack.of(recipe.getOutputState().getBlock()));
	}

	@Override
	public int getDisplayHeight() {
		return 44;
	}

	@Override
	public int getDisplayWidth() {
		return 96;
	}

	@Override
	public void addWidgets(WidgetHolder widgets) {
		addPureDaisyWidgets(widgets, this, input.get(0), PURE_DAISY, output.get(0));
	}

	public static void addPureDaisyWidgets(WidgetHolder widgets, EmiRecipe recipe,
			EmiIngredient input, EmiIngredient flower, EmiStack output) {
		widgets.add(new BlendTextureWidget(TEXTURE, 17, 0, 65, 44, 0, 0));
		widgets.addSlot(input, 10, 13).drawBack(false);
		widgets.addSlot(flower, 39, 13).catalyst(true).drawBack(false);
		widgets.addSlot(output, 68, 13).drawBack(false).recipeContext(recipe);
	}
}
