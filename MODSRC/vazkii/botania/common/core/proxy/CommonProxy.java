/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 13, 2014, 7:45:37 PM (GMT)]
 */
package vazkii.botania.common.core.proxy;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
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
import vazkii.botania.common.crafting.ModCrafingRecipes;
import vazkii.botania.common.crafting.ModElvenTradeRecipes;
import vazkii.botania.common.crafting.ModManaAlchemyRecipes;
import vazkii.botania.common.crafting.ModManaConjurationRecipes;
import vazkii.botania.common.crafting.ModManaInfusionRecipes;
import vazkii.botania.common.crafting.ModPetalRecipes;
import vazkii.botania.common.crafting.ModRuneRecipes;
import vazkii.botania.common.entity.EntityManaBurst;
import vazkii.botania.common.entity.ModEntities;
import vazkii.botania.common.integration.buildcraft.StatementAPIPlugin;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.network.GuiHandler;
import cpw.mods.fml.common.FMLCommonHandler;
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

		ModCrafingRecipes.init();
		ModPetalRecipes.init();
		ModRuneRecipes.init();
		ModManaAlchemyRecipes.init();
		ModManaConjurationRecipes.init();
		ModManaInfusionRecipes.init();
		ModElvenTradeRecipes.init();
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

		FMLCommonHandler.instance().bus().register(new CommonTickHandler());

		FMLInterModComms.sendMessage("ProjectE", "interdictionblacklist", EntityManaBurst.class.getCanonicalName());
		
		if(Botania.bcTriggersLoaded) new StatementAPIPlugin();
	}

	public void postInit(FMLPostInitializationEvent event) {
		ModBlocks.addDispenserBehaviours();
		ConfigHandler.loadPostInit();
		LexiconData.postInit();
	}

	public void serverStarting(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandDownloadLatest());
		event.registerServerCommand(new CommandShare());
		event.registerServerCommand(new CommandOpen());
	}

	public void setEntryToOpen(LexiconEntry entry) {
		// NO-OP
	}

	public boolean isTheClientPlayer(EntityLivingBase entity) {
		return false;
	}

	public void setExtraReach(EntityLivingBase entity, float reach) {
		if(entity instanceof EntityPlayerMP)
			((EntityPlayerMP) entity).theItemInWorldManager.setBlockReachDistance(Math.max(5, ((EntityPlayerMP) entity).theItemInWorldManager.getBlockReachDistance() + reach));
	}

	public boolean openWikiPage(World world, Block block, MovingObjectPosition pos) {
		return false;
	}

	public long getWorldElapsedTicks() {
		return MinecraftServer.getServer().worldServers[0].getTotalWorldTime();
	}

	public void setSparkleFXNoClip(boolean noclip) {
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
