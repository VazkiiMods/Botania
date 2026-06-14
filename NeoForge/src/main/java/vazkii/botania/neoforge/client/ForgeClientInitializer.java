/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.neoforge.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.BotaniaAPIClient;
import vazkii.botania.api.BotaniaForgeCapabilities;
import vazkii.botania.api.block.MonocleHud;
import vazkii.botania.api.block.WandHUD;
import vazkii.botania.api.mana.ManaBarTooltip;
import vazkii.botania.api.mana.ManaItem;
import vazkii.botania.client.BotaniaClientCapabilities;
import vazkii.botania.client.BotaniaItemProperties;
import vazkii.botania.client.core.handler.*;
import vazkii.botania.client.core.helper.CoreShaders;
import vazkii.botania.client.core.proxy.ClientProxy;
import vazkii.botania.client.fx.BotaniaParticles;
import vazkii.botania.client.gui.HUDHandler;
import vazkii.botania.client.gui.ManaBarTooltipComponent;
import vazkii.botania.client.gui.TooltipHandler;
import vazkii.botania.client.gui.bag.ColoredContentsPouchScreen;
import vazkii.botania.client.gui.box.BaubleBoxGui;
import vazkii.botania.client.gui.enderhand.HandOfEnderScreen;
import vazkii.botania.client.gui.monocle.MonocleHUDs;
import vazkii.botania.client.integration.ears.EarsIntegration;
import vazkii.botania.client.model.BotaniaLayerDefinitions;
import vazkii.botania.client.model.armor.ArmorModels;
import vazkii.botania.client.render.BlockRenderLayers;
import vazkii.botania.client.render.ColorHandler;
import vazkii.botania.client.render.entity.EntityRenderers;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.block_entity.BotaniaBlockEntities;
import vazkii.botania.common.block.block_entity.corporea.CorporeaIndexBlockEntity;
import vazkii.botania.common.entity.BotaniaEntities;
import vazkii.botania.common.handler.BotaniaRecipeIngredientsCache;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.item.equipment.armor.manasteel.ManasteelArmorItem;
import vazkii.botania.common.item.equipment.bauble.RingOfDexterousMotionItem;
import vazkii.botania.xplat.ClientXplatAbstractions;
import vazkii.botania.xplat.XplatAbstractions;
import vazkii.patchouli.api.BookDrawScreenEvent;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

@EventBusSubscriber(modid = BotaniaAPI.MODID, value = Dist.CLIENT)
public class ForgeClientInitializer {
	@SubscribeEvent
	public static void registerGuiOverlays(RegisterGuiLayersEvent e) {
		e.registerAbove(VanillaGuiLayers.EXPERIENCE_BAR, botaniaRL("hud_early"), HUDHandler::onDrawExperienceBarPost);
		e.registerAbove(VanillaGuiLayers.AIR_LEVEL, botaniaRL("hud_late"), HUDHandler::onDrawAirLevelPost);
	}

	@SubscribeEvent
	public static void clientInit(FMLClientSetupEvent evt) {
		// ensure API implementations are loaded
		BotaniaAPI.LOGGER.debug("Client API instances: {}",
				List.of(BotaniaAPIClient.instance(), ClientXplatAbstractions.instance()));

		BlockRenderLayers.skipPlatformBlocks = true; // platforms can use standard rendering on Forge
		BlockRenderLayers.init(ItemBlockRenderTypes::setRenderLayer);

		// Events
		var bus = NeoForge.EVENT_BUS;
		// GUIs
		bus.addListener((BookDrawScreenEvent e) -> KonamiHandler.renderBook(e.getBook(), e.getScreen(), e.getMouseX(), e.getMouseY(), e.getPartialTicks(), e.getGraphics()));
		bus.addListener((ClientTickEvent.Post e) -> {
			ClientTickHandler.clientTickEnd(Minecraft.getInstance());
			KonamiHandler.clientTick(Minecraft.getInstance());
		});
		bus.addListener((ItemTooltipEvent e) -> TooltipHandler.onTooltipEvent(e.getItemStack(), e.getContext(), e.getFlags(), e.getToolTip()));
		bus.addListener((ScreenEvent.KeyPressed.Post e) -> CorporeaInputHandler.buttonPressed(e.getKeyCode(), e.getScanCode()));

		// Forge bus events done with Mixins on Fabric
		bus.addListener(EventPriority.HIGH, (ClientChatEvent e) -> {
			var player = Minecraft.getInstance().player;
			if (player != null && CorporeaIndexBlockEntity.ClientHandler.onChat(player, e.getMessage())) {
				e.setCanceled(true);
			}
		});
		bus.addListener((RecipesUpdatedEvent e) -> BotaniaRecipeIngredientsCache.clearClientCache());
		bus.addListener((CustomizeGuiOverlayEvent.BossEventProgress e) -> {
			var result = BossBarHandler.onBarRender(e.getGuiGraphics(), e.getX(), e.getY(),
					e.getBossEvent(), true);
			result.ifPresent(increment -> {
				e.setCanceled(true);
				e.setIncrement(increment);
			});
		});
		bus.addListener((CustomizeGuiOverlayEvent.DebugText e) -> DebugHandler.onDrawDebugText(e.getLeft()));
		bus.addListener((InputEvent.Key e) -> {
			RingOfDexterousMotionItem.ClientLogic.onKeyDown();
			KonamiHandler.handleInput(e.getKey(), e.getAction(), e.getModifiers());
		});
		bus.addListener(EventPriority.LOWEST, (RenderTooltipEvent.Color e) -> {
			var manaItem = ManaItem.LOOKUP.find(e.getItemStack());
			if (manaItem == null) {
				return;
			}
			// Forge does not pass the tooltip width to any tooltip event.
			// To avoid a mixin here, we just duplicate the width checking part.
			int width = 0;
			ManaBarTooltipComponent manaBar = null;
			for (ClientTooltipComponent component : e.getComponents()) {
				width = Math.max(width, component.getWidth(e.getFont()));
				if (component instanceof ManaBarTooltipComponent c) {
					manaBar = c;
				}
			}
			if (manaBar != null) {
				manaBar.setContext(e.getX(), e.getY(), width);
			}
		});

		// Etc
		ClientProxy.initSeasonal();

		if (XplatAbstractions.INSTANCE.isModLoaded("ears")) {
			EarsIntegration.register();
		}
	}

