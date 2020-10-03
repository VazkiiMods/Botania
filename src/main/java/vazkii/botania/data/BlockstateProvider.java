/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.data;

import com.google.common.collect.ImmutableMap;

import net.minecraft.block.*;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.DyeColor;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.AlfPortalState;
import vazkii.botania.api.state.enums.CratePattern;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.*;
import vazkii.botania.common.block.decor.BlockBuriedPetals;
import vazkii.botania.common.block.decor.BlockFloatingFlower;
import vazkii.botania.common.block.decor.BlockModMushroom;
import vazkii.botania.common.block.decor.BlockMotifFlower;
import vazkii.botania.common.block.decor.BlockPetalBlock;
import vazkii.botania.common.block.string.BlockRedString;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static vazkii.botania.common.block.ModBlocks.*;
import static vazkii.botania.common.block.ModFluffBlocks.*;
import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class BlockstateProvider extends BlockStateProvider {
	public BlockstateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
		super(gen, LibMisc.MOD_ID, exFileHelper);
	}

	@Nonnull
	@Override
	public String getName() {
		return "Botania Blockstates";
	}

	@Override
	protected void registerStatesAndModels() {
		Set<Block> remainingBlocks = Registry.BLOCK.stream()
				.filter(b -> LibMisc.MOD_ID.equals(Registry.BLOCK.getKey(b).getNamespace()))
				.collect(Collectors.toSet());

		// Manually written blockstate + models
		remainingBlocks.remove(ghostRail);
		remainingBlocks.remove(solidVines);

		// Manually written simpleBlock
		manualModel(remainingBlocks, cocoon);
		manualModel(remainingBlocks, corporeaCrystalCube);
		manualModel(remainingBlocks, distributor);
		manualModel(remainingBlocks, prism);
		manualModel(remainingBlocks, runeAltar);
		manualModel(remainingBlocks, spawnerClaw);

		// Single blocks
		String alfPortalName = Registry.BLOCK.getKey(alfPortal).getPath();
		ModelFile alfPortalModel = models().cubeAll(alfPortalName, prefix("block/" + alfPortalName));
		ModelFile alfPortalActivatedModel = models().cubeAll(alfPortalName + "_activated", prefix("block/" + alfPortalName + "_activated"));
		getVariantBuilder(alfPortal).partialState().with(BotaniaStateProps.ALFPORTAL_STATE, AlfPortalState.OFF)
				.setModels(new ConfiguredModel(alfPortalModel));
		getVariantBuilder(alfPortal).partialState().with(BotaniaStateProps.ALFPORTAL_STATE, AlfPortalState.ON_X)
				.setModels(new ConfiguredModel(alfPortalActivatedModel));
		getVariantBuilder(alfPortal).partialState().with(BotaniaStateProps.ALFPORTAL_STATE, AlfPortalState.ON_Z)
				.setModels(new ConfiguredModel(alfPortalActivatedModel));
		remainingBlocks.remove(alfPortal);

		String bifrostPermName = Registry.BLOCK.getKey(bifrostPerm).getPath();
		simpleBlock(bifrostPerm, models().cubeAll(bifrostPermName, prefix("block/bifrost")));
		remainingBlocks.remove(bifrostPerm);

		String cacophoniumName = Registry.BLOCK.getKey(cacophonium).getPath();
		simpleBlock(cacophonium, models().cubeTop(cacophoniumName,
				prefix("block/" + cacophoniumName),
				prefix("block/" + cacophoniumName + "_top")));
		remainingBlocks.remove(cacophonium);

		String craftCrateName = Registry.BLOCK.getKey(craftCrate).getPath();
		getVariantBuilder(craftCrate).forAllStates(s -> {
			CratePattern pat = s.get(BotaniaStateProps.CRATE_PATTERN);
			String suffix = pat == CratePattern.NONE ? "" : "_" + pat.getString().substring("crafty_".length());
			String name = craftCrateName + suffix;
			ModelFile model = models().withExistingParent(name, prefix("block/shapes/crate"))
					.texture("bottom", prefix("block/" + craftCrateName + "_bottom"))
					.texture("side", prefix("block/" + name));
			return new ConfiguredModel[] { new ConfiguredModel(model) };
		});
		remainingBlocks.remove(craftCrate);

		ResourceLocation corpSlabSide = prefix("block/corporea_slab_side");
		ResourceLocation corpBlock = prefix("block/corporea_block");
		ModelFile corpSlabBottom = models().slab("corporea_slab", corpBlock, corpBlock, corpBlock);
		ModelFile corpSlabTop = models().slabTop("corporea_slab_top", corpBlock, corpBlock, corpBlock);
		ModelFile corpSlabDouble = models().cubeBottomTop("corporea_double_slab", corpSlabSide, corpBlock, corpBlock);
		slabBlock(corporeaSlab, corpSlabBottom, corpSlabTop, corpSlabDouble);
		remainingBlocks.remove(corporeaSlab);

		stairsBlock(corporeaStairs, prefix("block/corporea_block"));
		remainingBlocks.remove(corporeaStairs);

		fixedWallBlock((WallBlock) corporeaBrickWall, prefix("block/corporea_brick"));
		remainingBlocks.remove(corporeaBrickWall);

		String elfGlassName = Registry.BLOCK.getKey(elfGlass).getPath();
		ConfiguredModel[] elfGlassFiles = IntStream.rangeClosed(0, 3)
				.mapToObj(i -> {
					String varName = elfGlassName + "_" + i;
					return models().cubeAll(varName, prefix("block/" + varName));
				})
				.map(ConfiguredModel::new).toArray(ConfiguredModel[]::new);
		getVariantBuilder(elfGlass).partialState().setModels(elfGlassFiles);
		remainingBlocks.remove(elfGlass);

		String enchSoilName = Registry.BLOCK.getKey(enchantedSoil).getPath();
		simpleBlock(enchantedSoil, models().cubeBottomTop(enchSoilName,
				prefix("block/" + enchSoilName + "_side"),
				new ResourceLocation("block/dirt"),
				prefix("block/" + enchSoilName + "_top")
		));
		remainingBlocks.remove(enchantedSoil);

		String felName = Registry.BLOCK.getKey(felPumpkin).getPath();
		simpleBlock(felPumpkin, models().orientable(felName, new ResourceLocation("block/pumpkin_side"), prefix("block/" + felName),
				new ResourceLocation("block/pumpkin_top")));
		remainingBlocks.remove(felPumpkin);

		String forestEyeName = Registry.BLOCK.getKey(forestEye).getPath();
		ModelFile forestEyeFile = models().withExistingParent(forestEyeName, prefix("block/shapes/eightbyeight"))
				.texture("bottom", prefix("block/" + forestEyeName + "_bottom"))
				.texture("top", prefix("block/" + forestEyeName + "_top"))
				.texture("north", prefix("block/" + forestEyeName + "_north"))
				.texture("south", prefix("block/" + forestEyeName + "_south"))
				.texture("west", prefix("block/" + forestEyeName + "_west"))
				.texture("east", prefix("block/" + forestEyeName + "_east"));
		simpleBlock(forestEye, forestEyeFile);
		remainingBlocks.remove(forestEye);

		String plateName = Registry.BLOCK.getKey(incensePlate).getPath();
		ModelFile plateFile = models().getExistingFile(prefix("block/" + plateName));
		horizontalBlock(incensePlate, plateFile, 0);
		remainingBlocks.remove(incensePlate);

		String lightLauncherName = Registry.BLOCK.getKey(lightLauncher).getPath();
		ModelFile lightLauncherFile = models().withExistingParent(lightLauncherName, prefix("block/shapes/four_high_bottom_top"))
				.texture("bottom", prefix("block/" + lightLauncherName + "_end"))
				.texture("top", prefix("block/" + lightLauncherName + "_end"))
				.texture("side", prefix("block/" + lightLauncherName + "_side"));
		simpleBlock(lightLauncher, lightLauncherFile);
		remainingBlocks.remove(lightLauncher);

		String openCrateName = Registry.BLOCK.getKey(openCrate).getPath();
		ModelFile openCrateFile = models().withExistingParent(openCrateName, prefix("block/shapes/crate"))
				.texture("side", prefix("block/" + openCrateName))
				.texture("bottom", prefix("block/" + openCrateName + "_bottom"));
		simpleBlock(openCrate, openCrateFile);
		remainingBlocks.remove(openCrate);

		String sparkChangerName = Registry.BLOCK.getKey(sparkChanger).getPath();
		ModelFile sparkChangerFile = models().withExistingParent(sparkChangerName, prefix("block/shapes/three_high_bottom_top"))
				.texture("bottom", prefix("block/" + sparkChangerName + "_bottom"))
				.texture("top", prefix("block/" + sparkChangerName + "_top"))
				.texture("side", prefix("block/" + sparkChangerName + "_side"));
		simpleBlock(sparkChanger, sparkChangerFile);
		remainingBlocks.remove(sparkChanger);

		String starfieldName = Registry.BLOCK.getKey(starfield).getPath();
		ModelFile starfieldFile = models().withExistingParent(starfieldName, prefix("block/shapes/four_high_bottom_top"))
				.texture("bottom", prefix("block/" + starfieldName + "_bottom"))
				.texture("top", prefix("block/" + starfieldName + "_top"))
				.texture("side", prefix("block/" + starfieldName + "_side"));
		simpleBlock(starfield, starfieldFile);
		remainingBlocks.remove(starfield);

		String terraPlateName = Registry.BLOCK.getKey(terraPlate).getPath();
		ModelFile terraPlateFile = models().withExistingParent(terraPlateName, prefix("block/shapes/three_high_bottom_top"))
				.texture("bottom", prefix("block/" + terraPlateName + "_bottom"))
				.texture("top", prefix("block/" + terraPlateName + "_top"))
				.texture("side", prefix("block/" + terraPlateName + "_side"));
		simpleBlock(terraPlate, terraPlateFile);
		remainingBlocks.remove(terraPlate);

		String tinyPlanetName = Registry.BLOCK.getKey(tinyPlanet).getPath();
		ModelFile tinyPlanetFile = models().withExistingParent(tinyPlanetName, prefix("block/shapes/tenbyten_all"))
				.texture("all", prefix("block/" + tinyPlanetName));
		simpleBlock(tinyPlanet, tinyPlanetFile);
		remainingBlocks.remove(tinyPlanet);

		String turnTableName = Registry.BLOCK.getKey(turntable).getPath();
		simpleBlock(turntable, models().cubeBottomTop(turnTableName,
				prefix("block/" + turnTableName + "_side"),
				prefix("block/" + turnTableName + "_bottom"),
				prefix("block/" + turnTableName + "_top")
		));
		remainingBlocks.remove(turntable);

		fixedWallBlock((WallBlock) ModFluffBlocks.dreamwoodWall, prefix("block/dreamwood"));
		fixedWallBlock((WallBlock) ModFluffBlocks.livingrockWall, prefix("block/livingrock"));
		fixedWallBlock((WallBlock) ModFluffBlocks.livingwoodWall, prefix("block/livingwood"));
		remainingBlocks.remove(ModFluffBlocks.dreamwoodWall);
		remainingBlocks.remove(ModFluffBlocks.livingrockWall);
		remainingBlocks.remove(ModFluffBlocks.livingwoodWall);

		fenceBlock((FenceBlock) dreamwoodFence, prefix("block/dreamwood_planks"));
		fenceGateBlock((FenceGateBlock) dreamwoodFenceGate, prefix("block/dreamwood_planks"));
		fenceBlock((FenceBlock) livingwoodFence, prefix("block/livingwood_planks"));
		fenceGateBlock((FenceGateBlock) livingwoodFenceGate, prefix("block/livingwood_planks"));
		remainingBlocks.remove(dreamwoodFence);
		remainingBlocks.remove(dreamwoodFenceGate);
		remainingBlocks.remove(livingwoodFence);
		remainingBlocks.remove(livingwoodFenceGate);

		// TESRs with only particles
		particleOnly(remainingBlocks, animatedTorch, new ResourceLocation("block/redstone_torch"));
		particleOnly(remainingBlocks, avatar, prefix("block/livingwood"));
		particleOnly(remainingBlocks, bellows, prefix("block/livingwood"));
		particleOnly(remainingBlocks, brewery, prefix("block/livingrock"));
		particleOnly(remainingBlocks, corporeaIndex, prefix("block/elementium_block"));
		particleOnly(remainingBlocks, lightRelayDetector, prefix("block/detector_light_relay"));
		simpleBlock(fakeAir, models().getBuilder(Registry.BLOCK.getKey(ModBlocks.fakeAir).getPath()));
		remainingBlocks.remove(fakeAir);
		particleOnly(remainingBlocks, lightRelayFork, prefix("block/fork_light_relay"));
		particleOnly(remainingBlocks, gaiaHead, new ResourceLocation("block/soul_sand"));
		particleOnly(remainingBlocks, gaiaHeadWall, new ResourceLocation("block/soul_sand"));
		particleOnly(remainingBlocks, gaiaPylon, prefix("block/elementium_block"));
		particleOnly(remainingBlocks, hourglass, prefix("block/mana_glass"));
		particleOnly(remainingBlocks, lightRelayDefault, prefix("block/light_relay"));
		particleOnly(remainingBlocks, manaFlame, new ResourceLocation("block/fire_0"));
		particleOnly(remainingBlocks, manaPylon, prefix("block/manasteel_block"));
		particleOnly(remainingBlocks, naturaPylon, prefix("block/terrasteel_block"));
		particleOnly(remainingBlocks, teruTeruBozu, new ResourceLocation("block/white_wool"));
		particleOnly(remainingBlocks, lightRelayToggle, prefix("block/toggle_light_relay"));

		// Block groups
		Predicate<Block> flowers = b -> b instanceof BlockSpecialFlower
				|| b instanceof BlockModMushroom
				|| b instanceof BlockModFlower;
		takeAll(remainingBlocks, flowers).forEach(b -> {
			String name = Registry.BLOCK.getKey(b).getPath();
			ModelFile model = models().withExistingParent(name, prefix("block/shapes/cross"))
					.texture("cross", prefix("block/" + name));
			simpleBlock(b, model);
		});

		takeAll(remainingBlocks, b -> b instanceof BlockMotifFlower).forEach(b -> {
			String name = Registry.BLOCK.getKey(b).getPath().replace("_motif", "");
			ModelFile model = models().withExistingParent(name, prefix("block/shapes/cross"))
					.texture("cross", prefix("block/" + name));
			simpleBlock(b, model);
		});

		takeAll(remainingBlocks, corporeaFunnel, corporeaInterceptor, corporeaRetainer).forEach(b -> {
			String name = Registry.BLOCK.getKey(b).getPath();
			simpleBlock(b, models().cubeColumn(name, prefix("block/" + name + "_side"), prefix("block/" + name + "_end")));
		});

		takeAll(remainingBlocks, gatheringDrum, canopyDrum, wildDrum).forEach(b -> {
			String name = Registry.BLOCK.getKey(b).getPath();
			ModelFile model = models().withExistingParent(name, prefix("block/shapes/drum"))
					.texture("top", prefix("block/drum_top"))
					.texture("side", prefix("block/" + name));
			simpleBlock(b, model);
		});

		takeAll(remainingBlocks, manaSpreader, redstoneSpreader, gaiaSpreader, elvenSpreader).forEach(b -> {
			String name = Registry.BLOCK.getKey(b).getPath();
			String material;
			if (b == elvenSpreader) {
				material = "dreamwood";
			} else if (b == gaiaSpreader) {
				material = name + "_material";
			} else {
				material = "livingwood";
			}
			ModelFile model = models().withExistingParent(name, prefix("block/shapes/spreader"))
					.texture("side", prefix("block/" + name + "_side"))
					.texture("material", prefix("block/" + material));
			models().withExistingParent(name + "_inside", prefix("block/shapes/spreader_inside"))
					.texture("inside", prefix("block/" + name + "_inside"));
			simpleBlock(b, model);
		});

		takeAll(remainingBlocks, manaPool, dilutedPool, fabulousPool, creativePool).forEach(b -> {
			String name = Registry.BLOCK.getKey(b).getPath();
			ResourceLocation tex = b == manaPool || b == fabulousPool
					? prefix("block/livingrock")
					: prefix("block/" + name);
			ModelFile pool = models().withExistingParent(name, prefix("block/shapes/pool"))
					.texture("all", tex);
			models().withExistingParent(name + "_full", prefix("block/shapes/pool_full"))
					.texture("all", tex).texture("liquid", prefix("block/mana_water"));
			simpleBlock(b, pool);
		});

		takeAll(remainingBlocks, pump, tinyPotato).forEach(b -> {
			String name = Registry.BLOCK.getKey(b).getPath();
			ModelFile file = models().getExistingFile(prefix("block/" + name));
			horizontalBlock(b, file);
		});

		takeAll(remainingBlocks, enderEye, manaDetector).forEach(b -> {
			String name = Registry.BLOCK.getKey(b).getPath();
			ModelFile offFile = models().cubeAll(name, prefix("block/" + name));
			ModelFile onFile = models().cubeAll(name + "_powered", prefix("block/" + name + "_powered"));
			getVariantBuilder(b).partialState().with(BlockStateProperties.POWERED, false).setModels(new ConfiguredModel(offFile));
			getVariantBuilder(b).partialState().with(BlockStateProperties.POWERED, true).setModels(new ConfiguredModel(onFile));
		});

		ModelFile petalBlockModel = models().withExistingParent("petal_block", prefix("block/shapes/cube_all_tinted"))
				.texture("all", prefix("block/petal_block"));
		takeAll(remainingBlocks, b -> b instanceof BlockPetalBlock).forEach(b -> simpleBlock(b, petalBlockModel));

		takeAll(remainingBlocks, b -> b instanceof BlockAltGrass).forEach(b -> {
			String name = Registry.BLOCK.getKey(b).getPath();
			ResourceLocation side = prefix("block/" + name + "_side");
			ResourceLocation top = prefix("block/" + name + "_top");
			ModelFile model = models().cubeBottomTop(name, side, new ResourceLocation("block/dirt"), top);
			getVariantBuilder(b).partialState().setModels(new ConfiguredModel(model),
					new ConfiguredModel(model, 0, 90, false),
					new ConfiguredModel(model, 0, 180, false),
					new ConfiguredModel(model, 0, 270, false));
		});

		takeAll(remainingBlocks, b -> b instanceof BlockRedString).forEach(this::redStringBlock);

		takeAll(remainingBlocks, b -> b instanceof BlockModDoubleFlower).forEach(b -> {
			String name = Registry.BLOCK.getKey(b).getPath();
			ModelFile bottom = models().cross(name, prefix("block/" + name));
			ModelFile top = models().cross(name + "_top", prefix("block/" + name + "_top"));
			getVariantBuilder(b)
					.partialState().with(TallFlowerBlock.HALF, DoubleBlockHalf.LOWER).setModels(new ConfiguredModel(bottom))
					.partialState().with(TallFlowerBlock.HALF, DoubleBlockHalf.UPPER).setModels(new ConfiguredModel(top));
		});

		for (String variant : new String[] { "desert", "forest", "fungal", "mesa", "mountain",
				"plains", "swamp", "taiga" }) {
			ResourceLocation baseId = prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_stone");
			Block base = Registry.BLOCK.getOptional(baseId).get();
			simpleBlock(base);

			ResourceLocation cobbleId = prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_cobblestone");
			Block cobble = Registry.BLOCK.getOptional(cobbleId).get();
			simpleBlock(cobble);

			ResourceLocation cobbleWallId = prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_cobblestone" + LibBlockNames.WALL_SUFFIX);
			Block cobbleWall = Registry.BLOCK.getOptional(cobbleWallId).get();
			fixedWallBlock((WallBlock) cobbleWall, prefix("block/" + cobbleId.getPath()));

			ResourceLocation brickId = prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_bricks");
			Block brick = Registry.BLOCK.getOptional(brickId).get();
			simpleBlock(brick);

			ResourceLocation chiseledBricksId = prefix("chiseled_" + LibBlockNames.METAMORPHIC_PREFIX + variant + "_bricks");
			Block chiseledBricks = Registry.BLOCK.getOptional(chiseledBricksId).get();
			simpleBlock(chiseledBricks);

			// stairs and slabs handled above already
			remainingBlocks.removeAll(Arrays.asList(base, cobble, cobbleWall, brick, chiseledBricks));
		}

		for (String variant : new String[] { "dark", "mana", "blaze", "lavender", "red", "elf", "sunny" }) {
			ResourceLocation quartzId = prefix(variant + "_quartz");
			Block quartz = Registry.BLOCK.getOptional(quartzId).get();
			simpleBlock(quartz, models().cubeBottomTop(quartzId.getPath(),
					prefix("block/" + quartzId.getPath() + "_side"),
					prefix("block/" + quartzId.getPath() + "_bottom"),
					prefix("block/" + quartzId.getPath() + "_top")));

			ResourceLocation pillarId = prefix(variant + "_quartz_pillar");
			Block pillar = Registry.BLOCK.getOptional(pillarId).get();
			ModelFile pillarModel = models().cubeColumn(pillarId.getPath(),
					prefix("block/" + pillarId.getPath() + "_side"),
					prefix("block/" + pillarId.getPath() + "_end"));
			getVariantBuilder(pillar)
					.partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.X).setModels(new ConfiguredModel(pillarModel, 90, 90, false))
					.partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.Y).setModels(new ConfiguredModel(pillarModel))
					.partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.Z).setModels(new ConfiguredModel(pillarModel, 90, 0, false));

			ResourceLocation chiseledId = prefix("chiseled_" + variant + "_quartz");
			Block chiseled = Registry.BLOCK.getOptional(chiseledId).get();
			simpleBlock(chiseled, models().cubeColumn(chiseledId.getPath(),
					prefix("block/" + chiseledId.getPath() + "_side"),
					prefix("block/" + chiseledId.getPath() + "_end")));

			remainingBlocks.remove(quartz);
			remainingBlocks.remove(pillar);
			remainingBlocks.remove(chiseled);
		}

		takeAll(remainingBlocks, b -> b instanceof BlockBuriedPetals).forEach(b -> {
			DyeColor color = ((BlockBuriedPetals) b).color;
			ResourceLocation wool = new ResourceLocation("block/" + color.getString() + "_wool");
			particleOnly(remainingBlocks, b, wool);
		});

		takeAll(remainingBlocks, b -> b instanceof BlockAltar).forEach(b -> {
			String name = Registry.BLOCK.getKey(b).getPath();
			ModelFile model = models().withExistingParent(name, prefix("block/shapes/petal_apothecary"))
					.texture("side", prefix("block/" + name + "_side"))
					.texture("goblet", prefix("block/" + name + "_goblet"))
					.texture("top_bottom", prefix("block/" + name + "_top_bottom"));
			simpleBlock(b, model);
		});

		takeAll(remainingBlocks, b -> b instanceof BlockFloatingFlower).forEach(b -> {
			String name = Registry.BLOCK.getKey(b).getPath();
			simpleBlock(b, new ModelFile.UncheckedModelFile(prefix("block/" + name)));
		});

		takeAll(remainingBlocks, b -> b instanceof PaneBlock).forEach(b -> {
			String name = Registry.BLOCK.getKey(b).getPath();
			ResourceLocation edge = prefix("block/" + name);
			ResourceLocation pane = prefix("block/" + name.substring(0, name.length() - "_pane".length()));
			paneBlock((PaneBlock) b, pane, edge);
		});

		takeAll(remainingBlocks, b -> b instanceof StairsBlock).forEach(b -> {
			String name = Registry.BLOCK.getKey(b).getPath();
			String baseName = name.substring(0, name.length() - LibBlockNames.STAIR_SUFFIX.length());
			boolean quartz = name.contains("quartz");
			if (quartz) {
				ResourceLocation side = prefix("block/" + baseName + "_side");
				ResourceLocation bottom = prefix("block/" + baseName + "_bottom");
				ResourceLocation top = prefix("block/" + baseName + "_top");
				stairsBlock((StairsBlock) b, side, bottom, top);
			} else {
				stairsBlock((StairsBlock) b, prefix("block/" + baseName));
			}
		});

		// Double slab generation refers to the cube all model, so let them be generated first
		Iterable<Block> slabs = takeAll(remainingBlocks, b -> b instanceof SlabBlock);

		remainingBlocks.forEach(this::simpleBlock);

		slabs.forEach(b -> {
			String name = Registry.BLOCK.getKey(b).getPath();
			String baseName = name.substring(0, name.length() - LibBlockNames.SLAB_SUFFIX.length());
			boolean quartz = name.contains("quartz");
			if (quartz) {
				ResourceLocation side = prefix("block/" + baseName + "_side");
				ResourceLocation bottom = prefix("block/" + baseName + "_bottom");
				ResourceLocation top = prefix("block/" + baseName + "_top");
				slabBlock((SlabBlock) b, prefix(baseName), side, bottom, top);
			} else {
				slabBlock((SlabBlock) b, prefix(baseName), prefix("block/" + baseName));
			}
		});
	}

	private void particleOnly(Set<Block> blocks, Block b, ResourceLocation particle) {
		String name = Registry.BLOCK.getKey(b).getPath();
		ModelFile f = models().getBuilder(name)
				.texture("particle", particle);
		simpleBlock(b, f);
		blocks.remove(b);
	}

	private void manualModel(Set<Block> blocks, Block b) {
		String name = Registry.BLOCK.getKey(b).getPath();
		simpleBlock(b, models().getExistingFile(prefix("block/" + name)));
		blocks.remove(b);
	}

	// ? extends T technically not correct, but is more convenient in ItemModelProvider
	@SafeVarargs
	@SuppressWarnings("varargs")
	public static <T> Collection<T> takeAll(Set<? extends T> src, T... items) {
		List<T> ret = Arrays.asList(items);
		for (T item : items) {
			if (!src.contains(item)) {
				Botania.LOGGER.warn("Item {} not found in set", item);
			}
		}
		if (!src.removeAll(ret)) {
			Botania.LOGGER.warn("takeAll array didn't yield anything ({})", Arrays.toString(items));
		}
		return ret;
	}

	public static <T> Collection<T> takeAll(Set<T> src, Predicate<T> pred) {
		List<T> ret = new ArrayList<>();

		Iterator<T> iter = src.iterator();
		while (iter.hasNext()) {
			T item = iter.next();
			if (pred.test(item)) {
				iter.remove();
				ret.add(item);
			}
		}

		if (ret.isEmpty()) {
			Botania.LOGGER.warn("takeAll predicate yielded nothing", new Throwable());
		}
		return ret;
	}

	private static final Map<Direction, EnumProperty<WallHeight>> DIRECTION_TO_WALL_SIDE = ImmutableMap.<Direction, EnumProperty<WallHeight>>builder()
			.put(Direction.NORTH, WallBlock.WALL_HEIGHT_NORTH)
			.put(Direction.EAST, WallBlock.WALL_HEIGHT_EAST)
			.put(Direction.SOUTH, WallBlock.WALL_HEIGHT_SOUTH)
			.put(Direction.WEST, WallBlock.WALL_HEIGHT_WEST).build();

	// Copy of super but fixed to account for blockstate property changes in 1.16
	private void fixedWallBlock(WallBlock block, ResourceLocation tex) {
		String name = Registry.BLOCK.getKey(block).getPath();
		ModelFile post = models().withExistingParent(name + "_post", "block/template_wall_post")
				.texture("wall", tex);
		ModelFile side = models().withExistingParent(name + "_wall_side", "block/template_wall_side")
				.texture("wall", tex);
		ModelFile tallSide = models().withExistingParent(name + "_wall_side_tall", "block/template_wall_side_tall")
				.texture("wall", tex);
		MultiPartBlockStateBuilder builder = getMultipartBuilder(block)
				.part().modelFile(post).addModel()
				.condition(WallBlock.UP, true).end();

		DIRECTION_TO_WALL_SIDE.forEach((dir, value) -> {
			builder.part().modelFile(side).rotationY((((int) dir.getHorizontalAngle()) + 180) % 360).uvLock(true).addModel()
					.condition(value, WallHeight.LOW).end()
					.part().modelFile(tallSide).rotationY((((int) dir.getHorizontalAngle()) + 180) % 360).uvLock(true).addModel()
					.condition(value, WallHeight.TALL);
		});
	}

	private void redStringBlock(Block b) {
		String name = Registry.BLOCK.getKey(b).getPath();
		ResourceLocation selfName = prefix("block/" + name);
		ResourceLocation front = prefix("block/red_string_sender");
		ModelFile file = models().orientable(name, selfName, front, selfName);
		getVariantBuilder(b)
				.partialState().with(BlockStateProperties.FACING, Direction.NORTH).setModels(new ConfiguredModel(file))
				.partialState().with(BlockStateProperties.FACING, Direction.SOUTH).setModels(new ConfiguredModel(file, 0, 180, false))
				.partialState().with(BlockStateProperties.FACING, Direction.WEST).setModels(new ConfiguredModel(file, 0, 270, false))
				.partialState().with(BlockStateProperties.FACING, Direction.EAST).setModels(new ConfiguredModel(file, 0, 90, false))
				.partialState().with(BlockStateProperties.FACING, Direction.DOWN).setModels(new ConfiguredModel(file, 90, 0, false))
				.partialState().with(BlockStateProperties.FACING, Direction.UP).setModels(new ConfiguredModel(file, 270, 0, false));
	}
}
