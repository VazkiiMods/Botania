/**
 * This class was created by <WireSegal>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Apr 4, 2016, 7:49:08 PM (EST)]
 */
package vazkii.botania.common.crafting.recipe;

import com.google.gson.JsonObject;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.RecipeSerializers;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import vazkii.botania.common.Botania;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;

public class ArmorUpgradeRecipe implements IRecipe {
	private static final ResourceLocation TYPE_ID = new ResourceLocation(LibMisc.MOD_ID, "armor_upgrade");
	private final ShapedRecipe compose;

	public ArmorUpgradeRecipe(ShapedRecipe compose) {
		this.compose = compose;
	}

	@Override
	public boolean matches(@Nonnull IInventory inv, @Nonnull World world) {
		return compose.matches(inv, world);
	}

	@Nonnull
	@Override
	public ItemStack getCraftingResult(@Nonnull IInventory inv) {
		ItemStack out = compose.getCraftingResult(inv);
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if (!stack.isEmpty() && stack.getItem() instanceof ItemArmor && stack.hasTag()) {
				EnchantmentHelper.setEnchantments(EnchantmentHelper.getEnchantments(stack), out);
				if(Botania.thaumcraftLoaded)
					HelmRevealingRecipe.copyTCData(stack, out);
				break;
			}
		}
		return out;
	}

	@Override
	public boolean canFit(int width, int height) {
		return compose.canFit(width, height);
	}

	@Nonnull
	@Override
	public ItemStack getRecipeOutput() {
		return compose.getRecipeOutput();
	}

	@Nonnull
	@Override
	public ResourceLocation getId() {
		return compose.getId();
	}

	@Nonnull
	@Override
	public IRecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	public static final IRecipeSerializer<ArmorUpgradeRecipe> SERIALIZER = new IRecipeSerializer<ArmorUpgradeRecipe>() {
		@Override
		public ArmorUpgradeRecipe read(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json) {
			return new ArmorUpgradeRecipe(RecipeSerializers.CRAFTING_SHAPED.read(recipeId, json));
		}

		@Override
		public ArmorUpgradeRecipe read(@Nonnull ResourceLocation recipeId, @Nonnull PacketBuffer buffer) {
			return new ArmorUpgradeRecipe(RecipeSerializers.CRAFTING_SHAPED.read(recipeId, buffer));
		}

		@Override
		public void write(@Nonnull PacketBuffer buffer, @Nonnull ArmorUpgradeRecipe recipe) {
			RecipeSerializers.CRAFTING_SHAPED.write(buffer, recipe.compose);
		}

		@Nonnull
		@Override
		public ResourceLocation getName() {
			return TYPE_ID;
		}
	};
}
