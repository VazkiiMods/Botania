/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.neoforge.data.gog;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;

import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.loot.BotaniaLootTables;

import java.util.function.BiConsumer;

public class GogBlockUseLootProvider implements LootTableSubProvider {
	public GogBlockUseLootProvider(HolderLookup.Provider provider) {}

	@Override
	public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
		output.accept(BotaniaLootTables.GOG_PEBBLES_TABLE,
				LootTable.lootTable().withPool(LootPool.lootPool().add(LootItem.lootTableItem(BotaniaItems.pebble))));
	}

}
