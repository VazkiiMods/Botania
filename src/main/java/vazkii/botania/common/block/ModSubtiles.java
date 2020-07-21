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

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import vazkii.botania.api.subtile.TileEntitySpecialFlower;
import vazkii.botania.common.block.subtile.SubTileManastar;
import vazkii.botania.common.block.subtile.SubTilePureDaisy;
import vazkii.botania.common.block.subtile.functional.*;
import vazkii.botania.common.block.subtile.generating.*;
import vazkii.botania.common.brew.ModPotions;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

import java.util.List;
import java.util.function.Supplier;

import static vazkii.botania.common.block.ModBlocks.register;

public class ModSubtiles {
	public static final Block pureDaisy = new BlockSpecialFlower(ModPotions.clear, 1, AbstractBlock.Settings.copy(Blocks.POPPY), SubTilePureDaisy::new);
	public static final Block pureDaisyFloating = new BlockFloatingSpecialFlower(AbstractBlock.Settings.of(Material.SOIL).strength(0.5F).sounds(BlockSoundGroup.GRAVEL).lightLevel(s -> 15), SubTilePureDaisy::new);

	public static final Block manastar = new BlockSpecialFlower(StatusEffects.GLOWING, 10, AbstractBlock.Settings.copy(pureDaisy), SubTileManastar::new);
	public static final Block manastarFloating = new BlockFloatingSpecialFlower(AbstractBlock.Settings.copy(pureDaisyFloating), SubTileManastar::new);

	public static final Block hydroangeas = new BlockSpecialFlower(StatusEffects.UNLUCK, 10, AbstractBlock.Settings.copy(pureDaisy), SubTileHydroangeas::new);
	public static final Block hydroangeasFloating = new BlockFloatingSpecialFlower(AbstractBlock.Settings.copy(pureDaisyFloating), SubTileHydroangeas::new);

	public static final Block endoflame = new BlockSpecialFlower(StatusEffects.SLOWNESS, 10, AbstractBlock.Settings.copy(pureDaisy), SubTileEndoflame::new);
	public static final Block endoflameFloating = new BlockFloatingSpecialFlower(AbstractBlock.Settings.copy(pureDaisyFloating), SubTileEndoflame::new);

	public static final Block thermalily = new BlockSpecialFlower(StatusEffects.FIRE_RESISTANCE, 120, AbstractBlock.Settings.copy(pureDaisy), SubTileThermalily::new);
	public static final Block thermalilyFloating = new BlockFloatingSpecialFlower(AbstractBlock.Settings.copy(pureDaisyFloating), SubTileThermalily::new);

	public static final Block rosaArcana = new BlockSpecialFlower(StatusEffects.LUCK, 64, AbstractBlock.Settings.copy(pureDaisy), SubTileArcaneRose::new);
	public static final Block rosaArcanaFloating = new BlockFloatingSpecialFlower(AbstractBlock.Settings.copy(pureDaisyFloating), SubTileArcaneRose::new);

	public static final Block munchdew = new BlockSpecialFlower(StatusEffects.SLOW_FALLING, 300, AbstractBlock.Settings.copy(pureDaisy), SubTileMunchdew::new);
	public static final Block munchdewFloating = new BlockFloatingSpecialFlower(AbstractBlock.Settings.copy(pureDaisyFloating), SubTileMunchdew::new);

	public static final Block entropinnyum = new BlockSpecialFlower(StatusEffects.RESISTANCE, 72, AbstractBlock.Settings.copy(pureDaisy), SubTileEntropinnyum::new);
	public static final Block entropinnyumFloating = new BlockFloatingSpecialFlower(AbstractBlock.Settings.copy(pureDaisyFloating), SubTileEntropinnyum::new);

	public static final Block kekimurus = new BlockSpecialFlower(StatusEffects.SATURATION, 15, AbstractBlock.Settings.copy(pureDaisy), SubTileKekimurus::new);
	public static final Block kekimurusFloating = new BlockFloatingSpecialFlower(AbstractBlock.Settings.copy(pureDaisyFloating), SubTileKekimurus::new);

	public static final Block gourmaryllis = new BlockSpecialFlower(StatusEffects.HUNGER, 180, AbstractBlock.Settings.copy(pureDaisy), SubTileGourmaryllis::new);
	public static final Block gourmaryllisFloating = new BlockFloatingSpecialFlower(AbstractBlock.Settings.copy(pureDaisyFloating), SubTileGourmaryllis::new);

	public static final Block narslimmus = new BlockSpecialFlower(ModPotions.featherfeet, 240, AbstractBlock.Settings.copy(pureDaisy), SubTileNarslimmus::new);
	public static final Block narslimmusFloating = new BlockFloatingSpecialFlower(AbstractBlock.Settings.copy(pureDaisyFloating), SubTileNarslimmus::new);

	public static final Block spectrolus = new BlockSpecialFlower(StatusEffects.BLINDNESS, 240, AbstractBlock.Settings.copy(pureDaisy), SubTileSpectrolus::new);
	public static final Block spectrolusFloating = new BlockFloatingSpecialFlower(AbstractBlock.Settings.copy(pureDaisyFloating), SubTileSpectrolus::new);

	public static final Block dandelifeon = new BlockSpecialFlower(StatusEffects.NAUSEA, 240, AbstractBlock.Settings.copy(pureDaisy), SubTileDandelifeon::new);
	public static final Block dandelifeonFloating = new BlockFloatingSpecialFlower(AbstractBlock.Settings.copy(pureDaisyFloating), SubTileDandelifeon::new);

