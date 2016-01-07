/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jun 28, 2015, 10:19:20 PM (GMT)]
 */
package vazkii.botania.client.render.tile;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import vazkii.botania.common.block.tile.TileSparkChanger;

public class RenderTileSparkChanger extends TileEntitySpecialRenderer<TileSparkChanger> {

	@Override
	public void renderTileEntityAt(TileSparkChanger tileentity, double d0, double d1, double d2, float pticks, int digProgress) {
		GlStateManager.pushMatrix();
		GlStateManager.translate(d0, d1, d2);
		GlStateManager.rotate(90F, 1F, 0F, 0F);
		GlStateManager.translate(0.8F, 0.2F, -0.22F);
		GlStateManager.color(1F, 1F, 1F, 1F);
		ItemStack stack = tileentity.getStackInSlot(0);
		if(stack != null) {
			GlStateManager.pushMatrix();
			Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
			float s = 0.6F;
			GlStateManager.scale(s, s, s);
			GlStateManager.rotate(180F, 0F, 1F, 0F);
			GlStateManager.translate(0.5, 0.5, 0);
			Minecraft.getMinecraft().getRenderItem().renderItem(stack, ItemCameraTransforms.TransformType.GROUND);
			GlStateManager.popMatrix();
		}
		GlStateManager.color(1F, 1F, 1F, 1F);
		GlStateManager.popMatrix();
	}

}
