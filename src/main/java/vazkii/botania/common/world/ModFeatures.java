/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.world;

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
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.registries.IForgeRegistry;

import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.handler.ConfigHandler;

import java.util.Set;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class ModFeatures {
	public static final Feature<MysticalFlowerConfig> MYSTICAL_FLOWERS = new MysticalFlowerFeature();
	public static final Feature<MysticalMushroomConfig> MYSTICAL_MUSHROOMS = new MysticalMushroomFeature();
	public static final ConfiguredFeature<?, ?> MYSTICAL_FLOWERS_CONF = MYSTICAL_FLOWERS
			.withConfiguration(new MysticalFlowerConfig(2, 2, 6, 16, 0.05))
			.withPlacement(Placement.NOPE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG));
	public static final ConfiguredFeature<?, ?> MYSTICAL_MUSHROOMS_CONF = MYSTICAL_MUSHROOMS
			.withConfiguration(new MysticalMushroomConfig(40))
			.withPlacement(Placement.NOPE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG));

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

		Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, prefix("mystical_flowers"), MYSTICAL_FLOWERS_CONF);
		Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, prefix("mystical_mushrooms"), MYSTICAL_MUSHROOMS_CONF);
	}

	public static void onBiomeLoad(BiomeLoadingEvent event) {
		if (!ConfigHandler.COMMON.worldgenEnabled.get()) {
			return;
		}
		Biome.Category category = event.getCategory();
		if (!TYPE_BLACKLIST.contains(category)) {
			event.getGeneration().withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, MYSTICAL_FLOWERS_CONF);
		}
		if (category != Biome.Category.THEEND) {
			event.getGeneration().withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, MYSTICAL_MUSHROOMS_CONF);
		}
	}

}
