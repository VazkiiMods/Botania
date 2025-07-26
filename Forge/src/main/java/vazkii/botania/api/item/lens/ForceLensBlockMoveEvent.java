package vazkii.botania.api.item.lens;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public class ForceLensBlockMoveEvent extends Event {
    public final Player player;
    public final BlockPos from;
    public final BlockPos to;
    public final Direction direction;

    public ForceLensBlockMoveEvent(Player player, BlockPos from, BlockPos to, Direction direction) {
        this.player = player;
        this.from = from;
        this.to = to;
        this.direction = direction;
    }
}