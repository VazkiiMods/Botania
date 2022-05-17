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

public class ModelArmorTerrasteel {

	public static MeshDefinition createInsideMesh() {
		var deformation = new CubeDeformation(0.01F);
		var mesh = new MeshDefinition();
		var root = mesh.getRoot();
		root.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.ZERO);
		root.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);
		root.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.ZERO);
		root.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.ZERO);

		var body = root.addOrReplaceChild("body", CubeListBuilder.create()
				.addBox(-1.0F, 0.0F, -1.0F, 2, 2, 2, deformation), PartPose.ZERO);
		body.addOrReplaceChild("belt", CubeListBuilder.create().texOffs(0, 65)
				.addBox(-4.5F, 8.0F, -3.0F, 9, 5, 6, deformation), PartPose.ZERO);

		root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 76)
				.mirror()
				.addBox(-2.39F, -0.01F, -2.49F, 5, 6, 5, deformation),
				PartPose.offset(1.9F, 12.0F, 0.0F));
		root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 76)
				.addBox(-2.61F, -0.01F, -2.51F, 5, 6, 5, deformation),
				PartPose.offset(-1.9F, 12.0F, 0.0F));
		return mesh;
	}

	public static MeshDefinition createOutsideMesh() {
		var deformation = new CubeDeformation(0.01F);
		var mesh = new MeshDefinition();
		var root = mesh.getRoot();

		var head = root.addOrReplaceChild("head", CubeListBuilder.create()
				.addBox(-1.0F, -2.0F, 0.0F, 2, 2, 2, deformation), PartPose.ZERO);
		var helm = head.addOrReplaceChild("helm", CubeListBuilder.create()
				.addBox(-4.5F, -9.0F, -4.5F, 9, 11, 9, deformation), PartPose.ZERO);
		helm.addOrReplaceChild("helm_front", CubeListBuilder.create().texOffs(36, 0)
				.addBox(-1.5F, -10.0F, -5.5F, 3, 8, 7, deformation), PartPose.ZERO);
		helm.addOrReplaceChild("helm_leaf_1l", CubeListBuilder.create().texOffs(56, 14)
				.mirror()
				.addBox(0.0F, -3.0F, -1.0F, 2, 3, 1, deformation),
				PartPose.offsetAndRotation(4.5F, -6.0F, -2.5F, -Mth.PI / 12, -Mth.PI / 12, Mth.PI / 6));
		helm.addOrReplaceChild("helm_leaf_2l", CubeListBuilder.create().texOffs(56, 8)
				.mirror()
				.addBox(0.0F, -5.0F, -1.0F, 2, 5, 1, deformation),
				PartPose.offsetAndRotation(4.5F, -6.0F, -0.5F, -Mth.PI / 6, -Mth.PI / 6, Mth.PI / 4));
		helm.addOrReplaceChild("helm_leaf_3l", CubeListBuilder.create().texOffs(56, 0)
				.mirror()
				.addBox(0.0F, -7.0F, -1.0F, 2, 7, 1, deformation),
				PartPose.offsetAndRotation(4.5F, -6.0F, 2.5F, -Mth.PI / 4, -Mth.PI / 4, Mth.PI / 4));
		helm.addOrReplaceChild("helm_leaf_1r", CubeListBuilder.create().texOffs(56, 14)
				.addBox(-2.0F, -3.0F, -1.0F, 2, 3, 1, deformation),
				PartPose.offsetAndRotation(-4.5F, -6.0F, -2.5F, -Mth.PI / 12, Mth.PI / 12, -Mth.PI / 6));
		helm.addOrReplaceChild("helm_leaf_2r", CubeListBuilder.create().texOffs(56, 8)
				.addBox(-2.0F, -5.0F, -1.0F, 2, 5, 1, deformation),
				PartPose.offsetAndRotation(-4.5F, -6.0F, -0.5F, -Mth.PI / 6, Mth.PI / 6, -Mth.PI / 4));
		helm.addOrReplaceChild("helm_leaf_3r", CubeListBuilder.create().texOffs(56, 0)
				.addBox(-2.0F, -7.0F, -1.0F, 2, 7, 1, deformation),
				PartPose.offsetAndRotation(-4.5F, -6.0F, 2.5F, -Mth.PI / 4, Mth.PI / 4, -Mth.PI / 4));
		helm.addOrReplaceChild("helm_branch_1l", CubeListBuilder.create().texOffs(36, 15)
				.mirror()
				.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 7, deformation),
				PartPose.offsetAndRotation(2.5F, -9.0F, -3.5F, Mth.PI / 6, Mth.PI / 12, 0.0F));
		helm.addOrReplaceChild("helm_branch_2l", CubeListBuilder.create().texOffs(36, 15)
				.mirror()
				.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 7, deformation),
				PartPose.offsetAndRotation(4.5F, -3.0F, -2.5F, Mth.PI / 36, Mth.PI / 12, 0.0F));
		helm.addOrReplaceChild("helm_branch_1r", CubeListBuilder.create().texOffs(36, 15)
				.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 7, deformation),
				PartPose.offsetAndRotation(-2.5F, -9.0F, -3.5F, Mth.PI / 6, -Mth.PI / 12, 0.0F));
		helm.addOrReplaceChild("helm_branch_2r", CubeListBuilder.create().texOffs(36, 15)
				.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 7, deformation),
				PartPose.offsetAndRotation(-4.5F, -3.0F, -2.5F, Mth.PI / 36, -Mth.PI / 12, 0.0F));

		var body = root.addOrReplaceChild("body", CubeListBuilder.create()
				.addBox(-1.0F, 0.0F, -1.0F, 2, 2, 2, deformation), PartPose.ZERO);
		var bodyTop = body.addOrReplaceChild("body_top", CubeListBuilder.create().texOffs(0, 20)
				.addBox(-5.5F, 0.0F, -3.0F, 11, 6, 6, deformation), PartPose.ZERO);
		bodyTop.addOrReplaceChild("body_bottom", CubeListBuilder.create().texOffs(0, 32)
				.addBox(-4.5F, 5.0F, -2.5F, 9, 3, 5, deformation), PartPose.ZERO);

		var leftArm = root.addOrReplaceChild("left_arm", CubeListBuilder.create()
				.mirror()
				.addBox(0.0F, -1.0F, -1.0F, 2, 2, 2, deformation),
				PartPose.offset(4.0F, 2.0F, 0.0F));
		var armL = leftArm.addOrReplaceChild("left_arm_main", CubeListBuilder.create().texOffs(0, 52)
				.mirror()
				.addBox(-1.5F, 3.0F, -2.49F, 5, 8, 5, deformation), PartPose.ZERO);
		var armLpauldron = armL.addOrReplaceChild("left_arm_pauldron", CubeListBuilder.create().texOffs(0, 40)
				.mirror()
				.addBox(-1.0F, -3.0F, -3.0F, 6, 6, 6, deformation),
				PartPose.offset(1.5F, 0.0F, 0.0F));
		armLpauldron.addOrReplaceChild("left_arm_branch_1", CubeListBuilder.create().texOffs(36, 15)
				.mirror()
				.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 7, deformation),
				PartPose.offsetAndRotation(4.0F, -3.0F, -1.0F, Mth.PI / 6, Mth.PI / 6, 0.0F));
		armLpauldron.addOrReplaceChild("left_arm_branch_2", CubeListBuilder.create().texOffs(36, 24)
				.mirror()
				.addBox(-1.0F, 0.0F, 0.0F, 2, 2, 5, deformation),
				PartPose.offsetAndRotation(5.0F, -2.0F, 0.0F, Mth.PI / 36, Mth.PI / 4, 0.0F));

		var rightArm = root.addOrReplaceChild("right_arm", CubeListBuilder.create()
				.mirror()
				.addBox(-2.0F, -1.0F, -1.0F, 2, 2, 2, deformation),
				PartPose.offset(-4.0F, 2.0F, 0.0F));
		var armR = rightArm.addOrReplaceChild("right_arm_main", CubeListBuilder.create().texOffs(0, 52)
				.addBox(-3.5F, 3.0F, -2.51F, 5, 8, 5, deformation), PartPose.ZERO);
		var armRpauldron = armR.addOrReplaceChild("right_arm_pauldron", CubeListBuilder.create().texOffs(0, 40)
				.addBox(-5.0F, -3.0F, -3.0F, 6, 6, 6, deformation),
				PartPose.offset(-1.5F, 0.0F, 0.0F));
		armRpauldron.addOrReplaceChild("right_arm_branch_1", CubeListBuilder.create().texOffs(36, 15)
				.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 7, deformation),
				PartPose.offsetAndRotation(-4.0F, -3.0F, -1.0F, Mth.PI / 6, -Mth.PI / 6, 0.0F));
		armRpauldron.addOrReplaceChild("right_arm_branch_2", CubeListBuilder.create().texOffs(36, 24)
				.addBox(-1.0F, 0.0F, 0.0F, 2, 2, 5, deformation),
				PartPose.offsetAndRotation(-5.0F, -2.0F, 0.0F, Mth.PI / 36, -Mth.PI / 4, 0.0F));

		//boots
		var leftLeg = root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 94)
				.mirror()
				.addBox(-2.39F, 8.5F, -2.49F, 5, 4, 5, deformation),
				PartPose.offset(1.9F, 12.0F, 0.0F));
		var bootLtop = leftLeg.addOrReplaceChild("left_boot_top", CubeListBuilder.create().texOffs(0, 87)
				.mirror()
				.addBox(-2.39F, 6.0F, -2.49F, 6, 2, 5, deformation), PartPose.ZERO);
		bootLtop.addOrReplaceChild("left_boot_branch_1", CubeListBuilder.create().texOffs(36, 15)
				.mirror()
				.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 7, deformation),
				PartPose.offsetAndRotation(3.5F, 6.0F, 0.0F, Mth.PI / 12, Mth.PI / 12, -Mth.PI / 36));
		bootLtop.addOrReplaceChild("left_boot_branch_2", CubeListBuilder.create().texOffs(36, 24)
				.mirror()
				.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 5, deformation),
				PartPose.offsetAndRotation(2.5F, 9.0F, 0.0F, Mth.PI / 36, Mth.PI / 4, 0.0F));

		var rightLeg = root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 94)
				.addBox(-2.61F, 8.5F, -2.51F, 5, 4, 5, deformation),
				PartPose.offset(-1.9F, 12.0F, 0.0F));
		var bootRtop = rightLeg.addOrReplaceChild("right_boot_top", CubeListBuilder.create().texOffs(0, 87)
				.addBox(-3.61F, 6.0F, -2.51F, 6, 2, 5, deformation), PartPose.ZERO);
		bootRtop.addOrReplaceChild("right_boot_branch_1", CubeListBuilder.create().texOffs(36, 15)
				.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 7, deformation),
				PartPose.offsetAndRotation(-3.5F, 6.0F, 0.0F, Mth.PI / 12, -Mth.PI / 12, Mth.PI / 36));
		bootRtop.addOrReplaceChild("right_boot_branch_2", CubeListBuilder.create().texOffs(36, 24)
				.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 5, deformation),
				PartPose.offsetAndRotation(-2.5F, 9.0F, 0.5F, Mth.PI / 36, -Mth.PI / 4, 0.0F));

		root.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);
		return mesh;
	}
}
