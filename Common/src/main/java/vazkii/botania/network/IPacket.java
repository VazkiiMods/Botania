package vazkii.botania.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;


import io.netty.buffer.Unpooled;

public interface IPacket {
	default FriendlyByteBuf toBuf() {
		var ret = new FriendlyByteBuf(Unpooled.buffer());
		encode(ret);
		return ret;
	}

	void encode(FriendlyByteBuf buf);

	/**
	 * Forge auto-assigns incrementing integers, Fabric requires us to declare an ID
	 * These are sent using vanilla's custom plugin channel system and thus are written to every single packet.
	 * So this ID tends to be more terse.
	 */
	ResourceLocation getFabricId();
}
