/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 14, 2014, 6:44:59 PM (GMT)]
 */
package vazkii.botania.common.core.handler;

import vazkii.botania.api.internal.IInternalMethodHandler;
import vazkii.botania.api.page.LexiconPage;
import vazkii.botania.common.page.PageText;

public class InternalMethodHandler implements IInternalMethodHandler {

	@Override
	public LexiconPage textPage(String key) {
		return new PageText(key);
	}

}
