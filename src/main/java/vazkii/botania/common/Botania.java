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
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
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
import vazkii.botania.api.item.IExoflameHeatable;
import vazkii.botania.api.item.IFloatingFlower;
import vazkii.botania.client.core.proxy.ClientProxy;
import vazkii.botania.common.advancements.AlfPortalTrigger;
import vazkii.botania.common.advancements.CorporeaRequestTrigger;
import vazkii.botania.common.advancements.DopplegangerNoArmorTrigger;
import vazkii.botania.common.advancements.RelicBindTrigger;
import vazkii.botania.common.advancements.UseItemSuccessTrigger;
import vazkii.botania.common.block.ModBanners;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileAlfPortal;
import vazkii.botania.common.block.tile.TileEnchanter;
import vazkii.botania.common.block.tile.TileTerraPlate;
import vazkii.botania.common.block.tile.corporea.TileCorporeaIndex;
import vazkii.botania.common.brew.ModBrews;
import vazkii.botania.common.capability.NoopExoflameHeatable;
import vazkii.botania.common.capability.NoopCapStorage;
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
import vazkii.botania.common.crafting.FluxfieldCondition;
import vazkii.botania.common.crafting.SyncHandler;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.common.network.PacketHandler;
import vazkii.botania.common.world.ModFeatures;
import vazkii.botania.common.world.SkyblockWorldEvents;
import vazkii.botania.common.world.WorldTypeSkyblock;
import vazkii.patchouli.api.IMultiblock;
import vazkii.patchouli.api.IStateMatcher;
import vazkii.patchouli.api.PatchouliAPI;
import vazkii.patchouli.common.multiblock.StateMatcher;

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
	public static boolean finishedLoading = false;

	public static final Logger LOGGER = LogManager.getLogger(LibMisc.MOD_ID);

	public Botania() {
		instance = this;
		proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);
		proxy.registerHandlers();
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(IMCSender::enqueue);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(IMCHandler::handle);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::loadComplete);
		ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ConfigHandler.CLIENT_SPEC);
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigHandler.COMMON_SPEC);
		MinecraftForge.EVENT_BUS.addListener(this::serverAboutToStart);
		MinecraftForge.EVENT_BUS.addListener(this::serverStarting);
		MinecraftForge.EVENT_BUS.addListener(this::serverStopping);
	}

	private void commonSetup(FMLCommonSetupEvent event) {
		CapabilityManager.INSTANCE.register(IFloatingFlower.class, new IFloatingFlower.Storage(), FloatingFlowerImpl::new);
		CapabilityManager.INSTANCE.register(IExoflameHeatable.class, new NoopCapStorage<>(), NoopExoflameHeatable::new);

		gardenOfGlassLoaded = ModList.get().isLoaded("gardenofglass");
		thaumcraftLoaded = ModList.get().isLoaded("thaumcraft");
		bcApiLoaded = ModList.get().isLoaded("buildcraftlib");
		bloodMagicLoaded = ModList.get().isLoaded("bloodmagic"); // Psh, noob
		coloredLightsLoaded = ModList.get().isLoaded("easycoloredlights");
		curiosLoaded = ModList.get().isLoaded("curios");

		BotaniaAPI.internalHandler = new InternalMethodHandler();

		PacketHandler.init();
		ModBrews.init();

		if(Botania.thaumcraftLoaded) {
			if(ConfigHandler.COMMON.enableThaumcraftAspects.get()) {
				// todo 1.13 MinecraftForge.EVENT_BUS.register(TCAspects.class);
			}
			ModBrews.initTC();
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

			PatchouliAPI.instance.registerMultiblock(ModBlocks.alfPortal.getRegistryName(), TileAlfPortal.MULTIBLOCK.getValue());
			PatchouliAPI.instance.registerMultiblock(ModBlocks.terraPlate.getRegistryName(), TileTerraPlate.MULTIBLOCK.getValue());
			PatchouliAPI.instance.registerMultiblock(ModBlocks.enchanter.getRegistryName(), TileEnchanter.MULTIBLOCK.getValue());

			String[][] pat = new String[][] {
					{
						"P_______P",
						"_________",
						"_________",
						"_________",
						"_________",
						"_________",
						"_________",
						"_________",
						"P_______P",
					},
					{
						"_________",
						"_________",
						"_________",
						"_________",
						"____B____",
						"_________",
						"_________",
						"_________",
						"_________",
					},
					{
						"_________",
						"_________",
						"_________",
						"___III___",
						"___I0I___",
						"___III___",
						"_________",
						"_________",
						"_________",
					}
			};
			IStateMatcher sm = StateMatcher.fromPredicate(Blocks.IRON_BLOCK, state -> {
				try {
					// No world to pass here, so just fall back to false if it errors
					return state.isBeaconBase(null, null, null);
				} catch (Exception ignored) {
					return false;
				}
			});
			IMultiblock mb = PatchouliAPI.instance.makeMultiblock(
					pat,
					'P', ModBlocks.gaiaPylon,
					'B', Blocks.BEACON,
					'I', sm,
					'0', sm
			);
			PatchouliAPI.instance.registerMultiblock(new ResourceLocation(LibMisc.MOD_ID, "gaia_ritual"), mb);

			LootConditionManager.registerCondition(new TrueGuardianKiller.Serializer());
			LootConditionManager.registerCondition(new EnableRelics.Serializer());
			LootFunctionManager.registerFunction(new BindUuid.Serializer());

			CriteriaTriggers.register(AlfPortalTrigger.INSTANCE);
			CriteriaTriggers.register(CorporeaRequestTrigger.INSTANCE);
			CriteriaTriggers.register(DopplegangerNoArmorTrigger.INSTANCE);
			CriteriaTriggers.register(RelicBindTrigger.INSTANCE);
			CriteriaTriggers.register(UseItemSuccessTrigger.INSTANCE);

			CraftingHelper.register(FluxfieldCondition.SERIALIZER);

			ModBlocks.addDispenserBehaviours();

			ModFeatures.addWorldgen();
		});
	}

	/* todo 1.13
	public void init(FMLInitializationEvent event) {
		FMLInterModComms.sendMessage("projecte", "interdictionblacklist", EntityManaBurst.class.getCanonicalName());

		for(Block b : new Block[]{ ModBlocks.manaGlass, ModBlocks.elfGlass, ModBlocks.bifrostPerm })
			FMLInterModComms.sendMessage("chiselsandbits", "ignoreblocklogic", b.getRegistryName().toString());
	}
	*/

	private void loadComplete(FMLLoadCompleteEvent event) {
		finishedLoading = true;
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
		event.getServer().getResourceManager().addReloadListener(new SyncHandler.ReloadListener());
	}

	private void serverStarting(FMLServerStartingEvent event) {
		if(Botania.gardenOfGlassLoaded)
			CommandSkyblockSpread.register(event.getCommandDispatcher());
	}

	private void serverStopping(FMLServerStoppingEvent event) {
		ManaNetworkHandler.instance.clear();
	}

}
