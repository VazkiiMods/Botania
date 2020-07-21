/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.render.tile;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
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
import java.util.Optional;

public class RenderTileSpecialFlower<T extends TileEntitySpecialFlower> extends BlockEntityRenderer<T> {
	public RenderTileSpecialFlower(BlockEntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public void render(TileEntitySpecialFlower tile, float partialTicks, MatrixStack ms, VertexConsumerProvider buffers, int light, int overlay) {
		RenderTileFloatingFlower.renderFloatingIsland(tile, partialTicks, ms, buffers, light, overlay);
		if (!(MinecraftClient.getInstance().cameraEntity instanceof LivingEntity)) {
			return;
		}

		LivingEntity view = (LivingEntity) MinecraftClient.getInstance().cameraEntity;
		if (!ItemMonocle.hasMonocle(view)) {
			return;
		}

		HitResult ray = MinecraftClient.getInstance().crosshairTarget;
		if (ray != null && ray.getType() == HitResult.Type.BLOCK) {
			BlockPos pos = ((BlockHitResult) ray).getBlockPos();

			if (tile.getPos().equals(pos) || hasBindingAttempt(view, tile.getPos())) {
				RadiusDescriptor descriptor = ((TileEntitySpecialFlower) tile).getRadius();
				if (descriptor != null) {
					ms.push();
					ms.translate(-tile.getPos().getX(), -tile.getPos().getY(), -tile.getPos().getZ());
					if (descriptor.isCircle()) {
						renderCircle(ms, buffers, descriptor.getSubtileCoords(), descriptor.getCircleRadius());
					} else {
						renderRectangle(ms, buffers, descriptor.getAABB(), true, null, (byte) 32);
					}
					ms.pop();
				}
			}
		}
	}

	private static boolean hasBindingAttempt(LivingEntity view, BlockPos tilePos) {
		ItemStack stackHeld = PlayerHelper.getFirstHeldItem(view, ModItems.twigWand);
		if (!stackHeld.isEmpty() && ItemTwigWand.getBindMode(stackHeld)) {
			Optional<BlockPos> coords = ItemTwigWand.getBindingAttempt(stackHeld);
			return coords.isPresent() && coords.get().equals(tilePos);
		}
		return false;
	}

	private static void renderCircle(MatrixStack ms, VertexConsumerProvider buffers, BlockPos center, double radius) {
		ms.push();
		double x = center.getX() + 0.5;
		double y = center.getY();
		double z = center.getZ() + 0.5;
		ms.translate(x, y, z);
		int color = MathHelper.hsvToRgb(ClientTickHandler.ticksInGame % 200 / 200F, 0.6F, 1F);
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
		Matrix4f mat = ms.peek().getModel();

		Runnable centerFunc = () -> buffer.vertex(mat, 0, f, 0).color(r, g, b, alpha).next();
		List<Runnable> vertexFuncs = new ArrayList<>();
		for (int i = 0; i < totalAngles + 1; i += step) {
			double rad = (totalAngles - i) * Math.PI / 180.0;
			float xp = (float) (Math.cos(rad) * radius);
			float zp = (float) (Math.sin(rad) * radius);
			vertexFuncs.add(() -> buffer.vertex(mat, xp, f, zp).color(r, g, b, alpha).next());
		}
		RenderHelper.triangleFan(centerFunc, vertexFuncs);

		radius += f;
		float f1 = f + f / 4F;
		int alpha2 = 64;

		centerFunc = () -> buffer.vertex(mat, 0, f1, 0).color(r, g, b, alpha2).next();
		vertexFuncs.clear();
		for (int i = 0; i < totalAngles + 1; i += step) {
			double rad = (totalAngles - i) * Math.PI / 180.0;
			float xp = (float) (Math.cos(rad) * radius);
			float zp = (float) (Math.sin(rad) * radius);
			vertexFuncs.add(() -> buffer.vertex(mat, xp, f1, zp).color(r, g, b, alpha2).next());
		}
		RenderHelper.triangleFan(centerFunc, vertexFuncs);
		ms.pop();
	}

	public static void renderRectangle(MatrixStack ms, VertexConsumerProvider buffers, Box aabb, boolean inner, @Nullable Integer color, byte alpha) {
		ms.push();
		ms.translate(aabb.minX, aabb.minY, aabb.minZ);

		if (color == null) {
			color = MathHelper.hsvToRgb(ClientTickHandler.ticksInGame % 200 / 200F, 0.6F, 1F);
		}
		int r = (color >> 16 & 0xFF);
		int g = (color >> 8 & 0xFF);
		int b = (color & 0xFF);

		float f = 1F / 16F;
		float x = (float) (aabb.maxX - aabb.minX - f);
		float z = (float) (aabb.maxZ - aabb.minZ - f);

		VertexConsumer buffer = buffers.getBuffer(RenderHelper.RECTANGLE);
		Matrix4f mat = ms.peek().getModel();
		buffer.vertex(mat, x, f, f).color(r, g, b, alpha).next();
		buffer.vertex(mat, f, f, f).color(r, g, b, alpha).next();
		buffer.vertex(mat, f, f, z).color(r, g, b, alpha).next();
		buffer.vertex(mat, x, f, z).color(r, g, b, alpha).next();

		if (inner) {
			x += f;
			z += f;
			float f1 = f + f / 4F;
			alpha *= 2;
			buffer.vertex(mat, x, f1, 0).color(r, g, b, alpha).next();
			buffer.vertex(mat, 0, f1, 0).color(r, g, b, alpha).next();
			buffer.vertex(mat, 0, f1, z).color(r, g, b, alpha).next();
			buffer.vertex(mat, x, f1, z).color(r, g, b, alpha).next();
		}

		ms.pop();
	}
}
