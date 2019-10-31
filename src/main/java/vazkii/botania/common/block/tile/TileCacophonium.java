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
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.botania.common.item.ItemCacophonium;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

public class TileCacophonium extends TileMod {

	@ObjectHolder(LibMisc.MOD_ID + ":" + LibBlockNames.CACOPHONIUM)
	public static TileEntityType<TileCacophonium> TYPE;
	private static final String TAG_STACK = "stack";

	public ItemStack stack = ItemStack.EMPTY;

	public TileCacophonium() {
		super(TYPE);
	}

	public void annoyDirewolf() {
		ItemCacophonium.playSound(world, stack, pos.getX(), pos.getY(), pos.getZ(), SoundCategory.BLOCKS, 1F);
	}

	@Override
	public void writePacketNBT(CompoundNBT cmp) {
		super.writePacketNBT(cmp);

		CompoundNBT cmp1 = new CompoundNBT();
		if(!stack.isEmpty())
			cmp1 = stack.write(cmp1);
		cmp.put(TAG_STACK, cmp1);
	}

	@Override
	public void readPacketNBT(CompoundNBT cmp) {
		super.readPacketNBT(cmp);

		CompoundNBT cmp1 = cmp.getCompound(TAG_STACK);
		stack = ItemStack.read(cmp1);
	}

}
