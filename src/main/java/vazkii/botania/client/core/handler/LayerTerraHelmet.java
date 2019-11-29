package vazkii.botania.client.core.handler;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.item.ItemStack;
import vazkii.botania.common.item.equipment.armor.terrasteel.ItemTerrasteelHelm;

public class LayerTerraHelmet extends LayerRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> {
    public LayerTerraHelmet(IEntityRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> renderer) {
        super(renderer);
    }

    @Override
    public void render(AbstractClientPlayerEntity player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        GlStateManager.pushMatrix();
        ItemStack helm = player.inventory.armorItemInSlot(3);
        if(!helm.isEmpty() && helm.getItem() instanceof ItemTerrasteelHelm)
            ItemTerrasteelHelm.renderOnPlayer(helm, player, partialTicks);

        GlStateManager.popMatrix();
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
