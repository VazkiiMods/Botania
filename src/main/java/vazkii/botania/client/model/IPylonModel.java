/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

public interface IPylonModel {
	void renderRing(MatrixStack ms, IVertexBuilder buffer, int light, int overlay);

	void renderCrystal(MatrixStack ms, IVertexBuilder buffer, int light, int overlay);
}
