package vazkii.botania.common.advancements;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;
import java.util.*;

public class CorporeaRequestTrigger implements ICriterionTrigger<CorporeaRequestTrigger.Instance> {
    public static final ResourceLocation ID = new ResourceLocation(LibMisc.MOD_ID, "corporea_index_request");
    public static final CorporeaRequestTrigger INSTANCE = new CorporeaRequestTrigger();
    private final Map<PlayerAdvancements, PlayerTracker> playerTrackers = new HashMap<>();

    private CorporeaRequestTrigger() {}

    @Nonnull
    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public void addListener(@Nonnull PlayerAdvancements player, @Nonnull ICriterionTrigger.Listener<CorporeaRequestTrigger.Instance> listener) {
        this.playerTrackers.computeIfAbsent(player, PlayerTracker::new).listeners.add(listener);
    }

    @Override
    public void removeListener(@Nonnull PlayerAdvancements player, @Nonnull ICriterionTrigger.Listener<CorporeaRequestTrigger.Instance> listener) {
        PlayerTracker tracker = this.playerTrackers.get(player);

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
    public Instance deserializeInstance(@Nonnull JsonObject json, @Nonnull JsonDeserializationContext context) {
        return new Instance(MinMaxBounds.deserialize(json.get("extracted")), LocationPredicate.deserialize(json.get("location")));
    }

    static class PlayerTracker {
        private final PlayerAdvancements playerAdvancements;
        final Set<Listener<Instance>> listeners = new HashSet<>();

        PlayerTracker(PlayerAdvancements playerAdvancementsIn) {
            this.playerAdvancements = playerAdvancementsIn;
        }

        public void trigger(WorldServer world, BlockPos pos, int count) {
            List<Listener<Instance>> list = new ArrayList<>();

            for(Listener<CorporeaRequestTrigger.Instance> listener : this.listeners) {
                if(listener.getCriterionInstance().test(world, pos, count)) {
                    list.add(listener);
                }
            }

            for(Listener<CorporeaRequestTrigger.Instance> listener : list) {
                listener.grantCriterion(this.playerAdvancements);
            }
        }
    }

    public void trigger(EntityPlayerMP player, WorldServer world, BlockPos pos, int count) {
        PlayerTracker tracker = playerTrackers.get(player.getAdvancements());
        if(tracker != null) {
            tracker.trigger(world, pos, count);
        }
    }

    static class Instance implements ICriterionInstance {
        private final MinMaxBounds count;
        private final LocationPredicate indexPos;

        Instance(MinMaxBounds count, LocationPredicate indexPos) {
            this.count = count;
            this.indexPos = indexPos;
        }

        @Nonnull
        @Override
        public ResourceLocation getId() {
            return ID;
        }

        boolean test(WorldServer world, BlockPos pos, int count) {
            return this.count.test(count) && this.indexPos.test(world, pos.getX(), pos.getY(), pos.getZ());
        }
    }

}
