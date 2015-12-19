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

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import vazkii.botania.common.block.tile.string.TileRedString;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.item.ModItems;

public final class RedStringRenderer {

	public static final Queue<TileRedString> redStringTiles = new ArrayDeque();
	static float sizeAlpha = 0F;

	public static void renderAll() {
		if(!redStringTiles.isEmpty()) {
			GL11.glPushMatrix();
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glPushAttrib(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glColor4f(1F, 0F, 0F, sizeAlpha);

			Tessellator.renderingWorldRenderer = false;
			TileRedString tile;
			while((tile = redStringTiles.poll()) != null)
				renderTile(tile);

			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glPopAttrib();
			GL11.glPopMatrix();

		}
	}

	public static void tick() {
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		boolean hasWand = player != null && player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() == ModItems.twigWand;
		if(sizeAlpha > 0F && !hasWand)
			sizeAlpha -= 0.1F;
		else if(sizeAlpha < 1F &&hasWand)
			sizeAlpha += 0.1F;
	}

	private static void renderTile(TileRedString tile) {
		ForgeDirection dir = ForgeDirection.getOrientation(tile.getBlockMetadata());
		ChunkCoordinates bind = tile.getBinding();

		if(bind != null) {
			GL11.glPushMatrix();
			GL11.glTranslated(tile.xCoord + 0.5 - RenderManager.renderPosX, tile.yCoord + 0.5 - RenderManager.renderPosY, tile.zCoord + 0.5 - RenderManager.renderPosZ);
			Vector3 vecOrig = new Vector3(bind.posX - tile.xCoord, bind.posY - tile.yCoord, bind.posZ - tile.zCoord);
			Vector3 vecNorm = vecOrig.copy().normalize();
			Vector3 vecMag = vecNorm.copy().multiply(0.025);
			Vector3 vecApply = vecMag.copy();

			int stages = (int) (vecOrig.mag() / vecMag.mag());

			Tessellator tessellator = Tessellator.instance;
			GL11.glLineWidth(1F);
			tessellator.startDrawing(GL11.GL_LINES);

			double len = (double) -ClientTickHandler.ticksInGame / 100F + new Random(dir.ordinal() ^ tile.xCoord ^ tile.yCoord ^ tile.zCoord).nextInt(10000);
			double add = vecMag.mag();
			double rand = Math.random() - 0.5;
			for(int i = 0; i < stages; i++) {
				addVertexAtWithTranslation(tessellator, dir, vecApply.x, vecApply.y, vecApply.z, rand, len);
				rand = Math.random() - 0.5;
				vecApply.add(vecMag);
				len += add;
				addVertexAtWithTranslation(tessellator, dir, vecApply.x, vecApply.y, vecApply.z, rand, len);
			}

			tessellator.draw();

			GL11.glPopMatrix();
		}
	}

	private static void addVertexAtWithTranslation(Tessellator tess, ForgeDirection dir, double xpos, double ypos, double zpos, double rand, double l) {
		double freq = 20;
		double ampl = (0.15 * (Math.sin(l * 2F) * 0.5 + 0.5) + 0.1) * sizeAlpha;
		double randMul = 0.05;
		double x = xpos + Math.sin(l * freq) * ampl * Math.abs(Math.abs(dir.offsetX) - 1) + rand * randMul;
		double y = ypos + Math.cos(l * freq) * ampl * Math.abs(Math.abs(dir.offsetY) - 1) + rand * randMul;
		double z = zpos + (dir.offsetY == 0 ? Math.sin(l * freq) : Math.cos(l * freq)) * ampl * Math.abs(Math.abs(dir.offsetZ) - 1) + rand * randMul;

		tess.addVertex(x, y, z);
	}

}
