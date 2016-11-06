package vazkii.botania.api.sound;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

/**
 * Use these to play Botania sounds.
 * Do not access this class before Botania preinits!
 */
public final class BotaniaSoundEvents {

	public static final SoundEvent airRod = getRegisteredSoundEvent("botania:airRod");
	public static final SoundEvent agricarnation = getRegisteredSoundEvent("botania:agricarnation");
	public static final SoundEvent altarCraft = getRegisteredSoundEvent("botania:altarCraft");
	public static final SoundEvent babylonAttack = getRegisteredSoundEvent("botania:babylonAttack");
	public static final SoundEvent babylonSpawn = getRegisteredSoundEvent("botania:babylonSpawn");
	public static final SoundEvent bellows = getRegisteredSoundEvent("botania:bellows");
	public static final SoundEvent bifrostRod = getRegisteredSoundEvent("botania:bifrostRod");
	public static final SoundEvent blackLotus = getRegisteredSoundEvent("botania:blackLotus");
	public static final SoundEvent dash = getRegisteredSoundEvent("botania:dash");
	public static final SoundEvent ding = getRegisteredSoundEvent("botania:ding");
	public static final SoundEvent divaCharm = getRegisteredSoundEvent("botania:divaCharm");
	public static final SoundEvent divinationRod = getRegisteredSoundEvent("botania:divinationRod");
	public static final SoundEvent doit = getRegisteredSoundEvent("botania:doit");
	public static final SoundEvent enchanterFade = getRegisteredSoundEvent("botania:enchanterForm");
	public static final SoundEvent enchanterForm = getRegisteredSoundEvent("botania:enchanterFade");
	public static final SoundEvent enchanterEnchant = getRegisteredSoundEvent("botania:enchanterEnchant");
	public static final SoundEvent endoflame = getRegisteredSoundEvent("botania:endoflame");
	public static final SoundEvent equipBauble = getRegisteredSoundEvent("botania:equipBauble");
	public static final SoundEvent gaiaTrap = getRegisteredSoundEvent("botania:gaiaTrap");
	public static final SoundEvent goldenLaurel = getRegisteredSoundEvent("botania:goldenLaurel");
	public static final SoundEvent holyCloak = getRegisteredSoundEvent("botania:holyCloak");
	public static final SoundEvent laputaStart = getRegisteredSoundEvent("botania:laputaStart");
	public static final SoundEvent lexiconOpen = getRegisteredSoundEvent("botania:lexiconOpen");
	public static final SoundEvent lexiconPage = getRegisteredSoundEvent("botania:lexiconPage");
	public static final SoundEvent lightRelay = getRegisteredSoundEvent("botania:lightRelay");
	public static final SoundEvent manaBlaster = getRegisteredSoundEvent("botania:manaBlaster");
	public static final SoundEvent manaPoolCraft = getRegisteredSoundEvent("botania:manaPoolCraft");
	public static final SoundEvent missile = getRegisteredSoundEvent("botania:missile");
	public static final SoundEvent orechid = getRegisteredSoundEvent("botania:orechid");
	public static final SoundEvent potionCreate = getRegisteredSoundEvent("botania:potionCreate");
	public static final SoundEvent runeAltarCraft = getRegisteredSoundEvent("botania:runeAltarCraft");
	public static final SoundEvent runeAltarStart = getRegisteredSoundEvent("botania:runeAltarStart");
	public static final SoundEvent spreaderFire = getRegisteredSoundEvent("botania:spreaderFire");
	public static final SoundEvent starcaller = getRegisteredSoundEvent("botania:starcaller");
	public static final SoundEvent terraBlade = getRegisteredSoundEvent("botania:terraBlade");
	public static final SoundEvent terraformRod = getRegisteredSoundEvent("botania:terraformRod");
	public static final SoundEvent terraPickMode = getRegisteredSoundEvent("botania:terraPickMode");
	public static final SoundEvent terrasteelCraft = getRegisteredSoundEvent("botania:terrasteelCraft");
	public static final SoundEvent thermalily = getRegisteredSoundEvent("botania:thermalily");
	public static final SoundEvent unholyCloak = getRegisteredSoundEvent("botania:unholyCloak");
	public static final SoundEvent way = getRegisteredSoundEvent("botania:way");

	public static final SoundEvent gaiaMusic1 = getRegisteredSoundEvent("botania:music.gaia1");
	public static final SoundEvent gaiaMusic2 = getRegisteredSoundEvent("botania:music.gaia2");

	private static SoundEvent getRegisteredSoundEvent(String name) {
		return SoundEvent.REGISTRY.getObject(new ResourceLocation(name));
	}

	private BotaniaSoundEvents() {}
}
