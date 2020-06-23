/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.World;
import net.minecraftforge.registries.ObjectHolder;

import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.CratePattern;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;

import java.util.List;
import java.util.Optional;

public class TileCraftCrate extends TileOpenCrate {
	private static final String TAG_PATTERN = "pattern";

	private int signal = 0;

	public TileCraftCrate() {
		super(ModTiles.CRAFT_CRATE);
	}

	@Override
	public int getSizeInventory() {
		return 10;
	}

	@Override
	protected SimpleItemStackHandler createItemHandler() {
		return new SimpleItemStackHandler(this, true) {
			@Override
			protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
				return 1;
			}

			@Nonnull
			@Override
			public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
				if (slot != 9 && !isLocked(slot)) {
					return super.insertItem(slot, stack, simulate);
				} else {
					return stack;
				}
			}
		};
	}

	public CratePattern getPattern() {
		BlockState state = getBlockState();
		if (state.getBlock() != ModBlocks.craftCrate) {
			return CratePattern.NONE;
		}
		return state.get(BotaniaStateProps.CRATE_PATTERN);
	}

	private boolean isLocked(int slot) {
		return !getPattern().openSlots.get(slot);
	}

	@Override
	public void tick() {
		if (world.isRemote) {
			return;
		}

		if (canEject() && isFull() && craft(true)) {
			ejectAll();
		}

		int newSignal = 0;
		for (; newSignal < 9; newSignal++) // dis for loop be derpy
		{
			if (!isLocked(newSignal) && itemHandler.getStackInSlot(newSignal).isEmpty()) {
				break;
			}
		}

		if (newSignal != signal) {
			signal = newSignal;
			world.updateComparatorOutputLevel(pos, getBlockState().getBlock());
		}
	}

	private boolean craft(boolean fullCheck) {
		if (fullCheck && !isFull()) {
			return false;
		}

		CraftingInventory craft = new CraftingInventory(new Container(ContainerType.CRAFTING, -1) {
			@Override
			public boolean canInteractWith(@Nonnull PlayerEntity player) {
				return false;
			}
		}, 3, 3);
		for (int i = 0; i < craft.getSizeInventory(); i++) {
			ItemStack stack = itemHandler.getStackInSlot(i);

			if (stack.isEmpty() || isLocked(i) || stack.getItem() == ModItems.placeholder) {
				continue;
			}

			craft.setInventorySlotContents(i, stack);
		}

		Optional<ICraftingRecipe> matchingRecipe = world.getRecipeManager().getRecipe(IRecipeType.CRAFTING, craft, world);
		matchingRecipe.ifPresent(recipe -> {
			itemHandler.setStackInSlot(9, recipe.getCraftingResult(craft));

			List<ItemStack> remainders = recipe.getRemainingItems(craft);
			for (int i = 0; i < craft.getSizeInventory(); i++) {
				ItemStack s = remainders.get(i);
				if (!itemHandler.getStackInSlot(i).isEmpty()
						&& itemHandler.getStackInSlot(i).getItem() == ModItems.placeholder) {
					continue;
				}
				itemHandler.setStackInSlot(i, s);
			}
		});

		return matchingRecipe.isPresent();
	}

	boolean isFull() {
		for (int i = 0; i < 9; i++) {
			if (!isLocked(i) && itemHandler.getStackInSlot(i).isEmpty()) {
				return false;
			}
		}

		return true;
	}

	private void ejectAll() {
		for (int i = 0; i < getSizeInventory(); ++i) {
			ItemStack stack = itemHandler.getStackInSlot(i);
			if (!stack.isEmpty()) {
				eject(stack, false);
			}
			itemHandler.setStackInSlot(i, ItemStack.EMPTY);
		}
	}

	@Override
	public boolean onWanded(World world, PlayerEntity player, ItemStack stack) {
		if (!world.isRemote && canEject()) {
			craft(false);
			ejectAll();
		}
		return true;
	}

	@Override
	public void markDirty() {
		super.markDirty();
		VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
	}

	@Override
	public int getSignal() {
		return signal;
	}

}
