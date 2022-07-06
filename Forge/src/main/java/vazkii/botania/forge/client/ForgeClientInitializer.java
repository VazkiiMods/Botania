package vazkii.botania.forge.client;

import com.google.common.base.Suppliers;
import com.mojang.blaze3d.vertex.BufferBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.model.ForgeModelBakery;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.BotaniaForgeClientCapabilities;
import vazkii.botania.api.block.IWandHUD;
import vazkii.botania.api.mana.ManaBarTooltip;
import vazkii.botania.client.BotaniaItemProperties;
import vazkii.botania.client.core.handler.*;
import vazkii.botania.client.core.helper.CoreShaders;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.core.proxy.ClientProxy;
import vazkii.botania.client.fx.ModParticles;
import vazkii.botania.client.gui.HUDHandler;
import vazkii.botania.client.gui.ManaBarTooltipComponent;
import vazkii.botania.client.gui.TooltipHandler;
import vazkii.botania.client.gui.bag.GuiFlowerBag;
import vazkii.botania.client.gui.box.GuiBaubleBox;
import vazkii.botania.client.integration.ears.EarsIntegration;
import vazkii.botania.client.model.ModLayerDefinitions;
import vazkii.botania.client.render.BlockRenderLayers;
import vazkii.botania.client.render.ColorHandler;
import vazkii.botania.client.render.entity.EntityRenderers;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.block.tile.ModTiles;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.bauble.ItemDodgeRing;
import vazkii.botania.forge.CapabilityUtil;
import vazkii.botania.forge.mixin.client.ForgeAccessorModelBakery;
import vazkii.botania.mixin.client.AccessorRenderBuffers;
import vazkii.botania.xplat.IClientXplatAbstractions;
import vazkii.botania.xplat.IXplatAbstractions;
import vazkii.patchouli.api.BookDrawScreenEvent;

import java.io.IOException;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.function.Function;
import java.util.function.Supplier;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

