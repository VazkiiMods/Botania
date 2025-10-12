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
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.xplat.XplatAbstractions;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;
import static vazkii.botania.common.block.BotaniaBlocks.*;

public class BotaniaBlockEntities {
	private static final Map<ResourceLocation, BlockEntityType<?>> ALL = new HashMap<>();
	private static final Map<Block, BlockEntityType<?>> ADD_TO_EXISTING = new HashMap<>();
	public static final BlockEntityType<PetalApothecaryBlockEntity> ALTAR = type(LibBlockNames.ALTAR, PetalApothecaryBlockEntity::new,
			defaultAltar, forestAltar, plainsAltar, mountainAltar, fungalAltar,
			swampAltar, desertAltar, taigaAltar, mesaAltar, mossyAltar,
			livingrockAltar, deepslateAltar
	);
	public static final BlockEntityType<ManaSpreaderBlockEntity> SPREADER = type(LibBlockNames.SPREADER, ManaSpreaderBlockEntity::new, ManaSpreaderBlock.class::isInstance);
	public static final BlockEntityType<ManaPoolBlockEntity> POOL = type(LibBlockNames.POOL, ManaPoolBlockEntity::new, ManaPoolBlock.class::isInstance);
	public static final BlockEntityType<RunicAltarBlockEntity> RUNE_ALTAR = type(LibBlockNames.RUNE_ALTAR, RunicAltarBlockEntity::new, runeAltar);
	public static final BlockEntityType<PylonBlockEntity> PYLON = type(LibBlockNames.PYLON, PylonBlockEntity::new, manaPylon, naturaPylon, gaiaPylon);
	public static final BlockEntityType<ManaSplitterBlockEntity> DISTRIBUTOR = type(LibBlockNames.DISTRIBUTOR, ManaSplitterBlockEntity::new, distributor);
	public static final BlockEntityType<ManaEnchanterBlockEntity> ENCHANTER = type(LibBlockNames.ENCHANTER, ManaEnchanterBlockEntity::new, enchanter);
	public static final BlockEntityType<SpreaderTurntableBlockEntity> TURNTABLE = type(LibBlockNames.TURNTABLE, SpreaderTurntableBlockEntity::new, turntable);
	public static final BlockEntityType<TinyPlanetBlockEntity> TINY_PLANET = type(LibBlockNames.TINY_PLANET, TinyPlanetBlockEntity::new, tinyPlanet);
	public static final BlockEntityType<OpenCrateBlockEntity> OPEN_CRATE = type(LibBlockNames.OPEN_CRATE, OpenCrateBlockEntity::new, openCrate);
	public static final BlockEntityType<CraftyCrateBlockEntity> CRAFT_CRATE = type(LibBlockNames.CRAFT_CRATE, CraftyCrateBlockEntity::new, craftCrate);
	public static final BlockEntityType<EyeOfTheAncientsBlockEntity> FOREST_EYE = type(LibBlockNames.FOREST_EYE, EyeOfTheAncientsBlockEntity::new, forestEye);
	public static final BlockEntityType<PlatformBlockEntity> PLATFORM = type(LibBlockNames.PLATFORM, PlatformBlockEntity::new, abstrusePlatform, spectralPlatform, infrangiblePlatform);
	public static final BlockEntityType<AlfheimPortalBlockEntity> ALF_PORTAL = type(LibBlockNames.ALF_PORTAL, AlfheimPortalBlockEntity::new, alfPortal);
	public static final BlockEntityType<BifrostBlockEntity> BIFROST = type(LibBlockNames.BIFROST, BifrostBlockEntity::new, bifrost);
	public static final BlockEntityType<FloatingFlowerBlockEntity> MINI_ISLAND = type(LibBlockNames.MINI_ISLAND, FloatingFlowerBlockEntity::new, ColorHelper.supportedColors().map(BotaniaBlocks::getFloatingFlower).toArray(Block[]::new));
	public static final BlockEntityType<TinyPotatoBlockEntity> TINY_POTATO = type(LibBlockNames.TINY_POTATO, TinyPotatoBlockEntity::new, tinyPotato);
	public static final BlockEntityType<LifeImbuerBlockEntity> SPAWNER_CLAW = type(LibBlockNames.SPAWNER_CLAW, LifeImbuerBlockEntity::new, spawnerClaw);
	public static final BlockEntityType<EnderOverseerBlockEntity> ENDER_EYE = type(LibBlockNames.ENDER_EYE_BLOCK, EnderOverseerBlockEntity::new, enderEye);
	public static final BlockEntityType<StarfieldCreatorBlockEntity> STARFIELD = type(LibBlockNames.STARFIELD, StarfieldCreatorBlockEntity::new, starfield);
	public static final BlockEntityType<PowerGeneratorBlockEntity> FLUXFIELD = type(LibBlockNames.FLUXFIELD, PowerGeneratorBlockEntity::new, rfGenerator);
	public static final BlockEntityType<BreweryBlockEntity> BREWERY = type(LibBlockNames.BREWERY, BreweryBlockEntity::new, brewery);
	public static final BlockEntityType<TerrestrialAgglomerationPlateBlockEntity> TERRA_PLATE = type(LibBlockNames.TERRA_PLATE, TerrestrialAgglomerationPlateBlockEntity::new, terraPlate);
	public static final BlockEntityType<RedStringContainerBlockEntity> RED_STRING_CONTAINER = type(LibBlockNames.RED_STRING_CONTAINER, XplatAbstractions.INSTANCE::newRedStringContainer, redStringContainer);
	public static final BlockEntityType<RedStringDispenserBlockEntity> RED_STRING_DISPENSER = type(LibBlockNames.RED_STRING_DISPENSER, RedStringDispenserBlockEntity::new, redStringDispenser);
	public static final BlockEntityType<RedStringNutrifierBlockEntity> RED_STRING_FERTILIZER = type(LibBlockNames.RED_STRING_FERTILIZER, RedStringNutrifierBlockEntity::new, redStringFertilizer);
	public static final BlockEntityType<RedStringComparatorBlockEntity> RED_STRING_COMPARATOR = type(LibBlockNames.RED_STRING_COMPARATOR, RedStringComparatorBlockEntity::new, redStringComparator);
	public static final BlockEntityType<RedStringSpooferBlockEntity> RED_STRING_RELAY = type(LibBlockNames.RED_STRING_RELAY, RedStringSpooferBlockEntity::new, redStringRelay);
	public static final BlockEntityType<ManaFlameBlockEntity> MANA_FLAME = type(LibBlockNames.MANA_FLAME, ManaFlameBlockEntity::new, manaFlame);
	public static final BlockEntityType<ManaPrismBlockEntity> PRISM = type(LibBlockNames.PRISM, ManaPrismBlockEntity::new, prism);
	public static final BlockEntityType<CorporeaIndexBlockEntity> CORPOREA_INDEX = type(LibBlockNames.CORPOREA_INDEX, CorporeaIndexBlockEntity::new, corporeaIndex);
	public static final BlockEntityType<CorporeaFunnelBlockEntity> CORPOREA_FUNNEL = type(LibBlockNames.CORPOREA_FUNNEL, CorporeaFunnelBlockEntity::new, corporeaFunnel);
	public static final BlockEntityType<ManaPumpBlockEntity> PUMP = type(LibBlockNames.PUMP, ManaPumpBlockEntity::new, pump);
	public static final BlockEntityType<FakeAirBlockEntity> FAKE_AIR = type(LibBlockNames.FAKE_AIR, FakeAirBlockEntity::new, fakeAir);
	public static final BlockEntityType<CorporeaInterceptorBlockEntity> CORPOREA_INTERCEPTOR = type(LibBlockNames.CORPOREA_INTERCEPTOR, CorporeaInterceptorBlockEntity::new, corporeaInterceptor);
	public static final BlockEntityType<CorporeaCrystalCubeBlockEntity> CORPOREA_CRYSTAL_CUBE = type(LibBlockNames.CORPOREA_CRYSTAL_CUBE, CorporeaCrystalCubeBlockEntity::new, corporeaCrystalCube);
	public static final BlockEntityType<IncensePlateBlockEntity> INCENSE_PLATE = type(LibBlockNames.INCENSE_PLATE, IncensePlateBlockEntity::new, incensePlate);
	public static final BlockEntityType<HoveringHourglassBlockEntity> HOURGLASS = type(LibBlockNames.HOURGLASS, HoveringHourglassBlockEntity::new, hourglass);
	public static final BlockEntityType<SparkTinkererBlockEntity> SPARK_CHANGER = type(LibBlockNames.SPARK_CHANGER, SparkTinkererBlockEntity::new, sparkChanger);
	public static final BlockEntityType<CocoonBlockEntity> COCOON = type(LibBlockNames.COCOON, CocoonBlockEntity::new, cocoon);
	public static final BlockEntityType<LuminizerBlockEntity> LIGHT_RELAY = type(LibBlockNames.LIGHT_RELAY, LuminizerBlockEntity::new, lightRelayDefault, lightRelayDetector, lightRelayToggle, lightRelayFork);
	public static final BlockEntityType<CacophoniumBlockEntity> CACOPHONIUM = type(LibBlockNames.CACOPHONIUM, CacophoniumBlockEntity::new, cacophonium);
	public static final BlockEntityType<BellowsBlockEntity> BELLOWS = type(LibBlockNames.BELLOWS, BellowsBlockEntity::new, bellows);
	public static final BlockEntityType<CellularBlockEntity> CELL_BLOCK = type(LibBlockNames.CELL_BLOCK, CellularBlockEntity::new, cellBlock);
	public static final BlockEntityType<RedStringInterceptorBlockEntity> RED_STRING_INTERCEPTOR = type(LibBlockNames.RED_STRING_INTERCEPTOR, RedStringInterceptorBlockEntity::new, redStringInterceptor);
	public static final BlockEntityType<GaiaHeadBlockEntity> GAIA_HEAD = type(LibBlockNames.GAIA_HEAD, GaiaHeadBlockEntity::new, gaiaHead, gaiaHeadWall);
	public static final BlockEntityType<CorporeaRetainerBlockEntity> CORPOREA_RETAINER = type(LibBlockNames.CORPOREA_RETAINER, CorporeaRetainerBlockEntity::new, corporeaRetainer);
	public static final BlockEntityType<TeruTeruBozuBlockEntity> TERU_TERU_BOZU = type(LibBlockNames.TERU_TERU_BOZU, TeruTeruBozuBlockEntity::new, teruTeruBozu);
	public static final BlockEntityType<AvatarBlockEntity> AVATAR = type(LibBlockNames.AVATAR, AvatarBlockEntity::new, avatar);
	public static final BlockEntityType<AnimatedTorchBlockEntity> ANIMATED_TORCH = type(LibBlockNames.ANIMATED_TORCH, AnimatedTorchBlockEntity::new, animatedTorch);

