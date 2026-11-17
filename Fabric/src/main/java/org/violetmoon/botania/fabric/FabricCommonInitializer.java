/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.fabric;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.datafixers.util.Function3;

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
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.item.v1.EnchantmentEvents;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.api.networking.v1.EntityTrackingEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.MinecartComparatorLogicRegistry;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.fabricmc.fabric.api.registry.FlattenableBlockRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
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
import net.minecraft.core.BlockPos;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.material.Fluids;

import org.jetbrains.annotations.Nullable;

import org.violetmoon.botania.api.BotaniaAPI;
import org.violetmoon.botania.api.BotaniaRegistries;
import org.violetmoon.botania.api.block.IslandType;
import org.violetmoon.botania.api.brew.Brew;
import org.violetmoon.botania.api.corporea.CorporeaHelper;
import org.violetmoon.botania.api.fabric.BotaniaFabricCapabilities;
import org.violetmoon.botania.api.fabric.mana.ManaNetworkCallback;
import org.violetmoon.botania.api.internal.ItemSource;
import org.violetmoon.botania.api.mana.ManaItemHandler;
import org.violetmoon.botania.api.recipe.StateIngredientType;
import org.violetmoon.botania.client.fx.BotaniaParticles;
import org.violetmoon.botania.common.BotaniaCapabilities;
import org.violetmoon.botania.common.BotaniaStats;
import org.violetmoon.botania.common.advancements.*;
import org.violetmoon.botania.common.block.*;
import org.violetmoon.botania.common.block.block_entity.*;
import org.violetmoon.botania.common.block.block_entity.BotaniaBlockEntities;
import org.violetmoon.botania.common.block.block_entity.corporea.CorporeaIndexBlockEntity;
import org.violetmoon.botania.common.block.block_entity.flower.BotaniaIslandTypes;
import org.violetmoon.botania.common.block.block_entity.flower.functional.TigerseyeBlockEntity;
import org.violetmoon.botania.common.block.red_string.RedStringInterceptorBlock;
import org.violetmoon.botania.common.brew.BotaniaBrews;
import org.violetmoon.botania.common.brew.BotaniaMobEffects;
import org.violetmoon.botania.common.command.SkyblockCommand;
import org.violetmoon.botania.common.component.BotaniaDataComponents;
import org.violetmoon.botania.common.config.ConfigDataManagerImpl;
import org.violetmoon.botania.common.crafting.BotaniaRecipeTypes;
import org.violetmoon.botania.common.crafting.StateIngredients;
import org.violetmoon.botania.common.entity.BotaniaEntities;
import org.violetmoon.botania.common.entity.GaiaGuardianEntity;
import org.violetmoon.botania.common.handler.*;
import org.violetmoon.botania.common.helper.PlayerHelper;
import org.violetmoon.botania.common.impl.BotaniaAPIImpl;
import org.violetmoon.botania.common.impl.corporea.DefaultCorporeaMatchers;
import org.violetmoon.botania.common.integration.corporea.CorporeaNodeDetectors;
import org.violetmoon.botania.common.internal_caps.BotaniaDataAttachments;
import org.violetmoon.botania.common.internal_caps.ItemSources;
import org.violetmoon.botania.common.item.*;
import org.violetmoon.botania.common.item.equipment.bauble.FlugelTiaraItem;
import org.violetmoon.botania.common.item.equipment.tool.terrasteel.TerraBladeItem;
import org.violetmoon.botania.common.item.equipment.tool.terrasteel.TerraTruncatorItem;
import org.violetmoon.botania.common.item.relic.*;
import org.violetmoon.botania.common.item.rod.*;
import org.violetmoon.botania.common.lib.BotaniaTags;
import org.violetmoon.botania.common.loot.BotaniaLootModifiers;
import org.violetmoon.botania.common.world.BotaniaFeatures;
import org.violetmoon.botania.common.world.SkyblockChunkGenerator;
import org.violetmoon.botania.common.world.SkyblockWorldEvents;
import org.violetmoon.botania.fabric.block_entity.FabricRedStringContainerBlockEntity;
import org.violetmoon.botania.fabric.integration.corporea.FabricTransferCorporeaNodeDetector;
import org.violetmoon.botania.fabric.integration.tr_energy.FluxfieldTRStorage;
import org.violetmoon.botania.fabric.internal_caps.FabricInternalEntityAttachments;
import org.violetmoon.botania.fabric.internal_caps.RedStringContainerStorage;
import org.violetmoon.botania.fabric.loot.LootHandler;
import org.violetmoon.botania.fabric.network.FabricPacketHandler;
import org.violetmoon.botania.network.clientbound.ItemLifeTimePacket;
import org.violetmoon.botania.xplat.XplatAbstractions;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import static org.violetmoon.botania.api.BotaniaAPI.botaniaRL;

