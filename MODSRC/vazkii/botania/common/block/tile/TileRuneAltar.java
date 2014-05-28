/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Feb 2, 2014, 6:31:19 PM (GMT)]
 */
package vazkii.botania.common.block.tile;

import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.mana.IManaReceiver;
import vazkii.botania.api.recipe.RecipeRuneAltar;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibBlockNames;

public class TileRuneAltar extends TileSimpleInventory implements ISidedInventory, IManaReceiver {

	private static final String TAG_MANA = "mana";

	int manaToGet = 0;
	int mana = 0;
	int cooldown = 0;

	public boolean addItem(EntityPlayer player, ItemStack stack) {
		if(cooldown > 0 || stack.getItem() == ModItems.twigWand || stack.getItem() == ModItems.lexicon || manaToGet != 0)
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

		if(did)
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);

		return true;
	}

	@Override
	public void updateEntity() {
		super.updateEntity();

		// Update every tick.
		recieveMana(0);

		if(!worldObj.isRemote) {
			List<EntityItem> items = worldObj.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1));
			for(EntityItem item : items)
				if(!item.isDead && item.getEntityItem() != null && item.getEntityItem().getItem() != Item.getItemFromBlock(ModBlocks.livingrock)) {
					ItemStack stack = item.getEntityItem();
					if(addItem(null, stack) && stack.stackSize == 0)
						item.setDead();
				}
		}


		if(worldObj.isRemote && manaToGet > 0 && mana >= manaToGet) {
			if(worldObj.rand.nextInt(20) == 0) {
				worldObj.playSoundEffect(xCoord, yCoord, zCoord, "mob.creeper.live", 1F, 1F);

				Vector3 vec = Vector3.fromTileEntityCenter(this);
				Vector3 endVec = vec.copy().add(0, 2.5, 0);
				Botania.proxy.lightningFX(worldObj, vec, endVec, 2F, 0x00948B, 0x00E4D7);
			}
		}

		if(cooldown > 0) {
			cooldown--;
			Botania.proxy.wispFX(getWorldObj(), xCoord + Math.random(), yCoord + 0.8, zCoord + Math.random(), 0.2F, 0.2F, 0.2F, 0.2F, -0.025F);
		}

	}

	public void updateRecipe() {
		int manaToGet = this.manaToGet;

		getMana : {
			for(RecipeRuneAltar recipe : BotaniaAPI.runeAltarRecipes)
				if(recipe.matches(this)) {
					this.manaToGet = recipe.getManaUsage();
					break getMana;
				}
			this.manaToGet = 0;
		}

		if(manaToGet != this.manaToGet) {
			worldObj.playSoundEffect(xCoord, yCoord, zCoord, "random.orb", 1F, 1F);
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
	}

	public void onWanded(EntityPlayer player, ItemStack wand) {
		updateRecipe();

		RecipeRuneAltar recipe = null;

		for(RecipeRuneAltar recipe_ : BotaniaAPI.runeAltarRecipes) {
			if(recipe_.matches(this)) {
				recipe = recipe_;
				break;
			}
		}

		if(manaToGet > 0 && mana >= manaToGet) {
			List<EntityItem> items = worldObj.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1));
			EntityItem livingrock = null;
			for(EntityItem item : items)
				if(!item.isDead && item.getEntityItem() != null && item.getEntityItem().getItem() == Item.getItemFromBlock(ModBlocks.livingrock)) {
					livingrock = item;
					break;
				}

			if(livingrock != null) {
				int mana = recipe.getManaUsage();
				recieveMana(-mana);
				if(!worldObj.isRemote) {
					ItemStack output = recipe.getOutput().copy();
					EntityItem outputItem = new EntityItem(worldObj, xCoord + 0.5, yCoord + 1.5, zCoord + 0.5, output);
					worldObj.spawnEntityInWorld(outputItem);
					cooldown = 60;
				}

				for(int i = 0; i < getSizeInventory(); i++)
					setInventorySlotContents(i, null);

				if(!worldObj.isRemote) {
					ItemStack livingrockItem = livingrock.getEntityItem();
					livingrockItem.stackSize--;
					if(livingrockItem.stackSize == 0)
						livingrock.setDead();
				}

				craftingFanciness();
			}
		}

		updateRecipe();
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
		return 16;
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
		return false;
	}

	@Override
	public int getCurrentMana() {
		return mana;
	}

	@Override
	public boolean isFull() {
		return mana >= manaToGet;
	}

	@Override
	public void recieveMana(int mana) {
		this.mana = Math.min(this.mana + mana, manaToGet);
	}

	@Override
	public boolean canRecieveManaFromBursts() {
		return !isFull();
	}

	public int getTargetMana() {
		return manaToGet;
	}

}
