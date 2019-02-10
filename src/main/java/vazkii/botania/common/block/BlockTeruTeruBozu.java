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

import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
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

	public BlockTeruTeruBozu(Builder builder) {
		super(builder);
	}

	@Nonnull
	@Override
	public VoxelShape getShape(IBlockState state, IBlockReader world, BlockPos pos) {
		return SHAPE;
	}

	@Override
	public void onEntityCollision(IBlockState state, World world, BlockPos pos, Entity e) {
		if(!world.isRemote && e instanceof EntityItem) {
			EntityItem item = (EntityItem) e;
			ItemStack stack = item.getItem();
			if(isSunflower(stack) && removeRain(world) || isBlueOrchid(stack) && startRain(world)) {
				stack.shrink(1);
			}
		}
	}

	@Override
	public boolean onBlockActivated(IBlockState state, World world, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing s, float xs, float ys, float zs) {
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
	public boolean hasComparatorInputOverride(IBlockState state) {
		return true;
	}

	@Override
	public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
		return world.isRaining() ? 15 : 0;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Nonnull
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull IBlockState state, @Nonnull IBlockReader world) {
		return new TileTeruTeruBozu();
	}

	@Override
	public LexiconEntry getEntry(World world, BlockPos pos, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.teruTeruBozu;
	}

	@Nonnull
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockReader world, IBlockState state, BlockPos pos, EnumFacing side) {
		return BlockFaceShape.UNDEFINED;
	}

}
