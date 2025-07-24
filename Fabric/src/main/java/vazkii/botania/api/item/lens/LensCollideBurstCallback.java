package vazkii.botania.api.item.lens;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface LensCollideBurstCallback {
    Event<LensCollideBurstCallback> EVENT = EventFactory.createArrayBacked(LensCollideBurstCallback.class,
            listeners -> (pl, pos, stack) -> {
                for (LensCollideBurstCallback listener : listeners) {
                    if (listener.onLensCollideBurst(pl, pos, stack)) {
                        return true;
                    }
                }
                return false;
            });

    boolean onLensCollideBurst(Player player, BlockPos pos, ItemStack stack);
}
