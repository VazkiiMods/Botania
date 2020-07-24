/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.integration.jei.misc;

import com.mojang.blaze3d.matrix.MatrixStack;

import mezz.jei.api.MethodsReturnNonnullByDefault;
import mezz.jei.api.ingredients.IIngredientRenderer;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;

import vazkii.botania.client.integration.jei.JEIBotaniaPlugin;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class ArbIngredientRenderer<T> implements IIngredientRenderer<T> {

	public static void setupDefaultBlockTransforms(MatrixStack ms, int xPosition, int yPosition) {
		ms.translate(xPosition + 3, yPosition + 13, 100);
		ms.scale(10F, -10F, 10F); // 16 * 0.625, according to block.json

		ms.translate(0.5, 0.5, 0.5);
		ms.rotate(Vector3f.XP.rotationDegrees(30));
		ms.rotate(Vector3f.YP.rotationDegrees(45));
		ms.translate(-0.5, -0.5, -0.5);
	}

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
