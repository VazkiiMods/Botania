/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 14, 2014, 5:28:21 PM (GMT)]
 */
package vazkii.botania.client.core.helper;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

public final class IconHelper {
	/**
	 * Draw an icon into the buffer, using the {@link RenderHelper#ICON_OVERLAY} vertex format
	 */
	public static void renderIcon(MatrixStack ms, IVertexBuilder buffer, int x, int y, TextureAtlasSprite icon, int width, int height, float alpha) {
		Matrix4f mat = ms.peek().getModel();
		int fullbright = 0xF000F0;
		buffer.vertex(mat, x, y + height, 0).color(1, 1, 1, alpha).texture(icon.getMinU(), icon.getMaxV()).light(fullbright).endVertex();
		buffer.vertex(mat, x + width, y + height, 0).color(1, 1, 1, alpha).texture(icon.getMaxU(), icon.getMaxV()).light(fullbright).endVertex();
		buffer.vertex(mat, x + width, y, 0).color(1, 1, 1, alpha).texture(icon.getMaxU(), icon.getMinV()).light(fullbright).endVertex();
		buffer.vertex(mat, x, y, 0).color(1, 1, 1, alpha).texture(icon.getMinU(), icon.getMinV()).light(fullbright).endVertex();
	}
}