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
import net.minecraft.block.BlockAir;
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
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReaderBase;
import net.minecraft.world.World;
import vazkii.botania.common.block.tile.TileFakeAir;
import vazkii.botania.common.lib.LibBlockNames;

import javax.annotation.Nonnull;
import java.util.Random;

public class BlockFakeAir extends BlockAir {

	private static final AxisAlignedBB AABB = new AxisAlignedBB(0, 0, 0, 0, 0, 0);

	public BlockFakeAir(Properties builder) {
		super(builder);
	}

	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
		if(shouldRemove(world, pos))
			world.getPendingBlockTicks().scheduleTick(pos, this, tickRate(world));
	}

	private boolean shouldRemove(World world, BlockPos pos) {
		return !world.isRemote && world.getTileEntity(pos) == null || !(world.getTileEntity(pos) instanceof TileFakeAir) || !((TileFakeAir) world.getTileEntity(pos)).canStay();
	}

	@Override
	public void tick(IBlockState state, World world, BlockPos pos, Random rand) {
		if(shouldRemove(world, pos))
			world.setBlockState(pos, rand.nextInt(10) == 0 ? Blocks.WATER.getDefaultState() : Blocks.AIR.getDefaultState());
	}

	@Override
	public int tickRate(IWorldReaderBase world) {
		return 4;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull IBlockState state, @Nonnull IBlockReader world) {
		return new TileFakeAir();
	}
}
