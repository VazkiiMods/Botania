/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.block_entity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.block.WandHUD;
import vazkii.botania.api.block_entity.BindableSpecialFlowerBlockEntity;
import vazkii.botania.api.block_entity.FunctionalFlowerBlockEntity;
import vazkii.botania.api.block_entity.GeneratingFlowerBlockEntity;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.block.block_entity.corporea.*;
import vazkii.botania.common.block.block_entity.flower.FloatingFlowerBlockEntity;
import vazkii.botania.common.block.block_entity.flower.functional.*;
import vazkii.botania.common.block.block_entity.flower.generating.*;
import vazkii.botania.common.block.block_entity.flower.misc.*;
import vazkii.botania.common.block.block_entity.mana.*;
import vazkii.botania.common.block.block_entity.red_string.*;
import vazkii.botania.common.block.mana.ManaPoolBlock;
import vazkii.botania.common.block.mana.ManaSpreaderBlock;
import vazkii.botania.common.helper.ColorHelper;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

public class BotaniaBlockEntities {
	private static final Map<ResourceLocation, BlockEntityType<?>> ALL = new HashMap<>();
	private static final Map<Block, BlockEntityType<?>> ADD_TO_EXISTING = new HashMap<>();

	public static final BlockEntityType<PetalApothecaryBlockEntity> PETAL_APOTHECARY = type(
			"petal_apothecary", PetalApothecaryBlockEntity::new,
			BotaniaBlocks.ALL_APOTHECARIES);
	public static final BlockEntityType<ManaSpreaderBlockEntity> MANA_SPREADER = type(
			"mana_spreader", ManaSpreaderBlockEntity::new,
			ManaSpreaderBlock.class::isInstance);
	public static final BlockEntityType<ManaPoolBlockEntity> MANA_POOL = type(
			"mana_pool", ManaPoolBlockEntity::new,
			ManaPoolBlock.class::isInstance);
	public static final BlockEntityType<RunicAltarBlockEntity> RUNIC_ALTAR = type(
			"runic_altar", RunicAltarBlockEntity::new,
			BotaniaBlocks.RUNIC_ALTAR
	);
	public static final BlockEntityType<PylonBlockEntity> MANA_PYLON = type(
			"mana_pylon", PylonBlockEntity::new,
			BotaniaBlocks.MANA_PYLON, BotaniaBlocks.NATURA_PYLON, BotaniaBlocks.GAIA_PYLON
	);
	public static final BlockEntityType<ManaSplitterBlockEntity> MANA_SPLITTER = type(
			"mana_splitter", ManaSplitterBlockEntity::new,
			BotaniaBlocks.MANA_SPLITTER
	);
	public static final BlockEntityType<ManaEnchanterBlockEntity> MANA_ENCHANTER = type(
			"mana_enchanter", ManaEnchanterBlockEntity::new,
			BotaniaBlocks.MANA_ENCHANTER
	);
	public static final BlockEntityType<SpreaderTurntableBlockEntity> SPREADER_TURNTABLE = type(
			"spreader_turntable", SpreaderTurntableBlockEntity::new,
			BotaniaBlocks.SPREADER_TURNTABLE
	);
	public static final BlockEntityType<TinyPlanetBlockEntity> TINY_PLANET = type(
			"tiny_planet", TinyPlanetBlockEntity::new,
			BotaniaBlocks.TINY_PLANET
	);
	public static final BlockEntityType<OpenCrateBlockEntity> OPEN_CRATE = type(
			"open_crate", OpenCrateBlockEntity::new,
			BotaniaBlocks.OPEN_CRATE
	);
	public static final BlockEntityType<CraftyCrateBlockEntity> CRAFTY_CRATE = type(
			"crafty_crate", CraftyCrateBlockEntity::new,
			BotaniaBlocks.CRAFTY_CRATE
	);
	public static final BlockEntityType<EyeOfTheAncientsBlockEntity> EYE_OF_THE_ANCIENTS = type(
			"eye_of_the_ancients", EyeOfTheAncientsBlockEntity::new,
			BotaniaBlocks.EYE_OF_THE_ANCIENTS
	);
	public static final BlockEntityType<PlatformBlockEntity> PLATFORM = type(
			"platform", PlatformBlockEntity::new,
			BotaniaBlocks.ABSTRUSE_PLATFORM, BotaniaBlocks.SPECTRAL_PLATFORM, BotaniaBlocks.INFRANGIBLE_PLATFORM
	);
	public static final BlockEntityType<AlfheimPortalBlockEntity> ALFHEIM_PORTAL = type(
			"alfheim_portal", AlfheimPortalBlockEntity::new,
			BotaniaBlocks.ELVEN_GATEWAY_CORE
	);
	public static final BlockEntityType<BifrostBlockEntity> TEMPORARY_BIFROST_BLOCK = type(
			"temporary_bifrost_block", BifrostBlockEntity::new,
			BotaniaBlocks.TEMPORARY_BIFROST_BLOCK
	);
	public static final BlockEntityType<FloatingFlowerBlockEntity> FLOATING_MUNDANE_FLOWER = type(
			"floating_mundane_flower", FloatingFlowerBlockEntity::new,
			ColorHelper.supportedColors().map(BotaniaBlocks::getFloatingFlower).toArray(Block[]::new));
	public static final BlockEntityType<TinyPotatoBlockEntity> TINY_POTATO = type(
			"tiny_potato", TinyPotatoBlockEntity::new,
			BotaniaBlocks.TINY_POTATO
	);
	public static final BlockEntityType<LifeImbuerBlockEntity> LIFE_IMBUER = type(
			"life_imbuer", LifeImbuerBlockEntity::new,
			BotaniaBlocks.LIFE_IMBUER
	);
	public static final BlockEntityType<EnderOverseerBlockEntity> ENDER_OVERSEER = type(
			"ender_overseer", EnderOverseerBlockEntity::new,
			BotaniaBlocks.ENDER_OVERSEER
	);
	public static final BlockEntityType<StarfieldCreatorBlockEntity> STARFIELD_CREATOR = type(
			"starfield_creator", StarfieldCreatorBlockEntity::new,
			BotaniaBlocks.STARFIELD_CREATOR
	);
	public static final BlockEntityType<PowerGeneratorBlockEntity> MANA_FLUXFIELD = type(
			"mana_fluxfield", PowerGeneratorBlockEntity::new,
			BotaniaBlocks.MANA_FLUXFIELD
	);
	public static final BlockEntityType<BotanicalBreweryBlockEntity> BOTANICAL_BREWERY = type(
			"botanical_brewery", BotanicalBreweryBlockEntity::new,
			BotaniaBlocks.BOTANICAL_BREWERY
	);
	public static final BlockEntityType<TerrestrialAgglomerationPlateBlockEntity> TERRESTRIAL_AGGLOMERATION_PLATE = type(
			"terrestrial_agglomeration_plate", TerrestrialAgglomerationPlateBlockEntity::new,
			BotaniaBlocks.TERRESTRIAL_AGGLOMERATION_PLATE
	);
	public static final BlockEntityType<RedStringContainerBlockEntity> RED_STRINGED_CONTAINER = type(
			"red_stringed_container", XplatAbstractions.INSTANCE::newRedStringContainer,
			BotaniaBlocks.RED_STRINGED_CONTAINER
	);
	public static final BlockEntityType<RedStringDispenserBlockEntity> RED_STRINGED_DISPENSER = type(
			"red_stringed_dispenser", RedStringDispenserBlockEntity::new,
			BotaniaBlocks.RED_STRINGED_DISPENSER
	);
	public static final BlockEntityType<RedStringNutrifierBlockEntity> RED_STRINGED_NUTRIFIER = type(
			"red_stringed_nutrifier", RedStringNutrifierBlockEntity::new,
			BotaniaBlocks.RED_STRINGED_NUTRIFIER
	);
	public static final BlockEntityType<RedStringComparatorBlockEntity> RED_STRINGED_COMPARATOR = type(
			"red_stringed_comparator", RedStringComparatorBlockEntity::new,
			BotaniaBlocks.RED_STRINGED_COMPARATOR
	);
	public static final BlockEntityType<RedStringSpooferBlockEntity> RED_STRINGED_SPOOFER = type(
			"red_stringed_spoofer", RedStringSpooferBlockEntity::new,
			BotaniaBlocks.RED_STRINGED_SPOOFER
	);
	public static final BlockEntityType<ManaFlameBlockEntity> MANA_FLAME = type(
			"mana_flame", ManaFlameBlockEntity::new,
			BotaniaBlocks.MANA_FLAME
	);
	public static final BlockEntityType<ManaPrismBlockEntity> MANA_PRISM = type(
			"mana_prism", ManaPrismBlockEntity::new,
			BotaniaBlocks.MANA_PRISM
	);
	public static final BlockEntityType<CorporeaIndexBlockEntity> CORPOREA_INDEX = type(
			"corporea_index", CorporeaIndexBlockEntity::new,
			BotaniaBlocks.CORPOREA_INDEX
	);
	public static final BlockEntityType<CorporeaFunnelBlockEntity> CORPOREA_FUNNEL = type(
			"corporea_funnel", CorporeaFunnelBlockEntity::new,
			BotaniaBlocks.CORPOREA_FUNNEL
	);
	public static final BlockEntityType<ManaPumpBlockEntity> MANA_PUMP = type(
			"mana_mana_pump", ManaPumpBlockEntity::new,
			BotaniaBlocks.MANA_PUMP
	);
	public static final BlockEntityType<FakeAirBlockEntity> FAKE_AIR = type(
			"fake_air", FakeAirBlockEntity::new,
			BotaniaBlocks.FAKE_AIR
	);
	public static final BlockEntityType<CorporeaInterceptorBlockEntity> CORPOREA_INTERCEPTOR = type(
			"corporea_interceptor", CorporeaInterceptorBlockEntity::new,
			BotaniaBlocks.CORPOREA_INTERCEPTOR
	);
	public static final BlockEntityType<CorporeaCrystalCubeBlockEntity> CORPOREA_CRYSTAL_CUBE = type(
			"corporea_crystal_cube", CorporeaCrystalCubeBlockEntity::new,
			BotaniaBlocks.CORPOREA_CRYSTAL_CUBE
	);
	public static final BlockEntityType<IncensePlateBlockEntity> INCENSE_PLATE = type(
			"incense_plate", IncensePlateBlockEntity::new,
			BotaniaBlocks.INCENSE_PLATE
	);
	public static final BlockEntityType<HoveringHourglassBlockEntity> HOVERING_HOURGLASS = type(
			"hovering_hourglass", HoveringHourglassBlockEntity::new,
			BotaniaBlocks.HOVERING_HOURGLASS
	);
	public static final BlockEntityType<SparkTinkererBlockEntity> SPARK_TINKERER = type(
			"spark_tinkerer", SparkTinkererBlockEntity::new,
			BotaniaBlocks.SPARK_TINKERER
	);
	public static final BlockEntityType<CocoonBlockEntity> COCOON_OF_CAPRICE = type(
			"cocoon_of_caprice", CocoonBlockEntity::new,
			BotaniaBlocks.COCOON_OF_CAPRICE
	);
	public static final BlockEntityType<LuminizerBlockEntity> LUMINIZER = type(
			"luminizer", LuminizerBlockEntity::new,
			BotaniaBlocks.LUMINIZER, BotaniaBlocks.DETECTOR_LUMINIZER, BotaniaBlocks.TOGGLE_LUMINIZER, BotaniaBlocks.FORK_LUMINIZER
	);
	public static final BlockEntityType<CacophoniumBlockEntity> CACOPHONIUM_BLOCK = type(
			"cacophonium_block", CacophoniumBlockEntity::new,
			BotaniaBlocks.CACOPHONIUM_BLOCK
	);
	public static final BlockEntityType<BellowsBlockEntity> MANATIDE_BELLOWS = type(
			"manatide_bellows", BellowsBlockEntity::new,
			BotaniaBlocks.MANATIDE_BELLOWS
	);
	public static final BlockEntityType<CellularBlockEntity> CELLULAR_BLOCK = type(
			"cellular_block", CellularBlockEntity::new,
			BotaniaBlocks.CELLULAR_BLOCK
	);
	public static final BlockEntityType<RedStringInterceptorBlockEntity> RED_STRING_INTERCEPTOR = type(
			"red_stringed_interceptor", RedStringInterceptorBlockEntity::new,
			BotaniaBlocks.RED_STRINGED_INTERCEPTOR
	);
	public static final BlockEntityType<GaiaHeadBlockEntity> GAIA_HEAD = type(
			"gaia_head", GaiaHeadBlockEntity::new,
			BotaniaBlocks.GAIA_HEAD, BotaniaBlocks.GAIA_WALL_HEAD_BLOCK
	);
	public static final BlockEntityType<CorporeaRetainerBlockEntity> CORPOREA_RETAINER = type(
			"corporea_retainer", CorporeaRetainerBlockEntity::new,
			BotaniaBlocks.CORPOREA_RETAINER
	);
	public static final BlockEntityType<TeruTeruBozuBlockEntity> TERU_TERU_BOZU = type(
			"teru_teru_bozu", TeruTeruBozuBlockEntity::new,
			BotaniaBlocks.TERU_TERU_BOZU
	);
	public static final BlockEntityType<AvatarBlockEntity> AVATAR = type(
			"avatar", AvatarBlockEntity::new,
			BotaniaBlocks.LIVINGWOOD_AVATAR
	);
	public static final BlockEntityType<AnimatedTorchBlockEntity> ANIMATED_TORCH = type(
			"animated_torch", AnimatedTorchBlockEntity::new,
			BotaniaBlocks.ANIMATED_TORCH
	);

