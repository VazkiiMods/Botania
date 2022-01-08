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

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.Unit;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.BotaniaCapabilities;
import vazkii.botania.api.item.IWireframeCoordinateListProvider;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.xplat.BotaniaConfig;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

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
		if (!BotaniaConfig.client().boundBlockWireframe()) {
			return;
		}

		ms.pushPose();

		Player player = Minecraft.getInstance().player;
		int color = 0xFF000000 | Mth.hsvToRgb(ClientTickHandler.ticksInGame % 200 / 200F, 0.6F, 1F);

		if (!player.getMainHandItem().isEmpty()) {
			var coordBoundItem = BotaniaCapabilities.COORD_BOUND_ITEM.find(player.getMainHandItem(), Unit.INSTANCE);
			if (coordBoundItem != null) {
				BlockPos coords = coordBoundItem.getBinding(player.level);
				if (coords != null) {
					renderBlockOutlineAt(ms, LINE_BUFFERS, coords, color);
				}
			}
		}

		if (!player.getOffhandItem().isEmpty()) {
			var coordBoundItem = BotaniaCapabilities.COORD_BOUND_ITEM.find(player.getOffhandItem(), Unit.INSTANCE);
			if (coordBoundItem != null) {
				BlockPos coords = coordBoundItem.getBinding(player.level);
				if (coords != null) {
					renderBlockOutlineAt(ms, LINE_BUFFERS, coords, color);
				}
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
				if (coords != null && coords.getY() != Integer.MIN_VALUE) {
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
		ms.translate(pos.getX() - renderPosX, pos.getY() - renderPosY, pos.getZ() - renderPosZ);

		Level world = Minecraft.getInstance().level;
		BlockState state = world.getBlockState(pos);
		List<AABB> list = state.getShape(world, pos).toAabbs();

		if (!list.isEmpty()) {
			VertexConsumer buffer = buffers.getBuffer(thick ? RenderHelper.LINE_5_NO_DEPTH : RenderHelper.LINE_1_NO_DEPTH);
			for (AABB axis : list) {
				renderBlockOutline(ms, buffer, axis, color);
			}

			buffer = buffers.getBuffer(thick ? RenderHelper.LINE_8_NO_DEPTH : RenderHelper.LINE_4_NO_DEPTH);
			int alpha = 64;
			color = (color & ~0xff000000) | (alpha << 24);
			for (AABB axis : list) {
				renderBlockOutline(ms, buffer, axis, color);
			}
		}

		ms.popPose();
	}

	private static void renderBlockOutline(PoseStack pose, VertexConsumer buffer, AABB aabb, int color) {
		float a = ((color >> 24) & 0xFF) / 255.0F;
		float r = ((color >> 16) & 0xFF) / 255.0F;
		float g = ((color >> 8) & 0xFF) / 255.0F;
		float b = (color & 0xFF) / 255.F;
		LevelRenderer.renderLineBox(pose, buffer, aabb, r, g, b, a);
	}
}
