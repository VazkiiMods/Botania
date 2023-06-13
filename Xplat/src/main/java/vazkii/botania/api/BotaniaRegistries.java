package vazkii.botania.api;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import vazkii.botania.api.brew.Brew;

public class BotaniaRegistries {
	public static final ResourceKey<Registry<Brew>> BREWS =
			ResourceKey.createRegistryKey(new ResourceLocation(BotaniaAPI.MODID, "brews"));
}
