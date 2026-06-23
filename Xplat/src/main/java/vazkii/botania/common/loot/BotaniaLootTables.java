/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.common.loot;

import com.google.common.collect.Sets;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;

import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static vazkii.botania.api.BotaniaAPI.MODID;
import static vazkii.botania.api.BotaniaAPI.botaniaRL;
import static vazkii.botania.api.BotaniaAPI.gogRL;

public class BotaniaLootTables {
	private static final Set<ResourceKey<LootTable>> LOCATIONS = Sets.newLinkedHashSet();
	private static final Set<ResourceKey<LootTable>> IMMUTABLE_LOCATIONS = Collections.unmodifiableSet(LOCATIONS);
	private static final Pattern INJECTED_LOOT_TABLE_PATTERN = Pattern.compile("injected/(?<namespace>[a-z0-9_.-]+)/(?<path>[a-z0-9/._-]+)");

	static {
		// register entity types for injected elementium axe beheading loot tables
		Stream.of(
				EntityType.WITHER_SKELETON, EntityType.SKELETON, EntityType.ZOMBIE, EntityType.PIGLIN,
				EntityType.CREEPER, EntityType.ENDER_DRAGON, EntityType.PLAYER
		).map(BotaniaLootTables::getInjectedLootTable).forEach(LOCATIONS::add);
	}

	public static final ResourceKey<LootTable> INJECTED_CHEST_ABANDONED_MINESHAFT = register(
			getInjectedLootTable(BuiltInLootTables.ABANDONED_MINESHAFT));
	public static final ResourceKey<LootTable> INJECTED_CHEST_DESERT_PYRAMID = register(
			getInjectedLootTable(BuiltInLootTables.DESERT_PYRAMID));
	public static final ResourceKey<LootTable> INJECTED_CHEST_JUNGLE_TEMPLE = register(
			getInjectedLootTable(BuiltInLootTables.JUNGLE_TEMPLE));
	public static final ResourceKey<LootTable> INJECTED_CHEST_SIMPLE_DUNGEON = register(
			getInjectedLootTable(BuiltInLootTables.SIMPLE_DUNGEON));
	public static final ResourceKey<LootTable> INJECTED_CHEST_SPAWN_BONUS_CHEST = register(
			getInjectedLootTable(BuiltInLootTables.SPAWN_BONUS_CHEST));
	public static final ResourceKey<LootTable> INJECTED_CHEST_STRONGHOLD_CORRIDOR = register(
			getInjectedLootTable(BuiltInLootTables.STRONGHOLD_CORRIDOR));
	public static final ResourceKey<LootTable> INJECTED_CHEST_VILLAGE_TEMPLE = register(
			getInjectedLootTable(BuiltInLootTables.VILLAGE_TEMPLE));
	public static final ResourceKey<LootTable> INJECTED_CHEST_VILLAGE_TOOLSMITH = register(
			getInjectedLootTable(BuiltInLootTables.VILLAGE_TOOLSMITH));
	public static final ResourceKey<LootTable> INJECTED_CHEST_VILLAGE_WEAPONSMITH = register(
			getInjectedLootTable(BuiltInLootTables.VILLAGE_WEAPONSMITH));

	public static final List<ResourceKey<LootTable>> DICE_ROLL_LOOT_TABLES = IntStream.rangeClosed(1, 6)
			.mapToObj(i -> register("dice_of_fate/roll_" + i)).toList();
	public static final ResourceKey<LootTable> GHAST_LOOT_TABLE = register("gameplay/ghast_ender_air_crying");

	public static final ResourceKey<LootTable> FEL_BLAZE = register("entities/fel_blaze");

	public static final ResourceKey<LootTable> GAIA_GUARDIAN_REWARD = register("gaia_guardian/reward");
	public static final ResourceKey<LootTable> GAIA_GUARDIAN_REWARD_HARD = register("gaia_guardian/reward_hard");

