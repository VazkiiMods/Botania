package vazkii.botania.data;

import net.minecraft.block.FenceBlock;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.FlowerBlock;
import net.minecraft.block.PaneBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ModelFile;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.common.block.BlockAltar;
import vazkii.botania.common.block.BlockModDoubleFlower;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.decor.BlockFloatingFlower;
import vazkii.botania.common.block.decor.BlockModMushroom;
import vazkii.botania.common.block.decor.BlockPetalBlock;
import vazkii.botania.common.block.string.BlockRedString;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;

import java.util.stream.IntStream;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class BlockstateProvider extends BlockStateProvider {
    public BlockstateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, LibMisc.MOD_ID, exFileHelper);
    }

    @Nonnull
    @Override
    public String getName() {
        return "Botania Blockstates";
    }

    @Override
    protected void registerStatesAndModels() {
        Registry.BLOCK.stream().filter(b -> LibMisc.MOD_ID.equals(b.getRegistryName().getNamespace()))
        .forEach(b -> {
            if (b instanceof FenceBlock || b instanceof FenceGateBlock || b instanceof PaneBlock
                    || b instanceof SlabBlock || b instanceof StairsBlock || b instanceof WallBlock
                    || b instanceof FlowerBlock || b instanceof BlockAltar || b == ModBlocks.tinyPotato
                    || b instanceof BlockRedString || b instanceof BlockFloatingFlower || b instanceof BlockModMushroom
                    || b instanceof BlockModDoubleFlower || b.getRegistryName().getPath().contains("quartz")
                    || b.getRegistryName().getPath().contains("metamorphic")
                    || b == ModBlocks.pump || b == ModBlocks.incensePlate || b == ModBlocks.felPumpkin || b == ModBlocks.solidVines)
                return;

            if (b == ModBlocks.gaiaHead || b == ModBlocks.gaiaHeadWall) {
                ModelFile file = models().getExistingFile(new ResourceLocation("block/soul_sand"));
                getVariantBuilder(b).partialState().setModels(new ConfiguredModel(file));
            } else if (b instanceof BlockPetalBlock) {
                ModelFile file = models().getExistingFile(prefix("block/petal_block"));
                getVariantBuilder(b).partialState().setModels(new ConfiguredModel(file));
            } else if (b == ModBlocks.elfGlass) {
                ConfiguredModel[] files = IntStream.rangeClosed(0, 3)
                        .mapToObj(i -> models().getExistingFile(prefix("block/" + b.getRegistryName().getPath() + "_" + i)))
                        .map(ConfiguredModel::new).toArray(ConfiguredModel[]::new);
                getVariantBuilder(b).partialState().setModels(files);
            } else if (b == ModBlocks.enderEye || b == ModBlocks.manaDetector) {
                ModelFile offFile = models().getExistingFile(prefix("block/" + b.getRegistryName().getPath()));
                ModelFile onFile = models().getExistingFile(prefix("block/" + b.getRegistryName().getPath() + "_powered"));
                getVariantBuilder(b).partialState().with(BotaniaStateProps.POWERED, false).setModels(new ConfiguredModel(offFile));
                getVariantBuilder(b).partialState().with(BotaniaStateProps.POWERED, true).setModels(new ConfiguredModel(onFile));
            } else {
                simpleBlock(b, models().getExistingFile(prefix("block/" + b.getRegistryName().getPath())));
            }
        });
    }
}
