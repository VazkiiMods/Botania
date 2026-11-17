/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.neoforge.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import net.neoforged.neoforge.common.conditions.NotCondition;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import net.neoforged.neoforge.registries.datamaps.builtin.Strippable;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.handler.CompostingData;
import vazkii.botania.common.lib.BotaniaTags;

import java.util.concurrent.CompletableFuture;

public class BotaniaDataMapsProvider extends DataMapProvider {
	public BotaniaDataMapsProvider(PackOutput packOutput,
			CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(packOutput, lookupProvider);
	}

	@Override
	protected void gather(HolderLookup.Provider provider) {
		final var compostables = builder(NeoForgeDataMaps.COMPOSTABLES);
		CompostingData.init((item, chance) -> compostables.add(
				item.asItem().builtInRegistryHolder(), new Compostable(chance, true), false));

		final var strippables = builder(NeoForgeDataMaps.STRIPPABLES);
		BotaniaBlocks.addAxeStripping((input, output) -> strippables.add(
				input.builtInRegistryHolder(), new Strippable(output), false));

		final var furnaceFuels = builder(NeoForgeDataMaps.FURNACE_FUELS);
		int blazeRodTime = 2400;
		furnaceFuels.add(BotaniaBlocks.BLAZE_MESH.asItem().builtInRegistryHolder(),
				new FurnaceFuel(10 * blazeRodTime), false,
				new NotCondition(new ModLoadedCondition(BotaniaAPI.GOG_MODID)));
		int wallTime = 300;
		furnaceFuels.add(BotaniaTags.Items.WOODEN_WALLS, new FurnaceFuel(wallTime), false);
	}
}
