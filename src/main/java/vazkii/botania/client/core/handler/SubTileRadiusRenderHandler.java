/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Mar 31, 2015, 3:16:02 PM (GMT)]
 */
package vazkii.botania.client.core.handler;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import org.lwjgl.opengl.GL11;

import vazkii.botania.api.subtile.ISubTileContainer;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.SubTileEntity;
import vazkii.botania.common.Botania;
import vazkii.botania.common.item.ItemTwigWand;
import vazkii.botania.common.item.ModItems;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class SubTileRadiusRenderHandler {

	@SubscribeEvent
	public void onWorldRenderLast(RenderWorldLastEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		MovingObjectPosition pos = mc.objectMouseOver;

		if(!Botania.proxy.isClientPlayerWearingMonocle() || pos == null || pos.entityHit != null)
			return;
		BlockPos bPos = pos.getBlockPos();

		ItemStack stackHeld = mc.thePlayer.getCurrentEquippedItem();
		if(stackHeld != null && stackHeld.getItem() == ModItems.twigWand && ItemTwigWand.getBindMode(stackHeld)) {
			BlockPos coords = ItemTwigWand.getBoundTile(stackHeld);
			if(coords.getY() != -1) {
				bPos = coords;
			}
		}

		TileEntity tile = mc.theWorld.getTileEntity(bPos);
		if(tile == null || !(tile instanceof ISubTileContainer))
			return;
		ISubTileContainer container = (ISubTileContainer) tile;
		SubTileEntity subtile = container.getSubTile();
		if(subtile == null)
			return;
		RadiusDescriptor descriptor = subtile.getRadius();
		if(descriptor == null)
			return;

		GlStateManager.pushMatrix();
		GlStateManager.disableTexture2D();
		GL11.glPushAttrib(GL11.GL_LIGHTING);
		GlStateManager.disableLighting();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		// todo 1.8 Tessellator.renderingWorldRenderer = false;

		if(descriptor.isCircle())
			renderCircle(descriptor.getSubtileCoords(), descriptor.getCircleRadius());
		else renderRectangle(descriptor.getAABB());

		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
		GL11.glPopAttrib();
		GlStateManager.popMatrix();
	}

	public void renderRectangle(AxisAlignedBB aabb) {
		double renderPosX, renderPosY, renderPosZ;

		try {
			renderPosX = (double) ClientMethodHandles.renderPosX_getter.invokeExact(Minecraft.getMinecraft().getRenderManager());
			renderPosY = (double) ClientMethodHandles.renderPosY_getter.invokeExact(Minecraft.getMinecraft().getRenderManager());
			renderPosZ = (double) ClientMethodHandles.renderPosZ_getter.invokeExact(Minecraft.getMinecraft().getRenderManager());
		} catch (Throwable t) {
			return;
		}

		GlStateManager.pushMatrix();
		GlStateManager.translate(aabb.minX - renderPosX, aabb.minY - renderPosY, aabb.minZ - renderPosZ);
		int color = Color.HSBtoRGB(ClientTickHandler.ticksInGame % 200 / 200F, 0.6F, 1F);

		Color colorRGB = new Color(color);
		GL11.glColor4ub((byte) colorRGB.getRed(), (byte) colorRGB.getGreen(), (byte) colorRGB.getBlue(), (byte) 32);

		double f = 1F / 16F;
		double x = aabb.maxX - aabb.minX - f;
		double z = aabb.maxZ - aabb.minZ - f;

		Tessellator tessellator = Tessellator.getInstance();
		tessellator.getWorldRenderer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		tessellator.getWorldRenderer().pos(x, f, f).endVertex();
		tessellator.getWorldRenderer().pos(f, f, f).endVertex();
		tessellator.getWorldRenderer().pos(f, f, z).endVertex();
		tessellator.getWorldRenderer().pos(x, f, z).endVertex();
		tessellator.draw();

		x += f;
		z += f;
		double f1 = f + f / 4F;
		GL11.glColor4ub((byte) colorRGB.getRed(), (byte) colorRGB.getGreen(), (byte) colorRGB.getBlue(), (byte) 64);
		tessellator.getWorldRenderer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		tessellator.getWorldRenderer().pos(x, f1, 0).endVertex();
		tessellator.getWorldRenderer().pos(0, f1, 0).endVertex();
		tessellator.getWorldRenderer().pos(0, f1, z).endVertex();
		tessellator.getWorldRenderer().pos(x, f1, z).endVertex();
		tessellator.draw();

		GL11.glColor4ub(((byte) 255), ((byte) 255), ((byte) 255), ((byte) 255));
		GlStateManager.popMatrix();
	}

	public void renderCircle(BlockPos center, double radius) {
		double renderPosX, renderPosY, renderPosZ;

		try {
			renderPosX = (double) ClientMethodHandles.renderPosX_getter.invokeExact(Minecraft.getMinecraft().getRenderManager());
			renderPosY = (double) ClientMethodHandles.renderPosY_getter.invokeExact(Minecraft.getMinecraft().getRenderManager());
			renderPosZ = (double) ClientMethodHandles.renderPosZ_getter.invokeExact(Minecraft.getMinecraft().getRenderManager());
		} catch (Throwable t) {
			return;
		}

		GlStateManager.pushMatrix();
		double x = center.getX() + 0.5;
		double y = center.getY();
		double z = center.getZ() + 0.5;
		GlStateManager.translate(x - renderPosX, y - renderPosY, z - renderPosZ);
		int color = Color.HSBtoRGB(ClientTickHandler.ticksInGame % 200 / 200F, 0.6F, 1F);

		Color colorRGB = new Color(color);
		GL11.glColor4ub((byte) colorRGB.getRed(), (byte) colorRGB.getGreen(), (byte) colorRGB.getBlue(), (byte) 32);

		double f = 1F / 16F;

		int totalAngles = 360;
		int drawAngles = 360;
		int step = totalAngles / drawAngles;

		radius -= f;
		Tessellator tessellator = Tessellator.getInstance();
		tessellator.getWorldRenderer().begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION);
		tessellator.getWorldRenderer().pos(0, f, 0).endVertex();
		for(int i = 0; i < totalAngles + 1; i += step) {
			double rad = (totalAngles - i) * Math.PI / 180.0;
			double xp = Math.cos(rad) * radius;
			double zp = Math.sin(rad) * radius;
			tessellator.getWorldRenderer().pos(xp, f, zp).endVertex();
		}
		tessellator.getWorldRenderer().pos(0, f, 0).endVertex();
		tessellator.draw();

		radius += f;
		double f1 = f + f / 4F;
		GL11.glColor4ub((byte) colorRGB.getRed(), (byte) colorRGB.getGreen(), (byte) colorRGB.getBlue(), (byte) 64);
		tessellator.getWorldRenderer().begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION);
		tessellator.getWorldRenderer().pos(0, f1, 0).endVertex();
		for(int i = 0; i < totalAngles + 1; i += step) {
			double rad = (totalAngles - i) * Math.PI / 180.0;
			double xp = Math.cos(rad) * radius;
			double zp = Math.sin(rad) * radius;
			tessellator.getWorldRenderer().pos(xp, f1, zp).endVertex();
		}
		tessellator.getWorldRenderer().pos(0, f1, 0).endVertex();
		tessellator.draw();
		GL11.glColor4ub(((byte) 255), ((byte) 255), ((byte) 255), ((byte) 255));
		GlStateManager.popMatrix();
	}

}
