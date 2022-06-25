package vazkii.botania.fabric.data;

import net.minecraft.core.Registry;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

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
	public void run(CachedOutput cache) throws IOException {
		var tables = new HashMap<ResourceLocation, LootTable.Builder>();
		for (var b : Registry.BLOCK) {
			ResourceLocation id = Registry.BLOCK.getKey(b);
			if (!LibMisc.MOD_ID.equals(id.getNamespace())) {
				continue;
			}

			// Nothing for now
		}

		for (var e : tables.entrySet()) {
			Path path = BlockLootProvider.getPath(generator.getOutputFolder(), e.getKey());
			DataProvider.saveStable(cache, LootTables.serialize(e.getValue().setParamSet(LootContextParamSets.BLOCK).build()), path);
		}
	}

	@Override
	public String getName() {
		return "Botania block loot (Fabric-specific)";
	}
}
