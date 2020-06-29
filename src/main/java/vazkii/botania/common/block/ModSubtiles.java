/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;

import vazkii.botania.api.subtile.TileEntitySpecialFlower;
import vazkii.botania.common.block.subtile.SubTileManastar;
import vazkii.botania.common.block.subtile.SubTilePureDaisy;
import vazkii.botania.common.block.subtile.functional.*;
import vazkii.botania.common.block.subtile.generating.*;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

import java.util.List;
import java.util.function.Supplier;

import static vazkii.botania.common.block.ModBlocks.register;

public class ModSubtiles {
	public static final Block pureDaisy = new BlockSpecialFlower(Block.Properties.from(Blocks.POPPY), SubTilePureDaisy::new);
	public static final Block pureDaisyFloating = new BlockFloatingSpecialFlower(Block.Properties.create(Material.EARTH).hardnessAndResistance(0.5F).sound(SoundType.GROUND).lightValue(15), SubTilePureDaisy::new);

	public static final Block manastar = new BlockSpecialFlower(Block.Properties.from(pureDaisy), SubTileManastar::new);
	public static final Block manastarFloating = new BlockFloatingSpecialFlower(Block.Properties.from(pureDaisyFloating), SubTileManastar::new);

	public static final Block hydroangeas = new BlockSpecialFlower(Block.Properties.from(pureDaisy), SubTileHydroangeas::new);
	public static final Block hydroangeasFloating = new BlockFloatingSpecialFlower(Block.Properties.from(pureDaisyFloating), SubTileHydroangeas::new);

	public static final Block endoflame = new BlockSpecialFlower(Block.Properties.from(pureDaisy), SubTileEndoflame::new);
	public static final Block endoflameFloating = new BlockFloatingSpecialFlower(Block.Properties.from(pureDaisyFloating), SubTileEndoflame::new);

	public static final Block thermalily = new BlockSpecialFlower(Block.Properties.from(pureDaisy), SubTileThermalily::new);
	public static final Block thermalilyFloating = new BlockFloatingSpecialFlower(Block.Properties.from(pureDaisyFloating), SubTileThermalily::new);

	public static final Block rosaArcana = new BlockSpecialFlower(Block.Properties.from(pureDaisy), SubTileArcaneRose::new);
	public static final Block rosaArcanaFloating = new BlockFloatingSpecialFlower(Block.Properties.from(pureDaisyFloating), SubTileArcaneRose::new);

	public static final Block munchdew = new BlockSpecialFlower(Block.Properties.from(pureDaisy), SubTileMunchdew::new);
	public static final Block munchdewFloating = new BlockFloatingSpecialFlower(Block.Properties.from(pureDaisyFloating), SubTileMunchdew::new);

	public static final Block entropinnyum = new BlockSpecialFlower(Block.Properties.from(pureDaisy), SubTileEntropinnyum::new);
	public static final Block entropinnyumFloating = new BlockFloatingSpecialFlower(Block.Properties.from(pureDaisyFloating), SubTileEntropinnyum::new);

	public static final Block kekimurus = new BlockSpecialFlower(Block.Properties.from(pureDaisy), SubTileKekimurus::new);
	public static final Block kekimurusFloating = new BlockFloatingSpecialFlower(Block.Properties.from(pureDaisyFloating), SubTileKekimurus::new);

	public static final Block gourmaryllis = new BlockSpecialFlower(Block.Properties.from(pureDaisy), SubTileGourmaryllis::new);
	public static final Block gourmaryllisFloating = new BlockFloatingSpecialFlower(Block.Properties.from(pureDaisyFloating), SubTileGourmaryllis::new);

	public static final Block narslimmus = new BlockSpecialFlower(Block.Properties.from(pureDaisy), SubTileNarslimmus::new);
	public static final Block narslimmusFloating = new BlockFloatingSpecialFlower(Block.Properties.from(pureDaisyFloating), SubTileNarslimmus::new);

	public static final Block spectrolus = new BlockSpecialFlower(Block.Properties.from(pureDaisy), SubTileSpectrolus::new);
	public static final Block spectrolusFloating = new BlockFloatingSpecialFlower(Block.Properties.from(pureDaisyFloating), SubTileSpectrolus::new);

	public static final Block dandelifeon = new BlockSpecialFlower(Block.Properties.from(pureDaisy), SubTileDandelifeon::new);
	public static final Block dandelifeonFloating = new BlockFloatingSpecialFlower(Block.Properties.from(pureDaisyFloating), SubTileDandelifeon::new);

	public static final Block rafflowsia = new BlockSpecialFlower(Block.Properties.from(pureDaisy), SubTileRafflowsia::new);
	public static final Block rafflowsiaFloating = new BlockFloatingSpecialFlower(Block.Properties.from(pureDaisyFloating), SubTileRafflowsia::new);

	public static final Block shulkMeNot = new BlockSpecialFlower(Block.Properties.from(pureDaisy), SubTileShulkMeNot::new);
	public static final Block shulkMeNotFloating = new BlockFloatingSpecialFlower(Block.Properties.from(pureDaisyFloating), SubTileShulkMeNot::new);

	public static final Block bellethorn = new BlockSpecialFlower(Block.Properties.from(pureDaisy), SubTileBellethorn::new);
	public static final Block bellethornChibi = new BlockSpecialFlower(Block.Properties.from(pureDaisy), SubTileBellethorn.Mini::new);
	public static final Block bellethornFloating = new BlockFloatingSpecialFlower(Block.Properties.from(pureDaisy), SubTileBellethorn::new);
	public static final Block bellethornChibiFloating = new BlockFloatingSpecialFlower(Block.Properties.from(pureDaisy), SubTileBellethorn.Mini::new);

	public static final Block bergamute = new BlockSpecialFlower(Block.Properties.from(pureDaisy), SubTileBergamute::new);
	public static final Block bergamuteFloating = new BlockFloatingSpecialFlower(Block.Properties.from(pureDaisyFloating), SubTileBergamute::new);

