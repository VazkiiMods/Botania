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

import vazkii.botania.api.BotaniaAPI;
import net.minecraft.item.ItemStack;

public class RecipeMiniFlower extends RecipeManaInfusion {

	public RecipeMiniFlower(String flower, String mini, int mana) {
		super(BotaniaAPI.internalHandler.getSubTileAsStack(flower), BotaniaAPI.internalHandler.getSubTileAsStack(mini), mana);
		setAlchemy(true);
	}
	
	@Override
	public boolean matches(ItemStack stack) {
		String key = BotaniaAPI.internalHandler.getStackSubTileKey(stack);
		return key != null && key.equals(input);
	}

}
