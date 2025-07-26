package vazkii.botania.api.item.lens;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

import vazkii.botania.api.mana.ManaSpreader;

@Cancelable
public class RedirectiveLensRotateSpreaderEvent extends Event {
    public final Player shooter;
    public final ManaSpreader spreader;

    public RedirectiveLensRotateSpreaderEvent(Player shooter, ManaSpreader spreader) {
        this.shooter = shooter;
        this.spreader = spreader;
    }
}