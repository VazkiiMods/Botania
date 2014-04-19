/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Mar 24, 2014, 7:02:37 PM (GMT)]
 */
package vazkii.botania.client.core.handler;

import java.awt.Color;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import org.lwjgl.opengl.GL11;

import vazkii.botania.api.ITileBound;
import vazkii.botania.api.IWireframeAABBProvider;
import vazkii.botania.common.item.ItemTwigWand;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public final class BoundTileRenderer {

	@SubscribeEvent
	public void onWorldRenderLast(RenderWorldLastEvent event) {
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);

		Tessellator.renderingWorldRenderer = false;

		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		World world = player.worldObj;
		ItemStack stack = player.getCurrentEquippedItem();
		if(stack != null && stack.getItem() instanceof ItemTwigWand) {
			MovingObjectPosition pos = Minecraft.getMinecraft().objectMouseOver;
			if(pos != null) {
				TileEntity tile = world.getTileEntity(pos.blockX, pos.blockY, pos.blockZ);
				if(tile != null && tile instanceof ITileBound) {
					ChunkCoordinates coords = ((ITileBound) tile).getBinding();
					if(coords != null)
						renderBlockOutlineAt(coords, Color.HSBtoRGB(world.getTotalWorldTime() % 200 / 200F, 0.6F, 1F));
				}
			}
		}

		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}

	private void renderBlockOutlineAt(ChunkCoordinates pos, int color) {
		GL11.glPushMatrix();
		GL11.glTranslated(pos.posX - RenderManager.renderPosX, pos.posY - RenderManager.renderPosY, pos.posZ - RenderManager.renderPosZ + 1);
		Color colorRGB = new Color(color);
		GL11.glColor4ub((byte) colorRGB.getRed(), (byte) colorRGB.getGreen(), (byte) colorRGB.getBlue(), (byte) 255);

		World world = Minecraft.getMinecraft().theWorld;
		Block block = world.getBlock(pos.posX, pos.posY, pos.posZ);
		if(block != null) {
			AxisAlignedBB axis;
			
			if(block instanceof IWireframeAABBProvider)
				axis = ((IWireframeAABBProvider) block).getWireframeAABB(world, pos.posX, pos.posY, pos.posZ);
			else axis = block.getSelectedBoundingBoxFromPool(world, pos.posX, pos.posY, pos.posZ);
			
			axis.minX -= pos.posX;
			axis.maxX -= pos.posX;
			axis.minY -= pos.posY;
			axis.maxY -= pos.posY;
			axis.minZ -= pos.posZ;
			axis.maxZ -= pos.posZ;

			GL11.glTranslated(axis.minX, axis.minY, axis.minZ);
			GL11.glScaled(axis.maxX - axis.minX, axis.maxY - axis.minY, axis.maxZ - axis.minZ);

			GL11.glLineWidth(1F);
			renderBlockOutline();

			GL11.glLineWidth(4F);
			GL11.glColor4ub((byte) colorRGB.getRed(), (byte) colorRGB.getGreen(), (byte) colorRGB.getBlue(), (byte) 64);
			renderBlockOutline();
		}
		GL11.glPopMatrix();
	}

	private void renderBlockOutline() {
		Tessellator tessellator = Tessellator.instance;

		tessellator.startDrawing(GL11.GL_LINES);
		tessellator.addVertex(0, 0, 0);
		tessellator.addVertex(0, 1, 0);
		tessellator.addVertex(0, 1, 0);
		tessellator.addVertex(1, 1, 0);
		tessellator.addVertex(1, 1, 0);
		tessellator.addVertex(1, 0, 0);
		tessellator.addVertex(1, 0, 0);
		tessellator.addVertex(0, 0, 0);
		tessellator.addVertex(0, 0, -1);
		tessellator.addVertex(0, 1, -1);
		tessellator.addVertex(0, 0, -1);
		tessellator.addVertex(1, 0, -1);
		tessellator.addVertex(1, 0, -1);
		tessellator.addVertex(1, 1, -1);
		tessellator.addVertex(0, 1, -1);
		tessellator.addVertex(1, 1, -1);
		tessellator.addVertex(0, 0, 0);
		tessellator.addVertex(0, 0, -1);
		tessellator.addVertex(0, 1, 0);
		tessellator.addVertex(0, 1, -1);
		tessellator.addVertex(1, 0, 0);
		tessellator.addVertex(1, 0, -1);
		tessellator.addVertex(1, 1, 0);
		tessellator.addVertex(1, 1, -1);

		tessellator.draw();
	}
}
