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

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.registry.GameRegistry;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;

public class HelmRevealingRecipe extends ShapelessRecipes {
	@GameRegistry.ObjectHolder("thaumcraft:goggles")
	private static Item goggles = null;

	private final Item botaniaHelm;

	public HelmRevealingRecipe(Item output, Item botaniaHelm) {
		super("botania:helm_revealing", new ItemStack(output),
				NonNullList.from(Ingredient.EMPTY, Ingredient.fromItem(botaniaHelm), Ingredient.fromItem(goggles)));
		this.botaniaHelm = botaniaHelm;
	}

	@Nonnull
	@Override
	public ItemStack getCraftingResult(@Nonnull InventoryCrafting var1) {
		ItemStack helm = ItemStack.EMPTY;

		for(int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack stack = var1.getStackInSlot(i);
			if(!stack.isEmpty() && stack.getItem() == botaniaHelm)
				helm = stack;
		}

		if(helm.isEmpty())
			return ItemStack.EMPTY;

		ItemStack helmCopy = helm.copy();
		Item helmItem = helmCopy.getItem();

		ItemStack newHelm;

		if(helmItem == ModItems.manasteelHelm)
			newHelm = new ItemStack(ModItems.manasteelHelmRevealing);
		else if(helmItem == ModItems.terrasteelHelm)
			newHelm = new ItemStack(ModItems.terrasteelHelmRevealing);
		else if(helmItem == ModItems.elementiumHelm)
			newHelm = new ItemStack(ModItems.elementiumHelmRevealing);
		else return ItemStack.EMPTY;

		//Copy Ancient Wills
		for(int i = 0; i < 6; i++)
			if(ItemNBTHelper.getBoolean(helmCopy, "AncientWill" + i, false))
				ItemNBTHelper.setBoolean(newHelm, "AncientWill" + i, true);

		//Copy Enchantments
		NBTTagList enchList = ItemNBTHelper.getList(helmCopy, "ench", 10, true);
		if(enchList != null)
			ItemNBTHelper.setList(newHelm, "ench", enchList);

		//Copy Runic Hardening
		// TODO does tc6 still have this?
		byte runicHardening = ItemNBTHelper.getByte(helmCopy, "RS.HARDEN", (byte)0);
		ItemNBTHelper.setByte(newHelm, "RS.HARDEN", runicHardening);

		return newHelm;
	}

	@Override
	public boolean isDynamic() {
		return true;
	}
}