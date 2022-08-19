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

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.util.ForgeDirection;
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
	protected int anchorX, anchorY, anchorZ;

	@Override
	public Block getBlock(int x, int y, int z) {
		MultiblockComponent comp=getComponent(x, y, z);
		if(comp != null)
			return comp.getBlock();
		if(hasBlockAccess)
			return originalBlockAccess.getBlock(x, y, z);
		return Blocks.air;
	}

	@Override
	public TileEntity getTileEntity(int x, int y, int z) {
		MultiblockComponent comp=getComponent(x, y, z);
		if(comp != null)
			return comp.getTileEntity();
		if(hasBlockAccess)
			return originalBlockAccess.getTileEntity(x, y, z);
		return null;
	}

	@Override
	public int getLightBrightnessForSkyBlocks(int x, int y, int z, int p_72802_4_) {
		if(hasBlockAccess)
			return originalBlockAccess.getLightBrightnessForSkyBlocks(x,y,z,p_72802_4_);
		return 15728640;
	}

	@Override
	public int getBlockMetadata(int x, int y, int z) {
		MultiblockComponent comp=getComponent(x, y, z);
		if(comp != null)
			return comp.getMeta();
		if(hasBlockAccess)
			return originalBlockAccess.getBlockMetadata(x, y, z);
		return 0;
	}

	@Override
	public int isBlockProvidingPowerTo(int x, int y, int z, int direction) {
		return 0;
	}

	@Override
	public boolean isAirBlock(int x, int y, int z) {
		MultiblockComponent comp=getComponent(x, y, z);
		if(comp != null)
			return false;
		if(hasBlockAccess)
			return originalBlockAccess.isAirBlock(x, y, z);
		return true;
	}

	@Override
	public BiomeGenBase getBiomeGenForCoords(int x, int z) {
		if(hasBlockAccess)
			return originalBlockAccess.getBiomeGenForCoords(x, z);
		return null;
	}

	@Override
	public int getHeight() {
		if(hasBlockAccess)
			return originalBlockAccess.getHeight();
		return 256;
	}

	@Override
	public boolean extendedLevelsInChunkCache() {
		if(hasBlockAccess)
			return originalBlockAccess.extendedLevelsInChunkCache();
		return false;
	}

	@Override
	public boolean isSideSolid(int x, int y, int z, ForgeDirection side, boolean _default) {
		if(hasBlockAccess)
			return originalBlockAccess.isSideSolid(x, y, z, side, _default);
		return _default;
	}

	/**
	 * Updates the block access to the new parameters
	 */
	public void update(IBlockAccess access, Multiblock mb, int anchorX, int anchorY, int anchorZ) {
		originalBlockAccess = access;
		multiblock = mb;
		this.anchorX = anchorX;
		this.anchorY = anchorY;
		this.anchorZ = anchorZ;
		hasBlockAccess = access != null;
	}

	/**
	 * Returns the multiblock component for the coordinates, adjusted based on the anchor
	 */
	protected MultiblockComponent getComponent(int x, int y, int z) {
		MultiblockComponent comp = multiblock.getComponentForLocation(x - anchorX, y - anchorY, z - anchorZ);
		return comp;
	}
}
