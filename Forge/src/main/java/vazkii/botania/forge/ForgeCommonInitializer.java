package vazkii.botania.forge;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableSet;
import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.world.ForgeWorldPreset;
import net.minecraftforge.event.*;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.BotaniaForgeCapabilities;
import vazkii.botania.api.block.IHornHarvestable;
import vazkii.botania.api.block.IWandable;
import vazkii.botania.api.item.IAvatarWieldable;
import vazkii.botania.api.item.IBlockProvider;
import vazkii.botania.api.item.ICoordBoundItem;
import vazkii.botania.api.mana.ManaNetworkEvent;
import vazkii.botania.client.fx.ModParticles;
import vazkii.botania.common.ModStats;
import vazkii.botania.common.PlayerAccess;
import vazkii.botania.common.advancements.ModCriteriaTriggers;
import vazkii.botania.common.block.BlockPistonRelay;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModFluffBlocks;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.block.string.BlockRedStringInterceptor;
import vazkii.botania.common.block.subtile.functional.SubTileDaffomill;
import vazkii.botania.common.block.subtile.functional.SubTileLoonuim;
import vazkii.botania.common.block.subtile.functional.SubTileTigerseye;
import vazkii.botania.common.block.subtile.functional.SubTileVinculotus;
import vazkii.botania.common.block.tile.*;
import vazkii.botania.common.block.tile.corporea.TileCorporeaIndex;
import vazkii.botania.common.brew.ModBrews;
import vazkii.botania.common.brew.ModPotions;
import vazkii.botania.common.command.SkyblockCommand;
import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.botania.common.entity.EntityDoppleganger;
import vazkii.botania.common.entity.ModEntities;
import vazkii.botania.common.handler.*;
import vazkii.botania.common.impl.BotaniaAPIImpl;
import vazkii.botania.common.impl.DefaultHornHarvestable;
import vazkii.botania.common.impl.corporea.DefaultCorporeaMatchers;
import vazkii.botania.common.integration.corporea.CorporeaNodeDetectors;
import vazkii.botania.common.item.*;
import vazkii.botania.common.item.equipment.armor.terrasteel.ItemTerrasteelHelm;
import vazkii.botania.common.item.equipment.bauble.*;
import vazkii.botania.common.item.equipment.tool.elementium.ItemElementiumAxe;
import vazkii.botania.common.item.equipment.tool.terrasteel.ItemTerraAxe;
import vazkii.botania.common.item.equipment.tool.terrasteel.ItemTerraSword;
import vazkii.botania.common.item.material.ItemEnderAir;
import vazkii.botania.common.item.relic.ItemFlugelEye;
import vazkii.botania.common.item.relic.ItemLokiRing;
import vazkii.botania.common.item.relic.ItemOdinRing;
import vazkii.botania.common.item.rod.*;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.common.loot.LootHandler;
import vazkii.botania.common.loot.ModLootModifiers;
import vazkii.botania.common.world.ModFeatures;
import vazkii.botania.common.world.SkyblockChunkGenerator;
import vazkii.botania.common.world.SkyblockWorldEvents;
import vazkii.botania.forge.integration.corporea.ForgeCapCorporeaNodeDetector;
import vazkii.botania.forge.integration.curios.CurioIntegration;
import vazkii.botania.forge.network.ForgePacketHandler;
import vazkii.botania.xplat.BotaniaConfig;
import vazkii.botania.xplat.IXplatAbstractions;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

@Mod(LibMisc.MOD_ID)
public class ForgeCommonInitializer {
	public ForgeCommonInitializer() {
		coreInit();
		registryInit();
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
	}

