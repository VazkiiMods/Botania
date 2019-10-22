package vazkii.botania.common.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.advancements.criterion.EnchantmentPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.DoublePlantBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.data.LootTableProvider;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.state.properties.SlabType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.AlternativesLootEntry;
import net.minecraft.world.storage.loot.ConstantRange;
import net.minecraft.world.storage.loot.ItemLootEntry;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootParameterSets;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableManager;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.TableLootEntry;
import net.minecraft.world.storage.loot.conditions.BlockStateProperty;
import net.minecraft.world.storage.loot.conditions.MatchTool;
import net.minecraft.world.storage.loot.conditions.SurvivesExplosion;
import net.minecraft.world.storage.loot.functions.CopyName;
import net.minecraft.world.storage.loot.functions.ExplosionDecay;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraftforge.registries.ForgeRegistries;
import vazkii.botania.common.block.BlockAltGrass;
import vazkii.botania.common.block.BlockModDoubleFlower;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class BlockLootProvider implements IDataProvider {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final DataGenerator generator;

    public BlockLootProvider(DataGenerator generator) {
        this.generator = generator;
    }

    @Override
    public void act(DirectoryCache cache) throws IOException {
        Map<ResourceLocation, LootTable.Builder> tables = new HashMap<>();

        for (Block b : ForgeRegistries.BLOCKS) {
            if(!LibMisc.MOD_ID.equals(b.getRegistryName().getNamespace()))
                continue;
            if(b instanceof SlabBlock)
                tables.put(b.getRegistryName(), genSlab(b));
            else if(b instanceof BlockModDoubleFlower)
                tables.put(b.getRegistryName(), genDoubleFlower(b));
            else if (b instanceof BlockAltGrass)
                tables.put(b.getRegistryName(), genAltGrass(b));
            else if (b == ModBlocks.pistonRelay || b == ModBlocks.cocoon
                    || b == ModBlocks.manaFlame || b == ModBlocks.fakeAir || b == ModBlocks.bifrost)
                tables.put(b.getRegistryName(), empty());
            else if (b == ModBlocks.mossyAltar)
                tables.put(b.getRegistryName(), genRegular(ModBlocks.defaultAltar));
            else if (b == ModBlocks.enchantedSoil)
                tables.put(b.getRegistryName(), genRegular(Blocks.DIRT));
            else if (b == ModBlocks.enchanter)
                tables.put(b.getRegistryName(), genRegular(Blocks.LAPIS_BLOCK));
            else if (b == ModBlocks.root)
                tables.put(b.getRegistryName(), genRoot());
            else if (b == ModBlocks.solidVines)
                tables.put(b.getRegistryName(), genSolidVine());
            else if (b == ModBlocks.tinyPotato)
                tables.put(b.getRegistryName(), genTinyPotato(b));
            else if (b == ModBlocks.cellBlock)
                tables.put(b.getRegistryName(), genCellBlock(b));
            else if (b.getRegistryName().getPath().matches(LibBlockNames.METAMORPHIC_PREFIX + "\\w+" + "_stone"))
                tables.put(b.getRegistryName(), genMetamorphicStone(b));
            else
                tables.put(b.getRegistryName(), genRegular(b));
        }

        for (Map.Entry<ResourceLocation, LootTable.Builder> e : tables.entrySet()) {
            Path path = getPath(generator.getOutputFolder(), e.getKey());
            IDataProvider.save(GSON, cache, LootTableManager.toJson(e.getValue().setParameterSet(LootParameterSets.BLOCK).build()), path);
        }
    }

    private static Path getPath(Path root, ResourceLocation id) {
        return root.resolve("data/" + id.getNamespace() + "/loot_tables/blocks/" + id.getPath() + ".json");
    }

    private static LootTable.Builder empty() {
        return LootTable.builder();
    }

    private static LootTable.Builder genCellBlock(Block b) {
        ItemPredicate.Builder silkPred = ItemPredicate.Builder.create()
                .enchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.IntBound.atLeast(1)));
        LootEntry.Builder<?> silk = ItemLootEntry.builder(b)
                .acceptCondition(MatchTool.builder(silkPred));
        return LootTable.builder().addLootPool(LootPool.builder().name("main").rolls(ConstantRange.of(1)).addEntry(silk));
    }

    private static LootTable.Builder genTinyPotato(Block b) {
        LootEntry.Builder<?> entry = ItemLootEntry.builder(b)
                .acceptFunction(CopyName.builder(CopyName.Source.BLOCK_ENTITY));
        LootPool.Builder pool = LootPool.builder().name("main").rolls(ConstantRange.of(1)).addEntry(entry)
                .acceptCondition(SurvivesExplosion.builder());
        return LootTable.builder().addLootPool(pool);
    }

    private static LootTable.Builder genMetamorphicStone(Block b) {
        String cobbleName = b.getRegistryName().getPath().replaceAll("_stone", "_cobblestone");
        Block cobble = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(LibMisc.MOD_ID, cobbleName));
        if (cobble == Blocks.AIR)
            throw new RuntimeException("Couldn't find metamorphic cobble!");
        return genRegular(cobble);
    }

    private static LootTable.Builder genSolidVine() {
        LootEntry.Builder<?> entry = TableLootEntry.builder(new ResourceLocation("blocks/vine"));
        return LootTable.builder().addLootPool(LootPool.builder().name("main").rolls(ConstantRange.of(1)).addEntry(entry));
    }

    private static LootTable.Builder genRoot() {
        LootEntry.Builder<?> entry = ItemLootEntry.builder(ModItems.livingroot)
                .acceptFunction(SetCount.func_215932_a(RandomValueRange.of(2, 4)))
                .acceptFunction(ExplosionDecay.func_215863_b());
        return LootTable.builder().addLootPool(LootPool.builder().name("main").rolls(ConstantRange.of(1)).addEntry(entry));
    }

    private static LootTable.Builder genSlab(Block b) {
        LootEntry.Builder<?> entry = ItemLootEntry.builder(b)
                .acceptFunction(SetCount.func_215932_a(ConstantRange.of(2))
                        .acceptCondition(BlockStateProperty.builder(b).with(SlabBlock.TYPE, SlabType.DOUBLE)))
                .acceptFunction(ExplosionDecay.func_215863_b());
        return LootTable.builder().addLootPool(LootPool.builder().name("main").rolls(ConstantRange.of(1)).addEntry(entry));
    }

    private static LootTable.Builder genDoubleFlower(Block b) {
       LootEntry.Builder<?> entry = ItemLootEntry.builder(b)
               .acceptCondition(BlockStateProperty.builder(b).with(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER));
       LootPool.Builder pool = LootPool.builder().name("main").rolls(ConstantRange.of(1)).addEntry(entry)
               .acceptCondition(SurvivesExplosion.builder());
       return LootTable.builder().addLootPool(pool);
    }

    private static LootTable.Builder genAltGrass(Block b) {
        ItemPredicate.Builder silkPred = ItemPredicate.Builder.create()
                .enchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.IntBound.atLeast(1)));
        LootEntry.Builder<?> silk = ItemLootEntry.builder(b)
                .acceptCondition(MatchTool.builder(silkPred));
        LootEntry.Builder<?> dirt = ItemLootEntry.builder(Blocks.DIRT)
                .acceptCondition(SurvivesExplosion.builder());
        LootEntry.Builder<?> entry = AlternativesLootEntry.func_216149_a(silk, dirt);
        LootPool.Builder pool = LootPool.builder().name("main").rolls(ConstantRange.of(1)).addEntry(entry);
        return LootTable.builder().addLootPool(pool);
    }

    private static LootTable.Builder genRegular(Block b) {
        LootEntry.Builder<?> entry = ItemLootEntry.builder(b);
        LootPool.Builder pool = LootPool.builder().name("main").rolls(ConstantRange.of(1)).addEntry(entry)
                .acceptCondition(SurvivesExplosion.builder());
        return LootTable.builder().addLootPool(pool);
    }

    @Nonnull
    @Override
    public String getName() {
        return "Botania block loot tables";
    }
}
