/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import vazkii.botania.common.item.rod.ItemTornadoRod;

import java.util.function.Supplier;

public class PacketAvatarTornadoRod {
	private final boolean elytra;

	public PacketAvatarTornadoRod(boolean elytra) {
		this.elytra = elytra;
	}

	public void encode(PacketBuffer buf) {
		buf.writeBoolean(elytra);
	}

	public static PacketAvatarTornadoRod decode(PacketBuffer buf) {
		return new PacketAvatarTornadoRod(buf.readBoolean());
	}

	public static void handle(PacketAvatarTornadoRod pkt, Supplier<NetworkEvent.Context> ctx) {
		if (ctx.get().getDirection().getReceptionSide().isClient()) {
			//noinspection Convert2Lambda
			ctx.get().enqueueWork(new Runnable() {
				@Override
				public void run() {
					PlayerEntity player = Minecraft.getInstance().player;
					World world = Minecraft.getInstance().world;
					if (pkt.elytra) {
						ItemTornadoRod.doAvatarElytraBoost(player, world);
					} else {
						ItemTornadoRod.doAvatarJump(player, world);
					}
				}
			});
		}
	}
}
