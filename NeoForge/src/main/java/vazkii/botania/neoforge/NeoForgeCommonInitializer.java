/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.neoforge;

import com.google.common.base.Suppliers;
import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.event.*;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.event.entity.EntityEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.EntityTeleportEvent;
import net.neoforged.neoforge.event.entity.item.ItemTossEvent;
import net.neoforged.neoforge.event.entity.living.*;
import net.neoforged.neoforge.event.entity.player.*;
import net.neoforged.neoforge.event.level.ExplosionEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.items.wrapper.SidedInvWrapper;
import net.neoforged.neoforge.registries.RegisterEvent;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.BotaniaRegistries;
import vazkii.botania.api.block.Avatar;
import vazkii.botania.api.block.EdibleBlockWithEffects;
import vazkii.botania.api.block.ExoflameHeatable;
import vazkii.botania.api.block.HourglassTrigger;
import vazkii.botania.api.block.LifeAggregatorCarryable;
import vazkii.botania.api.block.PhantomInkableBlock;
import vazkii.botania.api.block.WandBindable;
import vazkii.botania.api.block.Wandable;
import vazkii.botania.api.corporea.CorporeaHelper;
import vazkii.botania.api.item.AvatarWieldable;
import vazkii.botania.api.item.BlockProvider;
import vazkii.botania.api.item.CoordBoundItem;
import vazkii.botania.api.item.HourglassMaterial;
import vazkii.botania.api.item.Relic;
import vazkii.botania.api.mana.*;
import vazkii.botania.api.mana.spark.ManaSparkAttachable;
import vazkii.botania.api.neoforge.BotaniaNeoForgeCapabilities;
import vazkii.botania.api.neoforge.mana.ManaNetworkEvent;
import vazkii.botania.client.fx.BotaniaParticles;
import vazkii.botania.common.BotaniaCapabilities;
import vazkii.botania.common.BotaniaStats;
import vazkii.botania.common.PlayerAccess;
import vazkii.botania.common.advancements.BotaniaCriteriaTriggers;
import vazkii.botania.common.block.*;
import vazkii.botania.common.block.block_entity.*;
import vazkii.botania.common.block.block_entity.BlockEntityConstants;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntities;
import vazkii.botania.common.block.block_entity.corporea.CorporeaIndexBlockEntity;
import vazkii.botania.common.block.block_entity.flower.BotaniaIslandTypes;
import vazkii.botania.common.block.block_entity.flower.functional.LooniumBlockEntity;
import vazkii.botania.common.block.block_entity.flower.functional.TigerseyeBlockEntity;
import vazkii.botania.common.block.block_entity.flower.functional.VinculotusBlockEntity;
import vazkii.botania.common.block.block_entity.mana.PowerGeneratorBlockEntity;
import vazkii.botania.common.block.mana.DrumBlock;
import vazkii.botania.common.block.mana.ManaDetectorBlock;
import vazkii.botania.common.block.mana.ManaVoidBlock;
import vazkii.botania.common.block.red_string.RedStringInterceptorBlock;
import vazkii.botania.common.brew.BotaniaBrews;
import vazkii.botania.common.brew.BotaniaMobEffects;
import vazkii.botania.common.brew.effect.SoulCrossMobEffect;
import vazkii.botania.common.command.SkyblockCommand;
import vazkii.botania.common.config.ConfigDataManagerImpl;
import vazkii.botania.common.crafting.BotaniaRecipeTypes;
import vazkii.botania.common.crafting.StateIngredients;
import vazkii.botania.common.entity.BotaniaEntities;
import vazkii.botania.common.entity.GaiaGuardianEntity;
import vazkii.botania.common.handler.*;
import vazkii.botania.common.helper.PlayerHelper;
import vazkii.botania.common.impl.BotaniaAPIImpl;
import vazkii.botania.common.impl.corporea.DefaultCorporeaMatchers;
import vazkii.botania.common.impl.mana.DefaultManaItemImpl;
import vazkii.botania.common.integration.corporea.CorporeaNodeDetectors;
import vazkii.botania.common.internal_caps.ItemSources;
import vazkii.botania.common.item.*;
import vazkii.botania.common.item.equipment.armor.terrasteel.TerrasteelHelmItem;
import vazkii.botania.common.item.equipment.bauble.*;
import vazkii.botania.common.item.equipment.tool.terrasteel.TerraBladeItem;
import vazkii.botania.common.item.equipment.tool.terrasteel.TerraTruncatorItem;
import vazkii.botania.common.item.relic.*;
import vazkii.botania.common.item.rod.*;
import vazkii.botania.common.loot.BotaniaLootModifiers;
import vazkii.botania.common.world.BotaniaFeatures;
import vazkii.botania.common.world.SkyblockChunkGenerator;
import vazkii.botania.common.world.SkyblockWorldEvents;
import vazkii.botania.neoforge.integration.InventorySorterIntegration;
import vazkii.botania.neoforge.integration.corporea.NeoForgeCapCorporeaNodeDetector;
import vazkii.botania.neoforge.integration.curios.CurioIntegration;
import vazkii.botania.neoforge.internal_caps.BotaniaNeoforgeDataComponents;
import vazkii.botania.neoforge.internal_caps.ExtrapolatedBucketFluidHandler;
import vazkii.botania.neoforge.internal_caps.NeoForgeInternalEntityCapabilities;
import vazkii.botania.neoforge.internal_caps.RedStringContainerCapProvider;
import vazkii.botania.neoforge.internal_caps.WaterBowlFluidHandler;
import vazkii.botania.neoforge.network.NeoForgePacketHandler;
import vazkii.botania.network.clientbound.ItemLifeTimePacket;
import vazkii.botania.xplat.XplatAbstractions;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

