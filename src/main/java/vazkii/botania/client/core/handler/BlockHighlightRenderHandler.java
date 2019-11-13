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

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.opengl.GL11;
import vazkii.botania.api.subtile.RadiusDescriptor;
import vazkii.botania.api.subtile.TileEntitySpecialFlower;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.helper.PlayerHelper;
import vazkii.botania.common.entity.EntityMagicLandmine;
import vazkii.botania.common.item.ItemTwigWand;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibMisc;

import java.awt.*;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = LibMisc.MOD_ID)
public final class BlockHighlightRenderHandler {

	private BlockHighlightRenderHandler() {}

	@SubscribeEvent
	public static void onWorldRenderLast(RenderWorldLastEvent event) {
		Minecraft mc = Minecraft.getInstance();
		RayTraceResult pos = mc.objectMouseOver;

		GlStateManager.pushMatrix();
		GlStateManager.disableTexture();
		GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
		GlStateManager.disableLighting();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		boundTile: {
			if(Botania.proxy.isClientPlayerWearingMonocle() && pos != null && pos.getType() == RayTraceResult.Type.BLOCK) {
				BlockPos bPos = ((BlockRayTraceResult) pos).getPos();

				ItemStack stackHeld = PlayerHelper.getFirstHeldItem(mc.player, ModItems.twigWand);
				if(!stackHeld.isEmpty() && ItemTwigWand.getBindMode(stackHeld)) {
					BlockPos coords = ItemTwigWand.getBoundTile(stackHeld);
					if(coords.getY() != -1)
						bPos = coords;
				}

				TileEntity tile = mc.world.getTileEntity(bPos);
				if(!(tile instanceof TileEntitySpecialFlower))
					break boundTile;
				TileEntitySpecialFlower subtile = (TileEntitySpecialFlower) tile;
				RadiusDescriptor descriptor = subtile.getRadius();
				if(descriptor == null)
					break boundTile;

				if(descriptor.isCircle())
					renderCircle(descriptor.getSubtileCoords(), descriptor.getCircleRadius());
				else renderRectangle(descriptor.getAABB(), true, null, (byte) 32);
			}
		}

		double offY = -1.0 / 16 + 0.005;
		for(Entity e : mc.world.getAllEntities())
			if(e instanceof EntityMagicLandmine) {
				BlockPos bpos = e.getPosition();
				AxisAlignedBB aabb = new AxisAlignedBB(bpos).offset(0, offY, 0).grow(2.5, 0, 2.5);

				float gs = (float) (Math.sin(ClientTickHandler.total / 20) + 1) * 0.2F + 0.6F;
				int r = (int) (105 * gs);
				int g = (int) (25 * gs);
				int b = (int) (145 * gs);
				Color color = new Color(r, g, b);

				int alpha = 32;
				if(e.ticksExisted < 8)
					alpha *= Math.min((e.ticksExisted + event.getPartialTicks()) / 8F, 1F);
				else if(e.ticksExisted > 47)
					alpha *= Math.min(1F - (e.ticksExisted - 47 + event.getPartialTicks()) / 8F, 1F);

				renderRectangle(aabb, false, color, (byte) alpha);
				offY += 0.001;
			}

		GlStateManager.enableTexture();
		GlStateManager.disableBlend();
		GL11.glPopAttrib();
		GlStateManager.popMatrix();
	}

