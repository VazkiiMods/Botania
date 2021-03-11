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
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.CratePattern;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

public class TileCraftCrate extends TileOpenCrate {
	private static final String TAG_CRAFTING_RESULT = "craft_result";
	private int signal = 0;
	private ItemStack craftResult = ItemStack.EMPTY;

	private final Queue<ResourceLocation> lastRecipes = new ArrayDeque<>();
	private boolean dirty;

	public TileCraftCrate() {
		super(ModTiles.CRAFT_CRATE);
	}

	@Override
	protected Inventory createItemHandler() {
		return new Inventory(9) {
			@Override
			public int getInventoryStackLimit() {
				return 1;
			}

			@Override
			public boolean isItemValidForSlot(int slot, ItemStack stack) {
				return !isLocked(slot);
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
	public void readPacketNBT(CompoundNBT tag) {
		super.readPacketNBT(tag);
		craftResult = ItemStack.read(tag.getCompound(TAG_CRAFTING_RESULT));
	}

	@Override
	public void writePacketNBT(CompoundNBT tag) {
		super.writePacketNBT(tag);
		tag.put(TAG_CRAFTING_RESULT, craftResult.write(new CompoundNBT()));
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
			if (!isLocked(newSignal) && getItemHandler().getStackInSlot(newSignal).isEmpty()) {
				break;
			}
		}

		if (newSignal != signal) {
			signal = newSignal;
			world.updateComparatorOutputLevel(pos, getBlockState().getBlock());
		}

		if (dirty) {
			dirty = false;
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
		}
	}

	private boolean craft(boolean fullCheck) {
		world.getProfiler().startSection("craft");
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
			ItemStack stack = getItemHandler().getStackInSlot(i);

			if (stack.isEmpty() || isLocked(i) || stack.getItem() == ModItems.placeholder) {
				continue;
			}

			craft.setInventorySlotContents(i, stack);
		}

		Optional<ICraftingRecipe> matchingRecipe = getMatchingRecipe(craft);
		matchingRecipe.ifPresent(recipe -> {
			craftResult = recipe.getCraftingResult(craft);

			IInventory handler = getItemHandler();
			List<ItemStack> remainders = recipe.getRemainingItems(craft);

			for (int i = 0; i < craft.getSizeInventory(); i++) {
				ItemStack s = remainders.get(i);
				ItemStack inSlot = handler.getStackInSlot(i);
				if ((inSlot.isEmpty() && s.isEmpty())
						|| (!inSlot.isEmpty() && inSlot.getItem() == ModItems.placeholder)) {
					continue;
				}
				handler.setInventorySlotContents(i, s);
			}
		});

		world.getProfiler().endSection();
		return matchingRecipe.isPresent();
	}

	private Optional<ICraftingRecipe> getMatchingRecipe(CraftingInventory craft) {
		for (ResourceLocation currentRecipe : lastRecipes) {
			Optional<? extends IRecipe<?>> recipe = world.getRecipeManager().getRecipe(currentRecipe)
					.filter(r -> r instanceof ICraftingRecipe)
					.filter(r -> ((ICraftingRecipe) r).matches(craft, world));
			if (recipe.isPresent()) {
				return recipe.map(r -> (ICraftingRecipe) r);
			}
		}
		Optional<ICraftingRecipe> recipe = world.getRecipeManager().getRecipe(IRecipeType.CRAFTING, craft, world);
		if (recipe.isPresent()) {
			if (lastRecipes.size() >= 8) {
				lastRecipes.remove();
			}
			lastRecipes.add(recipe.get().getId());
			return recipe;
		}
		return Optional.empty();
	}

	boolean isFull() {
		for (int i = 0; i < getItemHandler().getSizeInventory(); i++) {
			if (!isLocked(i) && getItemHandler().getStackInSlot(i).isEmpty()) {
				return false;
			}
		}

		return true;
	}

	private void ejectAll() {
		for (int i = 0; i < inventorySize(); ++i) {
			ItemStack stack = getItemHandler().getStackInSlot(i);
			if (!stack.isEmpty()) {
				eject(stack, false);
				getItemHandler().setInventorySlotContents(i, ItemStack.EMPTY);
			}
		}
		if (!craftResult.isEmpty()) {
			eject(craftResult, false);
			craftResult = ItemStack.EMPTY;
		}
	}

	public boolean onWanded(World world) {
		if (!world.isRemote && canEject()) {
			craft(false);
			ejectAll();
		}
		return true;
	}

	@Override
	public void markDirty() {
		super.markDirty();
		if (world != null && !world.isRemote) {
			this.dirty = true;
		}
	}

	public int getSignal() {
		return signal;
	}

}
