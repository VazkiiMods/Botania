/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 14, 2014, 6:45:33 PM (GMT)]
 */
package vazkii.botania.common.lexicon.page;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import vazkii.botania.api.internal.IGuiLexiconEntry;
import vazkii.botania.api.page.LexiconPage;
import vazkii.botania.client.core.helper.FontHelper;

public class PageText extends LexiconPage {

	public PageText(String unlocalizedName) {
		super(unlocalizedName);
	}

	@Override
	public void renderScreen(IGuiLexiconEntry gui, int mx, int my) {
		FontRenderer renderer = Minecraft.getMinecraft().fontRenderer;
		boolean unicode = renderer.getUnicodeFlag();
		renderer.setUnicodeFlag(true);
		String text = StatCollector.translateToLocal(getUnlocalizedName()).replaceAll("&", "\u00a7");
		String[] textEntries = text.split("<br>");
		
		int width = gui.getWidth() - 30;
		int x = gui.getLeft() + 16;
		int y = gui.getTop();
		
		String lastFormat = "";
		for(String s : textEntries) {
			List<String> wrappedLines = new ArrayList();
			String workingOn = "";
			
			int i = 0;
			String[] tokens = s.split(" ");
			for(String s1 : tokens) {
				String format = FontHelper.getFormatFromString(s1);
				if(MathHelper.stringNullOrLengthZero(format))
					format = lastFormat;
				
				if(renderer.getStringWidth(workingOn + " " + s1) >= width) {
					wrappedLines.add(workingOn);
					workingOn = "";
				}
				workingOn = workingOn + format + " " + s1;
				
				if(i == tokens.length - 1)
					wrappedLines.add(workingOn);
					
				++i;
				lastFormat = format;
			}
			
			for(String s1 : wrappedLines) {
				y += 10;
				renderer.drawString(s1, x, y, 0);
			}
			
			y += 10;
		}
		
		renderer.setUnicodeFlag(unicode);
	}

}
