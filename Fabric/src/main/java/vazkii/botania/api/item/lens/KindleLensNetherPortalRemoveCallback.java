package vazkii.botania.api.item.lens;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;

public interface KindleLensNetherPortalRemoveCallback {
    Event<KindleLensNetherPortalRemoveCallback> EVENT = EventFactory.createArrayBacked(KindleLensNetherPortalRemoveCallback.class,
            listeners -> (player, pos) -> {
                for (KindleLensNetherPortalRemoveCallback listener : listeners) {
                    if (listener.onKindleLensNetherPortalRemove(player, pos)) {
                        return true;
                    }
                }
                return false;
            });

    boolean onKindleLensNetherPortalRemove(Player player, BlockPos pos);
}