package vazkii.botania.data;

import com.google.gson.JsonElement;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;
import net.minecraft.world.level.storage.loot.LootTable;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.configdata.LooniumStructureConfiguration;
import vazkii.botania.api.configdata.MobAttributeModifier;
import vazkii.botania.api.configdata.MobEffectToApply;
import vazkii.botania.api.configdata.MobSpawnData;
import vazkii.botania.common.config.ConfigDataManagerImpl;
import vazkii.botania.common.loot.BotaniaLootTables;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

public class LooniumStructureConfigurationProvider implements DataProvider {

	private final PackOutput.PathProvider pathProvider;
	private final CompletableFuture<HolderLookup.Provider> registryLookupFuture;

	public LooniumStructureConfigurationProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
		pathProvider = packOutput.createPathProvider(PackOutput.Target.DATA_PACK, ConfigDataManagerImpl.LOONIUM_CONFIG_PATH);
		this.registryLookupFuture = registryLookupFuture;
	}

	@Override
	public CompletableFuture<?> run(CachedOutput cache) {
		return registryLookupFuture.thenCompose(registryLookup -> this.run(cache, registryLookup));
	}

	private CompletableFuture<?> run(CachedOutput cache, HolderLookup.Provider registries) {
		Map<ResourceLocation, LooniumStructureConfiguration> configs = new HashMap<>();
		addConfigs(configs);

		var output = new ArrayList<CompletableFuture<?>>(configs.size());
		for (Map.Entry<ResourceLocation, LooniumStructureConfiguration> e : configs.entrySet()) {
			Path path = pathProvider.json(e.getKey());
			LooniumStructureConfiguration config = e.getValue();
			JsonElement jsonTree = LooniumStructureConfiguration.CODEC.encodeStart(JsonOps.INSTANCE, config)
					.getOrThrow();
			output.add(DataProvider.saveStable(cache, jsonTree, path));
		}
		return CompletableFuture.allOf(output.toArray(CompletableFuture<?>[]::new));
	}

	private void addConfigs(Map<ResourceLocation, LooniumStructureConfiguration> configs) {
		ResourceLocation defaultConfigId = LooniumStructureConfiguration.DEFAULT_CONFIG_ID;
		configs.put(defaultConfigId, getDefaultConfig());

		configs.put(BuiltinStructures.ANCIENT_CITY.location(), getConfigAncientCity(defaultConfigId));
		configs.put(BuiltinStructures.BASTION_REMNANT.location(), getConfigBastionRemnant(defaultConfigId));
		configs.put(BuiltinStructures.DESERT_PYRAMID.location(), getConfigDesertPyramid(defaultConfigId));
		configs.put(BuiltinStructures.END_CITY.location(), getConfigEndCity(defaultConfigId));
		configs.put(BuiltinStructures.FORTRESS.location(), getConfigFortress(defaultConfigId));
		configs.put(BuiltinStructures.JUNGLE_TEMPLE.location(), getConfigJungleTemple(defaultConfigId));
		configs.put(BuiltinStructures.OCEAN_MONUMENT.location(), getConfigOceanMonument(defaultConfigId));

		ResourceLocation oceanRuinId = botaniaRL("ocean_ruins");
		configs.put(oceanRuinId,
				LooniumStructureConfiguration.forParent(defaultConfigId)
						.boundingBoxType(StructureSpawnOverride.BoundingBoxType.STRUCTURE).build()
		);
		configs.put(BuiltinStructures.OCEAN_RUIN_COLD.location(), getConfigOceanRuinCold(oceanRuinId));
		configs.put(BuiltinStructures.OCEAN_RUIN_WARM.location(), getConfigOceanRuinWarm(oceanRuinId));

		configs.put(BuiltinStructures.PILLAGER_OUTPOST.location(), getConfigPillagerOutpost(defaultConfigId));
		configs.put(BuiltinStructures.RUINED_PORTAL_DESERT.location(), getConfigRuinedPortalDesert(defaultConfigId));
		configs.put(BuiltinStructures.RUINED_PORTAL_JUNGLE.location(), getConfigRuinedPortalJungle(defaultConfigId));
		configs.put(BuiltinStructures.RUINED_PORTAL_MOUNTAIN.location(), getConfigRuinedPortalMountain(defaultConfigId));
		configs.put(BuiltinStructures.RUINED_PORTAL_NETHER.location(), getConfigRuinedPortalNether(defaultConfigId));
		configs.put(BuiltinStructures.RUINED_PORTAL_OCEAN.location(), getConfigRuinedPortalOcean(defaultConfigId));
		configs.put(BuiltinStructures.RUINED_PORTAL_STANDARD.location(), getConfigRuinedPortalStandard(defaultConfigId));
		configs.put(BuiltinStructures.RUINED_PORTAL_SWAMP.location(), getConfigRuinedPortalSwamp(defaultConfigId));

		configs.put(BuiltinStructures.SHIPWRECK.location(), getConfigShipwreck(defaultConfigId));
		configs.put(BuiltinStructures.SHIPWRECK_BEACHED.location(),
				LooniumStructureConfiguration.forParent(BuiltinStructures.SHIPWRECK.location()).build());

		configs.put(BuiltinStructures.STRONGHOLD.location(), getConfigStronghold(defaultConfigId));
		configs.put(BuiltinStructures.TRAIL_RUINS.location(), getConfigTrailRuins(defaultConfigId));
		configs.put(BuiltinStructures.TRIAL_CHAMBERS.location(), getConfigTrialChamber(defaultConfigId));

		ResourceLocation villageId = botaniaRL("village");
		configs.put(villageId, LooniumStructureConfiguration.forParent(defaultConfigId)
				.boundingBoxType(StructureSpawnOverride.BoundingBoxType.STRUCTURE).build()
		);
		configs.put(BuiltinStructures.VILLAGE_DESERT.location(), getConfigVillageDesert(villageId));
		configs.put(BuiltinStructures.VILLAGE_PLAINS.location(), getConfigVillagePlains(villageId));
		configs.put(BuiltinStructures.VILLAGE_SAVANNA.location(), getConfigVillageSavanna(villageId));
		configs.put(BuiltinStructures.VILLAGE_SNOWY.location(), getConfigVillageSnowy(villageId));
		configs.put(BuiltinStructures.VILLAGE_TAIGA.location(), getConfigVillageTaiga(villageId));

		configs.put(BuiltinStructures.WOODLAND_MANSION.location(), getConfigWoodlandMansion(defaultConfigId));
	}

	public static LooniumStructureConfiguration getDefaultConfig() {
		return LooniumStructureConfiguration.builder()
				.manaCost(LooniumStructureConfiguration.DEFAULT_COST)
				.maxNearbyMobs(LooniumStructureConfiguration.DEFAULT_MAX_NEARBY_MOBS)
				.boundingBoxType(StructureSpawnOverride.BoundingBoxType.PIECE)
				.spawnedMobs(
						// weights roughly based on original Loonium mob selection logic
						MobSpawnData.entityWeight(EntityType.ENDERMAN, 40).build(),
						getCreeperSpawnData(195, false, getCreeperEffects(false)),
						getCreeperSpawnData(1, true, getCreeperEffects(false)),
						MobSpawnData.entityWeight(EntityType.HUSK, 59)
								.equipmentTable(BotaniaLootTables.LOONIUM_ZOMBIE_DEFAULT).build(),
						MobSpawnData.entityWeight(EntityType.DROWNED, 106)
								.equipmentTable(BotaniaLootTables.LOONIUM_DROWNED_DEFAULT).build(),
						MobSpawnData.entityWeight(EntityType.ZOMBIE, 423)
								.equipmentTable(BotaniaLootTables.LOONIUM_ZOMBIE_DEFAULT).build(),
						MobSpawnData.entityWeight(EntityType.STRAY, 59)
								.equipmentTable(BotaniaLootTables.LOONIUM_SKELETON_DEFAULT).build(),
						MobSpawnData.entityWeight(EntityType.SKELETON, 529)
								.equipmentTable(BotaniaLootTables.LOONIUM_SKELETON_DEFAULT).build(),
						MobSpawnData.entityWeight(EntityType.CAVE_SPIDER, 59).build(),
						MobSpawnData.entityWeight(EntityType.SPIDER, 529).build()
				)
				.attributeModifiers(
						new MobAttributeModifier(
								Attributes.MAX_HEALTH, 2, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
						new MobAttributeModifier(
								Attributes.ATTACK_DAMAGE, 1.5, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
				)
				.effectsToApply(getStandardEffects(false, true))
				.build();
	}

	public static LooniumStructureConfiguration getConfigAncientCity(ResourceLocation parentId) {
		return LooniumStructureConfiguration.forParent(parentId).spawnedMobs(
				MobSpawnData.entityWeight(EntityType.ENDERMAN, 30).build(),
				getCreeperSpawnData(99, false, getCreeperEffects(false)),
				getCreeperSpawnData(1, true, getCreeperEffects(false)),
				MobSpawnData.entityWeight(EntityType.HUSK, 40)
						.equipmentTable(BotaniaLootTables.LOONIUM_ZOMBIE_ANCIENT_CITY).build(),
				MobSpawnData.entityWeight(EntityType.DROWNED, 80)
						.equipmentTable(BotaniaLootTables.LOONIUM_DROWNED_ANCIENT_CITY).build(),
				MobSpawnData.entityWeight(EntityType.ZOMBIE, 410)
						.equipmentTable(BotaniaLootTables.LOONIUM_ZOMBIE_ANCIENT_CITY).build(),
				MobSpawnData.entityWeight(EntityType.STRAY, 60)
						.equipmentTable(BotaniaLootTables.LOONIUM_SKELETON_ANCIENT_CITY).build(),
				MobSpawnData.entityWeight(EntityType.BOGGED, 40)
						.equipmentTable(BotaniaLootTables.LOONIUM_SKELETON_ANCIENT_CITY).build(),
				MobSpawnData.entityWeight(EntityType.SKELETON, 400)
						.equipmentTable(BotaniaLootTables.LOONIUM_SKELETON_ANCIENT_CITY).build(),
				MobSpawnData.entityWeight(EntityType.CAVE_SPIDER, 100).build(),
				MobSpawnData.entityWeight(EntityType.SPIDER, 200).build()
		).build();
	}

	public static LooniumStructureConfiguration getConfigBastionRemnant(ResourceLocation parentId) {
		return LooniumStructureConfiguration.forParent(parentId).spawnedMobs(
				MobSpawnData.entityWeight(EntityType.ENDERMAN, 30).build(),
				getCreeperSpawnData(99, false, getCreeperEffects(false)),
				getCreeperSpawnData(1, true, getCreeperEffects(false)),
				getPiglinSpawnData(450, BotaniaLootTables.LOONIUM_PIGLIN_BASTION_REMNANT, false, false),
				MobSpawnData.entityWeight(EntityType.PIGLIN_BRUTE, 50)
						.equipmentTable(BotaniaLootTables.LOONIUM_PIGLIN_BRUTE_DEFAULT)
						.attributeModifiers(
								new MobAttributeModifier(
										Attributes.MAX_HEALTH, 1.5, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
								new MobAttributeModifier(
										Attributes.ATTACK_DAMAGE, 1.5, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
						)
						.build(),
				MobSpawnData.entityWeight(EntityType.HOGLIN, 300).spawnAsAdult().build()
		).build();
	}

	public static LooniumStructureConfiguration getConfigDesertPyramid(ResourceLocation parentId) {
		return LooniumStructureConfiguration.forParent(parentId).spawnedMobs(
				MobSpawnData.entityWeight(EntityType.ENDERMAN, 50).build(),
				getCreeperSpawnData(149, false, getCreeperEffects(false)),
				getCreeperSpawnData(1, true, getCreeperEffects(false)),
				MobSpawnData.entityWeight(EntityType.HUSK, 450)
						.equipmentTable(BotaniaLootTables.LOONIUM_ZOMBIE_DESERT_PYRAMID).build(),
				MobSpawnData.entityWeight(EntityType.ZOMBIE, 50)
						.equipmentTable(BotaniaLootTables.LOONIUM_ZOMBIE_DESERT_PYRAMID).build(),
				MobSpawnData.entityWeight(EntityType.SKELETON, 500)
						.equipmentTable(BotaniaLootTables.LOONIUM_SKELETON_DESERT_PYRAMID).build(),
				MobSpawnData.entityWeight(EntityType.CAVE_SPIDER, 40).build(),
				MobSpawnData.entityWeight(EntityType.SPIDER, 360).build()
		).build();
	}

	public static LooniumStructureConfiguration getConfigEndCity(ResourceLocation parentId) {
		MobEffectToApply[] creeperEffects = {
				MobEffectToApply.effect(MobEffects.FIRE_RESISTANCE).duration(100).build(),
				MobEffectToApply.effect(MobEffects.REGENERATION).duration(100).build(),
				MobEffectToApply.effect(MobEffects.SLOW_FALLING).duration(400).build()
		};
		return LooniumStructureConfiguration.forParent(parentId).spawnedMobs(
				MobSpawnData.entityWeight(EntityType.SHULKER, 100)
						.effectsToApply(getStandardEffects(true, true)).build(),
				MobSpawnData.entityWeight(EntityType.ENDERMAN, 300).build(),
				getCreeperSpawnData(99, false, creeperEffects),
				getCreeperSpawnData(1, true, creeperEffects),
				MobSpawnData.entityWeight(EntityType.ZOMBIE, 300)
						.equipmentTable(BotaniaLootTables.LOONIUM_ZOMBIE_END_CITY).build(),
				MobSpawnData.entityWeight(EntityType.SKELETON, 300)
						.equipmentTable(BotaniaLootTables.LOONIUM_SKELETON_END_CITY).build(),
				MobSpawnData.entityWeight(EntityType.SPIDER, 300).build()
		).effectsToApply(
				MobEffectToApply.effect(MobEffects.FIRE_RESISTANCE).build(),
				MobEffectToApply.effect(MobEffects.REGENERATION).build(),
				MobEffectToApply.effect(MobEffects.SLOW_FALLING).build()
		).build();
	}

	public static LooniumStructureConfiguration getConfigFortress(ResourceLocation parentId) {
		return LooniumStructureConfiguration.forParent(parentId).spawnedMobs(
				MobSpawnData.entityWeight(EntityType.ENDERMAN, 30).build(),
				getCreeperSpawnData(99, false, getCreeperEffects(false)),
				getCreeperSpawnData(1, true, getCreeperEffects(false)),
				MobSpawnData.entityWeight(EntityType.BLAZE, 300)
						.effectsToApply(getStandardEffects(false, false)).build(),
				MobSpawnData.entityWeight(EntityType.WITHER_SKELETON, 450)
						.equipmentTable(BotaniaLootTables.LOONIUM_SKELETON_FORTRESS)
						.effectsToApply(getStandardEffects(false, false)).build(),
				MobSpawnData.entityWeight(EntityType.SKELETON, 50)
						.equipmentTable(BotaniaLootTables.LOONIUM_SKELETON_FORTRESS).build(),
				MobSpawnData.entityWeight(EntityType.ZOMBIFIED_PIGLIN, 400)
						.equipmentTable(BotaniaLootTables.LOONIUM_ZOMBIE_FORTRESS)
						.effectsToApply(getStandardEffects(false, false)).build()
		).build();
	}

	public static LooniumStructureConfiguration getConfigJungleTemple(ResourceLocation parentId) {
		return LooniumStructureConfiguration.forParent(parentId).spawnedMobs(
				MobSpawnData.entityWeight(EntityType.ENDERMAN, 30).build(),
				getCreeperSpawnData(149, false, getCreeperEffects(false)),
				getCreeperSpawnData(1, true, getCreeperEffects(false)),
				MobSpawnData.entityWeight(EntityType.DROWNED, 40)
						.equipmentTable(BotaniaLootTables.LOONIUM_DROWNED_JUNGLE_TEMPLE).build(),
				MobSpawnData.entityWeight(EntityType.ZOMBIE, 360)
						.equipmentTable(BotaniaLootTables.LOONIUM_ZOMBIE_JUNGLE_TEMPLE).build(),
				MobSpawnData.entityWeight(EntityType.BOGGED, 50)
						.equipmentTable(BotaniaLootTables.LOONIUM_SKELETON_JUNGLE_TEMPLE).build(),
				MobSpawnData.entityWeight(EntityType.SKELETON, 450)
						.equipmentTable(BotaniaLootTables.LOONIUM_SKELETON_JUNGLE_TEMPLE).build(),
				MobSpawnData.entityWeight(EntityType.CAVE_SPIDER, 300).build(),
				MobSpawnData.entityWeight(EntityType.SPIDER, 300).build()
		).build();
	}

	public static LooniumStructureConfiguration getConfigOceanMonument(ResourceLocation parentId) {
		MobEffectToApply[] standardEffectsInWater = getStandardEffects(true, true);
		return LooniumStructureConfiguration.forParent(parentId).spawnedMobs(
				MobSpawnData.entityWeight(EntityType.GUARDIAN, 200).build(),
				getCreeperSpawnData(199, false, getCreeperEffects(true)),
				getCreeperSpawnData(1, true, getCreeperEffects(true)),
				MobSpawnData.entityWeight(EntityType.DROWNED, 540)
						.equipmentTable(BotaniaLootTables.LOONIUM_DROWNED_MONUMENT).build(),
				MobSpawnData.entityWeight(EntityType.ZOMBIE, 60)
						.equipmentTable(BotaniaLootTables.LOONIUM_ZOMBIE_MONUMENT).build(),
				MobSpawnData.entityWeight(EntityType.STRAY, 40)
						.equipmentTable(BotaniaLootTables.LOONIUM_SKELETON_MONUMENT).build(),
				MobSpawnData.entityWeight(EntityType.BOGGED, 40)
						.equipmentTable(BotaniaLootTables.LOONIUM_SKELETON_MONUMENT).build(),
				MobSpawnData.entityWeight(EntityType.SKELETON, 320)
						.equipmentTable(BotaniaLootTables.LOONIUM_SKELETON_MONUMENT).build(),
				MobSpawnData.entityWeight(EntityType.CAVE_SPIDER, 30)
						.effectsToApply(standardEffectsInWater).build(),
				MobSpawnData.entityWeight(EntityType.SPIDER, 270)
						.effectsToApply(standardEffectsInWater).build()
		).build();
	}

	public static LooniumStructureConfiguration getConfigOceanRuinCold(ResourceLocation parentId) {
		MobEffectToApply[] standardEffectsInWater = getStandardEffects(true, true);
		return LooniumStructureConfiguration.forParent(parentId).spawnedMobs(
				getCreeperSpawnData(199, false, getCreeperEffects(true)),
				getCreeperSpawnData(1, true, getCreeperEffects(true)),
				MobSpawnData.entityWeight(EntityType.DROWNED, 540)
						.equipmentTable(BotaniaLootTables.LOONIUM_DROWNED_DEFAULT).build(),
				MobSpawnData.entityWeight(EntityType.ZOMBIE, 60)
						.equipmentTable(BotaniaLootTables.LOONIUM_ZOMBIE_DEFAULT).build(),
				MobSpawnData.entityWeight(EntityType.STRAY, 40)
						.equipmentTable(BotaniaLootTables.LOONIUM_SKELETON_DEFAULT).build(),
				MobSpawnData.entityWeight(EntityType.SKELETON, 360)
						.equipmentTable(BotaniaLootTables.LOONIUM_SKELETON_DEFAULT).build(),
				MobSpawnData.entityWeight(EntityType.CAVE_SPIDER, 30)
						.effectsToApply(standardEffectsInWater).build(),
				MobSpawnData.entityWeight(EntityType.SPIDER, 270)
						.effectsToApply(standardEffectsInWater).build()
		).build();
	}

	public static LooniumStructureConfiguration getConfigOceanRuinWarm(ResourceLocation parentId) {
		MobEffectToApply[] standardEffectsInWater = getStandardEffects(true, true);
		return LooniumStructureConfiguration.forParent(parentId).spawnedMobs(
				getCreeperSpawnData(199, false, getCreeperEffects(true)),
				getCreeperSpawnData(1, true, getCreeperEffects(true)),
				MobSpawnData.entityWeight(EntityType.DROWNED, 540)
						.equipmentTable(BotaniaLootTables.LOONIUM_DROWNED_DEFAULT).build(),
				MobSpawnData.entityWeight(EntityType.ZOMBIE, 60)
						.equipmentTable(BotaniaLootTables.LOONIUM_ZOMBIE_DEFAULT).build(),
				MobSpawnData.entityWeight(EntityType.BOGGED, 40)
						.equipmentTable(BotaniaLootTables.LOONIUM_SKELETON_DEFAULT).build(),
				MobSpawnData.entityWeight(EntityType.SKELETON, 360)
						.equipmentTable(BotaniaLootTables.LOONIUM_SKELETON_DEFAULT).build(),
				MobSpawnData.entityWeight(EntityType.CAVE_SPIDER, 30)
						.effectsToApply(standardEffectsInWater).build(),
				MobSpawnData.entityWeight(EntityType.SPIDER, 270)
						.effectsToApply(standardEffectsInWater).build()
		).build();
	}

	public static LooniumStructureConfiguration getConfigPillagerOutpost(ResourceLocation parentId) {
		return LooniumStructureConfiguration.forParent(parentId)
				.boundingBoxType(StructureSpawnOverride.BoundingBoxType.STRUCTURE).spawnedMobs(
						MobSpawnData.entityWeight(EntityType.ENDERMAN, 40).build(),
						getCreeperSpawnData(199, false, getCreeperEffects(false)),
						getCreeperSpawnData(1, true, getCreeperEffects(false)),
						MobSpawnData.entityWeight(EntityType.PILLAGER, 900)
								.equipmentTable(BotaniaLootTables.LOONIUM_PILLAGER_DEFAULT).build(),
						MobSpawnData.entityWeight(EntityType.VINDICATOR, 175)
								.equipmentTable(BotaniaLootTables.LOONIUM_VINDICATOR_DEFAULT).build(),
						MobSpawnData.entityWeight(EntityType.EVOKER, 25).build(),
						MobSpawnData.entityWeight(EntityType.SKELETON, 200)
								.equipmentTable(BotaniaLootTables.LOONIUM_SKELETON_OUTPOST).build(),
						MobSpawnData.entityWeight(EntityType.ZOMBIE, 200)
								.equipmentTable(BotaniaLootTables.LOONIUM_ZOMBIE_OUTPOST).build(),
						MobSpawnData.entityWeight(EntityType.SPIDER, 200).build()
				).build();
	}

	public static LooniumStructureConfiguration getConfigRuinedPortalDesert(ResourceLocation parentId) {
		return LooniumStructureConfiguration.forParent(parentId).spawnedMobs(
				MobSpawnData.entityWeight(EntityType.ZOGLIN, 25)
						.effectsToApply(getStandardEffects(false, false)).build(),
				getPiglinSpawnData(50, BotaniaLootTables.LOONIUM_PIGLIN_PORTAL, false, true),
				MobSpawnData.entityWeight(EntityType.ZOMBIFIED_PIGLIN, 50)
						.equipmentTable(BotaniaLootTables.LOONIUM_ZOMBIE_PORTAL).build(),
				MobSpawnData.entityWeight(EntityType.ENDERMAN, 50).build(),
				getCreeperSpawnData(149, false, getCreeperEffects(false)),
				getCreeperSpawnData(1, true, getCreeperEffects(false)),
				MobSpawnData.entityWeight(EntityType.HUSK, 450)
						.equipmentTable(BotaniaLootTables.LOONIUM_ZOMBIE_PORTAL).build(),
				MobSpawnData.entityWeight(EntityType.ZOMBIE, 50)
						.equipmentTable(BotaniaLootTables.LOONIUM_ZOMBIE_PORTAL).build(),
				MobSpawnData.entityWeight(EntityType.SKELETON, 450)
						.equipmentTable(BotaniaLootTables.LOONIUM_SKELETON_PORTAL).build(),
				MobSpawnData.entityWeight(EntityType.SPIDER, 360).build()
		).build();
	}

	public static LooniumStructureConfiguration getConfigRuinedPortalJungle(ResourceLocation parentId) {
		return LooniumStructureConfiguration.forParent(parentId).spawnedMobs(
				MobSpawnData.entityWeight(EntityType.ZOGLIN, 25)
						.effectsToApply(getStandardEffects(false, false)).build(),
				getPiglinSpawnData(50, BotaniaLootTables.LOONIUM_PIGLIN_PORTAL, false, true),
				MobSpawnData.entityWeight(EntityType.ZOMBIFIED_PIGLIN, 50)
						.equipmentTable(BotaniaLootTables.LOONIUM_ZOMBIE_PORTAL).build(),
				MobSpawnData.entityWeight(EntityType.ENDERMAN, 30).build(),
				getCreeperSpawnData(149, false, getCreeperEffects(false)),
				getCreeperSpawnData(1, true, getCreeperEffects(false)),
				MobSpawnData.entityWeight(EntityType.DROWNED, 40)
						.equipmentTable(BotaniaLootTables.LOONIUM_DROWNED_PORTAL).build(),
				MobSpawnData.entityWeight(EntityType.ZOMBIE, 360)
						.equipmentTable(BotaniaLootTables.LOONIUM_ZOMBIE_PORTAL).build(),
				MobSpawnData.entityWeight(EntityType.BOGGED, 50)
						.equipmentTable(BotaniaLootTables.LOONIUM_SKELETON_PORTAL).build(),
				MobSpawnData.entityWeight(EntityType.SKELETON, 450)
						.equipmentTable(BotaniaLootTables.LOONIUM_SKELETON_PORTAL).build(),
				MobSpawnData.entityWeight(EntityType.CAVE_SPIDER, 250).build(),
				MobSpawnData.entityWeight(EntityType.SPIDER, 50).build()
		).build();
	}

	public static LooniumStructureConfiguration getConfigRuinedPortalMountain(ResourceLocation parentId) {
		return LooniumStructureConfiguration.forParent(parentId).spawnedMobs(
				MobSpawnData.entityWeight(EntityType.ZOGLIN, 25)
						.effectsToApply(getStandardEffects(false, false)).build(),
				getPiglinSpawnData(50, BotaniaLootTables.LOONIUM_PIGLIN_PORTAL, false, true),
				MobSpawnData.entityWeight(EntityType.ZOMBIFIED_PIGLIN, 50)
						.equipmentTable(BotaniaLootTables.LOONIUM_ZOMBIE_PORTAL).build(),
				MobSpawnData.entityWeight(EntityType.ENDERMAN, 40).build(),
				getCreeperSpawnData(195, false, getCreeperEffects(false)),
				getCreeperSpawnData(1, true, getCreeperEffects(false)),
				MobSpawnData.entityWeight(EntityType.ZOMBIE, 529)
						.equipmentTable(BotaniaLootTables.LOONIUM_ZOMBIE_PORTAL).build(),
				MobSpawnData.entityWeight(EntityType.STRAY, 59)
						.equipmentTable(BotaniaLootTables.LOONIUM_SKELETON_PORTAL).build(),
				MobSpawnData.entityWeight(EntityType.SKELETON, 529)
						.equipmentTable(BotaniaLootTables.LOONIUM_SKELETON_PORTAL).build(),
				MobSpawnData.entityWeight(EntityType.SILVERFISH, 59).build(),
				MobSpawnData.entityWeight(EntityType.SPIDER, 529).build()
		).build();
	}

	public static LooniumStructureConfiguration getConfigRuinedPortalNether(ResourceLocation parentId) {
		return LooniumStructureConfiguration.forParent(parentId).spawnedMobs(
				MobSpawnData.entityWeight(EntityType.ZOGLIN, 125)
						.effectsToApply(getStandardEffects(false, false)).build(),
				getPiglinSpawnData(500, BotaniaLootTables.LOONIUM_PIGLIN_PORTAL, false, false),
				MobSpawnData.entityWeight(EntityType.ZOMBIFIED_PIGLIN, 450)
						.equipmentTable(BotaniaLootTables.LOONIUM_ZOMBIE_PORTAL).build(),
				MobSpawnData.entityWeight(EntityType.ENDERMAN, 40).build(),
				getCreeperSpawnData(195, false, getCreeperEffects(false)),
				getCreeperSpawnData(1, true, getCreeperEffects(false)),
				MobSpawnData.entityWeight(EntityType.ZOMBIE, 50)
						.equipmentTable(BotaniaLootTables.LOONIUM_ZOMBIE_PORTAL).build(),
				MobSpawnData.entityWeight(EntityType.SKELETON, 200)
						.equipmentTable(BotaniaLootTables.LOONIUM_SKELETON_PORTAL).build(),
				MobSpawnData.entityWeight(EntityType.CAVE_SPIDER, 10).build(),
				MobSpawnData.entityWeight(EntityType.SPIDER, 90).build()
		).build();
	}

	public static LooniumStructureConfiguration getConfigRuinedPortalOcean(ResourceLocation parentId) {
		MobEffectToApply[] standardEffectsInWater = getStandardEffects(true, true);
		return LooniumStructureConfiguration.forParent(parentId).spawnedMobs(
				MobSpawnData.entityWeight(EntityType.ZOGLIN, 25)
						.effectsToApply(getStandardEffects(false, false)).build(),
				getPiglinSpawnData(50, BotaniaLootTables.LOONIUM_PIGLIN_PORTAL, true, true),
				MobSpawnData.entityWeight(EntityType.ZOMBIFIED_PIGLIN, 50)
						.equipmentTable(BotaniaLootTables.LOONIUM_ZOMBIE_PORTAL).build(),
				getCreeperSpawnData(199, false, getCreeperEffects(true)),
				getCreeperSpawnData(1, true, getCreeperEffects(true)),
				MobSpawnData.entityWeight(EntityType.DROWNED, 540)
						.equipmentTable(BotaniaLootTables.LOONIUM_DROWNED_PORTAL).build(),
				MobSpawnData.entityWeight(EntityType.ZOMBIE, 60)
						.equipmentTable(BotaniaLootTables.LOONIUM_ZOMBIE_PORTAL).build(),
				MobSpawnData.entityWeight(EntityType.BOGGED, 40)
						.equipmentTable(BotaniaLootTables.LOONIUM_SKELETON_PORTAL).build(),
				MobSpawnData.entityWeight(EntityType.SKELETON, 360)
						.equipmentTable(BotaniaLootTables.LOONIUM_SKELETON_PORTAL).build(),
				MobSpawnData.entityWeight(EntityType.CAVE_SPIDER, 30)
						.effectsToApply(standardEffectsInWater).build(),
				MobSpawnData.entityWeight(EntityType.SPIDER, 270)
						.effectsToApply(standardEffectsInWater).build()
		).build();
	}

	public static LooniumStructureConfiguration getConfigRuinedPortalStandard(ResourceLocation parentId) {
		return LooniumStructureConfiguration.forParent(parentId).spawnedMobs(
				MobSpawnData.entityWeight(EntityType.ZOGLIN, 25)
						.effectsToApply(getStandardEffects(false, false)).build(),
				getPiglinSpawnData(50, BotaniaLootTables.LOONIUM_PIGLIN_PORTAL, false, true),
				MobSpawnData.entityWeight(EntityType.ZOMBIFIED_PIGLIN, 50)
						.equipmentTable(BotaniaLootTables.LOONIUM_ZOMBIE_PORTAL).build(),
				MobSpawnData.entityWeight(EntityType.ENDERMAN, 40).build(),
				getCreeperSpawnData(195, false, getCreeperEffects(false)),
				getCreeperSpawnData(1, true, getCreeperEffects(false)),
				MobSpawnData.entityWeight(EntityType.HUSK, 59)
						.equipmentTable(BotaniaLootTables.LOONIUM_ZOMBIE_PORTAL).build(),
				MobSpawnData.entityWeight(EntityType.DROWNED, 106)
						.equipmentTable(BotaniaLootTables.LOONIUM_DROWNED_PORTAL).build(),
				MobSpawnData.entityWeight(EntityType.ZOMBIE, 423)
						.equipmentTable(BotaniaLootTables.LOONIUM_ZOMBIE_PORTAL).build(),
				MobSpawnData.entityWeight(EntityType.STRAY, 59)
						.equipmentTable(BotaniaLootTables.LOONIUM_SKELETON_PORTAL).build(),
				MobSpawnData.entityWeight(EntityType.SKELETON, 529)
						.equipmentTable(BotaniaLootTables.LOONIUM_SKELETON_PORTAL).build(),
				MobSpawnData.entityWeight(EntityType.CAVE_SPIDER, 59).build(),
				MobSpawnData.entityWeight(EntityType.SPIDER, 529).build()
		).build();
	}

	public static LooniumStructureConfiguration getConfigRuinedPortalSwamp(ResourceLocation parentId) {
		return LooniumStructureConfiguration.forParent(parentId).spawnedMobs(
				MobSpawnData.entityWeight(EntityType.ZOGLIN, 25)
						.effectsToApply(getStandardEffects(false, false)).build(),
				getPiglinSpawnData(50, BotaniaLootTables.LOONIUM_PIGLIN_PORTAL, false, true),
				MobSpawnData.entityWeight(EntityType.ZOMBIFIED_PIGLIN, 50)
						.equipmentTable(BotaniaLootTables.LOONIUM_ZOMBIE_PORTAL).build(),
				MobSpawnData.entityWeight(EntityType.ENDERMAN, 30).build(),
				getCreeperSpawnData(149, false, getCreeperEffects(false)),
				getCreeperSpawnData(1, true, getCreeperEffects(false)),
				MobSpawnData.entityWeight(EntityType.DROWNED, 40)
						.equipmentTable(BotaniaLootTables.LOONIUM_DROWNED_PORTAL).build(),
				MobSpawnData.entityWeight(EntityType.ZOMBIE, 360)
						.equipmentTable(BotaniaLootTables.LOONIUM_ZOMBIE_PORTAL).build(),
				MobSpawnData.entityWeight(EntityType.BOGGED, 400)
						.equipmentTable(BotaniaLootTables.LOONIUM_SKELETON_PORTAL).build(),
				MobSpawnData.entityWeight(EntityType.SKELETON, 100)
						.equipmentTable(BotaniaLootTables.LOONIUM_SKELETON_PORTAL).build(),
				MobSpawnData.entityWeight(EntityType.CAVE_SPIDER, 50).build(),
				MobSpawnData.entityWeight(EntityType.SPIDER, 250).build()
		).build();
	}

	public static LooniumStructureConfiguration getConfigShipwreck(ResourceLocation parentId) {
		MobEffectToApply[] standardEffectsInWater = getStandardEffects(true, true);
		return LooniumStructureConfiguration.forParent(parentId).spawnedMobs(
				getCreeperSpawnData(199, false, getCreeperEffects(true)),
				getCreeperSpawnData(1, true, getCreeperEffects(true)),
				MobSpawnData.entityWeight(EntityType.DROWNED, 540)
						.equipmentTable(BotaniaLootTables.LOONIUM_DROWNED_SHIPWRECK).build(),
				MobSpawnData.entityWeight(EntityType.ZOMBIE, 60)
						.equipmentTable(BotaniaLootTables.LOONIUM_ZOMBIE_SHIPWRECK).build(),
				MobSpawnData.entityWeight(EntityType.STRAY, 40)
						.equipmentTable(BotaniaLootTables.LOONIUM_SKELETON_SHIPWRECK).build(),
				MobSpawnData.entityWeight(EntityType.BOGGED, 40)
						.equipmentTable(BotaniaLootTables.LOONIUM_SKELETON_SHIPWRECK).build(),
				MobSpawnData.entityWeight(EntityType.SKELETON, 320)
						.equipmentTable(BotaniaLootTables.LOONIUM_SKELETON_SHIPWRECK).build(),
				MobSpawnData.entityWeight(EntityType.CAVE_SPIDER, 30)
						.effectsToApply(standardEffectsInWater).build(),
				MobSpawnData.entityWeight(EntityType.SPIDER, 270)
						.effectsToApply(standardEffectsInWater).build()
		).build();
	}

	public static LooniumStructureConfiguration getConfigStronghold(ResourceLocation parentId) {
		return LooniumStructureConfiguration.forParent(parentId).spawnedMobs(
				MobSpawnData.entityWeight(EntityType.ENDERMAN, 80).build(),
				getCreeperSpawnData(149, false, getCreeperEffects(false)),
				getCreeperSpawnData(1, true, getCreeperEffects(false)),
				MobSpawnData.entityWeight(EntityType.HUSK, 50)
						.equipmentTable(BotaniaLootTables.LOONIUM_ZOMBIE_STRONGHOLD).build(),
				MobSpawnData.entityWeight(EntityType.DROWNED, 50)
						.equipmentTable(BotaniaLootTables.LOONIUM_DROWNED_STRONGHOLD).build(),
				MobSpawnData.entityWeight(EntityType.ZOMBIE, 400)
						.equipmentTable(BotaniaLootTables.LOONIUM_ZOMBIE_STRONGHOLD).build(),
				MobSpawnData.entityWeight(EntityType.STRAY, 50)
						.equipmentTable(BotaniaLootTables.LOONIUM_SKELETON_STRONGHOLD).build(),
				MobSpawnData.entityWeight(EntityType.BOGGED, 50)
						.equipmentTable(BotaniaLootTables.LOONIUM_SKELETON_STRONGHOLD).build(),
				MobSpawnData.entityWeight(EntityType.SKELETON, 400)
						.equipmentTable(BotaniaLootTables.LOONIUM_SKELETON_STRONGHOLD).build(),
				MobSpawnData.entityWeight(EntityType.CAVE_SPIDER, 100).build(),
				MobSpawnData.entityWeight(EntityType.SILVERFISH, 100).build(),
				MobSpawnData.entityWeight(EntityType.SPIDER, 400).build()
		).build();
	}

	public static LooniumStructureConfiguration getConfigTrailRuins(ResourceLocation parentId) {
		return LooniumStructureConfiguration.forParent(parentId).spawnedMobs(
				MobSpawnData.entityWeight(EntityType.ENDERMAN, 40).build(),
				getCreeperSpawnData(195, false, getCreeperEffects(false)),
				getCreeperSpawnData(1, true, getCreeperEffects(false)),
				MobSpawnData.entityWeight(EntityType.HUSK, 59)
						.equipmentTable(BotaniaLootTables.LOONIUM_ZOMBIE_TRAIL_RUINS).build(),
				MobSpawnData.entityWeight(EntityType.DROWNED, 106)
						.equipmentTable(BotaniaLootTables.LOONIUM_DROWNED_TRAIL_RUINS).build(),
				MobSpawnData.entityWeight(EntityType.ZOMBIE, 423)
						.equipmentTable(BotaniaLootTables.LOONIUM_ZOMBIE_TRAIL_RUINS).build(),
				MobSpawnData.entityWeight(EntityType.STRAY, 49)
						.equipmentTable(BotaniaLootTables.LOONIUM_SKELETON_TRAIL_RUINS).build(),
				MobSpawnData.entityWeight(EntityType.BOGGED, 49)
						.equipmentTable(BotaniaLootTables.LOONIUM_SKELETON_TRAIL_RUINS).build(),
				MobSpawnData.entityWeight(EntityType.SKELETON, 509)
						.equipmentTable(BotaniaLootTables.LOONIUM_SKELETON_TRAIL_RUINS).build(),
				MobSpawnData.entityWeight(EntityType.CAVE_SPIDER, 59).build(),
				MobSpawnData.entityWeight(EntityType.SPIDER, 529).build()
		).build();
	}

	public static LooniumStructureConfiguration getConfigTrialChamber(ResourceLocation parentId) {
		return LooniumStructureConfiguration.forParent(parentId).spawnedMobs(
				MobSpawnData.entityWeight(EntityType.ENDERMAN, 40).build(),
				getCreeperSpawnData(195, false, getCreeperEffects(false)),
				getCreeperSpawnData(1, true, getCreeperEffects(false)),
				MobSpawnData.entityWeight(EntityType.HUSK, 106)
						.equipmentTable(BotaniaLootTables.LOONIUM_ZOMBIE_TRIAL_CHAMBER).build(),
				MobSpawnData.entityWeight(EntityType.DROWNED, 59)
						.equipmentTable(BotaniaLootTables.LOONIUM_DROWNED_TRIAL_CHAMBER).build(),
				MobSpawnData.entityWeight(EntityType.ZOMBIE, 423)
						.equipmentTable(BotaniaLootTables.LOONIUM_ZOMBIE_TRIAL_CHAMBER).build(),
				MobSpawnData.entityWeight(EntityType.STRAY, 49)
						.equipmentTable(BotaniaLootTables.LOONIUM_SKELETON_TRIAL_CHAMBER).build(),
				MobSpawnData.entityWeight(EntityType.BOGGED, 49)
						.equipmentTable(BotaniaLootTables.LOONIUM_SKELETON_TRIAL_CHAMBER).build(),
				MobSpawnData.entityWeight(EntityType.SKELETON, 509)
						.equipmentTable(BotaniaLootTables.LOONIUM_SKELETON_TRIAL_CHAMBER).build(),
				MobSpawnData.entityWeight(EntityType.CAVE_SPIDER, 59).build(),
				MobSpawnData.entityWeight(EntityType.SPIDER, 529).build(),
				MobSpawnData.entityWeight(EntityType.BREEZE, 200).build()
		).build();
	}

	public static LooniumStructureConfiguration getConfigVillageDesert(ResourceLocation parentId) {
		return LooniumStructureConfiguration.forParent(parentId).spawnedMobs(
				MobSpawnData.entityWeight(EntityType.ENDERMAN, 40).build(),
				getCreeperSpawnData(195, false, getCreeperEffects(false)),
				getCreeperSpawnData(1, true, getCreeperEffects(false)),
				MobSpawnData.entityWeight(EntityType.HUSK, 423)
						.equipmentTable(BotaniaLootTables.LOONIUM_ZOMBIE_DEFAULT).build(),
				MobSpawnData.entityWeight(EntityType.ZOMBIE, 59)
						.equipmentTable(BotaniaLootTables.LOONIUM_ZOMBIE_DEFAULT).build(),
				MobSpawnData.entityWeight(EntityType.ZOMBIE_VILLAGER, 106)
						.equipmentTable(BotaniaLootTables.LOONIUM_ZOMBIE_VILLAGER).build(),
				MobSpawnData.entityWeight(EntityType.SKELETON, 600)
						.equipmentTable(BotaniaLootTables.LOONIUM_SKELETON_DEFAULT).build(),
				MobSpawnData.entityWeight(EntityType.CAVE_SPIDER, 59).build(),
				MobSpawnData.entityWeight(EntityType.SPIDER, 529).build()
		).build();
	}

	public static LooniumStructureConfiguration getConfigVillagePlains(ResourceLocation parentId) {
		return LooniumStructureConfiguration.forParent(parentId).spawnedMobs(
				MobSpawnData.entityWeight(EntityType.ENDERMAN, 40).build(),
				getCreeperSpawnData(195, false, getCreeperEffects(false)),
				getCreeperSpawnData(1, true, getCreeperEffects(false)),
				MobSpawnData.entityWeight(EntityType.DROWNED, 59)
						.equipmentTable(BotaniaLootTables.LOONIUM_DROWNED_DEFAULT).build(),
				MobSpawnData.entityWeight(EntityType.ZOMBIE, 423)
						.equipmentTable(BotaniaLootTables.LOONIUM_ZOMBIE_DEFAULT).build(),
				MobSpawnData.entityWeight(EntityType.ZOMBIE_VILLAGER, 106)
						.equipmentTable(BotaniaLootTables.LOONIUM_ZOMBIE_VILLAGER).build(),
				MobSpawnData.entityWeight(EntityType.SKELETON, 600)
						.equipmentTable(BotaniaLootTables.LOONIUM_SKELETON_DEFAULT).build(),
				MobSpawnData.entityWeight(EntityType.CAVE_SPIDER, 59).build(),
				MobSpawnData.entityWeight(EntityType.SPIDER, 529).build()
		).build();
	}

	public static LooniumStructureConfiguration getConfigVillageSavanna(ResourceLocation parentId) {
		return LooniumStructureConfiguration.forParent(parentId).spawnedMobs(
				MobSpawnData.entityWeight(EntityType.ENDERMAN, 40).build(),
				getCreeperSpawnData(195, false, getCreeperEffects(false)),
				getCreeperSpawnData(1, true, getCreeperEffects(false)),
				MobSpawnData.entityWeight(EntityType.DROWNED, 30)
						.equipmentTable(BotaniaLootTables.LOONIUM_DROWNED_DEFAULT).build(),
				MobSpawnData.entityWeight(EntityType.HUSK, 30)
						.equipmentTable(BotaniaLootTables.LOONIUM_ZOMBIE_DEFAULT).build(),
				MobSpawnData.entityWeight(EntityType.ZOMBIE, 423)
						.equipmentTable(BotaniaLootTables.LOONIUM_ZOMBIE_DEFAULT).build(),
				MobSpawnData.entityWeight(EntityType.ZOMBIE_VILLAGER, 106)
						.equipmentTable(BotaniaLootTables.LOONIUM_ZOMBIE_VILLAGER).build(),
				MobSpawnData.entityWeight(EntityType.BOGGED, 60)
						.equipmentTable(BotaniaLootTables.LOONIUM_SKELETON_DEFAULT).build(),
				MobSpawnData.entityWeight(EntityType.SKELETON, 540)
						.equipmentTable(BotaniaLootTables.LOONIUM_SKELETON_DEFAULT).build(),
				MobSpawnData.entityWeight(EntityType.CAVE_SPIDER, 59).build(),
				MobSpawnData.entityWeight(EntityType.SPIDER, 529).build()
		).build();
	}

	public static LooniumStructureConfiguration getConfigVillageSnowy(ResourceLocation parentId) {
		return LooniumStructureConfiguration.forParent(parentId).spawnedMobs(
				MobSpawnData.entityWeight(EntityType.ENDERMAN, 40).build(),
				getCreeperSpawnData(195, false, getCreeperEffects(false)),
				getCreeperSpawnData(1, true, getCreeperEffects(false)),
				MobSpawnData.entityWeight(EntityType.DROWNED, 59)
						.equipmentTable(BotaniaLootTables.LOONIUM_DROWNED_DEFAULT).build(),
				MobSpawnData.entityWeight(EntityType.ZOMBIE, 423)
						.equipmentTable(BotaniaLootTables.LOONIUM_ZOMBIE_DEFAULT).build(),
				MobSpawnData.entityWeight(EntityType.ZOMBIE_VILLAGER, 106)
						.equipmentTable(BotaniaLootTables.LOONIUM_ZOMBIE_VILLAGER).build(),
				MobSpawnData.entityWeight(EntityType.STRAY, 529)
						.equipmentTable(BotaniaLootTables.LOONIUM_SKELETON_DEFAULT).build(),
				MobSpawnData.entityWeight(EntityType.SKELETON, 59)
						.equipmentTable(BotaniaLootTables.LOONIUM_SKELETON_DEFAULT).build(),
				MobSpawnData.entityWeight(EntityType.CAVE_SPIDER, 59).build(),
				MobSpawnData.entityWeight(EntityType.SPIDER, 529).build()
		).build();
	}

	public static LooniumStructureConfiguration getConfigVillageTaiga(ResourceLocation parentId) {
		return LooniumStructureConfiguration.forParent(parentId).spawnedMobs(
				MobSpawnData.entityWeight(EntityType.ENDERMAN, 40).build(),
				getCreeperSpawnData(195, false, getCreeperEffects(false)),
				getCreeperSpawnData(1, true, getCreeperEffects(false)),
				MobSpawnData.entityWeight(EntityType.DROWNED, 59)
						.equipmentTable(BotaniaLootTables.LOONIUM_DROWNED_DEFAULT).build(),
				MobSpawnData.entityWeight(EntityType.ZOMBIE, 423)
						.equipmentTable(BotaniaLootTables.LOONIUM_ZOMBIE_DEFAULT).build(),
				MobSpawnData.entityWeight(EntityType.ZOMBIE_VILLAGER, 106)
						.equipmentTable(BotaniaLootTables.LOONIUM_ZOMBIE_VILLAGER).build(),
				MobSpawnData.entityWeight(EntityType.STRAY, 106)
						.equipmentTable(BotaniaLootTables.LOONIUM_SKELETON_DEFAULT).build(),
				MobSpawnData.entityWeight(EntityType.SKELETON, 423)
						.equipmentTable(BotaniaLootTables.LOONIUM_SKELETON_DEFAULT).build(),
				MobSpawnData.entityWeight(EntityType.CAVE_SPIDER, 59).build(),
				MobSpawnData.entityWeight(EntityType.SPIDER, 529).build()
		).build();
	}

	public static LooniumStructureConfiguration getConfigWoodlandMansion(ResourceLocation parentId) {
		return LooniumStructureConfiguration.forParent(parentId).spawnedMobs(
				MobSpawnData.entityWeight(EntityType.ENDERMAN, 40).build(),
				getCreeperSpawnData(199, false, getCreeperEffects(false)),
				getCreeperSpawnData(1, true, getCreeperEffects(false)),
				MobSpawnData.entityWeight(EntityType.VINDICATOR, 600)
						.equipmentTable(BotaniaLootTables.LOONIUM_VINDICATOR_DEFAULT).build(),
				MobSpawnData.entityWeight(EntityType.PILLAGER, 200)
						.equipmentTable(BotaniaLootTables.LOONIUM_PILLAGER_DEFAULT).build(),
				MobSpawnData.entityWeight(EntityType.EVOKER, 100).build(),
				MobSpawnData.entityWeight(EntityType.ZOMBIE, 150)
						.equipmentTable(BotaniaLootTables.LOONIUM_ARMOR_MANSION).build(),
				MobSpawnData.entityWeight(EntityType.ZOMBIE, 50).spawnAsBaby()
						.equipmentTable(BotaniaLootTables.LOONIUM_ARMOR_MANSION).build(),
				MobSpawnData.entityWeight(EntityType.BOGGED, 20)
						.equipmentTable(BotaniaLootTables.LOONIUM_ARMOR_MANSION).build(),
				MobSpawnData.entityWeight(EntityType.SKELETON, 180)
						.equipmentTable(BotaniaLootTables.LOONIUM_ARMOR_MANSION).build(),
				MobSpawnData.entityWeight(EntityType.CAVE_SPIDER, 30).build(),
				MobSpawnData.entityWeight(EntityType.SPIDER, 270).build()
		).build();
	}

	public static MobEffectToApply[] getStandardEffects(boolean withWaterBreathing, boolean withFireResistance) {
		return withFireResistance
				? (withWaterBreathing
						? new MobEffectToApply[] {
								MobEffectToApply.effect(MobEffects.FIRE_RESISTANCE).build(),
								MobEffectToApply.effect(MobEffects.REGENERATION).build(),
								MobEffectToApply.effect(MobEffects.WATER_BREATHING).build()
						}
						: new MobEffectToApply[] {
								MobEffectToApply.effect(MobEffects.FIRE_RESISTANCE).build(),
								MobEffectToApply.effect(MobEffects.REGENERATION).build()
						})
				: (withWaterBreathing
						? new MobEffectToApply[] {
								MobEffectToApply.effect(MobEffects.REGENERATION).build(),
								MobEffectToApply.effect(MobEffects.WATER_BREATHING).build()
						}
						: new MobEffectToApply[] {
								MobEffectToApply.effect(MobEffects.REGENERATION).build()
						});
	}

	public static MobEffectToApply[] getCreeperEffects(boolean withWaterBreathing) {
		return withWaterBreathing
				? new MobEffectToApply[] {
						MobEffectToApply.effect(MobEffects.FIRE_RESISTANCE).duration(100).build(),
						MobEffectToApply.effect(MobEffects.REGENERATION).duration(100).build(),
						MobEffectToApply.effect(MobEffects.WATER_BREATHING).build()
				}
				: new MobEffectToApply[] {
						MobEffectToApply.effect(MobEffects.FIRE_RESISTANCE).duration(100).build(),
						MobEffectToApply.effect(MobEffects.REGENERATION).duration(100).build()
				};
	}

	public static MobSpawnData getCreeperSpawnData(int weight, boolean charged,
			MobEffectToApply... creeperEffects) {
		MobSpawnData.Builder builder = MobSpawnData.entityWeight(EntityType.CREEPER, weight);
		builder.effectsToApply(creeperEffects);

		if (charged) {
			CompoundTag chargedCreeperNbt = new CompoundTag();
			chargedCreeperNbt.putBoolean("powered", true);
			builder.nbt(chargedCreeperNbt);
		}

		return builder.build();
	}

	public static MobSpawnData getPiglinSpawnData(int weight, ResourceKey<LootTable> equipmentTable,
			boolean needWaterBreathing, boolean zombificationImmune) {
		CompoundTag piglinNbt = new CompoundTag();
		if (zombificationImmune) {
			piglinNbt.putBoolean("IsImmuneToZombification", true);
		}

		Brain<LivingEntity> piglinBrain = Brain.provider(List.of(MemoryModuleType.UNIVERSAL_ANGER,
				MemoryModuleType.ADMIRING_DISABLED, MemoryModuleType.ATE_RECENTLY), List.of())
				.makeBrain(new Dynamic<>(NbtOps.INSTANCE));
		piglinBrain.setMemory(MemoryModuleType.UNIVERSAL_ANGER, true);
		piglinBrain.setMemory(MemoryModuleType.ADMIRING_DISABLED, true);
		piglinBrain.setMemory(MemoryModuleType.ATE_RECENTLY, true);

		DataResult<Tag> dataResult = piglinBrain.serializeStart(NbtOps.INSTANCE);
		dataResult.resultOrPartial(BotaniaAPI.LOGGER::error).ifPresent(tag -> piglinNbt.put("Brain", tag));

		return MobSpawnData.entityWeight(EntityType.PIGLIN, weight)
				.spawnAsAdult()
				.nbt(piglinNbt)
				.equipmentTable(equipmentTable)
				.effectsToApply(getStandardEffects(needWaterBreathing, true))
				.build();
	}

	@Override
	public String getName() {
		return "Loonium structure configuration";
	}
}
