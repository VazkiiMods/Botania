/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 */

package vazkii.botania.data.loot;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
import net.minecraft.world.level.storage.loot.entries.TagEntry;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.lib.BotaniaTags;
import vazkii.botania.common.loot.BotaniaLootTables;

import java.util.EnumSet;
import java.util.Set;
import java.util.function.BiConsumer;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

public class BotaniaGenericLoot implements LootTableSubProvider {
	// loot collections based on which village type hoses can actually have chests
	public static final EnumSet<VillageLoot> PLAINS_VILLAGE_LOOT = EnumSet
			.of(VillageLoot.CARTOGRAPHER, VillageLoot.FISHER, VillageLoot.TANNERY, VillageLoot.WEAPONSMITH);
	public static final EnumSet<VillageLoot> DESERT_VILLAGE_LOOT = EnumSet
			.of(VillageLoot.TEMPLE, VillageLoot.TOOLSMITH, VillageLoot.WEAPONSMITH);
	public static final EnumSet<VillageLoot> SAVANNA_VILLAGE_LOOT = EnumSet
			.of(VillageLoot.BUTCHER, VillageLoot.CARTOGRAPHER, VillageLoot.MASON, VillageLoot.TANNERY, VillageLoot.WEAPONSMITH);
	public static final EnumSet<VillageLoot> SNOWY_VILLAGE_LOOT = EnumSet
			.of(VillageLoot.ARMORER, VillageLoot.CARTOGRAPHER, VillageLoot.SHEPHERD, VillageLoot.TANNERY, VillageLoot.WEAPONSMITH);
	public static final EnumSet<VillageLoot> TAIGA_VILLAGE_LOOT = EnumSet
			.of(VillageLoot.CARTOGRAPHER, VillageLoot.FLETCHER, VillageLoot.TANNERY, VillageLoot.TOOLSMITH, VillageLoot.WEAPONSMITH);

	public static ResourceKey<LootTable> getStructureId(ResourceKey<Structure> structureKey) {
		return getStructureId(structureKey.location());
	}

	public static ResourceKey<LootTable> getStructureId(ResourceLocation structureId) {
		return ResourceKey.create(
				Registries.LOOT_TABLE, botaniaRL("loonium/%s/%s".formatted(structureId.getNamespace(), structureId.getPath())));
	}

