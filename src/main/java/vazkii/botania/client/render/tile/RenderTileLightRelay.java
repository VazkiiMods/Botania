/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.render.tile;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.Matrix4f;
import vazkii.botania.api.state.enums.LuminizerVariant;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.common.block.BlockLightRelay;
import vazkii.botania.common.block.tile.TileLightRelay;

import javax.annotation.Nonnull;

import java.util.EnumMap;
import java.util.Map;

public class RenderTileLightRelay extends BlockEntityRenderer<TileLightRelay> {

	private static final Map<LuminizerVariant, Sprite> sprites = new EnumMap<>(LuminizerVariant.class);

	public RenderTileLightRelay(BlockEntityRenderDispatcher manager) {
		super(manager);
	}

	@Override
	public void render(@Nonnull TileLightRelay tile, float pticks, MatrixStack ms, VertexConsumerProvider buffers, int light, int overlay) {
		BlockState state = tile.getCachedState();

		MinecraftClient mc = MinecraftClient.getInstance();
		if (sprites.isEmpty()) {
			sprites.put(LuminizerVariant.DEFAULT, MiscellaneousIcons.INSTANCE.lightRelayWorldIcon);
			sprites.put(LuminizerVariant.DETECTOR, MiscellaneousIcons.INSTANCE.lightRelayDetectorWorldIcon);
			sprites.put(LuminizerVariant.FORK, MiscellaneousIcons.INSTANCE.lightRelayForkWorldIcon);
			sprites.put(LuminizerVariant.TOGGLE, MiscellaneousIcons.INSTANCE.lightRelayToggleWorldIcon);
		}

		Sprite iicon = sprites.get(((BlockLightRelay) state.getBlock()).variant);

		ms.push();
		ms.translate(0.5, 0.3, 0.5);

		double time = ClientTickHandler.ticksInGame + pticks;

		float scale = 0.75F;
		ms.scale(scale, scale, scale);

		ms.multiply(mc.getEntityRenderDispatcher().getRotation());
		ms.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180.0F));

		float off = 0.25F;
		ms.translate(0F, off, 0F);
		ms.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion((float) time));
		ms.translate(0F, -off, 0F);

		VertexConsumer buffer = buffers.getBuffer(RenderHelper.LIGHT_RELAY);
		renderIcon(ms, buffer, iicon);

		ms.pop();
	}

	private void renderIcon(MatrixStack ms, VertexConsumer buffer, Sprite icon) {
		float size = icon.getMaxU() - icon.getMinU();
		float pad = size / 8F;
		float f = icon.getMinU() + pad;
		float f1 = icon.getMaxU() - pad;
		float f2 = icon.getMinV() + pad;
		float f3 = icon.getMaxV() - pad;

		float f4 = 1.0F;
		float f5 = 0.5F;
		float f6 = 0.25F;

		Matrix4f mat = ms.peek().getModel();
		int fullbright = 0xF000F0;
		buffer.vertex(mat, 0.0F - f5, 0.0F - f6, 0.0F).color(1F, 1F, 1F, 1F).texture(f, f3).light(fullbright).next();
		buffer.vertex(mat, f4 - f5, 0.0F - f6, 0.0F).color(1F, 1F, 1F, 1F).texture(f1, f3).light(fullbright).next();
		buffer.vertex(mat, f4 - f5, f4 - f6, 0.0F).color(1F, 1F, 1F, 1F).texture(f1, f2).light(fullbright).next();
		buffer.vertex(mat, 0.0F - f5, f4 - f6, 0.0F).color(1F, 1F, 1F, 1F).texture(f, f2).light(fullbright).next();

	}

}
