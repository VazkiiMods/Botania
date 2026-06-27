/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.fabric;

import com.mojang.brigadier.CommandDispatcher;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.FakePlayer;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.item.v1.EnchantmentEvents;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.fabricmc.fabric.api.lookup.v1.item.ItemApiLookup;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.api.networking.v1.EntityTrackingEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.MinecartComparatorLogicRegistry;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.fabricmc.fabric.api.registry.FlattenableBlockRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.fabricmc.fabric.api.registry.TillableBlockRegistry;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.FullItemFluidStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.InsertionOnlyStorage;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.Direction;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Unit;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.material.Fluids;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.BotaniaFabricCapabilities;
import vazkii.botania.api.BotaniaRegistries;
import vazkii.botania.api.block.Avatar;
import vazkii.botania.api.block.EdibleBlockWithEffects;
import vazkii.botania.api.block.ExoflameHeatable;
import vazkii.botania.api.block.HourglassTrigger;
import vazkii.botania.api.block.IslandType;
import vazkii.botania.api.block.PhantomInkableBlock;
import vazkii.botania.api.block.WandBindable;
import vazkii.botania.api.block.Wandable;
import vazkii.botania.api.brew.Brew;
import vazkii.botania.api.corporea.CorporeaHelper;
import vazkii.botania.api.internal.ItemSource;
import vazkii.botania.api.item.AvatarWieldable;
import vazkii.botania.api.item.BlockProvider;
import vazkii.botania.api.item.CoordBoundItem;
import vazkii.botania.api.item.HourglassMaterial;
import vazkii.botania.api.item.Relic;
import vazkii.botania.api.mana.ManaCollisionGhost;
import vazkii.botania.api.mana.ManaItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.api.mana.ManaNetworkCallback;
import vazkii.botania.api.mana.ManaReceiver;
import vazkii.botania.api.mana.ManaTrigger;
import vazkii.botania.api.mana.spark.SparkAttachable;
import vazkii.botania.api.recipe.StateIngredientType;
import vazkii.botania.client.fx.BotaniaParticles;
import vazkii.botania.common.BotaniaCapabilities;
import vazkii.botania.common.BotaniaStats;
import vazkii.botania.common.advancements.*;
import vazkii.botania.common.block.*;
import vazkii.botania.common.block.block_entity.*;
import vazkii.botania.common.block.block_entity.BlockEntityConstants;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntities;
import vazkii.botania.common.block.block_entity.corporea.CorporeaIndexBlockEntity;
import vazkii.botania.common.block.block_entity.flower.BotaniaIslandTypes;
import vazkii.botania.common.block.block_entity.flower.functional.TigerseyeBlockEntity;
import vazkii.botania.common.block.mana.DrumBlock;
import vazkii.botania.common.block.mana.ManaDetectorBlock;
import vazkii.botania.common.block.mana.ManaVoidBlock;
import vazkii.botania.common.block.red_string.RedStringInterceptorBlock;
import vazkii.botania.common.brew.BotaniaBrews;
import vazkii.botania.common.brew.BotaniaMobEffects;
import vazkii.botania.common.command.SkyblockCommand;
import vazkii.botania.common.component.BotaniaDataComponents;
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
import vazkii.botania.common.internal_caps.BotaniaDataAttachments;
import vazkii.botania.common.internal_caps.ItemSources;
import vazkii.botania.common.item.*;
import vazkii.botania.common.item.equipment.bauble.FlugelTiaraItem;
import vazkii.botania.common.item.equipment.tool.terrasteel.TerraBladeItem;
import vazkii.botania.common.item.equipment.tool.terrasteel.TerraTruncatorItem;
import vazkii.botania.common.item.material.EnderAirItem;
import vazkii.botania.common.item.relic.*;
import vazkii.botania.common.item.rod.*;
import vazkii.botania.common.lib.BotaniaTags;
import vazkii.botania.common.loot.BotaniaLootModifiers;
import vazkii.botania.common.world.BotaniaFeatures;
import vazkii.botania.common.world.SkyblockChunkGenerator;
import vazkii.botania.common.world.SkyblockWorldEvents;
import vazkii.botania.fabric.block_entity.FabricRedStringContainerBlockEntity;
import vazkii.botania.fabric.integration.corporea.FabricTransferCorporeaNodeDetector;
import vazkii.botania.fabric.integration.tr_energy.FluxfieldTRStorage;
import vazkii.botania.fabric.internal_caps.FabricInternalEntityAttachments;
import vazkii.botania.fabric.internal_caps.RedStringContainerStorage;
import vazkii.botania.fabric.loot.LootHandler;
import vazkii.botania.fabric.network.FabricPacketHandler;
import vazkii.botania.network.clientbound.ItemLifeTimePacket;
import vazkii.botania.xplat.XplatAbstractions;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.*;
import java.util.function.BiConsumer;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

