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
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;

import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.common.block.tile.string.TileRedString;
import vazkii.botania.common.core.helper.PlayerHelper;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.item.ModItems;

import java.util.Random;

public class RenderTileRedString<T extends TileRedString> implements BlockEntityRenderer<T> {
	// 0 -> none, 10 -> full
	private static int transparency = 0;

	public static void tick() {
		Player player = Minecraft.getInstance().player;
		boolean hasWand = player != null && PlayerHelper.hasHeldItem(player, ModItems.twigWand);
		if (transparency > 0 && !hasWand) {
			transparency--;
		} else if (transparency < 10 && hasWand) {
			transparency++;
		}
	}

	public RenderTileRedString(BlockEntityRendererProvider.Context ctx) {}

	@Override
	public void render(TileRedString tile, float partialTicks, PoseStack ms, MultiBufferSource buffers, int light, int overlay) {
		if (transparency <= 0) {
			return;
		}

		float sizeAlpha = transparency / 10.0F;
		int color = 0xFF0000 | ((int) (sizeAlpha * 255) << 24);

		Direction dir = tile.getOrientation();
		BlockPos bind = tile.getBinding();

		if (bind != null) {
			ms.pushPose();
			ms.translate(0.5, 0.5, 0.5);
			Vector3 vecOrig = new Vector3(bind.getX() - tile.getBlockPos().getX(), bind.getY() - tile.getBlockPos().getY(), bind.getZ() - tile.getBlockPos().getZ());
			Vector3 vecNorm = vecOrig.normalize();
			Vector3 vecMag = vecNorm.multiply(0.025);
			Vector3 vecApply = vecMag;

			int stages = (int) (vecOrig.mag() / vecMag.mag());

			double len = (double) -ClientTickHandler.ticksInGame / 100F + new Random(dir.ordinal() ^ tile.getBlockPos().hashCode()).nextInt(10000);
			double add = vecMag.mag();
			double rand = Math.random() - 0.5;
			VertexConsumer buffer = buffers.getBuffer(RenderHelper.LINE_1);
			for (int i = 0; i < stages; i++) {
				addVertexAtWithTranslation(ms, buffer, color, dir, vecApply.x, vecApply.y, vecApply.z, rand, len);
				rand = Math.random() - 0.5;
				vecApply = vecApply.add(vecMag);
				len += add;
				addVertexAtWithTranslation(ms, buffer, color, dir, vecApply.x, vecApply.y, vecApply.z, rand, len);
			}

			ms.popPose();
		}
	}

	private static void addVertexAtWithTranslation(PoseStack ms, VertexConsumer buffer, int color, Direction dir, double xpos, double ypos, double zpos, double rand, double l) {
		double freq = 20;
		float sizeAlpha = transparency / 10.0F;
		double ampl = (0.15 * (Math.sin(l * 2F) * 0.5 + 0.5) + 0.1) * sizeAlpha;
		double randMul = 0.05;
		double x = xpos + Math.sin(l * freq) * ampl * Math.abs(Math.abs(dir.getStepX()) - 1) + rand * randMul;
		double y = ypos + Math.cos(l * freq) * ampl * Math.abs(Math.abs(dir.getStepY()) - 1) + rand * randMul;
		double z = zpos + (dir.getStepY() == 0 ? Math.sin(l * freq) : Math.cos(l * freq)) * ampl * Math.abs(Math.abs(dir.getStepZ()) - 1) + rand * randMul;

		int a = (color >> 24) & 0xFF;
		int r = (color >> 16) & 0xFF;
		int g = (color >> 8) & 0xFF;
		int b = color & 0xFF;
		buffer.vertex(ms.last().pose(), (float) x, (float) y, (float) z).color(r, g, b, a).endVertex();
	}

}
