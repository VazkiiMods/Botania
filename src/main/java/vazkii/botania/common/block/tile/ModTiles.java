/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.corporea.*;
import vazkii.botania.common.block.tile.mana.*;
import vazkii.botania.common.block.tile.string.*;
import vazkii.botania.common.lib.LibBlockNames;

import java.util.Arrays;

import static vazkii.botania.common.block.ModBlocks.*;

public class ModTiles {
	public static final BlockEntityType<TileAltar> ALTAR = FabricBlockEntityTypeBuilder.create(TileAltar::new,
			defaultAltar, forestAltar, plainsAltar, mountainAltar, fungalAltar,
			swampAltar, desertAltar, taigaAltar, mesaAltar, mossyAltar
	).build(null);
	public static final BlockEntityType<TileSpreader> SPREADER = FabricBlockEntityTypeBuilder.create(TileSpreader::new, manaSpreader, redstoneSpreader, elvenSpreader, gaiaSpreader).build(null);
	public static final BlockEntityType<TilePool> POOL = FabricBlockEntityTypeBuilder.create(TilePool::new, manaPool, dilutedPool, fabulousPool, creativePool).build(null);
	public static final BlockEntityType<TileRuneAltar> RUNE_ALTAR = FabricBlockEntityTypeBuilder.create(TileRuneAltar::new, runeAltar).build(null);
	public static final BlockEntityType<TilePylon> PYLON = FabricBlockEntityTypeBuilder.create(TilePylon::new, manaPylon, naturaPylon, gaiaPylon).build(null);
	public static final BlockEntityType<TileDistributor> DISTRIBUTOR = FabricBlockEntityTypeBuilder.create(TileDistributor::new, distributor).build(null);
	public static final BlockEntityType<TileManaVoid> MANA_VOID = FabricBlockEntityTypeBuilder.create(TileManaVoid::new, manaVoid).build(null);
	public static final BlockEntityType<TileEnchanter> ENCHANTER = FabricBlockEntityTypeBuilder.create(TileEnchanter::new, enchanter).build(null);
	public static final BlockEntityType<TileTurntable> TURNTABLE = FabricBlockEntityTypeBuilder.create(TileTurntable::new, turntable).build(null);
	public static final BlockEntityType<TileTinyPlanet> TINY_PLANET = FabricBlockEntityTypeBuilder.create(TileTinyPlanet::new, tinyPlanet).build(null);
	public static final BlockEntityType<TileOpenCrate> OPEN_CRATE = FabricBlockEntityTypeBuilder.create(TileOpenCrate::new, openCrate).build(null);
	public static final BlockEntityType<TileCraftCrate> CRAFT_CRATE = FabricBlockEntityTypeBuilder.create(TileCraftCrate::new, craftCrate).build(null);
	public static final BlockEntityType<TileForestEye> FORSET_EYE = FabricBlockEntityTypeBuilder.create(TileForestEye::new, forestEye).build(null);
	public static final BlockEntityType<TilePlatform> PLATFORM = FabricBlockEntityTypeBuilder.create(TilePlatform::new, abstrusePlatform, spectralPlatform, infrangiblePlatform).build(null);
	public static final BlockEntityType<TileAlfPortal> ALF_PORTAL = FabricBlockEntityTypeBuilder.create(TileAlfPortal::new, alfPortal).build(null);
	public static final BlockEntityType<TileBifrost> BIFROST = FabricBlockEntityTypeBuilder.create(TileBifrost::new, bifrost).build(null);
	public static final BlockEntityType<TileFloatingFlower> MINI_ISLAND = FabricBlockEntityTypeBuilder.create(TileFloatingFlower::new, Arrays.stream(DyeColor.values()).map(ModBlocks::getFloatingFlower).toArray(Block[]::new)).build(null);
	public static final BlockEntityType<TileTinyPotato> TINY_POTATO = FabricBlockEntityTypeBuilder.create(TileTinyPotato::new, tinyPotato).build(null);
	public static final BlockEntityType<TileSpawnerClaw> SPAWNER_CLAW = FabricBlockEntityTypeBuilder.create(TileSpawnerClaw::new, spawnerClaw).build(null);
	public static final BlockEntityType<TileEnderEye> ENDER_EYE = FabricBlockEntityTypeBuilder.create(TileEnderEye::new, enderEye).build(null);
	public static final BlockEntityType<TileStarfield> STARFIELD = FabricBlockEntityTypeBuilder.create(TileStarfield::new, starfield).build(null);
	public static final BlockEntityType<TileRFGenerator> FLUXFIELD = FabricBlockEntityTypeBuilder.create(TileRFGenerator::new, rfGenerator).build(null);
	public static final BlockEntityType<TileBrewery> BREWERY = FabricBlockEntityTypeBuilder.create(TileBrewery::new, brewery).build(null);
	public static final BlockEntityType<TileTerraPlate> TERRA_PLATE = FabricBlockEntityTypeBuilder.create(TileTerraPlate::new, terraPlate).build(null);
	public static final BlockEntityType<TileRedStringContainer> RED_STRING_CONTAINER = FabricBlockEntityTypeBuilder.create(TileRedStringContainer::new, redStringContainer).build(null);
	public static final BlockEntityType<TileRedStringDispenser> RED_STRING_DISPENSER = FabricBlockEntityTypeBuilder.create(TileRedStringDispenser::new, redStringDispenser).build(null);
	public static final BlockEntityType<TileRedStringFertilizer> RED_STRING_FERTILIZER = FabricBlockEntityTypeBuilder.create(TileRedStringFertilizer::new, redStringFertilizer).build(null);
	public static final BlockEntityType<TileRedStringComparator> RED_STRING_COMPARATOR = FabricBlockEntityTypeBuilder.create(TileRedStringComparator::new, redStringComparator).build(null);
	public static final BlockEntityType<TileRedStringRelay> RED_STRING_RELAY = FabricBlockEntityTypeBuilder.create(TileRedStringRelay::new, redStringRelay).build(null);
	public static final BlockEntityType<TileManaFlame> MANA_FLAME = FabricBlockEntityTypeBuilder.create(TileManaFlame::new, manaFlame).build(null);
	public static final BlockEntityType<TilePrism> PRISM = FabricBlockEntityTypeBuilder.create(TilePrism::new, prism).build(null);
	public static final BlockEntityType<TileCorporeaIndex> CORPOREA_INDEX = FabricBlockEntityTypeBuilder.create(TileCorporeaIndex::new, corporeaIndex).build(null);
	public static final BlockEntityType<TileCorporeaFunnel> CORPOREA_FUNNEL = FabricBlockEntityTypeBuilder.create(TileCorporeaFunnel::new, corporeaFunnel).build(null);
	public static final BlockEntityType<TilePump> PUMP = FabricBlockEntityTypeBuilder.create(TilePump::new, pump).build(null);
	public static final BlockEntityType<TileFakeAir> FAKE_AIR = FabricBlockEntityTypeBuilder.create(TileFakeAir::new, fakeAir).build(null);
	public static final BlockEntityType<TileCorporeaInterceptor> CORPOREA_INTERCEPTOR = FabricBlockEntityTypeBuilder.create(TileCorporeaInterceptor::new, corporeaInterceptor).build(null);
	public static final BlockEntityType<TileCorporeaCrystalCube> CORPOREA_CRYSTAL_CUBE = FabricBlockEntityTypeBuilder.create(TileCorporeaCrystalCube::new, corporeaCrystalCube).build(null);
	public static final BlockEntityType<TileIncensePlate> INCENSE_PLATE = FabricBlockEntityTypeBuilder.create(TileIncensePlate::new, incensePlate).build(null);
	public static final BlockEntityType<TileHourglass> HOURGLASS = FabricBlockEntityTypeBuilder.create(TileHourglass::new, hourglass).build(null);
	public static final BlockEntityType<TileSparkChanger> SPARK_CHANGER = FabricBlockEntityTypeBuilder.create(TileSparkChanger::new, sparkChanger).build(null);
	public static final BlockEntityType<TileCocoon> COCOON = FabricBlockEntityTypeBuilder.create(TileCocoon::new, cocoon).build(null);
	public static final BlockEntityType<TileLightRelay> LIGHT_RELAY = FabricBlockEntityTypeBuilder.create(TileLightRelay::new, lightRelayDefault, lightRelayDetector, lightRelayToggle, lightRelayFork).build(null);
	public static final BlockEntityType<TileCacophonium> CACOPHONIUM = FabricBlockEntityTypeBuilder.create(TileCacophonium::new, cacophonium).build(null);
	public static final BlockEntityType<TileBellows> BELLOWS = FabricBlockEntityTypeBuilder.create(TileBellows::new, bellows).build(null);
	public static final BlockEntityType<TileCell> CELL_BLOCK = FabricBlockEntityTypeBuilder.create(TileCell::new, cellBlock).build(null);
	public static final BlockEntityType<TileRedStringInterceptor> RED_STRING_INTERCEPTOR = FabricBlockEntityTypeBuilder.create(TileRedStringInterceptor::new, redStringInterceptor).build(null);
	public static final BlockEntityType<TileGaiaHead> GAIA_HEAD = FabricBlockEntityTypeBuilder.create(TileGaiaHead::new, gaiaHead, gaiaHeadWall).build(null);
	public static final BlockEntityType<TileCorporeaRetainer> CORPOREA_RETAINER = FabricBlockEntityTypeBuilder.create(TileCorporeaRetainer::new, corporeaRetainer).build(null);
	public static final BlockEntityType<TileTeruTeruBozu> TERU_TERU_BOZU = FabricBlockEntityTypeBuilder.create(TileTeruTeruBozu::new, teruTeruBozu).build(null);
	public static final BlockEntityType<TileAvatar> AVATAR = FabricBlockEntityTypeBuilder.create(TileAvatar::new, avatar).build(null);
	public static final BlockEntityType<TileAnimatedTorch> ANIMATED_TORCH = FabricBlockEntityTypeBuilder.create(TileAnimatedTorch::new, animatedTorch).build(null);

	public static void registerTiles() {
		Registry<BlockEntityType<?>> r = Registry.BLOCK_ENTITY_TYPE;
		register(r, LibBlockNames.ALTAR, ALTAR);
		register(r, LibBlockNames.SPREADER, SPREADER);
		register(r, LibBlockNames.POOL, POOL);
		register(r, LibBlockNames.RUNE_ALTAR, RUNE_ALTAR);
		register(r, LibBlockNames.PYLON, PYLON);
		register(r, LibBlockNames.DISTRIBUTOR, DISTRIBUTOR);
		register(r, LibBlockNames.MANA_VOID, MANA_VOID);
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
