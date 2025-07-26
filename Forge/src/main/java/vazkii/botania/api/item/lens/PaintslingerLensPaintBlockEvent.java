package vazkii.botania.api.item.lens;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public class PaintslingerLensPaintBlockEvent extends Event {
    public final Player player;
    public final BlockPos pos;
    public final int color;
    public final Block originalBlock;
    public final Block newBlock;
    public final ItemStack lens;

    public PaintslingerLensPaintBlockEvent(Player player, BlockPos pos, int color, Block originalBlock, Block newBlock, ItemStack lens) {
        this.player = player;
        this.pos = pos;
        this.color = color;
        this.originalBlock = originalBlock;
        this.newBlock = newBlock;
        this.lens = lens;
    }
}