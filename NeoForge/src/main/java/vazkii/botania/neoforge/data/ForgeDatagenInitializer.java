package vazkii.botania.neoforge.data;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.lib.BotaniaTags;

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

	@SubscribeEvent
	public static void configureForgeDatagen(GatherDataEvent evt) {
		var generator = evt.getGenerator();
		var output = generator.getPackOutput();
		var blockTagProvider = new ForgeBlockTagProvider(output, evt.getLookupProvider());
		generator.addProvider(evt.includeServer(), blockTagProvider);
		generator.addProvider(evt.includeServer(), new ForgeItemTagProvider(output, evt.getLookupProvider(),
				blockTagProvider.contentsGetter()));
		generator.addProvider(evt.includeServer(), new ForgeEntityTagProvider(output, evt.getLookupProvider()));
		generator.addProvider(evt.includeServer(), new DatapackBuiltinEntriesProvider(output, evt.getLookupProvider(),
				addBiomeModifiers(), Set.of(BotaniaAPI.MODID)));
		generator.addProvider(evt.includeServer(), new BotaniaCuriosDataProvider(output, evt.getLookupProvider()));
		generator.addProvider(evt.includeServer(), new BotaniaGlobalLootModifierProvider(output, evt.getLookupProvider()));
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
}
