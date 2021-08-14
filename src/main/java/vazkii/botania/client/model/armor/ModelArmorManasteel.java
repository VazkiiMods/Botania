/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.model.armor;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;

public class ModelArmorManasteel extends ModelArmor {
	public ModelArmorManasteel(ModelPart root, EquipmentSlot slot) {
		super(root, slot);
	}

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
		body.addOrReplaceChild("belt", CubeListBuilder.create().texOffs(0, 51)
				.addBox(-4.5F, 9.0F, -3.0F, 9, 3, 6, deformation), PartPose.ZERO);

		root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 60)
				.mirror()
				.addBox(-0.39F, 0.0F, -2.49F, 3, 6, 5, deformation),
				PartPose.offset(1.9F, 12.0F, 0.0F));
		root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 60)
				.addBox(-2.61F, 0.0F, -2.51F, 3, 6, 5, deformation),
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
				.addBox(-4.5F, -8.5F, -4.5F, 9, 9, 9, deformation), PartPose.ZERO);
		helm.addOrReplaceChild("helm_top", CubeListBuilder.create().texOffs(36, 6)
				.addBox(-1.5F, 0.0F, -7.0F, 3, 3, 7, deformation),
				PartPose.offsetAndRotation(0.0F, -8.5F, 3.5F, -0.2618F, 0.0F, 0.0F));
		helm.addOrReplaceChild("helm_crystal", CubeListBuilder.create().texOffs(36, 0)
				.addBox(-1.5F, -4.0F, -1.0F, 3, 5, 1, deformation),
				PartPose.offsetAndRotation(0.0F, -7.5F, -4.5F, 0.0873F, 0.0F, 0.0F));

		var body = root.addOrReplaceChild("body", CubeListBuilder.create()
				.addBox(-1.0F, 0.0F, -1.0F, 2, 2, 2, deformation), PartPose.ZERO);
		var bodyTop = body.addOrReplaceChild("body_top", CubeListBuilder.create().texOffs(0, 18)
				.addBox(-4.5F, -0.5F, -3.0F, 9, 6, 6, deformation), PartPose.ZERO);
		bodyTop.addOrReplaceChild("body_bottom", CubeListBuilder.create().texOffs(0, 30)
				.addBox(-2.5F, 5.5F, -2.5F, 5, 3, 5), PartPose.ZERO);

		var leftArm = root.addOrReplaceChild("left_arm", CubeListBuilder.create()
				.mirror()
				.addBox(0.0F, -1.0F, -1.0F, 2, 2, 2, deformation),
				PartPose.offset(4.0F, 2.0F, 0.0F));
		var armL = leftArm.addOrReplaceChild("left_arm_main", CubeListBuilder.create().texOffs(22, 40)
				.mirror()
				.addBox(1.5F, 2.0F, -2.49F, 2, 6, 5, deformation), PartPose.ZERO);
		var armLpauldron = armL.addOrReplaceChild("left_arm_pauldron", CubeListBuilder.create().texOffs(0, 40)
				.mirror()
				.addBox(-0.5F, -3.0F, -3.0F, 5, 4, 6, deformation), PartPose.ZERO);
		armLpauldron.addOrReplaceChild("left_arm_crystal", CubeListBuilder.create().texOffs(36, 40)
				.mirror()
				.addBox(-0.5F, -2.5F, -1.5F, 2, 3, 3, deformation),
				PartPose.offsetAndRotation(2.0F, -2.5F, 0.0F, 0.0F, 0.0F, -Mth.PI / 36));

		var rightArm = root.addOrReplaceChild("right_arm", CubeListBuilder.create()
				.mirror()
				.addBox(-2.0F, -1.0F, -1.0F, 2, 2, 2, deformation),
				PartPose.offset(-4.0F, 2.0F, 0.0F));
		var armR = rightArm.addOrReplaceChild("right_arm_main", CubeListBuilder.create().texOffs(22, 40)
				.addBox(-3.5F, 2.0F, -2.51F, 2, 6, 5, deformation), PartPose.ZERO);
		var armRpauldron = armR.addOrReplaceChild("right_arm_pauldron", CubeListBuilder.create().texOffs(0, 40)
				.addBox(-4.5F, -3.0F, -3.0F, 5, 4, 6, deformation), PartPose.ZERO);
		armRpauldron.addOrReplaceChild("right_arm_crystal", CubeListBuilder.create().texOffs(36, 40)
				.addBox(-1.5F, -2.5F, -1.5F, 2, 3, 3, deformation),
				PartPose.offsetAndRotation(-2.0F, -2.5F, 0.0F, 0.0F, 0.0F, Mth.PI / 36));

		// boots
		var leftLeg = root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 71)
				.mirror()
				.addBox(-2.39F, 8.5F, -2.49F, 5, 4, 5, deformation),
				PartPose.offset(1.9F, 12.0F, 0.0F));
		leftLeg.addOrReplaceChild("left_leg_crystal", CubeListBuilder.create().texOffs(36, 46)
				.mirror()
				.addBox(-1.0F, -2.0F, -1.5F, 2, 3, 3, deformation),
				PartPose.offsetAndRotation(2.5F, 9.0F, 2.0F, 0.0F, 0.0F, Mth.PI / 36));

		var rightLeg = root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 71)
				.addBox(-2.5F, 8.5F, -2.51F, 5, 4, 5, deformation),
				PartPose.offset(-2.0F, 12.0F, 0.0F));
		rightLeg.addOrReplaceChild("right_leg_crystal", CubeListBuilder.create().texOffs(36, 46)
				.addBox(-1.0F, -2.0F, -1.5F, 2, 3, 3, deformation),
				PartPose.offsetAndRotation(-2.5F, 9.0F, 2.0F, 0.0F, 0.0F, -Mth.PI / 36));

		root.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);

		return mesh;
	}
}
