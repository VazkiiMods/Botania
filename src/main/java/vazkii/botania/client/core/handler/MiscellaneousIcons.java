/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.core.handler;

import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.Material;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.tileentity.BannerPattern;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import vazkii.botania.client.model.GunModel;
import vazkii.botania.client.model.LexiconModel;
import vazkii.botania.client.model.PlatformModel;
import vazkii.botania.client.render.tile.RenderTileCorporeaCrystalCube;
import vazkii.botania.client.render.tile.RenderTilePump;
import vazkii.botania.common.Botania;
import vazkii.botania.common.item.equipment.bauble.ItemFlightTiara;
import vazkii.botania.common.item.relic.ItemKingKey;
import vazkii.botania.common.lib.LibMisc;
import vazkii.botania.common.lib.LibObfuscation;

import java.lang.invoke.MethodHandle;
import java.util.Set;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class MiscellaneousIcons {
	private static final MethodHandle MATERIALS = LibObfuscation.getGetter(ModelBakery.class, "field_177602_b");
	public static final MiscellaneousIcons INSTANCE = new MiscellaneousIcons();

	public TextureAtlasSprite alfPortalTex,
			lightRelayWorldIcon,
			lightRelayWorldIconRed,
			lightRelayWorldIconGreen,
			lightRelayWorldIconPurple,
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

	public final TextureAtlasSprite[] sparkUpgradeIcons = new TextureAtlasSprite[4];
	// public final Map<TriggerManaLevel.State, TextureAtlasSprite> manaLevelTriggerIcons = new EnumMap<>(TriggerManaLevel.State.class);
	public final IBakedModel[] tiaraWingIcons = new IBakedModel[ItemFlightTiara.WING_TYPES];
	public final IBakedModel[] thirdEyeLayers = new IBakedModel[3];

	public TextureAtlasSprite tailIcon = null;

	public IBakedModel goldfishModel,
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

	public final IBakedModel[] kingKeyWeaponModels = new IBakedModel[ItemKingKey.WEAPON_TYPES];

	@SubscribeEvent
	public void onModelRegister(ModelRegistryEvent evt) throws Throwable {
		Set<Material> materials = (Set<Material>) MATERIALS.invokeExact();
		materials.add(RenderLexicon.TEXTURE);
		materials.add(RenderLexicon.ELVEN_TEXTURE);
		for (BannerPattern pattern : BannerPattern.values()) {
			if (pattern.getFileName().startsWith(LibMisc.MOD_ID)) {
				materials.add(new Material(Atlases.SHIELD_ATLAS, pattern.func_226957_a_(false)));
				materials.add(new Material(Atlases.BANNER_ATLAS, pattern.func_226957_a_(true)));
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

	@SubscribeEvent
	public void onModelBake(ModelBakeEvent evt) {
		if (!ModelHandler.registeredModels) {
			Botania.LOGGER.error("Additional models failed to register! Aborting baking models to avoid early crashing.");
			return;
		}
		// Platforms
		ModelResourceLocation abstruseName = new ModelResourceLocation("botania:abstruse_platform", "");
		IBakedModel abstruse = evt.getModelRegistry().get(abstruseName);
		ModelResourceLocation spectralName = new ModelResourceLocation("botania:spectral_platform", "");
		IBakedModel spectral = evt.getModelRegistry().get(spectralName);
		ModelResourceLocation infrangibleName = new ModelResourceLocation("botania:infrangible_platform", "");
		IBakedModel infrangible = evt.getModelRegistry().get(infrangibleName);

		evt.getModelRegistry().put(abstruseName, new PlatformModel(abstruse));
		evt.getModelRegistry().put(spectralName, new PlatformModel(spectral));
		evt.getModelRegistry().put(infrangibleName, new PlatformModel(infrangible));

		// Lexicon
		IBakedModel original = evt.getModelRegistry().get(new ModelResourceLocation("botania:lexicon", "inventory"));
		evt.getModelRegistry().put(new ModelResourceLocation("botania:lexicon", "inventory"),
				new LexiconModel(original));

		// models referenced using json overrides aren't put in the model registry, so just go through all override models and wrap them there
		for (int i = 0; i < original.getOverrides().overrideBakedModels.size(); i++) {
			original.getOverrides().overrideBakedModels.set(i, new LexiconModel(original.getOverrides().overrideBakedModels.get(i)));
		}

		// Mana Blaster
		ModelResourceLocation key = new ModelResourceLocation("botania:mana_gun", "inventory");
		IBakedModel originalModel = evt.getModelRegistry().get(key);
		ModelResourceLocation clipKey = new ModelResourceLocation("botania:mana_gun_clip", "inventory");
		IBakedModel originalModelClip = evt.getModelRegistry().get(clipKey);
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

	@SubscribeEvent
	public void onTextureStitchPre(TextureStitchEvent.Pre evt) {
		if (!evt.getMap().getTextureLocation().equals(AtlasTexture.LOCATION_BLOCKS_TEXTURE)) {
			return;
		}

		evt.addSprite(prefix("blocks/alfheim_portal_swirl"));
		evt.addSprite(prefix("blocks/alfheim_portal_swirl"));
		evt.addSprite(prefix("blocks/luminizer"));
		evt.addSprite(prefix("blocks/luminizer_detector"));
		evt.addSprite(prefix("blocks/luminizer_fork"));
		evt.addSprite(prefix("blocks/luminizer_toggle"));
		evt.addSprite(prefix("blocks/catalyst_alchemy_overlay"));
		evt.addSprite(prefix("blocks/catalyst_conjuration_overlay"));
		evt.addSprite(prefix("blocks/enchanter_overlay"));
		evt.addSprite(prefix("blocks/mana_void_overlay"));
		evt.addSprite(prefix("blocks/mana_water"));
		evt.addSprite(prefix("blocks/terra_plate_overlay"));
		evt.addSprite(prefix("items/spark_corporea"));
		evt.addSprite(prefix("items/spark_corporea_master"));
		evt.addSprite(prefix("items/spark_corporea_star"));
		evt.addSprite(prefix("items/spark"));

		for (int i = 0; i < 4; i++) {
			evt.addSprite(prefix("items/spark_upgrade_rune_" + i));
		}

		evt.addSprite(prefix("items/special_tail"));

		/*
		evt.addSprite(prefix("items/triggers/mana_detector"));
		evt.addSprite(prefix("items/triggers/rune_altar_can_craft"));
		
		for (TriggerManaLevel.State s : TriggerManaLevel.State.values()) {
			register(evt.getMap(), "items/triggers/mana" + WordUtils.capitalizeFully(s.name()));
		}
		*/
	}

	@SubscribeEvent
	public void onTextureStitchPost(TextureStitchEvent.Post evt) {
		if (!evt.getMap().getTextureLocation().equals(AtlasTexture.LOCATION_BLOCKS_TEXTURE)) {
			return;
		}

		alfPortalTex = get(evt.getMap(), "blocks/alfheim_portal_swirl");
		lightRelayWorldIcon = get(evt.getMap(), "blocks/luminizer");
		lightRelayWorldIconRed = get(evt.getMap(), "blocks/luminizer_detector");
		lightRelayWorldIconGreen = get(evt.getMap(), "blocks/luminizer_fork");
		lightRelayWorldIconPurple = get(evt.getMap(), "blocks/luminizer_toggle");
		alchemyCatalystOverlay = get(evt.getMap(), "blocks/catalyst_alchemy_overlay");
		conjurationCatalystOverlay = get(evt.getMap(), "blocks/catalyst_conjuration_overlay");
		enchanterOverlay = get(evt.getMap(), "blocks/enchanter_overlay");
		manaVoidOverlay = get(evt.getMap(), "blocks/mana_void_overlay");
		manaWater = get(evt.getMap(), "blocks/mana_water");
		terraPlateOverlay = get(evt.getMap(), "blocks/terra_plate_overlay");
		corporeaWorldIcon = get(evt.getMap(), "items/spark_corporea");
		corporeaWorldIconMaster = get(evt.getMap(), "items/spark_corporea_master");
		corporeaIconStar = get(evt.getMap(), "items/spark_corporea_star");
		sparkWorldIcon = get(evt.getMap(), "items/spark");

		for (int i = 0; i < 4; i++) {
			sparkUpgradeIcons[i] = get(evt.getMap(), "items/spark_upgrade_rune_" + i);
		}

		tailIcon = get(evt.getMap(), "items/special_tail");

		/*
		manaDetectorIcon = get(evt.getMap(), "items/triggers/mana_detector");
		runeAltarTriggerIcon = get(evt.getMap(), "items/triggers/rune_altar_can_craft");
		
		for (TriggerManaLevel.State s : TriggerManaLevel.State.values()) {
			manaLevelTriggerIcons.put(s, get(evt.getMap(), "items/triggers/mana" + WordUtils.capitalizeFully(s.name())));
		}
		*/
	}

	private TextureAtlasSprite get(AtlasTexture map, String name) {
		return map.getSprite(prefix(name));
	}

	private MiscellaneousIcons() {}
}
