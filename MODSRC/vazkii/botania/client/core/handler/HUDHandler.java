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

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.ForgeSubscribe;

import org.lwjgl.opengl.GL11;

import vazkii.botania.common.item.ModItems;

public final class HUDHandler {

	@ForgeSubscribe
	public void onDrawScreen(RenderGameOverlayEvent.Post event) {
		if(event.type == ElementType.ALL) {
			Minecraft mc = Minecraft.getMinecraft();
			MovingObjectPosition pos = mc.objectMouseOver;
			if(pos != null && mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.getCurrentEquippedItem().itemID == ModItems.twigWand.itemID) {
				int id = mc.theWorld.getBlockId(pos.blockX, pos.blockY, pos.blockZ);
				if(Block.blocksList[id] != null && Block.blocksList[id] instanceof IHUD)
					((IHUD) Block.blocksList[id]).renderHUD(mc, event.resolution, mc.theWorld, pos.blockX, pos.blockY, pos.blockZ);
			}
		}
	}

	public static void drawSimpleManaHUD(int color, int mana, int maxMana, String name, ScaledResolution res) {
		int type = 0;
		if(mana >= 0) {
			type = 1;
			double percentage = (double) mana / (double) maxMana * 100;
			if(percentage == 100)
				type = 5;
			else if(percentage >= 75)
				type = 4;
			else if(percentage >= 50)
				type = 3;
			else if(percentage > 0)
				type = 2;
		}
		String filling = StatCollector.translateToLocal("botaniamisc.status" + type);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		Minecraft mc = Minecraft.getMinecraft();
		mc.fontRenderer.drawStringWithShadow(name, res.getScaledWidth() / 2 - mc.fontRenderer.getStringWidth(name) / 2, res.getScaledHeight() / 2 + 10, color);
		mc.fontRenderer.drawStringWithShadow(filling, res.getScaledWidth() / 2 - mc.fontRenderer.getStringWidth(filling) / 2, res.getScaledHeight() / 2 + 20, color);
		GL11.glDisable(GL11.GL_BLEND);
	}

	public interface IHUD {
		public void renderHUD(Minecraft mc, ScaledResolution res, World world, int x, int y, int z);
	}
}
