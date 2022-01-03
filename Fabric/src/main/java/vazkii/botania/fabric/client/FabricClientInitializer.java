package vazkii.botania.fabric.client;

import com.mojang.blaze3d.vertex.BufferBuilder;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.LivingEntityFeatureRendererRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.TallFlowerBlock;

import org.lwjgl.glfw.GLFW;

import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.handler.ColorHandler;
import vazkii.botania.client.core.handler.ContributorFancinessHandler;
import vazkii.botania.client.core.handler.CorporeaInputHandler;
import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.client.core.handler.KonamiHandler;
import vazkii.botania.client.core.handler.LayerTerraHelmet;
import vazkii.botania.client.core.handler.ManaTabletRenderHandler;
import vazkii.botania.client.core.handler.MiscellaneousIcons;
import vazkii.botania.client.core.handler.ModelHandler;
import vazkii.botania.client.core.handler.TooltipHandler;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.client.core.proxy.ClientProxy;
import vazkii.botania.client.fx.ModParticles;
import vazkii.botania.client.gui.ManaBarTooltipComponent;
import vazkii.botania.client.model.ModLayerDefinitions;
import vazkii.botania.client.model.armor.ArmorModels;
import vazkii.botania.client.render.entity.RenderBabylonWeapon;
import vazkii.botania.client.render.entity.RenderCorporeaSpark;
import vazkii.botania.client.render.entity.RenderDoppleganger;
import vazkii.botania.client.render.entity.RenderMagicLandmine;
import vazkii.botania.client.render.entity.RenderManaSpark;
import vazkii.botania.client.render.entity.RenderManaStorm;
import vazkii.botania.client.render.entity.RenderPinkWither;
import vazkii.botania.client.render.entity.RenderPixie;
import vazkii.botania.client.render.entity.RenderPoolMinecart;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModFluffBlocks;
import vazkii.botania.common.block.decor.BlockFloatingFlower;
import vazkii.botania.common.block.decor.BlockModMushroom;
import vazkii.botania.common.block.mana.BlockPool;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.entity.ModEntities;
import vazkii.botania.common.item.ItemBlackHoleTalisman;
import vazkii.botania.common.item.ItemBottledMana;
import vazkii.botania.common.item.ItemLexicon;
import vazkii.botania.common.item.ItemSlimeBottle;
import vazkii.botania.common.item.ItemSpawnerMover;
import vazkii.botania.common.item.ItemTemperanceStone;
import vazkii.botania.common.item.ItemTwigWand;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.brew.ItemBrewBase;
import vazkii.botania.common.item.equipment.armor.manasteel.ItemManasteelArmor;
import vazkii.botania.common.item.equipment.bauble.ItemMagnetRing;
import vazkii.botania.common.item.equipment.tool.bow.ItemLivingwoodBow;
import vazkii.botania.common.item.equipment.tool.terrasteel.ItemTerraAxe;
import vazkii.botania.common.item.equipment.tool.terrasteel.ItemTerraPick;
import vazkii.botania.common.item.relic.ItemInfiniteFruit;
import vazkii.botania.common.item.rod.ItemTornadoRod;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.common.network.PacketHandler;
import vazkii.botania.common.world.WorldTypeSkyblock;
import vazkii.botania.mixin.AccessorRenderBuffers;
import vazkii.botania.mixin.AccessorWorldPreset;
import vazkii.patchouli.api.BookDrawScreenCallback;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Locale;
import java.util.SortedMap;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class FabricClientInitializer implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		Botania.proxy = new ClientProxy();
		PacketHandler.initClient();

		ModItems.registerGuis();
		ClientLifecycleEvents.CLIENT_STARTED.register(this::loadComplete);
		LivingEntityFeatureRendererRegistrationCallback.EVENT.register(this::initAuxiliaryRender);
		ModelLoadingRegistry.INSTANCE.registerModelProvider(MiscellaneousIcons.INSTANCE::onModelRegister);
		ModelLoadingRegistry.INSTANCE.registerModelProvider(ModelHandler::registerModels);
		ModelHandler.registerRenderers();
		ModParticles.FactoryHandler.registerFactories(new ModParticles.FactoryHandler.Consumer() {
			@Override
			public <T extends ParticleOptions> void register(ParticleType<T> type, ModParticles.FactoryHandler.ParticleProviderConstructor<T> constructor) {
				ParticleFactoryRegistry.getInstance().register(type, constructor::make);
			}
		});
		ItemTooltipCallback.EVENT.register(TooltipHandler::onTooltipEvent);
		ClientTickEvents.END_CLIENT_TICK.register(KonamiHandler::clientTick);
		BookDrawScreenCallback.EVENT.register(KonamiHandler::renderBook);
		HudRenderCallback.EVENT.register(HUDHandler::onDrawScreenPost);
		ClientTickEvents.END_CLIENT_TICK.register(ClientTickHandler::clientTickEnd);
		TooltipComponentCallback.EVENT.register(ManaBarTooltipComponent::tryConvert);
		ScreenEvents.AFTER_INIT.register(CorporeaInputHandler::registerEvent);

		if (ConfigHandler.CLIENT.enableSeasonalFeatures.getValue()) {
			LocalDateTime now = LocalDateTime.now();
			if (now.getMonth() == Month.DECEMBER && now.getDayOfMonth() >= 16 || now.getMonth() == Month.JANUARY && now.getDayOfMonth() <= 2) {
				ClientProxy.jingleTheBells = true;
			}
			if (now.getMonth() == Month.OCTOBER) {
				ClientProxy.dootDoot = true;
			}
		}

		registerRenderTypes();
		registerEntityRenderers();
		registerEntityModels();

		if (Botania.gardenOfGlassLoaded) {
			AccessorWorldPreset.getAllTypes().add(WorldTypeSkyblock.INSTANCE);
		}

		ClientProxy.CORPOREA_REQUEST = new KeyMapping("key.botania_corporea_request", GLFW.GLFW_KEY_C, LibMisc.MOD_NAME);
		KeyBindingHelper.registerKeyBinding(ClientProxy.CORPOREA_REQUEST);
		registerPropertyGetters();
		registerArmors();
	}

	private static void registerArmors() {
		Item[] armors = Registry.ITEM.stream()
				.filter(i -> i instanceof ItemManasteelArmor
						&& Registry.ITEM.getKey(i).getNamespace().equals(LibMisc.MOD_ID))
				.toArray(Item[]::new);

		ArmorRenderer renderer = (matrices, vertexConsumers, stack, entity, slot, light, contextModel) -> {
			ItemManasteelArmor armor = (ItemManasteelArmor) stack.getItem();
			var model = ArmorModels.get(stack);
			var texture = armor.getArmorTexture(stack, slot);
			if (model != null) {
				contextModel.copyPropertiesTo(model);
				ArmorRenderer.renderPart(matrices, vertexConsumers, light, stack, model, new ResourceLocation(texture));
			}
		};
		ArmorRenderer.register(renderer, armors);
	}

	private static void registerPropertyGetter(ItemLike item, ResourceLocation id, ClampedItemPropertyFunction propGetter) {
		FabricModelPredicateProviderRegistry.register(item.asItem(), id, propGetter);
	}

	private static void registerPropertyGetters() {
		registerPropertyGetter(ModItems.blackHoleTalisman, prefix("active"),
				(stack, world, entity, seed) -> ItemNBTHelper.getBoolean(stack, ItemBlackHoleTalisman.TAG_ACTIVE, false) ? 1 : 0);
		registerPropertyGetter(ModItems.manaBottle, prefix("swigs_taken"),
				(stack, world, entity, seed) -> ItemBottledMana.SWIGS - ItemBottledMana.getSwigsLeft(stack));

		ResourceLocation vuvuzelaId = prefix("vuvuzela");
		ClampedItemPropertyFunction isVuvuzela = (stack, world, entity, seed) -> stack.getHoverName().getString().toLowerCase(Locale.ROOT).contains("vuvuzela") ? 1 : 0;
		registerPropertyGetter(ModItems.grassHorn, vuvuzelaId, isVuvuzela);
		registerPropertyGetter(ModItems.leavesHorn, vuvuzelaId, isVuvuzela);
		registerPropertyGetter(ModItems.snowHorn, vuvuzelaId, isVuvuzela);

		registerPropertyGetter(ModItems.lexicon, prefix("elven"), (stack, world, living, seed) -> ItemLexicon.isElven(stack) ? 1 : 0);
		registerPropertyGetter(ModItems.manaCookie, prefix("totalbiscuit"),
				(stack, world, entity, seed) -> stack.getHoverName().getString().toLowerCase(Locale.ROOT).contains("totalbiscuit") ? 1F : 0F);
		registerPropertyGetter(ModItems.slimeBottle, prefix("active"),
				(stack, world, entity, seed) -> stack.hasTag() && stack.getTag().getBoolean(ItemSlimeBottle.TAG_ACTIVE) ? 1.0F : 0.0F);
		registerPropertyGetter(ModItems.spawnerMover, prefix("full"),
				(stack, world, entity, seed) -> ItemSpawnerMover.hasData(stack) ? 1 : 0);
		registerPropertyGetter(ModItems.temperanceStone, prefix("active"),
				(stack, world, entity, seed) -> ItemNBTHelper.getBoolean(stack, ItemTemperanceStone.TAG_ACTIVE, false) ? 1 : 0);
		registerPropertyGetter(ModItems.twigWand, prefix("bindmode"),
				(stack, world, entity, seed) -> ItemTwigWand.getBindMode(stack) ? 1 : 0);

		ResourceLocation poolFullId = prefix("full");
		ClampedItemPropertyFunction poolFull = (stack, world, entity, seed) -> {
			Block block = ((BlockItem) stack.getItem()).getBlock();
			boolean renderFull = ((BlockPool) block).variant == BlockPool.Variant.CREATIVE || stack.hasTag() && stack.getTag().getBoolean("RenderFull");
			return renderFull ? 1F : 0F;
		};
		registerPropertyGetter(ModBlocks.manaPool, poolFullId, poolFull);
		registerPropertyGetter(ModBlocks.dilutedPool, poolFullId, poolFull);
		registerPropertyGetter(ModBlocks.creativePool, poolFullId, poolFull);
		registerPropertyGetter(ModBlocks.fabulousPool, poolFullId, poolFull);

		ClampedItemPropertyFunction brewGetter = (stack, world, entity, seed) -> {
			ItemBrewBase item = ((ItemBrewBase) stack.getItem());
			return item.getSwigs() - item.getSwigsLeft(stack);
		};
		registerPropertyGetter(ModItems.brewVial, prefix("swigs_taken"), brewGetter);
		registerPropertyGetter(ModItems.brewFlask, prefix("swigs_taken"), brewGetter);

		ResourceLocation holidayId = prefix("holiday");
		ClampedItemPropertyFunction holidayGetter = (stack, worldIn, entityIn, seed) -> ClientProxy.jingleTheBells ? 1 : 0;
		registerPropertyGetter(ModItems.manaweaveHelm, holidayId, holidayGetter);
		registerPropertyGetter(ModItems.manaweaveChest, holidayId, holidayGetter);
		registerPropertyGetter(ModItems.manaweaveBoots, holidayId, holidayGetter);
		registerPropertyGetter(ModItems.manaweaveLegs, holidayId, holidayGetter);

		ClampedItemPropertyFunction ringOnGetter = (stack, worldIn, entityIn, seed) -> ItemMagnetRing.getCooldown(stack) <= 0 ? 1 : 0;
		registerPropertyGetter(ModItems.magnetRing, prefix("active"), ringOnGetter);
		registerPropertyGetter(ModItems.magnetRingGreater, prefix("active"), ringOnGetter);

		registerPropertyGetter(ModItems.elementiumShears, prefix("reddit"),
				(stack, world, entity, seed) -> stack.getHoverName().getString().equalsIgnoreCase("dammit reddit") ? 1F : 0F);
		registerPropertyGetter(ModItems.manasteelSword, prefix("elucidator"),
				(stack, world, entity, seed) -> "the elucidator".equals(stack.getHoverName().getString().toLowerCase(Locale.ROOT).trim()) ? 1 : 0);
		registerPropertyGetter(ModItems.terraAxe, prefix("active"),
				(stack, world, entity, seed) -> entity instanceof Player && !ItemTerraAxe.shouldBreak((Player) entity) ? 0 : 1);
		registerPropertyGetter(ModItems.terraPick, prefix("tipped"),
				(stack, world, entity, seed) -> ItemTerraPick.isTipped(stack) ? 1 : 0);
		registerPropertyGetter(ModItems.terraPick, prefix("active"),
				(stack, world, entity, seed) -> ItemTerraPick.isEnabled(stack) ? 1 : 0);
		registerPropertyGetter(ModItems.infiniteFruit, prefix("boot"),
				(stack, worldIn, entity, seed) -> ItemInfiniteFruit.isBoot(stack) ? 1F : 0F);
		registerPropertyGetter(ModItems.tornadoRod, prefix("active"),
				(stack, world, living, seed) -> ItemTornadoRod.isFlying(stack) ? 1 : 0);

		ClampedItemPropertyFunction pulling = (ClampedItemPropertyFunction) ItemProperties.getProperty(Items.BOW, new ResourceLocation("pulling"));
		ClampedItemPropertyFunction pull = (stack, worldIn, entity, seed) -> {
			if (entity == null) {
				return 0.0F;
			} else {
				ItemLivingwoodBow item = ((ItemLivingwoodBow) stack.getItem());
				return entity.getUseItem() != stack
						? 0.0F
						: (stack.getUseDuration() - entity.getUseItemRemainingTicks()) * item.chargeVelocityMultiplier() / 20.0F;
			}
		};
		registerPropertyGetter(ModItems.livingwoodBow, new ResourceLocation("pulling"), pulling);
		registerPropertyGetter(ModItems.livingwoodBow, new ResourceLocation("pull"), pull);
		registerPropertyGetter(ModItems.crystalBow, new ResourceLocation("pulling"), pulling);
		registerPropertyGetter(ModItems.crystalBow, new ResourceLocation("pull"), pull);
	}

	private static void registerRenderTypes() {
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.defaultAltar, RenderType.cutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.forestAltar, RenderType.cutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.plainsAltar, RenderType.cutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.mountainAltar, RenderType.cutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.fungalAltar, RenderType.cutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.swampAltar, RenderType.cutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.desertAltar, RenderType.cutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.taigaAltar, RenderType.cutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.mesaAltar, RenderType.cutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.mossyAltar, RenderType.cutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.ghostRail, RenderType.cutout());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.solidVines, RenderType.cutout());

		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.corporeaCrystalCube, RenderType.translucent());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.manaGlass, RenderType.translucent());
		BlockRenderLayerMap.INSTANCE.putBlock(ModFluffBlocks.managlassPane, RenderType.translucent());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.elfGlass, RenderType.translucent());
		BlockRenderLayerMap.INSTANCE.putBlock(ModFluffBlocks.alfglassPane, RenderType.translucent());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.bifrost, RenderType.translucent());
		BlockRenderLayerMap.INSTANCE.putBlock(ModFluffBlocks.bifrostPane, RenderType.translucent());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.bifrostPerm, RenderType.translucent());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.prism, RenderType.translucent());

		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.starfield, RenderType.cutoutMipped());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.abstrusePlatform, RenderType.translucent());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.infrangiblePlatform, RenderType.translucent());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.spectralPlatform, RenderType.translucent());

		Registry.BLOCK.stream().filter(b -> Registry.BLOCK.getKey(b).getNamespace().equals(LibMisc.MOD_ID))
				.forEach(b -> {
					if (b instanceof BlockFloatingFlower || b instanceof FlowerBlock
							|| b instanceof TallFlowerBlock || b instanceof BlockModMushroom) {
						BlockRenderLayerMap.INSTANCE.putBlock(b, RenderType.cutout());
					}
				});
	}

	private static void registerEntityRenderers() {
		EntityRendererRegistry.register(ModEntities.MANA_BURST, NoopRenderer::new);
		EntityRendererRegistry.register(ModEntities.PLAYER_MOVER, NoopRenderer::new);
		EntityRendererRegistry.register(ModEntities.FLAME_RING, NoopRenderer::new);
		EntityRendererRegistry.register(ModEntities.MAGIC_LANDMINE, RenderMagicLandmine::new);
		EntityRendererRegistry.register(ModEntities.MAGIC_MISSILE, NoopRenderer::new);
		EntityRendererRegistry.register(ModEntities.FALLING_STAR, NoopRenderer::new);
		EntityRendererRegistry.register(ModEntities.ENDER_AIR, NoopRenderer::new);
		EntityRendererRegistry.register(ModEntities.THROWN_ITEM, ItemEntityRenderer::new);
		EntityRendererRegistry.register(ModEntities.PIXIE, RenderPixie::new);
		EntityRendererRegistry.register(ModEntities.DOPPLEGANGER, RenderDoppleganger::new);
		EntityRendererRegistry.register(ModEntities.SPARK, RenderManaSpark::new);
		EntityRendererRegistry.register(ModEntities.CORPOREA_SPARK, RenderCorporeaSpark::new);
		EntityRendererRegistry.register(ModEntities.POOL_MINECART, RenderPoolMinecart::new);
		EntityRendererRegistry.register(ModEntities.PINK_WITHER, RenderPinkWither::new);
		EntityRendererRegistry.register(ModEntities.MANA_STORM, RenderManaStorm::new);
		EntityRendererRegistry.register(ModEntities.BABYLON_WEAPON, RenderBabylonWeapon::new);

		EntityRendererRegistry.register(ModEntities.THORN_CHAKRAM, ThrownItemRenderer::new);
		EntityRendererRegistry.register(ModEntities.VINE_BALL, ThrownItemRenderer::new);
		EntityRendererRegistry.register(ModEntities.ENDER_AIR_BOTTLE, ThrownItemRenderer::new);
	}

	private static void registerEntityModels() {
		ModLayerDefinitions.init((loc, def) -> EntityModelLayerRegistry.registerModelLayer(loc, () -> def));
	}

	private void loadComplete(Minecraft mc) {
		ColorHandler.submitBlocks(ColorProviderRegistry.BLOCK::register);
		ColorHandler.submitItems(ColorProviderRegistry.ITEM::register);

		// Needed to prevent mana pools on carts from X-raying through the cart
		SortedMap<RenderType, BufferBuilder> layers = ((AccessorRenderBuffers) mc.renderBuffers()).getEntityBuilders();
		layers.put(RenderHelper.MANA_POOL_WATER, new BufferBuilder(RenderHelper.MANA_POOL_WATER.bufferSize()));
	}

	private void initAuxiliaryRender(EntityType<? extends LivingEntity> type, LivingEntityRenderer<?, ?> renderer,
			LivingEntityFeatureRendererRegistrationCallback.RegistrationHelper helper, EntityRendererProvider.Context ctx) {
		if (type == EntityType.PLAYER && renderer instanceof PlayerRenderer) {
			helper.register(new ContributorFancinessHandler((PlayerRenderer) renderer));
			helper.register(new ManaTabletRenderHandler((PlayerRenderer) renderer));
			helper.register(new LayerTerraHelmet((PlayerRenderer) renderer));
		}
	}
}
