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
import net.minecraft.item.crafting.RecipeSerializers;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.ITwoNamedPage;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.lexicon.LexiconPage;
import vazkii.botania.client.core.proxy.ClientProxy;
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
import vazkii.botania.common.core.proxy.ServerProxy;
import vazkii.botania.common.crafting.ModBrewRecipes;
import vazkii.botania.common.crafting.ModCraftingRecipes;
import vazkii.botania.common.crafting.ModElvenTradeRecipes;
import vazkii.botania.common.crafting.ModManaAlchemyRecipes;
import vazkii.botania.common.crafting.ModManaConjurationRecipes;
import vazkii.botania.common.crafting.ModManaInfusionRecipes;
import vazkii.botania.common.crafting.ModPetalRecipes;
import vazkii.botania.common.crafting.ModPureDaisyRecipes;
import vazkii.botania.common.crafting.ModRuneRecipes;
import vazkii.botania.common.crafting.recipe.AncientWillRecipe;
import vazkii.botania.common.crafting.recipe.BlackHoleTalismanExtractRecipe;
import vazkii.botania.common.crafting.recipe.CompositeLensRecipe;
import vazkii.botania.common.crafting.recipe.CosmeticAttachRecipe;
import vazkii.botania.common.crafting.recipe.CosmeticRemoveRecipe;
import vazkii.botania.common.crafting.recipe.HelmRevealingRecipe;
import vazkii.botania.common.crafting.recipe.KeepIvyRecipe;
import vazkii.botania.common.crafting.recipe.LensDyeingRecipe;
import vazkii.botania.common.crafting.recipe.ManaGunClipRecipe;
import vazkii.botania.common.crafting.recipe.ManaGunLensRecipe;
import vazkii.botania.common.crafting.recipe.ManaGunRemoveLensRecipe;
import vazkii.botania.common.crafting.recipe.PhantomInkRecipe;
import vazkii.botania.common.crafting.recipe.SpellClothRecipe;
import vazkii.botania.common.crafting.recipe.TerraPickTippingRecipe;
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
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.common.network.GuiHandler;
import vazkii.botania.common.network.PacketHandler;
import vazkii.botania.common.world.SkyblockWorldEvents;
import vazkii.botania.common.world.WorldTypeSkyblock;

@Mod(LibMisc.MOD_ID)
public class Botania {

	public static boolean gardenOfGlassLoaded = false;

	public static boolean thaumcraftLoaded = false;
	public static boolean bcApiLoaded = false;
	public static boolean bloodMagicLoaded = false;
	public static boolean coloredLightsLoaded = false;

	public static Botania instance;
	public static IProxy proxy;

	public static final Logger LOGGER = LogManager.getLogger(LibMisc.MOD_ID);

	public Botania() {
		instance = this;
		proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
	}

	private void commonSetup(FMLCommonSetupEvent event) {
		gardenOfGlassLoaded = ModList.get().isLoaded("gardenofglass");

		thaumcraftLoaded = ModList.get().isLoaded("thaumcraft");
		bcApiLoaded = ModList.get().isLoaded("buildcraftlib");
		bloodMagicLoaded = ModList.get().isLoaded("bloodmagic"); // Psh, noob
		coloredLightsLoaded = ModList.get().isLoaded("easycoloredlights");

		BotaniaAPI.internalHandler = new InternalMethodHandler();

		ConfigHandler.loadConfig(event.getSuggestedConfigurationFile());

		PacketHandler.init();
		ModBrews.init();
		ModMultiblocks.init();
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

		proxy.preInit(event);

		MinecraftForge.EVENT_BUS.register(ManaNetworkHandler.instance);
		MinecraftForge.EVENT_BUS.register(TileCorporeaIndex.getInputHandler());
		MinecraftForge.EVENT_BUS.register(new LootHandler());

		if(Botania.gardenOfGlassLoaded)
			MinecraftForge.EVENT_BUS.register(SkyblockWorldEvents.class);

		DeferredWorkQueue.runLater(() -> {
			if(Botania.gardenOfGlassLoaded)
				new WorldTypeSkyblock();

			CriteriaTriggers.register(AlfPortalTrigger.INSTANCE);
			CriteriaTriggers.register(CorporeaRequestTrigger.INSTANCE);
			CriteriaTriggers.register(DopplegangerNoArmorTrigger.INSTANCE);
			CriteriaTriggers.register(RelicBindTrigger.INSTANCE);
			CriteriaTriggers.register(UseItemSuccessTrigger.INSTANCE);

			RecipeSerializers.register(AncientWillRecipe.SERIALIZER);
			RecipeSerializers.register(BlackHoleTalismanExtractRecipe.SERIALIZER);
			RecipeSerializers.register(CompositeLensRecipe.SERIALIZER);
			RecipeSerializers.register(CosmeticAttachRecipe.SERIALIZER);
			RecipeSerializers.register(CosmeticRemoveRecipe.SERIALIZER);
			RecipeSerializers.register(HelmRevealingRecipe.SERIALIZER);
			RecipeSerializers.register(KeepIvyRecipe.SERIALIZER);
			RecipeSerializers.register(LensDyeingRecipe.SERIALIZER);
			RecipeSerializers.register(ManaGunClipRecipe.SERIALIZER);
			RecipeSerializers.register(ManaGunLensRecipe.SERIALIZER);
			RecipeSerializers.register(ManaGunRemoveLensRecipe.SERIALIZER);
			RecipeSerializers.register(PhantomInkRecipe.SERIALIZER);
			RecipeSerializers.register(SpellClothRecipe.SERIALIZER);
			RecipeSerializers.register(TerraPickTippingRecipe.SERIALIZER);
		});
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {


		NetworkRegistry.INSTANCE.registerGuiHandler(Botania.instance, new GuiHandler());

		MinecraftForge.TERRAIN_GEN_BUS.register(BiomeDecorationHandler.class);

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
