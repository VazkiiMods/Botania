package vazkii.botania.api.item.lens;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;

public interface FlashLensManaFlameCreateCallback {
    Event<FlashLensManaFlameCreateCallback> EVENT = EventFactory.createArrayBacked(FlashLensManaFlameCreateCallback.class,
            listeners -> (player, pos, color) -> {
                for (FlashLensManaFlameCreateCallback listener : listeners) {
                    if (listener.onFlashLensManaFlameCreate(player, pos, color)) {
                        return true;
                    }
                }
                return false;
            });

    boolean onFlashLensManaFlameCreate(Player player, BlockPos pos, int color);
}