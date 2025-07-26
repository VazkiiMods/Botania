package vazkii.botania.api.item.lens;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface KindleLensNetherPortalRemoveCallback {
    Event<KindleLensNetherPortalRemoveCallback> EVENT = EventFactory.createArrayBacked(KindleLensNetherPortalRemoveCallback.class,
            listeners -> (player, pos, lens) -> {
                for (KindleLensNetherPortalRemoveCallback listener : listeners) {
                    if (listener.onKindleLensNetherPortalRemove(player, pos, lens)) {
                        return true;
                    }
                }
                return false;
            });

    boolean onKindleLensNetherPortalRemove(Player player, BlockPos pos, ItemStack lens);
}