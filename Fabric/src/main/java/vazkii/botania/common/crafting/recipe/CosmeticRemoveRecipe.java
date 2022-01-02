/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.crafting.recipe;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraft.world.level.Level;

import vazkii.botania.api.item.ICosmeticAttachable;
import vazkii.botania.api.item.ICosmeticBauble;
import vazkii.botania.common.item.equipment.bauble.ItemBauble;

import javax.annotation.Nonnull;

public class CosmeticRemoveRecipe extends CustomRecipe {
	public static final SimpleRecipeSerializer<CosmeticRemoveRecipe> SERIALIZER = new SimpleRecipeSerializer<>(CosmeticRemoveRecipe::new);

	public CosmeticRemoveRecipe(ResourceLocation id) {
		super(id);
	}

	@Override
	public boolean matches(@Nonnull CraftingContainer inv, @Nonnull Level world) {
		boolean foundAttachable = false;

		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack stack = inv.getItem(i);
			if (!stack.isEmpty()) {
				if (stack.getItem() instanceof ICosmeticAttachable && !(stack.getItem() instanceof ICosmeticBauble) && !((ICosmeticAttachable) stack.getItem()).getCosmeticItem(stack).isEmpty()) {
					foundAttachable = true;
				} else {
					return false;
				}
			}
		}

		return foundAttachable;
	}

	@Nonnull
	@Override
	public ItemStack assemble(@Nonnull CraftingContainer inv) {
		ItemStack attachableItem = ItemStack.EMPTY;

		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack stack = inv.getItem(i);
			if (!stack.isEmpty()) {
				attachableItem = stack;
			}
		}

		ICosmeticAttachable attachable = (ICosmeticAttachable) attachableItem.getItem();
		if (attachable.getCosmeticItem(attachableItem).isEmpty()) {
			return ItemStack.EMPTY;
		}

		ItemStack copy = attachableItem.copy();
		copy.setCount(1);
		attachable.setCosmeticItem(copy, ItemStack.EMPTY);
		return copy;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width * height > 0;
	}

	@Nonnull
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	@Nonnull
	@Override
	public NonNullList<ItemStack> getRemainingItems(@Nonnull CraftingContainer inv) {
		return RecipeUtils.getRemainingItemsSub(inv, s -> {
			if (s.getItem() instanceof ItemBauble) {
				ItemStack stack = ((ItemBauble) s.getItem()).getCosmeticItem(s);
				stack.setCount(1);
				return stack;
			}
			return null;
		});
	}
}
