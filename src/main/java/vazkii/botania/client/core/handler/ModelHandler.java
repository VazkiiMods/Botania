package vazkii.botania.client.core.handler;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockWall;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameData;
import vazkii.botania.api.state.enums.LivingRockVariant;
import vazkii.botania.api.state.enums.LivingWoodVariant;
import vazkii.botania.api.state.enums.StorageVariant;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModFluffBlocks;
import vazkii.botania.common.block.decor.slabs.BlockModSlab;

import java.util.List;

public final class ModelHandler {

    // Very hardocode-y right now. Will be refactored after things work.
    // In addition, many of the jsons currently use the vanilla format (simple and verified bug-free).
    // Once things settle down, we'll move to forge jsons, which will drastically cut down on the number of json files
    public static void registerModels() {
        /** Custom statemappers **/
        registerStateMappers();

        /** ItemBlocks **/
        registerStorageItemBlocks();
        registerMushroomItemBlocks();
        registerFlowerItemBlocks();
        registerLivingRockWood();
        registerStairs();
        registerSlabs();
        registerFullSlabs();
        /** Normal Items **/

        /** Special Item Meshers **/
    }

    private static void registerStateMappers() {
        // Ignore vanilla variant in flowers
        ModelLoader.setCustomStateMapper(ModBlocks.flower, (new StateMap.Builder()).ignore(((BlockFlower) ModBlocks.flower).getTypeProperty()).build());
        ModelLoader.setCustomStateMapper(ModBlocks.shinyFlower, (new StateMap.Builder()).ignore(((BlockFlower) ModBlocks.shinyFlower).getTypeProperty()).build());
        ModelLoader.setCustomStateMapper(ModBlocks.buriedPetals, (new StateMap.Builder()).ignore(((BlockFlower) ModBlocks.buriedPetals).getTypeProperty()).build());
        ModelLoader.setCustomStateMapper(ModBlocks.specialFlower, (new StateMap.Builder()).ignore(((BlockFlower) ModBlocks.specialFlower).getTypeProperty()).build());

        // Ignore vanilla variant in walls
        ModelLoader.setCustomStateMapper(ModFluffBlocks.biomeStoneWall, (new StateMap.Builder()).ignore(BlockWall.VARIANT).build());
        ModelLoader.setCustomStateMapper(ModFluffBlocks.dreamwoodWall, (new StateMap.Builder()).ignore(BlockWall.VARIANT).build());
        ModelLoader.setCustomStateMapper(ModFluffBlocks.livingrockWall, (new StateMap.Builder()).ignore(BlockWall.VARIANT).build());
        ModelLoader.setCustomStateMapper(ModFluffBlocks.livingwoodWall, (new StateMap.Builder()).ignore(BlockWall.VARIANT).build());
        ModelLoader.setCustomStateMapper(ModFluffBlocks.prismarineWall, (new StateMap.Builder()).ignore(BlockWall.VARIANT).build());
        ModelLoader.setCustomStateMapper(ModFluffBlocks.reedWall, (new StateMap.Builder()).ignore(BlockWall.VARIANT).build());
        ModelLoader.setCustomStateMapper(ModFluffBlocks.stoneWall, (new StateMap.Builder()).ignore(BlockWall.VARIANT).build());

        // Ignore dummy variant in slabs
        for (Block b : ModFluffBlocks.biomeStoneSlabs) {
            ModelLoader.setCustomStateMapper(b, (new StateMap.Builder()).ignore(BlockModSlab.DUMMY).build());
        }

        for (Block b : ModFluffBlocks.pavementSlabs) {
            ModelLoader.setCustomStateMapper(b, (new StateMap.Builder()).ignore(BlockModSlab.DUMMY).build());
        }

        for (Block b : ModFluffBlocks.stoneSlabs) {
            ModelLoader.setCustomStateMapper(b, (new StateMap.Builder()).ignore(BlockModSlab.DUMMY).build());
        }

        List<Block> otherSlabs = ImmutableList.copyOf(new Block[] {
                ModFluffBlocks.livingwoodSlab, ModFluffBlocks.livingrockSlab, ModFluffBlocks.dreamwoodSlab, ModFluffBlocks.livingrockBrickSlab,
                ModFluffBlocks.dreamwoodPlankSlab, ModFluffBlocks.prismarineSlab, ModFluffBlocks.prismarineBrickSlab, ModFluffBlocks.darkPrismarineSlab,
                ModFluffBlocks.reedSlab, ModFluffBlocks.thatchSlab, ModFluffBlocks.netherBrickSlab, ModFluffBlocks.soulBrickSlab, ModFluffBlocks.snowBrickSlab,
                ModFluffBlocks.tileSlab, ModFluffBlocks.darkQuartzSlab, ModFluffBlocks.manaQuartzSlab, ModFluffBlocks.blazeQuartzSlab,
                ModFluffBlocks.lavenderQuartzSlab, ModFluffBlocks.redQuartzSlab, ModFluffBlocks.elfQuartzSlab, ModFluffBlocks.sunnyQuartzSlab
        });

        for (Block b : otherSlabs) {
            ModelLoader.setCustomStateMapper(b, (new StateMap.Builder()).ignore(BlockModSlab.DUMMY).build());
        }

        // Ignore both dummy variant and half prop in full slabs
        for (Block b : ModFluffBlocks.biomeStoneFullSlabs) {
            ModelLoader.setCustomStateMapper(b, (new StateMap.Builder()).ignore(BlockModSlab.DUMMY, BlockSlab.HALF).build());
        }

        for (Block b : ModFluffBlocks.pavementFullSlabs) {
            ModelLoader.setCustomStateMapper(b, (new StateMap.Builder()).ignore(BlockModSlab.DUMMY, BlockSlab.HALF).build());
        }

        for (Block b : ModFluffBlocks.stoneFullSlabs) {
            ModelLoader.setCustomStateMapper(b, (new StateMap.Builder()).ignore(BlockModSlab.DUMMY, BlockSlab.HALF).build());
        }

        List<Block> otherFullSlabs = ImmutableList.copyOf(new Block[] {
                ModFluffBlocks.livingwoodSlabFull, ModFluffBlocks.livingrockSlabFull, ModFluffBlocks.dreamwoodSlabFull, ModFluffBlocks.livingrockBrickSlabFull,
                ModFluffBlocks.dreamwoodPlankSlabFull, ModFluffBlocks.prismarineSlabFull, ModFluffBlocks.prismarineBrickSlabFull, ModFluffBlocks.darkPrismarineSlabFull,
                ModFluffBlocks.reedSlabFull, ModFluffBlocks.thatchSlabFull, ModFluffBlocks.netherBrickSlabFull, ModFluffBlocks.soulBrickSlabFull, ModFluffBlocks.snowBrickSlabFull,
                ModFluffBlocks.tileSlabFull, ModFluffBlocks.darkQuartzSlabFull, ModFluffBlocks.manaQuartzSlabFull, ModFluffBlocks.blazeQuartzSlabFull,
                ModFluffBlocks.lavenderQuartzSlabFull, ModFluffBlocks.redQuartzSlabFull, ModFluffBlocks.elfQuartzSlabFull, ModFluffBlocks.sunnyQuartzSlabFull
        });

        for (Block b : otherFullSlabs) {
            ModelLoader.setCustomStateMapper(b, (new StateMap.Builder()).ignore(BlockModSlab.DUMMY, BlockSlab.HALF).build());
        }
    }

