package vazkii.botania.api.item;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.entity.LivingEntity;

/**
 * A few helper methods for the render.
 */
public final class AccessoryRenderHelper {

    /**
     * Rotates the render for a bauble correctly if the player is sneaking.
     */
    public static void rotateIfSneaking(MatrixStack ms, LivingEntity player) {
        if(player.isCrouching())
            applySneakingRotation(ms);
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

        float yaw = player.prevRotationYawHead + (player.rotationYawHead - player.prevRotationYawHead) * partialTicks;
        float yawOffset = player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * partialTicks;
        float pitch = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * partialTicks;

        ms.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-yawOffset));
        ms.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(yaw - 270));
        ms.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(pitch));

        ms.translate(0, -player.getEyeHeight(), 0);
//        if (player.shouldRenderSneaking()) TODO Sneaking needs a different adjustment now
//            GlStateManager.translatef(0.25F * MathHelper.sin(player.rotationPitch * (float) Math.PI / 180), 0.25F * MathHelper.cos(player.rotationPitch * (float) Math.PI / 180), 0F);
    }

    /**
     * Shifts the render for a bauble correctly to the face.
     * Use for renders after calling {@link AccessoryRenderHelper#translateToHeadLevel}.
     */
    public static void translateToFace(MatrixStack ms) {
        ms.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(90));
        ms.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(180));
        ms.translate(0f, -4.35f, -1.27f);
    }

    /**
     * Scales down the render to a correct size.
     * Use for any render.
     */
    public static void defaultTransforms(MatrixStack ms) {
        ms.translate(0.0, 3.0, 1.0);
        ms.scale(0.55F, 0.55F, 0.55F);
    }

    /**
     * Shifts the render for a bauble correctly to the chest.
     * Use for renders after calling {@link AccessoryRenderHelper#rotateIfSneaking}.
     */
    public static void translateToChest(MatrixStack ms) {
        ms.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(180));
        ms.translate(0F, -3.2F, -0.85F);
    }

}