public class FabricCommonInitializer implements ModInitializer {
	private static final Registry<Brew> BREW_REGISTRY = FabricRegistryBuilder
			.createDefaulted(BotaniaRegistries.BREWS, Brew.DEFAULT_ID).buildAndRegister();
	private static final Registry<IslandType> ISLAND_TYPE_REGISTRY = FabricRegistryBuilder
			.createDefaulted(BotaniaRegistries.ISLAND_TYPES, IslandType.DEFAULT_ID).buildAndRegister();
	private static final MappedRegistry<StateIngredientType<?>> STATE_INGREDIENT_TYPE_REGISTRY = FabricRegistryBuilder
			.createDefaulted(BotaniaRegistries.STATE_INGREDIENT_TYPE, StateIngredientType.DEFAULT_ID).buildAndRegister();
	private static final MappedRegistry<ItemSource> ITEM_SOURCE_REGISTRY = FabricRegistryBuilder
			.createSimple(BotaniaRegistries.ITEM_SOURCE).buildAndRegister();

	@Override
	public void onInitialize() {
		coreInit();
		registryInit();

		PaintableData.init();
		CompostingData.init(CompostingChanceRegistry.INSTANCE::add);
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
		CorporeaNodeDetectors.register(new FabricTransferCorporeaNodeDetector());

		registerCapabilities();
		registerEvents();
	}

	private void coreInit() {
		// ensure API implementations are loaded
		BotaniaAPI.LOGGER.debug("API instances: {}",
				List.of(BotaniaAPI.instance(), XplatAbstractions.instance(),
						CorporeaHelper.instance(), ManaItemHandler.instance()));

		FiberBotaniaConfig.setup();
		EquipmentHandler.init();
		FabricPacketHandler.init();
	}

