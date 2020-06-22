/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.advancements;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;

import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;

import java.util.*;

public class LokiPlaceTrigger implements ICriterionTrigger<LokiPlaceTrigger.Instance> {
	public static final ResourceLocation ID = new ResourceLocation(LibMisc.MOD_ID, "loki_placed_blocks");
	public static final LokiPlaceTrigger INSTANCE = new LokiPlaceTrigger();
	private final Map<PlayerAdvancements, LokiPlaceTrigger.PlayerTracker> playerTrackers = new HashMap<>();

	private LokiPlaceTrigger() {}

	@Nonnull
	@Override
	public ResourceLocation getId() {
		return ID;
	}

	@Override
	public void addListener(@Nonnull PlayerAdvancements player, @Nonnull ICriterionTrigger.Listener<LokiPlaceTrigger.Instance> listener) {
		this.playerTrackers.computeIfAbsent(player, LokiPlaceTrigger.PlayerTracker::new).listeners.add(listener);
	}

	@Override
	public void removeListener(@Nonnull PlayerAdvancements player, @Nonnull ICriterionTrigger.Listener<LokiPlaceTrigger.Instance> listener) {
		LokiPlaceTrigger.PlayerTracker tracker = this.playerTrackers.get(player);

		if (tracker != null) {
			tracker.listeners.remove(listener);

			if (tracker.listeners.isEmpty()) {
				this.playerTrackers.remove(player);
			}
		}
	}

	@Override
	public void removeAllListeners(@Nonnull PlayerAdvancements player) {
		playerTrackers.remove(player);
	}

	@Nonnull
	@Override
	public LokiPlaceTrigger.Instance deserializeInstance(@Nonnull JsonObject json, @Nonnull JsonDeserializationContext context) {
		return new LokiPlaceTrigger.Instance(EntityPredicate.deserialize(json.get("player")), ItemPredicate.deserialize(json.get("ring")), MinMaxBounds.IntBound.fromJson(json.get("blocks_placed")));
	}

	static class PlayerTracker {
		private final PlayerAdvancements playerAdvancements;
		final Set<ICriterionTrigger.Listener<LokiPlaceTrigger.Instance>> listeners = new HashSet<>();

		PlayerTracker(PlayerAdvancements playerAdvancementsIn) {
			this.playerAdvancements = playerAdvancementsIn;
		}

		public void trigger(ServerPlayerEntity player, ItemStack ring, int blocksPlaced) {
			List<ICriterionTrigger.Listener<LokiPlaceTrigger.Instance>> list = new ArrayList<>();

			for (ICriterionTrigger.Listener<LokiPlaceTrigger.Instance> listener : this.listeners) {
				if (listener.getCriterionInstance().test(player, ring, blocksPlaced)) {
					list.add(listener);
				}
			}

			for (ICriterionTrigger.Listener<LokiPlaceTrigger.Instance> listener : list) {
				listener.grantCriterion(this.playerAdvancements);
			}
		}
	}

	public void trigger(ServerPlayerEntity player, ItemStack ring, int blocksPlaced) {
		LokiPlaceTrigger.PlayerTracker tracker = playerTrackers.get(player.getAdvancements());
		if (tracker != null) {
			tracker.trigger(player, ring, blocksPlaced);
		}
	}

	static class Instance implements ICriterionInstance {
		private final EntityPredicate player;
		private final ItemPredicate ring;
		private final MinMaxBounds.IntBound blocksPlaced;

		Instance(EntityPredicate player, ItemPredicate ring, MinMaxBounds.IntBound blocksPlaced) {
			this.player = player;
			this.ring = ring;
			this.blocksPlaced = blocksPlaced;
		}

		@Nonnull
		@Override
		public ResourceLocation getId() {
			return ID;
		}

		boolean test(ServerPlayerEntity player, ItemStack ring, int blocksPlaced) {
			return this.player.test(player, null) && this.ring.test(ring) && this.blocksPlaced.test(blocksPlaced);
		}
	}
}
