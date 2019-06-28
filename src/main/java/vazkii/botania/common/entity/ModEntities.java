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

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vazkii.botania.common.block.tile.TileLightRelay.EntityPlayerMover;
import vazkii.botania.common.lib.LibEntityNames;
import vazkii.botania.common.lib.LibMisc;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModEntities {
	@SubscribeEvent
	public static void registerEntities(RegistryEvent.Register<EntityType<?>> evt) {
		evt.getRegistry().register(EntityType.Builder.<EntityManaBurst>create(
				EntityManaBurst::new, EntityClassification.MISC)
				.size(0, 0)
				.setUpdateInterval(10)
				.setTrackingRange(64)
				.setShouldReceiveVelocityUpdates(true)
				.build("")
				.setRegistryName(LibEntityNames.MANA_BURST));
		evt.getRegistry().register(EntityType.Builder.<EntitySignalFlare>create(
				EntitySignalFlare::new, EntityClassification.MISC)
				.size(0, 0)
				.setUpdateInterval(10)
				.setTrackingRange(2048)
				.setShouldReceiveVelocityUpdates(false)
				.build("")
				.setRegistryName(LibEntityNames.SIGNAL_FLARE));
		evt.getRegistry().register(EntityType.Builder.<EntityPixie>create(EntityPixie::new, EntityClassification.MISC)
				.size(1, 1)
				.setUpdateInterval(3)
				.setTrackingRange(16)
				.setShouldReceiveVelocityUpdates(true)
				.build("")
				.setRegistryName(LibEntityNames.PIXIE));
		evt.getRegistry().register(EntityType.Builder.<EntityFlameRing>create(EntityFlameRing::new, EntityClassification.MISC)
				.size(0, 0)
				.setTrackingRange(32)
				.setUpdateInterval(40)
				.setShouldReceiveVelocityUpdates(false)
				.build("")
				.setRegistryName(LibEntityNames.FLAME_RING));
		evt.getRegistry().register(EntityType.Builder.<EntityVineBall>create(EntityVineBall::new, EntityClassification.MISC)
				.setTrackingRange(64)
				.setUpdateInterval(10)
				.setShouldReceiveVelocityUpdates(true)
				.build("")
				.setRegistryName(LibEntityNames.VINE_BALL));
		evt.getRegistry().register(EntityType.Builder.<EntityDoppleganger>create(EntityDoppleganger::new, EntityClassification.MONSTER)
				.size(0.6F, 1.8F)
				.setTrackingRange(128)
				.setUpdateInterval(10)
				.setShouldReceiveVelocityUpdates(true)
				.build("")
				.setRegistryName(LibEntityNames.DOPPLEGANGER));
		evt.getRegistry().register(EntityType.Builder.<EntityMagicLandmine>create(EntityMagicLandmine::new, EntityClassification.MISC)
				.size(0, 0)
				.setTrackingRange(128)
				.setUpdateInterval(40)
				.setShouldReceiveVelocityUpdates(false)
				.build("")
				.setRegistryName(LibEntityNames.MAGIC_LANDMINE));
		evt.getRegistry().register(EntityType.Builder.<EntitySpark>create(EntitySpark::new, EntityClassification.MISC)
				.size(0.1F, 0.5F)
				.immuneToFire()
				.setTrackingRange(64)
				.setUpdateInterval(10)
				.setShouldReceiveVelocityUpdates(false)
				.build("")
				.setRegistryName(LibEntityNames.SPARK));
		evt.getRegistry().register(EntityType.Builder.<EntityThrownItem>create(EntityThrownItem::new, EntityClassification.MISC)
				.size(0.25F, 0.25F)
				.setTrackingRange(64)
				.setUpdateInterval(20)
				.setShouldReceiveVelocityUpdates(true)
				.build("")
				.setRegistryName(LibEntityNames.THROWN_ITEM));
		evt.getRegistry().register(EntityType.Builder.<EntityMagicMissile>create(EntityMagicMissile::new, EntityClassification.MISC)
				.size(0, 0)
				.setTrackingRange(64)
				.setUpdateInterval(2)
				.setShouldReceiveVelocityUpdates(true)
				.build("")
				.setRegistryName(LibEntityNames.MAGIC_MISSILE));
		evt.getRegistry().register(EntityType.Builder.<EntityThornChakram>create(EntityThornChakram::new, EntityClassification.MISC)
				.setTrackingRange(64)
				.setUpdateInterval(10)
				.setShouldReceiveVelocityUpdates(true)
				.build("")
				.setRegistryName(LibEntityNames.THORN_CHAKRAM));
		evt.getRegistry().register(EntityType.Builder.<EntityCorporeaSpark>create(EntityCorporeaSpark::new, EntityClassification.MISC)
				.size(0.1F, 0.5F)
				.immuneToFire()
				.setTrackingRange(64)
				.setUpdateInterval(40)
				.setShouldReceiveVelocityUpdates(false)
				.build("")
				.setRegistryName(LibEntityNames.CORPOREA_SPARK));
		evt.getRegistry().register(EntityType.Builder.<EntityEnderAirBottle>create(EntityEnderAirBottle::new, EntityClassification.MISC)
				.size(0.25F, 0.25F)
				.setTrackingRange(64)
				.setUpdateInterval(10)
				.setShouldReceiveVelocityUpdates(true)
				.build("")
				.setRegistryName(LibEntityNames.ENDER_AIR_BOTTLE));
		evt.getRegistry().register(EntityType.Builder.<EntityPoolMinecart>create(EntityPoolMinecart::new, EntityClassification.MISC)
				.size(0.98F, 0.7F)
				.setTrackingRange(80)
				.setUpdateInterval(3)
				.setShouldReceiveVelocityUpdates(true)
				.build("")
				.setRegistryName(LibEntityNames.POOL_MINECART));
		evt.getRegistry().register(EntityType.Builder.<EntityPinkWither>create(EntityPinkWither::new, EntityClassification.MISC)
				.size(0.9F, 3.5F)
				.setTrackingRange(80)
				.setUpdateInterval(3)
				.setShouldReceiveVelocityUpdates(false)
				.build("")
				.setRegistryName(LibEntityNames.PINK_WITHER));
		evt.getRegistry().register(EntityType.Builder.<EntityPlayerMover>create(EntityPlayerMover::new, EntityClassification.MISC)
				.size(0, 0)
				.setTrackingRange(40)
				.setUpdateInterval(3)
				.setShouldReceiveVelocityUpdates(true)
				.build("")
				.setRegistryName(LibEntityNames.PLAYER_MOVER));
		evt.getRegistry().register(EntityType.Builder.<EntityManaStorm>create(EntityManaStorm::new, EntityClassification.MISC)
				.setTrackingRange(64)
				.setUpdateInterval(10)
				.setShouldReceiveVelocityUpdates(false)
				.build("")
				.setRegistryName(LibEntityNames.MANA_STORM));
		evt.getRegistry().register(EntityType.Builder.<EntityBabylonWeapon>create(EntityBabylonWeapon::new, EntityClassification.MISC)
				.size(0, 0)
				.setTrackingRange(64)
				.setUpdateInterval(10)
				.setShouldReceiveVelocityUpdates(true)
				.build("")
				.setRegistryName(LibEntityNames.BABYLON_WEAPON));
		evt.getRegistry().register(EntityType.Builder.<EntityFallingStar>create(EntityFallingStar::new, EntityClassification.MISC)
				.size(0, 0)
				.setTrackingRange(64)
				.setUpdateInterval(10)
				.setShouldReceiveVelocityUpdates(true)
				.build("")
				.setRegistryName(LibEntityNames.FALLING_STAR));
	}

}
