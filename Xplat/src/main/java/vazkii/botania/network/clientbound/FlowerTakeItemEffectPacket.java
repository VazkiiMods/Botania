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
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import vazkii.botania.client.fx.FlowerItemPickupParticle;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

import io.netty.buffer.ByteBuf;

public record FlowerTakeItemEffectPacket(int itemId, BlockPos pos, int amount, boolean onFire) implements CustomPacketPayload {

	public static final Type<FlowerTakeItemEffectPacket> ID = new Type<>(botaniaRL("ti"));
	// we encode the "on-fire" information in the packet ID, since it would take up an entire byte otherwise
	public static final Type<FlowerTakeItemEffectPacket> FIRE_ID = new Type<>(botaniaRL("tf"));
	public static final StreamCodec<ByteBuf, FlowerTakeItemEffectPacket> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT, FlowerTakeItemEffectPacket::itemId,
			BlockPos.STREAM_CODEC, FlowerTakeItemEffectPacket::pos,
			ByteBufCodecs.VAR_INT, FlowerTakeItemEffectPacket::amount,
			FlowerTakeItemEffectPacket::create
	);
	public static final StreamCodec<ByteBuf, FlowerTakeItemEffectPacket> STREAM_CODEC_FIRE = StreamCodec.composite(
			ByteBufCodecs.VAR_INT, FlowerTakeItemEffectPacket::itemId,
			BlockPos.STREAM_CODEC, FlowerTakeItemEffectPacket::pos,
			ByteBufCodecs.VAR_INT, FlowerTakeItemEffectPacket::amount,
			FlowerTakeItemEffectPacket::creatOnFire
	);

	public static FlowerTakeItemEffectPacket create(int itemId, BlockPos pos, int amount) {
		return new FlowerTakeItemEffectPacket(itemId, pos, amount, false);
	}

	public static FlowerTakeItemEffectPacket creatOnFire(int itemId, BlockPos pos, int amount) {
		return new FlowerTakeItemEffectPacket(itemId, pos, amount, true);
	}

	@Override
	public Type<FlowerTakeItemEffectPacket> type() {
		return onFire ? FIRE_ID : ID;
	}

	public static class Handler {
		// [VanillaCopy] ClientPacketListener::handleTakeItemEntity
		public static void handle(FlowerTakeItemEffectPacket packet, Player localPlayer) {
			ClientLevel level = (ClientLevel) localPlayer.level();
			RandomSource random = level.random;
			Entity entity = level.getEntity(packet.itemId());

			// Usage of vanilla sound events: Subtitles are "Experience gained" and "Item picked up", which both apply
			if (entity != null) {
				if (entity instanceof ExperienceOrb) {
					level.playLocalSound(
							entity.getX(),
							entity.getY(),
							entity.getZ(),
							SoundEvents.EXPERIENCE_ORB_PICKUP,
							SoundSource.BLOCKS,
							0.1f,
							(random.nextFloat() - random.nextFloat()) * 0.35f + 0.9f,
							false
					);
				} else {
					level.playLocalSound(
							entity.getX(),
							entity.getY(),
							entity.getZ(),
							SoundEvents.ITEM_PICKUP,
							SoundSource.BLOCKS,
							0.2f,
							(random.nextFloat() - random.nextFloat()) * 1.4f + 2.0f,
							false
					);
				}

				Minecraft minecraft = Minecraft.getInstance();
				minecraft.particleEngine.add(new FlowerItemPickupParticle(minecraft.getEntityRenderDispatcher(),
						minecraft.renderBuffers(), level, entity, packet.pos(), packet.onFire()));
				if (entity instanceof ItemEntity itementity) {
					ItemStack itemstack = itementity.getItem();
					if (!itemstack.isEmpty()) {
						itemstack.shrink(packet.amount());
					}

					if (itemstack.isEmpty()) {
						level.removeEntity(packet.itemId(), Entity.RemovalReason.DISCARDED);
					}
				} else if (!(entity instanceof ExperienceOrb)) {
					level.removeEntity(packet.itemId(), Entity.RemovalReason.DISCARDED);
				}
			}
		}
	}
}
