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
		evt.getRegistry().register(EntityType.Builder.create(EntityManaBurst.class, EntityManaBurst::new)
				.tracker(64, 10, true).build("")
				.setRegistryName(LibEntityNames.MANA_BURST));
		evt.getRegistry().register(EntityType.Builder.create(EntitySignalFlare.class, EntitySignalFlare::new)
				.tracker(2048, 10, false).build("")
				.setRegistryName(LibEntityNames.SIGNAL_FLARE));
		evt.getRegistry().register(EntityType.Builder.create(EntityPixie.class, EntityPixie::new)
				.tracker(16, 3, true).build("")
				.setRegistryName(LibEntityNames.PIXIE));
		evt.getRegistry().register(EntityType.Builder.create(EntityFlameRing.class, EntityFlameRing::new)
				.tracker(32, 40, false).build("")
				.setRegistryName(LibEntityNames.FLAME_RING));
		evt.getRegistry().register(EntityType.Builder.create(EntityVineBall.class, EntityVineBall::new)
				.tracker(64, 10, true).build("")
				.setRegistryName(LibEntityNames.VINE_BALL));
		evt.getRegistry().register(EntityType.Builder.create(EntityDoppleganger.class, EntityDoppleganger::new)
				.tracker(128, 10, true).build("")
				.setRegistryName(LibEntityNames.DOPPLEGANGER));
		evt.getRegistry().register(EntityType.Builder.create(EntityMagicLandmine.class, EntityMagicLandmine::new)
				.tracker(128, 40, false).build("")
				.setRegistryName(LibEntityNames.MAGIC_LANDMINE));
		evt.getRegistry().register(EntityType.Builder.create(EntitySpark.class, EntitySpark::new)
				.tracker(64, 10, false).build("")
				.setRegistryName(LibEntityNames.SPARK));
		evt.getRegistry().register(EntityType.Builder.create(EntityThrownItem.class, EntityThrownItem::new)
				.tracker(64, 20, true).build("")
				.setRegistryName(LibEntityNames.THROWN_ITEM));
		evt.getRegistry().register(EntityType.Builder.create(EntityMagicMissile.class, EntityMagicMissile::new)
				.tracker(64, 2, true).build("")
				.setRegistryName(LibEntityNames.MAGIC_MISSILE));
		evt.getRegistry().register(EntityType.Builder.create(EntityThornChakram.class, EntityThornChakram::new)
				.tracker(64, 10, true).build("")
				.setRegistryName(LibEntityNames.THORN_CHAKRAM));
		evt.getRegistry().register(EntityType.Builder.create(EntityCorporeaSpark.class, EntityCorporeaSpark::new)
				.tracker(64, 10, false).build("")
				.setRegistryName(LibEntityNames.CORPOREA_SPARK));
		evt.getRegistry().register(EntityType.Builder.create(EntityEnderAirBottle.class, EntityEnderAirBottle::new)
				.tracker(64, 10, true).build("")
				.setRegistryName(LibEntityNames.ENDER_AIR_BOTTLE));
		evt.getRegistry().register(EntityType.Builder.create(EntityPoolMinecart.class, EntityPoolMinecart::new)
				.tracker(80, 3, true).build("")
				.setRegistryName(LibEntityNames.POOL_MINECART));
		evt.getRegistry().register(EntityType.Builder.create(EntityPinkWither.class, EntityPinkWither::new)
				.tracker(80, 3, false).build("")
				.setRegistryName(LibEntityNames.PINK_WITHER));
		evt.getRegistry().register(EntityType.Builder.create(EntityPlayerMover.class, EntityPlayerMover::new)
				.size(0, 0)
				.tracker(40, 3, true).build("")
				.setRegistryName(LibEntityNames.PLAYER_MOVER));
		evt.getRegistry().register(EntityType.Builder.create(EntityManaStorm.class, EntityManaStorm::new)
				.tracker(64, 10, false).build("")
				.setRegistryName(LibEntityNames.MANA_STORM));
		evt.getRegistry().register(EntityType.Builder.create(EntityBabylonWeapon.class, EntityBabylonWeapon::new)
				.size(0, 0)
				.tracker(64, 10, true).build("")
				.setRegistryName(LibEntityNames.BABYLON_WEAPON));
		evt.getRegistry().register(EntityType.Builder.create(EntityFallingStar.class, EntityFallingStar::new)
				.tracker(64, 10, true).build("")
				.setRegistryName(LibEntityNames.FALLING_STAR));
	}

}
