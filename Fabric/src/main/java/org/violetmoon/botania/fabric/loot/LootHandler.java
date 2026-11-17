/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.fabric.loot;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;

import vazkii.botania.common.loot.BotaniaLootTables;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.function.Consumer;

/**
 * Fabric-specific loot table injection. Neoforge has global loot modifiers for this.
 */
public final class LootHandler {
	public static void injectLoot(ResourceLocation id, Consumer<LootPool.Builder> addPool) {
		ResourceKey<LootTable> injectedLootTable = BotaniaLootTables.getInjectedLootTable(id);
		if (BotaniaLootTables.all().contains(injectedLootTable)) {
			addPool.accept(LootPool.lootPool().add(NestedLootTable.lootTableReference(injectedLootTable)));
		}
	}

	public static void injectGogLoot(ResourceLocation id, Consumer<LootPool.Builder> addPool) {
		if (XplatAbstractions.INSTANCE.gogLoaded() && (Blocks.SHORT_GRASS.getLootTable().location().equals(id)
				|| Blocks.TALL_GRASS.getLootTable().location().equals(id))) {
			addPool.accept(LootPool.lootPool().add(NestedLootTable.lootTableReference(BotaniaLootTables.GOG_EXTRA_SEEDS)));
		}
	}
}
