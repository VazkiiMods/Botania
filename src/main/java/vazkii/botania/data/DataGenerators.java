/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.data;


import vazkii.botania.data.recipes.*;

/* todo fabric hook this up?
public class DataGenerators {
	public static void gatherData(GatherDataEvent evt) {
		if (evt.includeServer()) {
			evt.getGenerator().install(new BlockLootProvider(evt.getGenerator()));
			BlockTagProvider blockTagProvider = new BlockTagProvider(evt.getGenerator());
			evt.getGenerator().addProvider(blockTagProvider);
			evt.getGenerator().addProvider(new ItemTagProvider(evt.getGenerator(), blockTagProvider));
			evt.getGenerator().addProvider(new EntityTagProvider(evt.getGenerator()));
			evt.getGenerator().addProvider(new StonecuttingProvider(evt.getGenerator()));
			evt.getGenerator().addProvider(new RecipeProvider(evt.getGenerator()));
			evt.getGenerator().addProvider(new SmeltingProvider(evt.getGenerator()));
			evt.getGenerator().addProvider(new ElvenTradeProvider(evt.getGenerator()));
			evt.getGenerator().addProvider(new ManaInfusionProvider(evt.getGenerator()));
			evt.getGenerator().addProvider(new PureDaisyProvider(evt.getGenerator()));
			evt.getGenerator().addProvider(new BrewProvider(evt.getGenerator()));
			evt.getGenerator().addProvider(new PetalProvider(evt.getGenerator()));
			evt.getGenerator().addProvider(new RuneProvider(evt.getGenerator()));
			evt.getGenerator().addProvider(new TerraPlateProvider(evt.getGenerator()));
		}
		if (evt.includeClient()) {
			evt.getGenerator().install(new BlockstateProvider(evt.getGenerator(), evt.getExistingFileHelper()));
			evt.getGenerator().install(new FloatingFlowerModelProvider(evt.getGenerator(), evt.getExistingFileHelper()));
			evt.getGenerator().install(new ItemModelProvider(evt.getGenerator(), evt.getExistingFileHelper()));
		}
	}

}
*/
