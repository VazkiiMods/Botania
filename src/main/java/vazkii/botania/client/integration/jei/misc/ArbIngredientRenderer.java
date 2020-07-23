/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.jei.misc;

import mezz.jei.api.MethodsReturnNonnullByDefault;
import mezz.jei.api.ingredients.IIngredientRenderer;
import vazkii.botania.client.integration.jei.JEIBotaniaPlugin;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class ArbIngredientRenderer<T> implements IIngredientRenderer<T> {

	@Override
	public List<ITextComponent> getTooltip(T ingredient, ITooltipFlag tooltipFlag) {
		return fallback(ingredient).getTooltip(ingredient, tooltipFlag);
	}

	@Nonnull
	protected IIngredientRenderer<T> fallback(T ingredient) {
		return JEIBotaniaPlugin.jeiRuntime.getIngredientManager()
				.getIngredientRenderer(ingredient);
	}
}
