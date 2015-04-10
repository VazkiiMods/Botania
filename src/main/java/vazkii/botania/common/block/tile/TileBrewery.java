/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Oct 31, 2014, 4:42:36 PM (GMT)]
 */
package vazkii.botania.common.block.tile;

import java.awt.Color;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.brew.IBrewContainer;
import vazkii.botania.api.brew.IBrewItem;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.mana.IManaReceiver;
import vazkii.botania.api.recipe.RecipeBrew;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.common.Botania;
import vazkii.botania.common.lib.LibBlockNames;

// This is mostly copypasta from TileRuneAltar
public class TileBrewery extends TileSimpleInventory implements ISidedInventory, IManaReceiver {

	private static final String TAG_MANA = "mana";

	public RecipeBrew recipe;
	int mana = 0;
	int manaLastTick = 0;
	public int signal = 0;

	public boolean addItem(EntityPlayer player, ItemStack stack) {
		if(recipe != null || stack == null || stack.getItem() instanceof IBrewItem && ((IBrewItem) stack.getItem()).getBrew(stack) != null && ((IBrewItem) stack.getItem()).getBrew(stack) != BotaniaAPI.fallbackBrew || getStackInSlot(0) == null != stack.getItem() instanceof IBrewContainer)
			return false;

		boolean did = false;

		for(int i = 0; i < getSizeInventory(); i++)
			if(getStackInSlot(i) == null) {
				did = true;
				ItemStack stackToAdd = stack.copy();
				stackToAdd.stackSize = 1;
				setInventorySlotContents(i, stackToAdd);

				if(player == null || !player.capabilities.isCreativeMode) {
					stack.stackSize--;
					if(stack.stackSize == 0 && player != null)
						player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
				}

				break;
			}

		if(did) {
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(worldObj, xCoord, yCoord, zCoord);
			for(RecipeBrew recipe : BotaniaAPI.brewRecipes)
				if(recipe.matches(this) && recipe.getOutput(getStackInSlot(0)) != null) {
					this.recipe = recipe;
					worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 1, 1 | 2);
				}
		}

