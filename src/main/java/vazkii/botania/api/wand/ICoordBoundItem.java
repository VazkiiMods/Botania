package vazkii.botania.api.wand;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;

/**
 * The item equivalent of ITileBound, renders when the
 * item is in hand.
 * @see ITileBound
 */
public interface ICoordBoundItem {

    @SideOnly(Side.CLIENT)
    public ChunkCoordinates getBinding(ItemStack stack);
}
