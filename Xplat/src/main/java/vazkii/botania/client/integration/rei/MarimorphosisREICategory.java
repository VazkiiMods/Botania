package vazkii.botania.client.integration.rei;

import net.minecraft.network.chat.Component;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.api.recipe.OrechidRecipe;
import vazkii.botania.client.integration.shared.OrechidUIHelper;
import vazkii.botania.common.block.BotaniaFlowerBlocks;

import java.util.stream.Stream;

public class MarimorphosisREICategory extends OrechidREICategory {
	public MarimorphosisREICategory() {
		super(BotaniaREICategoryIdentifiers.MARIMORPHOSIS, BotaniaFlowerBlocks.marimorphosis);
	}

	@NotNull
	@Override
	protected Stream<Component> getChanceTooltipComponents(double chance, OrechidRecipe recipe) {
		Stream<Component> genericChanceTooltipComponents = super.getChanceTooltipComponents(chance, recipe);
		Stream<Component> biomeChanceTooltipComponents = OrechidUIHelper.getBiomeChanceAndRatioTooltipComponents(chance, recipe);
		return Stream.concat(genericChanceTooltipComponents, biomeChanceTooltipComponents);
	}
}
