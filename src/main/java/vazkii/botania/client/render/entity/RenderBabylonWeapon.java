/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;

import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.common.entity.EntityBabylonWeapon;

import javax.annotation.Nonnull;

import java.util.Random;

public class RenderBabylonWeapon extends EntityRenderer<EntityBabylonWeapon> {

	public RenderBabylonWeapon(EntityRendererProvider.Context ctx) {
		super(ctx);
	}

	@Override
	public void render(@Nonnull EntityBabylonWeapon weapon, float yaw, float partialTicks, PoseStack ms, MultiBufferSource buffers, int light) {
		ms.pushPose();
		ms.mulPose(Vector3f.YP.rotationDegrees(weapon.getRotation()));

		int live = weapon.getLiveTicks();
		int delay = weapon.getDelay();
		float charge = Math.min(10F, Math.max(live, weapon.getChargeTicks()) + partialTicks);
		float chargeMul = charge / 10F;

		ms.pushPose();
		ms.translate(-0.75, 0, 1); // X shifts the weapon hilt to the center of the circle, Z makes it intersect it.
		float s = 1.5F;
		ms.scale(s, s, s);
		ms.mulPose(Vector3f.YP.rotationDegrees(90F)); // Rotate to make it match facing, instead of perpendicular to the circle
		ms.mulPose(Vector3f.ZP.rotationDegrees(-45F)); // Perpendicular to the ground, instead of the rising 45 deg of the sprite

		BakedModel model = MiscellaneousIcons.INSTANCE.kingKeyWeaponModels[weapon.getVariety()];
		Minecraft.getInstance().getBlockRenderer().getModelRenderer().renderModel(ms.last(), buffers.getBuffer(Sheets.translucentItemSheet()), null, model, 1, 1, 1, 0xF000F0, OverlayTexture.NO_OVERLAY);
		ms.popPose();

		Random rand = new Random(weapon.getUUID().getMostSignificantBits());
		ms.mulPose(Vector3f.XP.rotationDegrees(-90F)); // Lay the circle horizontally
		ms.translate(0F, -0.3F + rand.nextFloat() * 0.1F, 0F); // Randomly offset how deep the item is in the circle

		s = chargeMul;
		if (live > delay) {
			s -= Math.min(1F, (live - delay + partialTicks) * 0.2F);
		}
		s *= 2F;
		ms.scale(s, s, s);

		ms.mulPose(Vector3f.YP.rotationDegrees(charge * 9F + (weapon.tickCount + partialTicks) * 0.5F + rand.nextFloat() * 360F));

		VertexConsumer buffer = buffers.getBuffer(RenderHelper.BABYLON_ICON);
		Matrix4f mat = ms.last().pose();
		buffer.vertex(mat, -1, 0, -1).color(1, 1, 1, chargeMul).uv(0, 0).endVertex();
		buffer.vertex(mat, -1, 0, 1).color(1, 1, 1, chargeMul).uv(0, 1).endVertex();
		buffer.vertex(mat, 1, 0, 1).color(1, 1, 1, chargeMul).uv(1, 1).endVertex();
		buffer.vertex(mat, 1, 0, -1).color(1, 1, 1, chargeMul).uv(1, 0).endVertex();

		ms.popPose();
	}

	@Nonnull
	@Override
	public ResourceLocation getTextureLocation(@Nonnull EntityBabylonWeapon entity) {
		return InventoryMenu.BLOCK_ATLAS;
	}

}
