/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.model.armor;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;

public class ModelArmorTerrasteel extends ModelArmor {

	private final ModelPart helmAnchor;
	private final ModelPart helm;
	private final ModelPart helmFront;
	private final ModelPart helmLeaf1l;
	private final ModelPart helmLeaf2l;
	private final ModelPart helmLeaf3l;
	private final ModelPart helmLeaf1r;
	private final ModelPart helmLeaf2r;
	private final ModelPart helmLeaf3r;
	private final ModelPart helmbranch1l;
	private final ModelPart helmbranch2l;
	private final ModelPart helmbranch1r;
	private final ModelPart helmbranch2r;

	private final ModelPart bodyAnchor;
	private final ModelPart bodyTop;
	private final ModelPart bodyBottom;

	private final ModelPart armLAnchor;
	private final ModelPart armL;
	private final ModelPart armLpauldron;
	private final ModelPart armLbranch1;
	private final ModelPart armLbranch2;

	private final ModelPart armRAnchor;
	private final ModelPart armR;
	private final ModelPart armRpauldron;
	private final ModelPart armRbranch1;
	private final ModelPart armRbranch2;

	private final ModelPart pantsAnchor;
	private final ModelPart belt;
	private final ModelPart legL;
	private final ModelPart legR;

	private final ModelPart bootL;
	private final ModelPart bootLtop;
	private final ModelPart bootLbranch1;
	private final ModelPart bootLbranch2;

	private final ModelPart bootR;
	private final ModelPart bootRtop;
	private final ModelPart bootRbranch1;
	private final ModelPart bootRbranch2;

