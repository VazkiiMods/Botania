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
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.items.ItemHandlerHelper;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.core.helper.PlayerHelper;
import vazkii.botania.common.lib.LibMisc;

public class TileTinyPotato extends TileSimpleInventory implements ITickable {

	private static final String TAG_NAME = "name";

	public int jumpTicks = 0;
	public String name = "";
	public int nextDoIt = 0;

	public void interact(EntityPlayer player, EnumHand hand, ItemStack stack, EnumFacing side) {
		int index = side.getIndex();
		if(index >= 0) {
			ItemStack stackAt = getItemHandler().getStackInSlot(index);
			if(!stackAt.isEmpty() && stack.isEmpty()) {
				player.setHeldItem(hand, stackAt);
				getItemHandler().setStackInSlot(index, ItemStack.EMPTY);
			} else if(!stack.isEmpty()) {
				ItemStack copy = stack.splitStack(1);

				if(stack.isEmpty())
					player.setHeldItem(hand, stackAt);
				else if(!stackAt.isEmpty()) {
					ItemHandlerHelper.giveItemToPlayer(player, stackAt);
				}

				getItemHandler().setStackInSlot(index, copy);
			}
		}

		jump();

		if(!world.isRemote) {
			if(name.toLowerCase().trim().endsWith("shia labeouf")  && nextDoIt == 0) {
				nextDoIt = 40;
				world.playSound(null, pos, ModSounds.doit, SoundCategory.BLOCKS, 1F, 1F);
			}

			for(int i = 0; i < getSizeInventory(); i++) {
				ItemStack stackAt = getItemHandler().getStackInSlot(i);
				if(!stackAt.isEmpty() && stackAt.getItem() == Item.getItemFromBlock(ModBlocks.tinyPotato)) {
					player.sendMessage(new TextComponentString("Don't talk to me or my son ever again."));
					return;
				}
			}

			PlayerHelper.grantCriterion((EntityPlayerMP) player, new ResourceLocation(LibMisc.MOD_ID, "main/tiny_potato_pet"), "code_triggered");
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
	public void markDirty() {
		super.markDirty();
		VanillaPacketDispatcher.dispatchTEToNearbyPlayers(world, pos);
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
