package vazkii.botania.common.item;

import net.minecraft.item.ItemStack;

public interface IColorable {
    int getColorFromItemStack(ItemStack stack, int tintIndex);
}
