/**
 * This class was created by <williewillus>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.core.handler;

import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.commons.lang3.text.WordUtils;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.client.model.FloatingFlowerModel;
import vazkii.botania.client.model.GunModel;
import vazkii.botania.client.model.LexiconModel;
import vazkii.botania.client.model.PlatformModel;
import vazkii.botania.common.item.equipment.bauble.ItemFlightTiara;
import vazkii.botania.common.item.relic.ItemKingKey;
import vazkii.botania.common.lib.LibMisc;

import java.util.EnumMap;
import java.util.Map;

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
		// Floating flowers
		FloatingFlowerModel model = new FloatingFlowerModel();
		evt.getModelRegistry().put(new ModelResourceLocation("botania:miniIsland", "normal"), model);
		evt.getModelRegistry().put(new ModelResourceLocation("botania:miniIsland", "inventory"), model);
		evt.getModelRegistry().put(new ModelResourceLocation("botania:floatingSpecialFlower", "normal"), model);
		evt.getModelRegistry().put(new ModelResourceLocation("botania:floatingSpecialFlower", "inventory"), model);

		// Platforms
		ModelResourceLocation abstruseName = new ModelResourceLocation("botania:abstruse_platform", "normal");
		IBakedModel abstruse = evt.getModelRegistry().get(abstruseName);
		ModelResourceLocation spectralName = new ModelResourceLocation("botania:spectral_platform", "normal");
		IBakedModel spectral = evt.getModelRegistry().get(spectralName);
		ModelResourceLocation infrangibleName = new ModelResourceLocation("botania:infrangible_platform", "normal");
		IBakedModel infrangible = evt.getModelRegistry().get(infrangibleName);

		evt.getModelRegistry().put(abstruseName, new PlatformModel(abstruse));
		evt.getModelRegistry().put(spectralName, new PlatformModel(spectral));
		evt.getModelRegistry().put(infrangibleName, new PlatformModel(infrangible));

		// Lexicon
		IBakedModel original = evt.getModelRegistry().get(new ModelResourceLocation("botania:lexicon", "inventory"));
		evt.getModelRegistry().put(new ModelResourceLocation("botania:lexicon", "inventory"),
				new LexiconModel(original));
		original = evt.getModelRegistry().get(new ModelResourceLocation("botania:lexicon_elven", "inventory"));
		evt.getModelRegistry().put(new ModelResourceLocation("botania:lexicon_elven", "inventory"),
				new LexiconModel(original));

		// Mana Blaster
		ModelResourceLocation key = new ModelResourceLocation("botania:managun", "inventory");
		IBakedModel originalModel = evt.getModelRegistry().get(key);
		evt.getModelRegistry().put(key, new GunModel(originalModel));

		key = new ModelResourceLocation("botania:managunclip", "inventory");
		originalModel = evt.getModelRegistry().get(key);
		evt.getModelRegistry().put(key, new GunModel(originalModel));
	}
	
	@SubscribeEvent
	public void onTextureStitchPre(TextureStitchEvent.Pre evt) {
		register(evt.getMap(), "blocks/alfheim_portal_swirl");
		register(evt.getMap(), "blocks/alfheim_portal_swirl");
		register(evt.getMap(), "blocks/luminizer");
		register(evt.getMap(), "blocks/luminizer_detector");
		register(evt.getMap(), "blocks/luminizer_fork");
		register(evt.getMap(), "blocks/luminizer_toggle");
		register(evt.getMap(), "blocks/catalyst_alchemy_overlay");
		register(evt.getMap(), "blocks/catalyst_conjuration_overlay");
		register(evt.getMap(), "blocks/enchanter_overlay");
		register(evt.getMap(), "blocks/mana_void_overlay");
		register(evt.getMap(), "blocks/mana_water");
		register(evt.getMap(), "blocks/terra_plate_overlay");
		register(evt.getMap(), "items/spark_corporea");
		register(evt.getMap(), "items/spark_corporea_master");
		register(evt.getMap(), "items/spark_corporea_star");
		register(evt.getMap(), "items/spark");

		for(int i = 0; i < 4; i++) {
			register(evt.getMap(), "items/spark_upgrade_rune_" + i);
		}

		register(evt.getMap(), "items/special_tail");
		register(evt.getMap(), "items/special_phiflower");
		register(evt.getMap(), "items/special_goldfish");
		register(evt.getMap(), "items/special_nerfbat");

		for(int i = 0; i < ItemKingKey.WEAPON_TYPES; i++)
			register(evt.getMap(), "items/gate_weapon_" + i);

		for(int i = 0; i < 3; i++)
			register(evt.getMap(), "items/third_eye_" + i);

		register(evt.getMap(), "items/triggers/manaDetector");
		register(evt.getMap(), "items/triggers/runeAltarCanCraft");

		/*
		for (TriggerManaLevel.State s : TriggerManaLevel.State.values()) {
			register(evt.getMap(), "items/triggers/mana" + WordUtils.capitalizeFully(s.name()));
		}
		*/

		for (int i = 0; i < tiaraWingIcons.length; i++) {
			register(evt.getMap(), "items/headpiece_tiara_wing_" + (i + 1));
		}

		register(evt.getMap(), "items/willFlame");

		register(evt.getMap(), "items/pendant_blood_chain");
		register(evt.getMap(), "items/pendant_blood_gem");
		register(evt.getMap(), "items/pendant_ice_gem");
		register(evt.getMap(), "items/headpiece_item_finder_gem");
		register(evt.getMap(), "items/pendant_lava_gem");
		register(evt.getMap(), "items/pendant_lava_super_gem");
		register(evt.getMap(), "items/pendant_cloud_gem");
		register(evt.getMap(), "items/pendant_cloud_super_gem");
	}

	@SubscribeEvent
	public void onTextureStitchPost(TextureStitchEvent.Post evt) {
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

		manaDetectorIcon = get(evt.getMap(), "items/triggers/manaDetector");
		runeAltarTriggerIcon = get(evt.getMap(), "items/triggers/runeAltarCanCraft");

		/*
		for (TriggerManaLevel.State s : TriggerManaLevel.State.values()) {
			manaLevelTriggerIcons.put(s, get(evt.getMap(), "items/triggers/mana" + WordUtils.capitalizeFully(s.name())));
		}
		*/

		for (int i = 0; i < tiaraWingIcons.length; i++) {
			tiaraWingIcons[i] = get(evt.getMap(), "items/headpiece_tiara_wing_" + (i + 1));
		}

		terrasteelHelmWillIcon = get(evt.getMap(), "items/willFlame");

		bloodPendantChain = get(evt.getMap(), "items/pendant_blood_chain");
		bloodPendantGem = get(evt.getMap(), "items/pendant_blood_gem");
		snowflakePendantGem = get(evt.getMap(), "items/pendant_ice_gem");
		itemFinderGem = get(evt.getMap(), "items/headpiece_item_finder_gem");
		pyroclastGem = get(evt.getMap(), "items/pendant_lava_gem");
		crimsonGem = get(evt.getMap(), "items/pendant_lava_super_gem");
		cirrusGem = get(evt.getMap(), "items/pendant_cloud_gem");
		nimbusGem = get(evt.getMap(), "items/pendant_cloud_super_gem");
	}
	
	private void register(TextureMap map, String name) {
		map.registerSprite(null, new ResourceLocation(LibMisc.MOD_ID, name));
	}
	
	private TextureAtlasSprite get(TextureMap map, String name) {
		return map.getSprite(new ResourceLocation(LibMisc.MOD_ID, name));
	}

	private MiscellaneousIcons() {}
}