	public void commonSetup(FMLCommonSetupEvent evt) {
		ForgePacketHandler.init();
		registerEvents();

		evt.enqueueWork(ModBlocks::addDispenserBehaviours);
		PaintableData.init();
		DefaultCorporeaMatchers.init();

		PatchouliAPI.get().registerMultiblock(Registry.BLOCK.getKey(ModBlocks.alfPortal), TileAlfPortal.MULTIBLOCK.get());
		PatchouliAPI.get().registerMultiblock(Registry.BLOCK.getKey(ModBlocks.terraPlate), TileTerraPlate.MULTIBLOCK.get());
		PatchouliAPI.get().registerMultiblock(Registry.BLOCK.getKey(ModBlocks.enchanter), TileEnchanter.MULTIBLOCK.get());
		PatchouliAPI.get().registerMultiblock(prefix("gaia_ritual"), EntityDoppleganger.ARENA_MULTIBLOCK.get());

		OrechidManager.registerListener();
		TileCraftCrate.registerListener();
		CorporeaNodeDetectors.register(new ForgeCapCorporeaNodeDetector());
	}

	private void coreInit() {
		ForgeBotaniaConfig.setup();
		EquipmentHandler.init();
	}

	private void registryInit() {
		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
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

		// GUI and Recipe
		bind(ForgeRegistries.CONTAINERS, ModItems::registerMenuTypes);
		bind(ForgeRegistries.RECIPE_SERIALIZERS, ModItems::registerRecipeSerializers);
		bind(ForgeRegistries.RECIPE_SERIALIZERS, ModRecipeTypes::registerRecipeTypes);

		// Entities
		bind(ForgeRegistries.ENTITIES, ModEntities::registerEntities);
		modBus.addListener((EntityAttributeCreationEvent e) -> ModEntities.registerAttributes((type, builder) -> e.put(type, builder.build())));
		modBus.addListener((EntityAttributeModificationEvent e) -> {
			e.add(EntityType.PLAYER, PixieHandler.PIXIE_SPAWN_CHANCE);
		});
		bind(ForgeRegistries.ATTRIBUTES, PixieHandler::registerAttribute);

		// Potions
		bind(ForgeRegistries.MOB_EFFECTS, ModPotions::registerPotions);
		ModBrews.registerBrews();

		// Worldgen
		bind(ForgeRegistries.FEATURES, ModFeatures::registerFeatures);
		SkyblockChunkGenerator.init();
		modBus.addGenericListener(ForgeWorldPreset.class, (RegistryEvent.Register<ForgeWorldPreset> e) -> {
			ForgeWorldPreset preset = new ForgeWorldPreset(SkyblockChunkGenerator::createForWorldType) {
				@Override
				public String getTranslationKey() {
					return "generator.botania-skyblock";
				}
			};
			preset.setRegistryName(prefix("gardenofglass"));
			e.getRegistry().register(preset);
		});

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
		registerBlockLookasides();
		bus.addGenericListener(ItemStack.class, this::attachItemCaps);
		bus.addGenericListener(BlockEntity.class, this::attachBeCaps);

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
			bus.addListener((PlayerInteractEvent.RightClickBlock e) -> {
				InteractionResult result = SkyblockWorldEvents.onPlayerInteract(e.getPlayer(), e.getWorld(), e.getHand(), e.getHitVec());
				if (result == InteractionResult.SUCCESS) {
					e.setCanceled(true);
					e.setCancellationResult(InteractionResult.SUCCESS);
				}
			});
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
		bus.addListener((PlayerEvent.StartTracking e) -> SubTileDaffomill.onItemTrack(e.getEntity(), (ServerPlayer) e.getPlayer()));
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

		// Below here are events implemented via Mixins on the Fabric side, ordered by Mixin name
		bus.addListener((AnvilUpdateEvent e) -> {
			if (ItemSpellCloth.shouldDenyAnvil(e.getLeft(), e.getRight())) {
				e.setCanceled(true);
			}
		});
		bus.addListener((EntityTeleportEvent.EnderEntity e) -> {
			if (e.getEntityLiving() instanceof EnderMan em) {
				var newPos = SubTileVinculotus.onEndermanTeleport(em, e.getTargetX(), e.getTargetY(), e.getTargetZ());
				if (newPos != null) {
					e.setTargetX(newPos.x());
					e.setTargetY(newPos.y());
					e.setTargetZ(newPos.z());
				}
			}
		});
		bus.addListener((ExplosionEvent e) -> {
			if (ItemGoddessCharm.shouldProtectExplosion(e.getWorld(), e.getExplosion().getPosition())) {
				e.getExplosion().clearToBlow();
			}
		});
		bus.addListener((EntityItemPickupEvent e) -> {
			if (ItemFlowerBag.onPickupItem(e.getItem(), e.getPlayer())) {
				e.setCanceled(true);
			}
		});
		bus.addListener((LivingDropsEvent e) -> {
			var living = e.getEntityLiving();
			ItemElementiumAxe.onEntityDrops(e.isRecentlyHit(), e.getSource(), e.getEntityLiving(), stack -> {
				var ent = new ItemEntity(living.level, living.getX(), living.getY(), living.getZ(), stack);
				ent.setDefaultPickUpDelay();
				e.getDrops().add(ent);
			});
			SubTileLoonuim.dropLooniumItems(living, stack -> {
				e.getDrops().clear();
				var ent = new ItemEntity(living.level, living.getX(), living.getY(), living.getZ(), stack);
				ent.setDefaultPickUpDelay();
				e.getDrops().add(ent);
			});
		});
		bus.addListener((LivingEvent.LivingJumpEvent e) -> ItemTravelBelt.onPlayerJump(e.getEntityLiving()));
		{ // todo missing rest of FabricMixinPlayer. remove braces when done.
			bus.addListener((LivingAttackEvent e) -> {
				if (e.getEntityLiving() instanceof Player player
						&& ItemOdinRing.onPlayerAttacked(player, e.getSource())) {
					e.setCanceled(true);
				}
			});
			bus.addListener((ItemTossEvent e) -> ItemMagnetRing.onTossItem(e.getPlayer()));
			bus.addListener((LivingHurtEvent e) -> {
				if (e.getEntityLiving() instanceof Player player) {
					Container worn = EquipmentHandler.getAllWorn(player);
					for (int i = 0; i < worn.getContainerSize(); i++) {
						ItemStack stack = worn.getItem(i);
						if (stack.getItem() instanceof ItemHolyCloak cloak) {
							e.setAmount(cloak.onPlayerDamage(player, e.getSource(), e.getAmount()));
						}
					}

					PixieHandler.onDamageTaken(player, e.getSource());
				}
				if (e.getSource().getDirectEntity() instanceof Player player) {
					ItemDivaCharm.onEntityDamaged(player, e.getEntity());
				}
			});
			bus.addListener((LivingEvent.LivingUpdateEvent e) -> {
				if (e.getEntityLiving() instanceof Player player) {
					ItemFlightTiara.updatePlayerFlyStatus(player);
					ItemTravelBelt.tickBelt(player);
				}
			});
			bus.addListener((LivingFallEvent e) -> {
				if (e.getEntityLiving() instanceof Player player) {
					e.setDistance(ItemTravelBelt.onPlayerFall(player, e.getDistance()));
				}
			});
			// todo keepivy
			bus.addListener(EventPriority.LOW, (CriticalHitEvent e) -> {
				Event.Result result = e.getResult();
				if (e.getPlayer().level.isClientSide
						|| result == Event.Result.DENY
						|| result == Event.Result.DEFAULT && !e.isVanillaCritical()
						|| !ItemTerrasteelHelm.hasTerraArmorSet(e.getPlayer())
						|| !(e.getTarget() instanceof LivingEntity target)) {
					return;
				}
				e.setDamageModifier(e.getDamageModifier() * ItemTerrasteelHelm.getCritDamageMult(e.getPlayer()));
				((PlayerAccess) e.getPlayer()).botania$setCritTarget(target);
			});

		}
		bus.addListener((PlayerEvent.ItemCraftedEvent e) -> ItemCraftingHalo.onItemCrafted(e.getPlayer(), e.getInventory()));
		bus.addListener(EventPriority.HIGH, (ServerChatEvent e) -> {
			if (TileCorporeaIndex.getInputHandler().onChatMessage(e.getPlayer(), e.getMessage())) {
				e.setCanceled(true);
			}
		});
	}