	public static final Block dreadthorn = new BlockSpecialFlower(Block.Properties.from(pureDaisy), SubTileDreadthorn::new);
	public static final Block dreadthornFloating = new BlockFloatingSpecialFlower(Block.Properties.from(pureDaisyFloating), SubTileDreadthorn::new);

	public static final Block heiseiDream = new BlockSpecialFlower(Block.Properties.from(pureDaisy), SubTileHeiseiDream::new);
	public static final Block heiseiDreamFloating = new BlockFloatingSpecialFlower(Block.Properties.from(pureDaisyFloating), SubTileHeiseiDream::new);

	public static final Block tigerseye = new BlockSpecialFlower(Block.Properties.from(pureDaisy), SubTileTigerseye::new);
	public static final Block tigerseyeFloating = new BlockFloatingSpecialFlower(Block.Properties.from(pureDaisyFloating), SubTileTigerseye::new);

	public static final Block jadedAmaranthus = new BlockSpecialFlower(Block.Properties.from(pureDaisy), SubTileJadedAmaranthus::new);
	public static final Block jadedAmaranthusFloating = new BlockFloatingSpecialFlower(Block.Properties.from(pureDaisyFloating), SubTileJadedAmaranthus::new);

	public static final Block orechid = new BlockSpecialFlower(Block.Properties.from(pureDaisy), SubTileOrechid::new);
	public static final Block orechidFloating = new BlockFloatingSpecialFlower(Block.Properties.from(pureDaisyFloating), SubTileOrechid::new);

	public static final Block fallenKanade = new BlockSpecialFlower(Block.Properties.from(pureDaisy), SubTileFallenKanade::new);
	public static final Block fallenKanadeFloating = new BlockFloatingSpecialFlower(Block.Properties.from(pureDaisyFloating), SubTileFallenKanade::new);

	public static final Block exoflame = new BlockSpecialFlower(Block.Properties.from(pureDaisy), SubTileExoflame::new);
	public static final Block exoflameFloating = new BlockFloatingSpecialFlower(Block.Properties.from(pureDaisyFloating), SubTileExoflame::new);

	public static final Block agricarnation = new BlockSpecialFlower(Block.Properties.from(pureDaisy), SubTileAgricarnation::new);
	public static final Block agricarnationChibi = new BlockSpecialFlower(Block.Properties.from(pureDaisy), SubTileAgricarnation.Mini::new);
	public static final Block agricarnationFloating = new BlockFloatingSpecialFlower(Block.Properties.from(pureDaisy), SubTileAgricarnation::new);
	public static final Block agricarnationChibiFloating = new BlockFloatingSpecialFlower(Block.Properties.from(pureDaisy), SubTileAgricarnation.Mini::new);

	public static final Block hopperhock = new BlockSpecialFlower(Block.Properties.from(pureDaisy), SubTileHopperhock::new);
	public static final Block hopperhockChibi = new BlockSpecialFlower(Block.Properties.from(pureDaisy), SubTileHopperhock.Mini::new);
	public static final Block hopperhockFloating = new BlockFloatingSpecialFlower(Block.Properties.from(pureDaisy), SubTileHopperhock::new);
	public static final Block hopperhockChibiFloating = new BlockFloatingSpecialFlower(Block.Properties.from(pureDaisy), SubTileHopperhock.Mini::new);

	public static final Block tangleberrie = new BlockSpecialFlower(Block.Properties.from(pureDaisy), SubTileTangleberrie::new);
	public static final Block tangleberrieFloating = new BlockFloatingSpecialFlower(Block.Properties.from(pureDaisyFloating), SubTileTangleberrie::new);

	public static final Block jiyuulia = new BlockSpecialFlower(Block.Properties.from(pureDaisy), SubTileJiyuulia::new);
	public static final Block jiyuuliaFloating = new BlockFloatingSpecialFlower(Block.Properties.from(pureDaisyFloating), SubTileJiyuulia::new);

	public static final Block rannuncarpus = new BlockSpecialFlower(Block.Properties.from(pureDaisy), SubTileRannuncarpus::new);
	public static final Block rannuncarpusChibi = new BlockSpecialFlower(Block.Properties.from(pureDaisy), SubTileRannuncarpus.Mini::new);
	public static final Block rannuncarpusFloating = new BlockFloatingSpecialFlower(Block.Properties.from(pureDaisy), SubTileRannuncarpus::new);
	public static final Block rannuncarpusChibiFloating = new BlockFloatingSpecialFlower(Block.Properties.from(pureDaisy), SubTileRannuncarpus.Mini::new);

	public static final Block hyacidus = new BlockSpecialFlower(Block.Properties.from(pureDaisy), SubTileHyacidus::new);
	public static final Block hyacidusFloating = new BlockFloatingSpecialFlower(Block.Properties.from(pureDaisyFloating), SubTileHyacidus::new);

	public static final Block pollidisiac = new BlockSpecialFlower(Block.Properties.from(pureDaisy), SubTilePollidisiac::new);
	public static final Block pollidisiacFloating = new BlockFloatingSpecialFlower(Block.Properties.from(pureDaisyFloating), SubTilePollidisiac::new);

	public static final Block clayconia = new BlockSpecialFlower(Block.Properties.from(pureDaisy), SubTileClayconia::new);
	public static final Block clayconiaChibi = new BlockSpecialFlower(Block.Properties.from(pureDaisy), SubTileClayconia.Mini::new);
	public static final Block clayconiaFloating = new BlockFloatingSpecialFlower(Block.Properties.from(pureDaisy), SubTileClayconia::new);
	public static final Block clayconiaChibiFloating = new BlockFloatingSpecialFlower(Block.Properties.from(pureDaisy), SubTileClayconia.Mini::new);

	public static final Block loonium = new BlockSpecialFlower(Block.Properties.from(pureDaisy), SubTileLoonuim::new);
	public static final Block looniumFloating = new BlockFloatingSpecialFlower(Block.Properties.from(pureDaisyFloating), SubTileLoonuim::new);

