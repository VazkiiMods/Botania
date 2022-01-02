/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile;

import net.minecraft.core.Registry;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import vazkii.botania.api.block.IHourglassTrigger;
import vazkii.botania.api.block.IWandHUD;
import vazkii.botania.api.block.IWandable;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.corporea.*;
import vazkii.botania.common.block.tile.mana.*;
import vazkii.botania.common.block.tile.string.*;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.xplat.IXplatAbstractions;

import java.util.Arrays;

import static vazkii.botania.common.block.ModBlocks.*;

public class ModTiles {
	public static final BlockEntityType<TileAltar> ALTAR = IXplatAbstractions.INSTANCE.createBlockEntityType(TileAltar::new,
			defaultAltar, forestAltar, plainsAltar, mountainAltar, fungalAltar,
			swampAltar, desertAltar, taigaAltar, mesaAltar, mossyAltar
	);
	public static final BlockEntityType<TileSpreader> SPREADER = IXplatAbstractions.INSTANCE.createBlockEntityType(TileSpreader::new, manaSpreader, redstoneSpreader, elvenSpreader, gaiaSpreader);
	public static final BlockEntityType<TilePool> POOL = IXplatAbstractions.INSTANCE.createBlockEntityType(TilePool::new, manaPool, dilutedPool, fabulousPool, creativePool);
	public static final BlockEntityType<TileRuneAltar> RUNE_ALTAR = IXplatAbstractions.INSTANCE.createBlockEntityType(TileRuneAltar::new, runeAltar);
	public static final BlockEntityType<TilePylon> PYLON = IXplatAbstractions.INSTANCE.createBlockEntityType(TilePylon::new, manaPylon, naturaPylon, gaiaPylon);
	public static final BlockEntityType<TileDistributor> DISTRIBUTOR = IXplatAbstractions.INSTANCE.createBlockEntityType(TileDistributor::new, distributor);
	public static final BlockEntityType<TileManaVoid> MANA_VOID = IXplatAbstractions.INSTANCE.createBlockEntityType(TileManaVoid::new, manaVoid);
	public static final BlockEntityType<TileEnchanter> ENCHANTER = IXplatAbstractions.INSTANCE.createBlockEntityType(TileEnchanter::new, enchanter);
	public static final BlockEntityType<TileTurntable> TURNTABLE = IXplatAbstractions.INSTANCE.createBlockEntityType(TileTurntable::new, turntable);
	public static final BlockEntityType<TileTinyPlanet> TINY_PLANET = IXplatAbstractions.INSTANCE.createBlockEntityType(TileTinyPlanet::new, tinyPlanet);
	public static final BlockEntityType<TileOpenCrate> OPEN_CRATE = IXplatAbstractions.INSTANCE.createBlockEntityType(TileOpenCrate::new, openCrate);
	public static final BlockEntityType<TileCraftCrate> CRAFT_CRATE = IXplatAbstractions.INSTANCE.createBlockEntityType(TileCraftCrate::new, craftCrate);
	public static final BlockEntityType<TileForestEye> FORSET_EYE = IXplatAbstractions.INSTANCE.createBlockEntityType(TileForestEye::new, forestEye);
	public static final BlockEntityType<TilePlatform> PLATFORM = IXplatAbstractions.INSTANCE.createBlockEntityType(TilePlatform::new, abstrusePlatform, spectralPlatform, infrangiblePlatform);
	public static final BlockEntityType<TileAlfPortal> ALF_PORTAL = IXplatAbstractions.INSTANCE.createBlockEntityType(TileAlfPortal::new, alfPortal);
	public static final BlockEntityType<TileBifrost> BIFROST = IXplatAbstractions.INSTANCE.createBlockEntityType(TileBifrost::new, bifrost);
	public static final BlockEntityType<TileFloatingFlower> MINI_ISLAND = IXplatAbstractions.INSTANCE.createBlockEntityType(TileFloatingFlower::new, Arrays.stream(DyeColor.values()).map(ModBlocks::getFloatingFlower).toArray(Block[]::new));
	public static final BlockEntityType<TileTinyPotato> TINY_POTATO = IXplatAbstractions.INSTANCE.createBlockEntityType(TileTinyPotato::new, tinyPotato);
	public static final BlockEntityType<TileSpawnerClaw> SPAWNER_CLAW = IXplatAbstractions.INSTANCE.createBlockEntityType(TileSpawnerClaw::new, spawnerClaw);
	public static final BlockEntityType<TileEnderEye> ENDER_EYE = IXplatAbstractions.INSTANCE.createBlockEntityType(TileEnderEye::new, enderEye);
	public static final BlockEntityType<TileStarfield> STARFIELD = IXplatAbstractions.INSTANCE.createBlockEntityType(TileStarfield::new, starfield);
	public static final BlockEntityType<TileRFGenerator> FLUXFIELD = IXplatAbstractions.INSTANCE.createBlockEntityType(TileRFGenerator::new, rfGenerator);
	public static final BlockEntityType<TileBrewery> BREWERY = IXplatAbstractions.INSTANCE.createBlockEntityType(TileBrewery::new, brewery);
	public static final BlockEntityType<TileTerraPlate> TERRA_PLATE = IXplatAbstractions.INSTANCE.createBlockEntityType(TileTerraPlate::new, terraPlate);
	public static final BlockEntityType<TileRedStringContainer> RED_STRING_CONTAINER = IXplatAbstractions.INSTANCE.createBlockEntityType(TileRedStringContainer::new, redStringContainer);
	public static final BlockEntityType<TileRedStringDispenser> RED_STRING_DISPENSER = IXplatAbstractions.INSTANCE.createBlockEntityType(TileRedStringDispenser::new, redStringDispenser);
	public static final BlockEntityType<TileRedStringFertilizer> RED_STRING_FERTILIZER = IXplatAbstractions.INSTANCE.createBlockEntityType(TileRedStringFertilizer::new, redStringFertilizer);
	public static final BlockEntityType<TileRedStringComparator> RED_STRING_COMPARATOR = IXplatAbstractions.INSTANCE.createBlockEntityType(TileRedStringComparator::new, redStringComparator);
	public static final BlockEntityType<TileRedStringRelay> RED_STRING_RELAY = IXplatAbstractions.INSTANCE.createBlockEntityType(TileRedStringRelay::new, redStringRelay);
	public static final BlockEntityType<TileManaFlame> MANA_FLAME = IXplatAbstractions.INSTANCE.createBlockEntityType(TileManaFlame::new, manaFlame);
	public static final BlockEntityType<TilePrism> PRISM = IXplatAbstractions.INSTANCE.createBlockEntityType(TilePrism::new, prism);
	public static final BlockEntityType<TileCorporeaIndex> CORPOREA_INDEX = IXplatAbstractions.INSTANCE.createBlockEntityType(TileCorporeaIndex::new, corporeaIndex);
	public static final BlockEntityType<TileCorporeaFunnel> CORPOREA_FUNNEL = IXplatAbstractions.INSTANCE.createBlockEntityType(TileCorporeaFunnel::new, corporeaFunnel);
	public static final BlockEntityType<TilePump> PUMP = IXplatAbstractions.INSTANCE.createBlockEntityType(TilePump::new, pump);
	public static final BlockEntityType<TileFakeAir> FAKE_AIR = IXplatAbstractions.INSTANCE.createBlockEntityType(TileFakeAir::new, fakeAir);
	public static final BlockEntityType<TileCorporeaInterceptor> CORPOREA_INTERCEPTOR = IXplatAbstractions.INSTANCE.createBlockEntityType(TileCorporeaInterceptor::new, corporeaInterceptor);
	public static final BlockEntityType<TileCorporeaCrystalCube> CORPOREA_CRYSTAL_CUBE = IXplatAbstractions.INSTANCE.createBlockEntityType(TileCorporeaCrystalCube::new, corporeaCrystalCube);
	public static final BlockEntityType<TileIncensePlate> INCENSE_PLATE = IXplatAbstractions.INSTANCE.createBlockEntityType(TileIncensePlate::new, incensePlate);
	public static final BlockEntityType<TileHourglass> HOURGLASS = IXplatAbstractions.INSTANCE.createBlockEntityType(TileHourglass::new, hourglass);
	public static final BlockEntityType<TileSparkChanger> SPARK_CHANGER = IXplatAbstractions.INSTANCE.createBlockEntityType(TileSparkChanger::new, sparkChanger);
	public static final BlockEntityType<TileCocoon> COCOON = IXplatAbstractions.INSTANCE.createBlockEntityType(TileCocoon::new, cocoon);
	public static final BlockEntityType<TileLightRelay> LIGHT_RELAY = IXplatAbstractions.INSTANCE.createBlockEntityType(TileLightRelay::new, lightRelayDefault, lightRelayDetector, lightRelayToggle, lightRelayFork);
	public static final BlockEntityType<TileCacophonium> CACOPHONIUM = IXplatAbstractions.INSTANCE.createBlockEntityType(TileCacophonium::new, cacophonium);
	public static final BlockEntityType<TileBellows> BELLOWS = IXplatAbstractions.INSTANCE.createBlockEntityType(TileBellows::new, bellows);
	public static final BlockEntityType<TileCell> CELL_BLOCK = IXplatAbstractions.INSTANCE.createBlockEntityType(TileCell::new, cellBlock);
	public static final BlockEntityType<TileRedStringInterceptor> RED_STRING_INTERCEPTOR = IXplatAbstractions.INSTANCE.createBlockEntityType(TileRedStringInterceptor::new, redStringInterceptor);
	public static final BlockEntityType<TileGaiaHead> GAIA_HEAD = IXplatAbstractions.INSTANCE.createBlockEntityType(TileGaiaHead::new, gaiaHead, gaiaHeadWall);
	public static final BlockEntityType<TileCorporeaRetainer> CORPOREA_RETAINER = IXplatAbstractions.INSTANCE.createBlockEntityType(TileCorporeaRetainer::new, corporeaRetainer);
	public static final BlockEntityType<TileTeruTeruBozu> TERU_TERU_BOZU = IXplatAbstractions.INSTANCE.createBlockEntityType(TileTeruTeruBozu::new, teruTeruBozu);
	public static final BlockEntityType<TileAvatar> AVATAR = IXplatAbstractions.INSTANCE.createBlockEntityType(TileAvatar::new, avatar);
	public static final BlockEntityType<TileAnimatedTorch> ANIMATED_TORCH = IXplatAbstractions.INSTANCE.createBlockEntityType(TileAnimatedTorch::new, animatedTorch);

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

		IHourglassTrigger.API.registerForBlockEntities((be, c) -> {
			var torch = (TileAnimatedTorch) be;
			return hourglass -> torch.toggle();
		}, ANIMATED_TORCH);
		IWandable.API.registerSelf(
				ALF_PORTAL, ANIMATED_TORCH, CORPOREA_CRYSTAL_CUBE, CORPOREA_RETAINER,
				CRAFT_CRATE, ENCHANTER, HOURGLASS, PLATFORM, POOL,
				RUNE_ALTAR, SPREADER, TURNTABLE);
		IWandHUD.API.registerSelf(ANIMATED_TORCH, BREWERY, CORPOREA_RETAINER, CRAFT_CRATE,
				ENCHANTER, HOURGLASS, POOL, PRISM, SPREADER, TURNTABLE);

	}
}
