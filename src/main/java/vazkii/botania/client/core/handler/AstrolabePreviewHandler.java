/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.core.handler;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
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
		World world = Minecraft.getInstance().world;
		MatrixStack ms = event.getMatrixStack();
		IRenderTypeBuffer.Impl buffers = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
		IVertexBuilder buffer = buffers.getBuffer(RenderHelper.ASTROLABE_PREVIEW);

		for (PlayerEntity player : world.getPlayers()) {
			ItemStack currentStack = player.getHeldItemMainhand();
			if (currentStack.isEmpty() || !(currentStack.getItem() instanceof ItemAstrolabe)) {
				currentStack = player.getHeldItemOffhand();
			}

			if (!currentStack.isEmpty() && currentStack.getItem() instanceof ItemAstrolabe) {
				Block block = ItemAstrolabe.getBlock(currentStack);
				if (block != Blocks.AIR) {
					renderPlayerLook(ms, buffer, player, currentStack);
				}
			}
		}

		buffers.finish(RenderHelper.ASTROLABE_PREVIEW);
	}

	private static void renderPlayerLook(MatrixStack ms, IVertexBuilder buffer, PlayerEntity player, ItemStack stack) {
		List<BlockPos> coords = ItemAstrolabe.getBlocksToPlace(stack, player);
		if (ItemAstrolabe.hasBlocks(stack, player, coords)) {
			BlockState state = ItemAstrolabe.getBlockState(stack);

			for (BlockPos coord : coords) {
				renderBlockAt(ms, buffer, state, coord);
			}
		}
	}

	private static void renderBlockAt(MatrixStack ms, IVertexBuilder buffer, BlockState state, BlockPos pos) {
		double renderPosX = Minecraft.getInstance().getRenderManager().info.getProjectedView().getX();
		double renderPosY = Minecraft.getInstance().getRenderManager().info.getProjectedView().getY();
		double renderPosZ = Minecraft.getInstance().getRenderManager().info.getProjectedView().getZ();

		ms.push();
		ms.translate(-renderPosX, -renderPosY, -renderPosZ);

		BlockRendererDispatcher brd = Minecraft.getInstance().getBlockRendererDispatcher();
		ms.translate(pos.getX(), pos.getY(), pos.getZ());
		IBakedModel model = brd.getModelForState(state);
		int color = Minecraft.getInstance().getBlockColors().getColor(state, null, null, 0);
		float r = (float) (color >> 16 & 255) / 255.0F;
		float g = (float) (color >> 8 & 255) / 255.0F;
		float b = (float) (color & 255) / 255.0F;
		// always use entity translucent layer so blending is turned on
		brd.getBlockModelRenderer().renderModelBrightnessColor(ms.getLast(), buffer, state, model, r, g, b, 0xF000F0, OverlayTexture.NO_OVERLAY);

		ms.pop();
	}

}