	public static final Block daffomill = new BlockSpecialFlower(Block.Properties.from(pureDaisy), SubTileDaffomill::new);
	public static final Block daffomillFloating = new BlockFloatingSpecialFlower(Block.Properties.from(pureDaisyFloating), SubTileDaffomill::new);

	public static final Block vinculotus = new BlockSpecialFlower(Block.Properties.from(pureDaisy), SubTileVinculotus::new);
	public static final Block vinculotusFloating = new BlockFloatingSpecialFlower(Block.Properties.from(pureDaisyFloating), SubTileVinculotus::new);

	public static final Block spectranthemum = new BlockSpecialFlower(Block.Properties.from(pureDaisy), SubTileSpectranthemum::new);
	public static final Block spectranthemumFloating = new BlockFloatingSpecialFlower(Block.Properties.from(pureDaisyFloating), SubTileSpectranthemum::new);

	public static final Block medumone = new BlockSpecialFlower(Block.Properties.from(pureDaisy), SubTileMedumone::new);
	public static final Block medumoneFloating = new BlockFloatingSpecialFlower(Block.Properties.from(pureDaisyFloating), SubTileMedumone::new);

	public static final Block marimorphosis = new BlockSpecialFlower(Block.Properties.from(pureDaisy), SubTileMarimorphosis::new);
	public static final Block marimorphosisChibi = new BlockSpecialFlower(Block.Properties.from(pureDaisyFloating), SubTileMarimorphosis.Mini::new);
	public static final Block marimorphosisFloating = new BlockFloatingSpecialFlower(Block.Properties.from(pureDaisy), SubTileMarimorphosis::new);
	public static final Block marimorphosisChibiFloating = new BlockFloatingSpecialFlower(Block.Properties.from(pureDaisyFloating), SubTileMarimorphosis.Mini::new);

	public static final Block bubbell = new BlockSpecialFlower(Block.Properties.from(pureDaisy), SubTileBubbell::new);
	public static final Block bubbellChibi = new BlockSpecialFlower(Block.Properties.from(pureDaisy), SubTileBubbell.Mini::new);
	public static final Block bubbellFloating = new BlockFloatingSpecialFlower(Block.Properties.from(pureDaisy), SubTileBubbell::new);
	public static final Block bubbellChibiFloating = new BlockFloatingSpecialFlower(Block.Properties.from(pureDaisy), SubTileBubbell.Mini::new);

	public static final Block solegnolia = new BlockSpecialFlower(Block.Properties.from(pureDaisy), SubTileSolegnolia::new);
	public static final Block solegnoliaChibi = new BlockSpecialFlower(Block.Properties.from(pureDaisy), SubTileSolegnolia.Mini::new);
	public static final Block solegnoliaFloating = new BlockFloatingSpecialFlower(Block.Properties.from(pureDaisy), SubTileSolegnolia::new);
	public static final Block solegnoliaChibiFloating = new BlockFloatingSpecialFlower(Block.Properties.from(pureDaisy), SubTileSolegnolia.Mini::new);

	public static final Block orechidIgnem = new BlockSpecialFlower(Block.Properties.from(pureDaisy), SubTileOrechidIgnem::new);
	public static final Block orechidIgnemFloating = new BlockFloatingSpecialFlower(Block.Properties.from(pureDaisyFloating), SubTileOrechidIgnem::new);

