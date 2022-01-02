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
import com.mojang.math.Vector3f;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.renderer.RenderType;

public class ModelBellows extends Model {

	private final ModelPart top;
	private final ModelPart base;
	private final ModelPart pipe;
	private final ModelPart funnel;

	public ModelBellows(ModelPart root) {
		super(RenderType::entityCutout);

		top = root.getChild("top");
		base = root.getChild("base");
		pipe = root.getChild("pipe");
		funnel = root.getChild("funnel");
	}

	public static MeshDefinition createMesh() {
		var mesh = new MeshDefinition();
		var root = mesh.getRoot();
		root.addOrReplaceChild("top", CubeListBuilder.create()
				.addBox(-4.0F, -2.0F, -4.0F, 8, 1, 8),
				PartPose.offset(0.0F, 16.0F, 0.0F));
		root.addOrReplaceChild("base", CubeListBuilder.create().texOffs(0, 9)
				.addBox(-5.0F, 6.0F, -5.0F, 10, 2, 10),
				PartPose.offset(0.0F, 16.0F, 0.0F));
		root.addOrReplaceChild("pipe", CubeListBuilder.create().texOffs(0, 21)
				.addBox(-1.0F, 6.0F, -8.0F, 2, 2, 3),
				PartPose.offset(0.0F, 16.0F, 0.0F));
		root.addOrReplaceChild("funnel", CubeListBuilder.create().texOffs(40, 0)
				.addBox(0.0F, 0.0F, 0.0F, 6, 7, 6),
				PartPose.ZERO);
		return mesh;
	}

	@Override
	public void renderToBuffer(PoseStack ms, VertexConsumer buffer, int light, int overlay, float r, float g, float b, float a) {
		render(ms, buffer, light, overlay, r, g, b, a, 1);
	}

	public void render(PoseStack ms, VertexConsumer buffer, int light, int overlay, float r, float g, float b, float alpha, float fract) {
		base.render(ms, buffer, light, overlay, r, g, b, alpha);
		pipe.render(ms, buffer, light, overlay, r, g, b, alpha);

		float mov = (1F - fract) * 0.5F;

		ms.translate(0F, mov, 0F);
		top.render(ms, buffer, light, overlay, r, g, b, alpha);
		ms.translate(0F, -mov, 0F);

		ms.mulPose(Vector3f.XP.rotationDegrees(180F));
		ms.translate(-0.19F, -1.375F, -0.19F);
		ms.scale(1F, fract, 1F);
		funnel.render(ms, buffer, light, overlay, r, g, b, alpha);
		ms.scale(1F, 1F / fract, 1F);
	}

}
