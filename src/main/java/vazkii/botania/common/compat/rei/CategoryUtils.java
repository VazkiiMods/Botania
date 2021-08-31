/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.compat.rei;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;


import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.impl.ClientInternals;

@Environment(EnvType.CLIENT)
public interface CategoryUtils {
	/**
	 * method that always returns a "lighter" REI recipe background panel.
	 */
	static Widget drawRecipeBackground(Rectangle bounds) {
		return ClientInternals.getWidgetsProvider().createPanelWidget(bounds).yTextureOffset(0);
	}

	/**
	 * Widgets#createTexturedWidget doesn't allow partial transparency, so this is called in createDrawableWidget
	 * instead.
	 */
	static void drawOverlay(GuiComponent helper, PoseStack matrices, ResourceLocation texture, int x, int y, int u, int v, int width, int height) {
		RenderSystem.enableBlend();
		RenderSystem.setShaderTexture(0, texture);
		helper.blit(matrices, x, y, u, v, width, height);
		RenderSystem.disableBlend();
	}

	/**
	 * spin the wheel and laugh at god! Used for creating the Apothecary and Rune Altar categories.
	 */
	static Point rotatePointAbout(Point in, Point about, double degrees) {
		double rad = degrees * Math.PI / 180.0;
		double newX = Math.cos(rad) * (in.x - about.x) - Math.sin(rad) * (in.y - about.y) + about.x;
		double newY = Math.sin(rad) * (in.x - about.x) + Math.cos(rad) * (in.y - about.y) + about.y;
		return new Point((int) newX, (int) newY);
	}
}
