package vazkii.botania.data;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.BiasedToBottomInt;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;

import vazkii.botania.api.configdata.GaiaFightConfiguration;
import vazkii.botania.api.configdata.MobEffectToApply;
import vazkii.botania.api.configdata.MobSpawnData;
import vazkii.botania.common.config.ConfigDataManagerImpl;
import vazkii.botania.common.entity.BotaniaEntities;
import vazkii.botania.common.loot.BotaniaLootTables;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class GaiaFightConfigurationProvider implements DataProvider {

	private final PackOutput.PathProvider pathProvider;
	private final CompletableFuture<HolderLookup.Provider> registryLookupFuture;

	public GaiaFightConfigurationProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
		pathProvider = packOutput.createPathProvider(PackOutput.Target.DATA_PACK, ConfigDataManagerImpl.GAIA_CONFIG_PATH);
		this.registryLookupFuture = registryLookupFuture;
	}

	@Override
	public CompletableFuture<?> run(CachedOutput cache) {
		return registryLookupFuture.thenCompose(registryLookup -> this.run(cache, registryLookup));
	}

	private CompletableFuture<?> run(CachedOutput cache, HolderLookup.Provider registries) {
		Map<ResourceLocation, GaiaFightConfiguration> configs = new HashMap<>();
		addConfigs(configs);

		var output = new ArrayList<CompletableFuture<?>>(configs.size());
		for (Map.Entry<ResourceLocation, GaiaFightConfiguration> e : configs.entrySet()) {
			Path path = pathProvider.json(e.getKey());
			GaiaFightConfiguration config = e.getValue();
			JsonElement jsonTree = GaiaFightConfiguration.CODEC.encodeStart(JsonOps.INSTANCE, config)
					.getOrThrow();
			output.add(DataProvider.saveStable(cache, jsonTree, path));
		}
		return CompletableFuture.allOf(output.toArray(CompletableFuture<?>[]::new));
	}

	private void addConfigs(Map<ResourceLocation, GaiaFightConfiguration> configs) {
		configs.put(GaiaFightConfiguration.NORMAL, GaiaFightConfiguration
				.builder(BotaniaLootTables.GAIA_GUARDIAN_REWARD, 60, ConstantInt.of(6),
						0.2f, 40, ConstantInt.of(6),
						// this happens when you check the RNG in a for loop condition:
						BiasedToBottomInt.of(0, 2))
				.spawnedMobs(UniformInt.of(3, 4),
						MobSpawnData.entityWeight(EntityType.WITCH, 2)
								.allowEquipmentDrops()
								.build(),
						MobSpawnData.entityWeight(EntityType.ZOMBIE, 22)
								.effectsToApply(MobEffectToApply.effect(MobEffects.FIRE_RESISTANCE).build())
								.allowEquipmentDrops()
								.build(),
						MobSpawnData.entityWeight(EntityType.SKELETON, 21)
								.effectsToApply(MobEffectToApply.effect(MobEffects.FIRE_RESISTANCE).build())
								.allowEquipmentDrops()
								.build(),
						MobSpawnData.entityWeight(EntityType.WITHER_SKELETON, 3)
								.allowEquipmentDrops()
								.build(),
						MobSpawnData.entityWeight(BotaniaEntities.PIXIE, 24)
								// TODO: currently any properties except spawn count is ignored
								.count(BiasedToBottomInt.of(1, 5))
								.build()
				)
				.build());

		configs.put(GaiaFightConfiguration.HARD, GaiaFightConfiguration
				.builder(BotaniaLootTables.GAIA_GUARDIAN_REWARD_HARD, 45, ConstantInt.of(6),
						0.2f, 35, ConstantInt.of(7),
						BiasedToBottomInt.of(0, 5))
				.spawnMissiles(15, ConstantInt.of(4))
				.spawnedMobs(UniformInt.of(3, 4),
						MobSpawnData.entityWeight(EntityType.WITCH, 8)
								.allowEquipmentDrops()
								.build(),
						MobSpawnData.entityWeight(EntityType.ZOMBIE, 16)
								.effectsToApply(MobEffectToApply.effect(MobEffects.FIRE_RESISTANCE).build())
								.allowEquipmentDrops()
								.build(),
						MobSpawnData.entityWeight(EntityType.SKELETON, 21)
								.effectsToApply(MobEffectToApply.effect(MobEffects.FIRE_RESISTANCE).build())
								.allowEquipmentDrops()
								.build(),
						MobSpawnData.entityWeight(EntityType.WITHER_SKELETON, 3)
								.equipmentTable(BotaniaLootTables.GAIA_HARD_WITHER_SKELETON)
								.allowEquipmentDrops()
								.build(),
						MobSpawnData.entityWeight(BotaniaEntities.PIXIE, 24)
								.count(BiasedToBottomInt.of(1, 8))
								.build()
				)
				.build());
	}

	@Override
	public String getName() {
		return "Gaia fight configuration";
	}
}
