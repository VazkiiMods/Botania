/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.core.proxy;

import net.minecraft.block.FlowerBlock;
import net.minecraft.block.TallFlowerBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeBuffers;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.IParticleData;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import org.lwjgl.glfw.GLFW;

import vazkii.botania.client.core.handler.*;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.client.fx.FXLightning;
import vazkii.botania.client.fx.ModParticles;
import vazkii.botania.client.render.world.SkyblockRenderEvents;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModFluffBlocks;
import vazkii.botania.common.block.decor.BlockFloatingFlower;
import vazkii.botania.common.block.decor.BlockModMushroom;
import vazkii.botania.common.block.subtile.functional.BergamuteEventHandler;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.core.proxy.IProxy;
import vazkii.botania.common.entity.EntityDoppleganger;
import vazkii.botania.common.item.ItemSextant;
import vazkii.botania.common.item.equipment.bauble.ItemDodgeRing;
import vazkii.botania.common.item.equipment.bauble.ItemMonocle;
import vazkii.botania.common.lib.LibMisc;
import vazkii.patchouli.api.IMultiblock;
import vazkii.patchouli.api.PatchouliAPI;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Map;
import java.util.SortedMap;

public class ClientProxy implements IProxy {

	public static boolean jingleTheBells = false;
	public static boolean dootDoot = false;

	public static KeyBinding CORPOREA_REQUEST;

	@Override
	public void registerHandlers() {
		// This is the only place it works, but mods are constructed in parallel (brilliant idea) so this
		// *could* end up blowing up if it races with someone else. Let's pray that doesn't happen.
		ShaderHelper.initShaders();

		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
		modBus.addListener(this::clientSetup);
		modBus.addListener(this::loadComplete);
		modBus.addListener(MiscellaneousIcons.INSTANCE::onTextureStitchPre);
		modBus.addListener(MiscellaneousIcons.INSTANCE::onTextureStitchPost);
		modBus.addListener(MiscellaneousIcons.INSTANCE::onModelRegister);
		modBus.addListener(MiscellaneousIcons.INSTANCE::onModelBake);
		modBus.addListener(SplashHandler::registerFactories);
		modBus.addListener(ModelHandler::registerModels);
		modBus.addListener(ModParticles.FactoryHandler::registerFactories);

		IEventBus forgeBus = MinecraftForge.EVENT_BUS;
		forgeBus.addListener(EventPriority.HIGHEST, TooltipHandler::onTooltipEvent);
		forgeBus.addListener(TooltipAdditionDisplayHandler::onToolTipRender);
		forgeBus.addListener(RenderLexicon::renderHand);
		forgeBus.addListener(LightningHandler::onRenderWorldLast);
		forgeBus.addListener(KonamiHandler::clientTick);
		forgeBus.addListener(KonamiHandler::handleInput);
		forgeBus.addListener(KonamiHandler::renderBook);
		forgeBus.addListener(HUDHandler::onDrawScreenPost);
		forgeBus.addListener(DebugHandler::onDrawDebugText);
		forgeBus.addListener(CorporeaInputHandler::buttonPressed);
		forgeBus.addListener(ClientTickHandler::clientTickEnd);
		forgeBus.addListener(ClientTickHandler::renderTick);
		forgeBus.addListener(BoundTileRenderer::onWorldRenderLast);
		forgeBus.addListener(BossBarHandler::onBarRender);
		forgeBus.addListener(BlockHighlightRenderHandler::onWorldRenderLast);
		forgeBus.addListener(AstrolabePreviewHandler::onWorldRenderLast);
		forgeBus.addListener(SkyblockRenderEvents::onRender);
		forgeBus.addListener(ItemDodgeRing::onKeyDown);
		forgeBus.addListener(EventPriority.LOWEST, BergamuteEventHandler::onSoundEvent);
	}

	private void clientSetup(FMLClientSetupEvent event) {
		PersistentVariableHelper.setCacheFile(new File(Minecraft.getInstance().gameDir, "BotaniaVars.dat"));
		try {
			PersistentVariableHelper.load();
			PersistentVariableHelper.save();
		} catch (IOException e) {
			Botania.LOGGER.fatal("Persistent Variables couldn't load!!");
		}

		if (ConfigHandler.CLIENT.enableSeasonalFeatures.get()) {
			LocalDateTime now = LocalDateTime.now();
			if (now.getMonth() == Month.DECEMBER && now.getDayOfMonth() >= 16 || now.getMonth() == Month.JANUARY && now.getDayOfMonth() <= 2) {
				jingleTheBells = true;
			}
			if (now.getMonth() == Month.OCTOBER) {
				dootDoot = true;
			}
		}

		DeferredWorkQueue.runLater(() -> {
			CORPOREA_REQUEST = new KeyBinding("key.botania_corporea_request", KeyConflictContext.GUI, InputMappings.getInputByCode(GLFW.GLFW_KEY_C, 0), LibMisc.MOD_NAME);
			ClientRegistry.registerKeyBinding(ClientProxy.CORPOREA_REQUEST);
		});

		registerRenderTypes();
	}

