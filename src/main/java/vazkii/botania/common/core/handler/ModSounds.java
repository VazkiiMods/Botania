/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.core.handler;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public final class ModSounds {
	public static final SoundEvent airRod = makeSoundEvent("airrod");
	public static final SoundEvent agricarnation = makeSoundEvent("agricarnation");
	public static final SoundEvent altarCraft = makeSoundEvent("altarcraft");
	public static final SoundEvent babylonAttack = makeSoundEvent("babylonattack");
	public static final SoundEvent babylonSpawn = makeSoundEvent("babylonspawn");
	public static final SoundEvent bellows = makeSoundEvent("bellows");
	public static final SoundEvent bifrostRod = makeSoundEvent("bifrostrod");
	public static final SoundEvent blackLotus = makeSoundEvent("blacklotus");
	public static final SoundEvent dash = makeSoundEvent("dash");
	public static final SoundEvent ding = makeSoundEvent("ding");
	public static final SoundEvent divaCharm = makeSoundEvent("divacharm");
	public static final SoundEvent divinationRod = makeSoundEvent("divinationrod");
	public static final SoundEvent doit = makeSoundEvent("doit");
	public static final SoundEvent enchanterFade = makeSoundEvent("enchanterform");
	public static final SoundEvent enchanterForm = makeSoundEvent("enchanterfade");
	public static final SoundEvent enchanterEnchant = makeSoundEvent("enchanterenchant");
	public static final SoundEvent endoflame = makeSoundEvent("endoflame");
	public static final SoundEvent equipBauble = makeSoundEvent("equipbauble");
	public static final SoundEvent gaiaTrap = makeSoundEvent("gaiatrap");
	public static final SoundEvent goldenLaurel = makeSoundEvent("goldenlaurel");
	public static final SoundEvent holyCloak = makeSoundEvent("holycloak");
	public static final SoundEvent laputaStart = makeSoundEvent("laputastart");
	public static final SoundEvent lexiconOpen = makeSoundEvent("lexiconopen");
	public static final SoundEvent lexiconPage = makeSoundEvent("lexiconpage");
	public static final SoundEvent lightRelay = makeSoundEvent("lightrelay");
	public static final SoundEvent manaBlaster = makeSoundEvent("manablaster");
	public static final SoundEvent manaPoolCraft = makeSoundEvent("manapoolcraft");
	public static final SoundEvent missile = makeSoundEvent("missile");
	public static final SoundEvent orechid = makeSoundEvent("orechid");
	public static final SoundEvent potionCreate = makeSoundEvent("potioncreate");
	public static final SoundEvent runeAltarCraft = makeSoundEvent("runealtarcraft");
	public static final SoundEvent runeAltarStart = makeSoundEvent("runealtarstart");
	public static final SoundEvent spreaderFire = makeSoundEvent("spreaderfire");
	public static final SoundEvent starcaller = makeSoundEvent("starcaller");
	public static final SoundEvent terraBlade = makeSoundEvent("terrablade");
	public static final SoundEvent terraformRod = makeSoundEvent("terraformrod");
	public static final SoundEvent terraPickMode = makeSoundEvent("terrapickmode");
	public static final SoundEvent terrasteelCraft = makeSoundEvent("terrasteelcraft");
	public static final SoundEvent thermalily = makeSoundEvent("thermalily");
	public static final SoundEvent unholyCloak = makeSoundEvent("unholycloak");
	public static final SoundEvent way = makeSoundEvent("way");

	public static final SoundEvent gaiaMusic1 = makeSoundEvent("music.gaia1");
	public static final SoundEvent gaiaMusic2 = makeSoundEvent("music.gaia2");

	private static SoundEvent makeSoundEvent(String name) {
		Identifier loc = prefix(name);
		return Registry.register(Registry.SOUND_EVENT, loc, new SoundEvent(loc));
	}

	public static void init() {

	}

	private ModSounds() {}
}
