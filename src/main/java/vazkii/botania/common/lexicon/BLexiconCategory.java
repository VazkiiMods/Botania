/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Oct 18, 2014, 3:46:10 PM (GMT)]
 */
package vazkii.botania.common.lexicon;

import net.minecraft.util.ResourceLocation;
import vazkii.botania.api.lexicon.LexiconCategory;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.lib.LibLexicon;

public class BLexiconCategory extends LexiconCategory {

	public BLexiconCategory(String unlocalizedName, int priority) {
		super(LibLexicon.CATEGORY_PREFIX + unlocalizedName);
		setIcon(new ResourceLocation(LibResources.PREFIX_CATEGORIES + unlocalizedName + ".png"));
		setPriority(priority);
	}

}

