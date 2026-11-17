/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.client.integration.emi;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.TextWidget;
import dev.emi.emi.api.widget.WidgetHolder;

import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.recipe.OrechidRecipe;
import vazkii.botania.client.integration.shared.OrechidUIHelper;

import java.util.List;
import java.util.stream.Stream;

public class OrechidEmiRecipe extends BotaniaEmiRecipe {
	private final EmiIngredient orechid;
	protected final OrechidRecipe recipe;

	public OrechidEmiRecipe(EmiRecipeCategory category, RecipeHolder<? extends OrechidRecipe> recipe, EmiIngredient orechid) {
		super(category, recipe);
		try {
			this.input = List.of(EmiIngredient.of(recipe.value().getInput().getDisplayed().stream()
					.map(state -> EmiIngredient.of(Ingredient.of(state.getBlock()), 1)).toList()));
			this.output = recipe.value().getOutput().getDisplayed().stream()
					.map(state -> EmiStack.of(new ItemStack(state.getBlock()))).toList();
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		this.orechid = orechid;
		this.recipe = recipe.value();
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
		PureDaisyEmiRecipe.addPureDaisyWidgets(widgets, this, input.getFirst(), orechid, output.getFirst());

		final Double chance = getChance(recipe);
		if (chance != null) {
			final Component chanceComponent = OrechidUIHelper.getPercentageComponent(chance);
			widgets.add(new TextWidget(chanceComponent.getVisualOrderText(), 90, 3, 0x555555, false) {
				@Override
				public List<ClientTooltipComponent> getTooltip(int mouseX, int mouseY) {
					return getChanceTooltipComponents(chance)
							.map(Component::getVisualOrderText)
							.map(ClientTooltipComponent::create)
							.toList();
				}
			}.horizontalAlign(TextWidget.Alignment.END));
		}
	}

	protected Stream<Component> getChanceTooltipComponents(double chance) {
		final var ratio = OrechidUIHelper.getRatioForChance(chance);
		Stream<Component> biomeChanceTooltipComponents = OrechidUIHelper.getBiomeChanceAndRatioTooltipComponents(chance, recipe);
		return Stream.concat(Stream.of(OrechidUIHelper.getRatioTooltipComponent(ratio)), biomeChanceTooltipComponents);
	}

	@Nullable
	protected Double getChance(OrechidRecipe recipe) {
		return OrechidUIHelper.getChance(recipe, null);
	}

	public int getWeight() {
		return recipe.getWeight();
	}
}
