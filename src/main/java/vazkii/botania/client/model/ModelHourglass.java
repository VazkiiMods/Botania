/**
 * This class was created by <wiiv>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [? (GMT)]
 */
package vazkii.botania.client.model;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.Model;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class ModelHourglass extends Model {

    public RendererModel top;
    public RendererModel glassT;
	public RendererModel ring;
    public RendererModel glassB;
    public RendererModel bottom;
    
    public RendererModel sandT;
    public RendererModel sandB;

	public ModelHourglass() {
		
		textureWidth = 64;
		textureHeight = 32;

        top = new RendererModel(this, 20, 0);
        top.setRotationPoint(0.0F, 0.0F, 0.0F);
        top.addBox(-3.0F, -6.5F, -3.0F, 6, 1, 6, 0.0F);
		glassT = new RendererModel(this, 0, 0);
        glassT.setRotationPoint(0.0F, 0.0F, 0.0F);
        glassT.addBox(-2.5F, -5.5F, -2.5F, 5, 5, 5, 0.0F);
        ring = new RendererModel(this, 0, 20);
        ring.setRotationPoint(0.0F, 0.0F, 0.0F);
        ring.addBox(-1.5F, -0.5F, -1.5F, 3, 1, 3, 0.0F);
        glassB = new RendererModel(this, 0, 10);
        glassB.setRotationPoint(0.0F, 0.0F, 0.0F);
        glassB.addBox(-2.5F, 0.5F, -2.5F, 5, 5, 5, 0.0F);
        bottom = new RendererModel(this, 20, 7);
        bottom.setRotationPoint(0.0F, 0.0F, 0.0F);
        bottom.addBox(-3.0F, 5.5F, -3.0F, 6, 1, 6, 0.0F);
        
        sandT = new RendererModel(this, 20, 14);
        sandT.setRotationPoint(0.0F, 0.0F, 0.0F);
        sandT.addBox(0.0F, 0.0F, 0.0F, 4, 4, 4, 0.0F); // -2.0F, -5.0F, -2.0F
        sandB = new RendererModel(this, 20, 14);
        sandB.setRotationPoint(0.0F, 0.0F, 0.0F);
        sandB.addBox(0.0F, 0.0F, 0.0F, 4, 4, 4, 0.0F); // -2.0F, 1.0F, -2.05F
	}

	public void render(float fract1, float fract2, boolean flip, int color) {
		if(flip) {
			float fract3 = fract1;
			fract1 = fract2;
			fract2 = fract3;
		}

		float f = 1F / 16F;
		ring.render(f);
		top.render(f);
		bottom.render(f);
		Color c = new Color(color);
		GlStateManager.color3f(c.getRed() / 255F, c.getGreen() / 255F, c.getBlue() / 255F);

		GL11.glPushAttrib(GL11.GL_TRANSFORM_BIT);
		GlStateManager.disableRescaleNormal();
		GlStateManager.enableNormalize();

		if(fract1 > 0) {
			GlStateManager.pushMatrix();
			if(flip)
				GlStateManager.translatef(-2.0F * f, 1.0F * f, -2.0F * f);
			else {
				GlStateManager.rotatef(180F, 0F, 0F, 1F);
				GlStateManager.translatef(-2.0F * f, -5.0F * f, -2.0F * f);
			}
			GlStateManager.scalef(1F, fract1, 1F);
			sandT.render(f);
			GlStateManager.popMatrix();
		}

		if(fract2 > 0) {
			GlStateManager.pushMatrix();
			if(flip)
				GlStateManager.translatef(-2.0F * f, -5.0F * f, -2.0F * f);
			else {
				GlStateManager.rotatef(180F, 0F, 0F, 1F);
				GlStateManager.translatef(-2.0F * f, 1.0F * f, -2.0F * f);
			}
			GlStateManager.scalef(1F, fract2, 1F);
			sandB.render(f);
			GlStateManager.popMatrix();
		}

		GL11.glPopAttrib();

		GlStateManager.color3f(1, 1, 1);
		glassT.render(f);
		glassB.render(f);
	}

}