	// Attaching caps requires dispatching off the item, which is a huge pain because it generates long if-else
	// chains on items, and also doesn't match how Fabric is set up.
	// Instead, let's declare ahead of time what items get which caps, similar to how we do it for Fabric.
	// Needs to be lazy since items aren't initialized yet
	private static final Supplier<Map<Item, Function<ItemStack, IAvatarWieldable>>> AVATAR_WIELDABLES = Suppliers.memoize(() -> Map.of(
			ModItems.dirtRod, s -> new ItemDirtRod.AvatarBehavior(),
			ModItems.diviningRod, s -> new ItemDiviningRod.AvatarBehavior(),
			ModItems.fireRod, s -> new ItemFireRod.AvatarBehavior(),
			ModItems.missileRod, s -> new ItemMissileRod.AvatarBehavior(),
			ModItems.rainbowRod, s -> new ItemRainbowRod.AvatarBehavior(),
			ModItems.tornadoRod, s -> new ItemTornadoRod.AvatarBehavior()
	));

	private static final Supplier<Map<Item, Function<ItemStack, IBlockProvider>>> BLOCK_PROVIDER = Suppliers.memoize(() -> Map.of(
			ModItems.dirtRod, ItemDirtRod.BlockProvider::new,
			ModItems.blackHoleTalisman, ItemBlackHoleTalisman.BlockProvider::new,
			ModItems.cobbleRod, s -> new ItemCobbleRod.BlockProvider(),
			ModItems.enderHand, ItemEnderHand.BlockProvider::new,
			ModItems.terraformRod, s -> new ItemTerraformRod.BlockProvider()
	));

