/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.common.internal_caps;

import net.minecraft.resources.ResourceLocation;

import vazkii.botania.api.attachment.DataMarkerId;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

/**
 * When present on a mob, that mob will not despawn instantly when far away from all players.
 * (Random idle despawning still applies.)
 */
public final class SlowDespawn {
	public static final ResourceLocation ID = botaniaRL("slow_despawn");
	public static final DataMarkerId MARKER = new DataMarkerId(ID);

	private SlowDespawn() {}
}
