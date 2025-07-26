package vazkii.botania.api.item.lens;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface ForceLensBlockMoveCallback {
    Event<ForceLensBlockMoveCallback> EVENT = EventFactory.createArrayBacked(ForceLensBlockMoveCallback.class,
            listeners -> (player, from, to, direction, lens) -> {
                for (ForceLensBlockMoveCallback listener : listeners) {
                    if (listener.onForceLensBlockMove(player, from, to, direction, lens)) {
                        return true;
                    }
                }
                return false;
            });

    boolean onForceLensBlockMove(Player player, BlockPos from, BlockPos to, Direction direction, ItemStack lens);
}