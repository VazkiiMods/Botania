/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Apr 26, 2014, 7:08:53 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.bauble;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.integration.curios.RenderableCurio;

public class ItemKnockbackBelt extends ItemBauble {

	private static final ResourceLocation texture = new ResourceLocation(LibResources.MODEL_KNOCKBACK_BELT);
	private static BipedModel model;

	public ItemKnockbackBelt(Properties props) {
		super(props);
	}

	public static class Curio extends RenderableCurio {
		public Curio(ItemStack stack) {
			super(stack);
		}

		@Override
		public Multimap<String, AttributeModifier> getAttributeModifiers(String identifier) {
		    Multimap<String, AttributeModifier> attributes = HashMultimap.create();
			attributes.put(SharedMonsterAttributes.KNOCKBACK_RESISTANCE.getName(), new AttributeModifier(getBaubleUUID(stack),  "Knockback Belt", 1, 0).setSaved(false));
			return attributes;
		}

		@Override
        @OnlyIn(Dist.CLIENT)
		public void doRender(String identifier, LivingEntity living, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
			Minecraft.getInstance().textureManager.bindTexture(texture);

			GlStateManager.translatef(0F, 0.2F, 0F);

			float s = 1.05F / 16F;
			GlStateManager.scalef(s, s, s);

			if(model == null)
				model = new BipedModel();

			model.bipedBody.render(1F);
		}
	}
}
