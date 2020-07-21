/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.core.helper;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;

public final class IconHelper {
	/**
	 * Draw an icon into the buffer, using the {@link RenderHelper#ICON_OVERLAY} vertex format
	 */
	public static void renderIcon(MatrixStack ms, VertexConsumer buffer, int x, int y, Sprite icon, int width, int height, float alpha) {
		Matrix4f mat = ms.peek().getModel();
		int fullbright = 0xF000F0;
		buffer.vertex(mat, x, y + height, 0).color(1, 1, 1, alpha).texture(icon.getMinU(), icon.getMaxV()).light(fullbright).next();
		buffer.vertex(mat, x + width, y + height, 0).color(1, 1, 1, alpha).texture(icon.getMaxU(), icon.getMaxV()).light(fullbright).next();
		buffer.vertex(mat, x + width, y, 0).color(1, 1, 1, alpha).texture(icon.getMaxU(), icon.getMinV()).light(fullbright).next();
		buffer.vertex(mat, x, y, 0).color(1, 1, 1, alpha).texture(icon.getMinU(), icon.getMinV()).light(fullbright).next();
	}
}
