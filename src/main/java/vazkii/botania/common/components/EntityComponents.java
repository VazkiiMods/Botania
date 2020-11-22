package vazkii.botania.common.components;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import nerdhub.cardinal.components.api.util.RespawnCopyStrategy;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

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
		registry.registerForPlayers(KEPT_ITEMS, e -> new KeptItemsComponent(), RespawnCopyStrategy.ALWAYS_COPY);
	}
}
