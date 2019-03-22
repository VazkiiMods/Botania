/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Apr 22, 2015, 5:57:59 PM (GMT)]
 */
package vazkii.botania.api.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.api.BotaniaAPI;

public class RecipeMiniFlower extends RecipeManaInfusion {

	public RecipeMiniFlower(ResourceLocation mini, ResourceLocation flower, int mana) {
		super(BotaniaAPI.internalHandler.getSubTileAsStack(flower), Ingredient.fromStacks(BotaniaAPI.internalHandler.getSubTileAsStack(mini)), mana);
		setCatalyst(RecipeManaInfusion.alchemy.getDefaultState());
	}

	@Override
	public boolean matches(ItemStack stack) {
		ResourceLocation key = BotaniaAPI.internalHandler.getStackSubTileKey(stack);
		ItemStack[] matching = getInput().getMatchingStacks();
		if(matching.length == 0)
			return false;


		ResourceLocation input = BotaniaAPI.internalHandler.getStackSubTileKey(matching[0]);
		return key != null && key.equals(input);
	}

}
