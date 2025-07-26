package vazkii.botania.api.item.lens;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface EntropicLensExplodeCallback {
    Event<EntropicLensExplodeCallback> EVENT = EventFactory.createArrayBacked(EntropicLensExplodeCallback.class,
            listeners -> (player, pos, explosionPower, lens) -> {
                for (EntropicLensExplodeCallback listener : listeners) {
                    if (listener.onEntropicLensExplode(player, pos, explosionPower, lens)) {
                        return true;
                    }
                }
                return false;
            });

    boolean onEntropicLensExplode(Player player, BlockPos pos, float explosionPower, ItemStack lens);
}