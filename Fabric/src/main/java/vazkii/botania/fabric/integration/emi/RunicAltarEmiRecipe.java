package vazkii.botania.fabric.integration.emi;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;

import net.minecraft.resources.ResourceLocation;

import vazkii.botania.api.recipe.RunicAltarRecipe;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.mana.ManaPoolBlockEntity;
import vazkii.botania.common.item.material.ItemRune;

import java.util.List;
import java.util.stream.Stream;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class RunicAltarEmiRecipe extends BotaniaEmiRecipe {
	private static final ResourceLocation TEXTURE = prefix("textures/gui/petal_overlay.png");
	private static final EmiStack LIVINGROCK = EmiStack.of(ModBlocks.livingrock);
	private static final EmiStack ALTAR = EmiStack.of(ModBlocks.runeAltar);
	private final List<EmiIngredient> ingredients;
	private final int mana;

	public RunicAltarEmiRecipe(RunicAltarRecipe recipe) {
		super(BotaniaEmiPlugin.RUNIC_ALTAR, recipe);
		this.ingredients = recipe.getIngredients().stream().map(EmiIngredient::of).toList();
		this.input = Stream.concat(ingredients.stream(), Stream.of(LIVINGROCK)).toList();
		// TODO classify these as catalysts instead?
		for (EmiIngredient ing : input) {
			for (EmiStack stack : ing.getEmiStacks()) {
				if (stack.getItemStack().getItem() instanceof ItemRune) {
					stack.setRemainder(stack.copy());
				}
			}
		}
		this.output = List.of(EmiStack.of(recipe.getResultItem()));
		this.mana = recipe.getManaUsage();
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
		RunicAltarEmiRecipe.addRunicAltarWidgets(widgets, this, ingredients, ALTAR, output.get(0));
	}

	public static void addRunicAltarWidgets(WidgetHolder widgets, EmiRecipe recipe,
			List<EmiIngredient> input, EmiIngredient altar, EmiStack output) {
		double step = 360.0 / input.size();
		widgets.add(new BlendTextureWidget(TEXTURE, 21, 0, 85, 82, 42, 11));
		for (int i = 0; i < input.size(); i++) {
			EmiIngredient ing = input.get(i);
			widgets.addSlot(ing, BotaniaEmiPlugin.rotateXAround(44, 8, 44, 40, step * i),
					BotaniaEmiPlugin.rotateYAround(44, 8, 44, 40, step * i)).drawBack(false);
		}
		widgets.addSlot(altar, 44, 41).drawBack(false).catalyst(true);
		widgets.addSlot(output, 44 + 38, 5).drawBack(false).recipeContext(recipe);
	}
}
