package vazkii.botania.common.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;

import javax.annotation.Nonnull;

public class ItemTagProvider extends ItemTagsProvider {
    public ItemTagProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerTags() {
        this.copy(BlockTags.RAILS, ItemTags.RAILS);
        this.copy(BlockTags.SLABS, ItemTags.SLABS);
        this.copy(BlockTags.STAIRS, ItemTags.STAIRS);
        this.copy(BlockTags.WALLS, ItemTags.WALLS);
    }

    @Nonnull
    @Override
    public String getName() {
        return "Botania item tags";
    }
}
