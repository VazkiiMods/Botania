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

import vazkii.botania.common.block.block_entity.LuminizerBlockEntity.PlayerMoverEntity;
import vazkii.botania.common.lib.LibEntityNames;

import java.util.function.BiConsumer;

public final class BotaniaEntities {
	public static final EntityType<ManaBurstEntity> MANA_BURST = EntityType.Builder.<ManaBurstEntity>of(
			ManaBurstEntity::new, MobCategory.MISC)
			.sized(0, 0)
			.updateInterval(10)
			.clientTrackingRange(6)
			.build(LibEntityNames.MANA_BURST.toString());
	public static final EntityType<PixieEntity> PIXIE = EntityType.Builder.<PixieEntity>of(PixieEntity::new, MobCategory.MISC)
			.sized(1, 1)
			.updateInterval(3)
			.clientTrackingRange(6)
			.build(LibEntityNames.PIXIE.toString());
	public static final EntityType<FlameRingEntity> FLAME_RING = EntityType.Builder.of(FlameRingEntity::new, MobCategory.MISC)
			.sized(0, 0)
			.clientTrackingRange(3)
			.updateInterval(40)
			.build(LibEntityNames.FLAME_RING.toString());
	public static final EntityType<VineBallEntity> VINE_BALL = EntityType.Builder.<VineBallEntity>of(VineBallEntity::new, MobCategory.MISC)
			.sized(0.25F, 0.25F)
			.clientTrackingRange(4)
			.updateInterval(10)
			.build(LibEntityNames.VINE_BALL.toString());
	public static final EntityType<GaiaGuardianEntity> DOPPLEGANGER = EntityType.Builder.of(GaiaGuardianEntity::new, MobCategory.MONSTER)
			.sized(0.6F, 1.8F)
			.fireImmune()
			.clientTrackingRange(10)
			.updateInterval(10)
			.build(LibEntityNames.DOPPLEGANGER.toString());
	public static final EntityType<MagicLandmineEntity> MAGIC_LANDMINE = EntityType.Builder.of(MagicLandmineEntity::new, MobCategory.MISC)
			.sized(5F, 0.1F)
			.clientTrackingRange(8)
			.updateInterval(40)
			.build(LibEntityNames.MAGIC_LANDMINE.toString());
	public static final EntityType<ManaSparkEntity> SPARK = EntityType.Builder.<ManaSparkEntity>of(ManaSparkEntity::new, MobCategory.MISC)
			.sized(0.2F, 0.5F)
			.fireImmune()
			.clientTrackingRange(4)
			.updateInterval(10)
			.build(LibEntityNames.SPARK.toString());
	public static final EntityType<ThrownItemEntity> THROWN_ITEM = EntityType.Builder.<ThrownItemEntity>of(ThrownItemEntity::new, MobCategory.MISC)
			.sized(0.25F, 0.25F)
			.clientTrackingRange(4)
			.updateInterval(20)
			.build(LibEntityNames.THROWN_ITEM.toString());
	public static final EntityType<MagicMissileEntity> MAGIC_MISSILE = EntityType.Builder.<MagicMissileEntity>of(MagicMissileEntity::new, MobCategory.MISC)
			.sized(0, 0)
			.clientTrackingRange(4)
			.updateInterval(2)
			.build(LibEntityNames.MAGIC_MISSILE.toString());
	public static final EntityType<ThornChakramEntity> THORN_CHAKRAM = EntityType.Builder.<ThornChakramEntity>of(ThornChakramEntity::new, MobCategory.MISC)
			.sized(0.25F, 0.25F)
			.clientTrackingRange(5)
			.updateInterval(10)
			.build(LibEntityNames.THORN_CHAKRAM.toString());
	public static final EntityType<CorporeaSparkEntity> CORPOREA_SPARK = EntityType.Builder.of(CorporeaSparkEntity::new, MobCategory.MISC)
			.sized(0.2F, 0.5F)
			.fireImmune()
			.clientTrackingRange(4)
			.updateInterval(40)
			.build(LibEntityNames.CORPOREA_SPARK.toString());
	public static final EntityType<EnderAirBottleEntity> ENDER_AIR_BOTTLE = EntityType.Builder.<EnderAirBottleEntity>of(EnderAirBottleEntity::new, MobCategory.MISC)
			.sized(0.25F, 0.25F)
			.clientTrackingRange(4)
			.updateInterval(10)
			.build(LibEntityNames.ENDER_AIR_BOTTLE.toString());
	public static final EntityType<ManaPoolMinecartEntity> POOL_MINECART = EntityType.Builder.<ManaPoolMinecartEntity>of(ManaPoolMinecartEntity::new, MobCategory.MISC)
			.sized(0.98F, 0.7F)
			.clientTrackingRange(5)
			.updateInterval(3)
			.build(LibEntityNames.POOL_MINECART.toString());
	public static final EntityType<PinkWitherEntity> PINK_WITHER = EntityType.Builder.of(PinkWitherEntity::new, MobCategory.MISC)
			.sized(0.9F, 3.5F)
			.clientTrackingRange(6)
			.updateInterval(3)
			.build(LibEntityNames.PINK_WITHER.toString());
	public static final EntityType<PlayerMoverEntity> PLAYER_MOVER = EntityType.Builder.<PlayerMoverEntity>of(PlayerMoverEntity::new, MobCategory.MISC)
			.sized(0, 0)
			.clientTrackingRange(10)
			.updateInterval(3)
			.build(LibEntityNames.PLAYER_MOVER.toString());
	public static final EntityType<ManaStormEntity> MANA_STORM = EntityType.Builder.of(ManaStormEntity::new, MobCategory.MISC)
			.sized(0.98F, 0.98F)
			.clientTrackingRange(4)
			.updateInterval(10)
			.build(LibEntityNames.MANA_STORM.toString());
	public static final EntityType<BabylonWeaponEntity> BABYLON_WEAPON = EntityType.Builder.<BabylonWeaponEntity>of(BabylonWeaponEntity::new, MobCategory.MISC)
			.sized(0, 0)
			.clientTrackingRange(6)
			.updateInterval(10)
			.build(LibEntityNames.BABYLON_WEAPON.toString());
	public static final EntityType<FallingStarEntity> FALLING_STAR = EntityType.Builder.<FallingStarEntity>of(FallingStarEntity::new, MobCategory.MISC)
			.sized(0, 0)
			.clientTrackingRange(4)
			.updateInterval(10)
			.build(LibEntityNames.FALLING_STAR.toString());
	public static final EntityType<EnderAirEntity> ENDER_AIR = EntityType.Builder.of(EnderAirEntity::new, MobCategory.MISC)
			.fireImmune()
			.sized(1, 1)
			.clientTrackingRange(4)
			.updateInterval(Integer.MAX_VALUE)
			.build(LibEntityNames.ENDER_AIR.toString());

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
		consumer.accept(BotaniaEntities.DOPPLEGANGER, Mob.createMobAttributes()
				.add(Attributes.MOVEMENT_SPEED, 0.4)
				.add(Attributes.MAX_HEALTH, GaiaGuardianEntity.MAX_HP)
				.add(Attributes.KNOCKBACK_RESISTANCE, 1.0));
		consumer.accept(BotaniaEntities.PIXIE, Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 2.0));
		consumer.accept(BotaniaEntities.PINK_WITHER, WitherBoss.createAttributes());
	}
}
