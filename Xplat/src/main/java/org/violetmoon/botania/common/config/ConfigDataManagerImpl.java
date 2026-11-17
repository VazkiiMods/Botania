/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.common.config;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.configdata.ConfigDataManager;
import vazkii.botania.api.configdata.GaiaFightConfiguration;
import vazkii.botania.api.configdata.LooniumStructureConfiguration;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

public class ConfigDataManagerImpl implements ConfigDataManager {

	public static final String LOONIUM_CONFIG_PATH = "config/loonium";
	public static final String GAIA_CONFIG_PATH = "config/gaia";

	public static void registerListener() {
		XplatAbstractions.instance().registerReloadListener(PackType.SERVER_DATA, botaniaRL("configdata"), new ConfigDataManagerImpl());
	}

	private final Map<ResourceLocation, LooniumStructureConfiguration> looniumConfigs = new HashMap<>();
	private final Map<ResourceLocation, GaiaFightConfiguration> gaiaFightConfigs = new HashMap<>();

	@Nullable
	@Override
	public LooniumStructureConfiguration getEffectiveLooniumStructureConfiguration(ResourceLocation id) {
		LooniumStructureConfiguration configuration = this.looniumConfigs.get(id);
		return configuration != null ? configuration.getEffectiveConfig(looniumConfigs::get) : null;
	}

	@Nullable
	@Override
	public GaiaFightConfiguration getGaiaFightConfiguration(ResourceLocation id) {
		return gaiaFightConfigs.get(id);
	}

	private static void validateLooniumConfig(Map<ResourceLocation, LooniumStructureConfiguration> map) {
		Set<ResourceLocation> errorEntries = new HashSet<>();
		Set<ResourceLocation> visitedEntries = new LinkedHashSet<>();
		do {
			errorEntries.clear();
			for (Map.Entry<ResourceLocation, LooniumStructureConfiguration> entry : map.entrySet()) {
				ResourceLocation id = entry.getKey();
				ResourceLocation parent = entry.getValue().parent;
				if (id.equals(parent)) {
					BotaniaAPI.LOGGER.warn("Ignoring Loonium structure configuration, because it specified itself as parent: {}", id);
					errorEntries.add(id);
				} else {
					visitedEntries.clear();
					if (!findTopmostParent(map, id, parent, visitedEntries)) {
						BotaniaAPI.LOGGER.warn("Ignoring Loonium structure configuration(s) without top-most parent: {}", visitedEntries);
						errorEntries.addAll(visitedEntries);
						break;
					}
				}
			}
			errorEntries.forEach(map::remove);
		} while (!errorEntries.isEmpty() && !map.isEmpty());

		if (!map.containsKey(LooniumStructureConfiguration.DEFAULT_CONFIG_ID)) {
			BotaniaAPI.LOGGER.error("Default Loonium configuration not found!");
		}
	}

	private static void validateGaiaFightConfig(Map<ResourceLocation, GaiaFightConfiguration> map) {
		if (!map.containsKey(GaiaFightConfiguration.NORMAL)) {
			BotaniaAPI.LOGGER.error("Normal gaia fight configuration not found!");
		}
		if (!map.containsKey(GaiaFightConfiguration.HARD)) {
			BotaniaAPI.LOGGER.error("Hard mode gaia fight configuration not found!");
		}
	}

	private static boolean findTopmostParent(Map<ResourceLocation, LooniumStructureConfiguration> map,
			ResourceLocation id, @Nullable ResourceLocation parent, Set<ResourceLocation> visitedEntries) {
		if (!visitedEntries.add(id)) {
			BotaniaAPI.LOGGER.warn("Cyclic dependency between Loonium structure configurations detected: {}", visitedEntries);
			return false;
		}
		if (parent == null) {
			return true;
		}
		var parentConfig = map.get(parent);
		return parentConfig != null && findTopmostParent(map, parent, parentConfig.parent, visitedEntries);
	}

	private void applyLooniumConfig(Map<ResourceLocation, LooniumStructureConfiguration> looniumConfigs) {
		BotaniaAPI.LOGGER.info("Loaded {} Loonium configurations", looniumConfigs.size());
		this.looniumConfigs.putAll(looniumConfigs);
	}

	private void applyGaiaFightConfig(Map<ResourceLocation, GaiaFightConfiguration> gaiaFightConfigs) {
		BotaniaAPI.LOGGER.info("Loaded {} gaia fight configurations", gaiaFightConfigs.size());
		this.gaiaFightConfigs.putAll(gaiaFightConfigs);
	}

	@Override
	public CompletableFuture<Void> reload(PreparationBarrier barrier, ResourceManager manager,
			ProfilerFiller prepProfiler, ProfilerFiller reloadProfiler,
			Executor backgroundExecutor, Executor gameExecutor) {
		var looniumTask = scheduleConfigParse(barrier, manager, backgroundExecutor, gameExecutor, ConfigDataType.LOONIUM);
		var gaiaFightTask = scheduleConfigParse(barrier, manager, backgroundExecutor, gameExecutor, ConfigDataType.GAIA_FIGHT);

		return CompletableFuture.allOf(looniumTask, gaiaFightTask).thenRun(() -> BotaniaAPI.instance().setConfigData(this));
	}

	private <T> CompletableFuture<Void> scheduleConfigParse(PreparationBarrier barrier, ResourceManager manager,
			Executor backgroundExecutor, Executor gameExecutor, ConfigDataType<T> type) {
		return CompletableFuture.supplyAsync(() -> {
			Map<ResourceLocation, JsonElement> resourceMap = new HashMap<>();
			SimpleJsonResourceReloadListener.scanDirectory(manager, type.directory, new Gson(), resourceMap);
			Map<ResourceLocation, T> configs = new HashMap<>(resourceMap.size());
			resourceMap.forEach((id, jsonElement) -> {
				BotaniaAPI.LOGGER.debug("Parsing {} config '{}'", type.directory, id);
				type.codec.parse(JsonOps.INSTANCE, jsonElement)
						.ifError(error -> BotaniaAPI.LOGGER.error("Error parsing {}[{}]: {}", type.directory, id, error.message()))
						.ifSuccess(c -> configs.put(id, c));
			});
			type.validateFunction.accept(configs);
			return configs;
		}, backgroundExecutor)
				.thenCompose(barrier::wait)
				.thenAcceptAsync(c -> type.applyFunction.accept(this, c), gameExecutor);
	}

	private record ConfigDataType<T>(Codec<T> codec, String directory,
			Consumer<Map<ResourceLocation, T>> validateFunction,
			BiConsumer<ConfigDataManagerImpl, Map<ResourceLocation, T>> applyFunction) {
		private static final ConfigDataType<LooniumStructureConfiguration> LOONIUM =
				new ConfigDataType<>(LooniumStructureConfiguration.CODEC, LOONIUM_CONFIG_PATH,
						ConfigDataManagerImpl::validateLooniumConfig, ConfigDataManagerImpl::applyLooniumConfig);
		private static final ConfigDataType<GaiaFightConfiguration> GAIA_FIGHT =
				new ConfigDataType<>(GaiaFightConfiguration.CODEC, GAIA_CONFIG_PATH,
						ConfigDataManagerImpl::validateGaiaFightConfig, ConfigDataManagerImpl::applyGaiaFightConfig);

	}
}
