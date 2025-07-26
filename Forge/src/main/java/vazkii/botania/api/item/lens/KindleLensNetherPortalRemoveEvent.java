package vazkii.botania.api.item.lens;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public class KindleLensNetherPortalRemoveEvent extends Event {
    public final Player player;
    public final BlockPos pos;

    public KindleLensNetherPortalRemoveEvent(Player player, BlockPos pos) {
        this.player = player;
        this.pos = pos;
    }
}