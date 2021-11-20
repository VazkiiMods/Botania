/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.model.armor;

import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.inventory.EquipmentSlotType;

public class ModelArmorElementium extends ModelArmor {

	private final ModelRenderer helmAnchor;
	private final ModelRenderer helm;
	private final ModelRenderer helmFairy;
	private final ModelRenderer helmWing1r;
	private final ModelRenderer helmWing2l;
	private final ModelRenderer helmWing1l;
	private final ModelRenderer helmWing2r;

	private final ModelRenderer bodyAnchor;
	private final ModelRenderer bodyTop;
	private final ModelRenderer bodyBottom;

	private final ModelRenderer armLAnchor;
	private final ModelRenderer armL;
	private final ModelRenderer armLpauldron;
	private final ModelRenderer armLwing1;
	private final ModelRenderer armLwing2;

	private final ModelRenderer armRAnchor;
	private final ModelRenderer armR;
	private final ModelRenderer armRpauldron;
	private final ModelRenderer armRwing1;
	private final ModelRenderer armRwing2;

	private final ModelRenderer pantsAnchor;
	private final ModelRenderer belt;
	private final ModelRenderer legL;
	private final ModelRenderer legR;

	private final ModelRenderer bootL;
	private final ModelRenderer bootLwing1;
	private final ModelRenderer bootLwing2;
	private final ModelRenderer bootR;
	private final ModelRenderer bootRwing1;
	private final ModelRenderer bootRwing2;

