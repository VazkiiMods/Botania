package vazkii.botania.forge;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.forge.network.ForgePacketHandler;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = LibMisc.MOD_ID)
@Mod(LibMisc.MOD_ID)
public class ForgeCommonInitializer {
	public ForgeCommonInitializer() {
		ForgeBotaniaConfig.setup();
	}

	@SubscribeEvent
	public static void commonSetup(FMLCommonSetupEvent evt) {
		ForgePacketHandler.init();
	}
}
