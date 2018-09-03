/**
 * This class was created by <williewillus>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.core.handler;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.lang3.text.WordUtils;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.client.model.FloatingFlowerModel;
import vazkii.botania.client.model.GunModel;
import vazkii.botania.client.model.LexiconModel;
import vazkii.botania.client.model.PlatformModel;
import vazkii.botania.common.integration.buildcraft.TriggerManaLevel;
import vazkii.botania.common.item.ItemSparkUpgrade;
import vazkii.botania.common.item.equipment.bauble.ItemFlightTiara;
import vazkii.botania.common.item.relic.ItemKingKey;

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

	public TextureAtlasSprite[] sparkUpgradeIcons;
	public TextureAtlasSprite[] kingKeyWeaponIcons;
	public final Map<TriggerManaLevel.State, TextureAtlasSprite> manaLevelTriggerIcons = new EnumMap<>(TriggerManaLevel.State.class);
	public TextureAtlasSprite[] tiaraWingIcons;
	public TextureAtlasSprite[] thirdEyeLayers;

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
		evt.getModelRegistry().putObject(new ModelResourceLocation("botania:miniIsland", "normal"), model);
		evt.getModelRegistry().putObject(new ModelResourceLocation("botania:miniIsland", "inventory"), model);
		evt.getModelRegistry().putObject(new ModelResourceLocation("botania:floatingSpecialFlower", "normal"), model);
		evt.getModelRegistry().putObject(new ModelResourceLocation("botania:floatingSpecialFlower", "inventory"), model);

		// Platforms
		evt.getModelRegistry().putObject(new ModelResourceLocation("botania:platform", "normal"), new PlatformModel());

		// Lexicon
		evt.getModelRegistry().putObject(new ModelResourceLocation("botania:lexicon_3d_default", "inventory"),
				new LexiconModel(new ModelResourceLocation("botania:lexicon_default", "inventory")));
		evt.getModelRegistry().putObject(new ModelResourceLocation("botania:lexicon_3d_elven", "inventory"),
				new LexiconModel(new ModelResourceLocation("botania:lexicon_elven", "inventory")));

		// Mana Blaster
		ModelResourceLocation key = new ModelResourceLocation("botania:managun", "inventory");
		IBakedModel originalModel = evt.getModelRegistry().getObject(key);
		evt.getModelRegistry().putObject(key, new GunModel(originalModel));

		key = new ModelResourceLocation("botania:managunclip", "inventory");
		originalModel = evt.getModelRegistry().getObject(key);
		evt.getModelRegistry().putObject(key, new GunModel(originalModel));
	}

	@SubscribeEvent
	public void onTextureStitch(TextureStitchEvent.Pre evt) {
		alfPortalTex = IconHelper.forName(evt.getMap(), "alfheim_portal_swirl", "blocks");
		lightRelayWorldIcon = IconHelper.forName(evt.getMap(), "luminizer", "blocks");
		lightRelayWorldIconRed = IconHelper.forName(evt.getMap(), "luminizer_detector", "blocks");
		lightRelayWorldIconGreen = IconHelper.forName(evt.getMap(), "luminizer_fork", "blocks");
		lightRelayWorldIconPurple = IconHelper.forName(evt.getMap(), "luminizer_toggle", "blocks");
		alchemyCatalystOverlay = IconHelper.forName(evt.getMap(), "catalyst_alchemy_overlay", "blocks");
		conjurationCatalystOverlay = IconHelper.forName(evt.getMap(), "catalyst_conjuration_overlay", "blocks");
		enchanterOverlay = IconHelper.forName(evt.getMap(), "enchanter_overlay", "blocks");
		manaVoidOverlay = IconHelper.forName(evt.getMap(), "mana_void_overlay", "blocks");
		manaWater = IconHelper.forName(evt.getMap(), "mana_water", "blocks");
		terraPlateOverlay = IconHelper.forName(evt.getMap(), "terra_plate_overlay", "blocks");
		corporeaWorldIcon = IconHelper.forName(evt.getMap(), "spark_corporea", "items");
		corporeaWorldIconMaster = IconHelper.forName(evt.getMap(), "spark_corporea_master", "items");
		corporeaIconStar = IconHelper.forName(evt.getMap(), "spark_corporea_star", "items");
		sparkWorldIcon = IconHelper.forName(evt.getMap(), "spark", "items");

		sparkUpgradeIcons = new TextureAtlasSprite[ItemSparkUpgrade.VARIANTS];
		for(int i = 0; i < ItemSparkUpgrade.VARIANTS; i++) {
			sparkUpgradeIcons[i] = IconHelper.forName(evt.getMap(), "spark_upgrade_rune_" + i, "items");
		}

		tailIcon = IconHelper.forName(evt.getMap(), "special_tail", "items");
		phiFlowerIcon = IconHelper.forName(evt.getMap(), "special_phiflower", "items");
		goldfishIcon = IconHelper.forName(evt.getMap(), "special_goldfish", "items");
		nerfBatIcon = IconHelper.forName(evt.getMap(), "special_nerfbat", "items");

		kingKeyWeaponIcons = new TextureAtlasSprite[ItemKingKey.WEAPON_TYPES];
		for(int i = 0; i < ItemKingKey.WEAPON_TYPES; i++)
			kingKeyWeaponIcons[i] = IconHelper.forName(evt.getMap(), "gate_weapon_" + i, "items");
		
		thirdEyeLayers = new TextureAtlasSprite[3];
		for(int i = 0; i < 3; i++)
			thirdEyeLayers[i] = IconHelper.forName(evt.getMap(), "third_eye_" + i, "items");

		manaDetectorIcon = IconHelper.forName(evt.getMap(), "triggers/manaDetector", "items");
		runeAltarTriggerIcon = IconHelper.forName(evt.getMap(), "triggers/runeAltarCanCraft", "items");

		for (TriggerManaLevel.State s : TriggerManaLevel.State.values()) {
			manaLevelTriggerIcons.put(s, IconHelper.forName(evt.getMap(), "triggers/mana" + WordUtils.capitalizeFully(s.name()), "items"));
		}

		tiaraWingIcons = new TextureAtlasSprite[ItemFlightTiara.WING_TYPES];
		for (int i = 0; i < tiaraWingIcons.length; i++) {
			tiaraWingIcons[i] = IconHelper.forName(evt.getMap(), "headpiece_tiara_wing_" + (i + 1), "items");
		}

		terrasteelHelmWillIcon = IconHelper.forName(evt.getMap(), "willFlame", "items");

		bloodPendantChain = IconHelper.forName(evt.getMap(), "pendant_blood_chain", "items");
		bloodPendantGem = IconHelper.forName(evt.getMap(), "pendant_blood_gem", "items");
		snowflakePendantGem = IconHelper.forName(evt.getMap(), "pendant_ice_gem", "items");
		itemFinderGem = IconHelper.forName(evt.getMap(), "headpiece_item_finder_gem", "items");
		pyroclastGem = IconHelper.forName(evt.getMap(), "pendant_lava_gem", "items");
		crimsonGem = IconHelper.forName(evt.getMap(), "pendant_lava_super_gem", "items");
		cirrusGem = IconHelper.forName(evt.getMap(), "pendant_cloud_gem", "items");
		nimbusGem = IconHelper.forName(evt.getMap(), "pendant_cloud_super_gem", "items");
	}

	private MiscellaneousIcons() {}
}
