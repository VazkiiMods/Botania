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

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MinecartRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.render.tile.RenderTilePool;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.mana.TilePool;
import vazkii.botania.common.entity.EntityPoolMinecart;

import javax.annotation.Nonnull;

public class RenderPoolMinecart extends MinecartRenderer<EntityPoolMinecart> {
	private static final TilePool DUMMY = new TilePool(new BlockPos(0, Integer.MIN_VALUE, 0), ModBlocks.manaPool.defaultBlockState());

	public RenderPoolMinecart(EntityRendererProvider.Context ctx) {
		super(ctx, ModelLayers.MINECART);
	}

	@Override
	protected void renderMinecartContents(EntityPoolMinecart poolCart, float partialTicks, @Nonnull BlockState state, PoseStack ms, MultiBufferSource buffers, int light) {
		super.renderMinecartContents(poolCart, partialTicks, state, ms, buffers, light);
		RenderTilePool.cartMana = poolCart.getMana();
		Minecraft.getInstance().getBlockEntityRenderDispatcher().getRenderer(DUMMY)
				.render(null, ClientTickHandler.partialTicks, ms, buffers, light, OverlayTexture.NO_OVERLAY);
	}

}