	public static final Block rafflowsia = new BlockSpecialFlower(StatusEffects.HEALTH_BOOST, 18, AbstractBlock.Settings.copy(pureDaisy), SubTileRafflowsia::new);
	public static final Block rafflowsiaFloating = new BlockFloatingSpecialFlower(AbstractBlock.Settings.copy(pureDaisyFloating), SubTileRafflowsia::new);

	public static final Block shulkMeNot = new BlockSpecialFlower(StatusEffects.LEVITATION, 72, AbstractBlock.Settings.copy(pureDaisy), SubTileShulkMeNot::new);
	public static final Block shulkMeNotFloating = new BlockFloatingSpecialFlower(AbstractBlock.Settings.copy(pureDaisyFloating), SubTileShulkMeNot::new);

	public static final Block bellethorn = new BlockSpecialFlower(StatusEffects.WITHER, 10, AbstractBlock.Settings.copy(pureDaisy), SubTileBellethorn::new);
	public static final Block bellethornChibi = new BlockSpecialFlower(StatusEffects.WITHER, 10, AbstractBlock.Settings.copy(pureDaisy), SubTileBellethorn.Mini::new);
	public static final Block bellethornFloating = new BlockFloatingSpecialFlower(AbstractBlock.Settings.copy(pureDaisy), SubTileBellethorn::new);
	public static final Block bellethornChibiFloating = new BlockFloatingSpecialFlower(AbstractBlock.Settings.copy(pureDaisy), SubTileBellethorn.Mini::new);

	public static final Block bergamute = new BlockSpecialFlower(StatusEffects.BLINDNESS, 10, AbstractBlock.Settings.copy(pureDaisy), SubTileBergamute::new);
	public static final Block bergamuteFloating = new BlockFloatingSpecialFlower(AbstractBlock.Settings.copy(pureDaisyFloating), SubTileBergamute::new);

	public static final Block dreadthorn = new BlockSpecialFlower(StatusEffects.WITHER, 10, AbstractBlock.Settings.copy(pureDaisy), SubTileDreadthorn::new);
	public static final Block dreadthornFloating = new BlockFloatingSpecialFlower(AbstractBlock.Settings.copy(pureDaisyFloating), SubTileDreadthorn::new);

	public static final Block heiseiDream = new BlockSpecialFlower(ModPotions.soulCross, 300, AbstractBlock.Settings.copy(pureDaisy), SubTileHeiseiDream::new);
	public static final Block heiseiDreamFloating = new BlockFloatingSpecialFlower(AbstractBlock.Settings.copy(pureDaisyFloating), SubTileHeiseiDream::new);

	public static final Block tigerseye = new BlockSpecialFlower(StatusEffects.STRENGTH, 90, AbstractBlock.Settings.copy(pureDaisy), SubTileTigerseye::new);
	public static final Block tigerseyeFloating = new BlockFloatingSpecialFlower(AbstractBlock.Settings.copy(pureDaisyFloating), SubTileTigerseye::new);

	public static final Block jadedAmaranthus = new BlockSpecialFlower(StatusEffects.INSTANT_HEALTH, 1, AbstractBlock.Settings.copy(pureDaisy), SubTileJadedAmaranthus::new);
	public static final Block jadedAmaranthusFloating = new BlockFloatingSpecialFlower(AbstractBlock.Settings.copy(pureDaisyFloating), SubTileJadedAmaranthus::new);

	public static final Block orechid = new BlockSpecialFlower(StatusEffects.HASTE, 10, AbstractBlock.Settings.copy(pureDaisy), SubTileOrechid::new);
	public static final Block orechidFloating = new BlockFloatingSpecialFlower(AbstractBlock.Settings.copy(pureDaisyFloating), SubTileOrechid::new);

	public static final Block fallenKanade = new BlockSpecialFlower(StatusEffects.REGENERATION, 90, AbstractBlock.Settings.copy(pureDaisy), SubTileFallenKanade::new);
	public static final Block fallenKanadeFloating = new BlockFloatingSpecialFlower(AbstractBlock.Settings.copy(pureDaisyFloating), SubTileFallenKanade::new);

	public static final Block exoflame = new BlockSpecialFlower(StatusEffects.SPEED, 240, AbstractBlock.Settings.copy(pureDaisy), SubTileExoflame::new);
	public static final Block exoflameFloating = new BlockFloatingSpecialFlower(AbstractBlock.Settings.copy(pureDaisyFloating), SubTileExoflame::new);

	public static final Block agricarnation = new BlockSpecialFlower(StatusEffects.ABSORPTION, 48, AbstractBlock.Settings.copy(pureDaisy), SubTileAgricarnation::new);
	public static final Block agricarnationChibi = new BlockSpecialFlower(StatusEffects.ABSORPTION, 48, AbstractBlock.Settings.copy(pureDaisy), SubTileAgricarnation.Mini::new);
	public static final Block agricarnationFloating = new BlockFloatingSpecialFlower(AbstractBlock.Settings.copy(pureDaisy), SubTileAgricarnation::new);
	public static final Block agricarnationChibiFloating = new BlockFloatingSpecialFlower(AbstractBlock.Settings.copy(pureDaisy), SubTileAgricarnation.Mini::new);

