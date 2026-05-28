package vazkii.botania.common.handler;

import net.minecraft.resources.ResourceLocation;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

public interface DogEnabler {
	ResourceLocation DOG_ID = botaniaRL("misc/preventing_decay");

	static boolean isDog(ResourceLocation id) {
		return DOG_ID.equals(id);
	}

	void botania_enableDog();
}
