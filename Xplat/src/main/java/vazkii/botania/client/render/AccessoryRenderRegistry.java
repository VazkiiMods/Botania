package vazkii.botania.client.render;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class AccessoryRenderRegistry {
	private static final Map<Item, AccessoryRenderer> REGISTRATIONS = new HashMap<>();

	public static void register(Item item, AccessoryRenderer renderer) {
		REGISTRATIONS.put(item, renderer);
	}

	@Nullable
	public static AccessoryRenderer get(ItemStack stack) {
		return REGISTRATIONS.get(stack.getItem());
	}
}
