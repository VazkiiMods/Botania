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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.brew.IBrewContainer;
import vazkii.botania.api.brew.IBrewItem;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.mana.IManaReceiver;
import vazkii.botania.api.recipe.RecipeBrew;
import vazkii.botania.api.sound.BotaniaSoundEvents;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.client.core.helper.RenderHelper;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;

// This is mostly copypasta from TileRuneAltar
public class TileBrewery extends TileSimpleInventory implements IManaReceiver {

	private static final String TAG_MANA = "mana";

	public RecipeBrew recipe;
	int mana = 0;
	int manaLastTick = 0;
	public int signal = 0;

	public boolean addItem(@Nullable EntityPlayer player, ItemStack stack, @Nullable EnumHand hand) {
		if(recipe != null || stack == null || stack.getItem() instanceof IBrewItem && ((IBrewItem) stack.getItem()).getBrew(stack) != null && ((IBrewItem) stack.getItem()).getBrew(stack) != BotaniaAPI.fallbackBrew || itemHandler.getStackInSlot(0) == null != stack.getItem() instanceof IBrewContainer)
			return false;

		boolean did = false;

		for(int i = 0; i < getSizeInventory(); i++)
			if(itemHandler.getStackInSlot(i) == null) {
				did = true;
				ItemStack stackToAdd = stack.copy();
				stackToAdd.stackSize = 1;
				itemHandler.setStackInSlot(i, stackToAdd);

				if(player == null || !player.capabilities.isCreativeMode) {
					stack.stackSize--;
					if(stack.stackSize == 0 && player != null)
						player.setHeldItem(hand, null);
				}

				break;
			}

		if(did) {
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(worldObj, pos);
			for(RecipeBrew recipe : BotaniaAPI.brewRecipes)
				if(recipe.matches(itemHandler) && recipe.getOutput(itemHandler.getStackInSlot(0)) != null) {
					this.recipe = recipe;
					worldObj.setBlockState(pos, ModBlocks.brewery.getDefaultState().withProperty(BotaniaStateProps.POWERED, true), 1 | 2);
				}
		}

		return true;
	}

