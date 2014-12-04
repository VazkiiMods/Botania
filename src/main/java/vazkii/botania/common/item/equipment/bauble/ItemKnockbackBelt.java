/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Apr 26, 2014, 7:08:53 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.bauble;

import java.util.UUID;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderPlayerEvent;
import vazkii.botania.api.item.IBaubleRender;
import vazkii.botania.api.item.IBaubleRender.Helper;
import vazkii.botania.api.item.IBaubleRender.RenderType;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.lib.LibItemNames;
import baubles.api.BaubleType;

import com.google.common.collect.Multimap;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemKnockbackBelt extends ItemBaubleModifier implements IBaubleRender {

	private static final ResourceLocation texture = new ResourceLocation(LibResources.MODEL_KNOCKBACK_BELT);
	private static final ModelBiped model = new ModelBiped();
	
	public ItemKnockbackBelt() {
		super(LibItemNames.KNOCKBACK_BELT);
	}

	@Override
	public BaubleType getBaubleType(ItemStack itemstack) {
		return BaubleType.BELT;
	}

	@Override
	void fillModifiers(Multimap<String, AttributeModifier> attributes) {
		attributes.put(SharedMonsterAttributes.knockbackResistance.getAttributeUnlocalizedName(), new AttributeModifier(new UUID(2745708 /** Random number **/, 43743), "Bauble modifier", 1, 0));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void onPlayerBaubleRender(ItemStack stack, RenderPlayerEvent event, RenderType type) {
		if(type == RenderType.BODY) {
			Minecraft.getMinecraft().renderEngine.bindTexture(texture);
			Helper.rotateIfSneaking(event.entityPlayer);
			boolean armor = event.entityPlayer.getCurrentArmor(1) != null;
			GL11.glTranslatef(0F, 0.1F, 0F);

			float s = (armor ? 1.3F : 1.05F) / 16F;
			GL11.glScalef(s, s, s);
			model.bipedBody.render(1F);
		}
	}

}
