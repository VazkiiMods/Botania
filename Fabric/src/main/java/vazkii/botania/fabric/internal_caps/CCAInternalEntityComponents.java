/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.fabric.internal_caps;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;

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

public class CCAInternalEntityComponents implements EntityComponentInitializer {
	public static final ComponentKey<CCALooniumComponent> LOONIUM_DROP = ComponentRegistryV3.INSTANCE.getOrCreate(prefix("loonium_drop"), CCALooniumComponent.class);
	public static final ComponentKey<CCAEthicalComponent> TNT_ETHICAL = ComponentRegistryV3.INSTANCE.getOrCreate(prefix("tnt_ethical"), CCAEthicalComponent.class);
	public static final ComponentKey<CCANarslimmusComponent> NARSLIMMUS = ComponentRegistryV3.INSTANCE.getOrCreate(prefix("narslimmus"), CCANarslimmusComponent.class);
	public static final ComponentKey<CCAItemFlagsComponent> INTERNAL_ITEM = ComponentRegistryV3.INSTANCE.getOrCreate(prefix("iitem"), CCAItemFlagsComponent.class);
	public static final ComponentKey<CCAGhostRailComponent> GHOST_RAIL = ComponentRegistryV3.INSTANCE.getOrCreate(prefix("ghost_rail"), CCAGhostRailComponent.class);
	public static final ComponentKey<CCAKeptItemsComponent> KEPT_ITEMS = ComponentRegistryV3.INSTANCE.getOrCreate(prefix("kept_items"), CCAKeptItemsComponent.class);
	public static final ComponentKey<CCATigerseyeComponent> TIGERSEYE = ComponentRegistryV3.INSTANCE.getOrCreate(prefix("tigerseye_pacified"), CCATigerseyeComponent.class);

	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
		registry.registerFor(EnderMan.class, LOONIUM_DROP, e -> new CCALooniumComponent());
		registry.registerFor(Creeper.class, LOONIUM_DROP, e -> new CCALooniumComponent());
		registry.registerFor(Husk.class, LOONIUM_DROP, e -> new CCALooniumComponent());
		registry.registerFor(Drowned.class, LOONIUM_DROP, e -> new CCALooniumComponent());
		registry.registerFor(Zombie.class, LOONIUM_DROP, e -> new CCALooniumComponent());
		registry.registerFor(Stray.class, LOONIUM_DROP, e -> new CCALooniumComponent());
		registry.registerFor(Skeleton.class, LOONIUM_DROP, e -> new CCALooniumComponent());
		registry.registerFor(CaveSpider.class, LOONIUM_DROP, e -> new CCALooniumComponent());
		registry.registerFor(Spider.class, LOONIUM_DROP, e -> new CCALooniumComponent());

		registry.registerFor(PrimedTnt.class, TNT_ETHICAL, CCAEthicalComponent::new);
		registry.registerFor(Slime.class, NARSLIMMUS, CCANarslimmusComponent::new);
		registry.registerFor(ItemEntity.class, INTERNAL_ITEM, e -> new CCAItemFlagsComponent());
		registry.registerFor(AbstractMinecart.class, GHOST_RAIL, e -> new CCAGhostRailComponent());
		registry.registerFor(Creeper.class, TIGERSEYE, creeper -> new CCATigerseyeComponent());
		// Never copy as we handle it ourselves in ItemKeepIvy.onPlayerRespawn
		registry.registerForPlayers(KEPT_ITEMS, e -> new CCAKeptItemsComponent(), RespawnCopyStrategy.NEVER_COPY);
	}
}
