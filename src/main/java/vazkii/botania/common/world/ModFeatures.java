/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.world;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;

import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.mixin.AccessorBiomeGenerationSettings;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class ModFeatures {
	public static final Feature<MysticalFlowerConfig> MYSTICAL_FLOWERS = new MysticalFlowerFeature();
	public static final Feature<MysticalFlowerConfig> MYSTICAL_MUSHROOMS = new MysticalMushroomFeature();
	public static final ConfiguredFeature<?, ?> MYSTICAL_FLOWERS_CONF = MYSTICAL_FLOWERS.withConfiguration(MysticalFlowerConfig.fromConfig()).withPlacement(Placement.NOPE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG));
	public static final ConfiguredFeature<?, ?> MYSTICAL_MUSHROOMS_CONF = MYSTICAL_MUSHROOMS.withConfiguration(MysticalFlowerConfig.fromConfig()).withPlacement(Placement.NOPE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG));

	// todo 1.16.2 blacklist, this is about the closest to the old one?
	public static final Set<Biome.Category> TYPE_BLACKLIST = ImmutableSet.of(
			Biome.Category.NETHER,
			Biome.Category.THEEND,
			Biome.Category.ICY,
			Biome.Category.MUSHROOM
	);

	public static void registerFeatures(RegistryEvent.Register<Feature<?>> event) {
		IForgeRegistry<Feature<?>> r = event.getRegistry();

		ModBlocks.register(r, "mystical_flowers", MYSTICAL_FLOWERS);
		ModBlocks.register(r, "mystical_mushrooms", MYSTICAL_MUSHROOMS);

		Registry.register(WorldGenRegistries.field_243653_e, prefix("mystical_flowers"), MYSTICAL_FLOWERS_CONF);
		Registry.register(WorldGenRegistries.field_243653_e, prefix("mystical_mushrooms"), MYSTICAL_MUSHROOMS_CONF);
	}

	public static void addWorldgen() {
		Botania.LOGGER.debug("Injecting flowers and mushrooms into default biomes...");
		for (Biome biome : WorldGenRegistries.field_243657_i) {
			if (!TYPE_BLACKLIST.contains(biome.getCategory())) {
				injectIntoBiome(biome, MYSTICAL_FLOWERS_CONF);
			}
			if (biome.getCategory() != Biome.Category.THEEND) {
				injectIntoBiome(biome, MYSTICAL_MUSHROOMS_CONF);
			}
		}
	}

	public static void injectIntoBiome(Biome biome, ConfiguredFeature<?, ?> feature) {
		List<List<Supplier<ConfiguredFeature<?, ?>>>> features = biome.func_242440_e().func_242498_c();
		if (features instanceof ImmutableList) {
			List<List<Supplier<ConfiguredFeature<?, ?>>>> list = new ArrayList<>();
			for (List<Supplier<ConfiguredFeature<?, ?>>> featureList : features) {
				list.add(new ArrayList<>(featureList));
			}
			((AccessorBiomeGenerationSettings) biome.func_242440_e()).setFeatures(list);
			features = list;
		}

		GenerationStage.Decoration stage = GenerationStage.Decoration.VEGETAL_DECORATION;
		while (features.size() <= stage.ordinal()) {
			features.add(new ArrayList<>());
		}
		features.get(stage.ordinal()).add(() -> feature);
	}
}
