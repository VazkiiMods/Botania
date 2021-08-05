/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.network;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;

import vazkii.botania.common.lib.LibMisc;

import java.util.UUID;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

import io.netty.buffer.Unpooled;

// This should only be used for non-living entities. Vanilla's MobSpawn packet handles modded living entities fine.
// [VanillaCopy] Format is basically same as ClientboundAddEntityPacket.
public class PacketSpawnEntity {
	public static final ResourceLocation ID = prefix("sp");

	public static Packet<?> make(Entity e) {
		if (!Registry.ENTITY_TYPE.getKey(e.getType()).getNamespace().equals(LibMisc.MOD_ID)) {
			throw new IllegalArgumentException("Only should be used by Botania entities");
		}

		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		buf.writeVarInt(e.getId());
		buf.writeUUID(e.getUUID());
		buf.writeVarInt(Registry.ENTITY_TYPE.getId(e.getType()));
		buf.writeDouble(e.getX());
		buf.writeDouble(e.getY());
		buf.writeDouble(e.getZ());
		buf.writeByte(Mth.floor(e.getXRot() * 256.0F / 360.0F));
		buf.writeByte(Mth.floor(e.getYRot() * 256.0F / 360.0F));

		Vec3 velocity = e.getDeltaMovement();
		buf.writeShort((int) (Mth.clamp(velocity.x, -3.9D, 3.9D) * 8000.0D));
		buf.writeShort((int) (Mth.clamp(velocity.y, -3.9D, 3.9D) * 8000.0D));
		buf.writeShort((int) (Mth.clamp(velocity.z, -3.9D, 3.9D) * 8000.0D));

		return ServerPlayNetworking.createS2CPacket(ID, buf);
	}

	public static class Handler {
		public static void handle(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buf, PacketSender responseSender) {
			int id = buf.readVarInt();
			UUID uuid = buf.readUUID();
			EntityType<?> type = Registry.ENTITY_TYPE.byId(buf.readVarInt());
			double x = buf.readDouble();
			double y = buf.readDouble();
			double z = buf.readDouble();
			float pitch = (buf.readByte() * 360) / 256.0F;
			float yaw = (buf.readByte() * 360) / 256.0F;
			double dx = buf.readShort() / 8000.0;
			double dy = buf.readShort() / 8000.0;
			double dz = buf.readShort() / 8000.0;

			client.execute(() -> {
				ClientLevel world = client.level;
				Entity e = type.create(world);
				if (e != null) {
					e.setPacketCoordinates(x, y, z);
					e.moveTo(x, y, z);
					e.setXRot(pitch);
					e.setYRot(yaw);
					e.setId(id);
					e.setUUID(uuid);
					e.lerpMotion(dx, dy, dz);
					world.putNonPlayerEntity(id, e);
				}
			});
		}
	}

}
