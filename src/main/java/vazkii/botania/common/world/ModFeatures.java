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
import net.minecraft.world.gen.ChunkGeneratorType;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Set;

import static vazkii.botania.common.block.ModBlocks.register;

public class ModFeatures {
	public static final Feature<MysticalFlowerConfig> MYSTICAL_FLOWERS = new MysticalFlowerFeature(MysticalFlowerConfig::deserialize);
	public static final Feature<MysticalFlowerConfig> MYSTICAL_MUSHROOMS = new MysticalMushroomFeature(MysticalFlowerConfig::deserialize);

	public static final Set<BiomeDictionary.Type> TYPE_BLACKLIST = ImmutableSet.of(
			BiomeDictionary.Type.DEAD,
			BiomeDictionary.Type.NETHER,
			BiomeDictionary.Type.END,
			BiomeDictionary.Type.SNOWY,
			BiomeDictionary.Type.WASTELAND,
			BiomeDictionary.Type.VOID
	);

	public static void registerFeatures(RegistryEvent.Register<Feature<?>> event) {
		IForgeRegistry<Feature<?>> r = event.getRegistry();

		register(r, MYSTICAL_FLOWERS, "mystical_flowers");
		register(r, MYSTICAL_MUSHROOMS, "mystical_mushrooms");
	}

	public static void registerChunkGenerators(RegistryEvent.Register<ChunkGeneratorType<?, ?>> evt) {
		register(evt.getRegistry(), SkyblockChunkGenerator.TYPE, "garden_of_glass");
	}

	public static void addWorldgen() {
		for (Biome biome : Registry.BIOME) {
			if (BiomeDictionary.getTypes(biome).stream().noneMatch(TYPE_BLACKLIST::contains)) {
				biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, MYSTICAL_FLOWERS.withConfiguration(new MysticalFlowerConfig()).withPlacement(Placement.NOPE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));
			}
			biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, MYSTICAL_MUSHROOMS.withConfiguration(new MysticalFlowerConfig()).withPlacement(Placement.NOPE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));
		}
	}
}
