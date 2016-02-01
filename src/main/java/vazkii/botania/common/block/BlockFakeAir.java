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

import java.util.List;
import java.util.Random;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import vazkii.botania.common.block.tile.TileFakeAir;
import vazkii.botania.common.lib.LibBlockNames;

public class BlockFakeAir extends BlockModContainer {

	public BlockFakeAir() {
		super(Material.air);
		setUnlocalizedName(LibBlockNames.FAKE_AIR);
		setBlockBounds(0, 0, 0, 0, 0, 0);
		setTickRandomly(true);
	}

	@Override
	public int getRenderType() {
		return -1;
	}

	@Override
	protected boolean shouldRegisterInNameSet() {
		return false;
	}

	@Override
	public Block setUnlocalizedName(String par1Str) {
		GameRegistry.registerBlock(this, null, par1Str);
		return super.setUnlocalizedName(par1Str);
	}

	@Override
	public void onNeighborBlockChange(World world, BlockPos pos, IBlockState state, Block block) {
		if(shouldRemove(world, pos))
			world.scheduleUpdate(pos, this, tickRate(world));
	}

	private boolean shouldRemove(World world, BlockPos pos) {
		return !world.isRemote && world.getTileEntity(pos) == null || !(world.getTileEntity(pos) instanceof TileFakeAir) || !((TileFakeAir) world.getTileEntity(pos)).canStay();
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
		if(shouldRemove(world, pos))
			world.setBlockState(pos, Blocks.water.getDefaultState());
	}

	@Override
	public int tickRate(World p_149738_1_) {
		return 4;
	}

	@Override
	public boolean registerInCreative() {
		return false;
	}

	@Override
	public boolean isBlockNormalCube() {
		return false;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean canEntityDestroy(IBlockAccess world, BlockPos pos, Entity e) {
		return false;
	}

	@Override
	public boolean canCollideCheck(IBlockState state, boolean hitIfLiquid) {
		return false;
	}

	@Override
	public boolean canBeReplacedByLeaves(IBlockAccess world, BlockPos pos) {
		return true;
	}

	@Override
	public boolean canDropFromExplosion(Explosion par1Explosion) {
		return false;
	}

	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		return ImmutableList.of();
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(World par1World, BlockPos pos, IBlockState state) {
		return null;
	}

	@Override
	public boolean isAir(IBlockAccess world, BlockPos pos) {
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileFakeAir();
	}

}
