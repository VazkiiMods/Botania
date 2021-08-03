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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BannerPattern;

import vazkii.botania.common.block.ModBanners;

import javax.annotation.Nonnull;

// Workaround recipe for the loom hardcoding problem.
public class BannerRecipe extends CustomRecipe {
	public static SimpleRecipeSerializer<BannerRecipe> SERIALIZER = new SimpleRecipeSerializer<>(BannerRecipe::new);

	public BannerRecipe(ResourceLocation id) {
		super(id);
	}

	@Override
	public boolean matches(CraftingContainer inv, @Nonnull Level world) {
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
	public ItemStack assemble(@Nonnull CraftingContainer inv) {
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

	@Nonnull
	@Override
	public NonNullList<ItemStack> getRemainingItems(@Nonnull CraftingContainer inv) {
		return RecipeUtils.getRemainingItemsSub(inv, s -> {
			if (ModBanners.PATTERNS.containsKey(s.getItem())) {
				ItemStack stack = s.copy();
				stack.setCount(1);
				return stack;
			}
			return null;
		});
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
			DyeColor dyecolor = ((DyeItem) itemstack1.getItem()).getDyeColor();
			CompoundTag compoundnbt = itemstack2.getOrCreateTagElement("BlockEntityTag");
			ListTag listnbt;
			if (compoundnbt.contains("Patterns", 9)) {
				listnbt = compoundnbt.getList("Patterns", 10);
			} else {
				listnbt = new ListTag();
				compoundnbt.put("Patterns", listnbt);
			}

			CompoundTag compoundnbt1 = new CompoundTag();
			compoundnbt1.putString("Pattern", bannerpattern.getHashname());
			compoundnbt1.putInt("Color", dyecolor.getId());
			listnbt.add(compoundnbt1);
		}
		return itemstack2;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return true;
	}

	@Nonnull
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
}
