/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Oct 1, 2015, 9:02:30 PM (GMT)]
 */
package vazkii.botania.client.render.tile;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import vazkii.botania.client.core.proxy.ClientProxy;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.ModelTeruTeruBozu;
import vazkii.botania.common.Botania;

public class RenderTileTeruTeruBozu extends TileEntitySpecialRenderer {

	private static final ResourceLocation texture = new ResourceLocation(LibResources.MODEL_TERU_TERU_BOZU);
	private static final ResourceLocation textureHalloween = new ResourceLocation(LibResources.MODEL_TERU_TERU_BOZU_HALLOWEEN);
	ModelTeruTeruBozu model = new ModelTeruTeruBozu();

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float f) {
		GL11.glPushMatrix();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		GL11.glTranslated(d0, d1, d2);
		Minecraft.getMinecraft().renderEngine.bindTexture(ClientProxy.dootDoot ? textureHalloween : texture);
		GL11.glRotatef(180F, 1F, 0F, 0F);
		double time = Botania.proxy.getWorldElapsedTicks() + f;
		boolean hasWorld = tileentity.getWorldObj() != null;
		if(hasWorld)
			time += new Random(tileentity.xCoord ^ tileentity.yCoord ^ tileentity.zCoord).nextInt(1000);

		GL11.glTranslatef(0.5F, -1.25F + (hasWorld ? (float) Math.sin(time * 0.01F) * 0.05F : 0F), -0.5F);
		if(hasWorld) {
			GL11.glRotated(time * 0.3, 0F, 1F, 0F);
			GL11.glRotatef(4F * (float) Math.sin(time * 0.05F), 0F, 0F, 1F);
			float s = 0.75F;
			GL11.glScalef(s, s, s);
		}

		model.render();
		GL11.glColor3f(1F, 1F, 1F);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glPopMatrix();
	}

}