	@SubscribeEvent
	private static void registerMenuScreens(RegisterMenuScreensEvent e) {
		e.register(BotaniaItems.COLORED_CONTENTS_POUCH_CONTAINER, ColoredContentsPouchScreen::new);
		e.register(BotaniaItems.BAUBLE_BOX_CONTAINER, BaubleBoxGui::new);
		e.register(BotaniaItems.HAND_OF_ENDER_MENU_TYPE, HandOfEnderScreen::new);
	}

	@SubscribeEvent
	public static void registerTooltipComponent(RegisterClientTooltipComponentFactoriesEvent e) {
		e.register(ManaBarTooltip.class, ManaBarTooltipComponent::new);
	}

	@SubscribeEvent
	public static void registerKeys(RegisterKeyMappingsEvent e) {
		ClientProxy.initKeybindings(e::register);
	}

	@SubscribeEvent
	private static void attachClientCapabilities(RegisterCapabilitiesEvent e) {
		BotaniaClientCapabilities.registerClientCapabilities(BotaniaForgeCapabilities.getRegistration());

		BlockCapability<WandHUD, Void> wandHudBlockCap =
				BotaniaForgeCapabilities.getBlockApiLookupById(WandHUD.BLOCK_LOOKUP);
		BotaniaBlockEntities.registerWandHudCaps((factory, types) -> Stream.of(types).forEach(
				blockEntityType -> e.registerBlockEntity(wandHudBlockCap,
						blockEntityType, (blockEntity, context) -> factory.apply(blockEntity)
				)
		));

		EntityCapability<WandHUD, Void> wandHudEntityCap =
				BotaniaForgeCapabilities.getEntityApiLookupById(WandHUD.ENTITY_LOOKUP);
		BotaniaEntities.registerWandHudCaps(getECapConsumer(e, wandHudEntityCap));
		EntityCapability<MonocleHud, Void> monocleHudEntityCap =
				BotaniaForgeCapabilities.getEntityApiLookupById(MonocleHud.ENTITY_LOOKUP);
		MonocleHUDs.registerMonocleHudEntityCaps(getECapConsumer(e, monocleHudEntityCap), true);

		BlockCapability<MonocleHud, Void> monocleHudBlockCap =
				BotaniaForgeCapabilities.getBlockApiLookupById(MonocleHud.BLOCK_LOOKUP);
		MonocleHUDs.registerMonocleHudBlockCaps(getBCapsConsumer(e, monocleHudBlockCap),
				b -> !e.isBlockRegistered(monocleHudBlockCap, b));
	}

	private static <T, C> BotaniaBlocks.BCapConsumer<T> getBCapsConsumer(RegisterCapabilitiesEvent e,
			BlockCapability<T, C> capability) {
		return (factory, blocks) -> e.registerBlock(
				capability,
				(level, pos, state, blockEntity, context) -> factory.apply(state),
				blocks
		);
	}

	private static <C> BotaniaEntities.ECapConsumer<C> getECapConsumer(RegisterCapabilitiesEvent e,
			EntityCapability<C, Void> capability) {
		return (factory, types) -> {
			for (EntityType<?> entityType : types) {
				e.registerEntity(capability, entityType, (entity, context) -> factory.apply(entity));
			}
		};
	}

