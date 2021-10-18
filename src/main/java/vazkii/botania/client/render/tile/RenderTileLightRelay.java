/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.state.enums.LuminizerVariant;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.common.block.BlockLightRelay;
import vazkii.botania.common.block.tile.TileLightRelay;
import vazkii.botania.common.item.equipment.bauble.ItemMonocle;

import javax.annotation.Nonnull;

import java.util.EnumMap;
import java.util.Map;

public class RenderTileLightRelay implements BlockEntityRenderer<TileLightRelay> {

	private static final Map<LuminizerVariant, Material> sprites = Util.make(new EnumMap<>(LuminizerVariant.class), m -> {
		m.put(LuminizerVariant.DEFAULT, MiscellaneousIcons.INSTANCE.lightRelayWorldIcon);
		m.put(LuminizerVariant.DETECTOR, MiscellaneousIcons.INSTANCE.lightRelayDetectorWorldIcon);
		m.put(LuminizerVariant.FORK, MiscellaneousIcons.INSTANCE.lightRelayForkWorldIcon);
		m.put(LuminizerVariant.TOGGLE, MiscellaneousIcons.INSTANCE.lightRelayToggleWorldIcon);
	});

	public RenderTileLightRelay(BlockEntityRendererProvider.Context ctx) {}

	@Override
	public void render(@Nonnull TileLightRelay tile, float pticks, PoseStack ms, MultiBufferSource buffers, int light, int overlay) {
		BlockState state = tile.getBlockState();

		Minecraft mc = Minecraft.getInstance();

		if (mc.getCameraEntity() instanceof LivingEntity view) {
			if (ItemMonocle.hasMonocle(view) && RenderTileSpecialFlower.hasBindingAttempt(view, tile.getBlockPos())) {
				RenderTileSpecialFlower.renderRadius(tile, ms, buffers, new RadiusDescriptor.Circle(tile.getBlockPos(), TileLightRelay.MAX_DIST));
			}
		}

		TextureAtlasSprite iicon = sprites.get(((BlockLightRelay) state.getBlock()).variant).sprite();

		ms.pushPose();
		ms.translate(0.5, 0.3, 0.5);

		double time = ClientTickHandler.ticksInGame + pticks;

		float scale = 0.75F;
		ms.scale(scale, scale, scale);

		ms.mulPose(mc.getEntityRenderDispatcher().cameraOrientation());
		ms.mulPose(Vector3f.YP.rotationDegrees(180.0F));

		float off = 0.25F;
		ms.translate(0F, off, 0F);
		ms.mulPose(Vector3f.ZP.rotationDegrees((float) time));
		ms.translate(0F, -off, 0F);

		VertexConsumer buffer = buffers.getBuffer(RenderHelper.LIGHT_RELAY);
		renderIcon(ms, buffer, iicon);

		ms.popPose();
	}

	private void renderIcon(PoseStack ms, VertexConsumer buffer, TextureAtlasSprite icon) {
		float size = icon.getU1() - icon.getU0();
		float pad = size / 8F;
		float f = icon.getU0() + pad;
		float f1 = icon.getU1() - pad;
		float f2 = icon.getV0() + pad;
		float f3 = icon.getV1() - pad;

		float f4 = 1.0F;
		float f5 = 0.5F;
		float f6 = 0.25F;

		Matrix4f mat = ms.last().pose();
		buffer.vertex(mat, 0.0F - f5, 0.0F - f6, 0.0F).color(1F, 1F, 1F, 1F).uv(f, f3).endVertex();
		buffer.vertex(mat, f4 - f5, 0.0F - f6, 0.0F).color(1F, 1F, 1F, 1F).uv(f1, f3).endVertex();
		buffer.vertex(mat, f4 - f5, f4 - f6, 0.0F).color(1F, 1F, 1F, 1F).uv(f1, f2).endVertex();
		buffer.vertex(mat, 0.0F - f5, f4 - f6, 0.0F).color(1F, 1F, 1F, 1F).uv(f, f2).endVertex();

	}

}
