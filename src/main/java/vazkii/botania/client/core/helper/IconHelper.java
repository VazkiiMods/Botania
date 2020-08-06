/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.core.helper;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.math.vector.Matrix4f;

public final class IconHelper {
	/**
	 * Draw an icon into the buffer, using the {@link RenderHelper#ICON_OVERLAY} vertex format
	 */
	public static void renderIcon(MatrixStack ms, IVertexBuilder buffer, int x, int y, TextureAtlasSprite icon, int width, int height, float alpha) {
		Matrix4f mat = ms.getLast().getMatrix();
		int fullbright = 0xF000F0;
		buffer.pos(mat, x, y + height, 0).color(1, 1, 1, alpha).tex(icon.getMinU(), icon.getMaxV()).lightmap(fullbright).endVertex();
		buffer.pos(mat, x + width, y + height, 0).color(1, 1, 1, alpha).tex(icon.getMaxU(), icon.getMaxV()).lightmap(fullbright).endVertex();
		buffer.pos(mat, x + width, y, 0).color(1, 1, 1, alpha).tex(icon.getMaxU(), icon.getMinV()).lightmap(fullbright).endVertex();
		buffer.pos(mat, x, y, 0).color(1, 1, 1, alpha).tex(icon.getMinU(), icon.getMinV()).lightmap(fullbright).endVertex();
	}
}
