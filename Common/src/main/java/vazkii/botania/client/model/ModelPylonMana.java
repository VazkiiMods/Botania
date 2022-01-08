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

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.util.Mth;

public class ModelPylonMana implements IPylonModel {

	private final ModelPart platef;
	private final ModelPart plateb;
	private final ModelPart platel;
	private final ModelPart plater;

	private final ModelPart shardlf;
	private final ModelPart shardrf;
	private final ModelPart shardlb;
	private final ModelPart shardrb;

	public ModelPylonMana(ModelPart root) {
		platef = root.getChild("platef");
		plateb = root.getChild("plateb");
		platel = root.getChild("platel");
		plater = root.getChild("plater");

		shardlf = root.getChild("shardlf");
		shardrf = root.getChild("shardrf");
		shardlb = root.getChild("shardlb");
		shardrb = root.getChild("shardrb");
	}

	public static MeshDefinition createMesh() {
		var mesh = new MeshDefinition();
		var root = mesh.getRoot();
		root.addOrReplaceChild("platef", CubeListBuilder.create().texOffs(36, 0)
				.addBox(-3.0F, -4.0F, -8.0F, 6, 8, 2),
				PartPose.offset(0.0F, 16.0F, 0.0F));
		root.addOrReplaceChild("plateb", CubeListBuilder.create().texOffs(36, 0)
				.addBox(-3.0F, -4.0F, -8.0F, 6, 8, 2),
				PartPose.offsetAndRotation(0.0F, 16.0F, 0.0F, 0.0F, Mth.PI, 0.0F));
		root.addOrReplaceChild("platel", CubeListBuilder.create().texOffs(36, 0)
				.addBox(-3.0F, -4.0F, -8.0F, 6, 8, 2),
				PartPose.offsetAndRotation(0.0F, 16.0F, 0.0F, 0.0F, Mth.PI / 2, 0.0F));
		root.addOrReplaceChild("plater", CubeListBuilder.create().texOffs(36, 0)
				.addBox(-3.0F, -4.0F, -8.0F, 6, 8, 2),
				PartPose.offsetAndRotation(0.0F, 16.0F, 0.0F, 0.0F, -Mth.PI / 2, 0.0F));
		root.addOrReplaceChild("shardlf", CubeListBuilder.create().texOffs(0, 21)
				.addBox(-5.0F, -9.0F, -5.0F, 5, 16, 3),
				PartPose.offset(0.0F, 16.0F, 0.0F));
		root.addOrReplaceChild("shardrf", CubeListBuilder.create().texOffs(16, 21)
				.addBox(2.0F, -12.0F, -5.0F, 3, 16, 3),
				PartPose.offset(0.0F, 16.0F, 0.0F));
		root.addOrReplaceChild("shardlb", CubeListBuilder.create()
				.addBox(-5.0F, -10.0F, 0.0F, 6, 16, 5),
				PartPose.offset(0.0F, 16.0F, 0.0F));
		root.addOrReplaceChild("shardrb", CubeListBuilder.create().texOffs(22, 0)
				.addBox(3.0F, -11.0F, 0.0F, 2, 16, 5),
				PartPose.offset(0.0F, 16.0F, 0.0F));
		return mesh;
	}

	@Override
	public void renderCrystal(PoseStack ms, VertexConsumer buffer, int light, int overlay) {
		shardlf.render(ms, buffer, light, overlay);
		shardrf.render(ms, buffer, light, overlay);
		shardlb.render(ms, buffer, light, overlay);
		shardrb.render(ms, buffer, light, overlay);
	}

	@Override
	public void renderRing(PoseStack ms, VertexConsumer buffer, int light, int overlay) {
		platef.render(ms, buffer, light, overlay);
		plateb.render(ms, buffer, light, overlay);
		platel.render(ms, buffer, light, overlay);
		plater.render(ms, buffer, light, overlay);
	}
}
