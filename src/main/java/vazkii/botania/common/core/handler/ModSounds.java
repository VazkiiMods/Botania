/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.core.handler;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public final class ModSounds {
	public static final SoundEvent agricarnation = makeSoundEvent("agricarnation");
	public static final SoundEvent airRod = makeSoundEvent("air_rod");
	public static final SoundEvent altarCraft = makeSoundEvent("altar_craft");
	public static final SoundEvent arcaneRoseDisenchant = makeSoundEvent("arcane_rose_disenchant");
	public static final SoundEvent astrolabeConfigure = makeSoundEvent("astrolabe_configure");
	public static final SoundEvent babylonAttack = makeSoundEvent("babylon_attack");
	public static final SoundEvent babylonSpawn = makeSoundEvent("babylon_spawn");
	public static final SoundEvent bellows = makeSoundEvent("bellows");
	public static final SoundEvent bifrostRod = makeSoundEvent("bifrost_rod");
	public static final SoundEvent blackHoleTalismanConfigure = makeSoundEvent("black_hole_talisman_configure");
	public static final SoundEvent blackLotus = makeSoundEvent("black_lotus");
	public static final SoundEvent dash = makeSoundEvent("dash");
	public static final SoundEvent diceOfFate = makeSoundEvent("dice_of_fate");
	public static final SoundEvent ding = makeSoundEvent("ding");
	public static final SoundEvent divaCharm = makeSoundEvent("diva_charm");
	public static final SoundEvent divinationRod = makeSoundEvent("divination_rod");
	public static final SoundEvent doit = makeSoundEvent("doit");
	public static final SoundEvent drum = makeSoundEvent("drum");
	public static final SoundEvent enchanterFade = makeSoundEvent("enchanter_fade");
	public static final SoundEvent enchanterForm = makeSoundEvent("enchanter_form");
	public static final SoundEvent enchanterEnchant = makeSoundEvent("enchanter_enchant");
	public static final SoundEvent enderAirThrow = makeSoundEvent("ender_air_throw");
	public static final SoundEvent endoflame = makeSoundEvent("endoflame");
	public static final SoundEvent equipBauble = makeSoundEvent("equip_bauble");
	public static final SoundEvent equipElementium = makeSoundEvent("equip_elementium");
	public static final SoundEvent equipManasteel = makeSoundEvent("equip_manasteel");
	public static final SoundEvent equipManaweave = makeSoundEvent("equip_manaweave");
	public static final SoundEvent equipTerrasteel = makeSoundEvent("equip_terrasteel");
	public static final SoundEvent fireRod = makeSoundEvent("fire_rod");
	public static final SoundEvent flareChakramThrow = makeSoundEvent("flare_chakram_throw");
	public static final SoundEvent flugelEyeTeleport = makeSoundEvent("flugel_eye_teleport");
	public static final SoundEvent gaiaTrap = makeSoundEvent("gaia_trap");
	public static final SoundEvent holyCloak = makeSoundEvent("holy_cloak");
	public static final SoundEvent hornDoot = makeSoundEvent("horn_doot");
	public static final SoundEvent labellia = makeSoundEvent("labellia");
	public static final SoundEvent laputaStart = makeSoundEvent("laputa_start");
	public static final SoundEvent lexiconOpen = makeSoundEvent("lexicon_open");
	public static final SoundEvent lexiconPage = makeSoundEvent("lexicon_page");
	public static final SoundEvent lightRelay = makeSoundEvent("light_relay");
	public static final SoundEvent manaBlaster = makeSoundEvent("mana_blaster");
	public static final SoundEvent manaBlasterCycle = makeSoundEvent("mana_blaster_cycle");
	public static final SoundEvent manaBlasterMisfire = makeSoundEvent("mana_blaster_misfire");
	public static final SoundEvent manaPoolCraft = makeSoundEvent("mana_pool_craft");
	public static final SoundEvent missile = makeSoundEvent("missile");
	public static final SoundEvent orechid = makeSoundEvent("orechid");
	public static final SoundEvent potionCreate = makeSoundEvent("potion_create");
	public static final SoundEvent redStringInterceptorClick = makeSoundEvent("red_string_interceptor_click");
	public static final SoundEvent runeAltarCraft = makeSoundEvent("rune_altar_craft");
	public static final SoundEvent runeAltarStart = makeSoundEvent("rune_altar_start");
	public static final SoundEvent smeltRod = makeSoundEvent("smelt_rod");
	public static final SoundEvent smeltRod2 = makeSoundEvent("smelt_rod_extra_no_subtitle");
	public static final SoundEvent smeltRodSimmer = makeSoundEvent("smelt_rod_simmer");
	public static final SoundEvent spreaderCover = makeSoundEvent("spreader_cover");
	public static final SoundEvent spreaderFire = makeSoundEvent("spreader_fire");
	public static final SoundEvent spreaderUncover = makeSoundEvent("spreader_uncover");
	public static final SoundEvent starcaller = makeSoundEvent("starcaller");
	public static final SoundEvent temperanceStoneConfigure = makeSoundEvent("temperance_stone_configure");
	public static final SoundEvent terraBlade = makeSoundEvent("terrablade");
	public static final SoundEvent terraformRod = makeSoundEvent("terraform_rod");
	public static final SoundEvent terraPickMode = makeSoundEvent("terra_pick_mode");
	public static final SoundEvent terrasteelCraft = makeSoundEvent("terrasteel_craft");
	public static final SoundEvent thermalily = makeSoundEvent("thermalily");
	public static final SoundEvent thornChakramThrow = makeSoundEvent("thorn_chakram_throw");
	public static final SoundEvent unholyCloak = makeSoundEvent("unholy_cloak");
	public static final SoundEvent vineBallThrow = makeSoundEvent("vine_ball_throw");
	public static final SoundEvent virusInfects = makeSoundEvent("virus_infects");
	public static final SoundEvent worldSeedTeleport = makeSoundEvent("world_seed_teleport");

	public static final SoundEvent gaiaMusic1 = makeSoundEvent("music.gaia1");
	public static final SoundEvent gaiaMusic2 = makeSoundEvent("music.gaia2");
	public static final SoundEvent way = makeSoundEvent("way");

	private static SoundEvent makeSoundEvent(String name) {
		ResourceLocation loc = prefix(name);
		return Registry.register(Registry.SOUND_EVENT, loc, new SoundEvent(loc));
	}

	public static void init() {

	}

	private ModSounds() {}
}
