/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.world;

import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;

import static vazkii.botania.common.block.ModBlocks.register;

public class ModFeatures {
	public static final Feature<MysticalFlowerConfig> MYSTICAL_FLOWERS = new MysticalFlowerFeature();
	public static final Feature<MysticalFlowerConfig> MYSTICAL_MUSHROOMS = new MysticalMushroomFeature();

	/* todo 1.16.2 blacklist	
	public static final Set<BiomeDictionary.Type> TYPE_BLACKLIST = ImmutableSet.of(
			BiomeDictionary.Type.DEAD,
			BiomeDictionary.Type.NETHER,
			BiomeDictionary.Type.END,
			BiomeDictionary.Type.SNOWY,
			BiomeDictionary.Type.WASTELAND,
			BiomeDictionary.Type.VOID,
			BiomeDictionary.Type.MUSHROOM
	);*/

	public static void registerFeatures(RegistryEvent.Register<Feature<?>> event) {
		IForgeRegistry<Feature<?>> r = event.getRegistry();

		register(r, "mystical_flowers", MYSTICAL_FLOWERS);
		register(r, "mystical_mushrooms", MYSTICAL_MUSHROOMS);
	}

	public static void addWorldgen() {
		//todo 1.16.2
/*		for (Biome biome : Registry.BIOME) {
			if (BiomeDictionary.getTypes(biome).stream().noneMatch(TYPE_BLACKLIST::contains)) {
				biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, MYSTICAL_FLOWERS.withConfiguration(MysticalFlowerConfig.fromConfig()).withPlacement(Placement.NOPE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));
			}
			biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, MYSTICAL_MUSHROOMS.withConfiguration(MysticalFlowerConfig.fromConfig()).withPlacement(Placement.NOPE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG)));
		}*/
	}
}
