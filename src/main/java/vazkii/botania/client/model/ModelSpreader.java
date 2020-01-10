/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 25, 2014, 1:55:05 PM (GMT)]
 */
package vazkii.botania.client.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;

public class ModelSpreader extends Model {

	final ModelRenderer top;
    final ModelRenderer sideL;
    final ModelRenderer sideR;
    final ModelRenderer bottom;
	final ModelRenderer back;
	
    final ModelRenderer holeT;
	final ModelRenderer holeL;
	final ModelRenderer holeR;
	final ModelRenderer holeB;
    
    final ModelRenderer inside;

	public ModelSpreader() {
        super(RenderType::getEntitySolid);
        textureWidth = 64;
		textureHeight = 64;
		
		top = new ModelRenderer(this, 0, 0);
        top.setRotationPoint(0.0F, 16.0F, 0.0F);
        top.addCuboid(-7.0F, -7.0F, -7.0F, 14, 1, 14, 0.0F);
		sideL = new ModelRenderer(this, 0, 30);
        sideL.setRotationPoint(0.0F, 16.0F, 0.0F);
        sideL.addCuboid(6.0F, -6.0F, -6.0F, 1, 12, 12, 0.0F);
        sideR = new ModelRenderer(this, 0, 30);
        sideR.setRotationPoint(0.0F, 16.0F, 0.0F);
        sideR.addCuboid(-7.0F, -6.0F, -6.0F, 1, 12, 12, 0.0F);
        back = new ModelRenderer(this, 0, 29);
        back.setRotationPoint(0.0F, 16.0F, 0.0F);
        back.addCuboid(-7.0F, -6.0F, -7.0F, 14, 12, 1, 0.0F);
        bottom = new ModelRenderer(this, 0, 15);
        bottom.setRotationPoint(0.0F, 16.0F, 0.0F);
        bottom.addCuboid(-7.0F, 6.0F, -7.0F, 14, 1, 14, 0.0F);
        
        holeT = new ModelRenderer(this, 0, 54);
        holeT.setRotationPoint(0.0F, 10.0F, 6.0F);
        holeT.addCuboid(-7.0F, 0.0F, 0.0F, 14, 4, 1, 0.0F);
        holeL = new ModelRenderer(this, 30, 54);
        holeL.setRotationPoint(2.0F, 10.0F, 6.0F);
        holeL.addCuboid(0.0F, 4.0F, 0.0F, 5, 4, 1, 0.0F);
        holeR = new ModelRenderer(this, 30, 59);
        holeR.setRotationPoint(0.0F, 10.0F, 6.0F);
        holeR.addCuboid(-7.0F, 4.0F, 0.0F, 5, 4, 1, 0.0F);
        holeB = new ModelRenderer(this, 0, 59);
        holeB.setRotationPoint(0.0F, 18.0F, 6.0F);
        holeB.addCuboid(-7.0F, 0.0F, 0.0F, 14, 4, 1, 0.0F);
        
        inside = new ModelRenderer(this, 30, 30);
        inside.setRotationPoint(0.0F, 16.0F, 0.0F);
        inside.addCuboid(-3.0F, -3.0F, -3.0F, 6, 6, 6, 0.0F);
	}

	@Override
    public void render(MatrixStack ms, IVertexBuilder buffer, int light, int overlay, float r, float g, float b, float a) {
		float f = 1F / 16F; // todo 1.15
		
		top.render(ms, buffer, light, overlay, r, g, b, a);
        sideL.render(ms, buffer, light, overlay, r, g, b, a);
        sideR.render(ms, buffer, light, overlay, r, g, b, a);
        back.render(ms, buffer, light, overlay, r, g, b, a);
        bottom.render(ms, buffer, light, overlay, r, g, b, a);
        
        holeT.render(ms, buffer, light, overlay, r, g, b, a);
        holeL.render(ms, buffer, light, overlay, r, g, b, a);
        holeR.render(ms, buffer, light, overlay, r, g, b, a);
        holeB.render(ms, buffer, light, overlay, r, g, b, a);
	}

	public void renderCube(MatrixStack ms, IVertexBuilder buffer, int light, int overlay) {
		float f = 1F / 16F; // todo 1.15
		
		inside.render(ms, buffer, light, overlay);
	}
}