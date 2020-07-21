/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.core.handler;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.common.item.ItemAstrolabe;

import java.util.List;

public final class AstrolabePreviewHandler {
	public static void onWorldRenderLast(RenderWorldLastEvent event) {
		World world = MinecraftClient.getInstance().world;
		MatrixStack ms = event.getMatrixStack();
		VertexConsumerProvider.Immediate buffers = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
		VertexConsumer buffer = buffers.getBuffer(RenderHelper.ASTROLABE_PREVIEW);

		for (PlayerEntity player : world.getPlayers()) {
			ItemStack currentStack = player.getMainHandStack();
			if (currentStack.isEmpty() || !(currentStack.getItem() instanceof ItemAstrolabe)) {
				currentStack = player.getOffHandStack();
			}

			if (!currentStack.isEmpty() && currentStack.getItem() instanceof ItemAstrolabe) {
				Block block = ItemAstrolabe.getBlock(currentStack);
				if (block != Blocks.AIR) {
					renderPlayerLook(ms, buffer, player, currentStack);
				}
			}
		}

		buffers.draw(RenderHelper.ASTROLABE_PREVIEW);
	}

	private static void renderPlayerLook(MatrixStack ms, VertexConsumer buffer, PlayerEntity player, ItemStack stack) {
		List<BlockPos> coords = ItemAstrolabe.getBlocksToPlace(stack, player);
		if (ItemAstrolabe.hasBlocks(stack, player, coords)) {
			BlockState state = ItemAstrolabe.getBlockState(stack);

			for (BlockPos coord : coords) {
				renderBlockAt(ms, buffer, state, coord);
			}
		}
	}

	private static void renderBlockAt(MatrixStack ms, VertexConsumer buffer, BlockState state, BlockPos pos) {
		double renderPosX = MinecraftClient.getInstance().getEntityRenderManager().camera.getPos().getX();
		double renderPosY = MinecraftClient.getInstance().getEntityRenderManager().camera.getPos().getY();
		double renderPosZ = MinecraftClient.getInstance().getEntityRenderManager().camera.getPos().getZ();

		ms.push();
		ms.translate(-renderPosX, -renderPosY, -renderPosZ);

		BlockRenderManager brd = MinecraftClient.getInstance().getBlockRenderManager();
		ms.translate(pos.getX(), pos.getY(), pos.getZ());
		BakedModel model = brd.getModel(state);
		int color = MinecraftClient.getInstance().getBlockColors().getColor(state, null, null, 0);
		float r = (float) (color >> 16 & 255) / 255.0F;
		float g = (float) (color >> 8 & 255) / 255.0F;
		float b = (float) (color & 255) / 255.0F;
		// always use entity translucent layer so blending is turned on
		brd.getModelRenderer().render(ms.peek(), buffer, state, model, r, g, b, 0xF000F0, OverlayTexture.DEFAULT_UV);

		ms.pop();
	}

}
