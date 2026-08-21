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
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

import vazkii.botania.common.block.BotaniaBlocks;

import java.util.concurrent.CompletableFuture;

public class GogDataMapsProvider extends DataMapProvider {
	public GogDataMapsProvider(PackOutput packOutput,
			CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(packOutput, lookupProvider);
	}

	@Override
	protected void gather(HolderLookup.Provider provider) {
		final var furnaceFuels = builder(NeoForgeDataMaps.FURNACE_FUELS);
		int blazeRodTime = 2400;
		furnaceFuels.add(BotaniaBlocks.BLAZE_MESH.asItem().builtInRegistryHolder(),
				new FurnaceFuel(5 * blazeRodTime), false);
	}
}
