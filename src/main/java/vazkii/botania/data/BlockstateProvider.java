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
import net.minecraft.state.properties.SlabType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder;

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
		// Manually written blockstates + models
		blocks.remove(craftCrate);
		blocks.remove(ghostRail);
		blocks.remove(solidVines);

		// Single blocks
		String elfGlassName = Registry.BLOCK.getKey(elfGlass).getPath();
		ConfiguredModel[] elfGlassFiles = IntStream.rangeClosed(0, 3)
				.mapToObj(i -> models().getExistingFile(prefix("block/" + elfGlassName + "_" + i)))
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

		// TESRs with only particles
		particleOnly(animatedTorch, new ResourceLocation("block/redstone_torch"));
		particleOnly(avatar, prefix("block/livingwood"));
		particleOnly(bellows, prefix("block/livingwood"));
		particleOnly(brewery, prefix("block/livingrock"));
		particleOnly(corporeaIndex, prefix("block/elementium_block"));
		particleOnly(lightRelayDetector, prefix("block/luminizer_detector"));
		simpleBlock(fakeAir, models().getBuilder(Registry.BLOCK.getKey(ModBlocks.fakeAir).getPath()));
		particleOnly(lightRelayFork, prefix("block/luminizer_fork"));
		particleOnly(gaiaHead, new ResourceLocation("block/soul_sand"));
		particleOnly(gaiaHeadWall, new ResourceLocation("block/soul_sand"));
		particleOnly(gaiaPylon, prefix("block/elementium_block"));
		particleOnly(hourglass, prefix("block/mana_glass"));
		particleOnly(lightRelayDefault, prefix("block/luminizer"));
		particleOnly(manaFlame, new ResourceLocation("block/fire_0"));
		particleOnly(manaPylon, prefix("block/manasteel_block"));
		particleOnly(naturaPylon, prefix("block/terrasteel_block"));
		particleOnly(teruTeruBozu, new ResourceLocation("block/white_wool"));
		particleOnly(lightRelayToggle, prefix("block/luminizer_toggle"));

		blocks.removeAll(Arrays.asList(animatedTorch, avatar, bellows, brewery, corporeaIndex, lightRelayDetector,
				fakeAir, lightRelayFork, gaiaHead, gaiaHeadWall, gaiaPylon, hourglass,
				lightRelayDefault, manaFlame, manaPylon, naturaPylon, teruTeruBozu, lightRelayToggle));

		// Block types
		Predicate<Block> flowers = b -> b instanceof BlockSpecialFlower
				|| b instanceof BlockModMushroom
				|| b instanceof BlockModFlower;
		takeAll(blocks, flowers).forEach(b -> {
			String name = Registry.BLOCK.getKey(b).getPath();
			ModelFile model = models().withExistingParent(name, prefix("block/shapes/cross"))
					.texture("cross", prefix("block/" + name));
			simpleBlock(b, model);
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

			ResourceLocation slabId = prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_stone" + LibBlockNames.SLAB_SUFFIX);
			Block slab = Registry.BLOCK.getValue(slabId).get();
			slabBlock((SlabBlock) slab, baseId, prefix("block/" + baseId.getPath()));

			ResourceLocation stairId = prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_stone" + LibBlockNames.STAIR_SUFFIX);
			Block stair = Registry.BLOCK.getValue(stairId).get();
			stairsBlock((StairsBlock) stair, prefix("block/" + baseId.getPath()));

			ResourceLocation cobbleId = prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_cobblestone");
			Block cobble = Registry.BLOCK.getValue(cobbleId).get();
			simpleBlock(cobble);

			ResourceLocation cobbleSlabId = prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_cobblestone" + LibBlockNames.SLAB_SUFFIX);
			Block cobbleSlab = Registry.BLOCK.getValue(cobbleSlabId).get();
			slabBlock((SlabBlock) cobbleSlab, cobbleId, prefix("block/" + cobbleId.getPath()));

			ResourceLocation cobbleStairId = prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_cobblestone" + LibBlockNames.STAIR_SUFFIX);
			Block cobbleStair = Registry.BLOCK.getValue(cobbleStairId).get();
			stairsBlock((StairsBlock) cobbleStair, prefix("block/" + cobbleId.getPath()));

			ResourceLocation cobbleWallId = prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_cobblestone" + LibBlockNames.WALL_SUFFIX);
			Block cobbleWall = Registry.BLOCK.getValue(cobbleWallId).get();
			fixedWallBlock((WallBlock) cobbleWall, prefix("block/" + cobbleId.getPath()));

			ResourceLocation brickId = prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_bricks");
			Block brick = Registry.BLOCK.getValue(brickId).get();
			simpleBlock(brick);

			ResourceLocation brickSlabId = prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_bricks" + LibBlockNames.SLAB_SUFFIX);
			Block brickSlab = Registry.BLOCK.getValue(brickSlabId).get();
			slabBlock((SlabBlock) brickSlab, brickId, prefix("block/" + brickId.getPath()));

			ResourceLocation brickStairId = prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_bricks" + LibBlockNames.STAIR_SUFFIX);
			Block brickStair = Registry.BLOCK.getValue(brickStairId).get();
			stairsBlock((StairsBlock) brickStair, prefix("block/" + brickId.getPath()));

			ResourceLocation chiseledBricksId = prefix("chiseled_" + LibBlockNames.METAMORPHIC_PREFIX + variant + "_bricks");
			Block chiseledBricks = Registry.BLOCK.getValue(chiseledBricksId).get();
			simpleBlock(chiseledBricks);

			blocks.removeAll(Arrays.asList(base, slab, stair, cobble, cobbleSlab, cobbleStair, cobbleWall,
				brick, brickSlab, brickStair, chiseledBricks));
		}

		takeAll(blocks, b -> b instanceof BlockBuriedPetals).forEach(b -> {
			DyeColor color = ((BlockBuriedPetals) b).color;
			ResourceLocation wool = new ResourceLocation("block/" + color.func_176610_l() + "_wool");
			particleOnly(b, wool);
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
			} else if (b instanceof SlabBlock) {
				ModelFile file = models().getExistingFile(prefix("block/" + name));
				ModelFile fullFile = models().getExistingFile(prefix("block/" + name.substring(0, name.length() - LibBlockNames.SLAB_SUFFIX.length())));
				getVariantBuilder(b)
						.partialState().with(SlabBlock.TYPE, SlabType.BOTTOM).setModels(new ConfiguredModel(file))
						.partialState().with(SlabBlock.TYPE, SlabType.TOP).setModels(new ConfiguredModel(file, 180, 0, true))
						.partialState().with(SlabBlock.TYPE, SlabType.DOUBLE).setModels(new ConfiguredModel(fullFile));
			} else if (b instanceof StairsBlock) {
				ModelFile stair = models().getExistingFile(prefix("block/" + name));
				ModelFile inner = models().getExistingFile(prefix("block/" + name + "_inner"));
				ModelFile outer = models().getExistingFile(prefix("block/" + name + "_outer"));
				stairsBlock((StairsBlock) b, stair, inner, outer);
			} else if (b instanceof FenceBlock) {
				ModelFile post = models().getExistingFile(prefix("block/" + name + "_post"));
				ModelFile side = models().getExistingFile(prefix("block/" + name + "_side"));
				fourWayBlock((FenceBlock) b, post, side);
			} else if (b instanceof FenceGateBlock) {
				ModelFile gate = models().getExistingFile(prefix("block/" + name));
				ModelFile gateOpen = models().getExistingFile(prefix("block/" + name + "_open"));
				ModelFile wall = models().getExistingFile(prefix("block/" + name + "_wall"));
				ModelFile wallOpen = models().getExistingFile(prefix("block/" + name + "_wall_open"));
				fenceGateBlock((FenceGateBlock) b, gate, gateOpen, wall, wallOpen);
			} else if (b instanceof PaneBlock) {
				ModelFile post = models().getExistingFile(prefix("block/" + name + "_post"));
				ModelFile side = models().getExistingFile(prefix("block/" + name + "_side"));
				ModelFile sideAlt = models().getExistingFile(prefix("block/" + name + "_side_alt"));
				ModelFile noSide = models().getExistingFile(prefix("block/" + name + "_noside"));
				ModelFile noSideAlt = models().getExistingFile(prefix("block/" + name + "_noside_alt"));
				paneBlock((PaneBlock) b, post, side, sideAlt, noSide, noSideAlt);
			} else if (b instanceof BlockPetalBlock) {
				ModelFile file = models().getExistingFile(prefix("block/petal_block"));
				getVariantBuilder(b).partialState().setModels(new ConfiguredModel(file));
			} else if (b == ModBlocks.enderEye || b == ModBlocks.manaDetector) {
				ModelFile offFile = models().getExistingFile(prefix("block/" + name));
				ModelFile onFile = models().getExistingFile(prefix("block/" + name + "_powered"));
				getVariantBuilder(b).partialState().with(BlockStateProperties.POWERED, false).setModels(new ConfiguredModel(offFile));
				getVariantBuilder(b).partialState().with(BlockStateProperties.POWERED, true).setModels(new ConfiguredModel(onFile));
			} else if (b == ModBlocks.tinyPotato || b == ModBlocks.felPumpkin || b == ModBlocks.pump) {
				ModelFile file = models().getExistingFile(prefix("block/" + name));
				horizontalBlock(b, file);
			} else {
				simpleBlock(b, models().getExistingFile(prefix("block/" + name)));
			}
		});
	}

	private void particleOnly(Block b, ResourceLocation particle) {
		String name = Registry.BLOCK.getKey(b).getPath();
		ModelFile f = models().getBuilder(name)
				.texture("particle", particle);
		simpleBlock(b, f);
	}

	@SafeVarargs
	private static <T> Iterable<T> takeAll(Set<T> src, T... items) {
		List<T> ret = Arrays.asList(items);
		src.removeAll(ret);
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
