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

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

public class DataGenerators {
	public static void gatherData() {
		Path output = Paths.get(".").getParent().resolve("src/generated/resources");
		DataGenerator generator = new DataGenerator(output, Collections.emptyList());
		generator.install(new BlockLootProvider(generator));
		BlockTagProvider blockTagProvider = new BlockTagProvider(generator);
		generator.install(blockTagProvider);
		generator.install(new ItemTagProvider(generator, blockTagProvider));
		generator.install(new EntityTagProvider(generator));
		generator.install(new StonecuttingProvider(generator));
		// todo 1.16-fabric generator.install(new RecipeProvider(generator));
		generator.install(new SmeltingProvider(generator));
		generator.install(new ElvenTradeProvider(generator));
		generator.install(new ManaInfusionProvider(generator));
		generator.install(new PureDaisyProvider(generator));
		generator.install(new BrewProvider(generator));
		generator.install(new PetalProvider(generator));
		generator.install(new RuneProvider(generator));
		generator.install(new TerraPlateProvider(generator));
		/* todo 1.16-fabric
		if (evt.includeClient()) {
			generator.install(new BlockstateProvider(generator, evt.getExistingFileHelper()));
			generator.install(new FloatingFlowerModelProvider(generator, evt.getExistingFileHelper()));
			generator.install(new ItemModelProvider(generator, evt.getExistingFileHelper()));
		}
		*/
	}

}
