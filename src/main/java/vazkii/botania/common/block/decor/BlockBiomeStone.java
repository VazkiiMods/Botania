package vazkii.botania.common.block.decor;

import net.minecraft.block.BlockState;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.block.BlockMod;
import vazkii.botania.common.block.ModFluffBlocks;
import vazkii.botania.common.lexicon.LexiconData;

import javax.annotation.Nonnull;

public class BlockBiomeStone extends BlockMod implements ILexiconable {
    public BlockBiomeStone(Properties builder) {
        super(builder);
    }

    @Nonnull
    @Override
    public IItemProvider getItemDropped(BlockState state, World world, BlockPos pos, int fortune) {
        if(this == ModFluffBlocks.biomeStoneForest)
            return ModFluffBlocks.biomeCobblestoneForest;
        if(this == ModFluffBlocks.biomeStonePlains)
            return ModFluffBlocks.biomeCobblestonePlains;
        if(this == ModFluffBlocks.biomeStoneMountain)
            return ModFluffBlocks.biomeCobblestoneMountain;
        if(this == ModFluffBlocks.biomeStoneFungal)
            return ModFluffBlocks.biomeCobblestoneFungal;
        if(this == ModFluffBlocks.biomeStoneSwamp)
            return ModFluffBlocks.biomeCobblestoneSwamp;
        if(this == ModFluffBlocks.biomeStoneDesert)
            return ModFluffBlocks.biomeCobblestoneDesert;
        if(this == ModFluffBlocks.biomeStoneTaiga)
            return ModFluffBlocks.biomeCobblestoneTaiga;
        if(this == ModFluffBlocks.biomeStoneMesa)
            return ModFluffBlocks.biomeCobblestoneMesa;
        return Blocks.AIR;
    }

    @Override
    public LexiconEntry getEntry(World world, BlockPos pos, PlayerEntity player, ItemStack lexicon) {
        return LexiconData.marimorphosis;
    }
}
