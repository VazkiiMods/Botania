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
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;

import vazkii.botania.common.lib.LibMisc;

import java.util.UUID;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

import io.netty.buffer.Unpooled;

// This should only be used for non-living entities. Vanilla's MobSpawn packet handles modded living entities fine.
// [VanillaCopy] Format is basically same as EntitySpawnS2CPacket.
public class PacketSpawnEntity {
	public static final Identifier ID = prefix("sp");

	public static Packet<?> make(Entity e) {
		if (!Registry.ENTITY_TYPE.getId(e.getType()).getNamespace().equals(LibMisc.MOD_ID)) {
			throw new IllegalArgumentException("Only should be used by Botania entities");
		}

		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeVarInt(e.getEntityId());
		buf.writeUuid(e.getUuid());
		buf.writeVarInt(Registry.ENTITY_TYPE.getRawId(e.getType()));
		buf.writeDouble(e.getX());
		buf.writeDouble(e.getY());
		buf.writeDouble(e.getZ());
		buf.writeByte(MathHelper.floor(e.pitch * 256.0F / 360.0F));
		buf.writeByte(MathHelper.floor(e.yaw * 256.0F / 360.0F));

		Vec3d velocity = e.getVelocity();
		buf.writeShort((int) (MathHelper.clamp(velocity.x, -3.9D, 3.9D) * 8000.0D));
		buf.writeShort((int) (MathHelper.clamp(velocity.y, -3.9D, 3.9D) * 8000.0D));
		buf.writeShort((int) (MathHelper.clamp(velocity.z, -3.9D, 3.9D) * 8000.0D));

		return ServerPlayNetworking.createS2CPacket(ID, buf);
	}

	public static class Handler {
		public static void handle(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
			int id = buf.readVarInt();
			UUID uuid = buf.readUuid();
			EntityType<?> type = Registry.ENTITY_TYPE.get(buf.readVarInt());
			double x = buf.readDouble();
			double y = buf.readDouble();
			double z = buf.readDouble();
			float pitch = (buf.readByte() * 360) / 256.0F;
			float yaw = (buf.readByte() * 360) / 256.0F;
			double dx = buf.readShort() / 8000.0;
			double dy = buf.readShort() / 8000.0;
			double dz = buf.readShort() / 8000.0;

			client.execute(() -> {
				ClientWorld world = client.world;
				Entity e = type.create(world);
				if (e != null) {
					e.updateTrackedPosition(x, y, z);
					e.refreshPositionAfterTeleport(x, y, z);
					e.pitch = pitch;
					e.yaw = yaw;
					e.setEntityId(id);
					e.setUuid(uuid);
					e.setVelocityClient(dx, dy, dz);
					world.addEntity(id, e);
				}
			});
		}
	}

}
