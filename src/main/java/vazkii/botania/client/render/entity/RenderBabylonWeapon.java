/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.render.entity;

import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.common.entity.EntityBabylonWeapon;

import javax.annotation.Nonnull;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import java.util.Random;

public class RenderBabylonWeapon extends EntityRenderer<EntityBabylonWeapon> {

	public RenderBabylonWeapon(EntityRenderDispatcher renderManager) {
		super(renderManager);
	}

	@Override
	public void render(@Nonnull EntityBabylonWeapon weapon, float yaw, float partialTicks, MatrixStack ms, VertexConsumerProvider buffers, int light) {
		ms.push();
		ms.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(weapon.getRotation()));

		int live = weapon.getLiveTicks();
		int delay = weapon.getDelay();
		float charge = Math.min(10F, Math.max(live, weapon.getChargeTicks()) + partialTicks);
		float chargeMul = charge / 10F;

		ms.push();
		float s = 1.5F;
		ms.scale(s, s, s);
		ms.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(90F));

		BakedModel model = MiscellaneousIcons.INSTANCE.kingKeyWeaponModels[weapon.getVariety()];
		MinecraftClient.getInstance().getBlockRenderManager().getModelRenderer().render(ms.peek(), buffers.getBuffer(TexturedRenderLayers.method_29382()), null, model, 1, 1, 1, 0xF000F0, OverlayTexture.DEFAULT_UV);
		ms.pop();

		Random rand = new Random(weapon.getUuid().getMostSignificantBits());
		ms.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(-90F));
		ms.translate(0F, -0.3F + rand.nextFloat() * 0.1F, 1F);

		s = chargeMul;
		if (live > delay) {
			s -= Math.min(1F, (live - delay + partialTicks) * 0.2F);
		}
		s *= 2F;
		ms.scale(s, s, s);

		ms.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(charge * 9F + (weapon.age + partialTicks) * 0.5F + rand.nextFloat() * 360F));

		VertexConsumer buffer = buffers.getBuffer(RenderHelper.BABYLON_ICON);
		Matrix4f mat = ms.peek().getModel();
		buffer.vertex(mat, -1, 0, -1).color(1, 1, 1, chargeMul).texture(0, 0).next();
		buffer.vertex(mat, -1, 0, 1).color(1, 1, 1, chargeMul).texture(0, 1).next();
		buffer.vertex(mat, 1, 0, 1).color(1, 1, 1, chargeMul).texture(1, 1).next();
		buffer.vertex(mat, 1, 0, -1).color(1, 1, 1, chargeMul).texture(1, 0).next();

		ms.pop();
	}

	@Nonnull
	@Override
	public Identifier getEntityTexture(@Nonnull EntityBabylonWeapon entity) {
		return SpriteAtlasTexture.BLOCK_ATLAS_TEX;
	}

}
