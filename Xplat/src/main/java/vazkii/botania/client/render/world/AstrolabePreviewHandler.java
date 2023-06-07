/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.render.world;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.common.item.AstrolabeItem;

import java.util.List;

public final class AstrolabePreviewHandler {
	public static void onWorldRenderLast(PoseStack ms, RenderBuffers buffers, Level level) {
		MultiBufferSource.BufferSource bufferSource = buffers.bufferSource();
		VertexConsumer buffer = bufferSource.getBuffer(RenderHelper.ASTROLABE_PREVIEW);

		for (Player player : level.players()) {
			ItemStack currentStack = player.getMainHandItem();
			InteractionHand hand = InteractionHand.MAIN_HAND;
			if (currentStack.isEmpty() || !(currentStack.getItem() instanceof AstrolabeItem)) {
				currentStack = player.getOffhandItem();
				hand = InteractionHand.OFF_HAND;
			}

			if (!currentStack.isEmpty() && currentStack.getItem() instanceof AstrolabeItem) {
				Block block = AstrolabeItem.getBlock(currentStack, level.holderLookup(Registries.BLOCK));
				if (block != Blocks.AIR) {
					renderPlayerLook(ms, buffer, player, currentStack, hand);
				}
			}
		}

		bufferSource.endBatch(RenderHelper.ASTROLABE_PREVIEW);
	}

	private static void renderPlayerLook(PoseStack ms, VertexConsumer buffer, Player player, ItemStack stack, InteractionHand hand) {
		Block blockToPlace = AstrolabeItem.getBlock(stack, player.getLevel().holderLookup(Registries.BLOCK));
		int size = AstrolabeItem.getSize(stack);
		BlockPlaceContext ctx = AstrolabeItem.getBlockPlaceContext(player, hand, blockToPlace);
		List<BlockPos> placePositions = AstrolabeItem.getPlacePositions(ctx, size);
		if (ctx != null && AstrolabeItem.hasBlocks(stack, player, placePositions.size(), blockToPlace)) {
			for (BlockPos pos : placePositions) {
				BlockPlaceContext placeContext = getPlaceContext(player, ctx, pos);
				BlockState state = blockToPlace.getStateForPlacement(placeContext);
				if (state != null && placeContext.canPlace() && state.canSurvive(player.getLevel(), pos)) {
					renderBlockAt(ms, buffer, state, pos);
				}
			}
		}
	}

	@NotNull
	private static BlockPlaceContext getPlaceContext(Player player, BlockPlaceContext ctx, BlockPos pos) {
		Vec3 newHitVec = new Vec3(pos.getX() + Mth.frac(ctx.getClickLocation().x()),
				pos.getY() + Mth.frac(ctx.getClickLocation().y()),
				pos.getZ() + Mth.frac(ctx.getClickLocation().z()));
		BlockHitResult newHit = new BlockHitResult(newHitVec, ctx.getClickedFace(), pos, false);
		return new BlockPlaceContext(player, ctx.getHand(), ctx.getItemInHand(), newHit);
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
