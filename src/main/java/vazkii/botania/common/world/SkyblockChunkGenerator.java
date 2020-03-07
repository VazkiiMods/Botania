package vazkii.botania.common.world;

import net.minecraft.world.IWorld;
import net.minecraft.world.biome.BiomeManager;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGeneratorType;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.OverworldChunkGenerator;
import net.minecraft.world.gen.OverworldGenSettings;
import net.minecraft.world.gen.WorldGenRegion;

public class SkyblockChunkGenerator extends OverworldChunkGenerator {
    public static final ChunkGeneratorType<OverworldGenSettings, SkyblockChunkGenerator> TYPE = new ChunkGeneratorType<>(SkyblockChunkGenerator::new, false, OverworldGenSettings::new);

    private SkyblockChunkGenerator(IWorld worldIn, BiomeProvider provider, OverworldGenSettings settingsIn) {
        super(worldIn, provider, settingsIn);
    }

    @Override
    public void makeBase(IWorld worldIn, IChunk chunkIn) {}

    @Override
    public void buildSurface(WorldGenRegion region, IChunk chunkIn) {}

    @Override
    public void decorate(WorldGenRegion region) {}

    @Override
    public void carve(BiomeManager biomeManager, IChunk chunkIn, GenerationStage.Carving carvingSettings) {}
}