@Mod(BotaniaAPI.MODID)
public class NeoForgeCommonInitializer {
	public NeoForgeCommonInitializer(IEventBus modBus, ModContainer modContainer) {
		// ensure API implementations are loaded
		BotaniaAPI.LOGGER.debug("API instances: {}",
				List.of(BotaniaAPI.instance(), XplatAbstractions.instance(),
						CorporeaHelper.instance(), ManaItemHandler.instance()));

		NeoForgeBotaniaConfig.setup(modContainer);
		EquipmentHandler.init();
		modBus.register(this);
		modBus.addListener(NeoForgePacketHandler::registerPayloadHandlers);
		NeoForgeInternalEntityCapabilities.init(modBus);
	}

	@SubscribeEvent
	public void commonSetup(FMLCommonSetupEvent evt) {
		registerEvents();

		evt.enqueueWork(BotaniaBlocks::addDispenserBehaviours);
		evt.enqueueWork(() -> {
			BiConsumer<ResourceLocation, Supplier<? extends Block>> consumer = (resourceLocation, blockSupplier) -> ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(resourceLocation, blockSupplier);
			BotaniaBlocks.registerFlowerPotPlants(consumer);
		});
		BotaniaItems.registerCauldronInteractions();
		PaintableData.init();
		DefaultCorporeaMatchers.init();
		PlayerHelper.setFakePlayerClass(FakePlayer.class);

		PatchouliAPI.get().registerMultiblock(botaniaRL("alfheim_portal"), AlfheimPortalBlockEntity.MULTIBLOCK.get());
		PatchouliAPI.get().registerMultiblock(BuiltInRegistries.BLOCK.getKey(BotaniaBlocks.TERRESTRIAL_AGGLOMERATION_PLATE), TerrestrialAgglomerationPlateBlockEntity.MULTIBLOCK.get());
		PatchouliAPI.get().registerMultiblock(BuiltInRegistries.BLOCK.getKey(BotaniaBlocks.MANA_ENCHANTER), ManaEnchanterBlockEntity.MULTIBLOCK.get());
		PatchouliAPI.get().registerMultiblock(botaniaRL("gaia_ritual"), GaiaGuardianEntity.ARENA_MULTIBLOCK.get());

		OrechidManager.registerListener();
		BotaniaRecipeIngredientsCache.registerListener();
		ConfigDataManagerImpl.registerListener();
		CraftyCrateBlockEntity.registerListener();
		CorporeaNodeDetectors.register(new NeoForgeCapCorporeaNodeDetector());
	}

	@SubscribeEvent
	private void sendInterModCommunication(InterModEnqueueEvent evt) {
		InventorySorterIntegration.sendImc();
	}

	@SubscribeEvent
	private void createAttributes(EntityAttributeCreationEvent e) {
		BotaniaEntities.registerAttributes((type, builder) -> e.put(type, builder.build()));
	}

	@SubscribeEvent
	private void modifyAttributes(EntityAttributeModificationEvent e) {
		e.add(EntityType.PLAYER, PixieHandler.PIXIE_SPAWN_CHANCE);
	}

