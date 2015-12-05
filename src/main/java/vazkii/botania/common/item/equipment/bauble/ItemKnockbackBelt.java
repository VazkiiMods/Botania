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

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderPlayerEvent;

import org.lwjgl.opengl.GL11;

import vazkii.botania.api.item.IBaubleRender;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.lib.LibItemNames;
import baubles.api.BaubleType;

import com.google.common.collect.Multimap;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemKnockbackBelt extends ItemBaubleModifier implements IBaubleRender {

	private static final ResourceLocation texture = new ResourceLocation(LibResources.MODEL_KNOCKBACK_BELT);
	private static ModelBiped model;

	public ItemKnockbackBelt() {
		super(LibItemNames.KNOCKBACK_BELT);
	}

	@Override
	public BaubleType getBaubleType(ItemStack itemstack) {
		return BaubleType.BELT;
	}

	@Override
	void fillModifiers(Multimap<String, AttributeModifier> attributes, ItemStack stack) {
		attributes.put(SharedMonsterAttributes.knockbackResistance.getAttributeUnlocalizedName(), new AttributeModifier(getBaubleUUID(stack), "Bauble modifier", 1, 0));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onPlayerBaubleRender(ItemStack stack, RenderPlayerEvent event, RenderType type) {
		if(type == RenderType.BODY) {
			Minecraft.getMinecraft().renderEngine.bindTexture(texture);
			Helper.rotateIfSneaking(event.entityPlayer);
			GL11.glTranslatef(0F, 0.2F, 0F);

			float s = 1.05F / 16F;
			GL11.glScalef(s, s, s);

			if(model == null)
				model = new ModelBiped();

			model.bipedBody.render(1F);
		}
	}

}