	public static final TileEntityType<SubTilePureDaisy> PURE_DAISY = TileEntityType.Builder.create(SubTilePureDaisy::new, pureDaisy, pureDaisyFloating).build(null);
	public static final TileEntityType<SubTileManastar> MANASTAR = TileEntityType.Builder.create(SubTileManastar::new, manastar, manastarFloating).build(null);
	public static final TileEntityType<SubTileHydroangeas> HYDROANGEAS = TileEntityType.Builder.create(SubTileHydroangeas::new, hydroangeas, hydroangeasFloating).build(null);
	public static final TileEntityType<SubTileEndoflame> ENDOFLAME = TileEntityType.Builder.create(SubTileEndoflame::new, endoflame, endoflameFloating).build(null);
	public static final TileEntityType<SubTileThermalily> THERMALILY = TileEntityType.Builder.create(SubTileThermalily::new, thermalily, thermalilyFloating).build(null);
	public static final TileEntityType<SubTileArcaneRose> ROSA_ARCANA = TileEntityType.Builder.create(SubTileArcaneRose::new, rosaArcana, rosaArcanaFloating).build(null);
	public static final TileEntityType<SubTileMunchdew> MUNCHDEW = TileEntityType.Builder.create(SubTileMunchdew::new, munchdew, munchdewFloating).build(null);
	public static final TileEntityType<SubTileEntropinnyum> ENTROPINNYUM = TileEntityType.Builder.create(SubTileEntropinnyum::new, entropinnyum, entropinnyumFloating).build(null);
	public static final TileEntityType<SubTileKekimurus> KEKIMURUS = TileEntityType.Builder.create(SubTileKekimurus::new, kekimurus, kekimurusFloating).build(null);
	public static final TileEntityType<SubTileGourmaryllis> GOURMARYLLIS = TileEntityType.Builder.create(SubTileGourmaryllis::new, gourmaryllis, gourmaryllisFloating).build(null);
	public static final TileEntityType<SubTileNarslimmus> NARSLIMMUS = TileEntityType.Builder.create(SubTileNarslimmus::new, narslimmus, narslimmusFloating).build(null);
	public static final TileEntityType<SubTileSpectrolus> SPECTROLUS = TileEntityType.Builder.create(SubTileSpectrolus::new, spectrolus, spectrolusFloating).build(null);
	public static final TileEntityType<SubTileDandelifeon> DANDELIFEON = TileEntityType.Builder.create(SubTileDandelifeon::new, dandelifeon, dandelifeonFloating).build(null);
	public static final TileEntityType<SubTileRafflowsia> RAFFLOWSIA = TileEntityType.Builder.create(SubTileRafflowsia::new, rafflowsia, rafflowsiaFloating).build(null);
	public static final TileEntityType<SubTileShulkMeNot> SHULK_ME_NOT = TileEntityType.Builder.create(SubTileShulkMeNot::new, shulkMeNot, shulkMeNotFloating).build(null);
	public static final TileEntityType<SubTileBellethorn> BELLETHORNE = TileEntityType.Builder.create(SubTileBellethorn::new, bellethorn, bellethornFloating).build(null);
	public static final TileEntityType<SubTileBellethorn.Mini> BELLETHORNE_CHIBI = TileEntityType.Builder.create(SubTileBellethorn.Mini::new, bellethornChibi, bellethornChibiFloating).build(null);
	public static final TileEntityType<SubTileBergamute> BERGAMUTE = TileEntityType.Builder.create(SubTileBergamute::new, bergamute, bergamuteFloating).build(null);
	public static final TileEntityType<SubTileDreadthorn> DREADTHORN = TileEntityType.Builder.create(SubTileDreadthorn::new, dreadthorn, dreadthornFloating).build(null);
	public static final TileEntityType<SubTileHeiseiDream> HEISEI_DREAM = TileEntityType.Builder.create(SubTileHeiseiDream::new, heiseiDream, heiseiDreamFloating).build(null);
	public static final TileEntityType<SubTileTigerseye> TIGERSEYE = TileEntityType.Builder.create(SubTileTigerseye::new, tigerseye, tigerseyeFloating).build(null);
	public static final TileEntityType<SubTileJadedAmaranthus> JADED_AMARANTHUS = TileEntityType.Builder.create(SubTileJadedAmaranthus::new, jadedAmaranthus, jadedAmaranthusFloating).build(null);
	public static final TileEntityType<SubTileOrechid> ORECHID = TileEntityType.Builder.create(SubTileOrechid::new, orechid, orechidFloating).build(null);
	public static final TileEntityType<SubTileFallenKanade> FALLEN_KANADE = TileEntityType.Builder.create(SubTileFallenKanade::new, fallenKanade, fallenKanadeFloating).build(null);
	public static final TileEntityType<SubTileExoflame> EXOFLAME = TileEntityType.Builder.create(SubTileExoflame::new, exoflame, exoflameFloating).build(null);
	public static final TileEntityType<SubTileAgricarnation> AGRICARNATION = TileEntityType.Builder.create(SubTileAgricarnation::new, agricarnation, agricarnationFloating).build(null);
	public static final TileEntityType<SubTileAgricarnation.Mini> AGRICARNATION_CHIBI = TileEntityType.Builder.create(SubTileAgricarnation.Mini::new, agricarnationChibi, agricarnationChibiFloating).build(null);
	public static final TileEntityType<SubTileHopperhock> HOPPERHOCK = TileEntityType.Builder.create(SubTileHopperhock::new, hopperhock, hopperhockFloating).build(null);
	public static final TileEntityType<SubTileHopperhock.Mini> HOPPERHOCK_CHIBI = TileEntityType.Builder.create(SubTileHopperhock.Mini::new, hopperhockChibi, hopperhockChibiFloating).build(null);
	public static final TileEntityType<SubTileTangleberrie> TANGLEBERRIE = TileEntityType.Builder.create(SubTileTangleberrie::new, tangleberrie, tangleberrieFloating).build(null);
	public static final TileEntityType<SubTileJiyuulia> JIYUULIA = TileEntityType.Builder.create(SubTileJiyuulia::new, jiyuulia, jiyuuliaFloating).build(null);
	public static final TileEntityType<SubTileRannuncarpus> RANNUNCARPUS = TileEntityType.Builder.create(SubTileRannuncarpus::new, rannuncarpus, rannuncarpusFloating).build(null);
	public static final TileEntityType<SubTileRannuncarpus.Mini> RANNUNCARPUS_CHIBI = TileEntityType.Builder.create(SubTileRannuncarpus.Mini::new, rannuncarpusChibi, rannuncarpusChibiFloating).build(null);
	public static final TileEntityType<SubTileHyacidus> HYACIDUS = TileEntityType.Builder.create(SubTileHyacidus::new, hyacidus, hyacidusFloating).build(null);
	public static final TileEntityType<SubTilePollidisiac> POLLIDISIAC = TileEntityType.Builder.create(SubTilePollidisiac::new, pollidisiac, pollidisiacFloating).build(null);
	public static final TileEntityType<SubTileClayconia> CLAYCONIA = TileEntityType.Builder.create(SubTileClayconia::new, clayconia, clayconiaFloating).build(null);
	public static final TileEntityType<SubTileClayconia.Mini> CLAYCONIA_CHIBI = TileEntityType.Builder.create(SubTileClayconia.Mini::new, clayconiaChibi, clayconiaChibiFloating).build(null);
	public static final TileEntityType<SubTileLoonuim> LOONIUM = TileEntityType.Builder.create(SubTileLoonuim::new, loonium, looniumFloating).build(null);
	public static final TileEntityType<SubTileDaffomill> DAFFOMILL = TileEntityType.Builder.create(SubTileDaffomill::new, daffomill, daffomillFloating).build(null);
	public static final TileEntityType<SubTileVinculotus> VINCULOTUS = TileEntityType.Builder.create(SubTileVinculotus::new, vinculotus, vinculotusFloating).build(null);
	public static final TileEntityType<SubTileSpectranthemum> SPECTRANTHEMUM = TileEntityType.Builder.create(SubTileSpectranthemum::new, spectranthemum, spectranthemumFloating).build(null);
	public static final TileEntityType<SubTileMedumone> MEDUMONE = TileEntityType.Builder.create(SubTileMedumone::new, medumone, medumoneFloating).build(null);
	public static final TileEntityType<SubTileMarimorphosis> MARIMORPHOSIS = TileEntityType.Builder.create(SubTileMarimorphosis::new, marimorphosis, marimorphosisFloating).build(null);
	public static final TileEntityType<SubTileMarimorphosis.Mini> MARIMORPHOSIS_CHIBI = TileEntityType.Builder.create(SubTileMarimorphosis.Mini::new, marimorphosisChibi, marimorphosisChibiFloating).build(null);
	public static final TileEntityType<SubTileBubbell> BUBBELL = TileEntityType.Builder.create(SubTileBubbell::new, bubbell, bubbellFloating).build(null);
	public static final TileEntityType<SubTileBubbell.Mini> BUBBELL_CHIBI = TileEntityType.Builder.create(SubTileBubbell.Mini::new, bubbellChibi, bubbellChibiFloating).build(null);
	public static final TileEntityType<SubTileSolegnolia> SOLEGNOLIA = TileEntityType.Builder.create(SubTileSolegnolia::new, solegnolia, solegnoliaFloating).build(null);
	public static final TileEntityType<SubTileSolegnolia.Mini> SOLEGNOLIA_CHIBI = TileEntityType.Builder.create(SubTileSolegnolia.Mini::new, solegnoliaChibi, solegnoliaChibiFloating).build(null);
	public static final TileEntityType<SubTileOrechidIgnem> ORECHID_IGNEM = TileEntityType.Builder.create(SubTileOrechidIgnem::new, orechidIgnem, orechidIgnemFloating).build(null);

