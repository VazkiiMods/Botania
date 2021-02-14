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
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.DecoratorConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;

import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.handler.ConfigHandler;

import java.util.Set;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class ModFeatures {
	public static final Feature<MysticalFlowerConfig> MYSTICAL_FLOWERS = new MysticalFlowerFeature();
	public static final Feature<MysticalMushroomConfig> MYSTICAL_MUSHROOMS = new MysticalMushroomFeature();
	public static final ConfiguredFeature<?, ?> MYSTICAL_FLOWERS_CONF = MYSTICAL_FLOWERS
			.configure(new MysticalFlowerConfig(6, 2, 2, 16, 0.05))
			.decorate(Decorator.NOPE.configure(DecoratorConfig.DEFAULT));
	public static final ConfiguredFeature<?, ?> MYSTICAL_MUSHROOMS_CONF = MYSTICAL_MUSHROOMS
			.configure(new MysticalMushroomConfig(40))
			.decorate(Decorator.NOPE.configure(DecoratorConfig.DEFAULT));

	// todo 1.16.2 blacklist, this is about the closest to the old one?
	public static final Set<Biome.Category> TYPE_BLACKLIST = ImmutableSet.of(
			Biome.Category.NETHER,
			Biome.Category.THEEND,
			Biome.Category.ICY,
			Biome.Category.MUSHROOM
	);

	public static void registerFeatures() {
		Registry<Feature<?>> r = Registry.FEATURE;

		ModBlocks.register(r, "mystical_flowers", MYSTICAL_FLOWERS);
		ModBlocks.register(r, "mystical_mushrooms", MYSTICAL_MUSHROOMS);

		Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, prefix("mystical_flowers"), MYSTICAL_FLOWERS_CONF);
		Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, prefix("mystical_mushrooms"), MYSTICAL_MUSHROOMS_CONF);

		if (ConfigHandler.COMMON.worldgenEnabled.getValue()) {
			BiomeModifications.addFeature(ctx -> {
				Biome.Category category = ctx.getBiome().getCategory();
				return !TYPE_BLACKLIST.contains(category);
			},
					GenerationStep.Feature.VEGETAL_DECORATION,
					BuiltinRegistries.CONFIGURED_FEATURE.getKey(MYSTICAL_FLOWERS_CONF).get());
			BiomeModifications.addFeature(ctx -> {
				return ctx.getBiome().getCategory() != Biome.Category.THEEND;
			}, GenerationStep.Feature.VEGETAL_DECORATION,
					BuiltinRegistries.CONFIGURED_FEATURE.getKey(MYSTICAL_MUSHROOMS_CONF).get());
		}

	}

}
