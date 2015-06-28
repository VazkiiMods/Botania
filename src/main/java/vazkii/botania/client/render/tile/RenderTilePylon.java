/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Feb 18, 2014, 10:18:36 PM (GMT)]
 */
package vazkii.botania.client.render.tile;

import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.handler.MultiblockRenderHandler;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.IPylonModel;
import vazkii.botania.client.model.ModelPylon;
import vazkii.botania.client.model.ModelPylonOld;
import vazkii.botania.common.core.handler.ConfigHandler;

public class RenderTilePylon extends TileEntitySpecialRenderer {

	private static final ResourceLocation textureOld = new ResourceLocation(LibResources.MODEL_PYLON_OLD);
	private static final ResourceLocation texture = new ResourceLocation(LibResources.MODEL_PYLON);

	private static final ResourceLocation textureGreenOld = new ResourceLocation(LibResources.MODEL_PYLON_GREEN_OLD);
	private static final ResourceLocation textureGreen = new ResourceLocation(LibResources.MODEL_PYLON_GREEN);

	private static final ResourceLocation texturePinkOld = new ResourceLocation(LibResources.MODEL_PYLON_PINK_OLD);
	private static final ResourceLocation texturePink = new ResourceLocation(LibResources.MODEL_PYLON_PINK);

	IPylonModel model;
	public static boolean green = false;
	public static boolean pink = false;

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float pticks) {
		if(model == null)
			model = ConfigHandler.oldPylonModel ? new ModelPylonOld() : new ModelPylon();

			GL11.glPushMatrix();
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			float a = MultiblockRenderHandler.rendering ? 0.6F : 1F;
			GL11.glColor4f(1F, 1F, 1F, a);
			if(tileentity.getWorldObj() != null) {
				green = tileentity.getBlockMetadata() == 1;
				pink = tileentity.getBlockMetadata() == 2;
			}

			if(ConfigHandler.oldPylonModel)
				Minecraft.getMinecraft().renderEngine.bindTexture(pink ? texturePinkOld : green ? textureGreenOld : textureOld);
			else Minecraft.getMinecraft().renderEngine.bindTexture(pink ? texturePink : green ? textureGreen : texture);

			double worldTime = tileentity.getWorldObj() == null ? 0 : (double) (ClientTickHandler.ticksInGame + pticks);

			if(tileentity != null)
				worldTime += new Random(tileentity.xCoord ^ tileentity.yCoord ^ tileentity.zCoord).nextInt(360);

			if(ConfigHandler.oldPylonModel) {
				GL11.glTranslated(d0 + 0.5, d1 + 2.2, d2 + 0.5);
				GL11.glScalef(1F, -1.5F, -1F);
			} else {
				GL11.glTranslated(d0 + 0.2 + (green ? -0.1 : 0), d1 + 0.05, d2 + 0.8 + (green ? 0.1 : 0));
				float scale = green ? 0.8F : 0.6F;
				GL11.glScalef(scale, 0.6F, scale);
			}

			if(!green) {
				GL11.glPushMatrix();
				if(!ConfigHandler.oldPylonModel)
					GL11.glTranslatef(0.5F, 0F, -0.5F);
				GL11.glRotatef((float) worldTime * 1.5F, 0F, 1F, 0F);
				if(!ConfigHandler.oldPylonModel)
					GL11.glTranslatef(-0.5F, 0F, 0.5F);

				model.renderRing();
				GL11.glTranslated(0D, Math.sin(worldTime / 20D) / 20 - 0.025, 0D);
				model.renderGems();
				GL11.glPopMatrix();
			}

			GL11.glPushMatrix();
			GL11.glTranslated(0D, Math.sin(worldTime / 20D) / 17.5, 0D);

			if(!ConfigHandler.oldPylonModel)
				GL11.glTranslatef(0.5F, 0F, -0.5F);

			GL11.glRotatef((float) -worldTime, 0F, 1F, 0F);
			if(!ConfigHandler.oldPylonModel)
				GL11.glTranslatef(-0.5F, 0F, 0.5F);


			GL11.glDisable(GL11.GL_CULL_FACE);
			model.renderCrystal();

			GL11.glColor4f(1F, 1F, 1F, a);
			if(!ShaderHelper.useShaders()) {
				int light = 15728880;
				int lightmapX = light % 65536;
				int lightmapY = light / 65536;
				OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightmapX, lightmapY);
				float alpha = (float) ((Math.sin(worldTime / 20D) / 2D + 0.5) / (ConfigHandler.oldPylonModel ? 1D : 2D));
				GL11.glColor4f(1F, 1F, 1F, a * (alpha + 0.183F));
			}

			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glScalef(1.1F, 1.1F, 1.1F);
			if(!ConfigHandler.oldPylonModel)
				GL11.glTranslatef(-0.05F, -0.1F, 0.05F);
			else GL11.glTranslatef(0F, -0.09F, 0F);

			ShaderHelper.useShader(ShaderHelper.pylonGlow);
			model.renderCrystal();
			ShaderHelper.releaseShader();

			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glPopMatrix();

			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glPopMatrix();
	}


}
