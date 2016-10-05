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
import vazkii.botania.api.BotaniaAPI;

public class RecipeMiniFlower extends RecipeManaInfusion {

	public RecipeMiniFlower(String flower, String mini, int mana) {
		super(BotaniaAPI.internalHandler.getSubTileAsStack(flower), BotaniaAPI.internalHandler.getSubTileAsStack(mini), mana);
		setCatalyst(RecipeManaInfusion.alchemyState);
	}

	@Override
	public boolean matches(ItemStack stack) {
		String key = BotaniaAPI.internalHandler.getStackSubTileKey(stack);
		String input = getInput() instanceof String ? (String) getInput() : BotaniaAPI.internalHandler.getStackSubTileKey((ItemStack) getInput());
		return key != null && key.equals(input);
	}

}
