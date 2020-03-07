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
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntitySpecialFlower;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.helper.PlayerHelper;
import vazkii.botania.common.entity.EntityMagicLandmine;
import vazkii.botania.common.item.ItemTwigWand;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = LibMisc.MOD_ID)
public final class BlockHighlightRenderHandler {

	private BlockHighlightRenderHandler() {}

	@SubscribeEvent
	public static void onWorldRenderLast(RenderWorldLastEvent event) {
		Minecraft mc = Minecraft.getInstance();
		RayTraceResult pos = mc.objectMouseOver;
		MatrixStack ms = event.getMatrixStack();
		IRenderTypeBuffer.Impl buffers = IRenderTypeBuffer.immediate(Tessellator.getInstance().getBuffer());
		/* Needed because RenderState.CullState doesn't actually disable if you pass false, it keeps the current state.
		And we go into this method with cull enabled.
		*/
		RenderSystem.disableCull();

		ms.push();

		if (Botania.proxy.isClientPlayerWearingMonocle() && pos != null && pos.getType() == RayTraceResult.Type.BLOCK) {
			BlockPos bPos = ((BlockRayTraceResult) pos).getPos();

			ItemStack stackHeld = PlayerHelper.getFirstHeldItem(mc.player, ModItems.twigWand);
			if (!stackHeld.isEmpty() && ItemTwigWand.getBindMode(stackHeld)) {
				Optional<BlockPos> coords = ItemTwigWand.getBindingAttempt(stackHeld);
				if (coords.isPresent()) {
					bPos = coords.get();
				}
			}

			TileEntity tile = mc.world.getTileEntity(bPos);
			if (tile instanceof TileEntitySpecialFlower) {
				TileEntitySpecialFlower subtile = (TileEntitySpecialFlower) tile;
				RadiusDescriptor descriptor = subtile.getRadius();
				if (descriptor != null) {
					if (descriptor.isCircle()) {
						renderCircle(ms, buffers, descriptor.getSubtileCoords(), descriptor.getCircleRadius());
					} else {
						renderRectangle(ms, buffers, descriptor.getAABB(), true, null, (byte) 32);
					}
				}
			}
		}

		double offY = -1.0 / 16 + 0.005;
		for (Entity e : mc.world.getAllEntities()) {
			if (e instanceof EntityMagicLandmine) {
				BlockPos bpos = e.getPosition();
				AxisAlignedBB aabb = new AxisAlignedBB(bpos).offset(0, offY, 0).grow(2.5, 0, 2.5);

				float gs = (float) (Math.sin(ClientTickHandler.total / 20) + 1) * 0.2F + 0.6F;
				int r = (int) (105 * gs);
				int g = (int) (25 * gs);
				int b = (int) (145 * gs);
				int color = r << 16 | g << 8 | b;

				int alpha = 32;
				if (e.ticksExisted < 8) {
					alpha *= Math.min((e.ticksExisted + event.getPartialTicks()) / 8F, 1F);
				} else if (e.ticksExisted > 47) {
					alpha *= Math.min(1F - (e.ticksExisted - 47 + event.getPartialTicks()) / 8F, 1F);
				}

				renderRectangle(ms, buffers, aabb, false, color, (byte) alpha);
				offY += 0.001;
			}
		}

		ms.pop();
		buffers.draw();
	}

	private static void renderRectangle(MatrixStack ms, IRenderTypeBuffer buffers, AxisAlignedBB aabb, boolean inner, @Nullable Integer color, byte alpha) {
		double renderPosX = Minecraft.getInstance().getRenderManager().info.getProjectedView().getX();
		double renderPosY = Minecraft.getInstance().getRenderManager().info.getProjectedView().getY();
		double renderPosZ = Minecraft.getInstance().getRenderManager().info.getProjectedView().getZ();

		ms.push();
		ms.translate(aabb.minX - renderPosX, aabb.minY - renderPosY, aabb.minZ - renderPosZ);

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
		Matrix4f mat = ms.peek().getModel();
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

		ms.pop();
	}

	private static void renderCircle(MatrixStack ms, IRenderTypeBuffer buffers, BlockPos center, double radius) {
		double renderPosX = Minecraft.getInstance().getRenderManager().info.getProjectedView().getX();
		double renderPosY = Minecraft.getInstance().getRenderManager().info.getProjectedView().getY();
		double renderPosZ = Minecraft.getInstance().getRenderManager().info.getProjectedView().getZ();

		ms.push();
		double x = center.getX() + 0.5;
		double y = center.getY();
		double z = center.getZ() + 0.5;
		ms.translate(x - renderPosX, y - renderPosY, z - renderPosZ);
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
		Matrix4f mat = ms.peek().getModel();

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
		ms.pop();
	}

}
