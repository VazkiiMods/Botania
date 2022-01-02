/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.core.helper;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public final class IconHelper {
	/**
	 * Draw an icon into the buffer, using the {@link RenderHelper#ICON_OVERLAY} vertex format
	 */
	public static void renderIcon(PoseStack ms, VertexConsumer buffer, int x, int y, TextureAtlasSprite icon, int width, int height, float alpha) {
		Matrix4f mat = ms.last().pose();
		int fullbright = 0xF000F0;
		buffer.vertex(mat, x, y + height, 0).color(1, 1, 1, alpha).uv(icon.getU0(), icon.getV1()).uv2(fullbright).endVertex();
		buffer.vertex(mat, x + width, y + height, 0).color(1, 1, 1, alpha).uv(icon.getU1(), icon.getV1()).uv2(fullbright).endVertex();
		buffer.vertex(mat, x + width, y, 0).color(1, 1, 1, alpha).uv(icon.getU1(), icon.getV0()).uv2(fullbright).endVertex();
		buffer.vertex(mat, x, y, 0).color(1, 1, 1, alpha).uv(icon.getU0(), icon.getV0()).uv2(fullbright).endVertex();
	}
}
