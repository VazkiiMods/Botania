/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.core.handler;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public final class ModSounds {
	public static final SoundEvent airRod = makeSoundEvent("air_rod");
	public static final SoundEvent agricarnation = makeSoundEvent("agricarnation");
	public static final SoundEvent altarCraft = makeSoundEvent("altar_craft");
	public static final SoundEvent arcaneRoseDisenchant = makeSoundEvent("arcane_rose_disenchant");
	public static final SoundEvent babylonAttack = makeSoundEvent("babylon_attack");
	public static final SoundEvent babylonSpawn = makeSoundEvent("babylon_spawn");
	public static final SoundEvent bellows = makeSoundEvent("bellows");
	public static final SoundEvent bifrostRod = makeSoundEvent("bifrost_rod");
	public static final SoundEvent blackLotus = makeSoundEvent("black_lotus");
	public static final SoundEvent dash = makeSoundEvent("dash");
	public static final SoundEvent ding = makeSoundEvent("ding");
	public static final SoundEvent divaCharm = makeSoundEvent("diva_charm");
	public static final SoundEvent divinationRod = makeSoundEvent("divination_rod");
	public static final SoundEvent doit = makeSoundEvent("doit");
	public static final SoundEvent enchanterFade = makeSoundEvent("enchanter_fade");
	public static final SoundEvent enchanterForm = makeSoundEvent("enchanter_form");
	public static final SoundEvent enchanterEnchant = makeSoundEvent("enchanter_enchant");
	public static final SoundEvent endoflame = makeSoundEvent("endoflame");
	public static final SoundEvent equipBauble = makeSoundEvent("equip_bauble");
	public static final SoundEvent gaiaTrap = makeSoundEvent("gaia_trap");
	public static final SoundEvent holyCloak = makeSoundEvent("holy_cloak");
	public static final SoundEvent laputaStart = makeSoundEvent("laputa_start");
	public static final SoundEvent lexiconOpen = makeSoundEvent("lexicon_open");
	public static final SoundEvent lexiconPage = makeSoundEvent("lexicon_page");
	public static final SoundEvent lightRelay = makeSoundEvent("light_relay");
	public static final SoundEvent manaBlaster = makeSoundEvent("mana_blaster");
	public static final SoundEvent manaPoolCraft = makeSoundEvent("mana_pool_craft");
	public static final SoundEvent missile = makeSoundEvent("missile");
	public static final SoundEvent orechid = makeSoundEvent("orechid");
	public static final SoundEvent potionCreate = makeSoundEvent("potion_create");
	public static final SoundEvent runeAltarCraft = makeSoundEvent("rune_altar_craft");
	public static final SoundEvent runeAltarStart = makeSoundEvent("rune_altar_start");
	public static final SoundEvent spreaderFire = makeSoundEvent("spreader_fire");
	public static final SoundEvent starcaller = makeSoundEvent("starcaller");
	public static final SoundEvent terraBlade = makeSoundEvent("terrablade");
	public static final SoundEvent terraformRod = makeSoundEvent("terraform_rod");
	public static final SoundEvent terraPickMode = makeSoundEvent("terra_pick_mode");
	public static final SoundEvent terrasteelCraft = makeSoundEvent("terrasteel_craft");
	public static final SoundEvent thermalily = makeSoundEvent("thermalily");
	public static final SoundEvent unholyCloak = makeSoundEvent("unholy_cloak");
	public static final SoundEvent way = makeSoundEvent("way");

	public static final SoundEvent gaiaMusic1 = makeSoundEvent("music.gaia1");
	public static final SoundEvent gaiaMusic2 = makeSoundEvent("music.gaia2");

	private static SoundEvent makeSoundEvent(String name) {
		ResourceLocation loc = prefix(name);
		return new SoundEvent(loc).setRegistryName(loc);
	}

	public static void registerSounds(RegistryEvent.Register<SoundEvent> evt) {
		IForgeRegistry<SoundEvent> r = evt.getRegistry();
		r.register(airRod);
		r.register(agricarnation);
		r.register(altarCraft);
		r.register(arcaneRoseDisenchant);
		r.register(babylonAttack);
		r.register(babylonSpawn);
		r.register(bellows);
		r.register(bifrostRod);
		r.register(blackLotus);
		r.register(dash);
		r.register(ding);
		r.register(divaCharm);
		r.register(divinationRod);
		r.register(doit);
		r.register(enchanterFade);
		r.register(enchanterForm);
		r.register(enchanterEnchant);
		r.register(endoflame);
		r.register(equipBauble);
		r.register(gaiaTrap);
		r.register(holyCloak);
		r.register(laputaStart);
		r.register(lexiconOpen);
		r.register(lexiconPage);
		r.register(lightRelay);
		r.register(manaBlaster);
		r.register(manaPoolCraft);
		r.register(missile);
		r.register(orechid);
		r.register(potionCreate);
		r.register(runeAltarCraft);
		r.register(runeAltarStart);
		r.register(spreaderFire);
		r.register(starcaller);
		r.register(terraBlade);
		r.register(terraformRod);
		r.register(terraPickMode);
		r.register(terrasteelCraft);
		r.register(thermalily);
		r.register(unholyCloak);
		r.register(way);

		r.register(gaiaMusic1);
		r.register(gaiaMusic2);
	}

	private ModSounds() {}
}
