package vazkii.botania.client.integration.emi;

import dev.emi.emi.api.stack.EmiIngredient;

import net.minecraft.network.chat.Component;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.client.integration.shared.OrechidUIHelper;
import vazkii.botania.common.crafting.MarimorphosisRecipe;

import java.util.stream.Stream;

public class MarimorphosisEmiRecipe extends OrechidEmiRecipe {
	public MarimorphosisEmiRecipe(
			MarimorphosisRecipe recipe,
			EmiIngredient orechid) {
		super(BotaniaEmiPlugin.MARIMORPHOSIS, recipe, orechid);
	}

	@NotNull
	@Override
	protected Stream<Component> getChanceTooltipComponents(double chance) {
		Stream<Component> genericChanceTooltipComponents = super.getChanceTooltipComponents(chance);
		Stream<Component> biomeChanceTooltipComponents = OrechidUIHelper.getBiomeChanceAndRatioTooltipComponents(chance, recipe);
		return Stream.concat(genericChanceTooltipComponents, biomeChanceTooltipComponents);
	}
}
