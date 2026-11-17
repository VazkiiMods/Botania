/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package org.violetmoon.botania.neoforge.client;

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
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import org.violetmoon.botania.api.BotaniaAPI;
import org.violetmoon.botania.api.BotaniaAPIClient;
import org.violetmoon.botania.api.mana.ManaBarTooltip;
import org.violetmoon.botania.api.mana.ManaItem;
import org.violetmoon.botania.api.neoforge.BotaniaNeoForgeCapabilities;
import org.violetmoon.botania.client.BotaniaClientCapabilities;
import org.violetmoon.botania.client.BotaniaItemProperties;
import org.violetmoon.botania.client.core.handler.*;
import org.violetmoon.botania.client.core.helper.CoreShaders;
import org.violetmoon.botania.client.core.proxy.ClientProxy;
import org.violetmoon.botania.client.fx.BotaniaParticles;
import org.violetmoon.botania.client.gui.HUDHandler;
import org.violetmoon.botania.client.gui.ManaBarTooltipComponent;
import org.violetmoon.botania.client.gui.TooltipHandler;
import org.violetmoon.botania.client.gui.bag.ColoredContentsPouchScreen;
import org.violetmoon.botania.client.gui.box.TrinketCaseScreen;
import org.violetmoon.botania.client.gui.enderhand.HandOfEnderScreen;
import org.violetmoon.botania.client.integration.ears.EarsIntegration;
import org.violetmoon.botania.client.model.BotaniaLayerDefinitions;
import org.violetmoon.botania.client.model.armor.ArmorModels;
import org.violetmoon.botania.client.render.BlockRenderLayers;
import org.violetmoon.botania.client.render.ColorHandler;
import org.violetmoon.botania.client.render.entity.EntityRenderers;
import org.violetmoon.botania.common.block.BotaniaBlocks;
import org.violetmoon.botania.common.block.block_entity.corporea.CorporeaIndexBlockEntity;
import org.violetmoon.botania.common.handler.BotaniaRecipeIngredientsCache;
import org.violetmoon.botania.common.item.BotaniaItems;
import org.violetmoon.botania.common.item.equipment.armor.manasteel.ManasteelArmorItem;
import org.violetmoon.botania.common.item.equipment.bauble.RingOfDexterousMotionItem;
import org.violetmoon.botania.xplat.ClientXplatAbstractions;
import org.violetmoon.botania.xplat.XplatAbstractions;

import vazkii.patchouli.api.BookDrawScreenEvent;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.violetmoon.botania.api.BotaniaAPI.botaniaRL;

@EventBusSubscriber(modid = BotaniaAPI.MODID, value = Dist.CLIENT)
public class NeoForgeClientInitializer {
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

		BlockRenderLayers.skipPlatformBlocks = true; // platforms can use standard rendering on NeoForge
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

		// NeoForge bus events done with Mixins on Fabric
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
			// NeoForge does not pass the tooltip width to any tooltip event.
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

		if (XplatAbstractions.INSTANCE.isModLoaded("ears")) {
			EarsIntegration.register();
		}
	}

	@SubscribeEvent
	private static void registerMenuScreens(RegisterMenuScreensEvent e) {
		e.register(BotaniaItems.COLORED_CONTENTS_POUCH_MENU_TYPE, ColoredContentsPouchScreen::new);
		e.register(BotaniaItems.TRINKET_CASE_MENU_TYPE, TrinketCaseScreen::new);
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

	@SubscribeEvent(priority = EventPriority.HIGH)
	private static void createClientCapabilities(RegisterCapabilitiesEvent e) {
		BotaniaClientCapabilities.registerCapabilityTypes(BotaniaNeoForgeCapabilities.getRegistration());
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	private static void attachClientFallbackCapabilities(RegisterCapabilitiesEvent e) {
		BotaniaClientCapabilities.registerCapabilityFallbackProviders(BotaniaNeoForgeCapabilities.getProviderRegistration(e));
	}

	@SubscribeEvent
	private static void attachClientCapabilities(RegisterCapabilitiesEvent e) {
		BotaniaClientCapabilities.registerCapabilityProviders(BotaniaNeoForgeCapabilities.getProviderRegistration(e));
	}

	@SubscribeEvent
	public static void registerModelLoader(ModelEvent.RegisterGeometryLoaders evt) {
		evt.register(ClientXplatAbstractions.FLOATING_FLOWER_MODEL_LOADER_ID,
				NeoForgeFloatingFlowerModel.Loader.INSTANCE);
		evt.register(ClientXplatAbstractions.MANA_GUN_MODEL_LOADER_ID,
				NeoForgeManaBlasterModel.Loader.INSTANCE);
	}

	@SubscribeEvent
	public static void onModelRegister(ModelEvent.RegisterAdditional evt) {
		var resourceManager = Minecraft.getInstance().getResourceManager();
		MiscellaneousModels.INSTANCE.onModelRegister(resourceManager,
				id -> evt.register(ModelResourceLocation.standalone(id)));
		BotaniaItemProperties.init(ItemProperties::register);
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
				NeoForgeBlockEntityItemRendererHelper.PROPS,
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
