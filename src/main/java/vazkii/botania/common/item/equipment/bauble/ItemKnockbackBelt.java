/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.equipment.bauble;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import vazkii.botania.client.core.helper.AccessoryRenderHelper;
import vazkii.botania.client.lib.LibResources;

public class ItemKnockbackBelt extends ItemBauble {

	private static final Identifier texture = new Identifier(LibResources.MODEL_KNOCKBACK_BELT);
	private static BipedEntityModel<LivingEntity> model;

	public ItemKnockbackBelt(Settings props) {
		super(props);
	}

	@Override
	public Multimap<EntityAttribute, EntityAttributeModifier> getEquippedAttributeModifiers(ItemStack stack) {
		Multimap<EntityAttribute, EntityAttributeModifier> attributes = HashMultimap.create();
		attributes.put(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, new EntityAttributeModifier(getBaubleUUID(stack), "Knockback Belt", 1, EntityAttributeModifier.Operation.ADDITION));
		return attributes;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void doRender(BipedEntityModel<?> bipedModel, ItemStack stack, LivingEntity living, MatrixStack ms, VertexConsumerProvider buffers, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		AccessoryRenderHelper.rotateIfSneaking(ms, living);
		ms.translate(0F, 0.2F, 0F);

		float s = 0.85F;
		ms.scale(s, s, s);
		if (model == null) {
			model = new BipedEntityModel<>(1.0F);
		}

		VertexConsumer buffer = buffers.getBuffer(model.getLayer(texture));
		model.torso.render(ms, buffer, light, OverlayTexture.DEFAULT_UV);
	}
}
