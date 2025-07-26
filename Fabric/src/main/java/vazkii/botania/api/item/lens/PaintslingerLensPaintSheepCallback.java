package vazkii.botania.api.item.lens;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface PaintslingerLensPaintSheepCallback {
    Event<PaintslingerLensPaintSheepCallback> EVENT = EventFactory.createArrayBacked(PaintslingerLensPaintSheepCallback.class,
            listeners -> (player, sheep, oldColor, newColor, lens) -> {
                for (PaintslingerLensPaintSheepCallback listener : listeners) {
                    if (listener.onPaintslingerLensPaintSheep(player, sheep, oldColor, newColor, lens)) {
                        return true;
                    }
                }
                return false;
            });

    boolean onPaintslingerLensPaintSheep(Player player, Entity sheep, int oldColor, int newColor, ItemStack lens);
}