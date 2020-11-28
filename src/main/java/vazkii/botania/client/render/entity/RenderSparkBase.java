/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.render.entity;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;

import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.common.entity.EntitySparkBase;
import vazkii.botania.mixin.AccessorDyeColor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Random;

public abstract class RenderSparkBase<T extends EntitySparkBase> extends EntityRenderer<T> {

	public RenderSparkBase(EntityRenderDispatcher manager) {
		super(manager);
	}

	@Override
	public void render(@Nonnull T tEntity, float yaw, float partialTicks, MatrixStack ms, VertexConsumerProvider buffers, int light) {
		Sprite iicon = getBaseIcon(tEntity);

		ms.push();

		double time = (tEntity.world.getTime() % 24000) + partialTicks + new Random(tEntity.getEntityId()).nextInt(200);
		float a = 0.1F + (tEntity.isInvisible() ? 0 : 1) * 0.8F;

		int alpha = (int) ((0.7 + 0.3 * (Math.sin(time / 5.0) + 0.5) * 2) * a * 255.0);
		int iconColor = 0xFFFFFF | (alpha << 24);

		float scale = 0.75F + 0.1F * (float) Math.sin(time / 10);
		ms.scale(scale, scale, scale);

		VertexConsumer buffer = buffers.getBuffer(RenderHelper.SPARK);
		ms.push();
		ms.multiply(dispatcher.getRotation());
		ms.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180));
		renderIcon(ms, buffer, iicon, iconColor);

		ms.push();
		ms.translate(-0.02 + Math.sin(time / 20) * 0.2, 0.24 + Math.cos(time / 20) * 0.2, 0.005);
		ms.scale(0.2F, 0.2F, 0.2F);
		int starColor = ((AccessorDyeColor) (Object) tEntity.getNetwork()).getColor() | ((int) (a * 255.0F) << 24);
		renderIcon(ms, buffer, MiscellaneousIcons.INSTANCE.corporeaIconStar.getSprite(), starColor);
		ms.pop();

		Sprite spinningIcon = getSpinningIcon(tEntity);
		if (spinningIcon != null) {
			ms.translate(-0.02 + Math.sin(time / 20) * -0.2, 0.24 + Math.cos(time / 20) * -0.2, 0.005);
			ms.scale(0.2F, 0.2F, 0.2F);
			renderIcon(ms, buffer, spinningIcon, iconColor);
		}
		ms.pop();

		ms.pop();
	}

	protected Sprite getBaseIcon(T entity) {
		return MiscellaneousIcons.INSTANCE.sparkWorldIcon.getSprite();
	}

	@Nullable
	protected Sprite getSpinningIcon(T entity) {
		return null;
	}

	@Nonnull
	@Override
	public Identifier getTexture(@Nonnull EntitySparkBase entity) {
		return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
	}

	private void renderIcon(MatrixStack ms, VertexConsumer buffer, Sprite icon, int color) {
		float f = icon.getMinU();
		float f1 = icon.getMaxU();
		float f2 = icon.getMinV();
		float f3 = icon.getMaxV();
		float f4 = 1.0F;
		float f5 = 0.5F;
		float f6 = 0.25F;
		int fullbright = 0xF000F0;
		int a = (color >> 24) & 0xFF;
		int r = (color >> 16) & 0xFF;
		int g = (color >> 8) & 0xFF;
		int b = color & 0xFF;
		Matrix4f mat = ms.peek().getModel();
		buffer.vertex(mat, 0.0F - f5, 0.0F - f6, 0.0F).color(r, g, b, a).texture(f, f3).light(fullbright).next();
		buffer.vertex(mat, f4 - f5, 0.0F - f6, 0.0F).color(r, g, b, a).texture(f1, f3).light(fullbright).next();
		buffer.vertex(mat, f4 - f5, f4 - f6, 0.0F).color(r, g, b, a).texture(f1, f2).light(fullbright).next();
		buffer.vertex(mat, 0.0F - f5, f4 - f6, 0.0F).color(r, g, b, a).texture(f, f2).light(fullbright).next();
	}

}
