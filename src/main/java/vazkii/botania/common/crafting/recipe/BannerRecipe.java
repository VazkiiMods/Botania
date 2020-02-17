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
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class BannerRecipe extends SpecialRecipe {
	public static IRecipeSerializer<BannerRecipe> SERIALIZER = new SpecialRecipeSerializer<>(BannerRecipe::new);

	public BannerRecipe(ResourceLocation id) {
		super(id);
	}

	@Override
	public boolean matches(CraftingInventory inv, World worldIn) {
		return false;
	}

	@Override
	public ItemStack getCraftingResult(CraftingInventory inv) {
		return null;
	}

	@Override
	public boolean canFit(int width, int height) {
		return false;
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return null;
	}
}
