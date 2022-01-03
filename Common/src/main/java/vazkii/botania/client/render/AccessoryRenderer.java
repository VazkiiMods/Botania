package vazkii.botania.client.render;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface AccessoryRenderer {
	void doRender(HumanoidModel<?> bipedModel, ItemStack stack, LivingEntity living, PoseStack ms, MultiBufferSource buffers,
			int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch);
}