	public static final BlockEntityType<PureDaisyBlockEntity> PURE_DAISY = type(getId(pureDaisy), PureDaisyBlockEntity::new, pureDaisy, pureDaisyFloating);
	public static final BlockEntityType<ManastarBlockEntity> MANASTAR = type(getId(manastar), ManastarBlockEntity::new, manastar, manastarFloating);
	public static final BlockEntityType<HydroangeasBlockEntity> HYDROANGEAS = type(getId(hydroangeas), HydroangeasBlockEntity::new, hydroangeas, hydroangeasFloating);
	public static final BlockEntityType<EndoflameBlockEntity> ENDOFLAME = type(getId(endoflame), EndoflameBlockEntity::new, endoflame, endoflameFloating);
	public static final BlockEntityType<ThermalilyBlockEntity> THERMALILY = type(getId(thermalily), ThermalilyBlockEntity::new, thermalily, thermalilyFloating);
	public static final BlockEntityType<RosaArcanaBlockEntity> ROSA_ARCANA = type(getId(rosaArcana), RosaArcanaBlockEntity::new, rosaArcana, rosaArcanaFloating);
	public static final BlockEntityType<MunchdewBlockEntity> MUNCHDEW = type(getId(munchdew), MunchdewBlockEntity::new, munchdew, munchdewFloating);
	public static final BlockEntityType<EntropinnyumBlockEntity> ENTROPINNYUM = type(getId(entropinnyum), EntropinnyumBlockEntity::new, entropinnyum, entropinnyumFloating);
	public static final BlockEntityType<KekimurusBlockEntity> KEKIMURUS = type(getId(kekimurus), KekimurusBlockEntity::new, kekimurus, kekimurusFloating);
	public static final BlockEntityType<GourmaryllisBlockEntity> GOURMARYLLIS = type(getId(gourmaryllis), GourmaryllisBlockEntity::new, gourmaryllis, gourmaryllisFloating);
	public static final BlockEntityType<NarslimmusBlockEntity> NARSLIMMUS = type(getId(narslimmus), NarslimmusBlockEntity::new, narslimmus, narslimmusFloating);
	public static final BlockEntityType<SpectrolusBlockEntity> SPECTROLUS = type(getId(spectrolus), SpectrolusBlockEntity::new, spectrolus, spectrolusFloating);
	public static final BlockEntityType<DandelifeonBlockEntity> DANDELIFEON = type(getId(dandelifeon), DandelifeonBlockEntity::new, dandelifeon, dandelifeonFloating);
	public static final BlockEntityType<RafflowsiaBlockEntity> RAFFLOWSIA = type(getId(rafflowsia), RafflowsiaBlockEntity::new, rafflowsia, rafflowsiaFloating);
	public static final BlockEntityType<ShulkMeNotBlockEntity> SHULK_ME_NOT = type(getId(shulkMeNot), ShulkMeNotBlockEntity::new, shulkMeNot, shulkMeNotFloating);
	public static final BlockEntityType<BellethornBlockEntity> BELLETHORNE = type(getId(bellethorn), BellethornBlockEntity::new, bellethorn, bellethornFloating);
	public static final BlockEntityType<BellethornBlockEntity.Mini> BELLETHORNE_CHIBI = type(getId(bellethornChibi), BellethornBlockEntity.Mini::new, bellethornChibi, bellethornChibiFloating);
	public static final BlockEntityType<BergamuteBlockEntity> BERGAMUTE = type(getId(bergamute), BergamuteBlockEntity::new, bergamute, bergamuteFloating);
	public static final BlockEntityType<DreadthornBlockEntity> DREADTHORN = type(getId(dreadthorn), DreadthornBlockEntity::new, dreadthorn, dreadthornFloating);
	public static final BlockEntityType<HeiseiDreamBlockEntity> HEISEI_DREAM = type(getId(heiseiDream), HeiseiDreamBlockEntity::new, heiseiDream, heiseiDreamFloating);
	public static final BlockEntityType<TigerseyeBlockEntity> TIGERSEYE = type(getId(tigerseye), TigerseyeBlockEntity::new, tigerseye, tigerseyeFloating);
	public static final BlockEntityType<JadedAmaranthusBlockEntity> JADED_AMARANTHUS = type(getId(jadedAmaranthus), JadedAmaranthusBlockEntity::new, jadedAmaranthus, jadedAmaranthusFloating);
	public static final BlockEntityType<OrechidBlockEntity> ORECHID = type(getId(orechid), OrechidBlockEntity::new, orechid, orechidFloating);
	public static final BlockEntityType<FallenKanadeBlockEntity> FALLEN_KANADE = type(getId(fallenKanade), FallenKanadeBlockEntity::new, fallenKanade, fallenKanadeFloating);
	public static final BlockEntityType<ExoflameBlockEntity> EXOFLAME = type(getId(exoflame), ExoflameBlockEntity::new, exoflame, exoflameFloating);
	public static final BlockEntityType<AgricarnationBlockEntity> AGRICARNATION = type(getId(agricarnation), AgricarnationBlockEntity::new, agricarnation, agricarnationFloating);
	public static final BlockEntityType<AgricarnationBlockEntity.Mini> AGRICARNATION_CHIBI = type(getId(agricarnationChibi), AgricarnationBlockEntity.Mini::new, agricarnationChibi, agricarnationChibiFloating);
	public static final BlockEntityType<HopperhockBlockEntity> HOPPERHOCK = type(getId(hopperhock), HopperhockBlockEntity::new, hopperhock, hopperhockFloating);
	public static final BlockEntityType<HopperhockBlockEntity.Mini> HOPPERHOCK_CHIBI = type(getId(hopperhockChibi), HopperhockBlockEntity.Mini::new, hopperhockChibi, hopperhockChibiFloating);
	public static final BlockEntityType<TangleberrieBlockEntity> TANGLEBERRIE = type(getId(tangleberrie), TangleberrieBlockEntity::new, tangleberrie, tangleberrieFloating);
	public static final BlockEntityType<TangleberrieBlockEntity.Mini> TANGLEBERRIE_CHIBI = type(getId(tangleberrieChibi), TangleberrieBlockEntity.Mini::new, tangleberrieChibi, tangleberrieChibiFloating);
	public static final BlockEntityType<JiyuuliaBlockEntity> JIYUULIA = type(getId(jiyuulia), JiyuuliaBlockEntity::new, jiyuulia, jiyuuliaFloating);
	public static final BlockEntityType<JiyuuliaBlockEntity.Mini> JIYUULIA_CHIBI = type(getId(jiyuuliaChibi), JiyuuliaBlockEntity.Mini::new, jiyuuliaChibi, jiyuuliaChibiFloating);
	public static final BlockEntityType<RannuncarpusBlockEntity> RANNUNCARPUS = type(getId(rannuncarpus), RannuncarpusBlockEntity::new, rannuncarpus, rannuncarpusFloating);
	public static final BlockEntityType<RannuncarpusBlockEntity.Mini> RANNUNCARPUS_CHIBI = type(getId(rannuncarpusChibi), RannuncarpusBlockEntity.Mini::new, rannuncarpusChibi, rannuncarpusChibiFloating);
	public static final BlockEntityType<HyacidusBlockEntity> HYACIDUS = type(getId(hyacidus), HyacidusBlockEntity::new, hyacidus, hyacidusFloating);
	public static final BlockEntityType<LabelliaBlockEntity> LABELLIA = type(getId(labellia), LabelliaBlockEntity::new, labellia, labelliaFloating);
	public static final BlockEntityType<PollidisiacBlockEntity> POLLIDISIAC = type(getId(pollidisiac), PollidisiacBlockEntity::new, pollidisiac, pollidisiacFloating);
	public static final BlockEntityType<ClayconiaBlockEntity> CLAYCONIA = type(getId(clayconia), ClayconiaBlockEntity::new, clayconia, clayconiaFloating);
	public static final BlockEntityType<ClayconiaBlockEntity.Mini> CLAYCONIA_CHIBI = type(getId(clayconiaChibi), ClayconiaBlockEntity.Mini::new, clayconiaChibi, clayconiaChibiFloating);
	public static final BlockEntityType<LooniumBlockEntity> LOONIUM = type(getId(loonium), LooniumBlockEntity::new, loonium, looniumFloating);
	public static final BlockEntityType<DaffomillBlockEntity> DAFFOMILL = type(getId(daffomill), DaffomillBlockEntity::new, daffomill, daffomillFloating);
	public static final BlockEntityType<VinculotusBlockEntity> VINCULOTUS = type(getId(vinculotus), VinculotusBlockEntity::new, vinculotus, vinculotusFloating);
	public static final BlockEntityType<SpectranthemumBlockEntity> SPECTRANTHEMUM = type(getId(spectranthemum), SpectranthemumBlockEntity::new, spectranthemum, spectranthemumFloating);
	public static final BlockEntityType<MedumoneBlockEntity> MEDUMONE = type(getId(medumone), MedumoneBlockEntity::new, medumone, medumoneFloating);
	public static final BlockEntityType<MarimorphosisBlockEntity> MARIMORPHOSIS = type(getId(marimorphosis), MarimorphosisBlockEntity::new, marimorphosis, marimorphosisFloating);
	public static final BlockEntityType<MarimorphosisBlockEntity.Mini> MARIMORPHOSIS_CHIBI = type(getId(marimorphosisChibi), MarimorphosisBlockEntity.Mini::new, marimorphosisChibi, marimorphosisChibiFloating);
	public static final BlockEntityType<BubbellBlockEntity> BUBBELL = type(getId(bubbell), BubbellBlockEntity::new, bubbell, bubbellFloating);
	public static final BlockEntityType<BubbellBlockEntity.Mini> BUBBELL_CHIBI = type(getId(bubbellChibi), BubbellBlockEntity.Mini::new, bubbellChibi, bubbellChibiFloating);
	public static final BlockEntityType<SolegnoliaBlockEntity> SOLEGNOLIA = type(getId(solegnolia), SolegnoliaBlockEntity::new, solegnolia, solegnoliaFloating);
	public static final BlockEntityType<SolegnoliaBlockEntity.Mini> SOLEGNOLIA_CHIBI = type(getId(solegnoliaChibi), SolegnoliaBlockEntity.Mini::new, solegnoliaChibi, solegnoliaChibiFloating);
	public static final BlockEntityType<OrechidIgnemBlockEntity> ORECHID_IGNEM = type(getId(orechidIgnem), OrechidIgnemBlockEntity::new, orechidIgnem, orechidIgnemFloating);

