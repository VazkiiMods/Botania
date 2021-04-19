/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.data;

import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

import vazkii.botania.data.recipes.*;

public class DataGenerators {
	public static void gatherData(GatherDataEvent evt) {
		ExistingFileHelper helper = evt.getExistingFileHelper();
		if (evt.includeServer()) {
			evt.getGenerator().addProvider(new BlockLootProvider(evt.getGenerator()));
			BlockTagProvider blockTagProvider = new BlockTagProvider(evt.getGenerator(), helper);
			evt.getGenerator().addProvider(blockTagProvider);
			evt.getGenerator().addProvider(new ItemTagProvider(evt.getGenerator(), blockTagProvider, helper));
			evt.getGenerator().addProvider(new EntityTagProvider(evt.getGenerator(), helper));
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
			evt.getGenerator().addProvider(new OrechidProvider(evt.getGenerator()));
		}
		if (evt.includeClient()) {
			evt.getGenerator().addProvider(new BlockstateProvider(evt.getGenerator(), helper));
			evt.getGenerator().addProvider(new FloatingFlowerModelProvider(evt.getGenerator(), helper));
			evt.getGenerator().addProvider(new ItemModelProvider(evt.getGenerator(), helper));
			evt.getGenerator().addProvider(new TinyPotatoModelProvider(evt.getGenerator(), helper));
		}
	}

}
