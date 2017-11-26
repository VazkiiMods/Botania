package vazkii.botania.common.advancements;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.critereon.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.common.entity.EntityDoppleganger;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;
import java.util.*;

public class DopplegangerNoArmorTrigger implements ICriterionTrigger<DopplegangerNoArmorTrigger.Instance> {
    public static final ResourceLocation ID = new ResourceLocation(LibMisc.MOD_ID, "gaia_guardian_no_armor");
    public static final DopplegangerNoArmorTrigger INSTANCE = new DopplegangerNoArmorTrigger();
    private final Map<PlayerAdvancements, PlayerTracker> playerTrackers = new HashMap<>();

    private DopplegangerNoArmorTrigger() {}

    @Nonnull
    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public void addListener(@Nonnull PlayerAdvancements player, @Nonnull ICriterionTrigger.Listener<DopplegangerNoArmorTrigger.Instance> listener) {
        this.playerTrackers.computeIfAbsent(player, DopplegangerNoArmorTrigger.PlayerTracker::new).listeners.add(listener);
    }

    @Override
    public void removeListener(@Nonnull PlayerAdvancements player, @Nonnull ICriterionTrigger.Listener<DopplegangerNoArmorTrigger.Instance> listener) {
        DopplegangerNoArmorTrigger.PlayerTracker tracker = this.playerTrackers.get(player);

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
    public DopplegangerNoArmorTrigger.Instance deserializeInstance(@Nonnull JsonObject json, @Nonnull JsonDeserializationContext context) {
        return new DopplegangerNoArmorTrigger.Instance(EntityPredicate.deserialize(json.get("guardian")), DamageSourcePredicate.deserialize(json.get("killing_blow")));
    }

    static class PlayerTracker {
        private final PlayerAdvancements playerAdvancements;
        final Set<Listener<Instance>> listeners = new HashSet<>();

        PlayerTracker(PlayerAdvancements playerAdvancementsIn) {
            this.playerAdvancements = playerAdvancementsIn;
        }

        public void trigger(EntityPlayerMP player, EntityDoppleganger guardian, DamageSource src) {
            List<Listener<Instance>> list = new ArrayList<>();

            for(Listener<DopplegangerNoArmorTrigger.Instance> listener : this.listeners) {
                if(listener.getCriterionInstance().test(player, guardian, src)) {
                    list.add(listener);
                }
            }

            for(Listener<DopplegangerNoArmorTrigger.Instance> listener : list) {
                listener.grantCriterion(this.playerAdvancements);
            }
        }
    }

    public void trigger(EntityPlayerMP player, EntityDoppleganger guardian, DamageSource src) {
        DopplegangerNoArmorTrigger.PlayerTracker tracker = playerTrackers.get(player.getAdvancements());
        if(tracker != null) {
            tracker.trigger(player, guardian, src);
        }
    }

    static class Instance implements ICriterionInstance {
        private final EntityPredicate guardian;
        private final DamageSourcePredicate killingBlow;

        Instance(EntityPredicate count, DamageSourcePredicate indexPos) {
            this.guardian = count;
            this.killingBlow = indexPos;
        }

        @Nonnull
        @Override
        public ResourceLocation getId() {
            return ID;
        }

        boolean test(EntityPlayerMP player, EntityDoppleganger guardian, DamageSource src) {
            return this.guardian.test(player, guardian) && this.killingBlow.test(player, src);
        }
    }
}