	public static final BlockEntityType<PureDaisyBlockEntity> PURE_DAISY = type(
			getId(BotaniaBlocks.PURE_DAISY), PureDaisyBlockEntity::new,
			BotaniaBlocks.PURE_DAISY, BotaniaBlocks.FLOATING_PURE_DAISY
	);
	public static final BlockEntityType<ManastarBlockEntity> MANASTAR = type(
			getId(BotaniaBlocks.MANASTAR), ManastarBlockEntity::new,
			BotaniaBlocks.MANASTAR, BotaniaBlocks.FLOATING_MANASTAR
	);
	public static final BlockEntityType<HydroangeasBlockEntity> HYDROANGEAS = type(
			getId(BotaniaBlocks.HYDROANGEAS), HydroangeasBlockEntity::new,
			BotaniaBlocks.HYDROANGEAS, BotaniaBlocks.FLOATING_HYDROANGEAS
	);
	public static final BlockEntityType<EndoflameBlockEntity> ENDOFLAME = type(
			getId(BotaniaBlocks.ENDOFLAME), EndoflameBlockEntity::new,
			BotaniaBlocks.ENDOFLAME, BotaniaBlocks.FLOATING_ENDOFLAME
	);
	public static final BlockEntityType<ThermalilyBlockEntity> THERMALILY = type(
			getId(BotaniaBlocks.THERMALILY), ThermalilyBlockEntity::new,
			BotaniaBlocks.THERMALILY, BotaniaBlocks.FLOATING_THERMALILY
	);
	public static final BlockEntityType<RosaArcanaBlockEntity> ROSA_ARCANA = type(
			getId(BotaniaBlocks.ROSA_ARCANA), RosaArcanaBlockEntity::new,
			BotaniaBlocks.ROSA_ARCANA, BotaniaBlocks.FLOATING_ROSA_ARCANA
	);
	public static final BlockEntityType<MunchdewBlockEntity> MUNCHDEW = type(
			getId(BotaniaBlocks.MUNCHDEW), MunchdewBlockEntity::new,
			BotaniaBlocks.MUNCHDEW, BotaniaBlocks.FLOATING_MUNCHDEW
	);
	public static final BlockEntityType<EntropinnyumBlockEntity> ENTROPINNYUM = type(
			getId(BotaniaBlocks.ENTROPINNYUM), EntropinnyumBlockEntity::new,
			BotaniaBlocks.ENTROPINNYUM, BotaniaBlocks.FLOATING_ENTROPINNYUM
	);
	public static final BlockEntityType<KekimurusBlockEntity> KEKIMURUS = type(
			getId(BotaniaBlocks.KEKIMURUS), KekimurusBlockEntity::new,
			BotaniaBlocks.KEKIMURUS, BotaniaBlocks.FLOATING_KEKIMURUS
	);
	public static final BlockEntityType<GourmaryllisBlockEntity> GOURMARYLLIS = type(
			getId(BotaniaBlocks.GOURMARYLLIS), GourmaryllisBlockEntity::new,
			BotaniaBlocks.GOURMARYLLIS, BotaniaBlocks.FLOATING_GOURMARYLLIS
	);
	public static final BlockEntityType<NarslimmusBlockEntity> NARSLIMMUS = type(
			getId(BotaniaBlocks.NARSLIMMUS), NarslimmusBlockEntity::new,
			BotaniaBlocks.NARSLIMMUS, BotaniaBlocks.FLOATING_NARSLIMMUS
	);
	public static final BlockEntityType<SpectrolusBlockEntity> SPECTROLUS = type(
			getId(BotaniaBlocks.SPECTROLUS), SpectrolusBlockEntity::new,
			BotaniaBlocks.SPECTROLUS, BotaniaBlocks.FLOATING_SPECTROLUS
	);
	public static final BlockEntityType<DandelifeonBlockEntity> DANDELIFEON = type(
			getId(BotaniaBlocks.DANDELIFEON), DandelifeonBlockEntity::new,
			BotaniaBlocks.DANDELIFEON, BotaniaBlocks.FLOATING_DANDELIFEON
	);
	public static final BlockEntityType<RafflowsiaBlockEntity> RAFFLOWSIA = type(
			getId(BotaniaBlocks.RAFFLOWSIA), RafflowsiaBlockEntity::new,
			BotaniaBlocks.RAFFLOWSIA, BotaniaBlocks.FLOATING_RAFFLOWSIA
	);
	public static final BlockEntityType<ShulkMeNotBlockEntity> SHULK_ME_NOT = type(
			getId(BotaniaBlocks.SHULK_ME_NOT), ShulkMeNotBlockEntity::new,
			BotaniaBlocks.SHULK_ME_NOT, BotaniaBlocks.FLOATING_SHULK_ME_NOT
	);
	public static final BlockEntityType<BellethornBlockEntity> BELLETHORNE = type(
			getId(BotaniaBlocks.BELLETHORNE), BellethornBlockEntity::new,
			BotaniaBlocks.BELLETHORNE, BotaniaBlocks.FLOATING_BELLETHORNE
	);
	public static final BlockEntityType<BellethornBlockEntity.Mini> BELLETHORNE_PETITE = type(
			getId(BotaniaBlocks.BELLETHORNE_PETITE), BellethornBlockEntity.Mini::new,
			BotaniaBlocks.BELLETHORNE_PETITE, BotaniaBlocks.FLOATING_BELLETHORNE_PETITE
	);
	public static final BlockEntityType<BergamuteBlockEntity> BERGAMUTE = type(
			getId(BotaniaBlocks.BERGAMUTE), BergamuteBlockEntity::new,
			BotaniaBlocks.BERGAMUTE, BotaniaBlocks.FLOATING_BERGAMUTE
	);
	public static final BlockEntityType<DreadthornBlockEntity> DREADTHORN = type(
			getId(BotaniaBlocks.DREADTHORNE), DreadthornBlockEntity::new,
			BotaniaBlocks.DREADTHORNE, BotaniaBlocks.FLOATING_DREADTHORNE
	);
	public static final BlockEntityType<HeiseiDreamBlockEntity> HEISEI_DREAM = type(
			getId(BotaniaBlocks.HEISEI_DREAM), HeiseiDreamBlockEntity::new,
			BotaniaBlocks.HEISEI_DREAM, BotaniaBlocks.FLOATING_HEISEI_DREAM
	);
	public static final BlockEntityType<TigerseyeBlockEntity> TIGERSEYE = type(
			getId(BotaniaBlocks.TIGERSEYE), TigerseyeBlockEntity::new,
			BotaniaBlocks.TIGERSEYE, BotaniaBlocks.FLOATING_TIGERSEYE
	);
	public static final BlockEntityType<JadedAmaranthusBlockEntity> JADED_AMARANTHUS = type(
			getId(BotaniaBlocks.JADED_AMARANTHUS), JadedAmaranthusBlockEntity::new,
			BotaniaBlocks.JADED_AMARANTHUS, BotaniaBlocks.FLOATING_JADED_AMARANTHUS
	);
	public static final BlockEntityType<OrechidBlockEntity> ORECHID = type(
			getId(BotaniaBlocks.ORECHID), OrechidBlockEntity::new,
			BotaniaBlocks.ORECHID, BotaniaBlocks.FLOATING_ORECHID
	);
	public static final BlockEntityType<FallenKanadeBlockEntity> FALLEN_KANADE = type(
			getId(BotaniaBlocks.FALLEN_KANADE), FallenKanadeBlockEntity::new,
			BotaniaBlocks.FALLEN_KANADE, BotaniaBlocks.FLOATING_FALLEN_KANADE
	);
	public static final BlockEntityType<ExoflameBlockEntity> EXOFLAME = type(
			getId(BotaniaBlocks.EXOFLAME), ExoflameBlockEntity::new,
			BotaniaBlocks.EXOFLAME, BotaniaBlocks.FLOATING_EXOFLAME
	);
	public static final BlockEntityType<AgricarnationBlockEntity> AGRICARNATION = type(
			getId(BotaniaBlocks.AGRICARNATION), AgricarnationBlockEntity::new,
			BotaniaBlocks.AGRICARNATION, BotaniaBlocks.FLOATING_AGRICARNATION
	);
	public static final BlockEntityType<AgricarnationBlockEntity.Mini> AGRICARNATION_PETITE = type(
			getId(BotaniaBlocks.AGRICARNATION_PETITE), AgricarnationBlockEntity.Mini::new,
			BotaniaBlocks.AGRICARNATION_PETITE, BotaniaBlocks.FLOATING_AGRICARNATION_PETITE
	);
	public static final BlockEntityType<HopperhockBlockEntity> HOPPERHOCK = type(
			getId(BotaniaBlocks.HOPPERHOCK), HopperhockBlockEntity::new,
			BotaniaBlocks.HOPPERHOCK, BotaniaBlocks.FLOATING_HOPPERHOCK
	);
	public static final BlockEntityType<HopperhockBlockEntity.Mini> HOPPERHOCK_PETITE = type(
			getId(BotaniaBlocks.HOPPERHOCK_PETITE), HopperhockBlockEntity.Mini::new,
			BotaniaBlocks.HOPPERHOCK_PETITE, BotaniaBlocks.FLOATING_HOPPERHOCK_PETITE
	);
	public static final BlockEntityType<TangleberrieBlockEntity> TANGLEBERRIE = type(
			getId(BotaniaBlocks.TANGLEBERRIE), TangleberrieBlockEntity::new,
			BotaniaBlocks.TANGLEBERRIE, BotaniaBlocks.FLOATING_TANGLEBERRIE
	);
	public static final BlockEntityType<TangleberrieBlockEntity.Mini> TANGLEBERRIE_PETITE = type(
			getId(BotaniaBlocks.TANGLEBERRIE_PETITE), TangleberrieBlockEntity.Mini::new,
			BotaniaBlocks.TANGLEBERRIE_PETITE, BotaniaBlocks.FLOATING_TANGLEBERRIE_PETITE
	);
	public static final BlockEntityType<JiyuuliaBlockEntity> JIYUULIA = type(
			getId(BotaniaBlocks.JIYUULIA), JiyuuliaBlockEntity::new,
			BotaniaBlocks.JIYUULIA, BotaniaBlocks.FLOATING_JIYUULIA
	);
	public static final BlockEntityType<JiyuuliaBlockEntity.Mini> JIYUULIA_PETITE = type(
			getId(BotaniaBlocks.JIYUULIA_PETITE), JiyuuliaBlockEntity.Mini::new,
			BotaniaBlocks.JIYUULIA_PETITE, BotaniaBlocks.FLOATING_JIYUULIA_PETITE
	);
	public static final BlockEntityType<RannuncarpusBlockEntity> RANNUNCARPUS = type(
			getId(BotaniaBlocks.RANNUNCARPUS), RannuncarpusBlockEntity::new,
			BotaniaBlocks.RANNUNCARPUS, BotaniaBlocks.FLOATING_RANNUNCARPUS
	);
	public static final BlockEntityType<RannuncarpusBlockEntity.Mini> RANNUNCARPUS_PETITE = type(
			getId(BotaniaBlocks.RANNUNCARPUS_PETITE), RannuncarpusBlockEntity.Mini::new,
			BotaniaBlocks.RANNUNCARPUS_PETITE, BotaniaBlocks.FLOATING_RANNUNCARPUS_PETITE
	);
	public static final BlockEntityType<HyacidusBlockEntity> HYACIDUS = type(
			getId(BotaniaBlocks.HYACIDUS), HyacidusBlockEntity::new,
			BotaniaBlocks.HYACIDUS, BotaniaBlocks.FLOATING_HYACIDUS
	);
	public static final BlockEntityType<LabelliaBlockEntity> LABELLIA = type(
			getId(BotaniaBlocks.LABELLIA), LabelliaBlockEntity::new,
			BotaniaBlocks.LABELLIA, BotaniaBlocks.FLOATING_LABELLIA
	);
	public static final BlockEntityType<PollidisiacBlockEntity> POLLIDISIAC = type(
			getId(BotaniaBlocks.POLLIDISIAC), PollidisiacBlockEntity::new,
			BotaniaBlocks.POLLIDISIAC, BotaniaBlocks.FLOATING_POLLIDISIAC
	);
	public static final BlockEntityType<ClayconiaBlockEntity> CLAYCONIA = type(
			getId(BotaniaBlocks.CLAYCONIA), ClayconiaBlockEntity::new,
			BotaniaBlocks.CLAYCONIA, BotaniaBlocks.FLOATING_CLAYCONIA
	);
	public static final BlockEntityType<ClayconiaBlockEntity.Mini> CLAYCONIA_PETITE = type(
			getId(BotaniaBlocks.CLAYCONIA_PETITE), ClayconiaBlockEntity.Mini::new,
			BotaniaBlocks.CLAYCONIA_PETITE, BotaniaBlocks.FLOATING_CLAYCONIA_PETITE
	);
	public static final BlockEntityType<LooniumBlockEntity> LOONIUM = type(
			getId(BotaniaBlocks.LOONIUM), LooniumBlockEntity::new,
			BotaniaBlocks.LOONIUM, BotaniaBlocks.FLOATING_LOONIUM
	);
	public static final BlockEntityType<DaffomillBlockEntity> DAFFOMILL = type(
			getId(BotaniaBlocks.DAFFOMILL), DaffomillBlockEntity::new,
			BotaniaBlocks.DAFFOMILL, BotaniaBlocks.FLOATING_DAFFOMILL
	);
	public static final BlockEntityType<VinculotusBlockEntity> VINCULOTUS = type(
			getId(BotaniaBlocks.VINCULOTUS), VinculotusBlockEntity::new,
			BotaniaBlocks.VINCULOTUS, BotaniaBlocks.FLOATING_VINCULOTUS
	);
	public static final BlockEntityType<SpectranthemumBlockEntity> SPECTRANTHEMUM = type(
			getId(BotaniaBlocks.SPECTRANTHEMUM), SpectranthemumBlockEntity::new,
			BotaniaBlocks.SPECTRANTHEMUM, BotaniaBlocks.FLOATING_SPECTRANTHEMUM
	);
	public static final BlockEntityType<MedumoneBlockEntity> MEDUMONE = type(
			getId(BotaniaBlocks.MEDUMONE), MedumoneBlockEntity::new,
			BotaniaBlocks.MEDUMONE, BotaniaBlocks.FLOATING_MEDUMONE
	);
	public static final BlockEntityType<MarimorphosisBlockEntity> MARIMORPHOSIS = type(
			getId(BotaniaBlocks.MARIMORPHOSIS), MarimorphosisBlockEntity::new,
			BotaniaBlocks.MARIMORPHOSIS, BotaniaBlocks.FLOATING_MARIMORPHOSIS
	);
	public static final BlockEntityType<MarimorphosisBlockEntity.Mini> MARIMORPHOSIS_PETITE = type(
			getId(BotaniaBlocks.MARIMORPHOSIS_PETITE), MarimorphosisBlockEntity.Mini::new,
			BotaniaBlocks.MARIMORPHOSIS_PETITE, BotaniaBlocks.FLOATING_MARIMORPHOSIS_PETITE
	);
	public static final BlockEntityType<BubbellBlockEntity> BUBBELL = type(
			getId(BotaniaBlocks.BUBBELL), BubbellBlockEntity::new,
			BotaniaBlocks.BUBBELL, BotaniaBlocks.FLOATING_BUBBELL
	);
	public static final BlockEntityType<BubbellBlockEntity.Mini> BUBBELL_PETITE = type(
			getId(BotaniaBlocks.BUBBELL_PETITE), BubbellBlockEntity.Mini::new,
			BotaniaBlocks.BUBBELL_PETITE, BotaniaBlocks.FLOATING_BUBBELL_PETITE
	);
	public static final BlockEntityType<SolegnoliaBlockEntity> SOLEGNOLIA = type(
			getId(BotaniaBlocks.SOLEGNOLIA), SolegnoliaBlockEntity::new,
			BotaniaBlocks.SOLEGNOLIA, BotaniaBlocks.FLOATING_SOLEGNOLIA
	);
	public static final BlockEntityType<SolegnoliaBlockEntity.Mini> SOLEGNOLIA_PETITE = type(
			getId(BotaniaBlocks.SOLEGNOLIA_PETITE), SolegnoliaBlockEntity.Mini::new,
			BotaniaBlocks.SOLEGNOLIA_PETITE, BotaniaBlocks.FLOATING_SOLEGNOLIA_PETITE
	);
	public static final BlockEntityType<OrechidIgnemBlockEntity> ORECHID_IGNEM = type(
			getId(BotaniaBlocks.ORECHID_IGNEM), OrechidIgnemBlockEntity::new,
			BotaniaBlocks.ORECHID_IGNEM, BotaniaBlocks.FLOATING_ORECHID_IGNEM
	);

