/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.render.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;

import vazkii.botania.api.state.enums.LuminizerVariant;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.common.block.BlockLightRelay;
import vazkii.botania.common.block.tile.TileLightRelay;

import javax.annotation.Nonnull;

import java.util.EnumMap;
import java.util.Map;

public class RenderTileLightRelay extends TileEntityRenderer<TileLightRelay> {

	private static final Map<LuminizerVariant, TextureAtlasSprite> sprites = new EnumMap<>(LuminizerVariant.class);

	public RenderTileLightRelay(TileEntityRendererDispatcher manager) {
		super(manager);
	}

	@Override
	public void render(@Nonnull TileLightRelay tile, float pticks, MatrixStack ms, IRenderTypeBuffer buffers, int light, int overlay) {
		BlockState state = tile.getBlockState();

		Minecraft mc = Minecraft.getInstance();
		if (sprites.isEmpty()) {
			sprites.put(LuminizerVariant.DEFAULT, MiscellaneousIcons.INSTANCE.lightRelayWorldIcon);
			sprites.put(LuminizerVariant.DETECTOR, MiscellaneousIcons.INSTANCE.lightRelayDetectorWorldIcon);
			sprites.put(LuminizerVariant.FORK, MiscellaneousIcons.INSTANCE.lightRelayForkWorldIcon);
			sprites.put(LuminizerVariant.TOGGLE, MiscellaneousIcons.INSTANCE.lightRelayToggleWorldIcon);
		}

		TextureAtlasSprite iicon = sprites.get(((BlockLightRelay) state.getBlock()).variant);

		ms.push();
		ms.translate(0.5, 0.3, 0.5);

		double time = ClientTickHandler.ticksInGame + pticks;

		float scale = 0.75F;
		ms.scale(scale, scale, scale);

		ms.rotate(mc.getRenderManager().getCameraOrientation());
		ms.rotate(Vector3f.YP.rotationDegrees(180.0F));

		float off = 0.25F;
		ms.translate(0F, off, 0F);
		ms.rotate(Vector3f.ZP.rotationDegrees((float) time));
		ms.translate(0F, -off, 0F);

		IVertexBuilder buffer = buffers.getBuffer(RenderHelper.LIGHT_RELAY);
		renderIcon(ms, buffer, iicon);

		ms.pop();
	}

	private void renderIcon(MatrixStack ms, IVertexBuilder buffer, TextureAtlasSprite icon) {
		float size = icon.getMaxU() - icon.getMinU();
		float pad = size / 8F;
		float f = icon.getMinU() + pad;
		float f1 = icon.getMaxU() - pad;
		float f2 = icon.getMinV() + pad;
		float f3 = icon.getMaxV() - pad;

		float f4 = 1.0F;
		float f5 = 0.5F;
		float f6 = 0.25F;

		Matrix4f mat = ms.getLast().getMatrix();
		int fullbright = 0xF000F0;
		buffer.pos(mat, 0.0F - f5, 0.0F - f6, 0.0F).color(1F, 1F, 1F, 1F).tex(f, f3).lightmap(fullbright).endVertex();
		buffer.pos(mat, f4 - f5, 0.0F - f6, 0.0F).color(1F, 1F, 1F, 1F).tex(f1, f3).lightmap(fullbright).endVertex();
		buffer.pos(mat, f4 - f5, f4 - f6, 0.0F).color(1F, 1F, 1F, 1F).tex(f1, f2).lightmap(fullbright).endVertex();
		buffer.pos(mat, 0.0F - f5, f4 - f6, 0.0F).color(1F, 1F, 1F, 1F).tex(f, f2).lightmap(fullbright).endVertex();

	}

}
