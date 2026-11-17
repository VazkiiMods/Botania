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

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class BotaniaLootTableProvider {
	private BotaniaLootTableProvider() {}

	public static LootTableProvider create(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
		return new LootTableProvider(
				output,
				BuiltInLootTables.all(),
				List.of(
						new LootTableProvider.SubProviderEntry(BotaniaBlockLoot::new, LootContextParamSets.BLOCK)
				),
				registries
		);
	}

	public static LootTable.Builder copyReferencedLootTable(ResourceKey<LootTable> reference) {
		return LootTable.lootTable().withPool(
				LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1))
						.add(NestedLootTable.lootTableReference(reference))
		);
	}
}
