package vazkii.botania.common.world;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.IChunkGenSettings;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import vazkii.botania.api.item.IFlowerlessBiome;
import vazkii.botania.api.item.IFlowerlessWorld;
import vazkii.botania.common.block.BlockModFlower;
import vazkii.botania.common.block.ModBlocks;

import javax.annotation.Nonnull;
import java.util.Random;

public class MysticalFlowerFeature extends Feature<MysticalFlowerConfig> {
    @Override
    public boolean place(@Nonnull IWorld world, @Nonnull IChunkGenerator<? extends IChunkGenSettings> generator, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull MysticalFlowerConfig config) {
        boolean flowers = true;
        if(world.getDimension() instanceof IFlowerlessWorld)
            flowers = ((IFlowerlessWorld) world.getDimension()).generateFlowers(world);
        else if(world.getBiome(pos) instanceof IFlowerlessBiome)
            flowers = ((IFlowerlessBiome) world.getBiome(pos)).canGenerateFlowers(world, pos.getX(), pos.getZ());

        if(!flowers)
            return false;

        int dist = Math.min(8, Math.max(1, config.getPatchSize()));
        for(int i = 0; i < config.getPatchCount(); i++) {
            if(rand.nextInt(config.getPatchChance()) == 0) {
                int x = pos.getX() + rand.nextInt(16) + 8;
                int z = pos.getZ() + rand.nextInt(16) + 8;
                int y = world.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, pos).getY();

                EnumDyeColor color = EnumDyeColor.byId(rand.nextInt(16));
                IBlockState flower = ModBlocks.getFlower(color).getDefaultState();

                for(int j = 0; j < config.getPatchDensity() * config.getPatchChance(); j++) {
                    int x1 = x + rand.nextInt(dist * 2) - dist;
                    int y1 = y + rand.nextInt(4) - rand.nextInt(4);
                    int z1 = z + rand.nextInt(dist * 2) - dist;
                    BlockPos pos2 = new BlockPos(x1, y1, z1);
                    if(world.isAirBlock(pos2) && (!world.getDimension().isNether() || y1 < 127) && flower.isValidPosition(world, pos2)) {
                        world.setBlockState(pos2, flower, 2);
                        if(rand.nextDouble() < config.getTallChance() && ((BlockModFlower) flower).canGrow(world, pos2, world.getBlockState(pos2), false)) {
                            Block block = ModBlocks.getDoubleFlower(color);
                            if(block instanceof BlockDoublePlant) {
                                ((BlockDoublePlant) block).placeAt(world, pos2, 3);
                            }
                        }
                    }
                }
            }
        }

        for(int i = 0; i < config.getMushroomPatchSize(); i++) {
            int x = pos.getX() + rand.nextInt(16) + 8;
            int z = pos.getZ() + rand.nextInt(16) + 8;
            int y = rand.nextInt(26) + 4;
            BlockPos pos3 = new BlockPos(x, y, z);
            EnumDyeColor color = EnumDyeColor.byId(rand.nextInt(16));
            IBlockState mushroom = ModBlocks.getMushroom(color).getDefaultState();
            if(world.isAirBlock(pos3) && mushroom.isValidPosition(world, pos3))
                world.setBlockState(pos3, mushroom, 2);
        }
        return false;
    }
}