	private static ResourceLocation floating(ResourceLocation orig) {
		return new ResourceLocation(orig.getNamespace(), "floating_" + orig.getPath());
	}

	private static ResourceLocation chibi(ResourceLocation orig) {
		return new ResourceLocation(orig.getNamespace(), orig.getPath() + "_chibi");
	}

	public static List<Pair<Supplier<? extends TileEntitySpecialFlower>, ResourceLocation>> getTypes() {
		return ImmutableList.of(
				Pair.of(SubTilePureDaisy::new, LibBlockNames.SUBTILE_PUREDAISY),
				Pair.of(SubTileManastar::new, LibBlockNames.SUBTILE_MANASTAR),
				Pair.of(SubTileEndoflame::new, LibBlockNames.SUBTILE_ENDOFLAME),
				Pair.of(SubTileHydroangeas::new, LibBlockNames.SUBTILE_HYDROANGEAS),
				Pair.of(SubTileThermalily::new, LibBlockNames.SUBTILE_THERMALILY),
				Pair.of(SubTileArcaneRose::new, LibBlockNames.SUBTILE_ARCANE_ROSE),
				Pair.of(SubTileMunchdew::new, LibBlockNames.SUBTILE_MUNCHDEW),
				Pair.of(SubTileEntropinnyum::new, LibBlockNames.SUBTILE_ENTROPINNYUM),
				Pair.of(SubTileKekimurus::new, LibBlockNames.SUBTILE_KEKIMURUS),
				Pair.of(SubTileGourmaryllis::new, LibBlockNames.SUBTILE_GOURMARYLLIS),
				Pair.of(SubTileNarslimmus::new, LibBlockNames.SUBTILE_NARSLIMMUS),
				Pair.of(SubTileSpectrolus::new, LibBlockNames.SUBTILE_SPECTROLUS),
				Pair.of(SubTileDandelifeon::new, LibBlockNames.SUBTILE_DANDELIFEON),
				Pair.of(SubTileRafflowsia::new, LibBlockNames.SUBTILE_RAFFLOWSIA),
				Pair.of(SubTileShulkMeNot::new, LibBlockNames.SUBTILE_SHULK_ME_NOT),
				Pair.of(SubTileBellethorn::new, LibBlockNames.SUBTILE_BELLETHORN),
				Pair.of(SubTileBellethorn.Mini::new, chibi(LibBlockNames.SUBTILE_BELLETHORN)),
				Pair.of(SubTileBergamute::new, LibBlockNames.SUBTILE_BERGAMUTE),
				Pair.of(SubTileDreadthorn::new, LibBlockNames.SUBTILE_DREADTHORN),
				Pair.of(SubTileHeiseiDream::new, LibBlockNames.SUBTILE_HEISEI_DREAM),
				Pair.of(SubTileTigerseye::new, LibBlockNames.SUBTILE_TIGERSEYE),
				Pair.of(SubTileJadedAmaranthus::new, LibBlockNames.SUBTILE_JADED_AMARANTHUS),
				Pair.of(SubTileOrechid::new, LibBlockNames.SUBTILE_ORECHID),
				Pair.of(SubTileFallenKanade::new, LibBlockNames.SUBTILE_FALLEN_KANADE),
				Pair.of(SubTileExoflame::new, LibBlockNames.SUBTILE_EXOFLAME),
				Pair.of(SubTileAgricarnation::new, LibBlockNames.SUBTILE_AGRICARNATION),
				Pair.of(SubTileAgricarnation.Mini::new, chibi(LibBlockNames.SUBTILE_AGRICARNATION)),
				Pair.of(SubTileHopperhock::new, LibBlockNames.SUBTILE_HOPPERHOCK),
				Pair.of(SubTileHopperhock.Mini::new, chibi(LibBlockNames.SUBTILE_HOPPERHOCK)),
				Pair.of(SubTileTangleberrie::new, LibBlockNames.SUBTILE_TANGLEBERRIE),
				Pair.of(SubTileJiyuulia::new, LibBlockNames.SUBTILE_JIYUULIA),
				Pair.of(SubTileRannuncarpus::new, LibBlockNames.SUBTILE_RANNUNCARPUS),
				Pair.of(SubTileRannuncarpus.Mini::new, chibi(LibBlockNames.SUBTILE_RANNUNCARPUS)),
				Pair.of(SubTileHyacidus::new, LibBlockNames.SUBTILE_HYACIDUS),
				Pair.of(SubTilePollidisiac::new, LibBlockNames.SUBTILE_POLLIDISIAC),
				Pair.of(SubTileClayconia::new, LibBlockNames.SUBTILE_CLAYCONIA),
				Pair.of(SubTileClayconia.Mini::new, chibi(LibBlockNames.SUBTILE_CLAYCONIA)),
				Pair.of(SubTileLoonuim::new, LibBlockNames.SUBTILE_LOONIUM),
				Pair.of(SubTileDaffomill::new, LibBlockNames.SUBTILE_DAFFOMILL),
				Pair.of(SubTileVinculotus::new, LibBlockNames.SUBTILE_VINCULOTUS),
				Pair.of(SubTileSpectranthemum::new, LibBlockNames.SUBTILE_SPECTRANTHEMUM),
				Pair.of(SubTileMedumone::new, LibBlockNames.SUBTILE_MEDUMONE),
				Pair.of(SubTileMarimorphosis::new, LibBlockNames.SUBTILE_MARIMORPHOSIS),
				Pair.of(SubTileMarimorphosis.Mini::new, chibi(LibBlockNames.SUBTILE_MARIMORPHOSIS)),
				Pair.of(SubTileBubbell::new, LibBlockNames.SUBTILE_BUBBELL),
				Pair.of(SubTileBubbell.Mini::new, chibi(LibBlockNames.SUBTILE_BUBBELL)),
				Pair.of(SubTileSolegnolia::new, LibBlockNames.SUBTILE_SOLEGNOLIA),
				Pair.of(SubTileSolegnolia.Mini::new, chibi(LibBlockNames.SUBTILE_SOLEGNOLIA)),
				Pair.of(SubTileOrechidIgnem::new, LibBlockNames.SUBTILE_ORECHID_IGNEM)
		);
	}