	private void registryInit() {
		// Core item/block/BE
		BotaniaSounds.init(BuiltInRegistries.SOUND_EVENT);
		BotaniaArmorMaterials.registerArmorMaterials(BuiltInRegistries.ARMOR_MATERIAL);
		BotaniaDataComponents.registerComponents(bind(BuiltInRegistries.DATA_COMPONENT_TYPE));
		BotaniaBlocks.registerBlocks(bind(BuiltInRegistries.BLOCK));
		BotaniaBlocks.registerItemBlocks(boundForItem);
		BotaniaBlockFlammability.register();
		BotaniaBlockEntities.registerTiles(bind(BuiltInRegistries.BLOCK_ENTITY_TYPE));
		BotaniaBlockEntities.registerAdditionalBlocks(BlockEntityType::addSupportedBlock);
		BotaniaItems.registerItems(boundForItem);
		BotaniaBlocks.addDispenserBehaviours();
		BotaniaBlocks.addAxeStripping();
		BotaniaItems.registerCauldronInteractions();
		for (Block b : List.of(BotaniaBlocks.DRY_GRASS_BLOCK, BotaniaBlocks.GOLDEN_GRASS_BLOCK,
				BotaniaBlocks.VIVID_GRASS_BLOCK, BotaniaBlocks.SCORCHED_GRASS_BLOCK,
				BotaniaBlocks.INFUSED_GRASS_BLOCK, BotaniaBlocks.MUTATED_GRASS_BLOCK
		)) {
			TillableBlockRegistry.register(b, HoeItem::onlyIfAirAbove,
					Blocks.FARMLAND.defaultBlockState());
			FlattenableBlockRegistry.register(b, Blocks.DIRT_PATH.defaultBlockState());
		}
		EnchantmentEvents.ALLOW_ENCHANTING.register((enchantment, target, enchantingContext) -> target.is(BotaniaItems.ELEMENTIUM_AXE) && enchantment.is(Enchantments.LOOTING) ? TriState.TRUE : TriState.DEFAULT);

		int blazeTime = 2400;
		FuelRegistry.INSTANCE.add(BotaniaBlocks.BLAZE_MESH.asItem(), blazeTime * (XplatAbstractions.INSTANCE.gogLoaded() ? 5 : 10));

		// GUI and Recipe
		BotaniaItems.registerMenuTypes(bind(BuiltInRegistries.MENU));
		StateIngredients.submitRegistrations(bind(STATE_INGREDIENT_TYPE_REGISTRY));
		BotaniaRecipeTypes.submitRecipeTypes(bind(BuiltInRegistries.RECIPE_TYPE));
		BotaniaRecipeTypes.submitRecipeSerializers(bind(BuiltInRegistries.RECIPE_SERIALIZER));

		// Entities
		BotaniaEntities.registerEntities(bind(BuiltInRegistries.ENTITY_TYPE));
		PixieHandler.registerAttribute(BuiltInRegistries.ATTRIBUTE);
		BotaniaEntities.registerAttributes(FabricDefaultAttributeRegistry::register);
		MinecartComparatorLogicRegistry.register(BotaniaEntities.POOL_MINECART, (minecart, state, pos) -> minecart.getComparatorLevel());
		ItemSources.submitRegistrations(bind(ITEM_SOURCE_REGISTRY));

		// Potions
		BotaniaMobEffects.registerPotions(BuiltInRegistries.MOB_EFFECT);

		BotaniaBrews.submitRegistrations(bind(BREW_REGISTRY));

		// Worldgen
		BotaniaFeatures.registerFeatures(bind(BuiltInRegistries.FEATURE));
		SkyblockChunkGenerator.submitRegistration(bind(BuiltInRegistries.CHUNK_GENERATOR));
		BiomeModifications.addFeature(
				ctx -> ctx.hasTag(BotaniaTags.Biomes.MYSTICAL_FLOWER_SPAWNLIST)
						&& !ctx.hasTag(BotaniaTags.Biomes.MYSTICAL_FLOWER_BLOCKLIST),
				GenerationStep.Decoration.VEGETAL_DECORATION,
				BotaniaFeatures.MYSTICAL_FLOWERS_PLACED_FEATURE);
		BiomeModifications.addFeature(
				ctx -> ctx.hasTag(BotaniaTags.Biomes.SHIMMERING_MUSHROOM_SPAWNLIST)
						&& !ctx.hasTag(BotaniaTags.Biomes.SHIMMERING_MUSHROOM_BLOCKLIST),
				GenerationStep.Decoration.VEGETAL_DECORATION,
				BotaniaFeatures.SHIMMERING_MUSHROOMS_PLACED_FEATURE
		);

		// Rest
		BotaniaIslandTypes.registerIslandTypes(bind(ISLAND_TYPE_REGISTRY));
		BotaniaCriteriaTriggers.init(bind(BuiltInRegistries.TRIGGER_TYPES));
		BotaniaParticles.registerParticles(bind(BuiltInRegistries.PARTICLE_TYPE));
		BotaniaLootModifiers.submitLootConditions(bind(BuiltInRegistries.LOOT_CONDITION_TYPE));
		BotaniaLootModifiers.submitLootFunctions(bind(BuiltInRegistries.LOOT_FUNCTION_TYPE));
		BotaniaStats.init();
		Registry.register(
				BuiltInRegistries.CREATIVE_MODE_TAB,
				BotaniaRegistries.BOTANIA_TAB_KEY,
				FabricItemGroup.builder()
						.title(Component.translatable("itemGroup.botania").withStyle((style -> style.withColor(ChatFormatting.WHITE))))
						.icon(() -> new ItemStack(BotaniaItems.LEXICA_BOTANIA))
						.backgroundTexture(botaniaRL("textures/gui/tab_botania.png"))
						.build()
		);
		ItemGroupEvents.modifyEntriesEvent(BotaniaRegistries.BOTANIA_TAB_KEY)
				.register(entries -> {
					for (Item item : this.itemsToAddToCreativeTab) {
						if (item instanceof CustomCreativeTabContents cc) {
							cc.addToCreativeTab(item, entries);
						} else if (item instanceof BlockItem bi && bi.getBlock() instanceof CustomCreativeTabContents cc) {
							cc.addToCreativeTab(item, entries);
						} else {
							entries.accept(item);
						}
					}
				});
	}

