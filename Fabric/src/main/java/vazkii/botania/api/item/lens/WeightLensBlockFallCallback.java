package vazkii.botania.api.item.lens;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public interface WeightLensBlockFallCallback {
    Event<WeightLensBlockFallCallback> EVENT = EventFactory.createArrayBacked(WeightLensBlockFallCallback.class,
            listeners -> (player, pos, state, lens) -> {
                for (WeightLensBlockFallCallback listener : listeners) {
                    if (listener.onWeightLensBlockFall(player, pos, state, lens)) {
                        return true;
                    }
                }
                return false;
            });

    boolean onWeightLensBlockFall(Player player, BlockPos pos, BlockState state, ItemStack lens);
}