/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.model;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

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

	private static RenderLayer pixieLayer(Identifier texture) {
		RenderLayer normal = RenderLayer.getEntityCutoutNoCull(texture);
		return evil && ShaderHelper.useShaders()
				? new ShaderWrappedRenderLayer(ShaderHelper.BotaniaShader.DOPPLEGANGER, RenderPixie.SHADER_CALLBACK, normal)
				: normal;
	}

	public ModelPixie() {
		super(ModelPixie::pixieLayer);
		textureWidth = 32;
		textureHeight = 32;

		body = new ModelPart(this, 0, 0);
		body.setPivot(0.0F, 16.0F, 0.0F);
		body.addCuboid(-2.5F, 0.0F, -2.5F, 5, 5, 5, 0.0F);

		leftWingT = new ModelPart(this, 0, 4);
		leftWingT.setPivot(2.5F, 18.0F, 0.5F);
		leftWingT.addCuboid(0.0F, -5.0F, 0.0F, 0, 5, 6, 0.0F);
		setRotateAngle(leftWingT, 0.2617993877991494F, 0.5235987755982988F, 0.2617993877991494F);
		leftWingB = new ModelPart(this, 0, 11);
		leftWingB.setPivot(2.5F, 18.0F, 0.5F);
		leftWingB.addCuboid(0.0F, 0.0F, 0.0F, 0, 3, 4, 0.0F);
		setRotateAngle(leftWingB, -0.2617993877991494F, 0.2617993877991494F, -0.2617993877991494F);
		rightWingT = new ModelPart(this, 0, 4);
		rightWingT.setPivot(-2.5F, 18.0F, 0.5F);
		rightWingT.addCuboid(0.0F, -5.0F, 0.0F, 0, 5, 6, 0.0F);
		setRotateAngle(rightWingT, 0.2617993877991494F, -0.5235987755982988F, -0.2617993877991494F);
		rightWingB = new ModelPart(this, 0, 11);
		rightWingB.setPivot(-2.5F, 18.0F, 0.5F);
		rightWingB.addCuboid(0.0F, 0.0F, 0.0F, 0, 3, 4, 0.0F);
		setRotateAngle(rightWingB, -0.2617993877991494F, -0.2617993877991494F, 0.2617993877991494F);
	}

	@Override
	public void render(MatrixStack ms, VertexConsumer buffer, int light, int overlay, float red, float green, float blue, float alpha) {
		body.render(ms, buffer, light, overlay);

		leftWingT.render(ms, buffer, light, overlay);
		leftWingB.render(ms, buffer, light, overlay);
		rightWingT.render(ms, buffer, light, overlay);
		rightWingB.render(ms, buffer, light, overlay);
	}

	public void setRotateAngle(ModelPart modelRenderer, float x, float y, float z) {
		modelRenderer.pitch = x;
		modelRenderer.yaw = y;
		modelRenderer.roll = z;
	}

	@Override
	public void setRotationAngles(EntityPixie entity, float f, float f1, float f2, float f3, float f4) {
		evil = entity.getPixieType() == 1;
		rightWingT.yaw = -(MathHelper.cos(f2 * 1.7F) * (float) Math.PI * 0.5F);
		leftWingT.yaw = MathHelper.cos(f2 * 1.7F) * (float) Math.PI * 0.5F;
		rightWingB.yaw = -(MathHelper.cos(f2 * 1.7F) * (float) Math.PI * 0.25F);
		leftWingB.yaw = MathHelper.cos(f2 * 1.7F) * (float) Math.PI * 0.25F;
	}

}