	private static void registerRenderTypes() {
		RenderTypeLookup.setRenderLayer(ModBlocks.defaultAltar, RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.forestAltar, RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.plainsAltar, RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.mountainAltar, RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.fungalAltar, RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.swampAltar, RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.desertAltar, RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.taigaAltar, RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.mesaAltar, RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.mossyAltar, RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.ghostRail, RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(ModBlocks.solidVines, RenderType.getCutout());

		RenderTypeLookup.setRenderLayer(ModBlocks.manaGlass, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(ModFluffBlocks.managlassPane, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(ModBlocks.elfGlass, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(ModFluffBlocks.alfglassPane, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(ModBlocks.bifrost, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(ModFluffBlocks.bifrostPane, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(ModBlocks.bifrostPerm, RenderType.getTranslucent());
		RenderTypeLookup.setRenderLayer(ModBlocks.prism, RenderType.getTranslucent());

		RenderTypeLookup.setRenderLayer(ModBlocks.starfield, RenderType.getCutoutMipped());
		RenderTypeLookup.setRenderLayer(ModBlocks.abstrusePlatform, t -> true);
		RenderTypeLookup.setRenderLayer(ModBlocks.infrangiblePlatform, t -> true);
		RenderTypeLookup.setRenderLayer(ModBlocks.spectralPlatform, t -> true);

		Registry.BLOCK.stream().filter(b -> b.getRegistryName().getNamespace().equals(LibMisc.MOD_ID))
				.forEach(b -> {
					if (b instanceof BlockFloatingFlower || b instanceof FlowerBlock
							|| b instanceof TallFlowerBlock || b instanceof BlockModMushroom) {
						RenderTypeLookup.setRenderLayer(b, RenderType.getCutout());
					}
				});
	}

	private void loadComplete(FMLLoadCompleteEvent event) {
		DeferredWorkQueue.runLater(() -> {
			initAuxiliaryRender();
			ColorHandler.init();

			// Needed to prevent mana pools on carts from X-raying through the cart
			SortedMap<RenderType, BufferBuilder> layers = ObfuscationReflectionHelper.getPrivateValue(RenderTypeBuffers.class, Minecraft.getInstance().getRenderTypeBuffers(), "field_228480_b_");
			layers.put(RenderHelper.MANA_POOL_WATER, new BufferBuilder(RenderHelper.MANA_POOL_WATER.getBufferSize()));
		});
	}

	private void initAuxiliaryRender() {
		Map<String, PlayerRenderer> skinMap = Minecraft.getInstance().getRenderManager().getSkinMap();
		PlayerRenderer render;
		render = skinMap.get("default");
		render.addLayer(new ContributorFancinessHandler(render));
		if (Botania.curiosLoaded) {
			render.addLayer(new BaubleRenderHandler(render));
		}
		render.addLayer(new LayerTerraHelmet(render));

		render = skinMap.get("slim");
		render.addLayer(new ContributorFancinessHandler(render));
		if (Botania.curiosLoaded) {
			render.addLayer(new BaubleRenderHandler(render));
		}
		render.addLayer(new LayerTerraHelmet(render));
	}

	@Override
	public boolean isTheClientPlayer(LivingEntity entity) {
		return entity == Minecraft.getInstance().player;
	}

	@Override
	public PlayerEntity getClientPlayer() {
		return Minecraft.getInstance().player;
	}

	@Override
	public boolean isClientPlayerWearingMonocle() {
		return ItemMonocle.hasMonocle(Minecraft.getInstance().player);
	}

	@Override
	public long getWorldElapsedTicks() {
		return ClientTickHandler.ticksInGame;
	}

	@Override
	public void lightningFX(Vector3 vectorStart, Vector3 vectorEnd, float ticksPerMeter, long seed, int colorOuter, int colorInner) {
		Minecraft.getInstance().particles.addEffect(new FXLightning(Minecraft.getInstance().world, vectorStart, vectorEnd, ticksPerMeter, seed, colorOuter, colorInner));
	}

	@Override
	public void addBoss(EntityDoppleganger boss) {
		BossBarHandler.bosses.add(boss);
	}

	@Override
	public void removeBoss(EntityDoppleganger boss) {
		BossBarHandler.bosses.remove(boss);
	}

	@Override
	public int getClientRenderDistance() {
		return Minecraft.getInstance().gameSettings.renderDistanceChunks;
	}

	@Override
	public void addParticleForce(World world, IParticleData particleData, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
		world.addParticle(particleData, true, x, y, z, xSpeed, ySpeed, zSpeed);
	}

	@Override
	public void showMultiblock(IMultiblock mb, ITextComponent name, BlockPos anchor, Rotation rot) {
		PatchouliAPI.instance.showMultiblock(mb, name, anchor, rot);
	}

	@Override
	public void clearSextantMultiblock() {
		IMultiblock mb = PatchouliAPI.instance.getCurrentMultiblock();
		if (mb != null && mb.getID().equals(ItemSextant.MULTIBLOCK_ID)) {
			PatchouliAPI.instance.clearMultiblock();
		}
	}
}
