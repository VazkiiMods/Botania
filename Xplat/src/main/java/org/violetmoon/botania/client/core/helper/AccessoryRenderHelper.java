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

import net.minecraft.world.entity.LivingEntity;

import vazkii.botania.common.helper.VecHelper;

public final class AccessoryRenderHelper {

	/**
	 * Rotates the render for a bauble correctly if the player is sneaking.
	 */
	public static void rotateIfSneaking(PoseStack ms, LivingEntity living) {
		if (living.isCrouching()) {
			ms.translate(0F, 0.2F, 0F);
			ms.mulPose(VecHelper.rotateX(90F / (float) Math.PI));
		}
	}

}
