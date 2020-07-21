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
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.DecoratorConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.common.BiomeDictionary;

import java.util.Set;

import static vazkii.botania.common.block.ModBlocks.register;

public class ModFeatures {
	public static final Feature<MysticalFlowerConfig> MYSTICAL_FLOWERS = new MysticalFlowerFeature();
	public static final Feature<MysticalFlowerConfig> MYSTICAL_MUSHROOMS = new MysticalMushroomFeature();

	public static final Set<BiomeDictionary.Type> TYPE_BLACKLIST = ImmutableSet.of(
			BiomeDictionary.Type.DEAD,
			BiomeDictionary.Type.NETHER,
			BiomeDictionary.Type.END,
			BiomeDictionary.Type.SNOWY,
			BiomeDictionary.Type.WASTELAND,
			BiomeDictionary.Type.VOID
	);

	public static void registerFeatures() {
		Registry<Feature<?>> r = Registry.FEATURE;

		register(r, "mystical_flowers", MYSTICAL_FLOWERS);
		register(r, "mystical_mushrooms", MYSTICAL_MUSHROOMS);
	}

	public static void addWorldgen() {
		for (Biome biome : Registry.BIOME) {
			if (BiomeDictionary.getTypes(biome).stream().noneMatch(TYPE_BLACKLIST::contains)) {
				biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, MYSTICAL_FLOWERS.configure(MysticalFlowerConfig.fromConfig()).createDecoratedFeature(Decorator.NOPE.configure(DecoratorConfig.DEFAULT)));
			}
			biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, MYSTICAL_MUSHROOMS.configure(MysticalFlowerConfig.fromConfig()).createDecoratedFeature(Decorator.NOPE.configure(DecoratorConfig.DEFAULT)));
		}
	}
}
