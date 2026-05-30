package vazkii.botania.client.integration.emi;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;

import vazkii.botania.api.recipe.RunicAltarRecipe;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.mana.ManaPoolBlock;

import java.util.ArrayList;
import java.util.List;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

public class RunicAltarEmiRecipe extends BotaniaEmiRecipe {
	private static final ResourceLocation TEXTURE = botaniaRL("textures/gui/petal_overlay.png");
	private static final EmiStack ALTAR = EmiStack.of(BotaniaBlocks.runeAltar);
	public static final int CENTER_X = 44;
	public static final int CENTER_Y = 40;
	public static final int POS_X_INGREDIENTS = CENTER_X;
	public static final int POS_Y_INGREDIENTS = 8;
	public static final int POS_X_REAGENTS = CENTER_X;
	public static final int POS_Y_REAGENTS = 30;
	private final List<EmiIngredient> ingredients;
	private final EmiIngredient reagent;
	private final int mana;

	public RunicAltarEmiRecipe(RecipeHolder<? extends RunicAltarRecipe> recipe) {
		super(BotaniaEmiPlugin.RUNIC_ALTAR, recipe);
		this.ingredients = recipe.value().getIngredients().stream().map(EmiIngredient::of).toList();
		this.catalysts = recipe.value().getCatalysts().stream().map(EmiIngredient::of).toList();
		this.catalysts.forEach(catalysts -> catalysts.getEmiStacks().forEach(stack -> stack.setRemainder(stack)));
		this.reagent = EmiIngredient.of(recipe.value().getReagent());
		ArrayList<EmiIngredient> inputList = new ArrayList<>(ingredients.size() + catalysts.size() + 1);
		inputList.addAll(ingredients);
		inputList.addAll(catalysts);
		inputList.add(reagent);
		this.input = List.copyOf(inputList);
		this.output = List.of(EmiStack.of(recipe.value().getResultItem(getRegistryAccess())));
		this.mana = recipe.value().getMana();
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
		widgets.add(new ManaWidget(2, 100, mana, ManaPoolBlock.MAX_MANA / 10));
		RunicAltarEmiRecipe.addRunicAltarWidgets(widgets, this, ingredients, catalysts, ALTAR, output.getFirst(), reagent);
	}

	public static void addRunicAltarWidgets(WidgetHolder widgets, EmiRecipe recipe, List<EmiIngredient> ingredients,
			List<EmiIngredient> catalysts, EmiIngredient altar, EmiStack output, EmiIngredient... reagents) {
		double step = 360.0 / (ingredients.size() + catalysts.size());
		widgets.add(new BlendTextureWidget(TEXTURE, 21, 0, 85, 82, 42, 11));
		double angle = 0;
		for (EmiIngredient ingredient : ingredients) {
			widgets.addSlot(ingredient,
					BotaniaEmiPlugin.rotateXAround(POS_X_INGREDIENTS, POS_Y_INGREDIENTS, CENTER_X, CENTER_Y, angle),
					BotaniaEmiPlugin.rotateYAround(POS_X_INGREDIENTS, POS_Y_INGREDIENTS, CENTER_X, CENTER_Y, angle))
					.drawBack(false);
			angle += step;
		}
		for (EmiIngredient catalyst : catalysts) {
			widgets.addSlot(catalyst,
					BotaniaEmiPlugin.rotateXAround(POS_X_INGREDIENTS, POS_Y_INGREDIENTS, CENTER_X, CENTER_Y, angle),
					BotaniaEmiPlugin.rotateYAround(POS_X_INGREDIENTS, POS_Y_INGREDIENTS, CENTER_X, CENTER_Y, angle))
					.drawBack(false).catalyst(true);
			angle += step;
		}
		if (reagents.length > 0) {
			double reagentStep = 360.0 / (reagents.length + 1);
			widgets.addSlot(altar,
					BotaniaEmiPlugin.rotateXAround(POS_X_REAGENTS, POS_Y_REAGENTS + 1, CENTER_X, CENTER_Y, 0),
					BotaniaEmiPlugin.rotateYAround(POS_X_REAGENTS, POS_Y_REAGENTS, CENTER_X, CENTER_Y, 0))
					.drawBack(false).catalyst(true);
			angle = reagentStep;
			for (EmiIngredient reagent : reagents) {
				widgets.addSlot(reagent,
						BotaniaEmiPlugin.rotateXAround(POS_X_REAGENTS, POS_Y_REAGENTS, CENTER_X, CENTER_Y, angle),
						BotaniaEmiPlugin.rotateYAround(POS_X_REAGENTS, POS_Y_REAGENTS, CENTER_X, CENTER_Y, angle))
						.drawBack(false);
				angle += reagentStep;
			}
		} else {
			widgets.addSlot(altar, CENTER_X, CENTER_Y + 1).drawBack(false).catalyst(true);
		}
		widgets.addSlot(output, CENTER_X + 38, 5).drawBack(false).recipeContext(recipe);
	}
}
