/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Oct 1, 2015, 1:11:26 PM (GMT)]
 */
package vazkii.botania.common.block;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.BlockRenderType;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.block.tile.TileTeruTeruBozu;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;

import javax.annotation.Nonnull;

public class BlockTeruTeruBozu extends BlockMod implements ILexiconable {

	private static final VoxelShape SHAPE = makeCuboidShape(4, 0.16, 4, 12, 15.84, 12);

	public BlockTeruTeruBozu(Properties builder) {
		super(builder);
	}

	@Nonnull
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext ctx) {
		return SHAPE;
	}

	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity e) {
		if(!world.isRemote && e instanceof ItemEntity) {
			ItemEntity item = (ItemEntity) e;
			ItemStack stack = item.getItem();
			if(isSunflower(stack) && removeRain(world) || isBlueOrchid(stack) && startRain(world)) {
				stack.shrink(1);
			}
		}
	}

	@Override
	public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, Direction s, float xs, float ys, float zs) {
		ItemStack stack = player.getHeldItem(hand);
		if(!stack.isEmpty() && (isSunflower(stack) && removeRain(world) || isBlueOrchid(stack) && startRain(world))) {
			if(!player.abilities.isCreativeMode)
				stack.shrink(1);
			return true;
		}
		return false;
	}

	private boolean isSunflower(ItemStack stack) {
		return stack.getItem() == Blocks.SUNFLOWER.asItem();
	}

	private boolean isBlueOrchid(ItemStack stack) {
		return stack.getItem() == Blocks.BLUE_ORCHID.asItem();
	}

	private boolean removeRain(World world) {
		if(world.isRaining()) {
			world.getWorldInfo().setRaining(false);
			TileTeruTeruBozu.resetRainTime(world);
			return true;
		}
		return false;
	}

	private boolean startRain(World world) {
		if(!world.isRaining()) {
			if(world.rand.nextInt(10) == 0) {
				world.getWorldInfo().setRaining(true);
				TileTeruTeruBozu.resetRainTime(world);
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean hasComparatorInputOverride(BlockState state) {
		return true;
	}

	@Override
	public int getComparatorInputOverride(BlockState state, World world, BlockPos pos) {
		return world.isRaining() ? 15 : 0;
	}

	@Override
	public boolean isFullCube(BlockState state) {
		return false;
	}

	@Nonnull
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull BlockState state, @Nonnull IBlockReader world) {
		return new TileTeruTeruBozu();
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, PlayerEntity player, ItemStack lexicon) {
		return LexiconData.teruTeruBozu;
	}

}
