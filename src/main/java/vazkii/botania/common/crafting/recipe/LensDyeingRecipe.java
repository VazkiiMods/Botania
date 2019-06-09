/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jan 23, 2015, 4:10:24 PM (GMT)]
 */
package vazkii.botania.common.crafting.recipe;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags;
import vazkii.botania.api.mana.ILens;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.lens.ItemLens;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

public class LensDyeingRecipe extends SpecialRecipe {
	private static final ResourceLocation TYPE_ID = new ResourceLocation(LibMisc.MOD_ID, "lens_dye");
	public static final IRecipeSerializer<LensDyeingRecipe> SERIALIZER = new SpecialRecipeSerializer<>(LensDyeingRecipe::new);

	private final List<Ingredient> dyes = Arrays.asList(
			Ingredient.fromTag(Tags.Items.DYES_WHITE), Ingredient.fromTag(Tags.Items.DYES_ORANGE),
			Ingredient.fromTag(Tags.Items.DYES_MAGENTA), Ingredient.fromTag(Tags.Items.DYES_LIGHT_BLUE),
			Ingredient.fromTag(Tags.Items.DYES_YELLOW), Ingredient.fromTag(Tags.Items.DYES_LIME),
			Ingredient.fromTag(Tags.Items.DYES_PINK), Ingredient.fromTag(Tags.Items.DYES_GRAY),
			Ingredient.fromTag(Tags.Items.DYES_LIGHT_GRAY), Ingredient.fromTag(Tags.Items.DYES_CYAN),
			Ingredient.fromTag(Tags.Items.DYES_PURPLE), Ingredient.fromTag(Tags.Items.DYES_BLUE),
			Ingredient.fromTag(Tags.Items.DYES_BROWN), Ingredient.fromTag(Tags.Items.DYES_RED),
			Ingredient.fromTag(Tags.Items.DYES_GREEN), Ingredient.fromTag(Tags.Items.DYES_BLACK),
			Ingredient.fromItems(ModItems.manaPearl) // todo 1.13 tag for mana pearl?
	);

	public LensDyeingRecipe(ResourceLocation id) {
		super(id);
	}

	@Nonnull
	@Override
	public IRecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	@Override
	public boolean matches(@Nonnull CraftingInventory inv, @Nonnull World world) {
		boolean foundLens = false;
		boolean foundDye = false;

		for(int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if(!stack.isEmpty()) {
				if(stack.getItem() instanceof ILens && !foundLens)
					foundLens = true;
				else if(!foundDye) {
					int color = getStackColor(stack);
					if(color > -1)
						foundDye = true;
					else return false;
				}
				else return false;//This means we have an additional item in the recipe after the lens and dye
			}
		}

		return foundLens && foundDye;
	}

	@Nonnull
	@Override
	public ItemStack getCraftingResult(@Nonnull CraftingInventory inv) {
		ItemStack lens = ItemStack.EMPTY;
		int color = -1;

		for(int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if(!stack.isEmpty()) {
				if(stack.getItem() instanceof ILens && lens.isEmpty())
					lens = stack;
				else color = getStackColor(stack);//We can assume if its not a lens its a dye because we checked it in matches()
			}
		}

		if(lens.getItem() instanceof ILens) {
			ItemStack lensCopy = lens.copy();
			ItemLens.setLensColor(lensCopy, color);

			return lensCopy;
		}

		return ItemStack.EMPTY;
	}

	@Override
	public boolean canFit(int width, int height) {
		return width * height >= 2;
	}

	private int getStackColor(ItemStack stack) {
		for (int i = 0; i < dyes.size(); i++) {
			if(dyes.get(i).test(stack)) {
				return i;
			}
		}

		return -1;
	}
}
