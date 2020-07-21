/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.core.helper;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.LivingEntity;

public final class AccessoryRenderHelper {

	/**
	 * Rotates the render for a bauble correctly if the player is sneaking.
	 */
	public static void rotateIfSneaking(MatrixStack ms, LivingEntity player) {
		if (player.isInSneakingPose()) {
			applySneakingRotation(ms);
		}
	}

	/**
	 * Rotates the render for a bauble correctly for a sneaking player.
	 */
	public static void applySneakingRotation(MatrixStack ms) {
		ms.translate(0F, 0.2F, 0F);
		ms.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(90F / (float) Math.PI));
	}

	/**
	 * Shifts the render for a bauble correctly to the head, including sneaking rotation.
	 */
	public static void translateToHeadLevel(MatrixStack ms, LivingEntity player, float partialTicks) {

		float yaw = player.prevHeadYaw + (player.headYaw - player.prevHeadYaw) * partialTicks;
		float yawOffset = player.prevBodyYaw + (player.bodyYaw - player.prevBodyYaw) * partialTicks;
		float pitch = player.prevPitch + (player.pitch - player.prevPitch) * partialTicks;

		ms.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-yawOffset));
		ms.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(yaw - 270));
		ms.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(pitch));

		ms.translate(0, -player.getStandingEyeHeight(), 0);
//        if (player.shouldRenderSneaking()) TODO Sneaking needs a different adjustment now
//            GlStateManager.translatef(0.25F * MathHelper.sin(player.rotationPitch * (float) Math.PI / 180), 0.25F * MathHelper.cos(player.rotationPitch * (float) Math.PI / 180), 0F);
	}

}
