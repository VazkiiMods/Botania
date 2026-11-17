/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package org.violetmoon.botania.common.internal_caps;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import org.violetmoon.botania.api.attachment.DataHolderId;

import java.util.List;

import static org.violetmoon.botania.api.BotaniaAPI.botaniaRL;

/**
 * Attached to a player on death. Denotes items kept when respawning.
 * 
 * @see org.violetmoon.botania.common.item.ResoluteIvyItem
 */
public final class KeptItems {

	public static final ResourceLocation ID = botaniaRL("kept_items");
	public static final DataHolderId<List<ItemStack>> HOLDER = new DataHolderId<>(ID, ItemStack.CODEC.listOf());

	private KeptItems() {}
}