	public ModelArmorElementium(EquipmentSlotType slot) {
		super(slot);

		this.textureWidth = 64;
		this.textureHeight = 128;
		float s = 0.01F;

		//helm
		this.helmAnchor = new ModelRenderer(this, 0, 0);
		this.helmAnchor.addBox(-1.0F, -2.0F, 0.0F, 2, 2, 2, s);
		this.helm = new ModelRenderer(this, 0, 0);
		this.helm.addBox(-4.5F, -9.0F, -4.5F, 9, 9, 9, s);
		this.helmFairy = new ModelRenderer(this, 36, 11);
		this.helmFairy.addBox(-2.5F, -10.0F, -5.5F, 5, 5, 5, s);
		this.helmWing1l = new ModelRenderer(this, 36, 0);
		this.helmWing1l.mirror = true;
		this.helmWing1l.setRotationPoint(4.5F, -6.0F, -0.5F);
		this.helmWing1l.addBox(-1.0F, -5.0F, 0.0F, 1, 5, 6, s);
		this.setRotateAngle(helmWing1l, 0.2617993877991494F, 0.5235987755982988F, 0.2617993877991494F);
		this.helmWing2l = new ModelRenderer(this, 50, 0);
		this.helmWing2l.setRotationPoint(4.5F, -6.0F, -0.5F);
		this.helmWing2l.addBox(-1.0F, 0.0F, 0.0F, 1, 3, 4, s);
		this.setRotateAngle(helmWing2l, -0.2617993877991494F, 0.2617993877991494F, -0.2617993877991494F);
		this.helmWing1r = new ModelRenderer(this, 36, 0);
		this.helmWing1r.setRotationPoint(-4.5F, -6.0F, -0.5F);
		this.helmWing1r.addBox(0.0F, -5.0F, 0.0F, 1, 5, 6, s);
		this.setRotateAngle(helmWing1r, 0.2617993877991494F, -0.5235987755982988F, -0.2617993877991494F);
		this.helmWing2r = new ModelRenderer(this, 50, 0);
		this.helmWing2r.mirror = true;
		this.helmWing2r.setRotationPoint(-4.5F, -6.0F, -0.5F);
		this.helmWing2r.addBox(0.0F, 0.0F, 0.0F, 1, 3, 4, s);
		this.setRotateAngle(helmWing2r, -0.2617993877991494F, -0.2617993877991494F, 0.2617993877991494F);

		//body
		this.bodyAnchor = new ModelRenderer(this, 0, 0);
		this.bodyAnchor.addBox(-1.0F, 0.0F, -1.0F, 2, 2, 2, s);
		this.bodyTop = new ModelRenderer(this, 0, 19);
		this.bodyTop.addBox(-4.5F, 0.0F, -3.0F, 9, 6, 6, s);
		this.bodyBottom = new ModelRenderer(this, 0, 31);
		this.bodyBottom.addBox(-3.5F, 5.0F, -2.5F, 7, 3, 5, s);

		//armL
		this.armLAnchor = new ModelRenderer(this, 0, 0);
		this.armLAnchor.mirror = true;
		this.armLAnchor.addBox(0.0F, -1.0F, -1.0F, 2, 2, 2, s);
		this.armL = new ModelRenderer(this, 24, 40);
		this.armL.mirror = true;
		this.armL.addBox(0.5F, 4.5F, -2.49F, 3, 6, 5, s);
		this.armLpauldron = new ModelRenderer(this, 0, 40);
		this.armLpauldron.mirror = true;
		this.armLpauldron.addBox(-0.5F, -3.0F, -3.0F, 6, 7, 6, s);
		this.armLwing1 = new ModelRenderer(this, 40, 35);
		this.armLwing1.setRotationPoint(6.0F, -1.0F, 0.0F);
		this.armLwing1.addBox(0.0F, -4.0F, 0.0F, 0, 4, 5, s);
		this.setRotateAngle(armLwing1, 0.2617993877991494F, 0.5235987755982988F, 0.2617993877991494F);
		this.armLwing2 = new ModelRenderer(this, 40, 40);
		this.armLwing2.setRotationPoint(6.0F, -1.0F, 0.0F);
		this.armLwing2.addBox(0.0F, 0.0F, 0.0F, 0, 3, 4, s);
		this.setRotateAngle(armLwing2, -0.2617993877991494F, 0.2617993877991494F, -0.2617993877991494F);

		//armR
		this.armRAnchor = new ModelRenderer(this, 0, 0);
		this.armRAnchor.mirror = true;
		this.armRAnchor.addBox(-2.0F, -1.0F, -1.0F, 2, 2, 2, s);
		this.armR = new ModelRenderer(this, 24, 40);
		this.armR.addBox(-3.5F, 4.5F, -2.51F, 3, 6, 5, s);
		this.armRpauldron = new ModelRenderer(this, 0, 40);
		this.armRpauldron.addBox(-5.5F, -3.0F, -3.0F, 6, 7, 6, s);
		this.setRotateAngle(armRpauldron, 0.0F, 0.0F, 0.0017453292519943296F);
		this.armRwing1 = new ModelRenderer(this, 40, 35);
		this.armRwing1.mirror = true;
		this.armRwing1.setRotationPoint(-6.5F, -1.0F, 0.0F);
		this.armRwing1.addBox(0.0F, -4.0F, 0.0F, 0, 4, 5, s);
		this.setRotateAngle(armRwing1, 0.2617993877991494F, -0.5235987755982988F, -0.2617993877991494F);
		this.armRwing2 = new ModelRenderer(this, 40, 40);
		this.armRwing2.mirror = true;
		this.armRwing2.setRotationPoint(-6.5F, -1.0F, 0.0F);
		this.armRwing2.addBox(0.0F, 0.0F, 0.0F, 0, 3, 4, s);
		this.setRotateAngle(armRwing2, -0.2617993877991494F, -0.2617993877991494F, 0.2617993877991494F);

		//pants
		this.pantsAnchor = new ModelRenderer(this, 0, 0);
		this.pantsAnchor.addBox(-1.0F, 0.0F, -1.0F, 2, 2, 2, s);
		this.belt = new ModelRenderer(this, 0, 53);
		this.belt.addBox(-4.5F, 8.0F, -3.0F, 9, 5, 6, s);
		this.legL = new ModelRenderer(this, 0, 64);
		this.legL.mirror = true;
		this.legL.addBox(-2.39F, 0.0F, -2.49F, 5, 6, 5, s);
		this.legR = new ModelRenderer(this, 0, 64);
		this.legR.addBox(-2.61F, 0.0F, -2.51F, 5, 6, 5, s);

		//boots
		this.bootL = new ModelRenderer(this, 0, 75);
		this.bootL.mirror = true;
		this.bootL.addBox(-2.39F, 8.5F, -2.49F, 5, 5, 5, s);
		this.bootLwing1 = new ModelRenderer(this, 40, 40);
		this.bootLwing1.setRotationPoint(2.5F, 8.5F, 0.0F);
		this.bootLwing1.addBox(0.0F, -3.0F, 0.0F, 0, 3, 4, s);
		this.setRotateAngle(bootLwing1, 0.2617993877991494F, 0.5235987755982988F, 0.2617993877991494F);
		this.bootLwing2 = new ModelRenderer(this, 40, 44);
		this.bootLwing2.setRotationPoint(2.5F, 8.5F, 0.0F);
		this.bootLwing2.addBox(0.0F, 0.0F, 0.0F, 0, 2, 3, s);
		this.setRotateAngle(bootLwing2, -0.2617993877991494F, 0.2617993877991494F, -0.2617993877991494F);
		this.bootR = new ModelRenderer(this, 0, 75);
		this.bootR.addBox(-2.61F, 8.5F, -2.51F, 5, 5, 5, s);
		this.bootRwing1 = new ModelRenderer(this, 40, 40);
		this.bootRwing1.mirror = true;
		this.bootRwing1.setRotationPoint(-2.6F, 8.5F, 0.0F);
		this.bootRwing1.addBox(0.0F, -3.0F, 0.0F, 0, 3, 4, s);
		this.setRotateAngle(bootRwing1, 0.2617993877991494F, -0.5235987755982988F, -0.2617993877991494F);
		this.bootRwing2 = new ModelRenderer(this, 40, 44);
		this.bootRwing2.mirror = true;
		this.bootRwing2.setRotationPoint(-2.5F, 8.5F, 0.0F);
		this.bootRwing2.addBox(0.0F, 0.0F, 0.0F, 0, 2, 3, s);
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

		helmAnchor.showModel = slot == EquipmentSlotType.HEAD;
		bodyAnchor.showModel = slot == EquipmentSlotType.CHEST;
		armRAnchor.showModel = slot == EquipmentSlotType.CHEST;
		armLAnchor.showModel = slot == EquipmentSlotType.CHEST;
		legR.showModel = slot == EquipmentSlotType.LEGS;
		legL.showModel = slot == EquipmentSlotType.LEGS;
		bootL.showModel = slot == EquipmentSlotType.FEET;
		bootR.showModel = slot == EquipmentSlotType.FEET;
		bipedHeadwear.showModel = false;

		bipedHead.addChild(helmAnchor);
		bipedBody.addChild(bodyAnchor);
		bipedRightArm.addChild(armRAnchor);
		bipedLeftArm.addChild(armLAnchor);
		bipedBody.addChild(pantsAnchor);
		bipedRightLeg.addChild(legR);
		bipedLeftLeg.addChild(legL);
		bipedRightLeg.addChild(bootR);
		bipedLeftLeg.addChild(bootL);
	}
}
