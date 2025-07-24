package vazkii.botania.api.item.lens;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public interface BoreLensRemoveBlockCallback {
    Event<BoreLensRemoveBlockCallback> EVENT = EventFactory.createArrayBacked(BoreLensRemoveBlockCallback.class,
            listeners -> (pl, pos) -> {
                for (BoreLensRemoveBlockCallback listener : listeners) {
                    if (listener.onBoreLensRemoveBlock(pl, pos)) {
                        return true;
                    }
                }
                return false;
            });

    boolean onBoreLensRemoveBlock(Player player, BlockPos pos);
}
