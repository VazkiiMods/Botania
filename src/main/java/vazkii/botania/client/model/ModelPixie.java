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

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.client.core.helper.ShaderWrappedRenderLayer;
import vazkii.botania.client.render.entity.RenderPixie;
import vazkii.botania.common.entity.EntityPixie;

public class ModelPixie extends EntityModel<EntityPixie> {
	public ModelRenderer body;
    public ModelRenderer leftWingT;
    public ModelRenderer leftWingB;
    public ModelRenderer rightWingT;
    public ModelRenderer rightWingB;

	private static boolean evil = false;
    private static RenderType pixieLayer(ResourceLocation texture) {
        RenderType normal = RenderType.getEntityCutoutNoCull(texture);
        return evil
              ? new ShaderWrappedRenderLayer(ShaderHelper.BotaniaShader.DOPPLEGANGER, RenderPixie.SHADER_CALLBACK, normal)
              : normal;
    }

    public ModelPixie() {
        super(ModelPixie::pixieLayer);
        textureWidth = 32;
        textureHeight = 32;
        
        body = new ModelRenderer(this, 0, 0);
        body.setRotationPoint(0.0F, 16.0F, 0.0F);
        body.addCuboid(-2.5F, 0.0F, -2.5F, 5, 5, 5, 0.0F);
        
        leftWingT = new ModelRenderer(this, 0, 4);
        leftWingT.setRotationPoint(2.5F, 18.0F, 0.5F);
        leftWingT.addCuboid(0.0F, -5.0F, 0.0F, 0, 5, 6, 0.0F);
        setRotateAngle(leftWingT, 0.2617993877991494F, 0.5235987755982988F, 0.2617993877991494F);
        leftWingB = new ModelRenderer(this, 0, 11);
        leftWingB.setRotationPoint(2.5F, 18.0F, 0.5F);
        leftWingB.addCuboid(0.0F, 0.0F, 0.0F, 0, 3, 4, 0.0F);
        setRotateAngle(leftWingB, -0.2617993877991494F, 0.2617993877991494F, -0.2617993877991494F);
        rightWingT = new ModelRenderer(this, 0, 4);
        rightWingT.setRotationPoint(-2.5F, 18.0F, 0.5F);
        rightWingT.addCuboid(0.0F, -5.0F, 0.0F, 0, 5, 6, 0.0F);
        setRotateAngle(rightWingT, 0.2617993877991494F, -0.5235987755982988F, -0.2617993877991494F);
        rightWingB = new ModelRenderer(this, 0, 11);
        rightWingB.setRotationPoint(-2.5F, 18.0F, 0.5F);
        rightWingB.addCuboid(0.0F, 0.0F, 0.0F, 0, 3, 4, 0.0F);
        setRotateAngle(rightWingB, -0.2617993877991494F, -0.2617993877991494F, 0.2617993877991494F);
    }

	@Override
    public void render(MatrixStack ms, IVertexBuilder buffer, int light, int overlay, float red, float green, float blue, float alpha) {
		body.render(ms, buffer, light, overlay);

		leftWingT.render(ms, buffer, light, overlay);
		leftWingB.render(ms, buffer, light, overlay);
		rightWingT.render(ms, buffer, light, overlay);
		rightWingB.render(ms, buffer, light, overlay);
	}
	
	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

	@Override
	public void setAngles(EntityPixie entity, float f, float f1, float f2, float f3, float f4) {
		evil = entity.getPixieType() == 1;
		rightWingT.rotateAngleY = -(MathHelper.cos(f2 * 1.7F) * (float)Math.PI * 0.5F);
		leftWingT.rotateAngleY = MathHelper.cos(f2 * 1.7F) * (float)Math.PI * 0.5F;
		rightWingB.rotateAngleY = -(MathHelper.cos(f2 * 1.7F) * (float)Math.PI * 0.25F);
		leftWingB.rotateAngleY = MathHelper.cos(f2 * 1.7F) * (float)Math.PI * 0.25F;
	}



}