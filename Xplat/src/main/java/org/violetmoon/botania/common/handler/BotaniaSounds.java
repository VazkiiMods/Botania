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
	public static final SoundEvent APOTHECARY_CRAFT = makeSoundEvent("apothecary_craft");
	public static final SoundEvent MANATIDE_BELLOWS = makeSoundEvent("manatide_bellows");
	public static final SoundEvent DRUM = makeSoundEvent("drum");
	public static final SoundEvent ENCHANTER_ENCHANT = makeSoundEvent("enchanter_enchant");
	public static final SoundEvent ENCHANTER_FADE = makeSoundEvent("enchanter_fade");
	public static final SoundEvent ENCHANTER_FORM = makeSoundEvent("enchanter_form");
	public static final SoundEvent HORN_DOOT = makeSoundEvent("horn_doot");
	public static final SoundEvent INCENSE_PLATE_IGNITE = makeSoundEvent("incense_plate_ignite");
	public static final SoundEvent LUMINIZER = makeSoundEvent("luminizer");
	public static final SoundEvent MANA_POOL_CRAFT = makeSoundEvent("mana_pool_craft");
	public static final SoundEvent POTION_CREATE = makeSoundEvent("potion_create");
	public static final SoundEvent RED_STRINGED_INTERCEPTOR_CLICK = makeSoundEvent("red_stringed_interceptor_click");
	public static final SoundEvent RUNIC_ALTAR_CRAFT = makeSoundEvent("runic_altar_craft");
	public static final SoundEvent RUNIC_ALTAR_START = makeSoundEvent("runic_altar_start");
	public static final SoundEvent PRISM_ADD_LENS = makeSoundEvent("prism_add_lens");
	public static final SoundEvent PRISM_REMOVE_LENS = makeSoundEvent("prism_remove_lens");
	public static final SoundEvent SPREADER_ADD_LENS = makeSoundEvent("spreader_add_lens");
	public static final SoundEvent SPREADER_REMOVE_LENS = makeSoundEvent("spreader_remove_lens");
	public static final SoundEvent SPREADER_COVER = makeSoundEvent("spreader_cover");
	public static final SoundEvent SPREADER_UNCOVER = makeSoundEvent("spreader_uncover");
	public static final SoundEvent SPREADER_SCAFFOLD = makeSoundEvent("spreader_scaffold");
	public static final SoundEvent SPREADER_UN_SCAFFOLD = makeSoundEvent("spreader_un_scaffold");
	public static final SoundEvent SPREADER_FIRE = makeSoundEvent("spreader_fire");
	public static final SoundEvent TERRA_PLATE_CRAFT = makeSoundEvent("terra_plate_craft");

	//items, rods, and trinkets
	public static final SoundEvent ROD_OF_THE_SKIES = makeSoundEvent("rod_of_the_skies");
	public static final SoundEvent WORLDSHAPERS_ASTROLABE_CONFIGURE = makeSoundEvent("worldshapers_astrolabe_configure");
	public static final SoundEvent BIFROST_ROD = makeSoundEvent("bifrost_rod");
	public static final SoundEvent BLACK_HOLE_TALISMAN_CONFIGURE = makeSoundEvent("black_hole_talisman_configure");
	public static final SoundEvent BLACK_LOTUS = makeSoundEvent("black_lotus");
	public static final SoundEvent DASH = makeSoundEvent("dash");
	public static final SoundEvent DICE_OF_FATE = makeSoundEvent("dice_of_fate");
	public static final SoundEvent CHARM_OF_THE_DIVA = makeSoundEvent("charm_of_the_diva");
	public static final SoundEvent ROD_OF_THE_PLENTIFUL_MANTLE = makeSoundEvent("rod_of_the_plentiful_mantle");
	public static final SoundEvent ENDER_ESSENCE_FILL = makeSoundEvent("ender_essence_fill");
	public static final SoundEvent ENDER_ESSENCE_THROW = makeSoundEvent("ender_essence_throw");
	public static final SoundEvent EQUIP_BAUBLE = makeSoundEvent("equip_bauble");
	public static final Holder<SoundEvent> EQUIP_ELEMENTIUM = makeSoundEventHolder("equip_elementium");
	public static final Holder<SoundEvent> EQUIP_MANASTEEL = makeSoundEventHolder("equip_manasteel");
	public static final Holder<SoundEvent> EQUIP_MANAWEAVE = makeSoundEventHolder("equip_manaweave");
	public static final Holder<SoundEvent> EQUIP_TERRASTEEL = makeSoundEventHolder("equip_terrasteel");
	public static final SoundEvent ROD_OF_THE_HELLS = makeSoundEvent("rod_of_the_hells");
	public static final SoundEvent FLARE_CHAKRAM_THROW = makeSoundEvent("flare_chakram_throw");
	public static final SoundEvent EYE_OF_THE_FLUGEL_BIND = makeSoundEvent("eye_of_the_flugel_bind");
	public static final SoundEvent EYE_OF_THE_FLUGEL_TELEPORT = makeSoundEvent("eye_of_the_flugel_teleport");
	public static final SoundEvent CLOAK_OF_VIRTUE = makeSoundEvent("cloak_of_virtue");
	public static final SoundEvent LAPUTA_START = makeSoundEvent("laputa_start");
	public static final SoundEvent LEXICON_OPEN = makeSoundEvent("lexicon_open");
	//public static final SoundEvent LEXICON_PAGE = makeSoundEvent("lexicon_page");
	public static final SoundEvent MANA_BLASTER = makeSoundEvent("mana_blaster");
	public static final SoundEvent MANA_BLASTER_CYCLE = makeSoundEvent("mana_blaster_cycle");
	public static final SoundEvent MANA_BLASTER_MISFIRE = makeSoundEvent("mana_blaster_misfire");
	public static final SoundEvent MISSILE = makeSoundEvent("missile");
	public static final SoundEvent MISSILE_FUNNY = makeSoundEvent("missile_funny"); //pew pew
	public static final SoundEvent THE_PINKINATOR = makeSoundEvent("the_pinkinator");
	public static final SoundEvent ROD_OF_THE_MOLTEN_CORE = makeSoundEvent("rod_of_the_molten_core");
	public static final SoundEvent ROD_OF_THE_MOLTEN_CORE_EXTRA = makeSoundEvent("rod_of_the_molten_core_extra_no_subtitle");
	public static final SoundEvent ROD_OF_THE_MOLTEN_CORE_SIMMER = makeSoundEvent("rod_of_the_molten_core_simmer");
	public static final SoundEvent STARCALLER = makeSoundEvent("starcaller");
	public static final SoundEvent STONE_OF_TEMPERANCE_CONFIGURE = makeSoundEvent("stone_of_temperance_configure");
	public static final SoundEvent MANUFACTORY_HALO_CONFIGURE = makeSoundEvent("manufactory_halo_configure");
	public static final SoundEvent PETAL_POUCH_CONFIGURE = makeSoundEvent("petal_pouch_configure");
	public static final SoundEvent TERRABLADE = makeSoundEvent("terrablade");
	public static final SoundEvent TERRA_SHATTERER_MODE = makeSoundEvent("terra_shatterer_mode");
	public static final SoundEvent ROD_OF_THE_TERRA_FIRMA = makeSoundEvent("rod_of_the_terra_firma");
	public static final SoundEvent THORN_CHAKRAM_THROW = makeSoundEvent("thorn_chakram_throw");
	public static final SoundEvent CLOAK_OF_SIN = makeSoundEvent("cloak_of_sin");
	public static final SoundEvent VINE_BALL_THROW = makeSoundEvent("vine_ball_throw");
	public static final SoundEvent VIRUS_INFECT = makeSoundEvent("virus_infect");
	public static final SoundEvent WORLD_SEED_TELEPORT = makeSoundEvent("world_seed_teleport");

	//flowers
	public static final SoundEvent AGRICARNATION = makeSoundEvent("agricarnation");
	public static final SoundEvent ARCANE_ROSE_DISENCHANT = makeSoundEvent("arcane_rose_disenchant");
	public static final SoundEvent ENDOFLAME = makeSoundEvent("endoflame");
	public static final SoundEvent ENTROPINNYUM_ANGRY = makeSoundEvent("entropinnyum_angry");
	public static final SoundEvent ENTROPINNYUM_HAPPY = makeSoundEvent("entropinnyum_happy");
	public static final SoundEvent LABELLIA = makeSoundEvent("labellia");
	public static final SoundEvent NARSLIMMUS_EAT_BIG = makeSoundEvent("narslimmus_eat_big");
	public static final SoundEvent NARSLIMMUS_EAT_SMALL = makeSoundEvent("narslimmus_eat_small");
	public static final SoundEvent ORECHID = makeSoundEvent("orechid");
	public static final SoundEvent SHULK_ME_NOT = makeSoundEvent("shulk_me_not");
	public static final SoundEvent THERMALILY = makeSoundEvent("thermalily");
	public static final SoundEvent TIGERSEYE_PACIFY = makeSoundEvent("tigerseye_pacify");

	//entities
	public static final SoundEvent TREASURE_WEAPON_ATTACK = makeSoundEvent("treasure_weapon_attack");
	public static final SoundEvent TREASURE_WEAPON_SPAWN = makeSoundEvent("treasure_weapon_spawn");
	public static final SoundEvent GAIA_DEATH = makeSoundEvent("gaia_death");
	public static final SoundEvent GAIA_SUMMON = makeSoundEvent("gaia_summon");
	public static final SoundEvent GAIA_TELEPORT = makeSoundEvent("gaia_teleport");
	public static final SoundEvent GAIA_TRAP = makeSoundEvent("gaia_trap");

	//misc
	public static final SoundEvent DING = makeSoundEvent("ding");
	public static final SoundEvent DOIT = makeSoundEvent("doit");

	//music
	public static final Holder<SoundEvent> MUSIC_GAIA_BOSS_1 = makeSoundEventHolder("music.gaia1");
	public static final Holder<SoundEvent> MUSIC_GAIA_BOSS_2 = makeSoundEventHolder("music.gaia2");
	public static final SoundEvent MUSIC_DISC_GAIA_1 = makeSoundEvent("music_disc.gaia1");
	public static final SoundEvent MUSIC_DISC_GAIA_2 = makeSoundEvent("music_disc.gaia2");
	public static final SoundEvent way = makeSoundEvent("way");

	public static final Music GAIA1_BOSS = new Music(MUSIC_GAIA_BOSS_1, 0, 0, true);
	public static final Music GAIA2_BOSS = new Music(MUSIC_GAIA_BOSS_2, 0, 0, true);

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
