/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 *
 * File Created @ [Jan 14, 2014, 5:17:55 PM (GMT)]
 */
package vazkii.botania.common.block;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.block.subtile.SubTilePureDaisy;
import vazkii.botania.common.block.subtile.functional.*;
import vazkii.botania.common.block.subtile.generating.*;
import vazkii.botania.common.block.tile.*;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibOreDict;

public final class ModBlocks {

    public static Block flower;
    public static Block altar;
    public static Block livingrock;
    public static Block livingwood;
    public static Block specialFlower;
    public static Block spreader;
    public static Block pool;
    public static Block runeAltar;
    public static Block unstableBlock;
    public static Block pylon;
    public static Block pistonRelay;
    public static Block distributor;
    public static Block manaBeacon;
    public static Block manaVoid;
    public static Block manaDetector;
    public static Block enchanter;
    public static Block turntable;

    public static void init() {
        flower = new BlockModFlower();
        altar = new BlockAltar();
        livingrock = new BlockLivingrock();
        livingwood = new BlockLivingwood();
        specialFlower = new BlockSpecialFlower();
        spreader = new BlockSpreader();
        pool = new BlockPool();
        runeAltar = new BlockRuneAltar();
        unstableBlock = new BlockUnstable();
        pylon = new BlockPylon();
        pistonRelay = new BlockPistonRelay();
        distributor = new BlockDistributor();
        manaBeacon = new BlockManaBeacon();
        manaVoid = new BlockManaVoid();
        manaDetector = new BlockManaDetector();
        enchanter = new BlockEnchanter();
        turntable = new BlockTurntable();

        for(int i = 0; i < 16; i++)
            OreDictionary.registerOre(LibOreDict.FLOWER[i], new ItemStack(flower, 1, i));

        OreDictionary.registerOre(LibOreDict.LIVING_ROCK, livingrock);
        OreDictionary.registerOre(LibOreDict.LIVING_WOOD, livingwood);

        initTileEntities();
        registerMultiparts();
    }

    private static void initTileEntities() {
        GameRegistry.registerTileEntity(TileAltar.class, LibBlockNames.ALTAR);
        GameRegistry.registerTileEntity(TileSpecialFlower.class, LibBlockNames.SPECIAL_FLOWER);
        GameRegistry.registerTileEntity(TileSpreader.class, LibBlockNames.SPREADER);
        GameRegistry.registerTileEntity(TilePool.class, LibBlockNames.POOL);
        GameRegistry.registerTileEntity(TileRuneAltar.class, LibBlockNames.RUNE_ALTAR);
        GameRegistry.registerTileEntity(TilePylon.class, LibBlockNames.PYLON);
        GameRegistry.registerTileEntity(TileDistributor.class, LibBlockNames.DISTRIBUTOR);
        GameRegistry.registerTileEntity(TileManaBeacon.class, LibBlockNames.MANA_BEACON);
        GameRegistry.registerTileEntity(TileManaVoid.class, LibBlockNames.MANA_VOID);
        GameRegistry.registerTileEntity(TileManaDetector.class, LibBlockNames.MANA_DETECTOR);
        GameRegistry.registerTileEntity(TileEnchanter.class, LibBlockNames.ENCHANTER);
        GameRegistry.registerTileEntity(TileTurntable.class, LibBlockNames.TURNTABLE);

        BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_PUREDAISY, SubTilePureDaisy.class);

        BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_DAYBLOOM, SubTileDaybloom.class);
        BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_ENDOFLAME, SubTileEndoflame.class);
        BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_HYDROANGEAS, SubTileHydroangeas.class);
        BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_THERMALILY, SubTileThermalily.class);
        BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_NIGHTSHADE, SubTileNightshade.class);
        BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_ARCANE_ROSE, SubTileArcaneRose.class);

        BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_BELLETHORN, SubTileBellethorn.class);
        BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_HEISEI_DREAM, SubTileHeiseiDream.class);
        BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_TIGERSEYE, SubTileTigerseye.class);
        BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_JADED_AMARANTHUS, SubTileJadedAmaranthus.class);
        BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_ORECHID, SubtileOrechid.class);
        BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_FALLEN_KANADE, SubTileFallenKanade.class);
        BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_EXOFLAME, SubTileExoflame.class);
        BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_AGRICARNATION, SubTileAgricarnation.class);
        BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_HOPPERHOCK, SubTileHopperhock.class);
        BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_TANGLEBERRIE, SubTileTangleberrie.class);
        BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_JIYUULIA, SubTileJiyuulia.class);
    }

    private static void registerMultiparts() {
        if(Loader.isModLoaded("ForgeMultipart")) {
            try {
                Class clazz = Class.forName("vazkii.botania.common.block.multipart.MultipartHandler");
                clazz.newInstance();
            } catch(Throwable e) {
                e.printStackTrace();
            }
        }
    }

}