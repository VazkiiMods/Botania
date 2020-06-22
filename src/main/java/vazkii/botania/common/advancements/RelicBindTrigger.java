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
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;

import java.util.*;

public class RelicBindTrigger implements ICriterionTrigger<RelicBindTrigger.Instance> {
	public static final ResourceLocation ID = new ResourceLocation(LibMisc.MOD_ID, "relic_bind");
	public static final RelicBindTrigger INSTANCE = new RelicBindTrigger();
	private final Map<PlayerAdvancements, PlayerTracker> playerTrackers = new HashMap<>();

	private RelicBindTrigger() {}

	@Nonnull
	@Override
	public ResourceLocation getId() {
		return ID;
	}

	@Override
	public void addListener(@Nonnull PlayerAdvancements player, @Nonnull ICriterionTrigger.Listener<RelicBindTrigger.Instance> listener) {
		this.playerTrackers.computeIfAbsent(player, PlayerTracker::new).listeners.add(listener);
	}

	@Override
	public void removeListener(@Nonnull PlayerAdvancements player, @Nonnull ICriterionTrigger.Listener<RelicBindTrigger.Instance> listener) {
		PlayerTracker tracker = this.playerTrackers.get(player);

		if (tracker != null) {
			tracker.listeners.remove(listener);

			if (tracker.listeners.isEmpty()) {
				this.playerTrackers.remove(player);
			}
		}
	}

	@Override
	public void removeAllListeners(@Nonnull PlayerAdvancements playerAdvancementsIn) {
		playerTrackers.remove(playerAdvancementsIn);
	}

	@Nonnull
	@Override
	public Instance deserializeInstance(@Nonnull JsonObject json, @Nonnull JsonDeserializationContext context) {
		return new Instance(ItemPredicate.deserialize(json.get("relic")));
	}

	static class PlayerTracker {
		private final PlayerAdvancements playerAdvancements;
		final Set<Listener<Instance>> listeners = new HashSet<>();

		PlayerTracker(PlayerAdvancements playerAdvancementsIn) {
			this.playerAdvancements = playerAdvancementsIn;
		}

		public void trigger(ItemStack stack) {
			List<Listener<Instance>> list = new ArrayList<>();

			for (Listener<RelicBindTrigger.Instance> listener : this.listeners) {
				if (listener.getCriterionInstance().test(stack)) {
					list.add(listener);
				}
			}

			for (Listener<RelicBindTrigger.Instance> listener : list) {
				listener.grantCriterion(this.playerAdvancements);
			}
		}
	}

	public void trigger(ServerPlayerEntity player, ItemStack relic) {
		PlayerTracker tracker = playerTrackers.get(player.getAdvancements());
		if (tracker != null) {
			tracker.trigger(relic);
		}
	}

	static class Instance implements ICriterionInstance {
		private final ItemPredicate predicate;

		Instance(ItemPredicate predicate) {
			this.predicate = predicate;
		}

		@Nonnull
		@Override
		public ResourceLocation getId() {
			return ID;
		}

		boolean test(ItemStack stack) {
			return predicate.test(stack);
		}
	}
}
