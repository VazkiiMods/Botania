/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jul 8, 2014, 10:58:46 PM (GMT)]
 */
package vazkii.botania.client.render.tile;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.model.ModelMiniIsland;
import vazkii.botania.common.block.decor.IFloatingFlower;

public class RenderTileFloatingFlower extends TileEntitySpecialRenderer {

	private static final ModelMiniIsland model = new ModelMiniIsland();

	@Override
	public void renderTileEntityAt(TileEntity tile, double d0, double d1, double d2, float t, int digProgress) {
		IFloatingFlower flower = (IFloatingFlower) tile;
		GlStateManager.pushMatrix();
		GlStateManager.color(1F, 1F, 1F, 1F);
		GlStateManager.translate(d0, d1, d2);

		double worldTime = tile.getWorld() == null ? 0 : (double) (ClientTickHandler.ticksInGame + t);
		if(tile.getWorld() != null)
			worldTime += new Random(tile.getPos().hashCode()).nextInt(1000);

		GlStateManager.translate(0.5F, 0F, 0.5F);
		GlStateManager.rotate(-((float) worldTime * 0.5F), 0F, 1F, 0F);
		GlStateManager.translate(-0.5F, 0F, -0.5F);

		if(tile.getWorld() != null) {
			GlStateManager.translate(0F, (float) Math.sin(worldTime * 0.05F) * 0.1F, 0F);
			GlStateManager.rotate(4F * (float) Math.sin(worldTime * 0.04F), 1F, 0F, 0F);
		}

		Minecraft.getMinecraft().renderEngine.bindTexture(flower.getIslandType().getResource());
		GlStateManager.pushMatrix();
		GlStateManager.translate(0.5F, 1.4F, 0.5F);
		GlStateManager.scale(1F, -1F, -1F);
		model.render();
		GlStateManager.popMatrix();

		ItemStack stack = flower.getDisplayStack();
		Minecraft.getMinecraft().getRenderItem().renderItemModel(stack); // todo 1.8
//		IIcon icon = stack.getIconIndex();
//
//		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
//		float f = icon.getMinU();
//		float f1 = icon.getMaxU();
//		float f2 = icon.getMinV();
//		float f3 = icon.getMaxV();
//		GlStateManager.translate(0.25F, 0.4F, 0.5F);
//		GlStateManager.scale(0.5F, 0.5F, 0.5F);
//		ItemRenderer.renderItemIn2D(Tessellator.getInstance(), f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), 1F / 32F);
		GlStateManager.color(1F, 1F, 1F);
		GlStateManager.popMatrix();
	}

}
