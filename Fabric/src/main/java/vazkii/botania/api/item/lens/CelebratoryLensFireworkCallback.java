package vazkii.botania.api.item.lens;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface CelebratoryLensFireworkCallback {
    Event<CelebratoryLensFireworkCallback> EVENT = EventFactory.createArrayBacked(CelebratoryLensFireworkCallback.class,
            listeners -> (player, pos, firework, lens) -> {
                for (CelebratoryLensFireworkCallback listener : listeners) {
                    if (listener.onCelebratoryLensFirework(player, pos, firework, lens)) {
                        return true;
                    }
                }
                return false;
            });

    boolean onCelebratoryLensFirework(Player player, BlockPos pos, ItemStack firework, ItemStack lens);
}