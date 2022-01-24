package vazkii.botania.forge.data;

import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

import vazkii.botania.common.lib.LibMisc;

import java.util.Collections;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ForgeDatagenInitializer {
	@SubscribeEvent
	public static void configureForgeDatagen(GatherDataEvent evt) {
		if (evt.includeServer()) {
			var generator = evt.getGenerator();
			var disabledHelper = new ExistingFileHelper(Collections.emptyList(), Collections.emptySet(), false, null, null);
			var blockTagProvider = new ForgeBlockTagProvider(generator, disabledHelper);
			generator.addProvider(blockTagProvider);
			generator.addProvider(new ForgeItemTagProvider(generator, blockTagProvider, disabledHelper));
			generator.addProvider(new ForgeRecipeProvider(generator));
			generator.addProvider(new ForgeBlockLootProvider(generator));
		}
	}
}
