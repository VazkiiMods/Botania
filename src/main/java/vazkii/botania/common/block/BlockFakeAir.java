/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Apr 10, 2015, 10:22:38 PM (GMT)]
 */
package vazkii.botania.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import vazkii.botania.common.block.tile.TileFakeAir;
import vazkii.botania.common.lib.LibBlockNames;

import javax.annotation.Nonnull;
import java.util.Random;

public class BlockFakeAir extends BlockMod {

	private static final AxisAlignedBB AABB = new AxisAlignedBB(0, 0, 0, 0, 0, 0);

	public BlockFakeAir() {
		super(Material.STRUCTURE_VOID, LibBlockNames.FAKE_AIR);
		setTickRandomly(true);
	}

	@Nonnull
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		return AABB;
	}

	@Nonnull
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.INVISIBLE;
	}

	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
		if(shouldRemove(world, pos))
			world.scheduleUpdate(pos, this, tickRate(world));
	}

	private boolean shouldRemove(World world, BlockPos pos) {
		return !world.isRemote && world.getTileEntity(pos) == null || !(world.getTileEntity(pos) instanceof TileFakeAir) || !((TileFakeAir) world.getTileEntity(pos)).canStay();
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
		if(shouldRemove(world, pos))
			world.setBlockState(pos, rand.nextInt(10) == 0 ? Blocks.WATER.getDefaultState() : Blocks.AIR.getDefaultState());
	}

	@Override
	public int tickRate(World world) {
		return 4;
	}

	@Override
	public boolean registerInCreative() {
		return false;
	}

	@Override
	public boolean isBlockNormalCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity e) {
		return false;
	}

	@Override
	public boolean canCollideCheck(IBlockState state, boolean hitIfLiquid) {
		return false;
	}

	@Override
	public boolean canBeReplacedByLeaves(@Nonnull IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
		return true;
	}

	@Override
	public boolean canDropFromExplosion(Explosion par1Explosion) {
		return false;
	}

	@Nonnull
	@Override
	public Item getItemDropped(IBlockState state, @Nonnull Random rand, int fortune) {
		return Items.AIR;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
		return NULL_AABB;
	}

	@Override
	public boolean isAir(IBlockState state, IBlockAccess world, BlockPos pos) {
		return true;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
		return new TileFakeAir();
	}

	@Nonnull
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing side) {
		return BlockFaceShape.UNDEFINED;
	}

}
