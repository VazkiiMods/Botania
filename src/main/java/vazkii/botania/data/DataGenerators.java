package vazkii.botania.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import vazkii.botania.common.lib.LibMisc;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent evt) {
		DataGenerator generator = evt.getGenerator();
		generator.addProvider(new BlockLootProvider(generator));
		generator.addProvider(new BlockTagProvider(generator));
		generator.addProvider(new ItemTagProvider(generator));
		generator.addProvider(new StonecuttingProvider(generator));
		generator.addProvider(new RecipeAdvancementProvider(generator));
	}
}
