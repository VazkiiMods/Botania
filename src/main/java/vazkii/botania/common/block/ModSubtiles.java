package vazkii.botania.common.block;

import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.internal.DummySubTile;
import vazkii.botania.api.subtile.SubTileType;
import vazkii.botania.common.block.subtile.SubTileManastar;
import vazkii.botania.common.block.subtile.SubTilePureDaisy;
import vazkii.botania.common.block.subtile.functional.*;
import vazkii.botania.common.block.subtile.generating.SubTileArcaneRose;
import vazkii.botania.common.block.subtile.generating.SubTileDandelifeon;
import vazkii.botania.common.block.subtile.generating.SubTileEndoflame;
import vazkii.botania.common.block.subtile.generating.SubTileEntropinnyum;
import vazkii.botania.common.block.subtile.generating.SubTileGourmaryllis;
import vazkii.botania.common.block.subtile.generating.SubTileHydroangeas;
import vazkii.botania.common.block.subtile.generating.SubTileKekimurus;
import vazkii.botania.common.block.subtile.generating.SubTileMunchdew;
import vazkii.botania.common.block.subtile.generating.SubTileNarslimmus;
import vazkii.botania.common.block.subtile.generating.SubTileRafflowsia;
import vazkii.botania.common.block.subtile.generating.SubTileShulkMeNot;
import vazkii.botania.common.block.subtile.generating.SubTileSpectrolus;
import vazkii.botania.common.block.subtile.generating.SubTileThermalily;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(LibMisc.MOD_ID)
public class ModSubtiles {
	@ObjectHolder("dummy") public static SubTileType dummy;
	@ObjectHolder("pure_daisy") public static SubTileType pureDaisy;
	@ObjectHolder("manastar") public static SubTileType manastar;

	@ObjectHolder("hydroangeas") public static SubTileType hydroangeas;
	@ObjectHolder("endoflame") public static SubTileType endoflame;
	@ObjectHolder("thermalily") public static SubTileType thermalily;
	@ObjectHolder("rosa_arcana") public static SubTileType rosaArcana;
	@ObjectHolder("munchdew") public static SubTileType munchdew;
	@ObjectHolder("entropinnyum") public static SubTileType entropinnyum;
	@ObjectHolder("kekimurus") public static SubTileType kekimurus;
	@ObjectHolder("gourmaryllis") public static SubTileType gourmaryllis;
	@ObjectHolder("narslimmus") public static SubTileType narslimmus;
	@ObjectHolder("spectrolus") public static SubTileType spectrolus;
	@ObjectHolder("dandelifeon") public static SubTileType dandelifeon;
	@ObjectHolder("rafflowsia") public static SubTileType rafflowsia;
	@ObjectHolder("shulk_me_not") public static SubTileType shulkMeNot;

