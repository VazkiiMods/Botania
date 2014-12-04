/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Dec 4, 2014, 11:03:13 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.bauble;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderPlayerEvent;
import baubles.api.BaubleType;
import vazkii.botania.api.item.IBaubleRender;
import vazkii.botania.api.item.IBaubleRender.Helper;
import vazkii.botania.api.item.IBaubleRender.RenderType;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.lib.LibItemNames;

public class ItemHolyCloak extends ItemBauble implements IBaubleRender {

	private static final ResourceLocation texture = new ResourceLocation(LibResources.MODEL_HOLY_CLOAK);
	@SideOnly(Side.CLIENT)
	private static ModelBiped model;
	
	public ItemHolyCloak() {
		this(LibItemNames.HOLY_CLOAK);
	}
	
	public ItemHolyCloak(String name) {
		super(name);
	}

	@Override
	public BaubleType getBaubleType(ItemStack arg0) {
		return BaubleType.AMULET;
	}

	@SideOnly(Side.CLIENT)
	ResourceLocation getRenderTexture() {
		return texture;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void onPlayerBaubleRender(ItemStack stack, RenderPlayerEvent event, RenderType type) {
		if(type == RenderType.BODY) {
			Minecraft.getMinecraft().renderEngine.bindTexture(getRenderTexture());
			Helper.rotateIfSneaking(event.entityPlayer);
			boolean armor = event.entityPlayer.getCurrentArmor(2) != null;
			GL11.glTranslatef(0F, armor ? -0.07F : -0.01F, 0F);

			float s = 0.1F;
			GL11.glScalef(s, s, s);
			if(model == null)
				 model = new ModelBiped();
			
			model.bipedBody.render(1F);
		}
	}

	
}
