package vazkii.botania.forge;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.mana.ManaNetworkEvent;
import vazkii.botania.client.fx.ModParticles;
import vazkii.botania.common.ModStats;
import vazkii.botania.common.advancements.ModCriteriaTriggers;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModFluffBlocks;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.block.string.BlockRedStringInterceptor;
import vazkii.botania.common.block.subtile.functional.SubTileTigerseye;
import vazkii.botania.common.block.tile.ModTiles;
import vazkii.botania.common.block.tile.TileAlfPortal;
import vazkii.botania.common.block.tile.TileCraftCrate;
import vazkii.botania.common.block.tile.TileEnchanter;
import vazkii.botania.common.block.tile.TileTerraPlate;
import vazkii.botania.common.block.tile.corporea.TileCorporeaIndex;
import vazkii.botania.common.brew.ModBrews;
import vazkii.botania.common.brew.ModPotions;
import vazkii.botania.common.command.SkyblockCommand;
import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.botania.common.entity.EntityDoppleganger;
import vazkii.botania.common.entity.ModEntities;
import vazkii.botania.common.handler.*;
import vazkii.botania.common.impl.BotaniaAPIImpl;
import vazkii.botania.common.impl.corporea.DefaultCorporeaMatchers;
import vazkii.botania.common.item.ItemGrassSeeds;
import vazkii.botania.common.item.ItemKeepIvy;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.bauble.ItemFlightTiara;
import vazkii.botania.common.item.equipment.tool.terrasteel.ItemTerraAxe;
import vazkii.botania.common.item.equipment.tool.terrasteel.ItemTerraSword;
import vazkii.botania.common.item.material.ItemEnderAir;
import vazkii.botania.common.item.relic.ItemLokiRing;
import vazkii.botania.common.item.rod.*;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.common.loot.LootHandler;
import vazkii.botania.common.loot.ModLootModifiers;
import vazkii.botania.common.world.ModFeatures;
import vazkii.botania.common.world.SkyblockChunkGenerator;
import vazkii.botania.common.world.SkyblockWorldEvents;
import vazkii.botania.forge.network.ForgePacketHandler;
import vazkii.botania.xplat.BotaniaConfig;
import vazkii.botania.xplat.IXplatAbstractions;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

@Mod(LibMisc.MOD_ID)
public class ForgeCommonInitializer {
	public ForgeCommonInitializer() {
		coreInit();
		registryInit();
		registerCapabilities();
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
	}

	public void commonSetup(FMLCommonSetupEvent evt) {
		ForgePacketHandler.init();
		registerEvents();

		PaintableData.init();
		DefaultCorporeaMatchers.init();

		PatchouliAPI.get().registerMultiblock(Registry.BLOCK.getKey(ModBlocks.alfPortal), TileAlfPortal.MULTIBLOCK.get());
		PatchouliAPI.get().registerMultiblock(Registry.BLOCK.getKey(ModBlocks.terraPlate), TileTerraPlate.MULTIBLOCK.get());
		PatchouliAPI.get().registerMultiblock(Registry.BLOCK.getKey(ModBlocks.enchanter), TileEnchanter.MULTIBLOCK.get());
		PatchouliAPI.get().registerMultiblock(prefix("gaia_ritual"), EntityDoppleganger.ARENA_MULTIBLOCK.get());

		OrechidManager.registerListener();
		TileCraftCrate.registerListener();
	}

	private void coreInit() {
		ForgeBotaniaConfig.setup();
		EquipmentHandler.init();
	}

