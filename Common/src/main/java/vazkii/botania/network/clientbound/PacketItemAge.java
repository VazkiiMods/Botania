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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;

import vazkii.botania.network.IPacket;
import vazkii.botania.xplat.IXplatAbstractions;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public record PacketItemAge(int entityId, int timeCounter) implements IPacket {

	public static final ResourceLocation ID = prefix("ia");

	@Override
	public void encode(FriendlyByteBuf buf) {
		buf.writeVarInt(entityId());
		buf.writeVarInt(timeCounter());
	}

	@Override
	public ResourceLocation getFabricId() {
		return ID;
	}

	public static PacketItemAge decode(FriendlyByteBuf buf) {
		return new PacketItemAge(buf.readVarInt(), buf.readVarInt());
	}

	public static class Handler {
		public static void handle(PacketItemAge packet) {
			int entityId = packet.entityId();
			int counter = packet.timeCounter();
			Minecraft.getInstance().execute(() -> {
				Entity e = Minecraft.getInstance().level.getEntity(entityId);
				if (e instanceof ItemEntity item) {
					IXplatAbstractions.INSTANCE.itemFlagsComponent(item).timeCounter = counter;
				}
			});
		}
	}

}
