package vazkii.botania.common.advancements;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;
import java.util.*;

// Catch-all "used an item and it succeeded" trigger for Botania items, because making a separate
// trigger for each one is dumb.
public class UseItemSuccessTrigger implements ICriterionTrigger<UseItemSuccessTrigger.Instance> {
    public static final ResourceLocation ID = new ResourceLocation(LibMisc.MOD_ID, "use_item_success");
    public static final UseItemSuccessTrigger INSTANCE = new UseItemSuccessTrigger();
    private final Map<PlayerAdvancements, PlayerTracker> playerTrackers = new HashMap<>();

    private UseItemSuccessTrigger() {}

    @Nonnull
    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public void addListener(@Nonnull PlayerAdvancements player, @Nonnull ICriterionTrigger.Listener<UseItemSuccessTrigger.Instance> listener) {
        this.playerTrackers.computeIfAbsent(player, UseItemSuccessTrigger.PlayerTracker::new).listeners.add(listener);
    }

    @Override
    public void removeListener(@Nonnull PlayerAdvancements player, @Nonnull ICriterionTrigger.Listener<UseItemSuccessTrigger.Instance> listener) {
        UseItemSuccessTrigger.PlayerTracker tracker = this.playerTrackers.get(player);

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
    public UseItemSuccessTrigger.Instance deserializeInstance(@Nonnull JsonObject json, @Nonnull JsonDeserializationContext context) {
        return new UseItemSuccessTrigger.Instance(ItemPredicate.deserialize(json.get("item")), LocationPredicate.deserialize(json.get("location")));
    }

    static class PlayerTracker {
        private final PlayerAdvancements playerAdvancements;
        final Set<Listener<Instance>> listeners = new HashSet<>();

        PlayerTracker(PlayerAdvancements playerAdvancementsIn) {
            this.playerAdvancements = playerAdvancementsIn;
        }

        public void trigger(EntityPlayerMP player, ItemStack stack, WorldServer world, double x, double y, double z) {
            List<Listener<Instance>> list = new ArrayList<>();

            for(Listener<UseItemSuccessTrigger.Instance> listener : this.listeners) {
                if(listener.getCriterionInstance().test(stack, world, x, y, z)) {
                    list.add(listener);
                }
            }

            for(Listener<UseItemSuccessTrigger.Instance> listener : list) {
                listener.grantCriterion(this.playerAdvancements);
            }
        }
    }

    public void trigger(EntityPlayerMP player, ItemStack stack, WorldServer world, double x, double y, double z) {
        UseItemSuccessTrigger.PlayerTracker tracker = playerTrackers.get(player.getAdvancements());
        if(tracker != null) {
            tracker.trigger(player, stack, world, x, y, z);
        }
    }

    static class Instance implements ICriterionInstance {
        private final ItemPredicate item;
        private final LocationPredicate location;

        Instance(ItemPredicate count, LocationPredicate indexPos) {
            this.item = count;
            this.location = indexPos;
        }

        @Nonnull
        @Override
        public ResourceLocation getId() {
            return ID;
        }

        boolean test(ItemStack stack, WorldServer world, double x, double y, double z) {
            return this.item.test(stack) && this.location.test(world, x, y, z);
        }
    }
}
