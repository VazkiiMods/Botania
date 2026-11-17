/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

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
