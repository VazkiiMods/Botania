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
import net.minecraft.client.texture.MissingSprite;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;

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

import java.util.List;
import java.util.Locale;
import java.util.Set;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class MiscellaneousIcons {
	public static final MiscellaneousIcons INSTANCE = new MiscellaneousIcons();

	public Sprite alfPortalTex,
			lightRelayWorldIcon,
			lightRelayDetectorWorldIcon,
			lightRelayForkWorldIcon,
			lightRelayToggleWorldIcon,
			alchemyCatalystOverlay,
			conjurationCatalystOverlay,
			enchanterOverlay,
			manaVoidOverlay,
			manaWater,
			terraPlateOverlay,
			corporeaWorldIcon,
			corporeaWorldIconMaster,
			corporeaIconStar,
			sparkWorldIcon,
			manaDetectorIcon,
			runeAltarTriggerIcon;

	public final Sprite[] sparkUpgradeIcons = new Sprite[4];
	// public final Map<TriggerManaLevel.State, TextureAtlasSprite> manaLevelTriggerIcons = new EnumMap<>(TriggerManaLevel.State.class);
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

	public void onModelRegister(ModelRegistryEvent evt) {
		Set<SpriteIdentifier> materials = AccessorModelBakery.getMaterials();

		materials.add(RenderLexicon.TEXTURE);
		materials.add(RenderLexicon.ELVEN_TEXTURE);
		for (BannerPattern pattern : BannerPattern.values()) {
			if (pattern.getName().startsWith(LibMisc.MOD_ID)) {
				materials.add(new SpriteIdentifier(TexturedRenderLayers.SHIELD_PATTERNS_ATLAS_TEXTURE, pattern.getSpriteId(false)));
				materials.add(new SpriteIdentifier(TexturedRenderLayers.BANNER_PATTERNS_ATLAS_TEXTURE, pattern.getSpriteId(true)));
			}
		}
		ModelLoader.addSpecialModel(prefix("icon/goldfish"));
		ModelLoader.addSpecialModel(prefix("icon/phiflower"));
		ModelLoader.addSpecialModel(prefix("icon/nerfbat"));
		ModelLoader.addSpecialModel(prefix("icon/blood_pendant_chain"));
		ModelLoader.addSpecialModel(prefix("icon/blood_pendant_gem"));
		for (int i = 0; i < ItemKingKey.WEAPON_TYPES; i++) {
			ModelLoader.addSpecialModel(prefix("icon/gate_weapon_" + i));
		}
		ModelLoader.addSpecialModel(prefix("icon/will_flame"));
		for (int i = 0; i < thirdEyeLayers.length; i++) {
			ModelLoader.addSpecialModel(prefix("icon/third_eye_" + i));
		}
		ModelLoader.addSpecialModel(prefix("icon/lava_pendant_gem"));
		ModelLoader.addSpecialModel(prefix("icon/super_lava_pendant_gem"));
		ModelLoader.addSpecialModel(prefix("icon/itemfinder_gem"));
		ModelLoader.addSpecialModel(prefix("icon/cloud_pendant_gem"));
		ModelLoader.addSpecialModel(prefix("icon/super_cloud_pendant_gem"));
		ModelLoader.addSpecialModel(prefix("icon/ice_pendant_gem"));
		for (int i = 0; i < tiaraWingIcons.length; i++) {
			ModelLoader.addSpecialModel(prefix("icon/tiara_wing_" + (i + 1)));
		}
	}

	public void onModelBake(ModelBakeEvent evt) {
		if (!ModelHandler.registeredModels) {
			Botania.LOGGER.error("Additional models failed to register! Aborting baking models to avoid early crashing.");
			return;
		}
		// Platforms
		ModelIdentifier abstruseName = new ModelIdentifier("botania:abstruse_platform", "");
		BakedModel abstruse = evt.getModelRegistry().get(abstruseName);
		ModelIdentifier spectralName = new ModelIdentifier("botania:spectral_platform", "");
		BakedModel spectral = evt.getModelRegistry().get(spectralName);
		ModelIdentifier infrangibleName = new ModelIdentifier("botania:infrangible_platform", "");
		BakedModel infrangible = evt.getModelRegistry().get(infrangibleName);

		evt.getModelRegistry().put(abstruseName, new PlatformModel(abstruse));
		evt.getModelRegistry().put(spectralName, new PlatformModel(spectral));
		evt.getModelRegistry().put(infrangibleName, new PlatformModel(infrangible));

		// Lexicon
		BakedModel original = evt.getModelRegistry().get(new ModelIdentifier("botania:lexicon", "inventory"));
		evt.getModelRegistry().put(new ModelIdentifier("botania:lexicon", "inventory"),
				new LexiconModel(original));

		// models referenced using json overrides aren't put in the model registry, so just go through all override models and wrap them there
		List<BakedModel> overrides = ((AccessorItemOverrideList) original.getOverrides()).getOverrideBakedModels();
		for (int i = 0; i < overrides.size(); i++) {
			overrides.set(i, new LexiconModel(overrides.get(i)));
		}

		// Mana Blaster
		ModelIdentifier key = new ModelIdentifier("botania:mana_gun", "inventory");
		BakedModel originalModel = evt.getModelRegistry().get(key);
		ModelIdentifier clipKey = new ModelIdentifier("botania:mana_gun_clip", "inventory");
		BakedModel originalModelClip = evt.getModelRegistry().get(clipKey);
		evt.getModelRegistry().put(key, new GunModel(evt.getModelLoader(), originalModel, originalModelClip));

		RenderTileCorporeaCrystalCube.cubeModel = evt.getModelRegistry().get(prefix("block/corporea_crystal_cube_glass"));
		RenderTilePump.headModel = evt.getModelRegistry().get(prefix("block/pump_head"));
		elvenSpreaderInside = evt.getModelRegistry().get(prefix("block/elven_spreader_inside"));
		gaiaSpreaderInside = evt.getModelRegistry().get(prefix("block/gaia_spreader_inside"));
		manaSpreaderInside = evt.getModelRegistry().get(prefix("block/mana_spreader_inside"));
		redstoneSpreaderInside = evt.getModelRegistry().get(prefix("block/redstone_spreader_inside"));

		// Icons
		goldfishModel = evt.getModelRegistry().get(prefix("icon/goldfish"));
		phiFlowerModel = evt.getModelRegistry().get(prefix("icon/phiflower"));
		nerfBatModel = evt.getModelRegistry().get(prefix("icon/nerfbat"));
		bloodPendantChain = evt.getModelRegistry().get(prefix("icon/blood_pendant_chain"));
		bloodPendantGem = evt.getModelRegistry().get(prefix("icon/blood_pendant_gem"));
		for (int i = 0; i < ItemKingKey.WEAPON_TYPES; i++) {
			kingKeyWeaponModels[i] = evt.getModelRegistry().get(prefix("icon/gate_weapon_" + i));
		}
		terrasteelHelmWillModel = evt.getModelRegistry().get(prefix("icon/will_flame"));
		for (int i = 0; i < thirdEyeLayers.length; i++) {
			thirdEyeLayers[i] = evt.getModelRegistry().get(prefix("icon/third_eye_" + i));
		}
		pyroclastGem = evt.getModelRegistry().get(prefix("icon/lava_pendant_gem"));
		crimsonGem = evt.getModelRegistry().get(prefix("icon/super_lava_pendant_gem"));
		itemFinderGem = evt.getModelRegistry().get(prefix("icon/itemfinder_gem"));

		cirrusGem = evt.getModelRegistry().get(prefix("icon/cloud_pendant_gem"));
		nimbusGem = evt.getModelRegistry().get(prefix("icon/super_cloud_pendant_gem"));
		snowflakePendantGem = evt.getModelRegistry().get(prefix("icon/ice_pendant_gem"));
		for (int i = 0; i < tiaraWingIcons.length; i++) {
			tiaraWingIcons[i] = evt.getModelRegistry().get(prefix("icon/tiara_wing_" + (i + 1)));
		}
	}

	public void onTextureStitchPre(TextureStitchEvent.Pre evt) {
		if (!evt.getMap().getId().equals(SpriteAtlasTexture.BLOCK_ATLAS_TEX)) {
			return;
		}

		evt.addSprite(prefix("block/alfheim_portal_swirl"));
		evt.addSprite(prefix("block/alfheim_portal_swirl"));
		evt.addSprite(prefix("block/alchemy_catalyst_overlay"));
		evt.addSprite(prefix("block/conjuration_catalyst_overlay"));
		evt.addSprite(prefix("block/enchanter_overlay"));
		evt.addSprite(prefix("block/mana_void_overlay"));
		evt.addSprite(prefix("block/mana_water"));
		evt.addSprite(prefix("block/terra_plate_overlay"));
		evt.addSprite(prefix("item/corporea_spark_star"));
		evt.addSprite(prefix("item/spark"));

		for (SparkUpgradeType type : SparkUpgradeType.values()) {
			if (type != SparkUpgradeType.NONE) {
				evt.addSprite(prefix("item/spark_upgrade_rune_" + type.name().toLowerCase(Locale.ROOT)));
			}
		}

		evt.addSprite(prefix("item/special_tail"));

		/*
		evt.addSprite(prefix("item/triggers/mana_detector"));
		evt.addSprite(prefix("item/triggers/rune_altar_can_craft"));
		
		for (TriggerManaLevel.State s : TriggerManaLevel.State.values()) {
			register(evt.getMap(), "item/triggers/mana" + WordUtils.capitalizeFully(s.name()));
		}
		*/
	}

	public void onTextureStitchPost(TextureStitchEvent.Post evt) {
		if (!evt.getMap().getId().equals(SpriteAtlasTexture.BLOCK_ATLAS_TEX)) {
			return;
		}

		alfPortalTex = get(evt.getMap(), "block/alfheim_portal_swirl");
		lightRelayWorldIcon = get(evt.getMap(), "block/light_relay");
		lightRelayDetectorWorldIcon = get(evt.getMap(), "block/detector_light_relay");
		lightRelayForkWorldIcon = get(evt.getMap(), "block/fork_light_relay");
		lightRelayToggleWorldIcon = get(evt.getMap(), "block/toggle_light_relay");
		alchemyCatalystOverlay = get(evt.getMap(), "block/alchemy_catalyst_overlay");
		conjurationCatalystOverlay = get(evt.getMap(), "block/conjuration_catalyst_overlay");
		enchanterOverlay = get(evt.getMap(), "block/enchanter_overlay");
		manaVoidOverlay = get(evt.getMap(), "block/mana_void_overlay");
		manaWater = get(evt.getMap(), "block/mana_water");
		terraPlateOverlay = get(evt.getMap(), "block/terra_plate_overlay");
		corporeaWorldIcon = get(evt.getMap(), "item/corporea_spark");
		corporeaWorldIconMaster = get(evt.getMap(), "item/corporea_spark_master");
		corporeaIconStar = get(evt.getMap(), "item/corporea_spark_star");
		sparkWorldIcon = get(evt.getMap(), "item/spark");

		for (SparkUpgradeType type : SparkUpgradeType.values()) {
			if (type != SparkUpgradeType.NONE) {
				sparkUpgradeIcons[type.ordinal() - 1] = get(evt.getMap(), "item/spark_upgrade_rune_" + type.name().toLowerCase(Locale.ROOT));
			}
		}

		/*
		manaDetectorIcon = get(evt.getMap(), "item/triggers/mana_detector");
		runeAltarTriggerIcon = get(evt.getMap(), "item/triggers/rune_altar_can_craft");
		
		for (TriggerManaLevel.State s : TriggerManaLevel.State.values()) {
			manaLevelTriggerIcons.put(s, get(evt.getMap(), "item/triggers/mana" + WordUtils.capitalizeFully(s.name())));
		}
		*/
	}

	private Sprite get(SpriteAtlasTexture map, String name) {
		Sprite ret = map.getSprite(prefix(name));
		if (ret == map.getSprite(MissingSprite.getMissingSpriteId())) {
			Botania.LOGGER.error("Missing texture for {}", name);
		}
		return ret;
	}

	private MiscellaneousIcons() {}
}
