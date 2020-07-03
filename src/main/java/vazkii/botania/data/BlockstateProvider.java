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
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder;

import vazkii.botania.common.Botania;
import vazkii.botania.common.block.*;
import vazkii.botania.common.block.decor.BlockBuriedPetals;
import vazkii.botania.common.block.decor.BlockModMushroom;
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
		Set<Block> blocks = Registry.BLOCK.stream()
				.filter(b -> LibMisc.MOD_ID.equals(Registry.BLOCK.getKey(b).getNamespace()))
				.collect(Collectors.toSet());
		// Manually written blockstate + models
		blocks.remove(craftCrate);
		blocks.remove(ghostRail);
		blocks.remove(solidVines);

		// Manually written simpleBlock
		manualModel(blocks, cocoon);
		manualModel(blocks, corporeaCrystalCube);
		manualModel(blocks, runeAltar);

		// Single blocks
		String elfGlassName = Registry.BLOCK.getKey(elfGlass).getPath();
		ConfiguredModel[] elfGlassFiles = IntStream.rangeClosed(0, 3)
				.mapToObj(i -> {
					String varName = elfGlassName + "_" + i;
					return models().cubeAll(varName, prefix("block/" + varName));
				})
				.map(ConfiguredModel::new).toArray(ConfiguredModel[]::new);
		getVariantBuilder(elfGlass).partialState().setModels(elfGlassFiles);
		blocks.remove(elfGlass);

		String plateName = Registry.BLOCK.getKey(ModBlocks.incensePlate).getPath();
		ModelFile plateFile = models().getExistingFile(prefix("block/" + plateName));
		horizontalBlock(incensePlate, plateFile, 0);
		blocks.remove(incensePlate);

		fixedWallBlock((WallBlock) ModFluffBlocks.dreamwoodWall, prefix("block/dreamwood"));
		fixedWallBlock((WallBlock) ModFluffBlocks.livingrockWall, prefix("block/livingrock"));
		fixedWallBlock((WallBlock) ModFluffBlocks.livingwoodWall, prefix("block/livingwood"));
		blocks.remove(ModFluffBlocks.dreamwoodWall);
		blocks.remove(ModFluffBlocks.livingrockWall);
		blocks.remove(ModFluffBlocks.livingwoodWall);

		fenceBlock((FenceBlock) dreamwoodFence, prefix("block/dreamwood_planks"));
		fenceGateBlock((FenceGateBlock) dreamwoodFenceGate, prefix("block/dreamwood_planks"));
		fenceBlock((FenceBlock) livingwoodFence, prefix("block/livingwood_planks"));
		fenceGateBlock((FenceGateBlock) livingwoodFenceGate, prefix("block/livingwood_planks"));
		blocks.remove(dreamwoodFence);
		blocks.remove(dreamwoodFenceGate);
		blocks.remove(livingwoodFence);
		blocks.remove(livingwoodFenceGate);

		String felName = Registry.BLOCK.getKey(felPumpkin).getPath();
		simpleBlock(felPumpkin, models().orientable(felName, new ResourceLocation("block/pumpkin_side"), prefix("block/" + felName),
			new ResourceLocation("block/pumpkin_top")));
		blocks.remove(felPumpkin);

		// TESRs with only particles
		particleOnly(blocks, animatedTorch, new ResourceLocation("block/redstone_torch"));
		particleOnly(blocks, avatar, prefix("block/livingwood"));
		particleOnly(blocks, bellows, prefix("block/livingwood"));
		particleOnly(blocks, brewery, prefix("block/livingrock"));
		particleOnly(blocks, corporeaIndex, prefix("block/elementium_block"));
		particleOnly(blocks, lightRelayDetector, prefix("block/luminizer_detector"));
		simpleBlock(fakeAir, models().getBuilder(Registry.BLOCK.getKey(ModBlocks.fakeAir).getPath()));
		blocks.remove(fakeAir);
		particleOnly(blocks, lightRelayFork, prefix("block/luminizer_fork"));
		particleOnly(blocks, gaiaHead, new ResourceLocation("block/soul_sand"));
		particleOnly(blocks, gaiaHeadWall, new ResourceLocation("block/soul_sand"));
		particleOnly(blocks, gaiaPylon, prefix("block/elementium_block"));
		particleOnly(blocks, hourglass, prefix("block/mana_glass"));
		particleOnly(blocks, lightRelayDefault, prefix("block/luminizer"));
		particleOnly(blocks, manaFlame, new ResourceLocation("block/fire_0"));
		particleOnly(blocks, manaPylon, prefix("block/manasteel_block"));
		particleOnly(blocks, naturaPylon, prefix("block/terrasteel_block"));
		particleOnly(blocks, teruTeruBozu, new ResourceLocation("block/white_wool"));
		particleOnly(blocks, lightRelayToggle, prefix("block/luminizer_toggle"));

		// Block groups
		Predicate<Block> flowers = b -> b instanceof BlockSpecialFlower
				|| b instanceof BlockModMushroom
				|| b instanceof BlockModFlower;
		takeAll(blocks, flowers).forEach(b -> {
			String name = Registry.BLOCK.getKey(b).getPath();
			ModelFile model = models().withExistingParent(name, prefix("block/shapes/cross"))
					.texture("cross", prefix("block/" + name));
			simpleBlock(b, model);
		});

		takeAll(blocks, pump, tinyPotato).forEach(b -> {
			String name = Registry.BLOCK.getKey(b).getPath();
			ModelFile file = models().getExistingFile(prefix("block/" + name));
			horizontalBlock(b, file);
		});

		takeAll(blocks, enderEye, manaDetector).forEach(b -> {
			String name = Registry.BLOCK.getKey(b).getPath();
			ModelFile offFile = models().cubeAll(name, prefix("block/" + name));
			ModelFile onFile = models().cubeAll(name + "_powered", prefix("block/" + name + "_powered"));
			getVariantBuilder(b).partialState().with(BlockStateProperties.POWERED, false).setModels(new ConfiguredModel(offFile));
			getVariantBuilder(b).partialState().with(BlockStateProperties.POWERED, true).setModels(new ConfiguredModel(onFile));
		});

		ModelFile petalBlockModel = models().withExistingParent("petal_block", prefix("block/shapes/cube_all_tinted"))
			.texture("all", prefix("block/petal_block"));
		takeAll(blocks, b -> b instanceof BlockPetalBlock).forEach(b -> simpleBlock(b, petalBlockModel));

		takeAll(blocks, b -> b instanceof StairsBlock).forEach(b -> {
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

		takeAll(blocks, b -> b instanceof SlabBlock).forEach(b -> {
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

		takeAll(blocks, b -> b instanceof BlockAltGrass).forEach(b -> {
			String name = Registry.BLOCK.getKey(b).getPath();
			ResourceLocation side = prefix("block/" + name + "_side");
			ResourceLocation top = prefix("block/" + name + "_top");
			ModelFile model = models().cubeBottomTop(name, side, new ResourceLocation("block/dirt"), top);
			getVariantBuilder(b).partialState().setModels(new ConfiguredModel(model),
					new ConfiguredModel(model, 0, 90, false),
					new ConfiguredModel(model, 0, 180, false),
					new ConfiguredModel(model, 0, 270, false));
		});

		takeAll(blocks, b -> b instanceof BlockRedString).forEach(this::redStringBlock);

		takeAll(blocks, b -> b instanceof BlockModDoubleFlower).forEach(b -> {
			String name = Registry.BLOCK.getKey(b).getPath();
			ModelFile bottom = models().cross(name, prefix("block/" + name));
			ModelFile top = models().cross(name + "_top", prefix("block/" + name + "_top"));
			getVariantBuilder(b)
					.partialState().with(TallFlowerBlock.HALF, DoubleBlockHalf.LOWER).setModels(new ConfiguredModel(bottom))
					.partialState().with(TallFlowerBlock.HALF, DoubleBlockHalf.UPPER).setModels(new ConfiguredModel(top));
		});

		for (String variant : new String[] { "desert", "forest", "fungal", "mesa", "mountain",
			"plains", "swamp", "taiga"}) {
			ResourceLocation baseId = prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_stone");
			Block base = Registry.BLOCK.getValue(baseId).get();
			simpleBlock(base);

			ResourceLocation cobbleId = prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_cobblestone");
			Block cobble = Registry.BLOCK.getValue(cobbleId).get();
			simpleBlock(cobble);

			ResourceLocation cobbleWallId = prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_cobblestone" + LibBlockNames.WALL_SUFFIX);
			Block cobbleWall = Registry.BLOCK.getValue(cobbleWallId).get();
			fixedWallBlock((WallBlock) cobbleWall, prefix("block/" + cobbleId.getPath()));

			ResourceLocation brickId = prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_bricks");
			Block brick = Registry.BLOCK.getValue(brickId).get();
			simpleBlock(brick);

			ResourceLocation chiseledBricksId = prefix("chiseled_" + LibBlockNames.METAMORPHIC_PREFIX + variant + "_bricks");
			Block chiseledBricks = Registry.BLOCK.getValue(chiseledBricksId).get();
			simpleBlock(chiseledBricks);

			// stairs and slabs handled above already
			blocks.removeAll(Arrays.asList(base, cobble, cobbleWall, brick, chiseledBricks));
		}

		takeAll(blocks, b -> b instanceof BlockBuriedPetals).forEach(b -> {
			DyeColor color = ((BlockBuriedPetals) b).color;
			ResourceLocation wool = new ResourceLocation("block/" + color.func_176610_l() + "_wool");
			particleOnly(blocks, b, wool);
		});

		takeAll(blocks, b -> b instanceof BlockAltar).forEach(b -> {
			String name = Registry.BLOCK.getKey(b).getPath();
			ModelFile model = models().withExistingParent(name, prefix("block/shapes/petal_apothecary"))
				.texture("side", prefix("block/" + name + "_side"))
				.texture("goblet", prefix("block/" + name + "_goblet"))
				.texture("top_bottom", prefix("block/" + name + "_top_bottom"));
			simpleBlock(b, model);
		});

		blocks.forEach(b -> {
			String name = Registry.BLOCK.getKey(b).getPath();
			if (name.contains("quartz") && b instanceof RotatedPillarBlock) {
				ModelFile file = models().getExistingFile(prefix("block/" + name));
				getVariantBuilder(b)
						.partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.X).setModels(new ConfiguredModel(file, 90, 90, false))
						.partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.Y).setModels(new ConfiguredModel(file))
						.partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.Z).setModels(new ConfiguredModel(file, 90, 0, false));
			} else if (b instanceof PaneBlock) {
				ModelFile post = models().getExistingFile(prefix("block/" + name + "_post"));
				ModelFile side = models().getExistingFile(prefix("block/" + name + "_side"));
				ModelFile sideAlt = models().getExistingFile(prefix("block/" + name + "_side_alt"));
				ModelFile noSide = models().getExistingFile(prefix("block/" + name + "_noside"));
				ModelFile noSideAlt = models().getExistingFile(prefix("block/" + name + "_noside_alt"));
				paneBlock((PaneBlock) b, post, side, sideAlt, noSide, noSideAlt);
			} else {
				simpleBlock(b, models().getExistingFile(prefix("block/" + name)));
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

	@SafeVarargs
	private static <T> Iterable<T> takeAll(Set<T> src, T... items) {
		List<T> ret = Arrays.asList(items);
		if (!src.removeAll(ret)) {
			Botania.LOGGER.warn("takeAll array didn't yield anything ({})", Arrays.toString(items));
		}
		return ret;
	}

	private static <T> Iterable<T> takeAll(Set<T> src, Predicate<T> pred) {
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
			.put(Direction.NORTH, WallBlock.field_235613_c_)
			.put(Direction.EAST, WallBlock.field_235612_b_)
			.put(Direction.SOUTH, WallBlock.field_235614_d_)
			.put(Direction.WEST, WallBlock.field_235615_e_).build();

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
