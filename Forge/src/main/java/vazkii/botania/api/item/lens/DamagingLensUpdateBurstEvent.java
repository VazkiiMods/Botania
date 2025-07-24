package vazkii.botania.api.item.lens;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

import vazkii.botania.api.internal.ManaBurst;

@Cancelable
public class DamagingLensUpdateBurstEvent extends Event {
    public final ManaBurst burst;
    public final ItemStack stack;
    public final Player shooter;

    public DamagingLensUpdateBurstEvent(ManaBurst burst, ItemStack stack, Player shooter) {
        this.burst = burst;
        this.stack = stack;
        this.shooter = shooter;
    }
}