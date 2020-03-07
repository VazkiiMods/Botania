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
import net.minecraft.advancements.criterion.LocationPredicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AlfPortalBreadTrigger implements ICriterionTrigger<AlfPortalBreadTrigger.Instance> {
	public static final ResourceLocation ID = new ResourceLocation(LibMisc.MOD_ID, "alf_portal_bread");
	public static final AlfPortalBreadTrigger INSTANCE = new AlfPortalBreadTrigger();
	private final Map<PlayerAdvancements, AlfPortalBreadTrigger.PlayerTracker> playerTrackers = new HashMap<>();

	private AlfPortalBreadTrigger() {}

	@Nonnull
	@Override
	public ResourceLocation getId() {
		return ID;
	}

	@Override
	public void addListener(@Nonnull PlayerAdvancements player, @Nonnull ICriterionTrigger.Listener<AlfPortalBreadTrigger.Instance> listener) {
		this.playerTrackers.computeIfAbsent(player, AlfPortalBreadTrigger.PlayerTracker::new).listeners.add(listener);
	}

	@Override
	public void removeListener(@Nonnull PlayerAdvancements player, @Nonnull ICriterionTrigger.Listener<AlfPortalBreadTrigger.Instance> listener) {
		AlfPortalBreadTrigger.PlayerTracker tracker = this.playerTrackers.get(player);

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
	public AlfPortalBreadTrigger.Instance deserializeInstance(@Nonnull JsonObject json, @Nonnull JsonDeserializationContext context) {
		return new AlfPortalBreadTrigger.Instance(LocationPredicate.deserialize(json.get("portal_location")));
	}

	static class PlayerTracker {
		private final PlayerAdvancements playerAdvancements;
		final Set<Listener<AlfPortalBreadTrigger.Instance>> listeners = new HashSet<>();

		PlayerTracker(PlayerAdvancements playerAdvancementsIn) {
			this.playerAdvancements = playerAdvancementsIn;
		}

		public void trigger(ServerWorld world, BlockPos portal) {
			List<Listener<AlfPortalBreadTrigger.Instance>> list = new ArrayList<>();

			for (ICriterionTrigger.Listener<AlfPortalBreadTrigger.Instance> listener : this.listeners) {
				if (listener.getCriterionInstance().test(world, portal)) {
					list.add(listener);
				}
			}

			for (ICriterionTrigger.Listener<AlfPortalBreadTrigger.Instance> listener : list) {
				listener.grantCriterion(this.playerAdvancements);
			}
		}
	}

	public void trigger(ServerPlayerEntity player, BlockPos portal) {
		AlfPortalBreadTrigger.PlayerTracker tracker = playerTrackers.get(player.getAdvancements());
		if (tracker != null) {
			tracker.trigger(player.getServerWorld(), portal);
		}
	}

	static class Instance implements ICriterionInstance {
		private final LocationPredicate portal;

		Instance(LocationPredicate portal) {
			this.portal = portal;
		}

		@Nonnull
		@Override
		public ResourceLocation getId() {
			return ID;
		}

		boolean test(ServerWorld world, BlockPos portal) {
			return this.portal.test(world, portal.getX(), portal.getY(), portal.getZ());
		}
	}
}
