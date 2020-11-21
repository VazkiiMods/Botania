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

		this.textureWidth = 64;
		this.textureHeight = 128;
		float s = 0.01F;

		//helm
		this.helmAnchor = new ModelPart(this, 0, 0);
		this.helmAnchor.setPivot(0.0F, 0.0F, 0.0F);
		this.helmAnchor.addCuboid(-1.0F, -2.0F, 0.0F, 2, 2, 2, s);
		this.helm = new ModelPart(this, 0, 0);
		this.helm.setPivot(0.0F, 0.0F, 0.0F);
		this.helm.addCuboid(-4.5F, -9.0F, -4.5F, 9, 9, 9, s);
		this.helmFairy = new ModelPart(this, 36, 11);
		this.helmFairy.setPivot(0.0F, 0.0F, 0.0F);
		this.helmFairy.addCuboid(-2.5F, -10.0F, -5.5F, 5, 5, 5, s);
		this.helmWing1l = new ModelPart(this, 36, 0);
		this.helmWing1l.mirror = true;
		this.helmWing1l.setPivot(4.5F, -6.0F, -0.5F);
		this.helmWing1l.addCuboid(-1.0F, -5.0F, 0.0F, 1, 5, 6, s);
		this.setRotateAngle(helmWing1l, 0.2617993877991494F, 0.5235987755982988F, 0.2617993877991494F);
		this.helmWing2l = new ModelPart(this, 50, 0);
		this.helmWing2l.setPivot(4.5F, -6.0F, -0.5F);
		this.helmWing2l.addCuboid(-1.0F, 0.0F, 0.0F, 1, 3, 4, s);
		this.setRotateAngle(helmWing2l, -0.2617993877991494F, 0.2617993877991494F, -0.2617993877991494F);
		this.helmWing1r = new ModelPart(this, 36, 0);
		this.helmWing1r.setPivot(-4.5F, -6.0F, -0.5F);
		this.helmWing1r.addCuboid(0.0F, -5.0F, 0.0F, 1, 5, 6, s);
		this.setRotateAngle(helmWing1r, 0.2617993877991494F, -0.5235987755982988F, -0.2617993877991494F);
		this.helmWing2r = new ModelPart(this, 50, 0);
		this.helmWing2r.mirror = true;
		this.helmWing2r.setPivot(-4.5F, -6.0F, -0.5F);
		this.helmWing2r.addCuboid(0.0F, 0.0F, 0.0F, 1, 3, 4, s);
		this.setRotateAngle(helmWing2r, -0.2617993877991494F, -0.2617993877991494F, 0.2617993877991494F);

		//body
		this.bodyAnchor = new ModelPart(this, 0, 0);
		this.bodyAnchor.setPivot(0.0F, 0.0F, 0.0F);
		this.bodyAnchor.addCuboid(-1.0F, 0.0F, -1.0F, 2, 2, 2, s);
		this.bodyTop = new ModelPart(this, 0, 19);
		this.bodyTop.setPivot(0.0F, 0.0F, 0.0F);
		this.bodyTop.addCuboid(-4.5F, 0.0F, -3.0F, 9, 6, 6, s);
		this.bodyBottom = new ModelPart(this, 0, 31);
		this.bodyBottom.setPivot(0.0F, 0.0F, 0.0F);
		this.bodyBottom.addCuboid(-3.5F, 5.0F, -2.5F, 7, 3, 5, s);

		//armL
		this.armLAnchor = new ModelPart(this, 0, 0);
		this.armLAnchor.mirror = true;
		this.armLAnchor.setPivot(4.0F, 2.0F, 0.0F);
		this.armLAnchor.addCuboid(0.0F, -1.0F, -1.0F, 2, 2, 2, s);
		this.armL = new ModelPart(this, 24, 40);
		this.armL.mirror = true;
		this.armL.setPivot(0.0F, 0.0F, -0.0F);
		this.armL.addCuboid(0.5F, 4.5F, -2.49F, 3, 6, 5, s);
		this.armLpauldron = new ModelPart(this, 0, 40);
		this.armLpauldron.mirror = true;
		this.armLpauldron.setPivot(0.0F, 0.0F, 0.0F);
		this.armLpauldron.addCuboid(-0.5F, -3.0F, -3.0F, 6, 7, 6, s);
		this.armLwing1 = new ModelPart(this, 40, 35);
		this.armLwing1.setPivot(6.0F, -1.0F, 0.0F);
		this.armLwing1.addCuboid(0.0F, -4.0F, 0.0F, 0, 4, 5, s);
		this.setRotateAngle(armLwing1, 0.2617993877991494F, 0.5235987755982988F, 0.2617993877991494F);
		this.armLwing2 = new ModelPart(this, 40, 40);
		this.armLwing2.setPivot(6.0F, -1.0F, 0.0F);
		this.armLwing2.addCuboid(0.0F, 0.0F, 0.0F, 0, 3, 4, s);
		this.setRotateAngle(armLwing2, -0.2617993877991494F, 0.2617993877991494F, -0.2617993877991494F);

		//armR
		this.armRAnchor = new ModelPart(this, 0, 0);
		this.armRAnchor.mirror = true;
		this.armRAnchor.setPivot(-4.0F, 2.0F, 0.0F);
		this.armRAnchor.addCuboid(-2.0F, -1.0F, -1.0F, 2, 2, 2, s);
		this.armR = new ModelPart(this, 24, 40);
		this.armR.setPivot(0.0F, 0.0F, 0.0F);
		this.armR.addCuboid(-3.5F, 4.5F, -2.51F, 3, 6, 5, s);
		this.armRpauldron = new ModelPart(this, 0, 40);
		this.armRpauldron.setPivot(0.0F, 0.0F, 0.0F);
		this.armRpauldron.addCuboid(-5.5F, -3.0F, -3.0F, 6, 7, 6, s);
		this.setRotateAngle(armRpauldron, 0.0F, 0.0F, 0.0017453292519943296F);
		this.armRwing1 = new ModelPart(this, 40, 35);
		this.armRwing1.mirror = true;
		this.armRwing1.setPivot(-6.5F, -1.0F, 0.0F);
		this.armRwing1.addCuboid(0.0F, -4.0F, 0.0F, 0, 4, 5, s);
		this.setRotateAngle(armRwing1, 0.2617993877991494F, -0.5235987755982988F, -0.2617993877991494F);
		this.armRwing2 = new ModelPart(this, 40, 40);
		this.armRwing2.mirror = true;
		this.armRwing2.setPivot(-6.5F, -1.0F, 0.0F);
		this.armRwing2.addCuboid(0.0F, 0.0F, 0.0F, 0, 3, 4, s);
		this.setRotateAngle(armRwing2, -0.2617993877991494F, -0.2617993877991494F, 0.2617993877991494F);

		//pants
		this.pantsAnchor = new ModelPart(this, 0, 0);
		this.pantsAnchor.setPivot(0.0F, 0.0F, 0.0F);
		this.pantsAnchor.addCuboid(-1.0F, 0.0F, -1.0F, 2, 2, 2, s);
		this.belt = new ModelPart(this, 0, 53);
		this.belt.setPivot(0.0F, 0.0F, 0.0F);
		this.belt.addCuboid(-4.5F, 8.0F, -3.0F, 9, 5, 6, s);
		this.legL = new ModelPart(this, 0, 64);
		this.legL.mirror = true;
		this.legL.setPivot(1.9F, 12.0F, 0.0F);
		this.legL.addCuboid(-2.39F, 0.0F, -2.49F, 5, 6, 5, s);
		this.legR = new ModelPart(this, 0, 64);
		this.legR.setPivot(-1.9F, 12.0F, 0.0F);
		this.legR.addCuboid(-2.61F, 0.0F, -2.51F, 5, 6, 5, s);

		//boots
		this.bootL = new ModelPart(this, 0, 75);
		this.bootL.mirror = true;
		this.bootL.setPivot(1.9F, 12.0F, 0.0F);
		this.bootL.addCuboid(-2.39F, 8.5F, -2.49F, 5, 5, 5, s);
		this.bootLwing1 = new ModelPart(this, 40, 40);
		this.bootLwing1.setPivot(2.5F, 8.5F, 0.0F);
		this.bootLwing1.addCuboid(0.0F, -3.0F, 0.0F, 0, 3, 4, s);
		this.setRotateAngle(bootLwing1, 0.2617993877991494F, 0.5235987755982988F, 0.2617993877991494F);
		this.bootLwing2 = new ModelPart(this, 40, 44);
		this.bootLwing2.setPivot(2.5F, 8.5F, 0.0F);
		this.bootLwing2.addCuboid(0.0F, 0.0F, 0.0F, 0, 2, 3, s);
		this.setRotateAngle(bootLwing2, -0.2617993877991494F, 0.2617993877991494F, -0.2617993877991494F);
		this.bootR = new ModelPart(this, 0, 75);
		this.bootR.setPivot(-1.9F, 12.0F, 0.0F);
		this.bootR.addCuboid(-2.61F, 8.5F, -2.51F, 5, 5, 5, s);
		this.bootRwing1 = new ModelPart(this, 40, 40);
		this.bootRwing1.mirror = true;
		this.bootRwing1.setPivot(-2.6F, 8.5F, 0.0F);
		this.bootRwing1.addCuboid(0.0F, -3.0F, 0.0F, 0, 3, 4, s);
		this.setRotateAngle(bootRwing1, 0.2617993877991494F, -0.5235987755982988F, -0.2617993877991494F);
		this.bootRwing2 = new ModelPart(this, 40, 44);
		this.bootRwing2.mirror = true;
		this.bootRwing2.setPivot(-2.5F, 8.5F, 0.0F);
		this.bootRwing2.addCuboid(0.0F, 0.0F, 0.0F, 0, 2, 3, s);
		this.setRotateAngle(bootRwing2, -0.2617993877991494F, -0.2617993877991494F, 0.2617993877991494F);

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
