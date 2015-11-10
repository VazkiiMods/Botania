/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 17, 2015, 8:05:07 PM (GMT)]
 */
package vazkii.botania.client.render.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import vazkii.botania.api.mana.ILens;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.render.item.RenderLens;
import vazkii.botania.common.block.tile.mana.TilePrism;

public class RenderTilePrism extends TileEntitySpecialRenderer {

	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partTicks) {
		TilePrism prism = (TilePrism) tile;
		GL11.glPushMatrix();
		GL11.glTranslated(x, y, z);
		float pos = (float) Math.sin((ClientTickHandler.ticksInGame + partTicks) * 0.05F) * 0.5F * (1F - 1F / 16F) - 0.5F;

		ItemStack stack = prism.getStackInSlot(0);

		if(stack != null) {
			Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture);
			if(stack.getItem() instanceof ILens) {
				ILens lens = (ILens) stack.getItem();
				GL11.glPushMatrix();
				GL11.glRotatef(90F, 1F, 0F, 0F);
				GL11.glTranslatef(0F, 0F, pos);
				RenderLens.render(stack, lens.getLensColor(stack));
				GL11.glPopMatrix();
			}
		}
		GL11.glPopMatrix();
	}

}
