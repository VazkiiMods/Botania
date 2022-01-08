/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

import vazkii.botania.api.BotaniaClientCapabilities;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.corporea.*;
import vazkii.botania.common.block.tile.mana.*;
import vazkii.botania.common.block.tile.string.*;
import vazkii.botania.common.core.proxy.IProxy;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.xplat.IXplatAbstractions;

import java.util.Arrays;
import java.util.function.BiConsumer;

import static vazkii.botania.common.block.ModBlocks.*;
import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

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

	public static void registerTiles(BiConsumer<BlockEntityType<?>, ResourceLocation> r) {
		r.accept(ALTAR, prefix(LibBlockNames.ALTAR));
		r.accept(SPREADER, prefix(LibBlockNames.SPREADER));
		r.accept(POOL, prefix(LibBlockNames.POOL));
		r.accept(RUNE_ALTAR, prefix(LibBlockNames.RUNE_ALTAR));
		r.accept(PYLON, prefix(LibBlockNames.PYLON));
		r.accept(DISTRIBUTOR, prefix(LibBlockNames.DISTRIBUTOR));
		r.accept(MANA_VOID, prefix(LibBlockNames.MANA_VOID));
		r.accept(ENCHANTER, prefix(LibBlockNames.ENCHANTER));
		r.accept(TURNTABLE, prefix(LibBlockNames.TURNTABLE));
		r.accept(TINY_PLANET, prefix(LibBlockNames.TINY_PLANET));
		r.accept(OPEN_CRATE, prefix(LibBlockNames.OPEN_CRATE));
		r.accept(CRAFT_CRATE, prefix(LibBlockNames.CRAFT_CRATE));
		r.accept(FORSET_EYE, prefix(LibBlockNames.FOREST_EYE));
		r.accept(PLATFORM, prefix(LibBlockNames.PLATFORM));
		r.accept(ALF_PORTAL, prefix(LibBlockNames.ALF_PORTAL));
		r.accept(BIFROST, prefix(LibBlockNames.BIFROST));
		r.accept(MINI_ISLAND, prefix(LibBlockNames.MINI_ISLAND));
		r.accept(TINY_POTATO, prefix(LibBlockNames.TINY_POTATO));
		r.accept(SPAWNER_CLAW, prefix(LibBlockNames.SPAWNER_CLAW));
		r.accept(ENDER_EYE, prefix(LibBlockNames.ENDER_EYE_BLOCK));
		r.accept(STARFIELD, prefix(LibBlockNames.STARFIELD));
		r.accept(FLUXFIELD, prefix(LibBlockNames.FLUXFIELD));
		r.accept(BREWERY, prefix(LibBlockNames.BREWERY));
		r.accept(TERRA_PLATE, prefix(LibBlockNames.TERRA_PLATE));
		r.accept(RED_STRING_CONTAINER, prefix(LibBlockNames.RED_STRING_CONTAINER));
		r.accept(RED_STRING_DISPENSER, prefix(LibBlockNames.RED_STRING_DISPENSER));
		r.accept(RED_STRING_FERTILIZER, prefix(LibBlockNames.RED_STRING_FERTILIZER));
		r.accept(RED_STRING_COMPARATOR, prefix(LibBlockNames.RED_STRING_COMPARATOR));
		r.accept(RED_STRING_RELAY, prefix(LibBlockNames.RED_STRING_RELAY));
		r.accept(MANA_FLAME, prefix(LibBlockNames.MANA_FLAME));
		r.accept(PRISM, prefix(LibBlockNames.PRISM));
		r.accept(CORPOREA_INDEX, prefix(LibBlockNames.CORPOREA_INDEX));
		r.accept(CORPOREA_FUNNEL, prefix(LibBlockNames.CORPOREA_FUNNEL));
		r.accept(PUMP, prefix(LibBlockNames.PUMP));
		r.accept(FAKE_AIR, prefix(LibBlockNames.FAKE_AIR));
		r.accept(CORPOREA_INTERCEPTOR, prefix(LibBlockNames.CORPOREA_INTERCEPTOR));
		r.accept(CORPOREA_CRYSTAL_CUBE, prefix(LibBlockNames.CORPOREA_CRYSTAL_CUBE));
		r.accept(INCENSE_PLATE, prefix(LibBlockNames.INCENSE_PLATE));
		r.accept(HOURGLASS, prefix(LibBlockNames.HOURGLASS));
		r.accept(SPARK_CHANGER, prefix(LibBlockNames.SPARK_CHANGER));
		r.accept(COCOON, prefix(LibBlockNames.COCOON));
		r.accept(LIGHT_RELAY, prefix(LibBlockNames.LIGHT_RELAY));
		r.accept(CACOPHONIUM, prefix(LibBlockNames.CACOPHONIUM));
		r.accept(BELLOWS, prefix(LibBlockNames.BELLOWS));
		r.accept(CELL_BLOCK, prefix(LibBlockNames.CELL_BLOCK));
		r.accept(RED_STRING_INTERCEPTOR, prefix(LibBlockNames.RED_STRING_INTERCEPTOR));
		r.accept(GAIA_HEAD, prefix(LibBlockNames.GAIA_HEAD));
		r.accept(CORPOREA_RETAINER, prefix(LibBlockNames.CORPOREA_RETAINER));
		r.accept(TERU_TERU_BOZU, prefix(LibBlockNames.TERU_TERU_BOZU));
		r.accept(AVATAR, prefix(LibBlockNames.AVATAR));
		r.accept(ANIMATED_TORCH, prefix(LibBlockNames.ANIMATED_TORCH));

		IProxy.INSTANCE.runOnClient(() -> () -> {
			BotaniaClientCapabilities.WAND_HUD.registerForBlockEntities((be, c) -> new TileAnimatedTorch.WandHud((TileAnimatedTorch) be), ANIMATED_TORCH);
			BotaniaClientCapabilities.WAND_HUD.registerForBlockEntities((be, c) -> new TileBrewery.WandHud((TileBrewery) be), BREWERY);
			BotaniaClientCapabilities.WAND_HUD.registerForBlockEntities((be, c) -> new TileCorporeaRetainer.WandHud((TileCorporeaRetainer) be), CORPOREA_RETAINER);
			BotaniaClientCapabilities.WAND_HUD.registerForBlockEntities((be, c) -> new TileCraftCrate.WandHud((TileCraftCrate) be), CRAFT_CRATE);
			BotaniaClientCapabilities.WAND_HUD.registerForBlockEntities((be, c) -> new TileEnchanter.WandHud((TileEnchanter) be), ENCHANTER);
			BotaniaClientCapabilities.WAND_HUD.registerForBlockEntities((be, c) -> new TileHourglass.WandHud((TileHourglass) be), HOURGLASS);
			BotaniaClientCapabilities.WAND_HUD.registerForBlockEntities((be, c) -> new TilePool.WandHud((TilePool) be), POOL);
			BotaniaClientCapabilities.WAND_HUD.registerForBlockEntities((be, c) -> new TilePrism.WandHud((TilePrism) be), PRISM);
			BotaniaClientCapabilities.WAND_HUD.registerForBlockEntities((be, c) -> new TileSpreader.WandHud((TileSpreader) be), SPREADER);
			BotaniaClientCapabilities.WAND_HUD.registerForBlockEntities((be, c) -> new TileTurntable.WandHud((TileTurntable) be), TURNTABLE);
		});
	}
}
