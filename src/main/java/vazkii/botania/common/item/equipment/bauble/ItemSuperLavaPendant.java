/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.bauble;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import vazkii.botania.api.item.AccessoryRenderHelper;
import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.common.core.handler.EquipmentHandler;

public class ItemSuperLavaPendant extends ItemBauble {

	public ItemSuperLavaPendant(Properties props) {
		super(props);
		MinecraftForge.EVENT_BUS.addListener(this::onDamage);
	}

	private void onDamage(LivingHurtEvent evt) {
		if (evt.getSource().isFireDamage()
				&& !EquipmentHandler.findOrEmpty(this, evt.getEntityLiving()).isEmpty()) {
			evt.setCanceled(true);
		}
	}

	@Override
	public void onWornTick(ItemStack stack, LivingEntity living) {
		if (living.isBurning()) {
			living.extinguish();
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void doRender(ItemStack stack, LivingEntity player, MatrixStack ms, IRenderTypeBuffer buffers, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		AccessoryRenderHelper.rotateIfSneaking(ms, player);
		boolean armor = !player.getItemStackFromSlot(EquipmentSlotType.CHEST).isEmpty();
		ms.scale(0.5F, 0.5F, 0.5F);
		ms.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(180));
		ms.translate(-0.5, -0.90, armor ? -0.4 : -0.25);

		IBakedModel model = MiscellaneousIcons.INSTANCE.crimsonGem;
		IVertexBuilder buffer = buffers.getBuffer(Atlases.getEntitySolid());
		Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelRenderer()
				.render(ms.peek(), buffer, null, model, 1, 1, 1, light, OverlayTexture.DEFAULT_UV);
	}
}
