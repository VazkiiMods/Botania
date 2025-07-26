package vazkii.botania.api.item.lens;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public class PaintslingerLensPaintSheepEvent extends Event {
    public final Player player;
    public final Entity sheep;
    public final int oldColor;
    public final int newColor;

    public PaintslingerLensPaintSheepEvent(Player player, Entity sheep, int oldColor, int newColor) {
        this.player = player;
        this.sheep = sheep;
        this.oldColor = oldColor;
        this.newColor = newColor;
    }
}