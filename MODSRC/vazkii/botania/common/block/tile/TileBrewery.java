/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Oct 31, 2014, 4:42:36 PM (GMT)]
 */
package vazkii.botania.common.block.tile;

import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.brew.IBrewContainer;
import vazkii.botania.api.brew.IBrewItem;
import vazkii.botania.api.mana.IManaReceiver;
import vazkii.botania.api.recipe.RecipeBrew;
import vazkii.botania.api.recipe.RecipeRuneAltar;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibBlockNames;

// This is mostly copypasta from TileRuneAltar
public class TileBrewery extends TileSimpleInventory implements ISidedInventory, IManaReceiver {

	private static final String TAG_MANA = "mana";

	public RecipeBrew recipe;
	int mana = 0;
	public int signal = 0;

	public boolean addItem(EntityPlayer player, ItemStack stack) {
		if(recipe != null || stack == null || stack.getItem() instanceof IBrewItem || ((getStackInSlot(0) == null) != stack.getItem() instanceof IBrewContainer))
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
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			for(RecipeBrew recipe : BotaniaAPI.brewRecipes)
				if(recipe.matches(this)) {
					this.recipe = recipe;
					worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 1, 1 | 2);
				}
		}

		return true;
	}

	@Override
	public void updateEntity() {
		super.updateEntity();

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
			
			if(recipe != null && mana >= recipe.getManaUsage() && !worldObj.isRemote) {
				int mana = recipe.getManaUsage();
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

		int newSignal = 0;
		if(recipe != null)
			newSignal++;

		if(newSignal != signal) {
			signal = newSignal;
			worldObj.func_147453_f(xCoord, yCoord, zCoord, worldObj.getBlock(xCoord, yCoord, zCoord));
		}
	}

	public void craftingFanciness() {
		worldObj.playSoundEffect(xCoord, yCoord, zCoord, "botania:runeAltarCraft", 1F, 1F);
		for(int i = 0; i < 25; i++) {
			float red = (float) Math.random();
			float green = (float) Math.random();
			float blue = (float) Math.random();
			Botania.proxy.sparkleFX(worldObj, xCoord + 0.5 + Math.random() * 0.4 - 0.2, yCoord + 1, zCoord + 0.5 + Math.random() * 0.4 - 0.2, red, green, blue, (float) Math.random(), 10);
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
		return 6;
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
		return recipe == null ? true : mana >= recipe.getManaUsage();
	}

	@Override
	public void recieveMana(int mana) {
		this.mana = Math.min(this.mana + mana, recipe == null ? 0 : recipe.getManaUsage());
	}

	@Override
	public boolean canRecieveManaFromBursts() {
		return !isFull();
	}

}
