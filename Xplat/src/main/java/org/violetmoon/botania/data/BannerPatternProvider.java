/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 */

package vazkii.botania.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.entity.BannerPattern;

import vazkii.botania.common.block.BotaniaBannerPatterns;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class BannerPatternProvider implements DataProvider {
	private final PackOutput.PathProvider pathProvider;
	private final CompletableFuture<HolderLookup.Provider> registryLookupFuture;

	public BannerPatternProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
		this.pathProvider = packOutput.createRegistryElementsPathProvider(Registries.BANNER_PATTERN);
		this.registryLookupFuture = registryLookupFuture;
	}

	@Override
	public CompletableFuture<?> run(CachedOutput output) {
		return registryLookupFuture.thenCompose(registryLookup -> this.run(output, registryLookup));
	}

	private CompletableFuture<?> run(CachedOutput cache, HolderLookup.Provider registries) {
		Map<ResourceKey<BannerPattern>, BannerPattern> patterns = new HashMap<>();
		addBannerPatterns(patterns);

		var output = new ArrayList<CompletableFuture<?>>(patterns.size());
		for (Map.Entry<ResourceKey<BannerPattern>, BannerPattern> e : patterns.entrySet()) {
			Path path = pathProvider.json(e.getKey().location());
			output.add(DataProvider.saveStable(cache, registries, BannerPattern.DIRECT_CODEC, e.getValue(), path));
		}
		return CompletableFuture.allOf(output.toArray(CompletableFuture<?>[]::new));
	}

	private void addBannerPatterns(Map<ResourceKey<BannerPattern>, BannerPattern> patterns) {
		BotaniaBannerPatterns.provideData(patterns::put);
	}

	@Override
	public String getName() {
		return "Botania Banner Patterns";
	}
}