	@Override
	public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {

		output.accept(BotaniaLootTables.GAIA_GUARDIAN_LOTUSES, LootTable.lootTable().withPool(LootPool.lootPool()
				.add(LootItem.lootTableItem(BotaniaItems.BLACKER_LOTUS).setWeight(3).setQuality(1))
				.add(LootItem.lootTableItem(BotaniaItems.BLACK_LOTUS).setWeight(7)
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
		));
		output.accept(BotaniaLootTables.GAIA_GUARDIAN_MATERIALS, LootTable.lootTable()
				.withPool(LootPool.lootPool().add(LootItem.lootTableItem(BotaniaItems.MANASTEEL_INGOT)
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(16, 27))))
						.when(LootItemRandomChanceCondition.randomChance(0.9f)))
				.withPool(LootPool.lootPool().add(LootItem.lootTableItem(BotaniaItems.MANA_PEARL)
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(8, 13))))
						.when(LootItemRandomChanceCondition.randomChance(0.7f)))
				.withPool(LootPool.lootPool().add(LootItem.lootTableItem(BotaniaItems.MANA_DIAMOND)
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 6))))
						.when(LootItemRandomChanceCondition.randomChance(0.5f)))
		);
		output.accept(BotaniaLootTables.GAIA_GUARDIAN_RUNES, LootTable.lootTable().withPool(LootPool.lootPool()
				.add(TagEntry.expandTag(BotaniaTags.Items.RUNES))
				.apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 4)))
		));

		// Note: As far as world generating is concerned, dungeons are "features" (i.e. like trees or geodes),
		// not "structures" (like everything else the Loonium might care about).
		output.accept(BotaniaLootTables.LOONIUM_DEFAULT_LOOT,
				buildDelegateLootTable(BuiltInLootTables.SIMPLE_DUNGEON));

		/*
		Note: Be careful about adding individual items instead of loot table references.
		The Loonium will randomly either generate a virtual chest full of loot from any referenced table, or put just
		one of the defined item entries into the "chest". Either way it only picks a single stack from that "chest".
		Individual item entries need to be weighted accordingly to not have them picked too often.
		Also, archaeology loot tables need to be handled carefully, due to their limited loot pool options.
		*/

		output.accept(getStructureId(BuiltinStructures.ANCIENT_CITY),
				LootTable.lootTable().withPool(LootPool.lootPool()
						.add(NestedLootTable.lootTableReference(BuiltInLootTables.ANCIENT_CITY).setWeight(9))
						.add(NestedLootTable.lootTableReference(BuiltInLootTables.ANCIENT_CITY_ICE_BOX).setWeight(1))
				)
		);
		output.accept(getStructureId(BuiltinStructures.BASTION_REMNANT),
				LootTable.lootTable().withPool(LootPool.lootPool()
						.add(NestedLootTable.lootTableReference(BuiltInLootTables.BASTION_BRIDGE).setWeight(1))
						.add(NestedLootTable.lootTableReference(BuiltInLootTables.BASTION_HOGLIN_STABLE).setWeight(1))
						.add(NestedLootTable.lootTableReference(BuiltInLootTables.BASTION_TREASURE).setWeight(1))
						.add(NestedLootTable.lootTableReference(BuiltInLootTables.BASTION_OTHER).setWeight(7))
				)
		);
		output.accept(getStructureId(BuiltinStructures.BURIED_TREASURE), buildDelegateLootTable(BuiltInLootTables.BURIED_TREASURE));
		output.accept(getStructureId(BuiltinStructures.DESERT_PYRAMID),
				LootTable.lootTable().withPool(LootPool.lootPool()
						.add(NestedLootTable.lootTableReference(BuiltInLootTables.DESERT_PYRAMID).setWeight(37))
						.add(NestedLootTable.lootTableReference(BuiltInLootTables.DESERT_PYRAMID_ARCHAEOLOGY).setWeight(2))
						// desert wells are features, so not detectable by the Loonium
						.add(NestedLootTable.lootTableReference(BuiltInLootTables.DESERT_WELL_ARCHAEOLOGY))
				)
		);
		output.accept(getStructureId(BuiltinStructures.END_CITY),
				LootTable.lootTable().withPool(LootPool.lootPool()
						.add(NestedLootTable.lootTableReference(BuiltInLootTables.END_CITY_TREASURE).setWeight(49))
						.add(LootItem.lootTableItem(Items.ELYTRA))
				)
		);
		output.accept(getStructureId(BuiltinStructures.FORTRESS), buildDelegateLootTable(BuiltInLootTables.NETHER_BRIDGE));
		// skipping igloo, because the laboratory piece, which is the only part that has loot, can't be detected reliably
		output.accept(getStructureId(BuiltinStructures.JUNGLE_TEMPLE),
				LootTable.lootTable().withPool(LootPool.lootPool()
						.add(NestedLootTable.lootTableReference(BuiltInLootTables.JUNGLE_TEMPLE).setWeight(9))
						.add(NestedLootTable.lootTableReference(BuiltInLootTables.JUNGLE_TEMPLE_DISPENSER))
				)
		);
		output.accept(getStructureId(BuiltinStructures.MINESHAFT), buildDelegateLootTable(BuiltInLootTables.ABANDONED_MINESHAFT));
		output.accept(getStructureId(BuiltinStructures.MINESHAFT_MESA), buildDelegateLootTable(BuiltInLootTables.ABANDONED_MINESHAFT));
		output.accept(getStructureId(BuiltinStructures.OCEAN_MONUMENT),
				LootTable.lootTable().withPool(LootPool.lootPool()
						.add(NestedLootTable.lootTableReference(EntityType.ELDER_GUARDIAN.getDefaultLootTable()).setWeight(5))
						// sponge is a player-kill drop and won't be rolled for the elder guardian table by the Loonium
						.add(LootItem.lootTableItem(Items.WET_SPONGE))

				)
		);
		output.accept(getStructureId(BuiltinStructures.OCEAN_RUIN_COLD),
				buildOceanRuinLootTable(BuiltInLootTables.OCEAN_RUIN_COLD_ARCHAEOLOGY)
		);
		output.accept(getStructureId(BuiltinStructures.OCEAN_RUIN_WARM),
				buildOceanRuinLootTable(BuiltInLootTables.OCEAN_RUIN_WARM_ARCHAEOLOGY)
		);
		output.accept(getStructureId(BuiltinStructures.PILLAGER_OUTPOST), buildDelegateLootTable(BuiltInLootTables.PILLAGER_OUTPOST));
		output.accept(getStructureId(BuiltinStructures.RUINED_PORTAL_DESERT), buildDelegateLootTable(BuiltInLootTables.RUINED_PORTAL));
		output.accept(getStructureId(BuiltinStructures.RUINED_PORTAL_JUNGLE), buildDelegateLootTable(BuiltInLootTables.RUINED_PORTAL));
		output.accept(getStructureId(BuiltinStructures.RUINED_PORTAL_MOUNTAIN), buildDelegateLootTable(BuiltInLootTables.RUINED_PORTAL));
		output.accept(getStructureId(BuiltinStructures.RUINED_PORTAL_NETHER), buildDelegateLootTable(BuiltInLootTables.RUINED_PORTAL));
		output.accept(getStructureId(BuiltinStructures.RUINED_PORTAL_OCEAN), buildDelegateLootTable(BuiltInLootTables.RUINED_PORTAL));
		output.accept(getStructureId(BuiltinStructures.RUINED_PORTAL_STANDARD), buildDelegateLootTable(BuiltInLootTables.RUINED_PORTAL));
		output.accept(getStructureId(BuiltinStructures.RUINED_PORTAL_SWAMP), buildDelegateLootTable(BuiltInLootTables.RUINED_PORTAL));
		output.accept(getStructureId(BuiltinStructures.SHIPWRECK), buildShipwreckLootTable());
		output.accept(getStructureId(BuiltinStructures.SHIPWRECK_BEACHED), buildShipwreckLootTable());
		output.accept(getStructureId(BuiltinStructures.STRONGHOLD),
				// Strongholds generate up to 4 corridor chests, up to 6 crossings, and up to 2 libraries with 1 or 2 chests
				LootTable.lootTable().withPool(LootPool.lootPool()
						.add(NestedLootTable.lootTableReference(BuiltInLootTables.STRONGHOLD_CORRIDOR).setWeight(4))
						.add(NestedLootTable.lootTableReference(BuiltInLootTables.STRONGHOLD_CROSSING).setWeight(6))
						.add(NestedLootTable.lootTableReference(BuiltInLootTables.STRONGHOLD_LIBRARY).setWeight(3))
				)
		);
		// skipping swamp hut, because it doesn't contain unique loot (could merge witch/cat tables, I guess)
		output.accept(getStructureId(BuiltinStructures.TRAIL_RUINS),
				// Trail ruins have 2 common suspicious gravel for the tower top and each road section,
				// and 6 common plus 3 rare suspicious gravel per building and for the tower bottom.
				LootTable.lootTable().withPool(LootPool.lootPool()
						.add(NestedLootTable.lootTableReference(BuiltInLootTables.TRAIL_RUINS_ARCHAEOLOGY_COMMON).setWeight(9))
						.add(NestedLootTable.lootTableReference(BuiltInLootTables.TRAIL_RUINS_ARCHAEOLOGY_RARE))
				)
		);
		output.accept(getStructureId(BuiltinStructures.TRIAL_CHAMBERS),
				// TODO: figure out appropriate weights (rewards are probably drowned out by other choices)
				LootTable.lootTable().withPool(LootPool.lootPool()
						.add(NestedLootTable.lootTableReference(BuiltInLootTables.TRIAL_CHAMBERS_CORRIDOR))
						.add(NestedLootTable.lootTableReference(BuiltInLootTables.TRIAL_CHAMBERS_CORRIDOR_POT))
						.add(NestedLootTable.lootTableReference(BuiltInLootTables.TRIAL_CHAMBERS_ENTRANCE))
						.add(NestedLootTable.lootTableReference(BuiltInLootTables.TRIAL_CHAMBERS_INTERSECTION))
						.add(NestedLootTable.lootTableReference(BuiltInLootTables.TRIAL_CHAMBERS_INTERSECTION_BARREL))
						.add(NestedLootTable.lootTableReference(BuiltInLootTables.TRIAL_CHAMBERS_REWARD).setWeight(2))
						.add(NestedLootTable.lootTableReference(BuiltInLootTables.TRIAL_CHAMBERS_REWARD_OMINOUS))
						.add(NestedLootTable.lootTableReference(BuiltInLootTables.TRIAL_CHAMBERS_SUPPLY))
				)
		);
		output.accept(getStructureId(BuiltinStructures.VILLAGE_PLAINS),
				buildVillageLootTable(BuiltInLootTables.VILLAGE_PLAINS_HOUSE, PLAINS_VILLAGE_LOOT)
		);
		output.accept(getStructureId(BuiltinStructures.VILLAGE_DESERT),
				buildVillageLootTable(BuiltInLootTables.VILLAGE_DESERT_HOUSE, DESERT_VILLAGE_LOOT)
		);
		output.accept(getStructureId(BuiltinStructures.VILLAGE_SAVANNA),
				buildVillageLootTable(BuiltInLootTables.VILLAGE_SAVANNA_HOUSE, SAVANNA_VILLAGE_LOOT)
		);
		output.accept(getStructureId(BuiltinStructures.VILLAGE_SNOWY),
				buildVillageLootTable(BuiltInLootTables.VILLAGE_SNOWY_HOUSE, SNOWY_VILLAGE_LOOT)
		);
		output.accept(getStructureId(BuiltinStructures.VILLAGE_TAIGA),
				buildVillageLootTable(BuiltInLootTables.VILLAGE_TAIGA_HOUSE, TAIGA_VILLAGE_LOOT)
		);
		output.accept(getStructureId(BuiltinStructures.WOODLAND_MANSION),
				LootTable.lootTable().withPool(LootPool.lootPool()
						.add(NestedLootTable.lootTableReference(BuiltInLootTables.WOODLAND_MANSION).setWeight(99))
						.add(LootItem.lootTableItem(Items.TOTEM_OF_UNDYING).setWeight(1))
				)
		);
	}

	public static LootTable.Builder buildVillageLootTable(ResourceKey<LootTable> house, Set<VillageLoot> villageLootSet) {
		LootPool.Builder lootPool = LootPool.lootPool().add(NestedLootTable.lootTableReference(house).setWeight(3));
		for (VillageLoot loot : villageLootSet) {
			lootPool.add(NestedLootTable.lootTableReference(loot.lootTable));
		}
		return LootTable.lootTable().withPool(lootPool);
	}

	public static LootTable.Builder buildShipwreckLootTable() {
		return LootTable.lootTable().withPool(LootPool.lootPool()
				.add(NestedLootTable.lootTableReference(BuiltInLootTables.SHIPWRECK_MAP))
				.add(NestedLootTable.lootTableReference(BuiltInLootTables.SHIPWRECK_SUPPLY))
				.add(NestedLootTable.lootTableReference(BuiltInLootTables.SHIPWRECK_TREASURE))
		);
	}

	public static LootTable.Builder buildDelegateLootTable(ResourceKey<LootTable> reference) {
		return LootTable.lootTable().withPool(LootPool.lootPool()
				.add(NestedLootTable.lootTableReference(reference))
		);
	}

	public static LootTable.Builder buildOceanRuinLootTable(ResourceKey<LootTable> archaeology) {
		// Note: since the Loonium does not supply a location, treasure maps will roll as empty maps
		return LootTable.lootTable().withPool(LootPool.lootPool()
				// 30% of ocean ruin sites generate with a big ruin instead of a small one,
				// but 90% of those big ruin sites additionally generate 4-8 small ruins around the big one.
				.add(NestedLootTable.lootTableReference(BuiltInLootTables.UNDERWATER_RUIN_BIG))
				.add(NestedLootTable.lootTableReference(BuiltInLootTables.UNDERWATER_RUIN_SMALL).setWeight(8))
				.add(NestedLootTable.lootTableReference(archaeology))
		);
	}

	public enum VillageLoot {
		WEAPONSMITH(BuiltInLootTables.VILLAGE_WEAPONSMITH),
		TOOLSMITH(BuiltInLootTables.VILLAGE_TOOLSMITH),
		ARMORER(BuiltInLootTables.VILLAGE_ARMORER),
		CARTOGRAPHER(BuiltInLootTables.VILLAGE_CARTOGRAPHER),
		MASON(BuiltInLootTables.VILLAGE_MASON),
		SHEPHERD(BuiltInLootTables.VILLAGE_SHEPHERD),
		BUTCHER(BuiltInLootTables.VILLAGE_BUTCHER),
		FLETCHER(BuiltInLootTables.VILLAGE_FLETCHER),
		FISHER(BuiltInLootTables.VILLAGE_FISHER),
		TANNERY(BuiltInLootTables.VILLAGE_TANNERY),
		TEMPLE(BuiltInLootTables.VILLAGE_TEMPLE);

		public final ResourceKey<LootTable> lootTable;

		VillageLoot(ResourceKey<LootTable> lootTable) {
			this.lootTable = lootTable;
		}
	}
}
