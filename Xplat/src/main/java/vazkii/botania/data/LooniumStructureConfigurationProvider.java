package vazkii.botania.data;

import com.mojang.serialization.JsonOps;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;

import org.jetbrains.annotations.NotNull;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.configdata.LooniumStructureConfiguration;
import vazkii.botania.common.block.flower.functional.LooniumBlockEntity;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static vazkii.botania.data.LooniumStructureLootProvider.getStructureId;

public class LooniumStructureConfigurationProvider implements DataProvider {

	private final PackOutput.PathProvider pathProvider;

	public LooniumStructureConfigurationProvider(PackOutput packOutput) {
		pathProvider = packOutput.createPathProvider(PackOutput.Target.DATA_PACK, "config/loonium");
	}

	@NotNull
	@Override
	public CompletableFuture<?> run(@NotNull CachedOutput cache) {
		Map<ResourceLocation, LooniumStructureConfiguration> configs = new HashMap<>();

		List<LooniumStructureConfiguration.MobEffectToApply> effectsToApplyToCreepers = List.of(
				new LooniumStructureConfiguration.MobEffectToApply(MobEffects.FIRE_RESISTANCE, 100),
				new LooniumStructureConfiguration.MobEffectToApply(MobEffects.REGENERATION, 100)
		);
		CompoundTag chargedCreeperNbt = new CompoundTag();
		chargedCreeperNbt.putBoolean("powered", true);

		ResourceLocation defaultConfigId = LooniumStructureConfiguration.DEFAULT_CONFIG_ID;
		configs.put(defaultConfigId, new LooniumStructureConfiguration(
				LooniumBlockEntity.DEFAULT_COST, LooniumBlockEntity.DEFAULT_MAX_NEARBY_MOBS,
				StructureSpawnOverride.BoundingBoxType.PIECE,
				WeightedRandomList.create(
						// weights roughly based on original Loonium mob selection logic
						new LooniumStructureConfiguration.MobSpawnData(EntityType.ENDERMAN, 40),
						new LooniumStructureConfiguration.MobSpawnData(EntityType.CREEPER, 195,
								effectsToApplyToCreepers),
						new LooniumStructureConfiguration.MobSpawnData(EntityType.CREEPER, 1,
								effectsToApplyToCreepers, null, chargedCreeperNbt),
						new LooniumStructureConfiguration.MobSpawnData(EntityType.HUSK, 59),
						new LooniumStructureConfiguration.MobSpawnData(EntityType.DROWNED, 106),
						new LooniumStructureConfiguration.MobSpawnData(EntityType.ZOMBIE, 423),
						new LooniumStructureConfiguration.MobSpawnData(EntityType.STRAY, 59),
						new LooniumStructureConfiguration.MobSpawnData(EntityType.SKELETON, 529),
						new LooniumStructureConfiguration.MobSpawnData(EntityType.CAVE_SPIDER, 59),
						new LooniumStructureConfiguration.MobSpawnData(EntityType.SPIDER, 529)
				),
				List.of(
						new LooniumStructureConfiguration.MobAttributeModifier("Loonium Modififer Health",
								Attributes.MAX_HEALTH, 2, AttributeModifier.Operation.MULTIPLY_BASE),
						new LooniumStructureConfiguration.MobAttributeModifier("Loonium Modififer Damage",
								Attributes.ATTACK_DAMAGE, 1.5, AttributeModifier.Operation.MULTIPLY_BASE)
				),
				List.of(
						new LooniumStructureConfiguration.MobEffectToApply(MobEffects.FIRE_RESISTANCE),
						new LooniumStructureConfiguration.MobEffectToApply(MobEffects.REGENERATION)
				)
		));

		configs.put(getStructureId(BuiltinStructures.OCEAN_RUIN_COLD),
				new LooniumStructureConfiguration(defaultConfigId, StructureSpawnOverride.BoundingBoxType.STRUCTURE));
		configs.put(getStructureId(BuiltinStructures.OCEAN_RUIN_WARM),
				new LooniumStructureConfiguration(defaultConfigId, StructureSpawnOverride.BoundingBoxType.STRUCTURE));
		configs.put(getStructureId(BuiltinStructures.PILLAGER_OUTPOST),
				new LooniumStructureConfiguration(defaultConfigId, StructureSpawnOverride.BoundingBoxType.STRUCTURE));
		configs.put(getStructureId(BuiltinStructures.VILLAGE_DESERT),
				new LooniumStructureConfiguration(defaultConfigId, StructureSpawnOverride.BoundingBoxType.STRUCTURE));
		configs.put(getStructureId(BuiltinStructures.VILLAGE_PLAINS),
				new LooniumStructureConfiguration(defaultConfigId, StructureSpawnOverride.BoundingBoxType.STRUCTURE));
		configs.put(getStructureId(BuiltinStructures.VILLAGE_SAVANNA),
				new LooniumStructureConfiguration(defaultConfigId, StructureSpawnOverride.BoundingBoxType.STRUCTURE));
		configs.put(getStructureId(BuiltinStructures.VILLAGE_SNOWY),
				new LooniumStructureConfiguration(defaultConfigId, StructureSpawnOverride.BoundingBoxType.STRUCTURE));
		configs.put(getStructureId(BuiltinStructures.VILLAGE_TAIGA),
				new LooniumStructureConfiguration(defaultConfigId, StructureSpawnOverride.BoundingBoxType.STRUCTURE));

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

	@NotNull
	@Override
	public String getName() {
		return "Loonium structure configuration";
	}
}
