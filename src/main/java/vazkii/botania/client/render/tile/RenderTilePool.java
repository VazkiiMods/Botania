/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.render.tile;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.MathHelper;
import vazkii.botania.api.mana.IPoolOverlayProvider;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.common.block.mana.BlockPool;
import vazkii.botania.common.block.tile.mana.TilePool;
import vazkii.botania.common.core.helper.ColorHelper;

import javax.annotation.Nullable;

import java.util.Random;

public class RenderTilePool extends BlockEntityRenderer<TilePool> {

	// Overrides for when we call this TESR from a cart
	public static int cartMana = -1;

	public RenderTilePool(BlockEntityRenderDispatcher manager) {
		super(manager);
	}

	@Override
	public void render(@Nullable TilePool pool, float f, MatrixStack ms, VertexConsumerProvider buffers, int light, int overlay) {
		ms.push();

		boolean fab = pool != null && ((BlockPool) pool.getCachedState().getBlock()).variant == BlockPool.Variant.FABULOUS;

		if (fab) {
			float time = ClientTickHandler.ticksInGame + ClientTickHandler.partialTicks;
			time += new Random(pool.getPos().getX() ^ pool.getPos().getY() ^ pool.getPos().getZ()).nextInt(100000);
			time *= 0.005F;
			int poolColor = ColorHelper.getColorValue(pool.color);
			int color = vazkii.botania.common.core.helper.MathHelper.multiplyColor(MathHelper.hsvToRgb(MathHelper.fractionalPart(time), 0.6F, 1F), poolColor);

			int red = (color & 0xFF0000) >> 16;
			int green = (color & 0xFF00) >> 8;
			int blue = color & 0xFF;
			BlockState state = pool.getCachedState();
			BakedModel model = MinecraftClient.getInstance().getBlockRenderManager().getModels().getModel(state);
			VertexConsumer buffer = buffers.getBuffer(RenderLayers.getEntityBlockLayer(state, false));
			MinecraftClient.getInstance().getBlockRenderManager().getModelRenderer()
					.render(ms.peek(), buffer, state, model, red / 255F, green / 255F, blue / 255F, light, overlay);
		}

		ms.translate(0.5F, 1.5F, 0.5F);

		int mana = pool == null ? cartMana : pool.getCurrentMana();
		int cap = pool == null ? -1 : pool.manaCap;
		if (cap == -1) {
			cap = TilePool.MAX_MANA;
		}

		float waterLevel = (float) mana / (float) cap * 0.4F;

		float s = 1F / 16F;
		float v = 1F / 8F;
		float w = -v * 3.5F;

		if (pool != null) {
			Block below = pool.getWorld().getBlockState(pool.getPos().down()).getBlock();
			if (below instanceof IPoolOverlayProvider) {
				Sprite overlayIcon = ((IPoolOverlayProvider) below).getIcon(pool.getWorld(), pool.getPos());
				if (overlayIcon != null) {
					ms.push();
					float alpha = (float) ((Math.sin((ClientTickHandler.ticksInGame + f) / 20.0) + 1) * 0.3 + 0.2);
					ms.translate(-0.5F, -1F - 0.43F, -0.5F);
					ms.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(90F));
					ms.scale(s, s, s);

					VertexConsumer buffer = buffers.getBuffer(RenderHelper.ICON_OVERLAY);
					IconHelper.renderIcon(ms, buffer, 0, 0, overlayIcon, 16, 16, alpha);

					ms.pop();
				}
			}
		}

		if (waterLevel > 0) {
			s = 1F / 256F * 14F;
			ms.push();
			ms.translate(w, -1F - (0.43F - waterLevel), w);
			ms.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(90F));
			ms.scale(s, s, s);

			VertexConsumer buffer = buffers.getBuffer(RenderHelper.MANA_POOL_WATER);
			IconHelper.renderIcon(ms, buffer, 0, 0, MiscellaneousIcons.INSTANCE.manaWater.getSprite(), 16, 16, 1);

			ms.pop();
		}
		ms.pop();

		cartMana = -1;
	}

}
