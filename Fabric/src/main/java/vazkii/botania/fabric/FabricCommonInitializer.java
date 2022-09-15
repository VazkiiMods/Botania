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
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.networking.v1.EntityTrackingEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.MinecartComparatorLogicRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.FullItemFluidStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.material.Fluids;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.BotaniaFabricCapabilities;
import vazkii.botania.api.block.HornHarvestable;
import vazkii.botania.api.mana.ManaCollisionGhost;
import vazkii.botania.api.mana.ManaNetworkCallback;
import vazkii.botania.client.fx.BotaniaParticles;
import vazkii.botania.common.ModStats;
import vazkii.botania.common.advancements.*;
import vazkii.botania.common.block.*;
import vazkii.botania.common.block.block_entity.*;
import vazkii.botania.common.block.block_entity.BlockEntityConstants;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntities;
import vazkii.botania.common.block.block_entity.corporea.CorporeaIndexBlockEntity;
import vazkii.botania.common.block.flower.functional.DaffomillBlockEntity;
import vazkii.botania.common.block.flower.functional.TigerseyeBlockEntity;
import vazkii.botania.common.block.mana.DrumBlock;
import vazkii.botania.common.block.mana.ManaDetectorBlock;
import vazkii.botania.common.block.mana.ManaVoidBlock;
import vazkii.botania.common.block.red_string.RedStringInterceptorBlock;
import vazkii.botania.common.brew.BotaniaBrews;
import vazkii.botania.common.brew.BotaniaMobEffects;
import vazkii.botania.common.command.SkyblockCommand;
import vazkii.botania.common.crafting.BotaniaRecipeTypes;
import vazkii.botania.common.entity.EntityDoppleganger;
import vazkii.botania.common.entity.ModEntities;
import vazkii.botania.common.handler.*;
import vazkii.botania.common.impl.BotaniaAPIImpl;
import vazkii.botania.common.impl.DefaultHornHarvestable;
import vazkii.botania.common.impl.corporea.DefaultCorporeaMatchers;
import vazkii.botania.common.item.*;
import vazkii.botania.common.item.equipment.bauble.ItemFlightTiara;
import vazkii.botania.common.item.equipment.bauble.ItemGreaterManaRing;
import vazkii.botania.common.item.equipment.bauble.ItemManaRing;
import vazkii.botania.common.item.equipment.tool.terrasteel.ItemTerraAxe;
import vazkii.botania.common.item.equipment.tool.terrasteel.ItemTerraPick;
import vazkii.botania.common.item.equipment.tool.terrasteel.ItemTerraSword;
import vazkii.botania.common.item.material.ItemEnderAir;
import vazkii.botania.common.item.relic.*;
import vazkii.botania.common.item.rod.*;
import vazkii.botania.common.lib.ModTags;
import vazkii.botania.common.loot.LootHandler;
import vazkii.botania.common.loot.ModLootModifiers;
import vazkii.botania.common.world.ModFeatures;
import vazkii.botania.common.world.SkyblockChunkGenerator;
import vazkii.botania.common.world.SkyblockWorldEvents;
import vazkii.botania.fabric.block_entity.FabricRedStringContainerBlockEntity;
import vazkii.botania.fabric.integration.tr_energy.FluxfieldTRStorage;
import vazkii.botania.fabric.internal_caps.RedStringContainerStorage;
import vazkii.botania.fabric.network.FabricPacketHandler;
import vazkii.botania.xplat.IXplatAbstractions;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class FabricCommonInitializer implements ModInitializer {
	@Override
	public void onInitialize() {
		coreInit();
		registryInit();

		PaintableData.init();
		DefaultCorporeaMatchers.init();

		PatchouliAPI.get().registerMultiblock(Registry.BLOCK.getKey(BotaniaBlocks.alfPortal), AlfheimPortalBlockEntity.MULTIBLOCK.get());
		PatchouliAPI.get().registerMultiblock(Registry.BLOCK.getKey(BotaniaBlocks.terraPlate), TerrestrialAgglomerationPlateBlockEntity.MULTIBLOCK.get());
		PatchouliAPI.get().registerMultiblock(Registry.BLOCK.getKey(BotaniaBlocks.enchanter), ManaEnchanterBlockEntity.MULTIBLOCK.get());
		PatchouliAPI.get().registerMultiblock(prefix("gaia_ritual"), EntityDoppleganger.ARENA_MULTIBLOCK.get());

		OrechidManager.registerListener();
		CraftyCrateBlockEntity.registerListener();

		registerCapabilities();
		registerEvents();
	}

	private void coreInit() {
		FiberBotaniaConfig.setup();
		EquipmentHandler.init();
		FabricPacketHandler.init();
	}

	private void registryInit() {
		// Core item/block/BE
		ModSounds.init(bind(Registry.SOUND_EVENT));
		BotaniaBlocks.registerBlocks(bind(Registry.BLOCK));
		BotaniaBlocks.registerItemBlocks(bind(Registry.ITEM));
		BotaniaFluffBlocks.registerBlocks(bind(Registry.BLOCK));
		BotaniaFluffBlocks.registerItemBlocks(bind(Registry.ITEM));
		BotaniaBlockEntities.registerTiles(bind(Registry.BLOCK_ENTITY_TYPE));
		ModItems.registerItems(bind(Registry.ITEM));
		BotaniaFlowerBlocks.registerBlocks(bind(Registry.BLOCK));
		BotaniaFlowerBlocks.registerItemBlocks(bind(Registry.ITEM));
		BotaniaFlowerBlocks.registerTEs(bind(Registry.BLOCK_ENTITY_TYPE));
		BotaniaBlocks.addDispenserBehaviours();
		BotaniaBlocks.addAxeStripping();

		int blazeTime = 2400;
		FuelRegistry.INSTANCE.add(BotaniaBlocks.blazeBlock.asItem(), blazeTime * (IXplatAbstractions.INSTANCE.gogLoaded() ? 5 : 10));

		// GUI and Recipe
		ModItems.registerMenuTypes(bind(Registry.MENU));
		ModItems.registerRecipeSerializers(bind(Registry.RECIPE_SERIALIZER));
		BotaniaBannerPatterns.submitRegistrations(bind(Registry.BANNER_PATTERN));
		BotaniaRecipeTypes.submitRecipeTypes(bind(Registry.RECIPE_TYPE));
		BotaniaRecipeTypes.submitRecipeSerializers(bind(Registry.RECIPE_SERIALIZER));

		// Entities
		ModEntities.registerEntities(bind(Registry.ENTITY_TYPE));
		ModEntities.registerAttributes(FabricDefaultAttributeRegistry::register);
		PixieHandler.registerAttribute(bind(Registry.ATTRIBUTE));
		MinecartComparatorLogicRegistry.register(ModEntities.POOL_MINECART, (minecart, state, pos) -> minecart.getComparatorLevel());

		// Potions
		BotaniaMobEffects.registerPotions(bind(Registry.MOB_EFFECT));
		BotaniaBrews.registerBrews();

		// Worldgen
		ModFeatures.registerFeatures(bind(Registry.FEATURE));
		SkyblockChunkGenerator.submitRegistration(bind(Registry.CHUNK_GENERATOR));
		BiomeModifications.addFeature(
				BiomeSelectors.tag(ModTags.Biomes.MYSTICAL_FLOWER_SPAWNLIST)
						.and(Predicate.not(BiomeSelectors.tag(ModTags.Biomes.MYSTICAL_FLOWER_BLOCKLIST))),
				GenerationStep.Decoration.VEGETAL_DECORATION,
				ModFeatures.MYSTICAL_FLOWERS_ID);
		BiomeModifications.addFeature(
				BiomeSelectors.tag(ModTags.Biomes.MYSTICAL_MUSHROOM_SPAWNLIST)
						.and(Predicate.not(BiomeSelectors.tag(ModTags.Biomes.MYSTICAL_MUSHROOM_BLOCKLIST))),
				GenerationStep.Decoration.VEGETAL_DECORATION,
				ModFeatures.MYSTICAL_MUSHROOMS_ID);

		// Rest
		BotaniaCriteriaTriggers.init();
		BotaniaParticles.registerParticles(bind(Registry.PARTICLE_TYPE));
		ModLootModifiers.submitLootConditions(bind(Registry.LOOT_CONDITION_TYPE));
		ModLootModifiers.submitLootFunctions(bind(Registry.LOOT_FUNCTION_TYPE));
		ModStats.init();
	}

	private void registerEvents() {
		if (IXplatAbstractions.INSTANCE.gogLoaded()) {
			UseBlockCallback.EVENT.register(SkyblockWorldEvents::onPlayerInteract);
		}
		AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) -> ((ItemExchangeRod) ModItems.exchangeRod).onLeftClick(player, world, hand, pos, direction));
		AttackEntityCallback.EVENT.register(ItemGravityRod::onAttack);
		AttackEntityCallback.EVENT.register(ItemTerraSword::attackEntity);
		CommandRegistrationCallback.EVENT.register(this::registerCommands);
		EntitySleepEvents.ALLOW_SLEEPING.register(SleepingHandler::trySleep);
		EntityTrackingEvents.START_TRACKING.register(DaffomillBlockEntity::onItemTrack);
		LootTableEvents.MODIFY.register((resourceManager, manager, id, tableBuilder, lootTableSource) -> LootHandler.lootLoad(id, tableBuilder::withPool));
		ManaNetworkCallback.EVENT.register(ManaNetworkHandler.instance::onNetworkEvent);
		ServerEntityEvents.ENTITY_LOAD.register(TigerseyeBlockEntity::pacifyAfterLoad);
		ServerLifecycleEvents.SERVER_STARTED.register(this::serverAboutToStart);
		ServerLifecycleEvents.SERVER_STOPPING.register(this::serverStopping);
		ServerPlayConnectionEvents.DISCONNECT.register(((handler, server) -> ItemFlightTiara.playerLoggedOut(handler.player)));
		ServerPlayerEvents.AFTER_RESPAWN.register(ItemKeepIvy::onPlayerRespawn);
		ServerTickEvents.END_WORLD_TICK.register(CommonTickHandler::onTick);
		ServerTickEvents.END_WORLD_TICK.register(ItemGrassSeeds::onTickEnd);
		ServerTickEvents.END_WORLD_TICK.register(ItemTerraAxe::onTickEnd);
		UseBlockCallback.EVENT.register(RedStringInterceptorBlock::onInteract);
		UseBlockCallback.EVENT.register(ItemLokiRing::onPlayerInteract);
		UseItemCallback.EVENT.register(ItemEnderAir::onPlayerInteract);
	}

	private static <T> BiConsumer<T, ResourceLocation> bind(Registry<? super T> registry) {
		return (t, id) -> Registry.register(registry, id, t);
	}

	private void registerCapabilities() {
		FluidStorage.ITEM.registerForItems((stack, context) -> new FullItemFluidStorage(context, Items.BOWL,
				FluidVariant.of(Fluids.WATER), FluidConstants.BLOCK),
				ModItems.waterBowl);

		BotaniaFabricCapabilities.AVATAR_WIELDABLE.registerForItems((stack, c) -> new ItemDirtRod.AvatarBehavior(), ModItems.dirtRod);
		BotaniaFabricCapabilities.AVATAR_WIELDABLE.registerForItems((stack, c) -> new ItemDiviningRod.AvatarBehavior(), ModItems.diviningRod);
		BotaniaFabricCapabilities.AVATAR_WIELDABLE.registerForItems((stack, c) -> new ItemFireRod.AvatarBehavior(), ModItems.fireRod);
		BotaniaFabricCapabilities.AVATAR_WIELDABLE.registerForItems((stack, c) -> new ItemMissileRod.AvatarBehavior(), ModItems.missileRod);
		BotaniaFabricCapabilities.AVATAR_WIELDABLE.registerForItems((stack, c) -> new ItemRainbowRod.AvatarBehavior(), ModItems.rainbowRod);
		BotaniaFabricCapabilities.AVATAR_WIELDABLE.registerForItems((stack, c) -> new ItemTornadoRod.AvatarBehavior(), ModItems.tornadoRod);
		BotaniaFabricCapabilities.BLOCK_PROVIDER.registerForItems((stack, c) -> new ItemDirtRod.BlockProviderImpl(stack), ModItems.dirtRod, ModItems.skyDirtRod);
		BotaniaFabricCapabilities.BLOCK_PROVIDER.registerForItems((stack, c) -> new ItemBlackHoleTalisman.BlockProviderImpl(stack), ModItems.blackHoleTalisman);
		BotaniaFabricCapabilities.BLOCK_PROVIDER.registerForItems((stack, c) -> new ItemCobbleRod.BlockProviderImpl(), ModItems.cobbleRod);
		BotaniaFabricCapabilities.BLOCK_PROVIDER.registerForItems((stack, c) -> new ItemEnderHand.BlockProviderImpl(stack), ModItems.enderHand);
		BotaniaFabricCapabilities.BLOCK_PROVIDER.registerForItems((stack, c) -> new ItemTerraformRod.BlockProviderImpl(), ModItems.terraformRod);
		BotaniaFabricCapabilities.COORD_BOUND_ITEM.registerForItems((st, c) -> new ItemFlugelEye.CoordBoundItemImpl(st), ModItems.flugelEye);
		BotaniaFabricCapabilities.COORD_BOUND_ITEM.registerForItems((st, c) -> new ItemManaMirror.CoordBoundItemImpl(st), ModItems.manaMirror);
		BotaniaFabricCapabilities.COORD_BOUND_ITEM.registerForItems((st, c) -> new ItemTwigWand.CoordBoundItemImpl(st), ModItems.twigWand);
		BotaniaFabricCapabilities.COORD_BOUND_ITEM.registerForItems((st, c) -> new ItemTwigWand.CoordBoundItemImpl(st), ModItems.dreamwoodWand);
		BotaniaFabricCapabilities.MANA_ITEM.registerForItems((st, c) -> new ItemManaMirror.ManaItemImpl(st), ModItems.manaMirror);
		BotaniaFabricCapabilities.MANA_ITEM.registerForItems((st, c) -> new ItemManaRing.ManaItemImpl(st), ModItems.manaRing);
		BotaniaFabricCapabilities.MANA_ITEM.registerForItems((st, c) -> new ItemGreaterManaRing.GreaterManaItemImpl(st), ModItems.manaRingGreater);
		BotaniaFabricCapabilities.MANA_ITEM.registerForItems((st, c) -> new ItemManaTablet.ManaItemImpl(st), ModItems.manaTablet);
		BotaniaFabricCapabilities.MANA_ITEM.registerForItems((st, c) -> new ItemTerraPick.ManaItemImpl(st), ModItems.terraPick);
		BotaniaFabricCapabilities.RELIC.registerForItems((st, c) -> ItemDice.makeRelic(st), ModItems.dice);
		BotaniaFabricCapabilities.RELIC.registerForItems((st, c) -> ItemFlugelEye.makeRelic(st), ModItems.flugelEye);
		BotaniaFabricCapabilities.RELIC.registerForItems((st, c) -> ItemInfiniteFruit.makeRelic(st), ModItems.infiniteFruit);
		BotaniaFabricCapabilities.RELIC.registerForItems((st, c) -> ItemKingKey.makeRelic(st), ModItems.kingKey);
		BotaniaFabricCapabilities.RELIC.registerForItems((st, c) -> ItemLokiRing.makeRelic(st), ModItems.lokiRing);
		BotaniaFabricCapabilities.RELIC.registerForItems((st, c) -> ItemOdinRing.makeRelic(st), ModItems.odinRing);
		BotaniaFabricCapabilities.RELIC.registerForItems((st, c) -> ItemThorRing.makeRelic(st), ModItems.thorRing);

		BotaniaFabricCapabilities.EXOFLAME_HEATABLE.registerFallback((world, pos, state, blockEntity, context) -> {
			if (blockEntity instanceof AbstractFurnaceBlockEntity furnace) {
				return new ExoflameFurnaceHandler.FurnaceExoflameHeatable(furnace);
			}
			return null;
		});
		BotaniaFabricCapabilities.HORN_HARVEST.registerForBlocks((w, p, s, be, c) -> (world, pos, stack, hornType, living) -> hornType == HornHarvestable.EnumHornType.CANOPY,
				Blocks.VINE, Blocks.CAVE_VINES, Blocks.CAVE_VINES_PLANT, Blocks.TWISTING_VINES,
				Blocks.TWISTING_VINES_PLANT, Blocks.WEEPING_VINES, Blocks.WEEPING_VINES_PLANT);
		BotaniaFabricCapabilities.HORN_HARVEST.registerForBlocks((w, p, s, be, c) -> DefaultHornHarvestable.INSTANCE,
				Arrays.stream(DyeColor.values()).map(BotaniaBlocks::getMushroom).toArray(Block[]::new));
		BotaniaFabricCapabilities.HORN_HARVEST.registerForBlocks((w, p, s, be, c) -> DefaultHornHarvestable.INSTANCE,
				Arrays.stream(DyeColor.values()).map(BotaniaBlocks::getShinyFlower).toArray(Block[]::new));
		BotaniaFabricCapabilities.HOURGLASS_TRIGGER.registerForBlockEntities((be, c) -> {
			var torch = (AnimatedTorchBlockEntity) be;
			return hourglass -> torch.toggle();
		}, BotaniaBlockEntities.ANIMATED_TORCH);
		BotaniaFabricCapabilities.MANA_GHOST.registerForBlocks(
				(level, pos, state, be, context) -> ((ManaCollisionGhost) state.getBlock()),
				BotaniaBlocks.manaDetector,
				BotaniaBlocks.abstrusePlatform, BotaniaBlocks.infrangiblePlatform, BotaniaBlocks.spectralPlatform,
				BotaniaBlocks.prism, BotaniaBlocks.tinyPlanet
		);
		BotaniaFabricCapabilities.MANA_RECEIVER.registerSelf(
				BlockEntityConstants.SELF_MANA_RECEIVER_BES.toArray(BlockEntityType[]::new)
		);
		BotaniaFabricCapabilities.MANA_RECEIVER.registerForBlocks(
				(level, pos, state, be, side) -> new ManaVoidBlock.ManaReceiverImpl(level, pos, state),
				BotaniaBlocks.manaVoid);
		BotaniaFabricCapabilities.SPARK_ATTACHABLE.registerSelf(BlockEntityConstants.SELF_SPARK_ATTACHABLE_BES.toArray(BlockEntityType[]::new));
		BotaniaFabricCapabilities.MANA_TRIGGER.registerForBlocks(
				(level, pos, state, be, context) -> new DrumBlock.ManaTriggerImpl(level, pos, state),
				BotaniaBlocks.canopyDrum, BotaniaBlocks.gatheringDrum, BotaniaBlocks.wildDrum
		);
		BotaniaFabricCapabilities.MANA_TRIGGER.registerForBlocks(
				(level, pos, state, be, context) -> new ManastormChargeBlock.ManaTriggerImpl(level, pos, state),
				BotaniaBlocks.manaBomb
		);
		BotaniaFabricCapabilities.MANA_TRIGGER.registerForBlocks(
				(level, pos, state, be, context) -> new ManaDetectorBlock.ManaTriggerImpl(level, pos, state),
				BotaniaBlocks.manaDetector
		);
		BotaniaFabricCapabilities.MANA_TRIGGER.registerSelf(
				BlockEntityConstants.SELF_MANA_TRIGGER_BES.toArray(BlockEntityType[]::new));
		BotaniaFabricCapabilities.WANDABLE.registerForBlocks(
				(world, pos, state, blockEntity, context) -> (player, stack, side) -> ((ForceRelayBlock) state.getBlock()).onUsedByWand(player, stack, world, pos),
				BotaniaBlocks.pistonRelay
		);
		BotaniaFabricCapabilities.WANDABLE.registerSelf(
				BlockEntityConstants.SELF_WANDADBLE_BES.toArray(BlockEntityType[]::new));
		ItemStorage.SIDED.registerForBlockEntity(FabricRedStringContainerBlockEntity::getStorage, BotaniaBlockEntities.RED_STRING_CONTAINER);
		ItemStorage.SIDED.registerForBlockEntity(RedStringContainerStorage::new, BotaniaBlockEntities.RED_STRING_DISPENSER);

		if (IXplatAbstractions.INSTANCE.isModLoaded("team_reborn_energy")) {
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
		if (IXplatAbstractions.INSTANCE.gogLoaded()) {
			SkyblockCommand.register(dispatcher);
		}
	}

	private void serverStopping(MinecraftServer server) {
		ManaNetworkHandler.instance.clear();
		CorporeaIndexBlockEntity.clearIndexCache();
	}

}
