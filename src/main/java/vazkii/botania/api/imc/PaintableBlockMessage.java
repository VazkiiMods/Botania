package vazkii.botania.api.imc;

import net.minecraft.block.Block;
import net.minecraft.item.DyeColor;
import net.minecraftforge.registries.IRegistryDelegate;

import java.util.function.Function;

public class PaintableBlockMessage {
    private final IRegistryDelegate<Block> block;
    private final Function<DyeColor, Block> transformer;

    public PaintableBlockMessage(Function<DyeColor, Block> transformer, Block block) {
    	this(transformer, block.delegate);
    }

    /**
     * @param transformer Lookup function from color to destination block
     * @param block The block being converted
     */
    public PaintableBlockMessage(Function<DyeColor, Block> transformer, IRegistryDelegate<Block> block) {
        this.block = block;
        this.transformer = transformer;
    }

    public IRegistryDelegate<Block> getBlock() {
        return block;
    }

    public Function<DyeColor, Block> getTransformer() {
        return transformer;
    }
}