		return true;
	}

	@Override
	public void updateEntity() {
		super.updateEntity();

		if(mana > 0 && recipe == null) {
			for(RecipeBrew recipe : BotaniaAPI.brewRecipes)
				if(recipe.matches(this)) {
					this.recipe = recipe;
					worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 1, 1 | 2);
				}

			if(recipe == null)
				mana = 0;
		}

		// Update every tick.
		recieveMana(0);

		if(!worldObj.isRemote && recipe == null) {
			List<EntityItem> items = worldObj.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1));
			for(EntityItem item : items)
				if(!item.isDead && item.getEntityItem() != null) {
					ItemStack stack = item.getEntityItem();
					if(addItem(null, stack) && stack.stackSize == 0)
						item.setDead();
				}
		}

		if(recipe != null) {
			if(!recipe.matches(this)) {
				recipe = null;
				worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 0, 1 | 2);
			}

			if(recipe != null) {
				if(mana != manaLastTick) {
					Color color = new Color(recipe.getBrew().getColor(getStackInSlot(0)));
					float r = color.getRed() / 255F;
					float g = color.getGreen() / 255F;
					float b = color.getBlue() / 255F;
					for(int i = 0; i < 5; i++) {
						Botania.proxy.wispFX(worldObj, xCoord + 0.7 - Math.random() * 0.4, yCoord + 0.9 - Math.random() * 0.2, zCoord + 0.7 - Math.random() * 0.4, r, g, b, 0.1F + (float) Math.random() * 0.05F, 0.03F - (float) Math.random() * 0.06F, 0.03F + (float) Math.random() * 0.015F, 0.03F - (float) Math.random() * 0.06F);
						for(int j = 0; j < 2; j++)
							Botania.proxy.wispFX(worldObj, xCoord + 0.7 - Math.random() * 0.4, yCoord + 0.9 - Math.random() * 0.2, zCoord + 0.7 - Math.random() * 0.4, 0.2F, 0.2F, 0.2F, 0.1F + (float) Math.random() * 0.2F, 0.03F - (float) Math.random() * 0.06F, 0.03F + (float) Math.random() * 0.015F, 0.03F - (float) Math.random() * 0.06F);
					}
				}

				if(mana >= getManaCost() && !worldObj.isRemote) {
					int mana = getManaCost();
					recieveMana(-mana);
					if(!worldObj.isRemote) {
						ItemStack output = recipe.getOutput(getStackInSlot(0));
						EntityItem outputItem = new EntityItem(worldObj, xCoord + 0.5, yCoord + 1.5, zCoord + 0.5, output);
						worldObj.spawnEntityInWorld(outputItem);
					}

					for(int i = 0; i < getSizeInventory(); i++)
						setInventorySlotContents(i, null);

					craftingFanciness();
				}
			}
		}

		int newSignal = 0;
		if(recipe != null)
			newSignal++;

		if(newSignal != signal) {
			signal = newSignal;
			worldObj.func_147453_f(xCoord, yCoord, zCoord, worldObj.getBlock(xCoord, yCoord, zCoord));
		}

		manaLastTick = mana;
	}

	public int getManaCost() {
		ItemStack stack = getStackInSlot(0);
		if(recipe == null || stack == null || !(stack.getItem() instanceof IBrewContainer))
			return 0;
		IBrewContainer container = (IBrewContainer) stack.getItem();
		return container.getManaCost(recipe.getBrew(), stack);
	}

	public void craftingFanciness() {
		worldObj.playSoundEffect(xCoord, yCoord, zCoord, "botania:potionCreate", 1F, 1.5F + (float) Math.random() * 0.25F);
		for(int i = 0; i < 25; i++) {
			Color color = new Color(recipe.getBrew().getColor(getStackInSlot(0)));
			float r = color.getRed() / 255F;
			float g = color.getGreen() / 255F;
			float b = color.getBlue() / 255F;
			Botania.proxy.sparkleFX(worldObj, xCoord + 0.5 + Math.random() * 0.4 - 0.2, yCoord + 1, zCoord + 0.5 + Math.random() * 0.4 - 0.2, r, g, b, (float) Math.random() * 2F + 0.5F, 10);
			for(int j = 0; j < 2; j++)
				Botania.proxy.wispFX(worldObj, xCoord + 0.7 - Math.random() * 0.4, yCoord + 0.9 - Math.random() * 0.2, zCoord + 0.7 - Math.random() * 0.4, 0.2F, 0.2F, 0.2F, 0.1F + (float) Math.random() * 0.2F, 0.05F - (float) Math.random() * 0.1F, 0.05F + (float) Math.random() * 0.03F, 0.05F - (float) Math.random() * 0.1F);
		}
	}

	@Override
	public void writeCustomNBT(NBTTagCompound par1nbtTagCompound) {
		super.writeCustomNBT(par1nbtTagCompound);

		par1nbtTagCompound.setInteger(TAG_MANA, mana);
	}

	@Override
	public void readCustomNBT(NBTTagCompound par1nbtTagCompound) {
		super.readCustomNBT(par1nbtTagCompound);

		mana = par1nbtTagCompound.getInteger(TAG_MANA);
	}

	@Override
	public int getSizeInventory() {
		return 7;
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return INFINITE_EXTENT_AABB;
	}

	@Override
	public String getInventoryName() {
		return LibBlockNames.RUNE_ALTAR;
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int var1) {
		int accessibleSlot = -1;
		for(int i = 0; i < getSizeInventory(); i++)
			if(getStackInSlot(i) != null)
				accessibleSlot = i;

		return accessibleSlot == -1 ? new int[0] : new int[] { accessibleSlot };
	}

	@Override
	public boolean canInsertItem(int i, ItemStack itemstack, int j) {
		return true;
	}

	@Override
	public boolean canExtractItem(int i, ItemStack itemstack, int j) {
		return mana == 0;
	}

	@Override
	public int getCurrentMana() {
		return mana;
	}

	@Override
	public boolean isFull() {
		return mana >= getManaCost();
	}

	@Override
	public void recieveMana(int mana) {
		this.mana = Math.min(this.mana + mana, getManaCost());
	}

	@Override
	public boolean canRecieveManaFromBursts() {
		return !isFull();
	}

	public void renderHUD(Minecraft mc, ScaledResolution res) {
		int manaToGet = getManaCost();
		if(manaToGet > 0) {
			int x = res.getScaledWidth() / 2 + 20;
			int y = res.getScaledHeight() / 2 - 8;

			if(recipe == null)
				return;

			RenderHelper.renderProgressPie(x, y, (float) mana / (float) manaToGet, recipe.getOutput(getStackInSlot(0)));
		}
	}

}
