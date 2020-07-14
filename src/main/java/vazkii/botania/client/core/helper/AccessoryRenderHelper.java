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

	/**
	 * Shifts the render for a bauble correctly to the head, including sneaking rotation.
	 */
	public static void translateToHeadLevel(MatrixStack ms, LivingEntity player, float partialTicks) {

		float yaw = player.prevRotationYawHead + (player.rotationYawHead - player.prevRotationYawHead) * partialTicks;
		float yawOffset = player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * partialTicks;
		float pitch = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * partialTicks;

		ms.rotate(Vector3f.YP.rotationDegrees(-yawOffset));
		ms.rotate(Vector3f.YP.rotationDegrees(yaw - 270));
		ms.rotate(Vector3f.ZP.rotationDegrees(pitch));

		ms.translate(0, -player.getEyeHeight(), 0);
//        if (player.shouldRenderSneaking()) TODO Sneaking needs a different adjustment now
//            GlStateManager.translatef(0.25F * MathHelper.sin(player.rotationPitch * (float) Math.PI / 180), 0.25F * MathHelper.cos(player.rotationPitch * (float) Math.PI / 180), 0F);
	}

}
