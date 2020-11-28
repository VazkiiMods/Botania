/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.bauble;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Box;

import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.common.entity.EntityMagicMissile;

import java.util.List;

public class ItemThirdEye extends ItemBauble implements IManaUsingItem {

	private static final int COST = 2;

	public ItemThirdEye(Settings props) {
		super(props);
	}

	@Override
	public void onWornTick(ItemStack stack, LivingEntity living) {
		if (!(living instanceof PlayerEntity)) {
			return;
		}
		PlayerEntity eplayer = (PlayerEntity) living;

		double range = 24;
		Box aabb = new Box(living.getX(), living.getY(), living.getZ(), living.getX(), living.getY(), living.getZ()).expand(range);
		List<LivingEntity> mobs = living.world.getEntitiesByClass(LivingEntity.class, aabb, EntityMagicMissile.targetPredicate(living));

		for (LivingEntity e : mobs) {
			StatusEffectInstance potion = e.getStatusEffect(StatusEffects.GLOWING);
			if ((potion == null || potion.getDuration() <= 2) && ManaItemHandler.instance().requestManaExact(stack, eplayer, COST, true)) {
				e.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 12, 0));
			}
		}
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void doRender(BipedEntityModel<?> bipedModel, ItemStack stack, LivingEntity living, MatrixStack ms, VertexConsumerProvider buffers, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		boolean armor = !living.getEquippedStack(EquipmentSlot.CHEST).isEmpty();

		for (int i = 0; i < 3; i++) {
			ms.push();
			bipedModel.torso.rotate(ms);

			switch (i) {
			case 0:
				break;
			case 1:
				double time = ClientTickHandler.total * 0.12;
				double dist = 0.05;
				ms.translate(Math.sin(time) * dist, Math.cos(time * 0.5) * dist, 0);

				ms.scale(0.75F, 0.75F, 1F);
				ms.translate(0, 0.1, -0.025);
				break;
			case 2:
				ms.translate(0, 0, -0.05);
				break;
			}

			ms.translate(-0.3, 0.6, armor ? 0.10 : 0.15);
			ms.scale(0.6F, -0.6F, -0.6F);
			BakedModel model = MiscellaneousIcons.INSTANCE.thirdEyeLayers[i];
			VertexConsumer buffer = buffers.getBuffer(TexturedRenderLayers.getEntityCutout());
			MinecraftClient.getInstance().getBlockRenderManager().getModelRenderer()
					.render(ms.peek(), buffer, null, model, 1, 1, 1, light, OverlayTexture.DEFAULT_UV);
			ms.pop();
		}
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

}
