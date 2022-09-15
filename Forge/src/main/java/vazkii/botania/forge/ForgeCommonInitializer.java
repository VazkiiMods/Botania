package vazkii.botania.forge;

import com.google.common.base.Suppliers;
import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.event.*;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import net.minecraftforge.registries.RegisterEvent;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.BotaniaForgeCapabilities;
import vazkii.botania.api.block.HornHarvestable;
import vazkii.botania.api.block.Wandable;
import vazkii.botania.api.item.AvatarWieldable;
import vazkii.botania.api.item.BlockProvider;
import vazkii.botania.api.item.CoordBoundItem;
import vazkii.botania.api.item.Relic;
import vazkii.botania.api.mana.*;
import vazkii.botania.api.mana.spark.SparkAttachable;
import vazkii.botania.client.fx.BotaniaParticles;
import vazkii.botania.common.ModStats;
import vazkii.botania.common.PlayerAccess;
import vazkii.botania.common.advancements.BotaniaCriteriaTriggers;
import vazkii.botania.common.block.*;
import vazkii.botania.common.block.flower.functional.DaffomillBlockEntity;
import vazkii.botania.common.block.flower.functional.LooniumBlockEntity;
import vazkii.botania.common.block.flower.functional.TigerseyeBlockEntity;
import vazkii.botania.common.block.flower.functional.VinculotusBlockEntity;
import vazkii.botania.common.block.mana.DrumBlock;
import vazkii.botania.common.block.mana.ManaDetectorBlock;
import vazkii.botania.common.block.mana.ManaVoidBlock;
import vazkii.botania.common.block.red_string.RedStringInterceptorBlock;
import vazkii.botania.common.block.tile.*;
import vazkii.botania.common.block.tile.corporea.TileCorporeaIndex;
import vazkii.botania.common.block.tile.mana.TileRFGenerator;
import vazkii.botania.common.block.tile.string.TileRedStringContainer;
import vazkii.botania.common.brew.ModBrews;
import vazkii.botania.common.brew.ModPotions;
import vazkii.botania.common.brew.potion.PotionSoulCross;
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
import vazkii.botania.common.item.equipment.tool.terrasteel.ItemTerraPick;
import vazkii.botania.common.item.equipment.tool.terrasteel.ItemTerraSword;
import vazkii.botania.common.item.material.ItemEnderAir;
import vazkii.botania.common.item.relic.*;
import vazkii.botania.common.item.rod.*;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.common.loot.LootHandler;
import vazkii.botania.common.loot.ModLootModifiers;
import vazkii.botania.common.world.ModFeatures;
import vazkii.botania.common.world.SkyblockChunkGenerator;
import vazkii.botania.common.world.SkyblockWorldEvents;
import vazkii.botania.forge.integration.InventorySorterIntegration;
import vazkii.botania.forge.integration.corporea.ForgeCapCorporeaNodeDetector;
import vazkii.botania.forge.integration.curios.CurioIntegration;
import vazkii.botania.forge.internal_caps.RedStringContainerCapProvider;
import vazkii.botania.forge.network.ForgePacketHandler;
import vazkii.botania.forge.xplat.ForgeXplatImpl;
import vazkii.botania.xplat.IXplatAbstractions;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.Arrays;
import java.util.Map;
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
		ModBlocks.addAxeStripping();
		PaintableData.init();
		DefaultCorporeaMatchers.init();

		PatchouliAPI.get().registerMultiblock(Registry.BLOCK.getKey(ModBlocks.alfPortal), TileAlfPortal.MULTIBLOCK.get());
		PatchouliAPI.get().registerMultiblock(Registry.BLOCK.getKey(ModBlocks.terraPlate), TileTerraPlate.MULTIBLOCK.get());
		PatchouliAPI.get().registerMultiblock(Registry.BLOCK.getKey(ModBlocks.enchanter), TileEnchanter.MULTIBLOCK.get());
		PatchouliAPI.get().registerMultiblock(prefix("gaia_ritual"), EntityDoppleganger.ARENA_MULTIBLOCK.get());

		OrechidManager.registerListener();
		TileCraftCrate.registerListener();
		CorporeaNodeDetectors.register(new ForgeCapCorporeaNodeDetector());
		if (ModList.get().isLoaded("inventorysorter")) {
			InventorySorterIntegration.init();
		}
	}

	private void coreInit() {
		ForgeBotaniaConfig.setup();
		EquipmentHandler.init();
	}

	private void registryInit() {
		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
		// Core item/block/BE
		bind(Registry.SOUND_EVENT_REGISTRY, ModSounds::init);
		bind(Registry.BLOCK_REGISTRY, ModBlocks::registerBlocks);
		bind(Registry.ITEM_REGISTRY, ModBlocks::registerItemBlocks);
		bind(Registry.BLOCK_REGISTRY, ModFluffBlocks::registerBlocks);
		bind(Registry.ITEM_REGISTRY, ModFluffBlocks::registerItemBlocks);
		bind(Registry.BLOCK_ENTITY_TYPE_REGISTRY, ModTiles::registerTiles);
		bind(Registry.ITEM_REGISTRY, ModItems::registerItems);
		bind(Registry.BLOCK_REGISTRY, ModSubtiles::registerBlocks);
		bind(Registry.ITEM_REGISTRY, ModSubtiles::registerItemBlocks);
		bind(Registry.BLOCK_ENTITY_TYPE_REGISTRY, ModSubtiles::registerTEs);

		// GUI and Recipe
		bind(Registry.MENU_REGISTRY, ModItems::registerMenuTypes);
		bind(Registry.RECIPE_SERIALIZER_REGISTRY, ModItems::registerRecipeSerializers);
		bind(Registry.RECIPE_TYPE_REGISTRY, ModRecipeTypes::submitRecipeTypes);
		bind(Registry.RECIPE_SERIALIZER_REGISTRY, ModRecipeTypes::submitRecipeSerializers);

		// Entities
		bind(Registry.ENTITY_TYPE_REGISTRY, ModEntities::registerEntities);
		modBus.addListener((EntityAttributeCreationEvent e) -> ModEntities.registerAttributes((type, builder) -> e.put(type, builder.build())));
		modBus.addListener((EntityAttributeModificationEvent e) -> {
			e.add(EntityType.PLAYER, PixieHandler.PIXIE_SPAWN_CHANCE);
		});
		bind(Registry.ATTRIBUTE_REGISTRY, PixieHandler::registerAttribute);

		// Potions
		bind(Registry.MOB_EFFECT_REGISTRY, (consumer) -> {
			ModPotions.registerPotions(consumer);
			ModBrews.registerBrews();
		});

		// Worldgen
		bind(Registry.FEATURE_REGISTRY, ModFeatures::registerFeatures);
		bind(Registry.CHUNK_GENERATOR_REGISTRY, SkyblockChunkGenerator::submitRegistration);

		// Rest
		BotaniaCriteriaTriggers.init();
		bind(Registry.PARTICLE_TYPE_REGISTRY, BotaniaParticles::registerParticles);

		bind(Registry.LOOT_ITEM_REGISTRY, ModLootModifiers::submitLootConditions);
		bind(Registry.LOOT_FUNCTION_REGISTRY, ModLootModifiers::submitLootFunctions);
		// Vanilla's stat constructor does the registration too, so we use this
		// event only for timing, not for registering
		modBus.addListener((RegisterEvent evt) -> {
			if (evt.getRegistryKey().equals(Registry.CUSTOM_STAT_REGISTRY)) {
				ModStats.init();
			}
		});
	}

	private static <T> void bind(ResourceKey<Registry<T>> registry, Consumer<BiConsumer<T, ResourceLocation>> source) {
		FMLJavaModLoadingContext.get().getModEventBus().addListener((RegisterEvent event) -> {
			if (registry.equals(event.getRegistryKey())) {
				source.accept((t, rl) -> event.register(registry, rl, () -> t));
			}
		});
	}

	private void registerEvents() {
		IEventBus bus = MinecraftForge.EVENT_BUS;
		registerBlockLookasides();
		bus.addGenericListener(ItemStack.class, this::attachItemCaps);
		bus.addGenericListener(BlockEntity.class, this::attachBeCaps);

		int blazeTime = 2400 * (IXplatAbstractions.INSTANCE.gogLoaded() ? 5 : 10);
		bus.addListener((FurnaceFuelBurnTimeEvent e) -> {
			if (e.getItemStack().is(ModBlocks.blazeBlock.asItem())) {
				e.setBurnTime(blazeTime);
			}
		});

		if (IXplatAbstractions.INSTANCE.gogLoaded()) {
			bus.addListener((PlayerInteractEvent.RightClickBlock e) -> {
				InteractionResult result = SkyblockWorldEvents.onPlayerInteract(e.getEntity(), e.getLevel(), e.getHand(), e.getHitVec());
				if (result == InteractionResult.SUCCESS) {
					e.setCanceled(true);
					e.setCancellationResult(InteractionResult.SUCCESS);
				}
			});
		}
		bus.addListener((PlayerInteractEvent.LeftClickBlock e) -> ((ItemExchangeRod) ModItems.exchangeRod).onLeftClick(
				e.getEntity(), e.getLevel(), e.getHand(), e.getPos(), e.getFace()));
		bus.addListener((PlayerInteractEvent.LeftClickEmpty e) -> ItemTerraSword.leftClick(e.getItemStack()));
		bus.addListener((AttackEntityEvent e) -> ItemTerraSword.attackEntity(
				e.getEntity(), e.getEntity().level, InteractionHand.MAIN_HAND, e.getTarget(), null));
		bus.addListener((RegisterCommandsEvent e) -> this.registerCommands(
				e.getDispatcher(), e.getCommandSelection() == Commands.CommandSelection.DEDICATED));
		bus.addListener((PlayerSleepInBedEvent e) -> {
			Player.BedSleepingProblem problem = SleepingHandler.trySleep(e.getEntity(), e.getPos());
			if (problem != null) {
				e.setResult(problem);
			}
		});
		bus.addListener((PlayerEvent.StartTracking e) -> DaffomillBlockEntity.onItemTrack(e.getEntity(), (ServerPlayer) e.getEntity()));
		bus.addListener((LootTableLoadEvent e) -> LootHandler.lootLoad(e.getName(), b -> e.getTable().addPool(b.build())));
		bus.addListener((ManaNetworkEvent e) -> ManaNetworkHandler.instance.onNetworkEvent(e.getReceiver(), e.getType(), e.getAction()));
		bus.addListener((EntityJoinLevelEvent e) -> {
			if (!e.getLevel().isClientSide) {
				TigerseyeBlockEntity.pacifyAfterLoad(e.getEntity(), (ServerLevel) e.getLevel());
			}
		});

		bus.addListener((ServerAboutToStartEvent e) -> this.serverAboutToStart(e.getServer()));
		bus.addListener((ServerStoppingEvent e) -> this.serverStopping(e.getServer()));
		bus.addListener((PlayerEvent.PlayerLoggedOutEvent e) -> ItemFlightTiara.playerLoggedOut((ServerPlayer) e.getEntity()));
		bus.addListener((PlayerEvent.Clone e) -> ItemKeepIvy.onPlayerRespawn(e.getOriginal(), e.getEntity(), !e.isWasDeath()));
		bus.addListener((TickEvent.LevelTickEvent e) -> {
			if (e.phase == TickEvent.Phase.END && e.level instanceof ServerLevel level) {
				CommonTickHandler.onTick(level);
				ItemGrassSeeds.onTickEnd(level);
				ItemTerraAxe.onTickEnd(level);
			}
		});
		bus.addListener((PlayerInteractEvent.RightClickBlock e) -> {
			RedStringInterceptorBlock.onInteract(e.getEntity(), e.getLevel(), e.getHand(), e.getHitVec());
			ItemLokiRing.onPlayerInteract(e.getEntity(), e.getLevel(), e.getHand(), e.getHitVec());
		});
		bus.addListener((PlayerInteractEvent.RightClickItem e) -> {
			InteractionResultHolder<ItemStack> result = ItemEnderAir.onPlayerInteract(e.getEntity(), e.getLevel(), e.getHand());
			if (result.getResult().consumesAction()) {
				e.setCanceled(true);
				e.setCancellationResult(result.getResult());
			}
		});

		// Below here are events implemented via Mixins on the Fabric side, ordered by Mixin name
		// FabricMixinAnvilMenu
		bus.addListener((AnvilUpdateEvent e) -> {
			if (ItemSpellCloth.shouldDenyAnvil(e.getLeft(), e.getRight())) {
				e.setCanceled(true);
			}
		});
		// FabricMixinAxeItem
		bus.addListener((BlockEvent.BlockToolModificationEvent e) -> {
			if (e.getToolAction() == ToolActions.AXE_STRIP) {
				BlockState input = e.getState();
				Block output = ForgeXplatImpl.CUSTOM_STRIPPABLES.get(input.getBlock());
				if (output != null) {
					e.setFinalState(output.withPropertiesOf(input));
				}
			}
		});
		// FabricMixinEnderMan
		bus.addListener((EntityTeleportEvent.EnderEntity e) -> {
			if (e.getEntityLiving() instanceof EnderMan em) {
				var newPos = VinculotusBlockEntity.onEndermanTeleport(em, e.getTargetX(), e.getTargetY(), e.getTargetZ());
				if (newPos != null) {
					e.setTargetX(newPos.x());
					e.setTargetY(newPos.y());
					e.setTargetZ(newPos.z());
				}
			}
		});
		// FabricMixinExplosion
		bus.addListener((ExplosionEvent e) -> {
			if (ItemGoddessCharm.shouldProtectExplosion(e.getLevel(), e.getExplosion().getPosition())) {
				e.getExplosion().clearToBlow();
			}
		});
		// FabricMixinItemEntity
		bus.addListener((EntityItemPickupEvent e) -> {
			if (ItemFlowerBag.onPickupItem(e.getItem(), e.getEntity())) {
				e.setCanceled(true);
			}
		});
		// FabricMixinLivingEntity
		{
			bus.addListener((LivingDropsEvent e) -> {
				var living = e.getEntity();
				ItemElementiumAxe.onEntityDrops(e.isRecentlyHit(), e.getSource(), living, stack -> {
					var ent = new ItemEntity(living.level, living.getX(), living.getY(), living.getZ(), stack);
					ent.setDefaultPickUpDelay();
					e.getDrops().add(ent);
				});
				LooniumBlockEntity.dropLooniumItems(living, stack -> {
					e.getDrops().clear();
					var ent = new ItemEntity(living.level, living.getX(), living.getY(), living.getZ(), stack);
					ent.setDefaultPickUpDelay();
					e.getDrops().add(ent);
				});
			});
			bus.addListener((LivingDeathEvent e) -> {
				if (e.getSource().getEntity() instanceof LivingEntity killer) {
					PotionSoulCross.onEntityKill(e.getEntity(), killer);
				}
			});
			bus.addListener((LivingEvent.LivingJumpEvent e) -> ItemTravelBelt.onPlayerJump(e.getEntity()));
		}
		// FabricMixinPlayer
		{
			bus.addListener((LivingAttackEvent e) -> {
				if (e.getEntity() instanceof Player player
						&& ItemOdinRing.onPlayerAttacked(player, e.getSource())) {
					e.setCanceled(true);
				}
			});
			bus.addListener((ItemTossEvent e) -> ItemMagnetRing.onTossItem(e.getPlayer()));
			bus.addListener((LivingHurtEvent e) -> {
				if (e.getEntity() instanceof Player player) {
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
			bus.addListener((LivingEvent.LivingTickEvent e) -> {
				if (e.getEntity() instanceof Player player) {
					ItemFlightTiara.updatePlayerFlyStatus(player);
					ItemTravelBelt.tickBelt(player);
				}
			});
			bus.addListener((LivingFallEvent e) -> {
				if (e.getEntity() instanceof Player player) {
					e.setDistance(ItemTravelBelt.onPlayerFall(player, e.getDistance()));
				}
			});
			bus.addListener(EventPriority.LOW, (CriticalHitEvent e) -> {
				Event.Result result = e.getResult();
				if (e.getEntity().level.isClientSide
						|| result == Event.Result.DENY
						|| result == Event.Result.DEFAULT && !e.isVanillaCritical()
						|| !ItemTerrasteelHelm.hasTerraArmorSet(e.getEntity())
						|| !(e.getTarget() instanceof LivingEntity target)) {
					return;
				}
				e.setDamageModifier(e.getDamageModifier() * ItemTerrasteelHelm.getCritDamageMult(e.getEntity()));
				((PlayerAccess) e.getEntity()).botania$setCritTarget(target);
			});

		}
		// FabricMixinResultSlot
		bus.addListener((PlayerEvent.ItemCraftedEvent e) -> ItemCraftingHalo.onItemCrafted(e.getEntity(), e.getInventory()));
	}

	// Attaching caps requires dispatching off the item, which is a huge pain because it generates long if-else
	// chains on items, and also doesn't match how Fabric is set up.
	// Instead, let's declare ahead of time what items get which caps, similar to how we do it for Fabric.
	// Needs to be lazy since items aren't initialized yet
	private static final Supplier<Map<Item, Function<ItemStack, AvatarWieldable>>> AVATAR_WIELDABLES = Suppliers.memoize(() -> Map.of(
			ModItems.dirtRod, s -> new ItemDirtRod.AvatarBehavior(),
			ModItems.diviningRod, s -> new ItemDiviningRod.AvatarBehavior(),
			ModItems.fireRod, s -> new ItemFireRod.AvatarBehavior(),
			ModItems.missileRod, s -> new ItemMissileRod.AvatarBehavior(),
			ModItems.rainbowRod, s -> new ItemRainbowRod.AvatarBehavior(),
			ModItems.tornadoRod, s -> new ItemTornadoRod.AvatarBehavior()
	));

	private static final Supplier<Map<Item, Function<ItemStack, BlockProvider>>> BLOCK_PROVIDER = Suppliers.memoize(() -> Map.of(
			ModItems.dirtRod, ItemDirtRod.BlockProviderImpl::new,
			ModItems.skyDirtRod, ItemDirtRod.BlockProviderImpl::new,
			ModItems.blackHoleTalisman, ItemBlackHoleTalisman.BlockProviderImpl::new,
			ModItems.cobbleRod, s -> new ItemCobbleRod.BlockProviderImpl(),
			ModItems.enderHand, ItemEnderHand.BlockProviderImpl::new,
			ModItems.terraformRod, s -> new ItemTerraformRod.BlockProviderImpl()
	));

	private static final Supplier<Map<Item, Function<ItemStack, CoordBoundItem>>> COORD_BOUND_ITEM = Suppliers.memoize(() -> Map.of(
			ModItems.flugelEye, ItemFlugelEye.CoordBoundItemImpl::new,
			ModItems.manaMirror, ItemManaMirror.CoordBoundItemImpl::new,
			ModItems.twigWand, ItemTwigWand.CoordBoundItemImpl::new,
			ModItems.dreamwoodWand, ItemTwigWand.CoordBoundItemImpl::new
	));

	private static final Supplier<Map<Item, Function<ItemStack, ManaItem>>> MANA_ITEM = Suppliers.memoize(() -> Map.of(
			ModItems.manaMirror, ItemManaMirror.ManaItemImpl::new,
			ModItems.manaRing, ItemManaRing.ManaItemImpl::new,
			ModItems.manaRingGreater, ItemGreaterManaRing.GreaterManaItemImpl::new,
			ModItems.manaTablet, ItemManaTablet.ManaItemImpl::new,
			ModItems.terraPick, ItemTerraPick.ManaItemImpl::new
	));

	private static final Supplier<Map<Item, Function<ItemStack, Relic>>> RELIC = Suppliers.memoize(() -> Map.of(
			ModItems.dice, ItemDice::makeRelic,
			ModItems.flugelEye, ItemFlugelEye::makeRelic,
			ModItems.infiniteFruit, ItemInfiniteFruit::makeRelic,
			ModItems.kingKey, ItemKingKey::makeRelic,
			ModItems.lokiRing, ItemLokiRing::makeRelic,
			ModItems.odinRing, ItemOdinRing::makeRelic,
			ModItems.thorRing, ItemThorRing::makeRelic
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

		var makeManaItem = MANA_ITEM.get().get(stack.getItem());
		if (makeManaItem != null) {
			e.addCapability(prefix("mana_item"),
					CapabilityUtil.makeProvider(BotaniaForgeCapabilities.MANA_ITEM, makeManaItem.apply(stack)));
		}

		var makeRelic = RELIC.get().get(stack.getItem());
		if (makeRelic != null) {
			e.addCapability(prefix("relic"),
					CapabilityUtil.makeProvider(BotaniaForgeCapabilities.RELIC, makeRelic.apply(stack)));
		}
	}

	private void registerBlockLookasides() {
		CapabilityUtil.registerBlockLookaside(BotaniaForgeCapabilities.HORN_HARVEST, (w, p, s) -> (world, pos, stack, hornType, living) -> hornType == HornHarvestable.EnumHornType.CANOPY,
				Blocks.VINE, Blocks.CAVE_VINES, Blocks.CAVE_VINES_PLANT, Blocks.TWISTING_VINES,
				Blocks.TWISTING_VINES_PLANT, Blocks.WEEPING_VINES, Blocks.WEEPING_VINES_PLANT);
		CapabilityUtil.registerBlockLookaside(BotaniaForgeCapabilities.HORN_HARVEST, (w, p, s) -> DefaultHornHarvestable.INSTANCE,
				Arrays.stream(DyeColor.values()).map(ModBlocks::getMushroom).toArray(Block[]::new));
		CapabilityUtil.registerBlockLookaside(BotaniaForgeCapabilities.HORN_HARVEST, (w, p, s) -> DefaultHornHarvestable.INSTANCE,
				Arrays.stream(DyeColor.values()).map(ModBlocks::getShinyFlower).toArray(Block[]::new));
		CapabilityUtil.registerBlockLookaside(BotaniaForgeCapabilities.MANA_GHOST, (w, p, s) -> ((ManaCollisionGhost) s.getBlock()),
				ModBlocks.manaDetector,
				ModBlocks.abstrusePlatform, ModBlocks.infrangiblePlatform, ModBlocks.spectralPlatform,
				ModBlocks.prism, ModBlocks.tinyPlanet);
		CapabilityUtil.registerBlockLookaside(BotaniaForgeCapabilities.MANA_RECEIVER, ManaVoidBlock.ManaReceiverImpl::new, ModBlocks.manaVoid);
		CapabilityUtil.registerBlockLookaside(BotaniaForgeCapabilities.MANA_TRIGGER, DrumBlock.ManaTriggerImpl::new,
				ModBlocks.canopyDrum, ModBlocks.wildDrum, ModBlocks.gatheringDrum);
		CapabilityUtil.registerBlockLookaside(BotaniaForgeCapabilities.MANA_TRIGGER, BlockManaBomb.ManaTriggerImpl::new, ModBlocks.manaBomb);
		CapabilityUtil.registerBlockLookaside(BotaniaForgeCapabilities.MANA_TRIGGER, ManaDetectorBlock.ManaTriggerImpl::new, ModBlocks.manaDetector);
		CapabilityUtil.registerBlockLookaside(BotaniaForgeCapabilities.WANDABLE,
				(world, pos, state) -> (player, stack, side) -> ((BlockPistonRelay) state.getBlock()).onUsedByWand(player, stack, world, pos),
				ModBlocks.pistonRelay);
	}

	private void attachBeCaps(AttachCapabilitiesEvent<BlockEntity> e) {
		var be = e.getObject();
		if (be instanceof AbstractFurnaceBlockEntity furnace) {
			e.addCapability(prefix("exoflame_heatable"),
					CapabilityUtil.makeProvider(BotaniaForgeCapabilities.EXOFLAME_HEATABLE,
							new ExoflameFurnaceHandler.FurnaceExoflameHeatable(furnace)));
		}

		if (be instanceof TileExposedSimpleInventory inv) {
			e.addCapability(prefix("inv"), CapabilityUtil.makeProvider(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, new SidedInvWrapper(inv, null)));
		}

		if (be instanceof TileRFGenerator gen) {
			// we only provide a view of the energy level, no interaction allowed
			var energyStorage = new IEnergyStorage() {
				@Override
				public int getEnergyStored() {
					return gen.getEnergy();
				}

				@Override
				public int getMaxEnergyStored() {
					return TileRFGenerator.MAX_ENERGY;
				}

				@Override
				public boolean canExtract() {
					return false;
				}

				@Override
				public int extractEnergy(int maxExtract, boolean simulate) {
					return 0;
				}

				@Override
				public int receiveEnergy(int maxReceive, boolean simulate) {
					return 0;
				}

				@Override
				public boolean canReceive() {
					return false;
				}
			};
			e.addCapability(prefix("fe"), CapabilityUtil.makeProvider(CapabilityEnergy.ENERGY, energyStorage));
		}

		if (be.getType() == ModTiles.ANIMATED_TORCH) {
			e.addCapability(prefix("hourglass_trigger"), CapabilityUtil.makeProvider(BotaniaForgeCapabilities.HOURGLASS_TRIGGER,
					hourglass -> ((TileAnimatedTorch) be).toggle()));
		}

		if (BlockEntityConstants.SELF_WANDADBLE_BES.contains(be.getType())) {
			e.addCapability(prefix("wandable"), CapabilityUtil.makeProvider(BotaniaForgeCapabilities.WANDABLE,
					(Wandable) be));
		}

		if (be instanceof TileRedStringContainer container) {
			e.addCapability(prefix("red_string"), new RedStringContainerCapProvider(container));
		}

		if (BlockEntityConstants.SELF_MANA_TRIGGER_BES.contains(be.getType())) {
			e.addCapability(prefix("mana_trigger"), CapabilityUtil.makeProvider(BotaniaForgeCapabilities.MANA_TRIGGER, (ManaTrigger) be));
		}

		if (BlockEntityConstants.SELF_MANA_RECEIVER_BES.contains(be.getType())) {
			e.addCapability(prefix("mana_receiver"), CapabilityUtil.makeProvider(BotaniaForgeCapabilities.MANA_RECEIVER, (ManaReceiver) be));
		}

		if (BlockEntityConstants.SELF_SPARK_ATTACHABLE_BES.contains(be.getType())) {
			e.addCapability(prefix("spark_attachable"), CapabilityUtil.makeProvider(BotaniaForgeCapabilities.SPARK_ATTACHABLE, (SparkAttachable) be));
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
