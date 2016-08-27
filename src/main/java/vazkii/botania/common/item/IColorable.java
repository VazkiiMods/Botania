package vazkii.botania.common.item;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IColorable {
    @SideOnly(Side.CLIENT)
    int getColorFromItemStack(ItemStack stack, int tintIndex);
}
