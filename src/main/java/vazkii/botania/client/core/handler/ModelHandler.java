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
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.common.registry.GameData;
import vazkii.botania.api.state.enums.AltGrassVariant;
import vazkii.botania.api.state.enums.LivingRockVariant;
import vazkii.botania.api.state.enums.LivingWoodVariant;
import vazkii.botania.api.state.enums.PylonVariant;
import vazkii.botania.api.state.enums.StorageVariant;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModFluffBlocks;
import vazkii.botania.common.block.decor.slabs.BlockModSlab;
import vazkii.botania.common.item.ItemGaiaHead;
import vazkii.botania.common.lib.LibItemNames;
import vazkii.botania.common.lib.LibMisc;

import static vazkii.botania.common.item.ModItems.*;

import java.util.List;
import java.util.Locale;

public final class ModelHandler {

    // Very hardocode-y right now. Will be refactored after things work.
    // In addition, many of the jsons currently use the vanilla format (simple and verified bug-free).
    // Once things settle down, we'll move to forge jsons, which will drastically cut down on the number of json files
    public static void registerModels() {
        OBJLoader.instance.addDomain(LibMisc.MOD_ID.toLowerCase(Locale.ROOT));

        /** Custom statemappers **/
        registerStateMappers();

        /** ItemBlocks **/
        registerStandardBlocks();
        registerStorageItemBlocks();
        registerMushroomItemBlocks();
        registerFlowerItemBlocks();
        registerLivingRockWood();
        registerAltGrass();
        registerStairs();
        registerSlabs();
        registerFullSlabs();
        registerPanes();
        registerPylons();

        /** Normal Items **/
        registerStandardItems();
        registerManaResources();

        /** Special Item Meshers **/
    }
    private static void registerStandardBlocks() {
    	  registerItemModel(ModBlocks.manaGlass);
          registerItemModel(ModBlocks.elfGlass);
    }
    private static void registerStandardItems() {
        registerItemModel(pestleAndMortar);
        registerItemModel(blackLotus);
        registerItemModel(blackLotus, 1);
        registerItemModel(lexicon);

        registerItemModel(manasteelHelm);
        registerItemModel(manasteelChest);
        registerItemModel(manasteelLegs);
        registerItemModel(manasteelBoots);

        registerItemModel(manasteelPick);
        registerItemModel(manasteelShovel);
        registerItemModel(manasteelAxe);
        registerItemModel(manasteelShears);
        registerItemModel(manasteelSword);

        registerItemModel(elementiumHelm);
        registerItemModel(elementiumChest);
        registerItemModel(elementiumLegs);
        registerItemModel(elementiumBoots);

        registerItemModel(elementiumPick);
        registerItemModel(elementiumShovel);
        registerItemModel(elementiumAxe);
        registerItemModel(elementiumShears);
        registerItemModel(elementiumSword);

        registerItemModel(manaweaveHelm);
        registerItemModel(manaweaveChest);
        registerItemModel(manaweaveLegs);
        registerItemModel(manaweaveBoots);

        registerItemModel(terrasteelHelm);
        registerItemModel(terrasteelHelmRevealing);
        registerItemModel(terrasteelChest);
        registerItemModel(terrasteelLegs);
        registerItemModel(terrasteelBoots);

        registerItemModel(starSword);
        registerItemModel(thunderSword);
        registerItemModel(glassPick);
    }
    private static void registerManaResources() {
        Item item = manaResource;
        for (int i = 0; i < LibItemNames.MANA_RESOURCE_NAMES.length; i++) {
            String name = "botania:" + LibItemNames.MANA_RESOURCE_NAMES[i];
            ModelLoader.addVariantName(item, name);
            ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(name, "inventory"));
        }
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

    private static void registerAltGrass() {
        Item item = ItemGaiaHead.getItemFromBlock(ModBlocks.altGrass);
        for (AltGrassVariant variant : AltGrassVariant.values()) {
            String name = "botania:altGrass_" + variant.getName();
            ModelLoader.addVariantName(item, name);
            ModelLoader.setCustomModelResourceLocation(item, variant.ordinal(), new ModelResourceLocation(name, "inventory"));
        }
    }

    private static void registerStorageItemBlocks() {
        Item item = Item.getItemFromBlock(ModBlocks.storage);
        for (StorageVariant variant : StorageVariant.values()) {
            String name = "botania:storage_" + variant.getName();
            ModelLoader.addVariantName(item, name);
            ModelLoader.setCustomModelResourceLocation(item, variant.ordinal(), new ModelResourceLocation(name, "inventory"));
        }
    }

    private static void registerMushroomItemBlocks() {
        Item item = Item.getItemFromBlock(ModBlocks.mushroom);
        for (EnumDyeColor color : EnumDyeColor.values()) {
            String name = "botania:mushroom_" + color.getName();
            ModelLoader.addVariantName(item, name);
            ModelLoader.setCustomModelResourceLocation(item, color.getMetadata(), new ModelResourceLocation(name, "inventory"));
        }
    }

    private static void registerFlowerItemBlocks() {
        Item item = Item.getItemFromBlock(ModBlocks.flower);
        for (EnumDyeColor color : EnumDyeColor.values()) {
            String name = "botania:flower_" + color.getName();
            ModelLoader.addVariantName(item, name);
            ModelLoader.setCustomModelResourceLocation(item, color.getMetadata(), new ModelResourceLocation(name, "inventory"));
        }

        item = Item.getItemFromBlock(ModBlocks.shinyFlower);
        for (EnumDyeColor color : EnumDyeColor.values()) {
            String name = "botania:shinyFlower_" + color.getName();
            ModelLoader.addVariantName(item, name);
            ModelLoader.setCustomModelResourceLocation(item, color.getMetadata(), new ModelResourceLocation(name, "inventory"));
        }
    }

    private static void registerLivingRockWood() {
        Item item = Item.getItemFromBlock(ModBlocks.livingrock);
        for (LivingRockVariant variant : LivingRockVariant.values()) {
            String name = "botania:livingrock_" + variant.getName();
            ModelLoader.addVariantName(item, name);
            ModelLoader.setCustomModelResourceLocation(item, variant.ordinal(), new ModelResourceLocation(name, "inventory"));
        }

        item = Item.getItemFromBlock(ModBlocks.livingwood);
        for (LivingWoodVariant variant : LivingWoodVariant.values()) {
            String name = "botania:livingwood_" + variant.getName();
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

    private static void registerPanes() {

    }

    private static void registerPylons() {
        Item item = Item.getItemFromBlock(ModBlocks.pylon);
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation("botania:pylon", "variant=mana"));
        ModelLoader.setCustomModelResourceLocation(item, 1, new ModelResourceLocation("botania:pylon", "variant=natura"));
        ModelLoader.setCustomModelResourceLocation(item, 2, new ModelResourceLocation("botania:pylon", "variant=gaia"));
    }

    private static void registerItemModel(Block b) {
        registerItemModel(Item.getItemFromBlock(b));
    }
    private static void registerItemModel(Item i,int meta) {
        ResourceLocation loc = GameData.getItemRegistry().getNameForObject(i);
        ModelLoader.setCustomModelResourceLocation(i, meta, new ModelResourceLocation(loc, "inventory"));
    }
    private static void registerItemModel(Item i) {
        registerItemModel(i, 0);
    }

    private ModelHandler() {}
}