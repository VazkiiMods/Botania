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
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;

public class ModelArmor extends BipedEntityModel<LivingEntity> {
	protected final EquipmentSlot slot;

	public ModelArmor(EquipmentSlot slot) {
		super(1);
		this.slot = slot;
	}

	// [VanillaCopy] ArmorStandArmorModel.setRotationAngles because armor stands are dumb
	// This fixes the armor "breathing" and helmets always facing south on armor stands
	@Override
	public void setAngles(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		if (!(entity instanceof ArmorStandEntity)) {
			super.setAngles(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
			return;
		}

		ArmorStandEntity entityIn = (ArmorStandEntity) entity;
		this.head.pitch = ((float) Math.PI / 180F) * entityIn.getHeadRotation().getPitch();
		this.head.yaw = ((float) Math.PI / 180F) * entityIn.getHeadRotation().getYaw();
		this.head.roll = ((float) Math.PI / 180F) * entityIn.getHeadRotation().getRoll();
		this.head.setPivot(0.0F, 1.0F, 0.0F);
		this.torso.pitch = ((float) Math.PI / 180F) * entityIn.getBodyRotation().getPitch();
		this.torso.yaw = ((float) Math.PI / 180F) * entityIn.getBodyRotation().getYaw();
		this.torso.roll = ((float) Math.PI / 180F) * entityIn.getBodyRotation().getRoll();
		this.leftArm.pitch = ((float) Math.PI / 180F) * entityIn.getLeftArmRotation().getPitch();
		this.leftArm.yaw = ((float) Math.PI / 180F) * entityIn.getLeftArmRotation().getYaw();
		this.leftArm.roll = ((float) Math.PI / 180F) * entityIn.getLeftArmRotation().getRoll();
		this.rightArm.pitch = ((float) Math.PI / 180F) * entityIn.getRightArmRotation().getPitch();
		this.rightArm.yaw = ((float) Math.PI / 180F) * entityIn.getRightArmRotation().getYaw();
		this.rightArm.roll = ((float) Math.PI / 180F) * entityIn.getRightArmRotation().getRoll();
		this.leftLeg.pitch = ((float) Math.PI / 180F) * entityIn.getLeftLegRotation().getPitch();
		this.leftLeg.yaw = ((float) Math.PI / 180F) * entityIn.getLeftLegRotation().getYaw();
		this.leftLeg.roll = ((float) Math.PI / 180F) * entityIn.getLeftLegRotation().getRoll();
		this.leftLeg.setPivot(1.9F, 11.0F, 0.0F);
		this.rightLeg.pitch = ((float) Math.PI / 180F) * entityIn.getRightLegRotation().getPitch();
		this.rightLeg.yaw = ((float) Math.PI / 180F) * entityIn.getRightLegRotation().getYaw();
		this.rightLeg.roll = ((float) Math.PI / 180F) * entityIn.getRightLegRotation().getRoll();
		this.rightLeg.setPivot(-1.9F, 11.0F, 0.0F);
		this.helmet.copyPositionAndRotation(this.head);
	}

	protected void setRotateAngle(ModelPart modelRenderer, float x, float y, float z) {
		modelRenderer.pitch = x;
		modelRenderer.yaw = y;
		modelRenderer.roll = z;
	}
}
