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

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.vector.Vector3f;

public final class AccessoryRenderHelper {

	/**
	 * Rotates the render for a bauble correctly if the player is sneaking.
	 */
	public static void rotateIfSneaking(MatrixStack ms, LivingEntity player) {
		if (player.isCrouching()) {
			applySneakingRotation(ms);
		}
	}

	/**
	 * Rotates the render for a bauble correctly for a sneaking player.
	 */
	public static void applySneakingRotation(MatrixStack ms) {
		ms.translate(0F, 0.2F, 0F);
		ms.rotate(Vector3f.XP.rotationDegrees(90F / (float) Math.PI));
	}

}
