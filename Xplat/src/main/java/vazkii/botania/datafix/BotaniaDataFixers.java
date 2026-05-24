package vazkii.botania.datafix;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class BotaniaDataFixers {
	public static final Map<String, String> RENAMED_BLOCKS;
	public static final Map<String, String> RENAMED_ITEMS;

	public static final Set<String> GENERATING_FLOWERS = ImmutableSet.<String>builder()
			.add("botania:hydroangeas")
			.add("botania:endoflame")
			.add("botania:thermalily")
			.add("botania:rosa_arcana")
			.add("botania:munchdew")
			.add("botania:entropinnyum")
			.add("botania:kekimurus")
			.add("botania:gourmaryllis")
			.add("botania:narslimmus")
			.add("botania:spectrolus")
			.add("botania:dandelifeon")
			.add("botania:rafflowsia")
			.add("botania:shulk_me_not")
			.build();
	public static final Set<String> MANA_CONSUMING_FUNCTIONAL_FLOWERS = ImmutableSet.<String>builder()
			.add("botania:bellethorn")
			.add("botania:bellethorn_chibi")
			.add("botania:bergamute")
			.add("botania:dreadthorn")
			.add("botania:heisei_dream")
			.add("botania:tigerseye")
			.add("botania:jaded_amaranthus")
			.add("botania:orechid")
			.add("botania:orechid_ignem")
			.add("botania:fallen_kanade")
			.add("botania:exoflame")
			.add("botania:agricarnation")
			.add("botania:agricarnation_chibi")
			.add("botania:hopperhock")
			.add("botania:hopperhock_chibi")
			.add("botania:tangleberrie")
			.add("botania:tangleberrie_chibi")
			.add("botania:jiyuulia")
			.add("botania:jiyuulia_chibi")
			.add("botania:rannuncarpus")
			.add("botania:rannuncarpus_chibi")
			.add("botania:hyacidus")
			.add("botania:pollidisiac")
			.add("botania:clayconia")
			.add("botania:clayconia_chibi")
			.add("botania:loonium")
			.add("botania:daffomill")
			.add("botania:vinculotus")
			.add("botania:spectranthemum")
			.add("botania:medumone")
			.add("botania:marimorphosis")
			.add("botania:marimorphosis_chibi")
			.add("botania:bubbell")
			.add("botania:bubbell_chibi")
			.add("botania:solegnolia")
			.add("botania:solegnolia_chibi")
			.add("botania:labellia")
			.build();
	public static final Set<String> ALL_SPECIAL_FLOWERS = ImmutableSet.<String>builder()
			.addAll(GENERATING_FLOWERS)
			.addAll(MANA_CONSUMING_FUNCTIONAL_FLOWERS)
			.add("botania:pure_daisy")
			.add("botania:manastar")
			.build();

	public static final Map<String, String> FLOWER_RENAMES = ImmutableMap.<String, String>builder()
			.put("botania:bellethorn", "botania:bellethorne")
			.put("botania:bellethorn_chibi", "botania:bellethorne_petite")
			.put("botania:dreadthorn", "botania:dreadthorne")
			.put("botania:agricarnation_chibi", "botania:agricarnation_petite")
			.put("botania:hopperhock_chibi", "botania:hopperhock_petite")
			.put("botania:tangleberrie_chibi", "botania:tangleberrie_petite")
			.put("botania:jiyuulia_chibi", "botania:jiyuulia_petite")
			.put("botania:rannuncarpus_chibi", "botania:rannuncarpus_petite")
			.put("botania:clayconia_chibi", "botania:clayconia_petite")
			.put("botania:marimorphosis_chibi", "botania:marimorphosis_petite")
			.put("botania:bubbell_chibi", "botania:bubbell_petite")
			.put("botania:solegnolia_chibi", "botania:solegnolia_petite")
			.build();

	public static final Set<String> REDSTONE_RECHECK = ImmutableSet.<String>builder()
			.add("botania:mana_spreader")
			.add("botania:dandelifeon")
			.add("botania:bellethorn")
			.add("botania:bellethorn_chibi")
			.add("botania:bergamute")
			.add("botania:dreadthorn")
			.add("botania:jaded_amaranthus")
			.add("botania:orechid")
			.add("botania:orechid_ignem")
			.add("botania:agricarnation")
			.add("botania:agricarnation_chibi")
			.add("botania:hopperhock")
			.add("botania:hopperhock_chibi")
			.add("botania:rannuncarpus")
			.add("botania:rannuncarpus_chibi")
			.add("botania:hyacidus")
			.add("botania:loonium")
			.add("botania:daffomill")
			.add("botania:vinculotus")
			.add("botania:spectranthemum")
			.add("botania:medumone")
			.add("botania:marimorphosis")
			.add("botania:marimorphosis_chibi")
			.add("botania:solegnolia")
			.add("botania:solegnolia_chibi")
			.add("botania:labellia")
			.add()
			.build();

	static {
		ImmutableMap.Builder<String, String> blocks = ImmutableMap.builder();
		ImmutableMap.Builder<String, String> items = ImmutableMap.builder();

		for (DyeColor dyeColor : DyeColor.values()) {
			String color = dyeColor.getName();
			blocks.put("botania:" + color + "_shiny_flower", "botania:" + color + "_glimmering_flower");
			blocks.put("botania:potted_" + color + "_shiny_flower", "botania:potted_" + color + "_glimmering_flower");
			blocks.put("botania:" + color + "_mushroom", "botania:" + color + "_shimmering_mushroom");
			blocks.put("botania:potted_" + color + "_mushroom", "botania:potted_" + color + "_shimmering_mushroom");
			blocks.put("botania:" + color + "_double_flower", "botania:" + color + "_tall_mystical_flower");
			blocks.put("botania:" + color + "_buried_petals", "botania:" + color + "_buried_petal");

			items.put("botania:" + color + "_petal", "botania:" + color + "_mystical_petal");
		}

		blocks.putAll(FLOWER_RENAMES);
		blocks.putAll(FLOWER_RENAMES.entrySet().stream().map(flower -> Map.entry(
				ResourceLocation.parse(flower.getKey()).withPrefix("floating_").toString(),
				ResourceLocation.parse(flower.getValue()).withPrefix("floating_").toString()
		)).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
		blocks.putAll(FLOWER_RENAMES.entrySet().stream().map(flower -> Map.entry(
				ResourceLocation.parse(flower.getKey()).withPrefix("potted_").toString(),
				ResourceLocation.parse(flower.getValue()).withPrefix("potted_").toString()
		)).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));

		blocks.put("botania:redstone_spreader", "botania:pulse_mana_spreader");
		blocks.put("botania:elven_spreader", "botania:elven_mana_spreader");
		blocks.put("botania:gaia_spreader", "botania:gaia_mana_spreader");

		blocks.put("botania:creative_pool", "botania:creative_mana_pool");
		blocks.put("botania:diluted_pool", "botania:diluted_mana_pool");
		blocks.put("botania:fabulous_pool", "botania:fabulous_mana_pool");

		blocks.put("botania:apothecary_default", "botania:petal_apothecary");
		blocks.put("botania:apothecary_mossy", "botania:mossy_petal_apothecary");
		blocks.put("botania:apothecary_livingrock", "botania:livingrock_petal_apothecary");
		blocks.put("botania:apothecary_deepslate", "botania:deepslate_petal_apothecary");

		Map<String, String> metamorphicMap = ImmutableMap.<String, String>builder()
				.put("forest", "fuchsite")
				.put("plains", "talc")
				.put("mountain", "gneiss")
				.put("fungal", "mycelite")
				.put("swamp", "cataclasite")
				.put("desert", "solite")
				.put("taiga", "lunite")
				.put("mesa", "rosy_talc")
				.build();

		for (Map.Entry<String, String> variant : metamorphicMap.entrySet()) {
			blocks.put("botania:metamorphic_" + variant.getKey() + "_stone", "botania:" + variant.getValue());
			blocks.put("botania:metamorphic_" + variant.getKey() + "_stone_slab", "botania:" + variant.getValue() + "_slab");
			blocks.put("botania:metamorphic_" + variant.getKey() + "_stone_stairs", "botania:" + variant.getValue() + "_stairs");
			blocks.put("botania:metamorphic_" + variant.getKey() + "_stone_wall", "botania:" + variant.getValue() + "_wall");
			blocks.put("botania:metamorphic_" + variant.getKey() + "_cobblestone", "botania:cobbled_" + variant.getValue());
			blocks.put("botania:metamorphic_" + variant.getKey() + "_cobblestone_slab", "botania:cobbled_" + variant.getValue() + "_slab");
			blocks.put("botania:metamorphic_" + variant.getKey() + "_cobblestone_stairs", "botania:cobbled_" + variant.getValue() + "_stairs");
			blocks.put("botania:metamorphic_" + variant.getKey() + "_cobblestone_wall", "botania:cobbled_" + variant.getValue() + "_wall");
			blocks.put("botania:metamorphic_" + variant.getKey() + "_bricks", "botania:" + variant.getValue() + "_bricks");
			blocks.put("botania:metamorphic_" + variant.getKey() + "_bricks_slab", "botania:" + variant.getValue() + "_brick_slab");
			blocks.put("botania:metamorphic_" + variant.getKey() + "_bricks_stairs", "botania:" + variant.getValue() + "_brick_stairs");
			blocks.put("botania:metamorphic_" + variant.getKey() + "_bricks_wall", "botania:" + variant.getValue() + "_brick_wall");
			blocks.put("botania:chiseled_metamorphic_" + variant.getKey() + "_bricks", "botania:chiseled_" + variant.getValue() + "_bricks");

			blocks.put("botania:apothecary_" + variant.getKey(), "botania:" + variant.getValue() + "_petal_apothecary");
		}

		Map<String, String> quartzMap = ImmutableMap.<String, String>builder()
				.put("dark", "smokey")
				.put("mana", "mana")
				.put("blaze", "blaze")
				.put("lavender", "lavender")
				.put("red", "red")
				.put("elf", "elven")
				.put("sunny", "sunny")
				.build();

		for (Map.Entry<String, String> variant : quartzMap.entrySet()) {
			blocks.put("botania:" + variant.getKey() + "_quartz", "botania:" + variant.getValue() + "_quartz_block");
			blocks.put("botania:chiseled_" + variant.getKey() + "_quartz", "botania:chiseled_" + variant.getValue() + "_quartz_block");
			blocks.put("botania:" + variant.getKey() + "_quartz_pillar", "botania:" + variant.getValue() + "_quartz_pillar");
			blocks.put("botania:" + variant.getKey() + "_quartz_slab", "botania:" + variant.getValue() + "_quartz_slab");
			blocks.put("botania:" + variant.getKey() + "_quartz_stairs", "botania:" + variant.getValue() + "_quartz_stairs");

			items.put("botania:quartz_" + variant.getKey(), "botania:" + variant.getValue() + "_quartz");
		}

		for (String block : new String[] { "stair", "slab", "wall" }) {
			blocks.put("botania:livingrock_bricks_" + block, "botania:livingrock_brick_" + block);
			blocks.put("botania:mossy_livingrock_bricks_" + block, "botania:mossy_livingrock_brick_" + block);
		}

		blocks.put("botania:livingwood_planks_stairs", "botania:livingwood_plank_stairs");
		blocks.put("botania:livingwood_planks_slab", "botania:livingwood_plank_slab");
		blocks.put("botania:dreamwood_planks_stairs", "botania:dreamwood_plank_stairs");
		blocks.put("botania:dreamwood_planks_slab", "botania:dreamwood_plank_slab");
		blocks.put("botania:shimmerwood_planks_stairs", "botania:shimmerwood_plank_stairs");
		blocks.put("botania:shimmerwood_planks_slab", "botania:shimmerwood_plank_slab");

		blocks.put("botania:bifrost", "botania:bifrost_bridge");
		blocks.put("botania:bifrost_perm", "botania:bifrost");

		blocks.put("botania:enchanter", "botania:mana_enchanter");
		blocks.put("botania:brewery", "botania:botanical_brewery");
		blocks.put("botania:terra_plate", "botania:terrestrial_agglomeration_plate");
		blocks.put("botania:alfheim_portal", "botania:elven_gateway_core");
		blocks.put("botania:mana_distributor", "botania:mana_splitter");
		blocks.put("botania:piston_relay", "botania:force_relay");
		blocks.put("botania:turntable", "botania:spreader_turntable");
		blocks.put("botania:drum_wild", "botania:drum_of_the_wild");
		blocks.put("botania:drum_gathering", "botania:drum_of_the_gathering");
		blocks.put("botania:drum_canopy", "botania:drum_of_the_canopy");

		blocks.put("botania:spawner_claw", "botania:life_imbuer");
		blocks.put("botania:prism", "botania:mana_prism");
		blocks.put("botania:pump", "botania:mana_pump");
		blocks.put("botania:spark_changer", "botania:spark_tinkerer");
		blocks.put("botania:mana_bomb", "botania:manastorm_charge");
		blocks.put("botania:bellows", "botania:manatide_bellows");
		blocks.put("botania:forest_eye", "botania:eye_of_the_ancients");
		blocks.put("botania:ender_eye_block", "botania:ender_overseer");

		for (String block : new String[] { "container", "dispenser", "comparator", "interceptor" }) {
			blocks.put("botania:red_string_" + block, "botania:red_stringed_" + block);
		}
		blocks.put("botania:red_string_relay", "botania:red_stringed_spoofer");
		blocks.put("botania:red_string_fertilizer", "botania:red_stringed_nutrifier");

		blocks.put("botania:corporea_brick", "botania:corporea_bricks");
		blocks.put("botania:hourglass", "botania:hovering_hourglass");
		blocks.put("botania:ghost_rail", "botania:spectral_rail");
		blocks.put("botania:light_relay", "botania:luminizer");
		blocks.put("botania:detector_light_relay", "botania:detector_luminizer");
		blocks.put("botania:fork_light_relay", "botania:fork_luminizer");
		blocks.put("botania:toggle_light_relay", "botania:toggle_luminizer");
		blocks.put("botania:light_launcher", "botania:luminizer_launcher");

		blocks.put("botania:cell_block", "botania:cellular_block");
		blocks.put("botania:avatar", "botania:livingwood_avatar");
		blocks.put("botania:root", "botania:living_roots");
		blocks.put("botania:cocoon", "botania:cocoon_of_caprice");
		blocks.put("botania:enchanted_soil", "minecraft:grass_block");
		blocks.put("botania:starfield", "botania:starfield_creator");
		blocks.put("botania:blaze_block", "botania:blaze_mesh");

		blocks.put("botania:mana_glass_pane", "botania:managlass_pane");
		blocks.put("botania:elf_glass_pane", "botania:alfglass_pane");

		items.put("botania:lexicon", "botania:lexica_botania");
		items.put("botania:twig_wand", "botania:wand_of_the_forest");
		items.put("botania:dreamwood_wand", "botania:wand_of_the_elven_forest");
		items.put("botania:obedience_stick", "botania:floral_obedience_stick");
		items.put("botania:fertilizer", "botania:floral_fertilizer");
		items.put("botania:life_essence", "botania:gaia_spirit");

		items.put("botania:lens_normal", "botania:mana_lens");
		items.put("botania:lens_speed", "botania:velocity_lens");
		items.put("botania:lens_power", "botania:potency_lens");
		items.put("botania:lens_time", "botania:resistance_lens");
		items.put("botania:lens_efficiency", "botania:efficiency_lens");
		items.put("botania:lens_bounce", "botania:bounce_lens");
		items.put("botania:lens_gravity", "botania:gravity_lens");
		items.put("botania:lens_mine", "botania:bore_lens");
		items.put("botania:lens_damage", "botania:damaging_lens");
		items.put("botania:lens_phantom", "botania:phantom_lens");
		items.put("botania:lens_magnet", "botania:magnetizing_lens");
		items.put("botania:lens_explosive", "botania:entropic_lens");
		items.put("botania:lens_weight", "botania:weight_lens");
		items.put("botania:lens_paint", "botania:paintslinger_lens");
		items.put("botania:lens_fire", "botania:kindle_lens");
		items.put("botania:lens_piston", "botania:force_lens");
		items.put("botania:lens_light", "botania:flash_lens");
		items.put("botania:lens_warp", "botania:warp_lens");
		items.put("botania:lens_redirect", "botania:redirective_lens");
		items.put("botania:lens_firework", "botania:celebratory_lens");
		items.put("botania:lens_flare", "botania:flare_lens");
		items.put("botania:lens_messenger", "botania:messenger_lens");
		items.put("botania:lens_tripwire", "botania:tripwire_lens");
		items.put("botania:lens_storm", "botania:storm_lens");

		items.put("botania:rune_water", "botania:rune_of_water");
		items.put("botania:rune_fire", "botania:rune_of_fire");
		items.put("botania:rune_earth", "botania:rune_of_earth");
		items.put("botania:rune_air", "botania:rune_of_air");
		items.put("botania:rune_spring", "botania:rune_of_spring");
		items.put("botania:rune_summer", "botania:rune_of_summer");
		items.put("botania:rune_autumn", "botania:rune_of_autumn");
		items.put("botania:rune_winter", "botania:rune_of_winter");
		items.put("botania:rune_mana", "botania:rune_of_mana");
		items.put("botania:rune_lust", "botania:rune_of_lust");
		items.put("botania:rune_gluttony", "botania:rune_of_gluttony");
		items.put("botania:rune_greed", "botania:rune_of_greed");
		items.put("botania:rune_sloth", "botania:rune_of_sloth");
		items.put("botania:rune_wrath", "botania:rune_of_wrath");
		items.put("botania:rune_envy", "botania:rune_of_envy");
		items.put("botania:rune_pride", "botania:rune_of_pride");

		items.put("botania:grass_seeds", "botania:pasture_seeds");
		items.put("botania:podzol_seeds", "botania:boreal_seeds");
		items.put("botania:mycelium_seeds", "botania:infestation_spores");
		items.put("botania:dry_seeds", "botania:dry_pasture_seeds");
		items.put("botania:golden_seeds", "botania:golden_pasture_seeds");
		items.put("botania:vivid_seeds", "botania:vivid_pasture_seeds");
		items.put("botania:scorched_seeds", "botania:scorched_pasture_seeds");
		items.put("botania:infused_seeds", "botania:infused_pasture_seeds");
		items.put("botania:mutated_seeds", "botania:mutated_pasture_seeds");

		items.put("botania:dirt_rod", "botania:rod_of_the_lands");
		items.put("botania:skydirt_rod", "botania:rod_of_the_highlands");
		items.put("botania:terraform_rod", "botania:rod_of_the_terra_firma");
		items.put("botania:cobble_rod", "botania:rod_of_the_depths");
		items.put("botania:water_rod", "botania:rod_of_the_seas");
		items.put("botania:tornado_rod", "botania:rod_of_the_skies");
		items.put("botania:fire_rod", "botania:rod_of_the_hells");
		items.put("botania:divining_rod", "botania:rod_of_the_plentiful_mantle");
		items.put("botania:smelt_rod", "botania:rod_of_the_molten_core");
		items.put("botania:exchange_rod", "botania:rod_of_the_shifting_crust");
		items.put("botania:rainbow_rod", "botania:rod_of_the_bifrost");
		items.put("botania:gravity_rod", "botania:rod_of_the_shaded_mesa");
		items.put("botania:missile_rod", "botania:rod_of_the_unstable_reservoir");

		items.put("botania:manasteel_pick", "botania:manasteel_pickaxe");
		items.put("botania:terra_pick", "botania:terra_shatterer");
		items.put("botania:terra_axe", "botania:terra_truncator");
		items.put("botania:terra_sword", "botania:terra_blade");
		items.put("botania:star_sword", "botania:starcaller");
		items.put("botania:thunder_sword", "botania:thundercaller");
		items.put("botania:ender_dagger", "botania:soulscribe");
		items.put("botania:glass_pickaxe", "botania:vitreous_pickaxe");
		items.put("botania:mana_gun", "botania:mana_blaster");
		items.put("botania:clip", "botania:lens_clip");
		items.put("botania:horn_grass", "botania:horn_of_the_wild");
		items.put("botania:horn_leaves", "botania:horn_of_the_canopy");
		items.put("botania:horn_snow", "botania:horn_of_the_covering");
		items.put("botania:slingshot", "botania:livingwood_slingshot");
		items.put("botania:open_bucket", "botania:extrapolated_bucket");
		items.put("botania:spawner_mover", "botania:life_aggregator");
		items.put("botania:ender_hand", "botania:hand_of_ender");
		items.put("botania:crafting_halo", "botania:assembly_halo");
		items.put("botania:auto_crafting_halo", "botania:manufactory_halo");
		items.put("botania:spell_cloth", "botania:spellbinding_cloth");
		items.put("botania:flower_bag", "botania:flower_pouch");
		items.put("botania:temperance_stone", "botania:stone_of_temperance");
		items.put("botania:sextant", "botania:worldshapers_sextant");
		items.put("botania:astrolabe", "botania:worldshapers_astrolabe");

		items.put("botania:bauble_box", "botania:trinket_case");
		items.put("botania:mana_ring", "botania:band_of_mana");
		items.put("botania:mana_ring_greater", "botania:greater_band_of_mana");
		items.put("botania:aura_ring", "botania:band_of_aura");
		items.put("botania:aura_ring_greater", "botania:greater_band_of_aura");
		items.put("botania:magnet_ring", "botania:band_of_magnetization");
		items.put("botania:magnet_ring_greater", "botania:greater_band_of_magnetization");
		items.put("botania:water_ring", "botania:ring_of_chordata");
		items.put("botania:swap_ring", "botania:ring_of_correction");
		items.put("botania:dodge_ring", "botania:ring_of_dexterous_motion");
		items.put("botania:mining_ring", "botania:ring_of_the_mantle");
		items.put("botania:pixie_ring", "botania:great_fairy_ring");
		items.put("botania:reach_ring", "botania:ring_of_far_reach");
		items.put("botania:travel_belt", "botania:sojourners_sash");
		items.put("botania:super_travel_belt", "botania:globetrotters_sash");
		items.put("botania:speed_up_belt", "botania:planestriders_sash");
		items.put("botania:knockback_belt", "botania:tectonic_girdle");
		items.put("botania:ice_pendant", "botania:snowflake_pendant");
		items.put("botania:lava_pendant", "botania:pyroclast_pendant");
		items.put("botania:super_lava_pendant", "botania:crimson_pendant");
		items.put("botania:cloud_pendant", "botania:cirrus_amulet");
		items.put("botania:super_cloud_pendant", "botania:nimbus_amulet");
		items.put("botania:holy_cloak", "botania:cloak_of_virtue");
		items.put("botania:unholy_cloak", "botania:cloak_of_sin");
		items.put("botania:balance_cloak", "botania:cloak_of_balance");
		items.put("botania:monocle", "botania:manaseer_monocle");
		items.put("botania:goddess_charm", "botania:benevolent_goddess_charm");
		items.put("botania:diva_charm", "botania:charm_of_the_diva");
		items.put("botania:itemfinder", "botania:the_spectator");
		items.put("botania:flight_tiara", "botania:flugel_tiara");
		items.put("botania:mana_cookie", "botania:biscuit_of_totality");
		items.put("botania:mana_bottle", "botania:mana_in_a_bottle");
		items.put("botania:laputa_shard", "botania:shard_of_laputa");
		items.put("botania:virus_necrodermal", "botania:necrodermal_virus");
		items.put("botania:virus_nullodermal", "botania:nullodermal_virus");

		items.put("botania:spark", "botania:mana_spark");
		items.put("botania:corpoea_spark_master", "botania:master_corporea_spark");
		items.put("botania:corpoea_spark_creative", "botania:creative_corporea_spark");
		items.put("botania:spark_upgrade_dominant", "botania:spark_augment_dominant");
		items.put("botania:spark_upgrade_recessive", "botania:spark_augment_recessive");
		items.put("botania:spark_upgrade_dispersive", "botania:spark_augment_dispersive");
		items.put("botania:spark_upgrade_isolated", "botania:spark_augment_isolated");

		items.put("botania:overgrowth_seed", "minecraft:air");

		items.put("botania:keep_ivy", "botania:resolute_ivy");

		// These will be kept as uncraftable in 1.21.1 and removed in the next major version
		items.put("botania:placeholder", "botania:crafting_placeholder");
		items.put("botania:pattern_1_1", "botania:crafting_pattern_1_1");
		items.put("botania:pattern_2_2", "botania:crafting_pattern_2_2");
		items.put("botania:pattern_1_2", "botania:crafting_pattern_1_2");
		items.put("botania:pattern_2_1", "botania:crafting_pattern_2_1");
		items.put("botania:pattern_1_3", "botania:crafting_pattern_1_3");
		items.put("botania:pattern_3_1", "botania:crafting_pattern_3_1");
		items.put("botania:pattern_2_3", "botania:crafting_pattern_2_3");
		items.put("botania:pattern_3_2", "botania:crafting_pattern_3_2");
		items.put("botania:pattern_donut", "botania:crafting_pattern_donut");

		items.put("botania:dice", "botania:dice_of_fate");
		items.put("botania:infinite_fruit", "botania:fruit_of_grisaia");
		items.put("botania:king_key", "botania:key_of_the_kings_law");
		items.put("botania:flugel_eye", "botania:eye_of_the_flugel");
		items.put("botania:thor_ring", "botania:ring_of_thor");
		items.put("botania:odin_ring", "botania:ring_of_odin");
		items.put("botania:loki_ring", "botania:ring_of_loki");
		items.put("botania:record_gaia_1", "botania:scathed_music_disc_1");
		items.put("botania:record_gaia_2", "botania:scathed_music_disc_2");
		items.put("botania:pinkinator", "botania:the_pinkinator");
		items.put("botania:vial", "botania:managlass_vial");
		items.put("botania:flask", "botania:alfglass_flask");
		items.put("botania:blood_pendant", "botania:tainted_blood_pendant");

		items.put("botania:ancient_will_ahrim", "botania:will_of_ahrim");
		items.put("botania:ancient_will_dharok", "botania:will_of_dharok");
		items.put("botania:ancient_will_guthan", "botania:will_of_guthan");
		items.put("botania:ancient_will_torag", "botania:will_of_torag");
		items.put("botania:ancient_will_verac", "botania:will_of_verac");
		items.put("botania:ancient_will_karil", "botania:will_of_karil");

		RENAMED_BLOCKS = blocks.build();
		RENAMED_ITEMS = items.build();
	}
}
