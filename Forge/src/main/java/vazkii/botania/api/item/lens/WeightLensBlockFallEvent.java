package vazkii.botania.api.item.lens;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public class WeightLensBlockFallEvent extends Event {
    public final Player player;
    public final BlockPos pos;
    public final BlockState state;
    public final ItemStack lens;

    public WeightLensBlockFallEvent(Player player, BlockPos pos, BlockState state, ItemStack lens) {
        this.player = player;
        this.pos = pos;
        this.state = state;
        this.lens = lens;
    }
}