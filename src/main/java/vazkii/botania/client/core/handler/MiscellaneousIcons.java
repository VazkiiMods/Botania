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
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.client.model.FloatingFlowerModel;
import vazkii.botania.client.model.GunModel;
import vazkii.botania.client.model.LexiconModel;
import vazkii.botania.client.model.PlatformModel;
import vazkii.botania.common.item.ItemSparkUpgrade;
import vazkii.botania.common.item.equipment.bauble.ItemFlightTiara;
import vazkii.botania.common.item.relic.ItemKingKey;
import vazkii.botania.common.lib.LibMisc;

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
	// todo buildcraft public final Map<TriggerManaLevel.State, TextureAtlasSprite> manaLevelTriggerIcons = Maps.newEnumMap(TriggerManaLevel.State.class);
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
		ModelResourceLocation key = new ModelResourceLocation("botania:manaGun", "inventory");
		IBakedModel originalModel = evt.getModelRegistry().getObject(key);
		evt.getModelRegistry().putObject(key, new GunModel(originalModel));

		key = new ModelResourceLocation("botania:manaGunClip", "inventory");
		originalModel = evt.getModelRegistry().getObject(key);
		evt.getModelRegistry().putObject(key, new GunModel(originalModel));
	}

	@SubscribeEvent
	public void onTextureStitch(TextureStitchEvent.Pre evt) {
		alfPortalTex = IconHelper.forName(evt.getMap(), "alfheimPortalInside", "blocks");
		lightRelayWorldIcon = IconHelper.forName(evt.getMap(), "lightRelay1", "blocks");
		lightRelayWorldIconRed = IconHelper.forName(evt.getMap(), "lightRelay3", "blocks");
		lightRelayWorldIconGreen = IconHelper.forName(evt.getMap(), "lightRelay5", "blocks");
		lightRelayWorldIconPurple = IconHelper.forName(evt.getMap(), "lightRelay7", "blocks");
		alchemyCatalystOverlay = IconHelper.forName(evt.getMap(), "alchemyCatalyst3", "blocks");
		conjurationCatalystOverlay = IconHelper.forName(evt.getMap(), "conjurationCatalyst3", "blocks");
		enchanterOverlay = IconHelper.forName(evt.getMap(), "enchanterOverlay", "blocks");
		manaVoidOverlay = IconHelper.forName(evt.getMap(), "manaVoid1", "blocks");
		manaWater = IconHelper.forName(evt.getMap(), "mana_water", "blocks");
		terraPlateOverlay = IconHelper.forName(evt.getMap(), "terraPlateOverlay", "blocks");
		corporeaWorldIcon = IconHelper.forName(evt.getMap(), "corporeaSpark1", "items");
		corporeaWorldIconMaster = IconHelper.forName(evt.getMap(), "corporeaSpark3", "items");
		corporeaIconStar = IconHelper.forName(evt.getMap(), "corporeaSparkStar", "items");
		sparkWorldIcon = IconHelper.forName(evt.getMap(), "spark1", "items");

		sparkUpgradeIcons = new TextureAtlasSprite[ItemSparkUpgrade.VARIANTS];
		for(int i = 0; i < ItemSparkUpgrade.VARIANTS; i++) {
			sparkUpgradeIcons[i] = IconHelper.forName(evt.getMap(), "sparkUpgradeL" + i, "items");
		}

		tailIcon = IconHelper.forName(evt.getMap(), "tail", "items");
		phiFlowerIcon = IconHelper.forName(evt.getMap(), "phiFlower", "items");
		goldfishIcon = IconHelper.forName(evt.getMap(), "goldfish", "items");
		nerfBatIcon = IconHelper.forName(evt.getMap(), "special_nerfbat", "items");

		kingKeyWeaponIcons = new TextureAtlasSprite[ItemKingKey.WEAPON_TYPES];
		for(int i = 0; i < ItemKingKey.WEAPON_TYPES; i++)
			kingKeyWeaponIcons[i] = IconHelper.forName(evt.getMap(), "gateWeapon" + i, "items");
		
		thirdEyeLayers = new TextureAtlasSprite[3];
		for(int i = 0; i < 3; i++)
			thirdEyeLayers[i] = IconHelper.forName(evt.getMap(), "thirdEye" + i, "items");

		manaDetectorIcon = IconHelper.forName(evt.getMap(), "triggers/manaDetector", "items");
		runeAltarTriggerIcon = IconHelper.forName(evt.getMap(), "triggers/runeAltarCanCraft", "items");

		/*for (TriggerManaLevel.State s : TriggerManaLevel.State.values()) { todo buildcraft
			manaLevelTriggerIcons.put(s, IconHelper.forName(evt.getMap(), "triggers/mana" + WordUtils.capitalizeFully(s.name()), "items"));
		}*/

		tiaraWingIcons = new TextureAtlasSprite[ItemFlightTiara.WING_TYPES];
		for (int i = 0; i < tiaraWingIcons.length; i++) {
			tiaraWingIcons[i] = IconHelper.forName(evt.getMap(), "flightTiara" + (i + 1), "items");
		}

		terrasteelHelmWillIcon = IconHelper.forName(evt.getMap(), "willFlame", "items");

		bloodPendantChain = IconHelper.forName(evt.getMap(), "bloodPendant2", "items");
		bloodPendantGem = IconHelper.forName(evt.getMap(), "bloodPendant3", "items");
		snowflakePendantGem = IconHelper.forName(evt.getMap(), "pendant_ice_gem", "items");
		itemFinderGem = IconHelper.forName(evt.getMap(), "itemFinderGem", "items");
		pyroclastGem = IconHelper.forName(evt.getMap(), "pendant_lava_gem", "items");
		crimsonGem = IconHelper.forName(evt.getMap(), "pendant_lava_super_gem", "items");
		cirrusGem = IconHelper.forName(evt.getMap(), "pendant_cloud_gem", "items");
		nimbusGem = IconHelper.forName(evt.getMap(), "pendant_cloud_super_gem", "items");
	}

	private MiscellaneousIcons() {}
}
