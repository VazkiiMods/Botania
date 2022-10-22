/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.fabric.data;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.data.DataGenerator;

import vazkii.botania.data.*;
import vazkii.botania.data.recipes.*;

public class FabricDatagenInitializer implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator generator) {
		if (System.getProperty("botania.xplat_datagen") != null) {
			configureXplatDatagen(generator);
		} else {
			configureFabricDatagen(generator);
		}
	}

	private static void configureFabricDatagen(DataGenerator generator) {
		generator.addProvider(true, new FabricBlockLootProvider(generator));
		var blockTagProvider = new FabricBlockTagProvider(generator);
		generator.addProvider(true, blockTagProvider);
		generator.addProvider(true, new FabricItemTagProvider(generator, blockTagProvider));
		generator.addProvider(true, new FabricRecipeProvider(generator));
		generator.addProvider(true, new FabricBiomeTagProvider(generator));
	}

	private static void configureXplatDatagen(DataGenerator generator) {
		generator.addProvider(true, new BlockLootProvider(generator));
		BlockTagProvider blockTagProvider = new BlockTagProvider(generator);
		generator.addProvider(true, blockTagProvider);
		generator.addProvider(true, new ItemTagProvider(generator, blockTagProvider));
		generator.addProvider(true, new EntityTagProvider(generator));
		generator.addProvider(true, new BannerTagProvider(generator));
		generator.addProvider(true, new BiomeTagProvider(generator));
		generator.addProvider(true, new StonecuttingProvider(generator));
		generator.addProvider(true, new RecipeProvider(generator));
		generator.addProvider(true, new SmeltingProvider(generator));
		generator.addProvider(true, new ElvenTradeProvider(generator));
		generator.addProvider(true, new ManaInfusionProvider(generator));
		generator.addProvider(true, new PureDaisyProvider(generator));
		generator.addProvider(true, new BrewProvider(generator));
		generator.addProvider(true, new PetalApothecaryProvider(generator));
		generator.addProvider(true, new RunicAltarProvider(generator));
		generator.addProvider(true, new TerrestrialAgglomerationProvider(generator));
		generator.addProvider(true, new OrechidProvider(generator));
		generator.addProvider(true, new BlockstateProvider(generator));
		generator.addProvider(true, new FloatingFlowerModelProvider(generator));
		generator.addProvider(true, new ItemModelProvider(generator));
		generator.addProvider(true, new AdvancementProvider(generator));
	}
}
