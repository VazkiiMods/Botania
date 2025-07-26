package vazkii.botania.api.item.lens;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;

public interface StormLensLightningCallback {
    Event<StormLensLightningCallback> EVENT = EventFactory.createArrayBacked(StormLensLightningCallback.class,
            listeners -> (player, pos) -> {
                for (StormLensLightningCallback listener : listeners) {
                    if (listener.onStormLensLightning(player, pos)) {
                        return true;
                    }
                }
                return false;
            });

    boolean onStormLensLightning(Player player, BlockPos pos);
}