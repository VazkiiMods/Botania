/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [May 15, 2015, 4:09:07 PM (GMT)]
 */
package vazkii.botania.common.block.tile;

import java.awt.Color;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.brew.Brew;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.common.Botania;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.brew.ItemIncenseStick;
import vazkii.botania.common.lib.LibBlockNames;

public class TileIncensePlate extends TileSimpleInventory implements ISidedInventory {

	private static final String TAG_TIME_LEFT = "timeLeft";
	private static final String TAG_BURNING = "burning";
	private static final int RANGE = 32;

	public int timeLeft = 0;
	public boolean burning = false;

	public int comparatorOutput = 0;

	@Override
	public void updateEntity() {
		ItemStack stack = getStackInSlot(0);
		if(stack != null && burning) {
			Brew brew = ((ItemIncenseStick) ModItems.incenseStick).getBrew(stack);
			PotionEffect effect = brew.getPotionEffects(stack).get(0);
			if(timeLeft > 0) {
				timeLeft--;
				if(!worldObj.isRemote) {
					List<EntityPlayer> players = worldObj.getEntitiesWithinAABB(EntityPlayer.class, AxisAlignedBB.getBoundingBox(xCoord + 0.5 - RANGE, yCoord + 0.5 - RANGE, zCoord + 0.5 - RANGE, xCoord + 0.5 + RANGE, yCoord + 0.5 + RANGE, zCoord + 0.5 + RANGE));
					for(EntityPlayer player : players) {
						PotionEffect currentEffect = player.getActivePotionEffect(Potion.potionTypes[effect.getPotionID()]);
						boolean nightVision = effect.getPotionID() == Potion.nightVision.id;
						if(currentEffect == null || currentEffect.getDuration() < (nightVision ? 205 : 3)) {
							PotionEffect applyEffect = new PotionEffect(effect.getPotionID(), nightVision ? 285 : 80, effect.getAmplifier(), true);
							player.addPotionEffect(applyEffect);
						}
					}

					if(worldObj.rand.nextInt(20) == 0)
						worldObj.playSoundEffect(xCoord + 0.5, yCoord + 0.1, zCoord + 0.5, "fire.fire", 0.1F, 1F);
				} else {
					double x = xCoord + 0.5;
					double y = yCoord + 0.5;
					double z = zCoord + 0.5;
					Color color = new Color(brew.getColor(stack));
					float r = color.getRed() / 255F;
					float g = color.getGreen() / 255F;
					float b = color.getBlue() / 255F;
					Botania.proxy.wispFX(worldObj, x - (Math.random() - 0.5) * 0.2, y - (Math.random() - 0.5) * 0.2, z - (Math.random() - 0.5) * 0.2, r, g, b, 0.05F + (float) Math.random() * 0.02F, 0.005F - (float) Math.random() * 0.01F, 0.01F + (float) Math.random() * 0.005F, 0.005F - (float) Math.random() * 0.01F);
					Botania.proxy.wispFX(worldObj, x - (Math.random() - 0.5) * 0.2, y - (Math.random() - 0.5) * 0.2, z - (Math.random() - 0.5) * 0.2, 0.2F, 0.2F, 0.2F, 0.05F + (float) Math.random() * 0.02F, 0.005F - (float) Math.random() * 0.01F, 0.01F + (float) Math.random() * 0.001F, 0.005F - (float) Math.random() * 0.01F);
				}
			} else {
				setInventorySlotContents(0, null);
				burning = false;
				VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
			}
		} else timeLeft = 0;

		int newComparator = 0;
		if(stack != null)
			newComparator = 1;
		if(burning)
			newComparator = 2;
		if(comparatorOutput != newComparator) {
			comparatorOutput = newComparator;
			worldObj.func_147453_f(xCoord, yCoord, zCoord, worldObj.getBlock(xCoord, yCoord, zCoord));
		}
	}

	public void ignite() {
		ItemStack stack = getStackInSlot(0);
		if(stack == null || burning)
			return;

		burning = true;
		Brew brew = ((ItemIncenseStick) ModItems.incenseStick).getBrew(stack);
		timeLeft = brew.getPotionEffects(stack).get(0).getDuration() * ItemIncenseStick.TIME_MULTIPLIER;
	}

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public String getInventoryName() {
		return LibBlockNames.INCENSE_PLATE;
	}

	@Override
	public void writeCustomNBT(NBTTagCompound par1nbtTagCompound) {
		super.writeCustomNBT(par1nbtTagCompound);
		par1nbtTagCompound.setInteger(TAG_TIME_LEFT, timeLeft);
		par1nbtTagCompound.setBoolean(TAG_BURNING, burning);
	}

	@Override
	public void readCustomNBT(NBTTagCompound par1nbtTagCompound) {
		super.readCustomNBT(par1nbtTagCompound);
		timeLeft = par1nbtTagCompound.getInteger(TAG_TIME_LEFT);
		burning = par1nbtTagCompound.getBoolean(TAG_BURNING);
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return itemstack != null && itemstack.getItem() == ModItems.incenseStick && ((ItemIncenseStick) ModItems.incenseStick).getBrew(itemstack) != BotaniaAPI.fallbackBrew;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int side) {
		return new int[] { 0 };
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, int side) {
		return isItemValidForSlot(slot, stack);
	}

	@Override
	public boolean canExtractItem(int p_102008_1_, ItemStack p_102008_2_, int p_102008_3_) {
		return false;
	}

	@Override
	public void markDirty() {
		super.markDirty();
		if(!worldObj.isRemote)
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
	}

}
