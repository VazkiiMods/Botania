/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.DyeColor;
import net.minecraft.util.registry.Registry;

import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.corporea.*;
import vazkii.botania.common.block.tile.mana.*;
import vazkii.botania.common.block.tile.string.*;
import vazkii.botania.common.lib.LibBlockNames;

import java.util.Arrays;

import static vazkii.botania.common.block.ModBlocks.*;

public class ModTiles {
	public static final BlockEntityType<TileAltar> ALTAR = BlockEntityType.Builder.create(TileAltar::new,
			defaultAltar, forestAltar, plainsAltar, mountainAltar, fungalAltar,
			swampAltar, desertAltar, taigaAltar, mesaAltar, mossyAltar
	).build(null);
	public static final BlockEntityType<TileSpreader> SPREADER = BlockEntityType.Builder.create(TileSpreader::new, manaSpreader, redstoneSpreader, elvenSpreader, gaiaSpreader).build(null);
	public static final BlockEntityType<TilePool> POOL = BlockEntityType.Builder.create(TilePool::new, manaPool, dilutedPool, fabulousPool, creativePool).build(null);
	public static final BlockEntityType<TileRuneAltar> RUNE_ALTAR = BlockEntityType.Builder.create(TileRuneAltar::new, runeAltar).build(null);
	public static final BlockEntityType<TilePylon> PYLON = BlockEntityType.Builder.create(TilePylon::new, manaPylon, naturaPylon, gaiaPylon).build(null);
	public static final BlockEntityType<TileDistributor> DISTRIBUTOR = BlockEntityType.Builder.create(TileDistributor::new, distributor).build(null);
	public static final BlockEntityType<TileManaVoid> MANA_VOID = BlockEntityType.Builder.create(TileManaVoid::new, manaVoid).build(null);
	public static final BlockEntityType<TileManaDetector> MANA_DETECTOR = BlockEntityType.Builder.create(TileManaDetector::new, manaDetector).build(null);
	public static final BlockEntityType<TileEnchanter> ENCHANTER = BlockEntityType.Builder.create(TileEnchanter::new, enchanter).build(null);
	public static final BlockEntityType<TileTurntable> TURNTABLE = BlockEntityType.Builder.create(TileTurntable::new, turntable).build(null);
	public static final BlockEntityType<TileTinyPlanet> TINY_PLANET = BlockEntityType.Builder.create(TileTinyPlanet::new, tinyPlanet).build(null);
	public static final BlockEntityType<TileOpenCrate> OPEN_CRATE = BlockEntityType.Builder.create(TileOpenCrate::new, openCrate).build(null);
	public static final BlockEntityType<TileCraftCrate> CRAFT_CRATE = BlockEntityType.Builder.create(TileCraftCrate::new, craftCrate).build(null);
	public static final BlockEntityType<TileForestEye> FORSET_EYE = BlockEntityType.Builder.create(TileForestEye::new, forestEye).build(null);
	public static final BlockEntityType<TilePlatform> PLATFORM = BlockEntityType.Builder.create(TilePlatform::new, abstrusePlatform, spectralPlatform, infrangiblePlatform).build(null);
	public static final BlockEntityType<TileAlfPortal> ALF_PORTAL = BlockEntityType.Builder.create(TileAlfPortal::new, alfPortal).build(null);
	public static final BlockEntityType<TileBifrost> BIFROST = BlockEntityType.Builder.create(TileBifrost::new, bifrost).build(null);
	public static final BlockEntityType<TileFloatingFlower> MINI_ISLAND = BlockEntityType.Builder.create(TileFloatingFlower::new, Arrays.stream(DyeColor.values()).map(ModBlocks::getFloatingFlower).toArray(Block[]::new)).build(null);
	public static final BlockEntityType<TileTinyPotato> TINY_POTATO = BlockEntityType.Builder.create(TileTinyPotato::new, tinyPotato).build(null);
	public static final BlockEntityType<TileSpawnerClaw> SPAWNER_CLAW = BlockEntityType.Builder.create(TileSpawnerClaw::new, spawnerClaw).build(null);
	public static final BlockEntityType<TileEnderEye> ENDER_EYE = BlockEntityType.Builder.create(TileEnderEye::new, enderEye).build(null);
	public static final BlockEntityType<TileStarfield> STARFIELD = BlockEntityType.Builder.create(TileStarfield::new, starfield).build(null);
	public static final BlockEntityType<TileRFGenerator> FLUXFIELD = BlockEntityType.Builder.create(TileRFGenerator::new, rfGenerator).build(null);
	public static final BlockEntityType<TileBrewery> BREWERY = BlockEntityType.Builder.create(TileBrewery::new, brewery).build(null);
	public static final BlockEntityType<TileTerraPlate> TERRA_PLATE = BlockEntityType.Builder.create(TileTerraPlate::new, terraPlate).build(null);
	public static final BlockEntityType<TileRedStringContainer> RED_STRING_CONTAINER = BlockEntityType.Builder.create(TileRedStringContainer::new, redStringContainer).build(null);
	public static final BlockEntityType<TileRedStringDispenser> RED_STRING_DISPENSER = BlockEntityType.Builder.create(TileRedStringDispenser::new, redStringDispenser).build(null);
	public static final BlockEntityType<TileRedStringFertilizer> RED_STRING_FERTILIZER = BlockEntityType.Builder.create(TileRedStringFertilizer::new, redStringFertilizer).build(null);
	public static final BlockEntityType<TileRedStringComparator> RED_STRING_COMPARATOR = BlockEntityType.Builder.create(TileRedStringComparator::new, redStringComparator).build(null);
	public static final BlockEntityType<TileRedStringRelay> RED_STRING_RELAY = BlockEntityType.Builder.create(TileRedStringRelay::new, redStringRelay).build(null);
	public static final BlockEntityType<TileManaFlame> MANA_FLAME = BlockEntityType.Builder.create(TileManaFlame::new, manaFlame).build(null);
	public static final BlockEntityType<TilePrism> PRISM = BlockEntityType.Builder.create(TilePrism::new, prism).build(null);
	public static final BlockEntityType<TileCorporeaIndex> CORPOREA_INDEX = BlockEntityType.Builder.create(TileCorporeaIndex::new, corporeaIndex).build(null);
	public static final BlockEntityType<TileCorporeaFunnel> CORPOREA_FUNNEL = BlockEntityType.Builder.create(TileCorporeaFunnel::new, corporeaFunnel).build(null);
	public static final BlockEntityType<TilePump> PUMP = BlockEntityType.Builder.create(TilePump::new, pump).build(null);
	public static final BlockEntityType<TileFakeAir> FAKE_AIR = BlockEntityType.Builder.create(TileFakeAir::new, fakeAir).build(null);
	public static final BlockEntityType<TileCorporeaInterceptor> CORPOREA_INTERCEPTOR = BlockEntityType.Builder.create(TileCorporeaInterceptor::new, corporeaInterceptor).build(null);
	public static final BlockEntityType<TileCorporeaCrystalCube> CORPOREA_CRYSTAL_CUBE = BlockEntityType.Builder.create(TileCorporeaCrystalCube::new, corporeaCrystalCube).build(null);
	public static final BlockEntityType<TileIncensePlate> INCENSE_PLATE = BlockEntityType.Builder.create(TileIncensePlate::new, incensePlate).build(null);
	public static final BlockEntityType<TileHourglass> HOURGLASS = BlockEntityType.Builder.create(TileHourglass::new, hourglass).build(null);
	public static final BlockEntityType<TileSparkChanger> SPARK_CHANGER = BlockEntityType.Builder.create(TileSparkChanger::new, sparkChanger).build(null);
	public static final BlockEntityType<TileCocoon> COCOON = BlockEntityType.Builder.create(TileCocoon::new, cocoon).build(null);
	public static final BlockEntityType<TileLightRelay> LIGHT_RELAY = BlockEntityType.Builder.create(TileLightRelay::new, lightRelayDefault, lightRelayDetector, lightRelayToggle, lightRelayFork).build(null);
	public static final BlockEntityType<TileCacophonium> CACOPHONIUM = BlockEntityType.Builder.create(TileCacophonium::new, cacophonium).build(null);
	public static final BlockEntityType<TileBellows> BELLOWS = BlockEntityType.Builder.create(TileBellows::new, bellows).build(null);
	public static final BlockEntityType<TileCell> CELL_BLOCK = BlockEntityType.Builder.create(TileCell::new, cellBlock).build(null);
	public static final BlockEntityType<TileRedStringInterceptor> RED_STRING_INTERCEPTOR = BlockEntityType.Builder.create(TileRedStringInterceptor::new, redStringInterceptor).build(null);
	public static final BlockEntityType<TileGaiaHead> GAIA_HEAD = BlockEntityType.Builder.create(TileGaiaHead::new, gaiaHead, gaiaHeadWall).build(null);
	public static final BlockEntityType<TileCorporeaRetainer> CORPOREA_RETAINER = BlockEntityType.Builder.create(TileCorporeaRetainer::new, corporeaRetainer).build(null);
	public static final BlockEntityType<TileTeruTeruBozu> TERU_TERU_BOZU = BlockEntityType.Builder.create(TileTeruTeruBozu::new, teruTeruBozu).build(null);
	public static final BlockEntityType<TileAvatar> AVATAR = BlockEntityType.Builder.create(TileAvatar::new, avatar).build(null);
	public static final BlockEntityType<TileAnimatedTorch> ANIMATED_TORCH = BlockEntityType.Builder.create(TileAnimatedTorch::new, animatedTorch).build(null);

	public static void registerTiles() {
		Registry<BlockEntityType<?>> r = Registry.BLOCK_ENTITY_TYPE;
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
