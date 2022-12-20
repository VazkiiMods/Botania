package vazkii.botania.common.impl.corporea.matcher;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import vazkii.botania.api.corporea.CorporeaRequestMatcher;

/**
 * Either always or never matches
 */
public class CorporeaConstantMatcher implements CorporeaRequestMatcher {
	private static final String TAG_MATCH = "match";

	private final boolean match;

	public CorporeaConstantMatcher(boolean match) {
		this.match = match;
	}

	@Override
	public boolean test(ItemStack stack) {
		return this.match;
	}

	public static CorporeaConstantMatcher createFromNBT(CompoundTag tag) {
		var match = tag.getBoolean(TAG_MATCH);

		return new CorporeaConstantMatcher(match);
	}

	@Override
	public void writeToNBT(CompoundTag tag) {
		tag.putBoolean(TAG_MATCH, this.match);
	}

	public static CorporeaConstantMatcher createFromBuf(FriendlyByteBuf buf) {
		var match = buf.readBoolean();

		return new CorporeaConstantMatcher(match);
	}

	@Override
	public void writeToBuf(FriendlyByteBuf buf) {
		buf.writeBoolean(this.match);
	}

	@Override
	public Component getRequestName() {
		if (this.match) {
			return Component.literal("*");
		} else {
			return Component.translatable("tag.botania_corporea_request.never");
		}
	}
}
