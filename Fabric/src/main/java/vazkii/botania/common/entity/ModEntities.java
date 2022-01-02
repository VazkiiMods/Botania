/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.MinecartComparatorLogicRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.boss.wither.WitherBoss;

import vazkii.botania.common.block.tile.TileLightRelay.EntityPlayerMover;
import vazkii.botania.common.lib.LibEntityNames;

import java.util.function.BiConsumer;

import static vazkii.botania.common.block.ModBlocks.register;

public final class ModEntities {
	private static final EntityDimensions ZERO_SIZE = EntityDimensions.fixed(0, 0);

	public static final EntityType<EntityManaBurst> MANA_BURST = FabricEntityTypeBuilder.<EntityManaBurst>create(
			MobCategory.MISC, EntityManaBurst::new)
			.dimensions(ZERO_SIZE)
			.trackedUpdateRate(10)
			.trackRangeChunks(6)
			.forceTrackedVelocityUpdates(true)
			.build();
	public static final EntityType<EntityPixie> PIXIE = FabricEntityTypeBuilder.<EntityPixie>create(MobCategory.MISC, EntityPixie::new)
			.dimensions(EntityDimensions.scalable(1, 1))
			.trackedUpdateRate(3)
			.trackRangeChunks(6)
			.forceTrackedVelocityUpdates(true)
			.build();
	public static final EntityType<EntityFlameRing> FLAME_RING = FabricEntityTypeBuilder.<EntityFlameRing>create(MobCategory.MISC, EntityFlameRing::new)
			.dimensions(ZERO_SIZE)
			.trackRangeChunks(3)
			.trackedUpdateRate(40)
			.forceTrackedVelocityUpdates(false)
			.build();
	public static final EntityType<EntityVineBall> VINE_BALL = FabricEntityTypeBuilder.<EntityVineBall>create(MobCategory.MISC, EntityVineBall::new)
			.dimensions(EntityDimensions.scalable(0.25F, 0.25F))
			.trackRangeChunks(4)
			.trackedUpdateRate(10)
			.forceTrackedVelocityUpdates(true)
			.build();
	public static final EntityType<EntityDoppleganger> DOPPLEGANGER = FabricEntityTypeBuilder.create(MobCategory.MONSTER, EntityDoppleganger::new)
			.dimensions(EntityDimensions.scalable(0.6F, 1.8F))
			.fireImmune()
			.trackRangeChunks(10)
			.trackedUpdateRate(10)
			.forceTrackedVelocityUpdates(true)
			.build();
	public static final EntityType<EntityMagicLandmine> MAGIC_LANDMINE = FabricEntityTypeBuilder.create(MobCategory.MISC, EntityMagicLandmine::new)
			.dimensions(EntityDimensions.scalable(5F, 0.1F))
			.trackRangeChunks(8)
			.trackedUpdateRate(40)
			.forceTrackedVelocityUpdates(false)
			.build();
	public static final EntityType<EntityManaSpark> SPARK = FabricEntityTypeBuilder.<EntityManaSpark>create(MobCategory.MISC, EntityManaSpark::new)
			.dimensions(EntityDimensions.scalable(0.2F, 0.5F))
			.fireImmune()
			.trackRangeChunks(4)
			.trackedUpdateRate(10)
			.forceTrackedVelocityUpdates(false)
			.build();
	public static final EntityType<EntityThrownItem> THROWN_ITEM = FabricEntityTypeBuilder.<EntityThrownItem>create(MobCategory.MISC, EntityThrownItem::new)
			.dimensions(EntityDimensions.scalable(0.25F, 0.25F))
			.trackRangeChunks(4)
			.trackedUpdateRate(20)
			.forceTrackedVelocityUpdates(true)
			.build();
	public static final EntityType<EntityMagicMissile> MAGIC_MISSILE = FabricEntityTypeBuilder.<EntityMagicMissile>create(MobCategory.MISC, EntityMagicMissile::new)
			.dimensions(ZERO_SIZE)
			.trackRangeChunks(4)
			.trackedUpdateRate(2)
			.forceTrackedVelocityUpdates(true)
			.build();
	public static final EntityType<EntityThornChakram> THORN_CHAKRAM = FabricEntityTypeBuilder.<EntityThornChakram>create(MobCategory.MISC, EntityThornChakram::new)
			.dimensions(EntityDimensions.scalable(0.25F, 0.25F))
			.trackRangeChunks(5)
			.trackedUpdateRate(10)
			.forceTrackedVelocityUpdates(true)
			.build();
	public static final EntityType<EntityCorporeaSpark> CORPOREA_SPARK = FabricEntityTypeBuilder.<EntityCorporeaSpark>create(MobCategory.MISC, EntityCorporeaSpark::new)
			.dimensions(EntityDimensions.scalable(0.2F, 0.5F))
			.fireImmune()
			.trackRangeChunks(4)
			.trackedUpdateRate(40)
			.forceTrackedVelocityUpdates(false)
			.build();
	public static final EntityType<EntityEnderAirBottle> ENDER_AIR_BOTTLE = FabricEntityTypeBuilder.<EntityEnderAirBottle>create(MobCategory.MISC, EntityEnderAirBottle::new)
			.dimensions(EntityDimensions.scalable(0.25F, 0.25F))
			.trackRangeChunks(4)
			.trackedUpdateRate(10)
			.forceTrackedVelocityUpdates(true)
			.build();
	public static final EntityType<EntityPoolMinecart> POOL_MINECART = FabricEntityTypeBuilder.<EntityPoolMinecart>create(MobCategory.MISC, EntityPoolMinecart::new)
			.dimensions(EntityDimensions.scalable(0.98F, 0.7F))
			.trackRangeChunks(5)
			.trackedUpdateRate(3)
			.forceTrackedVelocityUpdates(true)
			.build();
	public static final EntityType<EntityPinkWither> PINK_WITHER = FabricEntityTypeBuilder.create(MobCategory.MISC, EntityPinkWither::new)
			.dimensions(EntityDimensions.scalable(0.9F, 3.5F))
			.trackRangeChunks(6)
			.trackedUpdateRate(3)
			.forceTrackedVelocityUpdates(false)
			.build();
	public static final EntityType<EntityPlayerMover> PLAYER_MOVER = FabricEntityTypeBuilder.<EntityPlayerMover>create(MobCategory.MISC, EntityPlayerMover::new)
			.dimensions(ZERO_SIZE)
			.trackRangeChunks(10)
			.trackedUpdateRate(3)
			.forceTrackedVelocityUpdates(true)
			.build();
	public static final EntityType<EntityManaStorm> MANA_STORM = FabricEntityTypeBuilder.create(MobCategory.MISC, EntityManaStorm::new)
			.dimensions(EntityDimensions.scalable(0.98F, 0.98F))
			.trackRangeChunks(4)
			.trackedUpdateRate(10)
			.forceTrackedVelocityUpdates(false)
			.build();
	public static final EntityType<EntityBabylonWeapon> BABYLON_WEAPON = FabricEntityTypeBuilder.<EntityBabylonWeapon>create(MobCategory.MISC, EntityBabylonWeapon::new)
			.dimensions(ZERO_SIZE)
			.trackRangeChunks(6)
			.trackedUpdateRate(10)
			.forceTrackedVelocityUpdates(true)
			.build();
	public static final EntityType<EntityFallingStar> FALLING_STAR = FabricEntityTypeBuilder.<EntityFallingStar>create(MobCategory.MISC, EntityFallingStar::new)
			.dimensions(ZERO_SIZE)
			.trackRangeChunks(4)
			.trackedUpdateRate(10)
			.forceTrackedVelocityUpdates(true)
			.build();
	public static final EntityType<EntityEnderAir> ENDER_AIR = FabricEntityTypeBuilder.create(MobCategory.MISC, EntityEnderAir::new)
			.fireImmune()
			.dimensions(EntityDimensions.fixed(1, 1))
			.trackRangeChunks(4)
			.trackedUpdateRate(Integer.MAX_VALUE)
			.build();

	public static void registerEntities(BiConsumer<EntityType<?>, ResourceLocation> r) {
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
		register(r, LibEntityNames.ENDER_AIR, ENDER_AIR);

		MinecartComparatorLogicRegistry.register(POOL_MINECART, (minecart, state, pos) -> minecart.getComparatorLevel());
	}

	public static void registerAttributes() {
		FabricDefaultAttributeRegistry.register(ModEntities.DOPPLEGANGER, Mob.createMobAttributes()
				.add(Attributes.MOVEMENT_SPEED, 0.4)
				.add(Attributes.MAX_HEALTH, EntityDoppleganger.MAX_HP)
				.add(Attributes.KNOCKBACK_RESISTANCE, 1.0));
		FabricDefaultAttributeRegistry.register(ModEntities.PIXIE, Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 2.0));
		FabricDefaultAttributeRegistry.register(ModEntities.PINK_WITHER, WitherBoss.createAttributes());
	}
}
