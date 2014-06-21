package vazkii.botania.common.item.equipment.tool.elementium;

import net.minecraft.item.ItemStack;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.core.handler.PixieHandler.IPixieSpawner;
import vazkii.botania.common.item.equipment.tool.manasteel.ItemManasteelSword;
import vazkii.botania.common.lib.LibItemNames;

public class ItemElementiumSword extends ItemManasteelSword implements IPixieSpawner {

	public ItemElementiumSword() {
		super(BotaniaAPI.elementiumToolMaterial, LibItemNames.ELEMENTIUM_SWORD);
	}

	@Override
	public float getPixieChance(ItemStack stack) {
		return 0.5F;
	}

}
