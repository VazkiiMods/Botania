package vazkii.botania.api.wand;

import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * The item equivalent of ITileBound, renders when the
 * item is in hand.
 * @see ITileBound
 */
public interface ICoordBoundItem {

	@SideOnly(Side.CLIENT)
	public BlockPos getBinding(ItemStack stack);

}
