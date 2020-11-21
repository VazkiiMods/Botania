/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.registry.Registry;

import vazkii.botania.common.block.tile.TileLightRelay.EntityPlayerMover;
import vazkii.botania.common.lib.LibEntityNames;

import static vazkii.botania.common.block.ModBlocks.register;

public final class ModEntities {
	private static final EntityDimensions ZERO_SIZE = EntityDimensions.fixed(0, 0);
	
	public static final EntityType<EntityManaBurst> MANA_BURST = FabricEntityTypeBuilder.<EntityManaBurst>create(
			SpawnGroup.MISC, EntityManaBurst::new)
			.dimensions(ZERO_SIZE)
			.trackedUpdateRate(10)
			.trackRangeBlocks(64)
			.forceTrackedVelocityUpdates(true)
			.build();
	public static final EntityType<EntityPixie> PIXIE = FabricEntityTypeBuilder.<EntityPixie>create(SpawnGroup.MISC, EntityPixie::new)
			.dimensions(EntityDimensions.changing(1, 1))
			.trackedUpdateRate(3)
			.trackRangeBlocks(16)
			.forceTrackedVelocityUpdates(true)
			.build();
	public static final EntityType<EntityFlameRing> FLAME_RING = FabricEntityTypeBuilder.<EntityFlameRing>create(SpawnGroup.MISC, EntityFlameRing::new)
			.dimensions(ZERO_SIZE)
			.trackRangeBlocks(32)
			.trackedUpdateRate(40)
			.forceTrackedVelocityUpdates(false)
			.build();
	public static final EntityType<EntityVineBall> VINE_BALL = FabricEntityTypeBuilder.<EntityVineBall>create(SpawnGroup.MISC, EntityVineBall::new)
			.dimensions(EntityDimensions.changing(0.25F, 0.25F))
			.trackRangeBlocks(64)
			.trackedUpdateRate(10)
			.forceTrackedVelocityUpdates(true)
			.build();
	public static final EntityType<EntityDoppleganger> DOPPLEGANGER = FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, EntityDoppleganger::new)
			.dimensions(EntityDimensions.changing(0.6F, 1.8F))
			.fireImmune()
			.trackRangeBlocks(128)
			.trackedUpdateRate(10)
			.forceTrackedVelocityUpdates(true)
			.build();
	public static final EntityType<EntityMagicLandmine> MAGIC_LANDMINE = FabricEntityTypeBuilder.create(SpawnGroup.MISC, EntityMagicLandmine::new)
			.dimensions(EntityDimensions.changing(5F, 0.1F))
			.trackRangeBlocks(128)
			.trackedUpdateRate(40)
			.forceTrackedVelocityUpdates(false)
			.build();
	public static final EntityType<EntitySpark> SPARK = FabricEntityTypeBuilder.<EntitySpark>create(SpawnGroup.MISC, EntitySpark::new)
			.dimensions(EntityDimensions.changing(0.2F, 0.5F))
			.fireImmune()
			.trackRangeBlocks(64)
			.trackedUpdateRate(10)
			.forceTrackedVelocityUpdates(false)
			.build();
	public static final EntityType<EntityThrownItem> THROWN_ITEM = FabricEntityTypeBuilder.<EntityThrownItem>create(SpawnGroup.MISC, EntityThrownItem::new)
			.dimensions(EntityDimensions.changing(0.25F, 0.25F))
			.trackRangeBlocks(64)
			.trackedUpdateRate(20)
			.forceTrackedVelocityUpdates(true)
			.build();
	public static final EntityType<EntityMagicMissile> MAGIC_MISSILE = FabricEntityTypeBuilder.<EntityMagicMissile>create(SpawnGroup.MISC, EntityMagicMissile::new)
			.dimensions(ZERO_SIZE)
			.trackRangeBlocks(64)
			.trackedUpdateRate(2)
			.forceTrackedVelocityUpdates(true)
			.build();
	public static final EntityType<EntityThornChakram> THORN_CHAKRAM = FabricEntityTypeBuilder.<EntityThornChakram>create(SpawnGroup.MISC, EntityThornChakram::new)
			.dimensions(EntityDimensions.changing(0.25F, 0.25F))
			.trackRangeBlocks(64)
			.trackedUpdateRate(10)
			.forceTrackedVelocityUpdates(true)
			.build();
	public static final EntityType<EntityCorporeaSpark> CORPOREA_SPARK = FabricEntityTypeBuilder.<EntityCorporeaSpark>create(SpawnGroup.MISC, EntityCorporeaSpark::new)
			.dimensions(EntityDimensions.changing(0.2F, 0.5F))
			.fireImmune()
			.trackRangeBlocks(64)
			.trackedUpdateRate(40)
			.forceTrackedVelocityUpdates(false)
			.build();
	public static final EntityType<EntityEnderAirBottle> ENDER_AIR_BOTTLE = FabricEntityTypeBuilder.<EntityEnderAirBottle>create(SpawnGroup.MISC, EntityEnderAirBottle::new)
			.dimensions(EntityDimensions.changing(0.25F, 0.25F))
			.trackRangeBlocks(64)
			.trackedUpdateRate(10)
			.forceTrackedVelocityUpdates(true)
			.build();
	public static final EntityType<EntityPoolMinecart> POOL_MINECART = FabricEntityTypeBuilder.<EntityPoolMinecart>create(SpawnGroup.MISC, EntityPoolMinecart::new)
			.dimensions(EntityDimensions.changing(0.98F, 0.7F))
			.trackRangeBlocks(80)
			.trackedUpdateRate(3)
			.forceTrackedVelocityUpdates(true)
			.build();
	public static final EntityType<EntityPinkWither> PINK_WITHER = FabricEntityTypeBuilder.create(SpawnGroup.MISC, EntityPinkWither::new)
			.dimensions(EntityDimensions.changing(0.9F, 3.5F))
			.trackRangeBlocks(80)
			.trackedUpdateRate(3)
			.forceTrackedVelocityUpdates(false)
			.build();
	public static final EntityType<EntityPlayerMover> PLAYER_MOVER = FabricEntityTypeBuilder.<EntityPlayerMover>create(SpawnGroup.MISC, EntityPlayerMover::new)
			.dimensions(ZERO_SIZE)
			.trackRangeBlocks(40)
			.trackedUpdateRate(3)
			.forceTrackedVelocityUpdates(true)
			.build();
	public static final EntityType<EntityManaStorm> MANA_STORM = FabricEntityTypeBuilder.create(SpawnGroup.MISC, EntityManaStorm::new)
			.dimensions(EntityDimensions.changing(0.98F, 0.98F))
			.trackRangeBlocks(64)
			.trackedUpdateRate(10)
			.forceTrackedVelocityUpdates(false)
			.build();
	public static final EntityType<EntityBabylonWeapon> BABYLON_WEAPON = FabricEntityTypeBuilder.<EntityBabylonWeapon>create(SpawnGroup.MISC, EntityBabylonWeapon::new)
			.dimensions(ZERO_SIZE)
			.trackRangeBlocks(64)
			.trackedUpdateRate(10)
			.forceTrackedVelocityUpdates(true)
			.build();
	public static final EntityType<EntityFallingStar> FALLING_STAR = FabricEntityTypeBuilder.<EntityFallingStar>create(SpawnGroup.MISC, EntityFallingStar::new)
			.dimensions(ZERO_SIZE)
			.trackRangeBlocks(64)
			.trackedUpdateRate(10)
			.forceTrackedVelocityUpdates(true)
			.build();

	public static void registerEntities() {
		Registry<EntityType<?>> r = Registry.ENTITY_TYPE;
		register(r, LibEntityNames.MANA_BURST, MANA_BURST);
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
