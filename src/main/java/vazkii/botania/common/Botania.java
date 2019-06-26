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
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonPartEntity;
import net.minecraft.entity.item.EnderCrystalEntity;
import net.minecraft.entity.item.EnderCrystalEntity;
import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.entity.item.PaintingEntity;
import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.entity.item.PaintingEntity;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.capability.FloatingFlowerImpl;
import vazkii.botania.api.item.IFloatingFlower;
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
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.handler.EquipmentHandler;
import vazkii.botania.common.core.handler.IMCHandler;
import vazkii.botania.common.core.handler.IMCSender;
import vazkii.botania.common.core.handler.InternalMethodHandler;
import vazkii.botania.common.core.handler.ManaNetworkHandler;
import vazkii.botania.common.core.loot.BindUuid;
import vazkii.botania.common.core.loot.EnableRelics;
import vazkii.botania.common.core.loot.LootHandler;
import vazkii.botania.common.core.loot.TrueGuardianKiller;
import vazkii.botania.common.core.proxy.IProxy;
import vazkii.botania.common.core.proxy.ServerProxy;
import vazkii.botania.common.crafting.FluxfieldConditionFactory;
import vazkii.botania.common.crafting.ModBrewRecipes;
import vazkii.botania.common.crafting.ModCraftingRecipes;
import vazkii.botania.common.crafting.ModElvenTradeRecipes;
import vazkii.botania.common.crafting.ModPetalRecipes;
import vazkii.botania.common.crafting.ModPureDaisyRecipes;
import vazkii.botania.common.crafting.ModRuneRecipes;
import vazkii.botania.common.crafting.recipe.AncientWillRecipe;
import vazkii.botania.common.crafting.recipe.ArmorUpgradeRecipe;
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
import vazkii.botania.common.crafting.recipe.ManaUpgradeRecipe;
import vazkii.botania.common.crafting.recipe.PhantomInkRecipe;
import vazkii.botania.common.crafting.recipe.ShapelessManaUpgradeRecipe;
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
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibMisc;
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
	public static boolean curiosLoaded = false;

	public static Botania instance;
	public static IProxy proxy;

	public static final Logger LOGGER = LogManager.getLogger(LibMisc.MOD_ID);

	public Botania() {
		instance = this;
		proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);
		proxy.registerHandlers();
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(IMCSender::enqueue);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(IMCHandler::handle);
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ConfigHandler.CLIENT_SPEC);
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigHandler.COMMON_SPEC);
		MinecraftForge.EVENT_BUS.addListener(this::serverAboutToStart);
		MinecraftForge.EVENT_BUS.addListener(this::serverStarting);
		MinecraftForge.EVENT_BUS.addListener(this::serverStopping);
	}

	private void commonSetup(FMLCommonSetupEvent event) {
		CapabilityManager.INSTANCE.register(IFloatingFlower.class, new IFloatingFlower.Storage(), FloatingFlowerImpl::new);
		gardenOfGlassLoaded = ModList.get().isLoaded("gardenofglass");
		thaumcraftLoaded = ModList.get().isLoaded("thaumcraft");
		bcApiLoaded = ModList.get().isLoaded("buildcraftlib");
		bloodMagicLoaded = ModList.get().isLoaded("bloodmagic"); // Psh, noob
		coloredLightsLoaded = ModList.get().isLoaded("easycoloredlights");
		curiosLoaded = ModList.get().isLoaded("curios");

		BotaniaAPI.internalHandler = new InternalMethodHandler();

		PacketHandler.init();
		ModBrews.init();
		ModMultiblocks.init();
		ModPetalRecipes.init();
		ModPureDaisyRecipes.init();
		ModRuneRecipes.init();
		ModElvenTradeRecipes.init();
		ModBrewRecipes.init();
		ModCraftingRecipes.init();
		// todo 1.13 LexiconData.init();

		if(Botania.thaumcraftLoaded) {
			if(ConfigHandler.COMMON.enableThaumcraftAspects.get()) {
				// todo 1.13 MinecraftForge.EVENT_BUS.register(TCAspects.class);
			}
			ModBrews.initTC();
			ModBrewRecipes.initTC();
		}

		MinecraftForge.EVENT_BUS.register(ManaNetworkHandler.instance);
		MinecraftForge.EVENT_BUS.register(TileCorporeaIndex.getInputHandler());
		MinecraftForge.EVENT_BUS.register(new LootHandler());

		EquipmentHandler.init();

		if(Botania.gardenOfGlassLoaded)
			MinecraftForge.EVENT_BUS.register(SkyblockWorldEvents.class);

		DeferredWorkQueue.runLater(() -> {
			if(Botania.gardenOfGlassLoaded)
				new WorldTypeSkyblock();

			ModBanners.init();

			LootConditionManager.registerCondition(new TrueGuardianKiller.Serializer());
			LootConditionManager.registerCondition(new EnableRelics.Serializer());
			LootFunctionManager.registerFunction(new BindUuid.Serializer());

			CriteriaTriggers.register(AlfPortalTrigger.INSTANCE);
			CriteriaTriggers.register(CorporeaRequestTrigger.INSTANCE);
			CriteriaTriggers.register(DopplegangerNoArmorTrigger.INSTANCE);
			CriteriaTriggers.register(RelicBindTrigger.INSTANCE);
			CriteriaTriggers.register(UseItemSuccessTrigger.INSTANCE);

			CraftingHelper.register(FluxfieldConditionFactory.KEY, new FluxfieldConditionFactory());

			ModBlocks.addDispenserBehaviours();
		});
	}

	/* todo 1.13
	public void init(FMLInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(Botania.instance, new GuiHandler());

		FMLInterModComms.sendMessage("projecte", "interdictionblacklist", EntityManaBurst.class.getCanonicalName());

		for(Block b : new Block[]{ ModBlocks.manaGlass, ModBlocks.elfGlass, ModBlocks.bifrostPerm })
			FMLInterModComms.sendMessage("chiselsandbits", "ignoreblocklogic", b.getRegistryName().toString());



		if(Botania.bcApiLoaded)
			new StatementAPIPlugin();
	}
	*/

	// todo 1.13 move everything here to where it belongs
	private void loadComplete(FMLLoadCompleteEvent event) {
		if(Botania.thaumcraftLoaded) {
			try {
				@SuppressWarnings("unchecked")
				Class<? extends Entity> clazz = (Class<? extends Entity>) Class.forName("thaumcraft.common.entities.EntityFluxRift");
				BotaniaAPI.blacklistEntityFromGravityRod(clazz);
			} catch (ClassNotFoundException ignored) {}
		}

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
	}

	// Overriding the internal method handler will break everything as it changes regularly.
	// So just don't be a moron and don't override it. Thanks.
	private void serverAboutToStart(FMLServerAboutToStartEvent event) {
		String clname = BotaniaAPI.internalHandler.getClass().getName();
		String expect = "vazkii.botania.common.core.handler.InternalMethodHandler";
		if(!clname.equals(expect)) {
			throw new IllegalAccessError("The Botania API internal method handler has been overriden. "
					+ "This will cause crashes and compatibility issues, and that's why it's marked as"
					+ " \"Do not Override\". Whoever had the brilliant idea of overriding it needs to go"
					+ " back to elementary school and learn to read. (Expected classname: " + expect + ", Actual classname: " + clname + ")");
		}
	}

	private void serverStarting(FMLServerStartingEvent event) {
		CommandShare.register(event.getCommandDispatcher());
		CommandOpen.register(event.getCommandDispatcher());
		if(Botania.gardenOfGlassLoaded)
			CommandSkyblockSpread.register(event.getCommandDispatcher());
	}

	private void serverStopping(FMLServerStoppingEvent event) {
		ManaNetworkHandler.instance.clear();
	}

	private int countWords(String s) {
		/* todo 1.13
		String s1 = I18n.translateToLocal(s);
		return s1.split("\\s+").length;
		*/
		return 0;
	}

	private void registerDefaultEntityBlacklist() {
		// Vanilla
		BotaniaAPI.blacklistEntityFromGravityRod(EnderDragonEntity.class);
		BotaniaAPI.blacklistEntityFromGravityRod(EnderDragonPartEntity.class);
		BotaniaAPI.blacklistEntityFromGravityRod(WitherEntity.class);
		BotaniaAPI.blacklistEntityFromGravityRod(ItemFrameEntity.class);
		BotaniaAPI.blacklistEntityFromGravityRod(EnderCrystalEntity.class);
		BotaniaAPI.blacklistEntityFromGravityRod(PaintingEntity.class);

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
