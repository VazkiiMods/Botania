package vazkii.botania.forge.client;

import com.mojang.blaze3d.vertex.BufferBuilder;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.model.ForgeModelBakery;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.mana.ManaBarTooltip;
import vazkii.botania.client.core.handler.*;
import vazkii.botania.client.core.helper.CoreShaders;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.core.proxy.ClientProxy;
import vazkii.botania.client.fx.ModParticles;
import vazkii.botania.client.gui.ManaBarTooltipComponent;
import vazkii.botania.client.gui.bag.GuiFlowerBag;
import vazkii.botania.client.gui.box.GuiBaubleBox;
import vazkii.botania.client.model.ModLayerDefinitions;
import vazkii.botania.client.render.BlockRenderLayers;
import vazkii.botania.client.render.entity.EntityRenderers;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.bauble.ItemDodgeRing;
import vazkii.botania.forge.mixin.client.ForgeAccessorModelBakery;
import vazkii.botania.mixin.client.AccessorRenderBuffers;
import vazkii.botania.xplat.IClientXplatAbstractions;
import vazkii.patchouli.api.BookDrawScreenEvent;

import java.io.IOException;
import java.util.SortedMap;
import java.util.function.Function;

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
		bus.addListener((RenderGameOverlayEvent e) -> HUDHandler.onDrawScreenPost(e.getMatrixStack(), e.getPartialTicks()));
		bus.addListener((ItemTooltipEvent e) -> TooltipHandler.onTooltipEvent(e.getItemStack(), e.getFlags(), e.getToolTip()));
		bus.addListener((ScreenEvent.KeyboardKeyPressedEvent e) -> CorporeaInputHandler.buttonPressed(e.getKeyCode(), e.getScanCode()));

		// Forge bus events done with Mixins on Fabric
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

		// Etc
		MinecraftForgeClient.registerTooltipComponentFactory(ManaBarTooltip.class, ManaBarTooltipComponent::new);
		ClientProxy.initSeasonal();
		ClientProxy.initKeybindings(ClientRegistry::registerKeyBinding);
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
		MiscellaneousIcons.INSTANCE.onModelRegister(ForgeModelBakery::addSpecialModel);
		var resourceManager = ((ForgeAccessorModelBakery) (Object) ForgeModelBakery.instance()).getResourceManager();
		ModelHandler.registerModels(resourceManager, ForgeModelBakery::addSpecialModel);
		BlockRenderLayers.init(ItemBlockRenderTypes::setRenderLayer);
		// todo 1.18-forge there's a  crash and idk why
		// BotaniaItemProperties.init((item, id, prop) -> ItemProperties.register(item.asItem(), id, prop));
	}

	@SubscribeEvent
	public static void registerEntityLayers(EntityRenderersEvent.RegisterLayerDefinitions evt) {
		ModLayerDefinitions.init(evt::registerLayerDefinition);
	}

	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers evt) {
		ModelHandler.registerRenderers(evt::registerBlockEntityRenderer);
		EntityRenderers.init(evt::registerEntityRenderer);
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
		MiscellaneousIcons.INSTANCE.onModelBake(evt.getModelLoader(), evt.getModelRegistry());
	}

}
