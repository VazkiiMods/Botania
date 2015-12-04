/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Feb 2, 2014, 6:31:19 PM (GMT)]
 */
package vazkii.botania.common.block.tile;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.mana.IManaReceiver;
import vazkii.botania.api.recipe.RecipeRuneAltar;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibBlockNames;

public class TileRuneAltar extends TileSimpleInventory implements ISidedInventory, IManaReceiver {

	private static final String TAG_MANA = "mana";
	private static final String TAG_MANA_TO_GET = "manaToGet";

	RecipeRuneAltar currentRecipe;

	public int manaToGet = 0;
	int mana = 0;
	int cooldown = 0;
	public int signal = 0;

	List<ItemStack> lastRecipe = null;
	int recipeKeepTicks = 0;

	public boolean addItem(EntityPlayer player, ItemStack stack) {
		if(cooldown > 0 || stack.getItem() == ModItems.twigWand || stack.getItem() == ModItems.lexicon)
			return false;

		if(stack.getItem() == Item.getItemFromBlock(ModBlocks.livingrock) && stack.getItemDamage() == 0) {
			if(player == null || !player.capabilities.isCreativeMode) {
				stack.stackSize--;
				if(stack.stackSize == 0 && player != null)
					player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
			}

			EntityItem item = new EntityItem(worldObj, xCoord + 0.5, yCoord + 1, zCoord + 0.5, new ItemStack(ModBlocks.livingrock));
			item.delayBeforeCanPickup = 40;
			item.motionX = item.motionY = item.motionZ = 0;
			if(!worldObj.isRemote)
				worldObj.spawnEntityInWorld(item);

			return true;
		}

		if(manaToGet != 0)
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
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(worldObj, xCoord, yCoord, zCoord);

		return true;
	}

	@Override
	public void updateEntity() {
		super.updateEntity();

		// Update every tick.
		recieveMana(0);

		if(!worldObj.isRemote && manaToGet == 0) {
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
				Vector3 vec = Vector3.fromTileEntityCenter(this);
				Vector3 endVec = vec.copy().add(0, 2.5, 0);
				Botania.proxy.lightningFX(worldObj, vec, endVec, 2F, 0x00948B, 0x00E4D7);
			}
		}

		if(cooldown > 0) {
			cooldown--;
			Botania.proxy.wispFX(getWorldObj(), xCoord + Math.random(), yCoord + 0.8, zCoord + Math.random(), 0.2F, 0.2F, 0.2F, 0.2F, -0.025F);
		}

		int newSignal = 0;
		if(manaToGet > 0) {
			newSignal++;
			if(mana >= manaToGet)
				newSignal++;
		}

		if(newSignal != signal) {
			signal = newSignal;
			worldObj.func_147453_f(xCoord, yCoord, zCoord, worldObj.getBlock(xCoord, yCoord, zCoord));
		}

		if(recipeKeepTicks > 0)
			--recipeKeepTicks;
		else lastRecipe = null;
		
		updateRecipe();
	}

	public void updateRecipe() {
		int manaToGet = this.manaToGet;

		getMana : {
			if(currentRecipe != null)
				this.manaToGet = currentRecipe.getManaUsage();
			else {
				for(RecipeRuneAltar recipe : BotaniaAPI.runeAltarRecipes)
					if(recipe.matches(this)) {
						this.manaToGet = recipe.getManaUsage();
						break getMana;
					}
				this.manaToGet = 0;
			}
		}

		if(manaToGet != this.manaToGet) {
			worldObj.playSoundEffect(xCoord, yCoord, zCoord, "botania:runeAltarStart", 1F, 1F);
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(worldObj, xCoord, yCoord, zCoord);
		}
	}

	public void saveLastRecipe() {
		lastRecipe = new ArrayList();
		for(int i = 0; i < getSizeInventory(); i++) {
			ItemStack stack = getStackInSlot(i);
			if(stack == null)
				break;
			lastRecipe.add(stack.copy());
		}
		recipeKeepTicks = 400;
	}

	public void trySetLastRecipe(EntityPlayer player) {
		TileAltar.tryToSetLastRecipe(player, this, lastRecipe);
		if(!isEmpty())
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(worldObj, xCoord, yCoord, zCoord);
	}

	public boolean hasValidRecipe() {
		for(RecipeRuneAltar recipe : BotaniaAPI.runeAltarRecipes)
			if(recipe.matches(this))
				return true;

		return false;
	}

	public void onWanded(EntityPlayer player, ItemStack wand) {
		RecipeRuneAltar recipe = null;

		if(currentRecipe != null)
			recipe = currentRecipe;
		else for(RecipeRuneAltar recipe_ : BotaniaAPI.runeAltarRecipes) {
			if(recipe_.matches(this)) {
				recipe = recipe_;
				break;
			}
		}

		if(manaToGet > 0 && mana >= manaToGet && !worldObj.isRemote) {
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
					currentRecipe = null;
					cooldown = 60;
				}

				saveLastRecipe();
				for(int i = 0; i < getSizeInventory(); i++) {
					ItemStack stack = getStackInSlot(i);
					if(stack != null) {
						if(stack.getItem() == ModItems.rune && (player == null || !player.capabilities.isCreativeMode)) {
							EntityItem outputItem = new EntityItem(worldObj, xCoord + 0.5, yCoord + 1.5, zCoord + 0.5, stack.copy());
							worldObj.spawnEntityInWorld(outputItem);
						}
						
						setInventorySlotContents(i, null);
					}
				}

				if(!worldObj.isRemote) {
					ItemStack livingrockItem = livingrock.getEntityItem();
					livingrockItem.stackSize--;
					if(livingrockItem.stackSize == 0)
						livingrock.setDead();
				}

				craftingFanciness();
			}
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

	public boolean isEmpty() {
		for(int i = 0; i < getSizeInventory(); i++)
			if(getStackInSlot(i) != null)
				return false;

		return true;
	}

	@Override
	public void writeCustomNBT(NBTTagCompound par1nbtTagCompound) {
		super.writeCustomNBT(par1nbtTagCompound);

		par1nbtTagCompound.setInteger(TAG_MANA, mana);
		par1nbtTagCompound.setInteger(TAG_MANA_TO_GET, manaToGet);
	}

	@Override
	public void readCustomNBT(NBTTagCompound par1nbtTagCompound) {
		super.readCustomNBT(par1nbtTagCompound);

		mana = par1nbtTagCompound.getInteger(TAG_MANA);
		manaToGet = par1nbtTagCompound.getInteger(TAG_MANA_TO_GET);
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
		return mana == 0;
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

	public void renderHUD(Minecraft mc, ScaledResolution res) {
		if(manaToGet > 0) {
			int x = res.getScaledWidth() / 2 + 20;
			int y = res.getScaledHeight() / 2 - 8;

			RecipeRuneAltar recipe = null;
			for(RecipeRuneAltar recipe_ : BotaniaAPI.runeAltarRecipes)
				if(recipe_.matches(this)) {
					recipe = recipe_;
					break;
				}
			if(recipe == null)
				return;

			RenderHelper.renderProgressPie(x, y, (float) mana / (float) manaToGet, recipe.getOutput());
		}
	}

	public int getTargetMana() {
		return manaToGet;
	}

}
