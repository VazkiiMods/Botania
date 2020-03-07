/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.network;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketAck implements ILoginPacket {
	private int loginIndex;

	public static PacketAck decode(PacketBuffer buf) {
		return new PacketAck();
	}

	@Override
	public void setLoginIndex(int index) {
		this.loginIndex = index;
	}

	@Override
	public int getLoginIndex() {
		return loginIndex;
	}

	public void handle(Supplier<NetworkEvent.Context> ctx) {
		ctx.get().setPacketHandled(true);
	}
}
