/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.world;

import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.Dimension;
import net.minecraft.world.DimensionType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.OverworldBiomeProvider;
import net.minecraft.world.gen.DimensionSettings;
import net.minecraft.world.gen.settings.DimensionGeneratorSettings;
import net.minecraft.world.storage.ServerWorldInfo;

import vazkii.botania.common.Botania;

import java.util.Locale;
import java.util.Optional;

public class WorldTypeUtil {
	private static boolean isServerLevelSkyblock(DedicatedServer server) {
		String levelType = Optional.ofNullable((String) server.getServerProperties().serverProperties.get("level-type")).map((str) -> str.toLowerCase(Locale.ROOT)).orElse("default");
		return Botania.gardenOfGlassLoaded && levelType.equals("botania-skyblock");
	}

	public static void setupForDedicatedServer(DedicatedServer server) {
		if (!isServerLevelSkyblock(server)) {
			return;
		}
		DynamicRegistries registries = server.func_244267_aX();
		ServerWorldInfo worldInfo = (ServerWorldInfo) server.func_240793_aU_();
		long seed = worldInfo.generatorSettings.getSeed();
		Registry<DimensionType> dimensions = registries.getRegistry(Registry.DIMENSION_TYPE_KEY);
		Registry<Biome> biomes = registries.getRegistry(Registry.BIOME_KEY);
		Registry<DimensionSettings> dimensionSettings = registries.getRegistry(Registry.NOISE_SETTINGS_KEY);
		SimpleRegistry<Dimension> simpleregistry = DimensionType.getDefaultSimpleRegistry(dimensions, biomes, dimensionSettings, seed);
		SimpleRegistry<Dimension> skyblock = DimensionGeneratorSettings.func_242749_a(dimensions, simpleregistry, new SkyblockChunkGenerator(new OverworldBiomeProvider(seed, false, false, biomes), seed, () -> dimensionSettings.getOrThrow(DimensionSettings.field_242734_c)));
		worldInfo.generatorSettings = new DimensionGeneratorSettings(worldInfo.generatorSettings.getSeed(), worldInfo.generatorSettings.doesGenerateFeatures(), worldInfo.generatorSettings.hasBonusChest(), skyblock);
	}
}
