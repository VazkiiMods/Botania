package vazkii.botania.api.item.lens;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface KindleLensFirePlaceCallback {
    Event<KindleLensFirePlaceCallback> EVENT = EventFactory.createArrayBacked(KindleLensFirePlaceCallback.class,
            listeners -> (player, pos, lens) -> {
                for (KindleLensFirePlaceCallback listener : listeners) {
                    if (listener.onKindleLensFirePlace(player, pos, lens)) {
                        return true;
                    }
                }
                return false;
            });

    boolean onKindleLensFirePlace(Player player, BlockPos pos, ItemStack lens);
}