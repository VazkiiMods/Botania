package vazkii.botania.api.item.lens;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public class BoreLensRemoveBlockEvent extends Event {
    public final Player shooter;
    public final BlockPos pos;

    public BoreLensRemoveBlockEvent(Player shooter, BlockPos pos) {
        this.shooter = shooter;
        this.pos = pos;
    }
}
