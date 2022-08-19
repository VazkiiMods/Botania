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

import java.awt.Color;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

import org.lwjgl.opengl.GL11;

public class ModelHourglass extends ModelBase {

	public ModelRenderer ring;
	public ModelRenderer base1;
	public ModelRenderer base2;
	public ModelRenderer glass1;
	public ModelRenderer sand1;
	public ModelRenderer glass2;
	public ModelRenderer sand2;

	public ModelHourglass() {
		textureWidth = 48;
		textureHeight = 24;
		sand2 = new ModelRenderer(this, 24, 0);
		sand2.setRotationPoint(0.0F, 0.0F, 0.0F);
		sand2.addBox(0F, 0F, 0F, 5, 5, 5, 0.0F); // -2.5F, 1.0F, -2.5F
		sand1 = new ModelRenderer(this, 24, 0);
		sand1.setRotationPoint(0.0F, 0.0F, 0.0F);
		sand1.addBox(0F, 0F, 0F, 5, 5, 5, 0.0F); // -2.5F, -6.0F, -2.5F
		glass1 = new ModelRenderer(this, 0, 0);
		glass1.setRotationPoint(0.0F, 0.0F, 0.0F);
		glass1.addBox(-3.0F, -6.501F, -3.0F, 6, 6, 6, 0.0F);
		base2 = new ModelRenderer(this, 0, 12);
		base2.setRotationPoint(0.0F, 0.0F, 0.0F);
		base2.addBox(-3.5F, 6.502F, -3.5F, 7, 1, 7, 0.0F);
		base1 = new ModelRenderer(this, 0, 12);
		base1.setRotationPoint(0.0F, 0.0F, 0.0F);
		base1.addBox(-3.5F, -7.502F, -3.5F, 7, 1, 7, 0.0F);
		ring = new ModelRenderer(this, 28, 12);
		ring.setRotationPoint(0.0F, 15.5F, 0.0F);
		ring.addBox(-2.0F, -16F, -2.0F, 4, 1, 4, 0.0F);
		glass2 = new ModelRenderer(this, 0, 0);
		glass2.setRotationPoint(0.0F, 0.0F, 0.0F);
		glass2.addBox(-3.0F, 0.501F, -3.0F, 6, 6, 6, 0.0F);
	}

	public void render(float fract1, float fract2, boolean flip, int color) {
		if(flip) {
			float fract3 = fract1;
			fract1 = fract2;
			fract2 = fract3;
		}

		float f = 1F / 16F;
		ring.render(f);
		base1.render(f);
		base2.render(f);
		Color c = new Color(color);
		GL11.glColor3ub((byte) c.getRed(), (byte) c.getGreen(), (byte) c.getBlue());
		GL11.glPushAttrib(GL11.GL_NORMALIZE);
		GL11.glEnable(GL11.GL_NORMALIZE);

		if(fract1 > 0) {
			GL11.glPushMatrix();
			if(flip)
				GL11.glTranslatef(-2.5F * f, 1.0F * f, -2.5F * f);
			else {
				GL11.glRotatef(180F, 0F, 0F, 1F);
				GL11.glTranslatef(-2.5F * f, -6.0F * f, -2.5F * f);
			}

			GL11.glScalef(1F, fract1, 1F);
			sand1.render(f);
			GL11.glPopMatrix();
		}

		if(fract2 > 0) {
			GL11.glPushMatrix();
			if(flip)
				GL11.glTranslatef(-2.5F * f, -6.0F * f, -2.5F * f);
			else {
				GL11.glRotatef(180F, 0F, 0F, 1F);
				GL11.glTranslatef(-2.5F * f, 1.0F * f, -2.5F * f);
			}

			GL11.glScalef(1F, fract2, 1F);
			sand2.render(f);
			GL11.glPopMatrix();
		}

		GL11.glPopAttrib();
		GL11.glColor3f(1F, 1F, 1F);
		glass1.render(f);
		glass2.render(f);
	}

}
