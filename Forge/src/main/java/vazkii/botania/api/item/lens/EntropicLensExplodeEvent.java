package vazkii.botania.api.item.lens;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public class EntropicLensExplodeEvent extends Event {
    public final Player player;
    public final BlockPos pos;
    public final float explosionPower;
    public final ItemStack lens;

    public EntropicLensExplodeEvent(Player player, BlockPos pos, float explosionPower, ItemStack lens) {
        this.player = player;
        this.pos = pos;
        this.explosionPower = explosionPower;
        this.lens = lens;
    }
}