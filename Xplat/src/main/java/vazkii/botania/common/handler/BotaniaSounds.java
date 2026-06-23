/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.handler;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;

import vazkii.botania.common.helper.RegistryHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

public final class BotaniaSounds {
	private static final List<RegistryHelper.HolderProxy<SoundEvent>> EVENTS = new ArrayList<>();
	//blocks
	public static final SoundEvent altarCraft = makeSoundEvent("altar_craft");
	public static final SoundEvent bellows = makeSoundEvent("manatide_bellows");
	public static final SoundEvent drum = makeSoundEvent("drum");
	public static final SoundEvent enchanterEnchant = makeSoundEvent("enchanter_enchant");
	public static final SoundEvent enchanterFade = makeSoundEvent("enchanter_fade");
	public static final SoundEvent enchanterForm = makeSoundEvent("enchanter_form");
	public static final SoundEvent hornDoot = makeSoundEvent("horn_doot");
	public static final SoundEvent incensePlateIgnite = makeSoundEvent("incense_plate_ignite");
	public static final SoundEvent lightRelay = makeSoundEvent("luminizer");
	public static final SoundEvent manaPoolCraft = makeSoundEvent("mana_pool_craft");
	public static final SoundEvent potionCreate = makeSoundEvent("potion_create");
	public static final SoundEvent redStringInterceptorClick = makeSoundEvent("red_stringed_interceptor_click");
	public static final SoundEvent runeAltarCraft = makeSoundEvent("runic_altar_craft");
	public static final SoundEvent runeAltarStart = makeSoundEvent("runic_altar_start");
	public static final SoundEvent prismAddLens = makeSoundEvent("prism_add_lens");
	public static final SoundEvent prismRemoveLens = makeSoundEvent("prism_remove_lens");
	public static final SoundEvent spreaderAddLens = makeSoundEvent("spreader_add_lens");
	public static final SoundEvent spreaderRemoveLens = makeSoundEvent("spreader_remove_lens");
	public static final SoundEvent spreaderCover = makeSoundEvent("spreader_cover");
	public static final SoundEvent spreaderUncover = makeSoundEvent("spreader_uncover");
	public static final SoundEvent spreaderScaffold = makeSoundEvent("spreader_scaffold");
	public static final SoundEvent spreaderUnScaffold = makeSoundEvent("spreader_un_scaffold");
	public static final SoundEvent spreaderFire = makeSoundEvent("spreader_fire");
	public static final SoundEvent terrasteelCraft = makeSoundEvent("terrasteel_craft");

	//items, rods, and trinkets
	public static final SoundEvent airRod = makeSoundEvent("air_rod");
	public static final SoundEvent astrolabeConfigure = makeSoundEvent("worldshapers_astrolabe_configure");
	public static final SoundEvent bifrostRod = makeSoundEvent("bifrost_rod");
	public static final SoundEvent blackHoleTalismanConfigure = makeSoundEvent("black_hole_talisman_configure");
	public static final SoundEvent blackLotus = makeSoundEvent("black_lotus");
	public static final SoundEvent dash = makeSoundEvent("dash");
	public static final SoundEvent diceOfFate = makeSoundEvent("dice_of_fate");
	public static final SoundEvent divaCharm = makeSoundEvent("diva_charm");
	public static final SoundEvent divinationRod = makeSoundEvent("divination_rod");
	public static final SoundEvent enderAirThrow = makeSoundEvent("ender_air_throw");
	public static final SoundEvent equipBauble = makeSoundEvent("equip_bauble");
	public static final Holder<SoundEvent> equipElementium = makeSoundEventHolder("equip_elementium");
	public static final Holder<SoundEvent> equipManasteel = makeSoundEventHolder("equip_manasteel");
	public static final Holder<SoundEvent> equipManaweave = makeSoundEventHolder("equip_manaweave");
	public static final Holder<SoundEvent> equipTerrasteel = makeSoundEventHolder("equip_terrasteel");
	public static final SoundEvent fireRod = makeSoundEvent("rod_of_the_hells");
	public static final SoundEvent flareChakramThrow = makeSoundEvent("flare_chakram_throw");
	public static final SoundEvent flugelEyeBind = makeSoundEvent("flugel_eye_bind");
	public static final SoundEvent flugelEyeTeleport = makeSoundEvent("flugel_eye_teleport");
	public static final SoundEvent holyCloak = makeSoundEvent("holy_cloak");
	public static final SoundEvent laputaStart = makeSoundEvent("laputa_start");
	public static final SoundEvent lexiconOpen = makeSoundEvent("lexicon_open");
	//public static final SoundEvent lexiconPage = makeSoundEvent("lexicon_page");
	public static final SoundEvent manaBlaster = makeSoundEvent("mana_blaster");
	public static final SoundEvent manaBlasterCycle = makeSoundEvent("mana_blaster_cycle");
	public static final SoundEvent manaBlasterMisfire = makeSoundEvent("mana_blaster_misfire");
	public static final SoundEvent missile = makeSoundEvent("missile");
	public static final SoundEvent missileFunny = makeSoundEvent("missile_funny"); //pew pew
	public static final SoundEvent pinkinator = makeSoundEvent("pinkinator");
	public static final SoundEvent smeltRod = makeSoundEvent("rod_of_the_molten_core");
	public static final SoundEvent smeltRod2 = makeSoundEvent("rod_of_the_molten_core_extra_no_subtitle");
	public static final SoundEvent smeltRodSimmer = makeSoundEvent("rod_of_the_molten_core_simmer");
	public static final SoundEvent starcaller_sword = makeSoundEvent("starcaller_sword");
	public static final SoundEvent temperanceStoneConfigure = makeSoundEvent("stone_of_temperance_configure");
	public static final SoundEvent manufactoryHaloConfigure = makeSoundEvent("manufactory_halo_configure");
	public static final SoundEvent petalPouchConfigure = makeSoundEvent("petal_pouch_configure");
	public static final SoundEvent terraBlade = makeSoundEvent("terrablade");
	public static final SoundEvent terraPickMode = makeSoundEvent("terrasteel_pickaxe_mode");
	public static final SoundEvent terraformRod = makeSoundEvent("rod_of_the_terra_firma");
	public static final SoundEvent thornChakramThrow = makeSoundEvent("thorn_chakram_throw");
	public static final SoundEvent unholyCloak = makeSoundEvent("unholy_cloak");
	public static final SoundEvent vineBallThrow = makeSoundEvent("vine_ball_throw");
	public static final SoundEvent virusInfect = makeSoundEvent("virus_infect");
	public static final SoundEvent worldSeedTeleport = makeSoundEvent("world_seed_teleport");

