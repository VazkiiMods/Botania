/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.api.internal;

import vazkii.botania.api.attachment.DataHolderId;
import vazkii.botania.common.internal_caps.ItemSources;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

/**
 * Specifies how an item entity was spawned.
 */
public record ItemSource(boolean allowsQuickPickup) {
	public static final net.minecraft.resources.ResourceLocation ID = botaniaRL("item_source");
	public static final DataHolderId<ItemSource> HOLDER = new DataHolderId<>(ID, ItemSources.CODEC);
}