	private static final Supplier<Map<Item, Function<ItemStack, ICoordBoundItem>>> COORD_BOUND_ITEM = Suppliers.memoize(() -> Map.of(
			ModItems.flugelEye, ItemFlugelEye.CoordBoundItem::new,
			ModItems.manaMirror, ItemManaMirror.CoordBoundItem::new,
			ModItems.twigWand, ItemTwigWand.CoordBoundItem::new
	));

	private void attachItemCaps(AttachCapabilitiesEvent<ItemStack> e) {
		var stack = e.getObject();

		if (stack.getItem() instanceof ItemBauble
				&& EquipmentHandler.instance instanceof CurioIntegration ci) {
			e.addCapability(prefix("curio"), ci.initCapability(stack));
		}

		if (stack.is(ModItems.waterBowl)) {
			e.addCapability(prefix("water_bowl"), new CapabilityUtil.WaterBowlFluidHandler(stack));
		}

		var makeAvatarWieldable = AVATAR_WIELDABLES.get().get(stack.getItem());
		if (makeAvatarWieldable != null) {
			e.addCapability(prefix("avatar_wieldable"),
					CapabilityUtil.makeProvider(BotaniaForgeCapabilities.AVATAR_WIELDABLE, makeAvatarWieldable.apply(stack)));
		}

		var makeBlockProvider = BLOCK_PROVIDER.get().get(stack.getItem());
		if (makeBlockProvider != null) {
			e.addCapability(prefix("block_provider"),
					CapabilityUtil.makeProvider(BotaniaForgeCapabilities.BLOCK_PROVIDER, makeBlockProvider.apply(stack)));
		}

		var makeCoordBoundItem = COORD_BOUND_ITEM.get().get(stack.getItem());
		if (makeCoordBoundItem != null) {
			e.addCapability(prefix("coord_bound_item"),
					CapabilityUtil.makeProvider(BotaniaForgeCapabilities.COORD_BOUND_ITEM, makeCoordBoundItem.apply(stack)));
		}
	}