	static {
		ADD_TO_EXISTING.put(livingwoodSign, BlockEntityType.SIGN);
		ADD_TO_EXISTING.put(livingwoodWallSign, BlockEntityType.SIGN);
		ADD_TO_EXISTING.put(dreamwoodSign, BlockEntityType.SIGN);
		ADD_TO_EXISTING.put(dreamwoodWallSign, BlockEntityType.SIGN);
		ADD_TO_EXISTING.put(livingwoodHangingSign, BlockEntityType.HANGING_SIGN);
		ADD_TO_EXISTING.put(livingwoodWallHangingSign, BlockEntityType.HANGING_SIGN);
		ADD_TO_EXISTING.put(dreamwoodHangingSign, BlockEntityType.HANGING_SIGN);
		ADD_TO_EXISTING.put(dreamwoodWallHangingSign, BlockEntityType.HANGING_SIGN);
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
		consumer.accept(be -> new BreweryBlockEntity.WandHud((BreweryBlockEntity) be), BotaniaBlockEntities.BREWERY);
		consumer.accept(be -> new CorporeaRetainerBlockEntity.WandHud((CorporeaRetainerBlockEntity) be), BotaniaBlockEntities.CORPOREA_RETAINER);
		consumer.accept(be -> new CraftyCrateBlockEntity.WandHud((CraftyCrateBlockEntity) be), BotaniaBlockEntities.CRAFT_CRATE);
		consumer.accept(be -> new ManaEnchanterBlockEntity.WandHud((ManaEnchanterBlockEntity) be), BotaniaBlockEntities.ENCHANTER);
		consumer.accept(be -> new EyeOfTheAncientsBlockEntity.WandHud((EyeOfTheAncientsBlockEntity) be), BotaniaBlockEntities.FOREST_EYE);
		consumer.accept(be -> new HoveringHourglassBlockEntity.WandHud((HoveringHourglassBlockEntity) be), BotaniaBlockEntities.HOURGLASS);
		consumer.accept(be -> new ManaPoolBlockEntity.WandHud((ManaPoolBlockEntity) be), BotaniaBlockEntities.POOL);
		consumer.accept(be -> new ManaPrismBlockEntity.WandHud((ManaPrismBlockEntity) be), BotaniaBlockEntities.PRISM);
		consumer.accept(be -> new ManaSpreaderBlockEntity.WandHud((ManaSpreaderBlockEntity) be), BotaniaBlockEntities.SPREADER);
		consumer.accept(be -> new SpreaderTurntableBlockEntity.WandHud((SpreaderTurntableBlockEntity) be), BotaniaBlockEntities.TURNTABLE);

		consumer.accept(be -> new SpectrolusBlockEntity.WandHud((SpectrolusBlockEntity) be), SPECTROLUS);
		consumer.accept(be -> new BindableSpecialFlowerBlockEntity.BindableFlowerWandHud<>((GeneratingFlowerBlockEntity) be),
				HYDROANGEAS, ENDOFLAME, THERMALILY, ROSA_ARCANA, MUNCHDEW, ENTROPINNYUM, KEKIMURUS, GOURMARYLLIS, NARSLIMMUS,
				DANDELIFEON, RAFFLOWSIA, SHULK_ME_NOT);
		consumer.accept(be -> new HopperhockBlockEntity.WandHud((HopperhockBlockEntity) be), HOPPERHOCK, HOPPERHOCK_CHIBI);
		consumer.accept(be -> new PollidisiacBlockEntity.WandHud((PollidisiacBlockEntity) be), POLLIDISIAC);
		consumer.accept(be -> new RannuncarpusBlockEntity.WandHud((RannuncarpusBlockEntity) be), RANNUNCARPUS, RANNUNCARPUS_CHIBI);
		consumer.accept(be -> new LooniumBlockEntity.WandHud((LooniumBlockEntity) be), LOONIUM);
		consumer.accept(be -> new BindableSpecialFlowerBlockEntity.BindableFlowerWandHud<>((FunctionalFlowerBlockEntity) be),
				BELLETHORNE, BELLETHORNE_CHIBI, DREADTHORN, HEISEI_DREAM, TIGERSEYE,
				JADED_AMARANTHUS, ORECHID, FALLEN_KANADE, EXOFLAME, AGRICARNATION, AGRICARNATION_CHIBI,
				TANGLEBERRIE, TANGLEBERRIE_CHIBI, JIYUULIA, JIYUULIA_CHIBI, HYACIDUS,
				CLAYCONIA, CLAYCONIA_CHIBI, DAFFOMILL, VINCULOTUS, SPECTRANTHEMUM, MEDUMONE,
				MARIMORPHOSIS, MARIMORPHOSIS_CHIBI, BUBBELL, BUBBELL_CHIBI, SOLEGNOLIA, SOLEGNOLIA_CHIBI,
				ORECHID_IGNEM, LABELLIA);
	}
}
