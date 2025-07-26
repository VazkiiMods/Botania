package vazkii.botania.api.item.lens;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public class PaintslingerLensPaintSparkEvent extends Event {
    public final Player player;
    public final Entity spark;
    public final int color;
    public final ItemStack lens;

    public PaintslingerLensPaintSparkEvent(Player player, Entity spark, int color, ItemStack lens) {
        this.player = player;
        this.spark = spark;
        this.color = color;
        this.lens = lens;
    }
}