	private void registerBlockLookasides() {
		CapabilityUtil.registerBlockLookaside(BotaniaForgeCapabilities.HORN_HARVEST, (w, p, s) -> (world, pos, stack, hornType) -> hornType == IHornHarvestable.EnumHornType.CANOPY,
				Blocks.VINE, Blocks.CAVE_VINES, Blocks.CAVE_VINES_PLANT, Blocks.TWISTING_VINES,
				Blocks.TWISTING_VINES_PLANT, Blocks.WEEPING_VINES, Blocks.WEEPING_VINES_PLANT);
		CapabilityUtil.registerBlockLookaside(BotaniaForgeCapabilities.HORN_HARVEST, (w, p, s) -> DefaultHornHarvestable.INSTANCE,
				Arrays.stream(DyeColor.values()).map(ModBlocks::getMushroom).toArray(Block[]::new));
		CapabilityUtil.registerBlockLookaside(BotaniaForgeCapabilities.HORN_HARVEST, (w, p, s) -> DefaultHornHarvestable.INSTANCE,
				Arrays.stream(DyeColor.values()).map(ModBlocks::getShinyFlower).toArray(Block[]::new));
		CapabilityUtil.registerBlockLookaside(BotaniaForgeCapabilities.WANDABLE,
				(world, pos, state) -> (player, stack, side) -> ((BlockPistonRelay) state.getBlock()).onUsedByWand(player, stack, world, pos),
				ModBlocks.pistonRelay);
	}

	private static final Supplier<Set<BlockEntityType<?>>> SELF_WANDADBLE_BES = Suppliers.memoize(() -> ImmutableSet.of(ModTiles.ALF_PORTAL, ModTiles.ANIMATED_TORCH, ModTiles.CORPOREA_CRYSTAL_CUBE, ModTiles.CORPOREA_RETAINER,
			ModTiles.CRAFT_CRATE, ModTiles.ENCHANTER, ModTiles.HOURGLASS, ModTiles.PLATFORM, ModTiles.POOL,
			ModTiles.RUNE_ALTAR, ModTiles.SPREADER, ModTiles.TURNTABLE,
			ModSubtiles.DAFFOMILL, ModSubtiles.HOPPERHOCK, ModSubtiles.HOPPERHOCK_CHIBI,
			ModSubtiles.RANNUNCARPUS, ModSubtiles.RANNUNCARPUS_CHIBI)
	);

	private void attachBeCaps(AttachCapabilitiesEvent<BlockEntity> e) {
		var be = e.getObject();
		if (be instanceof AbstractFurnaceBlockEntity furnace) {
			e.addCapability(prefix("exoflame_heatable"),
					CapabilityUtil.makeProvider(BotaniaForgeCapabilities.EXOFLAME_HEATABLE,
							new ExoflameFurnaceHandler.FurnaceExoflameHeatable(furnace)));
		}

		if (be.getType() == ModTiles.ANIMATED_TORCH) {
			e.addCapability(prefix("hourglass_trigger"), CapabilityUtil.makeProvider(BotaniaForgeCapabilities.HOURGLASS_TRIGGER,
					hourglass -> ((TileAnimatedTorch) be).toggle()));
		}

		if (SELF_WANDADBLE_BES.get().contains(be.getType())) {
			e.addCapability(prefix("wandable"), CapabilityUtil.makeProvider(BotaniaForgeCapabilities.WANDABLE,
					(IWandable) be));
		}
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
	}

	private void serverStopping(MinecraftServer server) {
		ManaNetworkHandler.instance.clear();
		TileCorporeaIndex.clearIndexCache();
	}

}
