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

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.DecoratorConfiguration;
import net.minecraft.world.level.levelgen.placement.FeatureDecorator;

import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.handler.ConfigHandler;

import java.util.Set;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class ModFeatures {
	public static final Feature<MysticalFlowerConfig> MYSTICAL_FLOWERS = new MysticalFlowerFeature();
	public static final Feature<MysticalMushroomConfig> MYSTICAL_MUSHROOMS = new MysticalMushroomFeature();
	public static final ConfiguredFeature<?, ?> MYSTICAL_FLOWERS_CONF = MYSTICAL_FLOWERS
			.configured(new MysticalFlowerConfig(6, 2, 2, 16, 0.05))
			.decorated(FeatureDecorator.NOPE.configured(DecoratorConfiguration.NONE));
	public static final ConfiguredFeature<?, ?> MYSTICAL_MUSHROOMS_CONF = MYSTICAL_MUSHROOMS
			.configured(new MysticalMushroomConfig(40))
			.decorated(FeatureDecorator.NOPE.configured(DecoratorConfiguration.NONE));

	// todo 1.16.2 blacklist, this is about the closest to the old one?
	public static final Set<Biome.BiomeCategory> TYPE_BLACKLIST = ImmutableSet.of(
			Biome.BiomeCategory.NETHER,
			Biome.BiomeCategory.THEEND,
			Biome.BiomeCategory.ICY,
			Biome.BiomeCategory.MUSHROOM
	);

	public static void registerFeatures() {
		Registry<Feature<?>> r = Registry.FEATURE;

		ModBlocks.register(r, "mystical_flowers", MYSTICAL_FLOWERS);
		ModBlocks.register(r, "mystical_mushrooms", MYSTICAL_MUSHROOMS);

		Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, prefix("mystical_flowers"), MYSTICAL_FLOWERS_CONF);
		Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, prefix("mystical_mushrooms"), MYSTICAL_MUSHROOMS_CONF);

		if (ConfigHandler.COMMON.worldgenEnabled.getValue()) {
			BiomeModifications.addFeature(ctx -> {
				Biome.BiomeCategory category = ctx.getBiome().getBiomeCategory();
				return !TYPE_BLACKLIST.contains(category);
			},
					GenerationStep.Decoration.VEGETAL_DECORATION,
					BuiltinRegistries.CONFIGURED_FEATURE.getResourceKey(MYSTICAL_FLOWERS_CONF).get());
			BiomeModifications.addFeature(ctx -> {
				return ctx.getBiome().getBiomeCategory() != Biome.BiomeCategory.THEEND;
			}, GenerationStep.Decoration.VEGETAL_DECORATION,
					BuiltinRegistries.CONFIGURED_FEATURE.getResourceKey(MYSTICAL_MUSHROOMS_CONF).get());
		}

	}

}
