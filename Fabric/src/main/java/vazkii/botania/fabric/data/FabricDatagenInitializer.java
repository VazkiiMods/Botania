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
		generator.addProvider(new FabricBlockLootProvider(generator));
		var blockTagProvider = new FabricBlockTagProvider(generator);
		generator.addProvider(blockTagProvider);
		generator.addProvider(new FabricItemTagProvider(generator, blockTagProvider));
		generator.addProvider(new FabricRecipeProvider(generator));
	}

	private static void configureXplatDatagen(DataGenerator generator) {
		generator.addProvider(new BlockLootProvider(generator));
		BlockTagProvider blockTagProvider = new BlockTagProvider(generator);
		generator.addProvider(blockTagProvider);
		generator.addProvider(new ItemTagProvider(generator, blockTagProvider));
		generator.addProvider(new EntityTagProvider(generator));
		generator.addProvider(new StonecuttingProvider(generator));
		generator.addProvider(new RecipeProvider(generator));
		generator.addProvider(new SmeltingProvider(generator));
		generator.addProvider(new ElvenTradeProvider(generator));
		generator.addProvider(new ManaInfusionProvider(generator));
		generator.addProvider(new PureDaisyProvider(generator));
		generator.addProvider(new BrewProvider(generator));
		generator.addProvider(new PetalProvider(generator));
		generator.addProvider(new RuneProvider(generator));
		generator.addProvider(new TerraPlateProvider(generator));
		generator.addProvider(new OrechidProvider(generator));
		generator.addProvider(new BlockstateProvider(generator));
		generator.addProvider(new FloatingFlowerModelProvider(generator));
		generator.addProvider(new ItemModelProvider(generator));
		generator.addProvider(new AdvancementProvider(generator));
	}
}