	private void registryInit() {
		// Core item/block/BE
		bind(ForgeRegistries.SOUND_EVENTS, ModSounds::init);
		bind(ForgeRegistries.BLOCKS, ModBlocks::registerBlocks);
		bind(ForgeRegistries.ITEMS, ModBlocks::registerItemBlocks);
		bind(ForgeRegistries.BLOCKS, ModFluffBlocks::registerBlocks);
		bind(ForgeRegistries.ITEMS, ModFluffBlocks::registerItemBlocks);
		bind(ForgeRegistries.BLOCK_ENTITIES, ModTiles::registerTiles);
		bind(ForgeRegistries.ITEMS, ModItems::registerItems);
		bind(ForgeRegistries.BLOCKS, ModSubtiles::registerBlocks);
		bind(ForgeRegistries.ITEMS, ModSubtiles::registerItemBlocks);
		bind(ForgeRegistries.BLOCK_ENTITIES, ModSubtiles::registerTEs);
		ModBlocks.addDispenserBehaviours();

		// GUI and Recipe
		bind(ForgeRegistries.CONTAINERS, ModItems::registerMenuTypes);
		bind(ForgeRegistries.RECIPE_SERIALIZERS, ModItems::registerRecipeSerializers);
		bind(ForgeRegistries.RECIPE_SERIALIZERS, ModRecipeTypes::registerRecipeTypes);

		// Entities
		bind(ForgeRegistries.ENTITIES, ModEntities::registerEntities);
		MinecraftForge.EVENT_BUS.addListener((EntityAttributeCreationEvent e) -> ModEntities.registerAttributes(
				(type, builder) -> e.put(type, builder.build())));
		bind(ForgeRegistries.ATTRIBUTES, PixieHandler::registerAttribute);

		// Potions
		bind(ForgeRegistries.MOB_EFFECTS, ModPotions::registerPotions);
		ModBrews.registerBrews();

		// Worldgen
		bind(ForgeRegistries.FEATURES, ModFeatures::registerFeatures);
		SkyblockChunkGenerator.init();

		// Rest
		ModCriteriaTriggers.init();
		ModLootModifiers.init();
		bind(ForgeRegistries.PARTICLE_TYPES, ModParticles::registerParticles);
		ModStats.init();
	}

	private static <T extends IForgeRegistryEntry<T>> void bind(IForgeRegistry<T> registry, Consumer<BiConsumer<T, ResourceLocation>> source) {
		FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(registry.getRegistrySuperType(),
				(RegistryEvent.Register<T> event) -> {
					IForgeRegistry<T> forgeRegistry = event.getRegistry();
					source.accept((t, rl) -> {
						t.setRegistryName(rl);
						forgeRegistry.register(t);
					});
				});
	}

