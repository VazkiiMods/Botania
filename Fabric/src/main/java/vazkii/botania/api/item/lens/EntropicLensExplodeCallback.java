package vazkii.botania.api.item.lens;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;

public interface EntropicLensExplodeCallback {
    Event<EntropicLensExplodeCallback> EVENT = EventFactory.createArrayBacked(EntropicLensExplodeCallback.class,
            listeners -> (player, pos, explosionPower) -> {
                for (EntropicLensExplodeCallback listener : listeners) {
                    if (listener.onEntropicLensExplode(player, pos, explosionPower)) {
                        return true;
                    }
                }
                return false;
            });

    boolean onEntropicLensExplode(Player player, BlockPos pos, float explosionPower);
}