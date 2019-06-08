package vazkii.botania.common.block;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;

public class BlockModLexiconable extends BlockMod implements ILexiconable {
    private final ILexiconable delegate;

    public BlockModLexiconable(Properties builder, ILexiconable delegate) {
        super(builder);
        this.delegate = delegate;
    }

    @Override
    public LexiconEntry getEntry(World world, BlockPos pos, PlayerEntity player, ItemStack lexicon) {
        return delegate.getEntry(world, pos, player, lexicon);
    }
}
