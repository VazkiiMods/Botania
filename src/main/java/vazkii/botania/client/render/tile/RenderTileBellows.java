/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Aug 28, 2015, 5:30:41 PM (GMT)]
 */
package vazkii.botania.client.render.tile;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.ModelBellows;
import vazkii.botania.common.block.tile.mana.TileBellows;

public class RenderTileBellows extends TileEntitySpecialRenderer {

	private static final float[] ROTATIONS = new float[] {
		180F, 0F, 90F, 270F
	};

	private static final ResourceLocation texture = new ResourceLocation(LibResources.MODEL_BELLOWS);
	private static final ModelBellows model = new ModelBellows();

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float f) {
		TileBellows bellows = (TileBellows) tileentity;

		GL11.glPushMatrix();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		GL11.glTranslated(d0, d1, d2);

		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		int meta = bellows.getWorldObj() != null ? bellows.getBlockMetadata() : 0;

		GL11.glTranslatef(0.5F, 1.5F, 0.5F);
		GL11.glScalef(1F, -1F, -1F);
		GL11.glRotatef(ROTATIONS[Math.max(Math.min(ROTATIONS.length, meta - 2), 0)], 0F, 1F, 0F);
		model.render(Math.max(0.1F, 1F - (bellows.movePos + bellows.moving * f + 0.1F)));
		GL11.glColor3f(1F, 1F, 1F);
		GL11.glScalef(1F, -1F, -1F);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glPopMatrix();
	}

}