	@SubscribeEvent
	private void registryInit(RegisterEvent event) {
		// Core item/block/BE
		runRegistration(event, Registries.SOUND_EVENT, BotaniaSounds::init);
		runRegistration(event, Registries.ARMOR_MATERIAL, BotaniaArmorMaterials::registerArmorMaterials);
		bind(event, Registries.DATA_COMPONENT_TYPE, BotaniaNeoforgeDataComponents::registerComponents);
		bind(event, Registries.BLOCK, consumer -> {
			BotaniaBlocks.registerBlocks(consumer);
			BotaniaBlockFlammability.register();
		});
		bindForItems(event, BotaniaBlocks::registerItemBlocks);
		bind(event, Registries.BLOCK_ENTITY_TYPE, BotaniaBlockEntities::registerTiles);
		bindForItems(event, BotaniaItems::registerItems);

		// GUI and Recipe
		bind(event, Registries.MENU, BotaniaItems::registerMenuTypes);
		bind(event, BotaniaRegistries.STATE_INGREDIENT_TYPE, StateIngredients::submitRegistrations);
		bind(event, Registries.RECIPE_TYPE, BotaniaRecipeTypes::submitRecipeTypes);
		bind(event, Registries.RECIPE_SERIALIZER, BotaniaRecipeTypes::submitRecipeSerializers);

		// Entities
		bind(event, Registries.ENTITY_TYPE, BotaniaEntities::registerEntities);
		runRegistration(event, Registries.ATTRIBUTE, PixieHandler::registerAttribute);
		bind(event, BotaniaRegistries.ITEM_SOURCE, ItemSources::submitRegistrations);

		// Potions
		runRegistration(event, Registries.MOB_EFFECT, BotaniaMobEffects::registerPotions);
		bind(event, BotaniaRegistries.BREWS, BotaniaBrews::submitRegistrations);

		// Worldgen
		bind(event, Registries.FEATURE, BotaniaFeatures::registerFeatures);
		bind(event, Registries.CHUNK_GENERATOR, SkyblockChunkGenerator::submitRegistration);

		// Rest
		bind(event, BotaniaRegistries.ISLAND_TYPES, BotaniaIslandTypes::registerIslandTypes);
		bind(event, Registries.TRIGGER_TYPE, BotaniaCriteriaTriggers::init);
		bind(event, Registries.PARTICLE_TYPE, BotaniaParticles::registerParticles);

		bind(event, Registries.LOOT_CONDITION_TYPE, BotaniaLootModifiers::submitLootConditions);
		bind(event, Registries.LOOT_FUNCTION_TYPE, BotaniaLootModifiers::submitLootFunctions);
		// Vanilla's stat constructor does the registration too, so we use this
		// event only for timing, not for registering
		if (event.getRegistryKey().equals(Registries.CUSTOM_STAT)) {
			// TODO: maybe actually do this the way NeoForge intended
			BotaniaStats.init();
		}

		bind(event, Registries.CREATIVE_MODE_TAB, consumer -> consumer.accept(
				CreativeModeTab.builder()
						.title(Component.translatable("itemGroup.botania").withStyle(style -> style.withColor(ChatFormatting.WHITE)))
						.icon(() -> new ItemStack(BotaniaItems.LEXICA_BOTANIA))
						.withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
						.backgroundTexture(botaniaRL("textures/gui/tab_botania.png"))
						.withSearchBar()
						.build(),
				BotaniaRegistries.BOTANIA_TAB_KEY.location()));
	}

	@SubscribeEvent
	private void registerAdditionalBlockEntityBlocks(BlockEntityTypeAddBlocksEvent event) {
		BotaniaBlockEntities.registerAdditionalBlocks(event::modify);
	}

	private static <T> void runRegistration(RegisterEvent event, ResourceKey<Registry<T>> registryKey, Consumer<Registry<T>> source) {
		Registry<T> registry = event.getRegistry(registryKey);
		if (registry != null) {
			source.accept(registry);
		}
	}

	private static <T> void bind(RegisterEvent event, ResourceKey<Registry<T>> registryKey, Consumer<BiConsumer<T, ResourceLocation>> source) {
		Registry<T> registry = event.getRegistry(registryKey);
		if (registry != null) {
			source.accept((t, rl) -> Registry.register(registry, rl, t));
		}
	}

	private final Set<Item> itemsToAddToCreativeTab = new LinkedHashSet<>();

	private void bindForItems(RegisterEvent event, Consumer<BiConsumer<Item, ResourceLocation>> source) {
		Registry<Item> registry = event.getRegistry(Registries.ITEM);
		if (registry != null) {
			source.accept((t, rl) -> {
				itemsToAddToCreativeTab.add(t);
				Registry.register(registry, rl, t);
			});
		}
	}

