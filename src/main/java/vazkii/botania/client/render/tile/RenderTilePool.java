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

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

import vazkii.botania.api.mana.IPoolOverlayProvider;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.common.block.mana.BlockPool;
import vazkii.botania.common.block.tile.mana.TilePool;

import javax.annotation.Nullable;

import java.util.Random;

public class RenderTilePool extends TileEntityRenderer<TilePool> {

	// Overrides for when we call this TESR from a cart
	public static int cartMana = -1;

	public RenderTilePool(TileEntityRendererDispatcher manager) {
		super(manager);
	}

	@Override
	public void render(@Nullable TilePool pool, float f, MatrixStack ms, IRenderTypeBuffer buffers, int light, int overlay) {
		ms.push();

		boolean fab = pool != null && ((BlockPool) pool.getBlockState().getBlock()).variant == BlockPool.Variant.FABULOUS;

		if (fab) {
			float time = ClientTickHandler.ticksInGame + ClientTickHandler.partialTicks;
			time += new Random(pool.getPos().getX() ^ pool.getPos().getY() ^ pool.getPos().getZ()).nextInt(100000);
			time *= 0.005F;
			int color = vazkii.botania.common.core.helper.MathHelper.multiplyColor(MathHelper.hsvToRGB(time - (int) time, 0.6F, 1F), pool.color.getColorValue());

			int red = (color & 0xFF0000) >> 16;
			int green = (color & 0xFF00) >> 8;
			int blue = color & 0xFF;
			BlockState state = pool.getBlockState();
			IBakedModel model = Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelShapes().getModel(state);
			IVertexBuilder buffer = buffers.getBuffer(RenderTypeLookup.func_239220_a_(state, false));
			Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelRenderer()
					.renderModelBrightnessColor(ms.getLast(), buffer, state, model, red / 255F, green / 255F, blue / 255F, light, overlay);
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
				TextureAtlasSprite overlayIcon = ((IPoolOverlayProvider) below).getIcon(pool.getWorld(), pool.getPos());
				if (overlayIcon != null) {
					ms.push();
					float alpha = (float) ((Math.sin((ClientTickHandler.ticksInGame + f) / 20.0) + 1) * 0.3 + 0.2);
					ms.translate(-0.5F, -1F - 0.43F, -0.5F);
					ms.rotate(Vector3f.XP.rotationDegrees(90F));
					ms.scale(s, s, s);

					IVertexBuilder buffer = buffers.getBuffer(RenderHelper.ICON_OVERLAY);
					IconHelper.renderIcon(ms, buffer, 0, 0, overlayIcon, 16, 16, alpha);

					ms.pop();
				}
			}
		}

		if (waterLevel > 0) {
			s = 1F / 256F * 14F;
			ms.push();
			ms.translate(w, -1F - (0.43F - waterLevel), w);
			ms.rotate(Vector3f.XP.rotationDegrees(90F));
			ms.scale(s, s, s);

			IVertexBuilder buffer = buffers.getBuffer(RenderHelper.MANA_POOL_WATER);
			IconHelper.renderIcon(ms, buffer, 0, 0, MiscellaneousIcons.INSTANCE.manaWater, 16, 16, 1);

			ms.pop();
		}
		ms.pop();

		cartMana = -1;
	}

}
