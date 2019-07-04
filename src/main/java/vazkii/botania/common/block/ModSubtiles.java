package vazkii.botania.common.block;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import org.apache.commons.lang3.tuple.Pair;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.subtile.TileEntitySpecialFlower;
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
import vazkii.botania.common.core.BotaniaCreativeTab;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import static vazkii.botania.common.block.ModBlocks.register;

@Mod.EventBusSubscriber(modid = LibMisc.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(LibMisc.MOD_ID)
public class ModSubtiles {
	@ObjectHolder("pure_daisy") public static Block pureDaisy;
	@ObjectHolder("manastar") public static Block manastar;
	@ObjectHolder("hydroangeas") public static Block hydroangeas;
	@ObjectHolder("endoflame") public static Block endoflame;
	@ObjectHolder("thermalily") public static Block thermalily;
	@ObjectHolder("rosa_arcana") public static Block rosaArcana;
	@ObjectHolder("munchdew") public static Block munchdew;
	@ObjectHolder("entropinnyum") public static Block entropinnyum;
	@ObjectHolder("kekimurus") public static Block kekimurus;
	@ObjectHolder("gourmaryllis") public static Block gourmaryllis;
	@ObjectHolder("narslimmus") public static Block narslimmus;
	@ObjectHolder("spectrolus") public static Block spectrolus;
	@ObjectHolder("dandelifeon") public static Block dandelifeon;
	@ObjectHolder("rafflowsia") public static Block rafflowsia;
	@ObjectHolder("shulk_me_not") public static Block shulkMeNot;
	@ObjectHolder("bellethorn") public static Block bellethorn;
	@ObjectHolder("bellethorn_chibi") public static Block bellethornChibi;
	@ObjectHolder("bergamute") public static Block bergamute;
	@ObjectHolder("dreadthorn") public static Block dreadthorn;
	@ObjectHolder("heisei_dream") public static Block heiseiDream;
	@ObjectHolder("tigerseye") public static Block tigerseye;
	@ObjectHolder("jaded_amaranthus") public static Block jadedAmaranthus;
	@ObjectHolder("orechid") public static Block orechid;
	@ObjectHolder("fallen_kanade") public static Block fallenKanade;
	@ObjectHolder("exoflame") public static Block exoflame;
	@ObjectHolder("agricarnation") public static Block agricarnation;
	@ObjectHolder("agricarnation_chibi") public static Block agricarnationChibi;
	@ObjectHolder("hopperhock") public static Block hopperhock;
	@ObjectHolder("hopperhock_chibi") public static Block hopperhockChibi;
	@ObjectHolder("tangleberrie") public static Block tangleberrie;
	@ObjectHolder("jiyuulia") public static Block jiyuulia;
	@ObjectHolder("rannuncarpus") public static Block rannuncarpus;
	@ObjectHolder("rannuncarpus_chibi") public static Block rannuncarpusChibi;
	@ObjectHolder("hyacidus") public static Block hyacidus;
	@ObjectHolder("pollidisiac") public static Block pollidisiac;
	@ObjectHolder("clayconia") public static Block clayconia;
	@ObjectHolder("clayconia_chibi") public static Block clayconiaChibi;
	@ObjectHolder("loonium") public static Block loonium;
	@ObjectHolder("daffomill") public static Block daffomill;
	@ObjectHolder("vinculotus") public static Block vinculotus;
	@ObjectHolder("spectranthemum") public static Block spectranthemum;
	@ObjectHolder("medumone") public static Block medumone;
	@ObjectHolder("marimorphosis") public static Block marimorphosis;
	@ObjectHolder("marimorphosis_chibi") public static Block marimorphosisChibi;
	@ObjectHolder("bubbell") public static Block bubbell;
	@ObjectHolder("bubbell_chibi") public static Block bubbellChibi;
	@ObjectHolder("solegnolia") public static Block solegnolia;
	@ObjectHolder("solegnolia_chibi") public static Block solegnoliaChibi;
	@ObjectHolder("orechid_ignem") public static Block orechidIgnem;
	
	@ObjectHolder("floating_pure_daisy") public static Block pureDaisyFloating;
	@ObjectHolder("floating_manastar") public static Block manastarFloating;
	@ObjectHolder("floating_hydroangeas") public static Block hydroangeasFloating;
	@ObjectHolder("floating_endoflame") public static Block endoflameFloating;
	@ObjectHolder("floating_thermalily") public static Block thermalilyFloating;
	@ObjectHolder("floating_rosa_arcana") public static Block rosaArcanaFloating;
	@ObjectHolder("floating_munchdew") public static Block munchdewFloating;
	@ObjectHolder("floating_entropinnyum") public static Block entropinnyumFloating;
	@ObjectHolder("floating_kekimurus") public static Block kekimurusFloating;
	@ObjectHolder("floating_gourmaryllis") public static Block gourmaryllisFloating;
	@ObjectHolder("floating_narslimmus") public static Block narslimmusFloating;
	@ObjectHolder("floating_spectrolus") public static Block spectrolusFloating;
	@ObjectHolder("floating_dandelifeon") public static Block dandelifeonFloating;
	@ObjectHolder("floating_rafflowsia") public static Block rafflowsiaFloating;
	@ObjectHolder("floating_shulk_me_not") public static Block shulkMeNotFloating;
	@ObjectHolder("floating_bellethorn") public static Block bellethornFloating;
	@ObjectHolder("floating_bellethorn_chibi") public static Block bellethornChibiFloating;
	@ObjectHolder("floating_bergamute") public static Block bergamuteFloating;
	@ObjectHolder("floating_dreadthorn") public static Block dreadthornFloating;
	@ObjectHolder("floating_heisei_dream") public static Block heiseiDreamFloating;
	@ObjectHolder("floating_tigerseye") public static Block tigerseyeFloating;
	@ObjectHolder("floating_jaded_amaranthus") public static Block jadedAmaranthusFloating;
	@ObjectHolder("floating_orechid") public static Block orechidFloating;
	@ObjectHolder("floating_fallen_kanade") public static Block fallenKanadeFloating;
	@ObjectHolder("floating_exoflame") public static Block exoflameFloating;
	@ObjectHolder("floating_agricarnation") public static Block agricarnationFloating;
	@ObjectHolder("floating_agricarnation_chibi") public static Block agricarnationChibiFloating;
	@ObjectHolder("floating_hopperhock") public static Block hopperhockFloating;
	@ObjectHolder("floating_hopperhock_chibi") public static Block hopperhockChibiFloating;
	@ObjectHolder("floating_tangleberrie") public static Block tangleberrieFloating;
	@ObjectHolder("floating_jiyuulia") public static Block jiyuuliaFloating;
	@ObjectHolder("floating_rannuncarpus") public static Block rannuncarpusFloating;
	@ObjectHolder("floating_rannuncarpus_chibi") public static Block rannuncarpusChibiFloating;
	@ObjectHolder("floating_hyacidus") public static Block hyacidusFloating;
	@ObjectHolder("floating_pollidisiac") public static Block pollidisiacFloating;
	@ObjectHolder("floating_clayconia") public static Block clayconiaFloating;
	@ObjectHolder("floating_clayconia_chibi") public static Block clayconiaChibiFloating;
	@ObjectHolder("floating_loonium") public static Block looniumFloating;
	@ObjectHolder("floating_daffomill") public static Block daffomillFloating;
	@ObjectHolder("floating_vinculotus") public static Block vinculotusFloating;
	@ObjectHolder("floating_spectranthemum") public static Block spectranthemumFloating;
	@ObjectHolder("floating_medumone") public static Block medumoneFloating;
	@ObjectHolder("floating_marimorphosis") public static Block marimorphosisFloating;
	@ObjectHolder("floating_marimorphosis_chibi") public static Block marimorphosisChibiFloating;
	@ObjectHolder("floating_bubbell") public static Block bubbellFloating;
	@ObjectHolder("floating_bubbell_chibi") public static Block bubbellChibiFloating;
	@ObjectHolder("floating_solegnolia") public static Block solegnoliaFloating;
	@ObjectHolder("floating_solegnolia_chibi") public static Block solegnoliaChibiFloating;
	@ObjectHolder("floating_orechid_ignem") public static Block orechidIgnemFloating;
	
	private static ResourceLocation floating(ResourceLocation orig) {
		return new ResourceLocation(orig.getNamespace(), "floating_" + orig.getPath());
	}

	private static ResourceLocation chibi(ResourceLocation orig) {
		return new ResourceLocation(orig.getNamespace(), orig.getPath() + "_chibi");
	}

	private static final List<Pair<Supplier<? extends TileEntitySpecialFlower>, ResourceLocation>> TYPES = ImmutableList.<Pair<Supplier<? extends TileEntitySpecialFlower>, ResourceLocation>>of(
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

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> evt) {
		IForgeRegistry<Block> r = evt.getRegistry();
		Block.Properties props = Block.Properties.from(Blocks.POPPY);
		Block.Properties floatProps = Block.Properties.create(Material.EARTH).hardnessAndResistance(0.5F).sound(SoundType.GROUND).lightValue(15);

		for (Pair<Supplier<? extends TileEntitySpecialFlower>, ResourceLocation> type : TYPES) {
			register(r, new BlockSpecialFlower(props, type.getLeft()), type.getValue());
			register(r, new BlockFloatingSpecialFlower(floatProps, type.getLeft()), floating(type.getValue()));
		}
	}

	@SubscribeEvent
	public static void registerItemBlocks(RegistryEvent.Register<Item> evt) {
		IForgeRegistry<Block> b = ForgeRegistries.BLOCKS;
		IForgeRegistry<Item> r = evt.getRegistry();
		Item.Properties props = ModItems.defaultBuilder();

		for (Pair<Supplier<? extends TileEntitySpecialFlower>, ResourceLocation> type : TYPES) {
			Block block = b.getValue(type.getRight());
			Block floating = b.getValue(floating(type.getRight()));

			register(r, new ItemBlockSpecialFlower(block, props), type.getRight());
			register(r, new ItemBlockSpecialFlower(floating, props), floating(type.getRight()));
		}
	}

	@SubscribeEvent
	public static void registerTEs(RegistryEvent.Register<TileEntityType<?>> evt) {
		IForgeRegistry<Block> b = ForgeRegistries.BLOCKS;
		IForgeRegistry<TileEntityType<?>> r = evt.getRegistry();

		for (Pair<Supplier<? extends TileEntitySpecialFlower>, ResourceLocation> type : TYPES) {
			Block block = b.getValue(type.getRight());
			Block floating = b.getValue(floating(type.getRight()));
			register(r, TileEntityType.Builder.create(type.getLeft(), block, floating).build(null), type.getRight());
		}
	}
}
