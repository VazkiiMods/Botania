/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 */

package vazkii.botania.fabric.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.core.HolderLookup;

import vazkii.botania.data.loot.BotaniaBlockLoot;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * Wrapper for executing BlockLootSubProvider logic in Fabric context without having to extend Fabric classes.
 */
public class BotaniaBlockLootWrapper extends FabricBlockLootTableProvider {
	private final Function<HolderLookup.Provider, BotaniaBlockLoot> blockLootProvider;
	private final CompletableFuture<HolderLookup.Provider> registryLookup;

	private BotaniaBlockLootWrapper(Function<HolderLookup.Provider, BotaniaBlockLoot> blockLootProvider,
			FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
		super(dataOutput, registryLookup);
		this.blockLootProvider = blockLootProvider;
		this.registryLookup = registryLookup;
	}

	public static FabricDataGenerator.Pack.RegistryDependentFactory<BotaniaBlockLootWrapper> wrap(
			Function<HolderLookup.Provider, BotaniaBlockLoot> vanillaSubProvider) {
		return (output, registriesFuture) -> new BotaniaBlockLootWrapper(
				vanillaSubProvider, output, registriesFuture);
	}

	@Override
	public void generate() {
		var generator = blockLootProvider.apply(registryLookup.join());
		generator.generate();
		this.map.putAll(generator.getMap());
	}
}
