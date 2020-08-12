/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.core.proxy;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.FlowerBlock;
import net.minecraft.block.TallFlowerBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.ModelPredicateProvider;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.text.Text;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import org.lwjgl.glfw.GLFW;

import vazkii.botania.client.core.WorldTypeSkyblock;
import vazkii.botania.client.core.handler.*;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.client.fx.FXLightning;
import vazkii.botania.client.fx.ModParticles;
import vazkii.botania.client.render.entity.RenderMagicLandmine;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModFluffBlocks;
import vazkii.botania.common.block.decor.BlockFloatingFlower;
import vazkii.botania.common.block.decor.BlockModMushroom;
import vazkii.botania.common.block.mana.BlockPool;
import vazkii.botania.common.block.subtile.functional.BergamuteEventHandler;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.core.proxy.IProxy;
import vazkii.botania.common.entity.EntityDoppleganger;
import vazkii.botania.common.item.*;
import vazkii.botania.common.item.brew.ItemBrewBase;
import vazkii.botania.common.item.equipment.bauble.ItemDodgeRing;
import vazkii.botania.common.item.equipment.bauble.ItemMagnetRing;
import vazkii.botania.common.item.equipment.bauble.ItemMonocle;
import vazkii.botania.common.item.equipment.tool.bow.ItemLivingwoodBow;
import vazkii.botania.common.item.equipment.tool.terrasteel.ItemTerraAxe;
import vazkii.botania.common.item.equipment.tool.terrasteel.ItemTerraPick;
import vazkii.botania.common.item.relic.ItemInfiniteFruit;
import vazkii.botania.common.item.rod.ItemTornadoRod;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.mixin.AccessorBiomeGeneratorTypeScreens;
import vazkii.botania.mixin.AccessorRenderTypeBuffers;
import vazkii.patchouli.api.BookDrawScreenCallback;
import vazkii.patchouli.api.IMultiblock;
import vazkii.patchouli.api.PatchouliAPI;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class ClientProxy implements IProxy, ClientModInitializer {

	public static boolean jingleTheBells = false;
	public static boolean dootDoot = false;

	public static KeyBinding CORPOREA_REQUEST;

	@Override
	public void onInitializeClient() {
		Botania.proxy = this;

		ShaderHelper.initShaders();

		ModItems.registerGuis();
		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
		modBus.addListener(this::loadComplete);
		modBus.addListener(MiscellaneousIcons.INSTANCE::onTextureStitchPre);
		modBus.addListener(MiscellaneousIcons.INSTANCE::onTextureStitchPost);
		ModelLoadingRegistry.INSTANCE.registerAppender(MiscellaneousIcons.INSTANCE::onModelRegister);
		modBus.addListener(MiscellaneousIcons.INSTANCE::onModelBake);
		ModelLoadingRegistry.INSTANCE.registerAppender(ModelHandler::registerModels);
		ModParticles.FactoryHandler.registerFactories();

		IEventBus forgeBus = MinecraftForge.EVENT_BUS;
		ItemTooltipCallback.EVENT.register(TooltipHandler::onTooltipEvent);
		forgeBus.addListener(RenderLexicon::renderHand);
		forgeBus.addListener(LightningHandler::onRenderWorldLast);
		ClientTickEvents.END_CLIENT_TICK.register(KonamiHandler::clientTick);
		forgeBus.addListener(KonamiHandler::handleInput);
		BookDrawScreenCallback.EVENT.register(KonamiHandler::renderBook);
		HudRenderCallback.EVENT.register(HUDHandler::onDrawScreenPost);
		forgeBus.addListener(DebugHandler::onDrawDebugText);
		forgeBus.addListener(CorporeaInputHandler::buttonPressed);
		ClientTickEvents.END_CLIENT_TICK.register(ClientTickHandler::clientTickEnd);
		forgeBus.addListener(ClientTickHandler::renderTick);
		forgeBus.addListener(BoundTileRenderer::onWorldRenderLast);
		forgeBus.addListener(BossBarHandler::onBarRender);
		forgeBus.addListener(RenderMagicLandmine::onWorldRenderLast);
		forgeBus.addListener(AstrolabePreviewHandler::onWorldRenderLast);
		forgeBus.addListener(ItemDodgeRing::onKeyDown);
		forgeBus.addListener(EventPriority.LOWEST, BergamuteEventHandler::onSoundEvent);

		PersistentVariableHelper.setCacheFile(new File(MinecraftClient.getInstance().runDirectory, "BotaniaVars.dat"));
		try {
			PersistentVariableHelper.load();
			PersistentVariableHelper.save();
		} catch (IOException e) {
			Botania.LOGGER.fatal("Persistent Variables couldn't load!!");
		}

		if (ConfigHandler.CLIENT.enableSeasonalFeatures.getValue()) {
			LocalDateTime now = LocalDateTime.now();
			if (now.getMonth() == Month.DECEMBER && now.getDayOfMonth() >= 16 || now.getMonth() == Month.JANUARY && now.getDayOfMonth() <= 2) {
				jingleTheBells = true;
			}
			if (now.getMonth() == Month.OCTOBER) {
				dootDoot = true;
			}
		}

		registerRenderTypes();

		AccessorBiomeGeneratorTypeScreens.getAllTypes().add(WorldTypeSkyblock.INSTANCE);

		CORPOREA_REQUEST = new KeyBinding("key.botania_corporea_request", GLFW.GLFW_KEY_C, LibMisc.MOD_NAME);
		KeyBindingHelper.registerKeyBinding(CORPOREA_REQUEST);
		registerPropertyGetters();

	}

	private static void registerPropertyGetter(ItemConvertible item, Identifier id, ModelPredicateProvider propGetter) {
		FabricModelPredicateProviderRegistry.register(item.asItem(), id, propGetter);
	}

	private static void registerPropertyGetters() {
		registerPropertyGetter(ModItems.blackHoleTalisman, prefix("active"),
				(stack, world, entity) -> ItemNBTHelper.getBoolean(stack, ItemBlackHoleTalisman.TAG_ACTIVE, false) ? 1 : 0);
		registerPropertyGetter(ModItems.manaBottle, prefix("swigs_taken"),
				(stack, world, entity) -> ItemBottledMana.SWIGS - ItemBottledMana.getSwigsLeft(stack));

		Identifier vuvuzelaId = prefix("vuvuzela");
		ModelPredicateProvider isVuvuzela = (stack, world, entity) -> stack.getName().getString().toLowerCase(Locale.ROOT).contains("vuvuzela") ? 1 : 0;
		registerPropertyGetter(ModItems.grassHorn, vuvuzelaId, isVuvuzela);
		registerPropertyGetter(ModItems.leavesHorn, vuvuzelaId, isVuvuzela);
		registerPropertyGetter(ModItems.snowHorn, vuvuzelaId, isVuvuzela);

		registerPropertyGetter(ModItems.lexicon, prefix("elven"), (stack, world, living) -> ModItems.lexicon.isElvenItem(stack) ? 1 : 0);
		registerPropertyGetter(ModItems.manaCookie, prefix("totalbiscuit"),
				(stack, world, entity) -> stack.getName().getString().toLowerCase(Locale.ROOT).contains("totalbiscuit") ? 1F : 0F);
		registerPropertyGetter(ModItems.slimeBottle, prefix("active"),
				(stack, world, entity) -> stack.hasTag() && stack.getTag().getBoolean(ItemSlimeBottle.TAG_ACTIVE) ? 1.0F : 0.0F);
		registerPropertyGetter(ModItems.spawnerMover, prefix("full"),
				(stack, world, entity) -> ItemSpawnerMover.hasData(stack) ? 1 : 0);
		registerPropertyGetter(ModItems.temperanceStone, prefix("active"),
				(stack, world, entity) -> ItemNBTHelper.getBoolean(stack, ItemTemperanceStone.TAG_ACTIVE, false) ? 1 : 0);
		registerPropertyGetter(ModItems.twigWand, prefix("bindmode"),
				(stack, world, entity) -> ItemTwigWand.getBindMode(stack) ? 1 : 0);

		Identifier poolFullId = prefix("full");
		ModelPredicateProvider poolFull = (stack, world, entity) -> {
			Block block = ((BlockItem) stack.getItem()).getBlock();
			boolean renderFull = ((BlockPool) block).variant == BlockPool.Variant.CREATIVE || stack.hasTag() && stack.getTag().getBoolean("RenderFull");
			return renderFull ? 1F : 0F;
		};
		registerPropertyGetter(ModBlocks.manaPool, poolFullId, poolFull);
		registerPropertyGetter(ModBlocks.dilutedPool, poolFullId, poolFull);
		registerPropertyGetter(ModBlocks.creativePool, poolFullId, poolFull);
		registerPropertyGetter(ModBlocks.fabulousPool, poolFullId, poolFull);

		ModelPredicateProvider brewGetter = (stack, world, entity) -> {
			ItemBrewBase item = ((ItemBrewBase) stack.getItem());
			return item.getSwigs() - item.getSwigsLeft(stack);
		};
		registerPropertyGetter(ModItems.brewVial, prefix("swigs_taken"), brewGetter);
		registerPropertyGetter(ModItems.brewFlask, prefix("swigs_taken"), brewGetter);

		Identifier holidayId = prefix("holiday");
		ModelPredicateProvider holidayGetter = (stack, worldIn, entityIn) -> ClientProxy.jingleTheBells ? 1 : 0;
		registerPropertyGetter(ModItems.manaweaveHelm, holidayId, holidayGetter);
		registerPropertyGetter(ModItems.manaweaveChest, holidayId, holidayGetter);
		registerPropertyGetter(ModItems.manaweaveBoots, holidayId, holidayGetter);
		registerPropertyGetter(ModItems.manaweaveLegs, holidayId, holidayGetter);

		ModelPredicateProvider ringOnGetter = (stack, worldIn, entityIn) -> ItemMagnetRing.getCooldown(stack) <= 0 ? 1 : 0;
		registerPropertyGetter(ModItems.magnetRing, prefix("active"), ringOnGetter);
		registerPropertyGetter(ModItems.magnetRingGreater, prefix("active"), ringOnGetter);

		registerPropertyGetter(ModItems.elementiumShears, prefix("reddit"),
				(stack, world, entity) -> stack.getName().getString().equalsIgnoreCase("dammit reddit") ? 1F : 0F);
		registerPropertyGetter(ModItems.manasteelSword, prefix("elucidator"),
				(stack, world, entity) -> "the elucidator".equals(stack.getName().getString().toLowerCase().trim()) ? 1 : 0);
		registerPropertyGetter(ModItems.terraAxe, prefix("active"),
				(stack, world, entity) -> entity instanceof PlayerEntity && !ItemTerraAxe.shouldBreak((PlayerEntity) entity) ? 0 : 1);
		registerPropertyGetter(ModItems.terraPick, prefix("tipped"),
				(stack, world, entity) -> ItemTerraPick.isTipped(stack) ? 1 : 0);
		registerPropertyGetter(ModItems.terraPick, prefix("active"),
				(stack, world, entity) -> ItemTerraPick.isEnabled(stack) ? 1 : 0);
		registerPropertyGetter(ModItems.infiniteFruit, prefix("boot"),
				(stack, worldIn, entity) -> ItemInfiniteFruit.isBoot(stack) ? 1F : 0F);
		registerPropertyGetter(ModItems.tornadoRod, prefix("active"),
				(stack, world, living) -> ItemTornadoRod.isFlying(stack) ? 1 : 0);

		ModelPredicateProvider pulling = ModelPredicateProviderRegistry.get(Items.BOW, new Identifier("pulling"));
		ModelPredicateProvider pull = (stack, worldIn, entity) -> {
			if (entity == null) {
				return 0.0F;
			} else {
				ItemLivingwoodBow item = ((ItemLivingwoodBow) stack.getItem());
				return entity.getActiveItem() != stack
						? 0.0F
						: (stack.getMaxUseTime() - entity.getItemUseTimeLeft()) * item.chargeVelocityMultiplier() / 20.0F;
			}
		};
		registerPropertyGetter(ModItems.livingwoodBow, new Identifier("pulling"), pulling);
		registerPropertyGetter(ModItems.livingwoodBow, new Identifier("pull"), pull);
		registerPropertyGetter(ModItems.crystalBow, new Identifier("pulling"), pulling);
		registerPropertyGetter(ModItems.crystalBow, new Identifier("pull"), pull);
	}

	private static void registerRenderTypes() {
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.defaultAltar, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.forestAltar, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.plainsAltar, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.mountainAltar, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.fungalAltar, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.swampAltar, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.desertAltar, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.taigaAltar, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.mesaAltar, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.mossyAltar, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.ghostRail, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.solidVines, RenderLayer.getCutout());

		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.corporeaCrystalCube, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.manaGlass, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(ModFluffBlocks.managlassPane, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.elfGlass, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(ModFluffBlocks.alfglassPane, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.bifrost, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(ModFluffBlocks.bifrostPane, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.bifrostPerm, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.prism, RenderLayer.getTranslucent());

		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.starfield, RenderLayer.getCutoutMipped());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.abstrusePlatform, t -> true);
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.infrangiblePlatform, t -> true);
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.spectralPlatform, t -> true);

		Registry.BLOCK.stream().filter(b -> Registry.BLOCK.getId(b).getNamespace().equals(LibMisc.MOD_ID))
				.forEach(b -> {
					if (b instanceof BlockFloatingFlower || b instanceof FlowerBlock
							|| b instanceof TallFlowerBlock || b instanceof BlockModMushroom) {
						BlockRenderLayerMap.INSTANCE.putBlock(b, RenderLayer.getCutout());
					}
				});
	}

	private void loadComplete(FMLLoadCompleteEvent event) {
		DeferredWorkQueue.runLater(() -> {
			initAuxiliaryRender();
			ColorHandler.init();

			// Needed to prevent mana pools on carts from X-raying through the cart
			SortedMap<RenderLayer, BufferBuilder> layers = ((AccessorRenderTypeBuffers) MinecraftClient.getInstance().getBufferBuilders()).getEntityBuilders();
			layers.put(RenderHelper.MANA_POOL_WATER, new BufferBuilder(RenderHelper.MANA_POOL_WATER.getExpectedBufferSize()));
		});
	}

	private void initAuxiliaryRender() {
		Map<String, PlayerEntityRenderer> skinMap = MinecraftClient.getInstance().getEntityRenderDispatcher().getSkinMap();
		PlayerEntityRenderer render;
		render = skinMap.get("default");
		render.addFeature(new ContributorFancinessHandler(render));
		render.addFeature(new ManaTabletRenderHandler(render));
		render.addFeature(new LayerTerraHelmet(render));

		render = skinMap.get("slim");
		render.addFeature(new ContributorFancinessHandler(render));
		render.addFeature(new ManaTabletRenderHandler(render));
		render.addFeature(new LayerTerraHelmet(render));
	}

	@Override
	public boolean isTheClientPlayer(LivingEntity entity) {
		return entity == MinecraftClient.getInstance().player;
	}

	@Override
	public PlayerEntity getClientPlayer() {
		return MinecraftClient.getInstance().player;
	}

	@Override
	public boolean isClientPlayerWearingMonocle() {
		return ItemMonocle.hasMonocle(MinecraftClient.getInstance().player);
	}

	@Override
	public long getWorldElapsedTicks() {
		return ClientTickHandler.ticksInGame;
	}

	@Override
	public void lightningFX(Vector3 vectorStart, Vector3 vectorEnd, float ticksPerMeter, long seed, int colorOuter, int colorInner) {
		MinecraftClient.getInstance().particleManager.addParticle(new FXLightning(MinecraftClient.getInstance().world, vectorStart, vectorEnd, ticksPerMeter, seed, colorOuter, colorInner));
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
		return MinecraftClient.getInstance().options.viewDistance;
	}

	@Override
	public void addParticleForce(World world, ParticleEffect particleData, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
		world.addParticle(particleData, true, x, y, z, xSpeed, ySpeed, zSpeed);
	}

	@Override
	public void showMultiblock(IMultiblock mb, Text name, BlockPos anchor, BlockRotation rot) {
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
