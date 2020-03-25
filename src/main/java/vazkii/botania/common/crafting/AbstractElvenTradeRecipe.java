package vazkii.botania.common.crafting;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import vazkii.botania.common.block.ModBlocks;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

public abstract class AbstractElvenTradeRecipe implements IRecipe<IInventory> {
	private final ResourceLocation id;

	protected AbstractElvenTradeRecipe(ResourceLocation id) {
		this.id = id;
	}

	/**
	 * Attempts to match the recipe
	 *
	 * @param  stacks Entire contents of the portal's buffer
	 * @return        {@link Optional#empty()} if recipe doesn't match, Optional with a set of items used by recipe
	 *                otherwise
	 */
	public abstract Optional<List<ItemStack>> match(List<ItemStack> stacks);

	/**
	 * If the recipe does not contain the item, it will be destroyed upon entering the portal.
	 */
	public abstract boolean containsItem(ItemStack stack);

	/**
	 * @return Preview of the inputs
	 */
	@Nonnull
	@Override
	public abstract NonNullList<Ingredient> getIngredients();

	/**
	 * @return Preview of the outputs
	 */
	public abstract List<ItemStack> getOutputs();

	/**
	 * Actually evaluate the recipe
	 */
	public abstract List<ItemStack> getOutputs(List<ItemStack> inputs);

	@Nonnull
	@Override
	public ItemStack getIcon() {
		return new ItemStack(ModBlocks.alfPortal);
	}

	@Override
	public final ResourceLocation getId() {
		return id;
	}

	@Override
	public final IRecipeType<?> getType() {
		return ModRecipeTypes.ELVEN_TRADE_TYPE;
	}

	// Ignored IRecipe boilerplate

	@Override
	public boolean matches(IInventory inv, World world) {
		return false;
	}

	@Override
	public ItemStack getCraftingResult(IInventory inv) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canFit(int width, int height) {
		return false;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return ItemStack.EMPTY;
	}
}