	public static void registerBlocks(RegistryEvent.Register<Block> evt) {
		IForgeRegistry<Block> r = evt.getRegistry();
		register(r, LibBlockNames.SUBTILE_PUREDAISY, pureDaisy);
		register(r, floating(LibBlockNames.SUBTILE_PUREDAISY), pureDaisyFloating);

		register(r, LibBlockNames.SUBTILE_MANASTAR, manastar);
		register(r, floating(LibBlockNames.SUBTILE_MANASTAR), manastarFloating);

		register(r, LibBlockNames.SUBTILE_HYDROANGEAS, hydroangeas);
		register(r, floating(LibBlockNames.SUBTILE_HYDROANGEAS), hydroangeasFloating);

		register(r, LibBlockNames.SUBTILE_ENDOFLAME, endoflame);
		register(r, floating(LibBlockNames.SUBTILE_ENDOFLAME), endoflameFloating);

		register(r, LibBlockNames.SUBTILE_THERMALILY, thermalily);
		register(r, floating(LibBlockNames.SUBTILE_THERMALILY), thermalilyFloating);

		register(r, LibBlockNames.SUBTILE_ARCANE_ROSE, rosaArcana);
		register(r, floating(LibBlockNames.SUBTILE_ARCANE_ROSE), rosaArcanaFloating);

		register(r, LibBlockNames.SUBTILE_MUNCHDEW, munchdew);
		register(r, floating(LibBlockNames.SUBTILE_MUNCHDEW), munchdewFloating);

		register(r, LibBlockNames.SUBTILE_ENTROPINNYUM, entropinnyum);
		register(r, floating(LibBlockNames.SUBTILE_ENTROPINNYUM), entropinnyumFloating);

		register(r, LibBlockNames.SUBTILE_KEKIMURUS, kekimurus);
		register(r, floating(LibBlockNames.SUBTILE_KEKIMURUS), kekimurusFloating);

		register(r, LibBlockNames.SUBTILE_GOURMARYLLIS, gourmaryllis);
		register(r, floating(LibBlockNames.SUBTILE_GOURMARYLLIS), gourmaryllisFloating);

		register(r, LibBlockNames.SUBTILE_NARSLIMMUS, narslimmus);
		register(r, floating(LibBlockNames.SUBTILE_NARSLIMMUS), narslimmusFloating);

		register(r, LibBlockNames.SUBTILE_SPECTROLUS, spectrolus);
		register(r, floating(LibBlockNames.SUBTILE_SPECTROLUS), spectrolusFloating);

		register(r, LibBlockNames.SUBTILE_DANDELIFEON, dandelifeon);
		register(r, floating(LibBlockNames.SUBTILE_DANDELIFEON), dandelifeonFloating);

		register(r, LibBlockNames.SUBTILE_RAFFLOWSIA, rafflowsia);
		register(r, floating(LibBlockNames.SUBTILE_RAFFLOWSIA), rafflowsiaFloating);

		register(r, LibBlockNames.SUBTILE_SHULK_ME_NOT, shulkMeNot);
		register(r, floating(LibBlockNames.SUBTILE_SHULK_ME_NOT), shulkMeNotFloating);

		register(r, LibBlockNames.SUBTILE_BELLETHORN, bellethorn);
		register(r, chibi(LibBlockNames.SUBTILE_BELLETHORN), bellethornChibi);
		register(r, floating(LibBlockNames.SUBTILE_BELLETHORN), bellethornFloating);
		register(r, chibi(floating(LibBlockNames.SUBTILE_BELLETHORN)), bellethornChibiFloating);

		register(r, LibBlockNames.SUBTILE_BERGAMUTE, bergamute);
		register(r, floating(LibBlockNames.SUBTILE_BERGAMUTE), bergamuteFloating);

		register(r, LibBlockNames.SUBTILE_DREADTHORN, dreadthorn);
		register(r, floating(LibBlockNames.SUBTILE_DREADTHORN), dreadthornFloating);

		register(r, LibBlockNames.SUBTILE_HEISEI_DREAM, heiseiDream);
		register(r, floating(LibBlockNames.SUBTILE_HEISEI_DREAM), heiseiDreamFloating);

		register(r, LibBlockNames.SUBTILE_TIGERSEYE, tigerseye);
		register(r, floating(LibBlockNames.SUBTILE_TIGERSEYE), tigerseyeFloating);

		register(r, LibBlockNames.SUBTILE_JADED_AMARANTHUS, jadedAmaranthus);
		register(r, floating(LibBlockNames.SUBTILE_JADED_AMARANTHUS), jadedAmaranthusFloating);

		register(r, LibBlockNames.SUBTILE_ORECHID, orechid);
		register(r, floating(LibBlockNames.SUBTILE_ORECHID), orechidFloating);

		register(r, LibBlockNames.SUBTILE_FALLEN_KANADE, fallenKanade);
		register(r, floating(LibBlockNames.SUBTILE_FALLEN_KANADE), fallenKanadeFloating);

		register(r, LibBlockNames.SUBTILE_EXOFLAME, exoflame);
		register(r, floating(LibBlockNames.SUBTILE_EXOFLAME), exoflameFloating);

		register(r, LibBlockNames.SUBTILE_AGRICARNATION, agricarnation);
		register(r, chibi(LibBlockNames.SUBTILE_AGRICARNATION), agricarnationChibi);
		register(r, floating(LibBlockNames.SUBTILE_AGRICARNATION), agricarnationFloating);
		register(r, chibi(floating(LibBlockNames.SUBTILE_AGRICARNATION)), agricarnationChibiFloating);

		register(r, LibBlockNames.SUBTILE_HOPPERHOCK, hopperhock);
		register(r, chibi(LibBlockNames.SUBTILE_HOPPERHOCK), hopperhockChibi);
		register(r, floating(LibBlockNames.SUBTILE_HOPPERHOCK), hopperhockFloating);
		register(r, chibi(floating(LibBlockNames.SUBTILE_HOPPERHOCK)), hopperhockChibiFloating);

		register(r, LibBlockNames.SUBTILE_TANGLEBERRIE, tangleberrie);
		register(r, floating(LibBlockNames.SUBTILE_TANGLEBERRIE), tangleberrieFloating);

		register(r, LibBlockNames.SUBTILE_JIYUULIA, jiyuulia);
		register(r, floating(LibBlockNames.SUBTILE_JIYUULIA), jiyuuliaFloating);

		register(r, LibBlockNames.SUBTILE_RANNUNCARPUS, rannuncarpus);
		register(r, chibi(LibBlockNames.SUBTILE_RANNUNCARPUS), rannuncarpusChibi);
		register(r, floating(LibBlockNames.SUBTILE_RANNUNCARPUS), rannuncarpusFloating);
		register(r, chibi(floating(LibBlockNames.SUBTILE_RANNUNCARPUS)), rannuncarpusChibiFloating);

		register(r, LibBlockNames.SUBTILE_HYACIDUS, hyacidus);
		register(r, floating(LibBlockNames.SUBTILE_HYACIDUS), hyacidusFloating);

		register(r, LibBlockNames.SUBTILE_POLLIDISIAC, pollidisiac);
		register(r, floating(LibBlockNames.SUBTILE_POLLIDISIAC), pollidisiacFloating);

		register(r, LibBlockNames.SUBTILE_CLAYCONIA, clayconia);
		register(r, chibi(LibBlockNames.SUBTILE_CLAYCONIA), clayconiaChibi);
		register(r, floating(LibBlockNames.SUBTILE_CLAYCONIA), clayconiaFloating);
		register(r, chibi(floating(LibBlockNames.SUBTILE_CLAYCONIA)), clayconiaChibiFloating);

		register(r, LibBlockNames.SUBTILE_LOONIUM, loonium);
		register(r, floating(LibBlockNames.SUBTILE_LOONIUM), looniumFloating);

		register(r, LibBlockNames.SUBTILE_DAFFOMILL, daffomill);
		register(r, floating(LibBlockNames.SUBTILE_DAFFOMILL), daffomillFloating);

		register(r, LibBlockNames.SUBTILE_VINCULOTUS, vinculotus);
		register(r, floating(LibBlockNames.SUBTILE_VINCULOTUS), vinculotusFloating);

		register(r, LibBlockNames.SUBTILE_SPECTRANTHEMUM, spectranthemum);
		register(r, floating(LibBlockNames.SUBTILE_SPECTRANTHEMUM), spectranthemumFloating);

		register(r, LibBlockNames.SUBTILE_MEDUMONE, medumone);
		register(r, floating(LibBlockNames.SUBTILE_MEDUMONE), medumoneFloating);

		register(r, LibBlockNames.SUBTILE_MARIMORPHOSIS, marimorphosis);
		register(r, chibi(LibBlockNames.SUBTILE_MARIMORPHOSIS), marimorphosisChibi);
		register(r, floating(LibBlockNames.SUBTILE_MARIMORPHOSIS), marimorphosisFloating);
		register(r, chibi(floating(LibBlockNames.SUBTILE_MARIMORPHOSIS)), marimorphosisChibiFloating);

		register(r, LibBlockNames.SUBTILE_BUBBELL, bubbell);
		register(r, chibi(LibBlockNames.SUBTILE_BUBBELL), bubbellChibi);
		register(r, floating(LibBlockNames.SUBTILE_BUBBELL), bubbellFloating);
		register(r, chibi(floating(LibBlockNames.SUBTILE_BUBBELL)), bubbellChibiFloating);

		register(r, LibBlockNames.SUBTILE_SOLEGNOLIA, solegnolia);
		register(r, chibi(LibBlockNames.SUBTILE_SOLEGNOLIA), solegnoliaChibi);
		register(r, floating(LibBlockNames.SUBTILE_SOLEGNOLIA), solegnoliaFloating);
		register(r, chibi(floating(LibBlockNames.SUBTILE_SOLEGNOLIA)), solegnoliaChibiFloating);

		register(r, LibBlockNames.SUBTILE_ORECHID_IGNEM, orechidIgnem);
		register(r, floating(LibBlockNames.SUBTILE_ORECHID_IGNEM), orechidIgnemFloating);
	}

