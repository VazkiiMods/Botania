package vazkii.botania.api.item.lens;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public interface PaintslingerLensPaintSheepCallback {
    Event<PaintslingerLensPaintSheepCallback> EVENT = EventFactory.createArrayBacked(PaintslingerLensPaintSheepCallback.class,
            listeners -> (player, sheep, oldColor, newColor) -> {
                for (PaintslingerLensPaintSheepCallback listener : listeners) {
                    if (listener.onPaintslingerLensPaintSheep(player, sheep, oldColor, newColor)) {
                        return true;
                    }
                }
                return false;
            });

    boolean onPaintslingerLensPaintSheep(Player player, Entity sheep, int oldColor, int newColor);
}