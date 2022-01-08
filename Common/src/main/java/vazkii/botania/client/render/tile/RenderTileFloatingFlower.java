/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.common.block.tile.TileFloatingFlower;
import vazkii.botania.xplat.BotaniaConfig;

import javax.annotation.Nonnull;

import java.util.Random;

public class RenderTileFloatingFlower implements BlockEntityRenderer<TileFloatingFlower> {

	public RenderTileFloatingFlower(BlockEntityRendererProvider.Context ctx) {}

	@Override
	public void render(@Nonnull TileFloatingFlower tile, float t, PoseStack ms, MultiBufferSource buffers, int light, int overlay) {
		renderFloatingIsland(tile, t, ms, buffers, light, overlay);
	}

	public static void renderFloatingIsland(BlockEntity tile, float t, PoseStack ms, MultiBufferSource buffers, int light, int overlay) {
		if (BotaniaConfig.client().staticFloaters()) {
			return;
		}

		ms.pushPose();

		double worldTime = ClientTickHandler.ticksInGame + t;
		if (tile.getLevel() != null) {
			worldTime += new Random(tile.getBlockPos().hashCode()).nextInt(1000);
		}

		ms.translate(0.5F, 0, 0.5F);
		ms.mulPose(Vector3f.YP.rotationDegrees(-((float) worldTime * 0.5F)));
		ms.translate(-0.5, (float) Math.sin(worldTime * 0.05F) * 0.1F, 0.5);

		ms.mulPose(Vector3f.XP.rotationDegrees(4F * (float) Math.sin(worldTime * 0.04F)));
		ms.mulPose(Vector3f.YP.rotationDegrees(90.0F));

		BlockRenderDispatcher brd = Minecraft.getInstance().getBlockRenderer();
		BlockState state = tile.getBlockState();

		var buffer = buffers.getBuffer(ItemBlockRenderTypes.getRenderType(state, false));
		brd.getModelRenderer().tesselateBlock(tile.getLevel(), brd.getBlockModel(state), state, tile.getBlockPos(), ms,
				buffer, true, new Random(), state.getSeed(tile.getBlockPos()), overlay);

		ms.popPose();
	}

}
