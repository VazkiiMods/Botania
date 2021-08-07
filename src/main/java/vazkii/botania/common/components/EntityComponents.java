/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.components;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.monster.CaveSpider;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.monster.Stray;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.vehicle.AbstractMinecart;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;

public class EntityComponents implements EntityComponentInitializer {
	public static final ComponentKey<LooniumComponent> LOONIUM_DROP = ComponentRegistryV3.INSTANCE.getOrCreate(prefix("loonium_drop"), LooniumComponent.class);
	public static final ComponentKey<EthicalComponent> TNT_ETHICAL = ComponentRegistryV3.INSTANCE.getOrCreate(prefix("tnt_ethical"), EthicalComponent.class);
	public static final ComponentKey<NarslimmusComponent> NARSLIMMUS = ComponentRegistryV3.INSTANCE.getOrCreate(prefix("narslimmus"), NarslimmusComponent.class);
	public static final ComponentKey<ItemFlagsComponent> INTERNAL_ITEM = ComponentRegistryV3.INSTANCE.getOrCreate(prefix("iitem"), ItemFlagsComponent.class);
	public static final ComponentKey<GhostRailComponent> GHOST_RAIL = ComponentRegistryV3.INSTANCE.getOrCreate(prefix("ghost_rail"), GhostRailComponent.class);
	public static final ComponentKey<KeptItemsComponent> KEPT_ITEMS = ComponentRegistryV3.INSTANCE.getOrCreate(prefix("kept_items"), KeptItemsComponent.class);

	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.registerFor(EnderMan.class, LOONIUM_DROP, e -> new LooniumComponent());
		registry.registerFor(Creeper.class, LOONIUM_DROP, e -> new LooniumComponent());
		registry.registerFor(Husk.class, LOONIUM_DROP, e -> new LooniumComponent());
		registry.registerFor(Drowned.class, LOONIUM_DROP, e -> new LooniumComponent());
		registry.registerFor(Zombie.class, LOONIUM_DROP, e -> new LooniumComponent());
		registry.registerFor(Stray.class, LOONIUM_DROP, e -> new LooniumComponent());
		registry.registerFor(Skeleton.class, LOONIUM_DROP, e -> new LooniumComponent());
		registry.registerFor(CaveSpider.class, LOONIUM_DROP, e -> new LooniumComponent());
		registry.registerFor(Spider.class, LOONIUM_DROP, e -> new LooniumComponent());

		registry.registerFor(PrimedTnt.class, TNT_ETHICAL, EthicalComponent::new);
		registry.registerFor(Slime.class, NARSLIMMUS, NarslimmusComponent::new);
		registry.registerFor(ItemEntity.class, INTERNAL_ITEM, e -> new ItemFlagsComponent());
		registry.registerFor(AbstractMinecart.class, GHOST_RAIL, e -> new GhostRailComponent());
		// Never copy as we handle it ourselves in ItemKeepIvy.onPlayerRespawn
		registry.registerForPlayers(KEPT_ITEMS, e -> new KeptItemsComponent(), RespawnCopyStrategy.NEVER_COPY);
	}
}
