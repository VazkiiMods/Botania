/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 21, 2014, 7:51:36 PM (GMT)]
 */
package vazkii.botania.common.block.tile;

import java.awt.Color;
import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.IFlowerComponent;
import vazkii.botania.api.recipe.RecipePetals;
import vazkii.botania.common.Botania;
import vazkii.botania.common.lib.LibBlockNames;
import cpw.mods.fml.common.network.PacketDispatcher;

public class TileAltar extends TileSimpleInventory implements ISidedInventory {

	public static final String TAG_HAS_WATER = "hasWater";

	public boolean hasWater = false;

	public boolean collideEntityItem(EntityItem item) {
		if(!hasWater)
			return false;

		boolean didChange = false;

		ItemStack stack = item.getEntityItem();
		if(stack == null)
			return false;

		if(stack.getItem() instanceof IFlowerComponent) {
			if(getStackInSlot(getSizeInventory() - 1) != null)
				return false;

			if(!worldObj.isRemote) {
				stack.stackSize--;
				if(stack.stackSize == 0)
					item.setDead();
			}

			for(int i = 0; i < getSizeInventory(); i++)
				if(getStackInSlot(i) == null) {
					ItemStack stackToPut = stack.copy();
					stackToPut.stackSize = 1;
					setInventorySlotContents(i, stackToPut);
					didChange = true;
					break;
				}
		} else if(stack.itemID == Item.seeds.itemID) {
			for(RecipePetals recipe : BotaniaAPI.petalRecipes) {
				if(recipe.matches(this)) {
					for(int i = 0; i < getSizeInventory(); i++)
						setInventorySlotContents(i, null);

					if(!worldObj.isRemote) {
						stack.stackSize--;
						if(stack.stackSize == 0)
							item.setDead();

						ItemStack output = recipe.getOutput().copy();
						EntityItem outputItem = new EntityItem(worldObj, xCoord + 0.5, yCoord + 1.5, zCoord + 0.5, output);
						worldObj.spawnEntityInWorld(outputItem);
					}

					craftingFanciness();
					hasWater = false;
					didChange = true;
					break;
				}
			}
		}

		return didChange;
	}

	public void craftingFanciness() {
		worldObj.playSoundEffect(xCoord, yCoord, zCoord, "random.levelup", 1F, 1F);
		for(int i = 0; i < 25; i++) {
			float red = (float) Math.random();
			float green = (float) Math.random();
			float blue = (float) Math.random();
			Botania.proxy.sparkleFX(worldObj, xCoord + 0.5 + Math.random() * 0.4 - 0.2, yCoord + 1, zCoord + 0.5 + Math.random() * 0.4 - 0.2, red, green, blue, (float) Math.random(), 10);
		}
	}

	@Override
	public void updateEntity() {
		List<EntityItem> items = worldObj.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(xCoord, yCoord + 1D / 16D * 20D, zCoord, xCoord + 1, yCoord + 1D / 16D * 21D, zCoord + 1));

		boolean didChange = false;

		for(EntityItem item : items)
			didChange = collideEntityItem(item) || didChange;

		if(didChange)
			PacketDispatcher.sendPacketToAllInDimension(getDescriptionPacket(), worldObj.provider.dimensionId);

		for(int i = 0; i < getSizeInventory(); i++) {
			ItemStack stackAt = getStackInSlot(i);
			if(stackAt == null)
				break;

			if(Math.random() >= 0.97) {
				Color color = new Color(((IFlowerComponent) stackAt.getItem()).getParticleColor(stackAt));
				float red = (float) color.getRed() / 255F;
				float green = (float) color.getGreen() / 255F;
				float blue = (float) color.getBlue() / 255F;
				Botania.proxy.sparkleFX(worldObj, xCoord + 0.5 + Math.random() * 0.4 - 0.2, yCoord + 1, zCoord + 0.5 + Math.random() * 0.4 - 0.2, red, green, blue, (float) Math.random(), 10);
			}
		}
	}

	@Override
	public void writeCustomNBT(NBTTagCompound cmp) {
		super.writeCustomNBT(cmp);

		cmp.setBoolean(TAG_HAS_WATER, hasWater);
	}

	@Override
	public void readCustomNBT(NBTTagCompound cmp) {
		super.readCustomNBT(cmp);

		hasWater = cmp.getBoolean(TAG_HAS_WATER);
	}

	@Override
	public String getInvName() {
		return LibBlockNames.ALTAR;
	}

	@Override
	public int getSizeInventory() {
		return 16;
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return false;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int var1) {
		return new int[0];
	}

	@Override
	public boolean canInsertItem(int i, ItemStack itemstack, int j) {
		return false;
	}

	@Override
	public boolean canExtractItem(int i, ItemStack itemstack, int j) {
		return false;
	}

}
