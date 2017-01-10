/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jul 18, 2014, 8:05:08 PM (GMT)]
 */
package vazkii.botania.common.block.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentString;
import vazkii.botania.api.sound.BotaniaSoundEvents;
import vazkii.botania.common.block.ModBlocks;

public class TileTinyPotato extends TileSimpleInventory {

	private static final String TAG_NAME = "name";

	public int jumpTicks = 0;
	public String name = "";
	public int nextDoIt = 0;

	public void interact(EntityPlayer player, EnumHand hand, ItemStack stack, EnumFacing side) {
		int index = side.getIndex();
		if(index >= 0) {
			ItemStack stackAt = getItemHandler().getStackInSlot(index);
			if(stackAt != null && stack == null) {
				player.setHeldItem(hand, stackAt);
				getItemHandler().setStackInSlot(index, null);
			} else if(stack != null) {
				ItemStack copy = stack.copy();
				copy.stackSize = 1;
				stack.stackSize--;

				if(stack.stackSize == 0)
					player.setHeldItem(hand, stackAt);
				else if(stackAt != null) {
					if(!player.inventory.addItemStackToInventory(stackAt))
						player.dropItem(stackAt, false);
				}

				getItemHandler().setStackInSlot(index, copy);
			}
		}

		jump();

		if(!world.isRemote) {
			if(name.toLowerCase().trim().endsWith("shia labeouf")  && nextDoIt == 0) {
				nextDoIt = 40;
				world.playSound(null, pos, BotaniaSoundEvents.doit, SoundCategory.BLOCKS, 1F, 1F);
			}

			for(int i = 0; i < getSizeInventory(); i++) {
				ItemStack stackAt = getItemHandler().getStackInSlot(i);
				if(stackAt != null && stackAt.getItem() == Item.getItemFromBlock(ModBlocks.tinyPotato)) {
					player.sendMessage(new TextComponentString("Don't talk to me or my son ever again."));
					return;
				}
			}
		}
	}

	public void jump() {
		if(jumpTicks == 0)
			jumpTicks = 20;
	}

	@Override
	public void update() {
		if(world.rand.nextInt(100) == 0)
			jump();

		if(jumpTicks > 0)
			jumpTicks--;
		if(nextDoIt > 0)
			nextDoIt--;
	}

	@Override
	public void writePacketNBT(NBTTagCompound cmp) {
		super.writePacketNBT(cmp);
		cmp.setString(TAG_NAME, name);
	}

	@Override
	public void readPacketNBT(NBTTagCompound cmp) {
		super.readPacketNBT(cmp);
		name = cmp.getString(TAG_NAME);
	}

	@Override
	public int getSizeInventory() {
		return 6;
	}

}
