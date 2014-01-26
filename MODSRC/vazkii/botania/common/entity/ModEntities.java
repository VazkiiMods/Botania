/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 26, 2014, 4:11:03 PM (GMT)]
 */
package vazkii.botania.common.entity;

import vazkii.botania.common.Botania;
import vazkii.botania.common.lib.LibEntityNames;
import cpw.mods.fml.common.registry.EntityRegistry;

public final class ModEntities {

	public static void init() {
		EntityRegistry.registerModEntity(EntityManaBurst.class, LibEntityNames.MANA_BURST, 0, Botania.instance, 64, 10, true);
	}
	
}
