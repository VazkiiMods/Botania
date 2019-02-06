/**
 * This class was created by <SoundLogic>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Aug 31, 2015, 11:40:59 PM (GMT)]
 */
package vazkii.botania.client.core.handler;

import net.minecraft.block.state.IBlockState;
import net.minecraft.fluid.IFluidState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Fluids;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import vazkii.botania.api.lexicon.multiblock.Multiblock;
import vazkii.botania.api.lexicon.multiblock.component.MultiblockComponent;

import javax.annotation.Nonnull;

/**
 * This class acts as a wrapper around a block access to
 * replace blocks with the blocks involved in the multiblock specified
 */
public class MultiblockBlockAccess implements IBlockReader {

	private IBlockReader compose;
	private BlockPos anchorPos;
	protected Multiblock multiblock;

	@Nonnull
	@Override
	public IBlockState getBlockState(@Nonnull BlockPos pos) {
		MultiblockComponent comp=getComponent(pos);
		if(comp != null)
			return comp.getBlockState();
		if(compose != null)
			return compose.getBlockState(pos);
		return Blocks.AIR.getDefaultState();
	}

	@Nonnull
	@Override
	public IFluidState getFluidState(BlockPos pos) {
		return compose != null ? compose.getFluidState(pos) : Fluids.EMPTY.getDefaultState();
	}

	@Override
	public TileEntity getTileEntity(@Nonnull BlockPos pos) {
		MultiblockComponent comp=getComponent(pos);
		if(comp != null)
			return comp.getTileEntity();
		if(compose != null)
			return compose.getTileEntity(pos);
		return null;
	}

	/**
	 * Updates the block access to the new parameters
	 */
	public void update(IBlockReader access, Multiblock mb, BlockPos anchorPos) {
		compose = access;
		multiblock = mb;
		this.anchorPos = anchorPos;
	}

	/**
	 * Returns the multiblock component for the coordinates, adjusted based on the anchor
	 */
	private MultiblockComponent getComponent(BlockPos pos) {
		return multiblock.getComponentForLocation(pos.add(new BlockPos(-anchorPos.getX(), -anchorPos.getY(), -anchorPos.getZ())));
	}
}
