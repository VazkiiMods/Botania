/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.data;

import net.minecraft.data.DataGenerator;

import vazkii.botania.data.recipes.*;

public class DataGenerators {
	static void configureXplatDatagen(DataGenerator generator) {
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
