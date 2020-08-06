/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.render.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;

import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.common.entity.EntityBabylonWeapon;

import javax.annotation.Nonnull;

import java.util.Random;

public class RenderBabylonWeapon extends EntityRenderer<EntityBabylonWeapon> {

	public RenderBabylonWeapon(EntityRendererManager renderManager) {
		super(renderManager);
	}

	@Override
	public void render(@Nonnull EntityBabylonWeapon weapon, float yaw, float partialTicks, MatrixStack ms, IRenderTypeBuffer buffers, int light) {
		ms.push();
		ms.rotate(Vector3f.YP.rotationDegrees(weapon.getRotation()));

		int live = weapon.getLiveTicks();
		int delay = weapon.getDelay();
		float charge = Math.min(10F, Math.max(live, weapon.getChargeTicks()) + partialTicks);
		float chargeMul = charge / 10F;

		ms.push();
		float s = 1.5F;
		ms.scale(s, s, s);
		ms.rotate(Vector3f.YP.rotationDegrees(90F));

		IBakedModel model = MiscellaneousIcons.INSTANCE.kingKeyWeaponModels[weapon.getVariety()];
		Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelRenderer().renderModelBrightnessColor(ms.getLast(), buffers.getBuffer(Atlases.func_239280_i_()), null, model, 1, 1, 1, 0xF000F0, OverlayTexture.NO_OVERLAY);
		ms.pop();

		Random rand = new Random(weapon.getUniqueID().getMostSignificantBits());
		ms.rotate(Vector3f.XP.rotationDegrees(-90F));
		ms.translate(0F, -0.3F + rand.nextFloat() * 0.1F, 1F);

		s = chargeMul;
		if (live > delay) {
			s -= Math.min(1F, (live - delay + partialTicks) * 0.2F);
		}
		s *= 2F;
		ms.scale(s, s, s);

		ms.rotate(Vector3f.YP.rotationDegrees(charge * 9F + (weapon.ticksExisted + partialTicks) * 0.5F + rand.nextFloat() * 360F));

		IVertexBuilder buffer = buffers.getBuffer(RenderHelper.BABYLON_ICON);
		Matrix4f mat = ms.getLast().getMatrix();
		buffer.pos(mat, -1, 0, -1).color(1, 1, 1, chargeMul).tex(0, 0).endVertex();
		buffer.pos(mat, -1, 0, 1).color(1, 1, 1, chargeMul).tex(0, 1).endVertex();
		buffer.pos(mat, 1, 0, 1).color(1, 1, 1, chargeMul).tex(1, 1).endVertex();
		buffer.pos(mat, 1, 0, -1).color(1, 1, 1, chargeMul).tex(1, 0).endVertex();

		ms.pop();
	}

	@Nonnull
	@Override
	public ResourceLocation getEntityTexture(@Nonnull EntityBabylonWeapon entity) {
		return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
	}

}
