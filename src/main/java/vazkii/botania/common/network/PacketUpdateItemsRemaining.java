package vazkii.botania.common.network;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;
import vazkii.botania.client.core.handler.ItemsRemainingRenderHandler;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class PacketUpdateItemsRemaining {
	private final ItemStack stack;
	private final int count;
	@Nullable
	private final ITextComponent tooltip;

	public PacketUpdateItemsRemaining(ItemStack stack, int count, @Nullable ITextComponent tooltip) {
		this.stack = stack;
		this.count = count;
		this.tooltip = tooltip;
	}

	public static PacketUpdateItemsRemaining decode(PacketBuffer buf) {
		return new PacketUpdateItemsRemaining(buf.readItemStack(), buf.readVarInt(), buf.readTextComponent());
	}

	public void encode(PacketBuffer buf) {
		buf.writeItemStack(stack);
		buf.writeVarInt(count);
		buf.writeTextComponent(tooltip);
	}

	public void handle(Supplier<NetworkEvent.Context> ctx) {
		if (ctx.get().getDirection().getReceptionSide() == LogicalSide.CLIENT) {
			ctx.get().enqueueWork(() -> ItemsRemainingRenderHandler.set(stack, count, tooltip));
			ctx.get().setPacketHandled(true);
		}
	}
}