	private void registerEvents() {
		if (XplatAbstractions.INSTANCE.gogLoaded()) {
			UseBlockCallback.EVENT.register(SkyblockWorldEvents::onPlayerInteract);
		}
		AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) -> ((ShiftingCrustRodItem) BotaniaItems.ROD_OF_THE_SHIFTING_CRUST).onLeftClick(player, world, hand, pos, direction));
		AttackEntityCallback.EVENT.register(ShadedMesaRodItem::onAttack);
		AttackEntityCallback.EVENT.register(TerraBladeItem::attackEntity);
		CommandRegistrationCallback.EVENT.register(this::registerCommands);
		EntitySleepEvents.ALLOW_SLEEPING.register(SleepingHandler::trySleep);
		EntityTrackingEvents.START_TRACKING.register(ItemLifeTimePacket::onItemTrack);
		LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
			if (source.isBuiltin()) {
				LootHandler.injectLoot(key.location(), tableBuilder::withPool);
				LootHandler.injectGogLoot(key.location(), tableBuilder::withPool);
			}
		});
		ManaNetworkCallback.EVENT.register(ManaNetworkHandler.instance::onNetworkEvent);
		ServerEntityEvents.ENTITY_LOAD.register(TigerseyeBlockEntity::pacifyAfterLoad);
		ServerLifecycleEvents.SERVER_STARTED.register(this::serverAboutToStart);
		ServerLifecycleEvents.SERVER_STOPPING.register(this::serverStopping);
		ServerPlayConnectionEvents.DISCONNECT.register(((handler, server) -> FlugelTiaraItem.playerLoggedOut(handler.player)));
		ServerPlayerEvents.AFTER_RESPAWN.register(ResoluteIvyItem::onPlayerRespawn);
		ServerTickEvents.END_WORLD_TICK.register(CommonTickHandler::onTick);
		ServerTickEvents.END_WORLD_TICK.register(GrassSeedsItem::onTickEnd);
		ServerTickEvents.END_WORLD_TICK.register(TerraTruncatorItem::onTickEnd);
		UseBlockCallback.EVENT.register(RedStringInterceptorBlock::onInteract);
		UseBlockCallback.EVENT.register(RingOfLokiItem::onPlayerInteract);
		UseItemCallback.EVENT.register(EnderAirItem::onPlayerInteract);
	}

	private static <T> BiConsumer<T, ResourceLocation> bind(Registry<? super T> registry) {
		return (t, id) -> Registry.register(registry, id, t);
	}

	private final Set<Item> itemsToAddToCreativeTab = new LinkedHashSet<>();
	private final BiConsumer<Item, ResourceLocation> boundForItem =
			(t, id) -> {
				this.itemsToAddToCreativeTab.add(t);
				Registry.register(BuiltInRegistries.ITEM, id, t);
			};

	private void registerCapabilities() {
		BotaniaCapabilities.registerCapabilities(BotaniaFabricCapabilities.getRegistration());
		BotaniaDataAttachments.registerDataAttachments(FabricInternalEntityAttachments::register);

		FluidStorage.ITEM.registerForItems((stack, context) -> new FullItemFluidStorage(context, Items.BOWL, FluidVariant.of(Fluids.WATER), FluidConstants.BLOCK),
				BotaniaItems.WATER_BOWL
		);
		FluidStorage.ITEM.registerForItems((itemStack, context) -> (InsertionOnlyStorage<FluidVariant>) (resource, maxAmount, transaction) -> Math.min(FluidConstants.BLOCK, maxAmount),
				BotaniaItems.EXTRAPOLATED_BUCKET
		);

		ItemApiLookup<AvatarWieldable, Avatar> avatarWieldableItemLookup = BotaniaFabricCapabilities.getItemApiLookupById(AvatarWieldable.LOOKUP);
		avatarWieldableItemLookup.registerForItems(LandsRodItem.AvatarBehavior::new, BotaniaItems.ROD_OF_THE_LANDS);
		avatarWieldableItemLookup.registerForItems(PlentifulMantleRodItem.AvatarBehavior::new, BotaniaItems.ROD_OF_THE_PLENTIFUL_MANTLE);
		avatarWieldableItemLookup.registerForItems(HellsRodItem.AvatarBehavior::new, BotaniaItems.ROD_OF_THE_HELLS);
		avatarWieldableItemLookup.registerForItems(UnstableReservoirRodItem.AvatarBehavior::new, BotaniaItems.ROD_OF_THE_UNSTABLE_RESERVOIR);
		avatarWieldableItemLookup.registerForItems(BifrostRodItem.AvatarBehavior::new, BotaniaItems.ROD_OF_THE_BIFROST);
		avatarWieldableItemLookup.registerForItems(SkiesRodItem.AvatarBehavior::new, BotaniaItems.ROD_OF_THE_SKIES);

		ItemApiLookup<BlockProvider, Unit> blockProviderItemLookup = BotaniaFabricCapabilities.getItemApiLookupById(BlockProvider.LOOKUP);
		blockProviderItemLookup.registerForItems((stack, c) -> new LandsRodItem.BlockProviderImpl(),
				BotaniaItems.ROD_OF_THE_LANDS, BotaniaItems.ROD_OF_THE_HIGHLANDS, BotaniaItems.ROD_OF_THE_TERRA_FIRMA
		);
		blockProviderItemLookup.registerForItems((stack, c) -> new BlackHoleTalismanItem.BlockProviderImpl(stack), BotaniaItems.BLACK_HOLE_TALISMAN);
		blockProviderItemLookup.registerForItems((stack, c) -> new DepthsRodItem.BlockProviderImpl(), BotaniaItems.ROD_OF_THE_DEPTHS);
		blockProviderItemLookup.registerForItems((stack, c) -> new EnderHandItem.BlockProviderImpl(stack), BotaniaItems.HAND_OF_ENDER);

		ItemApiLookup<CoordBoundItem, Unit> coordBoundItemLookup = BotaniaFabricCapabilities.getItemApiLookupById(CoordBoundItem.LOOKUP);
		coordBoundItemLookup.registerForItems((st, c) -> new EyeOfTheFlugelItem.CoordBoundItemImpl(st), BotaniaItems.EYE_OF_THE_FLUEGEL);
		coordBoundItemLookup.registerForItems((st, c) -> new ManaMirrorItem.CoordBoundItemImpl(st), BotaniaItems.MANA_MIRROR);
		coordBoundItemLookup.registerForItems((st, c) -> new WandOfTheForestItem.CoordBoundItemImpl(st), BotaniaItems.WAND_OF_THE_FOREST);
		coordBoundItemLookup.registerForItems((st, c) -> new WandOfTheForestItem.CoordBoundItemImpl(st), BotaniaItems.WAND_OF_THE_ELVEN_FOREST);

		ItemApiLookup<HourglassMaterial, Unit> hourglassMaterialLookup = BotaniaFabricCapabilities.getItemApiLookupById(HourglassMaterial.LOOKUP);
		hourglassMaterialLookup.registerForItems((st, c) -> HourglassMaterial.SAND, Items.SAND);
		hourglassMaterialLookup.registerForItems((st, c) -> HourglassMaterial.RED_SAND, Items.RED_SAND);
		hourglassMaterialLookup.registerForItems((st, c) -> HourglassMaterial.SOUL_SAND, Items.SOUL_SAND);
		hourglassMaterialLookup.registerForItems((st, c) -> HourglassMaterial.MANA_POWDER, BotaniaItems.MANA_POWDER);

		ItemApiLookup<ManaItem, Unit> manaItemLookup = BotaniaFabricCapabilities.getItemApiLookupById(ManaItem.LOOKUP);
		manaItemLookup.registerForItems((st, c) -> new DefaultManaItemImpl(st),
				BotaniaItems.MANA_MIRROR, BotaniaItems.BAND_OF_MANA, BotaniaItems.GREATER_BAND_OF_MANA, BotaniaItems.MANA_TABLET, BotaniaItems.TERRA_SHATTERER
		);

		ItemApiLookup<Relic, Unit> relicItemLookup = BotaniaFabricCapabilities.getItemApiLookupById(Relic.LOOKUP);
		relicItemLookup.registerForItems((st, c) -> DiceOfFateItem.makeRelic(st), BotaniaItems.DICE_OF_FATE);
		relicItemLookup.registerForItems((st, c) -> EyeOfTheFlugelItem.makeRelic(st), BotaniaItems.EYE_OF_THE_FLUEGEL);
		relicItemLookup.registerForItems((st, c) -> FruitOfGrisaiaItem.makeRelic(st), BotaniaItems.FRUIT_OF_GRISAIA);
		relicItemLookup.registerForItems((st, c) -> KeyOfTheKingsLawItem.makeRelic(st), BotaniaItems.KEY_OF_THE_KINGS_LAW);
		relicItemLookup.registerForItems((st, c) -> RingOfLokiItem.makeRelic(st), BotaniaItems.RING_OF_LOKI);
		relicItemLookup.registerForItems((st, c) -> RingOfOdinItem.makeRelic(st), BotaniaItems.RING_OF_ODIN);
		relicItemLookup.registerForItems((st, c) -> RingOfThorItem.makeRelic(st), BotaniaItems.RING_OF_THOR);

		BlockApiLookup<EdibleBlockWithEffects, Unit> edibleBlockWithEffectLookup = BotaniaFabricCapabilities.getBlockApiLookupById(EdibleBlockWithEffects.LOOKUP);
		// these two blocks implement the capability directly
		edibleBlockWithEffectLookup.registerForBlocks(
				(world, pos, state, blockEntity, context) -> (EdibleBlockWithEffects) state.getBlock(),
				BotaniaBlocks.MUTATED_GRASS_BLOCK, BotaniaBlocks.INFUSED_GRASS_BLOCK
		);

		BlockApiLookup<ExoflameHeatable, Unit> exoflameHeatableBlockLookup = BotaniaFabricCapabilities.getBlockApiLookupById(ExoflameHeatable.LOOKUP);
		exoflameHeatableBlockLookup.registerFallback((world, pos, state, blockEntity, context) -> {
			if (blockEntity instanceof AbstractFurnaceBlockEntity furnace) {
				return new ExoflameFurnaceHandler.FurnaceExoflameHeatable(furnace);
			}
			return null;
		});

		BlockApiLookup<HourglassTrigger, Unit> hourglassTriggerBlockLookup = BotaniaFabricCapabilities.getBlockApiLookupById(HourglassTrigger.LOOKUP);
		hourglassTriggerBlockLookup.registerSelf(BotaniaBlockEntities.ANIMATED_TORCH);

		BlockApiLookup<ManaCollisionGhost, Unit> manaCollisionGhostBlockLookup = BotaniaFabricCapabilities.getBlockApiLookupById(ManaCollisionGhost.LOOKUP);
		manaCollisionGhostBlockLookup.registerForBlocks(
				(level, pos, state, be, context) -> ((ManaCollisionGhost) state.getBlock()),
				BotaniaBlocks.MANA_DETECTOR,
				BotaniaBlocks.ABSTRUSE_PLATFORM, BotaniaBlocks.INFRANGIBLE_PLATFORM, BotaniaBlocks.SPECTRAL_PLATFORM,
				BotaniaBlocks.MANA_PRISM, BotaniaBlocks.TINY_PLANET
		);

		BlockApiLookup<ManaReceiver, Direction> manaReceiverBlockLookup = BotaniaFabricCapabilities.getBlockApiLookupById(ManaReceiver.LOOKUP);
		manaReceiverBlockLookup.registerSelf(
				BlockEntityConstants.SELF_MANA_RECEIVER_BES.toArray(BlockEntityType[]::new)
		);
		manaReceiverBlockLookup.registerForBlocks(
				(level, pos, state, be, side) -> new ManaVoidBlock.ManaReceiverImpl(level, pos, state),
				BotaniaBlocks.MANA_VOID
		);

		BlockApiLookup<SparkAttachable, Unit> sparkAttachableBlockLookup = BotaniaFabricCapabilities.getBlockApiLookupById(SparkAttachable.LOOKUP);
		sparkAttachableBlockLookup.registerSelf(BlockEntityConstants.SELF_SPARK_ATTACHABLE_BES.toArray(BlockEntityType[]::new));

		BlockApiLookup<ManaTrigger, Unit> manaTriggerBlockLookup = BotaniaFabricCapabilities.getBlockApiLookupById(ManaTrigger.LOOKUP);
		manaTriggerBlockLookup.registerSelf(
				BlockEntityConstants.SELF_MANA_TRIGGER_BES.toArray(BlockEntityType[]::new));
		manaTriggerBlockLookup.registerForBlocks(
				(level, pos, state, be, context) -> new DrumBlock.ManaTriggerImpl(level, pos, state),
				BotaniaBlocks.DRUM_OF_THE_CANOPY, BotaniaBlocks.DRUM_OF_THE_GATHERING, BotaniaBlocks.DRUM_OF_THE_WILD
		);
		manaTriggerBlockLookup.registerForBlocks(
				(level, pos, state, be, context) -> new ManastormChargeBlock.ManaTriggerImpl(level, pos),
				BotaniaBlocks.MANASTORM_CHARGE
		);
		manaTriggerBlockLookup.registerForBlocks(
				(level, pos, state, be, context) -> new ManaDetectorBlock.ManaTriggerImpl(level, pos, state),
				BotaniaBlocks.MANA_DETECTOR
		);

		BlockApiLookup<Wandable, Direction> wandableBlockLookup = BotaniaFabricCapabilities.getBlockApiLookupById(Wandable.LOOKUP);
		wandableBlockLookup.registerSelf(
				BlockEntityConstants.SELF_WANDABLE_BES.toArray(BlockEntityType[]::new));
		wandableBlockLookup.registerForBlocks(ForceRelayBlock::createWandable,
				BotaniaBlocks.FORCE_RELAY
		);
		wandableBlockLookup.registerForBlocks(ManaEnchanterBlockEntity::createLapisBlockWandable,
				Blocks.LAPIS_BLOCK);

		BlockApiLookup<WandBindable, Direction> wandBindableBlockLookup = BotaniaFabricCapabilities.getBlockApiLookupById(WandBindable.LOOKUP);
		wandBindableBlockLookup.registerSelf(
				BlockEntityConstants.SELF_WAND_BINDABLE_BES.toArray(BlockEntityType[]::new));
		wandBindableBlockLookup.registerForBlocks(ForceRelayBlock::createWandBindable,
				BotaniaBlocks.FORCE_RELAY
		);

		BlockApiLookup<PhantomInkableBlock, Unit> phantomInkableBlockLookup = BotaniaFabricCapabilities.getBlockApiLookupById(PhantomInkableBlock.LOOKUP);
		phantomInkableBlockLookup.registerSelf(
				BlockEntityConstants.SELF_PHANTOM_INKABLE_BES.toArray(BlockEntityType[]::new));

		ItemStorage.SIDED.registerForBlockEntity(FabricRedStringContainerBlockEntity::getStorage, BotaniaBlockEntities.RED_STRINGED_CONTAINER);
		ItemStorage.SIDED.registerForBlockEntity(RedStringContainerStorage::new, BotaniaBlockEntities.RED_STRINGED_DISPENSER);

		if (XplatAbstractions.INSTANCE.isModLoaded("team_reborn_energy")) {
			FluxfieldTRStorage.register();
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

	private void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext ctx, Commands.CommandSelection environment) {
		if (XplatAbstractions.INSTANCE.gogLoaded()) {
			SkyblockCommand.register(dispatcher);
		}
	}

	private void serverStopping(MinecraftServer server) {
		CorporeaIndexBlockEntity.clearIndexCache();
	}

}
