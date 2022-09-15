/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.bauble;

import com.google.common.base.Predicates;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import vazkii.botania.api.internal.ManaBurst;
import vazkii.botania.api.mana.TinyPlanetExcempt;
import vazkii.botania.client.render.AccessoryRenderRegistry;
import vazkii.botania.client.render.AccessoryRenderer;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.proxy.Proxy;

import java.util.List;

public class TinyPlanetItem extends BaubleItem {

	public TinyPlanetItem(Properties props) {
		super(props);
		Proxy.INSTANCE.runOnClient(() -> () -> AccessoryRenderRegistry.register(this, new Renderer()));
	}

	@Override
	public void onWornTick(ItemStack stack, LivingEntity living) {
		double x = living.getX();
		double y = living.getY() + living.getEyeHeight();
		double z = living.getZ();

		applyEffect(living.level, x, y, z);
	}

	public static class Renderer implements AccessoryRenderer {
		@Override
		public void doRender(HumanoidModel<?> bipedModel, ItemStack stack, LivingEntity living, PoseStack ms, MultiBufferSource buffers, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
			bipedModel.head.translateAndRotate(ms);
			ms.translate(-0.25, -0.4, 0);
			ms.scale(0.5F, -0.5F, -0.5F);
			Minecraft.getInstance().getBlockRenderer().renderSingleBlock(BotaniaBlocks.tinyPlanet.defaultBlockState(), ms, buffers, light, OverlayTexture.NO_OVERLAY);
		}
	}

	public static void applyEffect(Level world, double x, double y, double z) {
		int range = 8;
		List<ThrowableProjectile> entities = world.getEntitiesOfClass(ThrowableProjectile.class, new AABB(x - range, y - range, z - range, x + range, y + range, z + range), Predicates.instanceOf(ManaBurst.class));
		for (ThrowableProjectile entity : entities) {
			ManaBurst burst = (ManaBurst) entity;
			ItemStack lens = burst.getSourceLens();
			if (lens != null && lens.getItem() instanceof TinyPlanetExcempt excempt && !excempt.shouldPull(lens)) {
				continue;
			}

			int orbitTime = burst.getOrbitTime();
			if (orbitTime == 0) {
				burst.setMinManaLoss(burst.getMinManaLoss() * 3);
			}

			float radius = Math.min(7.5F, (Math.max(40, orbitTime) - 40) / 40F + 1.5F);
			int angle = orbitTime % 360;

			float xTarget = (float) (x + Math.cos(angle * 10 * Math.PI / 180F) * radius);
			float yTarget = (float) y;
			float zTarget = (float) (z + Math.sin(angle * 10 * Math.PI / 180F) * radius);

			Vec3 targetVec = new Vec3(xTarget, yTarget, zTarget);
			Vec3 currentVec = entity.position();
			Vec3 moveVector = targetVec.subtract(currentVec);

			entity.setDeltaMovement(moveVector);

			burst.setOrbitTime(burst.getOrbitTime() + 1);
		}
	}

}
