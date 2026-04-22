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

import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.loot.BotaniaLootTables;

import java.util.function.BiConsumer;

public class BotaniaChestLoot implements LootTableSubProvider {
	@Override
	public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {

		output.accept(BotaniaLootTables.INJECTED_CHEST_ABANDONED_MINESHAFT, LootTable.lootTable()
				.withPool(LootPool.lootPool().setBonusRolls(UniformGenerator.between(0, 1))
						.add(LootItem.lootTableItem(BotaniaItems.blackLotus).setWeight(35))
						.add(EmptyLootItem.emptyItem().setWeight(65))
				)
		);

		output.accept(BotaniaLootTables.INJECTED_CHEST_DESERT_PYRAMID, LootTable.lootTable()
				.withPool(LootPool.lootPool().setBonusRolls(UniformGenerator.between(0, 1))
						.add(LootItem.lootTableItem(BotaniaItems.blackLotus).setWeight(35))
						.add(EmptyLootItem.emptyItem().setWeight(65))
				)
		);

		output.accept(BotaniaLootTables.INJECTED_CHEST_JUNGLE_TEMPLE, LootTable.lootTable()
				.withPool(LootPool.lootPool().setBonusRolls(UniformGenerator.between(0, 1))
						.add(LootItem.lootTableItem(BotaniaItems.blackLotus).setWeight(25))
						.add(EmptyLootItem.emptyItem().setWeight(75))
				)
		);

		output.accept(BotaniaLootTables.INJECTED_CHEST_SIMPLE_DUNGEON, LootTable.lootTable()
				.withPool(LootPool.lootPool().setBonusRolls(UniformGenerator.between(0, 1))
						.add(LootItem.lootTableItem(BotaniaItems.manaSteel).setWeight(25)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 5)))
						)
						.add(LootItem.lootTableItem(BotaniaItems.lexicon).setWeight(20))
						.add(LootItem.lootTableItem(BotaniaItems.manaBottle).setWeight(10))
						.add(LootItem.lootTableItem(BotaniaItems.blackLotus).setWeight(5))
						.add(EmptyLootItem.emptyItem().setWeight(40))
				)
		);

		output.accept(BotaniaLootTables.INJECTED_CHEST_SPAWN_BONUS_CHEST, LootTable.lootTable()
				.withPool(LootPool.lootPool().setBonusRolls(UniformGenerator.between(0, 1))
						.add(LootItem.lootTableItem(BotaniaItems.blackLotus).setWeight(10))
						.add(EmptyLootItem.emptyItem().setWeight(90))
				)
				.withPool(LootPool.lootPool().add(LootItem.lootTableItem(BotaniaItems.lexicon)))
		);

		output.accept(BotaniaLootTables.INJECTED_CHEST_STRONGHOLD_CORRIDOR, LootTable.lootTable()
				.withPool(LootPool.lootPool().setBonusRolls(UniformGenerator.between(0, 1))
						.add(LootItem.lootTableItem(BotaniaItems.manaSteel).setWeight(45)
								.apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))
						)
						.add(LootItem.lootTableItem(BotaniaItems.blackLotus).setWeight(10))
						.add(EmptyLootItem.emptyItem().setWeight(45))
				)
		);

		output.accept(BotaniaLootTables.INJECTED_CHEST_VILLAGE_TEMPLE, LootTable.lootTable()
				.withPool(LootPool.lootPool().setBonusRolls(UniformGenerator.between(0, 1))
						.add(LootItem.lootTableItem(BotaniaItems.blackLotus).setWeight(10))
						.add(EmptyLootItem.emptyItem().setWeight(90))
				)
		);

		output.accept(BotaniaLootTables.INJECTED_CHEST_VILLAGE_TOOLSMITH, LootTable.lootTable()
				.withPool(LootPool.lootPool().setBonusRolls(UniformGenerator.between(0, 1))
						.add(LootItem.lootTableItem(BotaniaItems.blackLotus).setWeight(10))
						.add(EmptyLootItem.emptyItem().setWeight(90))
				)
		);

		output.accept(BotaniaLootTables.INJECTED_CHEST_VILLAGE_WEAPONSMITH, LootTable.lootTable()
				.withPool(LootPool.lootPool().setBonusRolls(UniformGenerator.between(0, 1))
						.add(LootItem.lootTableItem(BotaniaItems.blackLotus).setWeight(10))
						.add(EmptyLootItem.emptyItem().setWeight(90))
				)
		);
	}
}
