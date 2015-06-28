/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 13, 2014, 7:45:37 PM (GMT)]
 */
package vazkii.botania.common.core.proxy;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import org.apache.logging.log4j.Level;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.ITwoNamedPage;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.lexicon.LexiconPage;
import vazkii.botania.common.Botania;
import vazkii.botania.common.achievement.ModAchievements;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModMultiblocks;
import vazkii.botania.common.block.subtile.generating.SubTileNarslimmus;
import vazkii.botania.common.block.tile.corporea.TileCorporeaIndex;
import vazkii.botania.common.brew.ModBrews;
import vazkii.botania.common.brew.ModPotions;
import vazkii.botania.common.core.command.CommandDownloadLatest;
import vazkii.botania.common.core.command.CommandOpen;
import vazkii.botania.common.core.command.CommandShare;
import vazkii.botania.common.core.handler.BiomeDecorationHandler;
import vazkii.botania.common.core.handler.ChestGenHandler;
import vazkii.botania.common.core.handler.CommonTickHandler;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.handler.InternalMethodHandler;
import vazkii.botania.common.core.handler.ManaNetworkHandler;
import vazkii.botania.common.core.handler.PixieHandler;
import vazkii.botania.common.core.handler.SheddingHandler;
import vazkii.botania.common.core.handler.SpawnerChangingHandler;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.crafting.ModBrewRecipes;
import vazkii.botania.common.crafting.ModCraftingRecipes;
import vazkii.botania.common.crafting.ModElvenTradeRecipes;
import vazkii.botania.common.crafting.ModManaAlchemyRecipes;
import vazkii.botania.common.crafting.ModManaConjurationRecipes;
import vazkii.botania.common.crafting.ModManaInfusionRecipes;
import vazkii.botania.common.crafting.ModPetalRecipes;
import vazkii.botania.common.crafting.ModPureDaisyRecipes;
import vazkii.botania.common.crafting.ModRuneRecipes;
import vazkii.botania.common.entity.EntityManaBurst;
import vazkii.botania.common.entity.ModEntities;
import vazkii.botania.common.integration.buildcraft.StatementAPIPlugin;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.network.GuiHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {
		BotaniaAPI.internalHandler = new InternalMethodHandler();

		ConfigHandler.loadConfig(event.getSuggestedConfigurationFile());

		ModBlocks.init();
		ModItems.init();
		ModEntities.init();
		ModPotions.init();
		ModBrews.init();

		ModCraftingRecipes.init();
		ModPetalRecipes.init();
		ModPureDaisyRecipes.init();
		ModRuneRecipes.init();
		ModManaAlchemyRecipes.init();
		ModManaConjurationRecipes.init();
		ModManaInfusionRecipes.init();
		ModElvenTradeRecipes.init();
		ModBrewRecipes.init();
		ModAchievements.init();
		ModMultiblocks.init();

		ChestGenHandler.init();

		LexiconData.init();
	}

	public void init(FMLInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(Botania.instance, new GuiHandler());

		MinecraftForge.TERRAIN_GEN_BUS.register(new BiomeDecorationHandler());
		MinecraftForge.EVENT_BUS.register(ManaNetworkHandler.instance);
		MinecraftForge.EVENT_BUS.register(new PixieHandler());
		MinecraftForge.EVENT_BUS.register(new SheddingHandler());
		MinecraftForge.EVENT_BUS.register(new SpawnerChangingHandler());
		MinecraftForge.EVENT_BUS.register(new SubTileNarslimmus.SpawnIntercepter());
		MinecraftForge.EVENT_BUS.register(TileCorporeaIndex.getInputHandler());

		FMLCommonHandler.instance().bus().register(new CommonTickHandler());

		FMLInterModComms.sendMessage("ProjectE", "interdictionblacklist", EntityManaBurst.class.getCanonicalName());

		if(Botania.bcTriggersLoaded)
			new StatementAPIPlugin();
	}

	public void postInit(FMLPostInitializationEvent event) {
		if(Botania.thaumcraftLoaded) {
			ModBrews.initTC();
			ModBrewRecipes.initTC();
		}

		ModBlocks.addDispenserBehaviours();
		ConfigHandler.loadPostInit();
		LexiconData.postInit();

		registerNEIStuff();

		int words = 0;
		for(LexiconEntry entry : BotaniaAPI.getAllEntries())
			for(LexiconPage page : entry.pages) {
				words += countWords(page.getUnlocalizedName());
				if(page instanceof ITwoNamedPage)
					words += countWords(((ITwoNamedPage) page).getSecondUnlocalizedName());
			}
		FMLLog.log(Level.INFO, "[Botania] The Lexica Botania has %d words.", words);
	}

	private int countWords(String s) {
		String s1 = StatCollector.translateToLocal(s);
		return s1.split(" ").length;
	}

	public void serverStarting(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandDownloadLatest());
		event.registerServerCommand(new CommandShare());
		event.registerServerCommand(new CommandOpen());
	}

	public void registerNEIStuff() {
		// NO-OP
	}

	public void setEntryToOpen(LexiconEntry entry) {
		// NO-OP
	}

	public void setLexiconStack(ItemStack stack) {
		// NO-OP
	}

	public boolean isTheClientPlayer(EntityLivingBase entity) {
		return false;
	}

	public boolean isClientPlayerWearingMonocle() {
		return false;
	}

	public void setExtraReach(EntityLivingBase entity, float reach) {
		if(entity instanceof EntityPlayerMP)
			((EntityPlayerMP) entity).theItemInWorldManager.setBlockReachDistance(Math.max(5, ((EntityPlayerMP) entity).theItemInWorldManager.getBlockReachDistance() + reach));
	}

	public boolean openWikiPage(World world, Block block, MovingObjectPosition pos) {
		return false;
	}

	public void playRecordClientSided(World world, int x, int y, int z, ItemRecord record) {
		// NO-OP
	}

	public long getWorldElapsedTicks() {
		return MinecraftServer.getServer().worldServers[0].getTotalWorldTime();
	}

	public void setSparkleFXNoClip(boolean noclip) {
		// NO-OP
	}

	public void setSparkleFXCorrupt(boolean corrupt) {
		// NO-OP
	}

	public void sparkleFX(World world, double x, double y, double z, float r, float g, float b, float size, int m) {
		sparkleFX(world, x, y, z, r, g, b, size, m, false);
	}

	public void sparkleFX(World world, double x, double y, double z, float r, float g, float b, float size, int m, boolean fake) {
		// NO-OP
	}

	public void setWispFXDistanceLimit(boolean limit) {
		// NO-OP
	}

	public void setWispFXDepthTest(boolean depth) {
		// NO-OP
	}

	public void wispFX(World world, double x, double y, double z, float r, float g, float b, float size) {
		wispFX(world, x, y, z, r, g, b, size, 0F);
	}

	public void wispFX(World world, double x, double y, double z, float r, float g, float b, float size, float gravity) {
		wispFX(world, x, y, z, r, g, b, size, gravity, 1F);
	}

	public void wispFX(World world, double x, double y, double z, float r, float g, float b, float size, float gravity, float maxAgeMul) {
		wispFX(world, x, y, z, r, g, b, size, 0, -gravity, 0, maxAgeMul);
	}

	public void wispFX(World world, double x, double y, double z, float r, float g, float b, float size, float motionx, float motiony, float motionz) {
		wispFX(world, x, y, z, r, g, b, size, motionx, motiony, motionz, 1F);
	}

	public void wispFX(World world, double x, double y, double z, float r, float g, float b, float size, float motionx, float motiony, float motionz, float maxAgeMul) {
		// NO-OP
	}

	public void lightningFX(World world, Vector3 vectorStart, Vector3 vectorEnd, float ticksPerMeter, int colorOuter, int colorInner) {
		lightningFX(world, vectorStart, vectorEnd, ticksPerMeter, System.nanoTime(), colorOuter, colorInner);
	}

	public void lightningFX(World world, Vector3 vectorStart, Vector3 vectorEnd, float ticksPerMeter, long seed, int colorOuter, int colorInner) {
		// NO-OP
	}

}
