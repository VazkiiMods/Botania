/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 26, 2014, 4:11:03 PM (GMT)]
 */
package vazkii.botania.common.entity;

import net.minecraftforge.fml.common.registry.EntityRegistry;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.tile.TileLightRelay.EntityPlayerMover;
import vazkii.botania.common.lib.LibEntityNames;

public final class ModEntities {

	public static void init() {
		int id = 0;

		EntityRegistry.registerModEntity(LibEntityNames.MANA_BURST_REGISTRY, EntityManaBurst.class, LibEntityNames.MANA_BURST, id++, Botania.instance, 64, 10, true);
		EntityRegistry.registerModEntity(LibEntityNames.SIGNAL_FLARE_REGISTRY, EntitySignalFlare.class, LibEntityNames.SIGNAL_FLARE, id++, Botania.instance, 2048, 10, false);
		EntityRegistry.registerModEntity(LibEntityNames.PIXIE_REGISTRY, EntityPixie.class, LibEntityNames.PIXIE, id++, Botania.instance, 16, 3, true);
		EntityRegistry.registerModEntity(LibEntityNames.FLAME_RING_REGISTRY, EntityFlameRing.class, LibEntityNames.FLAME_RING, id++, Botania.instance, 32, 40, false);
		EntityRegistry.registerModEntity(LibEntityNames.VINE_BALL_REGISTRY, EntityVineBall.class, LibEntityNames.VINE_BALL, id++, Botania.instance, 64, 10, true);
		EntityRegistry.registerModEntity(LibEntityNames.DOPPLEGANGER_REGISTRY, EntityDoppleganger.class, LibEntityNames.DOPPLEGANGER, id++, Botania.instance, 128, 3, true);
		EntityRegistry.registerModEntity(LibEntityNames.MAGIC_LANDMINE_REGISTRY, EntityMagicLandmine.class, LibEntityNames.MAGIC_LANDMINE, id++, Botania.instance, 128, 40, false);
		EntityRegistry.registerModEntity(LibEntityNames.SPARK_REGISTRY, EntitySpark.class, LibEntityNames.SPARK, id++, Botania.instance, 64, 10, false);
		EntityRegistry.registerModEntity(LibEntityNames.THROWN_ITEM_REGISTRY, EntityThrownItem.class, LibEntityNames.THROWN_ITEM, id++, Botania.instance, 64, 20, true);
		EntityRegistry.registerModEntity(LibEntityNames.MAGIC_MISSILE_REGISTRY, EntityMagicMissile.class, LibEntityNames.MAGIC_MISSILE, id++, Botania.instance, 64, 2, true);
		EntityRegistry.registerModEntity(LibEntityNames.THORN_CHAKRAM_REGISTRY, EntityThornChakram.class, LibEntityNames.THORN_CHAKRAM, id++, Botania.instance, 64, 10, true);
		EntityRegistry.registerModEntity(LibEntityNames.CORPOREA_SPARK_REGISTRY, EntityCorporeaSpark.class, LibEntityNames.CORPOREA_SPARK, id++, Botania.instance, 64, 10, false);
		EntityRegistry.registerModEntity(LibEntityNames.ENDER_AIR_BOTTLE_REGISTRY, EntityEnderAirBottle.class, LibEntityNames.ENDER_AIR_BOTTLE, id++, Botania.instance, 64, 10, true);
		EntityRegistry.registerModEntity(LibEntityNames.POOL_MINECART_REGISTRY, EntityPoolMinecart.class, LibEntityNames.POOL_MINECART, id++, Botania.instance, 80, 3, true);
		EntityRegistry.registerModEntity(LibEntityNames.PINK_WITHER_REGISTRY, EntityPinkWither.class, LibEntityNames.PINK_WITHER, id++, Botania.instance, 80, 3, false);
		EntityRegistry.registerModEntity(LibEntityNames.PLAYER_MOVER_REGISTRY, EntityPlayerMover.class, LibEntityNames.PLAYER_MOVER, id++, Botania.instance, 40, 3, true);
		EntityRegistry.registerModEntity(LibEntityNames.MANA_STORM_REGISTRY, EntityManaStorm.class, LibEntityNames.MANA_STORM, id++, Botania.instance, 64, 10, false);
		EntityRegistry.registerModEntity(LibEntityNames.BABYLON_WEAPON_REGISTRY, EntityBabylonWeapon.class, LibEntityNames.BABYLON_WEAPON, id++, Botania.instance, 64, 10, true);
		EntityRegistry.registerModEntity(LibEntityNames.FALLING_STAR_REGISTRY, EntityFallingStar.class, LibEntityNames.FALLING_STAR, id++, Botania.instance, 64, 10, true);
	}

}