	public static final Block hopperhock = new BlockSpecialFlower(StatusEffects.SPEED, 30, AbstractBlock.Settings.copy(pureDaisy), SubTileHopperhock::new);
	public static final Block hopperhockChibi = new BlockSpecialFlower(StatusEffects.SPEED, 30, AbstractBlock.Settings.copy(pureDaisy), SubTileHopperhock.Mini::new);
	public static final Block hopperhockFloating = new BlockFloatingSpecialFlower(AbstractBlock.Settings.copy(pureDaisy), SubTileHopperhock::new);
	public static final Block hopperhockChibiFloating = new BlockFloatingSpecialFlower(AbstractBlock.Settings.copy(pureDaisy), SubTileHopperhock.Mini::new);

	public static final Block tangleberrie = new BlockSpecialFlower(ModPotions.bloodthrst, 120, AbstractBlock.Settings.copy(pureDaisy), SubTileTangleberrie::new);
	public static final Block tangleberrieFloating = new BlockFloatingSpecialFlower(AbstractBlock.Settings.copy(pureDaisyFloating), SubTileTangleberrie::new);

	public static final Block jiyuulia = new BlockSpecialFlower(ModPotions.emptiness, 120, AbstractBlock.Settings.copy(pureDaisy), SubTileJiyuulia::new);
	public static final Block jiyuuliaFloating = new BlockFloatingSpecialFlower(AbstractBlock.Settings.copy(pureDaisyFloating), SubTileJiyuulia::new);

	public static final Block rannuncarpus = new BlockSpecialFlower(StatusEffects.JUMP_BOOST, 30, AbstractBlock.Settings.copy(pureDaisy), SubTileRannuncarpus::new);
	public static final Block rannuncarpusChibi = new BlockSpecialFlower(StatusEffects.JUMP_BOOST, 30, AbstractBlock.Settings.copy(pureDaisy), SubTileRannuncarpus.Mini::new);
	public static final Block rannuncarpusFloating = new BlockFloatingSpecialFlower(AbstractBlock.Settings.copy(pureDaisy), SubTileRannuncarpus::new);
	public static final Block rannuncarpusChibiFloating = new BlockFloatingSpecialFlower(AbstractBlock.Settings.copy(pureDaisy), SubTileRannuncarpus.Mini::new);

	public static final Block hyacidus = new BlockSpecialFlower(StatusEffects.POISON, 48, AbstractBlock.Settings.copy(pureDaisy), SubTileHyacidus::new);
	public static final Block hyacidusFloating = new BlockFloatingSpecialFlower(AbstractBlock.Settings.copy(pureDaisyFloating), SubTileHyacidus::new);

	public static final Block pollidisiac = new BlockSpecialFlower(StatusEffects.HASTE, 369, AbstractBlock.Settings.copy(pureDaisy), SubTilePollidisiac::new);
	public static final Block pollidisiacFloating = new BlockFloatingSpecialFlower(AbstractBlock.Settings.copy(pureDaisyFloating), SubTilePollidisiac::new);

	public static final Block clayconia = new BlockSpecialFlower(StatusEffects.WEAKNESS, 30, AbstractBlock.Settings.copy(pureDaisy), SubTileClayconia::new);
	public static final Block clayconiaChibi = new BlockSpecialFlower(StatusEffects.WEAKNESS, 30, AbstractBlock.Settings.copy(pureDaisy), SubTileClayconia.Mini::new);
	public static final Block clayconiaFloating = new BlockFloatingSpecialFlower(AbstractBlock.Settings.copy(pureDaisy), SubTileClayconia::new);
	public static final Block clayconiaChibiFloating = new BlockFloatingSpecialFlower(AbstractBlock.Settings.copy(pureDaisy), SubTileClayconia.Mini::new);

	public static final Block loonium = new BlockSpecialFlower(ModPotions.allure, 900, AbstractBlock.Settings.copy(pureDaisy), SubTileLoonuim::new);
	public static final Block looniumFloating = new BlockFloatingSpecialFlower(AbstractBlock.Settings.copy(pureDaisyFloating), SubTileLoonuim::new);

	public static final Block daffomill = new BlockSpecialFlower(StatusEffects.LEVITATION, 6, AbstractBlock.Settings.copy(pureDaisy), SubTileDaffomill::new);
	public static final Block daffomillFloating = new BlockFloatingSpecialFlower(AbstractBlock.Settings.copy(pureDaisyFloating), SubTileDaffomill::new);

	public static final Block vinculotus = new BlockSpecialFlower(StatusEffects.NIGHT_VISION, 900, AbstractBlock.Settings.copy(pureDaisy), SubTileVinculotus::new);
	public static final Block vinculotusFloating = new BlockFloatingSpecialFlower(AbstractBlock.Settings.copy(pureDaisyFloating), SubTileVinculotus::new);

	public static final Block spectranthemum = new BlockSpecialFlower(StatusEffects.INVISIBILITY, 360, AbstractBlock.Settings.copy(pureDaisy), SubTileSpectranthemum::new);
	public static final Block spectranthemumFloating = new BlockFloatingSpecialFlower(AbstractBlock.Settings.copy(pureDaisyFloating), SubTileSpectranthemum::new);

	public static final Block medumone = new BlockSpecialFlower(StatusEffects.SLOWNESS, 3600, AbstractBlock.Settings.copy(pureDaisy), SubTileMedumone::new);
	public static final Block medumoneFloating = new BlockFloatingSpecialFlower(AbstractBlock.Settings.copy(pureDaisyFloating), SubTileMedumone::new);