@Mod.EventBusSubscriber(modid = BotaniaAPI.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ForgeClientInitializer {
	@SubscribeEvent
	public static void clientInit(FMLClientSetupEvent evt) {
		// GUIs
		evt.enqueueWork(() -> {
			MenuScreens.register(ModItems.FLOWER_BAG_CONTAINER, GuiFlowerBag::new);
			MenuScreens.register(ModItems.BAUBLE_BOX_CONTAINER, GuiBaubleBox::new);
		});

		// Events
		var bus = MinecraftForge.EVENT_BUS;
		bus.addListener((BookDrawScreenEvent e) -> KonamiHandler.renderBook(e.getBook(), e.getScreen(), e.getMouseX(), e.getMouseY(), e.getPartialTicks(), e.getPoseStack()));
		bus.addListener((TickEvent.ClientTickEvent e) -> {
			if (e.phase == TickEvent.Phase.END) {
				ClientTickHandler.clientTickEnd(Minecraft.getInstance());
				KonamiHandler.clientTick(Minecraft.getInstance());
			}
		});
		bus.addListener((RenderGameOverlayEvent.Post e) -> {
			if (e.getType() == RenderGameOverlayEvent.ElementType.ALL) {
				HUDHandler.onDrawScreenPost(e.getMatrixStack(), e.getPartialTicks());
			}
		});
		bus.addListener((ItemTooltipEvent e) -> TooltipHandler.onTooltipEvent(e.getItemStack(), e.getFlags(), e.getToolTip()));
		bus.addListener((ScreenEvent.KeyboardKeyPressedEvent.Post e) -> CorporeaInputHandler.buttonPressed(e.getKeyCode(), e.getScanCode()));

		// Forge bus events done with Mixins on Fabric
		bus.addListener((RenderGameOverlayEvent.BossInfo e) -> {
			var result = BossBarHandler.onBarRender(e.getMatrixStack(), e.getX(), e.getY(),
					e.getBossEvent(), true);
			result.ifPresent(increment -> {
				e.setCanceled(true);
				e.setIncrement(increment);
			});
		});
		bus.addListener((RenderGameOverlayEvent.Text e) -> DebugHandler.onDrawDebugText(e.getLeft()));
		bus.addListener((InputEvent.KeyInputEvent e) -> {
			ItemDodgeRing.ClientLogic.onKeyDown();
			KonamiHandler.handleInput(e.getKey(), e.getAction(), e.getModifiers());
		});
		bus.addListener((TickEvent.RenderTickEvent e) -> {
			if (e.phase == TickEvent.Phase.START) {
				ClientTickHandler.renderTick(e.renderTickTime);
			}
		});
		bus.addListener(EventPriority.LOWEST, (RenderTooltipEvent.Color e) -> {
			var manaItem = IXplatAbstractions.INSTANCE.findManaItem(e.getItemStack());
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
		MinecraftForgeClient.registerTooltipComponentFactory(ManaBarTooltip.class, ManaBarTooltipComponent::new);
		ClientProxy.initSeasonal();
		ClientProxy.initKeybindings(ClientRegistry::registerKeyBinding);
		MinecraftForge.EVENT_BUS.addGenericListener(BlockEntity.class, ForgeClientInitializer::attachBeCapabilities);

		if (IXplatAbstractions.INSTANCE.isModLoaded("ears")) {
			EarsIntegration.register();
		}
	}

	private static final Supplier<Map<BlockEntityType<?>, Function<BlockEntity, IWandHUD>>> WAND_HUD = Suppliers.memoize(() -> {
		var ret = new IdentityHashMap<BlockEntityType<?>, Function<BlockEntity, IWandHUD>>();
		ModTiles.registerWandHudCaps((factory, types) -> {
			for (var type : types) {
				ret.put(type, factory);
			}
		});
		ModSubtiles.registerWandHudCaps((factory, types) -> {
			for (var type : types) {
				ret.put(type, factory);
			}
		});
		return Collections.unmodifiableMap(ret);
	});

	private static void attachBeCapabilities(AttachCapabilitiesEvent<BlockEntity> e) {
		var be = e.getObject();

		var makeWandHud = WAND_HUD.get().get(be.getType());
		if (makeWandHud != null) {
			e.addCapability(prefix("wand_hud"),
					CapabilityUtil.makeProvider(BotaniaForgeClientCapabilities.WAND_HUD, makeWandHud.apply(be)));
		}
	}

	@SubscribeEvent
	public static void loadComplete(FMLLoadCompleteEvent evt) {
		// Needed to prevent mana pools on carts from X-raying through the cart
		SortedMap<RenderType, BufferBuilder> layers = ((AccessorRenderBuffers) Minecraft.getInstance()
				.renderBuffers()).getEntityBuilders();
		layers.put(RenderHelper.MANA_POOL_WATER, new BufferBuilder(RenderHelper.MANA_POOL_WATER.bufferSize()));
	}

	@SubscribeEvent
	public static void onModelRegister(ModelRegistryEvent evt) {
		ModelLoaderRegistry.registerLoader(IClientXplatAbstractions.FLOATING_FLOWER_MODEL_LOADER_ID,
				ForgeFloatingFlowerModel.Loader.INSTANCE);
		var resourceManager = ((ForgeAccessorModelBakery) (Object) ForgeModelBakery.instance()).getResourceManager();
		MiscellaneousModels.INSTANCE.onModelRegister(resourceManager, ForgeModelBakery::addSpecialModel);
		BlockRenderLayers.init(ItemBlockRenderTypes::setRenderLayer);
		BotaniaItemProperties.init((item, id, prop) -> ItemProperties.register(item.asItem(), id, prop));
	}

	@SubscribeEvent
	public static void registerEntityLayers(EntityRenderersEvent.RegisterLayerDefinitions evt) {
		ModLayerDefinitions.init(evt::registerLayerDefinition);
	}

	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers evt) {
		EntityRenderers.registerBlockEntityRenderers(evt::registerBlockEntityRenderer);
		EntityRenderers.registerEntityRenderers(evt::registerEntityRenderer);
	}

	@SubscribeEvent
	public static void registerParticleFactories(ParticleFactoryRegisterEvent evt) {
		ModParticles.FactoryHandler.registerFactories(new ModParticles.FactoryHandler.Consumer() {
			@Override
			public <T extends ParticleOptions> void register(ParticleType<T> type, Function<SpriteSet, ParticleProvider<T>> constructor) {
				Minecraft.getInstance().particleEngine.register(type, constructor::apply);
			}
		});
	}

	@SubscribeEvent
	public static void registerBlockColors(ColorHandlerEvent.Block evt) {
		ColorHandler.submitBlocks(evt.getBlockColors()::register);
	}

	@SubscribeEvent
	public static void registerItemColors(ColorHandlerEvent.Item evt) {
		ColorHandler.submitItems(evt.getItemColors()::register);
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
	public static void registerShaders(RegisterShadersEvent evt) throws IOException {
		CoreShaders.init(evt.getResourceManager(), p -> evt.registerShader(p.getFirst(), p.getSecond()));
	}

	@SubscribeEvent
	public static void onModelBake(ModelBakeEvent evt) {
		MiscellaneousModels.INSTANCE.onModelBake(evt.getModelLoader(), evt.getModelRegistry());
	}

}
