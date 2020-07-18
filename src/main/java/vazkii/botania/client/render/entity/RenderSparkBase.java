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

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;

import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.common.entity.EntitySparkBase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Random;

public abstract class RenderSparkBase<T extends EntitySparkBase> extends EntityRenderer<T> {

	public RenderSparkBase(EntityRendererManager manager) {
		super(manager);
	}

	@Override
	public void render(@Nonnull T tEntity, float yaw, float partialTicks, MatrixStack ms, IRenderTypeBuffer buffers, int light) {
		TextureAtlasSprite iicon = getBaseIcon(tEntity);

		ms.push();

		double time = tEntity.world.getDayTime() + partialTicks + new Random(tEntity.getEntityId()).nextInt(200);
		float a = 0.1F + (tEntity.isInvisible() ? 0 : 1) * 0.8F;

		int alpha = (int) ((0.7F + 0.3F * (float) (Math.sin(time / 5.0) + 0.5) * 2) * a * 255.0F);
		int iconColor = 0xFFFFFF | (alpha << 24);

		float scale = 0.75F + 0.1F * (float) Math.sin(time / 10);
		ms.scale(scale, scale, scale);

		IVertexBuilder buffer = buffers.getBuffer(RenderHelper.SPARK);
		ms.push();
		ms.rotate(renderManager.getCameraOrientation());
		ms.rotate(Vector3f.YP.rotationDegrees(180));
		renderIcon(ms, buffer, iicon, iconColor);

		ms.push();
		ms.translate(-0.02F + (float) Math.sin(time / 20) * 0.2F, 0.24F + (float) Math.cos(time / 20) * 0.2F, 0.005F);
		ms.scale(0.2F, 0.2F, 0.2F);
		int starColor = tEntity.getNetwork().getColorValue() | ((int) (a * 255.0F) << 24);
		renderIcon(ms, buffer, MiscellaneousIcons.INSTANCE.corporeaIconStar, starColor);
		ms.pop();

		TextureAtlasSprite spinningIcon = getSpinningIcon(tEntity);
		if (spinningIcon != null) {
			ms.translate(-0.02F + (float) Math.sin(time / 20) * -0.2F, 0.24F + (float) Math.cos(time / 20) * -0.2F, 0.005F);
			ms.scale(0.2F, 0.2F, 0.2F);
			renderIcon(ms, buffer, spinningIcon, iconColor);
		}

		ms.pop();
		renderCallback(tEntity, partialTicks, ms, buffers);

		ms.pop();
	}

	protected TextureAtlasSprite getBaseIcon(T entity) {
		return MiscellaneousIcons.INSTANCE.sparkWorldIcon;
	}

	@Nullable
	protected TextureAtlasSprite getSpinningIcon(T entity) {
		return null;
	}

	protected void renderCallback(T entity, float pticks, MatrixStack ms, IRenderTypeBuffer buffers) {}

	@Nonnull
	@Override
	public ResourceLocation getEntityTexture(@Nonnull EntitySparkBase entity) {
		return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
	}

	private void renderIcon(MatrixStack ms, IVertexBuilder buffer, TextureAtlasSprite icon, int color) {
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
		Matrix4f mat = ms.getLast().getMatrix();
		buffer.pos(mat, 0.0F - f5, 0.0F - f6, 0.0F).color(r, g, b, a).tex(f, f3).lightmap(fullbright).endVertex();
		buffer.pos(mat, f4 - f5, 0.0F - f6, 0.0F).color(r, g, b, a).tex(f1, f3).lightmap(fullbright).endVertex();
		buffer.pos(mat, f4 - f5, f4 - f6, 0.0F).color(r, g, b, a).tex(f1, f2).lightmap(fullbright).endVertex();
		buffer.pos(mat, 0.0F - f5, f4 - f6, 0.0F).color(r, g, b, a).tex(f, f2).lightmap(fullbright).endVertex();
	}

}
