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

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.block.subtile.SubTilePureDaisy;
import vazkii.botania.common.block.subtile.functional.SubTileBellethorn;
import vazkii.botania.common.block.subtile.functional.SubTileHeiseiDream;
import vazkii.botania.common.block.subtile.functional.SubTileTigerseye;
import vazkii.botania.common.block.subtile.generating.SubTileDaybloom;
import vazkii.botania.common.block.subtile.generating.SubTileEndoflame;
import vazkii.botania.common.block.tile.TileAltar;
import vazkii.botania.common.block.tile.TilePool;
import vazkii.botania.common.block.tile.TilePylon;
import vazkii.botania.common.block.tile.TileRuneAltar;
import vazkii.botania.common.block.tile.TileSpecialFlower;
import vazkii.botania.common.block.tile.TileSpreader;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibOreDict;
import cpw.mods.fml.common.registry.GameRegistry;

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

		for(int i = 0; i < 16; i++)
			OreDictionary.registerOre(LibOreDict.FLOWER[i], new ItemStack(flower.blockID, 1, i));

		OreDictionary.registerOre(LibOreDict.LIVING_ROCK, livingrock);
		OreDictionary.registerOre(LibOreDict.LIVING_WOOD, livingwood);

		initTileEntities();
	}

	private static void initTileEntities() {
		GameRegistry.registerTileEntity(TileAltar.class, LibBlockNames.ALTAR);
		GameRegistry.registerTileEntity(TileSpecialFlower.class, LibBlockNames.SPECIAL_FLOWER);
		GameRegistry.registerTileEntity(TileSpreader.class, LibBlockNames.SPREADER);
		GameRegistry.registerTileEntity(TilePool.class, LibBlockNames.POOL);
		GameRegistry.registerTileEntity(TileRuneAltar.class, LibBlockNames.RUNE_ALTAR);
		GameRegistry.registerTileEntity(TilePylon.class, LibBlockNames.PYLON);

		BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_PUREDAISY, SubTilePureDaisy.class);

		BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_DAYBLOOM, SubTileDaybloom.class);
		BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_ENDOFLAME, SubTileEndoflame.class);

		BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_BELLETHORN, SubTileBellethorn.class);
		BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_HEISEI_DREAM, SubTileHeiseiDream.class);
		BotaniaAPI.registerSubTile(LibBlockNames.SUBTILE_TIGERSEYE, SubTileTigerseye.class);
	}
}
