package vazkii.botania.forge.data;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

import vazkii.botania.common.lib.LibMisc;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ForgeDatagenInitializer {
	@SubscribeEvent
	public static void configureForgeDatagen(GatherDataEvent evt) {
		var generator = evt.getGenerator();
		var blockTagProvider = new ForgeBlockTagProvider(generator);
		generator.addProvider(blockTagProvider);
		generator.addProvider(new ForgeItemTagProvider(generator, blockTagProvider));
		generator.addProvider(new ForgeRecipeProvider(generator));
	}
}
