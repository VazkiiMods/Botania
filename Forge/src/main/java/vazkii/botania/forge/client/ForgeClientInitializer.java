package vazkii.botania.forge.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.model.ForgeModelBakery;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.client.BotaniaItemProperties;
import vazkii.botania.client.core.handler.ColorHandler;
import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.client.core.handler.ModelHandler;
import vazkii.botania.client.core.helper.CoreShaders;
import vazkii.botania.client.core.proxy.ClientProxy;
import vazkii.botania.client.fx.ModParticles;
import vazkii.botania.client.gui.bag.GuiFlowerBag;
import vazkii.botania.client.gui.box.GuiBaubleBox;
import vazkii.botania.client.model.ModLayerDefinitions;
import vazkii.botania.client.render.BlockRenderLayers;
import vazkii.botania.client.render.entity.EntityRenderers;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.forge.mixin.client.AccessorModelBakery;
import vazkii.botania.xplat.IClientXplatAbstractions;

import java.io.IOException;
import java.util.function.Function;

@Mod.EventBusSubscriber(modid = BotaniaAPI.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ForgeClientInitializer {
	@SubscribeEvent
	public static void clientEtcInit(FMLClientSetupEvent evt) {
		evt.enqueueWork(() -> {
			MenuScreens.register(ModItems.FLOWER_BAG_CONTAINER, GuiFlowerBag::new);
			MenuScreens.register(ModItems.BAUBLE_BOX_CONTAINER, GuiBaubleBox::new);
		});
		ClientProxy.initSeasonal();
		ClientProxy.initKeybindings(ClientRegistry::registerKeyBinding);
	}

	@SubscribeEvent
	public static void onModelRegister(ModelRegistryEvent evt) {
		ModelLoaderRegistry.registerLoader(IClientXplatAbstractions.FLOATING_FLOWER_MODEL_LOADER_ID,
				ForgeFloatingFlowerModel.Loader.INSTANCE);
		MiscellaneousIcons.INSTANCE.onModelRegister(ForgeModelBakery::addSpecialModel);
		var resourceManager = ((AccessorModelBakery) (Object) ForgeModelBakery.instance()).getResourceManager();
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
		// todo 1.18-forge teisrs need to be done in a mixin
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
