package vazkii.botania.api.item.lens;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;

public interface FlashLensManaFlameRemoveCallback {
    Event<FlashLensManaFlameRemoveCallback> EVENT = EventFactory.createArrayBacked(FlashLensManaFlameRemoveCallback.class,
            listeners -> (player, pos) -> {
                for (FlashLensManaFlameRemoveCallback listener : listeners) {
                    if (listener.onFlashLensManaFlameRemove(player, pos)) {
                        return true;
                    }
                }
                return false;
            });

    boolean onFlashLensManaFlameRemove(Player player, BlockPos pos);
}