/**
 * This class was created by <Lazersmoke>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [May 6, 2015, 9:45:56 PM (GMT)]
 */
package vazkii.botania.common.crafting.recipe;

import com.google.gson.JsonObject;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;

public class HelmRevealingRecipe implements ICraftingRecipe {
	private final ShapelessRecipe compose;

	public HelmRevealingRecipe(ShapelessRecipe compose) {
		this.compose = compose;
	}

	@Override
	public boolean matches(@Nonnull CraftingInventory inv, @Nonnull World world) {
		return compose.matches(inv, world);
	}

	@Nonnull
	@Override
	public ItemStack getCraftingResult(@Nonnull CraftingInventory inv) {
		ItemStack helm = ItemStack.EMPTY;

		for(int i = 0; i < inv.getSizeInventory(); i++) {
			ItemStack stack = inv.getStackInSlot(i);
			if(!stack.isEmpty() && stack.getItem().getRegistryName().getNamespace().equals(LibMisc.MOD_ID))
				helm = stack;
		}

		if(helm.isEmpty())
			return ItemStack.EMPTY;

		ItemStack newHelm = compose.getCraftingResult(inv);

		//Copy Ancient Wills
		for(int i = 0; i < 6; i++)
			if(ItemNBTHelper.getBoolean(helm, "AncientWill" + i, false))
				ItemNBTHelper.setBoolean(newHelm, "AncientWill" + i, true);

		//Copy Enchantments
		ListNBT enchList = ItemNBTHelper.getList(helm, "ench", 10, true);
		if(enchList != null)
			ItemNBTHelper.setList(newHelm, "ench", enchList);
		copyTCData(helm, newHelm);

		return newHelm;
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
	public NonNullList<Ingredient> getIngredients() {
		return compose.getIngredients();
	}

	@Override
	public boolean isDynamic() {
		return true;
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

	private static final String TAG_RUNIC = "TC.RUNIC";
	private static final String TAG_WARP = "TC.WARP";
	private static final String TAG_INFUSION_ENCH = "infench";

	/**
	 * Copies Thaumcraft's infusion enchantments, runic shielding,
	 * and warping (which is sometimes added as a side effect of infusion enchanting)
	 */
	public static void copyTCData(ItemStack source, ItemStack destination) {
		byte runicShielding = ItemNBTHelper.getByte(source, TAG_RUNIC, (byte) 0);
		if(runicShielding != 0)
			ItemNBTHelper.setByte(destination, TAG_RUNIC, runicShielding);

		byte warp = ItemNBTHelper.getByte(source, TAG_WARP, (byte) 0);
		if(warp != 0)
			ItemNBTHelper.setByte(destination, TAG_WARP, warp);

		ListNBT infEnchList = ItemNBTHelper.getList(source, TAG_INFUSION_ENCH, 10, true);
		if(infEnchList != null)
			ItemNBTHelper.setList(destination, TAG_INFUSION_ENCH, infEnchList);
	}

	public static final IRecipeSerializer<HelmRevealingRecipe> SERIALIZER = new Serializer();

	private static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<HelmRevealingRecipe> {
		@Nonnull
		@Override
		public HelmRevealingRecipe read(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json) {
			return new HelmRevealingRecipe(IRecipeSerializer.CRAFTING_SHAPELESS.read(recipeId, json));
		}

		@Override
		public HelmRevealingRecipe read(@Nonnull ResourceLocation recipeId, @Nonnull PacketBuffer buffer) {
			return new HelmRevealingRecipe(IRecipeSerializer.CRAFTING_SHAPELESS.read(recipeId, buffer));
		}

		@Override
		public void write(@Nonnull PacketBuffer buffer, @Nonnull HelmRevealingRecipe recipe) {
			IRecipeSerializer.CRAFTING_SHAPELESS.write(buffer, recipe.compose);
		}
	};
}
