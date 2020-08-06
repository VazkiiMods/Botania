/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.render.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Matrix4f;

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

public class RenderTileSpecialFlower extends TileEntityRenderer<TileEntity> {
	public RenderTileSpecialFlower(TileEntityRendererDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public void render(TileEntity tile, float partialTicks, MatrixStack ms, IRenderTypeBuffer buffers, int light, int overlay) {
		RenderTileFloatingFlower.renderFloatingIsland(tile, partialTicks, ms, buffers, light, overlay);
		if (!(tile instanceof TileEntitySpecialFlower)
				|| !(Minecraft.getInstance().renderViewEntity instanceof LivingEntity)) {
			return;
		}

		LivingEntity view = (LivingEntity) Minecraft.getInstance().renderViewEntity;
		if (!ItemMonocle.hasMonocle(view)) {
			return;
		}

		RayTraceResult ray = Minecraft.getInstance().objectMouseOver;
		if (ray != null && ray.getType() == RayTraceResult.Type.BLOCK) {
			BlockPos pos = ((BlockRayTraceResult) ray).getPos();

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

	private static void renderCircle(MatrixStack ms, IRenderTypeBuffer buffers, BlockPos center, double radius) {
		ms.push();
		double x = center.getX() + 0.5;
		double y = center.getY();
		double z = center.getZ() + 0.5;
		ms.translate(x, y, z);
		int color = MathHelper.hsvToRGB(ClientTickHandler.ticksInGame % 200 / 200F, 0.6F, 1F);
		int r = (color >> 16 & 0xFF);
		int g = (color >> 8 & 0xFF);
		int b = (color & 0xFF);

		int alpha = 32;
		float f = 1F / 16F;

		int totalAngles = 360;
		int drawAngles = 360;
		int step = totalAngles / drawAngles;

		radius -= f;
		IVertexBuilder buffer = buffers.getBuffer(RenderHelper.CIRCLE);
		Matrix4f mat = ms.getLast().getMatrix();

		Runnable centerFunc = () -> buffer.pos(mat, 0, f, 0).color(r, g, b, alpha).endVertex();
		List<Runnable> vertexFuncs = new ArrayList<>();
		for (int i = 0; i < totalAngles + 1; i += step) {
			double rad = (totalAngles - i) * Math.PI / 180.0;
			float xp = (float) (Math.cos(rad) * radius);
			float zp = (float) (Math.sin(rad) * radius);
			vertexFuncs.add(() -> buffer.pos(mat, xp, f, zp).color(r, g, b, alpha).endVertex());
		}
		RenderHelper.triangleFan(centerFunc, vertexFuncs);

		radius += f;
		float f1 = f + f / 4F;
		int alpha2 = 64;

		centerFunc = () -> buffer.pos(mat, 0, f1, 0).color(r, g, b, alpha2).endVertex();
		vertexFuncs.clear();
		for (int i = 0; i < totalAngles + 1; i += step) {
			double rad = (totalAngles - i) * Math.PI / 180.0;
			float xp = (float) (Math.cos(rad) * radius);
			float zp = (float) (Math.sin(rad) * radius);
			vertexFuncs.add(() -> buffer.pos(mat, xp, f1, zp).color(r, g, b, alpha2).endVertex());
		}
		RenderHelper.triangleFan(centerFunc, vertexFuncs);
		ms.pop();
	}

	public static void renderRectangle(MatrixStack ms, IRenderTypeBuffer buffers, AxisAlignedBB aabb, boolean inner, @Nullable Integer color, byte alpha) {
		ms.push();
		ms.translate(aabb.minX, aabb.minY, aabb.minZ);

		if (color == null) {
			color = MathHelper.hsvToRGB(ClientTickHandler.ticksInGame % 200 / 200F, 0.6F, 1F);
		}
		int r = (color >> 16 & 0xFF);
		int g = (color >> 8 & 0xFF);
		int b = (color & 0xFF);

		float f = 1F / 16F;
		float x = (float) (aabb.maxX - aabb.minX - f);
		float z = (float) (aabb.maxZ - aabb.minZ - f);

		IVertexBuilder buffer = buffers.getBuffer(RenderHelper.RECTANGLE);
		Matrix4f mat = ms.getLast().getMatrix();
		buffer.pos(mat, x, f, f).color(r, g, b, alpha).endVertex();
		buffer.pos(mat, f, f, f).color(r, g, b, alpha).endVertex();
		buffer.pos(mat, f, f, z).color(r, g, b, alpha).endVertex();
		buffer.pos(mat, x, f, z).color(r, g, b, alpha).endVertex();

		if (inner) {
			x += f;
			z += f;
			float f1 = f + f / 4F;
			alpha *= 2;
			buffer.pos(mat, x, f1, 0).color(r, g, b, alpha).endVertex();
			buffer.pos(mat, 0, f1, 0).color(r, g, b, alpha).endVertex();
			buffer.pos(mat, 0, f1, z).color(r, g, b, alpha).endVertex();
			buffer.pos(mat, x, f1, z).color(r, g, b, alpha).endVertex();
		}

		ms.pop();
	}
}
