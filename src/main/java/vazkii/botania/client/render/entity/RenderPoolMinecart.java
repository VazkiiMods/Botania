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

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MinecartRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;

import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.render.tile.RenderTilePool;
import vazkii.botania.common.block.tile.mana.TilePool;
import vazkii.botania.common.entity.EntityPoolMinecart;

import javax.annotation.Nonnull;

public class RenderPoolMinecart extends MinecartRenderer<EntityPoolMinecart> {
	private static final TilePool DUMMY = new TilePool();

	public RenderPoolMinecart(EntityRendererManager manager) {
		super(manager);
	}

	@Override
	protected void renderBlock(EntityPoolMinecart poolCart, float partialTicks, @Nonnull BlockState state, MatrixStack ms, IRenderTypeBuffer buffers, int light) {
		super.renderBlock(poolCart, partialTicks, state, ms, buffers, light);
		RenderTilePool.cartMana = poolCart.getMana();
		TileEntityRendererDispatcher.instance.getRenderer(DUMMY).render(null, ClientTickHandler.partialTicks, ms, buffers, light, OverlayTexture.DEFAULT_UV);
	}

}
