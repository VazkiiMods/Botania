package vazkii.botania.common.internal_caps;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import vazkii.botania.api.attachment.DataHolderId;

import java.util.List;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

/**
 * Attached to a player on death. Denotes items kept when respawning.
 * 
 * @see vazkii.botania.common.item.ResoluteIvyItem
 */
public final class KeptItems {

	public static final ResourceLocation ID = botaniaRL("kept_items");
	public static final DataHolderId<List<ItemStack>> HOLDER = new DataHolderId<>(ID, ItemStack.CODEC.listOf());

	private KeptItems() {}
}
