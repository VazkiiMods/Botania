package vazkii.botania.common.helper;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

public class EntityHelper {
	/**
	 * Shrinks the itemstack in an entity and ensures that the new size is sent to clients
	 */
	public static void shrinkItem(ItemEntity entity) {
		entity.getItem().shrink(1);
		syncItem(entity);
	}

	/**
	 * Forces an item entity to resync its item to the client.
	 *
	 * Entity data only resyncs when the old/new values are not Object.equals().
	 * Since stacks do not implement equals, and we're not changing the actual stack object,
	 * the old/new values are == and thus the game doesn't resync.
	 * We have to set a dummy value then set it back to tell the game to resync.
	 */
	public static void syncItem(ItemEntity entity) {
		var save = entity.getItem();
		entity.setItem(ItemStack.EMPTY);
		entity.setItem(save);
	}
}
