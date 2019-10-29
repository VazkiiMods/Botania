package vazkii.botania.data;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import vazkii.botania.common.lib.LibMisc;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent evt) {
        evt.getGenerator().addProvider(new BlockLootProvider(evt.getGenerator()));
        evt.getGenerator().addProvider(new BlockTagProvider(evt.getGenerator()));
        evt.getGenerator().addProvider(new ItemTagProvider(evt.getGenerator()));
        evt.getGenerator().addProvider(new StonecuttingProvider(evt.getGenerator()));
    }
}
