/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MinecartRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.state.BlockState;

import org.violetmoon.botania.api.block.Bound;
import org.violetmoon.botania.client.render.block_entity.ManaPoolBlockEntityRenderer;
import org.violetmoon.botania.common.block.BotaniaBlocks;
import org.violetmoon.botania.common.block.block_entity.mana.ManaPoolBlockEntity;
import org.violetmoon.botania.common.block.mana.ManaPoolBlock;
import org.violetmoon.botania.common.entity.ManaPoolMinecartEntity;

public class ManaPoolMinecartRenderer extends MinecartRenderer<ManaPoolMinecartEntity> {
	private static final ManaPoolBlockEntity DUMMY = new ManaPoolBlockEntity(Bound.UNBOUND_POS, BotaniaBlocks.MANA_POOL.defaultBlockState());

	public ManaPoolMinecartRenderer(EntityRendererProvider.Context ctx) {
		super(ctx, ModelLayers.MINECART);
	}

	@Override
	protected void renderMinecartContents(ManaPoolMinecartEntity poolCart, float partialTicks, BlockState state, PoseStack ms, MultiBufferSource buffers, int light) {
		super.renderMinecartContents(poolCart, partialTicks, state, ms, buffers, light);
		ManaPoolBlockEntityRenderer.cartMana = poolCart.getMana();
		// TODO: Support other pool types
		ManaPoolBlockEntityRenderer.cartMaxMana = ManaPoolBlock.MAX_MANA;
		ManaPoolBlockEntityRenderer.cartBlock = BotaniaBlocks.MANA_POOL;
		Minecraft.getInstance().getBlockEntityRenderDispatcher().getRenderer(DUMMY)
				.render(null, Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(false), ms, buffers, light, OverlayTexture.NO_OVERLAY);
	}

}