	static {
		ADD_TO_EXISTING.put(BotaniaBlocks.LIVINGWOOD_SIGN, BlockEntityType.SIGN);
		ADD_TO_EXISTING.put(BotaniaBlocks.LIVINGWOOD_WALL_SIGN, BlockEntityType.SIGN);
		ADD_TO_EXISTING.put(BotaniaBlocks.DREAMWOOD_SIGN, BlockEntityType.SIGN);
		ADD_TO_EXISTING.put(BotaniaBlocks.DREAMWOOD_WALL_SIGN, BlockEntityType.SIGN);
		ADD_TO_EXISTING.put(BotaniaBlocks.LIVINGWOOD_HANGING_SIGN, BlockEntityType.HANGING_SIGN);
		ADD_TO_EXISTING.put(BotaniaBlocks.LIVINGWOOD_WALL_HANGING_SIGN, BlockEntityType.HANGING_SIGN);
		ADD_TO_EXISTING.put(BotaniaBlocks.DREAMWOOD_HANGING_SIGN, BlockEntityType.HANGING_SIGN);
		ADD_TO_EXISTING.put(BotaniaBlocks.DREAMWOOD_WALL_HANGING_SIGN, BlockEntityType.HANGING_SIGN);
	}

	private static <T extends BlockEntity> BlockEntityType<T> type(String id, BlockEntityType.BlockEntitySupplier<T> factory, Block... blocks) {
		return type(botaniaRL(id), factory, blocks);
	}

