package vazkii.botania.api.item.lens;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;

public interface PaintslingerLensPaintBlockCallback {
    Event<PaintslingerLensPaintBlockCallback> EVENT = EventFactory.createArrayBacked(PaintslingerLensPaintBlockCallback.class,
            listeners -> (player, pos, color, originalBlock, newBlock) -> {
                for (PaintslingerLensPaintBlockCallback listener : listeners) {
                    if (listener.onPaintslingerLensPaintBlock(player, pos, color, originalBlock, newBlock)) {
                        return true;
                    }
                }
                return false;
            });

    boolean onPaintslingerLensPaintBlock(Player player, BlockPos pos, int color, Block originalBlock, Block newBlock);
}