/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 14, 2014, 6:04:31 PM (GMT)]
 */
package vazkii.botania.client.core.handler;

import vazkii.botania.client.lib.LibResources;
import cpw.mods.fml.common.registry.LanguageRegistry;

public final class LocalizationHandler {

	public static void loadLocalizations() {
		for(String locale : LibResources.LANGS)
			LanguageRegistry.instance().loadLocalization(LibResources.PREFIX_LANG + locale + ".lang", locale, false);
	}

}
