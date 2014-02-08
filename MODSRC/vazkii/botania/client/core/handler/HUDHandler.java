/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 25, 2014, 6:11:10 PM (GMT)]
 */
package vazkii.botania.client.core.handler;

import java.awt.Color;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.ForgeSubscribe;

import org.lwjgl.opengl.GL11;

import vazkii.botania.api.IWandHUD;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.item.ModItems;

public final class HUDHandler {

	private static final ResourceLocation manaBar = new ResourceLocation(LibResources.GUI_MANA_HUD);

	@ForgeSubscribe
	public void onDrawScreen(RenderGameOverlayEvent.Post event) {
		if(event.type == ElementType.ALL) {
			Minecraft mc = Minecraft.getMinecraft();
			MovingObjectPosition pos = mc.objectMouseOver;
			if(pos != null && mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.getCurrentEquippedItem().itemID == ModItems.twigWand.itemID) {
				int id = mc.theWorld.getBlockId(pos.blockX, pos.blockY, pos.blockZ);
				if(Block.blocksList[id] != null && Block.blocksList[id] instanceof IWandHUD)
					((IWandHUD) Block.blocksList[id]).renderHUD(mc, event.resolution, mc.theWorld, pos.blockX, pos.blockY, pos.blockZ);
			}
		}
	}

	public static void drawSimpleManaHUD(int color, int mana, int maxMana, String name, ScaledResolution res) {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		Minecraft mc = Minecraft.getMinecraft();
		int x = res.getScaledWidth() / 2 - mc.fontRenderer.getStringWidth(name) / 2;
		int y = res.getScaledHeight() / 2 + 10;

		mc.fontRenderer.drawStringWithShadow(name, x, y, color);

		x = res.getScaledWidth() / 2 - 51;
		y += 10;

		renderManaBar(x, y, color, 0.5F, mana, maxMana);

		if(mana < 0) {
			String text = StatCollector.translateToLocal("botaniamisc.statusUnknown");
			x = res.getScaledWidth() / 2 - mc.fontRenderer.getStringWidth(text) / 2;
			y -= 1;
			mc.fontRenderer.drawStringWithShadow(text, x, y, color);
		}

		GL11.glDisable(GL11.GL_BLEND);
	}

	public static void renderManaBar(int x, int y, int color, float alpha, int mana, int maxMana) {
		Minecraft mc = Minecraft.getMinecraft();

		GL11.glColor4f(1F, 1F, 1F, alpha);
		mc.renderEngine.bindTexture(manaBar);
		RenderHelper.drawTexturedModalRect(x, y, 0, 0, 0, 102, 5);

		int manaPercentage = Math.max(0, (int) ((double) mana / (double) maxMana * 100));
		RenderHelper.drawTexturedModalRect(x + 1 + manaPercentage, y + 1, 0, 0, 5, 100 - manaPercentage, 3);

		Color color_ = new Color(color);
		GL11.glColor4ub((byte) color_.getRed(), (byte) color_.getGreen(),(byte) color_.getBlue(), (byte) (255F * alpha));
		RenderHelper.drawTexturedModalRect(x + 1, y + 1, 0, 0, 5, manaPercentage, 3);
	}
}
