package vazkii.botania.client.model.armor;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.inventory.EquipmentSlotType;

public class ModelArmor extends BipedModel {
	protected final EquipmentSlotType slot;

	public ModelArmor(EquipmentSlotType slot) {
		this.slot = slot;
	}

	// [VanillaCopy] ArmorStandArmorModel.setRotationAngles because armor stands are dumb
	// This fixes the armor "breathing" and helmets always facing south on armor stands
	// todo 1.15 recheck param names
	@Override
	public void setAngles(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		if (!(entity instanceof ArmorStandEntity)) {
			super.setAngles(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
			return;
		}

		ArmorStandEntity entityIn = (ArmorStandEntity) entity;
		this.bipedHead.rotateAngleX = ((float)Math.PI / 180F) * entityIn.getHeadRotation().getX();
		this.bipedHead.rotateAngleY = ((float)Math.PI / 180F) * entityIn.getHeadRotation().getY();
		this.bipedHead.rotateAngleZ = ((float)Math.PI / 180F) * entityIn.getHeadRotation().getZ();
		this.bipedHead.setRotationPoint(0.0F, 1.0F, 0.0F);
		this.bipedBody.rotateAngleX = ((float)Math.PI / 180F) * entityIn.getBodyRotation().getX();
		this.bipedBody.rotateAngleY = ((float)Math.PI / 180F) * entityIn.getBodyRotation().getY();
		this.bipedBody.rotateAngleZ = ((float)Math.PI / 180F) * entityIn.getBodyRotation().getZ();
		this.bipedLeftArm.rotateAngleX = ((float)Math.PI / 180F) * entityIn.getLeftArmRotation().getX();
		this.bipedLeftArm.rotateAngleY = ((float)Math.PI / 180F) * entityIn.getLeftArmRotation().getY();
		this.bipedLeftArm.rotateAngleZ = ((float)Math.PI / 180F) * entityIn.getLeftArmRotation().getZ();
		this.bipedRightArm.rotateAngleX = ((float)Math.PI / 180F) * entityIn.getRightArmRotation().getX();
		this.bipedRightArm.rotateAngleY = ((float)Math.PI / 180F) * entityIn.getRightArmRotation().getY();
		this.bipedRightArm.rotateAngleZ = ((float)Math.PI / 180F) * entityIn.getRightArmRotation().getZ();
		this.bipedLeftLeg.rotateAngleX = ((float)Math.PI / 180F) * entityIn.getLeftLegRotation().getX();
		this.bipedLeftLeg.rotateAngleY = ((float)Math.PI / 180F) * entityIn.getLeftLegRotation().getY();
		this.bipedLeftLeg.rotateAngleZ = ((float)Math.PI / 180F) * entityIn.getLeftLegRotation().getZ();
		this.bipedLeftLeg.setRotationPoint(1.9F, 11.0F, 0.0F);
		this.bipedRightLeg.rotateAngleX = ((float)Math.PI / 180F) * entityIn.getRightLegRotation().getX();
		this.bipedRightLeg.rotateAngleY = ((float)Math.PI / 180F) * entityIn.getRightLegRotation().getY();
		this.bipedRightLeg.rotateAngleZ = ((float)Math.PI / 180F) * entityIn.getRightLegRotation().getZ();
		this.bipedRightLeg.setRotationPoint(-1.9F, 11.0F, 0.0F);
		this.bipedHeadwear.copyModelAngles(this.bipedHead);
	}

	protected void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}