	private static <T extends BlockEntity> BlockEntityType<T> type(String id, BlockEntityType.BlockEntitySupplier<T> factory, Predicate<Block> blockPredicate) {
		return type(id, factory, BuiltInRegistries.BLOCK.stream()
				.filter(blockPredicate)
				.filter(block -> BuiltInRegistries.BLOCK.getKey(block).getNamespace().equals(BotaniaAPI.MODID))
				.toArray(Block[]::new));
	}

	private static <T extends BlockEntity> BlockEntityType<T> type(ResourceLocation id, BlockEntityType.BlockEntitySupplier<T> factory, Block... blocks) {
		// TODO: should probably set up that datafixer type instead of passing null to build()
		var ret = BlockEntityType.Builder.of(factory, blocks).build(null);
		var old = ALL.put(id, ret);
		if (old != null) {
			throw new IllegalArgumentException("Duplicate id " + id);
		}
		return ret;
	}

	private static ResourceLocation getId(Block b) {
		return BuiltInRegistries.BLOCK.getKey(b);
	}

	public static void registerTiles(BiConsumer<BlockEntityType<?>, ResourceLocation> r) {
		for (var e : ALL.entrySet()) {
			r.accept(e.getValue(), e.getKey());
		}
	}

	public static void registerAdditionalBlocks(BiConsumer<BlockEntityType<?>, Block> consumer) {
		for (var e : ADD_TO_EXISTING.entrySet()) {
			consumer.accept(e.getValue(), e.getKey());
		}
	}

