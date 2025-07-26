package vazkii.botania.api.item.lens;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface FlashLensManaFlameRemoveCallback {
    Event<FlashLensManaFlameRemoveCallback> EVENT = EventFactory.createArrayBacked(FlashLensManaFlameRemoveCallback.class,
            listeners -> (player, pos, lens) -> {
                for (FlashLensManaFlameRemoveCallback listener : listeners) {
                    if (listener.onFlashLensManaFlameRemove(player, pos, lens)) {
                        return true;
                    }
                }
                return false;
            });

    boolean onFlashLensManaFlameRemove(Player player, BlockPos pos, ItemStack lens);
}