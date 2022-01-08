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
import com.mojang.math.Matrix4f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntitySpecialFlower;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.common.core.helper.PlayerHelper;
import vazkii.botania.common.item.ItemTwigWand;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.bauble.ItemMonocle;

import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RenderTileSpecialFlower<T extends TileEntitySpecialFlower> implements BlockEntityRenderer<T> {
	public RenderTileSpecialFlower(BlockEntityRendererProvider.Context ctx) {}

	@Override
	public void render(TileEntitySpecialFlower tile, float partialTicks, PoseStack ms, MultiBufferSource buffers, int light, int overlay) {
		if (tile.isFloating()) {
			RenderTileFloatingFlower.renderFloatingIsland(tile, partialTicks, ms, buffers, light, overlay);
		}
		if (!(Minecraft.getInstance().cameraEntity instanceof LivingEntity view)) {
			return;
		}

		if (!ItemMonocle.hasMonocle(view)) {
			return;
		}
		BlockPos pos = null;
		HitResult ray = Minecraft.getInstance().hitResult;
		if (ray != null && ray.getType() == HitResult.Type.BLOCK) {
			pos = ((BlockHitResult) ray).getBlockPos();
		}
		boolean hasBindingAttempt = hasBindingAttempt(view, tile.getBlockPos());

		if (hasBindingAttempt || tile.getBlockPos().equals(pos)) {
			TileEntitySpecialFlower flower = tile;
			ms.pushPose();
			if (hasBindingAttempt) {
				ms.translate(0, 0.005, 0);
			}
			renderRadius(tile, ms, buffers, flower.getRadius());
			ms.translate(0, 0.002, 0);
			renderRadius(tile, ms, buffers, flower.getSecondaryRadius());
			ms.popPose();

		}
	}

	public static void renderRadius(BlockEntity tile, PoseStack ms, MultiBufferSource buffers, @Nullable RadiusDescriptor descriptor) {
		if (descriptor != null) {
			ms.pushPose();
			ms.translate(-tile.getBlockPos().getX(), -tile.getBlockPos().getY(), -tile.getBlockPos().getZ());
			if (descriptor.isCircle()) {
				renderCircle(ms, buffers, descriptor.getSubtileCoords(), descriptor.getCircleRadius());
			} else {
				renderRectangle(ms, buffers, descriptor.getAABB(), true, null, (byte) 32);
			}
			ms.popPose();
		}
	}

	public static boolean hasBindingAttempt(LivingEntity view, BlockPos tilePos) {
		ItemStack stackHeld = PlayerHelper.getFirstHeldItem(view, ModItems.twigWand);
		if (!stackHeld.isEmpty() && ItemTwigWand.getBindMode(stackHeld)) {
			return ItemTwigWand.getBindingAttempt(stackHeld).filter(tilePos::equals).isPresent();
		}
		return false;
	}

	private static void renderCircle(PoseStack ms, MultiBufferSource buffers, BlockPos center, double radius) {
		ms.pushPose();
		double x = center.getX() + 0.5;
		double y = center.getY();
		double z = center.getZ() + 0.5;
		ms.translate(x, y, z);
		int color = Mth.hsvToRgb(ClientTickHandler.ticksInGame % 200 / 200F, 0.6F, 1F);
		int r = (color >> 16 & 0xFF);
		int g = (color >> 8 & 0xFF);
		int b = (color & 0xFF);

		int alpha = 32;
		float f = 1F / 16F;

		int totalAngles = 360;
		int drawAngles = 360;
		int step = totalAngles / drawAngles;

		radius -= f;
		VertexConsumer buffer = buffers.getBuffer(RenderHelper.CIRCLE);
		Matrix4f mat = ms.last().pose();

		Runnable centerFunc = () -> buffer.vertex(mat, 0, f, 0).color(r, g, b, alpha).endVertex();
		List<Runnable> vertexFuncs = new ArrayList<>();
		for (int i = 0; i < totalAngles + 1; i += step) {
			double rad = (totalAngles - i) * Math.PI / 180.0;
			float xp = (float) (Math.cos(rad) * radius);
			float zp = (float) (Math.sin(rad) * radius);
			vertexFuncs.add(() -> buffer.vertex(mat, xp, f, zp).color(r, g, b, alpha).endVertex());
		}
		RenderHelper.triangleFan(centerFunc, vertexFuncs);

		radius += f;
		float f1 = f + f / 4F;
		int alpha2 = 64;

		centerFunc = () -> buffer.vertex(mat, 0, f1, 0).color(r, g, b, alpha2).endVertex();
		vertexFuncs.clear();
		for (int i = 0; i < totalAngles + 1; i += step) {
			double rad = (totalAngles - i) * Math.PI / 180.0;
			float xp = (float) (Math.cos(rad) * radius);
			float zp = (float) (Math.sin(rad) * radius);
			vertexFuncs.add(() -> buffer.vertex(mat, xp, f1, zp).color(r, g, b, alpha2).endVertex());
		}
		RenderHelper.triangleFan(centerFunc, vertexFuncs);
		ms.popPose();
	}

	public static void renderRectangle(PoseStack ms, MultiBufferSource buffers, AABB aabb, boolean inner, @Nullable Integer color, byte alpha) {
		ms.pushPose();
		ms.translate(aabb.minX, aabb.minY, aabb.minZ);

		if (color == null) {
			color = Mth.hsvToRgb(ClientTickHandler.ticksInGame % 200 / 200F, 0.6F, 1F);
		}
		int r = (color >> 16 & 0xFF);
		int g = (color >> 8 & 0xFF);
		int b = (color & 0xFF);

		float f = 1F / 16F;
		float x = (float) (aabb.maxX - aabb.minX - f);
		float z = (float) (aabb.maxZ - aabb.minZ - f);

		VertexConsumer buffer = buffers.getBuffer(RenderHelper.RECTANGLE);
		Matrix4f mat = ms.last().pose();
		buffer.vertex(mat, x, f, f).color(r, g, b, alpha).endVertex();
		buffer.vertex(mat, f, f, f).color(r, g, b, alpha).endVertex();
		buffer.vertex(mat, f, f, z).color(r, g, b, alpha).endVertex();
		buffer.vertex(mat, x, f, z).color(r, g, b, alpha).endVertex();

		if (inner) {
			x += f;
			z += f;
			float f1 = f + f / 4F;
			alpha *= 2;
			buffer.vertex(mat, x, f1, 0).color(r, g, b, alpha).endVertex();
			buffer.vertex(mat, 0, f1, 0).color(r, g, b, alpha).endVertex();
			buffer.vertex(mat, 0, f1, z).color(r, g, b, alpha).endVertex();
			buffer.vertex(mat, x, f1, z).color(r, g, b, alpha).endVertex();
		}

		ms.popPose();
	}
}
