package vazkii.botania.common.core.handler;

import net.minecraft.item.ItemStack;

public class PixieHandler {

	public static interface IPixieSpawner {
		
		public float getPixieChance(ItemStack stack);
		
	}
	
}
