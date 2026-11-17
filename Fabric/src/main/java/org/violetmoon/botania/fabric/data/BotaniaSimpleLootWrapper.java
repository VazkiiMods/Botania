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
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class BotaniaSimpleLootWrapper extends SimpleFabricLootTableProvider {
	private final Supplier<LootTableSubProvider> lootProvider;

	public BotaniaSimpleLootWrapper(Supplier<LootTableSubProvider> lootProvider,
			FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registryLookup,
			LootContextParamSet lootContextType) {
		super(output, registryLookup, lootContextType);
		this.lootProvider = lootProvider;
	}

	public static FabricDataGenerator.Pack.RegistryDependentFactory<BotaniaSimpleLootWrapper> wrap(
			Supplier<LootTableSubProvider> vanillaSubProvider, LootContextParamSet lootContextType) {
		return (output, registriesFuture) -> new BotaniaSimpleLootWrapper(
				vanillaSubProvider, output, registriesFuture, lootContextType);
	}

	public static FabricDataGenerator.Pack.RegistryDependentFactory<BotaniaSimpleLootWrapper> wrap(
			Function<HolderLookup.Provider, LootTableSubProvider> vanillaSubProvider, LootContextParamSet lootContextType) {
		return (output, registriesFuture) -> new BotaniaSimpleLootWrapper(
				() -> vanillaSubProvider.apply(registriesFuture.join()), output, registriesFuture, lootContextType);
	}

	@Override
	public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
		lootProvider.get().generate(output);
	}
}
