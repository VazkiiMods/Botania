/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.model.armor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;

public class ModelArmorElementium extends ModelArmor {

	private final ModelPart helmAnchor;
	private final ModelPart helm;
	private final ModelPart helmFairy;
	private final ModelPart helmWing1r;
	private final ModelPart helmWing2l;
	private final ModelPart helmWing1l;
	private final ModelPart helmWing2r;

	private final ModelPart bodyAnchor;
	private final ModelPart bodyTop;
	private final ModelPart bodyBottom;

	private final ModelPart armLAnchor;
	private final ModelPart armL;
	private final ModelPart armLpauldron;
	private final ModelPart armLwing1;
	private final ModelPart armLwing2;

	private final ModelPart armRAnchor;
	private final ModelPart armR;
	private final ModelPart armRpauldron;
	private final ModelPart armRwing1;
	private final ModelPart armRwing2;

	private final ModelPart pantsAnchor;
	private final ModelPart belt;
	private final ModelPart legL;
	private final ModelPart legR;

	private final ModelPart bootL;
	private final ModelPart bootLwing1;
	private final ModelPart bootLwing2;
	private final ModelPart bootR;
	private final ModelPart bootRwing1;
	private final ModelPart bootRwing2;

	public ModelArmorElementium(EquipmentSlot slot) {
		super(slot);

		this.texWidth = 64;
		this.texHeight = 128;
		float s = 0.01F;

		//pants
		this.pantsAnchor = new ModelPart(this, 0, 0);
		this.pantsAnchor.setPos(0.0F, 0.0F, 0.0F);
		this.pantsAnchor.addBox(-1.0F, 0.0F, -1.0F, 2, 2, 2, s);
		this.belt = new ModelPart(this, 0, 53);
		this.belt.setPos(0.0F, 0.0F, 0.0F);
		this.belt.addBox(-4.5F, 8.0F, -3.0F, 9, 5, 6, s);
		this.legL = new ModelPart(this, 0, 64);
		this.legL.mirror = true;
		this.legL.setPos(1.9F, 12.0F, 0.0F);
		this.legL.addBox(-2.39F, 0.0F, -2.49F, 5, 6, 5, s);
		this.legR = new ModelPart(this, 0, 64);
		this.legR.setPos(-1.9F, 12.0F, 0.0F);
		this.legR.addBox(-2.61F, 0.0F, -2.51F, 5, 6, 5, s);

		//hierarchy
		this.helmAnchor.addChild(this.helm);
		this.helm.addChild(this.helmFairy);
		this.helm.addChild(this.helmWing1l);
		this.helm.addChild(this.helmWing2l);
		this.helm.addChild(this.helmWing1r);
		this.helm.addChild(this.helmWing2r);

		this.bodyAnchor.addChild(this.bodyTop);
		this.bodyTop.addChild(this.bodyBottom);
		this.armLAnchor.addChild(this.armL);
		this.armL.addChild(this.armLpauldron);
		this.armLpauldron.addChild(this.armLwing1);
		this.armLpauldron.addChild(this.armLwing2);
		this.armRAnchor.addChild(this.armR);
		this.armR.addChild(this.armRpauldron);
		this.armRpauldron.addChild(this.armRwing1);
		this.armRpauldron.addChild(this.armRwing2);

		this.pantsAnchor.addChild(this.belt);

		this.bootL.addChild(bootLwing1);
		this.bootL.addChild(bootLwing2);
		this.bootR.addChild(bootRwing1);
		this.bootR.addChild(bootRwing2);
	}

	public static MeshDefinition createInsideMesh() {

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
		var armL = leftArm.addOrReplaceChild("left_arm_main", CubeListBuilder.create()
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

	@Override
	public void renderToBuffer(PoseStack ms, VertexConsumer buffer, int light, int overlay, float r, float g, float b, float a) {

		helmAnchor.visible = slot == EquipmentSlot.HEAD;
		bodyAnchor.visible = slot == EquipmentSlot.CHEST;
		armRAnchor.visible = slot == EquipmentSlot.CHEST;
		armLAnchor.visible = slot == EquipmentSlot.CHEST;
		legR.visible = slot == EquipmentSlot.LEGS;
		legL.visible = slot == EquipmentSlot.LEGS;
		bootL.visible = slot == EquipmentSlot.FEET;
		bootR.visible = slot == EquipmentSlot.FEET;
		hat.visible = false;

		head = helmAnchor;
		body = bodyAnchor;
		rightArm = armRAnchor;
		leftArm = armLAnchor;
		if (slot == EquipmentSlot.LEGS) {
			body = pantsAnchor;
			rightLeg = legR;
			leftLeg = legL;
		} else {
			rightLeg = bootR;
			leftLeg = bootL;
		}

		super.renderToBuffer(ms, buffer, light, overlay, r, g, b, a);
	}
}
