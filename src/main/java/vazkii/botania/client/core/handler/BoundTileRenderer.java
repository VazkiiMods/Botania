/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.core.handler;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.block.IWireframeAABBProvider;
import vazkii.botania.api.item.ICoordBoundItem;
import vazkii.botania.api.item.IWireframeCoordinateListProvider;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.common.core.handler.ConfigHandler;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class BoundTileRenderer {
	private static final MultiBufferSource.BufferSource LINE_BUFFERS = MultiBufferSource.immediateWithBuffers(Util.make(() -> {
		Map<RenderType, BufferBuilder> ret = new IdentityHashMap<>();
		ret.put(RenderHelper.LINE_1_NO_DEPTH, new BufferBuilder(RenderHelper.LINE_1_NO_DEPTH.bufferSize()));
		ret.put(RenderHelper.LINE_4_NO_DEPTH, new BufferBuilder(RenderHelper.LINE_4_NO_DEPTH.bufferSize()));
		ret.put(RenderHelper.LINE_5_NO_DEPTH, new BufferBuilder(RenderHelper.LINE_5_NO_DEPTH.bufferSize()));
		ret.put(RenderHelper.LINE_8_NO_DEPTH, new BufferBuilder(RenderHelper.LINE_8_NO_DEPTH.bufferSize()));
		return ret;
	}), Tesselator.getInstance().getBuilder());

	private BoundTileRenderer() {}

	public static void onWorldRenderLast(PoseStack ms) {
		if (!ConfigHandler.CLIENT.boundBlockWireframe.getValue()) {
			return;
		}

		ms.pushPose();

		Player player = Minecraft.getInstance().player;
		int color = 0xFF000000 | Mth.hsvToRgb(ClientTickHandler.ticksInGame % 200 / 200F, 0.6F, 1F);

		if (!player.getMainHandItem().isEmpty() && player.getMainHandItem().getItem() instanceof ICoordBoundItem) {
			BlockPos coords = ((ICoordBoundItem) player.getMainHandItem().getItem()).getBinding(player.level, player.getMainHandItem());
			if (coords != null) {
				renderBlockOutlineAt(ms, LINE_BUFFERS, coords, color);
			}
		}

		if (!player.getOffhandItem().isEmpty() && player.getOffhandItem().getItem() instanceof ICoordBoundItem) {
			BlockPos coords = ((ICoordBoundItem) player.getOffhandItem().getItem()).getBinding(player.level, player.getOffhandItem());
			if (coords != null) {
				renderBlockOutlineAt(ms, LINE_BUFFERS, coords, color);
			}
		}

		renderWireframeProviders(player.getInventory(), player, ms, color);
		renderWireframeProviders(BotaniaAPI.instance().getAccessoriesInventory(player), player, ms, color);

		ms.popPose();
		RenderSystem.disableDepthTest();
		LINE_BUFFERS.endBatch();
	}

	private static void renderWireframeProviders(Container inv, Player player, PoseStack ms, int color) {
		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack stackInSlot = inv.getItem(i);

			if (!stackInSlot.isEmpty() && stackInSlot.getItem() instanceof IWireframeCoordinateListProvider provider) {
				List<BlockPos> coordsList = provider.getWireframesToDraw(player, stackInSlot);
				for (BlockPos coords : coordsList) {
					renderBlockOutlineAt(ms, LINE_BUFFERS, coords, color);
				}

				BlockPos coords = provider.getSourceWireframe(player, stackInSlot);
				if (coords != null && coords.getY() > -1) {
					renderBlockOutlineAt(ms, LINE_BUFFERS, coords, color, true);
				}
			}
		}
	}

	private static void renderBlockOutlineAt(PoseStack ms, MultiBufferSource buffers, BlockPos pos, int color) {
		renderBlockOutlineAt(ms, buffers, pos, color, false);
	}

	private static void renderBlockOutlineAt(PoseStack ms, MultiBufferSource buffers, BlockPos pos, int color, boolean thick) {
		double renderPosX = Minecraft.getInstance().getEntityRenderDispatcher().camera.getPosition().x();
		double renderPosY = Minecraft.getInstance().getEntityRenderDispatcher().camera.getPosition().y();
		double renderPosZ = Minecraft.getInstance().getEntityRenderDispatcher().camera.getPosition().z();

		ms.pushPose();
		ms.translate(pos.getX() - renderPosX, pos.getY() - renderPosY, pos.getZ() - renderPosZ + 1);

		Level world = Minecraft.getInstance().level;
		BlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		List<AABB> list;

		if (block instanceof IWireframeAABBProvider) {
			list = ((IWireframeAABBProvider) block).getWireframeAABB(world, pos);
		} else {
			VoxelShape shape = state.getShape(world, pos);
			list = shape.toAabbs().stream().map(b -> b.move(pos)).collect(Collectors.toList());
		}

		if (!list.isEmpty()) {
			ms.scale(1F, 1F, 1F);

			VertexConsumer buffer = buffers.getBuffer(thick ? RenderHelper.LINE_5_NO_DEPTH : RenderHelper.LINE_1_NO_DEPTH);
			for (AABB axis : list) {
				axis = axis.move(-pos.getX(), -pos.getY(), -(pos.getZ() + 1));
				renderBlockOutline(ms.last().pose(), buffer, axis, color);
			}

			buffer = buffers.getBuffer(thick ? RenderHelper.LINE_8_NO_DEPTH : RenderHelper.LINE_4_NO_DEPTH);
			int alpha = 64;
			color = (color & ~0xff000000) | (alpha << 24);
			for (AABB axis : list) {
				axis = axis.move(-pos.getX(), -pos.getY(), -(pos.getZ() + 1));
				renderBlockOutline(ms.last().pose(), buffer, axis, color);
			}
		}

		ms.popPose();
	}

	private static void renderBlockOutline(Matrix4f mat, VertexConsumer buffer, AABB aabb, int color) {
		float ix = (float) aabb.minX;
		float iy = (float) aabb.minY;
		float iz = (float) aabb.minZ;
		float ax = (float) aabb.maxX;
		float ay = (float) aabb.maxY;
		float az = (float) aabb.maxZ;
		int a = (color >> 24) & 0xFF;
		int r = (color >> 16) & 0xFF;
		int g = (color >> 8) & 0xFF;
		int b = color & 0xFF;

		buffer.vertex(mat, ix, iy, iz).color(r, g, b, a).endVertex();
		buffer.vertex(mat, ix, ay, iz).color(r, g, b, a).endVertex();

		buffer.vertex(mat, ix, ay, iz).color(r, g, b, a).endVertex();
		buffer.vertex(mat, ax, ay, iz).color(r, g, b, a).endVertex();

		buffer.vertex(mat, ax, ay, iz).color(r, g, b, a).endVertex();
		buffer.vertex(mat, ax, iy, iz).color(r, g, b, a).endVertex();

		buffer.vertex(mat, ax, iy, iz).color(r, g, b, a).endVertex();
		buffer.vertex(mat, ix, iy, iz).color(r, g, b, a).endVertex();

		buffer.vertex(mat, ix, iy, az).color(r, g, b, a).endVertex();
		buffer.vertex(mat, ix, ay, az).color(r, g, b, a).endVertex();

		buffer.vertex(mat, ix, iy, az).color(r, g, b, a).endVertex();
		buffer.vertex(mat, ax, iy, az).color(r, g, b, a).endVertex();

		buffer.vertex(mat, ax, iy, az).color(r, g, b, a).endVertex();
		buffer.vertex(mat, ax, ay, az).color(r, g, b, a).endVertex();

		buffer.vertex(mat, ix, ay, az).color(r, g, b, a).endVertex();
		buffer.vertex(mat, ax, ay, az).color(r, g, b, a).endVertex();

		buffer.vertex(mat, ix, iy, iz).color(r, g, b, a).endVertex();
		buffer.vertex(mat, ix, iy, az).color(r, g, b, a).endVertex();

		buffer.vertex(mat, ix, ay, iz).color(r, g, b, a).endVertex();
		buffer.vertex(mat, ix, ay, az).color(r, g, b, a).endVertex();

		buffer.vertex(mat, ax, iy, iz).color(r, g, b, a).endVertex();
		buffer.vertex(mat, ax, iy, az).color(r, g, b, a).endVertex();

		buffer.vertex(mat, ax, ay, iz).color(r, g, b, a).endVertex();
		buffer.vertex(mat, ax, ay, az).color(r, g, b, a).endVertex();
	}
}
