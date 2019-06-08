package vazkii.botania.common.block.decor.quartz;

import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.block.ModFluffBlocks;
import vazkii.botania.common.lexicon.LexiconData;

public class BlockSpecialQuartzPillar extends RotatedPillarBlock implements ILexiconable {
    public BlockSpecialQuartzPillar(Properties properties) {
        super(properties);
    }

    @Override
    public LexiconEntry getEntry(World world, BlockPos pos, PlayerEntity player, ItemStack lexicon) {
        return this == ModFluffBlocks.elfQuartzPillar ? LexiconData.elvenResources : LexiconData.decorativeBlocks;
    }
}
