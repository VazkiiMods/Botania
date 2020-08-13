package vazkii.botania.common.components;

import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import nerdhub.cardinal.components.api.ComponentRegistry;
import nerdhub.cardinal.components.api.ComponentType;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.mob.*;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class EntityComponents implements EntityComponentInitializer {
	public static final ComponentType<LooniumComponent> LOONIUM_DROP = ComponentRegistry.INSTANCE.registerStatic(prefix("loonium_drop"), LooniumComponent.class);
	public static final ComponentType<EthicalComponent> TNT_ETHICAL = ComponentRegistry.INSTANCE.registerStatic(prefix("tnt_ethical"), EthicalComponent.class);
	public static final ComponentType<NarslimmusComponent> NARSLIMMUS = ComponentRegistry.INSTANCE.registerStatic(prefix("narslimmus"), NarslimmusComponent.class);

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
	}
}