	@SubscribeEvent
	public static void registerModelLoader(ModelEvent.RegisterGeometryLoaders evt) {
		evt.register(ClientXplatAbstractions.FLOATING_FLOWER_MODEL_LOADER_ID,
				ForgeFloatingFlowerModel.Loader.INSTANCE);
		evt.register(ClientXplatAbstractions.MANA_GUN_MODEL_LOADER_ID,
				ForgeManaBlasterModel.Loader.INSTANCE);
	}

	@SubscribeEvent
	public static void onModelRegister(ModelEvent.RegisterAdditional evt) {
		var resourceManager = Minecraft.getInstance().getResourceManager();
		MiscellaneousModels.INSTANCE.onModelRegister(resourceManager,
				id -> evt.register(ModelResourceLocation.standalone(id)));
		BotaniaItemProperties.init((item, id, prop) -> ItemProperties.register(item.asItem(), id, prop));
	}

	@SubscribeEvent
	public static void registerEntityLayers(EntityRenderersEvent.RegisterLayerDefinitions evt) {
		BotaniaLayerDefinitions.init(evt::registerLayerDefinition);
	}

	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers evt) {
		EntityRenderers.registerBlockEntityRenderers(evt::registerBlockEntityRenderer);
		EntityRenderers.registerEntityRenderers(evt::registerEntityRenderer);
	}

	@SubscribeEvent
	public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
		List<Item> armorItems = new ArrayList<>();
		for (var entry : BuiltInRegistries.ITEM.entrySet()) {
			Item item = entry.getValue();
			ResourceLocation id = entry.getKey().location();
			if (item instanceof ManasteelArmorItem armor && id.getNamespace().equals(BotaniaAPI.MODID)) {
				armorItems.add(armor);
			}
		}

		event.registerItem(
				new IClientItemExtensions() {
					@Override
					public HumanoidModel<?> getHumanoidArmorModel(LivingEntity living, ItemStack stack, EquipmentSlot slot, HumanoidModel<?> defaultModel) {
						return Objects.requireNonNull(ArmorModels.get(stack));
					}
				},
				armorItems.toArray(Item[]::new)
		);

		event.registerItem(
				ForgeBlockEntityItemRendererHelper.PROPS,
				Stream.of(
						BotaniaBlocks.BOTANICAL_BREWERY, BotaniaBlocks.MANA_PYLON, BotaniaBlocks.NATURA_PYLON, BotaniaBlocks.GAIA_PYLON,
						BotaniaBlocks.MANATIDE_BELLOWS, BotaniaBlocks.CORPOREA_INDEX, BotaniaBlocks.HOVERING_HOURGLASS,
						BotaniaBlocks.TERU_TERU_BOZU, BotaniaBlocks.LIVINGWOOD_AVATAR
				).map(Block::asItem).toArray(Item[]::new)
		);
	}

	@SubscribeEvent
	public static void registerParticleFactories(RegisterParticleProvidersEvent evt) {
		BotaniaParticles.FactoryHandler.registerFactories(new BotaniaParticles.FactoryHandler.Consumer() {
			@Override
			public <T extends ParticleOptions> void register(ParticleType<T> type, Function<SpriteSet, ParticleProvider<T>> constructor) {
				evt.registerSpriteSet(type, constructor::apply);
			}
		});
	}

	@SubscribeEvent
	public static void registerBlockColors(RegisterColorHandlersEvent.Block evt) {
		ColorHandler.submitBlocks(evt::register);
	}

	@SubscribeEvent
	public static void registerItemColors(RegisterColorHandlersEvent.Item evt) {
		ColorHandler.submitItems(evt::register);
	}

	@SubscribeEvent
	public static void initAuxiliaryRender(EntityRenderersEvent.AddLayers evt) {
		for (var playerModelType : evt.getSkins()) {
			if (evt.getSkin(playerModelType) instanceof PlayerRenderer renderer) {
				EntityRenderers.addAuxiliaryPlayerRenders(renderer, renderer::addLayer);
			}
		}
	}

	@SubscribeEvent
	public static void registerShaders(RegisterShadersEvent evt) {
		CoreShaders.init((id, vertexFormat, onLoaded) -> {
			try {
				evt.registerShader(
						new ShaderInstance(evt.getResourceProvider(), id, vertexFormat),
						onLoaded
				);
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		});
	}

	@SubscribeEvent
	public static void onModelBake(ModelEvent.ModifyBakingResult evt) {
		MiscellaneousModels.INSTANCE.onModelBake(evt.getModelBakery(), evt.getModels());
	}
}