	public ModelArmorTerrasteel(EquipmentSlot slot) {
		super(slot);

		textureWidth = 64;
		textureHeight = 128;
		float s = 0.01F;

		//helm
		this.helmAnchor = new ModelPart(this, 0, 0);
		this.helmAnchor.setPivot(0.0F, 0.0F, 0.0F);
		this.helmAnchor.addCuboid(-1.0F, -2.0F, 0.0F, 2, 2, 2, s);
		this.helm = new ModelPart(this, 0, 0);
		this.helm.setPivot(0.0F, 0.0F, 0.0F);
		this.helm.addCuboid(-4.5F, -9.0F, -4.5F, 9, 11, 9, s);
		this.helmFront = new ModelPart(this, 36, 0);
		this.helmFront.setPivot(0.0F, 0.0F, 0.0F);
		this.helmFront.addCuboid(-1.5F, -10.0F, -5.5F, 3, 8, 7, s);
		this.helmLeaf1l = new ModelPart(this, 56, 14);
		this.helmLeaf1l.mirror = true;
		this.helmLeaf1l.setPivot(4.5F, -6.0F, -2.5F);
		this.helmLeaf1l.addCuboid(0.0F, -3.0F, -1.0F, 2, 3, 1, s);
		this.setRotateAngle(helmLeaf1l, -0.2617993877991494F, -0.2617993877991494F, 0.5235987755982988F);
		this.helmLeaf2l = new ModelPart(this, 56, 8);
		this.helmLeaf2l.mirror = true;
		this.helmLeaf2l.setPivot(4.5F, -6.0F, -0.5F);
		this.helmLeaf2l.addCuboid(0.0F, -5.0F, -1.0F, 2, 5, 1, s);
		this.setRotateAngle(helmLeaf2l, -0.5235987755982988F, -0.5235987755982988F, 0.7853981633974483F);
		this.helmLeaf3l = new ModelPart(this, 56, 0);
		this.helmLeaf3l.mirror = true;
		this.helmLeaf3l.setPivot(4.5F, -6.0F, 2.5F);
		this.helmLeaf3l.addCuboid(0.0F, -7.0F, -1.0F, 2, 7, 1, s);
		this.setRotateAngle(helmLeaf3l, -0.7853981633974483F, -0.7853981633974483F, 0.7853981633974483F);
		this.helmLeaf1r = new ModelPart(this, 56, 14);
		this.helmLeaf1r.setPivot(-4.5F, -6.0F, -2.5F);
		this.helmLeaf1r.addCuboid(-2.0F, -3.0F, -1.0F, 2, 3, 1, s);
		this.setRotateAngle(helmLeaf1r, -0.2617993877991494F, 0.2617993877991494F, -0.5235987755982988F);
		this.helmLeaf2r = new ModelPart(this, 56, 8);
		this.helmLeaf2r.setPivot(-4.5F, -6.0F, -0.5F);
		this.helmLeaf2r.addCuboid(-2.0F, -5.0F, -1.0F, 2, 5, 1, s);
		this.setRotateAngle(helmLeaf2r, -0.5235987755982988F, 0.5235987755982988F, -0.7853981633974483F);
		this.helmLeaf3r = new ModelPart(this, 56, 0);
		this.helmLeaf3r.setPivot(-4.5F, -6.0F, 2.5F);
		this.helmLeaf3r.addCuboid(-2.0F, -7.0F, -1.0F, 2, 7, 1, s);
		this.setRotateAngle(helmLeaf3r, -0.7853981633974483F, 0.7853981633974483F, -0.7853981633974483F);
		this.helmbranch1l = new ModelPart(this, 36, 15);
		this.helmbranch1l.mirror = true;
		this.helmbranch1l.setPivot(2.5F, -9.0F, -3.5F);
		this.helmbranch1l.addCuboid(-1.0F, -1.0F, 0.0F, 2, 2, 7, s);
		this.setRotateAngle(helmbranch1l, 0.5235987755982988F, 0.2617993877991494F, 0.0F);
		this.helmbranch2l = new ModelPart(this, 36, 15);
		this.helmbranch2l.mirror = true;
		this.helmbranch2l.setPivot(4.5F, -3.0F, -2.5F);
		this.helmbranch2l.addCuboid(-1.0F, -1.0F, 0.0F, 2, 2, 7, s);
		this.setRotateAngle(helmbranch2l, 0.08726646259971647F, 0.2617993877991494F, 0.0F);
		this.helmbranch1r = new ModelPart(this, 36, 15);
		this.helmbranch1r.setPivot(-2.5F, -9.0F, -3.5F);
		this.helmbranch1r.addCuboid(-1.0F, -1.0F, 0.0F, 2, 2, 7, s);
		this.setRotateAngle(helmbranch1r, 0.5235987755982988F, -0.2617993877991494F, 0.0F);
		this.helmbranch2r = new ModelPart(this, 36, 15);
		this.helmbranch2r.setPivot(-4.5F, -3.0F, -2.5F);
		this.helmbranch2r.addCuboid(-1.0F, -1.0F, 0.0F, 2, 2, 7, s);
		this.setRotateAngle(helmbranch2r, 0.08726646259971647F, -0.2617993877991494F, 0.0F);

		//body
		this.bodyAnchor = new ModelPart(this, 0, 0);
		this.bodyAnchor.setPivot(0.0F, 0.0F, 0.0F);
		this.bodyAnchor.addCuboid(-1.0F, 0.0F, -1.0F, 2, 2, 2, s);
		this.bodyTop = new ModelPart(this, 0, 20);
		this.bodyTop.setPivot(0.0F, 0.0F, 0.0F);
		this.bodyTop.addCuboid(-5.5F, 0.0F, -3.0F, 11, 6, 6, s);
		this.bodyBottom = new ModelPart(this, 0, 32);
		this.bodyBottom.setPivot(0.0F, 0.0F, 0.0F);
		this.bodyBottom.addCuboid(-4.5F, 5.0F, -2.5F, 9, 3, 5, s);

		//armL
		this.armLAnchor = new ModelPart(this, 0, 0);
		this.armLAnchor.mirror = true;
		this.armLAnchor.setPivot(4.0F, 2.0F, 0.0F);
		this.armLAnchor.addCuboid(0.0F, -1.0F, -1.0F, 2, 2, 2, s);
		this.armL = new ModelPart(this, 0, 52);
		this.armL.mirror = true;
		this.armL.setPivot(0.0F, 0.0F, 0.0F);
		this.armL.addCuboid(-1.5F, 3.0F, -2.49F, 5, 8, 5, s);
		this.armLpauldron = new ModelPart(this, 0, 40);
		this.armLpauldron.mirror = true;
		this.armLpauldron.setPivot(1.5F, 0.0F, 0.0F);
		this.armLpauldron.addCuboid(-1.0F, -3.0F, -3.0F, 6, 6, 6, s);
		this.armLbranch1 = new ModelPart(this, 36, 15);
		this.armLbranch1.mirror = true;
		this.armLbranch1.setPivot(4.0F, -3.0F, -1.0F);
		this.armLbranch1.addCuboid(-1.0F, -1.0F, 0.0F, 2, 2, 7, s);
		this.setRotateAngle(armLbranch1, 0.5235987755982988F, 0.5235987755982988F, 0.0F);
		this.armLbranch2 = new ModelPart(this, 36, 24);
		this.armLbranch2.mirror = true;
		this.armLbranch2.setPivot(5.0F, -2.0F, 0.0F);
		this.armLbranch2.addCuboid(-1.0F, 0.0F, 0.0F, 2, 2, 5, s);
		this.setRotateAngle(armLbranch2, 0.08726646259971647F, 0.7853981633974483F, 0.0F);

		//armR
		this.armRAnchor = new ModelPart(this, 0, 0);
		this.armRAnchor.mirror = true;
		this.armRAnchor.setPivot(-4.0F, 2.0F, 0.0F);
		this.armRAnchor.addCuboid(-2.0F, -1.0F, -1.0F, 2, 2, 2, s);
		this.armR = new ModelPart(this, 0, 52);
		this.armR.setPivot(0.0F, 0.0F, 0.0F);
		this.armR.addCuboid(-3.5F, 3.0F, -2.51F, 5, 8, 5, s);
		this.armRpauldron = new ModelPart(this, 0, 40);
		this.armRpauldron.setPivot(-1.5F, 0.0F, 0.0F);
		this.armRpauldron.addCuboid(-5.0F, -3.0F, -3.0F, 6, 6, 6, s);
		this.armRbranch1 = new ModelPart(this, 36, 15);
		this.armRbranch1.setPivot(-4.0F, -3.0F, -1.0F);
		this.armRbranch1.addCuboid(-1.0F, -1.0F, 0.0F, 2, 2, 7, s);
		this.setRotateAngle(armRbranch1, 0.5235987755982988F, -0.5235987755982988F, 0.0F);
		this.armRbranch2 = new ModelPart(this, 36, 24);
		this.armRbranch2.setPivot(-5.0F, -2.0F, 0.0F);
		this.armRbranch2.addCuboid(-1.0F, 0.0F, 0.0F, 2, 2, 5, s);
		this.setRotateAngle(armRbranch2, 0.08726646259971647F, -0.7853981633974483F, 0.0F);

		//pants
		this.pantsAnchor = new ModelPart(this, 0, 0);
		this.pantsAnchor.setPivot(0.0F, 0.0F, 0.0F);
		this.pantsAnchor.addCuboid(-1.0F, 0.0F, -1.0F, 2, 2, 2, s);
		this.belt = new ModelPart(this, 0, 65);
		this.belt.setPivot(0.0F, 0.0F, 0.0F);
		this.belt.addCuboid(-4.5F, 8.0F, -3.0F, 9, 5, 6, s);
		this.legL = new ModelPart(this, 0, 76);
		this.legL.mirror = true;
		this.legL.setPivot(1.9F, 12.0F, 0.0F);
		this.legL.addCuboid(-2.39F, -0.01F, -2.49F, 5, 6, 5, s);
		this.legR = new ModelPart(this, 0, 76);
		this.legR.setPivot(-1.9F, 12.0F, 0.0F);
		this.legR.addCuboid(-2.61F, -0.01F, -2.51F, 5, 6, 5, s);

		//boots
		this.bootL = new ModelPart(this, 0, 94);
		this.bootL.mirror = true;
		this.bootL.setPivot(1.9F, 12.0F, 0.0F);
		this.bootL.addCuboid(-2.39F, 8.5F, -2.49F, 5, 4, 5, s);
		this.bootLtop = new ModelPart(this, 0, 87);
		this.bootLtop.mirror = true;
		this.bootLtop.setPivot(0.0F, 0.0F, 0.0F);
		this.bootLtop.addCuboid(-2.39F, 6.0F, -2.49F, 6, 2, 5, s);
		this.bootLbranch1 = new ModelPart(this, 36, 15);
		this.bootLbranch1.mirror = true;
		this.bootLbranch1.setPivot(3.5F, 6.0F, 0.0F);
		this.bootLbranch1.addCuboid(-1.0F, -1.0F, 0.0F, 2, 2, 7, s);
		this.setRotateAngle(bootLbranch1, 0.2617993877991494F, 0.2617993877991494F, -0.08726646259971647F);
		this.bootLbranch2 = new ModelPart(this, 36, 24);
		this.bootLbranch2.mirror = true;
		this.bootLbranch2.setPivot(2.5F, 9.0F, 0.0F);
		this.bootLbranch2.addCuboid(-1.0F, -1.0F, 0.0F, 2, 2, 5, s);
		this.setRotateAngle(bootLbranch2, 0.08726646259971647F, 0.7853981633974483F, 0.0F);
		this.bootR = new ModelPart(this, 0, 94);
		this.bootR.setPivot(-1.9F, 12.0F, 0.0F);
		this.bootR.addCuboid(-2.61F, 8.5F, -2.51F, 5, 4, 5, s);
		this.bootRtop = new ModelPart(this, 0, 87);
		this.bootRtop.setPivot(0.0F, 0.0F, 0.0F);
		this.bootRtop.addCuboid(-3.61F, 6.0F, -2.51F, 6, 2, 5, s);
		this.bootRbranch1 = new ModelPart(this, 36, 15);
		this.bootRbranch1.setPivot(-3.5F, 6.0F, 0.0F);
		this.bootRbranch1.addCuboid(-1.0F, -1.0F, 0.0F, 2, 2, 7, s);
		this.setRotateAngle(bootRbranch1, 0.2617993877991494F, -0.2617993877991494F, 0.08726646259971647F);
		this.bootRbranch2 = new ModelPart(this, 36, 24);
		this.bootRbranch2.setPivot(-2.5F, 9.0F, 0.5F);
		this.bootRbranch2.addCuboid(-1.0F, -1.0F, 0.0F, 2, 2, 5, s);
		this.setRotateAngle(bootRbranch2, 0.08726646259971647F, -0.7853981633974483F, 0.0F);

		//hierarchy
		this.helmAnchor.addChild(this.helm);
		this.helm.addChild(this.helmFront);
		this.helm.addChild(this.helmLeaf1l);
		this.helm.addChild(this.helmLeaf2l);
		this.helm.addChild(this.helmLeaf3l);
		this.helm.addChild(this.helmLeaf1r);
		this.helm.addChild(this.helmLeaf2r);
		this.helm.addChild(this.helmLeaf3r);
		this.helm.addChild(this.helmbranch1l);
		this.helm.addChild(this.helmbranch2l);
		this.helm.addChild(this.helmbranch1r);
		this.helm.addChild(this.helmbranch2r);

		this.bodyAnchor.addChild(this.bodyTop);
		this.bodyTop.addChild(this.bodyBottom);
		this.armLAnchor.addChild(this.armL);
		this.armL.addChild(this.armLpauldron);
		this.armLpauldron.addChild(this.armLbranch1);
		this.armLpauldron.addChild(this.armLbranch2);
		this.armRAnchor.addChild(this.armR);
		this.armR.addChild(this.armRpauldron);
		this.armRpauldron.addChild(this.armRbranch1);
		this.armRpauldron.addChild(this.armRbranch2);

		this.pantsAnchor.addChild(this.belt);

		this.bootL.addChild(bootLtop);
		this.bootLtop.addChild(bootLbranch1);
		this.bootLtop.addChild(bootLbranch2);
		this.bootR.addChild(bootRtop);
		this.bootRtop.addChild(bootRbranch1);
		this.bootRtop.addChild(bootRbranch2);
	}

	@Override
	public void render(MatrixStack ms, VertexConsumer buffer, int light, int overlay, float r, float g, float b, float a) {

		helmAnchor.visible = slot == EquipmentSlot.HEAD;
		bodyAnchor.visible = slot == EquipmentSlot.CHEST;
		armLAnchor.visible = slot == EquipmentSlot.CHEST;
		armRAnchor.visible = slot == EquipmentSlot.CHEST;
		legR.visible = slot == EquipmentSlot.LEGS;
		legL.visible = slot == EquipmentSlot.LEGS;
		bootL.visible = slot == EquipmentSlot.FEET;
		bootR.visible = slot == EquipmentSlot.FEET;

		helmet.visible = false;

		head = helmAnchor;
		torso = bodyAnchor;
		rightArm = armRAnchor;
		leftArm = armLAnchor;
		if (slot == EquipmentSlot.LEGS) {
			torso = pantsAnchor;
			rightLeg = legR;
			leftLeg = legL;
		} else {
			rightLeg = bootR;
			leftLeg = bootL;
		}

		super.render(ms, buffer, light, overlay, r, g, b, a);
	}
}