	@ObjectHolder("bellethorn") public static SubTileType bellethorn;
	@ObjectHolder("bellethorn_chibi") public static SubTileType bellethornChibi;
	@ObjectHolder("bergamute") public static SubTileType bergamute;
	@ObjectHolder("dreadthorn") public static SubTileType dreadthorn;
	@ObjectHolder("heisei_dream") public static SubTileType heiseiDream;
	@ObjectHolder("tigerseye") public static SubTileType tigerseye;
	@ObjectHolder("jaded_amaranthus") public static SubTileType jadedAmaranthus;
	@ObjectHolder("orechid") public static SubTileType orechid;
	@ObjectHolder("fallen_kanade") public static SubTileType fallenKanade;
	@ObjectHolder("exoflame") public static SubTileType exoflame;
	@ObjectHolder("agricarnation") public static SubTileType agricarnation;
	@ObjectHolder("agricarnation_chibi") public static SubTileType agricarnationChibi;
	@ObjectHolder("hopperhock") public static SubTileType hopperhock;
	@ObjectHolder("hopperhock_chibi") public static SubTileType hopperhockChibi;
	@ObjectHolder("tangleberrie") public static SubTileType tangleberrie;
	@ObjectHolder("jiyuulia") public static SubTileType jiyuulia;
	@ObjectHolder("rannuncarpus") public static SubTileType rannuncarpus;
	@ObjectHolder("rannuncarpus_chibi") public static SubTileType rannuncarpusChibi;
	@ObjectHolder("hyacidus") public static SubTileType hyacidus;
	@ObjectHolder("pollidisiac") public static SubTileType pollidisiac;
	@ObjectHolder("clayconia") public static SubTileType clayconia;
	@ObjectHolder("clayconia_chibi") public static SubTileType clayconiaChibi;
	@ObjectHolder("loonium") public static SubTileType loonium;
	@ObjectHolder("daffomill") public static SubTileType daffomill;
	@ObjectHolder("vinculotus") public static SubTileType vinculotus;
	@ObjectHolder("spectranthemum") public static SubTileType spectranthemum;
	@ObjectHolder("medumone") public static SubTileType medumone;
	@ObjectHolder("marimorphosis") public static SubTileType marimorphosis;
	@ObjectHolder("marimorphosis_chibi") public static SubTileType marimorphosisChibi;
	@ObjectHolder("bubbell") public static SubTileType bubbell;
	@ObjectHolder("bubbell_chibi") public static SubTileType bubbellChibi;
	@ObjectHolder("solegnolia") public static SubTileType solegnolia;
	@ObjectHolder("solegnolia_chibi") public static SubTileType solegnoliaChibi;
	@ObjectHolder("orechid_ignem") public static SubTileType orechidIgnem;