public class FabricCommonInitializer implements ModInitializer {
	private static final Registry<Brew> BREW_REGISTRY = FabricRegistryBuilder
			.createDefaulted(BotaniaRegistries.BREWS, Brew.DEFAULT_ID).buildAndRegister();
	private static final Registry<IslandType> ISLAND_TYPE_REGISTRY = FabricRegistryBuilder
			.createDefaulted(BotaniaRegistries.ISLAND_TYPES, IslandType.DEFAULT_ID).buildAndRegister();
	private static final MappedRegistry<StateIngredientType<?>> STATE_INGREDIENT_TYPE_REGISTRY = FabricRegistryBuilder
			.createDefaulted(BotaniaRegistries.STATE_INGREDIENT_TYPE, StateIngredientType.DEFAULT_ID).buildAndRegister();
	private static final MappedRegistry<ItemSource> ITEM_SOURCE_REGISTRY = FabricRegistryBuilder
			.createSimple(BotaniaRegistries.ITEM_SOURCE).buildAndRegister();

	public FabricCommonInitializer() {
		// This happens before all mod initializations.
		// These don't involve "real" registries, so doing this here should hopefully be fine.
		BotaniaCapabilities.registerCapabilityTypes(BotaniaFabricCapabilities.getLookupRegistration());
		BotaniaDataAttachments.registerDataAttachments(FabricInternalEntityAttachments::register);
	}

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
		BotaniaBlocks.addAxeStripping(this::registerAxeStripping);
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
		int wallTime = 300;
		FuelRegistry.INSTANCE.add(BotaniaTags.Items.WOODEN_WALLS, wallTime);

		// GUI and Recipe
		BotaniaItems.registerMenuTypes(bind(BuiltInRegistries.MENU));
		StateIngredients.submitRegistrations(bind(STATE_INGREDIENT_TYPE_REGISTRY));
		BotaniaRecipeTypes.submitRecipeTypes(bind(BuiltInRegistries.RECIPE_TYPE));
		BotaniaRecipeTypes.submitRecipeSerializers(bind(BuiltInRegistries.RECIPE_SERIALIZER));

		// Entities
		BotaniaEntities.registerEntities(bind(BuiltInRegistries.ENTITY_TYPE));
		PixieHandler.registerAttribute(BuiltInRegistries.ATTRIBUTE);
		BotaniaEntities.registerAttributes(FabricDefaultAttributeRegistry::register);
		MinecartComparatorLogicRegistry.register(BotaniaEntities.MANA_POOL_MINECART, (minecart, state, pos) -> minecart.getComparatorLevel());
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

	private void registerAxeStripping(Block input, Block output) {
		// not sure why Fabric restricts it to blocks with the axis property, but we have to support non-log blocks
		if (input.getStateDefinition().getProperties().contains(BlockStateProperties.AXIS)
				&& output.getStateDefinition().getProperties().contains(BlockStateProperties.AXIS)) {
			StrippableBlockRegistry.register(input, output);
		} else {
			AxeStrippingData.addCustomStrippable(input, output);
		}
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
		BotaniaCapabilities.registerCapabilityProviders(BotaniaFabricCapabilities.getProviderRegistration());
		BotaniaCapabilities.registerCapabilityFallbackProviders(BotaniaFabricCapabilities.getProviderRegistration());

		// Fabric-specific implementations
		FluidStorage.ITEM.registerForItems((stack, context) -> new FullItemFluidStorage(context, Items.BOWL, FluidVariant.of(Fluids.WATER), FluidConstants.BLOCK),
				BotaniaItems.WATER_BOWL
		);
		FluidStorage.ITEM.registerForItems((itemStack, context) -> (InsertionOnlyStorage<FluidVariant>) (resource, maxAmount, transaction) -> Math.min(FluidConstants.BLOCK, maxAmount),
				BotaniaItems.EXTRAPOLATED_BUCKET
		);

		// TODO: NeoForge uses the same capability for both, check if these can be combined
		ItemStorage.SIDED.registerForBlockEntity(FabricRedStringContainerBlockEntity::getStorage,
				BotaniaBlockEntities.RED_STRINGED_CONTAINER);
		ItemStorage.SIDED.registerForBlockEntity(RedStringContainerStorage::new,
				BotaniaBlockEntities.RED_STRINGED_DISPENSER);

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

	private static <A, C> BlockApiLookup.BlockApiProvider<A, C> blockApi(BiFunction<Level, BlockPos, A> factory) {
		return (world, pos, state, blockEntity, context) -> factory.apply(world, pos);
	}

	private static <A, C> BlockApiLookup.BlockApiProvider<A, C> blockApi(Function3<Level, BlockPos, BlockState, A> factory) {
		return (world, pos, state, blockEntity, context) -> factory.apply(world, pos, state);
	}

	private static <T extends BlockEntity, A, C> BiFunction<? super T, C, @Nullable A> blockApi(Function<T, A> factory) {
		return (blockEntity, context) -> factory.apply(blockEntity);
	}
}
