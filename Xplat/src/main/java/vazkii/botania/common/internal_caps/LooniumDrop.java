package vazkii.botania.common.internal_caps;

import com.mojang.serialization.Codec;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import vazkii.botania.api.attachment.DataHolderId;

import java.util.Objects;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

/**
 * Attached to mobs when spawned by Loonium. Overrides the mob's loot table with a single (potentially empty) drop.
 */
public record LooniumDrop(ItemStack stack) {
	public static final Codec<LooniumDrop> CODEC = ItemStack.CODEC.xmap(LooniumDrop::new, LooniumDrop::stack);
	public static final ResourceLocation ID = botaniaRL("loonium_drop");
	public static final DataHolderId<LooniumDrop> HOLDER = new DataHolderId<>(ID, CODEC);

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof LooniumDrop(ItemStack otherItem)))
			return false;
		return ItemStack.matches(stack, otherItem);
	}

	@Override
	public int hashCode() {
		return Objects.hash(Item.getId(stack.getItem()), stack.getCount(), stack.getComponentsPatch());
	}
}
