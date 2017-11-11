/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Aug 23, 2015, 7:25:38 PM (GMT)]
 */
package vazkii.botania.common.block.tile;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import vazkii.botania.common.item.ItemCacophonium;

public class TileCacophonium extends TileMod {

	private static final String TAG_STACK = "stack";

	public ItemStack stack = ItemStack.EMPTY;

	public void annoyDirewolf() {
		ItemCacophonium.playSound(world, stack, pos.getX(), pos.getY(), pos.getZ(), SoundCategory.BLOCKS, 1F);
	}

	@Override
	public void writePacketNBT(NBTTagCompound cmp) {
		super.writePacketNBT(cmp);

		NBTTagCompound cmp1 = new NBTTagCompound();
		if(!stack.isEmpty())
			cmp1 = stack.writeToNBT(cmp1);
		cmp.setTag(TAG_STACK, cmp1);
	}

	@Override
	public void readPacketNBT(NBTTagCompound cmp) {
		super.readPacketNBT(cmp);

		NBTTagCompound cmp1 = cmp.getCompoundTag(TAG_STACK);
		stack = new ItemStack(cmp1);
	}

}
