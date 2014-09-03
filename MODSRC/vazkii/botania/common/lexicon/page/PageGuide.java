/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Sep 3, 2014, 9:41:47 PM (GMT)]
 */
package vazkii.botania.common.lexicon.page;

import java.awt.Desktop;
import java.net.URI;

public class PageGuide extends PageText {

	public PageGuide(String unlocalizedName) {
		super(unlocalizedName);
	}

	@Override
	public void onKeyPressed(char c, int key) {
		if(key == 28 && Desktop.isDesktopSupported())
			try {
				Desktop.getDesktop().browse(new URI("https://www.youtube.com/watch?v=rx0xyejC6fI"));
				if(Math.random() > 0.01)
					Desktop.getDesktop().browse(new URI("https://www.youtube.com/watch?v=dQw4w9WgXcQ"));
			} catch(Exception e) { }
	}

}
