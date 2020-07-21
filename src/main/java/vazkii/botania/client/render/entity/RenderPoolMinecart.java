/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.render.entity;

import net.minecraft.block.BlockState;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MinecartEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.render.tile.RenderTilePool;
import vazkii.botania.common.block.tile.mana.TilePool;
import vazkii.botania.common.entity.EntityPoolMinecart;

import javax.annotation.Nonnull;

public class RenderPoolMinecart extends MinecartEntityRenderer<EntityPoolMinecart> {
	private static final TilePool DUMMY = new TilePool();

	public RenderPoolMinecart(EntityRenderDispatcher manager) {
		super(manager);
	}

	@Override
	protected void renderBlockState(EntityPoolMinecart poolCart, float partialTicks, @Nonnull BlockState state, MatrixStack ms, VertexConsumerProvider buffers, int light) {
		super.renderBlock(poolCart, partialTicks, state, ms, buffers, light);
		RenderTilePool.cartMana = poolCart.getMana();
		BlockEntityRenderDispatcher.INSTANCE.get(DUMMY).render(null, ClientTickHandler.partialTicks, ms, buffers, light, OverlayTexture.DEFAULT_UV);
	}

}
