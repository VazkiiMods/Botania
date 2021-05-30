/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 13, 2014, 6:32:39 PM (GMT)]
 */
package vazkii.botania.common;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.ModAPIManager;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.corporea.CorporeaHelper;
import vazkii.botania.api.lexicon.ITwoNamedPage;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.lexicon.LexiconPage;
import vazkii.botania.common.advancements.*;
import vazkii.botania.common.block.ModBanners;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModMultiblocks;
import vazkii.botania.common.block.tile.TileLightRelay;
import vazkii.botania.common.block.tile.corporea.TileCorporeaIndex;
import vazkii.botania.common.brew.ModBrews;
import vazkii.botania.common.core.command.CommandOpen;
import vazkii.botania.common.core.command.CommandShare;
import vazkii.botania.common.core.command.CommandSkyblockSpread;
import vazkii.botania.common.core.handler.BiomeDecorationHandler;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.handler.IMCHandler;
import vazkii.botania.common.core.handler.InternalMethodHandler;
import vazkii.botania.common.core.handler.ManaNetworkHandler;
import vazkii.botania.common.core.loot.LootHandler;
import vazkii.botania.common.core.proxy.IProxy;
import vazkii.botania.common.crafting.ModBrewRecipes;
import vazkii.botania.common.crafting.ModCraftingRecipes;
import vazkii.botania.common.crafting.ModElvenTradeRecipes;
import vazkii.botania.common.crafting.ModManaAlchemyRecipes;
import vazkii.botania.common.crafting.ModManaConjurationRecipes;
import vazkii.botania.common.crafting.ModManaInfusionRecipes;
import vazkii.botania.common.crafting.ModPetalRecipes;
import vazkii.botania.common.crafting.ModPureDaisyRecipes;
import vazkii.botania.common.crafting.ModRuneRecipes;
import vazkii.botania.common.entity.EntityCorporeaSpark;
import vazkii.botania.common.entity.EntityDoppleganger;
import vazkii.botania.common.entity.EntityFlameRing;
import vazkii.botania.common.entity.EntityMagicLandmine;
import vazkii.botania.common.entity.EntityMagicMissile;
import vazkii.botania.common.entity.EntityManaBurst;
import vazkii.botania.common.entity.EntityPinkWither;
import vazkii.botania.common.entity.EntitySignalFlare;
import vazkii.botania.common.entity.EntitySpark;
import vazkii.botania.common.entity.ModEntities;
import vazkii.botania.common.integration.buildcraft.StatementAPIPlugin;
import vazkii.botania.common.integration.thaumcraft.TCAspects;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.common.network.GuiHandler;
import vazkii.botania.common.network.PacketHandler;
import vazkii.botania.common.world.SkyblockWorldEvents;
import vazkii.botania.common.world.WorldTypeSkyblock;

@Mod(modid = LibMisc.MOD_ID, name = LibMisc.MOD_NAME, version = LibMisc.VERSION, dependencies = LibMisc.DEPENDENCIES, guiFactory = LibMisc.GUI_FACTORY)
public class Botania {

	public static boolean gardenOfGlassLoaded = false;

	public static boolean thaumcraftLoaded = false;
	public static boolean bcApiLoaded = false;
	public static boolean bloodMagicLoaded = false;
	public static boolean coloredLightsLoaded = false;
	public static boolean etFuturumLoaded = false;

	@Instance(LibMisc.MOD_ID)
	public static Botania instance;

	@SidedProxy(serverSide = LibMisc.PROXY_SERVER, clientSide = LibMisc.PROXY_CLIENT)
	public static IProxy proxy;

	public static final Logger LOGGER = LogManager.getLogger(LibMisc.MOD_ID);

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		gardenOfGlassLoaded = Loader.isModLoaded("gardenofglass");

		thaumcraftLoaded = Loader.isModLoaded("thaumcraft");
		bcApiLoaded = Loader.isModLoaded("buildcraftlib");
		bloodMagicLoaded = Loader.isModLoaded("bloodmagic"); // Psh, noob
		coloredLightsLoaded = Loader.isModLoaded("easycoloredlights");
		etFuturumLoaded = Loader.isModLoaded("etfuturum");

		BotaniaAPI.internalHandler = new InternalMethodHandler();

		ConfigHandler.loadConfig(event.getSuggestedConfigurationFile());

		PacketHandler.init();
		ModEntities.init();
		ModBrews.init();
		ModMultiblocks.init();

		if(Botania.gardenOfGlassLoaded)
			new WorldTypeSkyblock();

		CriteriaTriggers.register(AlfPortalTrigger.INSTANCE);
		CriteriaTriggers.register(CorporeaRequestTrigger.INSTANCE);
		CriteriaTriggers.register(DopplegangerNoArmorTrigger.INSTANCE);
		CriteriaTriggers.register(RelicBindTrigger.INSTANCE);
		CriteriaTriggers.register(UseItemSuccessTrigger.INSTANCE);

		proxy.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		ModBanners.init();
		ModPetalRecipes.init();
		ModPureDaisyRecipes.init();
		ModRuneRecipes.init();
		ModManaAlchemyRecipes.init();
		ModManaConjurationRecipes.init();
		ModManaInfusionRecipes.init();
		ModElvenTradeRecipes.init();
		ModBrewRecipes.init();
		ModCraftingRecipes.init();
		LexiconData.init();

		NetworkRegistry.INSTANCE.registerGuiHandler(Botania.instance, new GuiHandler());