	private static void renderRectangle(AxisAlignedBB aabb, boolean inner, Color color, byte alpha) {
		double renderPosX = Minecraft.getInstance().getRenderManager().renderPosX;
		double renderPosY = Minecraft.getInstance().getRenderManager().renderPosY;
		double renderPosZ = Minecraft.getInstance().getRenderManager().renderPosZ;

		GlStateManager.disableCull();
		GlStateManager.pushMatrix();
		GlStateManager.translated(aabb.minX - renderPosX, aabb.minY - renderPosY, aabb.minZ - renderPosZ);

		if(color == null)
			color = Color.getHSBColor(ClientTickHandler.ticksInGame % 200 / 200F, 0.6F, 1F);
		GlStateManager.color4f(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, alpha / 255F);

		double f = 1F / 16F;
		double x = aabb.maxX - aabb.minX - f;
		double z = aabb.maxZ - aabb.minZ - f;

		Tessellator tessellator = Tessellator.getInstance();
		tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		tessellator.getBuffer().pos(x, f, f).endVertex();
		tessellator.getBuffer().pos(f, f, f).endVertex();
		tessellator.getBuffer().pos(f, f, z).endVertex();
		tessellator.getBuffer().pos(x, f, z).endVertex();
		tessellator.draw();

		if(inner) {
			x += f;
			z += f;
			double f1 = f + f / 4F;
			GlStateManager.color4f(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, (alpha * 2) / 255F);
			tessellator.getBuffer().begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
			tessellator.getBuffer().pos(x, f1, 0).endVertex();
			tessellator.getBuffer().pos(0, f1, 0).endVertex();
			tessellator.getBuffer().pos(0, f1, z).endVertex();
			tessellator.getBuffer().pos(x, f1, z).endVertex();
			tessellator.draw();
		}

		GlStateManager.color4f(1, 1, 1, 1);
		GlStateManager.popMatrix();
		GlStateManager.enableCull();
	}

	private static void renderCircle(BlockPos center, double radius) {
		double renderPosX = Minecraft.getInstance().getRenderManager().renderPosX;
		double renderPosY = Minecraft.getInstance().getRenderManager().renderPosY;
		double renderPosZ = Minecraft.getInstance().getRenderManager().renderPosZ;

		GlStateManager.disableCull();
		GlStateManager.pushMatrix();
		double x = center.getX() + 0.5;
		double y = center.getY();
		double z = center.getZ() + 0.5;
		GlStateManager.translated(x - renderPosX, y - renderPosY, z - renderPosZ);
		int color = Color.HSBtoRGB(ClientTickHandler.ticksInGame % 200 / 200F, 0.6F, 1F);

		Color colorRGB = new Color(color);
		GlStateManager.color4f(colorRGB.getRed() / 255F, colorRGB.getGreen() / 255F, colorRGB.getBlue() / 255F, 0.125F);

		double f = 1F / 16F;

		int totalAngles = 360;
		int drawAngles = 360;
		int step = totalAngles / drawAngles;

		radius -= f;
		Tessellator tessellator = Tessellator.getInstance();
		tessellator.getBuffer().begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION);
		tessellator.getBuffer().pos(0, f, 0).endVertex();
		for(int i = 0; i < totalAngles + 1; i += step) {
			double rad = (totalAngles - i) * Math.PI / 180.0;
			double xp = Math.cos(rad) * radius;
			double zp = Math.sin(rad) * radius;
			tessellator.getBuffer().pos(xp, f, zp).endVertex();
		}
		tessellator.getBuffer().pos(0, f, 0).endVertex();
		tessellator.draw();

		radius += f;
		double f1 = f + f / 4F;
		GlStateManager.color4f(colorRGB.getRed() / 255F, colorRGB.getGreen() / 255F, colorRGB.getBlue() / 255F, 0.25F);
		tessellator.getBuffer().begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION);
		tessellator.getBuffer().pos(0, f1, 0).endVertex();
		for(int i = 0; i < totalAngles + 1; i += step) {
			double rad = (totalAngles - i) * Math.PI / 180.0;
			double xp = Math.cos(rad) * radius;
			double zp = Math.sin(rad) * radius;
			tessellator.getBuffer().pos(xp, f1, zp).endVertex();
		}
		tessellator.getBuffer().pos(0, f1, 0).endVertex();
		tessellator.draw();
		GlStateManager.color4f(1, 1, 1, 1);
		GlStateManager.popMatrix();
		GlStateManager.enableCull();
	}

}
