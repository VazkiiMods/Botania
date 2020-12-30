/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.components;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import nerdhub.cardinal.components.api.util.RespawnCopyStrategy;

public class EntityComponents implements EntityComponentInitializer {
	public static final ComponentKey<LooniumComponent> LOONIUM_DROP = ComponentRegistryV3.INSTANCE.getOrCreate(prefix("loonium_drop"), LooniumComponent.class);
	public static final ComponentKey<EthicalComponent> TNT_ETHICAL = ComponentRegistryV3.INSTANCE.getOrCreate(prefix("tnt_ethical"), EthicalComponent.class);
	public static final ComponentKey<NarslimmusComponent> NARSLIMMUS = ComponentRegistryV3.INSTANCE.getOrCreate(prefix("narslimmus"), NarslimmusComponent.class);
	public static final ComponentKey<ItemFlagsComponent> INTERNAL_ITEM = ComponentRegistryV3.INSTANCE.getOrCreate(prefix("iitem"), ItemFlagsComponent.class);
	public static final ComponentKey<GhostRailComponent> GHOST_RAIL = ComponentRegistryV3.INSTANCE.getOrCreate(prefix("ghost_rail"), GhostRailComponent.class);
	public static final ComponentKey<KeptItemsComponent> KEPT_ITEMS = ComponentRegistryV3.INSTANCE.getOrCreate(prefix("kept_items"), KeptItemsComponent.class);

	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.registerFor(EndermanEntity.class, LOONIUM_DROP, e -> new LooniumComponent());
		registry.registerFor(CreeperEntity.class, LOONIUM_DROP, e -> new LooniumComponent());
		registry.registerFor(HuskEntity.class, LOONIUM_DROP, e -> new LooniumComponent());
		registry.registerFor(DrownedEntity.class, LOONIUM_DROP, e -> new LooniumComponent());
		registry.registerFor(ZombieEntity.class, LOONIUM_DROP, e -> new LooniumComponent());
		registry.registerFor(StrayEntity.class, LOONIUM_DROP, e -> new LooniumComponent());
		registry.registerFor(SkeletonEntity.class, LOONIUM_DROP, e -> new LooniumComponent());
		registry.registerFor(CaveSpiderEntity.class, LOONIUM_DROP, e -> new LooniumComponent());
		registry.registerFor(SpiderEntity.class, LOONIUM_DROP, e -> new LooniumComponent());

		registry.registerFor(TntEntity.class, TNT_ETHICAL, EthicalComponent::new);
		registry.registerFor(SlimeEntity.class, NARSLIMMUS, NarslimmusComponent::new);
		registry.registerFor(ItemEntity.class, INTERNAL_ITEM, e -> new ItemFlagsComponent());
		registry.registerFor(AbstractMinecartEntity.class, GHOST_RAIL, e -> new GhostRailComponent());
		// Never copy as we handle it ourselves in ItemKeepIvy.onPlayerRespawn
		registry.registerForPlayers(KEPT_ITEMS, e -> new KeptItemsComponent(), RespawnCopyStrategy.NEVER_COPY);
	}
}
