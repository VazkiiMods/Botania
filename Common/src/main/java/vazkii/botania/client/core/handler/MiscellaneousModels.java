/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.core.handler;

import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BannerPattern;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.BotaniaAPIClient;
import vazkii.botania.api.block.IFloatingFlower;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.GunModel;
import vazkii.botania.client.model.TinyPotatoModel;
import vazkii.botania.client.render.tile.RenderTileCorporeaCrystalCube;
import vazkii.botania.client.render.tile.RenderTilePump;
import vazkii.botania.common.item.equipment.bauble.ItemFlightTiara;
import vazkii.botania.common.item.relic.ItemKingKey;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.mixin.client.AccessorModelBakery;
import vazkii.botania.xplat.IClientXplatAbstractions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class MiscellaneousModels {
	public static final MiscellaneousModels INSTANCE = new MiscellaneousModels();

	public boolean registeredModels = false;
	public final Material alfPortalTex = mainAtlas("block/alfheim_portal_swirl");
	public final Material lightRelayWorldIcon = mainAtlas("block/light_relay");
	public final Material lightRelayDetectorWorldIcon = mainAtlas("block/detector_light_relay");
	public final Material lightRelayForkWorldIcon = mainAtlas("block/fork_light_relay");
	public final Material lightRelayToggleWorldIcon = mainAtlas("block/toggle_light_relay");
	public final Material alchemyCatalystOverlay = mainAtlas("block/alchemy_catalyst_overlay");
	public final Material conjurationCatalystOverlay = mainAtlas("block/conjuration_catalyst_overlay");
	public final Material enchanterOverlay = mainAtlas("block/enchanter_overlay");
	public final Material manaVoidOverlay = mainAtlas("block/mana_void_overlay");
	public final Material manaWater = mainAtlas("block/mana_water");
	public final Material terraPlateOverlay = mainAtlas("block/terra_plate_overlay");
	public final Material corporeaWorldIcon = mainAtlas("item/corporea_spark");
	public final Material corporeaWorldIconMaster = mainAtlas("item/corporea_spark_master");
	public final Material corporeaWorldIconCreative = mainAtlas("item/corporea_spark_creative");
	public final Material corporeaIconStar = mainAtlas("item/corporea_spark_star");
	public final Material sparkWorldIcon = mainAtlas("item/spark");

	public final Material[] sparkUpgradeIcons = new Material[] {
			mainAtlas("item/spark_upgrade_rune_dispersive"),
			mainAtlas("item/spark_upgrade_rune_dominant"),
			mainAtlas("item/spark_upgrade_rune_recessive"),
			mainAtlas("item/spark_upgrade_rune_isolated")
	};
	public final BakedModel[] tiaraWingIcons = new BakedModel[ItemFlightTiara.WING_TYPES];
	public final BakedModel[] thirdEyeLayers = new BakedModel[3];

	public BakedModel goldfishModel,
			phiFlowerModel,
			nerfBatModel,
			bloodPendantChain,
			bloodPendantGem,
			snowflakePendantGem,
			itemFinderGem,
			pyroclastGem,
			crimsonGem,
			cirrusGem,
			nimbusGem,
			terrasteelHelmWillModel,
			elvenSpreaderCore,
			gaiaSpreaderCore,
			manaSpreaderCore,
			redstoneSpreaderCore;

	public HashMap<DyeColor, BakedModel> spreaderPaddings = new HashMap<>();

	public final BakedModel[] kingKeyWeaponModels = new BakedModel[ItemKingKey.WEAPON_TYPES];

	public void onModelRegister(ResourceManager rm, Consumer<ResourceLocation> consumer) {
		Set<Material> materials = AccessorModelBakery.getMaterials();

		materials.addAll(Arrays.asList(alfPortalTex, lightRelayWorldIcon, lightRelayDetectorWorldIcon,
				lightRelayForkWorldIcon, lightRelayToggleWorldIcon, alchemyCatalystOverlay, conjurationCatalystOverlay,
				enchanterOverlay, manaVoidOverlay, manaWater, terraPlateOverlay, corporeaWorldIcon, corporeaWorldIconMaster,
				corporeaWorldIconCreative, corporeaIconStar, sparkWorldIcon));
		materials.addAll(Arrays.asList(sparkUpgradeIcons));
		materials.add(RenderLexicon.TEXTURE);
		materials.add(RenderLexicon.ELVEN_TEXTURE);
		for (BannerPattern pattern : BannerPattern.values()) {
			if (pattern.getFilename().startsWith(LibMisc.MOD_ID)) {
				materials.add(new Material(Sheets.SHIELD_SHEET, pattern.location(false)));
				materials.add(new Material(Sheets.BANNER_SHEET, pattern.location(true)));
			}
		}
		consumer.accept(prefix("icon/goldfish"));
		consumer.accept(prefix("icon/phiflower"));
		consumer.accept(prefix("icon/nerfbat"));
		consumer.accept(prefix("icon/blood_pendant_chain"));
		consumer.accept(prefix("icon/blood_pendant_gem"));
		for (int i = 0; i < ItemKingKey.WEAPON_TYPES; i++) {
			consumer.accept(prefix("icon/gate_weapon_" + i));
		}
		consumer.accept(prefix("icon/will_flame"));
		for (int i = 0; i < thirdEyeLayers.length; i++) {
			consumer.accept(prefix("icon/third_eye_" + i));
		}
		consumer.accept(prefix("icon/lava_pendant_gem"));
		consumer.accept(prefix("icon/super_lava_pendant_gem"));
		consumer.accept(prefix("icon/itemfinder_gem"));
		consumer.accept(prefix("icon/cloud_pendant_gem"));
		consumer.accept(prefix("icon/super_cloud_pendant_gem"));
		consumer.accept(prefix("icon/ice_pendant_gem"));
		for (int i = 0; i < tiaraWingIcons.length; i++) {
			consumer.accept(prefix("icon/tiara_wing_" + (i + 1)));
		}

		consumer.accept(new ModelResourceLocation(LibMisc.MOD_ID + ":mana_gun_clip", "inventory"));
		consumer.accept(new ModelResourceLocation(LibMisc.MOD_ID + ":desu_gun", "inventory"));
		consumer.accept(new ModelResourceLocation(LibMisc.MOD_ID + ":desu_gun_clip", "inventory"));
		consumer.accept(prefix("block/corporea_crystal_cube_glass"));
		consumer.accept(prefix("block/pump_head"));
		consumer.accept(prefix("block/elven_spreader_core"));
		consumer.accept(prefix("block/gaia_spreader_core"));
		consumer.accept(prefix("block/mana_spreader_core"));
		consumer.accept(prefix("block/redstone_spreader_core"));
		for (DyeColor color : DyeColor.values()) {
			consumer.accept(prefix("block/" + color.toString() + "_spreader_padding"));
		}

		registerIslands();
		registerTaters(rm, consumer);

		if (!registeredModels) {
			registeredModels = true;
		}
	}

	private static void registerIslands() {
		BotaniaAPIClient.instance().registerIslandTypeModel(IFloatingFlower.IslandType.GRASS, prefix("block/islands/island_grass"));
		BotaniaAPIClient.instance().registerIslandTypeModel(IFloatingFlower.IslandType.PODZOL, prefix("block/islands/island_podzol"));
		BotaniaAPIClient.instance().registerIslandTypeModel(IFloatingFlower.IslandType.MYCEL, prefix("block/islands/island_mycel"));
		BotaniaAPIClient.instance().registerIslandTypeModel(IFloatingFlower.IslandType.SNOW, prefix("block/islands/island_snow"));
		BotaniaAPIClient.instance().registerIslandTypeModel(IFloatingFlower.IslandType.DRY, prefix("block/islands/island_dry"));
		BotaniaAPIClient.instance().registerIslandTypeModel(IFloatingFlower.IslandType.GOLDEN, prefix("block/islands/island_golden"));
		BotaniaAPIClient.instance().registerIslandTypeModel(IFloatingFlower.IslandType.VIVID, prefix("block/islands/island_vivid"));
		BotaniaAPIClient.instance().registerIslandTypeModel(IFloatingFlower.IslandType.SCORCHED, prefix("block/islands/island_scorched"));
		BotaniaAPIClient.instance().registerIslandTypeModel(IFloatingFlower.IslandType.INFUSED, prefix("block/islands/island_infused"));
		BotaniaAPIClient.instance().registerIslandTypeModel(IFloatingFlower.IslandType.MUTATED, prefix("block/islands/island_mutated"));
	}

	private static void registerTaters(ResourceManager rm, Consumer<ResourceLocation> consumer) {
		for (ResourceLocation model : rm.listResources(LibResources.PREFIX_MODELS + LibResources.PREFIX_TINY_POTATO, s -> s.endsWith(LibResources.ENDING_JSON))) {
			if (LibMisc.MOD_ID.equals(model.getNamespace())) {
				String path = model.getPath();
				path = path.substring(LibResources.PREFIX_MODELS.length(), path.length() - LibResources.ENDING_JSON.length());
				consumer.accept(new ResourceLocation(LibMisc.MOD_ID, path));
			}
		}
	}

	public void onModelBake(ModelBakery loader, Map<ResourceLocation, BakedModel> map) {
		if (!registeredModels) {
			BotaniaAPI.LOGGER.error("Additional models failed to register! Aborting baking models to avoid early crashing.");
			return;
		}
		// Platforms
		ModelResourceLocation abstruseName = new ModelResourceLocation("botania:abstruse_platform", "");
		BakedModel abstruse = map.get(abstruseName);
		ModelResourceLocation spectralName = new ModelResourceLocation("botania:spectral_platform", "");
		BakedModel spectral = map.get(spectralName);
		ModelResourceLocation infrangibleName = new ModelResourceLocation("botania:infrangible_platform", "");
		BakedModel infrangible = map.get(infrangibleName);

		map.put(abstruseName, IClientXplatAbstractions.INSTANCE.wrapPlatformModel(abstruse));
		map.put(spectralName, IClientXplatAbstractions.INSTANCE.wrapPlatformModel(spectral));
		map.put(infrangibleName, IClientXplatAbstractions.INSTANCE.wrapPlatformModel(infrangible));

		// Mana Blaster
		ModelResourceLocation key = new ModelResourceLocation("botania:mana_gun", "inventory");
		BakedModel originalModel = map.get(key);
		ModelResourceLocation clipKey = new ModelResourceLocation("botania:mana_gun_clip", "inventory");
		BakedModel originalModelClip = map.get(clipKey);
		map.put(key, new GunModel(loader, originalModel, originalModelClip));

		// Tiny Potato
		ModelResourceLocation tinyPotato = new ModelResourceLocation("botania:tiny_potato", "inventory");
		BakedModel originalPotato = map.get(tinyPotato);
		map.put(tinyPotato, new TinyPotatoModel(originalPotato));

		RenderTileCorporeaCrystalCube.cubeModel = map.get(prefix("block/corporea_crystal_cube_glass"));
		RenderTilePump.headModel = map.get(prefix("block/pump_head"));

		// Spreader cores and paddings
		elvenSpreaderCore = map.get(prefix("block/elven_spreader_core"));
		gaiaSpreaderCore = map.get(prefix("block/gaia_spreader_core"));
		manaSpreaderCore = map.get(prefix("block/mana_spreader_core"));
		redstoneSpreaderCore = map.get(prefix("block/redstone_spreader_core"));
		for (DyeColor color : DyeColor.values()) {
			spreaderPaddings.put(color, map.get(prefix("block/" + color.getName() + "_spreader_padding")));
		}

		// Icons
		goldfishModel = map.get(prefix("icon/goldfish"));
		phiFlowerModel = map.get(prefix("icon/phiflower"));
		nerfBatModel = map.get(prefix("icon/nerfbat"));
		bloodPendantChain = map.get(prefix("icon/blood_pendant_chain"));
		bloodPendantGem = map.get(prefix("icon/blood_pendant_gem"));
		for (int i = 0; i < ItemKingKey.WEAPON_TYPES; i++) {
			kingKeyWeaponModels[i] = map.get(prefix("icon/gate_weapon_" + i));
		}
		terrasteelHelmWillModel = map.get(prefix("icon/will_flame"));
		for (int i = 0; i < thirdEyeLayers.length; i++) {
			thirdEyeLayers[i] = map.get(prefix("icon/third_eye_" + i));
		}
		pyroclastGem = map.get(prefix("icon/lava_pendant_gem"));
		crimsonGem = map.get(prefix("icon/super_lava_pendant_gem"));
		itemFinderGem = map.get(prefix("icon/itemfinder_gem"));

		cirrusGem = map.get(prefix("icon/cloud_pendant_gem"));
		nimbusGem = map.get(prefix("icon/super_cloud_pendant_gem"));
		snowflakePendantGem = map.get(prefix("icon/ice_pendant_gem"));
		for (int i = 0; i < tiaraWingIcons.length; i++) {
			tiaraWingIcons[i] = map.get(prefix("icon/tiara_wing_" + (i + 1)));
		}
	}

	private static Material mainAtlas(String name) {
		return new Material(InventoryMenu.BLOCK_ATLAS, prefix(name));
	}

	private MiscellaneousModels() {}
}
