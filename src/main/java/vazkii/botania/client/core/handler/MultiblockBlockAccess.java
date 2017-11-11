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
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import vazkii.botania.api.lexicon.multiblock.Multiblock;
import vazkii.botania.api.lexicon.multiblock.component.MultiblockComponent;

import javax.annotation.Nonnull;

/**
 * This class acts as a wrapper around a block access to
 * replace blocks with the blocks involved in the multiblock specified
 */
public class MultiblockBlockAccess implements IBlockAccess {

	private IBlockAccess originalBlockAccess;
	private boolean hasBlockAccess = false;
	private BlockPos anchorPos;
	protected Multiblock multiblock;

	@Nonnull
	@Override
	public IBlockState getBlockState(@Nonnull BlockPos pos) {
		MultiblockComponent comp=getComponent(pos);
		if(comp != null)
			return comp.getBlockState();
		if(hasBlockAccess)
			return originalBlockAccess.getBlockState(pos);
		return Blocks.AIR.getDefaultState();
	}

	@Override
	public TileEntity getTileEntity(@Nonnull BlockPos pos) {
		MultiblockComponent comp=getComponent(pos);
		if(comp != null)
			return comp.getTileEntity();
		if(hasBlockAccess)
			return originalBlockAccess.getTileEntity(pos);
		return null;
	}

	@Override
	public int getCombinedLight(@Nonnull BlockPos pos, int lightValue) {
		if(hasBlockAccess)
			return originalBlockAccess.getCombinedLight(pos, lightValue);
		return 15728640;
	}

	@Override
	public int getStrongPower(@Nonnull BlockPos pos, @Nonnull EnumFacing direction) {
		return 0;
	}

	@Nonnull
	@Override
	public WorldType getWorldType() {
		return WorldType.DEFAULT;
	}

	@Override
	public boolean isAirBlock(@Nonnull BlockPos pos) {
		MultiblockComponent comp=getComponent(pos);
		if(comp != null)
			return false;
		if(hasBlockAccess)
			return originalBlockAccess.isAirBlock(pos);
		return true;
	}

	@Nonnull
	@Override
	public Biome getBiome(@Nonnull BlockPos pos) {
		if(hasBlockAccess)
			return originalBlockAccess.getBiome(pos);
		return Biomes.DEFAULT;
	}

	@Override
	public boolean isSideSolid(@Nonnull BlockPos pos, @Nonnull EnumFacing side, boolean _default) {
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
	private MultiblockComponent getComponent(BlockPos pos) {
		return multiblock.getComponentForLocation(pos.add(new BlockPos(-anchorPos.getX(), -anchorPos.getY(), -anchorPos.getZ())));
	}
}