		MinecraftForge.TERRAIN_GEN_BUS.register(BiomeDecorationHandler.class);
		MinecraftForge.EVENT_BUS.register(ManaNetworkHandler.instance);
		MinecraftForge.EVENT_BUS.register(TileCorporeaIndex.getInputHandler());
		MinecraftForge.EVENT_BUS.register(new LootHandler());

		if(Botania.gardenOfGlassLoaded)
			MinecraftForge.EVENT_BUS.register(SkyblockWorldEvents.class);

		FMLInterModComms.sendMessage("projecte", "interdictionblacklist", EntityManaBurst.class.getCanonicalName());

		for(Block b : new Block[]{ ModBlocks.manaGlass, ModBlocks.elfGlass, ModBlocks.bifrostPerm })
			FMLInterModComms.sendMessage("chiselsandbits", "ignoreblocklogic", b.getRegistryName().toString());
		
		if(Botania.thaumcraftLoaded) {
			if(ConfigHandler.enableThaumcraftAspects) {
				MinecraftForge.EVENT_BUS.register(TCAspects.class);
			}
			ModBrews.initTC();
			ModBrewRecipes.initTC();
		}

		if(Botania.bcApiLoaded)
			new StatementAPIPlugin();
		proxy.init(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		if(Botania.thaumcraftLoaded) {
			try {
				@SuppressWarnings("unchecked")
				Class<? extends Entity> clazz = (Class<? extends Entity>) Class.forName("thaumcraft.common.entities.EntityFluxRift");
				BotaniaAPI.blacklistEntityFromGravityRod(clazz);
			} catch (ClassNotFoundException ignored) {}
		}

		ModBlocks.addDispenserBehaviours();
		ConfigHandler.loadPostInit();
		LexiconData.postInit();

		int words = 0;
		for(LexiconEntry entry : BotaniaAPI.getAllEntries())
			for(LexiconPage page : entry.pages) {
				words += countWords(page.getUnlocalizedName());
				if(page instanceof ITwoNamedPage)
					words += countWords(((ITwoNamedPage) page).getSecondUnlocalizedName());
			}
		Botania.LOGGER.info("The Lexica Botania has {} words.", words);

		registerDefaultEntityBlacklist();
		proxy.postInit(event);
	}

	// Overriding the internal method handler will break everything as it changes regularly.
	// So just don't be a moron and don't override it. Thanks.
	@EventHandler
	public void serverStarting(FMLServerAboutToStartEvent event) {
		String clname = BotaniaAPI.internalHandler.getClass().getName();
		String expect = "vazkii.botania.common.core.handler.InternalMethodHandler";
		if(!clname.equals(expect)) {
			new IllegalAccessError("The Botania API internal method handler has been overriden. "
					+ "This will cause crashes and compatibility issues, and that's why it's marked as"
					+ " \"Do not Override\". Whoever had the brilliant idea of overriding it needs to go"
					+ " back to elementary school and learn to read. (Expected classname: " + expect + ", Actual classname: " + clname + ")").printStackTrace();
			FMLCommonHandler.instance().exitJava(1, true);
		}
	}

	@EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
//		event.registerServerCommand(new CommandDownloadLatest());
		event.registerServerCommand(new CommandShare());
		event.registerServerCommand(new CommandOpen());
		if(Botania.gardenOfGlassLoaded)
			event.registerServerCommand(new CommandSkyblockSpread());
	}

	@EventHandler
	public void serverStopping(FMLServerStoppingEvent event) {
		ManaNetworkHandler.instance.clear();
		CorporeaHelper.clearCache();
	}

	@EventHandler
	public void handleIMC(FMLInterModComms.IMCEvent event) {
		IMCHandler.processMessages(event.getMessages());
	}

	private int countWords(String s) {
		String s1 = I18n.translateToLocal(s);
		return s1.split("\\s+").length;
	}

	private void registerDefaultEntityBlacklist() {
		// Vanilla
		BotaniaAPI.blacklistEntityFromGravityRod(EntityDragon.class);
		BotaniaAPI.blacklistEntityFromGravityRod(MultiPartEntityPart.class);
		BotaniaAPI.blacklistEntityFromGravityRod(EntityWither.class);
		BotaniaAPI.blacklistEntityFromGravityRod(EntityItemFrame.class);
		BotaniaAPI.blacklistEntityFromGravityRod(EntityEnderCrystal.class);
		BotaniaAPI.blacklistEntityFromGravityRod(EntityPainting.class);

		// Botania
		BotaniaAPI.blacklistEntityFromGravityRod(EntityCorporeaSpark.class);
		BotaniaAPI.blacklistEntityFromGravityRod(EntityDoppleganger.class);
		BotaniaAPI.blacklistEntityFromGravityRod(EntityFlameRing.class);
		BotaniaAPI.blacklistEntityFromGravityRod(EntityMagicLandmine.class);
		BotaniaAPI.blacklistEntityFromGravityRod(EntityMagicMissile.class);
		BotaniaAPI.blacklistEntityFromGravityRod(EntityManaBurst.class);
		BotaniaAPI.blacklistEntityFromGravityRod(EntityPinkWither.class);
		BotaniaAPI.blacklistEntityFromGravityRod(EntitySignalFlare.class);
		BotaniaAPI.blacklistEntityFromGravityRod(EntitySpark.class);
		BotaniaAPI.blacklistEntityFromGravityRod(TileLightRelay.EntityPlayerMover.class);
	}

}
