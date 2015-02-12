/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jul 15, 2014, 3:17:00 PM (GMT)]
 */
package vazkii.botania.common.core.handler;

import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import vazkii.botania.common.item.ModItems;

public final class ChestGenHandler {

	public static void init() {
		String c = ChestGenHooks.BONUS_CHEST;
		ChestGenHooks.addItem(c, new WeightedRandomChestContent(new ItemStack(ModItems.lexicon), 1, 1, 7));
		ChestGenHooks.addItem(c, new WeightedRandomChestContent(new ItemStack(ModItems.blackLotus), 1, 1, 1));

		c = ChestGenHooks.STRONGHOLD_CORRIDOR;
		ChestGenHooks.addItem(c, new WeightedRandomChestContent(new ItemStack(ModItems.manaResource, 1, 1), 1, 1, 8));
		ChestGenHooks.addItem(c, new WeightedRandomChestContent(new ItemStack(ModItems.manaResource, 1, 1), 1, 3, 2));
		ChestGenHooks.addItem(c, new WeightedRandomChestContent(new ItemStack(ModItems.blackLotus), 1, 1, 6));
		ChestGenHooks.addItem(c, new WeightedRandomChestContent(new ItemStack(ModItems.overgrowthSeed), 1, 1, 2));

		c = ChestGenHooks.DUNGEON_CHEST;
		ChestGenHooks.addItem(c, new WeightedRandomChestContent(new ItemStack(ModItems.manaResource, 1, 0), 1, 5, 9));
		ChestGenHooks.addItem(c, new WeightedRandomChestContent(new ItemStack(ModItems.lexicon), 1, 1, 6));
		ChestGenHooks.addItem(c, new WeightedRandomChestContent(new ItemStack(ModItems.manaBottle), 1, 1, 5));
		ChestGenHooks.addItem(c, new WeightedRandomChestContent(new ItemStack(ModItems.blackLotus), 1, 1, 6));
		ChestGenHooks.addItem(c, new WeightedRandomChestContent(new ItemStack(ModItems.overgrowthSeed), 1, 1, 2));

		c = ChestGenHooks.PYRAMID_DESERT_CHEST;
		ChestGenHooks.addItem(c, new WeightedRandomChestContent(new ItemStack(ModItems.blackLotus), 1, 1, 6));
		ChestGenHooks.addItem(c, new WeightedRandomChestContent(new ItemStack(ModItems.overgrowthSeed), 1, 1, 2));

		c = ChestGenHooks.MINESHAFT_CORRIDOR;
		ChestGenHooks.addItem(c, new WeightedRandomChestContent(new ItemStack(ModItems.blackLotus), 1, 1, 6));
		ChestGenHooks.addItem(c, new WeightedRandomChestContent(new ItemStack(ModItems.overgrowthSeed), 1, 1, 2));

		c = ChestGenHooks.PYRAMID_JUNGLE_CHEST;
		ChestGenHooks.addItem(c, new WeightedRandomChestContent(new ItemStack(ModItems.blackLotus), 1, 1, 6));
		ChestGenHooks.addItem(c, new WeightedRandomChestContent(new ItemStack(ModItems.overgrowthSeed), 1, 1, 2));

		c = ChestGenHooks.VILLAGE_BLACKSMITH;
		ChestGenHooks.addItem(c, new WeightedRandomChestContent(new ItemStack(ModItems.blackLotus), 1, 1, 6));
	}

}
