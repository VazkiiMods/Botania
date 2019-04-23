package vazkii.botania.api.imc;

import net.minecraft.block.Block;
import net.minecraft.item.EnumDyeColor;
import net.minecraftforge.registries.IRegistryDelegate;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class PaintableBlockMessage {
    private final IRegistryDelegate<Block> block;
    private final Function<EnumDyeColor, Block> transformer;

    /**
     * @param transformer Lookup function from color to destination block
     * @param block The block being converted
     */
    public PaintableBlockMessage(Function<EnumDyeColor, Block> transformer, Block block) {
        this.block = block.delegate;
        this.transformer = transformer;
    }

    public IRegistryDelegate<Block> getBlock() {
        return block;
    }

    public Function<EnumDyeColor, Block> getTransformer() {
        return transformer;
    }
}
