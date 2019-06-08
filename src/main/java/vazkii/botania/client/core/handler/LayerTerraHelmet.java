package vazkii.botania.client.core.handler;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import vazkii.botania.common.item.equipment.armor.terrasteel.ItemTerrasteelHelm;

public class LayerTerraHelmet implements LayerRenderer<PlayerEntity> {
    @Override
    public void render(PlayerEntity player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        float yaw = player.prevRotationYawHead + (player.rotationYawHead - player.prevRotationYawHead) * partialTicks;
        float yawOffset = player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * partialTicks;
        float pitch = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * partialTicks;

        GlStateManager.pushMatrix();
        GlStateManager.rotatef(yawOffset, 0, -1, 0);
        GlStateManager.rotatef(yaw - 270, 0, 1, 0);
        GlStateManager.rotatef(pitch, 0, 0, 1);

        ItemStack helm = player.inventory.armorItemInSlot(3);
        if(!helm.isEmpty() && helm.getItem() instanceof ItemTerrasteelHelm)
            ItemTerrasteelHelm.renderOnPlayer(helm, player);

        GlStateManager.popMatrix();
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
