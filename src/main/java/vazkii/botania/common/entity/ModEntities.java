/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.entity;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;

import vazkii.botania.common.block.tile.TileLightRelay.EntityPlayerMover;
import vazkii.botania.common.lib.LibEntityNames;

import static vazkii.botania.common.block.ModBlocks.register;

public final class ModEntities {
	public static final EntityType<EntityManaBurst> MANA_BURST = EntityType.Builder.<EntityManaBurst>create(
			EntityManaBurst::new, EntityClassification.MISC)
			.size(0, 0)
			.setUpdateInterval(10)
			.setTrackingRange(64)
			.setShouldReceiveVelocityUpdates(true)
			.build("");
	public static final EntityType<EntitySignalFlare> SIGNAL_FLARE = EntityType.Builder.<EntitySignalFlare>create(
			EntitySignalFlare::new, EntityClassification.MISC)
			.size(0, 0)
			.setUpdateInterval(10)
			.setTrackingRange(2048)
			.setShouldReceiveVelocityUpdates(false)
			.build("");
	public static final EntityType<EntityPixie> PIXIE = EntityType.Builder.<EntityPixie>create(EntityPixie::new, EntityClassification.MISC)
			.size(1, 1)
			.setUpdateInterval(3)
			.setTrackingRange(16)
			.setShouldReceiveVelocityUpdates(true)
			.build("");
	public static final EntityType<EntityFlameRing> FLAME_RING = EntityType.Builder.<EntityFlameRing>create(EntityFlameRing::new, EntityClassification.MISC)
			.size(0, 0)
			.setTrackingRange(32)
			.setUpdateInterval(40)
			.setShouldReceiveVelocityUpdates(false)
			.build("");
	public static final EntityType<EntityVineBall> VINE_BALL = EntityType.Builder.<EntityVineBall>create(EntityVineBall::new, EntityClassification.MISC)
			.size(0.25F, 0.25F)
			.setTrackingRange(64)
			.setUpdateInterval(10)
			.setShouldReceiveVelocityUpdates(true)
			.build("");
	public static final EntityType<EntityDoppleganger> DOPPLEGANGER = EntityType.Builder.<EntityDoppleganger>create(EntityDoppleganger::new, EntityClassification.MONSTER)
			.size(0.6F, 1.8F)
			.immuneToFire()
			.setTrackingRange(128)
			.setUpdateInterval(10)
			.setShouldReceiveVelocityUpdates(true)
			.build("");
	public static final EntityType<EntityMagicLandmine> MAGIC_LANDMINE = EntityType.Builder.<EntityMagicLandmine>create(EntityMagicLandmine::new, EntityClassification.MISC)
			.size(5F, 0.1F)
			.setTrackingRange(128)
			.setUpdateInterval(40)
			.setShouldReceiveVelocityUpdates(false)
			.build("");
	public static final EntityType<EntitySpark> SPARK = EntityType.Builder.<EntitySpark>create(EntitySpark::new, EntityClassification.MISC)
			.size(0.1F, 0.5F)
			.immuneToFire()
			.setTrackingRange(64)
			.setUpdateInterval(10)
			.setShouldReceiveVelocityUpdates(false)
			.build("");
	public static final EntityType<EntityThrownItem> THROWN_ITEM = EntityType.Builder.<EntityThrownItem>create(EntityThrownItem::new, EntityClassification.MISC)
			.size(0.25F, 0.25F)
			.setTrackingRange(64)
			.setUpdateInterval(20)
			.setShouldReceiveVelocityUpdates(true)
			.build("");
	public static final EntityType<EntityMagicMissile> MAGIC_MISSILE = EntityType.Builder.<EntityMagicMissile>create(EntityMagicMissile::new, EntityClassification.MISC)
			.size(0, 0)
			.setTrackingRange(64)
			.setUpdateInterval(2)
			.setShouldReceiveVelocityUpdates(true)
			.build("");
	public static final EntityType<EntityThornChakram> THORN_CHAKRAM = EntityType.Builder.<EntityThornChakram>create(EntityThornChakram::new, EntityClassification.MISC)
			.size(0.25F, 0.25F)
			.setTrackingRange(64)
			.setUpdateInterval(10)
			.setShouldReceiveVelocityUpdates(true)
			.build("");
	public static final EntityType<EntityCorporeaSpark> CORPOREA_SPARK = EntityType.Builder.<EntityCorporeaSpark>create(EntityCorporeaSpark::new, EntityClassification.MISC)
			.size(0.1F, 0.5F)
			.immuneToFire()
			.setTrackingRange(64)
			.setUpdateInterval(40)
			.setShouldReceiveVelocityUpdates(false)
			.build("");
	public static final EntityType<EntityEnderAirBottle> ENDER_AIR_BOTTLE = EntityType.Builder.<EntityEnderAirBottle>create(EntityEnderAirBottle::new, EntityClassification.MISC)
			.size(0.25F, 0.25F)
			.setTrackingRange(64)
			.setUpdateInterval(10)
			.setShouldReceiveVelocityUpdates(true)
			.build("");
	public static final EntityType<EntityPoolMinecart> POOL_MINECART = EntityType.Builder.<EntityPoolMinecart>create(EntityPoolMinecart::new, EntityClassification.MISC)
			.size(0.98F, 0.7F)
			.setTrackingRange(80)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(true)
			.build("");
	public static final EntityType<EntityPinkWither> PINK_WITHER = EntityType.Builder.<EntityPinkWither>create(EntityPinkWither::new, EntityClassification.MISC)
			.size(0.9F, 3.5F)
			.setTrackingRange(80)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false)
			.build("");
	public static final EntityType<EntityPlayerMover> PLAYER_MOVER = EntityType.Builder.<EntityPlayerMover>create(EntityPlayerMover::new, EntityClassification.MISC)
			.size(0, 0)
			.setTrackingRange(40)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(true)
			.build("");
	public static final EntityType<EntityManaStorm> MANA_STORM = EntityType.Builder.<EntityManaStorm>create(EntityManaStorm::new, EntityClassification.MISC)
			.size(0.98F, 0.98F)
			.setTrackingRange(64)
			.setUpdateInterval(10)
			.setShouldReceiveVelocityUpdates(false)
			.build("");
	public static final EntityType<EntityBabylonWeapon> BABYLON_WEAPON = EntityType.Builder.<EntityBabylonWeapon>create(EntityBabylonWeapon::new, EntityClassification.MISC)
			.size(0, 0)
			.setTrackingRange(64)
			.setUpdateInterval(10)
			.setShouldReceiveVelocityUpdates(true)
			.build("");
	public static final EntityType<EntityFallingStar> FALLING_STAR = EntityType.Builder.<EntityFallingStar>create(EntityFallingStar::new, EntityClassification.MISC)
			.size(0, 0)
			.setTrackingRange(64)
			.setUpdateInterval(10)
			.setShouldReceiveVelocityUpdates(true)
			.build("");

	public static void registerEntities(RegistryEvent.Register<EntityType<?>> evt) {
		IForgeRegistry<EntityType<?>> r = evt.getRegistry();
		register(r, LibEntityNames.MANA_BURST, MANA_BURST);
		register(r, LibEntityNames.SIGNAL_FLARE, SIGNAL_FLARE);
		register(r, LibEntityNames.PIXIE, PIXIE);
		register(r, LibEntityNames.FLAME_RING, FLAME_RING);
		register(r, LibEntityNames.VINE_BALL, VINE_BALL);
		register(r, LibEntityNames.DOPPLEGANGER, DOPPLEGANGER);
		register(r, LibEntityNames.MAGIC_LANDMINE, MAGIC_LANDMINE);
		register(r, LibEntityNames.SPARK, SPARK);
		register(r, LibEntityNames.THROWN_ITEM, THROWN_ITEM);
		register(r, LibEntityNames.MAGIC_MISSILE, MAGIC_MISSILE);
		register(r, LibEntityNames.THORN_CHAKRAM, THORN_CHAKRAM);
		register(r, LibEntityNames.CORPOREA_SPARK, CORPOREA_SPARK);
		register(r, LibEntityNames.ENDER_AIR_BOTTLE, ENDER_AIR_BOTTLE);
		register(r, LibEntityNames.POOL_MINECART, POOL_MINECART);
		register(r, LibEntityNames.PINK_WITHER, PINK_WITHER);
		register(r, LibEntityNames.PLAYER_MOVER, PLAYER_MOVER);
		register(r, LibEntityNames.MANA_STORM, MANA_STORM);
		register(r, LibEntityNames.BABYLON_WEAPON, BABYLON_WEAPON);
		register(r, LibEntityNames.FALLING_STAR, FALLING_STAR);
	}

}
