package vazkii.botania.api.item.lens;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.player.Player;

import vazkii.botania.api.mana.ManaSpreader;

public interface RedirectiveLensRotateSpreaderCallback {
    Event<RedirectiveLensRotateSpreaderCallback> EVENT = EventFactory.createArrayBacked(RedirectiveLensRotateSpreaderCallback.class,
            listeners -> (shooter, spreader) -> {
                for (RedirectiveLensRotateSpreaderCallback listener : listeners) {
                    if (listener.onRedirectiveLensRotateSpreader(shooter, spreader)) {
                        return true;
                    }
                }
                return false;
            });

    boolean onRedirectiveLensRotateSpreader(Player shooter, ManaSpreader spreader);
}