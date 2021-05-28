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
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.CratePattern;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.mixin.AccessorRecipeManager;

import javax.annotation.Nonnull;

import java.util.*;

public class TileCraftCrate extends TileOpenCrate {
	private static final String TAG_CRAFTING_RESULT = "craft_result";
	private int signal = 0;
	private ItemStack craftResult = ItemStack.EMPTY;

	private final Queue<Identifier> lastRecipes = new ArrayDeque<>();
	private boolean dirty;

	public TileCraftCrate() {
		super(ModTiles.CRAFT_CRATE);
	}

	@Override
	protected SimpleInventory createItemHandler() {
		return new SimpleInventory(9) {
			@Override
			public int getMaxCountPerStack() {
				return 1;
			}

			@Override
			public boolean isValid(int slot, ItemStack stack) {
				return !isLocked(slot);
			}
		};
	}

	public CratePattern getPattern() {
		BlockState state = getCachedState();
		if (state.getBlock() != ModBlocks.craftCrate) {
			return CratePattern.NONE;
		}
		return state.get(BotaniaStateProps.CRATE_PATTERN);
	}

	private boolean isLocked(int slot) {
		return !getPattern().openSlots.get(slot);
	}

	@Override
	public void readPacketNBT(CompoundTag tag) {
		super.readPacketNBT(tag);
		craftResult = ItemStack.fromTag(tag.getCompound(TAG_CRAFTING_RESULT));
	}

	@Override
	public void writePacketNBT(CompoundTag tag) {
		super.writePacketNBT(tag);
		tag.put(TAG_CRAFTING_RESULT, craftResult.toTag(new CompoundTag()));
	}

	@Override
	public void tick() {
		if (world.isClient) {
			return;
		}

		if (canEject() && isFull() && craft(true)) {
			ejectAll();
		}

		int newSignal = 0;
		for (; newSignal < 9; newSignal++) // dis for loop be derpy
		{
			if (!isLocked(newSignal) && getItemHandler().getStack(newSignal).isEmpty()) {
				break;
			}
		}

		if (newSignal != signal) {
			signal = newSignal;
			world.updateComparators(pos, getCachedState().getBlock());
		}

		if (dirty) {
			dirty = false;
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
		}
	}

	private boolean craft(boolean fullCheck) {
		world.getProfiler().push("craft");
		if (fullCheck && !isFull()) {
			return false;
		}

		CraftingInventory craft = new CraftingInventory(new ScreenHandler(ScreenHandlerType.CRAFTING, -1) {
			@Override
			public boolean canUse(@Nonnull PlayerEntity player) {
				return false;
			}
		}, 3, 3);
		for (int i = 0; i < craft.size(); i++) {
			ItemStack stack = getItemHandler().getStack(i);

			if (stack.isEmpty() || isLocked(i) || stack.getItem() == ModItems.placeholder) {
				continue;
			}

			craft.setStack(i, stack);
		}

		Optional<CraftingRecipe> matchingRecipe = getMatchingRecipe(craft);
		matchingRecipe.ifPresent(recipe -> {
			craftResult = recipe.craft(craft);

			Inventory handler = getItemHandler();
			List<ItemStack> remainders = recipe.getRemainingStacks(craft);

			for (int i = 0; i < craft.size(); i++) {
				ItemStack s = remainders.get(i);
				ItemStack inSlot = handler.getStack(i);
				if ((inSlot.isEmpty() && s.isEmpty())
						|| (!inSlot.isEmpty() && inSlot.getItem() == ModItems.placeholder)) {
					continue;
				}
				handler.setStack(i, s);
			}
		});

		world.getProfiler().pop();
		return matchingRecipe.isPresent();
	}

	private Optional<CraftingRecipe> getMatchingRecipe(CraftingInventory craft) {
		for (Identifier currentRecipe : lastRecipes) {
			Recipe<CraftingInventory> recipe = ((AccessorRecipeManager) world.getRecipeManager())
					.botania_getAll(RecipeType.CRAFTING)
					.get(currentRecipe);
			if (recipe instanceof CraftingRecipe && recipe.matches(craft, world)) {
				return Optional.of((CraftingRecipe) recipe);
			}
		}
		Optional<CraftingRecipe> recipe = world.getRecipeManager().getFirstMatch(RecipeType.CRAFTING, craft, world);
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
		for (int i = 0; i < getItemHandler().size(); i++) {
			if (!isLocked(i) && getItemHandler().getStack(i).isEmpty()) {
				return false;
			}
		}

		return true;
	}

	private void ejectAll() {
		for (int i = 0; i < inventorySize(); ++i) {
			ItemStack stack = getItemHandler().getStack(i);
			if (!stack.isEmpty()) {
				eject(stack, false);
				getItemHandler().setStack(i, ItemStack.EMPTY);
			}
		}
		if (!craftResult.isEmpty()) {
			eject(craftResult, false);
			craftResult = ItemStack.EMPTY;
		}
	}

	public boolean onWanded(World world) {
		if (!world.isClient && canEject()) {
			craft(false);
			ejectAll();
		}
		return true;
	}

	@Override
	public void markDirty() {
		super.markDirty();
		if (world != null && !world.isClient) {
			this.dirty = true;
		}
	}

	public int getSignal() {
		return signal;
	}

}
