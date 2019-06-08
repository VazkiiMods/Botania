package vazkii.botania.api.item;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;

/**
 * A few helper methods for the render.
 */
public final class AccessoryRenderHelper {

    /**
     * Rotates the render for a bauble correctly if the player is sneaking.
     */
    public static void rotateIfSneaking(LivingEntity player) {
        if(player.isSneaking())
            applySneakingRotation();
    }

    /**
     * Rotates the render for a bauble correctly for a sneaking player.
     */
    public static void applySneakingRotation() {
        GlStateManager.translatef(0F, 0.2F, 0F);
        GlStateManager.rotatef(90F / (float) Math.PI, 1.0F, 0.0F, 0.0F);
    }

    /**
     * Shifts the render for a bauble correctly to the head, including sneaking rotation.
     */
    public static void translateToHeadLevel(LivingEntity player) {
        GlStateManager.translatef(0, -player.getEyeHeight(), 0);
        if (player.isSneaking())
            GlStateManager.translatef(0.25F * MathHelper.sin(player.rotationPitch * (float) Math.PI / 180), 0.25F * MathHelper.cos(player.rotationPitch * (float) Math.PI / 180), 0F);
    }

    /**
     * Shifts the render for a bauble correctly to the face.
     * Use for renders after calling {@link AccessoryRenderHelper#translateToHeadLevel(LivingEntity)}.
     */
    public static void translateToFace() {
        GlStateManager.rotatef(90F, 0F, 1F, 0F);
        GlStateManager.rotatef(180F, 1F, 0F, 0F);
        GlStateManager.translatef(0f, -4.35f, -1.27f);
    }

    /**
     * Scales down the render to a correct size.
     * Use for any render.
     */
    public static void defaultTransforms() {
        GlStateManager.translated(0.0, 3.0, 1.0);
        GlStateManager.scaled(0.55, 0.55, 0.55);
    }

    /**
     * Shifts the render for a bauble correctly to the chest.
     * Use for renders after calling {@link AccessoryRenderHelper#rotateIfSneaking(LivingEntity)}.
     */
    public static void translateToChest() {
        GlStateManager.rotatef(180F, 1F, 0F, 0F);
        GlStateManager.translatef(0F, -3.2F, -0.85F);
    }

}
