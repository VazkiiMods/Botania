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
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import vazkii.botania.api.lexicon.multiblock.Multiblock;
import vazkii.botania.api.lexicon.multiblock.component.MultiblockComponent;

/**
 * This class acts as a wrapper around a block access to
 * replace blocks with the blocks involved in the multiblock specified
 */
public class MultiblockBlockAccess implements IBlockAccess {

	protected IBlockAccess originalBlockAccess;
	protected boolean hasBlockAccess = false;
	protected Multiblock multiblock;
	protected BlockPos anchorPos;

	@Override
	public IBlockState getBlockState(BlockPos pos) {
		MultiblockComponent comp=getComponent(pos);
		if(comp != null)
			return comp.getBlockState();
		if(hasBlockAccess)
			return originalBlockAccess.getBlockState(pos);
		return Blocks.air.getDefaultState();
	}

	@Override
	public TileEntity getTileEntity(BlockPos pos) {
		MultiblockComponent comp=getComponent(pos);
		if(comp != null)
			return comp.getTileEntity();
		if(hasBlockAccess)
			return originalBlockAccess.getTileEntity(pos);
		return null;
	}

	@Override
	public int getCombinedLight(BlockPos pos, int p_72802_4_) {
		if(hasBlockAccess)
			return originalBlockAccess.getCombinedLight(pos, p_72802_4_);
		return 15728640;
	}

	@Override
	public int getStrongPower(BlockPos pos, EnumFacing direction) {
		return 0;
	}

	@Override
	public WorldType getWorldType() {
		return WorldType.DEFAULT;
	}

	@Override
	public boolean isAirBlock(BlockPos pos) {
		MultiblockComponent comp=getComponent(pos);
		if(comp != null)
			return false;
		if(hasBlockAccess)
			return originalBlockAccess.isAirBlock(pos);
		return true;
	}

	@Override
	public BiomeGenBase getBiomeGenForCoords(BlockPos pos) {
		if(hasBlockAccess)
			return originalBlockAccess.getBiomeGenForCoords(pos);
		return null;
	}

	@Override
	public boolean extendedLevelsInChunkCache() {
		if(hasBlockAccess)
			return originalBlockAccess.extendedLevelsInChunkCache();
		return false;
	}

	@Override
	public boolean isSideSolid(BlockPos pos, EnumFacing side, boolean _default) {
		if(hasBlockAccess)
			return originalBlockAccess.isSideSolid(pos, side, _default);
		return _default;
	}

	/**
	 * Updates the block access to the new parameters
	 */
	public void update(IBlockAccess access, Multiblock mb, BlockPos anchorPos) {
		originalBlockAccess = access;
		multiblock = mb;
		this.anchorPos = anchorPos;
		hasBlockAccess = access != null;
	}

	/**
	 * Returns the multiblock component for the coordinates, adjusted based on the anchor
	 */
	protected MultiblockComponent getComponent(BlockPos pos) {
		MultiblockComponent comp = multiblock.getComponentForLocation(pos.add(new BlockPos(-anchorPos.getX(), -anchorPos.getY(), -anchorPos.getZ())));
		return comp;
	}
}