	//flowers
	public static final SoundEvent agricarnation = makeSoundEvent("agricarnation");
	public static final SoundEvent arcaneRoseDisenchant = makeSoundEvent("arcane_rose_disenchant");
	public static final SoundEvent endoflame = makeSoundEvent("endoflame");
	public static final SoundEvent entropinnyumAngry = makeSoundEvent("entropinnyum_angry");
	public static final SoundEvent entropinnyumHappy = makeSoundEvent("entropinnyum_happy");
	public static final SoundEvent labellia = makeSoundEvent("labellia");
	public static final SoundEvent narslimmusEatBig = makeSoundEvent("narslimmus_eat_big");
	public static final SoundEvent narslimmusEatSmall = makeSoundEvent("narslimmus_eat_small");
	public static final SoundEvent orechid = makeSoundEvent("orechid");
	public static final SoundEvent shulkMeNot = makeSoundEvent("shulk_me_not");
	public static final SoundEvent thermalily = makeSoundEvent("thermalily");
	public static final SoundEvent tigerseyePacify = makeSoundEvent("tigerseye_pacify");

	//entities
	public static final SoundEvent babylonAttack = makeSoundEvent("babylon_attack");
	public static final SoundEvent babylonSpawn = makeSoundEvent("babylon_spawn");
	public static final SoundEvent gaiaDeath = makeSoundEvent("gaia_death");
	public static final SoundEvent gaiaSummon = makeSoundEvent("gaia_summon");
	public static final SoundEvent gaiaTeleport = makeSoundEvent("gaia_teleport");
	public static final SoundEvent gaiaTrap = makeSoundEvent("gaia_trap");

	//misc
	public static final SoundEvent ding = makeSoundEvent("ding");
	public static final SoundEvent doit = makeSoundEvent("doit");

	//music
	public static final Holder<SoundEvent> musicGaiaBoss1 = makeSoundEventHolder("music.gaia1");
	public static final Holder<SoundEvent> musicGaiaBoss2 = makeSoundEventHolder("music.gaia2");
	public static final SoundEvent musicDiscGaia1 = makeSoundEvent("music_disc.gaia1");
	public static final SoundEvent musicDiscGaia2 = makeSoundEvent("music_disc.gaia2");
	public static final SoundEvent way = makeSoundEvent("way");

	public static final Music GAIA1_BOSS = new Music(musicGaiaBoss1, 0, 0, true);
	public static final Music GAIA2_BOSS = new Music(musicGaiaBoss2, 0, 0, true);

	public static final Set<Music> GAIA_BOSS_MUSIC = Set.of(GAIA1_BOSS, GAIA2_BOSS);

	private static SoundEvent makeSoundEvent(String name) {
		return makeSoundEventHolder(name).value();
	}

	private static Holder<SoundEvent> makeSoundEventHolder(String name) {
		ResourceLocation id = botaniaRL(name);
		RegistryHelper.HolderProxy<SoundEvent> proxy = RegistryHelper.holderProxy(Registries.SOUND_EVENT, id,
				SoundEvent.createVariableRangeEvent(id));
		EVENTS.add(proxy);
		return proxy;
	}

	public static void init(Registry<SoundEvent> registry) {
		EVENTS.forEach(proxy -> proxy.register(registry));
	}

	private BotaniaSounds() {}
}