	@Override
	public void update() {
		if(mana > 0 && recipe == null) {
			for(RecipeBrew recipe : BotaniaAPI.brewRecipes)
				if(recipe.matches(itemHandler)) {
					this.recipe = recipe;
					worldObj.setBlockState(pos, ModBlocks.brewery.getDefaultState().withProperty(BotaniaStateProps.POWERED, true), 1 | 2);
				}

			if(recipe == null)
				mana = 0;
		}

		// Update every tick.
		recieveMana(0);

		if(!worldObj.isRemote && recipe == null) {
			List<EntityItem> items = worldObj.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1));
			for(EntityItem item : items)
				if(!item.isDead && item.getEntityItem() != null) {
					ItemStack stack = item.getEntityItem();
					if(addItem(null, stack, null) && stack.stackSize == 0)
						item.setDead();
				}
		}

		if(recipe != null) {
			if(!recipe.matches(itemHandler)) {
				recipe = null;
				worldObj.setBlockState(pos, ModBlocks.brewery.getDefaultState(), 1 | 2);
			}

			if(recipe != null) {
				if(mana != manaLastTick) {
					Color color = new Color(recipe.getBrew().getColor(itemHandler.getStackInSlot(0)));
					float r = color.getRed() / 255F;
					float g = color.getGreen() / 255F;
					float b = color.getBlue() / 255F;
					for(int i = 0; i < 5; i++) {
						Botania.proxy.wispFX(pos.getX() + 0.7 - Math.random() * 0.4, pos.getY() + 0.9 - Math.random() * 0.2, pos.getZ() + 0.7 - Math.random() * 0.4, r, g, b, 0.1F + (float) Math.random() * 0.05F, 0.03F - (float) Math.random() * 0.06F, 0.03F + (float) Math.random() * 0.015F, 0.03F - (float) Math.random() * 0.06F);
						for(int j = 0; j < 2; j++)
							Botania.proxy.wispFX(pos.getX() + 0.7 - Math.random() * 0.4, pos.getY() + 0.9 - Math.random() * 0.2, pos.getZ() + 0.7 - Math.random() * 0.4, 0.2F, 0.2F, 0.2F, 0.1F + (float) Math.random() * 0.2F, 0.03F - (float) Math.random() * 0.06F, 0.03F + (float) Math.random() * 0.015F, 0.03F - (float) Math.random() * 0.06F);
					}
				}

				if(mana >= getManaCost() && !worldObj.isRemote) {
					int mana = getManaCost();
					recieveMana(-mana);
					if(!worldObj.isRemote) {
						ItemStack output = recipe.getOutput(itemHandler.getStackInSlot(0));
						EntityItem outputItem = new EntityItem(worldObj, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, output);
						worldObj.spawnEntityInWorld(outputItem);
					}

					for(int i = 0; i < getSizeInventory(); i++)
						itemHandler.setStackInSlot(i, null);

					craftingFanciness();
				}
			}
		}

		int newSignal = 0;
		if(recipe != null)
			newSignal++;

		if(newSignal != signal) {
			signal = newSignal;
			worldObj.updateComparatorOutputLevel(pos, worldObj.getBlockState(pos).getBlock());
		}

		manaLastTick = mana;
	}

	public int getManaCost() {
		ItemStack stack = itemHandler.getStackInSlot(0);
		if(recipe == null || stack == null || !(stack.getItem() instanceof IBrewContainer))
			return 0;
		IBrewContainer container = (IBrewContainer) stack.getItem();
		return container.getManaCost(recipe.getBrew(), stack);
	}

	public void craftingFanciness() {
		worldObj.playSound(null, pos, BotaniaSoundEvents.potionCreate, SoundCategory.BLOCKS, 1F, 1.5F + (float) Math.random() * 0.25F);
		for(int i = 0; i < 25; i++) {
			Color color = new Color(recipe.getBrew().getColor(itemHandler.getStackInSlot(0)));
			float r = color.getRed() / 255F;
			float g = color.getGreen() / 255F;
			float b = color.getBlue() / 255F;
			Botania.proxy.sparkleFX(pos.getX() + 0.5 + Math.random() * 0.4 - 0.2, pos.getY() + 1, pos.getZ() + 0.5 + Math.random() * 0.4 - 0.2, r, g, b, (float) Math.random() * 2F + 0.5F, 10);
			for(int j = 0; j < 2; j++)
				Botania.proxy.wispFX(pos.getX() + 0.7 - Math.random() * 0.4, pos.getY() + 0.9 - Math.random() * 0.2, pos.getZ() + 0.7 - Math.random() * 0.4, 0.2F, 0.2F, 0.2F, 0.1F + (float) Math.random() * 0.2F, 0.05F - (float) Math.random() * 0.1F, 0.05F + (float) Math.random() * 0.03F, 0.05F - (float) Math.random() * 0.1F);
		}
	}

	@Override
	public void writePacketNBT(NBTTagCompound par1nbtTagCompound) {
		super.writePacketNBT(par1nbtTagCompound);

		par1nbtTagCompound.setInteger(TAG_MANA, mana);
	}

	@Override
	public void readPacketNBT(NBTTagCompound par1nbtTagCompound) {
		super.readPacketNBT(par1nbtTagCompound);

		mana = par1nbtTagCompound.getInteger(TAG_MANA);
	}

	@Override
	public int getSizeInventory() {
		return 7;
	}

	@Nonnull
	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return INFINITE_EXTENT_AABB;
	}

	@Override
	protected SimpleItemStackHandler createItemHandler() {
		return new SimpleItemStackHandler(this, false) {
			@Override
			protected int getStackLimit(int slot, ItemStack stack) {
				return 1;
			}
		};
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

			RenderHelper.renderProgressPie(x, y, (float) mana / (float) manaToGet, recipe.getOutput(itemHandler.getStackInSlot(0)));
		}
	}

}
