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

public class ElementiumArmorModel {

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
		body.addOrReplaceChild("belt", CubeListBuilder.create().texOffs(0, 53)
				.addBox(-4.5F, 8.0F, -3.0F, 9, 5, 6, deformation), PartPose.ZERO);
		root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 64)
				.mirror()
				.addBox(-2.39F, 0.0F, -2.49F, 5, 6, 5, deformation),
				PartPose.offset(1.9F, 12.0F, 0.0F));
		root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 64)
				.addBox(-2.61F, 0.0F, -2.51F, 5, 6, 5, deformation),
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
				.addBox(-4.5F, -9.0F, -4.5F, 9, 9, 9, deformation), PartPose.ZERO);
		helm.addOrReplaceChild("helm_fairy", CubeListBuilder.create().texOffs(36, 11)
				.addBox(-2.5F, -10.0F, -5.5F, 5, 5, 5, deformation), PartPose.ZERO);
		helm.addOrReplaceChild("helm_wing_1l", CubeListBuilder.create().texOffs(36, 0)
				.mirror()
				.addBox(-1.0F, -5.0F, 0.0F, 1, 5, 6, deformation),
				PartPose.offsetAndRotation(4.5F, -6.0F, -0.5F, Mth.PI / 12, Mth.PI / 6, Mth.PI / 12));
		helm.addOrReplaceChild("helm_wing_2l", CubeListBuilder.create().texOffs(50, 0)
				.addBox(-1.0F, 0.0F, 0.0F, 1, 3, 4, deformation),
				PartPose.offsetAndRotation(4.5F, -6.0F, -0.5F, -Mth.PI / 12, Mth.PI / 12, -Mth.PI / 12));
		helm.addOrReplaceChild("helm_wing_1r", CubeListBuilder.create().texOffs(36, 0)
				.addBox(0.0F, -5.0F, 0.0F, 1, 5, 6, deformation),
				PartPose.offsetAndRotation(-4.5F, -6.0F, -0.5F, Mth.PI / 12, -Mth.PI / 6, -Mth.PI / 12));
		helm.addOrReplaceChild("helm_wing_2r", CubeListBuilder.create().texOffs(50, 0)
				.mirror()
				.addBox(0.0F, 0.0F, 0.0F, 1, 3, 4, deformation),
				PartPose.offsetAndRotation(-4.5F, -6.0F, -0.5F, -Mth.PI / 12, -Mth.PI / 12, Mth.PI / 12));

		var body = root.addOrReplaceChild("body", CubeListBuilder.create()
				.addBox(-1.0F, 0.0F, -1.0F, 2, 2, 2, deformation), PartPose.ZERO);
		var bodyTop = body.addOrReplaceChild("body_top", CubeListBuilder.create().texOffs(0, 19)
				.addBox(-4.5F, 0.0F, -3.0F, 9, 6, 6, deformation), PartPose.ZERO);
		bodyTop.addOrReplaceChild("body_bottom", CubeListBuilder.create().texOffs(0, 31)
				.addBox(-3.5F, 5.0F, -2.5F, 7, 3, 5, deformation), PartPose.ZERO);

		var leftArm = root.addOrReplaceChild("left_arm", CubeListBuilder.create()
				.mirror()
				.addBox(0.0F, -1.0F, -1.0F, 2, 2, 2, deformation),
				PartPose.offset(4.0F, 2.0F, 0.0F));
		var armL = leftArm.addOrReplaceChild("left_arm_main", CubeListBuilder.create().texOffs(24, 40)
				.mirror()
				.addBox(0.5F, 4.5F, -2.49F, 3, 6, 5, deformation), PartPose.ZERO);
		var armLpauldron = armL.addOrReplaceChild("left_arm_pauldron", CubeListBuilder.create().texOffs(0, 40)
				.mirror()
				.addBox(-0.5F, -3.0F, -3.0F, 6, 7, 6, deformation), PartPose.ZERO);
		armLpauldron.addOrReplaceChild("left_arm_wing_1", CubeListBuilder.create().texOffs(40, 35)
				.addBox(0.0F, -4.0F, 0.0F, 0, 4, 5, deformation),
				PartPose.offsetAndRotation(6.0F, -1.0F, 0.0F, Mth.PI / 12, Mth.PI / 6, Mth.PI / 12));
		armLpauldron.addOrReplaceChild("left_arm_wing_2", CubeListBuilder.create().texOffs(40, 40)
				.addBox(0.0F, 0.0F, 0.0F, 0, 3, 4, deformation),
				PartPose.offsetAndRotation(6.0F, -1.0F, 0.0F, -Mth.PI / 12, Mth.PI / 12, -Mth.PI / 12));

		var rightArm = root.addOrReplaceChild("right_arm", CubeListBuilder.create()
				.mirror()
				.addBox(-2.0F, -1.0F, -1.0F, 2, 2, 2, deformation),
				PartPose.offset(-4.0F, 2.0F, 0.0F));
		var armR = rightArm.addOrReplaceChild("right_arm_main", CubeListBuilder.create().texOffs(24, 40)
				.addBox(-3.5F, 4.5F, -2.51F, 3, 6, 5, deformation),
				PartPose.ZERO);
		var armRpauldron = armR.addOrReplaceChild("right_arm_pauldron", CubeListBuilder.create().texOffs(0, 40)
				.addBox(-5.5F, -3.0F, -3.0F, 6, 7, 6, deformation), PartPose.ZERO);
		armRpauldron.addOrReplaceChild("right_arm_wing_1", CubeListBuilder.create().texOffs(40, 35)
				.mirror()
				.addBox(0.0F, -4.0F, 0.0F, 0, 4, 5, deformation),
				PartPose.offsetAndRotation(-6.5F, -1.0F, 0.0F, Mth.PI / 12, -Mth.PI / 6, -Mth.PI / 12));
		armRpauldron.addOrReplaceChild("right_arm_wing_2", CubeListBuilder.create().texOffs(40, 40)
				.mirror()
				.addBox(0.0F, 0.0F, 0.0F, 0, 3, 4, deformation),
				PartPose.offsetAndRotation(-6.5F, -1.0F, 0.0F, -Mth.PI / 12, -Mth.PI / 12, Mth.PI / 12));

		// boots
		var bootL = root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 75)
				.mirror()
				.addBox(-2.39F, 8.5F, -2.49F, 5, 5, 5, deformation),
				PartPose.offset(1.9F, 12.0F, 0.0F));
		bootL.addOrReplaceChild("left_boot_wing_1", CubeListBuilder.create().texOffs(40, 40)
				.addBox(0.0F, -3.0F, 0.0F, 0, 3, 4, deformation),
				PartPose.offsetAndRotation(2.5F, 8.5F, 0.0F, Mth.PI / 12, Mth.PI / 6, Mth.PI / 12));
		bootL.addOrReplaceChild("left_boot_wing_2", CubeListBuilder.create().texOffs(40, 44)
				.addBox(0.0F, 0.0F, 0.0F, 0, 2, 3, deformation),
				PartPose.offsetAndRotation(2.5F, 8.5F, 0.0F, -Mth.PI / 12, Mth.PI / 12, -Mth.PI / 12));

		var bootR = root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 75)
				.addBox(-2.61F, 8.5F, -2.51F, 5, 5, 5, deformation),
				PartPose.offset(-1.9F, 12.0F, 0.0F));
		bootR.addOrReplaceChild("right_boot_wing_1", CubeListBuilder.create().texOffs(40, 40)
				.mirror()
				.addBox(0.0F, -3.0F, 0.0F, 0, 3, 4, deformation),
				PartPose.offsetAndRotation(-2.6F, 8.5F, 0.0F, Mth.PI / 12, -Mth.PI / 6, -Mth.PI / 12));
		bootR.addOrReplaceChild("right_boot_wing_2", CubeListBuilder.create().texOffs(40, 44)
				.mirror()
				.addBox(0.0F, 0.0F, 0.0F, 0, 2, 3, deformation),
				PartPose.offsetAndRotation(-2.5F, 8.5F, 0.0F, -Mth.PI / 12, -Mth.PI / 12, Mth.PI / 12));

		root.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);
		return mesh;
	}
}
