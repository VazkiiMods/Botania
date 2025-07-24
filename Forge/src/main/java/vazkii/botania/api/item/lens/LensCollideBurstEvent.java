package vazkii.botania.api.item.lens;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public class LensCollideBurstEvent extends Event {
    public final Player shooter;
    public final BlockPos pos;
    public final ItemStack stack;

    public LensCollideBurstEvent(Player shooter, BlockPos pos, ItemStack stack) {
        this.shooter = shooter;
        this.pos = pos;
        this.stack = stack;
    }
}
