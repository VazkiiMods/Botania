package vazkii.botania.api;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;

import vazkii.botania.api.brew.Brew;

public class BotaniaRegistries {
	public static final ResourceKey<Registry<Brew>> BREWS =
			ResourceKey.createRegistryKey(new ResourceLocation(BotaniaAPI.MODID, "brews"));

	/**
	 * The ID of Botania's Creative Tab
	 */
	public static final ResourceKey<CreativeModeTab> BOTANIA_TAB_KEY = ResourceKey.create(Registries.CREATIVE_MODE_TAB,
			new ResourceLocation(BotaniaAPI.MODID, "botania"));
}
