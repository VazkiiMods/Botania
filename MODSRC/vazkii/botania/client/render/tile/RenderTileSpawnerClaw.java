/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jul 23, 2014, 5:46:10 PM (GMT)]
 */
package vazkii.botania.client.render.tile;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.ModelSpawnerClaw;
import vazkii.botania.client.model.ModelTinyPotato;
import vazkii.botania.common.block.tile.TileTinyPotato;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

public class RenderTileSpawnerClaw extends TileEntitySpecialRenderer {

	private static final ResourceLocation texture = new ResourceLocation(LibResources.MODEL_SPAWNER_CLAW);
	private static final ModelSpawnerClaw model = new ModelSpawnerClaw();

	@Override
	public void renderTileEntityAt(TileEntity var1, double d0, double d1, double d2, float var8) {
//		TileTinyPotato potato = (TileTinyPotato) var1;
		GL11.glPushMatrix();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		GL11.glTranslated(d0, d1, d2);

		Minecraft.getMinecraft().renderEngine.bindTexture(texture);

		GL11.glTranslatef(0.5F, 1.5F, 0.5F);
		GL11.glScalef(1F, -1F, -1F);
//		int meta = potato.getWorldObj() == null ? 3 : potato.getBlockMetadata();
//		GL11.glRotatef(meta * 90F - 180F, 0F, 1F, 0F);
//
//		int jump = potato.jumpTicks;
//		float up = (float) -Math.abs(Math.sin((float) jump / 10 * Math.PI)) * 0.2F;
//		float rot = (float) Math.sin((float) jump / 10 * Math.PI) * 2;
//
//		GL11.glTranslatef(0F, up, 0F);
//		GL11.glRotatef(rot, 0F, 0F, 1F);

		model.render();
		GL11.glColor3f(1F, 1F, 1F);
		GL11.glScalef(1F, -1F, -1F);
		GL11.glPopMatrix();
	}

}
