/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Nov 17, 2014, 6:34:36 PM (GMT)]
 */
package vazkii.botania.common.crafting.recipe;

import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeHidden;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.decor.BlockFloatingFlower;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;

public class SpecialFloatingFlowerRecipe extends IRecipeHidden {
	private static final ResourceLocation TYPE_ID = new ResourceLocation(LibMisc.MOD_ID, "special_floating_flower");
	public final ResourceLocation flowerType;

	public SpecialFloatingFlowerRecipe(ResourceLocation id, ResourceLocation flowerType) {
		super(id);
		this.flowerType = flowerType;
	}

	@Override
	public boolean matches(@Nonnull IInventory inv, @Nonnull World world) {
		boolean foundFloatingFlower = false;
		boolean foundSpecialFlower = false;

		for(int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if(!stack.isEmpty()) {
				if(Block.getBlockFromItem(stack.getItem()) instanceof BlockFloatingFlower)
					foundFloatingFlower = true;

				else if(stack.getItem() == ModBlocks.specialFlower.asItem() && ItemBlockSpecialFlower.getType(stack).equals(flowerType))
					foundSpecialFlower = true;

				else return false; // Found an invalid item, breaking the recipe
			}
		}

		return foundFloatingFlower && foundSpecialFlower;
	}

	@Nonnull
	@Override
	public ItemStack getCraftingResult(@Nonnull IInventory inv) {
		ItemStack specialFlower = ItemStack.EMPTY;

		for(int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if(!stack.isEmpty() && stack.getItem() == ModBlocks.specialFlower.asItem() && ItemBlockSpecialFlower.getType(stack).equals(flowerType))
				specialFlower = stack;
		}

		if(specialFlower.isEmpty())
			return ItemStack.EMPTY;

		return ItemBlockSpecialFlower.ofType(new ItemStack(ModBlocks.floatingSpecialFlower), flowerType);
	}

	@Override
	public boolean canFit(int width, int height) {
		return width * height >= 2;
	}

	@Nonnull
	@Override
	public IRecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	public static final IRecipeSerializer<SpecialFloatingFlowerRecipe> SERIALIZER = new IRecipeSerializer<SpecialFloatingFlowerRecipe>() {
		@Nonnull
		@Override
		public SpecialFloatingFlowerRecipe read(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json) {
			ResourceLocation flowerType = new ResourceLocation(JsonUtils.getString(json, "type"));
			return new SpecialFloatingFlowerRecipe(recipeId, flowerType);
		}

		@Nonnull
		@Override
		public SpecialFloatingFlowerRecipe read(@Nonnull ResourceLocation recipeId, @Nonnull PacketBuffer buffer) {
			ResourceLocation flowerType = buffer.readResourceLocation();
			return new SpecialFloatingFlowerRecipe(recipeId, flowerType);
		}

		@Override
		public void write(@Nonnull PacketBuffer buffer, @Nonnull SpecialFloatingFlowerRecipe recipe) {
			buffer.writeResourceLocation(recipe.flowerType);
		}

		@Nonnull
		@Override
		public ResourceLocation getName() {
			return TYPE_ID;
		}
	};
}
