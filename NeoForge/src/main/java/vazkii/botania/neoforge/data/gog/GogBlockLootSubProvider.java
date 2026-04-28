package vazkii.botania.neoforge.data.gog;

import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.AnyOfCondition;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.neoforged.neoforge.common.Tags;

import vazkii.botania.common.loot.BotaniaLootTables;

import java.util.function.BiConsumer;

public class GogBlockLootSubProvider implements LootTableSubProvider {
	public GogBlockLootSubProvider(HolderLookup.Provider provider) {}

	@Override
	public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
		output.accept(BotaniaLootTables.GOG_EXTRA_SEEDS,
				LootTable.lootTable().withPool(LootPool.lootPool()
						.add(LootItem.lootTableItem(Items.MELON_SEEDS))
						.add(LootItem.lootTableItem(Items.PUMPKIN_SEEDS))
						.when(AnyOfCondition.anyOf(
								LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.SHORT_GRASS),
								LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.TALL_GRASS)
										.setProperties(StatePropertiesPredicate.Builder.properties()
												.hasProperty(BlockStateProperties.DOUBLE_BLOCK_HALF,
														DoubleBlockHalf.LOWER)
										)
						))
						.when(ExplosionCondition.survivesExplosion())
						.when(LootItemRandomChanceCondition.randomChance(0.03125f))
						.when(MatchTool.toolMatches(ItemPredicate.Builder.item().of(Tags.Items.TOOLS_SHEAR)).invert())
				));
	}
}