    private static void registerStorageItemBlocks() {
        Item item = Item.getItemFromBlock(ModBlocks.storage);
        for (StorageVariant variant : StorageVariant.values()) {
            String name = "botania:" + "storage_" + variant.getName();
            ModelLoader.addVariantName(item, name);
            ModelLoader.setCustomModelResourceLocation(item, variant.ordinal(), new ModelResourceLocation(name, "inventory"));
        }
    }

    private static void registerMushroomItemBlocks() {
        Item item = Item.getItemFromBlock(ModBlocks.mushroom);
        for (EnumDyeColor color : EnumDyeColor.values()) {
            String name = "botania:" + "mushroom_" + color.getName();
            ModelLoader.addVariantName(item, name);
            ModelLoader.setCustomModelResourceLocation(item, color.getMetadata(), new ModelResourceLocation(name, "inventory"));
        }
    }

    private static void registerFlowerItemBlocks() {
        Item item = Item.getItemFromBlock(ModBlocks.flower);
        for (EnumDyeColor color : EnumDyeColor.values()) {
            String name = "botania:" + "flower_" + color.getName();
            ModelLoader.addVariantName(item, name);
            ModelLoader.setCustomModelResourceLocation(item, color.getMetadata(), new ModelResourceLocation(name, "inventory"));
        }

        item = Item.getItemFromBlock(ModBlocks.shinyFlower);
        for (EnumDyeColor color : EnumDyeColor.values()) {
            String name = "botania:" + "shinyFlower_" + color.getName();
            ModelLoader.addVariantName(item, name);
            ModelLoader.setCustomModelResourceLocation(item, color.getMetadata(), new ModelResourceLocation(name, "inventory"));
        }
    }

    private static void registerLivingRockWood() {
        Item item = Item.getItemFromBlock(ModBlocks.livingrock);
        for (LivingRockVariant variant : LivingRockVariant.values()) {
            String name = "botania:" + "livingrock_" + variant.getName();
            ModelLoader.addVariantName(item, name);
            ModelLoader.setCustomModelResourceLocation(item, variant.ordinal(), new ModelResourceLocation(name, "inventory"));
        }

        item = Item.getItemFromBlock(ModBlocks.livingwood);
        for (LivingWoodVariant variant : LivingWoodVariant.values()) {
            String name = "botania:" + "livingwood_" + variant.getName();
            ModelLoader.addVariantName(item, name);
            ModelLoader.setCustomModelResourceLocation(item, variant.ordinal(), new ModelResourceLocation(name, "inventory"));
        }
    }

    private static void registerStairs() {
        registerItemModel(ModFluffBlocks.livingwoodStairs);
        registerItemModel(ModFluffBlocks.livingwoodPlankStairs);
        registerItemModel(ModFluffBlocks.livingrockStairs);
        registerItemModel(ModFluffBlocks.livingrockBrickStairs);

    }

    private static void registerSlabs() {

    }

    private static void registerFullSlabs() {

    }

    private static void registerItemModel(Block b) {
        registerItemModel(Item.getItemFromBlock(b));
    }

    private static void registerItemModel(Item i) {
        ResourceLocation loc = GameData.getItemRegistry().getNameForObject(i);
        ModelLoader.setCustomModelResourceLocation(i, 0, new ModelResourceLocation(loc, "inventory"));
    }

    private ModelHandler() {}
}
