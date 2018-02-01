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

import baubles.api.BaubleType;
import com.google.common.collect.Multimap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.item.IBaubleRender;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.lib.LibItemNames;

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
		if(stack.isEmpty()) // workaround for Azanor/Baubles#156
			return;
		
		attributes.put(SharedMonsterAttributes.KNOCKBACK_RESISTANCE.getName(), new AttributeModifier(getBaubleUUID(stack), "Knockback Belt", 1, 0).setSaved(false));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onPlayerBaubleRender(ItemStack stack, EntityPlayer player, RenderType type, float partialTicks) {
		if(type == RenderType.BODY) {
			Minecraft.getMinecraft().renderEngine.bindTexture(texture);
			Helper.rotateIfSneaking(player);

			GlStateManager.translate(0F, 0.2F, 0F);

			float s = 1.05F / 16F;
			GlStateManager.scale(s, s, s);

			if(model == null)
				model = new ModelBiped();

			model.bipedBody.render(1F);
		}
	}

}
