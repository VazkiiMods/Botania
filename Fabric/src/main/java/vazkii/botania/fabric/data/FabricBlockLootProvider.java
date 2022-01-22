package vazkii.botania.fabric.data;

import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import vazkii.botania.common.block.BlockModDoubleFlower;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.data.BlockLootProvider;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;

public class FabricBlockLootProvider implements DataProvider {
	private final DataGenerator generator;

	public FabricBlockLootProvider(DataGenerator generator) {
		this.generator = generator;
	}

	@Override
	public void run(HashCache cache) throws IOException {
		var tables = new HashMap<ResourceLocation, LootTable.Builder>();
		for (var b : Registry.BLOCK) {
			ResourceLocation id = Registry.BLOCK.getKey(b);
			if (!LibMisc.MOD_ID.equals(id.getNamespace())) {
				continue;
			}

			if (b instanceof BlockModDoubleFlower) {
				tables.put(id, genDoubleFlower(b));
			}
		}

		for (var e : tables.entrySet()) {
			Path path = BlockLootProvider.getPath(generator.getOutputFolder(), e.getKey());
			DataProvider.save(BlockLootProvider.GSON, cache, LootTables.serialize(e.getValue().setParamSet(LootContextParamSets.BLOCK).build()), path);
		}
	}

	protected static LootTable.Builder genDoubleFlower(Block b) {
		LootPoolEntryContainer.Builder<?> entry = LootItem.lootTableItem(b)
				.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(b).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER)))
				.when(MatchTool.toolMatches(ItemPredicate.Builder.item().of(FabricToolTags.SHEARS)));
		LootPool.Builder pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(entry)
				.when(ExplosionCondition.survivesExplosion());
		return LootTable.lootTable().withPool(pool);
	}

	@Override
	public String getName() {
		return "Botania block loot (Fabric-specific)";
	}
}
