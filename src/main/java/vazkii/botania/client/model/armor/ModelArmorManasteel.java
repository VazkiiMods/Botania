/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.model.armor;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.inventory.EquipmentSlotType;

public class ModelArmorManasteel extends ModelArmor {

	private final ModelRenderer helmAnchor;
	private final ModelRenderer helm;
	private final ModelRenderer helmTop;
	private final ModelRenderer helmCrystal;

	private final ModelRenderer bodyAnchor;
	private final ModelRenderer bodyTop;
	private final ModelRenderer bodyBottom;

	private final ModelRenderer armLAnchor;
	private final ModelRenderer armL;
	private final ModelRenderer armLpauldron;
	private final ModelRenderer armLcrystal;

	private final ModelRenderer armRAnchor;
	private final ModelRenderer armR;
	private final ModelRenderer armRpauldron;
	private final ModelRenderer armRcrystal;
	private final ModelRenderer pantsAnchor;
	private final ModelRenderer belt;
	private final ModelRenderer legL;
	private final ModelRenderer legR;

	private final ModelRenderer bootL;
	private final ModelRenderer bootLcrystal;
	private final ModelRenderer bootR;
	private final ModelRenderer bootRcrystal;

	public ModelArmorManasteel(EquipmentSlotType slot) {
		super(slot);

		this.textureWidth = 64;
		this.textureHeight = 128;
		float s = 0.01F;

		//helm
		this.helmAnchor = new ModelRenderer(this, 0, 0);
		this.helmAnchor.addBox(-1.0F, -2.0F, 0.0F, 2, 2, 2, s);
		this.helm = new ModelRenderer(this, 0, 0);
		this.helm.addBox(-4.5F, -8.5F, -4.5F, 9, 9, 9, s);
		this.helmTop = new ModelRenderer(this, 36, 6);
		this.helmTop.setRotationPoint(0.0F, -8.5F, 3.5F);
		this.helmTop.addBox(-1.5F, 0.0F, -7.0F, 3, 3, 7, s);
		this.setRotateAngle(helmTop, -0.2617993877991494F, 0.0F, 0.0F);
		this.helmCrystal = new ModelRenderer(this, 36, 0);
		this.helmCrystal.setRotationPoint(0.0F, -7.5F, -4.5F);
		this.helmCrystal.addBox(-1.5F, -4.0F, -1.0F, 3, 5, 1, s);
		this.setRotateAngle(helmCrystal, 0.08726646259971647F, 0.0F, 0.0F);

		//body
		this.bodyAnchor = new ModelRenderer(this, 0, 0);
		this.bodyAnchor.addBox(-1.0F, 0.0F, -1.0F, 2, 2, 2, s);
		this.bodyTop = new ModelRenderer(this, 0, 18);
		this.bodyTop.addBox(-4.5F, -0.5F, -3.0F, 9, 6, 6, s);
		this.setRotateAngle(bodyTop, 0.0F, 0.0F, 0.0F);
		this.bodyBottom = new ModelRenderer(this, 0, 30);
		this.bodyBottom.addBox(-2.5F, 5.5F, -2.5F, 5, 3, 5, 0.0F);

		//armL
		this.armLAnchor = new ModelRenderer(this, 0, 0);
		this.armLAnchor.mirror = true;
		this.armLAnchor.addBox(0.0F, -1.0F, -1.0F, 2, 2, 2, s);
		this.armL = new ModelRenderer(this, 22, 40);
		this.armL.mirror = true;
		this.armL.addBox(1.5F, 2.0F, -2.49F, 2, 6, 5, s);
		this.armLpauldron = new ModelRenderer(this, 0, 40);
		this.armLpauldron.mirror = true;
		this.armLpauldron.addBox(-0.5F, -3.0F, -3.0F, 5, 4, 6, s);
		this.armLcrystal = new ModelRenderer(this, 36, 40);
		this.armLcrystal.mirror = true;
		this.armLcrystal.setRotationPoint(2.0F, -2.5F, 0.0F);
		this.armLcrystal.addBox(-0.5F, -2.5F, -1.5F, 2, 3, 3, s);
		this.setRotateAngle(armLcrystal, 0.0F, 0.0F, -0.08726646259971647F);

		//armR
		this.armRAnchor = new ModelRenderer(this, 0, 0);
		this.armRAnchor.mirror = true;
		this.armRAnchor.addBox(-2.0F, -1.0F, -1.0F, 2, 2, 2, s);
		this.armR = new ModelRenderer(this, 22, 40);
		this.armR.addBox(-3.5F, 2.0F, -2.51F, 2, 6, 5, s);
		this.armRpauldron = new ModelRenderer(this, 0, 40);
		this.armRpauldron.addBox(-4.5F, -3.0F, -3.0F, 5, 4, 6, s);
		this.armRcrystal = new ModelRenderer(this, 36, 40);
		this.armRcrystal.setRotationPoint(-2.0F, -2.5F, 0.0F);
		this.armRcrystal.addBox(-1.5F, -2.5F, -1.5F, 2, 3, 3, s);
		this.setRotateAngle(armRcrystal, 0.0F, 0.0F, 0.08726646259971647F);

		//pants
		this.pantsAnchor = new ModelRenderer(this, 0, 0);
		this.pantsAnchor.addBox(-1.0F, 0.0F, -1.0F, 2, 2, 2, s);
		this.belt = new ModelRenderer(this, 0, 51);
		this.belt.addBox(-4.5F, 9.0F, -3.0F, 9, 3, 6, s);
		this.legL = new ModelRenderer(this, 0, 60);
		this.legL.mirror = true;
		this.legL.addBox(-0.39F, 0.0F, -2.49F, 3, 6, 5, s);
		this.legR = new ModelRenderer(this, 0, 60);
		this.legR.addBox(-2.61F, 0.0F, -2.51F, 3, 6, 5, s);

		//boots
		this.bootL = new ModelRenderer(this, 0, 71);
		this.bootL.mirror = true;
		this.bootL.addBox(-2.39F, 8.5F, -2.49F, 5, 4, 5, s);
		this.bootLcrystal = new ModelRenderer(this, 36, 46);
		this.bootLcrystal.mirror = true;
		this.bootLcrystal.setRotationPoint(2.5F, 9.0F, 2.0F);
		this.bootLcrystal.addBox(-1.0F, -2.0F, -1.5F, 2, 3, 3, s);
		this.setRotateAngle(bootLcrystal, 0.0F, 0.0F, 0.08726646259971647F);
		this.bootR = new ModelRenderer(this, 0, 71);
		this.bootR.addBox(-2.5F, 8.5F, -2.51F, 5, 4, 5, s);
		this.bootRcrystal = new ModelRenderer(this, 36, 46);
		this.bootRcrystal.setRotationPoint(-2.5F, 9.0F, 2.0F);
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
