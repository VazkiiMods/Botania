/**
 * This class was created by <williewillus>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.client.core.handler;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.text.WordUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.google.common.collect.Maps;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.client.model.FloatingFlowerModel;
import vazkii.botania.client.model.GunModel;
import vazkii.botania.client.model.LexiconModel;
import vazkii.botania.client.model.PlatformModel;
import vazkii.botania.common.Botania;
import vazkii.botania.common.integration.buildcraft.TriggerManaLevel;
import vazkii.botania.common.item.ItemSparkUpgrade;
import vazkii.botania.common.item.equipment.bauble.ItemFlightTiara;
import vazkii.botania.common.item.relic.ItemKingKey;

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
	public final Map<TriggerManaLevel.State, TextureAtlasSprite> manaLevelTriggerIcons = Maps.newEnumMap(TriggerManaLevel.State.class);
	public TextureAtlasSprite[] tiaraWingIcons;

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
		evt.getModelRegistry().putObject(new ModelResourceLocation("botania:lexicon", "inventory"), new LexiconModel());

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
		manaWater = IconHelper.forName(evt.getMap(), "manaWater", "blocks");
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
		nerfBatIcon = IconHelper.forName(evt.getMap(), "nerfBat", "items");

		kingKeyWeaponIcons = new TextureAtlasSprite[ItemKingKey.WEAPON_TYPES];
		for(int i = 0; i < ItemKingKey.WEAPON_TYPES; i++)
			kingKeyWeaponIcons[i] = IconHelper.forName(evt.getMap(), "gateWeapon" + i, "items");

		manaDetectorIcon = IconHelper.forName(evt.getMap(), "triggers/manaDetector", "items");
		runeAltarTriggerIcon = IconHelper.forName(evt.getMap(), "triggers/runeAltarCanCraft", "items");

		for (TriggerManaLevel.State s : TriggerManaLevel.State.values()) {
			manaLevelTriggerIcons.put(s, IconHelper.forName(evt.getMap(), "triggers/mana" + WordUtils.capitalizeFully(s.name()), "items"));
		}

		tiaraWingIcons = new TextureAtlasSprite[ItemFlightTiara.WING_TYPES];
		for (int i = 0; i < tiaraWingIcons.length; i++) {
			tiaraWingIcons[i] = IconHelper.forName(evt.getMap(), "flightTiara" + (i + 1), "items");
		}

		terrasteelHelmWillIcon = IconHelper.forName(evt.getMap(), "willFlame", "items");

		bloodPendantChain = IconHelper.forName(evt.getMap(), "bloodPendant2", "items");
		bloodPendantGem = IconHelper.forName(evt.getMap(), "bloodPendant3", "items");
		snowflakePendantGem = IconHelper.forName(evt.getMap(), "icePendantGem", "items");
		itemFinderGem = IconHelper.forName(evt.getMap(), "itemFinderGem", "items");
		pyroclastGem = IconHelper.forName(evt.getMap(), "lavaPendantGem", "items");
		crimsonGem = IconHelper.forName(evt.getMap(), "superLavaPendantGem", "items");
		cirrusGem = IconHelper.forName(evt.getMap(), "cloudPendantGem", "items");
		nimbusGem = IconHelper.forName(evt.getMap(), "superCloudPendantGem", "items");
	}

	@SubscribeEvent
	public void dumpAtlas(ArrowLooseEvent evt) {
		if (!evt.getEntityPlayer().worldObj.isRemote || !((Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment"))
				|| !evt.getEntityPlayer().isSneaking())
			return;
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

		int width = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);
		int height = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT);

		Botania.LOGGER.debug("Dumped atlas %d wide by %d tall%n", width, height);

		int pixels = width * height;

		IntBuffer buffer = BufferUtils.createIntBuffer(pixels);
		int[] pixelValues = new int[pixels];

		GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);

		GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, buffer);

		buffer.get(pixelValues);

		BufferedImage bufferedimage = new BufferedImage(width, height, 2);

		for (int k = 0; k < height; ++k)
		{
			for (int l = 0; l < width; ++l)
			{
				bufferedimage.setRGB(l, k, pixelValues[k * width + l]);
			}
		}

		File mcFolder = Minecraft.getMinecraft().mcDataDir;
		File result = new File(mcFolder, "atlas.png");

		try {
			ImageIO.write(bufferedimage, "png", result);
		} catch (IOException e) {
			Botania.LOGGER.warn("Failed to dump debug atlas");
		}
	}

	private MiscellaneousIcons() {}
}
