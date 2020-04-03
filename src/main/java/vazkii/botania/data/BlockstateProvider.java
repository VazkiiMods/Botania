/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.data;

import net.minecraft.block.Block;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.PaneBlock;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.TallFlowerBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.data.DataGenerator;
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

import vazkii.botania.common.block.BlockAltGrass;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.decor.BlockPetalBlock;
import vazkii.botania.common.block.string.BlockRedString;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;

import java.util.stream.IntStream;

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
		Registry.BLOCK.stream().filter(b -> LibMisc.MOD_ID.equals(b.getRegistryName().getNamespace()))
				.forEach(b -> {
					if (b == ModBlocks.craftCrate || b == ModBlocks.ghostRail
							|| b == ModBlocks.solidVines) {
						return;
					}

					String name = b.getRegistryName().getPath();
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
					} else if (b instanceof WallBlock) {
						ModelFile post = models().getExistingFile(prefix("block/" + name + "_post"));
						ModelFile side = models().getExistingFile(prefix("block/" + name + "_side"));
						wallBlock((WallBlock) b, post, side);
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
					} else if (b instanceof TallFlowerBlock) {
						ModelFile bottom = models().getExistingFile(prefix("block/" + name));
						ModelFile top = models().getExistingFile(prefix("block/" + name + "_top"));
						getVariantBuilder(b)
								.partialState().with(TallFlowerBlock.HALF, DoubleBlockHalf.LOWER).setModels(new ConfiguredModel(bottom))
								.partialState().with(TallFlowerBlock.HALF, DoubleBlockHalf.UPPER).setModels(new ConfiguredModel(top));
					} else if (b instanceof BlockAltGrass) {
						ModelFile model = models().getExistingFile(prefix("block/" + name));
						getVariantBuilder(b).partialState().setModels(new ConfiguredModel(model),
								new ConfiguredModel(model, 0, 90, false),
								new ConfiguredModel(model, 0, 180, false),
								new ConfiguredModel(model, 0, 270, false));
					} else if (b == ModBlocks.gaiaHead || b == ModBlocks.gaiaHeadWall) {
						ModelFile file = models().getExistingFile(new ResourceLocation("block/soul_sand"));
						getVariantBuilder(b).partialState().setModels(new ConfiguredModel(file));
					} else if (b instanceof BlockPetalBlock) {
						ModelFile file = models().getExistingFile(prefix("block/petal_block"));
						getVariantBuilder(b).partialState().setModels(new ConfiguredModel(file));
					} else if (b instanceof BlockRedString) {
						redStringBlock(b);
					} else if (b == ModBlocks.elfGlass) {
						ConfiguredModel[] files = IntStream.rangeClosed(0, 3)
								.mapToObj(i -> models().getExistingFile(prefix("block/" + name + "_" + i)))
								.map(ConfiguredModel::new).toArray(ConfiguredModel[]::new);
						getVariantBuilder(b).partialState().setModels(files);
					} else if (b == ModBlocks.enderEye || b == ModBlocks.manaDetector) {
						ModelFile offFile = models().getExistingFile(prefix("block/" + name));
						ModelFile onFile = models().getExistingFile(prefix("block/" + name + "_powered"));
						getVariantBuilder(b).partialState().with(BlockStateProperties.POWERED, false).setModels(new ConfiguredModel(offFile));
						getVariantBuilder(b).partialState().with(BlockStateProperties.POWERED, true).setModels(new ConfiguredModel(onFile));
					} else if (b == ModBlocks.tinyPotato || b == ModBlocks.felPumpkin || b == ModBlocks.pump) {
						ModelFile file = models().getExistingFile(prefix("block/" + name));
						horizontalBlock(b, file);
					} else if (b == ModBlocks.incensePlate) {
						ModelFile file = models().getExistingFile(prefix("block/" + name));
						horizontalBlock(b, file, 0);
					} else {
						simpleBlock(b, models().getExistingFile(prefix("block/" + name)));
					}
				});
	}

	private void redStringBlock(Block b) {
		ModelFile file = models().getExistingFile(prefix("block/" + b.getRegistryName().getPath()));
		getVariantBuilder(b)
				.partialState().with(BlockStateProperties.FACING, Direction.NORTH).setModels(new ConfiguredModel(file))
				.partialState().with(BlockStateProperties.FACING, Direction.SOUTH).setModels(new ConfiguredModel(file, 0, 180, false))
				.partialState().with(BlockStateProperties.FACING, Direction.WEST).setModels(new ConfiguredModel(file, 0, 270, false))
				.partialState().with(BlockStateProperties.FACING, Direction.EAST).setModels(new ConfiguredModel(file, 0, 90, false))
				.partialState().with(BlockStateProperties.FACING, Direction.DOWN).setModels(new ConfiguredModel(file, 90, 0, false))
				.partialState().with(BlockStateProperties.FACING, Direction.UP).setModels(new ConfiguredModel(file, 270, 0, false));
	}
}
