/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Nov 14, 2014, 5:04:22 PM (GMT)]
 */
package vazkii.botania.common.block.tile.string;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.common.util.ForgeDirection;
import vazkii.botania.api.wand.ITileBound;
import vazkii.botania.common.block.tile.TileMod;

public abstract class TileRedString extends TileMod implements ITileBound {

	private ChunkCoordinates binding;

	@Override
	public void updateEntity() {
		ForgeDirection dir = getOrientation();
		int x = xCoord;
		int y = yCoord;
		int z = zCoord;
		int range = getRange();
		ChunkCoordinates currBinding = getBinding();
		setBinding(null);

		for(int i = 0; i < range; i++) {
			x += dir.offsetX;
			y += dir.offsetY;
			z += dir.offsetZ;
			if(worldObj.isAirBlock(x, y, z))
				continue;

			TileEntity tile = worldObj.getTileEntity(x, y, z);
			if(tile instanceof TileRedString)
				continue;

			if(acceptBlock(x, y, z)) {
				setBinding(new ChunkCoordinates(x, y, z));
				if(currBinding == null || currBinding.posX != x || currBinding.posY != y || currBinding.posZ != z)
					onBound(x, y, z);
				break;
			}
		}
	}

	public int getRange() {
		return 8;
	}

	public abstract boolean acceptBlock(int x, int y, int z);

	public void onBound(int x, int y, int z) {
		// NO-OP
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return INFINITE_EXTENT_AABB;
	}

	@Override
	public ChunkCoordinates getBinding() {
		return binding;
	}

	public void setBinding(ChunkCoordinates binding) {
		this.binding = binding;
	}

	public ForgeDirection getOrientation() {
		return ForgeDirection.getOrientation(getBlockMetadata());
	}

	public TileEntity getTileAtBinding() {
		ChunkCoordinates binding = getBinding();
		return binding == null ? null : worldObj.getTileEntity(binding.posX, binding.posY, binding.posZ);
	}

	public Block getBlockAtBinding() {
		ChunkCoordinates binding = getBinding();
		return binding == null ? Blocks.air : worldObj.getBlock(binding.posX, binding.posY, binding.posZ);
	}

}
