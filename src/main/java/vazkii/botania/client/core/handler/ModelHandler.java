/**
 * This class was created by <williewillus>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.core.handler;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderSprite;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.animation.TileEntityRendererAnimation;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.registries.IRegistryDelegate;
import vazkii.botania.api.BotaniaAPIClient;
import vazkii.botania.api.item.IFloatingFlower;
import vazkii.botania.api.lexicon.multiblock.IMultiblockRenderHook;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.client.model.SpecialFlowerModel;
import vazkii.botania.client.render.entity.RenderBabylonWeapon;
import vazkii.botania.client.render.entity.RenderCorporeaSpark;
import vazkii.botania.client.render.entity.RenderDoppleganger;
import vazkii.botania.client.render.entity.RenderManaStorm;
import vazkii.botania.client.render.entity.RenderPinkWither;
import vazkii.botania.client.render.entity.RenderPixie;
import vazkii.botania.client.render.entity.RenderPoolMinecart;
import vazkii.botania.client.render.entity.RenderSnowballStack;
import vazkii.botania.client.render.entity.RenderSpark;
import vazkii.botania.client.render.tile.*;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.subtile.SubTileManastar;
import vazkii.botania.common.block.subtile.SubTilePureDaisy;
import vazkii.botania.common.block.subtile.functional.*;
import vazkii.botania.common.block.subtile.generating.SubTileArcaneRose;
import vazkii.botania.common.block.subtile.generating.SubTileDandelifeon;
import vazkii.botania.common.block.subtile.generating.SubTileEndoflame;
import vazkii.botania.common.block.subtile.generating.SubTileEntropinnyum;
import vazkii.botania.common.block.subtile.generating.SubTileGourmaryllis;
import vazkii.botania.common.block.subtile.generating.SubTileHydroangeas;
import vazkii.botania.common.block.subtile.generating.SubTileKekimurus;
import vazkii.botania.common.block.subtile.generating.SubTileMunchdew;
import vazkii.botania.common.block.subtile.generating.SubTileNarslimmus;
import vazkii.botania.common.block.subtile.generating.SubTileRafflowsia;
import vazkii.botania.common.block.subtile.generating.SubTileShulkMeNot;
import vazkii.botania.common.block.subtile.generating.SubTileSpectrolus;
import vazkii.botania.common.block.subtile.generating.SubTileThermalily;
import vazkii.botania.common.block.tile.*;
import vazkii.botania.common.block.tile.corporea.TileCorporeaCrystalCube;
import vazkii.botania.common.block.tile.corporea.TileCorporeaIndex;
import vazkii.botania.common.block.tile.mana.TileBellows;
import vazkii.botania.common.block.tile.mana.TilePool;
import vazkii.botania.common.block.tile.mana.TilePrism;
import vazkii.botania.common.block.tile.mana.TilePump;
import vazkii.botania.common.block.tile.mana.TileSpreader;
import vazkii.botania.common.block.tile.string.TileRedString;
import vazkii.botania.common.entity.EntityBabylonWeapon;
import vazkii.botania.common.entity.EntityCorporeaSpark;
import vazkii.botania.common.entity.EntityDoppleganger;
import vazkii.botania.common.entity.EntityEnderAirBottle;
import vazkii.botania.common.entity.EntityManaStorm;
import vazkii.botania.common.entity.EntityPinkWither;
import vazkii.botania.common.entity.EntityPixie;
import vazkii.botania.common.entity.EntityPoolMinecart;
import vazkii.botania.common.entity.EntitySpark;
import vazkii.botania.common.entity.EntityThornChakram;
import vazkii.botania.common.entity.EntityVineBall;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = LibMisc.MOD_ID)
public final class ModelHandler {
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent evt) {
		ModelLoaderRegistry.registerLoader(SpecialFlowerModel.Loader.INSTANCE);

		registerSubtiles();

		RenderTileFloatingFlower renderTileFloatingFlower = new RenderTileFloatingFlower();
		RenderTilePylon renderTilePylon = new RenderTilePylon();
		ClientRegistry.bindTileEntitySpecialRenderer(TileAltar.class, new RenderTileAltar());
		ClientRegistry.bindTileEntitySpecialRenderer(TileSpreader.class, new RenderTileSpreader());
		ClientRegistry.bindTileEntitySpecialRenderer(TilePool.class, new RenderTilePool());
		ClientRegistry.bindTileEntitySpecialRenderer(TileRuneAltar.class, new RenderTileRuneAltar());
		ClientRegistry.bindTileEntitySpecialRenderer(TilePylon.class, renderTilePylon);
		ClientRegistry.bindTileEntitySpecialRenderer(TileEnchanter.class, new RenderTileEnchanter());
		ClientRegistry.bindTileEntitySpecialRenderer(TileAlfPortal.class, new RenderTileAlfPortal());
		ClientRegistry.bindTileEntitySpecialRenderer(TileFloatingFlower.class, renderTileFloatingFlower);
		ClientRegistry.bindTileEntitySpecialRenderer(TileFloatingSpecialFlower.class, renderTileFloatingFlower);
		ClientRegistry.bindTileEntitySpecialRenderer(TileTinyPotato.class, new RenderTileTinyPotato());
		ClientRegistry.bindTileEntitySpecialRenderer(TileStarfield.class, new RenderTileStarfield());
		ClientRegistry.bindTileEntitySpecialRenderer(TileBrewery.class, new RenderTileBrewery());
		ClientRegistry.bindTileEntitySpecialRenderer(TileTerraPlate.class, new RenderTileTerraPlate());
		ClientRegistry.bindTileEntitySpecialRenderer(TileRedString.class, new RenderTileRedString());
		ClientRegistry.bindTileEntitySpecialRenderer(TilePrism.class, new RenderTilePrism());
		ClientRegistry.bindTileEntitySpecialRenderer(TileCorporeaIndex.class, new RenderTileCorporeaIndex());
		ClientRegistry.bindTileEntitySpecialRenderer(TilePump.class, new TileEntityRendererAnimation<>());
		ClientRegistry.bindTileEntitySpecialRenderer(TileCorporeaCrystalCube.class, new RenderTileCorporeaCrystalCube());
		ClientRegistry.bindTileEntitySpecialRenderer(TileIncensePlate.class, new RenderTileIncensePlate());
		ClientRegistry.bindTileEntitySpecialRenderer(TileHourglass.class, new RenderTileHourglass());
		ClientRegistry.bindTileEntitySpecialRenderer(TileSparkChanger.class, new RenderTileSparkChanger());
		ClientRegistry.bindTileEntitySpecialRenderer(TileCocoon.class, new RenderTileCocoon());
		ClientRegistry.bindTileEntitySpecialRenderer(TileLightRelay.class, new RenderTileLightRelay());
		ClientRegistry.bindTileEntitySpecialRenderer(TileBellows.class, new RenderTileBellows());
		ClientRegistry.bindTileEntitySpecialRenderer(TileGaiaHead.class, new RenderTileGaiaHead());
		ClientRegistry.bindTileEntitySpecialRenderer(TileTeruTeruBozu.class, new RenderTileTeruTeruBozu());
		ClientRegistry.bindTileEntitySpecialRenderer(TileAvatar.class, new RenderTileAvatar());
		ClientRegistry.bindTileEntitySpecialRenderer(TileAnimatedTorch.class, new RenderTileAnimatedTorch());

		RenderingRegistry.registerEntityRenderingHandler(EntityPixie.class, RenderPixie::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityDoppleganger.class, RenderDoppleganger::new);
		RenderingRegistry.registerEntityRenderingHandler(EntitySpark.class, RenderSpark::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityCorporeaSpark.class, RenderCorporeaSpark::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityPoolMinecart.class, RenderPoolMinecart::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityPinkWither.class, RenderPinkWither::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityManaStorm.class, RenderManaStorm::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityBabylonWeapon.class, RenderBabylonWeapon::new);

		RenderingRegistry.registerEntityRenderingHandler(EntityThornChakram.class, renderManager -> new RenderSnowballStack<>(renderManager, ModItems.thornChakram, Minecraft.getInstance().getItemRenderer(),
				entity -> entity.isFire() ? new ItemStack(ModItems.flareChakram) : new ItemStack(ModItems.thornChakram)));
		RenderingRegistry.registerEntityRenderingHandler(EntityVineBall.class, renderManager -> new RenderSprite<>(renderManager, ModItems.vineBall, Minecraft.getInstance().getItemRenderer()));
		RenderingRegistry.registerEntityRenderingHandler(EntityEnderAirBottle.class, renderManager -> new RenderSprite<>(renderManager, ModItems.enderAirBottle, Minecraft.getInstance().getItemRenderer()));

		ShaderHelper.initShaders();

		IMultiblockRenderHook.renderHooks.put(ModBlocks.manaPylon, renderTilePylon);
		IMultiblockRenderHook.renderHooks.put(ModBlocks.naturaPylon, renderTilePylon);
		IMultiblockRenderHook.renderHooks.put(ModBlocks.gaiaPylon, renderTilePylon);
	}

	private static void registerSubtiles() {
		BotaniaAPIClient.registerIslandTypeModel(IFloatingFlower.IslandType.GRASS, new ModelResourceLocation("botania:mini_island", "variant=grass"));
		BotaniaAPIClient.registerIslandTypeModel(IFloatingFlower.IslandType.PODZOL, new ModelResourceLocation("botania:mini_island", "variant=podzol"));
		BotaniaAPIClient.registerIslandTypeModel(IFloatingFlower.IslandType.MYCEL, new ModelResourceLocation("botania:mini_island", "variant=mycel"));
		BotaniaAPIClient.registerIslandTypeModel(IFloatingFlower.IslandType.SNOW, new ModelResourceLocation("botania:mini_island", "variant=snow"));
		BotaniaAPIClient.registerIslandTypeModel(IFloatingFlower.IslandType.DRY, new ModelResourceLocation("botania:mini_island", "variant=dry"));
		BotaniaAPIClient.registerIslandTypeModel(IFloatingFlower.IslandType.GOLDEN, new ModelResourceLocation("botania:mini_island", "variant=golden"));
		BotaniaAPIClient.registerIslandTypeModel(IFloatingFlower.IslandType.VIVID, new ModelResourceLocation("botania:mini_island", "variant=vivid"));
		BotaniaAPIClient.registerIslandTypeModel(IFloatingFlower.IslandType.SCORCHED, new ModelResourceLocation("botania:mini_island", "variant=scorched"));
		BotaniaAPIClient.registerIslandTypeModel(IFloatingFlower.IslandType.INFUSED, new ModelResourceLocation("botania:mini_island", "variant=infused"));
		BotaniaAPIClient.registerIslandTypeModel(IFloatingFlower.IslandType.MUTATED, new ModelResourceLocation("botania:mini_island", "variant=mutated"));

		BotaniaAPIClient.registerSubtileModel(SubTileManastar.class, new ModelResourceLocation(LibBlockNames.SUBTILE_MANASTAR.toString()));
		BotaniaAPIClient.registerSubtileModel(SubTilePureDaisy.class, new ModelResourceLocation(LibBlockNames.SUBTILE_PUREDAISY.toString()));

		BotaniaAPIClient.registerSubtileModel(SubTileEndoflame.class, new ModelResourceLocation(LibBlockNames.SUBTILE_ENDOFLAME.toString()));
		BotaniaAPIClient.registerSubtileModel(SubTileHydroangeas.class, new ModelResourceLocation(LibBlockNames.SUBTILE_HYDROANGEAS.toString()));
		BotaniaAPIClient.registerSubtileModel(SubTileThermalily.class, new ModelResourceLocation(LibBlockNames.SUBTILE_THERMALILY.toString()));
		BotaniaAPIClient.registerSubtileModel(SubTileArcaneRose.class, new ModelResourceLocation(LibBlockNames.SUBTILE_ARCANE_ROSE.toString()));
		BotaniaAPIClient.registerSubtileModel(SubTileMunchdew.class, new ModelResourceLocation(LibBlockNames.SUBTILE_MUNCHDEW.toString()));
		BotaniaAPIClient.registerSubtileModel(SubTileEntropinnyum.class, new ModelResourceLocation(LibBlockNames.SUBTILE_ENTROPINNYUM.toString()));
		BotaniaAPIClient.registerSubtileModel(SubTileKekimurus.class, new ModelResourceLocation(LibBlockNames.SUBTILE_KEKIMURUS.toString()));
		BotaniaAPIClient.registerSubtileModel(SubTileGourmaryllis.class, new ModelResourceLocation(LibBlockNames.SUBTILE_GOURMARYLLIS.toString()));
		BotaniaAPIClient.registerSubtileModel(SubTileNarslimmus.class, new ModelResourceLocation(LibBlockNames.SUBTILE_NARSLIMMUS.toString()));
		BotaniaAPIClient.registerSubtileModel(SubTileSpectrolus.class, new ModelResourceLocation(LibBlockNames.SUBTILE_SPECTROLUS.toString()));
		BotaniaAPIClient.registerSubtileModel(SubTileDandelifeon.class, new ModelResourceLocation(LibBlockNames.SUBTILE_DANDELIFEON.toString()));
		BotaniaAPIClient.registerSubtileModel(SubTileRafflowsia.class, new ModelResourceLocation(LibBlockNames.SUBTILE_RAFFLOWSIA.toString()));
		BotaniaAPIClient.registerSubtileModel(SubTileShulkMeNot.class, new ModelResourceLocation(LibBlockNames.SUBTILE_SHULK_ME_NOT.toString()));

		BotaniaAPIClient.registerSubtileModel(SubTileBellethorn.class, new ModelResourceLocation(LibBlockNames.SUBTILE_BELLETHORN.toString()));
		BotaniaAPIClient.registerSubtileModel(SubTileBellethorn.Mini.class, new ModelResourceLocation(LibBlockNames.SUBTILE_BELLETHORN + "_chibi"));
		BotaniaAPIClient.registerSubtileModel(SubTileDreadthorn.class, new ModelResourceLocation(LibBlockNames.SUBTILE_DREADTHORN.toString()));
		BotaniaAPIClient.registerSubtileModel(SubTileHeiseiDream.class, new ModelResourceLocation(LibBlockNames.SUBTILE_HEISEI_DREAM.toString()));
		BotaniaAPIClient.registerSubtileModel(SubTileTigerseye.class, new ModelResourceLocation(LibBlockNames.SUBTILE_TIGERSEYE.toString()));
		BotaniaAPIClient.registerSubtileModel(SubTileJadedAmaranthus.class, new ModelResourceLocation(LibBlockNames.SUBTILE_JADED_AMARANTHUS.toString()));
		BotaniaAPIClient.registerSubtileModel(SubTileOrechid.class, new ModelResourceLocation(LibBlockNames.SUBTILE_ORECHID.toString()));
		BotaniaAPIClient.registerSubtileModel(SubTileOrechidIgnem.class, new ModelResourceLocation(LibBlockNames.SUBTILE_ORECHID_IGNEM.toString()));
		BotaniaAPIClient.registerSubtileModel(SubTileFallenKanade.class, new ModelResourceLocation(LibBlockNames.SUBTILE_FALLEN_KANADE.toString()));
		BotaniaAPIClient.registerSubtileModel(SubTileExoflame.class, new ModelResourceLocation(LibBlockNames.SUBTILE_EXOFLAME.toString()));
		BotaniaAPIClient.registerSubtileModel(SubTileAgricarnation.class, new ModelResourceLocation(LibBlockNames.SUBTILE_AGRICARNATION.toString()));
		BotaniaAPIClient.registerSubtileModel(SubTileAgricarnation.Mini.class, new ModelResourceLocation(LibBlockNames.SUBTILE_AGRICARNATION + "_chibi"));
		BotaniaAPIClient.registerSubtileModel(SubTileHopperhock.class, new ModelResourceLocation(LibBlockNames.SUBTILE_HOPPERHOCK.toString()));
		BotaniaAPIClient.registerSubtileModel(SubTileHopperhock.Mini.class, new ModelResourceLocation(LibBlockNames.SUBTILE_HOPPERHOCK + "_chibi"));
		BotaniaAPIClient.registerSubtileModel(SubTileTangleberrie.class, new ModelResourceLocation(LibBlockNames.SUBTILE_TANGLEBERRIE.toString()));
		BotaniaAPIClient.registerSubtileModel(SubTileJiyuulia.class, new ModelResourceLocation(LibBlockNames.SUBTILE_JIYUULIA.toString()));
		BotaniaAPIClient.registerSubtileModel(SubTileRannuncarpus.class, new ModelResourceLocation(LibBlockNames.SUBTILE_RANNUNCARPUS.toString()));
		BotaniaAPIClient.registerSubtileModel(SubTileRannuncarpus.Mini.class, new ModelResourceLocation(LibBlockNames.SUBTILE_RANNUNCARPUS + "_chibi"));
		BotaniaAPIClient.registerSubtileModel(SubTileHyacidus.class, new ModelResourceLocation(LibBlockNames.SUBTILE_HYACIDUS.toString()));
		BotaniaAPIClient.registerSubtileModel(SubTilePollidisiac.class, new ModelResourceLocation(LibBlockNames.SUBTILE_POLLIDISIAC.toString()));
		BotaniaAPIClient.registerSubtileModel(SubTileClayconia.class, new ModelResourceLocation(LibBlockNames.SUBTILE_CLAYCONIA.toString()));
		BotaniaAPIClient.registerSubtileModel(SubTileClayconia.Mini.class, new ModelResourceLocation(LibBlockNames.SUBTILE_CLAYCONIA + "_chibi"));
		BotaniaAPIClient.registerSubtileModel(SubTileLoonuim.class, new ModelResourceLocation(LibBlockNames.SUBTILE_LOONIUM.toString()));
		BotaniaAPIClient.registerSubtileModel(SubTileDaffomill.class, new ModelResourceLocation(LibBlockNames.SUBTILE_DAFFOMILL.toString()));
		BotaniaAPIClient.registerSubtileModel(SubTileVinculotus.class, new ModelResourceLocation(LibBlockNames.SUBTILE_VINCULOTUS.toString()));
		BotaniaAPIClient.registerSubtileModel(SubTileSpectranthemum.class, new ModelResourceLocation(LibBlockNames.SUBTILE_SPECTRANTHEMUM.toString()));
		BotaniaAPIClient.registerSubtileModel(SubTileMedumone.class, new ModelResourceLocation(LibBlockNames.SUBTILE_MEDUMONE.toString()));
		BotaniaAPIClient.registerSubtileModel(SubTileMarimorphosis.class, new ModelResourceLocation(LibBlockNames.SUBTILE_MARIMORPHOSIS.toString()));
		BotaniaAPIClient.registerSubtileModel(SubTileMarimorphosis.Mini.class, new ModelResourceLocation(LibBlockNames.SUBTILE_MARIMORPHOSIS + "_chibi"));
		BotaniaAPIClient.registerSubtileModel(SubTileBubbell.class, new ModelResourceLocation(LibBlockNames.SUBTILE_BUBBELL.toString()));
		BotaniaAPIClient.registerSubtileModel(SubTileBubbell.Mini.class, new ModelResourceLocation(LibBlockNames.SUBTILE_BUBBELL + "_chibi"));
		BotaniaAPIClient.registerSubtileModel(SubTileSolegnolia.class, new ModelResourceLocation(LibBlockNames.SUBTILE_SOLEGNOLIA.toString()));
		BotaniaAPIClient.registerSubtileModel(SubTileSolegnolia.Mini.class, new ModelResourceLocation(LibBlockNames.SUBTILE_SOLEGNOLIA + "_chibi"));
		BotaniaAPIClient.registerSubtileModel(SubTileBergamute.class, new ModelResourceLocation(LibBlockNames.SUBTILE_BERGAMUTE.toString()));
	}

	private ModelHandler() {}
}