	public static final ResourceKey<LootTable> GAIA_GUARDIAN_LOTUSES = register("gaia_guardian/lotuses");
	public static final ResourceKey<LootTable> GAIA_GUARDIAN_MATERIALS = register("gaia_guardian/materials");
	public static final ResourceKey<LootTable> GAIA_GUARDIAN_RUNES = register("gaia_guardian/runes");

	public static final ResourceKey<LootTable> GAIA_HARD_WITHER_SKELETON = register("equipment/gaia/hard/wither_skeleton");

	public static final ResourceKey<LootTable> LOONIUM_DEFAULT_LOOT = register("loonium/default");

	public static final ResourceKey<LootTable> LOONIUM_ARMOR_ANCIENT_CITY = register("equipment/loonium/armor_ancient_city");
	public static final ResourceKey<LootTable> LOONIUM_ARMOR_BASTION_REMNANT = register("equipment/loonium/armor_bastion_remnant");
	public static final ResourceKey<LootTable> LOONIUM_ARMOR_DESERT_PYRAMID = register("equipment/loonium/armor_desert_pyramid");
	public static final ResourceKey<LootTable> LOONIUM_ARMOR_END_CITY = register("equipment/loonium/armor_end_city");
	public static final ResourceKey<LootTable> LOONIUM_ARMOR_FORTRESS = register("equipment/loonium/armor_fortress");
	public static final ResourceKey<LootTable> LOONIUM_ARMOR_JUNGLE_TEMPLE = register("equipment/loonium/armor_jungle_temple");
	public static final ResourceKey<LootTable> LOONIUM_ARMOR_MANSION = register("equipment/loonium/armor_mansion");
	public static final ResourceKey<LootTable> LOONIUM_ARMOR_MONUMENT = register("equipment/loonium/armor_monument");
	public static final ResourceKey<LootTable> LOONIUM_ARMOR_OUTPOST = register("equipment/loonium/armor_outpost");
	public static final ResourceKey<LootTable> LOONIUM_ARMOR_PORTAL = register("equipment/loonium/armor_portal");
	public static final ResourceKey<LootTable> LOONIUM_ARMOR_SHIPWRECK = register("equipment/loonium/armor_shipwreck");
	public static final ResourceKey<LootTable> LOONIUM_ARMOR_STRONGHOLD = register("equipment/loonium/armor_stronghold");
	public static final ResourceKey<LootTable> LOONIUM_ARMOR_TRAIL_RUINS = register("equipment/loonium/armor_trail_ruins");
	public static final ResourceKey<LootTable> LOONIUM_ARMOR_TRIAL_CHAMBER = register("equipment/loonium/armor_trial_chamber");

	public static final ResourceKey<LootTable> LOONIUM_DROWNED_DEFAULT = register("equipment/loonium/drowned");
	public static final ResourceKey<LootTable> LOONIUM_DROWNED_ANCIENT_CITY = register("equipment/loonium/drowned_ancient_city");
	public static final ResourceKey<LootTable> LOONIUM_DROWNED_JUNGLE_TEMPLE = register("equipment/loonium/drowned_jungle_temple");
	public static final ResourceKey<LootTable> LOONIUM_DROWNED_MONUMENT = register("equipment/loonium/drowned_monument");
	public static final ResourceKey<LootTable> LOONIUM_DROWNED_PORTAL = register("equipment/loonium/drowned_portal");
	public static final ResourceKey<LootTable> LOONIUM_DROWNED_SHIPWRECK = register("equipment/loonium/drowned_shipwreck");
	public static final ResourceKey<LootTable> LOONIUM_DROWNED_STRONGHOLD = register("equipment/loonium/drowned_stronghold");
	public static final ResourceKey<LootTable> LOONIUM_DROWNED_TRAIL_RUINS = register("equipment/loonium/drowned_trail_ruins");
	public static final ResourceKey<LootTable> LOONIUM_DROWNED_TRIAL_CHAMBER = register("equipment/loonium/drowned_trial_chamber");

