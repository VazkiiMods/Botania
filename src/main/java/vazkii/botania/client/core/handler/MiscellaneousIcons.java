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
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.block.entity.BannerPattern;

import vazkii.botania.client.model.GunModel;
import vazkii.botania.client.model.LexiconModel;
import vazkii.botania.client.model.PlatformModel;
import vazkii.botania.client.model.TinyPotatoModel;
import vazkii.botania.client.render.tile.RenderTileCorporeaCrystalCube;
import vazkii.botania.client.render.tile.RenderTilePump;
import vazkii.botania.common.Botania;
import vazkii.botania.common.item.equipment.bauble.ItemFlightTiara;
import vazkii.botania.common.item.relic.ItemKingKey;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.mixin.AccessorItemOverrides;
import vazkii.botania.mixin.AccessorModelBakery;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class MiscellaneousIcons {
	public static final MiscellaneousIcons INSTANCE = new MiscellaneousIcons();

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
			elvenSpreaderInside,
			gaiaSpreaderInside,
			manaSpreaderInside,
			redstoneSpreaderInside;

	public final BakedModel[] kingKeyWeaponModels = new BakedModel[ItemKingKey.WEAPON_TYPES];

	public void onModelRegister(ResourceManager rm, Consumer<ResourceLocation> consumer) {
		Set<Material> materials = AccessorModelBakery.getMaterials();

		materials.addAll(Arrays.asList(alfPortalTex, lightRelayWorldIcon, lightRelayDetectorWorldIcon,
				lightRelayForkWorldIcon, lightRelayToggleWorldIcon, alchemyCatalystOverlay, conjurationCatalystOverlay,
				enchanterOverlay, manaVoidOverlay, manaWater, terraPlateOverlay, corporeaWorldIcon, corporeaWorldIconMaster,
				corporeaIconStar, sparkWorldIcon));
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
	}

	public void onModelBake(ModelBakery loader, Map<ResourceLocation, BakedModel> map) {
		if (!ModelHandler.registeredModels) {
			Botania.LOGGER.error("Additional models failed to register! Aborting baking models to avoid early crashing.");
			return;
		}
		// Platforms
		ModelResourceLocation abstruseName = new ModelResourceLocation("botania:abstruse_platform", "");
		BakedModel abstruse = map.get(abstruseName);
		ModelResourceLocation spectralName = new ModelResourceLocation("botania:spectral_platform", "");
		BakedModel spectral = map.get(spectralName);
		ModelResourceLocation infrangibleName = new ModelResourceLocation("botania:infrangible_platform", "");
		BakedModel infrangible = map.get(infrangibleName);

		map.put(abstruseName, new PlatformModel(abstruse));
		map.put(spectralName, new PlatformModel(spectral));
		map.put(infrangibleName, new PlatformModel(infrangible));

		// Lexicon
		BakedModel original = map.get(new ModelResourceLocation("botania:lexicon", "inventory"));
		map.put(new ModelResourceLocation("botania:lexicon", "inventory"),
				new LexiconModel(original));

		// models referenced using json overrides aren't put in the model registry, so just go through all override models and wrap them there
		List<BakedModel> overrides = ((AccessorItemOverrides) original.getOverrides()).getModels();
		for (int i = 0; i < overrides.size(); i++) {
			overrides.set(i, new LexiconModel(overrides.get(i)));
		}

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

		// todo 1.16-fabric remove OrDefault when williewillus/botania-fabric-issues#15 is fixed
		BakedModel missing = map.get(ModelBakery.MISSING_MODEL_LOCATION);
		RenderTileCorporeaCrystalCube.cubeModel = map.getOrDefault(prefix("block/corporea_crystal_cube_glass"), missing);
		RenderTilePump.headModel = map.getOrDefault(prefix("block/pump_head"), missing);
		elvenSpreaderInside = map.getOrDefault(prefix("block/elven_spreader_inside"), missing);
		gaiaSpreaderInside = map.getOrDefault(prefix("block/gaia_spreader_inside"), missing);
		manaSpreaderInside = map.getOrDefault(prefix("block/mana_spreader_inside"), missing);
		redstoneSpreaderInside = map.getOrDefault(prefix("block/redstone_spreader_inside"), missing);

		// Icons
		goldfishModel = map.getOrDefault(prefix("icon/goldfish"), missing);
		phiFlowerModel = map.getOrDefault(prefix("icon/phiflower"), missing);
		nerfBatModel = map.getOrDefault(prefix("icon/nerfbat"), missing);
		bloodPendantChain = map.getOrDefault(prefix("icon/blood_pendant_chain"), missing);
		bloodPendantGem = map.getOrDefault(prefix("icon/blood_pendant_gem"), missing);
		for (int i = 0; i < ItemKingKey.WEAPON_TYPES; i++) {
			kingKeyWeaponModels[i] = map.getOrDefault(prefix("icon/gate_weapon_" + i), missing);
		}
		terrasteelHelmWillModel = map.getOrDefault(prefix("icon/will_flame"), missing);
		for (int i = 0; i < thirdEyeLayers.length; i++) {
			thirdEyeLayers[i] = map.getOrDefault(prefix("icon/third_eye_" + i), missing);
		}
		pyroclastGem = map.getOrDefault(prefix("icon/lava_pendant_gem"), missing);
		crimsonGem = map.getOrDefault(prefix("icon/super_lava_pendant_gem"), missing);
		itemFinderGem = map.getOrDefault(prefix("icon/itemfinder_gem"), missing);

		cirrusGem = map.getOrDefault(prefix("icon/cloud_pendant_gem"), missing);
		nimbusGem = map.getOrDefault(prefix("icon/super_cloud_pendant_gem"), missing);
		snowflakePendantGem = map.getOrDefault(prefix("icon/ice_pendant_gem"), missing);
		for (int i = 0; i < tiaraWingIcons.length; i++) {
			tiaraWingIcons[i] = map.getOrDefault(prefix("icon/tiara_wing_" + (i + 1)), missing);
		}
	}

	private static Material mainAtlas(String name) {
		return new Material(TextureAtlas.LOCATION_BLOCKS, prefix(name));
	}

	private MiscellaneousIcons() {}
}
