/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [May 4, 2014, 12:51:05 PM (GMT)]
 */
package vazkii.botania.common.block.tile;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ITickable;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibObfuscation;

public class TileOpenCrate extends TileSimpleInventory implements ITickable {

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return true;
	}

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public String getName() {
		return LibBlockNames.OPEN_CRATE;
	}

	@Override
	public void update() {
		boolean redstone = false;
		for(EnumFacing dir : EnumFacing.VALUES) {
			int redstoneSide = worldObj.getRedstonePower(pos.offset(dir), dir);
			if(redstoneSide > 0) {
				redstone = true;
				break;
			}
		}

		if(canEject()) {
			ItemStack stack = getStackInSlot(0);
			if(stack != null)
				eject(stack, redstone);
		}
	}

	public boolean canEject() {
		Block blockBelow = worldObj.getBlockState(pos.down()).getBlock();
		return blockBelow.isAir(worldObj, pos.down()) || blockBelow.getCollisionBoundingBox(worldObj, pos.down(), worldObj.getBlockState(pos.down())) == null;
	}

	public void eject(ItemStack stack, boolean redstone) {
		EntityItem item = new EntityItem(worldObj, pos.getX() + 0.5, pos.getY() - 0.5, pos.getZ() + 0.5, stack);
		item.motionX = 0;
		item.motionY = 0;
		item.motionZ = 0;

		if(redstone)
			ObfuscationReflectionHelper.setPrivateValue(EntityItem.class, item, -200, LibObfuscation.AGE);

		setInventorySlotContents(0, null);
		if(!worldObj.isRemote)
			worldObj.spawnEntityInWorld(item);
	}

	public boolean onWanded(EntityPlayer player, ItemStack stack) {
		return false;
	}

	public int getSignal() {
		return 0;
	}
}
