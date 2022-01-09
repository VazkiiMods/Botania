/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import vazkii.botania.common.block.subtile.SubTileManastar;
import vazkii.botania.common.block.subtile.SubTilePureDaisy;
import vazkii.botania.common.block.subtile.functional.*;
import vazkii.botania.common.block.subtile.generating.*;
import vazkii.botania.common.brew.ModPotions;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.xplat.IXplatAbstractions;

import java.util.function.BiConsumer;

public class ModSubtiles {
	private static final BlockBehaviour.Properties FLOWER_PROPS = BlockBehaviour.Properties.copy(Blocks.POPPY);
	private static final BlockBehaviour.Properties FLOATING_PROPS = ModBlocks.FLOATING_PROPS;

	public static final Block pureDaisy = new BlockSpecialFlower(ModPotions.clear, 1, FLOWER_PROPS, () -> ModSubtiles.PURE_DAISY);
	public static final Block pureDaisyFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, () -> ModSubtiles.PURE_DAISY);

	public static final Block manastar = new BlockSpecialFlower(MobEffects.GLOWING, 10, FLOWER_PROPS, () -> ModSubtiles.MANASTAR);
	public static final Block manastarFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, () -> ModSubtiles.MANASTAR);

	public static final Block hydroangeas = new BlockSpecialFlower(MobEffects.UNLUCK, 10, FLOWER_PROPS, () -> ModSubtiles.HYDROANGEAS);
	public static final Block hydroangeasFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, () -> ModSubtiles.HYDROANGEAS);

	public static final Block endoflame = new BlockSpecialFlower(MobEffects.MOVEMENT_SLOWDOWN, 10, FLOWER_PROPS, () -> ModSubtiles.ENDOFLAME);
	public static final Block endoflameFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, () -> ModSubtiles.ENDOFLAME);

	public static final Block thermalily = new BlockSpecialFlower(MobEffects.FIRE_RESISTANCE, 120, FLOWER_PROPS, () -> ModSubtiles.THERMALILY);
	public static final Block thermalilyFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, () -> ModSubtiles.THERMALILY);

	public static final Block rosaArcana = new BlockSpecialFlower(MobEffects.LUCK, 64, FLOWER_PROPS, () -> ModSubtiles.ROSA_ARCANA);
	public static final Block rosaArcanaFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, () -> ModSubtiles.ROSA_ARCANA);

	public static final Block munchdew = new BlockSpecialFlower(MobEffects.SLOW_FALLING, 300, FLOWER_PROPS, () -> ModSubtiles.MUNCHDEW);
	public static final Block munchdewFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, () -> ModSubtiles.MUNCHDEW);

	public static final Block entropinnyum = new BlockSpecialFlower(MobEffects.DAMAGE_RESISTANCE, 72, FLOWER_PROPS, () -> ModSubtiles.ENTROPINNYUM);
	public static final Block entropinnyumFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, () -> ModSubtiles.ENTROPINNYUM);

	public static final Block kekimurus = new BlockSpecialFlower(MobEffects.SATURATION, 15, FLOWER_PROPS, () -> ModSubtiles.KEKIMURUS);
	public static final Block kekimurusFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, () -> ModSubtiles.KEKIMURUS);

	public static final Block gourmaryllis = new BlockSpecialFlower(MobEffects.HUNGER, 180, FLOWER_PROPS, () -> ModSubtiles.GOURMARYLLIS);
	public static final Block gourmaryllisFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, () -> ModSubtiles.GOURMARYLLIS);

	public static final Block narslimmus = new BlockSpecialFlower(ModPotions.featherfeet, 240, FLOWER_PROPS, () -> ModSubtiles.NARSLIMMUS);
	public static final Block narslimmusFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, () -> ModSubtiles.NARSLIMMUS);

	public static final Block spectrolus = new BlockSpecialFlower(MobEffects.BLINDNESS, 240, FLOWER_PROPS, () -> ModSubtiles.SPECTROLUS);
	public static final Block spectrolusFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, () -> ModSubtiles.SPECTROLUS);

	public static final Block dandelifeon = new BlockSpecialFlower(MobEffects.CONFUSION, 240, FLOWER_PROPS, () -> ModSubtiles.DANDELIFEON);
	public static final Block dandelifeonFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, () -> ModSubtiles.DANDELIFEON);

	public static final Block rafflowsia = new BlockSpecialFlower(MobEffects.HEALTH_BOOST, 18, FLOWER_PROPS, () -> ModSubtiles.RAFFLOWSIA);
	public static final Block rafflowsiaFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, () -> ModSubtiles.RAFFLOWSIA);

	public static final Block shulkMeNot = new BlockSpecialFlower(MobEffects.LEVITATION, 72, FLOWER_PROPS, () -> ModSubtiles.SHULK_ME_NOT);
	public static final Block shulkMeNotFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, () -> ModSubtiles.SHULK_ME_NOT);

	public static final Block bellethorn = new BlockSpecialFlower(MobEffects.WITHER, 10, FLOWER_PROPS, () -> ModSubtiles.BELLETHORNE);
	public static final Block bellethornChibi = new BlockSpecialFlower(MobEffects.WITHER, 10, FLOWER_PROPS, () -> ModSubtiles.BELLETHORNE_CHIBI);
	public static final Block bellethornFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, () -> ModSubtiles.BELLETHORNE);
	public static final Block bellethornChibiFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, () -> ModSubtiles.BELLETHORNE_CHIBI);

	public static final Block bergamute = new BlockSpecialFlower(MobEffects.BLINDNESS, 10, FLOWER_PROPS, () -> ModSubtiles.BERGAMUTE);
	public static final Block bergamuteFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, () -> ModSubtiles.BERGAMUTE);

	public static final Block dreadthorn = new BlockSpecialFlower(MobEffects.WITHER, 10, FLOWER_PROPS, () -> ModSubtiles.DREADTHORN);
	public static final Block dreadthornFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, () -> ModSubtiles.DREADTHORN);

	public static final Block heiseiDream = new BlockSpecialFlower(ModPotions.soulCross, 300, FLOWER_PROPS, () -> ModSubtiles.HEISEI_DREAM);
	public static final Block heiseiDreamFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, () -> ModSubtiles.HEISEI_DREAM);

	public static final Block tigerseye = new BlockSpecialFlower(MobEffects.DAMAGE_BOOST, 90, FLOWER_PROPS, () -> ModSubtiles.TIGERSEYE);
	public static final Block tigerseyeFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, () -> ModSubtiles.TIGERSEYE);

	public static final Block jadedAmaranthus = new BlockSpecialFlower(MobEffects.HEAL, 1, FLOWER_PROPS, () -> ModSubtiles.JADED_AMARANTHUS);
	public static final Block jadedAmaranthusFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, () -> ModSubtiles.JADED_AMARANTHUS);

	public static final Block orechid = new BlockSpecialFlower(MobEffects.DIG_SPEED, 10, FLOWER_PROPS, () -> ModSubtiles.ORECHID);
	public static final Block orechidFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, () -> ModSubtiles.ORECHID);

	public static final Block fallenKanade = new BlockSpecialFlower(MobEffects.REGENERATION, 90, FLOWER_PROPS, () -> ModSubtiles.FALLEN_KANADE);
	public static final Block fallenKanadeFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, () -> ModSubtiles.FALLEN_KANADE);

	public static final Block exoflame = new BlockSpecialFlower(MobEffects.MOVEMENT_SPEED, 240, FLOWER_PROPS, () -> ModSubtiles.EXOFLAME);
	public static final Block exoflameFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, () -> ModSubtiles.EXOFLAME);

	public static final Block agricarnation = new BlockSpecialFlower(MobEffects.ABSORPTION, 48, FLOWER_PROPS, () -> ModSubtiles.AGRICARNATION);
	public static final Block agricarnationChibi = new BlockSpecialFlower(MobEffects.ABSORPTION, 48, FLOWER_PROPS, () -> ModSubtiles.AGRICARNATION_CHIBI);
	public static final Block agricarnationFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, () -> ModSubtiles.AGRICARNATION);
	public static final Block agricarnationChibiFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, () -> ModSubtiles.AGRICARNATION_CHIBI);

	public static final Block hopperhock = new BlockSpecialFlower(MobEffects.MOVEMENT_SPEED, 30, FLOWER_PROPS, () -> ModSubtiles.HOPPERHOCK);
	public static final Block hopperhockChibi = new BlockSpecialFlower(MobEffects.MOVEMENT_SPEED, 30, FLOWER_PROPS, () -> ModSubtiles.HOPPERHOCK_CHIBI);
	public static final Block hopperhockFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, () -> ModSubtiles.HOPPERHOCK);
	public static final Block hopperhockChibiFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, () -> ModSubtiles.HOPPERHOCK_CHIBI);

	public static final Block tangleberrie = new BlockSpecialFlower(ModPotions.bloodthrst, 120, FLOWER_PROPS, () -> ModSubtiles.TANGLEBERRIE);
	public static final Block tangleberrieChibi = new BlockSpecialFlower(ModPotions.bloodthrst, 120, FLOWER_PROPS, () -> ModSubtiles.TANGLEBERRIE_CHIBI);
	public static final Block tangleberrieFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, () -> ModSubtiles.TANGLEBERRIE);
	public static final Block tangleberrieChibiFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, () -> ModSubtiles.TANGLEBERRIE_CHIBI);

	public static final Block jiyuulia = new BlockSpecialFlower(ModPotions.emptiness, 120, FLOWER_PROPS, () -> ModSubtiles.JIYUULIA);
	public static final Block jiyuuliaChibi = new BlockSpecialFlower(ModPotions.emptiness, 120, FLOWER_PROPS, () -> ModSubtiles.JIYUULIA_CHIBI);
	public static final Block jiyuuliaFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, () -> ModSubtiles.JIYUULIA);
	public static final Block jiyuuliaChibiFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, () -> ModSubtiles.JIYUULIA_CHIBI);

	public static final Block rannuncarpus = new BlockSpecialFlower(MobEffects.JUMP, 30, FLOWER_PROPS, () -> ModSubtiles.RANNUNCARPUS);
	public static final Block rannuncarpusChibi = new BlockSpecialFlower(MobEffects.JUMP, 30, FLOWER_PROPS, () -> ModSubtiles.RANNUNCARPUS_CHIBI);
	public static final Block rannuncarpusFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, () -> ModSubtiles.RANNUNCARPUS);
	public static final Block rannuncarpusChibiFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, () -> ModSubtiles.RANNUNCARPUS_CHIBI);

	public static final Block hyacidus = new BlockSpecialFlower(MobEffects.POISON, 48, FLOWER_PROPS, () -> ModSubtiles.HYACIDUS);
	public static final Block hyacidusFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, () -> ModSubtiles.HYACIDUS);

	public static final Block pollidisiac = new BlockSpecialFlower(MobEffects.DIG_SPEED, 369, FLOWER_PROPS, () -> ModSubtiles.POLLIDISIAC);
	public static final Block pollidisiacFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, () -> ModSubtiles.POLLIDISIAC);

	public static final Block clayconia = new BlockSpecialFlower(MobEffects.WEAKNESS, 30, FLOWER_PROPS, () -> ModSubtiles.CLAYCONIA);
	public static final Block clayconiaChibi = new BlockSpecialFlower(MobEffects.WEAKNESS, 30, FLOWER_PROPS, () -> ModSubtiles.CLAYCONIA_CHIBI);
	public static final Block clayconiaFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, () -> ModSubtiles.CLAYCONIA);
	public static final Block clayconiaChibiFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, () -> ModSubtiles.CLAYCONIA_CHIBI);

	public static final Block loonium = new BlockSpecialFlower(ModPotions.allure, 900, FLOWER_PROPS, () -> ModSubtiles.LOONIUM);
	public static final Block looniumFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, () -> ModSubtiles.LOONIUM);

	public static final Block daffomill = new BlockSpecialFlower(MobEffects.LEVITATION, 6, FLOWER_PROPS, () -> ModSubtiles.DAFFOMILL);
	public static final Block daffomillFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, () -> ModSubtiles.DAFFOMILL);

	public static final Block vinculotus = new BlockSpecialFlower(MobEffects.NIGHT_VISION, 900, FLOWER_PROPS, () -> ModSubtiles.VINCULOTUS);
	public static final Block vinculotusFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, () -> ModSubtiles.VINCULOTUS);

	public static final Block spectranthemum = new BlockSpecialFlower(MobEffects.INVISIBILITY, 360, FLOWER_PROPS, () -> ModSubtiles.SPECTRANTHEMUM);
	public static final Block spectranthemumFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, () -> ModSubtiles.SPECTRANTHEMUM);

	public static final Block medumone = new BlockSpecialFlower(MobEffects.MOVEMENT_SLOWDOWN, 3600, FLOWER_PROPS, () -> ModSubtiles.MEDUMONE);
	public static final Block medumoneFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, () -> ModSubtiles.MEDUMONE);

	public static final Block marimorphosis = new BlockSpecialFlower(MobEffects.DIG_SLOWDOWN, 60, FLOWER_PROPS, () -> ModSubtiles.MARIMORPHOSIS);
	public static final Block marimorphosisChibi = new BlockSpecialFlower(MobEffects.DIG_SLOWDOWN, 60, FLOWER_PROPS, () -> ModSubtiles.MARIMORPHOSIS_CHIBI);
	public static final Block marimorphosisFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, () -> ModSubtiles.MARIMORPHOSIS);
	public static final Block marimorphosisChibiFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, () -> ModSubtiles.MARIMORPHOSIS_CHIBI);

	public static final Block bubbell = new BlockSpecialFlower(MobEffects.WATER_BREATHING, 240, FLOWER_PROPS, () -> ModSubtiles.BUBBELL);
	public static final Block bubbellChibi = new BlockSpecialFlower(MobEffects.WATER_BREATHING, 240, FLOWER_PROPS, () -> ModSubtiles.BUBBELL_CHIBI);
	public static final Block bubbellFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, () -> ModSubtiles.BUBBELL);
	public static final Block bubbellChibiFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, () -> ModSubtiles.BUBBELL_CHIBI);

	public static final Block solegnolia = new BlockSpecialFlower(MobEffects.HARM, 1, FLOWER_PROPS, () -> ModSubtiles.SOLEGNOLIA);
	public static final Block solegnoliaChibi = new BlockSpecialFlower(MobEffects.HARM, 1, FLOWER_PROPS, () -> ModSubtiles.SOLEGNOLIA_CHIBI);
	public static final Block solegnoliaFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, () -> ModSubtiles.SOLEGNOLIA);
	public static final Block solegnoliaChibiFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, () -> ModSubtiles.SOLEGNOLIA_CHIBI);

	public static final Block orechidIgnem = new BlockSpecialFlower(MobEffects.FIRE_RESISTANCE, 600, FLOWER_PROPS, () -> ModSubtiles.ORECHID_IGNEM);
	public static final Block orechidIgnemFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, () -> ModSubtiles.ORECHID_IGNEM);

	public static final Block labellia = new BlockSpecialFlower(MobEffects.FIRE_RESISTANCE, 600, FLOWER_PROPS, () -> ModSubtiles.LABELLIA);
	public static final Block labelliaFloating = new BlockFloatingSpecialFlower(FLOATING_PROPS, () -> ModSubtiles.LABELLIA);

	public static final BlockEntityType<SubTilePureDaisy> PURE_DAISY = IXplatAbstractions.INSTANCE.createBlockEntityType(SubTilePureDaisy::new, pureDaisy, pureDaisyFloating);
	public static final BlockEntityType<SubTileManastar> MANASTAR = IXplatAbstractions.INSTANCE.createBlockEntityType(SubTileManastar::new, manastar, manastarFloating);
	public static final BlockEntityType<SubTileHydroangeas> HYDROANGEAS = IXplatAbstractions.INSTANCE.createBlockEntityType(SubTileHydroangeas::new, hydroangeas, hydroangeasFloating);
	public static final BlockEntityType<SubTileEndoflame> ENDOFLAME = IXplatAbstractions.INSTANCE.createBlockEntityType(SubTileEndoflame::new, endoflame, endoflameFloating);
	public static final BlockEntityType<SubTileThermalily> THERMALILY = IXplatAbstractions.INSTANCE.createBlockEntityType(SubTileThermalily::new, thermalily, thermalilyFloating);
	public static final BlockEntityType<SubTileArcaneRose> ROSA_ARCANA = IXplatAbstractions.INSTANCE.createBlockEntityType(SubTileArcaneRose::new, rosaArcana, rosaArcanaFloating);
	public static final BlockEntityType<SubTileMunchdew> MUNCHDEW = IXplatAbstractions.INSTANCE.createBlockEntityType(SubTileMunchdew::new, munchdew, munchdewFloating);
	public static final BlockEntityType<SubTileEntropinnyum> ENTROPINNYUM = IXplatAbstractions.INSTANCE.createBlockEntityType(SubTileEntropinnyum::new, entropinnyum, entropinnyumFloating);
	public static final BlockEntityType<SubTileKekimurus> KEKIMURUS = IXplatAbstractions.INSTANCE.createBlockEntityType(SubTileKekimurus::new, kekimurus, kekimurusFloating);
	public static final BlockEntityType<SubTileGourmaryllis> GOURMARYLLIS = IXplatAbstractions.INSTANCE.createBlockEntityType(SubTileGourmaryllis::new, gourmaryllis, gourmaryllisFloating);
	public static final BlockEntityType<SubTileNarslimmus> NARSLIMMUS = IXplatAbstractions.INSTANCE.createBlockEntityType(SubTileNarslimmus::new, narslimmus, narslimmusFloating);
	public static final BlockEntityType<SubTileSpectrolus> SPECTROLUS = IXplatAbstractions.INSTANCE.createBlockEntityType(SubTileSpectrolus::new, spectrolus, spectrolusFloating);
	public static final BlockEntityType<SubTileDandelifeon> DANDELIFEON = IXplatAbstractions.INSTANCE.createBlockEntityType(SubTileDandelifeon::new, dandelifeon, dandelifeonFloating);
	public static final BlockEntityType<SubTileRafflowsia> RAFFLOWSIA = IXplatAbstractions.INSTANCE.createBlockEntityType(SubTileRafflowsia::new, rafflowsia, rafflowsiaFloating);
	public static final BlockEntityType<SubTileShulkMeNot> SHULK_ME_NOT = IXplatAbstractions.INSTANCE.createBlockEntityType(SubTileShulkMeNot::new, shulkMeNot, shulkMeNotFloating);
	public static final BlockEntityType<SubTileBellethorn> BELLETHORNE = IXplatAbstractions.INSTANCE.createBlockEntityType(SubTileBellethorn::new, bellethorn, bellethornFloating);
	public static final BlockEntityType<SubTileBellethorn.Mini> BELLETHORNE_CHIBI = IXplatAbstractions.INSTANCE.createBlockEntityType(SubTileBellethorn.Mini::new, bellethornChibi, bellethornChibiFloating);
	public static final BlockEntityType<SubTileBergamute> BERGAMUTE = IXplatAbstractions.INSTANCE.createBlockEntityType(SubTileBergamute::new, bergamute, bergamuteFloating);
	public static final BlockEntityType<SubTileDreadthorn> DREADTHORN = IXplatAbstractions.INSTANCE.createBlockEntityType(SubTileDreadthorn::new, dreadthorn, dreadthornFloating);
	public static final BlockEntityType<SubTileHeiseiDream> HEISEI_DREAM = IXplatAbstractions.INSTANCE.createBlockEntityType(SubTileHeiseiDream::new, heiseiDream, heiseiDreamFloating);
	public static final BlockEntityType<SubTileTigerseye> TIGERSEYE = IXplatAbstractions.INSTANCE.createBlockEntityType(SubTileTigerseye::new, tigerseye, tigerseyeFloating);
	public static final BlockEntityType<SubTileJadedAmaranthus> JADED_AMARANTHUS = IXplatAbstractions.INSTANCE.createBlockEntityType(SubTileJadedAmaranthus::new, jadedAmaranthus, jadedAmaranthusFloating);
	public static final BlockEntityType<SubTileOrechid> ORECHID = IXplatAbstractions.INSTANCE.createBlockEntityType(SubTileOrechid::new, orechid, orechidFloating);
	public static final BlockEntityType<SubTileFallenKanade> FALLEN_KANADE = IXplatAbstractions.INSTANCE.createBlockEntityType(SubTileFallenKanade::new, fallenKanade, fallenKanadeFloating);
	public static final BlockEntityType<SubTileExoflame> EXOFLAME = IXplatAbstractions.INSTANCE.createBlockEntityType(SubTileExoflame::new, exoflame, exoflameFloating);
	public static final BlockEntityType<SubTileAgricarnation> AGRICARNATION = IXplatAbstractions.INSTANCE.createBlockEntityType(SubTileAgricarnation::new, agricarnation, agricarnationFloating);
	public static final BlockEntityType<SubTileAgricarnation.Mini> AGRICARNATION_CHIBI = IXplatAbstractions.INSTANCE.createBlockEntityType(SubTileAgricarnation.Mini::new, agricarnationChibi, agricarnationChibiFloating);
	public static final BlockEntityType<SubTileHopperhock> HOPPERHOCK = IXplatAbstractions.INSTANCE.createBlockEntityType(SubTileHopperhock::new, hopperhock, hopperhockFloating);
	public static final BlockEntityType<SubTileHopperhock.Mini> HOPPERHOCK_CHIBI = IXplatAbstractions.INSTANCE.createBlockEntityType(SubTileHopperhock.Mini::new, hopperhockChibi, hopperhockChibiFloating);
	public static final BlockEntityType<SubTileTangleberrie> TANGLEBERRIE = IXplatAbstractions.INSTANCE.createBlockEntityType(SubTileTangleberrie::new, tangleberrie, tangleberrieFloating);
	public static final BlockEntityType<SubTileTangleberrie.Mini> TANGLEBERRIE_CHIBI = IXplatAbstractions.INSTANCE.createBlockEntityType(SubTileTangleberrie.Mini::new, tangleberrieChibi, tangleberrieChibiFloating);
	public static final BlockEntityType<SubTileJiyuulia> JIYUULIA = IXplatAbstractions.INSTANCE.createBlockEntityType(SubTileJiyuulia::new, jiyuulia, jiyuuliaFloating);
	public static final BlockEntityType<SubTileJiyuulia.Mini> JIYUULIA_CHIBI = IXplatAbstractions.INSTANCE.createBlockEntityType(SubTileJiyuulia.Mini::new, jiyuuliaChibi, jiyuuliaChibiFloating);
	public static final BlockEntityType<SubTileRannuncarpus> RANNUNCARPUS = IXplatAbstractions.INSTANCE.createBlockEntityType(SubTileRannuncarpus::new, rannuncarpus, rannuncarpusFloating);
	public static final BlockEntityType<SubTileRannuncarpus.Mini> RANNUNCARPUS_CHIBI = IXplatAbstractions.INSTANCE.createBlockEntityType(SubTileRannuncarpus.Mini::new, rannuncarpusChibi, rannuncarpusChibiFloating);
	public static final BlockEntityType<SubTileHyacidus> HYACIDUS = IXplatAbstractions.INSTANCE.createBlockEntityType(SubTileHyacidus::new, hyacidus, hyacidusFloating);
	public static final BlockEntityType<SubTileLabellia> LABELLIA = IXplatAbstractions.INSTANCE.createBlockEntityType(SubTileLabellia::new, labellia, labelliaFloating);
	public static final BlockEntityType<SubTilePollidisiac> POLLIDISIAC = IXplatAbstractions.INSTANCE.createBlockEntityType(SubTilePollidisiac::new, pollidisiac, pollidisiacFloating);
	public static final BlockEntityType<SubTileClayconia> CLAYCONIA = IXplatAbstractions.INSTANCE.createBlockEntityType(SubTileClayconia::new, clayconia, clayconiaFloating);
	public static final BlockEntityType<SubTileClayconia.Mini> CLAYCONIA_CHIBI = IXplatAbstractions.INSTANCE.createBlockEntityType(SubTileClayconia.Mini::new, clayconiaChibi, clayconiaChibiFloating);
	public static final BlockEntityType<SubTileLoonuim> LOONIUM = IXplatAbstractions.INSTANCE.createBlockEntityType(SubTileLoonuim::new, loonium, looniumFloating);
	public static final BlockEntityType<SubTileDaffomill> DAFFOMILL = IXplatAbstractions.INSTANCE.createBlockEntityType(SubTileDaffomill::new, daffomill, daffomillFloating);
	public static final BlockEntityType<SubTileVinculotus> VINCULOTUS = IXplatAbstractions.INSTANCE.createBlockEntityType(SubTileVinculotus::new, vinculotus, vinculotusFloating);
	public static final BlockEntityType<SubTileSpectranthemum> SPECTRANTHEMUM = IXplatAbstractions.INSTANCE.createBlockEntityType(SubTileSpectranthemum::new, spectranthemum, spectranthemumFloating);
	public static final BlockEntityType<SubTileMedumone> MEDUMONE = IXplatAbstractions.INSTANCE.createBlockEntityType(SubTileMedumone::new, medumone, medumoneFloating);
	public static final BlockEntityType<SubTileMarimorphosis> MARIMORPHOSIS = IXplatAbstractions.INSTANCE.createBlockEntityType(SubTileMarimorphosis::new, marimorphosis, marimorphosisFloating);
	public static final BlockEntityType<SubTileMarimorphosis.Mini> MARIMORPHOSIS_CHIBI = IXplatAbstractions.INSTANCE.createBlockEntityType(SubTileMarimorphosis.Mini::new, marimorphosisChibi, marimorphosisChibiFloating);
	public static final BlockEntityType<SubTileBubbell> BUBBELL = IXplatAbstractions.INSTANCE.createBlockEntityType(SubTileBubbell::new, bubbell, bubbellFloating);
	public static final BlockEntityType<SubTileBubbell.Mini> BUBBELL_CHIBI = IXplatAbstractions.INSTANCE.createBlockEntityType(SubTileBubbell.Mini::new, bubbellChibi, bubbellChibiFloating);
	public static final BlockEntityType<SubTileSolegnolia> SOLEGNOLIA = IXplatAbstractions.INSTANCE.createBlockEntityType(SubTileSolegnolia::new, solegnolia, solegnoliaFloating);
	public static final BlockEntityType<SubTileSolegnolia.Mini> SOLEGNOLIA_CHIBI = IXplatAbstractions.INSTANCE.createBlockEntityType(SubTileSolegnolia.Mini::new, solegnoliaChibi, solegnoliaChibiFloating);
	public static final BlockEntityType<SubTileOrechidIgnem> ORECHID_IGNEM = IXplatAbstractions.INSTANCE.createBlockEntityType(SubTileOrechidIgnem::new, orechidIgnem, orechidIgnemFloating);

	private static ResourceLocation floating(ResourceLocation orig) {
		return new ResourceLocation(orig.getNamespace(), "floating_" + orig.getPath());
	}

	private static ResourceLocation chibi(ResourceLocation orig) {
		return new ResourceLocation(orig.getNamespace(), orig.getPath() + "_chibi");
	}

	private static ResourceLocation getId(Block b) {
		return Registry.BLOCK.getKey(b);
	}

	public static void registerBlocks(BiConsumer<Block, ResourceLocation> r) {
		r.accept(pureDaisy, LibBlockNames.SUBTILE_PUREDAISY);
		r.accept(pureDaisyFloating, floating(LibBlockNames.SUBTILE_PUREDAISY));

		r.accept(manastar, LibBlockNames.SUBTILE_MANASTAR);
		r.accept(manastarFloating, floating(LibBlockNames.SUBTILE_MANASTAR));

		r.accept(hydroangeas, LibBlockNames.SUBTILE_HYDROANGEAS);
		r.accept(hydroangeasFloating, floating(LibBlockNames.SUBTILE_HYDROANGEAS));

		r.accept(endoflame, LibBlockNames.SUBTILE_ENDOFLAME);
		r.accept(endoflameFloating, floating(LibBlockNames.SUBTILE_ENDOFLAME));

		r.accept(thermalily, LibBlockNames.SUBTILE_THERMALILY);
		r.accept(thermalilyFloating, floating(LibBlockNames.SUBTILE_THERMALILY));

		r.accept(rosaArcana, LibBlockNames.SUBTILE_ARCANE_ROSE);
		r.accept(rosaArcanaFloating, floating(LibBlockNames.SUBTILE_ARCANE_ROSE));

		r.accept(munchdew, LibBlockNames.SUBTILE_MUNCHDEW);
		r.accept(munchdewFloating, floating(LibBlockNames.SUBTILE_MUNCHDEW));

		r.accept(entropinnyum, LibBlockNames.SUBTILE_ENTROPINNYUM);
		r.accept(entropinnyumFloating, floating(LibBlockNames.SUBTILE_ENTROPINNYUM));

		r.accept(kekimurus, LibBlockNames.SUBTILE_KEKIMURUS);
		r.accept(kekimurusFloating, floating(LibBlockNames.SUBTILE_KEKIMURUS));

		r.accept(gourmaryllis, LibBlockNames.SUBTILE_GOURMARYLLIS);
		r.accept(gourmaryllisFloating, floating(LibBlockNames.SUBTILE_GOURMARYLLIS));

		r.accept(narslimmus, LibBlockNames.SUBTILE_NARSLIMMUS);
		r.accept(narslimmusFloating, floating(LibBlockNames.SUBTILE_NARSLIMMUS));

		r.accept(spectrolus, LibBlockNames.SUBTILE_SPECTROLUS);
		r.accept(spectrolusFloating, floating(LibBlockNames.SUBTILE_SPECTROLUS));

		r.accept(dandelifeon, LibBlockNames.SUBTILE_DANDELIFEON);
		r.accept(dandelifeonFloating, floating(LibBlockNames.SUBTILE_DANDELIFEON));

		r.accept(rafflowsia, LibBlockNames.SUBTILE_RAFFLOWSIA);
		r.accept(rafflowsiaFloating, floating(LibBlockNames.SUBTILE_RAFFLOWSIA));

		r.accept(shulkMeNot, LibBlockNames.SUBTILE_SHULK_ME_NOT);
		r.accept(shulkMeNotFloating, floating(LibBlockNames.SUBTILE_SHULK_ME_NOT));

		r.accept(bellethorn, LibBlockNames.SUBTILE_BELLETHORN);
		r.accept(bellethornChibi, chibi(LibBlockNames.SUBTILE_BELLETHORN));
		r.accept(bellethornFloating, floating(LibBlockNames.SUBTILE_BELLETHORN));
		r.accept(bellethornChibiFloating, chibi(floating(LibBlockNames.SUBTILE_BELLETHORN)));

		r.accept(bergamute, LibBlockNames.SUBTILE_BERGAMUTE);
		r.accept(bergamuteFloating, floating(LibBlockNames.SUBTILE_BERGAMUTE));

		r.accept(dreadthorn, LibBlockNames.SUBTILE_DREADTHORN);
		r.accept(dreadthornFloating, floating(LibBlockNames.SUBTILE_DREADTHORN));

		r.accept(heiseiDream, LibBlockNames.SUBTILE_HEISEI_DREAM);
		r.accept(heiseiDreamFloating, floating(LibBlockNames.SUBTILE_HEISEI_DREAM));

		r.accept(tigerseye, LibBlockNames.SUBTILE_TIGERSEYE);
		r.accept(tigerseyeFloating, floating(LibBlockNames.SUBTILE_TIGERSEYE));

		r.accept(jadedAmaranthus, LibBlockNames.SUBTILE_JADED_AMARANTHUS);
		r.accept(jadedAmaranthusFloating, floating(LibBlockNames.SUBTILE_JADED_AMARANTHUS));

		r.accept(orechid, LibBlockNames.SUBTILE_ORECHID);
		r.accept(orechidFloating, floating(LibBlockNames.SUBTILE_ORECHID));

		r.accept(fallenKanade, LibBlockNames.SUBTILE_FALLEN_KANADE);
		r.accept(fallenKanadeFloating, floating(LibBlockNames.SUBTILE_FALLEN_KANADE));

		r.accept(exoflame, LibBlockNames.SUBTILE_EXOFLAME);
		r.accept(exoflameFloating, floating(LibBlockNames.SUBTILE_EXOFLAME));

		r.accept(agricarnation, LibBlockNames.SUBTILE_AGRICARNATION);
		r.accept(agricarnationChibi, chibi(LibBlockNames.SUBTILE_AGRICARNATION));
		r.accept(agricarnationFloating, floating(LibBlockNames.SUBTILE_AGRICARNATION));
		r.accept(agricarnationChibiFloating, chibi(floating(LibBlockNames.SUBTILE_AGRICARNATION)));

		r.accept(hopperhock, LibBlockNames.SUBTILE_HOPPERHOCK);
		r.accept(hopperhockChibi, chibi(LibBlockNames.SUBTILE_HOPPERHOCK));
		r.accept(hopperhockFloating, floating(LibBlockNames.SUBTILE_HOPPERHOCK));
		r.accept(hopperhockChibiFloating, chibi(floating(LibBlockNames.SUBTILE_HOPPERHOCK)));

		r.accept(tangleberrie, LibBlockNames.SUBTILE_TANGLEBERRIE);
		r.accept(tangleberrieChibi, chibi(LibBlockNames.SUBTILE_TANGLEBERRIE));
		r.accept(tangleberrieFloating, floating(LibBlockNames.SUBTILE_TANGLEBERRIE));
		r.accept(tangleberrieChibiFloating, chibi(floating(LibBlockNames.SUBTILE_TANGLEBERRIE)));

		r.accept(jiyuulia, LibBlockNames.SUBTILE_JIYUULIA);
		r.accept(jiyuuliaChibi, chibi(LibBlockNames.SUBTILE_JIYUULIA));
		r.accept(jiyuuliaFloating, floating(LibBlockNames.SUBTILE_JIYUULIA));
		r.accept(jiyuuliaChibiFloating, chibi(floating(LibBlockNames.SUBTILE_JIYUULIA)));

		r.accept(rannuncarpus, LibBlockNames.SUBTILE_RANNUNCARPUS);
		r.accept(rannuncarpusChibi, chibi(LibBlockNames.SUBTILE_RANNUNCARPUS));
		r.accept(rannuncarpusFloating, floating(LibBlockNames.SUBTILE_RANNUNCARPUS));
		r.accept(rannuncarpusChibiFloating, chibi(floating(LibBlockNames.SUBTILE_RANNUNCARPUS)));

		r.accept(hyacidus, LibBlockNames.SUBTILE_HYACIDUS);
		r.accept(hyacidusFloating, floating(LibBlockNames.SUBTILE_HYACIDUS));

		r.accept(pollidisiac, LibBlockNames.SUBTILE_POLLIDISIAC);
		r.accept(pollidisiacFloating, floating(LibBlockNames.SUBTILE_POLLIDISIAC));

		r.accept(clayconia, LibBlockNames.SUBTILE_CLAYCONIA);
		r.accept(clayconiaChibi, chibi(LibBlockNames.SUBTILE_CLAYCONIA));
		r.accept(clayconiaFloating, floating(LibBlockNames.SUBTILE_CLAYCONIA));
		r.accept(clayconiaChibiFloating, chibi(floating(LibBlockNames.SUBTILE_CLAYCONIA)));

		r.accept(loonium, LibBlockNames.SUBTILE_LOONIUM);
		r.accept(looniumFloating, floating(LibBlockNames.SUBTILE_LOONIUM));

		r.accept(daffomill, LibBlockNames.SUBTILE_DAFFOMILL);
		r.accept(daffomillFloating, floating(LibBlockNames.SUBTILE_DAFFOMILL));

		r.accept(vinculotus, LibBlockNames.SUBTILE_VINCULOTUS);
		r.accept(vinculotusFloating, floating(LibBlockNames.SUBTILE_VINCULOTUS));

		r.accept(spectranthemum, LibBlockNames.SUBTILE_SPECTRANTHEMUM);
		r.accept(spectranthemumFloating, floating(LibBlockNames.SUBTILE_SPECTRANTHEMUM));

		r.accept(medumone, LibBlockNames.SUBTILE_MEDUMONE);
		r.accept(medumoneFloating, floating(LibBlockNames.SUBTILE_MEDUMONE));

		r.accept(marimorphosis, LibBlockNames.SUBTILE_MARIMORPHOSIS);
		r.accept(marimorphosisChibi, chibi(LibBlockNames.SUBTILE_MARIMORPHOSIS));
		r.accept(marimorphosisFloating, floating(LibBlockNames.SUBTILE_MARIMORPHOSIS));
		r.accept(marimorphosisChibiFloating, chibi(floating(LibBlockNames.SUBTILE_MARIMORPHOSIS)));

		r.accept(bubbell, LibBlockNames.SUBTILE_BUBBELL);
		r.accept(bubbellChibi, chibi(LibBlockNames.SUBTILE_BUBBELL));
		r.accept(bubbellFloating, floating(LibBlockNames.SUBTILE_BUBBELL));
		r.accept(bubbellChibiFloating, chibi(floating(LibBlockNames.SUBTILE_BUBBELL)));

		r.accept(solegnolia, LibBlockNames.SUBTILE_SOLEGNOLIA);
		r.accept(solegnoliaChibi, chibi(LibBlockNames.SUBTILE_SOLEGNOLIA));
		r.accept(solegnoliaFloating, floating(LibBlockNames.SUBTILE_SOLEGNOLIA));
		r.accept(solegnoliaChibiFloating, chibi(floating(LibBlockNames.SUBTILE_SOLEGNOLIA)));

		r.accept(orechidIgnem, LibBlockNames.SUBTILE_ORECHID_IGNEM);
		r.accept(orechidIgnemFloating, floating(LibBlockNames.SUBTILE_ORECHID_IGNEM));

		r.accept(labellia, LibBlockNames.SUBTILE_LABELLIA);
		r.accept(labelliaFloating, floating(LibBlockNames.SUBTILE_LABELLIA));
	}

	public static void registerItemBlocks(BiConsumer<Item, ResourceLocation> r) {
		Item.Properties props = ModItems.defaultBuilder();

		r.accept(new ItemBlockSpecialFlower(pureDaisy, props), getId(pureDaisy));
		r.accept(new ItemBlockSpecialFlower(pureDaisyFloating, props), getId(pureDaisyFloating));

		r.accept(new ItemBlockSpecialFlower(manastar, props), getId(manastar));
		r.accept(new ItemBlockSpecialFlower(manastarFloating, props), getId(manastarFloating));

		r.accept(new ItemBlockSpecialFlower(hydroangeas, props), getId(hydroangeas));
		r.accept(new ItemBlockSpecialFlower(hydroangeasFloating, props), getId(hydroangeasFloating));

		r.accept(new ItemBlockSpecialFlower(endoflame, props), getId(endoflame));
		r.accept(new ItemBlockSpecialFlower(endoflameFloating, props), getId(endoflameFloating));

		r.accept(new ItemBlockSpecialFlower(thermalily, props), getId(thermalily));
		r.accept(new ItemBlockSpecialFlower(thermalilyFloating, props), getId(thermalilyFloating));

		r.accept(new ItemBlockSpecialFlower(rosaArcana, props), getId(rosaArcana));
		r.accept(new ItemBlockSpecialFlower(rosaArcanaFloating, props), getId(rosaArcanaFloating));

		r.accept(new ItemBlockSpecialFlower(munchdew, props), getId(munchdew));
		r.accept(new ItemBlockSpecialFlower(munchdewFloating, props), getId(munchdewFloating));

		r.accept(new ItemBlockSpecialFlower(entropinnyum, props), getId(entropinnyum));
		r.accept(new ItemBlockSpecialFlower(entropinnyumFloating, props), getId(entropinnyumFloating));

		r.accept(new ItemBlockSpecialFlower(kekimurus, props), getId(kekimurus));
		r.accept(new ItemBlockSpecialFlower(kekimurusFloating, props), getId(kekimurusFloating));

		r.accept(new ItemBlockSpecialFlower(gourmaryllis, props), getId(gourmaryllis));
		r.accept(new ItemBlockSpecialFlower(gourmaryllisFloating, props), getId(gourmaryllisFloating));

		r.accept(new ItemBlockSpecialFlower(narslimmus, props), getId(narslimmus));
		r.accept(new ItemBlockSpecialFlower(narslimmusFloating, props), getId(narslimmusFloating));

		r.accept(new ItemBlockSpecialFlower(spectrolus, props), getId(spectrolus));
		r.accept(new ItemBlockSpecialFlower(spectrolusFloating, props), getId(spectrolusFloating));

		r.accept(new ItemBlockSpecialFlower(dandelifeon, props), getId(dandelifeon));
		r.accept(new ItemBlockSpecialFlower(dandelifeonFloating, props), getId(dandelifeonFloating));

		r.accept(new ItemBlockSpecialFlower(rafflowsia, props), getId(rafflowsia));
		r.accept(new ItemBlockSpecialFlower(rafflowsiaFloating, props), getId(rafflowsiaFloating));

		r.accept(new ItemBlockSpecialFlower(shulkMeNot, props), getId(shulkMeNot));
		r.accept(new ItemBlockSpecialFlower(shulkMeNotFloating, props), getId(shulkMeNotFloating));

		r.accept(new ItemBlockSpecialFlower(bellethorn, props), getId(bellethorn));
		r.accept(new ItemBlockSpecialFlower(bellethornChibi, props), getId(bellethornChibi));
		r.accept(new ItemBlockSpecialFlower(bellethornFloating, props), getId(bellethornFloating));
		r.accept(new ItemBlockSpecialFlower(bellethornChibiFloating, props), getId(bellethornChibiFloating));

		r.accept(new ItemBlockSpecialFlower(bergamute, props), getId(bergamute));
		r.accept(new ItemBlockSpecialFlower(bergamuteFloating, props), getId(bergamuteFloating));

		r.accept(new ItemBlockSpecialFlower(dreadthorn, props), getId(dreadthorn));
		r.accept(new ItemBlockSpecialFlower(dreadthornFloating, props), getId(dreadthornFloating));

		r.accept(new ItemBlockSpecialFlower(heiseiDream, props), getId(heiseiDream));
		r.accept(new ItemBlockSpecialFlower(heiseiDreamFloating, props), getId(heiseiDreamFloating));

		r.accept(new ItemBlockSpecialFlower(tigerseye, props), getId(tigerseye));
		r.accept(new ItemBlockSpecialFlower(tigerseyeFloating, props), getId(tigerseyeFloating));

		r.accept(new ItemBlockSpecialFlower(jadedAmaranthus, props), getId(jadedAmaranthus));
		r.accept(new ItemBlockSpecialFlower(jadedAmaranthusFloating, props), getId(jadedAmaranthusFloating));

		r.accept(new ItemBlockSpecialFlower(orechid, props), getId(orechid));
		r.accept(new ItemBlockSpecialFlower(orechidFloating, props), getId(orechidFloating));

		r.accept(new ItemBlockSpecialFlower(fallenKanade, props), getId(fallenKanade));
		r.accept(new ItemBlockSpecialFlower(fallenKanadeFloating, props), getId(fallenKanadeFloating));

		r.accept(new ItemBlockSpecialFlower(exoflame, props), getId(exoflame));
		r.accept(new ItemBlockSpecialFlower(exoflameFloating, props), getId(exoflameFloating));

		r.accept(new ItemBlockSpecialFlower(agricarnation, props), getId(agricarnation));
		r.accept(new ItemBlockSpecialFlower(agricarnationChibi, props), getId(agricarnationChibi));
		r.accept(new ItemBlockSpecialFlower(agricarnationFloating, props), getId(agricarnationFloating));
		r.accept(new ItemBlockSpecialFlower(agricarnationChibiFloating, props), getId(agricarnationChibiFloating));

		r.accept(new ItemBlockSpecialFlower(hopperhock, props), getId(hopperhock));
		r.accept(new ItemBlockSpecialFlower(hopperhockChibi, props), getId(hopperhockChibi));
		r.accept(new ItemBlockSpecialFlower(hopperhockFloating, props), getId(hopperhockFloating));
		r.accept(new ItemBlockSpecialFlower(hopperhockChibiFloating, props), getId(hopperhockChibiFloating));

		r.accept(new ItemBlockSpecialFlower(tangleberrie, props), getId(tangleberrie));
		r.accept(new ItemBlockSpecialFlower(tangleberrieChibi, props), getId(tangleberrieChibi));
		r.accept(new ItemBlockSpecialFlower(tangleberrieFloating, props), getId(tangleberrieFloating));
		r.accept(new ItemBlockSpecialFlower(tangleberrieChibiFloating, props), getId(tangleberrieChibiFloating));

		r.accept(new ItemBlockSpecialFlower(jiyuulia, props), getId(jiyuulia));
		r.accept(new ItemBlockSpecialFlower(jiyuuliaChibi, props), getId(jiyuuliaChibi));
		r.accept(new ItemBlockSpecialFlower(jiyuuliaFloating, props), getId(jiyuuliaFloating));
		r.accept(new ItemBlockSpecialFlower(jiyuuliaChibiFloating, props), getId(jiyuuliaChibiFloating));

		r.accept(new ItemBlockSpecialFlower(rannuncarpus, props), getId(rannuncarpus));
		r.accept(new ItemBlockSpecialFlower(rannuncarpusChibi, props), getId(rannuncarpusChibi));
		r.accept(new ItemBlockSpecialFlower(rannuncarpusFloating, props), getId(rannuncarpusFloating));
		r.accept(new ItemBlockSpecialFlower(rannuncarpusChibiFloating, props), getId(rannuncarpusChibiFloating));

		r.accept(new ItemBlockSpecialFlower(hyacidus, props), getId(hyacidus));
		r.accept(new ItemBlockSpecialFlower(hyacidusFloating, props), getId(hyacidusFloating));

		r.accept(new ItemBlockSpecialFlower(pollidisiac, props), getId(pollidisiac));
		r.accept(new ItemBlockSpecialFlower(pollidisiacFloating, props), getId(pollidisiacFloating));

		r.accept(new ItemBlockSpecialFlower(clayconia, props), getId(clayconia));
		r.accept(new ItemBlockSpecialFlower(clayconiaChibi, props), getId(clayconiaChibi));
		r.accept(new ItemBlockSpecialFlower(clayconiaFloating, props), getId(clayconiaFloating));
		r.accept(new ItemBlockSpecialFlower(clayconiaChibiFloating, props), getId(clayconiaChibiFloating));

		r.accept(new ItemBlockSpecialFlower(loonium, props), getId(loonium));
		r.accept(new ItemBlockSpecialFlower(looniumFloating, props), getId(looniumFloating));

		r.accept(new ItemBlockSpecialFlower(daffomill, props), getId(daffomill));
		r.accept(new ItemBlockSpecialFlower(daffomillFloating, props), getId(daffomillFloating));

		r.accept(new ItemBlockSpecialFlower(vinculotus, props), getId(vinculotus));
		r.accept(new ItemBlockSpecialFlower(vinculotusFloating, props), getId(vinculotusFloating));

		r.accept(new ItemBlockSpecialFlower(spectranthemum, props), getId(spectranthemum));
		r.accept(new ItemBlockSpecialFlower(spectranthemumFloating, props), getId(spectranthemumFloating));

		r.accept(new ItemBlockSpecialFlower(medumone, props), getId(medumone));
		r.accept(new ItemBlockSpecialFlower(medumoneFloating, props), getId(medumoneFloating));

		r.accept(new ItemBlockSpecialFlower(marimorphosis, props), getId(marimorphosis));
		r.accept(new ItemBlockSpecialFlower(marimorphosisChibi, props), getId(marimorphosisChibi));
		r.accept(new ItemBlockSpecialFlower(marimorphosisFloating, props), getId(marimorphosisFloating));
		r.accept(new ItemBlockSpecialFlower(marimorphosisChibiFloating, props), getId(marimorphosisChibiFloating));

		r.accept(new ItemBlockSpecialFlower(bubbell, props), getId(bubbell));
		r.accept(new ItemBlockSpecialFlower(bubbellChibi, props), getId(bubbellChibi));
		r.accept(new ItemBlockSpecialFlower(bubbellFloating, props), getId(bubbellFloating));
		r.accept(new ItemBlockSpecialFlower(bubbellChibiFloating, props), getId(bubbellChibiFloating));

		r.accept(new ItemBlockSpecialFlower(solegnolia, props), getId(solegnolia));
		r.accept(new ItemBlockSpecialFlower(solegnoliaChibi, props), getId(solegnoliaChibi));
		r.accept(new ItemBlockSpecialFlower(solegnoliaFloating, props), getId(solegnoliaFloating));
		r.accept(new ItemBlockSpecialFlower(solegnoliaChibiFloating, props), getId(solegnoliaChibiFloating));

		r.accept(new ItemBlockSpecialFlower(orechidIgnem, props), getId(orechidIgnem));
		r.accept(new ItemBlockSpecialFlower(orechidIgnemFloating, props), getId(orechidIgnemFloating));

		r.accept(new ItemBlockSpecialFlower(labellia, props), getId(labellia));
		r.accept(new ItemBlockSpecialFlower(labelliaFloating, props), getId(labelliaFloating));
	}

	public static void registerTEs(BiConsumer<BlockEntityType<?>, ResourceLocation> r) {
		r.accept(PURE_DAISY, getId(pureDaisy));
		r.accept(MANASTAR, getId(manastar));
		r.accept(HYDROANGEAS, getId(hydroangeas));
		r.accept(ENDOFLAME, getId(endoflame));
		r.accept(THERMALILY, getId(thermalily));
		r.accept(ROSA_ARCANA, getId(rosaArcana));
		r.accept(MUNCHDEW, getId(munchdew));
		r.accept(ENTROPINNYUM, getId(entropinnyum));
		r.accept(KEKIMURUS, getId(kekimurus));
		r.accept(GOURMARYLLIS, getId(gourmaryllis));
		r.accept(NARSLIMMUS, getId(narslimmus));
		r.accept(SPECTROLUS, getId(spectrolus));
		r.accept(DANDELIFEON, getId(dandelifeon));
		r.accept(RAFFLOWSIA, getId(rafflowsia));
		r.accept(SHULK_ME_NOT, getId(shulkMeNot));
		r.accept(BELLETHORNE, getId(bellethorn));
		r.accept(BELLETHORNE_CHIBI, getId(bellethornChibi));
		r.accept(BERGAMUTE, getId(bergamute));
		r.accept(DREADTHORN, getId(dreadthorn));
		r.accept(HEISEI_DREAM, getId(heiseiDream));
		r.accept(TIGERSEYE, getId(tigerseye));
		r.accept(JADED_AMARANTHUS, getId(jadedAmaranthus));
		r.accept(ORECHID, getId(orechid));
		r.accept(FALLEN_KANADE, getId(fallenKanade));
		r.accept(EXOFLAME, getId(exoflame));
		r.accept(AGRICARNATION, getId(agricarnation));
		r.accept(AGRICARNATION_CHIBI, getId(agricarnationChibi));
		r.accept(HOPPERHOCK, getId(hopperhock));
		r.accept(HOPPERHOCK_CHIBI, getId(hopperhockChibi));
		r.accept(TANGLEBERRIE, getId(tangleberrie));
		r.accept(TANGLEBERRIE_CHIBI, getId(tangleberrieChibi));
		r.accept(JIYUULIA, getId(jiyuulia));
		r.accept(JIYUULIA_CHIBI, getId(jiyuuliaChibi));
		r.accept(RANNUNCARPUS, getId(rannuncarpus));
		r.accept(RANNUNCARPUS_CHIBI, getId(rannuncarpusChibi));
		r.accept(HYACIDUS, getId(hyacidus));
		r.accept(POLLIDISIAC, getId(pollidisiac));
		r.accept(CLAYCONIA, getId(clayconia));
		r.accept(CLAYCONIA_CHIBI, getId(clayconiaChibi));
		r.accept(LOONIUM, getId(loonium));
		r.accept(DAFFOMILL, getId(daffomill));
		r.accept(VINCULOTUS, getId(vinculotus));
		r.accept(SPECTRANTHEMUM, getId(spectranthemum));
		r.accept(MEDUMONE, getId(medumone));
		r.accept(MARIMORPHOSIS, getId(marimorphosis));
		r.accept(MARIMORPHOSIS_CHIBI, getId(marimorphosisChibi));
		r.accept(BUBBELL, getId(bubbell));
		r.accept(BUBBELL_CHIBI, getId(bubbellChibi));
		r.accept(SOLEGNOLIA, getId(solegnolia));
		r.accept(SOLEGNOLIA_CHIBI, getId(solegnoliaChibi));
		r.accept(ORECHID_IGNEM, getId(orechidIgnem));
		r.accept(LABELLIA, getId(labellia));
	}
}
