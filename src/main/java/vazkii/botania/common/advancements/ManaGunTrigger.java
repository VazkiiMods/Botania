package vazkii.botania.common.advancements;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.common.item.ItemManaGun;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class ManaGunTrigger implements ICriterionTrigger<ManaGunTrigger.Instance> {
	private static final ResourceLocation ID = prefix("fire_mana_blaster");

	public static final ManaGunTrigger INSTANCE = new ManaGunTrigger();
	private final Map<PlayerAdvancements, ManaGunTrigger.PlayerTracker> playerTrackers = new HashMap<>();

	private ManaGunTrigger() {}

	@Nonnull
	@Override
	public ResourceLocation getId() {
		return ID;
	}

	@Override
	public void addListener(@Nonnull PlayerAdvancements player, @Nonnull ICriterionTrigger.Listener<ManaGunTrigger.Instance> listener) {
		this.playerTrackers.computeIfAbsent(player, ManaGunTrigger.PlayerTracker::new).listeners.add(listener);
	}

	@Override
	public void removeListener(@Nonnull PlayerAdvancements player, @Nonnull ICriterionTrigger.Listener<ManaGunTrigger.Instance> listener) {
		ManaGunTrigger.PlayerTracker tracker = this.playerTrackers.get(player);

		if(tracker != null) {
			tracker.listeners.remove(listener);

			if(tracker.listeners.isEmpty()) {
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
	public ManaGunTrigger.Instance deserializeInstance(@Nonnull JsonObject json, @Nonnull JsonDeserializationContext context) {
		Boolean desu = json.get("desu") == null ? null : json.get("desu").getAsBoolean();
		return new ManaGunTrigger.Instance(ItemPredicate.deserialize(json.get("item")), EntityPredicate.deserialize(json.get("user")), desu);
	}

	static class PlayerTracker {
		private final PlayerAdvancements playerAdvancements;
		final Set<Listener<ManaGunTrigger.Instance>> listeners = new HashSet<>();

		PlayerTracker(PlayerAdvancements playerAdvancementsIn) {
			this.playerAdvancements = playerAdvancementsIn;
		}

		public void trigger(ServerPlayerEntity player, ItemStack stack) {
			List<Listener<ManaGunTrigger.Instance>> list = new ArrayList<>();

			for(Listener<ManaGunTrigger.Instance> listener : this.listeners) {
				if(listener.getCriterionInstance().test(stack, player)) {
					list.add(listener);
				}
			}

			for(Listener<ManaGunTrigger.Instance> listener : list) {
				listener.grantCriterion(this.playerAdvancements);
			}
		}
	}

	public void trigger(ServerPlayerEntity player, ItemStack stack) {
		ManaGunTrigger.PlayerTracker tracker = playerTrackers.get(player.getAdvancements());
		if(tracker != null) {
			tracker.trigger(player, stack);
		}
	}

	static class Instance implements ICriterionInstance {
		private final ItemPredicate item;
		private final EntityPredicate user;
		@Nullable
		private final Boolean desu;

		Instance(ItemPredicate count, EntityPredicate user, Boolean desu) {
			this.item = count;
			this.user = user;
			this.desu = desu;
		}

		@Nonnull
		@Override
		public ResourceLocation getId() {
			return ID;
		}

		boolean test(ItemStack stack, ServerPlayerEntity entity) {
			return this.item.test(stack) && this.user.test(entity, entity)
					&& (desu == null || desu == ItemManaGun.isSugoiKawaiiDesuNe(stack));
		}
	}
}
