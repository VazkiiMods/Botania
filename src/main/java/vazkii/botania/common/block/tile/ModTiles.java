package vazkii.botania.common.block.tile;

import net.minecraft.block.Block;
import net.minecraft.item.DyeColor;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;
import sun.security.x509.AVA;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.corporea.*;
import vazkii.botania.common.block.tile.mana.*;
import vazkii.botania.common.block.tile.string.*;
import vazkii.botania.common.lib.LibBlockNames;

import java.util.Arrays;

import static vazkii.botania.common.block.ModBlocks.*;

public class ModTiles
{
	public static TileEntityType<TileAltar> ALTAR = TileEntityType.Builder.create(TileAltar::new,
			defaultAltar, forestAltar, plainsAltar, mountainAltar, fungalAltar,
			swampAltar, desertAltar, taigaAltar, mesaAltar, mossyAltar
	).build(null);
	public static TileEntityType<TileSpreader> SPREADER = TileEntityType.Builder.create(TileSpreader::new, manaSpreader, redstoneSpreader, elvenSpreader, gaiaSpreader).build(null);
	public static TileEntityType<TilePool> POOL = TileEntityType.Builder.create(TilePool::new, manaPool, dilutedPool, fabulousPool, creativePool).build(null);
	public static TileEntityType<TileRuneAltar> RUNE_ALTAR = TileEntityType.Builder.create(TileRuneAltar::new, runeAltar).build(null);
	public static TileEntityType<TilePylon> PYLON = TileEntityType.Builder.create(TilePylon::new, manaPylon, naturaPylon, gaiaPylon).build(null);
	public static TileEntityType<TileDistributor> DISTRIBUTOR = TileEntityType.Builder.create(TileDistributor::new, distributor).build(null);
	public static TileEntityType<TileManaVoid> MANA_VOID = TileEntityType.Builder.create(TileManaVoid::new, manaVoid).build(null);
	public static TileEntityType<TileManaDetector> MANA_DETECTOR = TileEntityType.Builder.create(TileManaDetector::new, manaDetector).build(null);
	public static TileEntityType<TileEnchanter> ENCHANTER = TileEntityType.Builder.create(TileEnchanter::new, enchanter).build(null);
	public static TileEntityType<TileTurntable> TURNTABLE = TileEntityType.Builder.create(TileTurntable::new, turntable).build(null);
	public static TileEntityType<TileTinyPlanet> TINY_PLANET = TileEntityType.Builder.create(TileTinyPlanet::new, tinyPlanet).build(null);
	public static TileEntityType<TileOpenCrate> OPEN_CRATE = TileEntityType.Builder.create(TileOpenCrate::new, openCrate).build(null);
	public static TileEntityType<TileCraftCrate> CRAFT_CRATE = TileEntityType.Builder.create(TileCraftCrate::new, craftCrate).build(null);
	public static TileEntityType<TileForestEye> FORSET_EYE = TileEntityType.Builder.create(TileForestEye::new, forestEye).build(null);
	public static TileEntityType<TilePlatform> PLATFORM = TileEntityType.Builder.create(TilePlatform::new, abstrusePlatform, spectralPlatform, infrangiblePlatform).build(null);
	public static TileEntityType<TileAlfPortal> ALF_PORTAL = TileEntityType.Builder.create(TileAlfPortal::new, alfPortal).build(null);
	public static TileEntityType<TileBifrost> BIFROST = TileEntityType.Builder.create(TileBifrost::new, bifrost).build(null);
	public static TileEntityType<TileFloatingFlower> MINI_ISLAND = TileEntityType.Builder.create(TileFloatingFlower::new, Arrays.stream(DyeColor.values()).map(ModBlocks::getFloatingFlower).toArray(Block[]::new)).build(null);
	public static TileEntityType<TileTinyPotato> TINY_POTATO = TileEntityType.Builder.create(TileTinyPotato::new, tinyPotato).build(null);
	public static TileEntityType<TileSpawnerClaw> SPAWNER_CLAW = TileEntityType.Builder.create(TileSpawnerClaw::new, spawnerClaw).build(null);
	public static TileEntityType<TileEnderEye> ENDER_EYE = TileEntityType.Builder.create(TileEnderEye::new, enderEye).build(null);
	public static TileEntityType<TileStarfield> STARFIELD = TileEntityType.Builder.create(TileStarfield::new, starfield).build(null);
	public static TileEntityType<TileRFGenerator> FLUXFIELD = TileEntityType.Builder.create(TileRFGenerator::new, rfGenerator).build(null);
	public static TileEntityType<TileBrewery> BREWERY = TileEntityType.Builder.create(TileBrewery::new, brewery).build(null);
	public static TileEntityType<TileTerraPlate> TERRA_PLATE = TileEntityType.Builder.create(TileTerraPlate::new, terraPlate).build(null);
	public static TileEntityType<TileRedStringContainer> RED_STRING_CONTAINER = TileEntityType.Builder.create(TileRedStringContainer::new, redStringContainer).build(null);
	public static TileEntityType<TileRedStringDispenser> RED_STRING_DISPENSER = TileEntityType.Builder.create(TileRedStringDispenser::new, redStringDispenser).build(null);
	public static TileEntityType<TileRedStringFertilizer> RED_STRING_FERTILIZER = TileEntityType.Builder.create(TileRedStringFertilizer::new, redStringFertilizer).build(null);
	public static TileEntityType<TileRedStringComparator> RED_STRING_COMPARATOR = TileEntityType.Builder.create(TileRedStringComparator::new, redStringComparator).build(null);
	public static TileEntityType<TileRedStringRelay> RED_STRING_RELAY = TileEntityType.Builder.create(TileRedStringRelay::new, redStringRelay).build(null);
	public static TileEntityType<TileManaFlame> MANA_FLAME = TileEntityType.Builder.create(TileManaFlame::new, manaFlame).build(null);
	public static TileEntityType<TilePrism> PRISM = TileEntityType.Builder.create(TilePrism::new, prism).build(null);
	public static TileEntityType<TileCorporeaIndex> CORPOREA_INDEX = TileEntityType.Builder.create(TileCorporeaIndex::new, corporeaIndex).build(null);
	public static TileEntityType<TileCorporeaFunnel> CORPOREA_FUNNEL = TileEntityType.Builder.create(TileCorporeaFunnel::new, corporeaFunnel).build(null);
	public static TileEntityType<TilePump> PUMP = TileEntityType.Builder.create(TilePump::new, pump).build(null);
	public static TileEntityType<TileFakeAir> FAKE_AIR = TileEntityType.Builder.create(TileFakeAir::new, fakeAir).build(null);
	public static TileEntityType<TileCorporeaInterceptor> CORPOREA_INTERCEPTOR = TileEntityType.Builder.create(TileCorporeaInterceptor::new, corporeaInterceptor).build(null);
	public static TileEntityType<TileCorporeaCrystalCube> CORPOREA_CRYSTAL_CUBE = TileEntityType.Builder.create(TileCorporeaCrystalCube::new, corporeaCrystalCube).build(null);
	public static TileEntityType<TileIncensePlate> INCENSE_PLATE = TileEntityType.Builder.create(TileIncensePlate::new, incensePlate).build(null);
	public static TileEntityType<TileHourglass> HOURGLASS = TileEntityType.Builder.create(TileHourglass::new, hourglass).build(null);
	public static TileEntityType<TileSparkChanger> SPARK_CHANGER = TileEntityType.Builder.create(TileSparkChanger::new, sparkChanger).build(null);
	public static TileEntityType<TileCocoon> COCOON = TileEntityType.Builder.create(TileCocoon::new, cocoon).build(null);
	public static TileEntityType<TileLightRelay> LIGHT_RELAY = TileEntityType.Builder.create(TileLightRelay::new, lightRelayDefault, lightRelayDetector, lightRelayToggle, lightRelayFork).build(null);
	public static TileEntityType<TileCacophonium> CACOPHONIUM = TileEntityType.Builder.create(TileCacophonium::new, cacophonium).build(null);
	public static TileEntityType<TileBellows> BELLOWS = TileEntityType.Builder.create(TileBellows::new, bellows).build(null);
	public static TileEntityType<TileCell> CELL_BLOCK = TileEntityType.Builder.create(TileCell::new, cellBlock).build(null);
	public static TileEntityType<TileRedStringInterceptor> RED_STRING_INTERCEPTOR = TileEntityType.Builder.create(TileRedStringInterceptor::new, redStringInterceptor).build(null);
	public static TileEntityType<TileGaiaHead> GAIA_HEAD = TileEntityType.Builder.create(TileGaiaHead::new, gaiaHead, gaiaHeadWall).build(null);
	public static TileEntityType<TileCorporeaRetainer> CORPOREA_RETAINER = TileEntityType.Builder.create(TileCorporeaRetainer::new, corporeaRetainer).build(null);
	public static TileEntityType<TileTeruTeruBozu> TERU_TERU_BOZU = TileEntityType.Builder.create(TileTeruTeruBozu::new, teruTeruBozu).build(null);
	public static TileEntityType<TileAvatar> AVATAR = TileEntityType.Builder.create(TileAvatar::new, avatar).build(null);
	public static TileEntityType<TileAnimatedTorch> ANIMATED_TORCH = TileEntityType.Builder.create(TileAnimatedTorch::new, animatedTorch).build(null);
	
