package vazkii.botania.common.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

/**
 * A Botania Item or Block with this interface can customize what is added to the creative tab.
 */
public interface CustomCreativeTabContents {
	/**
	 * Add this item and any variants to the creative tab
	 * 
	 * @param me The item itself, for convenience when implementing this on a Block
	 */
	void addToCreativeTab(Item me, CreativeModeTab.Output output);
}
