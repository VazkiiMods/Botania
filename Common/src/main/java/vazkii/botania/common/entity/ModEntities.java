/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.entity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.boss.wither.WitherBoss;

import vazkii.botania.common.block.tile.TileLightRelay.EntityPlayerMover;
import vazkii.botania.common.lib.LibEntityNames;

import java.util.function.BiConsumer;

public final class ModEntities {
	public static final EntityType<EntityManaBurst> MANA_BURST = EntityType.Builder.<EntityManaBurst>of(
			EntityManaBurst::new, MobCategory.MISC)
			.sized(0, 0)
			.updateInterval(10)
			.clientTrackingRange(6)
			.build("");
	public static final EntityType<EntityPixie> PIXIE = EntityType.Builder.<EntityPixie>of(EntityPixie::new, MobCategory.MISC)
			.sized(1, 1)
			.updateInterval(3)
			.clientTrackingRange(6)
			.build("");
	public static final EntityType<EntityFlameRing> FLAME_RING = EntityType.Builder.of(EntityFlameRing::new, MobCategory.MISC)
			.sized(0, 0)
			.clientTrackingRange(3)
			.updateInterval(40)
			.build("");
	public static final EntityType<EntityVineBall> VINE_BALL = EntityType.Builder.<EntityVineBall>of(EntityVineBall::new, MobCategory.MISC)
			.sized(0.25F, 0.25F)
			.clientTrackingRange(4)
			.updateInterval(10)
			.build("");
	public static final EntityType<EntityDoppleganger> DOPPLEGANGER = EntityType.Builder.of(EntityDoppleganger::new, MobCategory.MONSTER)
			.sized(0.6F, 1.8F)
			.fireImmune()
			.clientTrackingRange(10)
			.updateInterval(10)
			.build("");
	public static final EntityType<EntityMagicLandmine> MAGIC_LANDMINE = EntityType.Builder.of(EntityMagicLandmine::new, MobCategory.MISC)
			.sized(5F, 0.1F)
			.clientTrackingRange(8)
			.updateInterval(40)
			.build("");
	public static final EntityType<EntityManaSpark> SPARK = EntityType.Builder.<EntityManaSpark>of(EntityManaSpark::new, MobCategory.MISC)
			.sized(0.2F, 0.5F)
			.fireImmune()
			.clientTrackingRange(4)
			.updateInterval(10)
			.build("");
	public static final EntityType<EntityThrownItem> THROWN_ITEM = EntityType.Builder.<EntityThrownItem>of(EntityThrownItem::new, MobCategory.MISC)
			.sized(0.25F, 0.25F)
			.clientTrackingRange(4)
			.updateInterval(20)
			.build("");
	public static final EntityType<EntityMagicMissile> MAGIC_MISSILE = EntityType.Builder.<EntityMagicMissile>of(EntityMagicMissile::new, MobCategory.MISC)
			.sized(0, 0)
			.clientTrackingRange(4)
			.updateInterval(2)
			.build("");
	public static final EntityType<EntityThornChakram> THORN_CHAKRAM = EntityType.Builder.<EntityThornChakram>of(EntityThornChakram::new, MobCategory.MISC)
			.sized(0.25F, 0.25F)
			.clientTrackingRange(5)
			.updateInterval(10)
			.build("");
	public static final EntityType<EntityCorporeaSpark> CORPOREA_SPARK = EntityType.Builder.of(EntityCorporeaSpark::new, MobCategory.MISC)
			.sized(0.2F, 0.5F)
			.fireImmune()
			.clientTrackingRange(4)
			.updateInterval(40)
			.build("");
	public static final EntityType<EntityEnderAirBottle> ENDER_AIR_BOTTLE = EntityType.Builder.<EntityEnderAirBottle>of(EntityEnderAirBottle::new, MobCategory.MISC)
			.sized(0.25F, 0.25F)
			.clientTrackingRange(4)
			.updateInterval(10)
			.build("");
	public static final EntityType<EntityPoolMinecart> POOL_MINECART = EntityType.Builder.<EntityPoolMinecart>of(EntityPoolMinecart::new, MobCategory.MISC)
			.sized(0.98F, 0.7F)
			.clientTrackingRange(5)
			.updateInterval(3)
			.build("");
	public static final EntityType<EntityPinkWither> PINK_WITHER = EntityType.Builder.of(EntityPinkWither::new, MobCategory.MISC)
			.sized(0.9F, 3.5F)
			.clientTrackingRange(6)
			.updateInterval(3)
			.build("");
	public static final EntityType<EntityPlayerMover> PLAYER_MOVER = EntityType.Builder.<EntityPlayerMover>of(EntityPlayerMover::new, MobCategory.MISC)
			.sized(0, 0)
			.clientTrackingRange(10)
			.updateInterval(3)
			.build("");
	public static final EntityType<EntityManaStorm> MANA_STORM = EntityType.Builder.of(EntityManaStorm::new, MobCategory.MISC)
			.sized(0.98F, 0.98F)
			.clientTrackingRange(4)
			.updateInterval(10)
			.build("");
	public static final EntityType<EntityBabylonWeapon> BABYLON_WEAPON = EntityType.Builder.<EntityBabylonWeapon>of(EntityBabylonWeapon::new, MobCategory.MISC)
			.sized(0, 0)
			.clientTrackingRange(6)
			.updateInterval(10)
			.build("");
	public static final EntityType<EntityFallingStar> FALLING_STAR = EntityType.Builder.<EntityFallingStar>of(EntityFallingStar::new, MobCategory.MISC)
			.sized(0, 0)
			.clientTrackingRange(4)
			.updateInterval(10)
			.build("");
	public static final EntityType<EntityEnderAir> ENDER_AIR = EntityType.Builder.of(EntityEnderAir::new, MobCategory.MISC)
			.fireImmune()
			.sized(1, 1)
			.clientTrackingRange(4)
			.updateInterval(Integer.MAX_VALUE)
			.build("");