	public interface BECapConsumer<T> {
		void accept(Function<BlockEntity, T> factory, BlockEntityType<?>... types);
	}

	public static void registerWandHudCaps(BECapConsumer<WandHUD> consumer) {
		consumer.accept(be -> new AnimatedTorchBlockEntity.WandHud((AnimatedTorchBlockEntity) be), BotaniaBlockEntities.ANIMATED_TORCH);
		consumer.accept(be -> new BotanicalBreweryBlockEntity.WandHud((BotanicalBreweryBlockEntity) be), BotaniaBlockEntities.BOTANICAL_BREWERY);
		consumer.accept(be -> new CorporeaRetainerBlockEntity.WandHud((CorporeaRetainerBlockEntity) be), BotaniaBlockEntities.CORPOREA_RETAINER);
		consumer.accept(be -> new CraftyCrateBlockEntity.WandHud((CraftyCrateBlockEntity) be), BotaniaBlockEntities.CRAFTY_CRATE);
		consumer.accept(be -> new ManaEnchanterBlockEntity.WandHud((ManaEnchanterBlockEntity) be), BotaniaBlockEntities.MANA_ENCHANTER);
		consumer.accept(be -> new EyeOfTheAncientsBlockEntity.WandHud((EyeOfTheAncientsBlockEntity) be), BotaniaBlockEntities.EYE_OF_THE_ANCIENTS);
		consumer.accept(be -> new HoveringHourglassBlockEntity.WandHud((HoveringHourglassBlockEntity) be), BotaniaBlockEntities.HOVERING_HOURGLASS);
		consumer.accept(be -> new ManaPoolBlockEntity.WandHud((ManaPoolBlockEntity) be), BotaniaBlockEntities.MANA_POOL);
		consumer.accept(be -> new ManaPrismBlockEntity.WandHud((ManaPrismBlockEntity) be), BotaniaBlockEntities.MANA_PRISM);
		consumer.accept(be -> new ManaSpreaderBlockEntity.WandHud((ManaSpreaderBlockEntity) be), BotaniaBlockEntities.MANA_SPREADER);
		consumer.accept(be -> new SpreaderTurntableBlockEntity.WandHud((SpreaderTurntableBlockEntity) be), BotaniaBlockEntities.SPREADER_TURNTABLE);

		consumer.accept(be -> new SpectrolusBlockEntity.WandHud((SpectrolusBlockEntity) be), SPECTROLUS);
		consumer.accept(be -> new BindableSpecialFlowerBlockEntity.BindableFlowerWandHud<>((GeneratingFlowerBlockEntity) be),
				HYDROANGEAS, ENDOFLAME, THERMALILY, ROSA_ARCANA, MUNCHDEW, ENTROPINNYUM, KEKIMURUS, GOURMARYLLIS, NARSLIMMUS,
				DANDELIFEON, RAFFLOWSIA, SHULK_ME_NOT);
		consumer.accept(be -> new HopperhockBlockEntity.WandHud((HopperhockBlockEntity) be), HOPPERHOCK,
				HOPPERHOCK_PETITE
		);
		consumer.accept(be -> new PollidisiacBlockEntity.WandHud((PollidisiacBlockEntity) be), POLLIDISIAC);
		consumer.accept(be -> new RannuncarpusBlockEntity.WandHud((RannuncarpusBlockEntity) be), RANNUNCARPUS,
				RANNUNCARPUS_PETITE
		);
		consumer.accept(be -> new LooniumBlockEntity.WandHud((LooniumBlockEntity) be), LOONIUM);
		consumer.accept(be -> new BindableSpecialFlowerBlockEntity.BindableFlowerWandHud<>((FunctionalFlowerBlockEntity) be),
				BELLETHORNE, BELLETHORNE_PETITE, DREADTHORN, HEISEI_DREAM, TIGERSEYE,
				JADED_AMARANTHUS, ORECHID, FALLEN_KANADE, EXOFLAME, AGRICARNATION, AGRICARNATION_PETITE,
				TANGLEBERRIE, TANGLEBERRIE_PETITE, JIYUULIA, JIYUULIA_PETITE, HYACIDUS,
				CLAYCONIA, CLAYCONIA_PETITE, DAFFOMILL, VINCULOTUS, SPECTRANTHEMUM, MEDUMONE,
				MARIMORPHOSIS, MARIMORPHOSIS_PETITE, BUBBELL, BUBBELL_PETITE,
				ORECHID_IGNEM, LABELLIA);
	}
}
