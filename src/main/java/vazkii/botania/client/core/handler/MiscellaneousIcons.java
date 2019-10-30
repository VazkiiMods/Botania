/**
 * This class was created by <williewillus>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.core.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import vazkii.botania.client.model.GunModel;
import vazkii.botania.client.model.LexiconModel;
import vazkii.botania.client.model.PlatformModel;
import vazkii.botania.client.render.tile.RenderTileCorporeaCrystalCube;
import vazkii.botania.common.item.equipment.bauble.ItemFlightTiara;
import vazkii.botania.common.item.relic.ItemKingKey;
import vazkii.botania.common.lib.LibMisc;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class MiscellaneousIcons {

	public static final MiscellaneousIcons INSTANCE = new MiscellaneousIcons();

	public TextureAtlasSprite
	alfPortalTex,
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
	runeAltarTriggerIcon,
	terrasteelHelmWillIcon;

	public final TextureAtlasSprite[] sparkUpgradeIcons = new TextureAtlasSprite[4];
	public final TextureAtlasSprite[] kingKeyWeaponIcons = new TextureAtlasSprite[ItemKingKey.WEAPON_TYPES];
	// public final Map<TriggerManaLevel.State, TextureAtlasSprite> manaLevelTriggerIcons = new EnumMap<>(TriggerManaLevel.State.class);
	public final TextureAtlasSprite[] tiaraWingIcons = new TextureAtlasSprite[ItemFlightTiara.WING_TYPES];
	public final TextureAtlasSprite[] thirdEyeLayers = new TextureAtlasSprite[3];

	// begin dank_memes
	public TextureAtlasSprite tailIcon = null;
	public TextureAtlasSprite phiFlowerIcon = null;
	public TextureAtlasSprite goldfishIcon = null;
	public TextureAtlasSprite nerfBatIcon = null;
	// end dank_memes

	// Icons for baubles that don't render their own model on the player
	public TextureAtlasSprite
	bloodPendantChain,
	bloodPendantGem,
	snowflakePendantGem,
	itemFinderGem,
	pyroclastGem,
	crimsonGem,
	cirrusGem,
	nimbusGem;

	@SubscribeEvent
	public void onModelBake(ModelBakeEvent evt) {
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

		RenderTileCorporeaCrystalCube.cubeModel = evt.getModelRegistry().get(new ResourceLocation(LibMisc.MOD_ID, "block/corporea_crystal_cube_glass"));
	}
	
	@SubscribeEvent
	public void onTextureStitchPre(TextureStitchEvent.Pre evt) {
		if(evt.getMap() != Minecraft.getInstance().getTextureMap())
			return;

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

		for(int i = 0; i < 4; i++) {
			evt.addSprite(prefix("items/spark_upgrade_rune_" + i));
		}

		evt.addSprite(prefix("items/special_tail"));
		evt.addSprite(prefix("items/special_phiflower"));
		evt.addSprite(prefix("items/special_goldfish"));
		evt.addSprite(prefix("items/special_nerfbat"));

		for(int i = 0; i < ItemKingKey.WEAPON_TYPES; i++)
			evt.addSprite(prefix("items/gate_weapon_" + i));

		for(int i = 0; i < 3; i++)
			evt.addSprite(prefix("items/third_eye_" + i));

		evt.addSprite(prefix("items/triggers/mana_detector"));
		evt.addSprite(prefix("items/triggers/rune_altar_can_craft"));

		/*
		for (TriggerManaLevel.State s : TriggerManaLevel.State.values()) {
			register(evt.getMap(), "items/triggers/mana" + WordUtils.capitalizeFully(s.name()));
		}
		*/

		for (int i = 0; i < tiaraWingIcons.length; i++) {
			evt.addSprite(prefix("items/headpiece_tiara_wing_" + (i + 1)));
		}

		evt.addSprite(prefix("items/will_flame"));

		evt.addSprite(prefix("items/pendant_blood_chain"));
		evt.addSprite(prefix("items/pendant_blood_gem"));
		evt.addSprite(prefix("items/pendant_ice_gem"));
		evt.addSprite(prefix("items/headpiece_item_finder_gem"));
		evt.addSprite(prefix("items/pendant_lava_gem"));
		evt.addSprite(prefix("items/pendant_lava_super_gem"));
		evt.addSprite(prefix("items/pendant_cloud_gem"));
		evt.addSprite(prefix("items/pendant_cloud_super_gem"));
	}

	@SubscribeEvent
	public void onTextureStitchPost(TextureStitchEvent.Post evt) {
		if(evt.getMap() != Minecraft.getInstance().getTextureMap())
			return;

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

		for(int i = 0; i < 4; i++) {
			sparkUpgradeIcons[i] = get(evt.getMap(), "items/spark_upgrade_rune_" + i);
		}

		tailIcon = get(evt.getMap(), "items/special_tail");
		phiFlowerIcon = get(evt.getMap(), "items/special_phiflower");
		goldfishIcon = get(evt.getMap(), "items/special_goldfish");
		nerfBatIcon = get(evt.getMap(), "items/special_nerfbat");

		for(int i = 0; i < ItemKingKey.WEAPON_TYPES; i++)
			kingKeyWeaponIcons[i] = get(evt.getMap(), "items/gate_weapon_" + i);
		
		for(int i = 0; i < 3; i++)
			thirdEyeLayers[i] = get(evt.getMap(), "items/third_eye_" + i);

		manaDetectorIcon = get(evt.getMap(), "items/triggers/mana_detector");
		runeAltarTriggerIcon = get(evt.getMap(), "items/triggers/rune_altar_can_craft");

		/*
		for (TriggerManaLevel.State s : TriggerManaLevel.State.values()) {
			manaLevelTriggerIcons.put(s, get(evt.getMap(), "items/triggers/mana" + WordUtils.capitalizeFully(s.name())));
		}
		*/

		for (int i = 0; i < tiaraWingIcons.length; i++) {
			tiaraWingIcons[i] = get(evt.getMap(), "items/headpiece_tiara_wing_" + (i + 1));
		}

		terrasteelHelmWillIcon = get(evt.getMap(), "items/will_flame");

		bloodPendantChain = get(evt.getMap(), "items/pendant_blood_chain");
		bloodPendantGem = get(evt.getMap(), "items/pendant_blood_gem");
		snowflakePendantGem = get(evt.getMap(), "items/pendant_ice_gem");
		itemFinderGem = get(evt.getMap(), "items/headpiece_item_finder_gem");
		pyroclastGem = get(evt.getMap(), "items/pendant_lava_gem");
		crimsonGem = get(evt.getMap(), "items/pendant_lava_super_gem");
		cirrusGem = get(evt.getMap(), "items/pendant_cloud_gem");
		nimbusGem = get(evt.getMap(), "items/pendant_cloud_super_gem");
	}

	private TextureAtlasSprite get(AtlasTexture map, String name) {
		return map.getSprite(new ResourceLocation(LibMisc.MOD_ID, name));
	}

	private MiscellaneousIcons() {}
}
