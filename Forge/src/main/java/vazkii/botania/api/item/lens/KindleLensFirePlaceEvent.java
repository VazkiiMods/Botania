package vazkii.botania.api.item.lens;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public class KindleLensFirePlaceEvent extends Event {
    public final Player player;
    public final BlockPos pos;
    public final ItemStack lens;

    public KindleLensFirePlaceEvent(Player player, BlockPos pos, ItemStack lens) {
        this.player = player;
        this.pos = pos;
        this.lens = lens;
    }
}