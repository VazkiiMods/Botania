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
		int id = 0;
		EntityRegistry.registerModEntity(EntityManaBurst.class, LibEntityNames.MANA_BURST, id++, Botania.instance, 64, 10, true);
		EntityRegistry.registerModEntity(EntitySignalFlare.class, LibEntityNames.SIGNAL_FLARE, id++, Botania.instance, 2048, 10, false);
		EntityRegistry.registerModEntity(EntityPixie.class, LibEntityNames.PIXIE, id++, Botania.instance, 16, 3, true);
		EntityRegistry.registerModEntity(EntityFlameRing.class, LibEntityNames.FLAME_RING, id++, Botania.instance, 32, 40, false);
		EntityRegistry.registerModEntity(EntityVineBall.class, LibEntityNames.VINE_BALL, id++, Botania.instance, 64, 10, true);
		EntityRegistry.registerModEntity(EntityDoppleganger.class, LibEntityNames.DOPPLEGANGER, id++, Botania.instance, 128, 3, true);
		EntityRegistry.registerModEntity(EntityMagicLandmine.class, LibEntityNames.MAGIC_LANDMINE, id++, Botania.instance, 128, 40, false);
		EntityRegistry.registerModEntity(EntitySpark.class, LibEntityNames.SPARK, id++, Botania.instance, 64, 10, false);
	}

}
