/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Feb 2, 2014, 6:37:00 PM (GMT)]
 */
package vazkii.botania.client.model;

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.Model;
import org.lwjgl.opengl.GL11;
import vazkii.botania.client.core.handler.ClientTickHandler;

public class ModelSpinningCubes extends Model {

	private final RendererModel spinningCube;

	public ModelSpinningCubes() {
		spinningCube = new RendererModel(this, 42, 0);
		spinningCube.addBox(0F, 0F, 0F, 1, 1, 1);
		spinningCube.setRotationPoint(0F, 0F, 0F);
		spinningCube.setTextureSize(64, 64);
	}

	public void renderSpinningCubes(int cubes, int repeat, int origRepeat) {
		GlStateManager.disableTexture();

		final float modifier = 6F;
		final float rotationModifier = 0.2F;
		final float radiusBase = 0.35F;
		final float radiusMod = 0.05F;

		double ticks = ClientTickHandler.ticksInGame + ClientTickHandler.partialTicks - 1.3 * (origRepeat - repeat);
		float offsetPerCube = 360 / cubes;

		GlStateManager.pushMatrix();
		GlStateManager.translatef(-0.025F, 0.85F, -0.025F);
		for(int i = 0; i < cubes; i++) {
			float offset = offsetPerCube * i;
			float deg = (int) (ticks / rotationModifier % 360F + offset);
			float rad = deg * (float) Math.PI / 180F;
			float radiusX = (float) (radiusBase + radiusMod * Math.sin(ticks / modifier));
			float radiusZ = (float) (radiusBase + radiusMod * Math.cos(ticks / modifier));
			float x =  (float) (radiusX * Math.cos(rad));
			float z = (float) (radiusZ * Math.sin(rad));
			float y = (float) Math.cos((ticks + 50 * i) / 5F) / 10F;

			GlStateManager.pushMatrix();
			GlStateManager.translatef(x, y, z);
			float xRotate = (float) Math.sin(ticks * rotationModifier) / 2F;
			float yRotate = (float) Math.max(0.6F, Math.sin(ticks * 0.1F) / 2F + 0.5F);
			float zRotate = (float) Math.cos(ticks * rotationModifier) / 2F;

			GlStateManager.rotatef(deg, xRotate, yRotate, zRotate);
			if(repeat < origRepeat) {
				GlStateManager.color4f(1F, 1F, 1F, (float) repeat / (float) origRepeat * 0.4F);
				GlStateManager.enableBlend();
				GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				GlStateManager.disableAlphaTest();
			} else GlStateManager.color4f(1F, 1F, 1F, 1F);

			int light = 15728880;
			int lightmapX = light % 65536;
			int lightmapY = light / 65536;

			GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, lightmapX, lightmapY);
			spinningCube.render(1F / 16F);

			if(repeat < origRepeat) {
				GlStateManager.disableBlend();
				GlStateManager.enableAlphaTest();
			}

			GlStateManager.popMatrix();
		}
		GlStateManager.popMatrix();
		GlStateManager.enableTexture();

		if(repeat != 0)
			renderSpinningCubes(cubes, repeat - 1, origRepeat);
	}

}