	private void registerEvents() {
		IEventBus bus = NeoForge.EVENT_BUS;

		if (XplatAbstractions.INSTANCE.gogLoaded()) {
			bus.addListener((PlayerInteractEvent.RightClickBlock e) -> {
				InteractionResult result = SkyblockWorldEvents.onPlayerInteract(e.getEntity(), e.getLevel(), e.getHand(), e.getHitVec());
				if (result == InteractionResult.SUCCESS) {
					e.setCanceled(true);
					e.setCancellationResult(InteractionResult.SUCCESS);
				}
			});
		}
		bus.addListener((PlayerInteractEvent.LeftClickBlock e) -> ((ShiftingCrustRodItem) BotaniaItems.ROD_OF_THE_SHIFTING_CRUST).onLeftClick(
				e.getEntity(), e.getLevel(), e.getHand(), e.getPos(), e.getFace()));
		bus.addListener((PlayerInteractEvent.LeftClickEmpty e) -> TerraBladeItem.leftClick(e.getItemStack()));
		bus.addListener((AttackEntityEvent e) -> TerraBladeItem.attackEntity(
				e.getEntity(), e.getEntity().level(), InteractionHand.MAIN_HAND, e.getTarget(), null));
		bus.addListener((RegisterCommandsEvent e) -> this.registerCommands(
				e.getDispatcher(), e.getCommandSelection() == Commands.CommandSelection.DEDICATED));
		bus.addListener((CanPlayerSleepEvent e) -> {
			Player.BedSleepingProblem problem = SleepingHandler.trySleep(e.getEntity(), e.getPos());
			if (problem != null) {
				e.setProblem(problem);
			}
		});
		bus.addListener((PlayerEvent.StartTracking e) -> ItemLifeTimePacket.onItemTrack(e.getTarget(), (ServerPlayer) e.getEntity()));
		bus.addListener((ManaNetworkEvent e) -> ManaNetworkHandler.instance.onNetworkEvent(e.getReceiver(), e.getType(), e.getAction()));
		bus.addListener((EntityJoinLevelEvent e) -> {
			if (!e.getLevel().isClientSide()) {
				TigerseyeBlockEntity.pacifyAfterLoad(e.getEntity(), (ServerLevel) e.getLevel());
			}
		});

		bus.addListener((ServerAboutToStartEvent e) -> this.serverAboutToStart(e.getServer()));
		bus.addListener((ServerStoppingEvent e) -> this.serverStopping(e.getServer()));
		bus.addListener((PlayerEvent.PlayerLoggedOutEvent e) -> FlugelTiaraItem.playerLoggedOut((ServerPlayer) e.getEntity()));
		bus.addListener((PlayerEvent.Clone e) -> ResoluteIvyItem.onPlayerRespawn(e.getOriginal(), e.getEntity(), !e.isWasDeath()));
		bus.addListener((LevelTickEvent.Post e) -> {
			if (e.getLevel() instanceof ServerLevel level) {
				CommonTickHandler.onTick(level);
				GrassSeedsItem.onTickEnd(level);
				TerraTruncatorItem.onTickEnd(level);
			}
		});
		bus.addListener((PlayerInteractEvent.RightClickBlock e) -> {
			RedStringInterceptorBlock.onInteract(e.getEntity(), e.getLevel(), e.getHand(), e.getHitVec());
			RingOfLokiItem.onPlayerInteract(e.getEntity(), e.getLevel(), e.getHand(), e.getHitVec());
		});

		bus.addListener(EntityEvent.EntityConstructing.class, NeoForgeInternalEntityCapabilities::trackTntSpawning);

		// Below here are events implemented via Mixins on the Fabric side, ordered by Mixin name
		// FabricMixinAnvilMenu
		bus.addListener((AnvilUpdateEvent e) -> {
			if (SpellbindingClothItem.shouldDenyAnvil(e.getLeft(), e.getRight())) {
				e.setCanceled(true);
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
		bus.addListener((ExplosionEvent.Detonate e) -> {
			if (BenevolentGoddessCharmItem.shouldProtectExplosion(e.getLevel(), e.getExplosion().center())) {
				e.getExplosion().clearToBlow();
			}
		});
		// FabricMixinItemEntity
		bus.addListener((ItemEntityPickupEvent.Pre e) -> {
			ItemEntity entity = e.getItemEntity();
			if (!entity.hasPickUpDelay() && (entity.getOwner() == null || entity.getOwner() == e.getPlayer())
					&& ColoredContentsPouchItem.onPickupItem(entity, e.getPlayer())) {
				e.setCanPickup(TriState.FALSE);
			}
		});
		// FabricMixinLivingEntity
		{
			bus.addListener((LivingDropsEvent e) -> {
				var living = e.getEntity();
				LooniumBlockEntity.dropLooniumItems(living, stack -> {
					e.getDrops().clear();
					if (!stack.isEmpty()) {
						var ent = new ItemEntity(living.level(), living.getX(), living.getY(), living.getZ(), stack);
						ent.setDefaultPickUpDelay();
						e.getDrops().add(ent);
					}
				});
			});
			bus.addListener((LivingDeathEvent e) -> {
				if (e.getSource().getEntity() instanceof LivingEntity killer) {
					SoulCrossMobEffect.onEntityKill(e.getEntity(), killer);
				}
			});
			bus.addListener((LivingEvent.LivingJumpEvent e) -> SojournersSashItem.onPlayerJump(e.getEntity()));
		}
		// FabricMixinPlayer
		{
			bus.addListener((ItemTossEvent e) -> RingOfMagnetizationItem.onTossItem(e.getPlayer()));
			// TODO parity: Fabric mixes into super.hurt() call in Player::hurt instead
			bus.addListener((LivingIncomingDamageEvent e) -> {
				if (e.getEntity() instanceof Player player) {
					Container worn = EquipmentHandler.getAllWorn(player);
					for (int i = 0; i < worn.getContainerSize(); i++) {
						ItemStack stack = worn.getItem(i);
						if (stack.getItem() instanceof CloakOfVirtueItem cloak) {
							e.setAmount(cloak.onPlayerDamage(player, e.getSource(), e.getAmount()));
						}
					}

					PixieHandler.onDamageTaken(player, e.getSource());
				}
				if (e.getSource().getDirectEntity() instanceof Player player) {
					CharmOfTheDivaItem.onEntityDamaged(player, e.getEntity());
				}
			});
			bus.addListener((PlayerTickEvent.Pre e) -> {
				Player player = e.getEntity();
				FlugelTiaraItem.updatePlayerFlyStatus(player);
				SojournersSashItem.tickBelt(player);
				if (!player.level().isClientSide()) {
					EnderOverseerBlockEntity.checkLookingAtEnderOverseer(player);
				}
			});
			bus.addListener((LivingFallEvent e) -> {
				if (e.getEntity() instanceof Player player) {
					e.setDistance(SojournersSashItem.onPlayerFall(player, e.getDistance()));
				}
			});
			bus.addListener(EventPriority.LOW, (CriticalHitEvent e) -> {
				if (e.getEntity().level().isClientSide()
						|| !e.isCriticalHit()
						|| !TerrasteelHelmItem.hasTerraArmorSet(e.getEntity())
						|| !(e.getTarget() instanceof LivingEntity target)) {
					return;
				}
				e.setDamageMultiplier(e.getDamageMultiplier() * TerrasteelHelmItem.getCritDamageMult(e.getEntity()));
				((PlayerAccess) e.getEntity()).botania$setCritTarget(target);
			});

		}
		// FabricMixinResultSlot
		bus.addListener((PlayerEvent.ItemCraftedEvent e) -> AssemblyHaloItem.onItemCrafted(e.getEntity(), e.getInventory()));
	}

	// Attaching caps requires dispatching off the item, which is a huge pain because it generates long if-else
	// chains on items, and also doesn't match how Fabric is set up.
	// Instead, let's declare ahead of time what items get which caps, similar to how we do it for Fabric.
	// Needs to be lazy since items aren't initialized yet
	private static final Supplier<Map<Item, BiFunction<ItemStack, Avatar, AvatarWieldable>>> AVATAR_WIELDABLES = Suppliers.memoize(() -> Map.of(
			BotaniaItems.ROD_OF_THE_LANDS, LandsRodItem.AvatarBehavior::new,
			BotaniaItems.ROD_OF_THE_PLENTIFUL_MANTLE, PlentifulMantleRodItem.AvatarBehavior::new,
			BotaniaItems.ROD_OF_THE_HELLS, HellsRodItem.AvatarBehavior::new,
			BotaniaItems.ROD_OF_THE_UNSTABLE_RESERVOIR, UnstableReservoirRodItem.AvatarBehavior::new,
			BotaniaItems.ROD_OF_THE_BIFROST, BifrostRodItem.AvatarBehavior::new,
			BotaniaItems.ROD_OF_THE_SKIES, SkiesRodItem.AvatarBehavior::new
	));

	private static final Supplier<Map<Item, Function<ItemStack, BlockProvider>>> BLOCK_PROVIDER = Suppliers.memoize(() -> Map.of(
			BotaniaItems.ROD_OF_THE_LANDS, s -> new LandsRodItem.BlockProviderImpl(),
			BotaniaItems.ROD_OF_THE_HIGHLANDS, s -> new LandsRodItem.BlockProviderImpl(),
			BotaniaItems.BLACK_HOLE_TALISMAN, BlackHoleTalismanItem.BlockProviderImpl::new,
			BotaniaItems.ROD_OF_THE_DEPTHS, s -> new DepthsRodItem.BlockProviderImpl(),
			BotaniaItems.HAND_OF_ENDER, EnderHandItem.BlockProviderImpl::new,
			BotaniaItems.ROD_OF_THE_TERRA_FIRMA, s -> new LandsRodItem.BlockProviderImpl()
	));

	private static final Supplier<Map<Item, Function<ItemStack, CoordBoundItem>>> COORD_BOUND_ITEM = Suppliers.memoize(() -> Map.of(
			BotaniaItems.EYE_OF_THE_FLUGEL, EyeOfTheFlugelItem.CoordBoundItemImpl::new,
			BotaniaItems.MANA_MIRROR, ManaMirrorItem.CoordBoundItemImpl::new,
			BotaniaItems.WAND_OF_THE_FOREST, WandOfTheForestItem.CoordBoundItemImpl::new,
			BotaniaItems.WAND_OF_THE_ELVEN_FOREST, WandOfTheForestItem.CoordBoundItemImpl::new
	));

	private static final Supplier<Map<Item, Function<ItemStack, HourglassMaterial>>> HOURGLASS_MATERIAL = Suppliers.memoize(() -> Map.of(
			Items.SAND, s -> HourglassMaterial.SAND,
			Items.RED_SAND, s -> HourglassMaterial.RED_SAND,
			Items.SOUL_SAND, s -> HourglassMaterial.SOUL_SAND,
			BotaniaItems.MANA_POWDER, s -> HourglassMaterial.MANA_POWDER
	));

	private static final Supplier<Map<Item, Function<ItemStack, ManaItem>>> MANA_ITEM = Suppliers.memoize(() -> Map.of(
			BotaniaItems.MANA_MIRROR, DefaultManaItemImpl::new,
			BotaniaItems.BAND_OF_MANA, DefaultManaItemImpl::new,
			BotaniaItems.GREATER_BAND_OF_MANA, DefaultManaItemImpl::new,
			BotaniaItems.MANA_TABLET, DefaultManaItemImpl::new,
			BotaniaItems.TERRA_SHATTERER, DefaultManaItemImpl::new
	));

	private static final Supplier<Map<Item, Function<ItemStack, Relic>>> RELIC = Suppliers.memoize(() -> Map.of(
			BotaniaItems.DICE_OF_FATE, DiceOfFateItem::makeRelic,
			BotaniaItems.EYE_OF_THE_FLUGEL, EyeOfTheFlugelItem::makeRelic,
			BotaniaItems.FRUIT_OF_GRISAIA, FruitOfGrisaiaItem::makeRelic,
			BotaniaItems.KEY_OF_THE_KINGS_LAW, KeyOfTheKingsLawItem::makeRelic,
			BotaniaItems.RING_OF_LOKI, RingOfLokiItem::makeRelic,
			BotaniaItems.RING_OF_ODIN, RingOfOdinItem::makeRelic,
			BotaniaItems.RING_OF_THOR, RingOfThorItem::makeRelic
	));

	@SubscribeEvent
	private void attachCapabilities(RegisterCapabilitiesEvent e) {
		BotaniaCapabilities.registerCapabilities(BotaniaNeoForgeCapabilities.getRegistration());

		attachItemCaps(e);
		registerBlockCapabilities(e);
	}

	private void attachItemCaps(RegisterCapabilitiesEvent e) {

		if (EquipmentHandler.instance instanceof CurioIntegration ci) {
			Item[] baubleItems = BuiltInRegistries.ITEM.stream()
					.filter(item -> item instanceof BaubleItem)
					.toArray(Item[]::new);
			ci.initCapability(e, baubleItems);
		}

		e.registerItem(Capabilities.FluidHandler.ITEM,
				(stack, context) -> new WaterBowlFluidHandler(stack),
				BotaniaItems.WATER_BOWL
		);
		e.registerItem(Capabilities.FluidHandler.ITEM,
				(stack, context) -> new ExtrapolatedBucketFluidHandler(stack),
				BotaniaItems.EXTRAPOLATED_BUCKET
		);

		attachMappedItemCapsWithContext(e, BotaniaNeoForgeCapabilities.getItemApiLookupById(AvatarWieldable.LOOKUP), AVATAR_WIELDABLES.get());
		attachMappedItemCaps(e, BotaniaNeoForgeCapabilities.getItemApiLookupById(BlockProvider.LOOKUP), BLOCK_PROVIDER.get());
		attachMappedItemCaps(e, BotaniaNeoForgeCapabilities.getItemApiLookupById(CoordBoundItem.LOOKUP), COORD_BOUND_ITEM.get());
		attachMappedItemCaps(e, BotaniaNeoForgeCapabilities.getItemApiLookupById(HourglassMaterial.LOOKUP), HOURGLASS_MATERIAL.get());
		attachMappedItemCaps(e, BotaniaNeoForgeCapabilities.getItemApiLookupById(ManaItem.LOOKUP), MANA_ITEM.get());
		attachMappedItemCaps(e, BotaniaNeoForgeCapabilities.getItemApiLookupById(Relic.LOOKUP), RELIC.get());
	}

	private static <T> void attachMappedItemCaps(RegisterCapabilitiesEvent e, ItemCapability<T, Void> capability,
			Map<Item, Function<ItemStack, T>> itemProviderMap) {
		itemProviderMap.forEach((item, provider) -> e.registerItem(
				capability, (stack, context) -> provider.apply(stack), item));
	}

	private static <T, C> void attachMappedItemCapsWithContext(RegisterCapabilitiesEvent e, ItemCapability<T, C> capability,
			Map<Item, BiFunction<ItemStack, C, T>> itemProviderMap) {
		itemProviderMap.forEach((item, provider) -> e.registerItem(
				capability, provider::apply, item));
	}

	private void registerBlockCapabilities(RegisterCapabilitiesEvent e) {
		BlockCapability<EdibleBlockWithEffects, Void> edibleBlockWithEffectCapability =
				BotaniaNeoForgeCapabilities.getBlockApiLookupById(EdibleBlockWithEffects.LOOKUP);
		// these two blocks implement the capability directly
		e.registerBlock(edibleBlockWithEffectCapability,
				(level, pos, state, blockEntity, context) -> (EdibleBlockWithEffects) state.getBlock(),
				BotaniaBlocks.MUTATED_GRASS_BLOCK, BotaniaBlocks.INFUSED_GRASS_BLOCK
		);

		// TODO: is there any way to identify all BlockEntityTypes for AbstractFurnaceBlock subclasses?
		BlockCapability<ExoflameHeatable, Void> exoflameHeatableBlockCap =
				BotaniaNeoForgeCapabilities.getBlockApiLookupById(ExoflameHeatable.LOOKUP);
		Stream.of(BlockEntityType.FURNACE, BlockEntityType.BLAST_FURNACE, BlockEntityType.SMOKER)
				.forEach(blockEntityType -> e.registerBlockEntity(
						exoflameHeatableBlockCap, blockEntityType,
						(furnace, context) -> new ExoflameFurnaceHandler.FurnaceExoflameHeatable(furnace)));

		BlockCapability<HourglassTrigger, Void> hourglassTriggerBlockCap =
				BotaniaNeoForgeCapabilities.getBlockApiLookupById(HourglassTrigger.LOOKUP);
		e.registerBlockEntity(hourglassTriggerBlockCap, BotaniaBlockEntities.ANIMATED_TORCH,
				(torchBlockEntity, context) -> torchBlockEntity);

		// TODO: ManaCollisionGhost feels like it could be represented by two tags for fully-ignored and trigger-only blocks, respectively
		BlockCapability<ManaCollisionGhost, Void> manaCollisionGhostBlockCap =
				BotaniaNeoForgeCapabilities.getBlockApiLookupById(ManaCollisionGhost.LOOKUP);
		e.registerBlock(manaCollisionGhostBlockCap,
				(level, pos, state, blockEntity, context) -> (ManaCollisionGhost) state.getBlock(),
				BotaniaBlocks.MANA_DETECTOR,
				BotaniaBlocks.ABSTRUSE_PLATFORM, BotaniaBlocks.INFRANGIBLE_PLATFORM, BotaniaBlocks.SPECTRAL_PLATFORM,
				BotaniaBlocks.MANA_PRISM, BotaniaBlocks.TINY_PLANET
		);

		BlockCapability<ManaReceiver, Direction> manaReceiverBlockCap =
				BotaniaNeoForgeCapabilities.getBlockApiLookupById(ManaReceiver.LOOKUP);
		BlockEntityConstants.SELF_MANA_RECEIVER_BES.forEach(type -> e.registerBlockEntity(
				manaReceiverBlockCap, type, (blockEntity, context) -> blockEntity));
		e.registerBlock(manaReceiverBlockCap,
				(level, pos, state, blockEntity, context) -> new ManaVoidBlock.ManaReceiverImpl(level, pos, state),
				BotaniaBlocks.MANA_VOID
		);

		BlockCapability<ManaSparkAttachable, Void> sparkAttachableBlockCap =
				BotaniaNeoForgeCapabilities.getBlockApiLookupById(ManaSparkAttachable.LOOKUP);
		BlockEntityConstants.SELF_SPARK_ATTACHABLE_BES.forEach(blockEntityType -> e.registerBlockEntity(
				sparkAttachableBlockCap, blockEntityType, (blockEntity, context) -> blockEntity));

		BlockCapability<ManaTrigger, Void> manaTriggerBlockCap =
				BotaniaNeoForgeCapabilities.getBlockApiLookupById(ManaTrigger.LOOKUP);
		BlockEntityConstants.SELF_MANA_TRIGGER_BES.forEach(blockEntityType -> e.registerBlockEntity(
				manaTriggerBlockCap, blockEntityType, (blockEntity, context) -> blockEntity));
		e.registerBlock(manaTriggerBlockCap,
				(level, pos, state, blockEntity, context) -> new DrumBlock.ManaTriggerImpl(level, pos, state),
				BotaniaBlocks.DRUM_OF_THE_CANOPY, BotaniaBlocks.DRUM_OF_THE_WILD, BotaniaBlocks.DRUM_OF_THE_GATHERING
		);
		e.registerBlock(manaTriggerBlockCap,
				(level, pos, state, blockEntity, context) -> new ManastormChargeBlock.ManaTriggerImpl(level, pos),
				BotaniaBlocks.MANASTORM_CHARGE
		);
		e.registerBlock(manaTriggerBlockCap,
				(level, pos, state, blockEntity, context) -> new ManaDetectorBlock.ManaTriggerImpl(level, pos, state),
				BotaniaBlocks.MANA_DETECTOR
		);

		BlockCapability<Wandable, Direction> wandableBlockCap =
				BotaniaNeoForgeCapabilities.getBlockApiLookupById(Wandable.LOOKUP);
		BlockEntityConstants.SELF_WANDABLE_BES.forEach(blockEntityType -> e.registerBlockEntity(
				wandableBlockCap, blockEntityType, (blockEntity, context) -> blockEntity));
		e.registerBlock(wandableBlockCap, ForceRelayBlock::createWandable,
				BotaniaBlocks.FORCE_RELAY
		);
		e.registerBlock(wandableBlockCap, ManaEnchanterBlockEntity::createLapisBlockWandable,
				Blocks.LAPIS_BLOCK);

		BlockCapability<WandBindable, Direction> wandBindableBlockCap =
				BotaniaNeoForgeCapabilities.getBlockApiLookupById(WandBindable.LOOKUP);
		BlockEntityConstants.SELF_WAND_BINDABLE_BES.forEach(blockEntityType -> e.registerBlockEntity(
				wandBindableBlockCap, blockEntityType, (blockEntity, context) -> blockEntity));
		e.registerBlock(wandBindableBlockCap, ForceRelayBlock::createWandBindable,
				BotaniaBlocks.FORCE_RELAY
		);

		BlockCapability<PhantomInkableBlock, Void> phantomInkableBlockCap =
				BotaniaNeoForgeCapabilities.getBlockApiLookupById(PhantomInkableBlock.LOOKUP);
		BlockEntityConstants.SELF_PHANTOM_INKABLE_BES.forEach(blockEntityType -> e.registerBlockEntity(
				phantomInkableBlockCap, blockEntityType, (blockEntity, context) -> blockEntity));

		Stream.of(BotaniaBlockEntities.RED_STRINGED_CONTAINER, BotaniaBlockEntities.RED_STRINGED_DISPENSER)
				.forEach(blockEntityType -> e.registerBlockEntity(
						Capabilities.ItemHandler.BLOCK, blockEntityType, new RedStringContainerCapProvider()));

		BlockEntityConstants.SELF_WORLDLY_CONTAINERS.forEach(blockEntityType -> e.registerBlockEntity(
				Capabilities.ItemHandler.BLOCK, blockEntityType, SidedInvWrapper::new));

		BlockCapability<LifeAggregatorCarryable, Void> lifeAggregatorCarryableBlockCap =
				BotaniaNeoForgeCapabilities.getBlockApiLookupById(LifeAggregatorCarryable.LOOKUP);
		e.registerBlockEntity(lifeAggregatorCarryableBlockCap, BlockEntityType.MOB_SPAWNER,
				(blockEntity, context) -> new LifeAggregatorHandler.MonsterSpawnerCarryable(blockEntity));
		e.registerBlockEntity(lifeAggregatorCarryableBlockCap, BlockEntityType.TRIAL_SPAWNER,
				(blockEntity, context) -> new LifeAggregatorHandler.TrialSpawnerCarryable(blockEntity));

		e.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, BotaniaBlockEntities.MANA_FLUXFIELD,
				// we only provide a view of the energy level, no interaction allowed
				(gen, context) -> new IEnergyStorage() {
					@Override
					public int getEnergyStored() {
						return gen.getEnergy();
					}

					@Override
					public int getMaxEnergyStored() {
						return PowerGeneratorBlockEntity.MAX_ENERGY;
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
				});
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
		if (XplatAbstractions.INSTANCE.gogLoaded()) {
			SkyblockCommand.register(dispatcher);
		}
	}

	private void serverStopping(MinecraftServer server) {
		CorporeaIndexBlockEntity.clearIndexCache();
	}

	@SubscribeEvent
	private void addItemsToCreativeTab(BuildCreativeModeTabContentsEvent e) {
		if (e.getTabKey() == BotaniaRegistries.BOTANIA_TAB_KEY) {
			for (Item item : this.itemsToAddToCreativeTab) {
				if (item instanceof CustomCreativeTabContents cc) {
					cc.addToCreativeTab(item, e);
				} else if (item instanceof BlockItem bi && bi.getBlock() instanceof CustomCreativeTabContents cc) {
					cc.addToCreativeTab(item, e);
				} else {
					e.accept(item);
				}
			}
		}
	}
}
