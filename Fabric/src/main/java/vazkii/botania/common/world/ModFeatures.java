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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import vazkii.botania.common.core.handler.ConfigHandler;

import java.util.Set;
import java.util.function.BiConsumer;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class ModFeatures {
	public static final Feature<MysticalFlowerConfig> MYSTICAL_FLOWERS = new MysticalFlowerFeature();
	public static final Feature<MysticalMushroomConfig> MYSTICAL_MUSHROOMS = new MysticalMushroomFeature();
	public static final ConfiguredFeature<?, ?> MYSTICAL_FLOWERS_CONF = MYSTICAL_FLOWERS
			.configured(new MysticalFlowerConfig(6, 2, 2, 16, 0.05));
	public static final ConfiguredFeature<?, ?> MYSTICAL_MUSHROOMS_CONF = MYSTICAL_MUSHROOMS
			.configured(new MysticalMushroomConfig(40));

	public static final PlacedFeature MYSTICAL_FLOWERS_PLACED = MYSTICAL_FLOWERS_CONF.placed();
	public static final PlacedFeature MYSTICAL_MUSHROOMS_PLACED = MYSTICAL_MUSHROOMS_CONF.placed();

	public static final Set<Biome.BiomeCategory> TYPE_BLACKLIST = ImmutableSet.of(
			Biome.BiomeCategory.NETHER,
			Biome.BiomeCategory.THEEND,
			Biome.BiomeCategory.ICY,
			Biome.BiomeCategory.MUSHROOM
	);

	public static void registerFeatures(BiConsumer<Feature<?>, ResourceLocation> r) {
		r.accept(MYSTICAL_FLOWERS, prefix("mystical_flowers"));
		r.accept(MYSTICAL_MUSHROOMS, prefix("mystical_mushrooms"));

		Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, prefix("mystical_flowers"), MYSTICAL_FLOWERS_CONF);
		Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, prefix("mystical_mushrooms"), MYSTICAL_MUSHROOMS_CONF);

		Registry.register(BuiltinRegistries.PLACED_FEATURE, prefix("mystical_flowers"), MYSTICAL_FLOWERS_PLACED);
		Registry.register(BuiltinRegistries.PLACED_FEATURE, prefix("mystical_mushrooms"), MYSTICAL_MUSHROOMS_PLACED);

		if (ConfigHandler.COMMON.worldgenEnabled.getValue()) {
			BiomeModifications.addFeature(ctx -> {
				Biome.BiomeCategory category = ctx.getBiome().getBiomeCategory();
				return !TYPE_BLACKLIST.contains(category);
			},
					GenerationStep.Decoration.VEGETAL_DECORATION,
					BuiltinRegistries.PLACED_FEATURE.getResourceKey(MYSTICAL_FLOWERS_PLACED).orElseThrow());
			BiomeModifications.addFeature(
					ctx -> ctx.getBiome().getBiomeCategory() != Biome.BiomeCategory.THEEND,
					GenerationStep.Decoration.VEGETAL_DECORATION,
					BuiltinRegistries.PLACED_FEATURE.getResourceKey(MYSTICAL_MUSHROOMS_PLACED).orElseThrow());
		}

	}

}