	public static final Block marimorphosis = new BlockSpecialFlower(StatusEffects.MINING_FATIGUE, 60, AbstractBlock.Settings.copy(pureDaisy), SubTileMarimorphosis::new);
	public static final Block marimorphosisChibi = new BlockSpecialFlower(StatusEffects.MINING_FATIGUE, 60, AbstractBlock.Settings.copy(pureDaisyFloating), SubTileMarimorphosis.Mini::new);
	public static final Block marimorphosisFloating = new BlockFloatingSpecialFlower(AbstractBlock.Settings.copy(pureDaisy), SubTileMarimorphosis::new);
	public static final Block marimorphosisChibiFloating = new BlockFloatingSpecialFlower(AbstractBlock.Settings.copy(pureDaisyFloating), SubTileMarimorphosis.Mini::new);

	public static final Block bubbell = new BlockSpecialFlower(StatusEffects.WATER_BREATHING, 240, AbstractBlock.Settings.copy(pureDaisy), SubTileBubbell::new);
	public static final Block bubbellChibi = new BlockSpecialFlower(StatusEffects.WATER_BREATHING, 240, AbstractBlock.Settings.copy(pureDaisy), SubTileBubbell.Mini::new);
	public static final Block bubbellFloating = new BlockFloatingSpecialFlower(AbstractBlock.Settings.copy(pureDaisy), SubTileBubbell::new);
	public static final Block bubbellChibiFloating = new BlockFloatingSpecialFlower(AbstractBlock.Settings.copy(pureDaisy), SubTileBubbell.Mini::new);

	public static final Block solegnolia = new BlockSpecialFlower(StatusEffects.INSTANT_DAMAGE, 1, AbstractBlock.Settings.copy(pureDaisy), SubTileSolegnolia::new);
	public static final Block solegnoliaChibi = new BlockSpecialFlower(StatusEffects.INSTANT_DAMAGE, 1, AbstractBlock.Settings.copy(pureDaisy), SubTileSolegnolia.Mini::new);
	public static final Block solegnoliaFloating = new BlockFloatingSpecialFlower(AbstractBlock.Settings.copy(pureDaisy), SubTileSolegnolia::new);
	public static final Block solegnoliaChibiFloating = new BlockFloatingSpecialFlower(AbstractBlock.Settings.copy(pureDaisy), SubTileSolegnolia.Mini::new);

	public static final Block orechidIgnem = new BlockSpecialFlower(StatusEffects.FIRE_RESISTANCE, 600, AbstractBlock.Settings.copy(pureDaisy), SubTileOrechidIgnem::new);
	public static final Block orechidIgnemFloating = new BlockFloatingSpecialFlower(AbstractBlock.Settings.copy(pureDaisyFloating), SubTileOrechidIgnem::new);

