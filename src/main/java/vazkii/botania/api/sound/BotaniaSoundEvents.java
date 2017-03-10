package vazkii.botania.api.sound;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

/**
 * Use these to play Botania sounds.
 * Do not access this class before Botania preinits!
 */
public final class BotaniaSoundEvents {

	public static final SoundEvent airRod = getRegisteredSoundEvent("airrod");
	public static final SoundEvent agricarnation = getRegisteredSoundEvent("agricarnation");
	public static final SoundEvent altarCraft = getRegisteredSoundEvent("altarcraft");
	public static final SoundEvent babylonAttack = getRegisteredSoundEvent("babylonattack");
	public static final SoundEvent babylonSpawn = getRegisteredSoundEvent("babylonspawn");
	public static final SoundEvent bellows = getRegisteredSoundEvent("bellows");
	public static final SoundEvent bifrostRod = getRegisteredSoundEvent("bifrostrod");
	public static final SoundEvent blackLotus = getRegisteredSoundEvent("blacklotus");
	public static final SoundEvent dash = getRegisteredSoundEvent("dash");
	public static final SoundEvent ding = getRegisteredSoundEvent("ding");
	public static final SoundEvent divaCharm = getRegisteredSoundEvent("divacharm");
	public static final SoundEvent divinationRod = getRegisteredSoundEvent("divinationrod");
	public static final SoundEvent doit = getRegisteredSoundEvent("doit");
	public static final SoundEvent enchanterFade = getRegisteredSoundEvent("enchanterform");
	public static final SoundEvent enchanterForm = getRegisteredSoundEvent("enchanterfade");
	public static final SoundEvent enchanterEnchant = getRegisteredSoundEvent("enchanterenchant");
	public static final SoundEvent endoflame = getRegisteredSoundEvent("endoflame");
	public static final SoundEvent equipBauble = getRegisteredSoundEvent("equipbauble");
	public static final SoundEvent gaiaTrap = getRegisteredSoundEvent("gaiatrap");
	public static final SoundEvent goldenLaurel = getRegisteredSoundEvent("goldenlaurel");
	public static final SoundEvent holyCloak = getRegisteredSoundEvent("holycloak");
	public static final SoundEvent laputaStart = getRegisteredSoundEvent("laputastart");
	public static final SoundEvent lexiconOpen = getRegisteredSoundEvent("lexiconopen");
	public static final SoundEvent lexiconPage = getRegisteredSoundEvent("lexiconpage");
	public static final SoundEvent lightRelay = getRegisteredSoundEvent("lightrelay");
	public static final SoundEvent manaBlaster = getRegisteredSoundEvent("manablaster");
	public static final SoundEvent manaPoolCraft = getRegisteredSoundEvent("manapoolcraft");
	public static final SoundEvent missile = getRegisteredSoundEvent("missile");
	public static final SoundEvent orechid = getRegisteredSoundEvent("orechid");
	public static final SoundEvent potionCreate = getRegisteredSoundEvent("potioncreate");
	public static final SoundEvent runeAltarCraft = getRegisteredSoundEvent("runealtarcraft");
	public static final SoundEvent runeAltarStart = getRegisteredSoundEvent("runealtarstart");
	public static final SoundEvent spreaderFire = getRegisteredSoundEvent("spreaderfire");
	public static final SoundEvent starcaller = getRegisteredSoundEvent("starcaller");
	public static final SoundEvent terraBlade = getRegisteredSoundEvent("terrablade");
	public static final SoundEvent terraformRod = getRegisteredSoundEvent("terraformrod");
	public static final SoundEvent terraPickMode = getRegisteredSoundEvent("terrapickmode");
	public static final SoundEvent terrasteelCraft = getRegisteredSoundEvent("terrasteelcraft");
	public static final SoundEvent thermalily = getRegisteredSoundEvent("thermalily");
	public static final SoundEvent unholyCloak = getRegisteredSoundEvent("unholycloak");
	public static final SoundEvent way = getRegisteredSoundEvent("way");

	public static final SoundEvent gaiaMusic1 = getRegisteredSoundEvent("music.gaia1");
	public static final SoundEvent gaiaMusic2 = getRegisteredSoundEvent("music.gaia2");

	private static SoundEvent getRegisteredSoundEvent(String name) {
		return SoundEvent.REGISTRY.getObject(new ResourceLocation("botania", name));
	}

	private BotaniaSoundEvents() {}
}
