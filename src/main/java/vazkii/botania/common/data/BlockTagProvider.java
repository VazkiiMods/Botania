package vazkii.botania.common.data;

import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.BlockTags;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;
import java.util.Comparator;
import java.util.function.Predicate;

public class BlockTagProvider extends BlockTagsProvider {
    public BlockTagProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void registerTags() {
        Predicate<Block> botania = b -> LibMisc.MOD_ID.equals(b.getRegistryName().getNamespace());

        getBuilder(BlockTags.RAILS).add(ModBlocks.ghostRail);

        getBuilder(BlockTags.SLABS).add(registry.stream().filter(botania)
                .filter(b -> b instanceof SlabBlock)
                .sorted(Comparator.comparing(Block::getRegistryName))
                .toArray(Block[]::new));

        getBuilder(BlockTags.STAIRS).add(registry.stream().filter(botania)
                .filter(b -> b instanceof StairsBlock)
                .sorted(Comparator.comparing(Block::getRegistryName))
                .toArray(Block[]::new));

        getBuilder(BlockTags.WALLS).add(registry.stream().filter(botania)
                .filter(b -> b instanceof WallBlock)
                .sorted(Comparator.comparing(Block::getRegistryName))
                .toArray(Block[]::new));
    }

    @Nonnull
    @Override
    public String getName() {
        return "Botania block tags";
    }
}
