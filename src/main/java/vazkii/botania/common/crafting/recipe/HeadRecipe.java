/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jul 23, 2014, 3:02:17 PM (GMT)]
 */
package vazkii.botania.common.crafting.recipe;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.items.IItemHandler;
import vazkii.botania.api.recipe.RecipeRuneAltar;
import vazkii.botania.common.core.helper.ItemNBTHelper;

public class HeadRecipe extends RecipeRuneAltar {

	private String name = "";

	public HeadRecipe(ItemStack output, int mana, Object... inputs) {
		super(output, mana, inputs);
	}

	@Override
	public boolean matches(IItemHandler inv) {
		boolean matches = super.matches(inv);

		if(matches) {
			for(int i = 0; i < inv.getSlots(); i++) {
				ItemStack stack = inv.getStackInSlot(i);
				if(stack.isEmpty())
					break;

				if(stack.getItem() == Items.NAME_TAG) {
					String defaultName = new TextComponentTranslation(Items.NAME_TAG.getTranslationKey()).getString();
					name = stack.getDisplayName().getString();
					if(name.equals(defaultName))
						return false;
				}
			}
		}

		return matches;
	}

	@Override
	public ItemStack getOutput() {
		ItemStack stack = new ItemStack(Items.PLAYER_HEAD);
		if(!name.isEmpty())
			ItemNBTHelper.setString(stack, "SkullOwner", name);
		return stack;
	}

}
