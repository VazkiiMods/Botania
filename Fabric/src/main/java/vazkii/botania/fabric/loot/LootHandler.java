/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.fabric.loot;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;

import vazkii.botania.common.loot.BotaniaLootTables;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.function.Consumer;

import static vazkii.botania.api.BotaniaAPI.gogRL;

public final class LootHandler {
	public static final ResourceLocation GOG_SEEDS_TABLE = gogRL("extra_seeds");

	// only for Fabric, since we generate global loot modifiers for NeoForge
	public static void injectLoot(ResourceLocation id, Consumer<LootPool.Builder> addPool) {
		ResourceKey<LootTable> injectedLootTable = BotaniaLootTables.getInjectedLootTable(id);
		if (BotaniaLootTables.all().contains(injectedLootTable)) {
			addPool.accept(LootPool.lootPool().add(NestedLootTable.lootTableReference(injectedLootTable)));
		}
	}

	public static void injectGogLoot(ResourceLocation id, Consumer<LootPool.Builder> addPool) {
		if (XplatAbstractions.INSTANCE.gogLoaded() && (Blocks.SHORT_GRASS.getLootTable().location().equals(id)
				|| Blocks.TALL_GRASS.getLootTable().location().equals(id))) {
			ResourceKey<LootTable> gogSeedsKey = ResourceKey.create(Registries.LOOT_TABLE, GOG_SEEDS_TABLE);
			addPool.accept(LootPool.lootPool().add(NestedLootTable.lootTableReference(gogSeedsKey)));
		}
	}
}
