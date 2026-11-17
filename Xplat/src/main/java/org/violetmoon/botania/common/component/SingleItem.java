/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 */

package vazkii.botania.common.component;

import com.mojang.serialization.Codec;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

public record SingleItem(ItemStack item) {
	public static final Codec<SingleItem> CODEC = ItemStack.SINGLE_ITEM_CODEC
			.xmap(SingleItem::new, SingleItem::item);

	public static final StreamCodec<RegistryFriendlyByteBuf, SingleItem> STREAM_CODEC = ItemStack.STREAM_CODEC
			.map(SingleItem::new, SingleItem::item);

	public SingleItem(ItemStack item) {
		this.item = item.copyWithCount(1);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof SingleItem(ItemStack otherItem))) {
			return false;
		}
		return ItemStack.isSameItemSameComponents(item, otherItem);
	}

	@Override
	public int hashCode() {
		return Objects.hash(Item.getId(item.getItem()), item.getComponentsPatch());
	}
}