	public static final BlockEntityType<SubTilePureDaisy> PURE_DAISY = BlockEntityType.Builder.create(SubTilePureDaisy::new, pureDaisy, pureDaisyFloating).build(null);
	public static final BlockEntityType<SubTileManastar> MANASTAR = BlockEntityType.Builder.create(SubTileManastar::new, manastar, manastarFloating).build(null);
	public static final BlockEntityType<SubTileHydroangeas> HYDROANGEAS = BlockEntityType.Builder.create(SubTileHydroangeas::new, hydroangeas, hydroangeasFloating).build(null);
	public static final BlockEntityType<SubTileEndoflame> ENDOFLAME = BlockEntityType.Builder.create(SubTileEndoflame::new, endoflame, endoflameFloating).build(null);
	public static final BlockEntityType<SubTileThermalily> THERMALILY = BlockEntityType.Builder.create(SubTileThermalily::new, thermalily, thermalilyFloating).build(null);
	public static final BlockEntityType<SubTileArcaneRose> ROSA_ARCANA = BlockEntityType.Builder.create(SubTileArcaneRose::new, rosaArcana, rosaArcanaFloating).build(null);
	public static final BlockEntityType<SubTileMunchdew> MUNCHDEW = BlockEntityType.Builder.create(SubTileMunchdew::new, munchdew, munchdewFloating).build(null);
	public static final BlockEntityType<SubTileEntropinnyum> ENTROPINNYUM = BlockEntityType.Builder.create(SubTileEntropinnyum::new, entropinnyum, entropinnyumFloating).build(null);
	public static final BlockEntityType<SubTileKekimurus> KEKIMURUS = BlockEntityType.Builder.create(SubTileKekimurus::new, kekimurus, kekimurusFloating).build(null);
	public static final BlockEntityType<SubTileGourmaryllis> GOURMARYLLIS = BlockEntityType.Builder.create(SubTileGourmaryllis::new, gourmaryllis, gourmaryllisFloating).build(null);
	public static final BlockEntityType<SubTileNarslimmus> NARSLIMMUS = BlockEntityType.Builder.create(SubTileNarslimmus::new, narslimmus, narslimmusFloating).build(null);
	public static final BlockEntityType<SubTileSpectrolus> SPECTROLUS = BlockEntityType.Builder.create(SubTileSpectrolus::new, spectrolus, spectrolusFloating).build(null);
	public static final BlockEntityType<SubTileDandelifeon> DANDELIFEON = BlockEntityType.Builder.create(SubTileDandelifeon::new, dandelifeon, dandelifeonFloating).build(null);
	public static final BlockEntityType<SubTileRafflowsia> RAFFLOWSIA = BlockEntityType.Builder.create(SubTileRafflowsia::new, rafflowsia, rafflowsiaFloating).build(null);
	public static final BlockEntityType<SubTileShulkMeNot> SHULK_ME_NOT = BlockEntityType.Builder.create(SubTileShulkMeNot::new, shulkMeNot, shulkMeNotFloating).build(null);
	public static final BlockEntityType<SubTileBellethorn> BELLETHORNE = BlockEntityType.Builder.create(SubTileBellethorn::new, bellethorn, bellethornFloating).build(null);
	public static final BlockEntityType<SubTileBellethorn.Mini> BELLETHORNE_CHIBI = BlockEntityType.Builder.create(SubTileBellethorn.Mini::new, bellethornChibi, bellethornChibiFloating).build(null);
	public static final BlockEntityType<SubTileBergamute> BERGAMUTE = BlockEntityType.Builder.create(SubTileBergamute::new, bergamute, bergamuteFloating).build(null);
	public static final BlockEntityType<SubTileDreadthorn> DREADTHORN = BlockEntityType.Builder.create(SubTileDreadthorn::new, dreadthorn, dreadthornFloating).build(null);
	public static final BlockEntityType<SubTileHeiseiDream> HEISEI_DREAM = BlockEntityType.Builder.create(SubTileHeiseiDream::new, heiseiDream, heiseiDreamFloating).build(null);
	public static final BlockEntityType<SubTileTigerseye> TIGERSEYE = BlockEntityType.Builder.create(SubTileTigerseye::new, tigerseye, tigerseyeFloating).build(null);
	public static final BlockEntityType<SubTileJadedAmaranthus> JADED_AMARANTHUS = BlockEntityType.Builder.create(SubTileJadedAmaranthus::new, jadedAmaranthus, jadedAmaranthusFloating).build(null);
	public static final BlockEntityType<SubTileOrechid> ORECHID = BlockEntityType.Builder.create(SubTileOrechid::new, orechid, orechidFloating).build(null);
	public static final BlockEntityType<SubTileFallenKanade> FALLEN_KANADE = BlockEntityType.Builder.create(SubTileFallenKanade::new, fallenKanade, fallenKanadeFloating).build(null);
	public static final BlockEntityType<SubTileExoflame> EXOFLAME = BlockEntityType.Builder.create(SubTileExoflame::new, exoflame, exoflameFloating).build(null);
	public static final BlockEntityType<SubTileAgricarnation> AGRICARNATION = BlockEntityType.Builder.create(SubTileAgricarnation::new, agricarnation, agricarnationFloating).build(null);
	public static final BlockEntityType<SubTileAgricarnation.Mini> AGRICARNATION_CHIBI = BlockEntityType.Builder.create(SubTileAgricarnation.Mini::new, agricarnationChibi, agricarnationChibiFloating).build(null);
	public static final BlockEntityType<SubTileHopperhock> HOPPERHOCK = BlockEntityType.Builder.create(SubTileHopperhock::new, hopperhock, hopperhockFloating).build(null);
	public static final BlockEntityType<SubTileHopperhock.Mini> HOPPERHOCK_CHIBI = BlockEntityType.Builder.create(SubTileHopperhock.Mini::new, hopperhockChibi, hopperhockChibiFloating).build(null);
	public static final BlockEntityType<SubTileTangleberrie> TANGLEBERRIE = BlockEntityType.Builder.create(SubTileTangleberrie::new, tangleberrie, tangleberrieFloating).build(null);
	public static final BlockEntityType<SubTileJiyuulia> JIYUULIA = BlockEntityType.Builder.create(SubTileJiyuulia::new, jiyuulia, jiyuuliaFloating).build(null);
	public static final BlockEntityType<SubTileRannuncarpus> RANNUNCARPUS = BlockEntityType.Builder.create(SubTileRannuncarpus::new, rannuncarpus, rannuncarpusFloating).build(null);
	public static final BlockEntityType<SubTileRannuncarpus.Mini> RANNUNCARPUS_CHIBI = BlockEntityType.Builder.create(SubTileRannuncarpus.Mini::new, rannuncarpusChibi, rannuncarpusChibiFloating).build(null);
	public static final BlockEntityType<SubTileHyacidus> HYACIDUS = BlockEntityType.Builder.create(SubTileHyacidus::new, hyacidus, hyacidusFloating).build(null);
	public static final BlockEntityType<SubTilePollidisiac> POLLIDISIAC = BlockEntityType.Builder.create(SubTilePollidisiac::new, pollidisiac, pollidisiacFloating).build(null);
	public static final BlockEntityType<SubTileClayconia> CLAYCONIA = BlockEntityType.Builder.create(SubTileClayconia::new, clayconia, clayconiaFloating).build(null);
	public static final BlockEntityType<SubTileClayconia.Mini> CLAYCONIA_CHIBI = BlockEntityType.Builder.create(SubTileClayconia.Mini::new, clayconiaChibi, clayconiaChibiFloating).build(null);
	public static final BlockEntityType<SubTileLoonuim> LOONIUM = BlockEntityType.Builder.create(SubTileLoonuim::new, loonium, looniumFloating).build(null);
	public static final BlockEntityType<SubTileDaffomill> DAFFOMILL = BlockEntityType.Builder.create(SubTileDaffomill::new, daffomill, daffomillFloating).build(null);
	public static final BlockEntityType<SubTileVinculotus> VINCULOTUS = BlockEntityType.Builder.create(SubTileVinculotus::new, vinculotus, vinculotusFloating).build(null);
	public static final BlockEntityType<SubTileSpectranthemum> SPECTRANTHEMUM = BlockEntityType.Builder.create(SubTileSpectranthemum::new, spectranthemum, spectranthemumFloating).build(null);
	public static final BlockEntityType<SubTileMedumone> MEDUMONE = BlockEntityType.Builder.create(SubTileMedumone::new, medumone, medumoneFloating).build(null);
	public static final BlockEntityType<SubTileMarimorphosis> MARIMORPHOSIS = BlockEntityType.Builder.create(SubTileMarimorphosis::new, marimorphosis, marimorphosisFloating).build(null);
	public static final BlockEntityType<SubTileMarimorphosis.Mini> MARIMORPHOSIS_CHIBI = BlockEntityType.Builder.create(SubTileMarimorphosis.Mini::new, marimorphosisChibi, marimorphosisChibiFloating).build(null);
	public static final BlockEntityType<SubTileBubbell> BUBBELL = BlockEntityType.Builder.create(SubTileBubbell::new, bubbell, bubbellFloating).build(null);
	public static final BlockEntityType<SubTileBubbell.Mini> BUBBELL_CHIBI = BlockEntityType.Builder.create(SubTileBubbell.Mini::new, bubbellChibi, bubbellChibiFloating).build(null);
	public static final BlockEntityType<SubTileSolegnolia> SOLEGNOLIA = BlockEntityType.Builder.create(SubTileSolegnolia::new, solegnolia, solegnoliaFloating).build(null);
	public static final BlockEntityType<SubTileSolegnolia.Mini> SOLEGNOLIA_CHIBI = BlockEntityType.Builder.create(SubTileSolegnolia.Mini::new, solegnoliaChibi, solegnoliaChibiFloating).build(null);
	public static final BlockEntityType<SubTileOrechidIgnem> ORECHID_IGNEM = BlockEntityType.Builder.create(SubTileOrechidIgnem::new, orechidIgnem, orechidIgnemFloating).build(null);

