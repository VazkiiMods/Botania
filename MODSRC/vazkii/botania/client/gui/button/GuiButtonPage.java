/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 16, 2014, 4:52:06 PM (GMT)]
 */
package vazkii.botania.client.gui.button;

import org.lwjgl.opengl.GL11;

import vazkii.botania.client.gui.GuiLexicon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class GuiButtonPage extends GuiButton {
	
	boolean right;

	public GuiButtonPage(int par1, int par2, int par3, boolean right) {
		super(par1, par2, par3, 18, 10, "");
		this.right = right;
	}
	
	@Override
	public void drawButton(Minecraft par1Minecraft, int par2, int par3) {
		if(enabled) {
			field_82253_i = par2 >= xPosition && par3 >= yPosition && par2 < xPosition + width && par3 < yPosition + height;
	        int k = getHoverState(field_82253_i);
	        
			par1Minecraft.renderEngine.bindTexture(GuiLexicon.texture);
			GL11.glColor4f(1F, 1F, 1F, 1F);
			drawTexturedModalRect(xPosition, yPosition, k == 2 ? 18 : 0, right ? 180 : 190, 18, 10);
		}
	}

}
