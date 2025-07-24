package vazkii.botania.api.item.lens;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;

public interface DamagingLensHurtCallback {
    Event<DamagingLensHurtCallback> EVENT = EventFactory.createArrayBacked(DamagingLensHurtCallback.class,
            listeners -> (shooter, pos) -> {
                for (DamagingLensHurtCallback listener : listeners) {
                    if (listener.onDamagingLensHurt(shooter, pos)) {
                        return true;
                    }
                }
                return false;
            });

    boolean onDamagingLensHurt(Player shooter, BlockPos pos);
}