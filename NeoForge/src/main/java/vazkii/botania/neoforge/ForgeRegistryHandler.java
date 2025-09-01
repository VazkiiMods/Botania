package vazkii.botania.neoforge;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

import vazkii.botania.api.BotaniaRegistries;
import vazkii.botania.common.lib.LibMisc;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

@EventBusSubscriber(modid = LibMisc.MOD_ID)
public class ForgeRegistryHandler {
	@SubscribeEvent
	public static void registerRegistry(NewRegistryEvent evt) {
		evt.create(new RegistryBuilder<>(BotaniaRegistries.BREWS)
				.defaultKey(botaniaRL("fallback")).sync(true));
		evt.create(new RegistryBuilder<>(BotaniaRegistries.STATE_INGREDIENT_TYPE)
				.defaultKey(botaniaRL("none")).sync(true));
	}
}
