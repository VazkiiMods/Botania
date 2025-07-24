package vazkii.botania.api.item.lens;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import vazkii.botania.api.internal.ManaBurst;

public interface LensUpdateBurstCallback {
    Event<LensUpdateBurstCallback> EVENT = EventFactory.createArrayBacked(LensUpdateBurstCallback.class,
            listeners -> (burst, stack, shooter) -> {
                for (LensUpdateBurstCallback listener : listeners) {
                    if (listener.onDamagingLensUpdateBurst(burst, stack, shooter)) {
                        return true;
                    }
                }
                return false;
            });

    boolean onDamagingLensUpdateBurst(ManaBurst burst, ItemStack stack, Player shooter);
}