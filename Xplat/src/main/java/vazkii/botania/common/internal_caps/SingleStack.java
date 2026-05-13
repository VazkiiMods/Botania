package vazkii.botania.common.internal_caps;

import com.mojang.serialization.Codec;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Objects;

public record SingleStack(ItemStack stack) {
	public static final Codec<SingleStack> CODEC = ItemStack.CODEC.xmap(SingleStack::new, SingleStack::stack);

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof SingleStack(ItemStack otherItem)))
			return false;
		return ItemStack.matches(stack, otherItem);
	}

	@Override
	public int hashCode() {
		return Objects.hash(Item.getId(stack.getItem()), stack.getCount(), stack.getComponentsPatch());
	}
}
