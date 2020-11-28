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

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.item.IWireframeCoordinateListProvider;
import vazkii.botania.api.wand.ICoordBoundItem;
import vazkii.botania.api.wand.IWireframeAABBProvider;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.common.core.handler.ConfigHandler;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class BoundTileRenderer {
	private static final VertexConsumerProvider.Immediate LINE_BUFFERS = VertexConsumerProvider.immediate(Util.make(() -> {
		Map<RenderLayer, BufferBuilder> ret = new IdentityHashMap<>();
		ret.put(RenderHelper.LINE_1_NO_DEPTH, new BufferBuilder(RenderHelper.LINE_1_NO_DEPTH.getExpectedBufferSize()));
		ret.put(RenderHelper.LINE_4_NO_DEPTH, new BufferBuilder(RenderHelper.LINE_4_NO_DEPTH.getExpectedBufferSize()));
		ret.put(RenderHelper.LINE_5_NO_DEPTH, new BufferBuilder(RenderHelper.LINE_5_NO_DEPTH.getExpectedBufferSize()));
		ret.put(RenderHelper.LINE_8_NO_DEPTH, new BufferBuilder(RenderHelper.LINE_8_NO_DEPTH.getExpectedBufferSize()));
		return ret;
	}), Tessellator.getInstance().getBuffer());

	private BoundTileRenderer() {}

	public static void onWorldRenderLast(MatrixStack ms) {
		if (!ConfigHandler.CLIENT.boundBlockWireframe.getValue()) {
			return;
		}

		ms.push();

		PlayerEntity player = MinecraftClient.getInstance().player;
		int color = 0xFF000000 | MathHelper.hsvToRgb(ClientTickHandler.ticksInGame % 200 / 200F, 0.6F, 1F);

		if (!player.getMainHandStack().isEmpty() && player.getMainHandStack().getItem() instanceof ICoordBoundItem) {
			BlockPos coords = ((ICoordBoundItem) player.getMainHandStack().getItem()).getBinding(player.world, player.getMainHandStack());
			if (coords != null) {
				renderBlockOutlineAt(ms, LINE_BUFFERS, coords, color);
			}
		}

		if (!player.getOffHandStack().isEmpty() && player.getOffHandStack().getItem() instanceof ICoordBoundItem) {
			BlockPos coords = ((ICoordBoundItem) player.getOffHandStack().getItem()).getBinding(player.world, player.getOffHandStack());
			if (coords != null) {
				renderBlockOutlineAt(ms, LINE_BUFFERS, coords, color);
			}
		}

		renderWireframeProviders(player.inventory, player, ms, color);
		renderWireframeProviders(BotaniaAPI.instance().getAccessoriesInventory(player), player, ms, color);

		ms.pop();
		RenderSystem.disableDepthTest();
		LINE_BUFFERS.draw();
	}

	private static void renderWireframeProviders(Inventory inv, PlayerEntity player, MatrixStack ms, int color) {
		for (int i = 0; i < inv.size(); i++) {
			ItemStack stackInSlot = inv.getStack(i);

			if (!stackInSlot.isEmpty() && stackInSlot.getItem() instanceof IWireframeCoordinateListProvider) {
				IWireframeCoordinateListProvider provider = (IWireframeCoordinateListProvider) stackInSlot.getItem();
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

	private static void renderBlockOutlineAt(MatrixStack ms, VertexConsumerProvider buffers, BlockPos pos, int color) {
		renderBlockOutlineAt(ms, buffers, pos, color, false);
	}

	private static void renderBlockOutlineAt(MatrixStack ms, VertexConsumerProvider buffers, BlockPos pos, int color, boolean thick) {
		double renderPosX = MinecraftClient.getInstance().getEntityRenderDispatcher().camera.getPos().getX();
		double renderPosY = MinecraftClient.getInstance().getEntityRenderDispatcher().camera.getPos().getY();
		double renderPosZ = MinecraftClient.getInstance().getEntityRenderDispatcher().camera.getPos().getZ();

		ms.push();
		ms.translate(pos.getX() - renderPosX, pos.getY() - renderPosY, pos.getZ() - renderPosZ + 1);

		World world = MinecraftClient.getInstance().world;
		BlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		List<Box> list;

		if (block instanceof IWireframeAABBProvider) {
			list = ((IWireframeAABBProvider) block).getWireframeAABB(world, pos);
		} else {
			VoxelShape shape = state.getOutlineShape(world, pos);
			list = shape.getBoundingBoxes().stream().map(b -> b.offset(pos)).collect(Collectors.toList());
		}

		if (!list.isEmpty()) {
			ms.scale(1F, 1F, 1F);

			VertexConsumer buffer = buffers.getBuffer(thick ? RenderHelper.LINE_5_NO_DEPTH : RenderHelper.LINE_1_NO_DEPTH);
			for (Box axis : list) {
				axis = axis.offset(-pos.getX(), -pos.getY(), -(pos.getZ() + 1));
				renderBlockOutline(ms.peek().getModel(), buffer, axis, color);
			}

			buffer = buffers.getBuffer(thick ? RenderHelper.LINE_8_NO_DEPTH : RenderHelper.LINE_4_NO_DEPTH);
			int alpha = 64;
			color = (color & ~0xff000000) | (alpha << 24);
			for (Box axis : list) {
				axis = axis.offset(-pos.getX(), -pos.getY(), -(pos.getZ() + 1));
				renderBlockOutline(ms.peek().getModel(), buffer, axis, color);
			}
		}

		ms.pop();
	}

	private static void renderBlockOutline(Matrix4f mat, VertexConsumer buffer, Box aabb, int color) {
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

		buffer.vertex(mat, ix, iy, iz).color(r, g, b, a).next();
		buffer.vertex(mat, ix, ay, iz).color(r, g, b, a).next();

		buffer.vertex(mat, ix, ay, iz).color(r, g, b, a).next();
		buffer.vertex(mat, ax, ay, iz).color(r, g, b, a).next();

		buffer.vertex(mat, ax, ay, iz).color(r, g, b, a).next();
		buffer.vertex(mat, ax, iy, iz).color(r, g, b, a).next();

		buffer.vertex(mat, ax, iy, iz).color(r, g, b, a).next();
		buffer.vertex(mat, ix, iy, iz).color(r, g, b, a).next();

		buffer.vertex(mat, ix, iy, az).color(r, g, b, a).next();
		buffer.vertex(mat, ix, ay, az).color(r, g, b, a).next();

		buffer.vertex(mat, ix, iy, az).color(r, g, b, a).next();
		buffer.vertex(mat, ax, iy, az).color(r, g, b, a).next();

		buffer.vertex(mat, ax, iy, az).color(r, g, b, a).next();
		buffer.vertex(mat, ax, ay, az).color(r, g, b, a).next();

		buffer.vertex(mat, ix, ay, az).color(r, g, b, a).next();
		buffer.vertex(mat, ax, ay, az).color(r, g, b, a).next();

		buffer.vertex(mat, ix, iy, iz).color(r, g, b, a).next();
		buffer.vertex(mat, ix, iy, az).color(r, g, b, a).next();

		buffer.vertex(mat, ix, ay, iz).color(r, g, b, a).next();
		buffer.vertex(mat, ix, ay, az).color(r, g, b, a).next();

		buffer.vertex(mat, ax, iy, iz).color(r, g, b, a).next();
		buffer.vertex(mat, ax, iy, az).color(r, g, b, a).next();

		buffer.vertex(mat, ax, ay, iz).color(r, g, b, a).next();
		buffer.vertex(mat, ax, ay, az).color(r, g, b, a).next();
	}
}