	public static final ResourceKey<LootTable> LOONIUM_PIGLIN_BRUTE_DEFAULT = register("equipment/loonium/piglin_brute");
	public static final ResourceKey<LootTable> LOONIUM_PIGLIN_BASTION_REMNANT = register("equipment/loonium/piglin_bastion_remnant");
	public static final ResourceKey<LootTable> LOONIUM_PIGLIN_PORTAL = register("equipment/loonium/piglin_ruined_portal");

	public static final ResourceKey<LootTable> LOONIUM_PILLAGER_DEFAULT = register("equipment/loonium/pillager");

	public static final ResourceKey<LootTable> LOONIUM_SKELETON_DEFAULT = register("equipment/loonium/skeleton");
	public static final ResourceKey<LootTable> LOONIUM_SKELETON_ANCIENT_CITY = register("equipment/loonium/skeleton_ancient_city");
	public static final ResourceKey<LootTable> LOONIUM_SKELETON_DESERT_PYRAMID = register("equipment/loonium/skeleton_desert_pyramid");
	public static final ResourceKey<LootTable> LOONIUM_SKELETON_JUNGLE_TEMPLE = register("equipment/loonium/skeleton_jungle_temple");
	public static final ResourceKey<LootTable> LOONIUM_SKELETON_END_CITY = register("equipment/loonium/skeleton_end_city");
	public static final ResourceKey<LootTable> LOONIUM_SKELETON_FORTRESS = register("equipment/loonium/skeleton_fortress");
	public static final ResourceKey<LootTable> LOONIUM_SKELETON_MONUMENT = register("equipment/loonium/skeleton_monument");
	public static final ResourceKey<LootTable> LOONIUM_SKELETON_OUTPOST = register("equipment/loonium/skeleton_outpost");
	public static final ResourceKey<LootTable> LOONIUM_SKELETON_PORTAL = register("equipment/loonium/skeleton_portal");
	public static final ResourceKey<LootTable> LOONIUM_SKELETON_SHIPWRECK = register("equipment/loonium/skeleton_shipwreck");
	public static final ResourceKey<LootTable> LOONIUM_SKELETON_STRONGHOLD = register("equipment/loonium/skeleton_stronghold");
	public static final ResourceKey<LootTable> LOONIUM_SKELETON_TRAIL_RUINS = register("equipment/loonium/skeleton_trail_ruins");
	public static final ResourceKey<LootTable> LOONIUM_SKELETON_TRIAL_CHAMBER = register("equipment/loonium/skeleton_trial_chamber");

	public static final ResourceKey<LootTable> LOONIUM_VINDICATOR_DEFAULT = register("equipment/loonium/vindicator");

	public static final ResourceKey<LootTable> LOONIUM_ZOMBIE_DEFAULT = register("equipment/loonium/zombie");
	public static final ResourceKey<LootTable> LOONIUM_ZOMBIE_ANCIENT_CITY = register("equipment/loonium/zombie_ancient_city");
	public static final ResourceKey<LootTable> LOONIUM_ZOMBIE_DESERT_PYRAMID = register("equipment/loonium/zombie_desert_pyramid");
	public static final ResourceKey<LootTable> LOONIUM_ZOMBIE_END_CITY = register("equipment/loonium/zombie_end_city");
	public static final ResourceKey<LootTable> LOONIUM_ZOMBIE_FORTRESS = register("equipment/loonium/zombie_fortress");
	public static final ResourceKey<LootTable> LOONIUM_ZOMBIE_JUNGLE_TEMPLE = register("equipment/loonium/zombie_jungle_temple");
	public static final ResourceKey<LootTable> LOONIUM_ZOMBIE_MONUMENT = register("equipment/loonium/zombie_monument");
	public static final ResourceKey<LootTable> LOONIUM_ZOMBIE_OUTPOST = register("equipment/loonium/zombie_outpost");
	public static final ResourceKey<LootTable> LOONIUM_ZOMBIE_PORTAL = register("equipment/loonium/zombie_portal");
	public static final ResourceKey<LootTable> LOONIUM_ZOMBIE_SHIPWRECK = register("equipment/loonium/zombie_shipwreck");
	public static final ResourceKey<LootTable> LOONIUM_ZOMBIE_STRONGHOLD = register("equipment/loonium/zombie_stronghold");
	public static final ResourceKey<LootTable> LOONIUM_ZOMBIE_TRAIL_RUINS = register("equipment/loonium/zombie_trail_ruins");
	public static final ResourceKey<LootTable> LOONIUM_ZOMBIE_TRIAL_CHAMBER = register("equipment/loonium/zombie_trial_chamber");

