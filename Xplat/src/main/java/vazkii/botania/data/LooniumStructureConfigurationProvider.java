package vazkii.botania.data;

import com.mojang.serialization.JsonOps;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.configdata.LooniumMobAttributeModifier;
import vazkii.botania.api.configdata.LooniumMobEffectToApply;
import vazkii.botania.api.configdata.LooniumMobSpawnData;
import vazkii.botania.api.configdata.LooniumStructureConfiguration;
import vazkii.botania.common.block.flower.functional.LooniumBlockEntity;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class LooniumStructureConfigurationProvider implements DataProvider {

	private final PackOutput.PathProvider pathProvider;

	public LooniumStructureConfigurationProvider(PackOutput packOutput) {
		pathProvider = packOutput.createPathProvider(PackOutput.Target.DATA_PACK, "config/loonium");
	}

	@NotNull
	@Override
	public CompletableFuture<?> run(@NotNull CachedOutput cache) {
		Map<ResourceLocation, LooniumStructureConfiguration> configs = new HashMap<>();

		List<LooniumMobEffectToApply> effectsToApplyToCreepers = List.of(
				LooniumMobEffectToApply.effect(MobEffects.FIRE_RESISTANCE).duration(100).build(),
				LooniumMobEffectToApply.effect(MobEffects.REGENERATION).duration(100).build()
		);

		ResourceLocation defaultConfigId = LooniumStructureConfiguration.DEFAULT_CONFIG_ID;
		configs.put(defaultConfigId, LooniumStructureConfiguration.builder()
				.manaCost(LooniumBlockEntity.DEFAULT_COST)
				.maxNearbyMobs(LooniumBlockEntity.DEFAULT_MAX_NEARBY_MOBS)
				.boundingBoxType(StructureSpawnOverride.BoundingBoxType.PIECE)
				.spawnedMobs(
						// weights roughly based on original Loonium mob selection logic
						LooniumMobSpawnData.entityWeight(EntityType.ENDERMAN, 40).build(),
						getCreeperSpawnData(effectsToApplyToCreepers, 195, false),
						getCreeperSpawnData(effectsToApplyToCreepers, 1, true),
						LooniumMobSpawnData.entityWeight(EntityType.HUSK, 59).build(),
						LooniumMobSpawnData.entityWeight(EntityType.DROWNED, 106).build(),
						LooniumMobSpawnData.entityWeight(EntityType.ZOMBIE, 423).build(),
						LooniumMobSpawnData.entityWeight(EntityType.STRAY, 59).build(),
						LooniumMobSpawnData.entityWeight(EntityType.SKELETON, 529).build(),
						LooniumMobSpawnData.entityWeight(EntityType.CAVE_SPIDER, 59).build(),
						LooniumMobSpawnData.entityWeight(EntityType.SPIDER, 529).build()
				)
				.attributeModifiers(
						new LooniumMobAttributeModifier("Loonium Modififer Health",
								Attributes.MAX_HEALTH, 2, AttributeModifier.Operation.MULTIPLY_BASE),
						new LooniumMobAttributeModifier("Loonium Modififer Damage",
								Attributes.ATTACK_DAMAGE, 1.5, AttributeModifier.Operation.MULTIPLY_BASE)
				).effectsToApply(
						LooniumMobEffectToApply.effect(MobEffects.FIRE_RESISTANCE).build(),
						LooniumMobEffectToApply.effect(MobEffects.REGENERATION).build()
				)
				.build());

		configs.put(BuiltinStructures.OCEAN_RUIN_COLD.location(),
				LooniumStructureConfiguration.forParent(defaultConfigId)
						.boundingBoxType(StructureSpawnOverride.BoundingBoxType.STRUCTURE).build());
		configs.put(BuiltinStructures.OCEAN_RUIN_WARM.location(),
				LooniumStructureConfiguration.forParent(defaultConfigId)
						.boundingBoxType(StructureSpawnOverride.BoundingBoxType.STRUCTURE).build());
		configs.put(BuiltinStructures.PILLAGER_OUTPOST.location(),
				LooniumStructureConfiguration.forParent(defaultConfigId)
						.boundingBoxType(StructureSpawnOverride.BoundingBoxType.STRUCTURE).build());
		configs.put(BuiltinStructures.VILLAGE_DESERT.location(),
				LooniumStructureConfiguration.forParent(defaultConfigId)
						.boundingBoxType(StructureSpawnOverride.BoundingBoxType.STRUCTURE).build());
		configs.put(BuiltinStructures.VILLAGE_PLAINS.location(),
				LooniumStructureConfiguration.forParent(defaultConfigId)
						.boundingBoxType(StructureSpawnOverride.BoundingBoxType.STRUCTURE).build());
		configs.put(BuiltinStructures.VILLAGE_SAVANNA.location(),
				LooniumStructureConfiguration.forParent(defaultConfigId)
						.boundingBoxType(StructureSpawnOverride.BoundingBoxType.STRUCTURE).build());
		configs.put(BuiltinStructures.VILLAGE_SNOWY.location(),
				LooniumStructureConfiguration.forParent(defaultConfigId)
						.boundingBoxType(StructureSpawnOverride.BoundingBoxType.STRUCTURE).build());
		configs.put(BuiltinStructures.VILLAGE_TAIGA.location(),
				LooniumStructureConfiguration.forParent(defaultConfigId)
						.boundingBoxType(StructureSpawnOverride.BoundingBoxType.STRUCTURE).build());

		var output = new ArrayList<CompletableFuture<?>>(configs.size());
		for (var e : configs.entrySet()) {
			Path path = pathProvider.json(e.getKey());
			var config = e.getValue();
			var jsonTree = LooniumStructureConfiguration.CODEC.encodeStart(JsonOps.INSTANCE, config)
					.getOrThrow(false, BotaniaAPI.LOGGER::error);
			output.add(DataProvider.saveStable(cache, jsonTree, path));
		}
		return CompletableFuture.allOf(output.toArray(CompletableFuture[]::new));
	}

	private static LooniumMobSpawnData getCreeperSpawnData(
			List<LooniumMobEffectToApply> effectsToApplyToCreepers, int weight, boolean charged) {
		CompoundTag chargedCreeperNbt;
		if (charged) {
			chargedCreeperNbt = new CompoundTag();
			chargedCreeperNbt.putBoolean("powered", true);
		} else {
			chargedCreeperNbt = null;
		}

		return LooniumMobSpawnData.entityWeight(EntityType.CREEPER, weight)
				.setNbt(chargedCreeperNbt)
				.setEffectsToApply(effectsToApplyToCreepers).build();
	}

	@NotNull
	@Override
	public String getName() {
		return "Loonium structure configuration";
	}
}
