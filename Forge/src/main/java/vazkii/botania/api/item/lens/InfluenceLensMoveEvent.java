package vazkii.botania.api.item.lens;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public class InfluenceLensMoveEvent extends Event {
    public final Player shooter;
    public final Entity entity;

    public InfluenceLensMoveEvent(Player shooter, Entity entity) {
        this.shooter = shooter;
        this.entity = entity;
    }
}