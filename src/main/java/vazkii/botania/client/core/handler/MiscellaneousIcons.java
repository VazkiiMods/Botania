/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.core.handler;

import net.minecraft.block.entity.BannerPattern;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.texture.MissingSprite;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.SpriteIdentifier;

import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import vazkii.botania.api.mana.spark.SparkUpgradeType;
import vazkii.botania.client.model.GunModel;
import vazkii.botania.client.model.LexiconModel;
import vazkii.botania.client.model.PlatformModel;
import vazkii.botania.client.render.tile.RenderTileCorporeaCrystalCube;
import vazkii.botania.client.render.tile.RenderTilePump;
import vazkii.botania.common.Botania;
import vazkii.botania.common.item.equipment.bauble.ItemFlightTiara;
import vazkii.botania.common.item.relic.ItemKingKey;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.mixin.AccessorItemOverrideList;
import vazkii.botania.mixin.AccessorModelBakery;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class MiscellaneousIcons {
	public static final MiscellaneousIcons INSTANCE = new MiscellaneousIcons();

	public final SpriteIdentifier alfPortalTex = mainAtlas("block/alfheim_portal_swirl");
	public final SpriteIdentifier lightRelayWorldIcon = mainAtlas("block/light_relay");
	public final SpriteIdentifier lightRelayDetectorWorldIcon = mainAtlas("block/detector_light_relay");
	public final SpriteIdentifier lightRelayForkWorldIcon = mainAtlas("block/fork_light_relay");
	public final SpriteIdentifier lightRelayToggleWorldIcon = mainAtlas("block/toggle_light_relay");
	public final SpriteIdentifier alchemyCatalystOverlay = mainAtlas("block/alchemy_catalyst_overlay");
	public final SpriteIdentifier conjurationCatalystOverlay = mainAtlas("block/conjuration_catalyst_overlay");
	public final SpriteIdentifier enchanterOverlay = mainAtlas("block/enchanter_overlay");
	public final SpriteIdentifier manaVoidOverlay = mainAtlas("block/mana_void_overlay");
	public final SpriteIdentifier manaWater = mainAtlas("block/mana_water");
	public final SpriteIdentifier terraPlateOverlay = mainAtlas("block/terra_plate_overlay");
	public final SpriteIdentifier corporeaWorldIcon = mainAtlas("item/corporea_spark");
	public final SpriteIdentifier corporeaWorldIconMaster = mainAtlas("item/corporea_spark_master");
	public final SpriteIdentifier corporeaIconStar = mainAtlas("item/corporea_spark_star");
	public final SpriteIdentifier sparkWorldIcon = mainAtlas("item/spark");

	public final SpriteIdentifier[] sparkUpgradeIcons = new SpriteIdentifier[] {
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

	public void onModelRegister(ResourceManager rm, Consumer<ModelIdentifier> consumer) {
		Set<SpriteIdentifier> materials = AccessorModelBakery.getMaterials();

		materials.addAll(Arrays.asList(alfPortalTex, lightRelayWorldIcon, lightRelayDetectorWorldIcon,
				lightRelayForkWorldIcon, lightRelayToggleWorldIcon, alchemyCatalystOverlay, conjurationCatalystOverlay,
				enchanterOverlay, manaVoidOverlay, manaWater, terraPlateOverlay, corporeaWorldIcon, corporeaWorldIconMaster,
				corporeaIconStar, sparkWorldIcon));
		materials.addAll(Arrays.asList(sparkUpgradeIcons));
		materials.add(RenderLexicon.TEXTURE);
		materials.add(RenderLexicon.ELVEN_TEXTURE);
		for (BannerPattern pattern : BannerPattern.values()) {
			if (pattern.getName().startsWith(LibMisc.MOD_ID)) {
				materials.add(new SpriteIdentifier(TexturedRenderLayers.SHIELD_PATTERNS_ATLAS_TEXTURE, pattern.getSpriteId(false)));
				materials.add(new SpriteIdentifier(TexturedRenderLayers.BANNER_PATTERNS_ATLAS_TEXTURE, pattern.getSpriteId(true)));
			}
		}
		consumer.accept(new ModelIdentifier(prefix("icon/goldfish"), "inventory"));
		consumer.accept(new ModelIdentifier(prefix("icon/phiflower"), "inventory"));
		consumer.accept(new ModelIdentifier(prefix("icon/nerfbat"), "inventory"));
		consumer.accept(new ModelIdentifier(prefix("icon/blood_pendant_chain"), "inventory"));
		consumer.accept(new ModelIdentifier(prefix("icon/blood_pendant_gem"), "inventory"));
		for (int i = 0; i < ItemKingKey.WEAPON_TYPES; i++) {
			consumer.accept(new ModelIdentifier(prefix("icon/gate_weapon_" + i), "inventory"));
		}
		consumer.accept(new ModelIdentifier(prefix("icon/will_flame"), "inventory"));
		for (int i = 0; i < thirdEyeLayers.length; i++) {
			consumer.accept(new ModelIdentifier(prefix("icon/third_eye_" + i), "inventory"));
		}
		consumer.accept(new ModelIdentifier(prefix("icon/lava_pendant_gem"), "inventory"));
		consumer.accept(new ModelIdentifier(prefix("icon/super_lava_pendant_gem"), "inventory"));
		consumer.accept(new ModelIdentifier(prefix("icon/itemfinder_gem"), "inventory"));
		consumer.accept(new ModelIdentifier(prefix("icon/cloud_pendant_gem"), "inventory"));
		consumer.accept(new ModelIdentifier(prefix("icon/super_cloud_pendant_gem"), "inventory"));
		consumer.accept(new ModelIdentifier(prefix("icon/ice_pendant_gem"), "inventory"));
		for (int i = 0; i < tiaraWingIcons.length; i++) {
			consumer.accept(new ModelIdentifier(prefix("icon/tiara_wing_" + (i + 1)), "inventory"));
		}
	}

	public void onModelBake(ModelLoader loader, Map<Identifier, BakedModel> map) {
		if (!ModelHandler.registeredModels) {
			Botania.LOGGER.error("Additional models failed to register! Aborting baking models to avoid early crashing.");
			return;
		}
		// Platforms
		ModelIdentifier abstruseName = new ModelIdentifier("botania:abstruse_platform", "");
		BakedModel abstruse = map.get(abstruseName);
		ModelIdentifier spectralName = new ModelIdentifier("botania:spectral_platform", "");
		BakedModel spectral = map.get(spectralName);
		ModelIdentifier infrangibleName = new ModelIdentifier("botania:infrangible_platform", "");
		BakedModel infrangible = map.get(infrangibleName);

		map.put(abstruseName, new PlatformModel(abstruse));
		map.put(spectralName, new PlatformModel(spectral));
		map.put(infrangibleName, new PlatformModel(infrangible));

		// Lexicon
		BakedModel original = map.get(new ModelIdentifier("botania:lexicon", "inventory"));
		map.put(new ModelIdentifier("botania:lexicon", "inventory"),
				new LexiconModel(original));

		// models referenced using json overrides aren't put in the model registry, so just go through all override models and wrap them there
		List<BakedModel> overrides = ((AccessorItemOverrideList) original.getOverrides()).getModels();
		for (int i = 0; i < overrides.size(); i++) {
			overrides.set(i, new LexiconModel(overrides.get(i)));
		}

		// Mana Blaster
		ModelIdentifier key = new ModelIdentifier("botania:mana_gun", "inventory");
		BakedModel originalModel = map.get(key);
		ModelIdentifier clipKey = new ModelIdentifier("botania:mana_gun_clip", "inventory");
		BakedModel originalModelClip = map.get(clipKey);
		map.put(key, new GunModel(loader, originalModel, originalModelClip));

		RenderTileCorporeaCrystalCube.cubeModel = map.get(prefix("block/corporea_crystal_cube_glass"));
		RenderTilePump.headModel = map.get(prefix("block/pump_head"));
		elvenSpreaderInside = map.get(prefix("block/elven_spreader_inside"));
		gaiaSpreaderInside = map.get(prefix("block/gaia_spreader_inside"));
		manaSpreaderInside = map.get(prefix("block/mana_spreader_inside"));
		redstoneSpreaderInside = map.get(prefix("block/redstone_spreader_inside"));

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

	private static SpriteIdentifier mainAtlas(String name) {
		return new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, prefix(name));
	}

	private MiscellaneousIcons() {}
}
