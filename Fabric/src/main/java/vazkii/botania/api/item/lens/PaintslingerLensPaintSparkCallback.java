package vazkii.botania.api.item.lens;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public interface PaintslingerLensPaintSparkCallback {
    Event<PaintslingerLensPaintSparkCallback> EVENT = EventFactory.createArrayBacked(PaintslingerLensPaintSparkCallback.class,
            listeners -> (player, spark, color) -> {
                for (PaintslingerLensPaintSparkCallback listener : listeners) {
                    if (listener.onPaintslingerLensPaintSpark(player, spark, color)) {
                        return true;
                    }
                }
                return false;
            });

    boolean onPaintslingerLensPaintSpark(Player player, Entity spark, int color);
}