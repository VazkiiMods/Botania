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
        registerMushrooms();
        registerFlowers();
        registerPavement();
        registerStairs();
        registerSlabs();
        registerWalls();
        registerPanes();
        registerAltars();
        registerQuartzBlocks();
        registerLuminizers();

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
        registerItemModel(ModBlocks.alchemyCatalyst);
        registerItemModel(ModBlocks.alfPortal);
        registerItemModel(ModBlocks.bifrost);
        registerItemModel(ModBlocks.bifrostPerm);
        registerItemModel(ModBlocks.blazeBlock);
        registerItemModel(ModBlocks.cacophonium);
        registerItemModel(ModBlocks.cellBlock);
        registerItemModel(ModBlocks.conjurationCatalyst);
        registerItemModel(ModBlocks.cocoon);
        registerItemModel(ModBlocks.corporeaFunnel);
        registerItemModel(ModBlocks.corporeaInterceptor);
        registerItemModel(ModBlocks.corporeaRetainer);
        registerItemModel(ModBlocks.dirtPath);
        registerItemModel(ModBlocks.distributor);
        registerItemModel(ModBlocks.elfGlass);
        registerItemModel(ModBlocks.enchantedSoil);
        registerItemModel(ModBlocks.enchanter);
        registerItemModel(ModBlocks.enderEye);
        registerItemModel(ModBlocks.felPumpkin);
        registerItemModel(ModBlocks.floatingSpecialFlower);
        registerItemModel(ModBlocks.forestEye);
        registerItemModel(ModBlocks.ghostRail);
        registerItemModel(ModBlocks.incensePlate);
        registerItemModel(ModBlocks.lightLauncher);
        registerItemModel(ModBlocks.manaBomb);
        registerItemModel(ModBlocks.manaDetector);
        registerItemModel(ModBlocks.manaGlass);
        registerItemModel(ModBlocks.manaVoid);
        registerItemModel(ModBlocks.prism);
        registerItemModel(ModBlocks.pistonRelay);
        registerItemModel(ModBlocks.pump);
        registerItemModel(ModBlocks.redStringComparator);
        registerItemModel(ModBlocks.redStringContainer);
        registerItemModel(ModBlocks.redStringDispenser);
        registerItemModel(ModBlocks.redStringFertilizer);
        registerItemModel(ModBlocks.redStringInterceptor);
        registerItemModel(ModBlocks.redStringRelay);
        registerItemModel(ModBlocks.rfGenerator);
        registerItemModel(ModBlocks.root);
        registerItemModel(ModBlocks.runeAltar);
        registerItemModel(ModBlocks.shimmerrock);
        registerItemModel(ModBlocks.shimmerwoodPlanks);
        registerItemModel(ModBlocks.sparkChanger);
        registerItemModel(ModBlocks.spawnerClaw);
        registerItemModel(ModBlocks.specialFlower);
        registerItemModel(ModBlocks.starfield);
        registerItemModel(ModBlocks.terraPlate);
        registerItemModel(ModBlocks.tinyPlanet);
        registerItemModel(ModBlocks.tinyPotato);
        registerItemModel(ModBlocks.turntable);

        // Register all metas to variant inventory, so the smartmodel can take over from there. See MiscellaneousIcons
        registerItemModelAllMeta(Item.getItemFromBlock(ModBlocks.floatingFlower), EnumDyeColor.values().length);

        // Item models which all use the same base model and recolored by render layer
        registerItemModelAllMeta(Item.getItemFromBlock(ModBlocks.manaBeacon), EnumDyeColor.values().length);
        registerItemModelAllMeta(Item.getItemFromBlock(ModBlocks.petalBlock), EnumDyeColor.values().length);
        registerItemModelAllMeta(Item.getItemFromBlock(ModBlocks.unstableBlock), EnumDyeColor.values().length);

        // Blocks which share models with their item, and have only one variant to switch over
        registerVariantsDefaulted(ModBlocks.pylon, PylonVariant.class, "variant");
        registerVariantsDefaulted(ModBlocks.altGrass, AltGrassVariant.class, "variant");
        registerVariantsDefaulted(ModFluffBlocks.biomeStoneA, BiomeStoneVariant.class, "variant");
        registerVariantsDefaulted(ModFluffBlocks.biomeStoneB, BiomeBrickVariant.class, "variant");
        registerVariantsDefaulted(ModBlocks.customBrick, CustomBrickVariant.class, "variant");
        registerVariantsDefaulted(ModBlocks.dreamwood, LivingWoodVariant.class, "variant");
        registerVariantsDefaulted(ModBlocks.forestDrum, DrumVariant.class, "variant");
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
        registerItemModel(ModBlocks.avatar);
        ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(ModBlocks.avatar), 0, TileAvatar.class);

        registerItemModel(ModBlocks.bellows);
        ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(ModBlocks.bellows), 0, TileBellows.class);

        registerItemModel(ModBlocks.brewery);
        ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(ModBlocks.brewery), 0, TileBrewery.class);

        registerItemModel(ModBlocks.corporeaCrystalCube);
        ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(ModBlocks.corporeaCrystalCube), 0, TileCorporeaCrystalCube.class);

        registerItemModel(ModBlocks.corporeaIndex);
        ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(ModBlocks.corporeaIndex), 0, TileCorporeaIndex.class);

        registerItemModel(ModItems.gaiaHead);
        ForgeHooksClient.registerTESRItemStack(ModItems.gaiaHead, 0, TileGaiaHead.class);

        registerItemModel(ModBlocks.hourglass);
        ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(ModBlocks.hourglass), 0, TileHourglass.class);

        registerItemModel(ModBlocks.teruTeruBozu);
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
        ModelLoader.setCustomStateMapper(ModFluffBlocks.biomeStoneWall, (new StateMap.Builder()).ignore(BlockWall.VARIANT).build());
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

    private static void registerMushrooms() {
        Item item = Item.getItemFromBlock(ModBlocks.mushroom);
        for (EnumDyeColor color : EnumDyeColor.values()) {
            String name = ForgeRegistries.BLOCKS.getKey(ModBlocks.mushroom).toString();
            ModelLoader.setCustomModelResourceLocation(item, color.getMetadata(), new ModelResourceLocation(name, "inventory_" + color.getName()));
        }
    }

    private static void registerFlowers() {
        Item item = Item.getItemFromBlock(ModBlocks.flower);
        for (EnumDyeColor color : EnumDyeColor.values()) {
            String name = ForgeRegistries.BLOCKS.getKey(ModBlocks.flower).toString();
            ModelLoader.setCustomModelResourceLocation(item, color.getMetadata(), new ModelResourceLocation(name, "inventory_" + color.getName()));
        }

        item = Item.getItemFromBlock(ModBlocks.shinyFlower);
        for (EnumDyeColor color : EnumDyeColor.values()) {
            String name = ForgeRegistries.BLOCKS.getKey(ModBlocks.shinyFlower).toString();
            ModelLoader.setCustomModelResourceLocation(item, color.getMetadata(), new ModelResourceLocation(name, "inventory_" + color.getName()));
        }

        item = Item.getItemFromBlock(ModBlocks.doubleFlower1);
        for (EnumDyeColor color : BotaniaStateProps.DOUBLEFLOWER_VARIANT_1.getAllowedValues()) {
            String name = ForgeRegistries.BLOCKS.getKey(ModBlocks.doubleFlower1).toString();
            String variant = "inventory_" + color.getName();
            ModelLoader.setCustomModelResourceLocation(item, color.getMetadata(), new ModelResourceLocation(name, variant));
        }

        item = Item.getItemFromBlock(ModBlocks.doubleFlower2);
        for (EnumDyeColor color : BotaniaStateProps.DOUBLEFLOWER_VARIANT_2.getAllowedValues()) {
            String name = ForgeRegistries.BLOCKS.getKey(ModBlocks.doubleFlower2).toString();
            String variant = "inventory_" + color.getName();
            ModelLoader.setCustomModelResourceLocation(item, color.getMetadata() - 8, new ModelResourceLocation(name, variant));
        }
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
            registerItemModel(b);
        }

        for (Block b : ModFluffBlocks.biomeStoneStairs) {
            registerItemModel(b);
        }

        registerItemModel(ModFluffBlocks.blazeQuartzStairs);
        if (ConfigHandler.darkQuartzEnabled)
            registerItemModel(ModFluffBlocks.darkQuartzStairs);
        registerItemModel(ModFluffBlocks.dreamwoodStairs);
        registerItemModel(ModFluffBlocks.dreamwoodPlankStairs);
        registerItemModel(ModFluffBlocks.elfQuartzStairs);
        registerItemModel(ModFluffBlocks.lavenderQuartzStairs);
        registerItemModel(ModFluffBlocks.livingrockStairs);
        registerItemModel(ModFluffBlocks.livingrockBrickStairs);
        registerItemModel(ModFluffBlocks.livingwoodStairs);
        registerItemModel(ModFluffBlocks.livingwoodPlankStairs);
        registerItemModel(ModFluffBlocks.manaQuartzStairs);
        registerItemModel(ModFluffBlocks.netherBrickStairs);
        registerItemModel(ModFluffBlocks.redQuartzStairs);
        registerItemModel(ModFluffBlocks.shimmerrockStairs);
        registerItemModel(ModFluffBlocks.shimmerwoodPlankStairs);
        registerItemModel(ModFluffBlocks.snowBrickStairs);
        registerItemModel(ModFluffBlocks.soulBrickStairs);
        registerItemModel(ModFluffBlocks.sunnyQuartzStairs);
        registerItemModel(ModFluffBlocks.tileStairs);
    }

    private static void registerSlabs() {
        for (Block b : ModFluffBlocks.biomeStoneSlabs) {
            registerItemModel(b);
        }

        for (Block b : ModFluffBlocks.pavementSlabs) {
            registerItemModel(b);
        }

        registerItemModel(ModFluffBlocks.livingwoodSlab);
        registerItemModel(ModFluffBlocks.livingwoodPlankSlab);
        registerItemModel(ModFluffBlocks.livingrockSlab);
        registerItemModel(ModFluffBlocks.livingrockBrickSlab);

        registerItemModel(ModFluffBlocks.blazeQuartzSlab);
        if (ConfigHandler.darkQuartzEnabled)
            registerItemModel(ModFluffBlocks.darkQuartzSlab);
        registerItemModel(ModFluffBlocks.elfQuartzSlab);
        registerItemModel(ModFluffBlocks.lavenderQuartzSlab);
        registerItemModel(ModFluffBlocks.manaQuartzSlab);
        registerItemModel(ModFluffBlocks.redQuartzSlab);
        registerItemModel(ModFluffBlocks.sunnyQuartzSlab);

        registerItemModel(ModFluffBlocks.dreamwoodSlab);
        registerItemModel(ModFluffBlocks.dreamwoodPlankSlab);
        registerItemModel(ModFluffBlocks.dirtPathSlab);
        registerItemModel(ModFluffBlocks.shimmerrockSlab);
        registerItemModel(ModFluffBlocks.shimmerwoodPlankSlab);
        registerItemModel(ModFluffBlocks.netherBrickSlab);
        registerItemModel(ModFluffBlocks.soulBrickSlab);
        registerItemModel(ModFluffBlocks.snowBrickSlab);
        registerItemModel(ModFluffBlocks.tileSlab);
    }

    private static void registerWalls() {
        Item item = Item.getItemFromBlock(ModFluffBlocks.biomeStoneWall);
        for (BiomeStoneVariant variant : BotaniaStateProps.BIOMESTONEWALL_VARIANT.getAllowedValues()) {
            String variantName = "inventory_" + variant.getName();
            ModelLoader.setCustomModelResourceLocation(item, variant.ordinal() - 8, new ModelResourceLocation("botania:biomeStoneA0Wall", variantName));
        }

        registerBlock(ModFluffBlocks.livingrockWall, "livingrock_wall");
        registerBlock(ModFluffBlocks.livingwoodWall, "livingwood_wall");
        registerBlock(ModFluffBlocks.dreamwoodWall, "dreamwood_wall");
    }

    private static void registerPanes() {
        registerItemModel(ModFluffBlocks.alfglassPane);
        registerItemModel(ModFluffBlocks.bifrostPane);
        registerItemModel(ModFluffBlocks.managlassPane);
    }

    private static void registerAltars() {
        Item item = Item.getItemFromBlock(ModBlocks.altar);
        String name = ForgeRegistries.BLOCKS.getKey(ModBlocks.altar).toString();
        for (int i = 0; i < AltarVariant.values().length - 1; i++) { // Off by one on purpose to exclude MOSSY
            String variantName = "variant=" + AltarVariant.values()[i].getName();
            ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(name, variantName));
        }
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

    private static void registerLuminizers() {
        Item item = Item.getItemFromBlock(ModBlocks.lightRelay);
        String name = ForgeRegistries.BLOCKS.getKey(ModBlocks.lightRelay).toString();
        for (LuminizerVariant v : LuminizerVariant.values()) {
            ModelLoader.setCustomModelResourceLocation(item, v.ordinal(), new ModelResourceLocation(name, "powered=false,variant=" + v.getName()));
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

    private static void registerBlock(Block b, String name) {
        ModelLoader.setCustomModelResourceLocation(
                Item.getItemFromBlock(b), 0,
                new ModelResourceLocation(LibMisc.MOD_ID + ":" + name, "inventory"));
    }

    private static void registerItemModel(Block b) {
        registerItemModel(Item.getItemFromBlock(b));
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
