/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.crafting.recipe;

import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.tag.ItemTags;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import vazkii.botania.common.block.ModBanners;

import javax.annotation.Nonnull;

// Workaround recipe for the loom hardcoding problem.
public class BannerRecipe extends SpecialCraftingRecipe {
	public static SpecialRecipeSerializer<BannerRecipe> SERIALIZER = new SpecialRecipeSerializer<>(BannerRecipe::new);

	public BannerRecipe(Identifier id) {
		super(id);
	}

	@Override
	public boolean matches(CraftingInventory inv, @Nonnull World world) {
		return false;
		/* todo 1.16-fabric
		boolean foundBanner = false;
		boolean foundItem = false;
		boolean foundDye = false;
		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getStack(i);
			Item item = stack.getItem();
			if (stack.isEmpty()) {
				continue;
			}

			if (ModBanners.PATTERNS.containsKey(item.delegate) && !foundItem) {
				foundItem = true;
			} else if (ItemTags.BANNERS.contains(item) && !foundBanner && BannerBlockEntity.getPatternCount(stack) < 6) {
				foundBanner = true;
			} else if (item instanceof DyeItem && !foundDye) {
				foundDye = true;
			} else {
				return false;
			}
		}
		return foundBanner && foundItem && foundDye;
		*/
	}

	@Nonnull
	@Override
	public ItemStack craft(@Nonnull CraftingInventory inv) {
		ItemStack banner = ItemStack.EMPTY;
		ItemStack dye = ItemStack.EMPTY;
		BannerPattern pattern = null;

		return ItemStack.EMPTY;
		/* todo 1.16-fabric
		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getStack(i);
			Item item = stack.getItem();
			if (ModBanners.PATTERNS.containsKey(item.delegate)) {
				pattern = ModBanners.PATTERNS.get(item.delegate);
			} else if (ItemTags.BANNERS.contains(item)) {
				banner = stack;
			} else if (item instanceof DyeItem) {
				dye = stack;
			}
		}
		return applyPattern(banner, pattern, dye);
		*/
	}

	// [VanillaCopy] From LoomContainer.createOutputStack, edits noted
	@SuppressWarnings("UnnecessaryLocalVariable")
	private static ItemStack applyPattern(ItemStack banner, BannerPattern pattern, ItemStack dye) {
		ItemStack itemstack = banner; // Apply our context here
		ItemStack itemstack1 = dye;
		ItemStack itemstack2 = ItemStack.EMPTY;

		if (!itemstack.isEmpty() && !itemstack1.isEmpty()) {
			itemstack2 = itemstack.copy();
			itemstack2.setCount(1);
			BannerPattern bannerpattern = pattern; // Replace the pattern with provided one
			DyeColor dyecolor = ((DyeItem) itemstack1.getItem()).getColor();
			CompoundTag compoundnbt = itemstack2.getOrCreateSubTag("BlockEntityTag");
			ListTag listnbt;
			if (compoundnbt.contains("Patterns", 9)) {
				listnbt = compoundnbt.getList("Patterns", 10);
			} else {
				listnbt = new ListTag();
				compoundnbt.put("Patterns", listnbt);
			}

			CompoundTag compoundnbt1 = new CompoundTag();
			compoundnbt1.putString("Pattern", bannerpattern.getId());
			compoundnbt1.putInt("Color", dyecolor.getId());
			listnbt.add(compoundnbt1);
		}
		return itemstack2;
	}

	@Override
	public boolean fits(int width, int height) {
		return true;
	}

	@Nonnull
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
}
