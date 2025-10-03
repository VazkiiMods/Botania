package vazkii.botania.neoforge;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

import vazkii.botania.api.BotaniaRegistries;
import vazkii.botania.api.block.IslandType;
import vazkii.botania.api.brew.Brew;
import vazkii.botania.api.recipe.StateIngredientType;
import vazkii.botania.common.lib.LibMisc;

@EventBusSubscriber(modid = LibMisc.MOD_ID)
public class ForgeRegistryHandler {
	@SubscribeEvent
	public static void registerRegistry(NewRegistryEvent evt) {
		evt.create(new RegistryBuilder<>(BotaniaRegistries.BREWS)
				.defaultKey(Brew.DEFAULT_ID).sync(true));
		evt.create(new RegistryBuilder<>(BotaniaRegistries.ISLAND_TYPES)
				.defaultKey(IslandType.DEFAULT_ID).sync(true));
		evt.create(new RegistryBuilder<>(BotaniaRegistries.STATE_INGREDIENT_TYPE)
				.defaultKey(StateIngredientType.DEFAULT_ID).sync(true));
	}
}
