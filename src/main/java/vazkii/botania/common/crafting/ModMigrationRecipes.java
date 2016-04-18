package vazkii.botania.common.crafting;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import vazkii.botania.api.state.enums.FutureStoneVariant;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModFluffBlocks;
import vazkii.botania.common.item.ModItems;

/**
 * Migration recipes for things that will be removed in the future
 */
public final class ModMigrationRecipes {

    public static void init() {
        boolean quark = Botania.quarkLoaded;

        // Prismarine -> vanilla
        GameRegistry.addShapelessRecipe(new ItemStack(Items.prismarine_shard), new ItemStack(ModItems.manaResource, 1, 10));
        GameRegistry.addShapelessRecipe(new ItemStack(Blocks.prismarine, 1, 0), new ItemStack(ModBlocks.prismarine, 1, 0));
        GameRegistry.addShapelessRecipe(new ItemStack(Blocks.prismarine, 1, 1), new ItemStack(ModBlocks.prismarine, 1, 1));
        GameRegistry.addShapelessRecipe(new ItemStack(Blocks.prismarine, 1, 2), new ItemStack(ModBlocks.prismarine, 1, 2));
        GameRegistry.addShapelessRecipe(new ItemStack(Blocks.sea_lantern), new ItemStack(ModBlocks.seaLamp));

        // Prismarine -> quark
        if (quark) {
            GameRegistry.addShapelessRecipe(new ItemStack(findQuarkBlock("prismarine_rough_wall")), new ItemStack(ModFluffBlocks.prismarineWall));
            GameRegistry.addShapelessRecipe(new ItemStack(findQuarkBlock("prismarine_stairs")), new ItemStack(ModFluffBlocks.prismarineStairs));
            GameRegistry.addShapelessRecipe(new ItemStack(findQuarkBlock("prismarine_slab")), new ItemStack(ModFluffBlocks.prismarineSlab));
            GameRegistry.addShapelessRecipe(new ItemStack(findQuarkBlock("prismarine_bricks_stairs")), new ItemStack(ModFluffBlocks.prismarineBrickStairs));
            GameRegistry.addShapelessRecipe(new ItemStack(findQuarkBlock("prismarine_bricks_slab")), new ItemStack(ModFluffBlocks.prismarineBrickSlab));
            GameRegistry.addShapelessRecipe(new ItemStack(findQuarkBlock("prismarine_dark_stairs")), new ItemStack(ModFluffBlocks.darkPrismarineStairs));
            GameRegistry.addShapelessRecipe(new ItemStack(findQuarkBlock("prismarine_dark_slab")), new ItemStack(ModFluffBlocks.darkPrismarineSlab));
        }

        // End stuff -> vanilla
        GameRegistry.addShapelessRecipe(new ItemStack(Blocks.end_bricks), new ItemStack(ModBlocks.endStoneBrick));
        GameRegistry.addShapelessRecipe(new ItemStack(Blocks.purpur_block), new ItemStack(ModBlocks.endStoneBrick, 1, 2));
        GameRegistry.addShapelessRecipe(new ItemStack(Blocks.purpur_pillar), new ItemStack(ModBlocks.endStoneBrick, 1, 3));
        GameRegistry.addShapelessRecipe(new ItemStack(Blocks.purpur_slab), new ItemStack(ModFluffBlocks.enderBrickSlab));
        GameRegistry.addShapelessRecipe(new ItemStack(Blocks.purpur_stairs), new ItemStack(ModFluffBlocks.enderBrickStairs));

        // End stuff -> quark
        if (quark) {
            GameRegistry.addShapelessRecipe(new ItemStack(findQuarkBlock("end_bricks_stairs")), new ItemStack(ModFluffBlocks.endStoneStairs));
            GameRegistry.addShapelessRecipe(new ItemStack(findQuarkBlock("end_bricks_slab")), new ItemStack(ModFluffBlocks.endStoneSlab));
            // todo End stone chiseled brick? (not in quark)
        }

        // Andesite normal + polished -> vanilla
        GameRegistry.addShapelessRecipe(new ItemStack(Blocks.stone, 1, 5), new ItemStack(ModFluffBlocks.stone, 1, FutureStoneVariant.ANDESITE.ordinal()));
        GameRegistry.addShapelessRecipe(new ItemStack(Blocks.stone, 1, 6), new ItemStack(ModFluffBlocks.stone, 1, FutureStoneVariant.POLISHED_ANDESITE.ordinal()));

        if (quark) {
            // Andesite rough stairs and slab
            GameRegistry.addShapelessRecipe(new ItemStack(findQuarkBlock("stone_andesite_stairs")), new ItemStack(ModFluffBlocks.stoneStairs[0]));
            GameRegistry.addShapelessRecipe(new ItemStack(findQuarkBlock("stone_andesite_slab")), new ItemStack(ModFluffBlocks.stoneSlabs[0]));
            // Andesite bricks, brick stairs, brick slab
            GameRegistry.addShapelessRecipe(new ItemStack(findQuarkBlock("world_stone_bricks"), 1, 2), new ItemStack(ModFluffBlocks.stone, 1, FutureStoneVariant.ANDESITE_BRICK.ordinal()));
            GameRegistry.addShapelessRecipe(new ItemStack(findQuarkBlock("stone_andesite_bricks_stairs")), new ItemStack(ModFluffBlocks.stoneStairs[4]));
            GameRegistry.addShapelessRecipe(new ItemStack(findQuarkBlock("stone_andesite_bricks_slab")), new ItemStack(ModFluffBlocks.stoneSlabs[4]));
            // todo Andesite chiseled brick? (not in quark)
            // Andesite wall
            GameRegistry.addShapelessRecipe(new ItemStack(findQuarkBlock("stone_andesite_wall")), new ItemStack(ModFluffBlocks.stoneWall, 1, FutureStoneVariant.ANDESITE.ordinal()));
        }

        if (quark) {
            // Basalt normal + polished -> quark
            GameRegistry.addShapelessRecipe(new ItemStack(findQuarkBlock("basalt")), new ItemStack(ModFluffBlocks.stone, 1, FutureStoneVariant.BASALT.ordinal()));
            GameRegistry.addShapelessRecipe(new ItemStack(findQuarkBlock("basalt"), 1, 1), new ItemStack(ModFluffBlocks.stone, 1, FutureStoneVariant.POLISHED_BASALT.ordinal()));
            // Basalt rough stairs and slab
            GameRegistry.addShapelessRecipe(new ItemStack(findQuarkBlock("stone_basalt_stairs")), new ItemStack(ModFluffBlocks.stoneStairs[1]));
            GameRegistry.addShapelessRecipe(new ItemStack(findQuarkBlock("stone_basalt_slab")), new ItemStack(ModFluffBlocks.stoneSlabs[1]));
            // Basalt bricks, brick stairs, brick slab
            GameRegistry.addShapelessRecipe(new ItemStack(findQuarkBlock("world_stone_bricks"), 1, 3), new ItemStack(ModFluffBlocks.stone, 1, FutureStoneVariant.BASALT_BRICK.ordinal()));
            GameRegistry.addShapelessRecipe(new ItemStack(findQuarkBlock("stone_basalt_bricks_stairs")), new ItemStack(ModFluffBlocks.stoneStairs[5]));
            GameRegistry.addShapelessRecipe(new ItemStack(findQuarkBlock("stone_basalt_bricks_slab")), new ItemStack(ModFluffBlocks.stoneSlabs[5]));
            // todo Basalt chiseled brick? (not in quark)
            // Basalt wall
            GameRegistry.addShapelessRecipe(new ItemStack(findQuarkBlock("basalt_wall")), new ItemStack(ModFluffBlocks.stoneWall, 1, FutureStoneVariant.BASALT.ordinal()));
        }

        // Diorite normal + polished -> vanilla
        GameRegistry.addShapelessRecipe(new ItemStack(Blocks.stone, 1, 3), new ItemStack(ModFluffBlocks.stone, 1, FutureStoneVariant.DIORITE.ordinal()));
        GameRegistry.addShapelessRecipe(new ItemStack(Blocks.stone, 1, 4), new ItemStack(ModFluffBlocks.stone, 1, FutureStoneVariant.POLISHED_DIORITE.ordinal()));

        if (quark) {
            // Diorite rough stairs and slab
            GameRegistry.addShapelessRecipe(new ItemStack(findQuarkBlock("stone_diorite_stairs")), new ItemStack(ModFluffBlocks.stoneStairs[2]));
            GameRegistry.addShapelessRecipe(new ItemStack(findQuarkBlock("stone_diorite_slab")), new ItemStack(ModFluffBlocks.stoneSlabs[2]));
            // Diorite bricks, brick stairs, brick slab
            GameRegistry.addShapelessRecipe(new ItemStack(findQuarkBlock("world_stone_bricks"), 1, 1), new ItemStack(ModFluffBlocks.stone, 1, FutureStoneVariant.DIORITE_BRICK.ordinal()));
            GameRegistry.addShapelessRecipe(new ItemStack(findQuarkBlock("stone_diorite_bricks_stairs")), new ItemStack(ModFluffBlocks.stoneStairs[6]));
            GameRegistry.addShapelessRecipe(new ItemStack(findQuarkBlock("stone_diorite_bricks_slab")), new ItemStack(ModFluffBlocks.stoneSlabs[6]));
            // todo Diorite chiseled brick? (not in quark)
            // Diorite wall
            GameRegistry.addShapelessRecipe(new ItemStack(findQuarkBlock("stone_diorite_wall")), new ItemStack(ModFluffBlocks.stoneWall, 1, FutureStoneVariant.DIORITE.ordinal()));
        }

        // Granite normal + polished -> vanilla
        GameRegistry.addShapelessRecipe(new ItemStack(Blocks.stone, 1, 1), new ItemStack(ModFluffBlocks.stone, 1, FutureStoneVariant.GRANITE.ordinal()));
        GameRegistry.addShapelessRecipe(new ItemStack(Blocks.stone, 1, 2), new ItemStack(ModFluffBlocks.stone, 1, FutureStoneVariant.POLISHED_GRANITE.ordinal()));

        if (quark) {
            // Granite rough stairs and slab
            GameRegistry.addShapelessRecipe(new ItemStack(findQuarkBlock("stone_granite_stairs")), new ItemStack(ModFluffBlocks.stoneStairs[3]));
            GameRegistry.addShapelessRecipe(new ItemStack(findQuarkBlock("stone_granite_slab")), new ItemStack(ModFluffBlocks.stoneSlabs[3]));
            // Granite bricks, brick stairs, brick slab
            GameRegistry.addShapelessRecipe(new ItemStack(findQuarkBlock("world_stone_bricks"), 1, 0), new ItemStack(ModFluffBlocks.stone, 1, FutureStoneVariant.GRANITE_BRICK.ordinal()));
            GameRegistry.addShapelessRecipe(new ItemStack(findQuarkBlock("stone_granite_bricks_stairs")), new ItemStack(ModFluffBlocks.stoneStairs[7]));
            GameRegistry.addShapelessRecipe(new ItemStack(findQuarkBlock("stone_granite_bricks_slab")), new ItemStack(ModFluffBlocks.stoneSlabs[7]));
            // todo Granite chiseled brick? (not in quark)
            // Granite wall
            GameRegistry.addShapelessRecipe(new ItemStack(findQuarkBlock("stone_granite_wall")), new ItemStack(ModFluffBlocks.stoneWall, 1, FutureStoneVariant.GRANITE.ordinal()));
        }

        // Thatch
        if (quark) {
            GameRegistry.addShapelessRecipe(new ItemStack(findQuarkBlock("thatch")), new ItemStack(ModBlocks.thatch));
            GameRegistry.addShapelessRecipe(new ItemStack(findQuarkBlock("thatch_slab")), new ItemStack(ModFluffBlocks.thatchSlab));
            GameRegistry.addShapelessRecipe(new ItemStack(findQuarkBlock("thatch_stairs")), new ItemStack(ModFluffBlocks.thatchStairs));
        }

        // Reeds
        if (quark) {
            GameRegistry.addShapelessRecipe(new ItemStack(findQuarkBlock("reed_block")), new ItemStack(ModBlocks.reedBlock));
            GameRegistry.addShapelessRecipe(new ItemStack(findQuarkBlock("reed_block_slab")), new ItemStack(ModFluffBlocks.reedSlab));
            GameRegistry.addShapelessRecipe(new ItemStack(findQuarkBlock("reed_block_stairs")), new ItemStack(ModFluffBlocks.reedStairs));
            GameRegistry.addShapelessRecipe(new ItemStack(findQuarkBlock("reed_block_wall")), new ItemStack(ModFluffBlocks.reedWall));
        }
    }

    private static Block findQuarkBlock(String name) {
        return Block.blockRegistry.getObject(new ResourceLocation("Quark", name));
    }

    private ModMigrationRecipes() {}

}