	private void registerEvents() {
		IEventBus bus = MinecraftForge.EVENT_BUS;

		if (BotaniaConfig.common().worldgenEnabled()) {
			bus.addListener((BiomeLoadingEvent e) -> {
				Biome.BiomeCategory category = e.getCategory();
				if (!ModFeatures.TYPE_BLACKLIST.contains(category)) {
					e.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModFeatures.MYSTICAL_FLOWERS_PLACED);
				}
				if (category != Biome.BiomeCategory.THEEND) {
					e.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModFeatures.MYSTICAL_MUSHROOMS_PLACED);
				}
			});
		}

		int blazeTime = 2400 * (IXplatAbstractions.INSTANCE.gogLoaded() ? 5 : 10);
		bus.addListener((FurnaceFuelBurnTimeEvent e) -> {
			if (e.getItemStack().is(ModBlocks.blazeBlock.asItem())) {
				e.setBurnTime(blazeTime);
			}
		});

		if (IXplatAbstractions.INSTANCE.gogLoaded()) {
			bus.addListener((PlayerInteractEvent.RightClickBlock e) -> SkyblockWorldEvents.onPlayerInteract(
					e.getPlayer(), e.getWorld(), e.getHand(), e.getHitVec()));
		}
		bus.addListener((PlayerInteractEvent.LeftClickBlock e) -> ((ItemExchangeRod) ModItems.exchangeRod).onLeftClick(
				e.getPlayer(), e.getWorld(), e.getHand(), e.getPos(), e.getFace()));
		bus.addListener((PlayerInteractEvent.LeftClickEmpty e) -> ItemTerraSword.leftClick(e.getItemStack()));
		bus.addListener((AttackEntityEvent e) -> ItemTerraSword.attackEntity(
				e.getPlayer(), e.getPlayer().level, InteractionHand.MAIN_HAND, e.getTarget(), null));
		bus.addListener((RegisterCommandsEvent e) -> this.registerCommands(
				e.getDispatcher(), e.getEnvironment() == Commands.CommandSelection.DEDICATED));
		bus.addListener((PlayerSleepInBedEvent e) -> {
			Player.BedSleepingProblem problem = SleepingHandler.trySleep(e.getPlayer(), e.getPos());
			if (problem != null) {
				e.setResult(problem);
			}
		});
		bus.addListener((LootTableLoadEvent e) -> LootHandler.lootLoad(e.getName(), e.getTable()::addPool));
		bus.addListener((ManaNetworkEvent e) -> ManaNetworkHandler.instance.onNetworkEvent(e.getBlockEntity(), e.getType(), e.getAction()));
		bus.addListener((EntityJoinWorldEvent e) -> {
			if (!e.getWorld().isClientSide) {
				SubTileTigerseye.pacifyAfterLoad(e.getEntity(), (ServerLevel) e.getWorld());
			}
		});

		bus.addListener((ServerAboutToStartEvent e) -> this.serverAboutToStart(e.getServer()));
		bus.addListener((ServerStoppingEvent e) -> this.serverStopping(e.getServer()));
		bus.addListener((PlayerEvent.PlayerLoggedOutEvent e) -> ItemFlightTiara.playerLoggedOut((ServerPlayer) e.getPlayer()));
		bus.addListener((PlayerEvent.PlayerRespawnEvent e) -> ItemKeepIvy.onPlayerRespawn(e.getPlayer(), e.getPlayer(), e.isEndConquered())); // TODO: This is probably incorrect
		bus.addListener((TickEvent.WorldTickEvent e) -> {
			if (e.phase == TickEvent.Phase.END && e.world instanceof ServerLevel level) {
				CommonTickHandler.onTick(level);
				ItemGrassSeeds.onTickEnd(level);
				ItemTerraAxe.onTickEnd(level);
			}
		});
		bus.addListener((PlayerInteractEvent.RightClickBlock e) -> {
			BlockRedStringInterceptor.onInteract(e.getPlayer(), e.getWorld(), e.getHand(), e.getHitVec());
			ItemLokiRing.onPlayerInteract(e.getPlayer(), e.getWorld(), e.getHand(), e.getHitVec());
		});
		bus.addListener((PlayerInteractEvent.RightClickItem e) -> {
			InteractionResultHolder<ItemStack> result = ItemEnderAir.onPlayerInteract(e.getPlayer(), e.getWorld(), e.getHand());
			if (result.getResult().consumesAction()) {
				e.setCanceled(true);
				e.setCancellationResult(result.getResult());
			}
		});
	}

	private void registerCapabilities() {
		// TODO Forge caps
	}

	private void serverAboutToStart(MinecraftServer server) {
		if (BotaniaAPI.instance().getClass() != BotaniaAPIImpl.class) {
			String clname = BotaniaAPI.instance().getClass().getName();
			throw new IllegalAccessError("The Botania API has been overriden. "
					+ "This will cause crashes and compatibility issues, and that's why it's marked as"
					+ " \"Do not Override\". Whoever had the brilliant idea of overriding it needs to go"
					+ " back to elementary school and learn to read. (Actual classname: " + clname + ")");
		}

		if (server.isDedicatedServer()) {
			ContributorList.firstStart();
		}
	}

	private void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher, boolean dedicated) {
		if (IXplatAbstractions.INSTANCE.gogLoaded()) {
			SkyblockCommand.register(dispatcher);
		}
//		DataGenerators.registerCommands(dispatcher); TODO Forge datagen
	}

	private void serverStopping(MinecraftServer server) {
		ManaNetworkHandler.instance.clear();
		TileCorporeaIndex.clearIndexCache();
	}

}
