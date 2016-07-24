/**
 * This class was created by <williewillus>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.core.handler;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockWall;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;
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
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModFluffBlocks;
import vazkii.botania.common.block.decor.slabs.BlockModSlab;
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
import vazkii.botania.common.block.tile.TileAvatar;
import vazkii.botania.common.block.tile.TileBrewery;
import vazkii.botania.common.block.tile.TileGaiaHead;
import vazkii.botania.common.block.tile.TileHourglass;
import vazkii.botania.common.block.tile.TileTeruTeruBozu;
import vazkii.botania.common.block.tile.corporea.TileCorporeaCrystalCube;
import vazkii.botania.common.block.tile.corporea.TileCorporeaIndex;
import vazkii.botania.common.block.tile.mana.TileBellows;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.brew.ItemBrewBase;
import vazkii.botania.common.item.equipment.bauble.ItemFlightTiara;
import vazkii.botania.common.item.relic.ItemInfiniteFruit;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibItemNames;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Locale;
import java.util.function.IntFunction;

import static vazkii.botania.common.item.ModItems.*;

public final class ModelHandler {

    public static void registerModels() {
        ModelLoaderRegistry.registerLoader(SpecialFlowerModel.Loader.INSTANCE);
        OBJLoader.INSTANCE.addDomain(LibMisc.MOD_ID.toLowerCase(Locale.ROOT));

        /** Subtile block models **/
        registerSubtiles();

        /** Custom statemappers **/
        registerStateMappers();

        /** ItemBlocks **/
        registerStandardBlocks();
        registerPavement();
        registerStairs();
        registerSlabs();
        registerWalls();
        registerPanes();
        registerQuartzBlocks();

        /** Normal Items **/
        registerStandardItems();
        registerTESRItems();
        registerManaResources();
        registerRunes();
        registerLens();
        registerBrews();

        /** Special Item Meshers **/
        // Cannot use lambdas directly yet because FG/SS can't reobfuscate them, need a dummy wrapper
        // See https://github.com/MinecraftForge/ForgeGradle/issues/314.

        ModelLoader.registerItemVariants(Item.getItemFromBlock(ModBlocks.pool),
                new ModelResourceLocation("botania:pool", "variant=default"),
                new ModelResourceLocation("botania:pool", "variant=diluted"),
                new ModelResourceLocation("botania:pool", "variant=creative"),
                new ModelResourceLocation("botania:pool", "variant=fabulous"),
                new ModelResourceLocation("botania:pool", "default_full"),
                new ModelResourceLocation("botania:pool", "diluted_full"),
                new ModelResourceLocation("botania:pool", "creative_full"),
                new ModelResourceLocation("botania:pool", "fabulous_full"));
        ModelLoader.setCustomMeshDefinition(Item.getItemFromBlock(ModBlocks.pool), MesherWrapper.of(stack -> {
            int meta = stack.getMetadata();
            if (meta < 0 || meta > 3)
                meta = 0;
            PoolVariant v = PoolVariant.values()[meta];
            boolean renderFull = v == PoolVariant.CREATIVE || (stack.hasTagCompound() && stack.getTagCompound().getBoolean("RenderFull"));
            return new ModelResourceLocation("botania:pool", renderFull ? v.getName() + "_full" : "variant=" + v.getName());
        }));

        ModelLoader.registerItemVariants(elementiumShears,
                new ModelResourceLocation("botania:elementiumShears", "inventory"),
                new ModelResourceLocation("botania:dammitReddit", "inventory"));
        ModelLoader.setCustomMeshDefinition(elementiumShears, MesherWrapper.of(stack ->
                stack.getDisplayName().equalsIgnoreCase("dammit reddit")
                    ? new ModelResourceLocation("botania:dammitReddit", "inventory")
                    : new ModelResourceLocation("botania:elementiumShears", "inventory")));

        ModelLoader.registerItemVariants(grassHorn,
                new ModelResourceLocation("botania:grassHorn0", "inventory"),
                new ModelResourceLocation("botania:grassHorn1", "inventory"),
                new ModelResourceLocation("botania:grassHorn2", "inventory"),
                new ModelResourceLocation("botania:vuvuzela", "inventory"));
        ModelLoader.setCustomMeshDefinition(grassHorn, MesherWrapper.of(stack ->
                stack.getDisplayName().toLowerCase().contains("vuvuzela")
                        ? new ModelResourceLocation("botania:vuvuzela", "inventory")
                        : new ModelResourceLocation("botania:grassHorn" + stack.getMetadata(), "inventory")));

        ModelLoader.registerItemVariants(infiniteFruit,
                new ModelResourceLocation("botania:infiniteFruit", "inventory"),
                new ModelResourceLocation("botania:infiniteFruitBoot", "inventory"));
        ModelLoader.setCustomMeshDefinition(infiniteFruit, MesherWrapper.of(stack ->
                ItemInfiniteFruit.isBoot(stack)
                    ? new ModelResourceLocation("botania:infiniteFruitBoot", "inventory")
                    : new ModelResourceLocation("botania:infiniteFruit", "inventory")));

        ModelLoader.registerItemVariants(manaCookie,
                new ModelResourceLocation("botania:manaCookie", "inventory"),
                new ModelResourceLocation("botania:totalBiscuit", "inventory"));
        ModelLoader.setCustomMeshDefinition(manaCookie, MesherWrapper.of(stack ->
                stack.getDisplayName().toLowerCase().equals("totalbiscuit")
                    ? new ModelResourceLocation("botania:totalBiscuit", "inventory")
                    : new ModelResourceLocation("botania:manaCookie", "inventory")));
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
        registerBlockStandardPath(ModBlocks.alchemyCatalyst);
        registerBlockStandardPath(ModBlocks.alfPortal);
        registerBlockStandardPath(ModBlocks.bifrost);
        registerBlockStandardPath(ModBlocks.bifrostPerm);
        registerBlockStandardPath(ModBlocks.blazeBlock);
        registerBlockStandardPath(ModBlocks.cacophonium);
        registerBlockStandardPath(ModBlocks.cellBlock);
        registerBlockStandardPath(ModBlocks.conjurationCatalyst);
        registerBlockStandardPath(ModBlocks.cocoon);
        registerBlockStandardPath(ModBlocks.corporeaFunnel);
        registerBlockStandardPath(ModBlocks.corporeaInterceptor);
        registerBlockStandardPath(ModBlocks.corporeaRetainer);
        registerBlockVariant(ModBlocks.dirtPath);
        registerBlockCustomPathMetas(ModBlocks.doubleFlower1, 8, i -> "double_flower_" + EnumDyeColor.byMetadata(i).getName());
        Item item = Item.getItemFromBlock(ModBlocks.doubleFlower2); // todo make this look pretty :(
        for (EnumDyeColor color : BotaniaStateProps.DOUBLEFLOWER_VARIANT_2.getAllowedValues()) {
            ModelLoader.setCustomModelResourceLocation(item, color.getMetadata() - 8, new ModelResourceLocation(LibMisc.MOD_ID + ":itemblock/double_flower_" + color.getName(), "inventory"));
        }
        registerBlockStandardPath(ModBlocks.distributor);
        registerBlockStandardPath(ModBlocks.elfGlass);
        registerBlockStandardPath(ModBlocks.enchantedSoil);
        registerBlockStandardPath(ModBlocks.enchanter);
        registerBlockStandardPath(ModBlocks.enderEye);
        registerBlockStandardPath(ModBlocks.felPumpkin);
        registerBlockStandardPath(ModBlocks.floatingSpecialFlower);
        registerBlockCustomPathMetas(ModBlocks.flower, EnumDyeColor.values().length, i -> "flower_" + EnumDyeColor.byMetadata(i).getName());
        registerBlockVariant(ModBlocks.forestEye);
        registerBlockStandardPath(ModBlocks.ghostRail);
        registerBlockStandardPath(ModBlocks.incensePlate);
        registerBlockVariant(ModBlocks.lightLauncher, "powered=false");
        registerBlockStandardPath(ModBlocks.manaBomb);
        registerBlockStandardPath(ModBlocks.manaDetector);
        registerBlockStandardPath(ModBlocks.manaGlass);
        registerBlockStandardPath(ModBlocks.manaVoid);
        registerBlockCustomPathMetas(ModBlocks.mushroom, EnumDyeColor.values().length, i -> "mushroom_" + EnumDyeColor.byMetadata(i).getName());
        registerBlockStandardPath(ModBlocks.prism);
        registerBlockStandardPath(ModBlocks.pistonRelay);
        registerBlockStandardPath(ModBlocks.pump);
        registerBlockStandardPath(ModBlocks.redStringComparator);
        registerBlockStandardPath(ModBlocks.redStringContainer);
        registerBlockStandardPath(ModBlocks.redStringDispenser);
        registerBlockStandardPath(ModBlocks.redStringFertilizer);
        registerBlockStandardPath(ModBlocks.redStringInterceptor);
        registerBlockStandardPath(ModBlocks.redStringRelay);
        registerBlockStandardPath(ModBlocks.rfGenerator);
        registerBlockStandardPath(ModBlocks.root);
        registerBlockVariant(ModBlocks.runeAltar);
        registerBlockStandardPath(ModBlocks.shimmerrock);
        registerBlockStandardPath(ModBlocks.shimmerwoodPlanks);
        registerBlockCustomPathMetas(ModBlocks.shinyFlower, EnumDyeColor.values().length, i -> "glimmering_flower_" + EnumDyeColor.byMetadata(i).getName());
        registerBlockVariant(ModBlocks.sparkChanger, "powered=false");
        registerBlockVariant(ModBlocks.spawnerClaw);
        registerBlockStandardPath(ModBlocks.specialFlower);
        registerBlockVariant(ModBlocks.starfield, "powered=false");
        registerBlockVariant(ModBlocks.terraPlate);
        registerBlockVariant(ModBlocks.tinyPlanet);
        registerBlockVariant(ModBlocks.tinyPotato, "facing=north");
        registerBlockStandardPath(ModBlocks.turntable);

        // Register all metas to variant inventory, so the smartmodel can take over from there. See MiscellaneousIcons
        registerItemModelAllMeta(Item.getItemFromBlock(ModBlocks.floatingFlower), EnumDyeColor.values().length);

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
        registerVariantsDefaulted(ModBlocks.spreader, SpreaderVariant.class, "variant");
        // todo 1.8 this is temporary until animation API is done, currnetly item model textures will not match true spreader texture
        registerVariantsDefaulted(ModBlocks.storage, StorageVariant.class, "variant");
    }

    private static void registerStandardItems() {
        registerItemModel(manasteelSword);
        registerItemModel(spawnerMover);
        registerItemModel(terraPick);
        registerItemModel(magnetRing);
        registerItemModel(magnetRingGreater);
        registerItemModel(manaGun);
        registerItemModel(tornadoRod);

        registerItemModel(twigWand);
        registerItemModel(pestleAndMortar);
        registerItemModel(blackLotus);
        registerItemModel(blackLotus, 1);
        ModelLoader.registerItemVariants(lexicon, new ModelResourceLocation("botania:lexicon_default", "inventory"));
        registerItemModel(lexicon);

        registerItemModel(manasteelHelm);
        registerItemModel(manasteelHelmRevealing);
        registerItemModel(manasteelChest);
        registerItemModel(manasteelLegs);
        registerItemModel(manasteelBoots);

        registerItemModel(manasteelPick);
        registerItemModel(manasteelShovel);
        registerItemModel(manasteelAxe);
        registerItemModel(manasteelShears);

        registerItemModel(manaweaveHelm);
        registerItemModel(manaweaveChest);
        registerItemModel(manaweaveLegs);
        registerItemModel(manaweaveBoots);

        registerItemModel(elementiumHelm);
        registerItemModel(elementiumHelmRevealing);
        registerItemModel(elementiumChest);
        registerItemModel(elementiumLegs);
        registerItemModel(elementiumBoots);

        registerItemModel(elementiumPick);
        registerItemModel(elementiumShovel);
        registerItemModel(elementiumAxe);
        registerItemModel(elementiumSword);

        registerItemModel(terrasteelHelm);
        registerItemModel(terrasteelHelmRevealing);
        registerItemModel(terrasteelChest);
        registerItemModel(terrasteelLegs);
        registerItemModel(terrasteelBoots);
        registerItemModel(terraSword);
        registerItemModel(terraAxe);

        registerItemModel(starSword);
        registerItemModel(thunderSword);
        registerItemModel(crystalBow);
        registerItemModel(livingwoodBow);
        registerItemModel(glassPick);

        registerItemModel(flowerBag);
        registerItemModel(fertilizer);
        registerItemModel(obedienceStick);
        
        registerItemModel(dirtRod);
        registerItemModel(waterRod);
        registerItemModel(cobbleRod);
        registerItemModel(fireRod);
        registerItemModel(rainbowRod);
        registerItemModel(skyDirtRod);
        registerItemModel(terraformRod);
        registerItemModel(diviningRod);
        registerItemModel(gravityRod);
        registerItemModel(missileRod);
        registerItemModel(smeltRod);
        registerItemModel(exchangeRod);

        registerItemModel(openBucket);
        registerItemModel(bloodPendant);
        registerItemModel(manaTablet);
        registerItemModel(enderDagger);
        registerItemModel(slingshot);
        registerItemModel(vineBall);
        registerItemModel(regenIvy);
        registerItemModel(keepIvy);
        registerItemModel(recordGaia1);
        registerItemModel(recordGaia2);
        registerItemModel(overgrowthSeed);
        registerItemModel(worldSeed);
        registerItemModel(incenseStick);
        registerItemModel(enderHand);
        registerItemModel(craftingHalo);
        registerItemModel(spellCloth);
        registerItemModel(autocraftingHalo);
        registerItemModel(sextant);
        registerItemModel(cacophonium);
        registerItemModel(clip);
        registerItemModel(phantomInk);
        registerItemModel(poolMinecart);
        registerItemModel(pinkinator);
        registerItemModel(dice);
        registerItemModel(kingKey);
        registerItemModel(flugelEye);
        registerItemModel(thorRing);
        registerItemModel(lokiRing);
        registerItemModel(odinRing);
        registerItemModel(aesirRing);
        registerItemModel(baubleBox);
        registerItemModel(tinyPlanet);
        registerItemModel(manaRing);
        registerItemModel(manaRingGreater);
        registerItemModel(auraRing);
        registerItemModel(auraRingGreater);
        registerItemModel(spark);

        registerItemModel(waterRing);
        registerItemModel(miningRing);
        registerItemModel(reachRing);
        registerItemModel(swapRing);
        registerItemModel(pixieRing);
        registerItemModel(travelBelt);
        registerItemModel(superTravelBelt);
        registerItemModel(speedUpBelt);
        registerItemModel(knockbackBelt);
        registerItemModel(itemFinder);
        registerItemModel(monocle);
        registerItemModel(icePendant);
        registerItemModel(lavaPendant);
        registerItemModel(superLavaPendant);
        registerItemModel(holyCloak);
        registerItemModel(unholyCloak);
        registerItemModel(goldLaurel);
        registerItemModel(flightTiara);
        registerItemModel(divaCharm);
        registerItemModel(manaMirror);
        registerItemModel(manaInkwell);
        registerItemModel(waterBowl);

        registerItemModelAllMeta(flightTiara, ItemFlightTiara.WING_TYPES);
        registerItemModelAllMeta(laputaShard, 20);
        registerItemModelAllMeta(signalFlare, EnumDyeColor.values().length);
        registerItemModelAllMeta(dye, EnumDyeColor.values().length);
        registerItemModelAllMeta(petal, EnumDyeColor.values().length);

        registerItemModelMetas(sparkUpgrade, LibItemNames.SPARK_UPGRADE, 4);
        registerItemModelMetas(corporeaSpark, LibItemNames.CORPOREA_SPARK, 2);
        registerItemModelMetas(manaBottle, LibItemNames.MANA_BOTTLE, 6);
        registerItemModelMetas(ancientWill, LibItemNames.ANCIENT_WILL, 6);
        registerItemModelMetas(temperanceStone, LibItemNames.TEMPERANCE_STONE, 2);
        registerItemModelMetas(thornChakram, LibItemNames.THORN_CHAKRAM, 2);
        registerItemModelMetas(blackHoleTalisman, LibItemNames.BLACK_HOLE_TALISMAN, 2);
        registerItemModelMetas(slimeBottle, LibItemNames.SLIME_BOTTLE, 2);
        registerItemModelMetas(grassSeeds, LibItemNames.GRASS_SEEDS, 9);
        registerItemModelMetas(quartz, LibItemNames.QUARTZ, 7);
        registerItemModelMetas(cosmetic, LibItemNames.COSMETIC, 32);
        registerItemModelMetas(craftPattern, LibItemNames.CRAFT_PATTERN, 9);
        registerItemModelMetas(virus, LibItemNames.VIRUS, 2);
    }

    // Only for models that absolutely can't be converted to JSON. Use VERY sparingly
    @SuppressWarnings("deprecation")
    private static void registerTESRItems() {
        registerBlockStandardPath(ModBlocks.avatar);
        ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(ModBlocks.avatar), 0, TileAvatar.class);

        registerBlockStandardPath(ModBlocks.bellows);
        ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(ModBlocks.bellows), 0, TileBellows.class);

        registerBlockStandardPath(ModBlocks.brewery);
        ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(ModBlocks.brewery), 0, TileBrewery.class);

        registerBlockStandardPath(ModBlocks.corporeaCrystalCube);
        ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(ModBlocks.corporeaCrystalCube), 0, TileCorporeaCrystalCube.class);

        registerBlockStandardPath(ModBlocks.corporeaIndex);
        ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(ModBlocks.corporeaIndex), 0, TileCorporeaIndex.class);

        registerItemModel(ModItems.gaiaHead);
        ForgeHooksClient.registerTESRItemStack(ModItems.gaiaHead, 0, TileGaiaHead.class);

        registerBlockStandardPath(ModBlocks.hourglass);
        ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(ModBlocks.hourglass), 0, TileHourglass.class);

        registerBlockStandardPath(ModBlocks.teruTeruBozu);
        ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(ModBlocks.teruTeruBozu), 0, TileTeruTeruBozu.class);
    }

    private static void registerManaResources() {
        Item item = manaResource;
        for (int i = 0; i < LibItemNames.MANA_RESOURCE_NAMES.length; i++) {
            String name = "botania:" + LibItemNames.MANA_RESOURCE_NAMES[i];
            ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(name, "inventory"));
        }
    }

    private static void registerRunes() {
        List<String> variantNames = ImmutableList.of("water", "fire", "earth", "air", "spring", "summer", "autumn", "winter", "mana", "lust", "gluttony", "greed", "sloth", "wrath", "envy", "pride");
        for (int i = 0; i < variantNames.size(); i++) {
            ModelLoader.setCustomModelResourceLocation(rune, i, new ModelResourceLocation("botania:rune_" + variantNames.get(i), "inventory"));
        }
    }

    private static void registerLens() {
        int counter = 0;
        for (String s : LibItemNames.LENS_NAMES) {
            ModelLoader.setCustomModelResourceLocation(lens, counter++, new ModelResourceLocation("botania:" + s, "inventory"));
        }
    }

    private static void registerBrews() {
        ModelLoader.setCustomModelResourceLocation(vial, 0, new ModelResourceLocation("botania:vial", "inventory"));
        ModelLoader.setCustomModelResourceLocation(vial, 1, new ModelResourceLocation("botania:flask", "inventory"));

        for (int i = 0; i < 6; i++) {
            ModelLoader.registerItemVariants(brewFlask, new ModelResourceLocation("botania:flask1_" + i, "inventory"));
        }

        ModelLoader.setCustomMeshDefinition(brewFlask, MesherWrapper.of(stack -> {
            int swigsTaken = 6 - ((ItemBrewBase) brewFlask).getSwigsLeft(stack);
            return new ModelResourceLocation("botania:flask1_" + swigsTaken, "inventory");
        }));

        for (int i = 0; i < 4; i++) {
            ModelLoader.registerItemVariants(brewVial, new ModelResourceLocation("botania:vial1_" + i, "inventory"));
        }

        ModelLoader.setCustomMeshDefinition(brewVial, MesherWrapper.of(stack -> {
            int swigsTaken = 4 - ((ItemBrewBase) brewVial).getSwigsLeft(stack);
            return new ModelResourceLocation("botania:vial1_" + swigsTaken, "inventory");
        }));
    }

    private static void registerStateMappers() {
        // Override to let custom model loader work, see SpecialFlowerModel
        ModelLoader.setCustomStateMapper(ModBlocks.specialFlower, new StateMap.Builder().ignore(BotaniaStateProps.COLOR).ignore(((BlockFlower) ModBlocks.specialFlower).getTypeProperty()).build());

        // Override to let smart models work, see MiscellaneousIcons
        ModelLoader.setCustomStateMapper(ModBlocks.floatingSpecialFlower, new StateMap.Builder().ignore(BotaniaStateProps.COLOR).build());
        ModelLoader.setCustomStateMapper(ModBlocks.floatingFlower, new StateMap.Builder().ignore(BotaniaStateProps.COLOR).build());
        ModelLoader.setCustomStateMapper(ModBlocks.platform, new StateMap.Builder().ignore(BotaniaStateProps.PLATFORM_VARIANT).build());

        // Ignore vanilla facing, variant in double flower
        ModelLoader.setCustomStateMapper(ModBlocks.doubleFlower1, (new StateMap.Builder()).ignore(BlockDoublePlant.VARIANT, BlockDoublePlant.FACING).build());
        ModelLoader.setCustomStateMapper(ModBlocks.doubleFlower2, (new StateMap.Builder()).ignore(BlockDoublePlant.VARIANT, BlockDoublePlant.FACING).build());

        // Ignore color in pool, unstable cube, mana beacon, special flower, and petals (handled by Block.colorMultiplier)
        ModelLoader.setCustomStateMapper(ModBlocks.pool, (new StateMap.Builder()).ignore(BotaniaStateProps.COLOR).build());
        ModelLoader.setCustomStateMapper(ModBlocks.unstableBlock, (new StateMap.Builder()).ignore(BotaniaStateProps.COLOR).build());
        ModelLoader.setCustomStateMapper(ModBlocks.manaBeacon, (new StateMap.Builder()).ignore(BotaniaStateProps.COLOR).build());
        ModelLoader.setCustomStateMapper(ModBlocks.petalBlock, (new StateMap.Builder()).ignore(BotaniaStateProps.COLOR).build());
        ModelLoader.setCustomStateMapper(ModBlocks.specialFlower, (new StateMap.Builder()).ignore(BotaniaStateProps.COLOR, ((BlockFlower) ModBlocks.specialFlower).getTypeProperty()).build());

        // Ignore vanilla variant in flowers
        ModelLoader.setCustomStateMapper(ModBlocks.flower, (new StateMap.Builder()).ignore(((BlockFlower) ModBlocks.flower).getTypeProperty()).build());
        ModelLoader.setCustomStateMapper(ModBlocks.shinyFlower, (new StateMap.Builder()).ignore(((BlockFlower) ModBlocks.shinyFlower).getTypeProperty()).build());
        ModelLoader.setCustomStateMapper(ModBlocks.buriedPetals, (new StateMap.Builder()).ignore(((BlockFlower) ModBlocks.buriedPetals).getTypeProperty()).build());

        // Ignore vanilla variant in walls
        ModelLoader.setCustomStateMapper(ModFluffBlocks.biomeStoneWall,
                (new StateMap.Builder()).withName(BotaniaStateProps.BIOMESTONEWALL_VARIANT).ignore(BlockWall.VARIANT).withSuffix("_wall").build());
        ModelLoader.setCustomStateMapper(ModFluffBlocks.dreamwoodWall, (new StateMap.Builder()).ignore(BlockWall.VARIANT).build());
        ModelLoader.setCustomStateMapper(ModFluffBlocks.livingrockWall, (new StateMap.Builder()).ignore(BlockWall.VARIANT).build());
        ModelLoader.setCustomStateMapper(ModFluffBlocks.livingwoodWall, (new StateMap.Builder()).ignore(BlockWall.VARIANT).build());

        // Ignore dummy variant in slabs
        for (Block b : ModFluffBlocks.biomeStoneSlabs) {
            ModelLoader.setCustomStateMapper(b, (new StateMap.Builder()).ignore(BlockModSlab.DUMMY).build());
        }

        for (Block b : ModFluffBlocks.pavementSlabs) {
            ModelLoader.setCustomStateMapper(b, (new StateMap.Builder()).ignore(BlockModSlab.DUMMY).build());
        }

        List<Block> otherSlabs = Lists.newArrayList(ModFluffBlocks.livingwoodSlab, ModFluffBlocks.livingwoodPlankSlab, ModFluffBlocks.livingrockSlab, ModFluffBlocks.dreamwoodSlab, ModFluffBlocks.livingrockBrickSlab,
                ModFluffBlocks.dreamwoodPlankSlab,
                ModFluffBlocks.netherBrickSlab, ModFluffBlocks.soulBrickSlab, ModFluffBlocks.snowBrickSlab,
                ModFluffBlocks.tileSlab, ModFluffBlocks.manaQuartzSlab, ModFluffBlocks.blazeQuartzSlab, ModFluffBlocks.darkQuartzSlab,
                ModFluffBlocks.lavenderQuartzSlab, ModFluffBlocks.redQuartzSlab, ModFluffBlocks.elfQuartzSlab, ModFluffBlocks.sunnyQuartzSlab, ModFluffBlocks.dirtPathSlab,
                ModFluffBlocks.shimmerrockSlab, ModFluffBlocks.shimmerwoodPlankSlab);

        for (Block b : otherSlabs) {
            if (b == null) // Dark quartz disabled
                continue;
            ModelLoader.setCustomStateMapper(b, (new StateMap.Builder()).ignore(BlockModSlab.DUMMY).build());
        }

        // Ignore both dummy variant and half prop in full slabs
        for (Block b : ModFluffBlocks.biomeStoneFullSlabs) {
            ModelLoader.setCustomStateMapper(b, (new StateMap.Builder()).ignore(BlockModSlab.DUMMY, BlockSlab.HALF).build());
        }

        for (Block b : ModFluffBlocks.pavementFullSlabs) {
            ModelLoader.setCustomStateMapper(b, (new StateMap.Builder()).ignore(BlockModSlab.DUMMY, BlockSlab.HALF).build());
        }

        List<Block> otherFullSlabs = Lists.newArrayList(ModFluffBlocks.livingwoodSlabFull, ModFluffBlocks.livingwoodPlankSlabFull, ModFluffBlocks.livingrockSlabFull, ModFluffBlocks.dreamwoodSlabFull, ModFluffBlocks.livingrockBrickSlabFull,
                ModFluffBlocks.dreamwoodPlankSlabFull,
                ModFluffBlocks.netherBrickSlabFull, ModFluffBlocks.soulBrickSlabFull, ModFluffBlocks.snowBrickSlabFull,
                ModFluffBlocks.tileSlabFull, ModFluffBlocks.darkQuartzSlabFull, ModFluffBlocks.manaQuartzSlabFull, ModFluffBlocks.blazeQuartzSlabFull,
                ModFluffBlocks.lavenderQuartzSlabFull, ModFluffBlocks.redQuartzSlabFull, ModFluffBlocks.elfQuartzSlabFull, ModFluffBlocks.sunnyQuartzSlabFull, ModFluffBlocks.dirtPathSlabFull,
                ModFluffBlocks.shimmerrockSlabFull, ModFluffBlocks.shimmerwoodPlankSlabFull);

        for (Block b : otherFullSlabs) {
            if (b == null) // Dark quartz disabled
                continue;
            ModelLoader.setCustomStateMapper(b, (new StateMap.Builder()).ignore(BlockModSlab.DUMMY, BlockSlab.HALF).build());
        }

        // Ignore everything in TESR items to suppress missing model errors
        ModelLoader.setCustomStateMapper(ModBlocks.avatar, new StateMap.Builder().ignore(BotaniaStateProps.CARDINALS).build());
        ModelLoader.setCustomStateMapper(ModBlocks.bellows, new StateMap.Builder().ignore(BotaniaStateProps.CARDINALS).build());
        ModelLoader.setCustomStateMapper(ModBlocks.brewery, new StateMap.Builder().ignore(BotaniaStateProps.POWERED).build());
        ModelLoader.setCustomStateMapper(ModBlocks.gaiaHead, new StateMap.Builder().ignore(BlockSkull.FACING, BlockSkull.NODROP).build());
        ModelLoader.setCustomStateMapper(ModBlocks.hourglass, new StateMap.Builder().ignore(BotaniaStateProps.POWERED).build());
    }

    private static void registerPavement() {
        Item item = Item.getItemFromBlock(ModFluffBlocks.pavement);
        String name = ForgeRegistries.BLOCKS.getKey(ModFluffBlocks.pavement).toString();

        for (EnumDyeColor e : BotaniaStateProps.PAVEMENT_COLOR.getAllowedValues()) {
            String variant = "color=" + e.getName();
            switch (e) { // Explicit switch needed because saved meta does not match EnumDyeColor
                case BLACK: {
                    ModelLoader.setCustomModelResourceLocation(item, 1, new ModelResourceLocation(name, variant));
                    break;
                }
                case BLUE: {
                    ModelLoader.setCustomModelResourceLocation(item, 2, new ModelResourceLocation(name, variant));
                    break;
                }
                case RED: {
                    ModelLoader.setCustomModelResourceLocation(item, 3, new ModelResourceLocation(name, variant));
                    break;
                }
                case YELLOW: {
                    ModelLoader.setCustomModelResourceLocation(item, 4, new ModelResourceLocation(name, variant));
                    break;
                }
                case GREEN: {
                    ModelLoader.setCustomModelResourceLocation(item, 5, new ModelResourceLocation(name, variant));
                    break;
                }
                case WHITE:
                default: {
                    ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(name, variant));
                    break;
                }
            }
        }
    }

    private static void registerStairs() {
        for (Block b : ModFluffBlocks.pavementStairs) {
            registerBlockStandardPath(b);
        }

        for (Block b : ModFluffBlocks.biomeStoneStairs) {
            registerBlockStandardPath(b);
        }

        registerBlockStandardPath(ModFluffBlocks.blazeQuartzStairs);
        if (ConfigHandler.darkQuartzEnabled)
            registerBlockStandardPath(ModFluffBlocks.darkQuartzStairs);
        registerBlockStandardPath(ModFluffBlocks.dreamwoodStairs);
        registerBlockStandardPath(ModFluffBlocks.dreamwoodPlankStairs);
        registerBlockStandardPath(ModFluffBlocks.elfQuartzStairs);
        registerBlockStandardPath(ModFluffBlocks.lavenderQuartzStairs);
        registerBlockStandardPath(ModFluffBlocks.livingrockStairs);
        registerBlockStandardPath(ModFluffBlocks.livingrockBrickStairs);
        registerBlockStandardPath(ModFluffBlocks.livingwoodStairs);
        registerBlockStandardPath(ModFluffBlocks.livingwoodPlankStairs);
        registerBlockStandardPath(ModFluffBlocks.manaQuartzStairs);
        registerBlockStandardPath(ModFluffBlocks.netherBrickStairs);
        registerBlockStandardPath(ModFluffBlocks.redQuartzStairs);
        registerBlockStandardPath(ModFluffBlocks.shimmerrockStairs);
        registerBlockStandardPath(ModFluffBlocks.shimmerwoodPlankStairs);
        registerBlockStandardPath(ModFluffBlocks.snowBrickStairs);
        registerBlockStandardPath(ModFluffBlocks.soulBrickStairs);
        registerBlockStandardPath(ModFluffBlocks.sunnyQuartzStairs);
        registerBlockStandardPath(ModFluffBlocks.tileStairs);
    }

    private static void registerSlabs() {
        for (Block b : ModFluffBlocks.biomeStoneSlabs) {
            registerBlockVariant(b, "half=bottom");
        }

        for (Block b : ModFluffBlocks.pavementSlabs) {
            registerBlockVariant(b, "half=bottom");
        }

        registerBlockVariant(ModFluffBlocks.livingwoodSlab, "half=bottom");
        registerBlockVariant(ModFluffBlocks.livingwoodPlankSlab, "half=bottom");
        registerBlockVariant(ModFluffBlocks.livingrockSlab, "half=bottom");
        registerBlockVariant(ModFluffBlocks.livingrockBrickSlab, "half=bottom");

        registerBlockVariant(ModFluffBlocks.blazeQuartzSlab, "half=bottom");
        if (ConfigHandler.darkQuartzEnabled)
            registerBlockVariant(ModFluffBlocks.darkQuartzSlab, "half=bottom");
        registerBlockVariant(ModFluffBlocks.elfQuartzSlab, "half=bottom");
        registerBlockVariant(ModFluffBlocks.lavenderQuartzSlab, "half=bottom");
        registerBlockVariant(ModFluffBlocks.manaQuartzSlab, "half=bottom");
        registerBlockVariant(ModFluffBlocks.redQuartzSlab, "half=bottom");
        registerBlockVariant(ModFluffBlocks.sunnyQuartzSlab, "half=bottom");

        registerBlockVariant(ModFluffBlocks.dreamwoodSlab, "half=bottom");
        registerBlockVariant(ModFluffBlocks.dreamwoodPlankSlab, "half=bottom");
        registerBlockVariant(ModFluffBlocks.dirtPathSlab, "half=bottom");
        registerBlockVariant(ModFluffBlocks.shimmerrockSlab, "half=bottom");
        registerBlockVariant(ModFluffBlocks.shimmerwoodPlankSlab, "half=bottom");
        registerBlockVariant(ModFluffBlocks.netherBrickSlab, "half=bottom");
        registerBlockVariant(ModFluffBlocks.soulBrickSlab, "half=bottom");
        registerBlockVariant(ModFluffBlocks.snowBrickSlab, "half=bottom");
        registerBlockVariant(ModFluffBlocks.tileSlab, "half=bottom");
    }

    private static void registerWalls() {
        Item item = Item.getItemFromBlock(ModFluffBlocks.biomeStoneWall);
        for (BiomeStoneVariant variant : BotaniaStateProps.BIOMESTONEWALL_VARIANT.getAllowedValues()) {
            ModelLoader.setCustomModelResourceLocation(item, variant.ordinal() - 8, new ModelResourceLocation(LibMisc.MOD_ID + ":itemblock/" + variant.getName() + "_wall", "inventory"));
        }

        registerBlockCustomPath(ModFluffBlocks.livingrockWall, "livingrock_wall");
        registerBlockCustomPath(ModFluffBlocks.livingwoodWall, "livingwood_wall");
        registerBlockCustomPath(ModFluffBlocks.dreamwoodWall, "dreamwood_wall");
    }

    private static void registerPanes() {
        registerBlockStandardPath(ModFluffBlocks.alfglassPane);
        registerBlockStandardPath(ModFluffBlocks.bifrostPane);
        registerBlockStandardPath(ModFluffBlocks.managlassPane);
    }

    private static void registerQuartzBlocks() {
        for (Block b : Lists.newArrayList(ModFluffBlocks.blazeQuartz, ModFluffBlocks.darkQuartz, ModFluffBlocks.elfQuartz, ModFluffBlocks.lavenderQuartz, ModFluffBlocks.manaQuartz, ModFluffBlocks.redQuartz, ModFluffBlocks.sunnyQuartz)) {
            if (b == null) // Dark quartz disabled
                continue;
            Item item = Item.getItemFromBlock(b);
            String name = ForgeRegistries.BLOCKS.getKey(b).toString();
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(name, "variant=normal"));
            ModelLoader.setCustomModelResourceLocation(item, 1, new ModelResourceLocation(name, "variant=chiseled"));
            ModelLoader.setCustomModelResourceLocation(item, 2, new ModelResourceLocation(name, "variant=pillar_y"));
        }
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
    private static void registerItemModelAllMeta(Item item, int range) {
        String loc = ForgeRegistries.ITEMS.getKey(item).toString();
        for(int i = 0; i < range; i++) {
            ModelLoader.registerItemVariants(item, new ModelResourceLocation(loc, "inventory"));
            ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(loc, "inventory"));
        }
    }

    /**
     * Registers each meta of an item to models/item/registryname<meta>.json
     * Range is exclusive upper bound
     */
    private static void registerItemModelMetas(Item item, String loc, int range) {
     	for(int i = 0; i < range; i++) {
     		String name = "botania:" + loc + i;
     		ModelLoader.registerItemVariants(item, new ModelResourceLocation(name, "inventory"));
     		ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(name, "inventory"));
     	}
    }


    // -- Registers the ItemBlock model to be the Block's model, of the specified variant -- //
    private static void registerBlockVariant(Block b) {
        registerBlockVariant(b, "normal");
    }

    private static void registerBlockVariant(Block b, String variant) {
        registerBlockVariantMetas(b, 1, variant);
    }

    private static void registerBlockVariantMetas(Block b, int maxExclusive, String variant) {
        registerBlockVariantMetas(b, maxExclusive, i -> variant);
    }

    private static void registerBlockVariantMetas(Block b, int maxExclusive, IntFunction<String> metaToVariant) {
        Item item = Item.getItemFromBlock(b);
        for (int i = 0; i < maxExclusive; i++) {
            ModelLoader.setCustomModelResourceLocation(
                    item, i,
                    new ModelResourceLocation(item.getRegistryName(), metaToVariant.apply(i))
            );
        }
    }

    // Registers the ItemBlock to models/item/<registryname>#inventory
    private static void registerBlockStandardPath(Block b) {
        registerBlockVariant(b, "inventory");
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

    private static void registerItemModel(Item i,int meta) {
        ResourceLocation loc = i.getRegistryName();
        ModelLoader.setCustomModelResourceLocation(i, meta, new ModelResourceLocation(loc, "inventory"));
    }

    private static void registerItemModel(Item i) {
        registerItemModel(i, 0);
    }

    private ModelHandler() {}

    private interface MesherWrapper extends ItemMeshDefinition {

        static MesherWrapper of(MesherWrapper w) {
            return w;
        }

        ModelResourceLocation getLocation(ItemStack stack);

        @Nonnull
        @Override
        default ModelResourceLocation getModelLocation(@Nonnull ItemStack stack) {
            return getLocation(stack);
        }

    }

}
