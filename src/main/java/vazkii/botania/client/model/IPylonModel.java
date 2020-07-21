/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.model;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public interface IPylonModel {
	void renderRing(MatrixStack ms, VertexConsumer buffer, int light, int overlay);

	void renderCrystal(MatrixStack ms, VertexConsumer buffer, int light, int overlay);
}
