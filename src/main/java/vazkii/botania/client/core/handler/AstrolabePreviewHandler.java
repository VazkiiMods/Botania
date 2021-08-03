/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.core.handler;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.common.item.ItemAstrolabe;

import java.util.List;

public final class AstrolabePreviewHandler {
	public static void onWorldRenderLast(PoseStack ms) {
		Level world = Minecraft.getInstance().level;
		MultiBufferSource.BufferSource buffers = Minecraft.getInstance().renderBuffers().bufferSource();
		VertexConsumer buffer = buffers.getBuffer(RenderHelper.ASTROLABE_PREVIEW);

		for (Player player : world.players()) {
			ItemStack currentStack = player.getMainHandItem();
			if (currentStack.isEmpty() || !(currentStack.getItem() instanceof ItemAstrolabe)) {
				currentStack = player.getOffhandItem();
			}

			if (!currentStack.isEmpty() && currentStack.getItem() instanceof ItemAstrolabe) {
				Block block = ItemAstrolabe.getBlock(currentStack);
				if (block != Blocks.AIR) {
					renderPlayerLook(ms, buffer, player, currentStack);
				}
			}
		}

		buffers.endBatch(RenderHelper.ASTROLABE_PREVIEW);
	}

	private static void renderPlayerLook(PoseStack ms, VertexConsumer buffer, Player player, ItemStack stack) {
		List<BlockPos> coords = ItemAstrolabe.getBlocksToPlace(stack, player);
		if (ItemAstrolabe.hasBlocks(stack, player, coords)) {
			BlockState state = ItemAstrolabe.getBlockState(stack);

			for (BlockPos coord : coords) {
				renderBlockAt(ms, buffer, state, coord);
			}
		}
	}

	private static void renderBlockAt(PoseStack ms, VertexConsumer buffer, BlockState state, BlockPos pos) {
		double renderPosX = Minecraft.getInstance().getEntityRenderDispatcher().camera.getPosition().x();
		double renderPosY = Minecraft.getInstance().getEntityRenderDispatcher().camera.getPosition().y();
		double renderPosZ = Minecraft.getInstance().getEntityRenderDispatcher().camera.getPosition().z();

		ms.pushPose();
		ms.translate(-renderPosX, -renderPosY, -renderPosZ);

		BlockRenderDispatcher brd = Minecraft.getInstance().getBlockRenderer();
		ms.translate(pos.getX(), pos.getY(), pos.getZ());
		BakedModel model = brd.getBlockModel(state);
		int color = Minecraft.getInstance().getBlockColors().getColor(state, null, null, 0);
		float r = (float) (color >> 16 & 255) / 255.0F;
		float g = (float) (color >> 8 & 255) / 255.0F;
		float b = (float) (color & 255) / 255.0F;
		// always use entity translucent layer so blending is turned on
		brd.getModelRenderer().renderModel(ms.last(), buffer, state, model, r, g, b, 0xF000F0, OverlayTexture.NO_OVERLAY);

		ms.popPose();
	}

}
