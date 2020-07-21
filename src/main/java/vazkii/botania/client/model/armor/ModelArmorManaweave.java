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

public class ModelArmorManaweave extends ModelArmor {

	private final ModelPart helmAnchor;
	private final ModelPart helm;

	private final ModelPart bodyAnchor;
	private final ModelPart bodyTop;
	private final ModelPart bodyBottom;

	private final ModelPart armLAnchor;
	private final ModelPart armL;
	private final ModelPart armLpauldron;
	private final ModelPart armRAnchor;
	private final ModelPart armR;
	private final ModelPart armRpauldron;

	private final ModelPart pantsAnchor;
	private final ModelPart legL;
	private final ModelPart skirtL;
	private final ModelPart legR;
	private final ModelPart skirtR;

	private final ModelPart bootL;
	private final ModelPart bootR;

	public ModelArmorManaweave(EquipmentSlot slot) {
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
		this.helm.addCuboid(-4.5F, -9.5F, -4.0F, 9, 11, 10, s);
		this.setRotateAngle(helm, 0.17453292519943295F, 0.0F, 0.0F);

		//body
		this.bodyAnchor = new ModelPart(this, 0, 0);
		this.bodyAnchor.setPivot(0.0F, 0.0F, 0.0F);
		this.bodyAnchor.addCuboid(-1.0F, 0.0F, -1.0F, 2, 2, 2, s);
		this.bodyTop = new ModelPart(this, 0, 21);
		this.bodyTop.setPivot(0.0F, 0.0F, 0.0F);
		this.bodyTop.addCuboid(-4.5F, -0.5F, -3.0F, 9, 7, 6, s);
		this.setRotateAngle(bodyTop, 0.0F, 0.0F, 0.0F);
		this.bodyBottom = new ModelPart(this, 0, 34);
		this.bodyBottom.setPivot(0.0F, 0.0F, 0.0F);
		this.bodyBottom.addCuboid(-4.5F, 6.5F, -2.5F, 9, 5, 5, s);
		this.setRotateAngle(bodyBottom, -0F, 0.0F, 0.0F);

		//armL
		this.armLAnchor = new ModelPart(this, 0, 0);
		this.armLAnchor.mirror = true;
		this.armLAnchor.setPivot(4.0F, 2.0F, 0.0F);
		this.armLAnchor.addCuboid(0.0F, -1.0F, -1.0F, 2, 2, 2, s);
		this.armL = new ModelPart(this, 0, 44);
		this.armL.mirror = true;
		this.armL.setPivot(0.0F, 0.0F, 0.0F);
		this.armL.addCuboid(-1.5F, -2.5F, -2.49F, 5, 10, 5, s);
		this.armLpauldron = new ModelPart(this, 20, 44);
		this.armLpauldron.mirror = true;
		this.armLpauldron.setPivot(0.0F, 0.0F, 0.0F);
		this.armLpauldron.addCuboid(-1.0F, -3.0F, -3.0F, 6, 5, 6, s);
		this.setRotateAngle(armLpauldron, 0.0F, 0.0F, -0.08726646259971647F);

		//armR
		this.armRAnchor = new ModelPart(this, 0, 0);
		this.armRAnchor.mirror = true;
		this.armRAnchor.setPivot(-4.0F, 2.0F, 0.0F);
		this.armRAnchor.addCuboid(-2.0F, -1.0F, -1.0F, 2, 2, 2, s);
		this.armR = new ModelPart(this, 0, 44);
		this.armR.setPivot(0.0F, 0.0F, 0.0F);
		this.armR.addCuboid(-3.5F, -2.5F, -2.51F, 5, 10, 5, s);
		this.armRpauldron = new ModelPart(this, 20, 44);
		this.armRpauldron.setPivot(0.0F, 0.0F, 0.0F);
		this.armRpauldron.addCuboid(-5.0F, -3.0F, -3.0F, 6, 5, 6, s);
		this.setRotateAngle(armRpauldron, 0.0F, 0.0F, 0.08726646259971647F);

		//pants
		this.pantsAnchor = new ModelPart(this, 0, 0);
		this.pantsAnchor.setPivot(0.0F, 0.0F, 0.0F);
		this.pantsAnchor.addCuboid(-1.0F, 0.0F, -1.0F, 2, 2, 2, s);
		this.legL = new ModelPart(this, 0, 78);
		this.legL.mirror = true;
		this.legL.setPivot(1.9F, 12.0F, 0.0F);
		this.legL.addCuboid(-2.39F, -0.5F, -2.49F, 5, 6, 5, s);
		this.skirtL = new ModelPart(this, 0, 59);
		this.skirtL.mirror = true;
		this.skirtL.setPivot(-0.5F, -2.0F, -2.5F);
		this.skirtL.addCuboid(-1.0F, 0.0F, -0.5F, 5, 13, 6, s);
		this.setRotateAngle(skirtL, 0.0F, -0.17453292519943295F, -0.2617993877991494F);
		this.legR = new ModelPart(this, 0, 78);
		this.legR.setPivot(-1.9F, 12.0F, 0.0F);
		this.legR.addCuboid(-2.61F, 0.0F, -2.51F, 5, 6, 5, s);
		this.skirtR = new ModelPart(this, 0, 59);
		this.skirtR.setPivot(0.5F, -2.0F, -2.5F);
		this.skirtR.addCuboid(-4.0F, 0.0F, -0.5F, 5, 13, 6, s);
		this.setRotateAngle(skirtR, 0.0F, 0.17453292519943295F, 0.2617993877991494F);

		//boot
		this.bootL = new ModelPart(this, 0, 89);
		this.bootL.mirror = true;
		this.bootL.setPivot(1.9F, 12.0F, 0.0F);
		this.bootL.addCuboid(-2.39F, 8.5F, -2.49F, 5, 4, 5, s);
		this.bootR = new ModelPart(this, 0, 89);
		this.bootR.setPivot(-1.9F, 12.0F, 0.0F);
		this.bootR.addCuboid(-2.61F, 8.5F, -2.51F, 5, 4, 5, s);

		//hierarchy
		this.helmAnchor.addChild(this.helm);

		this.bodyAnchor.addChild(this.bodyTop);
		this.bodyTop.addChild(this.bodyBottom);

		this.armLAnchor.addChild(this.armL);
		this.armL.addChild(this.armLpauldron);

		this.armRAnchor.addChild(this.armR);
		this.armR.addChild(this.armRpauldron);

		this.pantsAnchor.addChild(this.legL);
		this.pantsAnchor.addChild(this.legR);
		this.legL.addChild(this.skirtL);
		this.legR.addChild(this.skirtR);
	}

	@Override
	public void render(MatrixStack ms, VertexConsumer buffer, int light, int overlay, float r, float g, float b, float a) {

		helmAnchor.visible = slot == EquipmentSlot.HEAD;
		bodyAnchor.visible = slot == EquipmentSlot.CHEST;
		armRAnchor.visible = slot == EquipmentSlot.CHEST;
		armLAnchor.visible = slot == EquipmentSlot.CHEST;
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
			rightLeg = legR;
			leftLeg = legL;
		} else {
			rightLeg = bootR;
			leftLeg = bootL;
		}

		super.render(ms, buffer, light, overlay, r, g, b, a);
	}
}
