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

public record FlowerTakeItemEffectPacket(int itemId, BlockPos pos, int amount) implements CustomPacketPayload {

	public static final Type<FlowerTakeItemEffectPacket> ID = new Type<>(botaniaRL("ti"));
	public static final StreamCodec<ByteBuf, FlowerTakeItemEffectPacket> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT, FlowerTakeItemEffectPacket::itemId,
			BlockPos.STREAM_CODEC, FlowerTakeItemEffectPacket::pos,
			ByteBufCodecs.VAR_INT, FlowerTakeItemEffectPacket::amount,
			FlowerTakeItemEffectPacket::new
	);

	@Override
	public Type<FlowerTakeItemEffectPacket> type() {
		return ID;
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
						minecraft.renderBuffers(), level, entity, packet.pos()));
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
