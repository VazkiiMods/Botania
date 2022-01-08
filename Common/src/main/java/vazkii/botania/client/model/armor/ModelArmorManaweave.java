/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.model.armor;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.util.Mth;

public class ModelArmorManaweave {

	public static MeshDefinition createInsideMesh() {
		var deformation = new CubeDeformation(0.01F);
		var mesh = new MeshDefinition();
		var root = mesh.getRoot();
		root.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.ZERO);
		root.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);
		root.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.ZERO);
		root.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.ZERO);
		root.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.ZERO);
		var legL = root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 78)
				.mirror()
				.addBox(-2.39F, -0.5F, -2.49F, 5, 6, 5, deformation),
				PartPose.offset(1.9F, 12.0F, 0.0F));
		legL.addOrReplaceChild("left_leg_skirt", CubeListBuilder.create().texOffs(0, 59)
				.mirror()
				.addBox(-1.0F, 0.0F, -0.5F, 5, 13, 6, deformation),
				PartPose.offsetAndRotation(-0.5F, -2.0F, -2.5F, 0.0F, -Mth.PI / 18, -Mth.PI / 12));
		var legR = root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 78)
				.addBox(-2.61F, 0.0F, -2.51F, 5, 6, 5, deformation),
				PartPose.offset(-1.9F, 12.0F, 0.0F));
		legR.addOrReplaceChild("right_leg_skirt", CubeListBuilder.create().texOffs(0, 59)
				.addBox(-4.0F, 0.0F, -0.5F, 5, 13, 6, deformation),
				PartPose.offsetAndRotation(0.5F, -2.0F, -2.5F, 0.0F, Mth.PI / 18, Mth.PI / 12));
		return mesh;
	}

	public static MeshDefinition createOutsideMesh() {
		var deformation = new CubeDeformation(0.01F);
		var mesh = new MeshDefinition();
		var root = mesh.getRoot();
		var head = root.addOrReplaceChild("head", CubeListBuilder.create()
				.addBox(-1.0F, -2.0F, 0.0F, 2, 2, 2, deformation), PartPose.ZERO);
		head.addOrReplaceChild("helm", CubeListBuilder.create()
				.addBox(-4.5F, -9.5F, -4.0F, 9, 11, 10, deformation),
				PartPose.rotation(Mth.PI / 18, 0.0F, 0.0F));
		var body = root.addOrReplaceChild("body", CubeListBuilder.create()
				.addBox(-1.0F, 0.0F, -1.0F, 2, 2, 2, deformation), PartPose.ZERO);
		var bodyTop = body.addOrReplaceChild("body_top", CubeListBuilder.create().texOffs(0, 21)
				.addBox(-4.5F, -0.5F, -3.0F, 9, 7, 6), PartPose.ZERO);
		bodyTop.addOrReplaceChild("body_bottom", CubeListBuilder.create().texOffs(0, 34)
				.addBox(-4.5F, 6.5F, -2.5F, 9, 5, 5, deformation), PartPose.ZERO);

		var leftArm = root.addOrReplaceChild("left_arm", CubeListBuilder.create().mirror()
				.addBox(0.0F, -1.0F, -1.0F, 2, 2, 2, deformation),
				PartPose.offset(4.0F, 2.0F, 0.0F));
		var armL = leftArm.addOrReplaceChild("left_arm_main", CubeListBuilder.create().texOffs(0, 44)
				.mirror()
				.addBox(-1.5F, -2.5F, -2.49F, 5, 10, 5), PartPose.ZERO);
		armL.addOrReplaceChild("left_arm_pauldron", CubeListBuilder.create().texOffs(20, 44)
				.mirror()
				.addBox(-1.0F, -3.0F, -3.0F, 6, 5, 6),
				PartPose.rotation(0.0F, 0.0F, -Mth.PI / 36));

		var rightArm = root.addOrReplaceChild("right_arm", CubeListBuilder.create().mirror()
				.addBox(-2.0F, -1.0F, -1.0F, 2, 2, 2, deformation),
				PartPose.offset(-4.0F, 2.0F, 0.0F));
		var armR = rightArm.addOrReplaceChild("right_arm_main", CubeListBuilder.create().texOffs(0, 44)
				.addBox(-3.5F, -2.5F, -2.51F, 5, 10, 5, deformation), PartPose.ZERO);
		armR.addOrReplaceChild("right_arm_pauldron", CubeListBuilder.create().texOffs(20, 44)
				.addBox(-5.0F, -3.0F, -3.0F, 6, 5, 6, deformation),
				PartPose.rotation(0.0F, 0.0F, Mth.PI / 36));

		// boots
		root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 89)
				.mirror()
				.addBox(-2.39F, 8.5F, -2.49F, 5, 4, 5, deformation),
				PartPose.offset(1.9F, 12.0F, 0.0F));
		root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 89)
				.addBox(-2.61F, 8.5F, -2.51F, 5, 4, 5, deformation),
				PartPose.offset(-1.9F, 12.0F, 0.0F));

		root.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);
		return mesh;
	}
}