	public static void registerTiles(RegistryEvent.Register<TileEntityType<?>> evt)
	{
		IForgeRegistry<TileEntityType<?>> r = evt.getRegistry();
		register(r, LibBlockNames.ALTAR, ALTAR);
		register(r, LibBlockNames.SPREADER, SPREADER);
		register(r, LibBlockNames.POOL, POOL);
		register(r, LibBlockNames.RUNE_ALTAR, RUNE_ALTAR);
		register(r, LibBlockNames.PYLON, PYLON);
		register(r, LibBlockNames.DISTRIBUTOR, DISTRIBUTOR);
		register(r, LibBlockNames.MANA_VOID, MANA_VOID);
		register(r, LibBlockNames.MANA_DETECTOR, MANA_DETECTOR);
		register(r, LibBlockNames.ENCHANTER, ENCHANTER);
		register(r, LibBlockNames.TURNTABLE, TURNTABLE);
		register(r, LibBlockNames.TINY_PLANET, TINY_PLANET);
		register(r, LibBlockNames.OPEN_CRATE, OPEN_CRATE);
		register(r, LibBlockNames.CRAFT_CRATE, CRAFT_CRATE);
		register(r, LibBlockNames.FOREST_EYE, FORSET_EYE);
		register(r, LibBlockNames.PLATFORM, PLATFORM);
		register(r, LibBlockNames.ALF_PORTAL, ALF_PORTAL);
		register(r, LibBlockNames.BIFROST, BIFROST);
		register(r, LibBlockNames.MINI_ISLAND, MINI_ISLAND);
		register(r, LibBlockNames.TINY_POTATO, TINY_POTATO);
		register(r, LibBlockNames.SPAWNER_CLAW, SPAWNER_CLAW);
		register(r, LibBlockNames.ENDER_EYE_BLOCK, ENDER_EYE);
		register(r, LibBlockNames.STARFIELD, STARFIELD);
		register(r, LibBlockNames.FLUXFIELD, FLUXFIELD);
		register(r, LibBlockNames.BREWERY, BREWERY);
		register(r, LibBlockNames.TERRA_PLATE, TERRA_PLATE);
		register(r, LibBlockNames.RED_STRING_CONTAINER, RED_STRING_CONTAINER);
		register(r, LibBlockNames.RED_STRING_DISPENSER, RED_STRING_DISPENSER);
		register(r, LibBlockNames.RED_STRING_FERTILIZER, RED_STRING_FERTILIZER);
		register(r, LibBlockNames.RED_STRING_COMPARATOR, RED_STRING_COMPARATOR);
		register(r, LibBlockNames.RED_STRING_RELAY, RED_STRING_RELAY);
		register(r, LibBlockNames.MANA_FLAME, MANA_FLAME);
		register(r, LibBlockNames.PRISM, PRISM);
		register(r, LibBlockNames.CORPOREA_INDEX, CORPOREA_INDEX);
		register(r, LibBlockNames.CORPOREA_FUNNEL, CORPOREA_FUNNEL);
		register(r, LibBlockNames.PUMP, PUMP);
		register(r, LibBlockNames.FAKE_AIR, FAKE_AIR);
		register(r, LibBlockNames.CORPOREA_INTERCEPTOR, CORPOREA_INTERCEPTOR);
		register(r, LibBlockNames.CORPOREA_CRYSTAL_CUBE, CORPOREA_CRYSTAL_CUBE);
		register(r, LibBlockNames.INCENSE_PLATE, INCENSE_PLATE);
		register(r, LibBlockNames.HOURGLASS, HOURGLASS);
		register(r, LibBlockNames.SPARK_CHANGER, SPARK_CHANGER);
		register(r, LibBlockNames.COCOON, COCOON);
		register(r, LibBlockNames.LIGHT_RELAY, LIGHT_RELAY);
		register(r, LibBlockNames.CACOPHONIUM, CACOPHONIUM);
		register(r, LibBlockNames.BELLOWS, BELLOWS);
		register(r, LibBlockNames.CELL_BLOCK, CELL_BLOCK);
		register(r, LibBlockNames.RED_STRING_INTERCEPTOR, RED_STRING_INTERCEPTOR);
		register(r, LibBlockNames.GAIA_HEAD, GAIA_HEAD);
		register(r, LibBlockNames.CORPOREA_RETAINER, CORPOREA_RETAINER);
		register(r, LibBlockNames.TERU_TERU_BOZU, TERU_TERU_BOZU);
		register(r, LibBlockNames.AVATAR, AVATAR);
		register(r, LibBlockNames.ANIMATED_TORCH, ANIMATED_TORCH);
	}
}