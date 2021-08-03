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
import net.minecraft.world.entity.EquipmentSlot;

public class ModelArmorManasteel extends ModelArmor {

	private final ModelPart helmAnchor;
	private final ModelPart helm;
	private final ModelPart helmTop;
	private final ModelPart helmCrystal;

	private final ModelPart bodyAnchor;
	private final ModelPart bodyTop;
	private final ModelPart bodyBottom;

	private final ModelPart armLAnchor;
	private final ModelPart armL;
	private final ModelPart armLpauldron;
	private final ModelPart armLcrystal;

	private final ModelPart armRAnchor;
	private final ModelPart armR;
	private final ModelPart armRpauldron;
	private final ModelPart armRcrystal;
	private final ModelPart pantsAnchor;
	private final ModelPart belt;
	private final ModelPart legL;
	private final ModelPart legR;

	private final ModelPart bootL;
	private final ModelPart bootLcrystal;
	private final ModelPart bootR;
	private final ModelPart bootRcrystal;

	public ModelArmorManasteel(EquipmentSlot slot) {
		super(slot);

		this.texWidth = 64;
		this.texHeight = 128;
		float s = 0.01F;

		//helm
		this.helmAnchor = new ModelPart(this, 0, 0);
		this.helmAnchor.setPos(0.0F, 0.0F, 0.0F);
		this.helmAnchor.addBox(-1.0F, -2.0F, 0.0F, 2, 2, 2, s);
		this.helm = new ModelPart(this, 0, 0);
		this.helm.setPos(0.0F, 0.0F, 0.0F);
		this.helm.addBox(-4.5F, -8.5F, -4.5F, 9, 9, 9, s);
		this.helmTop = new ModelPart(this, 36, 6);
		this.helmTop.setPos(0.0F, -8.5F, 3.5F);
		this.helmTop.addBox(-1.5F, 0.0F, -7.0F, 3, 3, 7, s);
		this.setRotateAngle(helmTop, -0.2617993877991494F, 0.0F, 0.0F);
		this.helmCrystal = new ModelPart(this, 36, 0);
		this.helmCrystal.setPos(0.0F, -7.5F, -4.5F);
		this.helmCrystal.addBox(-1.5F, -4.0F, -1.0F, 3, 5, 1, s);
		this.setRotateAngle(helmCrystal, 0.08726646259971647F, 0.0F, 0.0F);

		//body
		this.bodyAnchor = new ModelPart(this, 0, 0);
		this.bodyAnchor.setPos(0.0F, 0.0F, 0.0F);
		this.bodyAnchor.addBox(-1.0F, 0.0F, -1.0F, 2, 2, 2, s);
		this.bodyTop = new ModelPart(this, 0, 18);
		this.bodyTop.setPos(0.0F, 0.0F, 0.0F);
		this.bodyTop.addBox(-4.5F, -0.5F, -3.0F, 9, 6, 6, s);
		this.setRotateAngle(bodyTop, 0.0F, 0.0F, 0.0F);
		this.bodyBottom = new ModelPart(this, 0, 30);
		this.bodyBottom.setPos(0.0F, 0.0F, 0.0F);
		this.bodyBottom.addBox(-2.5F, 5.5F, -2.5F, 5, 3, 5, 0.0F);

		//armL
		this.armLAnchor = new ModelPart(this, 0, 0);
		this.armLAnchor.mirror = true;
		this.armLAnchor.setPos(4.0F, 2.0F, 0.0F);
		this.armLAnchor.addBox(0.0F, -1.0F, -1.0F, 2, 2, 2, s);
		this.armL = new ModelPart(this, 22, 40);
		this.armL.mirror = true;
		this.armL.setPos(0.0F, 0.0F, 0.0F);
		this.armL.addBox(1.5F, 2.0F, -2.49F, 2, 6, 5, s);
		this.armLpauldron = new ModelPart(this, 0, 40);
		this.armLpauldron.mirror = true;
		this.armLpauldron.setPos(0.0F, 0.0F, 0.0F);
		this.armLpauldron.addBox(-0.5F, -3.0F, -3.0F, 5, 4, 6, s);
		this.armLcrystal = new ModelPart(this, 36, 40);
		this.armLcrystal.mirror = true;
		this.armLcrystal.setPos(2.0F, -2.5F, 0.0F);
		this.armLcrystal.addBox(-0.5F, -2.5F, -1.5F, 2, 3, 3, s);
		this.setRotateAngle(armLcrystal, 0.0F, 0.0F, -0.08726646259971647F);

		//armR
		this.armRAnchor = new ModelPart(this, 0, 0);
		this.armRAnchor.mirror = true;
		this.armRAnchor.setPos(-4.0F, 2.0F, 0.0F);
		this.armRAnchor.addBox(-2.0F, -1.0F, -1.0F, 2, 2, 2, s);
		this.armR = new ModelPart(this, 22, 40);
		this.armR.setPos(0.0F, 0.0F, 0.0F);
		this.armR.addBox(-3.5F, 2.0F, -2.51F, 2, 6, 5, s);
		this.armRpauldron = new ModelPart(this, 0, 40);
		this.armRpauldron.setPos(0.0F, 0.0F, 0.0F);
		this.armRpauldron.addBox(-4.5F, -3.0F, -3.0F, 5, 4, 6, s);
		this.armRcrystal = new ModelPart(this, 36, 40);
		this.armRcrystal.setPos(-2.0F, -2.5F, 0.0F);
		this.armRcrystal.addBox(-1.5F, -2.5F, -1.5F, 2, 3, 3, s);
		this.setRotateAngle(armRcrystal, 0.0F, 0.0F, 0.08726646259971647F);

		//pants
		this.pantsAnchor = new ModelPart(this, 0, 0);
		this.pantsAnchor.setPos(0.0F, 0.0F, 0.0F);
		this.pantsAnchor.addBox(-1.0F, 0.0F, -1.0F, 2, 2, 2, s);
		this.belt = new ModelPart(this, 0, 51);
		this.belt.setPos(0.0F, 0.0F, 0.0F);
		this.belt.addBox(-4.5F, 9.0F, -3.0F, 9, 3, 6, s);
		this.legL = new ModelPart(this, 0, 60);
		this.legL.mirror = true;
		this.legL.setPos(1.9F, 12.0F, 0.0F);
		this.legL.addBox(-0.39F, 0.0F, -2.49F, 3, 6, 5, s);
		this.legR = new ModelPart(this, 0, 60);
		this.legR.setPos(-1.9F, 12.0F, 0.0F);
		this.legR.addBox(-2.61F, 0.0F, -2.51F, 3, 6, 5, s);

		//boots
		this.bootL = new ModelPart(this, 0, 71);
		this.bootL.mirror = true;
		this.bootL.setPos(1.9F, 12.0F, 0.0F);
		this.bootL.addBox(-2.39F, 8.5F, -2.49F, 5, 4, 5, s);
		this.bootLcrystal = new ModelPart(this, 36, 46);
		this.bootLcrystal.mirror = true;
		this.bootLcrystal.setPos(2.5F, 9.0F, 2.0F);
		this.bootLcrystal.addBox(-1.0F, -2.0F, -1.5F, 2, 3, 3, s);
		this.setRotateAngle(bootLcrystal, 0.0F, 0.0F, 0.08726646259971647F);
		this.bootR = new ModelPart(this, 0, 71);
		this.bootR.setPos(-2.0F, 12.0F, 0.0F);
		this.bootR.addBox(-2.5F, 8.5F, -2.51F, 5, 4, 5, s);
		this.bootRcrystal = new ModelPart(this, 36, 46);
		this.bootRcrystal.setPos(-2.5F, 9.0F, 2.0F);
		this.bootRcrystal.addBox(-1.0F, -2.0F, -1.5F, 2, 3, 3, s);
		this.setRotateAngle(bootRcrystal, 0.0F, 0.0F, -0.08726646259971647F);

		//hierarchy
		this.helmAnchor.addChild(this.helm);
		this.helm.addChild(this.helmTop);
		this.helm.addChild(this.helmCrystal);

		this.bodyAnchor.addChild(this.bodyTop);
		this.bodyTop.addChild(this.bodyBottom);
		this.armLAnchor.addChild(this.armL);
		this.armL.addChild(this.armLpauldron);
		this.armLpauldron.addChild(this.armLcrystal);
		this.armRAnchor.addChild(this.armR);
		this.armR.addChild(this.armRpauldron);
		this.armRpauldron.addChild(this.armRcrystal);

		this.pantsAnchor.addChild(this.belt);

		this.bootL.addChild(bootLcrystal);
		this.bootR.addChild(bootRcrystal);
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
