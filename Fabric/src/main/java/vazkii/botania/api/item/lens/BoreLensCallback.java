package vazkii.botania.api.item.lens;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface BoreLensCallback {
    Event<BoreLensCallback> EVENT = EventFactory.createArrayBacked(BoreLensCallback.class,
            listeners -> (pl, pos) -> {
                for (BoreLensCallback listener : listeners) {
                    listener.onBoreLensCollideBurst(pl, pos);
                }
            });

    boolean onBoreLensCollideBurst(Player player, BlockPos pos);
}
