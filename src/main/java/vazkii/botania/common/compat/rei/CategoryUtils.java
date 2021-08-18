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
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.level.block.Block;

import java.util.Map;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.gui.widget.Widget;
import me.shedaniel.rei.impl.Internals;

@Environment(EnvType.CLIENT)
public interface CategoryUtils {
	/**
	 * method that always returns a "lighter" REI recipe background panel.
	 */
	static Widget drawRecipeBackground(Rectangle bounds) {
		return Internals.getWidgetsProvider().createPanelWidget(bounds).yTextureOffset(0);
	}

	/**
	 * Widgets#createTexturedWidget doesn't allow partial transparency, so this is called in createDrawableWidget
	 * instead.
	 */
	static void drawOverlay(GuiComponent helper, PoseStack matrices, ResourceLocation texture, int x, int y, int u, int v, int width, int height) {
		RenderSystem.enableBlend();
		Minecraft.getInstance().getTextureManager().bindForSetup(texture);
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

	static boolean doesOreExist(ResourceLocation tagId) {
		Tag<Block> tag = BlockTags.getAllTags().getTag(tagId);
		return tag != null && !tag.getValues().isEmpty();
	}

	static float getTotalOreWeight(Map<ResourceLocation, Integer> weights, int myWeight) {
		return (weights.entrySet().stream()
				.filter(e -> doesOreExist(e.getKey()))
				.map(Map.Entry::getValue)
				.reduce(Integer::sum)).orElse(myWeight * 64 * 64);
	}
}
