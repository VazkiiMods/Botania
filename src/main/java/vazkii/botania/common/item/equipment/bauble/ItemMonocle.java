/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 24, 2015, 3:03:18 PM (GMT)]
 */
package vazkii.botania.common.item.equipment.bauble;

import org.lwjgl.opengl.GL11;

import vazkii.botania.api.item.IBaubleRender;
import vazkii.botania.api.item.IBurstViewerBauble;
import vazkii.botania.api.item.IBaubleRender.Helper;
import vazkii.botania.api.item.IBaubleRender.RenderType;
import vazkii.botania.common.lib.LibItemNames;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderPlayerEvent;
import baubles.api.BaubleType;

public class ItemMonocle extends ItemBauble implements IBurstViewerBauble, IBaubleRender {

	public ItemMonocle() {
		super(LibItemNames.MONOCLE);
	}
	
	@Override
	public BaubleType getBaubleType(ItemStack arg0) {
		return BaubleType.AMULET;
	}

	@Override
	public void onPlayerBaubleRender(ItemStack stack, RenderPlayerEvent event, RenderType type) {
		if(type == RenderType.HEAD) {
			float f = itemIcon.getMinU();
			float f1 = itemIcon.getMaxU();
			float f2 = itemIcon.getMinV();
			float f3 = itemIcon.getMaxV();
			boolean armor = event.entityPlayer.getCurrentArmor(3) != null;
			Helper.translateToHeadLevel(event.entityPlayer);
			Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture);
			GL11.glRotatef(90F, 0F, 1F, 0F);
			GL11.glRotatef(180F, 1F, 0F, 0F);
			GL11.glTranslatef(-0.35F, -0.1F, armor ? -0.3F : -0.25F);
			GL11.glScalef(0.35F, 0.35F, 0.35F);
			ItemRenderer.renderItemIn2D(Tessellator.instance, f1, f2, f, f3, itemIcon.getIconWidth(), itemIcon.getIconHeight(), 1F / 16F);
		}
	}

}
