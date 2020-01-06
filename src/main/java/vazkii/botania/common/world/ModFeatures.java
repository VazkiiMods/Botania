/**
 * This class was created by <Hubry>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [sie 16 2019, 19:17]
 */
package vazkii.botania.common.world;

import com.google.common.collect.ImmutableSet;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGeneratorType;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import vazkii.botania.common.lib.LibMisc;

import java.util.Set;

import static vazkii.botania.common.block.ModBlocks.register;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
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

	@SubscribeEvent
	public static void registerFeatures(RegistryEvent.Register<Feature<?>> event) {
		IForgeRegistry<Feature<?>> r = event.getRegistry();

		register(r, MYSTICAL_FLOWERS, "mystical_flowers");
		register(r, MYSTICAL_MUSHROOMS, "mystical_mushrooms");
	}

	@SubscribeEvent
	public static void registerChunkGenerators(RegistryEvent.Register<ChunkGeneratorType<?, ?>> evt) {
		register(evt.getRegistry(), SkyblockChunkGenerator.TYPE, "garden_of_glass");
	}
	
	public static void addWorldgen() {
		for(Biome biome : ForgeRegistries.BIOMES) {
			if (BiomeDictionary.getTypes(biome).stream().noneMatch(TYPE_BLACKLIST::contains)) {
				biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Biome.createDecoratedFeature(MYSTICAL_FLOWERS, new MysticalFlowerConfig(), Placement.NOPE, IPlacementConfig.NO_PLACEMENT_CONFIG));
			}
			biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Biome.createDecoratedFeature(MYSTICAL_MUSHROOMS, new MysticalFlowerConfig(), Placement.NOPE, IPlacementConfig.NO_PLACEMENT_CONFIG));
		}
	}
}