	public static void registerEntities(BiConsumer<EntityType<?>, ResourceLocation> r) {
		r.accept(MANA_BURST, LibEntityNames.MANA_BURST);
		r.accept(PIXIE, LibEntityNames.PIXIE);
		r.accept(FLAME_RING, LibEntityNames.FLAME_RING);
		r.accept(VINE_BALL, LibEntityNames.VINE_BALL);
		r.accept(DOPPLEGANGER, LibEntityNames.DOPPLEGANGER);
		r.accept(MAGIC_LANDMINE, LibEntityNames.MAGIC_LANDMINE);
		r.accept(SPARK, LibEntityNames.SPARK);
		r.accept(THROWN_ITEM, LibEntityNames.THROWN_ITEM);
		r.accept(MAGIC_MISSILE, LibEntityNames.MAGIC_MISSILE);
		r.accept(THORN_CHAKRAM, LibEntityNames.THORN_CHAKRAM);
		r.accept(CORPOREA_SPARK, LibEntityNames.CORPOREA_SPARK);
		r.accept(ENDER_AIR_BOTTLE, LibEntityNames.ENDER_AIR_BOTTLE);
		r.accept(POOL_MINECART, LibEntityNames.POOL_MINECART);
		r.accept(PINK_WITHER, LibEntityNames.PINK_WITHER);
		r.accept(PLAYER_MOVER, LibEntityNames.PLAYER_MOVER);
		r.accept(MANA_STORM, LibEntityNames.MANA_STORM);
		r.accept(BABYLON_WEAPON, LibEntityNames.BABYLON_WEAPON);
		r.accept(FALLING_STAR, LibEntityNames.FALLING_STAR);
		r.accept(ENDER_AIR, LibEntityNames.ENDER_AIR);
	}

	public static void registerAttributes(BiConsumer<EntityType<? extends LivingEntity>, AttributeSupplier.Builder> consumer) {
		consumer.accept(ModEntities.DOPPLEGANGER, Mob.createMobAttributes()
				.add(Attributes.MOVEMENT_SPEED, 0.4)
				.add(Attributes.MAX_HEALTH, EntityDoppleganger.MAX_HP)
				.add(Attributes.KNOCKBACK_RESISTANCE, 1.0));
		consumer.accept(ModEntities.PIXIE, Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 2.0));
		consumer.accept(ModEntities.PINK_WITHER, WitherBoss.createAttributes());
	}
}
