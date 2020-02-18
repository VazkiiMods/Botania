/**
 * This class was created by <Hubry>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [2020-01-30, 19:13 (UTC+2)]
 */
package vazkii.botania.common.crafting.recipe;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.DyeColor;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tags.ItemTags;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.tileentity.BannerTileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import vazkii.botania.common.block.ModBanners;

import javax.annotation.Nonnull;

// Workaround recipe for the loom hardcoding problem.
public class BannerRecipe extends SpecialRecipe {
	public static SpecialRecipeSerializer<BannerRecipe> SERIALIZER = new SpecialRecipeSerializer<>(BannerRecipe::new);

	public BannerRecipe(ResourceLocation id) {
		super(id);
	}

	@Override
	public boolean matches(CraftingInventory inv, @Nonnull World world) {
		boolean foundBanner = false;
		boolean foundItem = false;
		boolean foundDye = false;
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			Item item = stack.getItem();
			if (stack.isEmpty())
				continue;

			if (ModBanners.PATTERNS.containsKey(item.delegate) && !foundItem) {
				foundItem = true;
			} else if (ItemTags.BANNERS.contains(item) && !foundBanner && BannerTileEntity.getPatterns(stack) < 6) {
				foundBanner = true;
			} else if (item instanceof DyeItem && !foundDye) {
				foundDye = true;
			} else return false;
		}
		return foundBanner && foundItem && foundDye;
	}

	@Nonnull
	@Override
	public ItemStack getCraftingResult(@Nonnull CraftingInventory inv) {
		ItemStack banner = ItemStack.EMPTY;
		ItemStack dye = ItemStack.EMPTY;
		BannerPattern pattern = null;

		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
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
	}

	// [VanillaCopy] From LoomContainer.func_217031_j, edits noted
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
			CompoundNBT compoundnbt = itemstack2.getOrCreateChildTag("BlockEntityTag");
			ListNBT listnbt;
			if (compoundnbt.contains("Patterns", 9)) {
				listnbt = compoundnbt.getList("Patterns", 10);
			} else {
				listnbt = new ListNBT();
				compoundnbt.put("Patterns", listnbt);
			}

			CompoundNBT compoundnbt1 = new CompoundNBT();
			compoundnbt1.putString("Pattern", bannerpattern.getHashname());
			compoundnbt1.putInt("Color", dyecolor.getId());
			listnbt.add(compoundnbt1);
		}
		return itemstack2;
	}

	@Override
	public boolean canFit(int width, int height) {
		return true;
	}

	@Nonnull
	@Override
	public IRecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}
}