	private static Identifier floating(Identifier orig) {
		return new Identifier(orig.getNamespace(), "floating_" + orig.getPath());
	}

	private static Identifier chibi(Identifier orig) {
		return new Identifier(orig.getNamespace(), orig.getPath() + "_chibi");
	}

	private static Identifier getId(Block b) {
		return Registry.BLOCK.getId(b);
	}

	public static void registerBlocks() {
		Registry<Block> r = Registry.BLOCK;
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

	public static void registerItemBlocks() {
		Registry<Item> r = Registry.ITEM;
		Item.Settings props = ModItems.defaultBuilder();

		register(r, getId(pureDaisy), new ItemBlockSpecialFlower(pureDaisy, props));
		register(r, getId(pureDaisyFloating), new ItemBlockSpecialFlower(pureDaisyFloating, props));

		register(r, getId(manastar), new ItemBlockSpecialFlower(manastar, props));
		register(r, getId(manastarFloating), new ItemBlockSpecialFlower(manastarFloating, props));

		register(r, getId(hydroangeas), new ItemBlockSpecialFlower(hydroangeas, props));
		register(r, getId(hydroangeasFloating), new ItemBlockSpecialFlower(hydroangeasFloating, props));

		register(r, getId(endoflame), new ItemBlockSpecialFlower(endoflame, props));
		register(r, getId(endoflameFloating), new ItemBlockSpecialFlower(endoflameFloating, props));

		register(r, getId(thermalily), new ItemBlockSpecialFlower(thermalily, props));
		register(r, getId(thermalilyFloating), new ItemBlockSpecialFlower(thermalilyFloating, props));

		register(r, getId(rosaArcana), new ItemBlockSpecialFlower(rosaArcana, props));
		register(r, getId(rosaArcanaFloating), new ItemBlockSpecialFlower(rosaArcanaFloating, props));

		register(r, getId(munchdew), new ItemBlockSpecialFlower(munchdew, props));
		register(r, getId(munchdewFloating), new ItemBlockSpecialFlower(munchdewFloating, props));

		register(r, getId(entropinnyum), new ItemBlockSpecialFlower(entropinnyum, props));
		register(r, getId(entropinnyumFloating), new ItemBlockSpecialFlower(entropinnyumFloating, props));

		register(r, getId(kekimurus), new ItemBlockSpecialFlower(kekimurus, props));
		register(r, getId(kekimurusFloating), new ItemBlockSpecialFlower(kekimurusFloating, props));

		register(r, getId(gourmaryllis), new ItemBlockSpecialFlower(gourmaryllis, props));
		register(r, getId(gourmaryllisFloating), new ItemBlockSpecialFlower(gourmaryllisFloating, props));

		register(r, getId(narslimmus), new ItemBlockSpecialFlower(narslimmus, props));
		register(r, getId(narslimmusFloating), new ItemBlockSpecialFlower(narslimmusFloating, props));

		register(r, getId(spectrolus), new ItemBlockSpecialFlower(spectrolus, props));
		register(r, getId(spectrolusFloating), new ItemBlockSpecialFlower(spectrolusFloating, props));

		register(r, getId(dandelifeon), new ItemBlockSpecialFlower(dandelifeon, props));
		register(r, getId(dandelifeonFloating), new ItemBlockSpecialFlower(dandelifeonFloating, props));

		register(r, getId(rafflowsia), new ItemBlockSpecialFlower(rafflowsia, props));
		register(r, getId(rafflowsiaFloating), new ItemBlockSpecialFlower(rafflowsiaFloating, props));

		register(r, getId(shulkMeNot), new ItemBlockSpecialFlower(shulkMeNot, props));
		register(r, getId(shulkMeNotFloating), new ItemBlockSpecialFlower(shulkMeNotFloating, props));

		register(r, getId(bellethorn), new ItemBlockSpecialFlower(bellethorn, props));
		register(r, getId(bellethornChibi), new ItemBlockSpecialFlower(bellethornChibi, props));
		register(r, getId(bellethornFloating), new ItemBlockSpecialFlower(bellethornFloating, props));
		register(r, getId(bellethornChibiFloating), new ItemBlockSpecialFlower(bellethornChibiFloating, props));

		register(r, getId(bergamute), new ItemBlockSpecialFlower(bergamute, props));
		register(r, getId(bergamuteFloating), new ItemBlockSpecialFlower(bergamuteFloating, props));

		register(r, getId(dreadthorn), new ItemBlockSpecialFlower(dreadthorn, props));
		register(r, getId(dreadthornFloating), new ItemBlockSpecialFlower(dreadthornFloating, props));

		register(r, getId(heiseiDream), new ItemBlockSpecialFlower(heiseiDream, props));
		register(r, getId(heiseiDreamFloating), new ItemBlockSpecialFlower(heiseiDreamFloating, props));

		register(r, getId(tigerseye), new ItemBlockSpecialFlower(tigerseye, props));
		register(r, getId(tigerseyeFloating), new ItemBlockSpecialFlower(tigerseyeFloating, props));

		register(r, getId(jadedAmaranthus), new ItemBlockSpecialFlower(jadedAmaranthus, props));
		register(r, getId(jadedAmaranthusFloating), new ItemBlockSpecialFlower(jadedAmaranthusFloating, props));

		register(r, getId(orechid), new ItemBlockSpecialFlower(orechid, props));
		register(r, getId(orechidFloating), new ItemBlockSpecialFlower(orechidFloating, props));

		register(r, getId(fallenKanade), new ItemBlockSpecialFlower(fallenKanade, props));
		register(r, getId(fallenKanadeFloating), new ItemBlockSpecialFlower(fallenKanadeFloating, props));

		register(r, getId(exoflame), new ItemBlockSpecialFlower(exoflame, props));
		register(r, getId(exoflameFloating), new ItemBlockSpecialFlower(exoflameFloating, props));

		register(r, getId(agricarnation), new ItemBlockSpecialFlower(agricarnation, props));
		register(r, getId(agricarnationChibi), new ItemBlockSpecialFlower(agricarnationChibi, props));
		register(r, getId(agricarnationFloating), new ItemBlockSpecialFlower(agricarnationFloating, props));
		register(r, getId(agricarnationChibiFloating), new ItemBlockSpecialFlower(agricarnationChibiFloating, props));

		register(r, getId(hopperhock), new ItemBlockSpecialFlower(hopperhock, props));
		register(r, getId(hopperhockChibi), new ItemBlockSpecialFlower(hopperhockChibi, props));
		register(r, getId(hopperhockFloating), new ItemBlockSpecialFlower(hopperhockFloating, props));
		register(r, getId(hopperhockChibiFloating), new ItemBlockSpecialFlower(hopperhockChibiFloating, props));

		register(r, getId(tangleberrie), new ItemBlockSpecialFlower(tangleberrie, props));
		register(r, getId(tangleberrieFloating), new ItemBlockSpecialFlower(tangleberrieFloating, props));

		register(r, getId(jiyuulia), new ItemBlockSpecialFlower(jiyuulia, props));
		register(r, getId(jiyuuliaFloating), new ItemBlockSpecialFlower(jiyuuliaFloating, props));

		register(r, getId(rannuncarpus), new ItemBlockSpecialFlower(rannuncarpus, props));
		register(r, getId(rannuncarpusChibi), new ItemBlockSpecialFlower(rannuncarpusChibi, props));
		register(r, getId(rannuncarpusFloating), new ItemBlockSpecialFlower(rannuncarpusFloating, props));
		register(r, getId(rannuncarpusChibiFloating), new ItemBlockSpecialFlower(rannuncarpusChibiFloating, props));

		register(r, getId(hyacidus), new ItemBlockSpecialFlower(hyacidus, props));
		register(r, getId(hyacidusFloating), new ItemBlockSpecialFlower(hyacidusFloating, props));

		register(r, getId(pollidisiac), new ItemBlockSpecialFlower(pollidisiac, props));
		register(r, getId(pollidisiacFloating), new ItemBlockSpecialFlower(pollidisiacFloating, props));

		register(r, getId(clayconia), new ItemBlockSpecialFlower(clayconia, props));
		register(r, getId(clayconiaChibi), new ItemBlockSpecialFlower(clayconiaChibi, props));
		register(r, getId(clayconiaFloating), new ItemBlockSpecialFlower(clayconiaFloating, props));
		register(r, getId(clayconiaChibiFloating), new ItemBlockSpecialFlower(clayconiaChibiFloating, props));

		register(r, getId(loonium), new ItemBlockSpecialFlower(loonium, props));
		register(r, getId(looniumFloating), new ItemBlockSpecialFlower(looniumFloating, props));

		register(r, getId(daffomill), new ItemBlockSpecialFlower(daffomill, props));
		register(r, getId(daffomillFloating), new ItemBlockSpecialFlower(daffomillFloating, props));

		register(r, getId(vinculotus), new ItemBlockSpecialFlower(vinculotus, props));
		register(r, getId(vinculotusFloating), new ItemBlockSpecialFlower(vinculotusFloating, props));

		register(r, getId(spectranthemum), new ItemBlockSpecialFlower(spectranthemum, props));
		register(r, getId(spectranthemumFloating), new ItemBlockSpecialFlower(spectranthemumFloating, props));

		register(r, getId(medumone), new ItemBlockSpecialFlower(medumone, props));
		register(r, getId(medumoneFloating), new ItemBlockSpecialFlower(medumoneFloating, props));

		register(r, getId(marimorphosis), new ItemBlockSpecialFlower(marimorphosis, props));
		register(r, getId(marimorphosisChibi), new ItemBlockSpecialFlower(marimorphosisChibi, props));
		register(r, getId(marimorphosisFloating), new ItemBlockSpecialFlower(marimorphosisFloating, props));
		register(r, getId(marimorphosisChibiFloating), new ItemBlockSpecialFlower(marimorphosisChibiFloating, props));

		register(r, getId(bubbell), new ItemBlockSpecialFlower(bubbell, props));
		register(r, getId(bubbellChibi), new ItemBlockSpecialFlower(bubbellChibi, props));
		register(r, getId(bubbellFloating), new ItemBlockSpecialFlower(bubbellFloating, props));
		register(r, getId(bubbellChibiFloating), new ItemBlockSpecialFlower(bubbellChibiFloating, props));

		register(r, getId(solegnolia), new ItemBlockSpecialFlower(solegnolia, props));
		register(r, getId(solegnoliaChibi), new ItemBlockSpecialFlower(solegnoliaChibi, props));
		register(r, getId(solegnoliaFloating), new ItemBlockSpecialFlower(solegnoliaFloating, props));
		register(r, getId(solegnoliaChibiFloating), new ItemBlockSpecialFlower(solegnoliaChibiFloating, props));

		register(r, getId(orechidIgnem), new ItemBlockSpecialFlower(orechidIgnem, props));
		register(r, getId(orechidIgnemFloating), new ItemBlockSpecialFlower(orechidIgnemFloating, props));
	}

	public static void registerTEs() {
		Registry<BlockEntityType<?>> r = Registry.BLOCK_ENTITY_TYPE;
		register(r, getId(pureDaisy), PURE_DAISY);
		register(r, getId(manastar), MANASTAR);
		register(r, getId(hydroangeas), HYDROANGEAS);
		register(r, getId(endoflame), ENDOFLAME);
		register(r, getId(thermalily), THERMALILY);
		register(r, getId(rosaArcana), ROSA_ARCANA);
		register(r, getId(munchdew), MUNCHDEW);
		register(r, getId(entropinnyum), ENTROPINNYUM);
		register(r, getId(kekimurus), KEKIMURUS);
		register(r, getId(gourmaryllis), GOURMARYLLIS);
		register(r, getId(narslimmus), NARSLIMMUS);
		register(r, getId(spectrolus), SPECTROLUS);
		register(r, getId(dandelifeon), DANDELIFEON);
		register(r, getId(rafflowsia), RAFFLOWSIA);
		register(r, getId(shulkMeNot), SHULK_ME_NOT);
		register(r, getId(bellethorn), BELLETHORNE);
		register(r, getId(bellethornChibi), BELLETHORNE_CHIBI);
		register(r, getId(bergamute), BERGAMUTE);
		register(r, getId(dreadthorn), DREADTHORN);
		register(r, getId(heiseiDream), HEISEI_DREAM);
		register(r, getId(tigerseye), TIGERSEYE);
		register(r, getId(jadedAmaranthus), JADED_AMARANTHUS);
		register(r, getId(orechid), ORECHID);
		register(r, getId(fallenKanade), FALLEN_KANADE);
		register(r, getId(exoflame), EXOFLAME);
		register(r, getId(agricarnation), AGRICARNATION);
		register(r, getId(agricarnationChibi), AGRICARNATION_CHIBI);
		register(r, getId(hopperhock), HOPPERHOCK);
		register(r, getId(hopperhockChibi), HOPPERHOCK_CHIBI);
		register(r, getId(tangleberrie), TANGLEBERRIE);
		register(r, getId(jiyuulia), JIYUULIA);
		register(r, getId(rannuncarpus), RANNUNCARPUS);
		register(r, getId(rannuncarpusChibi), RANNUNCARPUS_CHIBI);
		register(r, getId(hyacidus), HYACIDUS);
		register(r, getId(pollidisiac), POLLIDISIAC);
		register(r, getId(clayconia), CLAYCONIA);
		register(r, getId(clayconiaChibi), CLAYCONIA_CHIBI);
		register(r, getId(loonium), LOONIUM);
		register(r, getId(daffomill), DAFFOMILL);
		register(r, getId(vinculotus), VINCULOTUS);
		register(r, getId(spectranthemum), SPECTRANTHEMUM);
		register(r, getId(medumone), MEDUMONE);
		register(r, getId(marimorphosis), MARIMORPHOSIS);
		register(r, getId(marimorphosisChibi), MARIMORPHOSIS_CHIBI);
		register(r, getId(bubbell), BUBBELL);
		register(r, getId(bubbellChibi), BUBBELL_CHIBI);
		register(r, getId(solegnolia), SOLEGNOLIA);
		register(r, getId(solegnoliaChibi), SOLEGNOLIA_CHIBI);
		register(r, getId(orechidIgnem), ORECHID_IGNEM);
	}
}
