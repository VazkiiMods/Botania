/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.network.clientbound;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import vazkii.botania.common.item.rod.ItemTornadoRod;
import vazkii.botania.network.IPacket;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public record PacketAvatarTornadoRod(boolean elytra) implements IPacket {
	public static final ResourceLocation ID = prefix("atr");

	@Override
	public void encode(FriendlyByteBuf buf) {
		buf.writeBoolean(elytra);
	}

	@Override
	public ResourceLocation getFabricId() {
		return ID;
	}

	public static PacketAvatarTornadoRod decode(FriendlyByteBuf buf) {
		return new PacketAvatarTornadoRod(buf.readBoolean());
	}

	public static class Handler {
		public static void handle(PacketAvatarTornadoRod packet) {
			boolean elytra = packet.elytra();
			Minecraft.getInstance().execute(() -> {
				Player player = Minecraft.getInstance().player;
				Level world = Minecraft.getInstance().level;
				if (elytra) {
					ItemTornadoRod.doAvatarElytraBoost(player, world);
				} else {
					ItemTornadoRod.doAvatarJump(player, world);
				}
			});
		}
	}
}
