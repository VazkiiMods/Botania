/**
 * This class was created by <williewillus>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.core.handler;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockFlower;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import vazkii.botania.api.BotaniaAPIClient;
import vazkii.botania.api.item.IFloatingFlower;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.AltGrassVariant;
import vazkii.botania.api.state.enums.AltarVariant;
import vazkii.botania.api.state.enums.BiomeBrickVariant;
import vazkii.botania.api.state.enums.BiomeStoneVariant;
import vazkii.botania.api.state.enums.CrateVariant;
import vazkii.botania.api.state.enums.CustomBrickVariant;
import vazkii.botania.api.state.enums.DrumVariant;
import vazkii.botania.api.state.enums.LivingRockVariant;
import vazkii.botania.api.state.enums.LivingWoodVariant;
import vazkii.botania.api.state.enums.LuminizerVariant;
import vazkii.botania.api.state.enums.PlatformVariant;
import vazkii.botania.api.state.enums.PoolVariant;
import vazkii.botania.api.state.enums.PylonVariant;
import vazkii.botania.api.state.enums.SpreaderVariant;
import vazkii.botania.api.state.enums.StorageVariant;
import vazkii.botania.client.model.SpecialFlowerModel;
import vazkii.botania.client.render.IModelRegister;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModFluffBlocks;
import vazkii.botania.common.block.subtile.SubTileDecor;
import vazkii.botania.common.block.subtile.SubTileManastar;
import vazkii.botania.common.block.subtile.SubTilePureDaisy;
import vazkii.botania.common.block.subtile.functional.SubTileAgricarnation;
import vazkii.botania.common.block.subtile.functional.SubTileBellethorn;
import vazkii.botania.common.block.subtile.functional.SubTileBergamute;
import vazkii.botania.common.block.subtile.functional.SubTileBubbell;
import vazkii.botania.common.block.subtile.functional.SubTileClayconia;
import vazkii.botania.common.block.subtile.functional.SubTileDaffomill;
import vazkii.botania.common.block.subtile.functional.SubTileDreadthorn;
import vazkii.botania.common.block.subtile.functional.SubTileExoflame;
import vazkii.botania.common.block.subtile.functional.SubTileFallenKanade;
import vazkii.botania.common.block.subtile.functional.SubTileHeiseiDream;
import vazkii.botania.common.block.subtile.functional.SubTileHopperhock;
import vazkii.botania.common.block.subtile.functional.SubTileHyacidus;
import vazkii.botania.common.block.subtile.functional.SubTileJadedAmaranthus;
import vazkii.botania.common.block.subtile.functional.SubTileJiyuulia;
import vazkii.botania.common.block.subtile.functional.SubTileLoonuim;
import vazkii.botania.common.block.subtile.functional.SubTileMarimorphosis;
import vazkii.botania.common.block.subtile.functional.SubTileMedumone;
import vazkii.botania.common.block.subtile.functional.SubTileOrechid;
import vazkii.botania.common.block.subtile.functional.SubTileOrechidIgnem;
import vazkii.botania.common.block.subtile.functional.SubTilePollidisiac;
import vazkii.botania.common.block.subtile.functional.SubTileRannuncarpus;
import vazkii.botania.common.block.subtile.functional.SubTileSolegnolia;
import vazkii.botania.common.block.subtile.functional.SubTileSpectranthemum;
import vazkii.botania.common.block.subtile.functional.SubTileTangleberrie;
import vazkii.botania.common.block.subtile.functional.SubTileTigerseye;
import vazkii.botania.common.block.subtile.functional.SubTileVinculotus;
import vazkii.botania.common.block.subtile.generating.SubTileArcaneRose;
import vazkii.botania.common.block.subtile.generating.SubTileDandelifeon;
import vazkii.botania.common.block.subtile.generating.SubTileDaybloom;
import vazkii.botania.common.block.subtile.generating.SubTileEndoflame;
import vazkii.botania.common.block.subtile.generating.SubTileEntropinnyum;
import vazkii.botania.common.block.subtile.generating.SubTileGourmaryllis;
import vazkii.botania.common.block.subtile.generating.SubTileHydroangeas;
import vazkii.botania.common.block.subtile.generating.SubTileKekimurus;
import vazkii.botania.common.block.subtile.generating.SubTileMunchdew;
import vazkii.botania.common.block.subtile.generating.SubTileNarslimmus;
import vazkii.botania.common.block.subtile.generating.SubTileNightshade;
import vazkii.botania.common.block.subtile.generating.SubTileRafflowsia;
import vazkii.botania.common.block.subtile.generating.SubTileSpectrolus;
import vazkii.botania.common.block.subtile.generating.SubTileThermalily;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

import java.util.Locale;
import java.util.function.IntFunction;

public final class ModelHandler {

    public static void registerModels() {
        ModelLoaderRegistry.registerLoader(SpecialFlowerModel.Loader.INSTANCE);
        OBJLoader.INSTANCE.addDomain(LibMisc.MOD_ID.toLowerCase(Locale.ROOT));

        registerSubtiles();

        registerStandardBlocks();

        for (Block block : Block.REGISTRY) {
            if (block.getRegistryName().getResourceDomain().equalsIgnoreCase(LibMisc.MOD_ID)
                    && block instanceof IModelRegister)
                ((IModelRegister) block).registerModels();
        }

        for (Item item : Item.REGISTRY) {
            if (item.getRegistryName().getResourceDomain().equalsIgnoreCase(LibMisc.MOD_ID)
                    && item instanceof IModelRegister)
                ((IModelRegister) item).registerModels();
        }
    }

    private static void registerSubtiles() {
        BotaniaAPIClient.registerIslandTypeModel(IFloatingFlower.IslandType.GRASS, new ModelResourceLocation("botania:miniIsland", "variant=grass"));
        BotaniaAPIClient.registerIslandTypeModel(IFloatingFlower.IslandType.PODZOL, new ModelResourceLocation("botania:miniIsland", "variant=podzol"));
        BotaniaAPIClient.registerIslandTypeModel(IFloatingFlower.IslandType.MYCEL, new ModelResourceLocation("botania:miniIsland", "variant=mycel"));
        BotaniaAPIClient.registerIslandTypeModel(IFloatingFlower.IslandType.SNOW, new ModelResourceLocation("botania:miniIsland", "variant=snow"));
        BotaniaAPIClient.registerIslandTypeModel(IFloatingFlower.IslandType.DRY, new ModelResourceLocation("botania:miniIsland", "variant=dry"));
        BotaniaAPIClient.registerIslandTypeModel(IFloatingFlower.IslandType.GOLDEN, new ModelResourceLocation("botania:miniIsland", "variant=golden"));
        BotaniaAPIClient.registerIslandTypeModel(IFloatingFlower.IslandType.VIVID, new ModelResourceLocation("botania:miniIsland", "variant=vivid"));
        BotaniaAPIClient.registerIslandTypeModel(IFloatingFlower.IslandType.SCORCHED, new ModelResourceLocation("botania:miniIsland", "variant=scorched"));
        BotaniaAPIClient.registerIslandTypeModel(IFloatingFlower.IslandType.INFUSED, new ModelResourceLocation("botania:miniIsland", "variant=infused"));
        BotaniaAPIClient.registerIslandTypeModel(IFloatingFlower.IslandType.MUTATED, new ModelResourceLocation("botania:miniIsland", "variant=mutated"));

        BotaniaAPIClient.registerSubtileModel(SubTileManastar.class, new ModelResourceLocation("botania:" + LibBlockNames.SUBTILE_MANASTAR));
        BotaniaAPIClient.registerSubtileModel(SubTilePureDaisy.class, new ModelResourceLocation("botania:" + LibBlockNames.SUBTILE_PUREDAISY));

        BotaniaAPIClient.registerSubtileModel(SubTileDaybloom.class, new ModelResourceLocation("botania:" + LibBlockNames.SUBTILE_DAYBLOOM));
        BotaniaAPIClient.registerSubtileModel(SubTileDecor.Daybloom.class, new ModelResourceLocation("botania:" + LibBlockNames.SUBTILE_DAYBLOOM + "Decor"));
        BotaniaAPIClient.registerSubtileModel(SubTileDaybloom.Prime.class, new ModelResourceLocation("botania:" + LibBlockNames.SUBTILE_DAYBLOOM_PRIME));
        BotaniaAPIClient.registerSubtileModel(SubTileEndoflame.class, new ModelResourceLocation("botania:" + LibBlockNames.SUBTILE_ENDOFLAME));
        BotaniaAPIClient.registerSubtileModel(SubTileHydroangeas.class, new ModelResourceLocation("botania:" + LibBlockNames.SUBTILE_HYDROANGEAS));
        BotaniaAPIClient.registerSubtileModel(SubTileDecor.Hydroangeas.class, new ModelResourceLocation("botania:" + LibBlockNames.SUBTILE_HYDROANGEAS + "Decor"));
        BotaniaAPIClient.registerSubtileModel(SubTileThermalily.class, new ModelResourceLocation("botania:" + LibBlockNames.SUBTILE_THERMALILY));
        BotaniaAPIClient.registerSubtileModel(SubTileNightshade.class, new ModelResourceLocation("botania:" + LibBlockNames.SUBTILE_NIGHTSHADE));
        BotaniaAPIClient.registerSubtileModel(SubTileDecor.Nightshade.class, new ModelResourceLocation("botania:" + LibBlockNames.SUBTILE_NIGHTSHADE + "Decor"));
        BotaniaAPIClient.registerSubtileModel(SubTileNightshade.Prime.class, new ModelResourceLocation("botania:" + LibBlockNames.SUBTILE_NIGHTSHADE_PRIME));
        BotaniaAPIClient.registerSubtileModel(SubTileArcaneRose.class, new ModelResourceLocation("botania:" + LibBlockNames.SUBTILE_ARCANE_ROSE));
        BotaniaAPIClient.registerSubtileModel(SubTileMunchdew.class, new ModelResourceLocation("botania:" + LibBlockNames.SUBTILE_MUNCHDEW));
        BotaniaAPIClient.registerSubtileModel(SubTileEntropinnyum.class, new ModelResourceLocation("botania:" + LibBlockNames.SUBTILE_ENTROPINNYUM));
        BotaniaAPIClient.registerSubtileModel(SubTileKekimurus.class, new ModelResourceLocation("botania:" + LibBlockNames.SUBTILE_KEKIMURUS));
        BotaniaAPIClient.registerSubtileModel(SubTileGourmaryllis.class, new ModelResourceLocation("botania:" + LibBlockNames.SUBTILE_GOURMARYLLIS));
        BotaniaAPIClient.registerSubtileModel(SubTileNarslimmus.class, new ModelResourceLocation("botania:" + LibBlockNames.SUBTILE_NARSLIMMUS));
        BotaniaAPIClient.registerSubtileModel(SubTileSpectrolus.class, new ModelResourceLocation("botania:" + LibBlockNames.SUBTILE_SPECTROLUS));
        BotaniaAPIClient.registerSubtileModel(SubTileDandelifeon.class, new ModelResourceLocation("botania:" + LibBlockNames.SUBTILE_DANDELIFEON));
        BotaniaAPIClient.registerSubtileModel(SubTileRafflowsia.class, new ModelResourceLocation("botania:" + LibBlockNames.SUBTILE_RAFFLOWSIA));

        BotaniaAPIClient.registerSubtileModel(SubTileBellethorn.class, new ModelResourceLocation("botania:" + LibBlockNames.SUBTILE_BELLETHORN));
        BotaniaAPIClient.registerSubtileModel(SubTileBellethorn.Mini.class, new ModelResourceLocation("botania:" + LibBlockNames.SUBTILE_BELLETHORN + "Chibi"));
        BotaniaAPIClient.registerSubtileModel(SubTileDreadthorn.class, new ModelResourceLocation("botania:" + LibBlockNames.SUBTILE_DREADTHORN));
        BotaniaAPIClient.registerSubtileModel(SubTileHeiseiDream.class, new ModelResourceLocation("botania:" + LibBlockNames.SUBTILE_HEISEI_DREAM));
        BotaniaAPIClient.registerSubtileModel(SubTileTigerseye.class, new ModelResourceLocation("botania:" + LibBlockNames.SUBTILE_TIGERSEYE));
        BotaniaAPIClient.registerSubtileModel(SubTileJadedAmaranthus.class, new ModelResourceLocation("botania:" + LibBlockNames.SUBTILE_JADED_AMARANTHUS));
        BotaniaAPIClient.registerSubtileModel(SubTileOrechid.class, new ModelResourceLocation("botania:" + LibBlockNames.SUBTILE_ORECHID));
        BotaniaAPIClient.registerSubtileModel(SubTileOrechidIgnem.class, new ModelResourceLocation("botania:" + LibBlockNames.SUBTILE_ORECHID_IGNEM));
        BotaniaAPIClient.registerSubtileModel(SubTileFallenKanade.class, new ModelResourceLocation("botania:" + LibBlockNames.SUBTILE_FALLEN_KANADE));
        BotaniaAPIClient.registerSubtileModel(SubTileExoflame.class, new ModelResourceLocation("botania:" + LibBlockNames.SUBTILE_EXOFLAME));
        BotaniaAPIClient.registerSubtileModel(SubTileAgricarnation.class, new ModelResourceLocation("botania:" + LibBlockNames.SUBTILE_AGRICARNATION));
        BotaniaAPIClient.registerSubtileModel(SubTileAgricarnation.Mini.class, new ModelResourceLocation("botania:" + LibBlockNames.SUBTILE_AGRICARNATION + "Chibi"));
        BotaniaAPIClient.registerSubtileModel(SubTileHopperhock.class, new ModelResourceLocation("botania:" + LibBlockNames.SUBTILE_HOPPERHOCK));
        BotaniaAPIClient.registerSubtileModel(SubTileHopperhock.Mini.class, new ModelResourceLocation("botania:" + LibBlockNames.SUBTILE_HOPPERHOCK + "Chibi"));
        BotaniaAPIClient.registerSubtileModel(SubTileTangleberrie.class, new ModelResourceLocation("botania:" + LibBlockNames.SUBTILE_TANGLEBERRIE));
        BotaniaAPIClient.registerSubtileModel(SubTileJiyuulia.class, new ModelResourceLocation("botania:" + LibBlockNames.SUBTILE_JIYUULIA));
        BotaniaAPIClient.registerSubtileModel(SubTileRannuncarpus.class, new ModelResourceLocation("botania:" + LibBlockNames.SUBTILE_RANNUNCARPUS));
        BotaniaAPIClient.registerSubtileModel(SubTileRannuncarpus.Mini.class, new ModelResourceLocation("botania:" + LibBlockNames.SUBTILE_RANNUNCARPUS + "Chibi"));
        BotaniaAPIClient.registerSubtileModel(SubTileHyacidus.class, new ModelResourceLocation("botania:" + LibBlockNames.SUBTILE_HYACIDUS));
        BotaniaAPIClient.registerSubtileModel(SubTilePollidisiac.class, new ModelResourceLocation("botania:" + LibBlockNames.SUBTILE_POLLIDISIAC));
        BotaniaAPIClient.registerSubtileModel(SubTileClayconia.class, new ModelResourceLocation("botania:" + LibBlockNames.SUBTILE_CLAYCONIA));
        BotaniaAPIClient.registerSubtileModel(SubTileClayconia.Mini.class, new ModelResourceLocation("botania:" + LibBlockNames.SUBTILE_CLAYCONIA + "Chibi"));
        BotaniaAPIClient.registerSubtileModel(SubTileLoonuim.class, new ModelResourceLocation("botania:" + LibBlockNames.SUBTILE_LOONIUM));
        BotaniaAPIClient.registerSubtileModel(SubTileDaffomill.class, new ModelResourceLocation("botania:" + LibBlockNames.SUBTILE_DAFFOMILL));
        BotaniaAPIClient.registerSubtileModel(SubTileVinculotus.class, new ModelResourceLocation("botania:" + LibBlockNames.SUBTILE_VINCULOTUS));
        BotaniaAPIClient.registerSubtileModel(SubTileSpectranthemum.class, new ModelResourceLocation("botania:" + LibBlockNames.SUBTILE_SPECTRANTHEMUM));
        BotaniaAPIClient.registerSubtileModel(SubTileMedumone.class, new ModelResourceLocation("botania:" + LibBlockNames.SUBTILE_MEDUMONE));
        BotaniaAPIClient.registerSubtileModel(SubTileMarimorphosis.class, new ModelResourceLocation("botania:" + LibBlockNames.SUBTILE_MARIMORPHOSIS));
        BotaniaAPIClient.registerSubtileModel(SubTileMarimorphosis.Mini.class, new ModelResourceLocation("botania:" + LibBlockNames.SUBTILE_MARIMORPHOSIS + "Chibi"));
        BotaniaAPIClient.registerSubtileModel(SubTileBubbell.class, new ModelResourceLocation("botania:" + LibBlockNames.SUBTILE_BUBBELL));
        BotaniaAPIClient.registerSubtileModel(SubTileBubbell.Mini.class, new ModelResourceLocation("botania:" + LibBlockNames.SUBTILE_BUBBELL + "Chibi"));
        BotaniaAPIClient.registerSubtileModel(SubTileSolegnolia.class, new ModelResourceLocation("botania:" + LibBlockNames.SUBTILE_SOLEGNOLIA));
        BotaniaAPIClient.registerSubtileModel(SubTileSolegnolia.Mini.class, new ModelResourceLocation("botania:" + LibBlockNames.SUBTILE_SOLEGNOLIA + "Chibi"));
        BotaniaAPIClient.registerSubtileModel(SubTileBergamute.class, new ModelResourceLocation("botania:" + LibBlockNames.SUBTILE_BERGAMUTE));
    }

    private static void registerStandardBlocks() {
        registerBlockCustomPath(ModBlocks.avatar, "avatar");
        registerBlockCustomPath(ModBlocks.bellows, "bellows");
        registerBlockCustomPath(ModBlocks.brewery, "brewery");
        registerBlockVariantInventory(ModBlocks.corporeaCrystalCube);
        registerBlockCustomPath(ModBlocks.corporeaIndex, "corporea_index");
        registerBlockCustomPath(ModBlocks.hourglass, "hovering_hourglass");
        registerBlockCustomPath(ModBlocks.teruTeruBozu, "teru_teru_bozu");

        registerBlockVariantNormal(ModBlocks.alchemyCatalyst);
        registerBlockVariant(ModBlocks.alfPortal, "state=off");
        registerBlockCustomPath(ModFluffBlocks.alfglassPane, "alfglass_pane");
        registerBlockVariantNormal(ModBlocks.bifrost);
        registerBlockCustomPath(ModFluffBlocks.bifrostPane, "bifrost_pane");
        registerBlockVariantNormal(ModBlocks.bifrostPerm);
        registerBlockCustomPathMetas(ModFluffBlocks.biomeStoneWall, 8, i -> BiomeStoneVariant.values()[i + 8].getName() + "_wall");
        registerBlockVariantNormal(ModBlocks.blazeBlock);
        registerBlockVariant(ModBlocks.cacophonium, "powered=false");
        registerBlockVariantNormal(ModBlocks.cellBlock);
        registerBlockVariantNormal(ModBlocks.conjurationCatalyst);
        registerBlockVariantNormal(ModBlocks.cocoon);
        registerBlockVariant(ModBlocks.corporeaFunnel, "powered=false");
        registerBlockVariant(ModBlocks.corporeaInterceptor, "powered=false");
        registerBlockVariant(ModBlocks.corporeaRetainer, "powered=false");
        registerBlockVariantNormal(ModBlocks.dirtPath);
        registerBlockCustomPathMetas(ModBlocks.doubleFlower1, 8, i -> "double_flower_" + EnumDyeColor.byMetadata(i).getName());
        registerBlockCustomPathMetas(ModBlocks.doubleFlower2, 8, i -> "double_flower_" + EnumDyeColor.byMetadata(i + 8).getName());
        registerBlockVariantNormal(ModBlocks.distributor);
        registerBlockCustomPath(ModFluffBlocks.dreamwoodWall, "dreamwood_wall");
        registerBlockVariantNormal(ModBlocks.elfGlass);
        registerBlockVariantNormal(ModBlocks.enchantedSoil);
        registerBlockVariant(ModBlocks.enchanter, "facing=x");
        registerBlockVariant(ModBlocks.enderEye, "powered=false");
        registerBlockVariant(ModBlocks.felPumpkin, "facing=north");
        registerBlockVariantInventory(ModBlocks.floatingSpecialFlower);
        registerBlockCustomPathMetas(ModBlocks.flower, EnumDyeColor.values().length, i -> "flower_" + EnumDyeColor.byMetadata(i).getName());
        registerBlockVariantNormal(ModBlocks.forestEye);
        registerBlockCustomPath(ModBlocks.ghostRail, "ghost_rail");
        registerBlockVariant(ModBlocks.incensePlate, "facing=south");
        registerBlockVariant(ModBlocks.lightLauncher, "powered=false");
        registerBlockCustomPath(ModFluffBlocks.livingrockWall, "livingrock_wall");
        registerBlockCustomPath(ModFluffBlocks.livingwoodWall, "livingwood_wall");
        registerBlockVariantNormal(ModBlocks.manaBomb);
        registerBlockVariant(ModBlocks.manaDetector, "powered=false");
        registerBlockVariantNormal(ModBlocks.manaGlass);
        registerBlockCustomPath(ModFluffBlocks.managlassPane, "managlass_pane");
        registerBlockVariantNormal(ModBlocks.manaVoid);
        registerBlockCustomPathMetas(ModBlocks.mushroom, EnumDyeColor.values().length, i -> "mushroom_" + EnumDyeColor.byMetadata(i).getName());
        // Explicit mapping needed because saved meta does not match EnumDyeColor
        EnumDyeColor[] manualMapping = { EnumDyeColor.WHITE, EnumDyeColor.BLACK, EnumDyeColor.BLUE, EnumDyeColor.RED, EnumDyeColor.YELLOW, EnumDyeColor.GREEN };
        registerBlockVariantMetas(ModFluffBlocks.pavement, 6, i -> "color=" + manualMapping[i].getName());
        registerBlockVariant(ModBlocks.prism, "has_lens=false,powered=false");
        registerBlockVariantNormal(ModBlocks.pistonRelay);
        registerBlockVariantInventory(ModBlocks.pump);
        registerBlockVariant(ModBlocks.redStringComparator, "facing=north");
        registerBlockVariant(ModBlocks.redStringContainer, "facing=north");
        registerBlockVariant(ModBlocks.redStringDispenser, "facing=north");
        registerBlockVariant(ModBlocks.redStringFertilizer, "facing=north");
        registerBlockVariant(ModBlocks.redStringInterceptor, "facing=north");
        registerBlockVariant(ModBlocks.redStringRelay, "facing=north");
        registerBlockVariantNormal(ModBlocks.rfGenerator);
        registerBlockVariantNormal(ModBlocks.root);
        registerBlockVariantNormal(ModBlocks.runeAltar);
        registerBlockVariantNormal(ModBlocks.shimmerrock);
        registerBlockVariantNormal(ModBlocks.shimmerwoodPlanks);
        registerBlockCustomPathMetas(ModBlocks.shinyFlower, EnumDyeColor.values().length, i -> "glimmering_flower_" + EnumDyeColor.byMetadata(i).getName());
        registerBlockVariant(ModBlocks.sparkChanger, "powered=false");
        registerBlockVariantNormal(ModBlocks.spawnerClaw);
        registerBlockVariantInventory(ModBlocks.specialFlower);
        registerBlockVariant(ModBlocks.starfield, "powered=false");
        registerBlockVariantNormal(ModBlocks.terraPlate);
        registerBlockVariantNormal(ModBlocks.tinyPlanet);
        registerBlockVariant(ModBlocks.tinyPotato, "facing=north");
        registerBlockVariantNormal(ModBlocks.turntable);

        // Register all metas to variant inventory, so the smartmodel can take over from there. See MiscellaneousIcons
        registerItemAllMeta(Item.getItemFromBlock(ModBlocks.floatingFlower), EnumDyeColor.values().length);

        // Item models which all use the same base model and recolored by render layer
        registerBlockVariantMetas(ModBlocks.manaBeacon, EnumDyeColor.values().length, "normal");
        registerBlockVariantMetas(ModBlocks.petalBlock, EnumDyeColor.values().length, "normal");
        registerBlockVariantMetas(ModBlocks.unstableBlock, EnumDyeColor.values().length, "normal");

        // Blocks which share models with their item, and have only one variant to switch over
        registerVariantsDefaulted(ModBlocks.pylon, PylonVariant.class, "variant");
        registerVariantsDefaulted(ModBlocks.altar, AltarVariant.class, "variant");
        registerVariantsDefaulted(ModBlocks.altGrass, AltGrassVariant.class, "variant");
        registerVariantsDefaulted(ModFluffBlocks.biomeStoneA, BiomeStoneVariant.class, "variant");
        registerVariantsDefaulted(ModFluffBlocks.biomeStoneB, BiomeBrickVariant.class, "variant");
        registerVariantsDefaulted(ModBlocks.customBrick, CustomBrickVariant.class, "variant");
        registerVariantsDefaulted(ModBlocks.dreamwood, LivingWoodVariant.class, "variant");
        registerVariantsDefaulted(ModBlocks.forestDrum, DrumVariant.class, "variant");
        registerVariantsDefaulted(ModBlocks.lightRelay, LuminizerVariant.class, "powered=false,variant");
        registerVariantsDefaulted(ModBlocks.livingrock, LivingRockVariant.class, "variant");
        registerVariantsDefaulted(ModBlocks.livingwood, LivingWoodVariant.class, "variant");
        registerVariantsDefaulted(ModBlocks.openCrate, CrateVariant.class, "pattern=none,variant");
        registerVariantsDefaulted(ModBlocks.platform, PlatformVariant.class, "variant");
        registerVariantsDefaulted(ModBlocks.pool, PoolVariant.class, "variant");
        registerVariantsDefaulted(ModBlocks.spreader, SpreaderVariant.class, "variant");
        // todo 1.8 this is temporary until animation API is done, currnetly item model textures will not match true spreader texture
        registerVariantsDefaulted(ModBlocks.storage, StorageVariant.class, "variant");
    }

    private static <T extends Enum<T> & IStringSerializable> void registerVariantsDefaulted(Block b, Class<T> enumclazz, String variantHeader) {
        Item item = Item.getItemFromBlock(b);
        for (T e : enumclazz.getEnumConstants()) {
            String baseName = ForgeRegistries.BLOCKS.getKey(b).toString();
            String variantName = variantHeader + "=" + e.getName();
            ModelLoader.setCustomModelResourceLocation(item, e.ordinal(), new ModelResourceLocation(baseName, variantName));
        }
    }

    /**
     * Registers all metas of the given item to models/item/registryname.json
     */
    public static void registerItemAllMeta(Item item, int range) {
        registerItemMetas(item, range, i -> item.getRegistryName().getResourcePath());
    }

    /**
     * Registers each meta of an item to models/item/registryname<meta>.json
     * Range is exclusive upper bound
     */
    public static void registerItemAppendMeta(Item item, int maxExclusive, String loc) {
        registerItemMetas(item, maxExclusive, i -> loc + i);
    }

    public static void registerItemMetas(Item item, int maxExclusive, IntFunction<String> metaToName) {
        for (int i = 0; i < maxExclusive; i++) {
            ModelLoader.setCustomModelResourceLocation(
                    item, i,
                    new ModelResourceLocation(LibMisc.MOD_ID + ":" + metaToName.apply(i), "inventory")
            );
        }
    }

    // -- Registers the ItemBlock model to be the Block's model, of the specified variant -- //
    private static void registerBlockVariantNormal(Block b) {
        registerBlockVariant(b, "normal");
    }

    // Registers the ItemBlock to models/item/<registryname>#inventory
    private static void registerBlockVariantInventory(Block b) {
        registerBlockVariant(b, "inventory");
    }

    public static void registerBlockVariant(Block b, String variant) {
        registerBlockVariantMetas(b, 1, variant);
    }

    private static void registerBlockVariantMetas(Block b, int maxExclusive, String variant) {
        registerBlockVariantMetas(b, maxExclusive, i -> variant);
    }

    public static void registerBlockVariantMetas(Block b, int maxExclusive, IntFunction<String> metaToVariant) {
        Item item = Item.getItemFromBlock(b);
        for (int i = 0; i < maxExclusive; i++) {
            ModelLoader.setCustomModelResourceLocation(
                    item, i,
                    new ModelResourceLocation(item.getRegistryName(), metaToVariant.apply(i))
            );
        }
    }

    // Registers the ItemBlock to a custom path in models/item/itemblock/
    private static void registerBlockCustomPath(Block b, String path) {
        registerBlockCustomPathMetas(b, 1, path);
    }

    private static void registerBlockCustomPathMetas(Block b, int maxExclusive, String path) {
        registerBlockCustomPathMetas(b, maxExclusive, i -> path);
    }

    private static void registerBlockCustomPathMetas(Block b, int maxExclusive, IntFunction<String> metaToPath) {
        Item item = Item.getItemFromBlock(b);
        for (int i = 0; i < maxExclusive; i++) {
            ModelLoader.setCustomModelResourceLocation(
                    item, i,
                    new ModelResourceLocation(LibMisc.MOD_ID + ":itemblock/" + metaToPath.apply(i), "inventory")
            );
        }
    }

    public static void registerItemModel(Item i,int meta) {
        ResourceLocation loc = i.getRegistryName();
        ModelLoader.setCustomModelResourceLocation(i, meta, new ModelResourceLocation(loc, "inventory"));
    }

    private ModelHandler() {}
}
