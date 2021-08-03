/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.client.core.helper.ShaderWrappedRenderLayer;
import vazkii.botania.client.render.entity.RenderPixie;
import vazkii.botania.common.entity.EntityPixie;

public class ModelPixie extends EntityModel<EntityPixie> {
	public ModelPart body;
	public ModelPart leftWingT;
	public ModelPart leftWingB;
	public ModelPart rightWingT;
	public ModelPart rightWingB;

	private static boolean evil = false;

	private static RenderType pixieLayer(ResourceLocation texture) {
		RenderType normal = RenderType.entityCutoutNoCull(texture);
		return evil && ShaderHelper.useShaders()
				? new ShaderWrappedRenderLayer(ShaderHelper.BotaniaShader.DOPPLEGANGER, RenderPixie.SHADER_CALLBACK, normal)
				: normal;
	}

	public ModelPixie() {
		super(ModelPixie::pixieLayer);
		texWidth = 32;
		texHeight = 32;

		body = new ModelPart(this, 0, 0);
		body.setPos(0.0F, 16.0F, 0.0F);
		body.addBox(-2.5F, 0.0F, -2.5F, 5, 5, 5, 0.0F);

		leftWingT = new ModelPart(this, 0, 4);
		leftWingT.setPos(2.5F, 18.0F, 0.5F);
		leftWingT.addBox(0.0F, -5.0F, 0.0F, 0, 5, 6, 0.0F);
		setRotateAngle(leftWingT, 0.2617993877991494F, 0.5235987755982988F, 0.2617993877991494F);
		leftWingB = new ModelPart(this, 0, 11);
		leftWingB.setPos(2.5F, 18.0F, 0.5F);
		leftWingB.addBox(0.0F, 0.0F, 0.0F, 0, 3, 4, 0.0F);
		setRotateAngle(leftWingB, -0.2617993877991494F, 0.2617993877991494F, -0.2617993877991494F);
		rightWingT = new ModelPart(this, 0, 4);
		rightWingT.setPos(-2.5F, 18.0F, 0.5F);
		rightWingT.addBox(0.0F, -5.0F, 0.0F, 0, 5, 6, 0.0F);
		setRotateAngle(rightWingT, 0.2617993877991494F, -0.5235987755982988F, -0.2617993877991494F);
		rightWingB = new ModelPart(this, 0, 11);
		rightWingB.setPos(-2.5F, 18.0F, 0.5F);
		rightWingB.addBox(0.0F, 0.0F, 0.0F, 0, 3, 4, 0.0F);
		setRotateAngle(rightWingB, -0.2617993877991494F, -0.2617993877991494F, 0.2617993877991494F);
	}

	@Override
	public void renderToBuffer(PoseStack ms, VertexConsumer buffer, int light, int overlay, float red, float green, float blue, float alpha) {
		body.render(ms, buffer, light, overlay);

		leftWingT.render(ms, buffer, light, overlay);
		leftWingB.render(ms, buffer, light, overlay);
		rightWingT.render(ms, buffer, light, overlay);
		rightWingB.render(ms, buffer, light, overlay);
	}

	public void setRotateAngle(ModelPart modelRenderer, float x, float y, float z) {
		modelRenderer.xRot = x;
		modelRenderer.yRot = y;
		modelRenderer.zRot = z;
	}

	@Override
	public void setupAnim(EntityPixie entity, float f, float f1, float f2, float f3, float f4) {
		evil = entity.getPixieType() == 1;
		rightWingT.yRot = -(Mth.cos(f2 * 1.7F) * (float) Math.PI * 0.5F);
		leftWingT.yRot = Mth.cos(f2 * 1.7F) * (float) Math.PI * 0.5F;
		rightWingB.yRot = -(Mth.cos(f2 * 1.7F) * (float) Math.PI * 0.25F);
		leftWingB.yRot = Mth.cos(f2 * 1.7F) * (float) Math.PI * 0.25F;
	}

}
