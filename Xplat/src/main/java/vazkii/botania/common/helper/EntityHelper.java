package vazkii.botania.common.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.TicketType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;

public class EntityHelper {
	/**
	 * Shrinks the itemstack in an entity and ensures that the new size is sent to clients
	 */
	public static void shrinkItem(ItemEntity entity) {
		entity.getItem().shrink(1);
		syncItem(entity);
	}

	/**
	 * Forces an item entity to resync its item to the client.
	 *
	 * Entity data only resyncs when the old/new values are not Object.equals().
	 * Since stacks do not implement equals, and we're not changing the actual stack object,
	 * the old/new values are == and thus the game doesn't resync.
	 * We have to set a dummy value then set it back to tell the game to resync.
	 */
	public static void syncItem(ItemEntity entity) {
		var save = entity.getItem();
		entity.setItem(ItemStack.EMPTY);
		entity.setItem(save);
	}

	/**
	 * Adds an infinite-duration effect to a mob or player. The effect is not applied if the target already has the
	 * same effect at a higher level with any duration, or at the same level with infinite duration.
	 */
	public static void addStaticEffect(LivingEntity living, MobEffect mobEffect, int amplifier) {
		MobEffectInstance effect = living.getEffect(mobEffect);
		if (effect == null || effect.getAmplifier() < amplifier
				|| effect.getAmplifier() == amplifier && !effect.isInfiniteDuration()) {
			living.addEffect(new MobEffectInstance(mobEffect, MobEffectInstance.INFINITE_DURATION,
					amplifier, false, true, true));
		}
	}

	/**
	 * Replaces an infinite-duration effect on a mob or player with a finite-duration version of itself.
	 * The effect is only replaced if it has the expected level and infinite duration.
	 */
	public static void convertStaticEffectToFinite(LivingEntity living, MobEffect mobEffect, int expectedAmplifier, int duration) {
		MobEffectInstance effect = living.getEffect(mobEffect);
		if (effect != null && effect.getAmplifier() == expectedAmplifier && effect.isInfiniteDuration()) {
			living.removeEffect(mobEffect);
			living.addEffect(new MobEffectInstance(mobEffect, duration, expectedAmplifier, false, true, true));
		}
	}

	/**
	 * Removes an infinite-duration effect from a mob or player.
	 * The effect is only removed if it has the expected level and infinite duration.
	 */
	public static void removeStaticEffect(LivingEntity living, MobEffect mobEffect, int expectedAmplifier) {
		MobEffectInstance effect = living.getEffect(mobEffect);
		if (effect != null && effect.getAmplifier() == expectedAmplifier && effect.isInfiniteDuration()) {
			living.removeEffect(mobEffect);
		}
	}

	/**
	 * Creates a "post-teleport" chunk loading ticket for the specified entity if it is located at least two chunks
	 * away from the specified source position in one of the cardinal directions.
	 * The post-teleport ticket only keeps the target chunk loaded for a bit to potentially reduce loading/unloading
	 * related lag spikes, but does not necessarily make the target chunk entity-/redstone-processing.
	 */
	public static void addTeleportTicketIfFarAway(Entity entity, BlockPos sourcePos) {
		ChunkPos entityChunk = new ChunkPos(entity.blockPosition());
		ChunkPos sourceChunk = new ChunkPos(sourcePos);
		if (entity.level() instanceof ServerLevel serverLevel && entityChunk.getChessboardDistance(sourceChunk) > 2) {
			serverLevel.getChunkSource().addRegionTicket(TicketType.POST_TELEPORT, entityChunk, 0, entity.getId());
		}
	}
}
