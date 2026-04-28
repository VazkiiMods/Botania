package vazkii.botania.neoforge.data;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterList;
import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterLists;
import net.minecraft.world.level.biome.TheEndBiomeSource;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.lib.BotaniaTags;
import vazkii.botania.common.world.SkyblockChunkGenerator;
import vazkii.botania.neoforge.data.gog.GogGlobalLootModifierProvider;
import vazkii.botania.neoforge.data.gog.GogRecipeProvider;
import vazkii.botania.neoforge.data.gog.GogWorldPresetTagsProvider;

import java.util.Map;
import java.util.Set;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

@EventBusSubscriber(modid = BotaniaAPI.MODID)
public class ForgeDatagenInitializer {
	private static final ResourceKey<PlacedFeature> MYSTICAL_FLOWERS_FEATURE = ResourceKey.create(
			Registries.PLACED_FEATURE, botaniaRL("mystical_flowers"));
	private static final ResourceKey<PlacedFeature> MYSTICAL_MUSHROOMS_FEATURE = ResourceKey.create(
			Registries.PLACED_FEATURE, botaniaRL("mystical_mushrooms"));

	private static final ResourceKey<BiomeModifier> ADD_MYSTICAL_FLOWERS = ResourceKey.create(
			NeoForgeRegistries.Keys.BIOME_MODIFIERS, botaniaRL("add_mystical_flowers"));
	private static final ResourceKey<BiomeModifier> ADD_MYSTICAL_MUSHROOMS = ResourceKey.create(
			NeoForgeRegistries.Keys.BIOME_MODIFIERS, botaniaRL("add_mystical_mushrooms"));
	private static final ResourceKey<BiomeModifier> REMOVE_MYSTICAL_FLOWERS = ResourceKey.create(
			NeoForgeRegistries.Keys.BIOME_MODIFIERS, botaniaRL("remove_mystical_flowers"));
	private static final ResourceKey<BiomeModifier> REMOVE_MYSTICAL_MUSHROOMS = ResourceKey.create(
			NeoForgeRegistries.Keys.BIOME_MODIFIERS, botaniaRL("remove_mystical_mushrooms"));

	public static final ResourceKey<WorldPreset> SKYBLOCK_PRESET = ResourceKey.create(
			Registries.WORLD_PRESET, BotaniaAPI.gogRL("gardenofglass"));

	@SubscribeEvent
	public static void configureForgeDatagen(GatherDataEvent evt) {
		if (System.getProperty("botania.gog_datagen") != null) {
			configureGogDatagen(evt);
		} else {
			configureNeoforgeDatagen(evt);
		}
	}

	private static void configureNeoforgeDatagen(GatherDataEvent evt) {
		evt.createBlockAndItemTags(ForgeBlockTagProvider::new, ForgeItemTagProvider::new);
		evt.createProvider(ForgeEntityTagProvider::new);
		evt.createDatapackRegistryObjects(addBiomeModifiers());
		evt.createProvider(BotaniaCuriosDataProvider::new);
		evt.createProvider(BotaniaGlobalLootModifierProvider::new);
	}

	private static void configureGogDatagen(GatherDataEvent evt) {
		evt.createProvider(GogRecipeProvider::new);
		evt.createProvider(GogGlobalLootModifierProvider::new);
		evt.createDatapackRegistryObjects(addWorldPresets(), Set.of(BotaniaAPI.GOG_MODID));
		evt.createProvider(GogWorldPresetTagsProvider::new);
	}

	private static RegistrySetBuilder addBiomeModifiers() {
		return new RegistrySetBuilder().add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, context -> {
			HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
			HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);
			context.register(ADD_MYSTICAL_FLOWERS, new BiomeModifiers.AddFeaturesBiomeModifier(
					biomes.getOrThrow(BotaniaTags.Biomes.MYSTICAL_FLOWER_SPAWNLIST),
					HolderSet.direct(placedFeatures.getOrThrow(MYSTICAL_FLOWERS_FEATURE)),
					GenerationStep.Decoration.VEGETAL_DECORATION));
			context.register(ADD_MYSTICAL_MUSHROOMS, new BiomeModifiers.AddFeaturesBiomeModifier(
					biomes.getOrThrow(BotaniaTags.Biomes.MYSTICAL_MUSHROOM_SPAWNLIST),
					HolderSet.direct(placedFeatures.getOrThrow(MYSTICAL_MUSHROOMS_FEATURE)),
					GenerationStep.Decoration.VEGETAL_DECORATION));
			context.register(REMOVE_MYSTICAL_FLOWERS, BiomeModifiers.RemoveFeaturesBiomeModifier.allSteps(
					biomes.getOrThrow(BotaniaTags.Biomes.MYSTICAL_FLOWER_BLOCKLIST),
					HolderSet.direct(placedFeatures.getOrThrow(MYSTICAL_FLOWERS_FEATURE))));
			context.register(REMOVE_MYSTICAL_MUSHROOMS, BiomeModifiers.RemoveFeaturesBiomeModifier.allSteps(
					biomes.getOrThrow(BotaniaTags.Biomes.MYSTICAL_MUSHROOM_BLOCKLIST),
					HolderSet.direct(placedFeatures.getOrThrow(MYSTICAL_MUSHROOMS_FEATURE))));
		});
	}

	private static RegistrySetBuilder addWorldPresets() {
		return new RegistrySetBuilder().add(Registries.WORLD_PRESET, context -> {
			HolderGetter<DimensionType> dimensionTypes = context.lookup(Registries.DIMENSION_TYPE);
			HolderGetter<MultiNoiseBiomeSourceParameterList> noiseParameterLists = context.lookup(Registries.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST);
			HolderGetter<NoiseGeneratorSettings> noiseSettings = context.lookup(Registries.NOISE_SETTINGS);
			HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);

			// same as preset minecraft:normal, except using SkyblockChunkGenerator for the overworld
			context.register(SKYBLOCK_PRESET, new WorldPreset(Map.of(
					LevelStem.OVERWORLD, new LevelStem(
							dimensionTypes.getOrThrow(BuiltinDimensionTypes.OVERWORLD),
							new SkyblockChunkGenerator(
									MultiNoiseBiomeSource.createFromPreset(noiseParameterLists
											.get(MultiNoiseBiomeSourceParameterLists.OVERWORLD).orElseThrow()),
									noiseSettings.getOrThrow(NoiseGeneratorSettings.OVERWORLD)
							)
					),
					LevelStem.NETHER, new LevelStem(
							dimensionTypes.getOrThrow(BuiltinDimensionTypes.NETHER),
							new NoiseBasedChunkGenerator(
									MultiNoiseBiomeSource.createFromPreset(noiseParameterLists
											.get(MultiNoiseBiomeSourceParameterLists.NETHER).orElseThrow()),
									noiseSettings.getOrThrow(NoiseGeneratorSettings.NETHER)
							)
					),
					LevelStem.END, new LevelStem(
							dimensionTypes.getOrThrow(BuiltinDimensionTypes.END),
							new NoiseBasedChunkGenerator(TheEndBiomeSource.create(biomes),
									noiseSettings.getOrThrow(NoiseGeneratorSettings.END)
							)
					)
			)));
		});
	}
}