	@SubscribeEvent
	public static void initSubtiles(RegistryEvent.Register<SubTileType> evt) {
		IForgeRegistry<SubTileType> r = evt.getRegistry();
		r.register(new SubTileType(SubTileType.MISC_TYPE, DummySubTile::new).setRegistryName(BotaniaAPI.DUMMY_SUBTILE_NAME));
		r.register(new SubTileType(SubTileType.MISC_TYPE, SubTilePureDaisy::new).setRegistryName(LibBlockNames.SUBTILE_PUREDAISY));
		r.register(new SubTileType(SubTileType.MISC_TYPE, SubTileManastar::new).setRegistryName(LibBlockNames.SUBTILE_MANASTAR));

		r.register(new SubTileType(SubTileType.GENERATING_TYPE, SubTileHydroangeas::new).setRegistryName(LibBlockNames.SUBTILE_HYDROANGEAS));
		r.register(new SubTileType(SubTileType.GENERATING_TYPE, SubTileEndoflame::new).setRegistryName(LibBlockNames.SUBTILE_ENDOFLAME));
		r.register(new SubTileType(SubTileType.GENERATING_TYPE, SubTileThermalily::new).setRegistryName(LibBlockNames.SUBTILE_THERMALILY));
		r.register(new SubTileType(SubTileType.GENERATING_TYPE, SubTileArcaneRose::new).setRegistryName(LibBlockNames.SUBTILE_ARCANE_ROSE));
		r.register(new SubTileType(SubTileType.GENERATING_TYPE, SubTileMunchdew::new).setRegistryName(LibBlockNames.SUBTILE_MUNCHDEW));
		r.register(new SubTileType(SubTileType.GENERATING_TYPE, SubTileEntropinnyum::new).setRegistryName(LibBlockNames.SUBTILE_ENTROPINNYUM));
		r.register(new SubTileType(SubTileType.GENERATING_TYPE, SubTileKekimurus::new).setRegistryName(LibBlockNames.SUBTILE_KEKIMURUS));
		r.register(new SubTileType(SubTileType.GENERATING_TYPE, SubTileGourmaryllis::new).setRegistryName(LibBlockNames.SUBTILE_GOURMARYLLIS));
		r.register(new SubTileType(SubTileType.GENERATING_TYPE, SubTileNarslimmus::new).setRegistryName(LibBlockNames.SUBTILE_NARSLIMMUS));
		r.register(new SubTileType(SubTileType.GENERATING_TYPE, SubTileSpectrolus::new).setRegistryName(LibBlockNames.SUBTILE_SPECTROLUS));
		r.register(new SubTileType(SubTileType.GENERATING_TYPE, SubTileDandelifeon::new).setRegistryName(LibBlockNames.SUBTILE_DANDELIFEON));
		r.register(new SubTileType(SubTileType.GENERATING_TYPE, SubTileRafflowsia::new).setRegistryName(LibBlockNames.SUBTILE_RAFFLOWSIA));
		r.register(new SubTileType(SubTileType.GENERATING_TYPE, SubTileShulkMeNot::new).setRegistryName(LibBlockNames.SUBTILE_SHULK_ME_NOT));

		r.register(new SubTileType(SubTileType.FUNCTIONAL_TYPE, SubTileBellethorn::new).setRegistryName(LibBlockNames.SUBTILE_BELLETHORN));
		r.register(new SubTileType(SubTileType.FUNCTIONAL_TYPE, SubTileBellethorn.Mini::new).setRegistryName(LibMisc.MOD_ID, LibBlockNames.SUBTILE_BELLETHORN.getPath() + "_chibi"));
		r.register(new SubTileType(SubTileType.FUNCTIONAL_TYPE, SubTileDreadthorn::new).setRegistryName(LibBlockNames.SUBTILE_DREADTHORN));
		r.register(new SubTileType(SubTileType.FUNCTIONAL_TYPE, SubTileHeiseiDream::new).setRegistryName(LibBlockNames.SUBTILE_HEISEI_DREAM));
		r.register(new SubTileType(SubTileType.FUNCTIONAL_TYPE, SubTileTigerseye::new).setRegistryName(LibBlockNames.SUBTILE_TIGERSEYE));
		r.register(new SubTileType(SubTileType.FUNCTIONAL_TYPE, SubTileJadedAmaranthus::new).setRegistryName(LibBlockNames.SUBTILE_JADED_AMARANTHUS));
		r.register(new SubTileType(SubTileType.FUNCTIONAL_TYPE, SubTileOrechid::new).setRegistryName(LibBlockNames.SUBTILE_ORECHID));
		r.register(new SubTileType(SubTileType.FUNCTIONAL_TYPE, SubTileOrechidIgnem::new).setRegistryName(LibBlockNames.SUBTILE_ORECHID_IGNEM));
		r.register(new SubTileType(SubTileType.FUNCTIONAL_TYPE, SubTileFallenKanade::new).setRegistryName(LibBlockNames.SUBTILE_FALLEN_KANADE));
		r.register(new SubTileType(SubTileType.FUNCTIONAL_TYPE, SubTileExoflame::new).setRegistryName(LibBlockNames.SUBTILE_EXOFLAME));
		r.register(new SubTileType(SubTileType.FUNCTIONAL_TYPE, SubTileAgricarnation::new).setRegistryName(LibBlockNames.SUBTILE_AGRICARNATION));
		r.register(new SubTileType(SubTileType.FUNCTIONAL_TYPE, SubTileAgricarnation.Mini::new).setRegistryName(LibMisc.MOD_ID, LibBlockNames.SUBTILE_AGRICARNATION.getPath() + "_chibi"));
		r.register(new SubTileType(SubTileType.FUNCTIONAL_TYPE, SubTileHopperhock::new).setRegistryName(LibBlockNames.SUBTILE_HOPPERHOCK));
		r.register(new SubTileType(SubTileType.FUNCTIONAL_TYPE, SubTileHopperhock.Mini::new).setRegistryName(LibMisc.MOD_ID, LibBlockNames.SUBTILE_HOPPERHOCK.getPath() + "_chibi"));
		r.register(new SubTileType(SubTileType.FUNCTIONAL_TYPE, SubTileTangleberrie::new).setRegistryName(LibBlockNames.SUBTILE_TANGLEBERRIE));
		r.register(new SubTileType(SubTileType.FUNCTIONAL_TYPE, SubTileJiyuulia::new).setRegistryName(LibBlockNames.SUBTILE_JIYUULIA));
		r.register(new SubTileType(SubTileType.FUNCTIONAL_TYPE, SubTileRannuncarpus::new).setRegistryName(LibBlockNames.SUBTILE_RANNUNCARPUS));
		r.register(new SubTileType(SubTileType.FUNCTIONAL_TYPE, SubTileRannuncarpus.Mini::new).setRegistryName(LibMisc.MOD_ID, LibBlockNames.SUBTILE_RANNUNCARPUS.getPath() + "_chibi"));
		r.register(new SubTileType(SubTileType.FUNCTIONAL_TYPE, SubTileHyacidus::new).setRegistryName(LibBlockNames.SUBTILE_HYACIDUS));
		r.register(new SubTileType(SubTileType.FUNCTIONAL_TYPE, SubTilePollidisiac::new).setRegistryName(LibBlockNames.SUBTILE_POLLIDISIAC));
		r.register(new SubTileType(SubTileType.FUNCTIONAL_TYPE, SubTileClayconia::new).setRegistryName(LibBlockNames.SUBTILE_CLAYCONIA));
		r.register(new SubTileType(SubTileType.FUNCTIONAL_TYPE, SubTileClayconia.Mini::new).setRegistryName(LibMisc.MOD_ID, LibBlockNames.SUBTILE_CLAYCONIA.getPath() + "_chibi"));
		r.register(new SubTileType(SubTileType.FUNCTIONAL_TYPE, SubTileLoonuim::new).setRegistryName(LibBlockNames.SUBTILE_LOONIUM));
		r.register(new SubTileType(SubTileType.FUNCTIONAL_TYPE, SubTileDaffomill::new).setRegistryName(LibBlockNames.SUBTILE_DAFFOMILL));
		r.register(new SubTileType(SubTileType.FUNCTIONAL_TYPE, SubTileVinculotus::new).setRegistryName(LibBlockNames.SUBTILE_VINCULOTUS));
		r.register(new SubTileType(SubTileType.FUNCTIONAL_TYPE, SubTileSpectranthemum::new).setRegistryName(LibBlockNames.SUBTILE_SPECTRANTHEMUM));
		r.register(new SubTileType(SubTileType.FUNCTIONAL_TYPE, SubTileMedumone::new).setRegistryName(LibBlockNames.SUBTILE_MEDUMONE));
		r.register(new SubTileType(SubTileType.FUNCTIONAL_TYPE, SubTileMarimorphosis::new).setRegistryName(LibBlockNames.SUBTILE_MARIMORPHOSIS));
		r.register(new SubTileType(SubTileType.FUNCTIONAL_TYPE, SubTileMarimorphosis.Mini::new).setRegistryName(LibMisc.MOD_ID, LibBlockNames.SUBTILE_MARIMORPHOSIS.getPath() + "_chibi"));
		r.register(new SubTileType(SubTileType.FUNCTIONAL_TYPE, SubTileBubbell::new).setRegistryName(LibBlockNames.SUBTILE_BUBBELL));
		r.register(new SubTileType(SubTileType.FUNCTIONAL_TYPE, SubTileBubbell.Mini::new).setRegistryName(LibMisc.MOD_ID, LibBlockNames.SUBTILE_BUBBELL.getPath() + "_chibi"));
		r.register(new SubTileType(SubTileType.FUNCTIONAL_TYPE, SubTileSolegnolia::new).setRegistryName(LibBlockNames.SUBTILE_SOLEGNOLIA));
		r.register(new SubTileType(SubTileType.FUNCTIONAL_TYPE, SubTileSolegnolia.Mini::new).setRegistryName(LibMisc.MOD_ID, LibBlockNames.SUBTILE_SOLEGNOLIA.getPath() + "_chibi"));
		r.register(new SubTileType(SubTileType.FUNCTIONAL_TYPE, SubTileBergamute::new).setRegistryName(LibBlockNames.SUBTILE_BERGAMUTE));
	}
}
