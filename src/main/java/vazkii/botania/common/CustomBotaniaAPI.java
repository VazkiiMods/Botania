package vazkii.botania.common;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.item.Item;
import vazkii.botania.api.recipe.IFlowerComponent;

public class CustomBotaniaAPI {
    public static Map<Item, IFlowerComponent> extraFlowerComponents = new HashMap<Item, IFlowerComponent>();
}
