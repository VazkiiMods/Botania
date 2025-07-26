package vazkii.botania.api.item.lens;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public interface InfluenceLensMoveCallback {
    Event<InfluenceLensMoveCallback> EVENT = EventFactory.createArrayBacked(InfluenceLensMoveCallback.class,
            listeners -> (shooter, entity) -> {
                for (InfluenceLensMoveCallback listener : listeners) {
                    if (listener.onInfluenceLensMove(shooter, entity)) {
                        return true;
                    }
                }
                return false;
            });

    boolean onInfluenceLensMove(Player shooter, Entity entity);
}