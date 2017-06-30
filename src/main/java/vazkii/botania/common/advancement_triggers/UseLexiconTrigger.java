package vazkii.botania.common.advancement_triggers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.critereon.AbstractCriterionInstance;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class UseLexiconTrigger implements ICriterionTrigger<UseLexiconTrigger.Instance> {
	public static final UseLexiconTrigger INSTANCE = new UseLexiconTrigger();
	private static final ResourceLocation ID = new ResourceLocation(LibMisc.MOD_ID, "use_lexicon");
	private final Map<PlayerAdvancements, ListenerSet> listeners = new HashMap<>();

	private UseLexiconTrigger() {}

	@Nonnull
	@Override
	public ResourceLocation getId() {
		return ID;
	}

	@Override
	public void addListener(@Nonnull PlayerAdvancements advancements, @Nonnull Listener<Instance> listener) {
		listeners.computeIfAbsent(advancements, ListenerSet::new).listeners.add(listener);
	}

	@Override
	public void removeListener(@Nonnull PlayerAdvancements advancements, @Nonnull Listener<Instance> listener) {
		if (listeners.containsKey(advancements)) {
			listeners.get(advancements).listeners.remove(listener);

			if (listeners.get(advancements).listeners.isEmpty()) {
				listeners.remove(advancements);
			}
		}
	}

	@Override
	public void removeAllListeners(@Nonnull PlayerAdvancements advancements) {
		listeners.remove(advancements);
	}

	@Nonnull
	@Override
	public Instance deserializeInstance(@Nonnull JsonObject json, @Nonnull JsonDeserializationContext context) {
		return new Instance(ItemPredicate.deserialize(json.get("item")));
	}

	public void trigger(EntityPlayerMP player, ItemStack lexicon) {
		ListenerSet set = listeners.get(player.getAdvancements());
		if(set != null) {
			set.trigger(player, lexicon);
		}
	}

	public static class Instance extends AbstractCriterionInstance {
		private final ItemPredicate item;

		public Instance(ItemPredicate item) {
			super(ID);
			this.item = item;
		}

		public boolean test(ItemStack lexicon) {
			return item.test(lexicon);
		}
	}

	public static class ListenerSet {
		private final PlayerAdvancements advancements;
		private final Set<Listener<Instance>> listeners = new HashSet<>();

		public ListenerSet(PlayerAdvancements advancements) {
			this.advancements = advancements;
		}

		public void trigger(EntityPlayerMP player, ItemStack lexicon) {
			List<Listener<Instance>> toTrigger = listeners.stream()
					.filter(l -> l.getCriterionInstance().test(lexicon))
					.collect(Collectors.toList());

			for (ICriterionTrigger.Listener<Instance> listener : toTrigger) {
				listener.grantCriterion(advancements);
			}
		}
	}

}