	public static void registerItemBlocks(RegistryEvent.Register<Item> evt) {
		Registry<Block> b = Registry.BLOCK;
		IForgeRegistry<Item> r = evt.getRegistry();
		Item.Properties props = ModItems.defaultBuilder();

		for (Pair<Supplier<? extends TileEntitySpecialFlower>, ResourceLocation> type : getTypes()) {
			Block block = b.getValue(type.getSecond()).get();
			Block floating = b.getValue(floating(type.getSecond())).get();

			register(r, type.getSecond(), new ItemBlockSpecialFlower(block, props));
			register(r, floating(type.getSecond()), new ItemBlockSpecialFlower(floating, props));
		}
	}

	public static void registerTEs(RegistryEvent.Register<TileEntityType<?>> evt) {
		IForgeRegistry<TileEntityType<?>> r = evt.getRegistry();
		register(r, LibBlockNames.SUBTILE_PUREDAISY, PURE_DAISY);
		register(r, LibBlockNames.SUBTILE_MANASTAR, MANASTAR);
		register(r, LibBlockNames.SUBTILE_HYDROANGEAS, HYDROANGEAS);
		register(r, LibBlockNames.SUBTILE_ENDOFLAME, ENDOFLAME);
		register(r, LibBlockNames.SUBTILE_THERMALILY, THERMALILY);
		register(r, LibBlockNames.SUBTILE_ARCANE_ROSE, ROSA_ARCANA);
		register(r, LibBlockNames.SUBTILE_MUNCHDEW, MUNCHDEW);
		register(r, LibBlockNames.SUBTILE_ENTROPINNYUM, ENTROPINNYUM);
		register(r, LibBlockNames.SUBTILE_KEKIMURUS, KEKIMURUS);
		register(r, LibBlockNames.SUBTILE_GOURMARYLLIS, GOURMARYLLIS);
		register(r, LibBlockNames.SUBTILE_NARSLIMMUS, NARSLIMMUS);
		register(r, LibBlockNames.SUBTILE_SPECTROLUS, SPECTROLUS);
		register(r, LibBlockNames.SUBTILE_DANDELIFEON, DANDELIFEON);
		register(r, LibBlockNames.SUBTILE_RAFFLOWSIA, RAFFLOWSIA);
		register(r, LibBlockNames.SUBTILE_SHULK_ME_NOT, SHULK_ME_NOT);
		register(r, LibBlockNames.SUBTILE_BELLETHORN, BELLETHORNE);
		register(r, chibi(LibBlockNames.SUBTILE_BELLETHORN), BELLETHORNE_CHIBI);
		register(r, LibBlockNames.SUBTILE_BERGAMUTE, BERGAMUTE);
		register(r, LibBlockNames.SUBTILE_DREADTHORN, DREADTHORN);
		register(r, LibBlockNames.SUBTILE_HEISEI_DREAM, HEISEI_DREAM);
		register(r, LibBlockNames.SUBTILE_TIGERSEYE, TIGERSEYE);
		register(r, LibBlockNames.SUBTILE_JADED_AMARANTHUS, JADED_AMARANTHUS);
		register(r, LibBlockNames.SUBTILE_ORECHID, ORECHID);
		register(r, LibBlockNames.SUBTILE_FALLEN_KANADE, FALLEN_KANADE);
		register(r, LibBlockNames.SUBTILE_EXOFLAME, EXOFLAME);
		register(r, LibBlockNames.SUBTILE_AGRICARNATION, AGRICARNATION);
		register(r, chibi(LibBlockNames.SUBTILE_AGRICARNATION), AGRICARNATION_CHIBI);
		register(r, LibBlockNames.SUBTILE_HOPPERHOCK, HOPPERHOCK);
		register(r, chibi(LibBlockNames.SUBTILE_HOPPERHOCK), HOPPERHOCK_CHIBI);
		register(r, LibBlockNames.SUBTILE_TANGLEBERRIE, TANGLEBERRIE);
		register(r, LibBlockNames.SUBTILE_JIYUULIA, JIYUULIA);
		register(r, LibBlockNames.SUBTILE_RANNUNCARPUS, RANNUNCARPUS);
		register(r, chibi(LibBlockNames.SUBTILE_RANNUNCARPUS), RANNUNCARPUS_CHIBI);
		register(r, LibBlockNames.SUBTILE_HYACIDUS, HYACIDUS);
		register(r, LibBlockNames.SUBTILE_POLLIDISIAC, POLLIDISIAC);
		register(r, LibBlockNames.SUBTILE_CLAYCONIA, CLAYCONIA);
		register(r, chibi(LibBlockNames.SUBTILE_CLAYCONIA), CLAYCONIA_CHIBI);
		register(r, LibBlockNames.SUBTILE_LOONIUM, LOONIUM);
		register(r, LibBlockNames.SUBTILE_DAFFOMILL, DAFFOMILL);
		register(r, LibBlockNames.SUBTILE_VINCULOTUS, VINCULOTUS);
		register(r, LibBlockNames.SUBTILE_SPECTRANTHEMUM, SPECTRANTHEMUM);
		register(r, LibBlockNames.SUBTILE_MEDUMONE, MEDUMONE);
		register(r, LibBlockNames.SUBTILE_MARIMORPHOSIS, MARIMORPHOSIS);
		register(r, chibi(LibBlockNames.SUBTILE_MARIMORPHOSIS), MARIMORPHOSIS_CHIBI);
		register(r, LibBlockNames.SUBTILE_BUBBELL, BUBBELL);
		register(r, chibi(LibBlockNames.SUBTILE_BUBBELL), BUBBELL_CHIBI);
		register(r, LibBlockNames.SUBTILE_SOLEGNOLIA, SOLEGNOLIA);
		register(r, chibi(LibBlockNames.SUBTILE_SOLEGNOLIA), SOLEGNOLIA_CHIBI);
		register(r, LibBlockNames.SUBTILE_ORECHID_IGNEM, ORECHID_IGNEM);
	}
}
