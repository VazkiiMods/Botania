package vazkii.botania.api.block;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.Nullable;

/**
 * Any block with this component can have phantom ink used on it.
 */
public interface PhantomInkableBlock {
	/**
	 * Called when the block is clicked with phantom ink.
	 *
	 * @param player Null if the block is being inked by a dispenser
	 */
	boolean onPhantomInked(@Nullable Player player, ItemStack stack, Direction side);
}