	public static final ResourceKey<LootTable> LOONIUM_ZOMBIE_VILLAGER = register("equipment/loonium/zombie_villager");

	public static final ResourceKey<LootTable> GOG_EXTRA_SEEDS = registerGog("extra_seeds");
	public static final ResourceKey<LootTable> GOG_PEBBLES_TABLE = registerGog("pebbles");

	private static ResourceKey<LootTable> register(String path) {
		return register(ResourceKey.create(Registries.LOOT_TABLE, botaniaRL(path)));
	}

	private static ResourceKey<LootTable> registerGog(String path) {
		return register(ResourceKey.create(Registries.LOOT_TABLE, gogRL(path)));
	}

	private static ResourceKey<LootTable> register(ResourceKey<LootTable> location) {
		if (LOCATIONS.add(location)) {
			return location;
		} else {
			throw new IllegalArgumentException(location + " is already a registered built-in loot table");
		}
	}

	public static Set<ResourceKey<LootTable>> all() {
		return IMMUTABLE_LOCATIONS;
	}

	/**
	 * Gets the resource key for the dice loot table for the specified number.
	 * 
	 * @param roll The rolled number. Must be between 1 and 6, inclusive.
	 * @return Loot table resource key for the dice roll.
	 */
	public static ResourceKey<LootTable> getDiceRollTable(int roll) {
		return DICE_ROLL_LOOT_TABLES.get(roll - 1);
	}

	/**
	 * Gets the resource key of the loot table to inject into the specified entity type's default loot table.
	 */
	public static ResourceKey<LootTable> getInjectedLootTable(EntityType<?> entityType) {
		return getInjectedLootTable(entityType.getDefaultLootTable());
	}

	/**
	 * Gets the resource key of the loot table to inject into the specified table.
	 */
	public static ResourceKey<LootTable> getInjectedLootTable(ResourceKey<LootTable> baseLootTable) {
		return getInjectedLootTable(baseLootTable.location());
	}

	/**
	 * Gets the resource key of the loot table to inject into the specified table.
	 */
	public static ResourceKey<LootTable> getInjectedLootTable(ResourceLocation baseId) {
		return ResourceKey.create(Registries.LOOT_TABLE,
				botaniaRL("injected/%s/%s".formatted(baseId.getNamespace(), baseId.getPath())));
	}

	/**
	 * Gets the resource key of the loot table the specified table is supposed to be injected to.
	 */
	@Nullable
	public static ResourceKey<LootTable> getInjectionTargetLootTable(ResourceKey<LootTable> injectedLootTable) {
		Matcher matcher = INJECTED_LOOT_TABLE_PATTERN.matcher(injectedLootTable.location().getPath());
		if (!injectedLootTable.location().getNamespace().equals(MODID) || !matcher.matches()) {
			return null;
		}

		return ResourceKey.create(injectedLootTable.registryKey(),
				ResourceLocation.fromNamespaceAndPath(matcher.group("namespace"), matcher.group("path")));
	}
}
