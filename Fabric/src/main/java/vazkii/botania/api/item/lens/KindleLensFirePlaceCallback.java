package vazkii.botania.api.item.lens;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;

public interface KindleLensFirePlaceCallback {
    Event<KindleLensFirePlaceCallback> EVENT = EventFactory.createArrayBacked(KindleLensFirePlaceCallback.class,
            listeners -> (player, pos) -> {
                for (KindleLensFirePlaceCallback listener : listeners) {
                    if (listener.onKindleLensFirePlace(player, pos)) {
                        return true;
                    }
                }
                return false;
            });

    boolean onKindleLensFirePlace(Player player, BlockPos pos);
}