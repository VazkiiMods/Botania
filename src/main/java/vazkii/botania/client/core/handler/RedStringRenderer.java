/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Nov 14, 2014, 7:05:32 PM (GMT)]
 */
package vazkii.botania.client.core.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;
import vazkii.botania.common.block.tile.string.TileRedString;
import vazkii.botania.common.core.helper.PlayerHelper;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.item.ModItems;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Random;

public final class RedStringRenderer {

	public static final Queue<TileRedString> redStringTiles = new ArrayDeque<>();
	private static float sizeAlpha = 0F;

	public static void renderAll() {
		if(!redStringTiles.isEmpty()) {
			GlStateManager.pushMatrix();
			GlStateManager.disableTexture2D();
			GlStateManager.enableBlend();
			GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
			GlStateManager.disableLighting();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GlStateManager.color(1F, 0F, 0F, sizeAlpha);

			TileRedString tile;
			while((tile = redStringTiles.poll()) != null)
				renderTile(tile);

			GlStateManager.enableTexture2D();
			GlStateManager.disableBlend();
			GL11.glPopAttrib();
			GlStateManager.popMatrix();

		}
	}

	public static void tick() {
		EntityPlayer player = Minecraft.getMinecraft().player;
		boolean hasWand = player != null && PlayerHelper.hasHeldItem(player, ModItems.twigWand);
		if(sizeAlpha > 0F && !hasWand)
			sizeAlpha -= 0.1F;
		else if(sizeAlpha < 1F &&hasWand)
			sizeAlpha += 0.1F;
	}

	private static void renderTile(TileRedString tile) {
		double renderPosX = Minecraft.getMinecraft().getRenderManager().renderPosX;
		double renderPosY = Minecraft.getMinecraft().getRenderManager().renderPosY;
		double renderPosZ = Minecraft.getMinecraft().getRenderManager().renderPosZ;

		EnumFacing dir = tile.getOrientation();
		BlockPos bind = tile.getBinding();

		if(bind != null) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(tile.getPos().getX() + 0.5 - renderPosX, tile.getPos().getY() + 0.5 - renderPosY, tile.getPos().getZ() + 0.5 - renderPosZ);
			Vector3 vecOrig = new Vector3(bind.getX() - tile.getPos().getX(), bind.getY() - tile.getPos().getY(), bind.getZ() - tile.getPos().getZ());
			Vector3 vecNorm = vecOrig.normalize();
			Vector3 vecMag = vecNorm.multiply(0.025);
			Vector3 vecApply = vecMag;

			int stages = (int) (vecOrig.mag() / vecMag.mag());

			Tessellator tessellator = Tessellator.getInstance();
			GL11.glLineWidth(1F);
			tessellator.getBuffer().begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);

			double len = (double) -ClientTickHandler.ticksInGame / 100F + new Random(dir.ordinal() ^ tile.getPos().hashCode()).nextInt(10000);
			double add = vecMag.mag();
			double rand = Math.random() - 0.5;
			for(int i = 0; i < stages; i++) {
				addVertexAtWithTranslation(tessellator, dir, vecApply.x, vecApply.y, vecApply.z, rand, len);
				rand = Math.random() - 0.5;
				vecApply = vecApply.add(vecMag);
				len += add;
				addVertexAtWithTranslation(tessellator, dir, vecApply.x, vecApply.y, vecApply.z, rand, len);
			}

			tessellator.draw();

			GlStateManager.popMatrix();
		}
	}

	private static void addVertexAtWithTranslation(Tessellator tess, EnumFacing dir, double xpos, double ypos, double zpos, double rand, double l) {
		double freq = 20;
		double ampl = (0.15 * (Math.sin(l * 2F) * 0.5 + 0.5) + 0.1) * sizeAlpha;
		double randMul = 0.05;
		double x = xpos + Math.sin(l * freq) * ampl * Math.abs(Math.abs(dir.getXOffset()) - 1) + rand * randMul;
		double y = ypos + Math.cos(l * freq) * ampl * Math.abs(Math.abs(dir.getYOffset()) - 1) + rand * randMul;
		double z = zpos + (dir.getYOffset() == 0 ? Math.sin(l * freq) : Math.cos(l * freq)) * ampl * Math.abs(Math.abs(dir.getZOffset()) - 1) + rand * randMul;

		tess.getBuffer().pos(x, y, z).endVertex();
	}

}
